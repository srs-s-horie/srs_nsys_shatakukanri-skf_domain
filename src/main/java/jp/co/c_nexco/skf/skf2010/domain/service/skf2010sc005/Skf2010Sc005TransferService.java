package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005TransferDto;

/**
 * Skf2010Sc005 承認一覧更新処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005TransferService extends SkfServiceAbstract<Skf2010Sc005TransferDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;

	@Override
	public BaseDto index(Skf2010Sc005TransferDto transDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("確認", CodeConstant.C001, FunctionIdConstant.SKF2010_SC005);

		String applNo = transDto.getApplNo();
		String applId = transDto.getApplId();
		// 申請状況
		String applStatus = transDto.getSendApplStatus();
		// 更新前申請状況
		String defaultApplStatus = applStatus;

		String applShainNo = transDto.getApplShainNo();

		String shonin1 = transDto.getShonin1();
		String shonin2 = transDto.getShonin2();

		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// ページ遷移先は「申請状況一覧」
		TransferPageInfo tpi = null;
		Map<String, Object> attribute = new HashMap<String, Object>();

		// ステータスが「承認中」である場合のみ、二重承認チェックを行う
		if (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHONIN1)) {
			// 「承認者名1」「承認者名2」いずれかが、セッションのログインユーザ名と同じ場合（システム管理者は除く）
			if ((CheckUtils.isEqual(loginUserInfo.get("userName"), shonin1)
					|| CheckUtils.isEqual(loginUserInfo.get("userName"), shonin2))) {
				switch (applId) {
				case FunctionIdConstant.R0104:
					// ページ遷移先は「備品希望申請（アウトソース用）」
					tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2030_SC002);
					break;
				case FunctionIdConstant.R0105:
					// ページ遷移先は「備品返却申請（アウトソース用）」
					tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2050_SC002);
					break;
				default:
					// 申請書類承認／差戻し／通知
					tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC006);
				}
				attribute.put("applNo", applNo);
				attribute.put("applId", applId);
				attribute.put("applStatus", applStatus);
				tpi.setTransferAttributes(attribute);
				transDto.setTransferPageInfo(tpi);
				return transDto;
			}
		}

		// ログインユーザが承認権限を持つ申請書類であるかどうかを確認
		boolean agreeAuthority = skf2010Sc005SharedService.isAgreeAuthority(applId, loginUserInfo.get("roleId"),
				applStatus);

		Map<String, String> statusMap = new HashMap<String, String>();

		// 「申請書類ID」「ステータス」の組み合わせにより、ステータスの値を更新
		boolean updateResult = skf2010Sc005SharedService.updateStatusShinsachu(applId, applNo, agreeAuthority,
				applStatus, statusMap, transDto);
		if (!updateResult) {
			if (transDto.getResultMessages() == null) {
				ServiceHelper.addErrorResultMessage(transDto, null, MessageIdConstant.E_SKF_1075);
			}
			throwBusinessExceptionIfErrors(transDto.getResultMessages());
			return transDto;
		}

		// 更新後の申請状況を設定
		if (NfwStringUtils.isNotEmpty(statusMap.get("applStatus"))) {
			applStatus = statusMap.get("applStatus");
		}

		// 承認一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		param = skf2010Sc005SharedService.setParam(transDto);
		tApplHistoryData = skf2010Sc005SharedService.searchApplList(param);
		// 最新の検索結果をセッションに保存
		menuScopeSessionBean.put(SessionCacheKeyConstant.SKF2010SC005_SEARCH_RESULT_SESSION_KEY, tApplHistoryData);
		// グリッド表示（リストテーブル）作成
		List<Map<String, Object>> dispList = skf2010Sc005SharedService.createListTable(tApplHistoryData, transDto);
		transDto.setLtResultList(dispList);

		if (!CheckUtils.isEqual(applStatus, defaultApplStatus)
				&& !CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSACHU)) {
			// 更新前と更新後の申請状況が違っていた場合（更新後が「審査中」以外の場合
			// 社宅管理データ連携処理実行
			List<String> resultBatch = skf2010Sc005SharedService.doShatakuRenkei(menuScopeSessionBean, applShainNo,
					applNo, applStatus, applId, FunctionIdConstant.SKF2010_SC005);
			if (resultBatch != null) {
				skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(transDto, resultBatch);
				skfRollBackExpRepository.rollBack();
				throwBusinessExceptionIfErrors(transDto.getResultMessages());
				return transDto;
			}
		}

		// 遷移先画面ID
		String nextPageId = CodeConstant.NONE;

		// ログインユーザが承認権限を持つ申請書類である場合
		if (agreeAuthority) {
			if (CheckUtils.isEqual(applId, FunctionIdConstant.R0100)) {
				if (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSEICHU)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSACHU)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_DOI_SHINAI)) {
					// 社宅入居希望等調書（アウトソース用）
					nextPageId = FunctionIdConstant.SKF2020_SC003;

					if (skf2010Sc005SharedService.isShatakuTaiyoFuyo(applNo)) {
						nextPageId = CodeConstant.NONE;
					}
				} else if (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_DOI_ZUMI)
						|| CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHONIN1)) {
					// 社宅入居希望等調書の「同意済」から「承認中」の場合には、承認画面へ遷移前に使用料月額、共益費を更新
					skf2010Sc005SharedService.updateShatakuRental(applShainNo, CodeConstant.NYUTAIKYO_KBN_NYUKYO,
							applNo);
				}
			} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0103)) {
				// 退居（自動車の保管場所返還）届、備品返却申請
				nextPageId = FunctionIdConstant.SKF2040_SC002;
			} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0104)) {
				// 備品希望申請
				nextPageId = FunctionIdConstant.SKF2030_SC002;
			} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0105)) {
				// 備品返却申請
				if (CheckUtils.isEqual(applStatus, CodeConstant.STATUS_SHINSACHU)) {
					nextPageId = FunctionIdConstant.SKF2040_SC002;
				} else {
					nextPageId = FunctionIdConstant.SKF2050_SC002;
				}
			}

			if (NfwStringUtils.isEmpty(nextPageId)) {
				nextPageId = FunctionIdConstant.SKF2010_SC006;
			}

			tpi = TransferPageInfo.nextPage(nextPageId);
		} else {
			if (CheckUtils.isEqual(applId, FunctionIdConstant.R0104)) {
				nextPageId = FunctionIdConstant.SKF2030_SC002;
			} else if (CheckUtils.isEqual(applId, FunctionIdConstant.R0105)) {
				nextPageId = FunctionIdConstant.SKF2050_SC002;
			} else {
				nextPageId = FunctionIdConstant.SKF2010_SC006;
			}
			tpi = TransferPageInfo.nextPage(nextPageId);
		}
		attribute.put("applNo", applNo);
		attribute.put("applId", applId);
		attribute.put("applStatus", applStatus);
		tpi.setTransferAttributes(attribute);
		transDto.setTransferPageInfo(tpi, true);

		return transDto;
	}

	private boolean checkAdminRole(String roleId) {
		if (NfwStringUtils.isNotEmpty(roleId)) {
			if (CheckUtils.isEqual(roleId, SkfCommonConstant.ADMIN_ROLE1)
					|| CheckUtils.isEqual(roleId, SkfCommonConstant.ADMIN_ROLE2)
					|| CheckUtils.isEqual(roleId, SkfCommonConstant.ADMIN_ROLE3)) {
				return true;
			}
		}

		return false;
	}

}
