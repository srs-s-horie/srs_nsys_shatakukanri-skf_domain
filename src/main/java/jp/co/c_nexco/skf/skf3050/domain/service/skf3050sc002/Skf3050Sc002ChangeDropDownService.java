/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc002;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc002.Skf3050Sc002ChangeDropDownDto;

/**
 * Skf3050Sc002ChangeDropDownService 月次運用管理画面の対象年度ドロップダウン変更
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc002ChangeDropDownService extends BaseServiceAbstract<Skf3050Sc002ChangeDropDownDto> {

	@Autowired
	private Skf3050Sc002SharedService skf3050Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	protected BaseDto index(Skf3050Sc002ChangeDropDownDto changeDropDownDto) throws Exception {

		skfOperationLogUtils.setAccessLog("対象年度ドロップダウン変更処理開始", CodeConstant.C001, changeDropDownDto.getPageId());

		String targetYyyymm = changeDropDownDto.getHdnSelectedTaisyonendo();

		List<Map<String, Object>> gridList = skf3050Sc002SharedService.createGetsujiGrid(targetYyyymm);
		changeDropDownDto.setGetujiGrid(gridList);

		changeDropDownDto = (Skf3050Sc002ChangeDropDownDto) skf3050Sc002SharedService
				.getJikkoushijiHighlightData(changeDropDownDto, changeDropDownDto.getHdnJikkouShijiYoteiNengetsu());

		changeDropDownDto = (Skf3050Sc002ChangeDropDownDto) skf3050Sc002SharedService.setBtnMsg(changeDropDownDto);

		changeDropDownDto = (Skf3050Sc002ChangeDropDownDto) skf3050Sc002SharedService
				.changeButtonStatus(changeDropDownDto);

		return changeDropDownDto;
	}

}
