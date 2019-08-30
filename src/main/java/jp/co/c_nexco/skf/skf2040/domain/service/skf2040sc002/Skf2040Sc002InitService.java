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
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
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
	private Skf2040Sc002ShareService skf2040Sc002ShareService;
	@Autowired
	Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;

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

		// 画面内容の設定
		if (setDisplayData(initDto)) {
			// 「添付資料」欄の更新を行う
			refreshHeaderAttachedFile();
		} else {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
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
		applHistoryList = getApplHistoryList(initDto.getShainNo(), initDto.getApplNo());

		// 取得できなかった場合エラー
		if (applHistoryList == null) {
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
		initDto.setApplStatusText(changeApplStatusText(applHistoryList.get(0).getApplStatus()));

		// 添付資料がある場合、添付資料表示処理を行う
		if (SkfCommonConstant.AVAILABLE.equals(applHistoryList.get(0).getApplTacFlg())) {
			List<Map<String, Object>> tmpAttachedFileList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
			attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean, initDto.getApplNo(),
					SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

			if (tmpAttachedFileList != null && tmpAttachedFileList.size() > 0) {
				int defaultAttachedNo = 0;
				for (Map<String, Object> tmpAttachedFileMap : tmpAttachedFileList) {
					skf2040Sc002ShareService.addShatakuAttachedFile(tmpAttachedFileMap.get("attachedName").toString(),
							(byte[]) tmpAttachedFileMap.get("fileStream"),
							tmpAttachedFileMap.get("fileSize").toString(), defaultAttachedNo, attachedFileList);
				}
			}
		}
		// 添付ファイルの設定

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
			initDto.setTaikyoPdfViewFlg(sFalse);
			// （添付ファイルは非表示）
			initDto.setTenpViewFlag(sFalse);

			// 備品情報の表示
			if (!getBihinInfoMain(initDto)) {

			}
			// 初期表示エラー

			// ボタン制御
			// 申請状況が「審査中」【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】
			// 申請状況が「搬入済」【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】
			// 申請状況が上記以外【全ボタン非表示】
			break;
		case FunctionIdConstant.R0103:
			// ◆退居（自動車の保管場所返還）届

			// レポート表示
			// 返却備品があるかどうか

			// 申請状況が「審査中」のみ備品情報を表示する
			// 備品情報の表示
			// 備品情報の表示エラーは警告表示で継続
			// それ以外提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

			// 申請状況が「審査中」以外は「社宅の状態」を表示する
			// 退居届テーブルから退居情報を取得
			// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
			// データテーブルに項目名、データを格納する列を作成
			// 社宅の状態の取得
			// 社宅の状態

			// ボタン制御
			// 返却備品なし
			// 申請状況が【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

			// 返却備品あり
			// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

			break;
		}

		// 表示正常終了
		return returnValue;

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

		return returnValue;
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
			initDto.setCommentViewFlag(sTrue);
		} else {
			initDto.setCommentViewFlag(sFalse);
		}

	}

	/**
	 * 申請書履歴から社員番号を取得
	 * 
	 * @param userId ユーザーID
	 * @param applId 申請書類番号
	 * @return 取得結果
	 */
	public List<Skf2040Sc002GetApplHistoryInfoExp> getApplHistoryList(String shainNo, String applNo) {

		// DB検索処理
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		Skf2040Sc002GetApplHistoryInfoExpParameter param = new Skf2040Sc002GetApplHistoryInfoExpParameter();
		param.setShainNo(shainNo);
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
