package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005InitDto;

/**
 * Skf2010Sc005 承認一覧初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005InitService extends BaseServiceAbstract<Skf2010Sc005InitDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	@Override
	public BaseDto index(Skf2010Sc005InitDto initDto) throws Exception {

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC005_TITLE);

		// 初期化処理
		init(initDto);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", companyCd, initDto.getPageId());

		// ドロップダウン作成
		setDropDown(initDto);

		// 申請書類種別ドロップダウンをセット
		// ※申請書類種別は社宅では「福利厚生」のみなので、ドロップダウンの必要が無い

		// チェックボックスの初期値セット
		List<String> defaultApplStatusList = getDefaultApplStatusValue();
		initDto.setApplStatus(defaultApplStatusList.toArray(new String[defaultApplStatusList.size()]));

		// 所属機関の初期値セット
		initDto.setShozokuKikan(CodeConstant.SHOZOKU_SHINSEI);

		// 社宅連携フラグ(0：社宅未連携、1：社宅連携)によるチェックボックスの切替

		// 検索処理（リストテーブル作成）
		initDto.setLtResultList(searchApplList(initDto, defaultApplStatusList));

		return initDto;
	}

	/**
	 * ドロップダウンを作成します
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	private void setDropDown(Skf2010Sc005InitDto dto) {
		// ドロップダウン作成
		Map<String, Object> dropDownMap = new HashMap<String, Object>();
		skf2010Sc005SharedService.setDropDown(dropDownMap, companyCd, dto.getAgency(), dto.getAffiliation1(),
				dto.getAffiliation2());
		// 機関ドロップダウンをセット
		dto.setDdlAgencyList((List<Map<String, Object>>) dropDownMap.get("Agency"));
		// 部等ドロップダウンをセット
		dto.setDdlAffiliation1List((List<Map<String, Object>>) dropDownMap.get("Affiliation1"));
		// 室、チーム又は課ドロップダウンをセット
		dto.setDdlAffiliation2List((List<Map<String, Object>>) dropDownMap.get("Affiliation2"));

		return;
	}

	/**
	 * 初期化処理
	 * 
	 * @param dto
	 */
	private void init(Skf2010Sc005InitDto dto) {
		// エラー色を削除
		dto.setApplDateFromErr("");
		dto.setApplDateToErr("");
		dto.setAgreDateFromErr("");
		dto.setAgreDateToErr("");
		dto.setApplStatusErr("");

		dto.setMaskPattern("USER");
		Set<String> roleIds = LoginUserInfoUtils.getRoleIds();
		for (String roleId : roleIds) {
			if (roleId != null && (roleId.equals(CodeConstant.NAKASA_SHATAKU_KANRI)
					|| roleId.equals(CodeConstant.NAKASA_SHATAKU_TANTO) || roleId.equals(CodeConstant.SYSTEM_KANRI))) {
				dto.setMaskPattern("ADMIN");
			}
		}

		// 操作ガイド取得
		dto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(dto.getPageId()));

		// 画面ID保持
		dto.setPrePageId(FunctionIdConstant.SKF2010_SC005);

	}

	/**
	 * 申請情報一覧を取得します
	 * 
	 * @param dto
	 * @param applStatus
	 * @return
	 */
	private List<Map<String, Object>> searchApplList(Skf2010Sc005InitDto dto, List<String> applStatus) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		// 承認一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		param = setParam(dto);
		param.setShozokuKikan(CodeConstant.SHOZOKU_SHINSEI);
		tApplHistoryData = skf2010Sc005SharedService.SearchApplList(param);
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
	private Skf2010Sc005GetShoninIchiranShoninExpParameter setParam(Skf2010Sc005InitDto dto) {
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
			List<String> applStatusList = new ArrayList<String>();
			List<String> tmpApplStatus = Arrays.asList(dto.getApplStatus());
			applStatusList.addAll(tmpApplStatus);
			// 申請状況に「30：承認中」がセットされていた場合、承認1～4まで全て設定する
			if (applStatusList.contains(CodeConstant.STATUS_SHONIN)) {
				applStatusList.add(CodeConstant.STATUS_SHONIN1);
				applStatusList.add(CodeConstant.STATUS_SHONIN2);
				applStatusList.add(CodeConstant.STATUS_SHONIN3);
				applStatusList.add(CodeConstant.STATUS_SHONIN4);
			}
			param.setApplStatus(applStatusList);
		}
		// 承認者名
		if (dto.getAgreementName() != null && !CheckUtils.isEmpty(dto.getAgreementName())) {
			param.setAgreeName(dto.getAgreementName());
		}
		return param;
	}

	/**
	 * チェックボックスの初期値を設定します
	 * 
	 * @return
	 */
	private List<String> getDefaultApplStatusValue() {
		List<String> rtnApplStatus = new ArrayList<String>();
		rtnApplStatus.add(CodeConstant.STATUS_SHINSACHU); // 10：審査中
		rtnApplStatus.add(CodeConstant.STATUS_SHONIN); // 30：承認中
		rtnApplStatus.add(CodeConstant.STATUS_SHINSEICHU); // 01：申請中
		rtnApplStatus.add(CodeConstant.STATUS_DOI_SHINAI); // 21：同意しない
		rtnApplStatus.add(CodeConstant.STATUS_DOI_ZUMI); // 22：同意済み
		rtnApplStatus.add(CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI); // 05：所属長確認済み
		rtnApplStatus.add(CodeConstant.STATUS_TORIKOMI_KANRYO); // 15：取込完了
		rtnApplStatus.add(CodeConstant.STATUS_HANNYU_ZUMI); // 24：搬入済み
		rtnApplStatus.add(CodeConstant.STATUS_HANSYUTSU_ZUMI); // 26：搬出済み

		return rtnApplStatus;
	}

}
