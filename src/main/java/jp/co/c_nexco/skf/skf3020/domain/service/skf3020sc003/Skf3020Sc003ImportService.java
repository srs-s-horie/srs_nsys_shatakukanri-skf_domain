/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc003;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc003.Skf3020Sc003ImportDto;
import jp.co.c_nexco.skf.skf3020.domain.service.common.Skf302010CommonSharedService;

/**
 * Skf3020Sc003 転任者調書更新・登録処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc003ImportService extends BaseServiceAbstract<Skf3020Sc003ImportDto> {

	private String companyCd = CodeConstant.C001;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3020Sc003SharedService skf3020Sc003SharedService;
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	@Autowired
	private Skf302010CommonSharedService skf302010CommonSharedService;

	/** 最大文字数 */
	private final static int MAX_LEN_4 = 4;
	private final static int MAX_LEN_8 = 8;
	private final static int MAX_LEN_20 = 20;
	private final static int MAX_LEN_255 = 255;
	private final static int MAX_LEN_1600 = 1600;

	private final static String TENNIN_CHOSHO_DATA_UPDATE_KEY = "Skf3020TTenninshaChoshoDataUpdateDate";

	public BaseDto index(Skf3020Sc003ImportDto importDto) throws Exception {
		skfOperationLogUtils.setAccessLog("転任者情報の取込開始", companyCd, importDto.getPageId());

		// 取込チェック
		importDto = validateTorikomiJikko(importDto);
		if (importDto.getResultMessages() != null) {
			return importDto;
		}

		String errMsgId = operateTenninshaInfo(importDto);
		if (!"".equals(errMsgId)) {
			ServiceHelper.addErrorResultMessage(importDto, null, errMsgId);

		} else {
			// 画面遷移(転任者一覧へ遷移)
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			importDto.setTransferPageInfo(nextPage);
		}

		return importDto;
	}

	/**
	 * 転任者情報テーブルのチェック
	 * 
	 * @param importDto
	 *            Skf3020Sc003ImportDto
	 * @return 結果
	 */
	private Skf3020Sc003ImportDto validateTorikomiJikko(Skf3020Sc003ImportDto importDto) {
		// メッセージ初期化
		importDto.setResultMessages(null);
		List<Map<String, Object>> tenninshaChoshoDataTable = importDto.getTenninshaChoshoDataTable(); // 転任者情報テーブル
		boolean errFlg = false; // エラー判定フラグ

		// リストテーブル項目すべてチェック
		for (int i = 0; i < tenninshaChoshoDataTable.size(); i++) {
			Map<String, Object> targetMap = tenninshaChoshoDataTable.get(i);
			// エラー行を初期化
			targetMap.put(Skf3020Sc003SharedService.ERR_COL, "");
			String errItems = ""; // エラー対象の項目保持用

			String shainNo = (String) targetMap.get(Skf3020Sc003SharedService.SHAIN_NO_COL); // 社員番号
			// 社員番号チェック
			if (!checkShainNo(shainNo)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.SHAIN_NO_COL);
				errFlg = true;
			}

			String shainName = (String) targetMap.get(Skf3020Sc003SharedService.NAME_COL); // 社員氏名
			// 社員氏名チェック
			if (!checkShainName(shainName)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.NAME_COL);
				errFlg = true;
			}

			String tokyu = (String) targetMap.get(Skf3020Sc003SharedService.TOKYU_COL); // 等級
			// 等級チェック
			if (!checkStrLen(tokyu, MAX_LEN_255)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.TOKYU_COL);
				errFlg = true;
			}

			String age = (String) targetMap.get(Skf3020Sc003SharedService.AGE_COL); // 年齢
			// 年齢チェック
			if (!checkStrLen(age, MAX_LEN_4)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.AGE_COL);
				errFlg = true;
			}

			String newAffiliation = (String) targetMap.get(Skf3020Sc003SharedService.NEW_AFFILIATION_COL); // 新所属
			// 新所属チェック
			if (!checkStrLen(newAffiliation, MAX_LEN_255)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.NEW_AFFILIATION_COL);
				errFlg = true;
			}

			String nowAffiliation = (String) targetMap.get(Skf3020Sc003SharedService.NOW_AFFILIATION_COL); // 現所属
			// 現所属チェック
			if (!checkStrLen(nowAffiliation, MAX_LEN_255)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.NOW_AFFILIATION_COL);
				errFlg = true;
			}

			String biko = (String) targetMap.get(Skf3020Sc003SharedService.BIKO_COL); // 備考
			// 備考チェック
			if (!checkStrLen(biko, MAX_LEN_1600)) {
				errItems = setErrItems(errItems, Skf3020Sc003SharedService.BIKO_COL);
				errFlg = true;
			}

			targetMap.put(Skf3020Sc003SharedService.ERR_COL, errItems);
			tenninshaChoshoDataTable.set(i, targetMap);
		}

		importDto.setTenninshaChoshoDataTable(tenninshaChoshoDataTable);

		if (errFlg) {
			ServiceHelper.addErrorResultMessage(importDto, null, MessageIdConstant.E_SKF_1068);
		}

		return importDto;
	}

	/**
	 * 社員番号チェック
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 結果
	 */
	private boolean checkShainNo(String shainNo) {

		if (skf302010CommonSharedService.isExistSpace(shainNo)
				|| !shainNo.matches("^[a-zA-Z0-9]*$")) {
			return false;
		}

		if (!checkStrLen(shainNo, MAX_LEN_8)) {
			return false;
		}

		return true;
	}

	/**
	 * 社員氏名チェック
	 * 
	 * @param shainName
	 *            社員氏名
	 * @return 結果
	 */
	private boolean checkShainName(String shainName) {

		if (shainName == null || CheckUtils.isEmpty(shainName)) {
			return false;
		}

		if (!checkStrLen(shainName, MAX_LEN_20)) {
			return false;
		}

		return true;
	}

	/**
	 * 文字列の長さチェック
	 * 
	 * @param str
	 *            対象文字列
	 * @param maxByte
	 *            比較する長さ（上限）
	 * @return 結果
	 */
	private boolean checkStrLen(String str, int maxLen) {

		int targetLen = str.length();
		if (targetLen > maxLen) {
			return false;
		}

		return true;
	}

	/**
	 * エラー対象項目の設定
	 * 
	 * @param inItems
	 *            エラー項目
	 * @param targetItem
	 *            今回のエラー対象項目
	 * @return エラー対象項目
	 */
	private String setErrItems(String inItems, String targetItem) {

		String outItems = "";

		if ("".equals(inItems)) {
			outItems = targetItem;

		} else {
			// カンマ区切りでエラー対象の項目を結合していく。
			outItems = inItems + CodeConstant.COMMA + targetItem;
		}

		return outItems;
	}

	/**
	 * 転任者調書データ更新、もしくは登録を行う。
	 * 
	 * @param importDto
	 *            転任者調書取込DTO
	 * @return 更新、登録結果(エラーの場合はメッセージIDを設定）
	 * @throws Exception
	 */
	@Transactional
	private String operateTenninshaInfo(Skf3020Sc003ImportDto importDto) throws Exception {

		List<Map<String, Object>> infoList = importDto.getTenninshaChoshoDataTable();
		String errResult = "";
		
		String firstShainNo = (String) infoList.get(0).get(Skf3020Sc003SharedService.SHAIN_NO_COL); // 社員番号
		
//		// 最新更新日を取得
//		String update_data_1 = skf3020Sc003SharedService.getTenninshaInfoForUpdate(firstShainNo);
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
//		
//		if (!NfwStringUtils.isEmpty(update_data_1)) {
//			Date lastUpdateData = sdFormat.parse(update_data_1);
//			// 排他用更新日設定
//			importDto.addLastUpdateDate(TENNIN_CHOSHO_DATA_UPDATE_KEY, lastUpdateData);
//		}

		for (int i = 0; i < infoList.size(); i++) {

			String flg = "0"; // 現社宅判定フラグ
			Map<String, Object> targetMap = infoList.get(i);
			String shainNo = (String) targetMap.get(Skf3020Sc003SharedService.SHAIN_NO_COL); // 社員番号
			
			if (shainNo == null || CheckUtils.isEmpty(shainNo)) {
				// 社員番号が無い場合は登録処理
				errResult = skf3020Sc003SharedService.insertTenninshaInfo(targetMap,flg);
				if (!"".equals(errResult)) {
					return errResult;
				}

			} else {
				// 転任者調書データを取得
				Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository
						.selectByPrimaryKey(shainNo);
				
				// 現社宅判定フラグ設定
				flg = skf3020Sc003SharedService.setGenshatakuFlg(shainNo);
				
				if (tenninshaInfo != null) {
//					if (tenninshaInfo.getShainNo() == null || CheckUtils.isEmpty(tenninshaInfo.getShainNo())) {
//						LogUtils.debugByMsg("転任者調書データ内の指定の社員番号が取得出来なかった。");
//						importDto.setResultMessages(null);
//						ServiceHelper.addErrorResultMessage(importDto, null, "");
//						throwBusinessExceptionIfErrors(importDto.getResultMessages());
//					}

//					String update_data_2 = skf3020Sc003SharedService.getTenninshaInfoForUpdate(firstShainNo);
//					Date updateData = sdFormat.parse(update_data_2);
//					// 排他チェック
//					super.checkLockException(importDto.getLastUpdateDate(TENNIN_CHOSHO_DATA_UPDATE_KEY),
//							updateData);

					// 転任者調書データ更新結果
					errResult = skf3020Sc003SharedService.updateTenninshaInfo(tenninshaInfo, targetMap, flg);
					if (!"".equals(errResult)) {
						return errResult;
					}

				} else {
					//転任者調書データが存在しない場合、登録処理を行う。
					errResult = skf3020Sc003SharedService.insertTenninshaInfo(targetMap,flg);
					if (!"".equals(errResult)) {
						return errResult;
					}
				}
			}
		}

		return errResult;
	}

}
