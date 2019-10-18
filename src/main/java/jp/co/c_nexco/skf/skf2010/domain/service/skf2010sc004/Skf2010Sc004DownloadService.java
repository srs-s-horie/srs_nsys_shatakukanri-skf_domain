package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc004;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc004.Skf2010Sc004DownloadDto;

/**
 * Skf2010Sc004 申請内容表示/引戻し添付資料ダウンロード処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc004DownloadService extends BaseServiceAbstract<Skf2010Sc004DownloadDto> {

	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;
	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	@SuppressWarnings("unchecked")
	@Override
	public Skf2010Sc004DownloadDto index(Skf2010Sc004DownloadDto dlDto) throws Exception {
		// 操作ログの出力
		skfOperationLogUtils.setAccessLog("添付資料ダウンロード", companyCd, FunctionIdConstant.SKF2010_SC004);

		// 添付資料番号
		String attachedNo = dlDto.getAttachedNo();

		// ファイル名
		String fileName = "";
		byte[] fileData = null;

		// 添付ファイル情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);

		// 申請情報の取得を行う
		if (attachedFileList == null || attachedFileList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(dlDto, null, MessageIdConstant.E_SKF_1067, "添付資料");
			throwBusinessExceptionIfErrors(dlDto.getResultMessages());
			return dlDto;
		}

		for (Map<String, Object> attachedFileMap : attachedFileList) {
			String nowAttachedNo = attachedFileMap.get("attachedNo").toString();
			if (attachedNo.equals(nowAttachedNo)) {
				fileName = attachedFileMap.get("attachedName").toString();
				fileData = (byte[]) attachedFileMap.get("fileStream");
				break;
			}
		}

		dlDto.setFileData(fileData);
		dlDto.setUploadFileName(fileName);
		dlDto.setViewPath(NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH);

		return dlDto;
	}
}
