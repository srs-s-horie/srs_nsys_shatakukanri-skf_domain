/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc005;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuShinseiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuShinseiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHanshutsuExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHanshutsuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinShinseiShoruiKanriBangouExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinShinseiShoruiKanriBangouExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHannyuShinseiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinNameHanshutsuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetBihinShinseiShoruiKanriBangouExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;

import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;

import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005.Skf3022Sc005SendMailDto;
import jp.co.intra_mart.foundation.BaseUrl;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3022Sc005SearchService 提示データ一覧メール送信処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc005SendMailService extends SkfServiceAbstract<Skf3022Sc005SendMailDto> {

	@Autowired
	private Skf3022Sc005SharedService skf3022Sc005SharedService;
	@Autowired
	private Skf3022Sc005GetBihinNameHanshutsuExpRepository skf3022Sc005GetBihinNameHanshutsuExpRepository;
	@Autowired
	private Skf3022Sc005GetBihinNameHannyuExpRepository skf3022Sc005GetBihinNameHannyuExpRepository;
	@Autowired
	private Skf3022Sc005GetBihinShinseiShoruiKanriBangouExpRepository skf3022Sc005GetBihinShinseiShoruiKanriBangouExpRepository;
	@Autowired
	private Skf3022Sc005GetBihinNameHannyuShinseiExpRepository skf3022Sc005GetBihinNameHannyuShinseiExpRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3022.skf3022_sc005.max_row_count}")
	private String listTableMaxRowCount;
	
	//提示区分
	public static final String TEIJI_KBN_SHATAKU = "1";
	public static final String TEIJI_KBN_BIHIN = "2";
	public static final String TEIJI_KBN_INOUT = "3";
	//社宅提示データ本人確認督促通知
	public static final String SHATAKU_TEIJI_DATA_KAKUNIN_TSUCHI = "41";
	//備品申請の確認督促
	public static final String BIHIN_SHINSEI_KAKUNIN_TSUCHI = "42";
	//備品返却の確認督促
	public static final String BIHIN_RETURN_KAKUNIN_TSUCHI = "43";
	//備品搬入・搬出督促通知
	public static final String BIHIN_HANRYU_HANSHUTSU_TSUCHI = "44";
	/**
	 * サービス処理を行う。
	 * 
	 * @param sendDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3022Sc005SendMailDto index(Skf3022Sc005SendMailDto sendDto) throws Exception {

		sendDto.setPageTitleKey(MessageIdConstant.SKF3022_SC005_TITLE);

		//ボタン識別
		String mailTeijiKbn = sendDto.getMailTeijiKbn();
		// 操作ログを出力する
		String btnTitle = "督促";
		if(TEIJI_KBN_SHATAKU.equals(mailTeijiKbn)){
			btnTitle = "社宅提示データ本人確認督促";//MessageIdConstant.SKF3022_SC005_ST_TEIJI_KAKUNIN;
		}else if(TEIJI_KBN_BIHIN.equals(mailTeijiKbn)){
			btnTitle = "備品提示データ本人確認督促";//MessageIdConstant.SKF3022_SC005_BH_TEIJI_KAKUNIN;
		}else if(TEIJI_KBN_INOUT.equals(mailTeijiKbn)){
			btnTitle = "備品搬入・搬出督促";//MessageIdConstant.SKF3022_SC005_BTN_MOVE_INOUT;
		}
		skfOperationLogUtils.setAccessLog(btnTitle, CodeConstant.C001, sendDto.getPageId());
		
		//入退居区分リスト
		String nyutaikyoKbn = sendDto.getNyutaikyoKbn();
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//社宅提示状況リスト
		String stJyokyo = sendDto.getStJyokyo();
		List<Map<String, Object>> stJyokyoList = new ArrayList<Map<String, Object>>();
		//社宅提示確認督促リスト
		String stKakunin = sendDto.getStKakunin();
		List<Map<String, Object>> stKakuninList = new ArrayList<Map<String, Object>>();
		//備品提示状況リスト
		String bhJyokyo = sendDto.getBhJyokyo();
		List<Map<String, Object>> bhJyokyoList = new ArrayList<Map<String, Object>>();
		//備品提示確認督促リスト
		String bhKakunin = sendDto.getBhKakunin();
		List<Map<String, Object>> bhKakuninList = new ArrayList<Map<String, Object>>();
		//備品搬入搬出督促リスト
		String moveInOut = sendDto.getMoveInOut();
		List<Map<String, Object>> moveInOutList = new ArrayList<Map<String, Object>>();
//      'コントロールの設定
		skf3022Sc005SharedService.getDropDownList(nyutaikyoKbn, nyutaikyoKbnList, stJyokyo, stJyokyoList, stKakunin, stKakuninList, 
				bhJyokyo, bhJyokyoList, bhKakunin, bhKakuninList, moveInOut, moveInOutList);
		
		//SendDunningMail
		boolean sendMailFlg = false;
		//StringBuilder retMessage = new StringBuilder();
		String message = CodeConstant.DOUBLE_QUOTATION;
		// '送信件数
		int mailCnt = 0;
		//'送信成功件数
		int mailSuccessCnt = 0;
		//'送信失敗件数
		int mailFailCnt = 0;
		//申請状況一覧画面URL
		String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";

		List<Map<String,Object>> mailList = createMailList(sendDto.getMailListData());
		for(Map<String,Object> map : mailList){
			String mailKbn = CodeConstant.DOUBLE_QUOTATION;
			// メール送信
			Map<String, String> replaceMap = new HashMap<String, String>();
			
			switch(mailTeijiKbn){
				case TEIJI_KBN_SHATAKU:
					mailKbn = SHATAKU_TEIJI_DATA_KAKUNIN_TSUCHI;
				
					//'社宅提示データ本人確認督促
					String jssShatakuTeijiDate = map.get("jssShatakuTeijiDate").toString();
					String teijiDate = jssShatakuTeijiDate.substring(0, 4) + "年" + jssShatakuTeijiDate.substring(4, 6) + "月" + jssShatakuTeijiDate.substring(6, 8) + "日";
					//'メール本文置換え
					replaceMap.put("【shainname】", map.get("shainName").toString());
					replaceMap.put("【teijidate】", teijiDate);

					break;
				case TEIJI_KBN_BIHIN:
					//'備品提示データ本人確認督促
					if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(map.get("nyutaikyoKbn").toString())){
						//'社員情報対象の入退居区分が”入居”
//                      '備品申請の確認督促
						mailKbn = BIHIN_SHINSEI_KAKUNIN_TSUCHI;
						//                      'メール本文置換え
						replaceMap.put("【shainname】", map.get("shainName").toString());
					}
					else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(map.get("nyutaikyoKbn").toString())){
//                      '社員情報対象の入退居区分が”退居”の場合、
//                      '備品返却の確認督促
						mailKbn = BIHIN_RETURN_KAKUNIN_TSUCHI;
//                      '備品名称リストを取得する。
						int teijiNo = Integer.parseInt(map.get("teijiNo").toString());
						
						Skf3022Sc005GetBihinNameHanshutsuExpParameter parameter = new Skf3022Sc005GetBihinNameHanshutsuExpParameter();
						List<Skf3022Sc005GetBihinNameHanshutsuExp> bihinTable = new ArrayList<Skf3022Sc005GetBihinNameHanshutsuExp>();
						parameter.setTeijiNo(teijiNo);
						bihinTable = skf3022Sc005GetBihinNameHanshutsuExpRepository.getBihinNameHanshutsu(parameter);
						String bihinName = CodeConstant.DOUBLE_QUOTATION;
						StringBuilder bihinNameBuf = new StringBuilder();
						
						int index=0;
						for(Skf3022Sc005GetBihinNameHanshutsuExp bihinInfo : bihinTable){
							if(index == 0){
								bihinNameBuf.append(bihinInfo.getBihinName().trim());
							}else{
								String temp = CodeConstant.ZEN_SPACE +CodeConstant.ZEN_SPACE +bihinInfo.getBihinName().trim();
								bihinNameBuf.append(temp);
							}
							//改行
							bihinNameBuf.append(System.getProperty("line.separator"));
							index += 1;
						}
						if(index > 0){
							bihinName = bihinNameBuf.toString();
						}

//                      'メール本文置換え
						replaceMap.put("【shainname】", map.get("shainName").toString());
						replaceMap.put("【bihin_n】", bihinName);

					}
					break;
				case TEIJI_KBN_INOUT:
					//'備品搬入・搬出督促
					mailKbn = BIHIN_HANRYU_HANSHUTSU_TSUCHI;
//                    '備品名称リストを取得する。
					int teijiNo = Integer.parseInt(map.get("teijiNo").toString());

					String bihinName = CodeConstant.DOUBLE_QUOTATION;
					StringBuilder bihinNameBuf = new StringBuilder();
					
					if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(map.get("nyutaikyoKbn").toString())){
						int index=0;
//                      '社員情報対象の入退居区分が”入居”
						String bihinTeijiJokyo = map.get("bihinTeijiJokyo").toString();
						switch(bihinTeijiJokyo){
							case CodeConstant.STATUS_ICHIJIHOZON :
							case CodeConstant.STATUS_MISAKUSEI :
							case CodeConstant.STATUS_SHINSEICHU :
							case CodeConstant.STATUS_SHINSACHU :
							//上の条件はたぶん既存バグ、下が正解。。。
							case CodeConstant.BIHIN_STATUS_MI_SAKUSEI :
							case CodeConstant.BIHIN_STATUS_SAKUSEI_CHU :
							case CodeConstant.BIHIN_STATUS_SAKUSEI_SUMI :
							case CodeConstant.BIHIN_STATUS_TEIJI_CHU :
							case CodeConstant.BIHIN_STATUS_DOI_SUMI :
//                              '備品提示ステータスが“搬入待ち”より前の場合、常に最新の情報を表示する
								Skf3022Sc005GetBihinNameHannyuExpParameter parameter = new Skf3022Sc005GetBihinNameHannyuExpParameter();
								List<Skf3022Sc005GetBihinNameHannyuExp> bihinTable = new ArrayList<Skf3022Sc005GetBihinNameHannyuExp>();
								parameter.setTeijiNo(teijiNo);
								bihinTable = skf3022Sc005GetBihinNameHannyuExpRepository.getBihinNameHannyu(parameter);

								for(Skf3022Sc005GetBihinNameHannyuExp bihinInfo : bihinTable){
									if(index == 0){
										bihinNameBuf.append(bihinInfo.getBihinName().trim());
									}else{
										String temp = CodeConstant.ZEN_SPACE + CodeConstant.ZEN_SPACE + bihinInfo.getBihinName().trim();
										bihinNameBuf.append(temp);
									}
									//改行
									bihinNameBuf.append(System.getProperty("line.separator"));
									index += 1;
								}
								if(index > 0){
									bihinName = bihinNameBuf.toString();
								}
								break;
							default :
//                              '備品申請テーブルからデータを取得し表示する
//                              '備品の申請書類管理番号取得
								Skf3022Sc005GetBihinShinseiShoruiKanriBangouExpParameter bhShinseiParam = new Skf3022Sc005GetBihinShinseiShoruiKanriBangouExpParameter();
								List<Skf3022Sc005GetBihinShinseiShoruiKanriBangouExp> bihinShinseiTable = new ArrayList<Skf3022Sc005GetBihinShinseiShoruiKanriBangouExp>();
								bhShinseiParam.setCompanyCd(CodeConstant.C001);
								bhShinseiParam.setNyukyoApplNo(map.get("shinseiShoruiNo").toString());
								
								bihinShinseiTable = skf3022Sc005GetBihinShinseiShoruiKanriBangouExpRepository.getBihinShinseiShoruiKanriBangou(bhShinseiParam);
//                              '上記で取得した備品の申請書類管理番号から備品名称を取得
								Skf3022Sc005GetBihinNameHannyuShinseiExpParameter bihinNameParam = new Skf3022Sc005GetBihinNameHannyuShinseiExpParameter();
								List<Skf3022Sc005GetBihinNameHannyuShinseiExp> bihinNameTable = new ArrayList<Skf3022Sc005GetBihinNameHannyuShinseiExp>();
								bihinNameParam.setCompanyCd(CodeConstant.C001);
								bihinNameParam.setApplNo(bihinShinseiTable.get(0).getApplNo());
								
								bihinNameTable = skf3022Sc005GetBihinNameHannyuShinseiExpRepository.getBihinNameHannyuShinsei(bihinNameParam);
								index=0;
								for(Skf3022Sc005GetBihinNameHannyuShinseiExp bihinInfo : bihinNameTable){
									if(index == 0){
										bihinNameBuf.append(bihinInfo.getBihinName().trim());
									}else{
										String temp = CodeConstant.ZEN_SPACE + CodeConstant.ZEN_SPACE + bihinInfo.getBihinName().trim();
										bihinNameBuf.append(temp);
									}
									//改行
									bihinNameBuf.append(System.getProperty("line.separator"));
									index += 1;
								}
								if(index > 0){
									bihinName = bihinNameBuf.toString();
								}
								break;
								
						}
//
					}
					else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(map.get("nyutaikyoKbn").toString())){
//                      '備品返却の確認督促
//                      '備品名称リストを取得する。
						Skf3022Sc005GetBihinNameHanshutsuExpParameter parameter = new Skf3022Sc005GetBihinNameHanshutsuExpParameter();
						List<Skf3022Sc005GetBihinNameHanshutsuExp> bihinTable = new ArrayList<Skf3022Sc005GetBihinNameHanshutsuExp>();
						parameter.setTeijiNo(teijiNo);
						bihinTable = skf3022Sc005GetBihinNameHanshutsuExpRepository.getBihinNameHanshutsu(parameter);
						int index=0;
						for(Skf3022Sc005GetBihinNameHanshutsuExp bihinInfo : bihinTable){
							if(index == 0){
								bihinNameBuf.append(bihinInfo.getBihinName().trim());
							}else{
								String temp = CodeConstant.ZEN_SPACE + CodeConstant.ZEN_SPACE + bihinInfo.getBihinName().trim();
								bihinNameBuf.append(temp);
							}
							//改行
							bihinNameBuf.append(System.getProperty("line.separator"));
							index += 1;
						}
						if(index > 0){
							bihinName = bihinNameBuf.toString();
						}
						
					}

//                  'メール本文置換え
					replaceMap.put("【shainname】", map.get("shainName").toString());
					replaceMap.put("【bihin_n】", bihinName);
					break;
			}
			
			// 短縮URL作成
			if (urlBase != null) {
				String url = BaseUrl.get() + "/" + urlBase.replaceFirst("^/", "");
				replaceMap.put("【url】", url);
			}
			
			//メール区分
			String mailCd = "SKF_ML" + mailKbn;
			//送信結果
			boolean ret = false;
//    	       'メールアドレスを取得
			String shainNo = map.get("shainNo").toString().replace(CodeConstant.ASTERISK, "");
			String mailAddress = getSendMailAddressByShainNo(CodeConstant.C001,shainNo);
			
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
				LogUtils.debugByMsg("メール送信失敗:"+ map.get("shainNo").toString());
				//'送信失敗件数
				mailFailCnt = mailFailCnt + 1;
				//MessageIdConstant.I_SKF_3080
//                message = "社員" + map.get("shainNo").toString() + "に送信失敗しました。";
//                retMessage.append(message + System.getProperty("line.separator"));
				ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3080, map.get("shainNo").toString());

//                'メール送信に失敗した場合
				LogUtils.warn(Skf3022Sc005SendMailService.class, message);

			}else{
				LogUtils.debugByMsg("メール送信成功:"+ map.get("shainNo").toString());
				sendMailFlg = true;
				//'送信成功件数
				mailSuccessCnt = mailSuccessCnt + 1;
				//MessageIdConstant.I_SKF_3081
//                message = "社員" + map.get("shainNo").toString() + "に送信成功しました。";
//                retMessage.append(message + System.getProperty("line.separator"));
				ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3081, map.get("shainNo").toString());
				
				Long teijiNo = Long.parseLong(map.get("teijiNo").toString());
				LogUtils.debugByMsg("提示No." + teijiNo);
				int updateRes = updateTeijiData(teijiNo ,map.get("updateDate").toString(), mailTeijiKbn);
				if(updateRes <= 0){
					//MessageIdConstant.E_SKF_1075"更新時にエラーが発生しました。ヘルプデスクへ連絡してください。";
					ServiceHelper.addErrorResultMessage(sendDto, null,  MessageIdConstant.E_SKF_1075);
					throwBusinessExceptionIfErrors(sendDto.getResultMessages());
				}

			}
		}

//        '送信失敗件数i_skf_3082
//        message = "メール送信失敗件数  ：" + mailFailCnt + "件 ";
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3082, mailFailCnt);
		LogUtils.infoByMsg("index, メール送信失敗件数  ：" + Integer.toString(mailFailCnt));

//        '送信成功件数infomation.skf.i_skf_3083  ：{0}件
//        message = "メール送信成功件数  ：" + mailSuccessCnt + "件 ";
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3083, mailSuccessCnt);
		LogUtils.infoByMsg("index, メール送信成功件数  ：" + Integer.toString(mailSuccessCnt));

//        '送信件数infomation.skf.i_skf_3084=メール送信件数        ：{0}件
//        message = "メール送信件数        ：" + mailCnt + "件 ";
		ServiceHelper.addResultMessage(sendDto, MessageIdConstant.I_SKF_3084, mailCnt);
		LogUtils.infoByMsg("index, メール送信件数        ：" + Integer.toString(mailCnt));

		if(sendMailFlg){
			//督促ボタンは使用不可に設定
			sendDto.setBtnShatakuTeijiDisabled("true");
			sendDto.setBtnBihinTeijiDisabled("true");
			sendDto.setBtnBihinInOutDisabled("true");
			
//            '再検索
			// リストデータ取得用
			List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
			
			Skf3022Sc005GetTeijiDataInfoExpParameter param = new Skf3022Sc005GetTeijiDataInfoExpParameter();
			//検索条件の設定
			param.setShainNo(skf3022Sc005SharedService.escapeParameter(sendDto.getShainNo()));
			param.setShainName(skf3022Sc005SharedService.escapeParameter(sendDto.getShainName()));
			param.setShatakuName(skf3022Sc005SharedService.escapeParameter(sendDto.getShatakuName()));
			param.setNyutaikyoKbn(sendDto.getNyutaikyoKbn());
			param.setStJyokyo(sendDto.getStJyokyo());
			param.setStKakunin(sendDto.getStKakunin());
			param.setBhJyokyo(sendDto.getBhJyokyo());
			param.setBhKakunin(sendDto.getBhKakunin());
			param.setMoveInOut(sendDto.getMoveInOut());
			
			// リストテーブルの情報を取得
			skf3022Sc005SharedService.getListTableData(param, listTableData);
			sendDto.setListTableData(listTableData);
			
		}else{
			int stTeijiCnt = Integer.parseInt(sendDto.getHdnStTeijiCnt());
			if(stTeijiCnt > 0){
				sendDto.setBtnShatakuTeijiDisabled("false");
			}
			int bhTeijiCnt = Integer.parseInt(sendDto.getHdnBhTeijiCnt());
			if(bhTeijiCnt > 0){
				sendDto.setBtnBihinTeijiDisabled("false");
			}
			int moveInOutCnt = Integer.parseInt(sendDto.getHdnMoveInOutCnt());
			if(moveInOutCnt > 0){
				sendDto.setBtnBihinInOutDisabled("false");
			}

		}
		
		
		sendDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		sendDto.setStJyokyoList(stJyokyoList);
		sendDto.setStKakuninList(stKakuninList);
		sendDto.setBhJyokyoList(bhJyokyoList);
		sendDto.setBhKakuninList(bhKakuninList);
		sendDto.setMoveInOutList(moveInOutList);


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
			if(mail.length >= 8){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", mail[0]);
				forListMap.put("shainName", mail[1]);
				forListMap.put("teijiNo", mail[2]);
				forListMap.put("nyutaikyoKbn", mail[3]);
				forListMap.put("updateDate", mail[4]);
				forListMap.put("shinseiShoruiNo", mail[5]);
				forListMap.put("bihinTeijiJokyo", mail[6]);
				forListMap.put("jssShatakuTeijiDate", mail[7]);
				
				resultList.add(forListMap);
			}else if(mail.length == 7){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", mail[0]);
				forListMap.put("shainName", mail[1]);
				forListMap.put("teijiNo", mail[2]);
				forListMap.put("nyutaikyoKbn", mail[3]);
				forListMap.put("updateDate", mail[4]);
				forListMap.put("shinseiShoruiNo", mail[5]);
				forListMap.put("bihinTeijiJokyo", mail[6]);
				forListMap.put("jssShatakuTeijiDate", "");
				
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
	 * 提示データ更新
	 * @param teijiNo
	 * @param updateDate
	 * @param teijiKbn
	 * @return
	 */
	@Transactional
	private int updateTeijiData(Long teijiNo, String updateDate,String teijiKbn) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		Skf3022TTeijiData record = new Skf3022TTeijiData();
		
		record.setTeijiNo(teijiNo);
		
		if(!SkfCheckUtils.isNullOrEmpty(updateDate)){
			try{
				Date mapDate = dateFormat.parse(updateDate);	
				LogUtils.debugByMsg("UpdateDate：" + mapDate);
				record.setLastUpdateDate(mapDate);
			}	
			catch(ParseException ex){
				LogUtils.infoByMsg("updateTeijiData, 提示データ-更新日時変換NG :" + updateDate);
				return -1;
			}
		}
		
		//督促日取得
		SimpleDateFormat sysdateFormat = new SimpleDateFormat("yyyyMMdd");
		Date sysDate = skfBaseBusinessLogicUtils.getSystemDateTime();
		String urgeDate =  sysdateFormat.format(sysDate);
		switch(teijiKbn){
		case TEIJI_KBN_SHATAKU:
			record.setShatakuTeijiUrgeDate(urgeDate);
			break;
		case TEIJI_KBN_BIHIN:
			record.setBihinTeijiUrgeDate(urgeDate);
			break;
		case TEIJI_KBN_INOUT:
			record.setBihinInoutUrgeDate(urgeDate);
			break;
		}
		
		int updateRes = skf3022TTeijiDataRepository.updateByPrimaryKeySelective(record);
		
		return updateRes;

	}
}
