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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuRoomBihinDataExpRepository;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002InitDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002InitService extends BaseServiceAbstract<Skf2040Sc002InitDto> {

	private String sTrue = "true";
	private String sFalse = "false";

	// 最終更新日付のキャッシュキー
	public static final String KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history";

	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private Skf2040Sc002SharedService skf2040Sc002ShareService;
	@Autowired
	Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;

	@Autowired
	Skf2040Sc002GetShatakuRoomBihinDataExpRepository skf2040Sc002GetShatakuRoomBihinDataExpRepository;
	@Autowired
	Skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository skf2040Sc002GetBihinHenkyakuShinseiApplNoExpRepository;

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
		initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC002_TITLE);
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// セッション情報引き渡し
		skf2040Sc002ShareService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2040Sc002ShareService.clearMenuScopeSessionBean();

		// 画面内容の設定
		if (setDisplayData(initDto)) {
			// 添付ファイルの設定
			skf2040Sc002ShareService.refreshHeaderAttachedFile(initDto);
		} else {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			setInitializeError(initDto);
		}

		return initDto;
	}

	/**
	 * 「添付資料」欄の更新を行う
	 */
	private void refreshHeaderAttachedFile() {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * 画面情報の設定を行う
	 * 
	 * @param initDto
	 * @return true=表示OK、false=表示NG
	 */
	private boolean setDisplayData(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;
		String applTacFlg = CodeConstant.DOUBLE_QUOTATION;

		// 申請書類履歴取得
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		applHistoryList = getApplHistoryList(initDto.getApplNo());

		// 取得できなかった場合エラー
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
		}

		// 排他チェック用の日付設定
		LogUtils.debugByMsg("更新日時" + applHistoryList.get(0).getUpdateDate());
		initDto.addLastUpdateDate(KEY_LAST_UPDATE_DATE, applHistoryList.get(0).getUpdateDate());

		// 申請書類履歴情報を画面の隠し項目に設定
		initDto.setHdnApplShainNo(applHistoryList.get(0).getShainNo());

		// 添付書類有無が取得できた場合は設定
		if (NfwStringUtils.isNotEmpty(applHistoryList.get(0).getApplTacFlg())) {
			initDto.setApplTacFlg(applHistoryList.get(0).getApplTacFlg());
		}

		// 申請状況を設定
		initDto.setApplStatus(applHistoryList.get(0).getApplStatus());
		initDto.setApplStatusText(changeApplStatusText(applHistoryList.get(0).getApplStatus()));

		// 承認2済以降の場合
		if (CodeConstant.STATUS_SHONIN1.equals(applHistoryList.get(0).getApplStatus())) {
			// コメント入力欄の設定
			setInputComment(initDto);
		}

		// コントロール制御
		initDto.setApplId(applHistoryList.get(0).getApplId());
		switch (initDto.getApplId()) {
		case FunctionIdConstant.R0105:
			// ◆備品返却希望

			// （レポートは非表示）
			initDto.setTaikyoViewFlag(sFalse);
			// （添付ファイルは非表示）
			initDto.setTenpViewFlag(sFalse);

			// 備品情報の表示
			if (!getBihinInfoMain(initDto)) {
				returnValue = false;
				return returnValue;
			}

			// ボタン制御
			switch (initDto.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
				// 申請状況が「審査中」
				// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】【PDFダウンロードボタン：非表示】
				skf2040Sc002ShareService.setButtonVisible(true, false, false, false, false, false, initDto);
				break;
			case CodeConstant.STATUS_HANSYUTSU_ZUMI:
				// 申請状況が「搬出済」
				// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】【PDFダウンロードボタン：非表示】
				skf2040Sc002ShareService.setButtonVisible(false, true, false, false, false, false, initDto);
				break;
			default:
				// 申請状況が上記以外
				// 【全ボタン非表示】
				skf2040Sc002ShareService.setButtonVisible(false, false, false, false, false, false, initDto);
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

			Skf2040Sc002GetShatakuInfoExp shatakuInfo = new Skf2040Sc002GetShatakuInfoExp();
			shatakuInfo = skf2040Sc002ShareService.getShatakuInfo(taikyoRepDt.getShatakuNo(), initDto.getShainNo());
			// 取得できなかった場合は戻り値をfalse
			if (shatakuInfo == null) {
				returnValue = false;
				return returnValue;
			}
			// レポート表示
			initDto.setTaikyoViewFlag(sTrue);
			// 帳票情報の設定
			skf2040Sc002ShareService.setReportInfo(initDto, taikyoRepDt, shatakuInfo);

			// 添付ファイル表示
			initDto.setTenpViewFlag(sTrue);
			// 返却備品があるかどうか
			initDto.setIsHenkyakuBihinNothing(sFalse);

			// 申請状況が「審査中」のみ備品情報を表示する
			if (CodeConstant.STATUS_SHINSACHU.equals(initDto.getApplStatus())) {
				// 備品情報の表示
				if (!getBihinInfoMain(initDto)) {
					// 備品情報の表示エラー警告表示
					initDto.setWarningDispFlag(sTrue);
				}
			} else {
				// 申請状況が「審査中」以外

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
					// 「社宅の状態」を表示する
					if (NfwStringUtils.isEmpty(taikyoRepDt.getShatakuJotai())) {
						initDto.setShatakuJyotai(taikyoRepDt.getShatakuJotai());
					}
				}
			}

			// ボタン制御
			switch (initDto.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
				// 申請状況が「審査中」

				if (sFalse.equals(initDto.getIsHenkyakuBihinNothing())) {
					// 備品返却なし
					// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】【PDFダウンロードボタン：表示】
					skf2040Sc002ShareService.setButtonVisible(false, true, true, true, true, true, initDto);
				} else {
					// 備品返却あり
					// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】
					skf2040Sc002ShareService.setButtonVisible(true, false, true, true, true, true, initDto);

					// 社宅管理の提示データが作成完了か判定し、提示ボタン活性化
					Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = skf2040Sc002ShareService
							.getTeijiDataInfo(initDto.getShainNo(), initDto.getApplNo());

					// 社宅提示データが取得できなかった場合
					if (teijiDataInfo == null) {
						// 承認ボタン
						initDto.setBtnApproveDisabled(sTrue);
						// 提示ボタン
						initDto.setBtnPresentDisabeld(sTrue);
						// 提示不可で中断
						ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078,
								"社宅管理システムで提示データを確認", "（社宅提示データが取得できませんでした。）");

					}
				}

				break;
			case CodeConstant.STATUS_SHONIN1:
			case CodeConstant.STATUS_SHONIN2:
			case CodeConstant.STATUS_SHONIN3:
			case CodeConstant.STATUS_SHONIN4:
				// 申請状況が「承認中」（備品情報は非表示）

				// 【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】【PDFダウンロードボタン：非表示】
				skf2040Sc002ShareService.setButtonVisible(false, true, false, false, false, false, initDto);
				break;
			default:
				// 申請状況が上記以外(備品情報は非表示)
				// 【全ボタン非表示】
				skf2040Sc002ShareService.setButtonVisible(false, false, false, false, false, false, initDto);
				break;
			}
			break;
		}

		// 表示正常終了
		return returnValue;

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
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfoMain(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;
		switch (initDto.getApplId()) {
		case FunctionIdConstant.R0103:
			// 退居届の場合
			returnValue = getBihinInfo_Taikyo(initDto);
			break;
		case FunctionIdConstant.R0105:
			// 備品返却申請の場合
			returnValue = getBihinInfo_Henkyaku(initDto);
			break;
		}
		return returnValue;
	}

	/**
	 * 備品情報の表示（退居届の場合）
	 * 
	 * @param initDto
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfo_Taikyo(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;
		// 退居届テーブルから退居情報を取得
		Skf2040TTaikyoReport taikyoRepDt = skf2040Sc002ShareService.getTaikyoReport(initDto.getApplNo());

		// 取得できなかった、社宅退居区分が設定されていない場合は戻り値をfalse
		if (taikyoRepDt == null || NfwStringUtils.isEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
			returnValue = false;
			return returnValue;
		} else {
			// 社宅退居区分を設定
			initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn());
		}

		// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
		if (CodeConstant.SHATAKU_CHUSHAJO_WO_TAIKYO_HENKAN.equals(initDto.getShatakuTaikyoKbn())
				|| CodeConstant.SHATAKU_WO_TAIKYO.equals(initDto.getShatakuTaikyoKbn())) {

			// 「返却備品なしフラグ」を立てる
			initDto.setIsHenkyakuBihinNothing(sTrue);

			long roomNo = taikyoRepDt.getRoomKanriNo();// 部屋管理番号
			long shatakuNo = taikyoRepDt.getShatakuNo();// 社宅管理番号

			// 社宅管理提示データ、提示備品データ、備品項目設定から備品情報を取得
			List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = skf2040Sc002ShareService
					.getHenkyakuBihinInfo(initDto.getApplNo(), initDto.getShainNo(), shatakuNo, roomNo);

			// 備品貸与区分と社宅の部屋備品備付区分から、最新の備品返却区分を設定する。
			if (henkyakuDt != null && henkyakuDt.size() > 0) {
				List<Map<String, Object>> henkyakuList = new ArrayList<Map<String, Object>>();
				henkyakuList = skf2040Sc002ShareService.updateBihinReturnKbn(initDto, shatakuNo, roomNo, henkyakuDt);
				initDto.setHenkyakuList(henkyakuList);

				for (int i = 0; i <= henkyakuDt.size(); i++) {
					String bihinLentStatusKbn = henkyakuDt.get(i).getBihinLentStatusKbn();
					// 返却対象備品(会社保有かレンタル)がある場合は「返却備品なしフラグ」を折る
					if (NfwStringUtils.isEmpty(bihinLentStatusKbn)) {
						if (CodeConstant.BIHIN_HENKYAKU_KBN_KAISHA_HOYU_HENKYAKU.equals(bihinLentStatusKbn)
								|| CodeConstant.BIHIN_HENKYAKU_KBN_RENTAL_HENKYAKU.equals(bihinLentStatusKbn)) {
							// 返却対象備品がある場合は「返却備品なしフラグ」を折る
							initDto.setIsHenkyakuBihinNothing(sFalse);
							break;
						}
					}
				}
			}

			// 備品返却申請の書類管理番号を備品返却申請テーブルより取得する
			String BihinHenkyakuApplNo = getBihinHenkyakuShinseiApplNo(initDto.getShainNo(), initDto.getApplNo(),
					shatakuNo, roomNo);
			if (NfwStringUtils.isEmpty(BihinHenkyakuApplNo)) {
				// 取得できなかった場合は戻り値をfalse
				returnValue = false;
				return returnValue;
			}
			// Hidden項目に備品返却申請の書類管理番号を保存する
			initDto.setHdnBihinHenkyakuApplNo(BihinHenkyakuApplNo);

			// 社宅の状態の取得
			if (NfwStringUtils.isEmpty(taikyoRepDt.getShatakuJotai())) {
				initDto.setShatakuJyotai(taikyoRepDt.getShatakuJotai());
			}

			// 備品がある場合は、返却立会希望日、連絡先を表示
			if (sTrue.equals(initDto.getIsHenkyakuBihinNothing())) {
				// 備品がある場合の表示項目の設定
				skf2040Sc002ShareService.setBihinHenkyakuDisp(initDto, taikyoRepDt);
			}
		} else {
			// 退居届情報の退居する社宅区分が3：駐車場のみの場合
			// 「返却備品なしフラグ」を立てる
			initDto.setIsHenkyakuBihinNothing(sTrue);
		}
		return returnValue;
	}

	/**
	 * 備品情報の表示（備品返却申請の場合）
	 * 
	 * @param initDto
	 * @return true：正常、false：異常
	 */
	private boolean getBihinInfo_Henkyaku(Skf2040Sc002InitDto initDto) {
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

		// 退居届テーブルから退居情報を取得
		Skf2040TTaikyoReport taikyoRepDt = skf2040Sc002ShareService.getTaikyoReport(taikyoRepApplNo);

		// 取得できなかった、社宅退居区分が設定されていない場合は戻り値をfalse
		if (taikyoRepDt == null || NfwStringUtils.isEmpty(taikyoRepDt.getShatakuTaikyoKbn())) {
			returnValue = false;
			return returnValue;
		} else {
			// 社宅退居区分を設定
			initDto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn());
		}

		// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
		if (CodeConstant.SHATAKU_CHUSHAJO_WO_TAIKYO_HENKAN.equals(initDto.getShatakuTaikyoKbn())
				|| CodeConstant.SHATAKU_WO_TAIKYO.equals(initDto.getShatakuTaikyoKbn())) {

			long roomNo = taikyoRepDt.getRoomKanriNo();// 部屋管理番号
			long shatakuNo = taikyoRepDt.getShatakuNo();// 社宅管理番号

			// 社宅管理提示データ、提示備品データ、備品項目設定から備品情報を取得
			List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = skf2040Sc002ShareService
					.getHenkyakuBihinInfo(taikyoRepApplNo, initDto.getShainNo(), shatakuNo, roomNo);

			// 備品情報を取得できた場合
			if (henkyakuDt != null && henkyakuDt.size() > 0) {
				// 備品貸与区分と社宅の部屋備品備付区分から、最新の備品返却区分を設定する。
				List<Map<String, Object>> henkyakuList = skf2040Sc002ShareService.updateBihinReturnKbn(initDto,
						shatakuNo, roomNo, henkyakuDt);
				initDto.setHenkyakuList(henkyakuList);
			}
			// 備品がある場合の表示項目の設定
			skf2040Sc002ShareService.setBihinHenkyakuDisp(initDto, taikyoRepDt);
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

		BihinHenkyakuApplNo = dtBihinApplNo.getApplNo();

		return BihinHenkyakuApplNo;
	}

	/**
	 * コメント入力欄の設定
	 * 
	 * @param initDto
	 */
	private void setInputComment(Skf2040Sc002InitDto initDto) {
		// コメント一覧取得
		List<SkfCommentUtilsGetCommentInfoExp> commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001,
				initDto.getApplNo(), null);
		// コメントがあれば「コメント表示」ボタンを表示
		if (commentList != null && commentList.size() > 0) {
			initDto.setCommentBtnViewFlag(sTrue);
		} else {
			initDto.setCommentBtnViewFlag(sFalse);
		}

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

}
