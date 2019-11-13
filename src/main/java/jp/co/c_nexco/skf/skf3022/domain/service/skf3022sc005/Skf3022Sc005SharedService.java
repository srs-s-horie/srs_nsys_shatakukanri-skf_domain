/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetSameAppNoCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetSameAppNoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

/**
 * Skf3022Sc005SharedService 提示データ一覧共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc005SharedService {

	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private Skf3022Sc005GetTeijiDataInfoExpRepository skf3022Sc005GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf3022Sc005GetSameAppNoCountExpRepository skf3022Sc005GetSameAppNoCountExpRepository;
	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	private static final String STRING_K = "K";
	//提示区分：0(督促対象外)
	private static final String TEIJI_KBN_0 ="0";
	//提示区分：1（社宅提示データ本人確認督促の対象）
	private static final String TEIJI_KBN_1 ="1";
	//提示区分：2（備品提示データ本人確認督促の対象）
	private static final String TEIJI_KBN_2 ="2";
	//提示区分：3（備品搬入・搬出督促の対象）
	private static final String TEIJI_KBN_3 ="3";

	/**
	 * ドロップダウンリストに設定するリストを取得する。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param nyutaikyoKbn 
	 * @param nyutaikyoKbnList  「入退居区分」ドロップダウンリスト
	 * @param stJyokyo
	 * @param stJyokyoList 「社宅提示状況」ドロップダウンリスト
	 * @param stKakunin
	 * @param stKakuninList 「社宅提示確認督促」ドロップダウンリスト
	 * @param bhJyokyo
	 * @param bhJyokyoList  「備品提示状況」ドロップダウンリスト
	 * @param bhKakunin
	 * @param bhKakuninList 「備品提示確認督促」ドロップダウンリスト
	 * @param moveInOut
	 * @param moveInOutList  「備品搬入搬出督促」ドロップダウンリスト
	 */
	public void getDropDownList(String nyutaikyoKbn, List<Map<String, Object>> nyutaikyoKbnList,
								 String stJyokyo, List<Map<String, Object>> stJyokyoList,
								 String stKakunin , List<Map<String, Object>> stKakuninList,
								 String bhJyokyo , List<Map<String, Object>> bhJyokyoList,
								 String bhKakunin , List<Map<String, Object>> bhKakuninList,
								 String moveInOut , List<Map<String, Object>> moveInOutList) {

		boolean isFirstRowEmpty = true;

//		' 「入退居区分」ドロップダウンリストの設定
		nyutaikyoKbnList.clear();
		nyutaikyoKbnList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_NYUTAIKYO_KBN, nyutaikyoKbn,
				isFirstRowEmpty));

//        ' 「社宅提示状況」ドロップダウンリストの設定
		stJyokyoList.clear();
		stJyokyoList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJI_STATUS_KBN, stJyokyo,
				isFirstRowEmpty));

//        ' 「社宅提示確認督促」ドロップダウンリストの設定
		stKakuninList.clear();
		stKakuninList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJIURGE_KBN, stKakunin,
				isFirstRowEmpty));

//        ' 「備品提示状況」ドロップダウンリストの設定
		bhJyokyoList.clear();
		bhJyokyoList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN, bhJyokyo,
				isFirstRowEmpty));

//        ' 「備品提示確認督促」ドロップダウンリストの設定
		bhKakuninList.clear();
		bhKakuninList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHIN_TEIJI_URGE_KBN, bhKakunin,
				isFirstRowEmpty));

//        ' 「備品搬入搬出督促」ドロップダウンリストの設定
		moveInOutList.clear();
		moveInOutList.addAll(ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_BIHIN_INOUT_URGE_KBN, moveInOut,
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
	public int getListTableData(Skf3022Sc005GetTeijiDataInfoExpParameter param, List<Map<String, Object>> listTableData) {

		LogUtils.debugByMsg("リストテーブルデータ取得処理開始");

		// リストテーブルに格納するデータを取得する
		int resultCount = 0;
		List<Skf3022Sc005GetTeijiDataInfoExp> resultListTableData = new ArrayList<Skf3022Sc005GetTeijiDataInfoExp>();
		
		resultListTableData = skf3022Sc005GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);

		// 取得レコード数を設定
		resultCount = resultListTableData.size();
		LogUtils.debugByMsg("取得件数:"+resultCount);
		
		// 取得データレコード数判定
		if (resultCount <= 0) {
			// 取得データレコード数が0件場合、何もせず処理終了
			return resultCount;
		}

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
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf3022Sc005GetTeijiDataInfoExp> originList) {

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
		//社宅提示状況区分
		Map<String, String> genericCodeStJyokyo = new HashMap<String, String>();
		genericCodeStJyokyo = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_TEIJI_STATUS_KBN);
		//備品提示状況区分
		Map<String, String> genericCodeMapBhJyokyo = new HashMap<String, String>();
		genericCodeMapBhJyokyo = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHINTEIJISTATUS_KBN);
		
		for (int i = 0; i < originList.size(); i++) {

			Skf3022Sc005GetTeijiDataInfoExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			//値セット
			String hdnTaiyoKbn = tmpData.getBihinTaiyoKbn();
			//入退居区分
			String nyutaikyoKbn = tmpData.getNyutaikyoKbn();
			tmpMap.put("hdnNyutaikyoKbnCd", nyutaikyoKbn);
			tmpMap.put("colNyutaikyoKbn", genericCodeNyutaikyoKbn.get(nyutaikyoKbn));
			//社員番号
			tmpMap.put("colShainNo", tmpData.getShainNo());
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getShainNoChangeFlg())){
				if("1".equals(tmpData.getShainNoChangeFlg().trim()) && !SkfCheckUtils.isNullOrEmpty(tmpData.getShainNo())){
					String shainNo = tmpData.getShainNo().trim() + CodeConstant.ASTERISK;
					tmpMap.put("colShainNo", shainNo);
				}
			}
			//社員名
			tmpMap.put("colShainName", tmpData.getName());
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getName())) {
				String shainName = tmpData.getName().trim();
				if(shainName.length() > 4){
					tmpMap.put("colShainName", shainName);
				}
			}
			//申請区分
			String shinseiKbn = tmpData.getApplKbn();
			tmpMap.put("colSinseiKbn", genericCodeApplKbn.get(shinseiKbn));
			//入居予定日
			tmpMap.put("colNyukyoDate", skfDateFormatUtils.dateFormatFromString(tmpData.getNyukyoYoteiDate(), "yyyy/MM/dd"));
			//退居予定日
			tmpMap.put("colTaikyoDate", skfDateFormatUtils.dateFormatFromString(tmpData.getTaikyoYoteiDate(), "yyyy/MM/dd"));
			//社宅名
			tmpMap.put("colShatakuName", tmpData.getShatakuName());
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getShatakuName())) {
				String shatakuName = tmpData.getShatakuName().trim();
				if(shatakuName.length() > 6){
					tmpMap.put("colShatakuName", shatakuName);
				}
			}
			//部屋番号
			tmpMap.put("colRoomNo", tmpData.getRoomNo());
			//用途
			tmpMap.put("colYouto", genericCodeAuseKbn.get(tmpData.getAuse()));
			//社宅提示　状況
			String stJyokyo = tmpData.getShatakuTeijiStatus();
			tmpMap.put("hdnStJyokyoCd", stJyokyo);
			tmpMap.put("colStJyokyo", genericCodeStJyokyo.get(stJyokyo));
			//社宅提示　確認督促
			String stKakunin = tmpData.getShatakuTeijiUrgeDate();
			if (!SkfCheckUtils.isNullOrEmpty(stKakunin)) {
				stKakunin = skfDateFormatUtils.dateFormatFromString(stKakunin, "yyyy/MM/dd");
			}else if(SkfCheckUtils.isNullOrEmpty(shinseiKbn)){
				//申請なしの場合
				stKakunin = CodeConstant.HYPHEN;
			}
			tmpMap.put("colStKakunin", stKakunin);
			
			//備品提示状況の設定
			String bhJyokyo = tmpData.getBihinTeijiStatus();
			tmpMap.put("hdnBhJyokyoCd", bhJyokyo);
			String colBhJyokyo = CodeConstant.HYPHEN;
			if (!SkfCheckUtils.isNullOrEmpty(bhJyokyo)) {
				colBhJyokyo = genericCodeMapBhJyokyo.get(bhJyokyo);
			}else if(SkfCheckUtils.isNullOrEmpty(shinseiKbn)){
				//申請なしの場合
				colBhJyokyo = CodeConstant.HYPHEN;
			}else if(CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn)){
				colBhJyokyo = CodeConstant.HYPHEN;
			}else if(CodeConstant.BIHIN_TAIYO_KBN_FUYO.equals(hdnTaiyoKbn)){
				//退居或は同意済の場合
				if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn) || 
						CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(stJyokyo)){
					colBhJyokyo = CodeConstant.HYPHEN;
				}
			}
			tmpMap.put("colBhJyokyo", colBhJyokyo);
			
			//備品提示　確認督促
			String bhKakunin = tmpData.getBihinTeijiUrgeDate();
			if (!SkfCheckUtils.isNullOrEmpty(bhKakunin)) {
				bhKakunin = skfDateFormatUtils.dateFormatFromString(bhKakunin, "yyyy/MM/dd");
			}else if(SkfCheckUtils.isNullOrEmpty(shinseiKbn)){
				//申請なしの場合
				bhKakunin = CodeConstant.HYPHEN;
			}else if(CodeConstant.BIHIN_TAIYO_KBN_FUYO.equals(hdnTaiyoKbn) || 
					CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn) ||
					CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
				bhKakunin = CodeConstant.HYPHEN;
			}
			tmpMap.put("colBhkakunin", bhKakunin);
			
			//搬入搬出督促
			String mobeInOut = tmpData.getBihinInoutUrgeDate();
			if (!SkfCheckUtils.isNullOrEmpty(mobeInOut)) {
				mobeInOut = skfDateFormatUtils.dateFormatFromString(mobeInOut, "yyyy/MM/dd");
			}else if(SkfCheckUtils.isNullOrEmpty(shinseiKbn)){
				//申請なしの場合
				mobeInOut = CodeConstant.HYPHEN;
			}else if(CodeConstant.BIHIN_TAIYO_KBN_FUYO.equals(hdnTaiyoKbn) || 
					CodeConstant.SHINSEI_KBN_PARKING.equals(shinseiKbn) ||
					CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
				mobeInOut = CodeConstant.HYPHEN;
			}
			tmpMap.put("colMoveInOut", mobeInOut);
			tmpMap.put("colDetail", "");
			
			//非表示項目
			tmpMap.put("hdnTeijiNo", tmpData.getTeijiNo());
			tmpMap.put("hdnShoruikanriNo", tmpData.getApplNo());
			tmpMap.put("hdnNyushutsuBi", tmpData.getBihinConfirmDate());
			tmpMap.put("hdnShainName", tmpData.getName());
			tmpMap.put("hdnUpdateDate", tmpData.getUpdateDateTeiji());
			tmpMap.put("hdnUpdateDateNtk", tmpData.getUpdateDateNtk());
			tmpMap.put("hdnShatakuNo", tmpData.getShatakuKanriNo());
			tmpMap.put("hdnRoomKanriNo", tmpData.getShatakuRoomKanriNo());
			tmpMap.put("hdnParkNoOne", tmpData.getParkingKanriNo1());
			tmpMap.put("hdnParkNoTwo", tmpData.getParkingKanriNo2());
			tmpMap.put("hdnUpdateDateShataku", tmpData.getUpdateDateShataku());
			tmpMap.put("hdnUpdateDateParkOne", tmpData.getUpdateDateParkingone());
			tmpMap.put("hdnUpdateDateParkTwo", tmpData.getUpdateDateParkingtwo());
			tmpMap.put("hdnShainChangeFlg", tmpData.getShainNoChangeFlg());
			tmpMap.put("hdnTaiyoKbn", tmpData.getBihinTaiyoKbn());
			tmpMap.put("hdnJssShatakuTeijiDate", tmpData.getJssShatakuTeijiDate());
			tmpMap.put("hdnTeijiKbn", "");
			
			//同じ申請書類管理番号存在チェック
			if(!SkfCheckUtils.isNullOrEmpty(tmpData.getApplNo())){
				if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
					//退去時
					Skf3022Sc005GetSameAppNoCountExpParameter parameter = new Skf3022Sc005GetSameAppNoCountExpParameter();
					parameter.setAppNo(tmpData.getApplNo());
					parameter.setDelFlg(CodeConstant.STRING_ZERO);
					parameter.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_NYUKYO);
					//申請書類管理番号存在チェック
					int sameCount = skf3022Sc005GetSameAppNoCountExpRepository.getSameAppNoCount(parameter);
					
					if(sameCount > 0){
						tmpMap.put("sameAppNoCount", "一緒に申請されている入居申請がまだ同意済となっていないため、退居申請は提示データを作成できません。");//I_SKF_3074 infomation.skf.i_skf_3074
					}
				}
			}

			//削除確認ダイアログ設定
//			 btnDelete.OnClientClick() = MyBase.GetConfirmScript(HttpUtility.HtmlEncode( _
//                     String.Format(Com_MessageManager.GetScreenMsgInfo( _
//                                   Constant.MessageScreenId.C01023), _
//                                   hdnShainName.Value.Trim)))
			//削除ボタンの設定
	        //社宅提示状況が1(作成中)以外の場合、削除ボタン：使用不可
			if(CodeConstant.PRESENTATION_SITUATION_SAKUSEI_CHU.equals(stJyokyo)){
				tmpMap.put("colDelete", "");
			}
			
			//チェックボックス制御
			//'入退居区分が1(入居)の場合、
			if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyutaikyoKbn)){
				//'正社員番号
				if(!STRING_K.equals(tmpData.getShainNo().trim().substring(0, 1))){
					//'社宅提示状況区分が3(提示中)の場合
					if(CodeConstant.PRESENTATION_SITUATION_TEIJI_CHU.equals(stJyokyo)){
						//'選択チェックボックス
						tmpMap.put("chkSelect", "true");
						//'提示区分(1:社宅本人確認督促)
						tmpMap.put("hdnTeijiKbn", TEIJI_KBN_1);
					}
					else if((CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(stJyokyo) || 
							CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(stJyokyo))
						&& CodeConstant.BIHIN_STATUS_MI_SAKUSEI.equals(bhJyokyo)){
							//'社宅提示状況区分が「4(同意済)、または、5(承認)」、且つ、備品提示状況区分が0(未作成)の場合
							//'選択チェックボックス
							tmpMap.put("chkSelect", "true");
							//'提示区分(2：備品本人確認督促)
							tmpMap.put("hdnTeijiKbn", TEIJI_KBN_2);
					}
					else if((CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(stJyokyo) || 
							CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(stJyokyo))
						&& CodeConstant.BIHIN_STATUS_HANNYU_MACHI.equals(bhJyokyo) 
						&& SkfCheckUtils.isNullOrEmpty(tmpData.getBihinConfirmDate())){
							//'社宅提示状況区分が「4(同意済)、または、5(承認)」、且つ、備品提示状況区分が5(搬入待ち)、
							//'且つ、搬入搬出日確認日(搬入日)が空白の場合、
							//'選択チェックボックス
							tmpMap.put("chkSelect", "true");
							//'提示区分(3：備品搬入・搬出督促)
							tmpMap.put("hdnTeijiKbn", TEIJI_KBN_3);
					}else{
						//'選択チェックボックス
						tmpMap.put("chkSelect", "false");
						//'提示区分(0：督促対象外)
						tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
					}
				}
				else{
					//'仮社員番号
					//'選択チェックボックス
					tmpMap.put("chkSelect", "false");
					//'提示区分(0：督促対象外)
					tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
				}
			}
			else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyutaikyoKbn)){
				//'入退居区分が2(退居)の場合、
				//'正社員番号
				if(!STRING_K.equals(tmpData.getShainNo().trim().substring(0, 1))){
					if((CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(stJyokyo) || 
							CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(stJyokyo)) 
						&& CodeConstant.BIHIN_STATUS_TEIJI_CHU.equals(bhJyokyo)){
							//'社宅提示状況区分が「4(同意済)、または、5(承認)」、且つ、備品提示状況区分が3(提示中)の場合
							//'選択チェックボックス
							tmpMap.put("chkSelect", "true");
							//'提示区分(2：備品本人確認督促)
							tmpMap.put("hdnTeijiKbn", TEIJI_KBN_2);
						
					}
					else if((CodeConstant.PRESENTATION_SITUATION_DOI_SUMI.equals(stJyokyo) || 
							CodeConstant.PRESENTATION_SITUATION_SHONIN.equals(stJyokyo)) 
						&& CodeConstant.BIHIN_STATUS_HANSHUTSU_MACHI.equals(bhJyokyo) 
						&& SkfCheckUtils.isNullOrEmpty(tmpData.getBihinConfirmDate())){
							//'社宅提示状況区分が「4(同意済)、または、5(承認)」、且つ、備品提示状況区分が7(搬出待ち)、
							//'且つ、搬入搬出日確認日(搬出日)が空白の場合、
							//'選択チェックボックス
							tmpMap.put("chkSelect", "true");
							//'提示区分(3：備品搬入・搬出督促)
							tmpMap.put("hdnTeijiKbn", TEIJI_KBN_3);
						
					}
					else{
						//'選択チェックボックス
						tmpMap.put("chkSelect", "false");
						//'提示区分(0：督促対象外)
						tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
					}
				}
				else{
					//'仮社員番号
					//'選択チェックボックス
					tmpMap.put("chkSelect", "false");
					//'提示区分(0：督促対象外)
					tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
				}
			}
			else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyutaikyoKbn)){
				//'選択チェックボックス
				tmpMap.put("chkSelect", "false");
				//'提示区分(0：督促対象外)
				tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
			}else{
				//'上記以外の場合、
				//'選択チェックボックス
				tmpMap.put("chkSelect", "false");
				//'提示区分(0：督促対象外)
				tmpMap.put("hdnTeijiKbn", TEIJI_KBN_0);
			}

			setViewList.add(tmpMap);
		}

		return setViewList;
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
