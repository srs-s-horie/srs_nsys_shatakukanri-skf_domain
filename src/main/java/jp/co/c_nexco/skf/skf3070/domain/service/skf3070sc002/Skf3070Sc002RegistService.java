/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3070TOwnerInfo;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3070Sc002.Skf3070Sc002UpdateOwnerInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3070TOwnerInfoRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc002.Skf3070Sc002RegistDto;

/**
 * Skf3070_Sc002 賃貸人（代理人）情報登録画面の登録処理クラス
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc002RegistService extends BaseServiceAbstract<Skf3070Sc002RegistDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070Sc002SharedService skf3070Sc002SheardService;
	@Autowired
	private Skf3070TOwnerInfoRepository skf3070TOwnerInfoRepository;
	@Autowired
	private Skf3070Sc002UpdateOwnerInfoExpRepository skf3070Sc002UpdateOwnerInfoExpRepository;

	@Override
	protected BaseDto index(Skf3070Sc002RegistDto registDto) throws Exception {

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());

		// 入力チェック
		if (isValidateInput(registDto) == false) {
			// 入力チェックエラーの場合、ドロップダウンリストを再検索して処理を終了する
			skf3070Sc002SheardService.getDropDownList(registDto);
			return registDto;
		}

		// エラー発生時用に、ドロップダウンリストを再検索する
		skf3070Sc002SheardService.getDropDownList(registDto);

		// 登録項目処理
		registOwnerInfo(registDto);

		return registDto;
	}

	/**
	 * 入力判定<br>
	 * エラーがある場合は、falseを返す
	 * 
	 * @param registDto
	 * @return result true or false
	 * @throws UnsupportedEncodingException
	 */
	private boolean isValidateInput(Skf3070Sc002RegistDto registDto) throws UnsupportedEncodingException {

		boolean checkEmpty = true;
		boolean checkType = true;
		boolean checkByte = true;

		// 必須入力チェック
		checkEmpty = isValidateEmpty(registDto);
		// 文字種チェック
		checkType = isValidateType(registDto);
		// 桁数チェック
		checkByte = isValidateByte(registDto);
		// エラーがある場合はfalseを返す
		if (checkEmpty == false || checkType == false || checkByte == false) {
			return false;
		}
		return true;
	}

	/**
	 * 必須入力チェック
	 * 
	 * @param registDto
	 * @return result true or false
	 */
	private boolean isValidateEmpty(Skf3070Sc002RegistDto registDto) {

		boolean result = true;

		// 氏名又は名称
		if (NfwStringUtils.isBlank(registDto.getOwnerName())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "ownerName" }, MessageIdConstant.E_SKF_1048,
					"氏名又は名称");
			result = false;
		}

		// 氏名又は名称（フリガナ）
		if (NfwStringUtils.isBlank(registDto.getOwnerNameKk())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "ownerNameKk" }, MessageIdConstant.E_SKF_1048,
					"氏名又は名称（フリガナ）");
			result = false;
		}

		// 住所
		if (NfwStringUtils.isBlank(registDto.getAddress())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "address" }, MessageIdConstant.E_SKF_1048,
					"住所");
			result = false;
		}

		// 個人法人区分
		if (NfwStringUtils.isBlank(registDto.getBusinessKbn())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "businessKbn" }, MessageIdConstant.E_SKF_1054,
					"個人法人区分");
			result = false;
		}

		// 個人番号
		if (NfwStringUtils.isBlank(registDto.getAcceptFlg())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "acceptFlg" }, MessageIdConstant.E_SKF_1054,
					"個人番号");
			result = false;
		}
		return result;
	}

	/**
	 * 文字種チェック
	 * 
	 * @param registDto
	 * @return result true or false
	 */
	private boolean isValidateType(Skf3070Sc002RegistDto registDto) {
		boolean result = true;

		// 氏名又は名称（フリガナ）
		if (NfwStringUtils.isNotBlank(registDto.getOwnerNameKk())
				&& !CheckUtils.isFullWidthKatakanaSpace(registDto.getOwnerNameKk())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "ownerNameKk" }, MessageIdConstant.E_SKF_1005,
					"氏名又は名称（フリガナ）");
			result = false;
		}

		// 郵便番号
		if (NfwStringUtils.isNotBlank(registDto.getZipCode()) && !CheckUtils.isZipcodeFormat(registDto.getZipCode())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "zipCode" }, MessageIdConstant.E_SKF_1007,
					"郵便番号", "半角数字7");
			result = false;
		}

		return result;
	}

	/**
	 * 桁数チェック
	 * 
	 * @param registDto
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean isValidateByte(Skf3070Sc002RegistDto registDto) throws UnsupportedEncodingException {
		boolean result = true;

		// 氏名又は名称
		LogUtils.debugByMsg("氏名又は名称 　桁数チェック：" + registDto.getOwnerName());
		if (NfwStringUtils.isNotBlank(registDto.getOwnerName())
				&& CheckUtils.isMoreThanByteSize(registDto.getOwnerName().trim(), 120)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "ownerName" }, MessageIdConstant.E_SKF_1071,
					"氏名又は名称", "60");
			result = false;
		}

		// 氏名又は名称（フリガナ）
		LogUtils.debugByMsg("氏名又は名称（フリガナ） 　桁数チェック：" + registDto.getOwnerNameKk());
		if (NfwStringUtils.isNotBlank(registDto.getOwnerNameKk())
				&& CheckUtils.isMoreThanByteSize(registDto.getOwnerNameKk().trim(), 120)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "ownerNameKk" }, MessageIdConstant.E_SKF_1071,
					"氏名又は名称（フリガナ）", "60");
			result = false;
		}

		// 住所
		LogUtils.debugByMsg("住所 　桁数チェック：" + registDto.getAddress());
		if (NfwStringUtils.isNotBlank(registDto.getAddress())
				&& CheckUtils.isMoreThanByteSize(registDto.getAddress().trim(), 120)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "address" }, MessageIdConstant.E_SKF_1071,
					"住所", "60");
			result = false;
		}

		// 個人番号(督促状況）
		LogUtils.debugByMsg("個人番号(督促状況） 　桁数チェック：" + registDto.getAcceptStatus());
		if (NfwStringUtils.isNotBlank(registDto.getAcceptStatus())
				&& CheckUtils.isMoreThanByteSize(registDto.getAcceptStatus().trim(), 40)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "acceptStatus" },
					MessageIdConstant.E_SKF_1071, "個人番号（督促状況）", "20");
			result = false;
		}

		// 備考
		LogUtils.debugByMsg("備考 　桁数チェック：" + registDto.getRemarks());
		if (NfwStringUtils.isNotBlank(registDto.getRemarks())
				&& CheckUtils.isMoreThanByteSize(registDto.getRemarks().trim(), 800)) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "remarks" }, MessageIdConstant.E_SKF_1071,
					"備考", "400");
			result = false;
		}

		return result;
	}

	/**
	 * 登録、更新処理
	 * 
	 * @param registDto
	 */
	private void registOwnerInfo(Skf3070Sc002RegistDto registDto) {

		Skf3070TOwnerInfo ownerInfo = new Skf3070TOwnerInfo();

		// 登録項目の設定
		// 賃貸人（代理人）番号 (ある場合のみ）
		if (NfwStringUtils.isNotEmpty(registDto.getOwnerNo())) {
			long ownerNoL = Long.parseLong(registDto.getOwnerNo());
			ownerInfo.setOwnerNo(ownerNoL);
		}
		// 賃貸人（代理人）氏名又は名称
		ownerInfo.setOwnerName(registDto.getOwnerName());
		// 賃貸人（代理人）氏名又は名称（フリガナ）
		ownerInfo.setOwnerNameKk(registDto.getOwnerNameKk());
		// 郵便番号
		ownerInfo.setZipCd(registDto.getZipCode());
		// 住所
		ownerInfo.setAddress(registDto.getAddress());
		// 個人法人区分
		ownerInfo.setBusinessKbn(registDto.getBusinessKbn());
		// 個人番号受領フラグ
		ownerInfo.setAcceptFlg(registDto.getAcceptFlg());
		// 督促状況
		ownerInfo.setAcceptStatus(registDto.getAcceptStatus());
		// 備考
		ownerInfo.setRemarks(registDto.getRemarks());

		// 登録処理
		if (NfwStringUtils.isEmpty(registDto.getOwnerNo())) {
			// 登録処理
			skf3070TOwnerInfoRepository.insertSelective(ownerInfo);
		} else {
			// 更新処理
			ownerInfo.setLastUpdateDate(registDto.getLastUpdateDate(Skf3070Sc002SharedService.KEY_LAST_UPDATE_DATE));
			updateOwnerInfo(registDto.getOwnerNo(), ownerInfo);
		}

		// 画面遷移
		registDto.setTransferPageInfo(TransferPageInfo.nextPage(FunctionIdConstant.SKF3070_SC001), true);
	}

	/**
	 * 賃貸人（代理人）情報 更新処理
	 * 
	 * @param ownerNo
	 * @param ownerInfo
	 */
	private int updateOwnerInfo(String ownerNo, Skf3070TOwnerInfo ownerInfo) {
		// 排他制御チェック
		long ownerNoL = Long.parseLong(ownerNo);
		Skf3070TOwnerInfo updateCheck = new Skf3070TOwnerInfo();
		updateCheck = skf3070TOwnerInfoRepository.selectByPrimaryKey(ownerNoL);
		super.checkLockException(ownerInfo.getLastUpdateDate(), updateCheck.getUpdateDate());

		// 更新処理
		return skf3070Sc002UpdateOwnerInfoExpRepository.updateOwnerInfo(ownerInfo);
	}

}
