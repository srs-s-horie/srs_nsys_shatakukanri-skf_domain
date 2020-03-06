/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetShatakuAttachedFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetShatakuAttachedFileExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplComment;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetShatakuAttachedFileExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc002common.Skf2010Sc002CommonDto;

/**
 * Skf2010Sc002 申請書類確認画面の共通処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002SharedService {

	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2010Sc002GetApplHistoryInfoByParameterExpRepository skf2010Sc002GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	Skf2010Sc002GetShatakuAttachedFileExpRepository skf2010Sc002GetShatakuAttachedFileExpRepository;

	// 申請書類履歴の最終更新日付のキャッシュキー
	protected static final String KEY_LAST_UPDATE_DATE_HISTORY = "skf2010_t_appl_history";

	// 承認者更新フラグ
	private String agreNameUpdate = "1";

	/**
	 * セッション情報を取得
	 * 
	 * @param bean
	 */
	protected void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		menuScopeSessionBean = bean;
	}

	/**
	 * セッションの添付資料情報の初期化
	 * 
	 * @param bean
	 */
	protected void clearMenuScopeSessionBean() {
		if (menuScopeSessionBean == null) {
			return;
		}
		skfAttachedFileUtils.clearAttachedFileBySessionData(menuScopeSessionBean,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
	}

	/**
	 * 社宅入居希望等申請情報を取得する
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return
	 */
	protected Skf2020TNyukyoChoshoTsuchi getNyukyoChoshoTsuchiInfo(String applNo) {

		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchiInfo = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(CodeConstant.C001);
		key.setApplNo(applNo);
		nyukyoChoshoTsuchiInfo = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		return nyukyoChoshoTsuchiInfo;
	}

	/**
	 * 申請書類種別IDを取得
	 * 
	 * @param applNo
	 * @return
	 */
	protected Skf2010Sc002GetApplHistoryInfoByParameterExp getApplHistoryInfoByParameter(String applNo) {
		Skf2010Sc002GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc002GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc002GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc002GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc002GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		return tApplHistoryData;
	}

	/**
	 * 申請書類履歴の更新 + 申請書類コメント更新処理メソッド
	 * 
	 * @param applInfoMap
	 * @param lastUpdateDate
	 * @param agreNameUpdateFlg 承認者更新フラグ 0:更新しない 1：更新する
	 * @return
	 */
	protected String updateShinseiHistory(Map<String, String> applInfoMap, Date lastUpdateDate,
			String agreNameUpdateFlg) {

		String result = CodeConstant.NONE;

		// 更新対象の申請情報を取得
		String applNo = applInfoMap.get("applNo");
		Skf2010Sc002GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc002GetApplHistoryInfoByParameterExp();
		tApplHistoryData = getApplHistoryInfoByParameter(applNo);

		// 楽観的排他チェック（申請情報履歴）
		if (!CheckUtils.isEqual(tApplHistoryData.getUpdateDate(), lastUpdateDate)) {
			// 排他チェックエラー
			return "exclusiveError";
		}

		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 更新処理
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		// プライマリキー設定
		updateData.setCompanyCd(CodeConstant.C001);
		updateData.setShainNo(tApplHistoryData.getShainNo());
		updateData.setApplDate(tApplHistoryData.getApplDate());
		updateData.setApplId(tApplHistoryData.getApplId());
		updateData.setApplNo(tApplHistoryData.getApplNo());
		// 更新項目
		updateData.setApplStatus(applInfoMap.get("status"));
		// 提示ボタンから来た場合は、承認者名を更新する
		if (agreNameUpdate.equals(agreNameUpdateFlg)) {
			updateData.setAgreName1(loginUserInfoMap.get("userName"));
		}
		// 申請情報履歴更新
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {
			result = "updateError";
			return result;
		}

		// コメントの更新
		if (!updateCommentInfo(applInfoMap, loginUserInfoMap)) {
			result = "updateError";
			return result;
		}

		return result;
	}

	/**
	 * 申請書類コメントを更新する。
	 * 
	 * @param applInfoMap
	 * @param loginUserInfoMap ログインユーザー情報
	 * @return
	 */
	private boolean updateCommentInfo(Map<String, String> applInfoMap, Map<String, String> loginUserInfoMap) {

		// 承認2済から承認4済は、承認1済にする
		String applStatus = applInfoMap.get("status");
		if (CodeConstant.STATUS_SHONIN1.compareTo(applStatus) < 0
				&& CodeConstant.STATUS_SHONIN_ZUMI.compareTo(applStatus) > 0) {
			applStatus = CodeConstant.STATUS_SHONIN1;
		}

		// TODO
		// コメントが登録されている、かつ、承認1済～承認済の場合、削除処理をする（承認のコメントを上書きのため）→ルート上こないためいらない？

		// コメントが入力されていない場合、処理を終了
		String comment = applInfoMap.get("commentNote");
		if (NfwStringUtils.isEmpty(comment)) {
			return true;
		}

		// コメント登録者名を設定
		List<String> tmpNameList = new ArrayList<String>();
		// 承認者権限かチェック
		boolean isApplyUser = false;
		switch (loginUserInfoMap.get("roleId")) {
		case CodeConstant.SKF_220:
		case CodeConstant.SKF_230:
		case CodeConstant.SKF_900:
			isApplyUser = true;
			break;
		}
		// 承認者権限の場合のみ「室、チーム又は課」名称を表示する
		if (isApplyUser && NfwStringUtils.isNotEmpty(loginUserInfoMap.get("affiliation2Name"))) {
			tmpNameList.add(loginUserInfoMap.get("affiliation2Name"));
		}
		tmpNameList.add(loginUserInfoMap.get("userName"));
		String commentName = String.join("\r\n", tmpNameList); // ログインユーザーの名前を取得
		Date commentDate = new Date();

		// コメントを更新する
		if (comment != null && !CheckUtils.isEmpty(comment)) {
			Skf2010TApplComment skf2010TApplComment = new Skf2010TApplComment();
			skf2010TApplComment.setCompanyCd(CodeConstant.C001);
			skf2010TApplComment.setApplNo(applInfoMap.get("applNo"));
			skf2010TApplComment.setApplStatus(applInfoMap.get("status"));
			skf2010TApplComment.setCommentDate(commentDate);
			skf2010TApplComment.setCommentName(commentName);
			skf2010TApplComment.setCommentNote(comment);
			int insCommentRes = skf2010TApplCommentRepository.insertSelective(skf2010TApplComment);
			// 取得できなかった場合処理終了
			if (insCommentRes <= 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * コメントエラーチェック
	 * 
	 * @param comment
	 * @return エラー結果
	 * @throws UnsupportedEncodingException
	 */
	protected boolean validateComment(String comment) throws UnsupportedEncodingException {
		// コメント欄の入力文字数が4000バイト以上ならエラー
		if (NfwStringUtils.isNotEmpty(comment) && CheckUtils.isMoreThanByteSize(comment.trim(), 4000)) {
			return false;
		}
		return true;
	}

	/**
	 * 退居届情報取得
	 * 
	 * @param applNo
	 * @return
	 */
	protected Skf2040TTaikyoReport getTaikyoReportInfo(String applNo) {

		Skf2040TTaikyoReport taikyoRepDt = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey setKey = new Skf2040TTaikyoReportKey();
		setKey.setCompanyCd(CodeConstant.C001);
		setKey.setApplNo(applNo);
		taikyoRepDt = skf2040TTaikyoReportRepository.selectByPrimaryKey(setKey);
		return taikyoRepDt;
	}

	/**
	 * 社宅添付ファイル情報の取得
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param dto
	 */
	/**
	 * @param shainNo
	 * @param applNo
	 * @param dto
	 */
	protected void setAttachedFileList(String shainNo, String applNo, Skf2010Sc002CommonDto dto) {

		// 添付ファイルの設定
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();

		attachedFileList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, dto.getApplNo(),
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		// 添付ファイル情報設定（社宅添付ファイルと通常添付ファイルを別に保持）
		dto.setAttachedFileList(attachedFileList);

	}

	/**
	 * 条件を指定して社宅添付ファイル情報を取得
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @param shatakuAttachedDtList
	 * @return
	 */
	private List<Skf2010Sc002GetShatakuAttachedFileExp> getshatakuAttachedDt(String shainNo, String nyutaikyoKbn,
			String applNo, List<Skf2010Sc002GetShatakuAttachedFileExp> shatakuAttachedDtList) {

		Skf2010Sc002GetShatakuAttachedFileExpParameter param = new Skf2010Sc002GetShatakuAttachedFileExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		shatakuAttachedDtList = skf2010Sc002GetShatakuAttachedFileExpRepository.getShatakuAttachedFile(param);

		return shatakuAttachedDtList;
	}

}
