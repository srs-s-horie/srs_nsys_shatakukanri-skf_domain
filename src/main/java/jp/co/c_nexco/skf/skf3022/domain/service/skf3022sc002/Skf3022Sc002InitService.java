/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc002;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc002.Skf3022Sc002GetChushajoInfoExp;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc002.Skf3022Sc002InitDto;

/**
 * Skf3022Sc002InitService 駐車場入力支援画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc002InitService extends SkfServiceAbstract<Skf3022Sc002InitDto> {

	@Autowired
	private Skf3022Sc002SharedService skf3022Sc002SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Value("${skf3022.skf3022_sc002.max_search_count}")
	private String maxCount;
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
	public Skf3022Sc002InitDto index(Skf3022Sc002InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC002_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3022_SC002);
		
		init(initDto);

		// 初期表示では検索結果は0件表示
		List<Skf3022Sc002GetChushajoInfoExp> parkingInfoList = new ArrayList<Skf3022Sc002GetChushajoInfoExp>();
		initDto.setListTableList(skf3022Sc002SharedService.createListTable(parkingInfoList));

		return initDto;
	}

	/**
	 * 初期化処理
	 * 
	 * @param initDto
	 */
	private void init(Skf3022Sc002InitDto initDto) {
		initDto.setShatakuName(initDto.getHdnShatakuName());
		initDto.setShiyosha(CodeConstant.DOUBLE_QUOTATION);
		initDto.setAkiParking(new String[]{"1"});//「空き駐車場」の初期値:チェック
		initDto.setParkingBiko(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkNo(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkBlock(CodeConstant.DOUBLE_QUOTATION);
		initDto.setParkRentalAsjust(CodeConstant.DOUBLE_QUOTATION);
		initDto.setEndDay(CodeConstant.DOUBLE_QUOTATION);
		initDto.setMaxCount(maxCount);
	}

}
