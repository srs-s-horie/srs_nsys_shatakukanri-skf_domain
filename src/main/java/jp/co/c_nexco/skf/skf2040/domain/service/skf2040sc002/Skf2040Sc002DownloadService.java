/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002DownloadDto;

/**
 * Skf2040Sc002DownloadService 添付資料リンククリック時の処理
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002DownloadService extends BaseServiceAbstract<Skf2040Sc002DownloadDto> {

	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2040Sc002DownloadDto dlDto) throws Exception {

		// 添付資料番号
		String attachedNo = dlDto.getAttachedNo();

		// ファイル名
		String fileName = "";
		byte[] fileData = null;

		// 添付ファイル情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		if (attachedFileList == null || attachedFileList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(dlDto, null, MessageIdConstant.E_SKF_1067, "添付資料");
		}

		// ダウンロード対象ファイルデータを設定する
		int target = Integer.parseInt(attachedNo);
		Map<String, Object> attachedFileMap = attachedFileList.get(target);
		fileName = attachedFileMap.get("attachedName").toString();
		fileData = (byte[]) attachedFileMap.get("fileStream");

		dlDto.setFileData(fileData);
		dlDto.setUploadFileName(fileName);
		dlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		return dlDto;

	}

}
