/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformation;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008.Skf3090Sc008CheckRegistAsyncDto;

/**
 * お知らせマスタメンテナンス画面のCheckRegistAsyncサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc008CheckRegistAsyncService extends SkfAsyncServiceAbstract<Skf3090Sc008CheckRegistAsyncDto> {
	
	@Autowired
	private Skf3090Sc008SharedService skf3090Sc008SharedService;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param craDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc008CheckRegistAsyncDto index(Skf3090Sc008CheckRegistAsyncDto craDto) throws Exception {
		
		boolean dialogFlg = false;
		
		String openDate = craDto.getOpenDateBox();
		
		//入力チェックを行う
		//「公開開始日」未入力チェック
		if(openDate ==null || CheckUtils.isEmpty(openDate.trim())){
			ServiceHelper.addErrorResultMessage(craDto, new String[] { "openDateBox" }, MessageIdConstant.E_SKF_1048, "公開開始日");
			throwBusinessExceptionIfErrors(craDto.getResultMessages());
			//形式チェック
		}else if(!(SkfCheckUtils.isSkfDateFormat(openDate, CheckUtils.DateFormatType.YYYYMMDD))){
			ServiceHelper.addErrorResultMessage(craDto, new String[] { "openDateBox" }, MessageIdConstant.E_SKF_1025, "公開開始日");
			throwBusinessExceptionIfErrors(craDto.getResultMessages());
		}

		openDate = openDate.replace("/", "");
		
		Skf1010TInformation informationData = skf3090Sc008SharedService.getInformation(companyCd, openDate);
		//お知らせの公開開始日が重複しているか
		if(informationData != null){
			dialogFlg = true;
		}
		
		LogUtils.debugByMsg("重複チェック：" + dialogFlg);
		craDto.setDialogFlg(dialogFlg);
		
		return craDto;
	}
	
}
