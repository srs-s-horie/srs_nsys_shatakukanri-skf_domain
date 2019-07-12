/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetAttachedFileInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetAttachedFileInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetCommentListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplComment;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetApplHistoryInfoByParameterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetAttachedFileInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetCommentListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

/**
 * Skf2010Sc002 申請書類確認の共通処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002SharedService {

	private String companyCd = CodeConstant.C001;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2010Sc002GetApplHistoryInfoByParameterExpRepository skf2010Sc002GetApplHistoryInfoByParameterExpRepository;
	@Autowired
	private Skf2010Sc002GetAttachedFileInfoExpRepository skf2010Sc002GetAttachedFileInfoExpRepository;
	@Autowired
	private Skf2010Sc002GetCommentListExpRepository skf2010Sc002GetCommentListExpRepository;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKey;

	/**
	 * 社宅入居希望等申請情報を取得する
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param name
	 * @param nameKk
	 * @param agencyName
	 * @return
	 */
	public Skf2020TNyukyoChoshoTsuchi getNyukyoChoshoTsuchiInfo(String companyCd, String applNo) {
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoTsuchiInfo = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
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
	public Skf2010Sc002GetApplHistoryInfoByParameterExp getApplHistoryInfo(String applNo) {
		Skf2010Sc002GetApplHistoryInfoByParameterExpParameter param = new Skf2010Sc002GetApplHistoryInfoByParameterExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		// 更新対象の申請情報を取得
		Skf2010Sc002GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc002GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc002GetApplHistoryInfoByParameterExpRepository.getApplHistoryInfoByParameter(param);

		return tApplHistoryData;
	}

	/**
	 * 添付ファイルを取得し、セッションに保存します
	 * 
	 * @param applNo
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttachedFileInfo(String applNo) {
		List<Map<String, Object>> resultAttachedFileList = null;

		resultAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (resultAttachedFileList != null) {
			return resultAttachedFileList;
		}
		// 初期化
		resultAttachedFileList = new ArrayList<Map<String, Object>>();

		// 添付ファイル管理テーブルから添付ファイル情報を取得
		List<Skf2010Sc002GetAttachedFileInfoExp> attachedFileList = new ArrayList<Skf2010Sc002GetAttachedFileInfoExp>();
		Skf2010Sc002GetAttachedFileInfoExpParameter param = new Skf2010Sc002GetAttachedFileInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		attachedFileList = skf2010Sc002GetAttachedFileInfoExpRepository.getAttachedFileInfo(param);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = 0;
			Map<String, Object> attachedFileMap = null;
			for (Skf2010Sc002GetAttachedFileInfoExp attachedFileInfo : attachedFileList) {
				attachedFileMap = new HashMap<String, Object>();
				// 添付資料番号
				attachedFileMap.put("attachedNo", String.valueOf(attachedNo));
				// 添付資料名
				attachedFileMap.put("attachedName", attachedFileInfo.getAttachedName());
				// ファイルサイズ
				attachedFileMap.put("fileSize", attachedFileInfo.getFileSize());
				// 更新日
				String applDate = skfDateFormatUtils.dateFormatFromDate(attachedFileInfo.getApplDate(),
						"yyyy/MM/dd HH:mm:ss.SS");
				attachedFileMap.put("applDate", applDate);
				// 添付資料
				attachedFileMap.put("fileStream", attachedFileInfo.getFileStream());
				// ファイルタイプ
				String fileType = skfAttachedFileUtiles.getFileTypeInfo(attachedFileInfo.getAttachedName());
				attachedFileMap.put("fileType", fileType);

				resultAttachedFileList.add(attachedFileMap);

				attachedNo++;
			}

			// 取得した添付ファイル情報をセッションに保持
			menuScopeSessionBean.put(sessionKey, resultAttachedFileList);
		}
		return resultAttachedFileList;
	}

	/**
	 * 申請コメントの一覧を取得します
	 * 
	 * @param applNo
	 * @param applStatus
	 * @return
	 */
	public List<Skf2010Sc002GetCommentListExp> getApplCommentList(String applNo, String applStatus) {
		List<Skf2010Sc002GetCommentListExp> returnList = new ArrayList<Skf2010Sc002GetCommentListExp>();
		Skf2010Sc002GetCommentListExpParameter param = new Skf2010Sc002GetCommentListExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		if (applStatus != null && !CheckUtils.isEmpty(applStatus)) {
			param.setApplStatus(applStatus);
		}
		returnList = skf2010Sc002GetCommentListExpRepository.getCommentList(param);

		return returnList;
	}

	/**
	 * 申請書類履歴の更新 + 申請書類コメント更新処理メソッド
	 * 
	 * @param applInfo
	 * @return
	 */
	public boolean updateShinseiHistory(Map<String, String> applInfo) {

		// 更新対象の申請情報を取得
		String applNo = applInfo.get("applNo");
		Skf2010Sc002GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc002GetApplHistoryInfoByParameterExp();
		tApplHistoryData = getApplHistoryInfo(applNo);
		// 更新処理
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		// プライマリキー設定
		updateData.setCompanyCd(companyCd);
		updateData.setShainNo(tApplHistoryData.getShainNo());
		updateData.setApplDate(tApplHistoryData.getApplDate());
		updateData.setApplId(tApplHistoryData.getApplId());
		updateData.setApplNo(tApplHistoryData.getApplNo());
		updateData.setApplStatus(applInfo.get("status"));
		// 申請情報履歴更新
		int applHistoryRes = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		if (applHistoryRes <= 0) {
			return false;
		}

		// コメントの更新
		if (!updateCommentInfo(applInfo)) {
			return false;
		}

		return true;
	}

	/**
	 * 申請書類コメントを更新する。
	 * 
	 * @param applInfo
	 * @return
	 */
	private boolean updateCommentInfo(Map<String, String> applInfo) {

		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 承認2済から承認4済は、承認1済にする
		String applStatus = applInfo.get("status");
		if (CodeConstant.STATUS_SHONIN1.compareTo(applStatus) < 0
				&& CodeConstant.STATUS_SHONIN_ZUMI.compareTo(applStatus) > 0) {
			applStatus = CodeConstant.STATUS_SHONIN1;
		}

		// TODO
		// コメントが登録されている、かつ、承認1済～承認済の場合、削除処理をする（承認のコメントを上書きのため）→ルート上こないためいらない？

		// コメントが入力されていない場合、処理を終了
		String comment = applInfo.get("commentNote");
		if (NfwStringUtils.isEmpty(comment)) {
			return true;
		}

		// コメント登録者名を設定
		List<String> tmpNameList = new ArrayList<String>();
		if (loginUserInfoMap.get("affiliation2Name") != null) {
			tmpNameList.add(loginUserInfoMap.get("affiliation2Name"));
		}
		tmpNameList.add(loginUserInfoMap.get("userName"));
		String commentName = String.join("\r\n", tmpNameList); // ログインユーザーの名前を取得
		Date commentDate = new Date();

		// コメントを更新する
		if (comment != null && !CheckUtils.isEmpty(comment)) {
			Skf2010TApplComment skf2010TApplComment = new Skf2010TApplComment();
			skf2010TApplComment.setCompanyCd(companyCd);
			skf2010TApplComment.setApplNo(applInfo.get("applNo"));
			skf2010TApplComment.setApplStatus(applInfo.get("status"));
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

}
