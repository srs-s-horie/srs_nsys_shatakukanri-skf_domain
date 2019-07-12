package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006UpdateDto;

/**
 * Skf2010Sc006 申請書類承認／修正依頼／通知 承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006UpdateService extends BaseServiceAbstract<Skf2010Sc006UpdateDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKey;

	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2010Sc006UpdateDto updDto) throws Exception {

		// 添付ファイル有無を取得
		String applTacFlag = "0";
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			applTacFlag = "1";
		}

		// 申請情報の取得を行う
		Map<String, String> errMap = new HashMap<String, String>();
		String applNo = updDto.getApplNo();
		String comment = updDto.getCommentNote();
		boolean res = skf2010Sc006SharedService.updateApplStatus(companyCd, applNo, comment, applTacFlag, errMap);
		if (!res) {
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
		}
		// TODO 添付ファイル管理テーブル更新処理
		updateAttachedFileInfo(applNo, updDto.getShainNo(), attachedFileList, updDto);

		// TODO 社宅管理データ連携処理実行（オンラインバッチ）

		// 次のステータスを設定する

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		updDto.setTransferPageInfo(nextPage);

		return updDto;
	}

	/**
	 * @param applNo
	 * @param shainNo
	 * @param attachedFileList
	 * @param updDto
	 */
	@Transactional
	private void updateAttachedFileInfo(String applNo, String shainNo, List<Map<String, Object>> attachedFileList,
			Skf2010Sc006UpdateDto updDto) {
		// 添付ファイル管理テーブルを更新する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				Skf2010TAttachedFile insertData = new Skf2010TAttachedFile();
				insertData = mappingTAttachedFile(attachedFileMap, applNo, shainNo);
				skf2010Sc006SharedService.insertAttachedFileInfo(insertData);
			}
		}

	}

	private Skf2010TAttachedFile mappingTAttachedFile(Map<String, Object> attachedFileMap, String applNo,
			String shainNo) {
		Skf2010TAttachedFile resultData = new Skf2010TAttachedFile();

		// 会社コード
		resultData.setCompanyCd(companyCd);
		// 社員番号
		resultData.setShainNo(shainNo);
		// 登録日時
		Date nowDate = new Date();
		resultData.setApplDate(nowDate);
		// 申請番号
		resultData.setApplNo(applNo);
		// 添付番号
		resultData.setAttachedNo(attachedFileMap.get("attachedNo").toString());
		// 添付資料名
		resultData.setAttachedName(attachedFileMap.get("attachedName").toString());
		// ファイル
		resultData.setFileStream((byte[]) attachedFileMap.get("fileStream"));
		// ファイルサイズ
		resultData.setFileSize(attachedFileMap.get("fileSize").toString());

		return resultData;
	}

}
