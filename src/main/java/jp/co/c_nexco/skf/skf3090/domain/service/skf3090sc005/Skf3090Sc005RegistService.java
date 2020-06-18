/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc005;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc005.Skf3090Sc005UpdateShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005.Skf3090Sc005RegistDto;
import jp.co.c_nexco.skf.skf3090.domain.service.common.Skf309030CommonSharedService;

/**
 * Skf3090Sc005ResistService 従業員マスタ登録登録ボタン押下時処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc005RegistService extends SkfServiceAbstract<Skf3090Sc005RegistDto> {

	/** 定数 */
	private static final String AFFILIATION1_00 = "00";
	private static final String AFFILIATION2_000 = "000";

	@Autowired
	private Skf3090Sc005SharedService skf3090Sc005SharedService;

	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;

	@Autowired
	private Skf3090Sc005UpdateShainInfoExpRepository skf3090Sc005UpdateShainInfoExpRepository;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * メインメソッド.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected BaseDto index(Skf3090Sc005RegistDto registDto) throws Exception {

		registDto.setPageTitleKey(MessageIdConstant.SKF3090_SC005_TITLE);

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("登録", companyCd, FunctionIdConstant.SKF3090_SC005);

		// エラー系のDto値を初期化
		registDto.setShainNoError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setNameError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setNameKkError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setMailAddressError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setRetireDateError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalCompanyCdError(CodeConstant.DOUBLE_QUOTATION);

		// ドロップダウンリスト用リストのインスタンス生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();

		// 入力値チェック
		if (isValidateInput(registDto) == false) {
			// 入力チェックエラーの場合、ドロップダウンリストを再検索して処理を終了する

			// ドロップダウンリスト取得
			Map<String, Object> returnMap = skf3090Sc005SharedService.getDropDownLists(registDto.getOriginalCompanyCd(),
					registDto.getAgencyCd(), registDto.getAffiliation1Cd(), registDto.getAffiliation2Cd(),
					registDto.getBusinessAreaCd());

			companyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_COMPANY_LIST));
			agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AGENCY_LIST));
			affiliation1List
					.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION1_LIST));
			affiliation2List
					.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION2_LIST));
			businessAreaList.addAll(
					(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_BUSINESS_AREA_LIST));

			// DTOにセット
			registDto.setCompanyList(companyList);
			registDto.setAgencyList(agencyList);
			registDto.setAffiliation1List(affiliation1List);
			registDto.setAffiliation2List(affiliation2List);
			registDto.setBusinessAreaList(businessAreaList);

			return registDto;
		}

		if (Objects.equals(registDto.getUpdateFlag(), Skf309030CommonSharedService.UPDATE_FLAG_NEW)) {
			/** 登録処理 */
			// 登録候補社員の存在チェック
			boolean existFlag = isExistShain(CodeConstant.C001, registDto.getShainNo());

			if (!(existFlag)) {
				// 従業員マスタ一覧から新規ボタンで遷移してきた場合、登録処理を行う
				registShainInfo(registDto);
				/** 画面遷移 */
				registDto.setTransferPageInfo(TransferPageInfo.nextPage(FunctionIdConstant.SKF3090_SC004), true);
			} else {
				/** 同一社員番号の社員が存在する場合、ドロップダウンリストを再検索してエラーを表示する。 */
				// ドロップダウンリスト取得
				Map<String, Object> returnMap = skf3090Sc005SharedService.getDropDownLists(
						registDto.getOriginalCompanyCd(), registDto.getAgencyCd(), registDto.getAffiliation1Cd(),
						registDto.getAffiliation2Cd(), registDto.getBusinessAreaCd());

				companyList
						.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_COMPANY_LIST));
				agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AGENCY_LIST));
				affiliation1List.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION1_LIST));
				affiliation2List.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_AFFILIATION2_LIST));
				businessAreaList.addAll(
						(List<Map<String, Object>>) returnMap.get(Skf3090Sc005SharedService.KEY_BUSINESS_AREA_LIST));

				// DTOにセット
				registDto.setCompanyList(companyList);
				registDto.setAgencyList(agencyList);
				registDto.setAffiliation1List(affiliation1List);
				registDto.setAffiliation2List(affiliation2List);
				registDto.setBusinessAreaList(businessAreaList);

				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1063);
			}

		} else {
			/** 更新処理 */
			// 従業員マスタ一覧のリストテーブルから遷移してきた更新処理を行う
			registShainInfo(registDto);
			/** 画面遷移 */
			registDto.setTransferPageInfo(TransferPageInfo.nextPage(FunctionIdConstant.SKF3090_SC004), true);

		}

		return registDto;
	}

	/**
	 * 入力チェックを行う。.
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */
	private boolean isValidateInput(Skf3090Sc005RegistDto registDto) throws UnsupportedEncodingException {

		boolean isCheckOk = true;

		String debugMessage = "";

		/** 社員番号 */
		// 必須チェック
		if (registDto.getShainNo() == null || CheckUtils.isEmpty(registDto.getShainNo().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "社員番号");
			registDto.setShainNoError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "社員番号";

			// 桁数チェック
		} else if (CheckUtils.isMoreThanByteSize(registDto.getShainNo().trim(), 8)) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "社員番号", "8");
			registDto.setShainNoError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　桁数チェック - " + "社員番号 - " + registDto.getShainNo();

			// 形式チェック
		} else if (!(CheckUtils.isAlphabetNumeric(registDto.getShainNo().trim()))) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1052, "社員番号");
			registDto.setShainNoError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　形式チェック - " + "社員番号 - " + registDto.getShainNo();
		}

		/** 氏名 */
		// 必須チェック
		if (registDto.getName() == null || CheckUtils.isEmpty(registDto.getName().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "氏名");
			registDto.setNameError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "氏名";

			// 桁数チェック
		} else if (CheckUtils.isMoreThanByteSize(registDto.getName().trim(), 40)) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "氏名", "20");
			registDto.setNameError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　桁数チェック - " + "氏名 - " + registDto.getName();
		}

		/** 氏名カナ */
		// 入力されている場合
		if (!(registDto.getNameKk() == null || CheckUtils.isEmpty(registDto.getNameKk().trim()))) {
			// 桁数チェック
			if (CheckUtils.isMoreThanByteSize(registDto.getNameKk().trim(), 80)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "氏名カナ", "40");
				registDto.setNameKkError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "氏名カナ - " + registDto.getNameKk();
			}
		}

		/** メールアドレス */
		// 必須チェック
		if (registDto.getMailAddress() == null || CheckUtils.isEmpty(registDto.getMailAddress().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "メールアドレス");
			debugMessage += "　必須入力チェック - " + "メールアドレス";
			registDto.setMailAddressError(CodeConstant.NFW_VALIDATION_ERROR);

			// 桁数チェック
		} else if (CheckUtils.isMoreThanByteSize(registDto.getMailAddress().trim(), 255)) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "メールアドレス", "255");
			registDto.setMailAddressError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　桁数チェック - " + "メールアドレス - " + registDto.getMailAddress();

			// 形式チェック
		} else if (!(CheckUtils.isEmail(registDto.getMailAddress().trim()))) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3032, "メールアドレス");
			registDto.setMailAddressError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　形式チェック - " + "メールアドレス - " + registDto.getMailAddress();
		}

		/** 会社 */
		// 必須チェック
		if (registDto.getOriginalCompanyCd() == null || CheckUtils.isEmpty(registDto.getOriginalCompanyCd().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1054, "会社");
			registDto.setOriginalCompanyCdError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "会社";
		}

		/** 退職日 */
		// 入力されている場合
		if (!(registDto.getRetireDate() == null || CheckUtils.isEmpty(registDto.getRetireDate().trim()))) {
			LogUtils.debugByMsg("退職日：" + registDto.getRetireDate());
			// 桁数チェック
			if (CheckUtils.isMoreThanByteSize(registDto.getRetireDate().trim(), 10)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "退職日", "10");
				registDto.setRetireDateError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "退職日 - " + registDto.getRetireDate();

				// 形式チェック
			} else if (NfwStringUtils.isNotEmpty(registDto.getRetireDate()) && !(SkfCheckUtils
					.isSkfDateFormat(registDto.getRetireDate().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "退職日");
				registDto.setRetireDateError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "退職日 - " + registDto.getRetireDate();
			}
		}

		// デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		}

		return isCheckOk;
	}

	/**
	 * 社員マスタテーブルより該当社員が存在するか確認する。.
	 * 
	 * @param companyCd 会社コード
	 * @param shainNo 社員番号
	 * @return 検索結果。該当社員が存在する場合trueを返す。
	 */
	private boolean isExistShain(String companyCd, String shainNo) {

		boolean resultFlag = false;

		// 社員情報を取得する
		Skf1010MShain resultValue = skf3090Sc005SharedService.getShainByPrimaryKey(companyCd, shainNo);

		if (resultValue != null) {
			resultFlag = true;
		}

		return resultFlag;
	}

	/**
	 * 社員マスタテーブルに対して登録または更新処理を行う。<br>
	 * 登録、更新の判断は、RegistDtoに含まれる従業員マスタ一覧画面から受け取った更新フラグによって判断する。.
	 * 
	 * @param dto indexメソッドの引数であるDto
	 */
	private void registShainInfo(Skf3090Sc005RegistDto dto) {

		Skf1010MShain setValue = new Skf1010MShain();
		
		

		/** 登録項目をセット */
		// 会社コード（中日本固定）
		setValue.setCompanyCd(CodeConstant.C001);
		// 社員番号
		setValue.setShainNo(dto.getShainNo().trim());
		// 氏名
		setValue.setName(dto.getName().trim());
		// 氏名カナ
		setValue.setNameKk(dto.getNameKk().trim());
		// メールアドレス
		setValue.setMailAddress(dto.getMailAddress().trim());
		// 原籍会社コード
		setValue.setOriginalCompanyCd(dto.getOriginalCompanyCd());
		
		// 機関コード設定分岐
		if (NfwStringUtils.isNotEmpty(dto.getAgencyCd())) {
			/** 機関コード入力ありの場合以下選択値 */
			LogUtils.debugByMsg("機関コード入力ありの場合");
			setValue.setAgencyCd(dto.getAgencyCd());
			
			// 部等コード設定分岐
			if (NfwStringUtils.isNotEmpty(dto.getAffiliation1Cd())) {
				/** 部等コード入力ありの場合以下選択値 */
				LogUtils.debugByMsg("部等コード入力ありの場合");
				// 部等コード
				setValue.setAffiliation1Cd(dto.getAffiliation1Cd());
				
				// 室コード設定分岐
				if (NfwStringUtils.isNotEmpty(dto.getAffiliation2Cd())) {
					/** 室コード入力ありの場合以下選択値 */
					LogUtils.debugByMsg("室コード入力ありの場合");
					// 室コード
					setValue.setAffiliation2Cd(dto.getAffiliation2Cd());
				}else{
					/** 室コード入力なしの場合以下選択値 */
					LogUtils.debugByMsg("室コード入力なしの場合");
					setValue.setAffiliation2Cd(AFFILIATION2_000);
				}
				
			}else{
				/** 部等コード入力なしの場合以下選択値 */
				LogUtils.debugByMsg("部等コード入力なしの場合");
				// 部等コード
				setValue.setAffiliation1Cd(AFFILIATION1_00);
				// 室コード
				setValue.setAffiliation2Cd(AFFILIATION2_000);
				
			}
		} else {
			/** 機関コード入力なしの場合以下固定値 */
			LogUtils.debugByMsg("機関コード入力なしの場合");
			// 機関コード
			setValue.setAgencyCd(null);
			// 部等コード
			setValue.setAffiliation1Cd(null);
			// 室コード
			setValue.setAffiliation2Cd(null);
		}
		// 事業領域コード
		if (NfwStringUtils.isNotEmpty(dto.getBusinessAreaCd())) {
			setValue.setBusinessAreaCd(dto.getBusinessAreaCd());
		}
		// 性別・生年月日は除外

		// 退職日入力分岐
		if (dto.getRetireDate() == null || NfwStringUtils.isEmpty(dto.getRetireDate().trim())) {
			// 退職日の入力が無い場合
			// 退職者フラグ = 0
			setValue.setRetireFlg("0");
		} else {
			// 退職日の入力がある場合
			// 退職者フラグ = 1
			setValue.setRetireFlg("1");
			// 退職日
			setValue.setRetireDate(dto.getRetireDate().replace("/", ""));
		}
		// 登録フラグ（1固定）
		setValue.setRegistFlg("1");

		/** 登録 */
		LogUtils.debugByMsg("DBCommonItems：" + this.dbCommonItems.toString());
		int registCount = 0;
		if (Skf309030CommonSharedService.UPDATE_FLAG_NEW.equals(dto.getUpdateFlag())) {
			// 新規登録の場合はINSERT
			registCount = skf1010MShainRepository.insertSelective(setValue);
			LogUtils.debugByMsg("社員マスタ登録件数：" + registCount + "件");
		} else {
			// 更新の場合はUPDATE
			// 最終更新時間
			setValue.setLastUpdateDate(dto.getLastUpdateDate(Skf3090Sc005SharedService.KEY_LAST_UPDATE_DATE));
			registCount = updateShainInfo(setValue, dto);
			LogUtils.debugByMsg("社員マスタ更新件数：" + registCount + "件");
		}

	}

	/**
	 * 社員マスタを更新する。.
	 * 
	 * @param setValue 更新値を含むSkf1010MShainインスタンス
	 * @return 更新件数
	 */
	private int updateShainInfo(Skf1010MShain setValue, Skf3090Sc005RegistDto dto) {

		Skf1010MShain resultValue = new Skf1010MShain();
		// 排他チェック
		resultValue = skf3090Sc005SharedService.getShainByPrimaryKey(CodeConstant.C001, setValue.getShainNo());
		if (resultValue == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			throwBusinessExceptionIfErrors(dto.getResultMessages());
		}
		super.checkLockException(setValue.getLastUpdateDate(), resultValue.getUpdateDate());
		// 更新
		return skf3090Sc005UpdateShainInfoExpRepository.updateShainInfo(setValue);

	}

}
