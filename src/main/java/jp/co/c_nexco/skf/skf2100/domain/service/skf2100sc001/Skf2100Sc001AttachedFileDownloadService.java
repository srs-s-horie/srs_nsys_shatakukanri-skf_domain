/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc001.Skf2100Sc001AttachedFileDownloadDto;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)添付ファイルダウンロードサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001AttachedFileDownloadService extends SkfServiceAbstract<Skf2100Sc001AttachedFileDownloadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	
	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2100Sc001AttachedFileDownloadDto dlDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("モバイルルーター補足資料ダウンロード");

		// 添付資料番号
		String attachedNo = dlDto.getAttachedNo();

		// ファイル名
		String fileName = "";
		byte[] fileData = null;

		// 添付ファイル情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		if (attachedFileList == null || attachedFileList.size() == 0) {
			ServiceHelper.addErrorResultMessage(dlDto, null, MessageIdConstant.E_SKF_1067, "添付資料");
		}

		// ダウンロード対象ファイルデータを設定する
		int target = Integer.parseInt(attachedNo);
		Map<String, Object> attachedFileMap = attachedFileList.get(target);
		fileName = attachedFileMap.get("attachedName").toString();
		fileData = (byte[]) attachedFileMap.get("fileStream");
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog(fileName, CodeConstant.C001, FunctionIdConstant.SKF2100_SC001);

		dlDto.setFileData(fileData);
		dlDto.setUploadFileName(fileName);
		dlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		return dlDto;
	}

}
