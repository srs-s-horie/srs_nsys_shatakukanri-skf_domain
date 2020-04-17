package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc001;

/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKind;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKindKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.CellDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc001.Skf3090Sc001ImportDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc001 現物支給価額一覧の読み込み処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc001ImportService extends BaseServiceAbstract<Skf3090Sc001ImportDto> {

	@Autowired
	private Skf3090Sc001SharedService skf3090Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;

	@Autowired
	private Skf3050MPayInKindRepository skf3050MPayInKindRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	/** メッセージ */
	private final static String JOGAI_MOJI = "都道府県名";

	/// ** エラー時の変更対象項目 */
	/** 取込ファイルの長さ（上限） */
	private final static int TORIKOMI_FILE_LENGTH = 256;

	/** ヘッダ行の検索回数 */
	private final static int HEADER_SERCH_CNT = 48;

	/** 取込列数 **/
	private final static int ROW_COUNT = 4;

	private final static String PREF_CD = "都道府県コード";
	private final static String JYUKYO_RIEKI_GAKU = "現物支給価額";
	private final static String TEKIYOU_DATE = "改定日";
	/** 都道府県コードチェック用最小値 **/
	private final static int PREFCD_STAR = 1;
	/** 都道府県コードチェック用最大値 **/
	private final static int PREFCD_END = 47;

	/** 現物支給価額マスタ一覧 列番号 */
	private enum IMPORT_COL {
		IMPORT_COL_NO("No"), IMPORT_COL_PREF_NAME("都道府県"), IMPORT_COL_MONEY("現物支給価額"), IMPORT_COL_EFFECTIVE_DATE("改定日");

		private String colStr;

		private IMPORT_COL(String colStr) {
			this.colStr = colStr;
		}

		public String getColStr() {
			return colStr;
		}
	}

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * サービス処理を行う。
	 * 
	 * @param importDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	@Transactional
	public BaseDto index(Skf3090Sc001ImportDto importDto) throws Exception {

		importDto.setPageTitleKey(MessageIdConstant.SKF3090_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("現物支給価額リスト取込", CodeConstant.C001, importDto.getPageId());

		// 必須チェック
		boolean isNormalData = checkValidateInput(importDto);
		if (!isNormalData) {
			return importDto;
		}

		// 転任者調書の取込
		boolean canImportData = importGenbutsuShikyuKagaku(importDto);
		// 取込に失敗
		if (!canImportData) {
			// エラー発生
			// 例外時はthrowしてRollBack エラーメッセージは設定済み
			throwBusinessExceptionIfErrors(importDto.getResultMessages());
		} else {
			// 画面リロード？？

			// 現物支給価額マスタ一覧を取得
			skf3090Sc001SharedService.getGenbutsuShikyuKagakuDataInfo(importDto);

		}

		return importDto;
	}

	/**
	 * 入力チェック (未入力チェックとファイル名のバイト数チェック）
	 * 
	 * @param importDto
	 * @return チェック結果
	 */
	private boolean checkValidateInput(Skf3090Sc001ImportDto importDto) {

		MultipartFile genbutsuShikyuKagakuFile = importDto.getListGenbutsuShikyuKagaku(); // 取り込んだ現物支給価額マスタ一覧
		String fileName = genbutsuShikyuKagakuFile.getOriginalFilename(); // ファイル名

		// 未入力チェック
		if (null == fileName || CheckUtils.isEmpty(fileName)) {
			LogUtils.debugByMsg("Excelファイル未入力");
			return false;
		}

		// ファイル名のバイト数チェック
		int fileLen = fileName.getBytes().length; // ファイルのバイト数
		if (fileLen > TORIKOMI_FILE_LENGTH) {
			ServiceHelper.addErrorResultMessage(importDto, null, MessageIdConstant.E_SKF_1049, "取込ファイル",
					TORIKOMI_FILE_LENGTH);
			importDto.setFileBoxErr(validationErrorCode);
			return false;
		}

		return true;
	}

	/**
	 * 現物支給価額マスタ一覧を取り込む
	 * 
	 * @param importData
	 * @return 取込結果
	 */
	private boolean importGenbutsuShikyuKagaku(Skf3090Sc001ImportDto importDto) {
		// 現物支給価額マスタエクセルファイル
		WorkBookDataBean excelFile = importExcelFile(importDto);
		if (excelFile == null) {
			return false;
		}

		// システム処理年月の取得
		String systemShoriNengetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();

		List<SheetDataBean> sheetDataBeanList = excelFile.getSheetDataBeanList();
		/*
		 * // エクセルのシート数チェック if (sheetDataBeanList.size() > SHEET_COUNT) {
		 * ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[]
		 * { ERR_TARGET_ITEM }, MessageIdConstant.E_SKF_1043, MSG_SHEET_COUNT);
		 * return false; }
		 */
		SheetDataBean sheetDataBean = sheetDataBeanList.get(0);
		List<RowDataBean> rowDataBeanList = sheetDataBean.getRowDataBeanList();
		Map<String, Integer> posMap = createColumnNoMap(rowDataBeanList); // 対象のデータ格納position
		if (posMap == null) {
			// 先頭行が取得できなかった
			// error.skf.e_skf_1091={0}が存在しません。
			ServiceHelper.addErrorResultMessage(importDto, null, MessageIdConstant.E_SKF_1091, "取込対象");
			return false;
		}

		// 全件数カウンタ
		int recordCount = 0;
		// 取込可カウンタ
		int impOkRecordCount = 0;
		// 取込不可カウンタ
		int impNgRecordCount = 0;

		List<String> existGenbutsuShikyuKagakuKeyList = new ArrayList<String>(); // 取り込んだ現物支給価額保持リスト(重複確認用)

		// 転任者調書エクセルファイルの内容読み込んでいく
		for (int i = 0; i < (rowDataBeanList.size() - 1); i++) {

			// 先頭行は飛ばすので、i+1
			RowDataBean row = rowDataBeanList.get(i + 1);
			List<CellDataBean> cellDataBeanList = row.getCellDataBeanList();

			if (cellDataBeanList.size() == 0) {
				// 全列ブランクの場合、読み飛ばす
				LogUtils.debugByMsg("現物支給価額マスタ取込：全列ブランク（" + (i + 1) + "行目)");
				// 次の行へ
				continue;
			}

			// データが登録されていない場合はスキップ
			if (cellDataBeanList.size() == ROW_COUNT) {
				if (false == dataEmptyCheck(posMap, cellDataBeanList)) {
					// 全列ブランクの場合、読み飛ばす
					LogUtils.debugByMsg("現物支給価額マスタ取込：全列ブランク（" + (i + 1) + "行目)");
					// 次の行へ
					continue;
				}
			}

			// 全件数カウンタ
			recordCount += 1;

			if (cellDataBeanList.size() != ROW_COUNT) {
				// 項目数エラー
				ServiceHelper.addErrorResultMessage(importDto, null, MessageIdConstant.E_SKF_1095, (i + 2) + "行目 項目数",
						cellDataBeanList.size());
				// 取り込み不可カウンタをインクリメント
				impNgRecordCount += 1;
				// 次の行へ
				continue;
			}

			// フォーマットチェック
			List<String> rowDataList = new ArrayList<String>();
			if (false == isFormatCheckDetail(posMap, cellDataBeanList, importDto, rowDataList, i + 2,
					systemShoriNengetsu)) {
				// フォーマットエラー
				// 取り込み不可カウンタをインクリメント
				impNgRecordCount += 1;
				// 次の行へ
				continue;
			}
			// フォーマットチェックが終わったデータを各変数へ
			// - 都道府県コード
			String prefCd = rowDataList.get(0);
			// - 都道府県
//			String prefName = rowDataList.get(1);
			// - 現物支給価額
			String money = rowDataList.get(2);
			// - 改定日
			String effectiveDate = rowDataList.get(3);

			// 取込Excel内に同一キーが存在しないかチェック
			StringBuilder genbutsuShikyuKagakuKey = new StringBuilder();
			genbutsuShikyuKagakuKey.append(effectiveDate);
			genbutsuShikyuKagakuKey.append(prefCd);

			if (existGenbutsuShikyuKagakuKeyList.size() > 0
					&& existGenbutsuShikyuKagakuKeyList.contains(genbutsuShikyuKagakuKey.toString())) {
				// 重複エラー
				// error.skf.e_skf_1123=取込ファイル内に、キー重複を検出しました。
				// ServiceHelper.addErrorResultMessage(importDto, null,
				// MessageIdConstant.E_SKF_1123);
				ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1123);
				// 取り込み不可カウンタをインクリメント
				impNgRecordCount += 1;
				// 次の行へ
				continue;
			} else {
				// 重複チェック用のキー項目に取得したキー項目を設定
				existGenbutsuShikyuKagakuKeyList.add(genbutsuShikyuKagakuKey.toString());
			}

			// 都道府県コードの0埋め
			String paddingPrefCd = String.format("%2s", prefCd).replace(" ", "0");

			try {
				// 現物支給価額マスタの削除・登録
				Skf3050MPayInKindKey key = new Skf3050MPayInKindKey();
				key.setEffectiveDate(effectiveDate);
				key.setPrefCd(paddingPrefCd);
				skf3050MPayInKindRepository.deleteByPrimaryKey(key);

				// 新規登録
				Skf3050MPayInKind record = new Skf3050MPayInKind();
				record.setEffectiveDate(effectiveDate);
				record.setPrefCd(paddingPrefCd);
				record.setKyojuRiekigaku(Short.parseShort(money));
				record.setDeleteFlag("0");
				int insertResult = skf3050MPayInKindRepository.insertSelective(record);

				if (insertResult == -1) {
					// 登録エラー
					// 取込不可カウンタ加算
					impNgRecordCount += 1;
				} else {
					// 取込可カウンタの加算
					impOkRecordCount += 1;
				}

			} catch (Exception e) {
				LogUtils.infoByMsg("importGenbutsuShikyuKagaku, 例外発生：" + e.getMessage());
				// error.skf.e_skf_1079=予期せぬエラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(importDto, null, MessageIdConstant.E_SKF_1079);
				return false;
			}
		}

		// 正常ログ
		if (0 == impNgRecordCount) {
			// infomation.skf.i_skf_2032=取込が完了しました。
			ServiceHelper.addResultMessage(importDto, MessageIdConstant.I_SKF_2032);
			// infomation.skf.i_skf_1015=取込全件数：{0}
			ServiceHelper.addResultMessage(importDto, MessageIdConstant.I_SKF_1015, String.valueOf(recordCount));
			// infomation.skf.i_skf_1029=取込可件数（登録可）：{0}
			ServiceHelper.addResultMessage(importDto, MessageIdConstant.I_SKF_1029, String.valueOf(impOkRecordCount));
			// infomation.skf.i_skf_1016=取込不可件数（登録不可）：{0}
			ServiceHelper.addResultMessage(importDto, MessageIdConstant.I_SKF_1016, String.valueOf(impNgRecordCount));
		} else {
			// infomation.skf.i_skf_2032=取込が完了しました。
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.I_SKF_2032);
			// infomation.skf.i_skf_1015=取込全件数：{0}
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.I_SKF_1015, String.valueOf(recordCount));
			// infomation.skf.i_skf_1029=取込可件数（登録可）：{0}
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.I_SKF_1029,
					String.valueOf(impOkRecordCount));
			// infomation.skf.i_skf_1016=取込不可件数（登録不可）：{0}
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.I_SKF_1016,
					String.valueOf(impNgRecordCount));
		}

		return true;
	}

	/**
	 * 取り込んだファイルをエクセルとして読み込む。
	 * 
	 * @param importFile
	 * @return 読込ファイルデータ
	 */
	private WorkBookDataBean importExcelFile(Skf3090Sc001ImportDto importFile) {
		WorkBookDataBean excelFile = null;

		MultipartFile genbutsuShikyuKagakuFile = importFile.getListGenbutsuShikyuKagaku(); // 取り込んだファイル
		String fileName = genbutsuShikyuKagakuFile.getOriginalFilename(); // ファイル名
		String extension = SkfAttachedFileUtils.getExtension(fileName); // 拡張子
		// 拡張子チェック
		if (!CodeConstant.EXTENSION_XLSX.equals(extension)) {
			ServiceHelper.addErrorResultMessage(importFile, null, MessageIdConstant.E_SKF_1056);
			return excelFile;
		}

		try {
			excelFile = uploadExcelFile(genbutsuShikyuKagakuFile, 1, null);

		} catch (Exception e) {
			LogUtils.infoByMsg("importExcelFile, 例外発生：" + e.toString());
			importFile.setResultMessages(null);
			ServiceHelper.addErrorResultMessage(importFile, null, MessageIdConstant.E_SKF_1078, "");
			throwBusinessExceptionIfErrors(importFile.getResultMessages());
		}

		return excelFile;
	}

	/**
	 * 対象データの取得位置（列）を保持する為のmapを作成する。
	 * 
	 * @param rowDataBeanList
	 * @return 対象データの取得位置保持map
	 */
	private Map<String, Integer> createColumnNoMap(List<RowDataBean> rowDataBeanList) {
		RowDataBean startow = getStartRow(rowDataBeanList); // 取込開始行
		if (null == startow) {
			return null;
		}
		List<CellDataBean> cellDataBeanList = startow.getCellDataBeanList(); // 対象のセルデータ
		Map<String, Integer> outMap = new HashMap<String, Integer>(); // 対象のデータ格納position
		IMPORT_COL[] importColArray = IMPORT_COL.values();

		for (int i = 0; i < importColArray.length; i++) {
			String key = importColArray[i].getColStr();
			// keyを１セルずつ探す
			for (int j = 0; j < cellDataBeanList.size(); j++) {
				String targetVal = cellDataBeanList.get(j).getValue();
				if (targetVal.contains(key)) {
					outMap.put(key, j);
					break;
				}
			}
		}

		return outMap;
	}

	/**
	 * データ読込開始行を取得する。
	 * 
	 * @param rowDataBeanList
	 * @return データ読込開始行
	 */
	private RowDataBean getStartRow(List<RowDataBean> rowDataBeanList) {
		int startIdx = -1;
		int serchCnt = HEADER_SERCH_CNT; // 検索回数
		if (0 == rowDataBeanList.size()) {
			// 行データがない（データ開始行が見つからない）
			return null;
		} else if (rowDataBeanList.size() < HEADER_SERCH_CNT) {
			// serchCnt = HEADER_SERCH_CNT - rowDataBeanList.size();
			serchCnt = rowDataBeanList.size();
		}

		// 開始行を探す
		for (int i = 0; i < serchCnt; i++) {
			if (startIdx >= 0) {
				break;
			}

			RowDataBean row = rowDataBeanList.get(i);
			List<CellDataBean> cellDataBeanList = row.getCellDataBeanList();
			// 指定の文字列を１セルずつ探す
			for (int j = 0; j < cellDataBeanList.size(); j++) {
				String targetVal = cellDataBeanList.get(j).getValue();
				if (JOGAI_MOJI.equals(targetVal)) {
					startIdx = i;
					break;
				}
			}
		}

		// データ開始行が見つからない
		if (startIdx == -1) {
			return null;
		} else {
			return rowDataBeanList.get(startIdx);
		}
	}

	/**
	 * 対象行がすべて空っぽかチェックする
	 * 
	 * @param posMap
	 * @param cellDataBeanList
	 * 
	 * @return チェック結果
	 */
	private boolean dataEmptyCheck(Map<String, Integer> posMap, List<CellDataBean> cellDataBeanList) {

		boolean returnFlg = true;

		// 都道府県コード（=No）
		int prefCdPos = posMap.get(IMPORT_COL.IMPORT_COL_NO.getColStr());
		// 都道府県
		int prefNamePos = posMap.get(IMPORT_COL.IMPORT_COL_PREF_NAME.getColStr());
		// 現物支給価額
		int moneyPos = posMap.get(IMPORT_COL.IMPORT_COL_MONEY.getColStr());
		// 改定日
		int effectiveDatePos = posMap.get(IMPORT_COL.IMPORT_COL_EFFECTIVE_DATE.getColStr());

		if ((true == cellDataBeanList.get(prefCdPos).getValue().isEmpty())
				&& (true == cellDataBeanList.get(prefNamePos).getValue().isEmpty())
				&& (true == cellDataBeanList.get(moneyPos).getValue().isEmpty())
				&& (true == cellDataBeanList.get(effectiveDatePos).getValue().isEmpty())) {
			returnFlg = false;
		}
		return returnFlg;
	}

	/**
	 * フォーマットチェックデータ読込開始行を取得する。
	 * 
	 * @param posMap
	 * @param cellDataBeanList
	 * @param importDto
	 * @param rowDataList
	 * @param rowNo
	 * @param systemShoriNengetsu
	 * 
	 * @return チェック結果
	 */
	private boolean isFormatCheckDetail(Map<String, Integer> posMap, List<CellDataBean> cellDataBeanList,
			Skf3090Sc001ImportDto importDto, List<String> rowDataList, int rowNo, String systemShoriNengetsu) {
		boolean returnFlg = true;

		rowDataList.add(0, null); // 都道府県コード（帳票のNo）用
		rowDataList.add(1, null); // 都道府県名用
		rowDataList.add(2, null); // 現物支給価額用
		rowDataList.add(3, null); // 改定日用

		// 都道府県コード（=No）
		int prefCdPos = posMap.get(IMPORT_COL.IMPORT_COL_NO.getColStr());
		// 都道府県
		int prefNamePos = posMap.get(IMPORT_COL.IMPORT_COL_PREF_NAME.getColStr());
		// 現物支給価額
		int moneyPos = posMap.get(IMPORT_COL.IMPORT_COL_MONEY.getColStr());
		// 改定日
		int effectiveDatePos = posMap.get(IMPORT_COL.IMPORT_COL_EFFECTIVE_DATE.getColStr());

		// 必須チェック
		// - 適用年月日
		if ((true == cellDataBeanList.get(effectiveDatePos).getValue().isEmpty())
				|| cellDataBeanList.get(effectiveDatePos).getValue().trim().length() == 0) {
			// error.skf.e_skf_1096={0}列目の{0}は必須項目です。
			// ServiceHelper.addErrorResultMessage(importDto, null,
			// MessageIdConstant.E_SKF_1096, String.valueOf(rowNo),
			// TEKIYOU_DATE);
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1096, String.valueOf(rowNo) + "行目 4",
					TEKIYOU_DATE);
			// 戻り値として異常を設定
			returnFlg = false;
		}
		// - 都道府県コード
		if ((true == cellDataBeanList.get(prefCdPos).getValue().isEmpty())
				|| cellDataBeanList.get(prefCdPos).getValue().trim().length() == 0) {
			// error.skf.e_skf_1096={0}列目の{0}は必須項目です。
			// ServiceHelper.addErrorResultMessage(importDto, null,
			// MessageIdConstant.E_SKF_1096, String.valueOf(rowNo), PREF_CD);
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1096, String.valueOf(rowNo) + "行目 1",
					PREF_CD);
			// 戻り値として異常を設定
			returnFlg = false;
		}
		// 必須チェックでエラーの場合、呼び出しもとへFalseでReturn
		if (false == returnFlg) {
			return returnFlg;
		}

		// 桁数チェック
		// - 現物支給価額
		String money = cellDataBeanList.get(moneyPos).getValue();
		money = money.replace(",", ""); // カンマを削除
		if (NfwStringUtils.isNotEmpty(money.trim())) {
			try {
				if (CheckUtils.isMoreThanByteSize(money.trim(), 4)) {
					// error.skf.e_skf_1097={0}列目の{1}は{2}桁を超えています。
					// ServiceHelper.addErrorResultMessage(importDto, null,
					// MessageIdConstant.E_SKF_1097, String.valueOf(rowNo),
					// JYUKYO_RIEKI_GAKU, String.valueOf(4));
					ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1097,
							String.valueOf(rowNo) + "行目 3", JYUKYO_RIEKI_GAKU, String.valueOf(4));
					// 戻り値として異常を設定
					returnFlg = false;
				}
			} catch (UnsupportedEncodingException e) {
				// 何かしらのエラーが発生
				// error.skf.e_skf_1097={0}列目の{1}は{2}桁を超えています。
				// ServiceHelper.addErrorResultMessage(importDto, null,
				// MessageIdConstant.E_SKF_1097, String.valueOf(rowNo),
				// JYUKYO_RIEKI_GAKU, String.valueOf(4));
				LogUtils.infoByMsg("isFormatCheckDetail, 例外発生：" + e.toString());
				ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1097, String.valueOf(rowNo) + "行目 3",
						JYUKYO_RIEKI_GAKU, String.valueOf(4));
				// 戻り値として異常を設定
				returnFlg = false;
			}
		}else{
			// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126, String.valueOf(rowNo) + "行目 3",
					JYUKYO_RIEKI_GAKU);
			// 戻り値として異常を設定
			returnFlg = false;
		}


		// 半角数字チェック
		// - 現物支給価額
		if (false == money.matches("^[0-9]*$")) {
			// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
			// ServiceHelper.addErrorResultMessage(importDto, null,
			// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo),
			// JYUKYO_RIEKI_GAKU);
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126, String.valueOf(rowNo) + "行目 3",
					JYUKYO_RIEKI_GAKU);
			// 戻り値として異常を設定
			returnFlg = false;
		}
		// - 都道府県コード
		String prefCd = cellDataBeanList.get(prefCdPos).getValue();
		prefCd = prefCd.trim();
		if (false == prefCd.matches("^[0-9]*$")) {
			// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
			// ServiceHelper.addErrorResultMessage(importDto, null,
			// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo), PREF_CD);
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126, String.valueOf(rowNo) + "行目 1",
					PREF_CD);
			// 戻り値として異常を設定
			returnFlg = false;
		} else {
			// 半角数字の範囲チェック
			if ((Integer.parseInt(prefCd) < PREFCD_STAR) || (Integer.parseInt(prefCd) > PREFCD_END)) {
				// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
				// ServiceHelper.addErrorResultMessage(importDto, null,
				// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo),
				// PREF_CD);
				ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126,
						String.valueOf(rowNo) + "行目 1", PREF_CD);
				// 戻り値として異常を設定
				returnFlg = false;
			}
		}

		// 日付チェック
		// - 適用年月日
		// Excelのシリアル値をフォーマット変換
		String effectiveDate = cellDataBeanList.get(effectiveDatePos).getValue();
		// 数値チェック
		if (false == effectiveDate.matches("^[0-9.]*$")) {
			// 数値以外の場合は全てエラー
			// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
			// ServiceHelper.addErrorResultMessage(importDto, null,
			// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo),
			// TEKIYOU_DATE);
			ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126, String.valueOf(rowNo) + "行目 4",
					TEKIYOU_DATE);
			// 戻り値として異常を設定
			returnFlg = false;
		} else {
			// 数値の場合のみ、年月日に変換
			Calendar cal = Calendar.getInstance();
			cal.set(1900, 0, 1, 12, 0, 0);
			// 1900/1/1のシリアルが1なので、加算する値は取得した値 - 1になる
			effectiveDate = String.format("%d", (int) Double.parseDouble(effectiveDate));
			int serial = Integer.parseInt(effectiveDate) - 1;
			// 1900/2/29は存在しないがエクセル内では存在しているらしいので
			// その日付以降であればさらに -1
			serial -= (serial > 60 ? 1 : 0);
			// 基準日に加算
			cal.add(Calendar.DATE, serial);
			effectiveDate = skfDateFormatUtils.dateFormatFromDate(cal.getTime(), "yyyyMMdd");

			if (skfDateFormatUtils.dateFormatFromDate(cal.getTime(), "yyyyMMdd").isEmpty()) {
				// 戻り値が空（変換できない）
				// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
				// ServiceHelper.addErrorResultMessage(importDto, null,
				// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo),
				// TEKIYOU_DATE);
				ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126,
						String.valueOf(rowNo) + "行目 4", TEKIYOU_DATE);
				// 戻り値として異常を設定
				returnFlg = false;
			} else {
				// 日付の整合性チェック
				if (effectiveDate.compareTo(systemShoriNengetsu + "01") < 0) {
					// 当月以前の日付の場合
					// error.skf.e_skf_1126={0}列目の{1}に不正な値が設定されています。。
					// ServiceHelper.addErrorResultMessage(importDto, null,
					// MessageIdConstant.E_SKF_1126, String.valueOf(rowNo),
					// TEKIYOU_DATE);
					ServiceHelper.addWarnResultMessage(importDto, MessageIdConstant.E_SKF_1126,
							String.valueOf(rowNo) + "行目 4", TEKIYOU_DATE);
					// 戻り値として異常を設定
					returnFlg = false;
				}
			}
		}

		if (true == returnFlg) {
			if ((false == cellDataBeanList.get(prefNamePos).getValue().isEmpty())
					|| cellDataBeanList.get(prefNamePos).getValue().trim().length() > 0) {
				String prefName = cellDataBeanList.get(prefNamePos).getValue();
				rowDataList.set(1, prefName); // 都道府県名用
			}

			rowDataList.set(0, prefCd); // 都道府県コード（帳票のNo）用
			rowDataList.set(2, money); // 現物支給価額用
			rowDataList.set(3, effectiveDate); // 改定日用
		}

		return returnFlg;
	}

}
