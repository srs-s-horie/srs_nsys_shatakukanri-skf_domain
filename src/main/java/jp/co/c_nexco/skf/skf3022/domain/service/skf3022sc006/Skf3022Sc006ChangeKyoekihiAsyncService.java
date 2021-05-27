/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ChangeKyoekihiAsyncDto;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006ChangeNyutaikyoYoteiAsyncDto;

/**
 * Skf3022Sc006ChangeNyutaikyoYoteiAsyncService 提示データ登録画面：個人負担共益費変更非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc006ChangeKyoekihiAsyncService
	extends SkfAsyncServiceAbstract<Skf3022Sc006ChangeKyoekihiAsyncDto> {

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
	public Skf3022Sc006ChangeKyoekihiAsyncDto index(Skf3022Sc006ChangeKyoekihiAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("共益費変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("共益費変更", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);
		/** パラメータ取得 */
		// 使用料計算用Map
		Map<String, String> paramMap = asyncDto.getMapParam();
		// 戻り値初期化
		skf3022Sc006SharedService.initializeKyoekihiKeiSanParamAsync(asyncDto);
		// 使用料計算処理
		Map<String, String> resultMap = new HashMap<String, String>();	// 使用料計算戻り値
		StringBuffer errMsg = new StringBuffer();						// エラーメッセージ
		
		// 共益費日割計算対応 2021/5/14 add start
		// 共益費月額
		String kyoekihiMonthPay = asyncDto.getSc006KyoekihiMonthPay();
		String sc006KyoekihiPayMonthSelect = asyncDto.getSc006KyoekihiPayMonthSelect();
		String sc006KyoekihiTyoseiPay = asyncDto.getSc006KyoekihiTyoseiPay();
		if(skf3022Sc006SharedService.kyoekihiKeiSan(kyoekihiMonthPay, sc006KyoekihiPayMonthSelect, sc006KyoekihiTyoseiPay, paramMap, resultMap, errMsg)){
			// 共益費計算でエラー
		}else{
			//共益費計算戻り値設定
			skf3022Sc006SharedService.setKyoekihiKeiSanParamAsync(resultMap, asyncDto);
		}
		// 共益費日割計算対応 2021/5/14 add end
		
		
		return asyncDto;
	}
}
