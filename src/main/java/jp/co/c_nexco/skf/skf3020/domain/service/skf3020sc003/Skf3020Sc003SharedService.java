/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc003;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc003.Skf3020Sc003GetShatakuNyukyoCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc003.Skf3020Sc003GetTenninshaInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc003.Skf3020Sc003GetShatakuNyukyoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc003.Skf3020Sc003GetTenninshaInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.skf3020.domain.service.common.Skf302010CommonSharedService;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;

/**
 * Skf3020Sc003 転任者調書確認共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc003SharedService {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	@Autowired
	private Skf3020Sc003GetShatakuNyukyoCountExpRepository skf3020Sc003GetShatakuNyukyoCountExpRepository;
	@Autowired
	private Skf3020Sc003GetTenninshaInfoForUpdateExpRepository skf3020Sc003GetTenninshaInfoForUpdateExpRepository;
	@Autowired
	private Skf302010CommonSharedService skf302010CommonSharedService;

	/***********************************
	 * 画面表示のlistTableのカラム設定用
	 ***********************************/
	/** 社員番号 */
	public static final String SHAIN_NO_COL = "col1";
	/** 氏名 */
	public static final String NAME_COL = "col2";
	/** 等級 */
	public static final String TOKYU_COL = "col3";
	/** 年齢 */
	public static final String AGE_COL = "col4";
	/** 新所属 */
	public static final String NEW_AFFILIATION_COL = "col5";
	/** 現所属 */
	public static final String NOW_AFFILIATION_COL = "col6";
	/** 備考 */
	public static final String BIKO_COL = "col7";
	/** エラー */
	public static final String ERR_COL = "col8";

	private static final int SHATAKU_NYUKYO_CNT_JUDG_VAL = 1;

	/**
	 * 現社宅判定フラグを設定する。
	 * 
	 * @param shainNo
	 * @return
	 */
	public String setGenshatakuFlg(String shainNo) {
		// 社員番号をキーにして、社宅入居情報の件数を取得
		Skf3020Sc003GetShatakuNyukyoCountExpParameter param = new Skf3020Sc003GetShatakuNyukyoCountExpParameter();
		param.setShainNo(shainNo);
		// システム処理年月(YYYYMM)を取得
		String sysShoriNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		param.setSysShoriNenGetsu(sysShoriNenGetsu);
		// 社宅入居情報の件数取得
		int shatakuNyukyoCnt = skf3020Sc003GetShatakuNyukyoCountExpRepository.getShatakuNyukyoCount(param);

		if (shatakuNyukyoCnt > SHATAKU_NYUKYO_CNT_JUDG_VAL) {
			return "2";
		} else if (shatakuNyukyoCnt == SHATAKU_NYUKYO_CNT_JUDG_VAL) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * 転任者調書データ更新
	 * 
	 * @param tenninshaInfo
	 * @param targetMap
	 * @return
	 */
	public String updateTenninshaInfo(Skf3020TTenninshaChoshoData tenninshaInfo, Map<String, Object> targetMap,
			String shatakKbn) {
		// 転任者調書データ更新
		Skf3020TTenninshaChoshoData updateData = editUpdateTenninshaInfoData(tenninshaInfo, targetMap, shatakKbn);
		updateData.setLastUpdateDate(tenninshaInfo.getUpdateDate());
		int result = skf3020TTenninshaChoshoDataRepository.updateByPrimaryKeySelective(updateData);

		String outMsg = "";
		switch (result) {
		case Skf302010CommonSharedService.DB_ERR_MINUS_1:
			outMsg = MessageIdConstant.E_SKF_1075;
			return outMsg;
		case Skf302010CommonSharedService.DB_ERR_0:
			outMsg = MessageIdConstant.E_SKF_1009;
			return outMsg;
		default:
			break;
		}

		return outMsg;
	}

	/**
	 * 更新する為の転任者調書データを編集する。
	 * 
	 * @param tenninshaChoshoData
	 * @param targetData
	 * @return 転任者調書データ
	 */
	private Skf3020TTenninshaChoshoData editUpdateTenninshaInfoData(Skf3020TTenninshaChoshoData inData,
			Map<String, Object> targetData, String shatakKbn) {

		Skf3020TTenninshaChoshoData tenninshaChoshoData = new Skf3020TTenninshaChoshoData();

		// 社員番号
		tenninshaChoshoData.setShainNo(inData.getShainNo());
		// 社員氏名
		String name = (String) targetData.get(NAME_COL);
		tenninshaChoshoData.setName(skf302010CommonSharedService.replaceEscapeStr(name));
		// 等級
		String tokyu = (String) targetData.get(TOKYU_COL);
		tenninshaChoshoData.setTokyu(skf302010CommonSharedService.replaceEscapeStr(tokyu));
		// 年齢
		String age = (String) targetData.get(AGE_COL);
		tenninshaChoshoData.setAge(skf302010CommonSharedService.replaceEscapeStr(age).replace("歳", ""));
		// 現所属
		String nowAffiliation = (String) targetData.get(NOW_AFFILIATION_COL);
		nowAffiliation = nowAffiliation.replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNowAffiliation(skf302010CommonSharedService.replaceEscapeStr(nowAffiliation));
		// 新所属
		String newAffiliation = (String) targetData.get(NEW_AFFILIATION_COL);
		newAffiliation = newAffiliation.replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNewAffiliation(skf302010CommonSharedService.replaceEscapeStr(newAffiliation));
		// 備考
		String biko = (String) targetData.get(BIKO_COL);
		tenninshaChoshoData.setBiko(skf302010CommonSharedService.replaceEscapeStr(biko));
//		// 社員番号変更対象区分
//		tenninshaChoshoData.setShainNoHenkoKbn(inData.getShainNoHenkoKbn());
//		// 入居フラグ
//		tenninshaChoshoData.setNyukyoFlg(inData.getNyukyoFlg());
//		// 退居フラグ
//		tenninshaChoshoData.setTaikyoFlg(inData.getTaikyoFlg());
//		// 変更フラグ
//		tenninshaChoshoData.setHenkouFlg(inData.getHenkouFlg());
		// 現社宅区分
		tenninshaChoshoData.setNowShatakuKbn(shatakKbn);
//		// 入退居予定作成区分
//		tenninshaChoshoData.setNyutaikyoYoteiKbn(inData.getNyutaikyoYoteiKbn());
		// 転任者調書取込日
		Date nowTime = skfBaseBusinessLogicUtils.getSystemDateTime();
		tenninshaChoshoData.setDataTakinginDate(new SimpleDateFormat("yyyyMMdd").format(nowTime));

		return tenninshaChoshoData;
	}

	/**
	 * 転任者調書データ登録
	 * 
	 * @param targetMap
	 * @return
	 */
	public String insertTenninshaInfo(Map<String, Object> targetMap, String shatakKbn) {
		// 転任者調書データ登録
		Skf3020TTenninshaChoshoData insertData = createInsertTenninshaInfoData(targetMap, shatakKbn);
		int result = skf3020TTenninshaChoshoDataRepository.insert(insertData);

		String outMsg = "";
		switch (result) {
		case Skf302010CommonSharedService.DB_ERR_MINUS_1:
			outMsg = MessageIdConstant.E_SKF_1073;
			return outMsg;
		case Skf302010CommonSharedService.DB_ERR_0:
			outMsg = MessageIdConstant.E_SKF_1010;
			return outMsg;
		default:
			break;
		}

		return outMsg;
	}

	/**
	 * 登録する為の転任者調書データを作成する。
	 * 
	 * @param targetData
	 * @param kariShainNo
	 * @return
	 */
	private Skf3020TTenninshaChoshoData createInsertTenninshaInfoData(Map<String, Object> targetData , String shatakKbn) {
		Skf3020TTenninshaChoshoData tenninshaChoshoData = new Skf3020TTenninshaChoshoData();

		String shainNo = (String) targetData.get(SHAIN_NO_COL); // 社員番号
		if (shainNo == null || CheckUtils.isEmpty(shainNo)) {
			// 社員番号が空白の場合、仮社員番号を取得する。
			String kariShainNo = skfBaseBusinessLogicUtils.getMaxKariShainNo();
			// 社員番号
			tenninshaChoshoData.setShainNo(kariShainNo);
		}else{
			tenninshaChoshoData.setShainNo(shainNo);
		}
		
		// 社員氏名
		String name = (String) targetData.get(NAME_COL);
		tenninshaChoshoData.setName(skf302010CommonSharedService.replaceEscapeStr(name));
		// 等級
		String tokyu = (String) targetData.get(TOKYU_COL);
		tenninshaChoshoData.setTokyu(skf302010CommonSharedService.replaceEscapeStr(tokyu));
		// 年齢
		String age = (String) targetData.get(AGE_COL);
		tenninshaChoshoData.setAge(skf302010CommonSharedService.replaceEscapeStr(age));
		// 現所属
		String nowAffiliation = (String) targetData.get(NOW_AFFILIATION_COL);
		nowAffiliation = nowAffiliation.replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNowAffiliation(skf302010CommonSharedService.replaceEscapeStr(nowAffiliation));
		// 新所属
		String newAffiliation = (String) targetData.get(NEW_AFFILIATION_COL);
		newAffiliation = newAffiliation.replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNewAffiliation(skf302010CommonSharedService.replaceEscapeStr(newAffiliation));
		// 備考
		String biko = (String) targetData.get(BIKO_COL);
		tenninshaChoshoData.setBiko(skf302010CommonSharedService.replaceEscapeStr(biko));
		// 社員番号変更対象区分
		tenninshaChoshoData.setShainNoHenkoKbn(null);
		// 入居フラグ
		tenninshaChoshoData.setNyukyoFlg(null);
		// 退居フラグ
		tenninshaChoshoData.setTaikyoFlg(null);
		// 変更フラグ
		tenninshaChoshoData.setHenkouFlg(null);
		// 入退居予定作成区分
		tenninshaChoshoData.setNyutaikyoYoteiKbn("0");
		// 現社宅区分
		tenninshaChoshoData.setNowShatakuKbn(shatakKbn);
		// 転任者調書取込日
		Date nowTime = skfBaseBusinessLogicUtils.getSystemDateTime();
		tenninshaChoshoData.setDataTakinginDate(new SimpleDateFormat("yyyyMMdd").format(nowTime));

		return tenninshaChoshoData;
	}

	/**
	 * 転任者調書データの更新日を取得する。
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 更新日
	 */
	public String getTenninshaInfoForUpdate(String shainNo) {
		Skf3020Sc003GetTenninshaInfoForUpdateExpParameter param = new Skf3020Sc003GetTenninshaInfoForUpdateExpParameter();
		param.setShainNo(shainNo);
		String updateData = skf3020Sc003GetTenninshaInfoForUpdateExpRepository.getTenninshaInfoForUpdate(param);

		return updateData;
	}
}
