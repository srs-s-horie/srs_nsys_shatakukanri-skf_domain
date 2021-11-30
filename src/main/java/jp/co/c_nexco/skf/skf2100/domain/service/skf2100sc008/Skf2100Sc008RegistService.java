/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc008;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc008.Skf2100Sc008UpdateMMobileRouterExpRepository;
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
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc008common.Skf2100Sc008CommonDto;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc008.Skf2100Sc008RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf2100Sc006RegistService モバイルルーターマスタ登録画面の登録処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc008RegistService extends SkfServiceAbstract<Skf2100Sc008RegistDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf2100Sc008SharedService skf2100Sc008SharedService;
	@Autowired
	private Skf2100MMobileRouterRepository skf2100MMobileRouterRepository;
	@Autowired
	private Skf2100Sc008UpdateMMobileRouterExpRepository skf2100Sc008UpdateMMobileRouterExpRepository;

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
	public Skf2100Sc008RegistDto index(Skf2100Sc008RegistDto registDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, FunctionIdConstant.SKF2100_SC008);

		// ドロップダウンリストを設定
		List<Map<String, Object>> contractKbnDropDownList = skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_ROUTER_CONTRACT_KBN, registDto.getContractKbnSelect(), true);
		registDto.setContractKbnDropDownList(contractKbnDropDownList);
		if(!"true".equals(registDto.getHdnChkFaultSelect())){
			registDto.setHdnChkFaultSelect(null);
		}
		
		// 共通チェック
		if (!skf2100Sc008SharedService.validateInput(registDto) ){
			// エラー有終了
			return registDto;
		}
		
		// 重複チェック
		if (!skf2100Sc008SharedService.checkDuplicate(registDto) ){
			// エラー有終了
			return registDto;
		}
		
		boolean regResult = false;
		// 新規登録フラグ
		if (registDto.isNewDataFlag()){
			// 新規登録
			regResult = registClickInsert(registDto);
		}else{
			// 更新
			regResult = registClickUpdate(registDto);
		}
		
		if(regResult){
			// 成功時画面遷移
			// 画面遷移（戻る）
			TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF2100_SC007, "init");
			registDto.setTransferPageInfo(nextPage);
		}

		return registDto;
	}
	

	/**
	 * 登録処理(トランザクション部分)
	 * 
	 * @param registDto
	 *            DTO
	 * @return 登録成否
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	public boolean registClickInsert(Skf2100Sc008RegistDto registDto){
		
		// モバイルルーターマスタ登録
		// データ設定
		Skf2100MMobileRouterWithBLOBs routerData = skf2100Sc008SharedService.setRouterData(registDto);
		
		LogUtils.infoByMsg("モバイルルーターマスタ登録:通しNo" + registDto.getRouterNo());
		int inCount = skf2100MMobileRouterRepository.insertSelective(routerData);
		if(inCount <= 0){
			// 登録失敗
			LogUtils.infoByMsg("モバイルルーターマスタ登録異常:更新件数" + inCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
			return false;
		}
	
		// 正常時
		ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		
		return true;
	}
	
	/**
	 * 更新処理(トランザクション部分)
	 * 
	 * @param registDto
	 *            DTO
	 * @throws Exception
	 *             例外
	 */
	@Transactional
	public boolean registClickUpdate(Skf2100Sc008RegistDto registDto){
		
		// モバイルルーターマスタ更新
		// データ設定
		Skf2100MMobileRouterWithBLOBs routerData = skf2100Sc008SharedService.setRouterData(registDto);
		// 排他用最終更新日設定
		routerData.setLastUpdateDate(registDto.getLastUpdateDate(Skf2100Sc008CommonDto.ROUTER_KEY_LAST_UPDATE_DATE));
		
		LogUtils.infoByMsg("モバイルルーターマスタ更新:通しNo" + registDto.getRouterNo());
		int updCount = skf2100Sc008UpdateMMobileRouterExpRepository.updateByPrimaryKeySelective(routerData);
		if(updCount <= 0){
			// 更新失敗
			LogUtils.infoByMsg("モバイルルーターマスタ更新異常:更新件数" + updCount);
			// エラーメッセージを設定
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
			return false;
		}
		
		// 正常
		// 更新完了メッセージ
		ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
		
		return true;
	}
	

}
