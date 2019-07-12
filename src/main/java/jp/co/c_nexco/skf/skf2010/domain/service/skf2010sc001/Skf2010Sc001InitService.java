/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetShainMasterInfoByParameterExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc001.Skf2010Sc001InitDto;

/**
 * 社員名入力支援画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf2010Sc001InitService extends BaseServiceAbstract<Skf2010Sc001InitDto> {

	@Autowired
	private Skf2010Sc001SharedService skf2010Sc001SharedService;

	private String companyCd = CodeConstant.C001;

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
	public Skf2010Sc001InitDto index(Skf2010Sc001InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC001_TITLE);

		init(initDto);

		// 初期表示では検索結果は0件表示
		List<Skf2010Sc001GetShainMasterInfoByParameterExp> shainInfoList = new ArrayList<Skf2010Sc001GetShainMasterInfoByParameterExp>();
		initDto.setListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));

		return initDto;
	}

	/**
	 * 初期化処理
	 * 
	 * @param initDto
	 */
	private void init(Skf2010Sc001InitDto initDto) {
		initDto.setShainNo("");
		initDto.setName("");
		initDto.setNameKk("");
		initDto.setAgency("");
		initDto.setErrShainNo("");
		initDto.setErrAgency("");
		initDto.setErrName("");
		initDto.setErrNameKk("");
	}

}
