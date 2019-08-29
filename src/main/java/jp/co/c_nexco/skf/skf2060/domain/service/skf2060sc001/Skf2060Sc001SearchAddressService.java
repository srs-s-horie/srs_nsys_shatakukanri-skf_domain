/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008SelectDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001SearchAddressDto;

/**
 * TestPrjTop画面のSearchAddressサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001SearchAddressService extends BaseServiceAbstract<Skf2060Sc001SearchAddressDto> {
	
	@Autowired
	private Skf2060Sc001PostalCodeAddressExpRepository skf2060Sc001PostalCodeAddressExpRepository;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2060Sc001SearchAddressDto index(Skf2060Sc001SearchAddressDto searchAddressDto) throws Exception {
		
		searchAddressDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		Skf2060Sc001PostalCodeAddressExp resultEntity = new Skf2060Sc001PostalCodeAddressExp();
		Skf2060Sc001PostalCodeAddressExpParameter param = new Skf2060Sc001PostalCodeAddressExpParameter();
		
		//「郵便番号」が入力されている場合
		if(!(searchAddressDto.getPostalCd() == null || CheckUtils.isEmpty(searchAddressDto.getPostalCd().trim()))){
			//郵便番号で住所検索
			param.setPostalCd(searchAddressDto.getPostalCd());
			resultEntity = skf2060Sc001PostalCodeAddressExpRepository.getAddressInfo(param);
			
			//該当する住所が無かった場合
			if(resultEntity == null){
				ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "postalCd", "address" }, MessageIdConstant.E_SKF_1047);
			}
		//「郵便番号」が未入力で、「住所」が入力されている場合
		}else if(!(searchAddressDto.getAddress() == null || CheckUtils.isEmpty(searchAddressDto.getAddress().trim()))){
			//住所で郵便番号を検索
			param.setAddress(searchAddressDto.getAddress());
			resultEntity = skf2060Sc001PostalCodeAddressExpRepository.getAddressInfo(param);
			
			//該当する住所が無かった場合
			if(resultEntity == null){
				ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "postalCd", "address" }, MessageIdConstant.E_SKF_1047);
			}
		//「郵便番号」と「住所」が未入力の場合
		}else{
			ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "postalCd" }, MessageIdConstant.E_SKF_1048, "郵便番号");
		}
		
		//対象の住所が存在する場合
		if(resultEntity != null){
			//どうにかしたんもんだねぇ(´・ω・｀)
			if(resultEntity.getPostalCd()!=null&&resultEntity.getPrefName()!=null&&resultEntity.getCityName()!=null&&resultEntity.getAreaName()!=null){
				//画面の郵便番号と住所に検索結果から取得した値を挿入
				System.out.println("(｀・ω・´)住所は正しく入ったよ");
				searchAddressDto.setPostalCd(resultEntity.getPostalCd());
				searchAddressDto.setAddress(resultEntity.getPrefName()+resultEntity.getCityName()+resultEntity.getAreaName());
			}
		}
		return searchAddressDto;
	}
	
}
