/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
		skfOperationLogUtils.setAccessLog("住所を検索", CodeConstant.C001, dto.getPageId());

		// 未入力チェック
		if (NfwStringUtils.isEmpty(dto.getZipCode()) && NfwStringUtils.isEmpty(dto.getAddress())) {
			// 郵便番号と住所が未入力の場合
			ServiceHelper.addErrorResultMessage(dto, new String[] { "zipCode" }, MessageIdConstant.E_SKF_1048, "郵便番号");
			ServiceHelper.addErrorResultMessage(dto, new String[] { "address" }, MessageIdConstant.E_SKF_1048, "住所");
			return dto;
		}

		return dto;
	}
}
