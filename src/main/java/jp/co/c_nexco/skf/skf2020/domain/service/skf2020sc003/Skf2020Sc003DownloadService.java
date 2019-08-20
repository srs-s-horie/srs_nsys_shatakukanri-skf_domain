package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003DownloadDto;

/**
 * Skf2020Sc003 申請書類承認／修正依頼／通知 承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003DownloadService extends BaseServiceAbstract<Skf2020Sc003DownloadDto> {

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2020Sc003DownloadDto dlDto) throws Exception {
		// 添付資料番号
		String attachedNo = dlDto.getAttachedNo();

		// ファイル名
		String fileName = "";
		byte[] fileData = null;

		// 添付ファイル情報を取得
		List<Map<String, Object>> shatakuAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.SHATAKU_ATTACHED_FILE_SESSION_KEY);
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);

		if (shatakuAttachedFileList == null) {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		if (attachedFileList == null || attachedFileList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(dlDto, null, MessageIdConstant.E_SKF_1067, "添付資料");
		}
		shatakuAttachedFileList.addAll(attachedFileList);

		// ダウンロード対象ファイルデータを設定する
		int target = Integer.parseInt(attachedNo);
		Map<String, Object> attachedFileMap = shatakuAttachedFileList.get(target);
		fileName = attachedFileMap.get("attachedName").toString();
		fileData = (byte[]) attachedFileMap.get("fileStream");

		dlDto.setFileData(fileData);
		dlDto.setUploadFileName(fileName);
		dlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		return dlDto;
	}
}
