package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc002;

/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jp.co.c_nexco.nfw.common.bean.ApplicationScopeBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.bean.CellDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.RowDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.SheetDataBean;
import jp.co.c_nexco.nfw.webcore.utils.bean.WorkBookDataBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.common.Skf302010SaveDto;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc002.Skf3020Sc002ImportDto;

/**
 * Skf3020Sc002 転任者調書情報の読み込みと編集処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc002ImportService extends SkfServiceAbstract<Skf3020Sc002ImportDto> {

	private String companyCd = CodeConstant.C001;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3020Sc002SharedService skf3020Sc002SharedService;
	@Autowired
	private ApplicationScopeBean bean;

	/** メッセージ */
	private final static String MSG_FILE_REQUIRED = "取込ファイル";
	private final static String MSG_FILE_NAME = "転任者調書ファイル名";
	private final static String MSG_FILE_FORMAT = "ファイル形式";
	private final static String MSG_SHEET_COUNT = "シート数";
	private final static String MSG_SHEET_NAME = "シート名";

	private final static String JOGAI_MOJI_1 = "氏名";
	private final static String JOGAI_MOJI_2 = "人事異動";

	/** エラー時の変更対象項目 */
	private final static String ERR_TARGET_ITEM = "fuTenninsha";
	/** 取込ファイルの長さ（上限） */
	private final static int TORIKOMI_FILE_LENGTH = 254;
	/** 社員氏名の検索回数 */
	private final static int NAME_SERCH_CNT = 10;
	/** 取込データ件数の上限 */
	private final static int IMPORT_DATA_CNT_UPPER = 1000;

	/** 転任者調書列番号 */
	private enum IMPORT_COL {
		IMPORT_COL_SHAIN_NO("社員番号"),
		IMPORT_COL_NAME("氏名"),
		IMPORT_COL_TOKYU("等級"),
		IMPORT_COL_AGE("年齢"),
		IMPORT_COL_NEW_AFFILIATION1("新事業所"),
		IMPORT_COL_NEW_AFFILIATION2("新室・部名"),
		IMPORT_COL_NEW_AFFILIATION3("新課等名"),
		IMPORT_COL_NEW_AFFILIATION4("新役職"),
		IMPORT_COL_NEW_AFFILIATION5("新兼務区分"),
		IMPORT_COL_NOW_AFFILIATION1("現事業所"),
		IMPORT_COL_NOW_AFFILIATION2("現室・部名"),
		IMPORT_COL_NOW_AFFILIATION3("現課等名"),
		IMPORT_COL_NOW_AFFILIATION4("現役職"),
		IMPORT_COL_NOW_AFFILIATION5("現兼務区分"),
		IMPORT_COL_BIKO("備考"),
		IMPORT_COL_DATA_ISSUED_DATE("解禁日");

		private String colStr;

		private IMPORT_COL(String colStr) {
			this.colStr = colStr;
		}

		public String getColStr() {
			return colStr;
		}
	}
	
	// 読み取り対象シート名
	@Value("${skf3020.skf3020_sc002.sheet_name}")
	private String readSheetName;	

	@Override
	public BaseDto index(Skf3020Sc002ImportDto tenninshaChoshoDto) throws Exception {
		skfOperationLogUtils.setAccessLog("転任者調書の取込開始", companyCd, FunctionIdConstant.SKF3020_SC002);

		// 必須チェック
		boolean isNormalData = checkValidateInput(tenninshaChoshoDto);
		if (!isNormalData) {
			return tenninshaChoshoDto;
		}

		List<Skf302010SaveDto> tenninshaChoshoData = new ArrayList<Skf302010SaveDto>();
		// 転任者調書の取込
		boolean canImportData = importTenninshaChosho(tenninshaChoshoDto, tenninshaChoshoData);
		// 取込に失敗
		if (!canImportData) {
			return tenninshaChoshoDto;
		} else {
			// セッションの登録
			bean.put(SessionCacheKeyConstant.TENNINSHAREGIST_INFO, tenninshaChoshoData);
		}

		// 画面遷移(転任者情報確認へ遷移)
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC003, "init");
		tenninshaChoshoDto.setTransferPageInfo(nextPage);

		return tenninshaChoshoDto;
	}

	/**
	 * 入力チェック
	 * 
	 * @param tenninshaChoshoDto
	 * @return チェック結果
	 */
	private boolean checkValidateInput(Skf3020Sc002ImportDto tenninshaChoshoDto) {

		MultipartFile tenninshaChoshoFile = tenninshaChoshoDto.getFuTenninsha(); // 取り込んだ転任者調書
		String fileName = tenninshaChoshoFile.getOriginalFilename(); // ファイル名
		// 未入力チェック
		if (fileName == null || CheckUtils.isEmpty(fileName)) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1054, MSG_FILE_REQUIRED);
			return false;
		}

		int fileLen = fileName.getBytes().length; // ファイルのバイト数
		// ファイル名のバイト数チェック
		if (fileLen > TORIKOMI_FILE_LENGTH) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1071, MSG_FILE_NAME, TORIKOMI_FILE_LENGTH);
			return false;
		}

		return true;
	}

	/**
	 * 転任者調書ファイルの内容を取り込んで、遷移先へ渡すデータを作成する。
	 * 
	 * @param tenninshaChoshoDto
	 * @param importData
	 * @return 取込結果
	 */
	private boolean importTenninshaChosho(Skf3020Sc002ImportDto tenninshaChoshoDto, List<Skf302010SaveDto> importData) {
		// 転任者調書エクセルファイル
		WorkBookDataBean excelFile = uploadExcelFile(tenninshaChoshoDto);
		if (excelFile == null) {
			return false;
		}

		List<SheetDataBean> sheetDataBeanList = excelFile.getSheetDataBeanList();
		// エクセルのシート数チェック
		//複数シートのファイルに変更されたので、0以下でエラーに変更
		if (sheetDataBeanList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1043, MSG_SHEET_COUNT);
			return false;
		}

		SheetDataBean sheetDataBean = null;
		for(int i=0; i < sheetDataBeanList.size(); i++){
			sheetDataBean = sheetDataBeanList.get(i);
			//シート名が読込対象と一致するか
			if(Objects.equals(readSheetName, sheetDataBean.getSheetName())){
				//一致でループ抜け
				break;
			}
			//不一致の場合nullに戻す
			sheetDataBean = null;
		}
		if(sheetDataBean == null){
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1043, MSG_SHEET_NAME);
			return false;
		}
				
		//SheetDataBean sheetDataBean = sheetDataBeanList.get(0);
		List<RowDataBean> rowDataBeanList = sheetDataBean.getRowDataBeanList();
		Map<String, Integer> posMap = createColumnNoMap(rowDataBeanList); // 対象のデータ格納position
		int shimeiEmptyCnt = 0; // 空の社員氏名カウント
		boolean duplicateShainNo = false; // 重複する社員番号の判定用
		List<String> existShainNoList = new ArrayList<String>(); // 取り込んだ社員番号保持リスト(重複確認用)
		List<String> existShainNoErrorList = new ArrayList<String>(); // 取り込んだ社員番号保持リスト(重複エラー用)
		boolean nonexistShainNo = false; // 存在しない社員番号の判定用
		List<String> nonexistShainNoList = new ArrayList<String>(); // 取り込んだ社員番号保持リスト(既存確認用)

		// 転任者調書エクセルファイルの内容読み込んでいく
		for (int i = 0; i < (rowDataBeanList.size() - 1); i++) {
			// 先頭行は飛ばす
			RowDataBean row = rowDataBeanList.get(i + 1);
			List<CellDataBean> cellDataBeanList = row.getCellDataBeanList();

			int shimeiPos = posMap.get(IMPORT_COL.IMPORT_COL_NAME.getColStr());
			String shimei = cellDataBeanList.get(shimeiPos).getValue(); // 社員氏名
			if (shimei == null || CheckUtils.isEmpty(shimei)) {
				shimeiEmptyCnt++;
				// 社員氏名が連続で無い場合
				if (shimeiEmptyCnt > NAME_SERCH_CNT) {
					break;
				}
				continue;
			}

			// 社員氏名に除外文字が含まれている場合
			if (JOGAI_MOJI_1.equals(shimei) || shimei.contains(JOGAI_MOJI_2)) {
				continue;
			}

			int shainNoPos = posMap.get(IMPORT_COL.IMPORT_COL_SHAIN_NO.getColStr());
			String shainNo = cellDataBeanList.get(shainNoPos).getValue(); // 社員番号
			if (shainNo != null && !CheckUtils.isEmpty(shainNo)) {
				boolean exsistNoFlg = false;

				for (int j = 0; j < existShainNoList.size(); j++) {
					String listShainNo = existShainNoList.get(j);
					// 社員番号重複チェック
					if (Objects.equals(shainNo, listShainNo)) {
						exsistNoFlg = true;
						duplicateShainNo = true;
						
						boolean errorFlg = true;
						for (int k = 0; k < existShainNoErrorList.size(); k++) {
							// エラー社員番号重複チェック
							if (Objects.equals(shainNo, existShainNoErrorList.get(k))) {
								//エラー用リストに存在する
								errorFlg = false;
								break;
							}
						}
						if(errorFlg){
							// エラー用リストに無い場合
							existShainNoErrorList.add(shainNo);
						}
						break;
					}
				}

				// リストに格納されてない社員番号の場合
				if (!exsistNoFlg) {
					existShainNoList.add(shainNo);
				}
				exsistNoFlg = false;

				// 社員番号存在チェック
				if (!skf3020Sc002SharedService.checkShainExists(shainNo)) {
					nonexistShainNo = true;
					exsistNoFlg = true;
					for (int j = 0; j < nonexistShainNoList.size(); j++) {
						// 社員番号重複チェック
						if (Objects.equals(shainNo, nonexistShainNoList.get(j))) {
							exsistNoFlg = false;
							break;
						}
					}
				}
				
				if (exsistNoFlg) {
					nonexistShainNoList.add(shainNo);
				}
			}

			// 取込データ保持DTO作成
			Skf302010SaveDto saveDto = createSaveDto(shimei, shainNo, cellDataBeanList, posMap);
			importData.add(saveDto);

			if (importData.size() > IMPORT_DATA_CNT_UPPER) {
				break;
			}

			// カウント初期化
			shimeiEmptyCnt = 0;
		}

		// 社員番号重複している場合
		if (duplicateShainNo) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_3045, "");
			
			for (int i=0; i < existShainNoErrorList.size(); i++) {
					ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
							MessageIdConstant.SKF3020_ERR_MSG_COMMON, existShainNoErrorList.get(i));
			}
			return false;
		}

		// 存在しない社員番号がある場合
		if (nonexistShainNo) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_3046, "");
			
			for (int i=0; i < nonexistShainNoList.size(); i++) {
					ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
							MessageIdConstant.SKF3020_ERR_MSG_COMMON, nonexistShainNoList.get(i));
			}
			return false;
		}

		// データ無しの場合
		if (importData.size() == 0) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1064);
			return false;
		}

		// データ件数の上限越え
		if (importData.size() > IMPORT_DATA_CNT_UPPER) {
			ServiceHelper.addErrorResultMessage(tenninshaChoshoDto, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1065, IMPORT_DATA_CNT_UPPER);
			return false;
		}

		return true;
	}

	/**
	 * 取り込んだファイルをエクセルとして読み込む。
	 * 
	 * @param importFile
	 * @return 読込ファイルデータ
	 */
	private WorkBookDataBean uploadExcelFile(Skf3020Sc002ImportDto importFile) {
		WorkBookDataBean excelFile = null;

		MultipartFile tenninshaChoshoFile = importFile.getFuTenninsha(); // 取り込んだ転任者調書
		String fileName = tenninshaChoshoFile.getOriginalFilename(); // ファイル名
		String extension = SkfAttachedFileUtils.getExtension(fileName); // 拡張子
		// 拡張子チェック
		if (!CodeConstant.EXTENSION_XLSX.equals(extension)) {
			ServiceHelper.addErrorResultMessage(importFile, new String[] { ERR_TARGET_ITEM },
					MessageIdConstant.E_SKF_1043, MSG_FILE_FORMAT);
			return excelFile;
		}

		try {
			excelFile = uploadExcelFile(tenninshaChoshoFile, 1, null);

		} catch (Exception e) {
			LogUtils.infoByMsg("uploadExcelFile, エクセルファイル読込に失敗 " + e.toString());
			importFile.setResultMessages(null);
			ServiceHelper.addErrorResultMessage(importFile, null, MessageIdConstant.E_SKF_1078, "");
			throwBusinessExceptionIfErrors(importFile.getResultMessages());
		}

		return excelFile;
	}

	/**
	 * データ読込開始行を取得する。
	 * 
	 * @param rowDataBeanList
	 * @return データ読込開始行
	 */
	private RowDataBean getStartRow(List<RowDataBean> rowDataBeanList) {
		int startIdx = -1;
		int serchCnt = NAME_SERCH_CNT; // 検索回数
		if (rowDataBeanList.size() < NAME_SERCH_CNT) {
			serchCnt = NAME_SERCH_CNT - rowDataBeanList.size();
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
				if (JOGAI_MOJI_1.equals(targetVal)) {
					startIdx = i;
					break;
				}
			}
		}

		return rowDataBeanList.get(startIdx);
	}

	/**
	 * 対象データの取得位置（列）を保持する為のmapを作成する。
	 * 
	 * @param rowDataBeanList
	 * @return 対象データの取得位置保持map
	 */
	private Map<String, Integer> createColumnNoMap(List<RowDataBean> rowDataBeanList) {
		RowDataBean startow = getStartRow(rowDataBeanList); // 取込開始行
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
	 * 読込んだデータを保持しておく為のDTOを作成する。
	 * 
	 * @param shimei
	 * @param shainNo
	 * @param cellDataBeanList
	 * @param posMap
	 * @return 読込データ保持DTO
	 */
	private Skf302010SaveDto createSaveDto(String shimei, String shainNo, List<CellDataBean> cellDataBeanList,
			Map<String, Integer> posMap) {
		
		Skf302010SaveDto saveDto = new Skf302010SaveDto();
		// 社員氏名
		saveDto.setName(shimei);
		// 社員番号
		saveDto.setShainNo(shainNo);
		// 等級
		int tokyuPos = posMap.get(IMPORT_COL.IMPORT_COL_TOKYU.getColStr());
		saveDto.setTokyu(cellDataBeanList.get(tokyuPos).getValue());
		// 年齢
		int agePos = posMap.get(IMPORT_COL.IMPORT_COL_AGE.getColStr());
		//saveDto.setAge(cellDataBeanList.get(agePos).getValue());
		if(cellDataBeanList.get(agePos).getValue() != null){
			saveDto.setAge(cellDataBeanList.get(agePos).getValue().replace("歳", ""));
		}
		// 備考
		int bikoPos = posMap.get(IMPORT_COL.IMPORT_COL_BIKO.getColStr());
		saveDto.setBiko(cellDataBeanList.get(bikoPos).getValue());

		int[] nowAffiliationPosArray = { posMap.get(IMPORT_COL.IMPORT_COL_NOW_AFFILIATION1.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NOW_AFFILIATION2.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NOW_AFFILIATION3.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NOW_AFFILIATION4.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NOW_AFFILIATION5.getColStr()) };
		// 現所属
		String nowAffiliation = editAffiliation(cellDataBeanList, nowAffiliationPosArray);
		saveDto.setNowAffiliation(nowAffiliation);

		int[] newAffiliationPosArray = { posMap.get(IMPORT_COL.IMPORT_COL_NEW_AFFILIATION1.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NEW_AFFILIATION2.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NEW_AFFILIATION3.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NEW_AFFILIATION4.getColStr()),
				posMap.get(IMPORT_COL.IMPORT_COL_NEW_AFFILIATION5.getColStr()) };
		// 新所属
		String newAffiliation = editAffiliation(cellDataBeanList, newAffiliationPosArray);
		saveDto.setNewAffiliation(newAffiliation);

		LogUtils.debugByMsg("セッション登録DTO ： " + saveDto);

		return saveDto;
	}

	/**
	 * 所属の表示内容を編集する。
	 * 
	 * @param cellDataBeanList
	 * @param posMap
	 * @return 編集後の所属
	 */
	private String editAffiliation(List<CellDataBean> cellDataBeanList, int[] posArray) {

		String br = "\n";
		// 改行ごとに配列にセットしていく。
		String str_1 = cellDataBeanList.get(posArray[0]).getValue();
		String[] line_1 = str_1.split(br, 0);
		String str_2 = cellDataBeanList.get(posArray[1]).getValue();
		String[] line_2 = str_2.split(br, 0);
		String str_3 = cellDataBeanList.get(posArray[2]).getValue();
		String[] line_3 = str_3.split(br, 0);
		String str_4 = cellDataBeanList.get(posArray[3]).getValue();
		String[] line_4 = str_4.split(br, 0);
		String str_5 = cellDataBeanList.get(posArray[4]).getValue();
		String[] line_5 = str_5.split(br, 0);

		int addLineCnt = 0;
		String outVal = "";

		for (;;) {
			outVal += line_1.length > addLineCnt ? line_1[addLineCnt] + CodeConstant.SPACE : "";
			outVal += line_2.length > addLineCnt ? line_2[addLineCnt] + CodeConstant.SPACE : "";
			outVal += line_3.length > addLineCnt ? line_3[addLineCnt] + CodeConstant.SPACE : "";
			outVal += line_4.length > addLineCnt ? line_4[addLineCnt] + CodeConstant.SPACE : "";
			outVal += line_5.length > addLineCnt ? line_5[addLineCnt] + CodeConstant.SPACE : "";
			addLineCnt++;

			if (line_1.length < (addLineCnt + 1) && line_2.length < (addLineCnt + 1) && line_3.length < (addLineCnt + 1)
					&& line_4.length < (addLineCnt + 1) && line_5.length < (addLineCnt + 1)) {
				break;

			} else {
				// 1回目は改行無し
				if (addLineCnt != 0) {
					// データがまだある場合は1行あける。
					outVal += br + br;
				}
			}
		}

		return outVal;
	}

}
