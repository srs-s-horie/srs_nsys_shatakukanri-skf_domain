package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MAgency;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MSoshiki;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MSoshikiRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007RegistDto;
import jp.co.c_nexco.skf.skf3090.domain.service.common.Skf309040CommonSharedService;

/**
 * Skf3090Sc007RegistService 登録ボタン押下時の処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007RegistService extends SkfServiceAbstract<Skf3090Sc007RegistDto> {

	/** 定数 */
	private static final String AFFILIATION1_000 = "000";
	private static final String AFFILIATION2_000 = "000";

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf1010MSoshikiRepository skf1010MSoshikiRepository;

	@Autowired
	private Skf3090Sc007SharedService skf3090Sc007SharedService;

	@SuppressWarnings("unchecked")
	@Override
	public Skf3090Sc007RegistDto index(Skf3090Sc007RegistDto registDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPrePageId());

		// エラー系のDtoの初期化
		registDto.setAgencyCdError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAffiliation1CdError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAffiliation2CdError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAgencyNameError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAffiliation1NameError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setAffiliation2NameError(CodeConstant.DOUBLE_QUOTATION);

		// ドロップダウン用リストのインスタンス生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();

		// 入力値チェック
		if (isValidateInput(registDto) == false) {

			// ドロップダウン取得
			Map<String, Object> returnMap = skf3090Sc007SharedService.getDropDownLists(registDto.getRegistCompanyCd(),
					registDto.getRegistAgencyCd(), registDto.getRegistBusinessAreaCd());

			// 画面表示するドロップダウンを取得
			companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));
			businessAreaList.addAll(
					(List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_BUSINESS_AREA_LIST));

			// Dtoにセット
			registDto.setCompanyList(companyList);
			registDto.setBusinessAreaList(businessAreaList);

			return registDto;
		}

		if (Objects.equals(registDto.getUpdateFlag(), Skf309040CommonSharedService.UPDATE_FLAG_NEW)) {
			/** 登録処理 */
			// 登録候補組織の存在のチェック
			boolean existFlag = isExistSoshiki(registDto.getRegistCompanyCd(), registDto.getRegistAgencyCd(),
					registDto.getRegistAffiliation1Cd(), registDto.getRegistAffiliation2Cd());

			boolean existAgencyFlag = isExistAgency(registDto.getRegistCompanyCd(), registDto.getRegistAgencyCd());

			if (!(existFlag)) {
				if (existAgencyFlag) {
					// 組織マスタ一覧から新規ボタンで遷移してきた場合、登録処理を行う
					registSoshikiInfo(registDto);
					// 画面遷移
					registDto.setTransferPageInfo(TransferPageInfo.nextPage(FunctionIdConstant.SKF3090_SC006));
				} else {
					Map<String, Object> returnMap = skf3090Sc007SharedService.getDropDownLists(
							registDto.getRegistCompanyCd(), registDto.getRegistAgencyCd(),
							registDto.getRegistBusinessAreaCd());

					companyList.addAll(
							(List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));
					businessAreaList.addAll((List<Map<String, Object>>) returnMap
							.get(Skf3090Sc007SharedService.KEY_BUSINESS_AREA_LIST));

					registDto.setCompanyList(companyList);
					registDto.setBusinessAreaList(businessAreaList);

					ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyCd" },
							MessageIdConstant.E_SKF_3049);
				}
			} else {
				Map<String, Object> returnMap = skf3090Sc007SharedService.getDropDownLists(
						registDto.getRegistCompanyCd(), registDto.getRegistAgencyCd(),
						registDto.getRegistBusinessAreaCd());

				companyList
						.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_COMPANY_LIST));
				businessAreaList.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc007SharedService.KEY_BUSINESS_AREA_LIST));

				registDto.setCompanyList(companyList);
				registDto.setBusinessAreaList(businessAreaList);

				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3034);
			}
		}
		return registDto;
	}

	/**
	 * 入力チェック
	 * 
	 * @param registDto
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean isValidateInput(Skf3090Sc007RegistDto registDto) throws UnsupportedEncodingException {

		boolean isCheck = true;

		/** 会社 */
		// 必須チェック
		if (registDto.getRegistCompanyCd() == null || CheckUtils.isEmpty(registDto.getRegistCompanyCd().trim())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registCompanyCd" },
					MessageIdConstant.E_SKF_1048, "会社");
			isCheck = false;
		}

		/** 機関コード */
		// 必須チェック
		// 桁数チェック
		// 形式チェック
		if (registDto.getRegistAgencyCd() == null || CheckUtils.isEmpty(registDto.getRegistAgencyCd().trim())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyCd" },
					MessageIdConstant.E_SKF_1048, "機関コード");
			isCheck = false;
		} else if (CheckUtils.isMoreThanByteSize(registDto.getRegistAgencyCd().trim(), 3)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyCd" },
					MessageIdConstant.E_SKF_1071, "機関コード", "3");
			isCheck = false;
		} else if (NfwStringUtils.isNotEmpty(registDto.getRegistAgencyCd())
				&& !(CheckUtils.isAlphabetNumeric(registDto.getRegistAgencyCd().trim()))) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyCd" },
					MessageIdConstant.E_SKF_1052, "機関コード");
			isCheck = false;
		}

		/** 部等コード */
		// 桁数チェック
		// 形式チェック
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation1Cd())
				&& CheckUtils.isMoreThanByteSize(registDto.getRegistAffiliation1Cd().trim(), 3)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation1Cd" },
					MessageIdConstant.E_SKF_1071, "部等コード", "3");
			isCheck = false;
		} else if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation1Cd())
				&& !(CheckUtils.isAlphabetNumeric(registDto.getRegistAffiliation1Cd().trim()))) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation1Cd" },
					MessageIdConstant.E_SKF_1052, "部等コード");
			isCheck = false;
		}

		/** 室・課等コード */
		// 桁数チェック
		// 形式チェック
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation2Cd())
				&& CheckUtils.isMoreThanByteSize(registDto.getRegistAffiliation2Cd().trim(), 3)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation2Cd" },
					MessageIdConstant.E_SKF_1071, "室・課等コード", "3");
			isCheck = false;
		} else if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation2Cd())
				&& !(CheckUtils.isAlphabetNumeric(registDto.getRegistAffiliation2Cd().trim()))) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation2Cd" },
					MessageIdConstant.E_SKF_1052, "室・課等コード");
			isCheck = false;
		}

		/** 機関名称 */
		// 必須チェック
		// 桁数チェック
		if (registDto.getRegistAgencyName() == null || CheckUtils.isEmpty(registDto.getRegistAgencyName().trim())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyName" },
					MessageIdConstant.E_SKF_1048, "機関名称");
			isCheck = false;
		} else if (CheckUtils.isMoreThanByteSize(registDto.getRegistAgencyName().trim(), 128)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAgencyName" },
					MessageIdConstant.E_SKF_1071, "機関名称", "64");
			isCheck = false;
		}

		/** 部等名称 */
		// 桁数チェック
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation1Name())
				&& CheckUtils.isMoreThanByteSize(registDto.getRegistAffiliation1Name().trim(), 128)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation1Name" },
					MessageIdConstant.E_SKF_1071, "部等名称", "64");
			isCheck = false;
		}

		/** 室・課等名称 */
		// 桁数チェック
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation2Name())
				&& CheckUtils.isMoreThanByteSize(registDto.getRegistAffiliation2Name().trim(), 128)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "registAffiliation2Name" },
					MessageIdConstant.E_SKF_1071, "室・課等名称", "64");
			isCheck = false;
		}

		return isCheck;
	}

	/**
	 * 重複チェック
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @return 存在すればfalse、存在しなければtrue
	 */
	private boolean isExistSoshiki(String companyCd, String agencyCd, String affiliation1Cd, String affiliation2Cd) {

		// 部等コードがnullの場合000をセット
		if (affiliation1Cd == null) {
			affiliation1Cd = AFFILIATION1_000;
		}

		// 室・課等コードがnullの場合000をセット
		if (affiliation2Cd == null) {
			affiliation2Cd = AFFILIATION2_000;
		}

		boolean resultFlag = false;

		// 組織情報を取得する
		Skf1010MSoshiki resultValue = skf3090Sc007SharedService.getSoshikiByPrimaryKey(companyCd, agencyCd,
				affiliation1Cd, affiliation2Cd);

		if (resultValue != null) {
			resultFlag = true;
		}

		return resultFlag;

	}

	/**
	 * 存在チェック（機関コード）
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @return 存在すればtrue、存在しなければfalse
	 */
	private boolean isExistAgency(String companyCd, String agencyCd) {

		boolean resultFlag = true;

		// 組織情報を取得する
		Skf1010MAgency resultValue = skf3090Sc007SharedService.getAgencyByPrimaryKey(companyCd, agencyCd);

		if (resultValue == null) {
			resultFlag = false;
		}

		return resultFlag;

	}

	/**
	 * Insertの処理
	 * 
	 * @param registDto
	 */
	private void registSoshikiInfo(Skf3090Sc007RegistDto registDto) {

		Skf1010MSoshiki setValue = new Skf1010MSoshiki();

		/** 登録項目をセット */
		// 会社
		setValue.setCompanyCd(registDto.getRegistCompanyCd().trim());
		// 機関コード
		setValue.setAgencyCd(registDto.getRegistAgencyCd().trim());
		// 部等コード
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation1Cd())) {
			setValue.setAffiliation1Cd(registDto.getRegistAffiliation1Cd());
		} else {
			setValue.setAffiliation1Cd(AFFILIATION1_000);
		}
		// 室・課等コード
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation2Cd())) {
			setValue.setAffiliation2Cd(registDto.getRegistAffiliation2Cd());
		} else {
			setValue.setAffiliation2Cd(AFFILIATION2_000);
		}
		// 機関名称
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAgencyName())) {
			setValue.setAgencyName(registDto.getRegistAgencyName());
			setValue.setAgencyAbbr(registDto.getRegistAgencyName());
		} else {
			setValue.setAgencyName("");
		}
		// 部等名称
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation1Name())) {
			setValue.setAffiliation1Name(registDto.getRegistAffiliation1Name());
			setValue.setAffiliation1Abbr(registDto.getRegistAffiliation1Name());
		} else {
			setValue.setAffiliation1Name("");
		}
		// 室・課等名称
		if (NfwStringUtils.isNotEmpty(registDto.getRegistAffiliation2Name())) {
			setValue.setAffiliation2Name(registDto.getRegistAffiliation2Name());
			setValue.setAffiliation2Abbr(registDto.getRegistAffiliation2Name());
		} else {
			setValue.setAffiliation2Name("");
		}
		// 事業領域
		if (NfwStringUtils.isNotEmpty(registDto.getRegistBusinessAreaCd())) {
			setValue.setBusinessAreaCd(registDto.getRegistBusinessAreaCd());
		}

		// 登録フラグ(1固定)
		setValue.setRegistFlg("1");

		/** 登録 */
		skf1010MSoshikiRepository.insertSelective(setValue);

	}
}
