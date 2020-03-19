package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006RevisionDto;

/**
 * Skf2010Sc006 申請書類承認／修正依頼／通知 修正依頼処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006RevisionService extends BaseServiceAbstract<Skf2010Sc006RevisionDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;

	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2010Sc006RevisionDto reDto) throws Exception {

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("修正依頼", CodeConstant.C001, FunctionIdConstant.SKF2010_SC006);

		// 添付ファイル有無を取得
		String applTacFlag = "0";
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			applTacFlag = "1";
		}

		// 修正依頼理由入力チェック
		if (!skf2010Sc006SharedService.checkReason(reDto)) {
			reDto.setAttachedFileList(attachedFileList);
			return reDto;
		}

		// 申請情報の取得を行う
		String applStatus = CodeConstant.STATUS_SASHIMODOSHI;
		Map<String, String> errMap = new HashMap<String, String>();
		String applNo = reDto.getApplNo();
		Date lastUpdateDate = reDto.getLastUpdateDate(skf2010Sc006SharedService.KEY_LAST_UPDATE_DATE_HISTORY);
		boolean res = skf2010Sc006SharedService.saveApplInfo(applStatus, applTacFlag, reDto, lastUpdateDate, errMap);
		if (!res) {
			if (NfwStringUtils.isNotEmpty(errMap.get("value"))) {
				if (NfwStringUtils.isNotEmpty(errMap.get("value"))) {
					ServiceHelper.addErrorResultMessage(reDto, null, errMap.get("error"), errMap.get("value"),
							errMap.get("value2"));
				} else {
					ServiceHelper.addErrorResultMessage(reDto, null, errMap.get("error"), errMap.get("value"));
				}
			} else {
				ServiceHelper.addErrorResultMessage(reDto, null, errMap.get("error"));
			}

			return reDto;
		}

		// 添付ファイル管理テーブル更新処理
		boolean commentRes = skf2010Sc006SharedService.updateAttachedFileInfo(applNo, reDto.getShainNo(),
				attachedFileList);
		if (!commentRes) {
			ServiceHelper.addErrorResultMessage(reDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(reDto.getResultMessages());
			return reDto;
		}

		// 修正依頼通知メール送信
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("applNo", reDto.getApplNo());
		applInfo.put("applId", reDto.getApplId());
		applInfo.put("applShainNo", reDto.getShainNo());

		String commentNote = reDto.getCommentNote();

		// メールの記載URLは「申請状況一覧画面」
		String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";

		skfMailUtils.sendApplTsuchiMail(CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI, applInfo, commentNote,
				CodeConstant.NONE, applInfo.get("applShainNo"), CodeConstant.NONE, urlBase);

		// 社宅管理データ連携処理実行（オンラインバッチ）
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc006SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData == null) {
			ServiceHelper.addErrorResultMessage(reDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(reDto.getResultMessages());
			return reDto;
		}
		String applId = tApplHistoryData.getApplId();
		String shainNo = tApplHistoryData.getShainNo();
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc006SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, applStatus,
				applId, FunctionIdConstant.SKF2010_SC006);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC006);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(reDto, resultBatch);
			skfRollBackExpRepository.rollBack();
		}

		// 画面遷移

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2030);
		reDto.setTransferPageInfo(tpi);
		return reDto;
	}

}
