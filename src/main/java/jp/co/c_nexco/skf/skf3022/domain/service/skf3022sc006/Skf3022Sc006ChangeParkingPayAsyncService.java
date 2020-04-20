/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ChangeParkingPayAsyncDto;

/**
 * Skf3022Sc006ChangeYakuinSanteiAsyncService 提示データ登録画面：駐車場料金変更非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc006ChangeParkingPayAsyncService
	extends SkfAsyncServiceAbstract<Skf3022Sc006ChangeParkingPayAsyncDto> {

	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

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
	public Skf3022Sc006ChangeParkingPayAsyncDto index(Skf3022Sc006ChangeParkingPayAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("駐車場料金変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("駐車場料金変更", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		// 使用料計算
//		if (!skf3022Sc006SharedService.threeShiyouryoCalcAsync(asyncDto)) {
//			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
//		}
		skf3022Sc006SharedService.threeShiyouryoCalcAsync(asyncDto, true);
		return asyncDto;
	}
}
