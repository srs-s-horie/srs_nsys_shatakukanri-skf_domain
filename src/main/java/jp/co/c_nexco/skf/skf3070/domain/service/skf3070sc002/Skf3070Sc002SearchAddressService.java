/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc002.Skf3070Sc002SearchAddressDto;

/**
 * Skf3070_Sc002 賃貸人（代理人）情報登録画面の住所検索処理クラス
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc002SearchAddressService extends BaseServiceAbstract<Skf3070Sc002SearchAddressDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070Sc002SharedService skf3070Sc002SheardService;
	@Autowired
	private SkfGetInfoUtilsGetPostalCodeAddressExpRepository skfGetInfoUtilsGetPostalCodeAddressExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param dto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc002SearchAddressDto dto) throws Exception {

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("住所検索", CodeConstant.C001, dto.getPageId());

		// 未入力チェック
		if (NfwStringUtils.isEmpty(dto.getZipCode()) && NfwStringUtils.isEmpty(dto.getAddress())) {
			// 郵便番号と住所が未入力の場合
			ServiceHelper.addErrorResultMessage(dto, new String[] { "zipCode" }, MessageIdConstant.E_SKF_1048, "郵便番号");
			ServiceHelper.addErrorResultMessage(dto, new String[] { "address" }, MessageIdConstant.E_SKF_1048, "住所");
			// ドロップダウンリストを再検索して処理を終了する
			skf3070Sc002SheardService.getDropDownList(dto);
			return dto;
		}

		SkfGetInfoUtilsGetPostalCodeAddressExp resultEntity = new SkfGetInfoUtilsGetPostalCodeAddressExp();
		SkfGetInfoUtilsGetPostalCodeAddressExpParameter param = new SkfGetInfoUtilsGetPostalCodeAddressExpParameter();
		// 住所検索
		if (NfwStringUtils.isNotEmpty(dto.getZipCode())) {
			// 「郵便番号」が入力されている場合、郵便番号で住所検索
			param.setPostalCd(dto.getZipCode());
			resultEntity = skfGetInfoUtilsGetPostalCodeAddressExpRepository.getAddressInfo(param);

			// 該当する郵便番号が無かった場合
			if (resultEntity == null) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "zipCode" }, MessageIdConstant.E_SKF_1047);
				// ドロップダウンリストを再検索して処理を終了する
				skf3070Sc002SheardService.getDropDownList(dto);
			}
		} else if (NfwStringUtils.isNotEmpty(dto.getAddress())) {
			// 「郵便番号」が未入力で、「住所」が入力されている場合、住所で郵便番号を検索
			String address = dto.getAddress().trim();
			param.setAddress(address);
			resultEntity = skfGetInfoUtilsGetPostalCodeAddressExpRepository.getAddressInfo(param);

			// 該当する住所が無かった場合
			if (resultEntity == null) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "address" }, MessageIdConstant.E_SKF_2019);
				// ドロップダウンリストを再検索して処理を終了する
				skf3070Sc002SheardService.getDropDownList(dto);
			}
		}

		// 住所情報が存在する場合
		if (resultEntity != null) {
			if (resultEntity.getPostalCd() != null && resultEntity.getPrefName() != null
					&& resultEntity.getCityName() != null && resultEntity.getAreaName() != null) {
				// 「郵便番号」と「住所」に検索結果から取得した値をそれぞれ設定
				dto.setZipCode(resultEntity.getPostalCd());
				dto.setAddress(resultEntity.getPrefName() + resultEntity.getCityName() + resultEntity.getAreaName());
			}
		}

		return dto;
	}
}
