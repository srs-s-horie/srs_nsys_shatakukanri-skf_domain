/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetPostalCodeAddressExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001SearchAddressDto;

/**
 * SKF2060_SC001 借上候補物件登録画面のSearchAddressサービス処理クラス。
 * 
 */
@Service
public class Skf2060Sc001SearchAddressService extends SkfServiceAbstract<Skf2060Sc001SearchAddressDto> {

	@Autowired
	private SkfGetInfoUtilsGetPostalCodeAddressExpRepository skfGetInfoUtilsGetPostalCodeAddressExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param searchAddressDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001SearchAddressDto index(Skf2060Sc001SearchAddressDto searchAddressDto) throws Exception {

		searchAddressDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("住所を検索", companyCd, searchAddressDto.getPageId());

		List<SkfGetInfoUtilsGetPostalCodeAddressExp> resultEntity = new ArrayList<SkfGetInfoUtilsGetPostalCodeAddressExp>();
		SkfGetInfoUtilsGetPostalCodeAddressExpParameter param = new SkfGetInfoUtilsGetPostalCodeAddressExpParameter();

		// 「郵便番号」が入力されている場合
		if (!(searchAddressDto.getPostalCd() == null || CheckUtils.isEmpty(searchAddressDto.getPostalCd().trim()))) {
			// 郵便番号で住所検索
			param.setPostalCd(searchAddressDto.getPostalCd());
			resultEntity = skfGetInfoUtilsGetPostalCodeAddressExpRepository.getAddressInfo(param);

			// 該当する郵便番号データが無かった場合
			if (resultEntity.size() <= 0) {
				ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "postalCd" },
						MessageIdConstant.E_SKF_1047);
			}
			// 「郵便番号」が未入力で、「住所」が入力されている場合
		} else if (!(searchAddressDto.getAddress() == null
				|| CheckUtils.isEmpty(searchAddressDto.getAddress().trim()))) {
			// 住所で郵便番号を検索
			param.setAddress(searchAddressDto.getAddress());
			resultEntity = skfGetInfoUtilsGetPostalCodeAddressExpRepository.getAddressInfo(param);

			// 該当する郵便番号データが無かった場合
			if (resultEntity.size() <= 0) {
				ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "address" },
						MessageIdConstant.E_SKF_2019);
			}
			// 「郵便番号」と「住所」が未入力の場合
		} else {
			ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "postalCd" },
					MessageIdConstant.E_SKF_1048, "郵便番号");
			ServiceHelper.addErrorResultMessage(searchAddressDto, new String[] { "address" },
					MessageIdConstant.E_SKF_1048, "住所");
		}

		// 郵便番号データが存在する場合
		if (resultEntity.size() > 0) {
			if (resultEntity.get(0).getPostalCd() != null && resultEntity.get(0).getPrefName() != null
					&& resultEntity.get(0).getCityName() != null && resultEntity.get(0).getAreaName() != null) {
				// 「郵便番号」と「住所」に検索結果から取得した値をそれぞれ設定
				searchAddressDto.setPostalCd(resultEntity.get(0).getPostalCd());
				searchAddressDto.setAddress(resultEntity.get(0).getPrefName() + resultEntity.get(0).getCityName()
						+ resultEntity.get(0).getAreaName());
			}
		}
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg ,searchAddressDto.getShainNo(), searchAddressDto.getApplNo());
		searchAddressDto.setListTableData(dataParamList);
		
		return searchAddressDto;
	}

}
