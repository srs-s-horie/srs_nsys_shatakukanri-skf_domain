/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetShatakuParkingBlockExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common.Skf3022Sc006CommonDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ParkingSupportCallBackAsyncDto;

/**
 * Skf3022Sc006ParkingSupportCallBackAsyncService 提示データ登録画面：駐車場入力支援コールバック時非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc006ParkingSupportCallBackAsyncService
	extends AsyncBaseServiceAbstract<Skf3022Sc006ParkingSupportCallBackAsyncDto> {

	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006GetShatakuParkingBlockExpRepository skf3022Sc006GetShatakuParkingBlockExpRepository;

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
	public Skf3022Sc006ParkingSupportCallBackAsyncDto index(Skf3022Sc006ParkingSupportCallBackAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("駐車場入力支援");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場入力支援", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		// 日付フォーマット
		SimpleDateFormat dateFormat = new SimpleDateFormat(Skf3022Sc006CommonDto.DATE_FORMAT);
		/** パラメータ取得 */
		// 使用料計算用Map
		Map<String, String> paramMap = asyncDto.getMapParam();
		// 戻り値初期化
		skf3022Sc006SharedService.initializeSiyoryoKeiSanParamAsync(asyncDto);
		// 使用料計算処理
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		if (skf3022Sc006SharedService.siyoryoKeiSan(asyncDto.getSc006ChushajoKanriNo(),
				asyncDto.getSc006ParkBlockKind(), paramMap, resultMap, errMsg)) {
			// 使用料計算でエラー
//			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.SKF3020_ERR_MSG_COMMON, errMsg);
//			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		} else {
			// 使用料計算戻り値設定
			skf3022Sc006SharedService.setSiyoryoKeiSanParamAsync(resultMap, asyncDto);
		}
		// 社宅駐車場区画情報マスタ（区画）を取得
		List<Skf3022Sc006GetShatakuParkingBlockExp> parkingBlockList = new ArrayList<Skf3022Sc006GetShatakuParkingBlockExp>();
		Skf3022Sc006GetShatakuParkingBlockExpParameter parkingBlockParam = new Skf3022Sc006GetShatakuParkingBlockExpParameter();
		// 社宅管理番号
		Long shatakuKanriNo = CheckUtils.isEmpty(asyncDto.getHdnShatakuKanriNo()) ? null : Long.parseLong(asyncDto.getHdnShatakuKanriNo());
		// 駐車場(区画)管理番号
		Long parkingKanriNo = CheckUtils.isEmpty(asyncDto.getSc006ChushajoKanriNo()) ? null : Long.parseLong(asyncDto.getSc006ChushajoKanriNo());
		if (shatakuKanriNo != null && parkingKanriNo != null) {
			parkingBlockParam.setShatakuKanriNo(shatakuKanriNo);
			parkingBlockParam.setParkingKanriNo(parkingKanriNo);
			parkingBlockList = skf3022Sc006GetShatakuParkingBlockExpRepository.getShatakuParkingBlock(parkingBlockParam);
		}
		// 結果判定
		if (parkingBlockList.size() > 0) {
			// 駐車場区画情報マスタ(区画)更新日設定
			asyncDto.setHdnParkingBlockUpdateDate(dateFormat.format(parkingBlockList.get(0).getUpdateDate()));
		}
		return asyncDto;
	}
}
