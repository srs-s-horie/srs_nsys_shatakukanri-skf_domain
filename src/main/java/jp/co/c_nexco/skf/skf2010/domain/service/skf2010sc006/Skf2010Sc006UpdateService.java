package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetApplHistoryInfoByParameterExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006UpdateDto;

/**
 * Skf2010Sc006 申請書類承認／修正依頼／通知 承認処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006UpdateService extends SkfServiceAbstract<Skf2010Sc006UpdateDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	

	private String companyCd = CodeConstant.C001;

	private String sessionKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY;
	private String sessionConflictKey = SessionCacheKeyConstant.COMMON_ATTACHED_FILE_CONFLICT_SESSION_KEY;

	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf2010Sc006UpdateDto updDto) throws Exception {

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("承認", CodeConstant.C001, FunctionIdConstant.SKF2010_SC006);
		
		//複数タブによる添付ファイルセッションチェック		
		boolean checkResults = skfAttachedFileUtils.attachedFileSessionConflictCheck(menuScopeSessionBean,updDto.getApplNo());
		
		//申請書管理番号が一致しない
		if (!checkResults) {			
			// セッション情報の削除
			menuScopeSessionBean.remove(sessionKey);
			menuScopeSessionBean.remove(sessionConflictKey);
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.I_SKF_1005,"セッション情報が異なっ","ブラウザを閉じて操作をやり直","");
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
			return updDto;
		}	
		
		// 添付ファイル有無を取得
		String applTacFlag = "0";
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (attachedFileList != null && attachedFileList.size() > 0) {
			applTacFlag = "1";
		}

		// 申請情報の取得を行う
		Map<String, String> errMap = new HashMap<String, String>();
		String applId = updDto.getApplId();
		String applNo = updDto.getApplNo();
		String shainNo = updDto.getShainNo();
		String comment = updDto.getCommentNote();
		Date lastUpdateDate = updDto.getLastUpdateDate(skf2010Sc006SharedService.KEY_LAST_UPDATE_DATE_HISTORY);
		boolean res = skf2010Sc006SharedService.updateApplStatus(companyCd, applNo, comment, applTacFlag,
				lastUpdateDate, errMap);
		if (!res) {
			if (errMap.get("error") != null) {
				if (errMap.get("value") != null) {
					ServiceHelper.addErrorResultMessage(updDto, null, errMap.get("error"), errMap.get("value"));
				} else {
					ServiceHelper.addErrorResultMessage(updDto, null, errMap.get("error"));
				}
			} else {
				ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1073);
			}
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
			return updDto;
		}
		// 添付ファイル管理テーブル更新処理
		boolean commentRes = skf2010Sc006SharedService.updateAttachedFileInfo(applNo, updDto.getShainNo(),
				attachedFileList);
		if (!commentRes) {
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
			return updDto;
		}

		// 社宅管理データ連携処理実行（オンラインバッチ）
		Skf2010Sc006GetApplHistoryInfoByParameterExp tApplHistoryData = new Skf2010Sc006GetApplHistoryInfoByParameterExp();
		tApplHistoryData = skf2010Sc006SharedService.getApplHistoryInfo(applNo);
		if (tApplHistoryData == null) {
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
			return updDto;
		}
		String afterApplStatus = tApplHistoryData.getApplStatus();
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2010Sc006SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, afterApplStatus,
				applId, FunctionIdConstant.SKF2010_SC006);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2010SC006);
		if (resultBatch != null) {
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(updDto, resultBatch);
			skfRollBackExpRepository.rollBack();
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
			return updDto;
		}

		//メール送信処理を行う
		String mailKbn = "";
		String sendGroupId = "";
		Map<String, String> applInfo = new HashMap<String, String>();
		// 申請情報設定
		applInfo.put("applId", tApplHistoryData.getApplId());
		applInfo.put("applNo", applNo);
		applInfo.put("applStatus", tApplHistoryData.getApplStatus());
		applInfo.put("applShainNo", tApplHistoryData.getShainNo());
		String nowApplStatus = tApplHistoryData.getApplStatus();
		if (CodeConstant.STATUS_SHONIN_ZUMI.equals(nowApplStatus)) {
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			// 承認完了通知の場合のみ
			// メール送付
			String annai = CodeConstant.NONE;
			String sendUser = applInfo.get("applShainNo");
			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
			skfMailUtils.sendApplTsuchiMail(mailKbn, applInfo, comment, annai, sendUser, sendGroupId, urlBase);
		}

		
		//画面遷移前にデータの初期化を行う
		FormHelper.removeFormBean(FunctionIdConstant.SKF2040_SC002);
		
		// 次のステータスを設定する
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005, "init");
		nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
		updDto.setTransferPageInfo(nextPage);

		
		
		return updDto;
	}

}
