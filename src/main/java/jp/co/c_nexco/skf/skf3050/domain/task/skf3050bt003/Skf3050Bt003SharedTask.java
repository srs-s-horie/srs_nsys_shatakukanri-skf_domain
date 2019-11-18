/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.task.skf3050bt003;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Bt003.Skf3050Bt003UpdateGetsujiShoriKanriExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Bt003.Skf3050Bt003UpdateGetsujiShoriKanriExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;

/**
 * Skf3050Bt003AsyncCreatePositiveCooperationTask POSITIVE連携データ作成（オンラインバッチ）共通クラス
 *
 * @author NEXCOシステムズ
 */
@Scope("prototype")
@Component
public class Skf3050Bt003SharedTask {

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository;
	@Autowired
	private Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository;
	@Autowired
	private Skf3050Bt003UpdateGetsujiShoriKanriExpRepository skf3050Bt003UpdateGetsujiShoriKanriExpRepository;

	public static final String BATCH_NAME = "POSITIVE連携データ作成";
	public static final int PARAMETER_NUM = 4;

	public static final String SKF3050BT003_BATCH_PRG_ID_KEY = "postvCoopBatchPrgId";
	public static final String SKF3050BT003_COMPANY_CD_KEY = "postvCoopCompanyCd";
	public static final String SKF3050BT003_USER_ID_KEY = "postvCoopUserId";
	public static final String SKF3050BT003_SHORI_NENGETSU_KEY = "postvCoopShoriNengetsu";

	private static final String PARAM_NAME_PRG_ID = "バッチプログラムID";
	private static final String PARAM_NAME_COMPANY_CD = "会社コード";
	private static final String PARAM_NAME_USER_ID = "ユーザID";
	private static final String PARAM_NAME_SHORI_NENGETSU = "処理年月";
	private static final String BATCH_ID_B5003 = "B5003";

	private static final String IF0002 = "IF0002";
	private static final String IF0003 = "IF0003";
	private static final String IF0004 = "IF0004";
	private static final String IF0005 = "IF0005";
	private static final String IF0006 = "IF0006";
	private static final String IF0007 = "IF0007";
	private static final String IF0008 = "IF0008";
	private static final String IF0009 = "IF0009";
	private static final String IF0010 = "IF0010";
	private static final String KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI = "7900010702";
	private static final String KEIRIKANJOU_AZUKARI_SHATAKU_KASHI = "4000112400";
	private static final String KEIRIKANJOU_CHIDAI_YACHIN = "8800040510";
	private static final String KEIRIKANJOU_AZUKARI_KYOUEKIHI = "4000111400";
	private static final String KEIRIKANJOU_AZUKARI_KYOUEKIHI_SHAGAI = "4000112300";

	private static final int START_LINE = 1;
	private static final String KEY_FILE_NAME = "skf3050.skf3050_bt003.file_name";
	private static final String KEY_SHEET_NAME = "skf3050.skf3050_bt003.sheet_name";

	/**
	 * バッチ制御テーブルへ登録する。
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return 結果
	 * @throws ParseException
	 */
	@Transactional
	public int registBatchControl(Map<String, String> parameter) throws ParseException {

		String retParameterName = checkParameter(parameter);
		String programId = BATCH_ID_B5003;
		Date sysDate = getSystemDate();

		if (!NfwStringUtils.isEmpty(retParameterName)) {

			if (!retParameterName.contains(PARAM_NAME_COMPANY_CD)) {
				skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT003_COMPANY_CD_KEY), programId,
						parameter.get(SKF3050BT003_USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

				LogUtils.error(MessageIdConstant.E_SKF_1089, retParameterName);
				return CodeConstant.SYS_NG;

			} else {
				LogUtils.error(MessageIdConstant.E_SKF_1089, PARAM_NAME_COMPANY_CD);
				return CodeConstant.SYS_NG;
			}
		}

		if (!BATCH_ID_B5003.equals(parameter.get(SKF3050BT003_BATCH_PRG_ID_KEY))) {

			skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT003_COMPANY_CD_KEY), programId,
					parameter.get(SKF3050BT003_USER_ID_KEY), SkfCommonConstant.ABNORMAL, sysDate, getSystemDate());

			LogUtils.errorByMsg(
					"バッチプログラムIDが正しくありません。（バッチプログラムID：" + parameter.get(SKF3050BT003_BATCH_PRG_ID_KEY) + "）");
			return CodeConstant.SYS_NG;
		}

		skfBatchBusinessLogicUtils.insertBatchControl(parameter.get(SKF3050BT003_COMPANY_CD_KEY), programId,
				parameter.get(SKF3050BT003_USER_ID_KEY), SkfCommonConstant.PROCESSING, sysDate, null);

		return CodeConstant.SYS_OK;
	}

	/**
	 * POSITIVE連携データをエクセルに出力する。
	 * 
	 * @param parameter
	 *            パラメータ
	 * @throws Exception
	 */
	@Transactional
	public Map<String, Object> createPositiveCooperationData(Map<String, String> parameter) throws Exception {

		String paramShoriNengetsu = parameter.get(SKF3050BT003_SHORI_NENGETSU_KEY);
		String paramCompanyCd = parameter.get(SKF3050BT003_COMPANY_CD_KEY);

		List<RowDataBean> rowDataBeanList = new ArrayList<>();

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI, "", true, false, false, false, IF0002);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_SHATAKU_KASHI_GYOUGAI, "", false, true, false, false, IF0003);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_AZUKARI_SHATAKU_KASHI, "", true, false, false, false, IF0004);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_AZUKARI_SHATAKU_KASHI, "", false, true, false, false, IF0005);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_CHIDAI_YACHIN, "", true, false, false, false, IF0006);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd,
				KEIRIKANJOU_CHIDAI_YACHIN, "", false, true, false, false, IF0007);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd, "",
				KEIRIKANJOU_AZUKARI_KYOUEKIHI, false, false, true, false, IF0008);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd, "",
				KEIRIKANJOU_AZUKARI_KYOUEKIHI_SHAGAI, false, false, true, false, IF0009);

		rowDataBeanList = createExcelFilePositiveRenkeiInfo(rowDataBeanList, paramShoriNengetsu, paramCompanyCd, "", "",
				false, false, false, true, IF0010);

		rowDataBeanList = createExcelFilePositiveRenkeiBihinIF0011Info(rowDataBeanList, paramShoriNengetsu,
				paramCompanyCd);

		Map<String, Object> rtnMap = outputExcel(rowDataBeanList);

		updateGetsujiShoriKanri(parameter.get(SKF3050BT003_USER_ID_KEY), paramShoriNengetsu);

		return rtnMap;
	}

	/**
	 * バッチ制御テーブルを更新
	 * 
	 * @param endFlag
	 *            終了フラグ
	 * @param companyCd
	 *            会社コード
	 * @param programId
	 *            プログラムID
	 * @param searchEndFlag
	 *            検索用終了フラグ
	 * @throws ParseException
	 */
	@Transactional
	public void endProc(String companyCd) throws ParseException {

		Date endDate = getSystemDate();

		skfBatchBusinessLogicUtils.updateBatchControl(endDate, SkfCommonConstant.COMPLETE, companyCd, BATCH_ID_B5003,
				SkfCommonConstant.PROCESSING);
	}

	/**
	 * パラメータ取得可否チェック
	 * 
	 * @param parameter
	 *            パラメータ
	 * @return エラー対象パラメータ名
	 */
	private String checkParameter(Map<String, String> parameter) {

		String retParameterName = "";

		if (isEmpty(parameter.get(SKF3050BT003_BATCH_PRG_ID_KEY))) {
			retParameterName = PARAM_NAME_PRG_ID;
		}

		if (isEmpty(parameter.get(SKF3050BT003_COMPANY_CD_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_COMPANY_CD;
		}

		if (isEmpty(parameter.get(SKF3050BT003_USER_ID_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_USER_ID;
		}

		if (isEmpty(parameter.get(SKF3050BT003_SHORI_NENGETSU_KEY))) {
			retParameterName = CodeConstant.COMMA + PARAM_NAME_SHORI_NENGETSU;
		}

		if (retParameterName.startsWith(CodeConstant.COMMA)) {
			retParameterName = retParameterName.substring(1);
		}

		return retParameterName;
	}

	/**
	 * パラメータが空であるかチェックする。
	 * 
	 * @param inVal
	 *            チェックする値
	 * @return 結果
	 */
	public boolean isEmpty(String inVal) {

		if (NfwStringUtils.isEmpty(inVal)) {
			return true;
		}

		String param = inVal.replaceFirst("^[\\h]+", "").replaceFirst("[\\h]+$", "");

		if (NfwStringUtils.isEmpty(param)) {
			return true;
		}

		return false;
	}

	/**
	 * システムデータを取得する。
	 * 
	 * @return システムデータ
	 */
	public Date getSystemDate() {

		Date systemDate = skfBaseBusinessLogicUtils.getSystemDateTime();

		return systemDate;
	}

	/**
	 * POSITIVE連携データ作成（Excelファイル作成）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param saveFolder
	 *            保存先フォルダ名
	 * @param shatakuKanjouCd
	 *            社宅使用料計上勘定科目コード
	 * @param kyouekiKanjouCd
	 *            共栄費個人負担金計上勘定科目コード
	 * @param shatakuShiyouFlg
	 *            社宅使用料
	 * @param tyushajoShiyouFlg
	 *            駐車場使用料
	 * @param kyouekihiFlg
	 *            共益費（個人負担金）
	 * @param genbutsuSanteiFlg
	 *            現物算定額
	 * @param interfaceId
	 *            インターフェースID
	 * @return 作成件数
	 * @throws Exception
	 */
	private List<RowDataBean> createExcelFilePositiveRenkeiInfo(List<RowDataBean> rowDataBeanList, String shoriNengetsu,
			String kyuyoCompanyCd, String shatakuKanjouCd, String kyouekiKanjouCd, boolean shatakuShiyouFlg,
			boolean tyushajoShiyouFlg, boolean kyouekihiFlg, boolean genbutsuSanteiFlg, String interfaceId)
			throws Exception {

		if (IF0010.equals(interfaceId)) {

			List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> genbutsuDataList = getPositiveGenbutsuSanteiSakuseiSyoriData(
					shoriNengetsu, kyuyoCompanyCd, genbutsuSanteiFlg);

			if (genbutsuDataList.size() > 0) {
				int targetRow = START_LINE;

				if (rowDataBeanList.size() != 0) {
					targetRow = rowDataBeanList.size() + START_LINE;
				}

				for (int i = 0; i < genbutsuDataList.size(); i++) {
					Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp genbutsuShori = genbutsuDataList.get(i);

					targetRow++;

					String genbutuSantei = "";
					if (genbutsuShori.getGenbutuSantei() != null) {
						genbutuSantei = genbutsuShori.getGenbutuSantei().toString();
					}

					RowDataBean rowData = createRowData(targetRow, genbutsuShori.getShainNo(), shoriNengetsu, "", "",
							"", "", "", "", "", "", genbutuSantei, "");
					if (rowData != null) {
						rowDataBeanList.add(rowData);
					}
				}
			}

		} else {
			List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> sakuseiSyoriDataList = getPositiveRenkeiDataSakuseiSyoriData(
					shoriNengetsu, kyuyoCompanyCd, shatakuKanjouCd, kyouekiKanjouCd, shatakuShiyouFlg,
					tyushajoShiyouFlg, kyouekihiFlg, genbutsuSanteiFlg);

			if (sakuseiSyoriDataList.size() > 0) {
				int targetRow = START_LINE;

				if (rowDataBeanList.size() != 0) {
					targetRow = rowDataBeanList.size() + START_LINE;
				}

				for (int i = 0; i < sakuseiSyoriDataList.size(); i++) {

					Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp sakuseiSyoriData = sakuseiSyoriDataList.get(i);

					String shainNo = "";
					if (sakuseiSyoriData.getShainNo() != null) {
						shainNo = sakuseiSyoriData.getShainNo();
					}

					String siyoryoHoyu = "";
					String chusyajyoHoyu = "";
					String siyoryoShagai = "";
					String chusyajyoShagai = "";
					String siyoryoKasiage = "";
					String chusyajyoKasiage = "";
					String kyoekihiHoyu = "";
					String kyoekihiShagai = "";

					switch (interfaceId) {
					case IF0002:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							siyoryoHoyu = sakuseiSyoriData.getRentalTotal().toString();
						}
						break;
					case IF0003:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							chusyajyoHoyu = sakuseiSyoriData.getParkingRentalTotal().toString();
						}
						break;
					case IF0004:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							siyoryoShagai = sakuseiSyoriData.getRentalTotal().toString();
						}
						break;
					case IF0005:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							chusyajyoShagai = sakuseiSyoriData.getParkingRentalTotal().toString();
						}
						break;
					case IF0006:
						if (sakuseiSyoriData.getRentalTotal() != null) {
							siyoryoKasiage = sakuseiSyoriData.getRentalTotal().toString();
						}
						break;
					case IF0007:
						if (sakuseiSyoriData.getParkingRentalTotal() != null) {
							chusyajyoKasiage = sakuseiSyoriData.getParkingRentalTotal().toString();
						}
						break;
					case IF0008:
						if (sakuseiSyoriData.getKyoekihiPersonTotal() != null) {
							kyoekihiHoyu = sakuseiSyoriData.getKyoekihiPersonTotal().toString();
						}
						break;
					case IF0009:
						if (sakuseiSyoriData.getKyoekihiPersonTotal() != null) {
							kyoekihiShagai = sakuseiSyoriData.getKyoekihiPersonTotal().toString();
						}
						break;
					}

					targetRow++;
					RowDataBean rowData = createRowData(targetRow, shainNo, shoriNengetsu, siyoryoHoyu, chusyajyoHoyu,
							siyoryoShagai, chusyajyoShagai, siyoryoKasiage, chusyajyoKasiage, kyoekihiHoyu,
							kyoekihiShagai, "", "");
					if (rowData != null) {
						rowDataBeanList.add(rowData);
					}
				}
			}
		}

		return rowDataBeanList;
	}

	/**
	 * 月別使用料履歴データ取得(現物算定額用)
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param genbutsuSanteiFlg
	 *            現物算定額フラグ
	 * @return 月別使用料履歴データ
	 */
	private List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> getPositiveGenbutsuSanteiSakuseiSyoriData(
			String shoriNengetsu, String kyuyoCompanyCd, boolean genbutsuSanteiFlg) {

		Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter param = new Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpParameter();

		param.setYearMonth(shoriNengetsu);
		param.setPayCompanyCd(kyuyoCompanyCd);

		if (genbutsuSanteiFlg) {
			param.setGenbutuSantei(0);
			param.setGenbutuSanteiFlg("1");
		} else {
			param.setGenbutuSantei(null);
			param.setGenbutuSanteiFlg("0");
		}

		List<Skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExp> outData = skf3050Bt003GetPositiveGenbutsuSanteiSakuseiSyoriDataExpRepository
				.getPositiveGenbutsuSanteiSakuseiSyoriData(param);

		return outData;
	}

	/**
	 * エクセルシートに出力する１列分のデータを作成する。
	 * 
	 * @param targetRow 対象列
	 * @param shaiNo 社員番号
	 * @param shoriNengetsu 処理年月
	 * @param siyoryoHoyu 社宅使用料保有
	 * @param chusyajyoHoyu 駐車場使用料保有
	 * @param siyoryoShagai 社宅使用料社外
	 * @param chusyajyoShagai 駐車場使用料社外
	 * @param siyoryoKasiage 社宅使用料借上
	 * @param chusyajyoKasiage 駐車場使用料借上
	 * @param kyoekihiHoyu 共益費保有・借上
	 * @param kyoekihiShagai 共益費社外
	 * @param genbutugakuShaho 社宅価格現物(社保)
	 * @param bihingenbutuShaho 貸与備品現物(課税・社保)
	 * @return エクセルシートに出力する１列分のデータ
	 */
	private RowDataBean createRowData(int targetRow, String shaiNo, String shoriNengetsu, String siyoryoHoyu,
			String chusyajyoHoyu, String siyoryoShagai, String chusyajyoShagai, String siyoryoKasiage,
			String chusyajyoKasiage, String kyoekihiHoyu, String kyoekihiShagai, String genbutugakuShaho,
			String bihingenbutuShaho) {

		if (NfwStringUtils.isEmpty(shaiNo) && NfwStringUtils.isEmpty(shoriNengetsu)
				&& NfwStringUtils.isEmpty(siyoryoHoyu) && NfwStringUtils.isEmpty(chusyajyoHoyu)
				&& NfwStringUtils.isEmpty(siyoryoShagai) && NfwStringUtils.isEmpty(chusyajyoShagai)
				&& NfwStringUtils.isEmpty(siyoryoKasiage) && NfwStringUtils.isEmpty(chusyajyoKasiage)
				&& NfwStringUtils.isEmpty(kyoekihiHoyu) && NfwStringUtils.isEmpty(kyoekihiShagai)
				&& NfwStringUtils.isEmpty(genbutugakuShaho) && NfwStringUtils.isEmpty(bihingenbutuShaho)) {
			return null;
		}

		RowDataBean rtnRowData = new RowDataBean();

		rtnRowData.addCellDataBean("A" + targetRow, "1");
		rtnRowData.addCellDataBean("B" + targetRow, shaiNo == null ? "" : shaiNo);
		rtnRowData.addCellDataBean("C" + targetRow, "1");
		rtnRowData.addCellDataBean("D" + targetRow, "0");
		rtnRowData.addCellDataBean("E" + targetRow, shoriNengetsu == null ? "" : shoriNengetsu);
		rtnRowData.addCellDataBean("F" + targetRow, "");
		rtnRowData.addCellDataBean("G" + targetRow, siyoryoHoyu == null ? "" : siyoryoHoyu);
		rtnRowData.addCellDataBean("H" + targetRow, chusyajyoHoyu == null ? "" : chusyajyoHoyu);
		rtnRowData.addCellDataBean("I" + targetRow, siyoryoShagai == null ? "" : siyoryoShagai);
		rtnRowData.addCellDataBean("J" + targetRow, chusyajyoShagai == null ? "" : chusyajyoShagai);
		rtnRowData.addCellDataBean("K" + targetRow, siyoryoKasiage == null ? "" : siyoryoKasiage);
		rtnRowData.addCellDataBean("L" + targetRow, chusyajyoKasiage == null ? "" : chusyajyoKasiage);
		rtnRowData.addCellDataBean("M" + targetRow, kyoekihiHoyu == null ? "" : kyoekihiHoyu);
		rtnRowData.addCellDataBean("N" + targetRow, kyoekihiShagai == null ? "" : kyoekihiShagai);
		rtnRowData.addCellDataBean("O" + targetRow, genbutugakuShaho == null ? "" : genbutugakuShaho);
		rtnRowData.addCellDataBean("P" + targetRow, bihingenbutuShaho == null ? "" : bihingenbutuShaho);
		rtnRowData.addCellDataBean("Q" + targetRow, "");
		rtnRowData.addCellDataBean("R" + targetRow, "");
		rtnRowData.addCellDataBean("S" + targetRow, "");
		rtnRowData.addCellDataBean("T" + targetRow, "");

		return rtnRowData;
	}

	/**
	 * エクセルファイルを作成して出力する。
	 * 
	 * @param rowDataBeanList
	 *            列ごとのデータリスト
	 * @param sheetName
	 *            シート名
	 * @throws Exception
	 */
	private Map<String, Object> outputExcel(List<RowDataBean> rowDataBeanList) throws Exception {

		SheetDataBean sheetDataBean = new SheetDataBean();
		String sheetName = PropertyUtils.getValue(Skf3050Bt003SharedTask.KEY_SHEET_NAME);
		sheetDataBean.setSheetName(sheetName);
		sheetDataBean.setRowDataBeanList(rowDataBeanList);

		List<SheetDataBean> sheetDataBeanList = new ArrayList<>();
		sheetDataBeanList.add(sheetDataBean);

		String fileName = sheetName + CodeConstant.UNDER_SCORE + DateTime.now().toString("YYYYMMddHHmmss")
				+ CodeConstant.DOT + CodeConstant.EXTENSION_XLSX;
		WorkBookDataBean wbdb = new WorkBookDataBean(fileName);
		wbdb.setSheetDataBeanList(sheetDataBeanList);

		Map<String, Object> cellparams = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();

		String functionId = PropertyUtils.getValue("skf3050.skf3050_bt003.output_file_function_id");

		SkfFileOutputUtils.fileOutputExcel(wbdb, cellparams, Skf3050Bt003SharedTask.KEY_FILE_NAME, functionId,
				Skf3050Bt003SharedTask.START_LINE, null, resultMap);

		resultMap.put("uploadFileName", fileName);

		// 解放
		sheetDataBean = null;
		sheetDataBeanList = null;
		cellparams = null;
		wbdb = null;

		return resultMap;
	}

	/**
	 * 月別使用料履歴データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param shatakuKanjouCd
	 *            社宅使用料計上勘定科目コード
	 * @param kyouekihiKanjouCd
	 *            共益費個人負担金計上勘定科目コード
	 * @param shatakuShiyouFlg
	 *            社宅使用料
	 * @param tyushajoShiyouFlg
	 *            駐車場使用料
	 * @param kyouekihiFlg
	 *            共益費（個人負担金）
	 * @param genbutsuSanteiFlg
	 *            現物算定額
	 * @return 月別使用料履歴データ
	 */
	private List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> getPositiveRenkeiDataSakuseiSyoriData(
			String shoriNengetsu, String kyuyoCompanyCd, String shatakuKanjouCd, String kyouekihiKanjouCd,
			boolean shatakuShiyouFlg, boolean tyushajoShiyouFlg, boolean kyouekihiFlg, boolean genbutsuSanteiFlg) {

		Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter param = new Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpParameter();

		param.setYearMonth(shoriNengetsu);
		param.setPayCompanyCd(kyuyoCompanyCd);

		if (!NfwStringUtils.isEmpty(shatakuKanjouCd)) {
			param.setShatakuAccountCd(shatakuKanjouCd);
			param.setShatakukanjouCd(shatakuKanjouCd);
		} else {
			param.setShatakuAccountCd(null);
			param.setShatakukanjouCd(null);
		}

		if (!NfwStringUtils.isEmpty(kyouekihiKanjouCd)) {
			param.setKyoekiAccountCd(kyouekihiKanjouCd);
			param.setKyouekihiKanjouCd(kyouekihiKanjouCd);
		} else {
			param.setKyoekiAccountCd(null);
			param.setKyouekihiKanjouCd(null);
		}

		if (shatakuShiyouFlg) {
			param.setRentalTotal(0);
			param.setShatakushiyouFlg("1");
		} else {
			param.setShatakushiyouFlg("0");
		}

		if (tyushajoShiyouFlg) {
			param.setParkingRentalTotal(0);
			param.setTyushajoShiyouFlg("1");
		} else {
			param.setTyushajoShiyouFlg("0");
		}

		if (kyouekihiFlg) {
			param.setKyoekihiPersonTotal(0);
			param.setKyouekihiFlg("1");
		} else {
			param.setKyouekihiFlg("0");
		}

		if (genbutsuSanteiFlg) {
			param.setGenbutuSantei(0);
			param.setGenbutsusanteiFlg("1");
		} else {
			param.setGenbutsusanteiFlg("0");
		}

		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExp> outData = skf3050Bt003GetPositiveRenkeiDataSakuseiSyoriDataExpRepository
				.getPositiveRenkeiDataSakuseiSyoriData(param);

		return outData;
	}

	/**
	 * 貸与データの作成（Excelファイル作成）
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @param saveFolder
	 *            保存先フォルダ名
	 * @param wageType
	 *            ウェッジタイプ
	 * @return 作成件数
	 * @throws Exception
	 */
	private List<RowDataBean> createExcelFilePositiveRenkeiBihinIF0011Info(List<RowDataBean> rowDataBeanList,
			String shoriNengetsu, String kyuyoCompanyCd) throws Exception {

		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> bihinDataList = getPositiveRenkeiDataSakuseiBihinData(
				shoriNengetsu, kyuyoCompanyCd);

		if (bihinDataList.size() > 0) {
			int targetRow = START_LINE;

			if (rowDataBeanList.size() != 0) {
				targetRow = rowDataBeanList.size() + START_LINE;
			}

			for (int i = 0; i < bihinDataList.size(); i++) {
				Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp bihinData = bihinDataList.get(i);

				String shainNo = "";
				if (!NfwStringUtils.isEmpty(bihinData.getShainNo())) {
					shainNo = bihinData.getShainNo();
				}

				String bihinGenbutuGoukei = "";
				if (bihinData.getBihinGenbutuGoukei() != null) {
					bihinGenbutuGoukei = bihinData.getBihinGenbutuGoukei().toString();
				}

				targetRow++;

				RowDataBean rowData = createRowData(targetRow, shainNo, shoriNengetsu, "", "", "", "", "", "", "", "",
						"", bihinGenbutuGoukei);
				if (rowData != null) {
					rowDataBeanList.add(rowData);
				}
			}
		}

		return rowDataBeanList;
	}

	/**
	 * POSITIVE連携備品データ取得
	 * 
	 * @param shoriNengetsu
	 *            処理年月
	 * @param kyuyoCompanyCd
	 *            給与支給会社コード
	 * @return POSITIVE連携備品データ
	 */
	private List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> getPositiveRenkeiDataSakuseiBihinData(
			String shoriNengetsu, String kyuyoCompanyCd) {

		Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter param = new Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpParameter();

		param.setYearMonth(shoriNengetsu);

		if (kyuyoCompanyCd == null) {
			param.setPayCompanyCd(null);
		} else {
			param.setPayCompanyCd(kyuyoCompanyCd);
		}

		List<Skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExp> outData = skf3050Bt003GetPositiveRenkeiDataSakuseiBihinDataExpRepository
				.getPositiveRenkeiDataSakuseiBihinData(param);

		return outData;
	}

	/**
	 * 月次処理管理テーブルのデータ更新
	 * 
	 * @param updateUser
	 *            更新ユーザ
	 * @param shoriNengetsu
	 *            処理年月
	 */
	private void updateGetsujiShoriKanri(String updateUser, String shoriNengetsu) {

		Skf3050Bt003UpdateGetsujiShoriKanriExpParameter param = new Skf3050Bt003UpdateGetsujiShoriKanriExpParameter();

		if (updateUser == null) {
			param.setUpdateUserId(null);
		} else {
			param.setUpdateUserId(updateUser);
		}

		param.setCycleBillingYymm(shoriNengetsu);

		Integer updCnt = skf3050Bt003UpdateGetsujiShoriKanriExpRepository.updateGetsujiShoriKanri(param);

		LogUtils.debugByMsg("月次処理管理テーブル更新件数：" + updCnt + "件");
	}

}
