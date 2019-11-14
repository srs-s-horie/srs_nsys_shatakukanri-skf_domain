package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005UpdateDto;

/**
 * Skf2010Sc005 承認一覧更新処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005UpdateService extends BaseServiceAbstract<Skf2010Sc005UpdateDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;

	@Override
	public BaseDto index(Skf2010Sc005UpdateDto updDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("一括承認処理開始", CodeConstant.C001, FunctionIdConstant.SKF2010_SC005);

		// 申請情報履歴更新処理
		updateApplStatus(updDto);

		// 機関ドロップダウンをセット
		updDto.setDdlAgencyList(skfDropDownUtils.getDdlAgencyByCd(companyCd, updDto.getAgency(), true));

		// 部等ドロップダウンをセット
		updDto.setDdlAffiliation1List(
				skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, updDto.getAgency(), updDto.getAffiliation1(), true));

		// 室、チーム又は課ドロップダウンをセット
		updDto.setDdlAffiliation2List(skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, updDto.getAgency(),
				updDto.getAffiliation1(), updDto.getAffiliation2(), true));

		// 検索処理（リストテーブル作成）
		updDto.setLtResultList(searchApplList(updDto));

		return updDto;
	}

	@SuppressWarnings("unchecked")
	private void updateApplStatus(Skf2010Sc005UpdateDto updDto) throws Exception {
		// 社宅連携用セッション情報セット
		skf2010Sc005SharedService.setMenuScopeSessionBean(menuScopeSessionBean);

		String submitApplNo = updDto.getSubmitApplNo();

		if (submitApplNo == null || CheckUtils.isEmpty(submitApplNo)) {
			return;
		}

		List<String> updateApplNoList = new ArrayList<String>();
		String[] applNos = submitApplNo.split(",");
		updateApplNoList = Arrays.asList(applNos);

		for (String applNo : updateApplNoList) {
			boolean res = skf2010Sc005SharedService.updateApplStatus(companyCd, applNo, updDto);
			if (!res) {
				throwBusinessExceptionIfErrors(updDto.getResultMessages());
			}
		}
		// セッション情報削除
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC002);

		return;
	}

	private List<Map<String, Object>> searchApplList(Skf2010Sc005UpdateDto dto) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		// 承認一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();

		// 申請状況チェック
		if (dto.getApplStatus() == null || dto.getApplStatus().length == 0) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1054, "申請状況");
			throwBusinessExceptionIfErrors(dto.getResultMessages());
		} else {
			// 検索条件セット
			param = setParam(dto);
			// 検索処理
			tApplHistoryData = skf2010Sc005SharedService.SearchApplList(param);
			if (tApplHistoryData == null || tApplHistoryData.size() == 0) {
				// 検索結果0件
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.W_SKF_1007);
			} else if (tApplHistoryData.size() > Integer.parseInt(searchMaxCount)) {
				// 検索結果表示最大数以上
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.W_SKF_1002, "最大件数");
			}
		}

		// グリッド表示（リストテーブル）作成
		rtnList = skf2010Sc005SharedService.createListTable(tApplHistoryData);

		return rtnList;
	}

	/**
	 * 検索条件をセットします。
	 * 
	 * @param dto
	 * @return
	 */
	private Skf2010Sc005GetShoninIchiranShoninExpParameter setParam(Skf2010Sc005UpdateDto dto) {
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		// 会社コード
		param.setCompanyCd(companyCd);

		// 機関
		if (dto.getAgency() != null && !CheckUtils.isEmpty(dto.getAgency())) {
			param.setAgencyName(skf2010Sc005SharedService.getAgencyName(companyCd, dto.getAgency()));
		}
		// 部等
		if (dto.getAffiliation1() != null && !CheckUtils.isEmpty(dto.getAffiliation1())) {
			param.setAffiliation1Name(
					skf2010Sc005SharedService.getAffiliation1Name(companyCd, dto.getAgency(), dto.getAffiliation1()));

		}
		// 室、チーム
		if (dto.getAffiliation2() != null && !CheckUtils.isEmpty(dto.getAffiliation2())) {
			param.setAffiliation2Name(skf2010Sc005SharedService.getAffiliation2Name(companyCd, dto.getAgency(),
					dto.getAffiliation1(), dto.getAffiliation2()));

		}
		// 所属機関
		param.setShozokuKikan(dto.getShozokuKikan());
		// 申請日時（FROM）
		if (dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())) {
			param.setApplDateFrom(dto.getApplDateFrom().replace("/", ""));
		}
		// 申請日時（TO）
		if (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())) {
			param.setApplDateTo(dto.getApplDateTo().replace("/", ""));
		}
		// 承認日／修正依頼日（From）
		if (dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())) {
			param.setAgreDateFrom(dto.getAgreDateFrom().replace("/", ""));
		}
		// 承認日／修正依頼日（To）
		if (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())) {
			param.setAgreDateTo(dto.getAgreDateTo().replace("/", ""));
		}
		// 申請者名
		if (dto.getName() != null && !CheckUtils.isEmpty(dto.getName())) {
			param.setName(dto.getName());
		}
		// 申請書類種別
		param.setApplCtgryId(dto.getApplCtgry());
		// 申請状況
		if (dto.getApplStatus() != null && dto.getApplStatus().length > 0) {
			param.setApplStatus(Arrays.asList(dto.getApplStatus()));
		}
		// 承認者名
		if (dto.getAgreementName() != null && !CheckUtils.isEmpty(dto.getAgreementName())) {
			param.setAgreeName(dto.getAgreementName());
		}
		return param;
	}

}
