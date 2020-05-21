/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001SendMailDto;
import jp.co.intra_mart.foundation.BaseUrl;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3021Sc001SendMailService 入退居予定一覧督促メール送信処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3021Sc001SendMailService extends SkfServiceAbstract<Skf3021Sc001SendMailDto> {

	@Autowired
	private Skf3021Sc001SharedService skf3021Sc001SharedService;
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
	//提示区分
	public static final String TEIJI_KBN_SHATAKU = "1";
	public static final String TEIJI_KBN_BIHIN = "2";
	public static final String TEIJI_KBN_INOUT = "3";
	//入居申請督促完了通知
	public static final String NYUKYO_SHINSEI_TOKUSOKU_TSUCHI = "21";
	//退居申請督促完了通知
	public static final String TAIKYO_SHINSEI_TOKUSOKU_TSUCHI = "22";
	//希望等調書（メール件名置換え前文字列）
	public static final String TYOUSHO = "希望等調書";
	//社宅（自動車保管場所）退居届（メール件名置換え前文字列）
	public static final String TAIKYO_TODOKE = "社宅（自動車保管場所）退居届";
	//入居
	public static final String NYUKYO = "入居";
	//退居
	public static final String TAIKYO = "退居";
	//ボタン押下区分：初期化
	private static final int BTNFLG_INIT = 0;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param sendDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3021Sc001SendMailDto index(Skf3021Sc001SendMailDto sendDto) throws Exception {

		sendDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("督促", CodeConstant.C001, sendDto.getPageId());
		
		//入退居区分リスト
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請状況リスト
		List<Map<String, Object>> nyuTaikyoShinseiJokyoList = new ArrayList<Map<String, Object>>();
		//特殊事情リスト
		List<Map<String, Object>> tokushuJijoList = new ArrayList<Map<String, Object>>();
		//提示データ作成区分リスト(提示対象)
		List<Map<String, Object>> teijiDetaSakuseiKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請督促リスト
		List<Map<String, Object>> nyuTaikyoShinseiTokusokuList = new ArrayList<Map<String, Object>>();
		
		String message = CodeConstant.DOUBLE_QUOTATION;
		// '送信件数
		int mailCnt = 0;
		//'送信成功件数
		int mailSuccessCnt = 0;
		//'送信失敗件数
		int mailFailCnt = 0;
		//メインメニューURL
		String urlBase = "/skf/Skf1010Sc001/init?menuFlg=1";
		//日付形式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		
		//システム日時の取得
		String sysDateTime = dateFormat.format(skfBaseBusinessLogicUtils.getSystemDateTime());

		List<Map<String,Object>> mailList = createMailList(sendDto.getMailListData());
		for(Map<String,Object> map : mailList){
			String mailKbn = CodeConstant.DOUBLE_QUOTATION;
			// メール送信
			Map<String, String> replaceMap = new HashMap<String, String>();
			
			//社員番号
			String shainNo = map.get("shainNo").toString().replace(CodeConstant.ASTERISK, "");
			//入退居区分
			String nyutaikyoKbn = map.get("nyutaikyoKbn").toString();
			//メールアドレスを取得
			String mailAddress = getSendMailAddressByShainNo(CodeConstant.C001,shainNo);
			
			//メール情報を取得
			if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
				//入居メール情報
				mailKbn = NYUKYO_SHINSEI_TOKUSOKU_TSUCHI;
				//メール件名置き換え($shinseishorui$－未申請の確認)
				//入居メール件名
				replaceMap.put("【shinseishorui】", NYUKYO + TYOUSHO);
				//メール本文置換え
				//入居予定日⇒メール本文になし
				//String nyukyoYoteibi = map.get("nyukyoDate").toString().replace("/", "");
				//replaceMap.put("【idoudate】", nyukyoYoteibi.substring(0, 4) + "年" + nyukyoYoteibi.substring(4, 6) + "月" + nyukyoYoteibi.substring(6, 8) + "日");
				
			}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
				//退居メール情報
				mailKbn = TAIKYO_SHINSEI_TOKUSOKU_TSUCHI;
				//メール件名置き換え($shinseishorui$－未申請の確認)
				//退居メール件名
				replaceMap.put("【shinseishorui】", TAIKYO_TODOKE);
				//メール本文置換え
				//退居予定日⇒メール本文になし
				//String nyukyoYoteibi = map.get("taikyoDate").toString().replace("/", "");
				//replaceMap.put("【idoudate】", nyukyoYoteibi.substring(0, 4) + "年" + nyukyoYoteibi.substring(4, 6) + "月" + nyukyoYoteibi.substring(6, 8) + "日");
				
			}
			
			//メール本文置換え
			replaceMap.put("【shainname】", map.get("shainName").toString());
			
			// 短縮URL作成
			if (urlBase != null) {
				String url = BaseUrl.get() + "/" + urlBase.replaceFirst("^/", "");
				replaceMap.put("【url】", url);
			}
			
			//メール区分
			String mailCd = "SKF_ML" + mailKbn;
			//送信結果
			boolean ret = false;
			
			if(!SkfCheckUtils.isNullOrEmpty(mailAddress)){
				//メールアドレス無しの場合例外になるので、取得成功時のみ送信処理実行
				replaceMap.put("【to】", mailAddress); // 送信先メールアドレス

				LogUtils.debugByMsg("メール送信:" + mailCd);
				// メール送信
				ret = NfwSendMailUtils.sendMail(mailCd, replaceMap);
			}

//            '送信件数
			mailCnt = mailCnt + 1;
			
			if(!ret){
				LogUtils.debugByMsg("メール送信失敗");
				//'送信失敗件数
				mailFailCnt = mailFailCnt + 1;
				//MessageIdConstant.I_SKF_3080
				message = "社員" + map.get("shainNo").toString() + "に送信失敗しました。";
				ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3080, map.get("shainNo").toString());

//                'メール送信に失敗した場合
				LogUtils.info(Skf3021Sc001SendMailService.class, message);

			}else{
				LogUtils.debugByMsg("メール送信成功");
				//'送信成功件数
				mailSuccessCnt = mailSuccessCnt + 1;
				//MessageIdConstant.I_SKF_3081
				message = "社員" + map.get("shainNo").toString() + "に送信成功しました。";
				ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3081, map.get("shainNo").toString());
				
				//成功もwarnログで出してる・・・
				LogUtils.debug(Skf3021Sc001SendMailService.class, message);
				
				//入退居予定データ更新
				int updateRes = updateNyutaikyoYoteiInfoOfUrgeDate(shainNo , nyutaikyoKbn, sysDateTime, map.get("updateDateNtkyo").toString());
				if(updateRes <= 0){
					//MessageIdConstant.E_SKF_1075"更新時にエラーが発生しました。ヘルプデスクへ連絡してください。";
					ServiceHelper.addErrorResultMessage(sendDto, null,  MessageIdConstant.E_SKF_1075);
					throwBusinessExceptionIfErrors(sendDto.getResultMessages());
				}

			}
		}

//        '送信失敗件数i_skf_3082
		message = "メール送信失敗件数  ：" + mailFailCnt + "件 ";
		LogUtils.info(Skf3021Sc001SendMailService.class, message);
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3082, mailFailCnt);

//        '送信成功件数infomation.skf.i_skf_3083  ：{0}件
		message = "メール送信成功件数  ：" + mailSuccessCnt + "件 ";
		LogUtils.info(Skf3021Sc001SendMailService.class, message);
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3083, mailSuccessCnt);

//        '送信件数infomation.skf.i_skf_3084=メール送信件数        ：{0}件
		message = "メール送信件数        ：" + mailCnt + "件 ";
		LogUtils.info(Skf3021Sc001SendMailService.class, message);
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3084, mailCnt);
		
		skf3021Sc001SharedService.getDropDownList(sendDto.getNyutaikyoKbn(), nyutaikyoKbnList,
				sendDto.getTeijiDetaSakuseiKbn(), teijiDetaSakuseiKbnList,
				sendDto.getNyuTaikyoShinseiJokyo() , nyuTaikyoShinseiJokyoList,
				sendDto.getNyuTaikyoShinseiTokusoku() , nyuTaikyoShinseiTokusokuList,
				sendDto.getTokushuJijo() , tokushuJijoList);
		//リスト情報
		sendDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		sendDto.setTeijiDetaSakuseiKbnList(teijiDetaSakuseiKbnList);
		sendDto.setNyuTaikyoShinseiJokyoList(nyuTaikyoShinseiJokyoList);
		sendDto.setNyuTaikyoShinseiTokusokuList(nyuTaikyoShinseiTokusokuList);
		sendDto.setTokushuJijoList(tokushuJijoList);
		
		//再検索
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		//入退居予定を取得（全件）
		skf3021Sc001SharedService.setGrvNyuTaikyoYoteiIchiran(BTNFLG_INIT, sendDto, listTableData);
		sendDto.setListTableData(listTableData);


		return sendDto;
	}
	
	/**
	 * 送信情報文字列をListに変換
	 * @param mailListData
	 * @return
	 */
	private List<Map<String,Object>> createMailList(String mailListData){
		//返却リスト初期化
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//備品情報リスト（表示分）
		//画面からは文字列で取得するので、まず行ごとに分割
		String[] infoList = mailListData.split(";");
		for(String info : infoList){
			LogUtils.debugByMsg("送信情報文字列:"+info);
			//行データを項目ごとに分割
			String[] mail = info.split(",");
			if(mail.length >= 6){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", mail[0]);//社員番号
				forListMap.put("shainName", mail[1]);//社員名
				forListMap.put("nyutaikyoKbn", mail[2]);//入退居区分
				forListMap.put("nyukyoDate", mail[3]);//入居予定日
				forListMap.put("taikyoDate", mail[4]);//退居予定日
				forListMap.put("updateDateNtkyo", mail[5]);//入退居予定更新日時
				
				
				resultList.add(forListMap);
			}
		}
		
		return resultList;
	}
	
	/**
	 * 通知メール用の社員情報を取得します
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @return
	 */
	private String getSendMailAddressByShainNo(String companyCd, String shainNo) {
		String mailAddress = "";
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(companyCd);
		key.setShainNo(shainNo);
		Skf1010MShain mShainData = skf1010MShainRepository.selectByPrimaryKey(key);
		if (mShainData != null) {
			mailAddress = mShainData.getMailAddress();
		}
		LogUtils.debugByMsg("mailAddress:" + mailAddress);
		return mailAddress;
	}
	
	
	/**
	 * 入退居予定データ更新
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param sysDateTime
	 * @param hdnUpdateDate
	 * @return
	 */
	@Transactional
	private int updateNyutaikyoYoteiInfoOfUrgeDate(String shainNo, String nyutaikyoKbn, String sysDateTime, String hdnUpdateDate)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		//引数がない場合
		if(SkfCheckUtils.isNullOrEmpty(shainNo) || SkfCheckUtils.isNullOrEmpty(nyutaikyoKbn)){
			return 0;
		}
		Skf3021TNyutaikyoYoteiData updateData = new Skf3021TNyutaikyoYoteiData();
		//入退居申請督促日
		String urgeDate = sysDateTime.replace("/", "");
		updateData.setNyutaikyoApplUrgeDate(urgeDate);
		//キー
		updateData.setShainNo(shainNo);
		updateData.setNyutaikyoKbn(nyutaikyoKbn);
		
		if(!SkfCheckUtils.isNullOrEmpty(hdnUpdateDate)){
			try{
				Date mapDate = dateFormat.parse(hdnUpdateDate);	
				LogUtils.debugByMsg("UpdateDate：" + mapDate);
				updateData.setLastUpdateDate(mapDate);
			}	
			catch(ParseException ex){
				LogUtils.infoByMsg("updateNyutaikyoYoteiInfoOfUrgeDate, 入退居予定データ-更新日時変換NG :" + hdnUpdateDate);
				return -1;
			}
		}
		
		int retCount = skf3021TNyutaikyoYoteiDataRepository.updateByPrimaryKeySelective(updateData);
		
		return retCount;
	}
	
}
