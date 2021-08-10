/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetNyutaikyoYoteiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetNyutaikyoYoteiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinUpdateDateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetNyutaikyoYoteiInfoExpRepository;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002InitDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002InitService extends SkfServiceAbstract<Skf2040Sc002InitDto> {

	private String sTrue = "true";
	private String sFalse = "false";

	@Autowired
	private SkfBatchUtils skfBatchUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtiles;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2040Sc002SharedService skf2040Sc002ShareService;
	@Autowired
	Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;
	@Autowired
	Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository;
	@Autowired
	Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExpRepository skf2040Sc002GetBihinHenkyakuShinseiApplStatusExpRepository;
	@Autowired
	Skf2040Sc002GetBihinUpdateDateExpRepository skf2040Sc002GetBihinUpdateDateExpRepository;
	@Autowired
	Skf2040Sc002GetNyutaikyoYoteiInfoExpRepository skf2040Sc002GetNyutaikyoYoteiInfoExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc002InitDto index(Skf2040Sc002InitDto initDto) throws Exception {

		// タイトル設定
		if (FunctionIdConstant.R0105.equals(initDto.getApplId())) {
			// 備品返却申請からの遷移
			// 退居（自動車の保管場所返還）届（備品返却確認）
			initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC002_TITLE2);
		} else {
			// 退居（自動車の保管場所返還）届
			initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC002_TITLE);
		}

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2040_SC002);
		// セッション情報引き渡し
		skf2040Sc002ShareService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2040Sc002ShareService.clearMenuScopeSessionBean();

		// 画面内容の設定
		if (setDisplayData(initDto)) {
			// 「添付資料」欄の更新を行う
			List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
			attachedFileList = refreshHeaderAttachedFile(initDto.getApplNo(), attachedFileList);
			initDto.setAttachedFileList(attachedFileList);
		} else {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			setInitializeError(initDto);
		}

		return initDto;
	}

	/**
	 * 「添付資料」欄の更新を行う
	 * 
	 * @param initDto
	 */
	private List<Map<String, Object>> refreshHeaderAttachedFile(String applNo,
			List<Map<String, Object>> attachedFileList) {

		// 添付ファイルを取得し、セッションに保存
		attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean, applNo,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		return attachedFileList;
	}

	/**
	 * 画面情報の設定を行う
	 * 
	 * @param initDto
	 * @return true=表示OK、false=表示NG
	 */
	private boolean setDisplayData(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;

		// 申請書類履歴取得
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		applHistoryList = getApplHistoryList(initDto.getApplNo());

		// 取得できなかった場合エラー
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			returnValue = false;
			return returnValue;
		}
		// 申請書類履歴情報を画面に設定
		initDto.setShainNo(applHistoryList.get(0).getShainNo());
		initDto.setHdnApplShainNo(applHistoryList.get(0).getShainNo());
		initDto.setApplHistoryDate(applHistoryList.get(0).getApplDate());

		// 添付書類有無が取得できた場合は設定
		if (NfwStringUtils.isNotEmpty(applHistoryList.get(0).getApplTacFlg())) {
			initDto.setApplTacFlg(applHistoryList.get(0).getApplTacFlg());
		}

		// 申請状況を設定
		initDto.setApplStatus(applHistoryList.get(0).getApplStatus());
		initDto.setApplStatusText(changeApplStatusText(applHistoryList.get(0).getApplStatus()));

		// 排他チェック用の日付設定
		if (FunctionIdConstant.R0105.equals(initDto.getApplId())) {
			// 備品返却申請からの遷移
			// 申請書類履歴（退居届）の最終更新日付保持
			LogUtils.debugByMsg("更新日時" + applHistoryList.get(0).getUpdateDate());
			initDto.addLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY_BIHIN,
					applHistoryList.get(0).getUpdateDate());

			// 備品申請の取得
			Skf2040Sc002GetBihinUpdateDateExp record = new Skf2040Sc002GetBihinUpdateDateExp();
			record = skf2040Sc002GetBihinUpdateDateExpRepository.getBihinUpdateDate(initDto.getApplNo());
			if (record != null) {
				// 備品申請の最終更新日付保持
				LogUtils.debugByMsg("更新日時" + record.getUpdateDate());
				initDto.addLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_BIHIN, record.getUpdateDate());
			}

		} else {
			// 退居（自動車の保管場所返還）届
			LogUtils.debugByMsg("更新日時" + applHistoryList.get(0).getUpdateDate());
			initDto.addLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY_TAIKYO,
					applHistoryList.get(0).getUpdateDate());
		}

		// データ連携用の排他制御用更新日を取得
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkageMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(initDto.getShainNo());
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2040SC002, dateLinkageMap);

		// コメントボタンの設定
		String commetFlg = setInputComment(initDto.getApplNo());
		if (NfwStringUtils.isNotEmpty(commetFlg)) {
			initDto.setCommentViewFlg(commetFlg);
		}
		if (CheckUtils.isEqual(initDto.getApplStatus(), CodeConstant.STATUS_SHONIN1)) {
			List<SkfCommentUtilsGetCommentInfoExp> commentInfo = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
			commentInfo = skfCommentUtils.getCommentInfo(CodeConstant.C001, initDto.getApplNo(),
					initDto.getApplStatus());
			if (commentInfo != null && commentInfo.size() > 0) {
				String commentNote = commentInfo.get(0).getCommentNote();
				initDto.setCommentNote(commentNote);
			}
		}

		// コントロール制御
		initDto.setApplId(applHistoryList.get(0).getApplId());
		switch (initDto.getApplId()) {
		case FunctionIdConstant.R0105:
			// ◆備品返却希望

			// （レポートは非表示）
			initDto.setTaikyoViewFlg(sFalse);
			// アコーディオン初期表示状態（閉じる）
			initDto.setLevelOpen(sFalse);
			// （添付資料欄は非表示）
			initDto.setTenpViewFlg(sFalse);
			// 社宅の状態は非表示
			initDto.setShatakuJotaiViewFlg(sFalse);
			// 返却情報欄の表示
			initDto.setHenkyakuInfoViewFlg(sTrue);
			// 返却備品項目の表示
			initDto.setBihinViewFlg(sTrue);
			// 返却備品なしフラグはありに設定
			initDto.setHenkyakuBihinNothing(sFalse);

			// 備品情報の表示
			if (!getBihinInfoMain(initDto)) {
				returnValue = false;
				return returnValue;
			}

			// ボタン制御
			// ◆備品返却希望
			switch (initDto.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
				// 申請状況が「審査中」
				// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】→PTN_A
				// 【PDFダウンロードボタン：非表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_A", sFalse, initDto);
				break;
			default:
				// 申請状況が上記以外
				// 【全ボタン非表示】→PTN_F
				skf2040Sc002ShareService.setButtonVisible("PTN_F", sFalse, initDto);
				break;
			}

			break;

		case FunctionIdConstant.R0103:
			// ◆退居（自動車の保管場所返還）届

			// 退居届テーブルから退居情報を取得
			Skf2040TTaikyoReport taikyoRepDt = skf2040Sc002ShareService.getTaikyoReport(initDto.getApplNo());
			// 取得できなかった場合は戻り値をfalse
			if (taikyoRepDt == null) {
				returnValue = false;
				return returnValue;
			}

			// 社宅管理の提示データを取得
			Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = skf2040Sc002ShareService
					.getTeijiDataInfo(initDto.getShainNo(), initDto.getApplNo());
			// 提示番号の取得
			if (teijiDataInfo != null && teijiDataInfo.getTeijiNo() >= 0) {
				initDto.setTeijiNo(teijiDataInfo.getTeijiNo());
			} else {
				initDto.setTeijiNo(CodeConstant.LONG_ZERO);
			}

			// レポート表示
			initDto.setTaikyoViewFlg(sTrue);
			// アコーディオン初期表示
			initDto.setLevelOpen(sTrue);
			// 帳票情報の設定
			skf2040Sc002ShareService.setReportInfo(initDto, taikyoRepDt);
			// 添付資料欄表示
			initDto.setTenpViewFlg(sTrue);
			// 返却情報欄の表示
			initDto.setHenkyakuInfoViewFlg(sTrue);
			// 返却備品項目の非表示
			initDto.setBihinViewFlg(sFalse);
			// 返却備品があるかどうか
			initDto.setHenkyakuBihinNothing(sFalse);

			// 申請状況が「審査中」のみ備品情報を表示する
			if (CodeConstant.STATUS_SHINSACHU.equals(initDto.getApplStatus())) {

				// 社宅の状態は非表示
				initDto.setShatakuJotaiViewFlg(sFalse);

				initDto.setTeijiBtnViewFlag(false);

				// 備品情報の表示
				if (!getBihinInfoMain(initDto)) {
					// 返却情報欄の表示
					initDto.setHenkyakuInfoViewFlg(sFalse);
					// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】→PTN_C
					// 【PDFダウンロードボタン：表示】
					skf2040Sc002ShareService.setButtonVisible("PTN_C", sTrue, initDto);
					// 提示ボタン
					initDto.setBtnPresentDisabeld(sTrue);
					// 備品情報の表示エラーは警告表示で継続
					ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.I_SKF_1006,
							"返却備品情報が取得できませんでした。必要に応じて社宅管理台帳から返却備品", "メンテナンス");
					initDto.setTeijiBtnViewFlag(true);
					return returnValue;
				}

			} else {
				// 申請状況が「審査中」以外

				// 返却備品があるかどうか
				initDto.setHenkyakuBihinNothing(sTrue);

				// 社宅退居区分が設定されていない場合は戻り値をfalse
				if (NfwStringUtils.isEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
					returnValue = false;
					return returnValue;
				} else {
					// 社宅退居区分を設定
					initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn());
				}

				// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
				if (CodeConstant.SHATAKU_CHUSHAJO_WO_TAIKYO_HENKAN.equals(initDto.getShatakuTaikyoKbn())
						|| CodeConstant.SHATAKU_WO_TAIKYO.equals(initDto.getShatakuTaikyoKbn())) {

					// 返却情報欄の表示
					initDto.setHenkyakuInfoViewFlg(sTrue);
					// 申請状況が「審査中」以外は「社宅の状態」を表示する
					initDto.setShatakuJotaiViewFlg(sTrue);
					// 「社宅の状態」の設定
					if (NfwStringUtils.isNotEmpty(taikyoRepDt.getShatakuJotai())) {
						initDto.setShatakuJotai(taikyoRepDt.getShatakuJotai());
					}
				} else {
					// 返却情報欄の表示
					initDto.setHenkyakuInfoViewFlg(sFalse);
				}
			}

			// ボタン制御
			setButtonControl(initDto, teijiDataInfo, applHistoryList.get(0));
			break;
		}

		// 表示正常終了
		return returnValue;

	}

	/**
	 * ボタン制御(退居届の場合）
	 * 
	 * @param initDto
	 * @param teijiDataInfo
	 */
	private void setButtonControl(Skf2040Sc002InitDto initDto, Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo,
			Skf2040Sc002GetApplHistoryInfoExp applHistoryInfo) {

		// ◆退居（自動車の保管場所返還）届
		switch (initDto.getApplStatus()) {
		case CodeConstant.STATUS_SHINSACHU:
			// 申請状況が「審査中」

			if (sTrue.equals(initDto.getHenkyakuBihinNothing())) {
				// 備品返却なし
				// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】→PTN_E
				// 【PDFダウンロードボタン：表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_E", sTrue, initDto);	
				
				//退居届が出されている社宅に対して未処理の「変更」データが存在するか確認。存在した場合警告文を出して処理終了
				boolean checkValue = checkShatakuChangeFlag(initDto);
				if(checkValue){
					break;
				}
							
			} else {
				// 備品返却あり
				// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】→PTN_C
				// 【PDFダウンロードボタン：表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_C", sTrue, initDto);
				
				//退居届が出されている社宅に対して未処理の「変更」データが存在するか確認。存在した場合警告文を出して処理終了
				boolean checkValue = checkShatakuChangeFlag(initDto);
				if(checkValue){
					break;
				}
								
				// 社宅管理の提示データが作成完了か判定し、提示ボタン活性化
				if (teijiDataInfo != null && teijiDataInfo.getTeijiNo() > 0) {
					// 社宅提示データの備品提示ステータスが作成完了されていない場合
					if (NfwStringUtils.isNotEmpty(teijiDataInfo.getCreateCompleteKbn())) {
						switch (teijiDataInfo.getCreateCompleteKbn()) {
						case CodeConstant.MI_SAKUSEI:
						case CodeConstant.SHATAKU_SAKUSEI_SUMI:
							// 未作成、社宅提示作成完了までは提示不可で継続
							// 承認ボタン
							initDto.setBtnApproveDisabled(sTrue);
							// 提示ボタン
							initDto.setBtnPresentDisabeld(sTrue);
							// 提示データ一覧ボタン表示
							initDto.setTeijiBtnViewFlag(true);
							ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, "提示データを確認",
									"（備品提示データが作成完了されていません。）");
							break;
						}
					}
				} else {
					// 社宅提示データが取得できなかった場合
					// 承認ボタン
					initDto.setBtnApproveDisabled(sTrue);
					// 提示ボタン
					initDto.setBtnPresentDisabeld(sTrue);
					// 提示不可で中断
					ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, "提示データを確認",
							"（社宅提示データが取得できませんでした。）");
				}
			}
			break;
		case CodeConstant.STATUS_SHONIN1:
		case CodeConstant.STATUS_SHONIN2:
		case CodeConstant.STATUS_SHONIN3:
		case CodeConstant.STATUS_SHONIN4:
			// 申請状況が「承認中」（備品情報は非表示）

			// 備品返却の書類管理番号から状態を取得
			String bihinHenkyakuStatus = getBihinHenkyakuApplStatus(initDto.getApplNo());

			if (CodeConstant.STATUS_SHINSACHU.equals(bihinHenkyakuStatus)) {
				// 備品返却が「審査中」の場合
				// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】→PTN_E
				// 【PDFダウンロードボタン：表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_E", sTrue, initDto);
			} else {
				// 備品返却が「審査中」以外の場合
				// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】→PTN_B
				// 【PDFダウンロードボタン：表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_B", sTrue, initDto);
			}

			// 承認者１がログインユーザー名と同じだった場合
			Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
			if (CheckUtils.isEqual(applHistoryInfo.getAgreName1(), loginUserInfo.get("userName"))) {
				// 【全ボタン非表示】→"PTN_F"【PDFダウンロードボタン：表示】
				skf2040Sc002ShareService.setButtonVisible("PTN_F", sTrue, initDto);
				// initDto.setBtnApproveDisabled(sTrues);
			}

			break;
		default:
			// 申請状況が上記以外(備品情報は非表示)
			// 【全ボタン非表示】→"PTN_F"
			skf2040Sc002ShareService.setButtonVisible("PTN_F", sFalse, initDto);
			break;
		}

	}

	/**
	 * 備品返却申請の申請書類番号取得
	 * 
	 * @param hdnBihinHenkyakuApplNo
	 * @return
	 */
	private String getBihinHenkyakuApplStatus(String hdnBihinHenkyakuApplNo) {

		String bihinHenkyakuStatus = CodeConstant.DOUBLE_QUOTATION;

		Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExp record = new Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExp();
		record.setCompanyCd(CodeConstant.C001);
		record.setApplNo(hdnBihinHenkyakuApplNo);
		Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExp reStatus = new Skf2040Sc002GetBihinHenkyakuShinseiApplStatusExp();
		reStatus = skf2040Sc002GetBihinHenkyakuShinseiApplStatusExpRepository.getBihinHenkyakuApplStatus(record);

		if (reStatus != null && NfwStringUtils.isNotEmpty(reStatus.getApplStatus())) {
			bihinHenkyakuStatus = reStatus.getApplStatus();
		}

		return bihinHenkyakuStatus;
	}

	/**
	 * 初期表示エラー時の処理
	 * 
	 * @param initDto
	 */
	private void setInitializeError(Skf2040Sc002InitDto initDto) {
		// 更新処理を行わせないようボタンを使用不可に
		// 承認ボタン
		initDto.setBtnApproveDisabled(sTrue);
		// 提示ボタン
		initDto.setBtnPresentDisabeld(sTrue);
		ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
	}

	/**
	 * 備品情報の表示
	 * 
	 * @param initDto
	 * @param teijiDataInfo
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfoMain(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;
		switch (initDto.getApplId()) {
		case FunctionIdConstant.R0105:
			// 備品返却申請の場合
			returnValue = getBihinInfoHenkyaku(initDto);
			break;
		case FunctionIdConstant.R0103:
			// 退居（自動車の保管場所返還）届の場合
			returnValue = getBihinInfoTaikyo(initDto);
			break;
		}
		return returnValue;
	}

	/**
	 * 備品情報の表示（退居（自動車の保管場所返還）届の場合）
	 * 
	 * @param initDto
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfoTaikyo(Skf2040Sc002InitDto initDto) {

		// 退居届テーブルから退居情報を取得
		Skf2040TTaikyoReport taikyoRepDt = skf2040Sc002ShareService.getTaikyoReport(initDto.getApplNo());

		// 取得できなかった、社宅退居区分が設定されていない場合は戻り値をfalse
		if (taikyoRepDt == null || NfwStringUtils.isEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
			return false;
		} else {
			// 社宅退居区分を設定
			initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn());
			initDto.setShatakuKanriNo(taikyoRepDt.getShatakuNo());
			initDto.setShatakuRoomKanriNo(taikyoRepDt.getRoomKanriNo());
		}

		// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
		if (CodeConstant.SHATAKU_CHUSHAJO_WO_TAIKYO_HENKAN.equals(initDto.getShatakuTaikyoKbn())
				|| CodeConstant.SHATAKU_WO_TAIKYO.equals(initDto.getShatakuTaikyoKbn())) {

			// 「返却備品なしフラグ」を立てる
			initDto.setHenkyakuBihinNothing(sTrue);
			// 返却備品項目の非表示表示
			initDto.setBihinViewFlg(sFalse);

			long roomNo = taikyoRepDt.getRoomKanriNo();// 部屋管理番号
			long shatakuNo = taikyoRepDt.getShatakuNo();// 社宅管理番号

			// 社宅管理提示データ、提示備品データ、備品項目設定から備品情報を取得
			List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = skf2040Sc002ShareService.getHenkyakuBihinInfo(
					initDto.getApplNo(), initDto.getShainNo(), shatakuNo, roomNo, initDto.getTeijiNo());

			// 備品貸与区分と社宅の部屋備品備付区分から、最新の備品返却区分を設定する。
			if (henkyakuDt != null && henkyakuDt.size() > 0) {
				List<Map<String, Object>> henkyakuList = new ArrayList<Map<String, Object>>();
				henkyakuList = skf2040Sc002ShareService.updateBihinReturnKbn(initDto.getShainNo(), initDto.getApplNo(),
						shatakuNo, roomNo, henkyakuDt);
				// リストが設定できなかった場合は特に何もしていない
				if (henkyakuList != null) {
					initDto.setHenkyakuList(henkyakuList);
					// 表示項目の設定
					skf2040Sc002ShareService.setBihinData(initDto, henkyakuList);
					for (Map<String, Object> list : henkyakuList) {
						String bihinLentStatusKbn = list.get("bihinLentStatusKbn").toString();
						// 返却対象備品(会社保有かレンタル)がある場合は「返却備品なしフラグ」を折る
						if (NfwStringUtils.isNotEmpty(bihinLentStatusKbn)
								&& (CodeConstant.BIHIN_HENKYAKU_KBN_KAISHA_HOYU_HENKYAKU.equals(bihinLentStatusKbn)
										|| CodeConstant.BIHIN_HENKYAKU_KBN_RENTAL_HENKYAKU
												.equals(bihinLentStatusKbn))) {
							// 返却対象備品がある場合は「返却備品なしフラグ」を折る
							initDto.setHenkyakuBihinNothing(sFalse);
							break;
						}
					}
				}
			}

			// 返却備品項目の表示
			initDto.setBihinViewFlg(sTrue);

			// 備品返却申請の書類管理番号を備品返却申請テーブルより取得する
			String BihinHenkyakuApplNo = getBihinHenkyakuShinseiApplNo(initDto.getShainNo(), initDto.getApplNo(),
					shatakuNo, roomNo);
			if (NfwStringUtils.isEmpty(BihinHenkyakuApplNo)) {
				// 取得できなかった場合は戻り値をfalse
				return false;
			}
			// Hidden項目に備品返却申請の書類管理番号を保存する
			initDto.setHdnBihinHenkyakuApplNo(BihinHenkyakuApplNo);

			// 申請書類履歴取得
			List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
			applHistoryList = getApplHistoryList(initDto.getHdnBihinHenkyakuApplNo());

			// 備品返却申請の申請書類履歴レコードの更新日を保持
			if (applHistoryList != null && applHistoryList.size() > 0) {
				// 排他チェック用の日付設定
				LogUtils.debugByMsg("更新日時" + applHistoryList.get(0).getUpdateDate());
				initDto.addLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY_BIHIN,
						applHistoryList.get(0).getUpdateDate());
			}

			// 備品申請情報の取得
			Skf2040Sc002GetBihinUpdateDateExp record = new Skf2040Sc002GetBihinUpdateDateExp();
			record = skf2040Sc002GetBihinUpdateDateExpRepository
					.getBihinUpdateDate(initDto.getHdnBihinHenkyakuApplNo());
			if (record != null) {
				// 備品申請の最終更新日付のキャッシュキー
				LogUtils.debugByMsg("更新日時" + record.getUpdateDate());
				initDto.addLastUpdateDate(Skf2040Sc002SharedService.KEY_LAST_UPDATE_DATE_BIHIN, record.getUpdateDate());
			}

			// 返却情報表示
			initDto.setHenkyakuInfoViewFlg(sTrue);
			// 社宅の状態の設定
			initDto.setShatakuJotaiViewFlg(sTrue);
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getShatakuJotai())) {
				initDto.setShatakuJotai(taikyoRepDt.getShatakuJotai());
			}

			// 備品がある場合は、タイトル変更。返却立会希望日、連絡先を表示
			if (sFalse.equals(initDto.getHenkyakuBihinNothing())) {

				// 退居（自動車の保管場所返還）届（備品返却確認）
				initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC002_TITLE2);

				// 提示データ取得
				Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = skf2040Sc002ShareService
						.getTeijiDataInfo(initDto.getShainNo(), initDto.getApplNo());

				// 備品がある場合の表示項目の設定
				skf2040Sc002ShareService.setBihinHenkyakuDisp(initDto, taikyoRepDt, teijiDataInfo);
			}
		} else {
			// 退居届情報の退居する社宅区分が3：駐車場のみの場合
			// 「返却備品なしフラグ」を立てる
			initDto.setHenkyakuBihinNothing(sTrue);
			// 返却情報欄の非表示
			initDto.setHenkyakuInfoViewFlg(sFalse);
		}
		return true;
	}

	/**
	 * 備品情報の表示（備品返却申請の場合）
	 * 
	 * @param initDto
	 * @param teijiDataInfo
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfoHenkyaku(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;

		// 備品返却申請テーブルから退居届書類管理番号の取得
		Skf2050TBihinHenkyakuShinsei bihinDt = skf2040Sc002ShareService.getBihinHenkyakuShinsei(initDto.getApplNo());

		// 取得できなかった、社宅退居区分が設定されていない場合は戻り値をfalse
		String taikyoRepApplNo = CodeConstant.DOUBLE_QUOTATION;
		if (bihinDt == null) {
			returnValue = false;
			return returnValue;
		} else {
			// 退居届書類管理番号の設定
			taikyoRepApplNo = bihinDt.getTaikyoApplNo();
		}

		// 退居（自動車の保管場所変換）届テーブルから退居情報を取得
		Skf2040TTaikyoReport taikyoRepDt = skf2040Sc002ShareService.getTaikyoReport(taikyoRepApplNo);

		// 取得できなかった、社宅退居区分が設定されていない場合は戻り値をfalse
		if (taikyoRepDt == null || NfwStringUtils.isEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
			returnValue = false;
			return returnValue;
		} else {
			// 社宅退居区分を設定
			initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn());
			initDto.setShatakuKanriNo(taikyoRepDt.getShatakuNo());
			initDto.setShatakuRoomKanriNo(taikyoRepDt.getRoomKanriNo());
		}

		// 社宅管理の提示データを取得
		Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = skf2040Sc002ShareService.getTeijiDataInfo(initDto.getShainNo(),
				taikyoRepApplNo);
		// 提示番号の取得
		if (teijiDataInfo != null && teijiDataInfo.getTeijiNo() >= 0) {
			initDto.setTeijiNo(teijiDataInfo.getTeijiNo());
		} else {
			initDto.setTeijiNo(CodeConstant.LONG_ZERO);
		}

		// 退居届情報の社宅退居区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
		if (CodeConstant.SHATAKU_CHUSHAJO_WO_TAIKYO_HENKAN.equals(initDto.getShatakuTaikyoKbn())
				|| CodeConstant.SHATAKU_WO_TAIKYO.equals(initDto.getShatakuTaikyoKbn())) {

			long shatakuNo = taikyoRepDt.getShatakuNo();// 社宅管理番号
			long roomNo = taikyoRepDt.getRoomKanriNo();// 部屋管理番号

			// 社宅管理提示データ、提示備品データ、備品項目設定から備品情報を取得
			List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = skf2040Sc002ShareService.getHenkyakuBihinInfo(
					taikyoRepApplNo, initDto.getShainNo(), shatakuNo, roomNo, initDto.getTeijiNo());

			// 備品情報を取得できた場合
			if (henkyakuDt != null && henkyakuDt.size() > 0) {
				// 備品貸与区分と社宅の部屋備品備付区分から、最新の備品返却区分を設定する。
				List<Map<String, Object>> henkyakuList = skf2040Sc002ShareService
						.updateBihinReturnKbn(initDto.getShainNo(), taikyoRepApplNo, shatakuNo, roomNo, henkyakuDt);
				// リストが設定できなかった場合はfalseでリターン
				if (henkyakuList == null) {
					returnValue = false;
					return returnValue;
				} else {
					initDto.setHenkyakuList(henkyakuList);
					// 表示項目の設定
					skf2040Sc002ShareService.setBihinData(initDto, henkyakuList);
				}
			}

			// 返却備品項目表示
			initDto.setHenkyakuInfoViewFlg(sTrue);
			// 返却情報の設定
			skf2040Sc002ShareService.setBihinHenkyakuDisp(initDto, taikyoRepDt, teijiDataInfo);
		}
		return returnValue;
	}

	/**
	 * 備品返却申請の書類管理番号を備品返却申請テーブルより取得
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param shatakuNo
	 * @param roomNo
	 * @return 備品返却申請の書類管理番号
	 */
	private String getBihinHenkyakuShinseiApplNo(String shainNo, String applNo, long shatakuNo, long roomNo) {

		String BihinHenkyakuApplNo = CodeConstant.DOUBLE_QUOTATION;

		Skf2040Sc002GetBihinHenkyakuShinseiApplNoExp dtBihinApplNo = new Skf2040Sc002GetBihinHenkyakuShinseiApplNoExp();
		Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpParameter bihinApplNoParam = new Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpParameter();
		bihinApplNoParam.setCompanyCd(CodeConstant.C001);
		bihinApplNoParam.setShainNo(shainNo);
		bihinApplNoParam.setTaikyoApplNo(applNo);
		bihinApplNoParam.setShatakuNo(shatakuNo);
		bihinApplNoParam.setRoomKanriNo(roomNo);
		dtBihinApplNo = skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository
				.getBihinHenkyakuShinseiApplNo(bihinApplNoParam);

		if (dtBihinApplNo != null) {
			BihinHenkyakuApplNo = dtBihinApplNo.getApplNo();
		}

		return BihinHenkyakuApplNo;
	}

	/**
	 * コメントボタンの設定
	 * 
	 * @param initDto
	 */
	private String setInputComment(String applNo) {
		// コメント一覧取得
		List<SkfCommentUtilsGetCommentListExp> commentList = skfCommentUtils.getCommentList(CodeConstant.C001, applNo,
				null);
		// コメントがあれば「コメント表示」ボタンを表示
		String commentViewFlag = CodeConstant.NONE;
		if (commentList == null || commentList.size() <= 0) {
			// コメントが無ければ非表示
			commentViewFlag = sFalse;
		} else {
			// コメントがあれば表示
			commentViewFlag = sTrue;
		}
		return commentViewFlag;

	}

	/**
	 * 申請書履歴から社員番号を取得
	 * 
	 * @param userId ユーザーID
	 * @param applId 申請書類番号
	 * @return 取得結果
	 */
	public List<Skf2040Sc002GetApplHistoryInfoExp> getApplHistoryList(String applNo) {

		// DB検索処理
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		Skf2040Sc002GetApplHistoryInfoExpParameter param = new Skf2040Sc002GetApplHistoryInfoExpParameter();
		param.setApplNo(applNo);
		applHistoryList = skf2040Sc002GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("社員情報情報のリスト：" + applHistoryList.toString());

		return applHistoryList;
	}

	/**
	 * 申請状況の情報取得
	 * 
	 * @param applStatus
	 * @return
	 */
	private String changeApplStatusText(String applStatus) {

		String applStatusText = "";

		Map<String, BaseCodeEntity> applStatusMap = codeCacheUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		// 申請状況をコードから汎用コードに変更
		if (applStatus != null) {
			BaseCodeEntity baseCodeEntity = new BaseCodeEntity();
			baseCodeEntity = applStatusMap.get(applStatus);
			applStatusText = baseCodeEntity.getCodeName();
		}

		return applStatusText;
	}
	
	
	// 現行障害：入退居区分が「変更」のデータ処理残留してしまう問題対応  add start /
	/**
	 * 「変更」の入退居予定データおよび提示データが登録されていないかのチェック
	 * 
	 * @param applStatus
	 * @return
	 */
	private boolean checkShatakuChangeFlag(Skf2040Sc002InitDto initDto) {
		
		LogUtils.debugByMsg("「変更」の入退居予定データおよび提示データが登録されていないかのチェック:" );
		boolean checkValue = false;

		//　退居する社宅に対して未処理の「変更」ステータスの内容がないか確認する。
		Skf2040Sc002GetNyutaikyoYoteiInfoExpParameter param = new Skf2040Sc002GetNyutaikyoYoteiInfoExpParameter();
		param.setShainNo(initDto.getShainNo());
		param.setShatakuKanriNo(initDto.getShatakuKanriNo());
		param.setShatakuRoomKanriNo(initDto.getShatakuRoomKanriNo());
		Skf2040Sc002GetNyutaikyoYoteiInfoExp result = new Skf2040Sc002GetNyutaikyoYoteiInfoExp();
		result = skf2040Sc002GetNyutaikyoYoteiInfoExpRepository.getNyutaikyoYoteiInfo(param);
		
		LogUtils.debugByMsg("「変更」の入退居予定データおよび提示データが登録されていないかのチェック:"  + result);
		
		boolean warningFlg = false;
		//取得できた場合は、警告フラグをたてる
		if(result != null){
			warningFlg = true; 
		}
		
		if(warningFlg){
			//警告フラグがtrueの場合
			initDto.setNyutaikyoYoteiBtnViewFlag(false);
			initDto.setTeijiBtnViewFlag(true);
			
			if(!(CodeConstant.LAND_CREATE_KBN_SAKUSEI_SUMI.equals(result.getLandCreateKbn()))){	
				//台帳作成区分が「作成済み」以外の場合警告文設定
				
				if(CodeConstant.YES.equals(result.getTeijiCreateKbn())){	
					//「変更」社宅の提示データが作成されている場合
					// 提示不可で継続
					// 承認ボタン
					initDto.setBtnApproveDisabled(sTrue);
					// 提示ボタン
					initDto.setBtnPresentDisabeld(sTrue);
					// 提示データ一覧ボタン表示
					initDto.setTeijiBtnViewFlag(true);
					ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, "退居届が出されている社宅について未処理の「変更」提示データが存在しています。提示データ登録画面にて処理を完了",
							"（処理を完了してから、退居届の承認または返却備品の提示に進んでください。）");	
					checkValue = true;
					
				}else{
					//「変更」社宅の提示データが未作成の場合
					//提示不可で継続
					// 承認ボタン
					initDto.setBtnApproveDisabled(sTrue);
					// 提示ボタン
					initDto.setBtnPresentDisabeld(sTrue);
					//入退居予定一覧ボタン表示
					initDto.setNyutaikyoYoteiBtnViewFlag(true);
					ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, "退居届が出されている社宅について未処理の「変更」入退居予定データが存在しています。提示データを作成し処理を完了",
							"（処理を完了してから、退居届の承認または返却備品の提示に進んでください。）");
					checkValue = true;							
				}	
			}			
		}

		LogUtils.debugByMsg("「変更」の入退居予定データおよび提示データが登録されていないかのチェック:"  + checkValue);
		return checkValue;
	}
	// 現行障害：入退居区分が「変更」のデータ処理残留してしまう問題対応  add end /
}
