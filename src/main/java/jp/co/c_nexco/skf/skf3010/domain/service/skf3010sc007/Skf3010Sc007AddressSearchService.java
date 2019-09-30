/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc007.Skf3010Sc007GetZipToAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc007.Skf3010Sc007GetZipToAddressExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc007.Skf3010Sc007GetZipToAddressExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc007.Skf3010Sc007AddressSearchDto;


/**
 * Skf3010Sc007AddressSearchService　駐車場契約情報登録画面の住所検索サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc007AddressSearchService extends BaseServiceAbstract<Skf3010Sc007AddressSearchDto> {
	
	
	@Autowired
	private Skf3010Sc007GetZipToAddressExpRepository skf3010Sc007GetZipToAddressExpRepository;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Skf3010Sc007AddressSearchDto index(Skf3010Sc007AddressSearchDto searchDto) throws Exception {
		
		// Debugログで出力
		LogUtils.debugByMsg("駐車場契約情報-郵便番号：" + searchDto.getParkingZipCd());
		//
		searchDto.setParkingZipCdError(CodeConstant.DOUBLE_QUOTATION);
		
		//入力チェック
		//必須チェック
		if (SkfCheckUtils.isNullOrEmpty(searchDto.getParkingZipCd())) {
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1048, "郵便番号");
			searchDto.setParkingZipCdError(CodeConstant.NFW_VALIDATION_ERROR);
			return searchDto;
		}
		//形式チェック
		String parkingZipCode = searchDto.getParkingZipCd();
		if(!CheckUtils.isNumeric(parkingZipCode) || parkingZipCode.length() != 7){
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1042, "郵便番号");
			searchDto.setParkingZipCdError(CodeConstant.NFW_VALIDATION_ERROR);
			return searchDto;
		}
		
		//住所情報取得
		Skf3010Sc007GetZipToAddressExpParameter param = new Skf3010Sc007GetZipToAddressExpParameter();
		param.setPost7Cd(parkingZipCode);
		List<Skf3010Sc007GetZipToAddressExp> resultList = new ArrayList<Skf3010Sc007GetZipToAddressExp>();
		resultList = skf3010Sc007GetZipToAddressExpRepository.getZipToAddress(param);
		
		// 取得データレコード数判定
		if (resultList.size() <= 0) {
			// 取得データレコード数が0件場合、エラー設定して処理終了
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1047);
			searchDto.setParkingZipCdError(CodeConstant.NFW_VALIDATION_ERROR);
			return searchDto;
		}
		
		String adderss = resultList.get(0).getAddress();
		
		//住所設定
		searchDto.setParkingAddress(adderss);
		
		return searchDto;
	}
	
		
}
