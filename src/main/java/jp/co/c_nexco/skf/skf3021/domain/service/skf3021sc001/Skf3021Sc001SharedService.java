/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyutaikyoYoteiInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021Sc001common.Skf3021Sc001CommonDto;

/**
 * Skf3022Sc005SharedService 入退居予定一覧共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3021Sc001SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3021Sc001GetNyutaikyoYoteiInfoExpRepository skf3021Sc001GetNyutaikyoYoteiInfoExpRepository;
	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	//ボタン押下区分：初期化
	//private static final int BTNFLG_INIT = 0;
	//ボタン押下区分：検索ボタン押下
	private static final int BTNFLG_KENSAKU = 1;
	//特殊事情有
	private static final String TOKUSHU_JIJO_ARI = "○";
	//仮社員
	private static final String STRING_K = "K";
	
	// システム連携フラグ(0固定でよいと思うが一応フラグのまま)
	@Value("${skf.common.jss_link_flg}")
	private String jssLinkFlg;
	private static final String RENKEI_ARI = "0";
	private static final String RENKEI_NASHI = "1";
	
	// 未作成
	private static final String TEIJI_CREATE_KBN_MI_SAKUSEI = "0";
	// 作成済
	private static final String TEIJI_CREATE_KBN_SAKUSEI_SUMI = "1";

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param nyutaikyoKbn 
	 * @param nyutaikyoKbnList  「入退居区分」ドロップダウンリスト
	 * @param stJyokyo
	 * @param stJyokyoList 「社宅提示状況」ドロップダウンリスト
	 * @param stKakunin
	 * @param stKakuninList 「入退居申請状況区分」ドロップダウンリスト
	 * @param bhJyokyo
	 * @param bhJyokyoList  「入退居申請督促区分」ドロップダウンリスト
	 * @param bhKakunin
	 * @param bhKakuninList 「特殊事情区分」ドロップダウンリスト
	 */
	public void getDropDownList(String nyutaikyoKbn, List<Map<String, Object>> nyutaikyoKbnList,
								 String teijiDetaSakuseiKbn, List<Map<String, Object>> teijiDetaSakuseiKbnList,
								 String nyuTaikyoShinseiJokyo , List<Map<String, Object>> nyuTaikyoShinseiJokyoList,
								 String nyuTaikyoShinseiTokusoku , List<Map<String, Object>> nyuTaikyoShinseiTokusokuList,
								 String tokushuJijo , List<Map<String, Object>> tokushuJijoList) {

		boolean isFirstRowEmpty = true;

//		' 「入退居区分」ドロップダウンリストの設定
		nyutaikyoKbnList.clear();
		nyutaikyoKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN, nyutaikyoKbn,
				isFirstRowEmpty));

//      ' 「提示データ作成区分」ドロップダウンリストの設定
		teijiDetaSakuseiKbnList.clear();
		teijiDetaSakuseiKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TEIJICREATE_KBN, teijiDetaSakuseiKbn,
				isFirstRowEmpty));
		
//        ' 「入退居申請状況区分(検索用)」ドロップダウンリストの設定
		nyuTaikyoShinseiJokyoList.clear();
		nyuTaikyoShinseiJokyoList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_APPL_STATUS_SEARCH_KBN, nyuTaikyoShinseiJokyo,
				isFirstRowEmpty));

//        ' 「入退居申請督促区分」ドロップダウンリストの設定
		nyuTaikyoShinseiTokusokuList.clear();
		nyuTaikyoShinseiTokusokuList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYOAPPLURGE_KBN, nyuTaikyoShinseiTokusoku,
				isFirstRowEmpty));

//        ' 「特殊事情区分」ドロップダウンリストの設定
		tokushuJijoList.clear();
		tokushuJijoList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TOKUSHU_JIJO_KBN, tokushuJijo,
				isFirstRowEmpty));

	}

	/**
	 * リストテーブルを取得する。 <br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuKanriNo 社宅管理番号
	 * @param listTableData リストテーブル用データ
	 * @param hdnListData 情報保持用
	 * @return 取得データのレコードカウント
	 */
	public int setGrvNyuTaikyoYoteiIchiran(int bthFlg, Skf3021Sc001CommonDto dto, List<Map<String, Object>> listTableData) {

		LogUtils.debugByMsg("入退居予定データ取得処理開始");

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3021Sc001GetNyutaikyoYoteiInfoExp> resultListTableData = new ArrayList<Skf3021Sc001GetNyutaikyoYoteiInfoExp>();
		
		Skf3021Sc001GetNyutaikyoYoteiInfoExpParameter param = new Skf3021Sc001GetNyutaikyoYoteiInfoExpParameter();
		//検索パラメータ設定
		param.setShainNo(dto.getShainNo());
		param.setShainName(dto.getShainName());
		//param.setShinseiKubun("");//空固定
		param.setNyutaikyoKbn(dto.getNyutaikyoKbn());
		param.setTeijiDetaSakuseiKubun(dto.getTeijiDetaSakuseiKbn());
		param.setNyuTaikyoShinseiJokyo(dto.getNyuTaikyoShinseiJokyo());
		param.setNyuTaikyoShinseiTokusoku(dto.getNyuTaikyoShinseiTokusoku());
		param.setTokushuJijo(dto.getTokushuJijo());
		param.setJssLinkFlg(dto.getJssLinkFlg());
		
		//表示件数を取得
		int listCount = skf3021Sc001GetNyutaikyoYoteiInfoExpRepository.getNyutaikyoYoteiCount(param);
		LogUtils.debugByMsg("検索件数:" + listCount);
		if(listCount==0 && bthFlg == BTNFLG_KENSAKU){
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.W_SKF_1007);
			return 0;
		}
		
		//入退居予定の一覧情報取得
		resultListTableData = skf3021Sc001GetNyutaikyoYoteiInfoExpRepository.getNyutaikyoYoteiInfo(param);
		
		// 取得レコード数を設定
		resultCount = resultListTableData.size();
		LogUtils.debugByMsg("取得件数:" + resultCount);
		
		//検索条件を退避

		// リストテーブルに出力するリストを取得する
		listTableData.clear();
		listTableData.addAll(getListTableDataViewColumn(resultListTableData));

		return resultCount;

	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3021Sc001GetNyutaikyoYoteiInfoExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		
		//入退居区分
		Map<String, String> genericCodeNyutaikyoKbn = new HashMap<String, String>();
		genericCodeNyutaikyoKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN);
		//申請区分
		Map<String, String> genericCodeApplKbn = new HashMap<String, String>();
		genericCodeApplKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHINSEI_KBN);
		//用途区分
		Map<String, String> genericCodeAuseKbn = new HashMap<String, String>();
		genericCodeAuseKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		//入退居申請状況
		Map<String, String> genericCodeApplStatusKbn = new HashMap<String, String>();
		genericCodeApplStatusKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_NYUTAIKYOAPPLSTATUS_KBN);
		//提示データ作成区分
		Map<String, String> genericCodeTeijiCreateKbn = new HashMap<String, String>();
		genericCodeTeijiCreateKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_TEIJICREATE_KBN);
		
		for (int i = 0; i < originList.size(); i++) {

			Skf3021Sc001GetNyutaikyoYoteiInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			//値セット

			//入退居区分
			String nyutaikyoKbn = tmpData.getNyutaikyoKbn();
			tmpMap.put("hdnNyutaikyoKbnCd", nyutaikyoKbn);
			tmpMap.put("colNyutaikyoKbn", genericCodeNyutaikyoKbn.get(nyutaikyoKbn));
			//社員番号
			String colShainNo = tmpData.getShainNo();
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getShainNoChangeFlg())){
				if("1".equals(tmpData.getShainNoChangeFlg().trim()) && !SkfCheckUtils.isNullOrEmpty(tmpData.getShainNo())){
					colShainNo = tmpData.getShainNo().trim() + CodeConstant.ASTERISK;
				}
			}
			tmpMap.put("colShainNo", colShainNo);
			
			tmpMap.put("hdnPreShainNo", tmpData.getPreShainNo());
			//社員名
			tmpMap.put("colShainName", tmpData.getName());
			//申請区分
			String shinseiKbn = tmpData.getApplKbn();
			tmpMap.put("hdnSinseiKbn", shinseiKbn);
			if(SkfCheckUtils.isNullOrEmpty(shinseiKbn)){
				tmpMap.put("colSinseiKbn", setHaifun(colShainNo));
			}else{
				tmpMap.put("colSinseiKbn", genericCodeApplKbn.get(shinseiKbn));
			}
			//入居予定日
			//tmpMap.put("colNyukyoDate", skfDateFormatUtils.dateFormatFromString(tmpData.getNyukyoYoteiDate(), "yyyy/MM/dd"));
			//退居予定日
			//tmpMap.put("colTaikyoDate", skfDateFormatUtils.dateFormatFromString(tmpData.getTaikyoYoteiDate(), "yyyy/MM/dd"));
			//用途
			//tmpMap.put("colYouto", genericCodeAuseKbn.get(tmpData.getAuse()));
			//駐車場希望
			//tmpMap.put("colParkingReqest", tmpData.getParkingRequestSu());
			//入退居申請状況
			String nyutaikyoApplStatusKbn = tmpData.getNyutaikyoApplStatusKbn();
			tmpMap.put("hdnShinseiJyokyo", nyutaikyoApplStatusKbn);
			//メールチェックボックス
			String mailCheck="mailCheck"+ String.valueOf(i+1);
			tmpMap.put("colmail", "<input type=\"checkbox\" name=\""+mailCheck+"\" id=\""+mailCheck+"\">");
			//提示データ作成区分
			String teijiDataKbn = tmpData.getTeijiCreateKbn();
			tmpMap.put("hdnTeijiDataKbn", teijiDataKbn);
			
			//非表示項目
			//提示番号
			tmpMap.put("hdnTeijiNo", tmpData.getTeijiNo());
			//社員番号変更フラグ
			tmpMap.put("hdnShainChangeFlg", tmpData.getShainNoChangeFlg());
			//申請書類管理番号
			tmpMap.put("hdnApplNo", tmpData.getApplNo());
			tmpMap.put("hdnParking1StartDate", tmpData.getParking1StartDate());
			tmpMap.put("hdnParking2StartDate", tmpData.getParking2StartDate());
			tmpMap.put("hdnUpdateDateNtkyo", tmpData.getUpdateDateNtk());
			tmpMap.put("hdnUpdateDateTenninsha", tmpData.getUpdateDateTc());
/* AS 結合1041対応 */
			// 現所属
			tmpMap.put("hdnNowAffiliation", tmpData.getNowAffiliation());
			// 新所属
			tmpMap.put("hdnNewAffiliation", tmpData.getNewAffiliation());
/* AE 結合1041対応 */
			//提示チェックボックスの活性を設定
			boolean chkSlSentaku = false;
			if(RENKEI_ARI.equals(jssLinkFlg)){
				//「0：連携あり」の場合
				if(colShainNo.startsWith(STRING_K)){
					//仮社員番号
					if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
						//入退居区分 → 1：入居
						if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
							//提示データ作成区分 → 0：未作成
							chkSlSentaku = true;
						}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
							//提示データ作成区分 → 1：作成済
							chkSlSentaku = false;
						}else{
							chkSlSentaku = false;
						}
					}else{
						chkSlSentaku = false;
					}
				}else if(colShainNo.endsWith(CodeConstant.ASTERISK)){
					//旧社員番号（末尾に＊）
					if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
						//入退居区分　→　3：変更
						if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
							//提示データ作成区分 → 0：未作成
							chkSlSentaku = true;
						}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
							//提示データ作成区分 → 1：作成済
							chkSlSentaku = false;
						}else{
							chkSlSentaku = false;
						}
					}else{
						chkSlSentaku = false;
					}
				}else{
					//正社員番号
					if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
						//入退居区分　→　1：入居CodeConstant.SHINSEI_KBN_PARKING
						if(CodeConstant.SHINSEI_KBN_SHIATAKU.equals(shinseiKbn) ||
								CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn)){
							//申請区分　→　1：社宅、2：駐車場
							if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
								//提示データ作成区分 → 0：未作成
								chkSlSentaku = true;
							}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
								//提示データ作成区分 → 1：作成済
								chkSlSentaku = false;
							}else{
								chkSlSentaku = false;
							}
						}else{
							chkSlSentaku = false;
						}
					}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
						//入退居区分　→　2：退居
						if(CodeConstant.SHINSEI_KBN_SHIATAKU.equals(shinseiKbn) ||
							CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn)){
							//申請区分　→　1：社宅、2：駐車場
							if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
								//提示データ作成区分 → 0：未作成
								chkSlSentaku = true;
							}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
								//提示データ作成区分 → 1：作成済
								chkSlSentaku = false;
							}else{
								chkSlSentaku = false;
							}
						}else{
							chkSlSentaku = false;
						}
					}else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
						//申請区分　→　空白
						if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
							//提示データ作成区分 → 0：未作成
							chkSlSentaku = true;
						}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
							//提示データ作成区分 → 1：作成済
							chkSlSentaku = false;
						}else{
							chkSlSentaku = false;
						}
					}else{
						chkSlSentaku = false;
					}
				}
			}
			else if(RENKEI_NASHI.equals(jssLinkFlg)){
				//「1：連携なし」の場合
				if(colShainNo.startsWith(STRING_K)){
					//仮社員番号
					if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
						//入退居区分 → 1：入居
						if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
							//提示データ作成区分（未作成：0）
							chkSlSentaku = true;
						}else if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
							//提示データ作成区分 → 1：作成済
							chkSlSentaku = false;
						}
					}else{
						//入退居区分（退居：2／変更：3）
						chkSlSentaku = false;
					}
				}else if(colShainNo.endsWith(CodeConstant.ASTERISK)){
					//旧社員番号
					if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
						//入退居区分（変更：3）
						if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
							//提示データ作成区分（未作成：0）
							chkSlSentaku = true;
						}else{
							chkSlSentaku = false;
						}
					}else{
						//入退居区分（入居：1／退居：2）
						chkSlSentaku = false;
					}
				}else{
					//正社員番号
					if(TEIJI_CREATE_KBN_MI_SAKUSEI.equals(teijiDataKbn)){
						//提示データ作成区分（未作成：0）
						chkSlSentaku = true;
					}else{
						chkSlSentaku = false;
					}
				}
			}
			tmpMap.put("chkSelect", chkSlSentaku);

			//チェック状態を設定する
			if(!SkfCheckUtils.isNullOrEmpty(teijiDataKbn)){
				if(TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
					//提示データ作成区分（作成済：1）⇒チェックする
					tmpMap.put("chkSelectChecked", true);
				}else{
					tmpMap.put("chkSelectChecked", false);
				}
			}

			//メールチェックボックスの活性を設定
			boolean chkSlMail = false;
			if(RENKEI_NASHI.equals(jssLinkFlg)){
				//連携なし場合
				chkSlMail = false;
			}else{
				//連携あり場合
				if(colShainNo.startsWith(STRING_K)){
					//仮社員番号
					chkSlMail = false;
				}else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
					//入退居区分（変更：3）
					chkSlMail = false;
				}else if(!SkfCheckUtils.isNullOrEmpty(nyutaikyoApplStatusKbn)){
					chkSlMail = false;
				}else{
					chkSlMail = true;
				}
				//入居と退居がある社員の場合は、退居のチェックボックスを非活性にしてメールを2通送れない様に制御
				if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
					if(Objects.equals(colShainNo, tmpData.getPreShainNo())){
						chkSlMail = false;
					}
				}
			}
			tmpMap.put("chkMailSelect", chkSlMail);
			
			//退居と変更の時、入居予定日を非表示にする
			String colNyukyoDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getNyukyoYoteiDate()) && 
					CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
				//入居予定日をフォーマット
				colNyukyoDate = skfDateFormatUtils.dateFormatFromString(tmpData.getNyukyoYoteiDate(), "yyyy/MM/dd");
			}else{
				//―を設定
				colNyukyoDate = setHaifun(colShainNo);
			}
			tmpMap.put("colNyukyoDate", setHaifunBySinseiKbn(shinseiKbn, colNyukyoDate));

			String colTaikyoDate = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getTaikyoYoteiDate())){
				//退居予定日をフォーマット
				colTaikyoDate = skfDateFormatUtils.dateFormatFromString(tmpData.getTaikyoYoteiDate(), "yyyy/MM/dd");
			}else{
				//―を設定
				colTaikyoDate = setHaifun(colShainNo);
			}
			tmpMap.put("colTaikyoDate", setHaifunBySinseiKbn(shinseiKbn, colTaikyoDate));
			
			//用途の設定
			String colYouto = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getAuse())){
				colYouto = genericCodeAuseKbn.get(tmpData.getAuse());
			}else{
				colYouto = setHaifun(colShainNo);
			}
			//駐車場申請の時、用途を”―”に設定する
			if(CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn)){
				colYouto = CodeConstant.ZEN_HYPHEN;
			}else{
				colYouto = setHaifunBySinseiKbn(shinseiKbn,colYouto);
			}
			tmpMap.put("colYouto", colYouto);
			
			//駐車場希望の設定
			String colParkingReqest = CodeConstant.DOUBLE_QUOTATION;
			if(tmpData.getParkingRequestSu() != null){
				colParkingReqest = String.valueOf(tmpData.getParkingRequestSu());
			}
			if(SkfCheckUtils.isNullOrEmpty(colParkingReqest)){
				//―を設定
				colParkingReqest = setHaifun(colShainNo);
			}
			tmpMap.put("colParkingReqest", setHaifunBySinseiKbn(shinseiKbn,colParkingReqest));
			
			//入退居申請状況の設定
			String colShinseiJyokyo = CodeConstant.DOUBLE_QUOTATION;;
			if(!SkfCheckUtils.isNullOrEmpty(nyutaikyoApplStatusKbn)){
				colShinseiJyokyo = genericCodeApplStatusKbn.get(nyutaikyoApplStatusKbn);
			}else{
				colShinseiJyokyo = setHaifun(colShainNo);
			}
			tmpMap.put("colShinseiJyokyo", colShinseiJyokyo);
			
			//入退居申請督促日
			String nyutaikyoApplUrgeDate = tmpData.getNyutaikyoApplUrgeDate();
			if (!SkfCheckUtils.isNullOrEmpty(nyutaikyoApplUrgeDate)) {
				nyutaikyoApplUrgeDate = skfDateFormatUtils.dateFormatFromString(nyutaikyoApplUrgeDate, "yyyy/MM/dd");
			}else {
				//申請なしの場合
				nyutaikyoApplUrgeDate = setHaifun(colShainNo);
			}
			tmpMap.put("colNtkyoShinseiTokusoku", nyutaikyoApplUrgeDate);
			
			//特殊事情
			String tokushuJijo = tmpData.getTokushuJijo();
			tmpMap.put("hdnTokushuJijo", tokushuJijo);
			String colTokushuJijo = CodeConstant.DOUBLE_QUOTATION;
			if (!SkfCheckUtils.isNullOrEmpty(tokushuJijo)) {
				colTokushuJijo = TOKUSHU_JIJO_ARI;
			}
			tmpMap.put("colTokushuJijo", colTokushuJijo);
			
			//提示データ作成区分の設定
			String colTeijiDataKbn = CodeConstant.DOUBLE_QUOTATION;
			if(!SkfCheckUtils.isNullOrEmpty(teijiDataKbn)){
				colTeijiDataKbn = genericCodeTeijiCreateKbn.get(teijiDataKbn);
			}else{
				colTeijiDataKbn = setHaifun(colShainNo);
			}
			tmpMap.put("colTeijiDataKbn", setHaifunBySinseiKbn(shinseiKbn,colTeijiDataKbn));
			
			if(CodeConstant.ZEN_HYPHEN.equals(colShinseiJyokyo) || 
					SkfCheckUtils.isNullOrEmpty(colShinseiJyokyo)){
				//「申請内容ボタン」を非表示
			}else{
				tmpMap.put("colDetail", "");
			}
			
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getApplNo()) ||
					TEIJI_CREATE_KBN_SAKUSEI_SUMI.equals(teijiDataKbn)){
				//「削除ボタン」を非活性
			}else{
				tmpMap.put("colDelete", "");
				//削除確認ダイアログ設定
				//infomation.skf.i_skf_3020={0}予定データ（{1}）を削除します。よろしいですか？
				String delMessage = String.format("%s予定データ（%s）を削除します。よろしいですか？",
						genericCodeNyutaikyoKbn.get(nyutaikyoKbn),tmpData.getName());
				tmpMap.put("hdnDeleteText", delMessage);
			}
			
			

			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	/**
	 *  ―を設定する
	 * @param shainNo
	 */
	private String setHaifun(String shainNo){
		//連携あり場合、
		if(RENKEI_ARI.equals(jssLinkFlg)){
			if(STRING_K.equals(shainNo.trim().substring(0, 1)) ||
					shainNo.endsWith(CodeConstant.ASTERISK)){
				return CodeConstant.ZEN_HYPHEN;
			}else{
				return CodeConstant.DOUBLE_QUOTATION;
			}
		}else{
			return CodeConstant.ZEN_HYPHEN;
		}
	}
	
	/**
	 * 自宅、自借、その他の場合、‐を設定する
	 * @param sinseiKbn
	 * @param value
	 * @return
	 */
	private String setHaifunBySinseiKbn(String sinseiKbn,String value){
		//自宅、自借、その他場合、
		if(CodeConstant.SHINSEI_KBN_JITAKU.equals(sinseiKbn) ||
			CodeConstant.SHINSEI_KBN_JIKO_KARIAGE.equals(sinseiKbn)	||
			CodeConstant.SHINSEI_KBN_OTHER.equals(sinseiKbn)){
			return CodeConstant.ZEN_HYPHEN;
		}else{
			return value;
		}
	}
	
	
	/**
	 * パラメータ文字列のエスケープ処理
	 * @param param
	 * @return
	 */
	public String escapeParameter(String param){
		
		String resultStr=CodeConstant.DOUBLE_QUOTATION;
		
		// 文字エスケープ(% _ ' \)
		if (param != null) {
			// 「\」を「\\」に置換
			resultStr = param.replace("\\", "\\\\");
			// 「%」を「\%」に置換、「_」を「\_」に置換、「'」を「''」に置換
			resultStr = resultStr.replace("%", "\\%").replace("_", "\\_").replace("'", "''");
		}
		
		return resultStr;
	}
	
}
