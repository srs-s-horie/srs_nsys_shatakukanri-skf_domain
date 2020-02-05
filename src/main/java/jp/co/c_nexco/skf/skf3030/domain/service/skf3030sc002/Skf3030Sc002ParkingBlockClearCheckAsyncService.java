/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetParkingInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002ParkingBlockClearCheckAsyncDto;

/**
 * Skf3030Sc002ParkingBlockClearCheckAsyncService 入退居情報登録画面：駐車場区画クリア処理非同期処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002ParkingBlockClearCheckAsyncService
	extends AsyncBaseServiceAbstract<Skf3030Sc002ParkingBlockClearCheckAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf3030Sc002GetParkingInfoExpRepository skf3030Sc002GetParkingInfoExpRepository;

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
	public Skf3030Sc002ParkingBlockClearCheckAsyncDto index(Skf3030Sc002ParkingBlockClearCheckAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("駐車場区画クリア");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場区画クリア", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);

		//前月取得
		String zengetu = skfDateFormatUtils.addYearMonth(asyncDto.getHdnNengetsu(),-1);
		
		if(asyncDto.getHdnShatakuKanriId() == null || 
			asyncDto.getHdnShatakuKanriNo() == null ||
			asyncDto.getHdnChushajoKanriNo() == null){
			asyncDto.setCheckResult("true");
			return asyncDto;
		}
		
		//前月の駐車場履歴情報を取得
		Skf3030Sc002GetParkingInfoExpParameter parkingParam = new Skf3030Sc002GetParkingInfoExpParameter();
		parkingParam.setShatakuKanriId(Long.parseLong(asyncDto.getHdnShatakuKanriId()));
		parkingParam.setShatakuKanriNo(Long.parseLong(asyncDto.getHdnShatakuKanriNo()));
		parkingParam.setYearMonth(zengetu);
		List<Skf3030Sc002GetParkingInfoExp> paringDt = new ArrayList<Skf3030Sc002GetParkingInfoExp>();
		paringDt = skf3030Sc002GetParkingInfoExpRepository.getParkingInfo(parkingParam);
		Skf3030Sc002GetParkingInfoExp zengetuParkingDt = null;
		if(paringDt.size() > 0){
			for(Skf3030Sc002GetParkingInfoExp dr : paringDt){
				if(dr.getParkingLendNo().equals(Long.parseLong(asyncDto.getParkingLendNo())) && 
						dr.getParkingKanriNo().equals(Long.parseLong(asyncDto.getHdnChushajoKanriNo()))){
					zengetuParkingDt = dr;
					break;
				}
			}
		}
		//下記条件の場合、駐車場情報クリア可能
		//条件①：区画1に前月の実績がない
		//条件②：区画1に前月の実績があるが、利用終了日が設定されている。(一旦、利用終了しているため、当月分は新規登録扱い。)
		if((zengetuParkingDt == null) ||
				!SkfCheckUtils.isNullOrEmpty(zengetuParkingDt.getParkingEndDate())){
			
			asyncDto.setCheckResult("true");
		}else{
			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.E_SKF_3055);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
			asyncDto.setCheckResult("false");
		}
		
		return asyncDto;
	}
}
