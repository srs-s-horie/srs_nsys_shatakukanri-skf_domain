/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002ShareService {

	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;

	/**
	 * セッション情報を取得
	 * 
	 * @param bean
	 */
	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		menuScopeSessionBean = bean;
	}

	/**
	 * セッションの添付資料情報の初期化
	 * 
	 * @param bean
	 */
	public void clearMenuScopeSessionBean() {
		if (menuScopeSessionBean == null) {
			return;
		}
		skfAttachedFileUtiles.clearAttachedFileBySessionData(menuScopeSessionBean,
				SessionCacheKeyConstant.SHATAKU_ATTACHED_FILE_SESSION_KEY);
	}

	/**
	 * 添付資料データを設定します
	 * 
	 * @param fileName
	 * @param file
	 * @param fileSize
	 */
	@SuppressWarnings({ "static-access" })
	protected void addShatakuAttachedFile(String fileName, byte[] file, String fileSize, int attachedNo,
			List<Map<String, Object>> shatakuAttachedFileList) {
		// 添付資料のコレクションをSessionより取得

		// リンクリストチェック
		boolean findFlg = false;
		if (shatakuAttachedFileList != null) {
			for (Map<String, Object> attachedFileMap : shatakuAttachedFileList) {
				if (fileName.equals(attachedFileMap.get("attachedName"))) {
					findFlg = true;
					break;
				}
			}
		} else {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイルリストに無い場合
		if (!findFlg) {
			Map<String, Object> addAttachedFileInfo = new HashMap<String, Object>();

			addAttachedFileInfo.put("attachedNo", attachedNo);

			// 添付資料名
			addAttachedFileInfo.put("attachedName", fileName);
			// ファイルサイズ
			addAttachedFileInfo.put("attachedFileSize", fileSize);
			// 更新日
			addAttachedFileInfo.put("registDate", new Date());
			// 添付資料
			addAttachedFileInfo.put("fileStream", file);
			// 添付ファイルステータス
			// ファイルタイプ
			addAttachedFileInfo.put("fileType", skfAttachedFileUtiles.getFileTypeInfo(fileName));

			shatakuAttachedFileList.add(addAttachedFileInfo);
		}

		return;
	}

}
