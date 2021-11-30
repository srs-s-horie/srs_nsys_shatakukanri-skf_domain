/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100MMobileRouterRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008DeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf2100Sc006RegistService モバイルルーターマスタ登録画面の削除処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc008DeleteService extends SkfServiceAbstract<Skf2100Sc008DeleteDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2100MMobileRouterRepository skf2100MMobileRouterRepository;


	/**
	 * サービス処理を行う。　
	 * 
	 * @param registDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2100Sc008DeleteDto index(Skf2100Sc008DeleteDto delDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);

		// ドロップダウンリストを設定
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, delDto.getContractKbnSelect(), true);
		delDto.setContractKbnDropDownList(contractKbnDropDownList);
		if(!"true".equals(delDto.getHdnChkFaultSelect())){
			delDto.setHdnChkFaultSelect(null);
		}
		
		// 対象通しNo
		Long mobileRouterNo = Long.parseLong(delDto.getRouterNo());
		
		// 利用実績チェック
		int rirekiCount = skfRouterInfoUtils.getRouterRirekiCount(mobileRouterNo);
		if(rirekiCount > 0){
			// 実績有
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_3060,"モバイルルーター: "+ delDto.getRouterNo());
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}
		
		// 削除処理
		boolean delResult = false;
		delResult = deleteRouterData(delDto, mobileRouterNo);
		
		if(delResult){
			// 成功時画面遷移
			// 正常時
			ServiceHelper.addResultMessage(delDto, MessageIdConstant.I_SKF_1013);
			// 画面遷移（戻る）
			TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF2100_SC007, "init");
			delDto.setTransferPageInfo(nextPage);
		}

		return delDto;
	}
	

	/**
	 * 削除処理(トランザクション部分)
	 * 
	 * @param registDto
	 *            DTO
	 * @return 登録成否
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	public boolean deleteRouterData(Skf2100Sc008DeleteDto delDto,Long mobileRouterNo){
		
		// モバイルルーターマスタ削除
		Skf2100MMobileRouterKey delKey = new  Skf2100MMobileRouterKey();
		delKey.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		delKey.setMobileRouterNo(mobileRouterNo);
		LogUtils.infoByMsg("モバイルルーターマスタ削除:通しNo" + mobileRouterNo);
		int delCount = skf2100MMobileRouterRepository.deleteByPrimaryKey(delKey);
		if(delCount <= 0){
			// 削除失敗
			LogUtils.infoByMsg("モバイルルーターマスタ削除異常：削除件数" + delCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return false;
		}
		
		
		return true;
	}
	

	

}
