/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002ChangeYakuinSanteiAsyncDto;

/**
 * Skf3022Sc006ChangeYakuinSanteiAsyncService 提示データ登録画面：役員算定変更非同期呼出処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3030Sc002ChangeYakuinSanteiAsyncService
	extends SkfAsyncServiceAbstract<Skf3030Sc002ChangeYakuinSanteiAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
	
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
	public Skf3030Sc002ChangeYakuinSanteiAsyncDto index(Skf3030Sc002ChangeYakuinSanteiAsyncDto asyncDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("役員算定変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("役員算定変更", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);
		
		//「役員算定」が"役員"または"執行役員"が選択された、「社宅賃貸料」が設定されていない場合
		if((CodeConstant.YAKUIN_KBN_YAKUIN.compareTo(asyncDto.getYakuinSanteiSelectedValue()) == 0 ||
			CodeConstant.YAKUIN_KBN_SHIKKO.compareTo(asyncDto.getYakuinSanteiSelectedValue()) == 0 ) 
				&& SkfCheckUtils.isNullOrEmpty(asyncDto.getTxtShatakuChintairyo())){
//			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.W_SKF_1008,"社宅賃貸料");
//			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
			//メッセージ設定はしているが、現行動かすとメッセージ表示はしない（意図的？たまたま？）
			asyncDto.setSc006ChintaiRyoErr(CodeConstant.NFW_VALIDATION_ERROR);
		}else{
			asyncDto.setSc006ChintaiRyoErr("");
		}
		
		
		//「区画１ 区画番号」または「区画２ 区画番号」が設定されている、且つ、「駐車場料金」が設定されていない場合
		if((!SkfCheckUtils.isNullOrEmpty(asyncDto.getLitKukaku1No()) || 
			!SkfCheckUtils.isNullOrEmpty(asyncDto.getLitKukaku2No()) )
			&& SkfCheckUtils.isNullOrEmpty(asyncDto.getTxtChishajoRyokin())){
//			ServiceHelper.addErrorResultMessage(asyncDto, null, MessageIdConstant.W_SKF_1008,"駐車場料金");
//			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
			//メッセージ設定はしているが、現行動かすとメッセージ表示はしない（意図的？たまたま？）
			asyncDto.setSc006TyusyajoRyokinErr(CodeConstant.NFW_VALIDATION_ERROR);
		}else{
			asyncDto.setSc006TyusyajoRyokinErr("");
		}
		// エラーなしの場合は、再計算
		if(SkfCheckUtils.isNullOrEmpty(asyncDto.getSc006ChintaiRyoErr()) &&
				SkfCheckUtils.isNullOrEmpty(asyncDto.getSc006TyusyajoRyokinErr()) ){
			skf3030Sc002SharedService.siyoryoKeiSanAsync(asyncDto);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		
		return asyncDto;
	}
}
