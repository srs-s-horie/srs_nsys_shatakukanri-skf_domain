/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc001.Skf3030Sc001ChangeCompanyDropDownAsyncDto;

/**
 * Skf3030Sc001ChangeCompanyDropDownAsyncService 社宅管理台帳「管理会社」ドロップダウン変更処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc001ChangeCompanyDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3030Sc001ChangeCompanyDropDownAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	@Override
	protected AsyncBaseDto index(Skf3030Sc001ChangeCompanyDropDownAsyncDto inDto) throws Exception {

		skfOperationLogUtils.setAccessLog("社宅管理台帳「管理会社」ドロップダウン変更処理", CodeConstant.C001,
				FunctionIdConstant.SKF3030_SC001);

		String companyCd = inDto.getHdnAsyncCompanyAgencySelect();
		List<Map<String, Object>> agencyDropDownList = new ArrayList<Map<String, Object>>();

		inDto.setHdnAsyncAgencyDisabled(Skf3030Sc001SharedService.ABLED);

		if (!NfwStringUtils.isEmpty(companyCd)) {
			if (Skf3030Sc001SharedService.CD_EXTERNAL_AGENCY.equals(companyCd)) {
				inDto.setHdnAsyncAgencyDisabled(Skf3030Sc001SharedService.DISABLED);

			} else {
				agencyDropDownList = skfDropDownUtils.getDdlAgencyByCd(companyCd, "", true);
			}
		}

		inDto.setAgencyDropDownList(agencyDropDownList);

		return inDto;
	}

}
