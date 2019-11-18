/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetRirekiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetRirekiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetShikyuKagakuCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetShikyuKagakuCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKindKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetRirekiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetShikyuKagakuCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindBikoRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc002.Skf3090Sc002DeleteDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002.Skf3090Sc002SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc002DeleteService 現物支給価額マスタ登録画面のDeleteサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc002DeleteService extends BaseServiceAbstract<Skf3090Sc002DeleteDto> {
	
	@Autowired
	Skf3090Sc002SharedService skf3090Sc002SharedService;

	@Autowired
	Skf3090Sc002GetShikyuKagakuCountExpRepository skf3090Sc002GetShikyuKagakuCountExpRepository;
	
	@Autowired
	Skf3090Sc002GetRirekiCountExpRepository skf3090Sc002GetRirekiCountExpRepository;
	
	@Autowired
	Skf3050MPayInKindRepository skf3050MPayInKindRepository;
	
	@Autowired
	Skf3050MPayInKindBikoRepository skf3050MPayInKindBikoRepository;	
	
	@Autowired
	Skf3090Sc002GetBikokaiteibiCountExpRepository skf3090Sc002GetBikokaiteibiCountExpRepository;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3090Sc002DeleteDto index(Skf3090Sc002DeleteDto deleteDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());		

		// エラースタイルをクリア
		skf3090Sc002SharedService.errorStyleClear(deleteDto);

		// 改定日を取得（削除時は必ず入っている＆編集不可）
		String effectiveDate = skf3090Sc002SharedService.replaceDateFormat(deleteDto.getEffectiveDate().trim());
		
		// 使用実績を取得
		int searchCount = getRireki(deleteDto, effectiveDate.substring(0,6));
		if(searchCount > 0){
			// error.skf.e_skf_3023=該当の現物支給価額マスタは、使用履歴があるため削除できません。
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_3023);
			// この段階ではロールバックする必要はないので、そのままreturn
		}else{
			// 削除
			boolean isGotoPrevPage = false;
			int deleteCount = deleteGenbutsuShikyuKagakuList(deleteDto, effectiveDate);
			if(0 == deleteCount){
				// 	排他エラー(warning.skf.w_skf_1009=他のユーザによって更新されています。もう一度画面を更新してください。)
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.W_SKF_1009);
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
			}else if(-1 == deleteCount){
				// 削除エラー（error.skf.e_skf_1076=削除時にエラーが発生しました。ヘルプデスクへ連絡してください。）
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
			}else{
				// 削除完了（現物支給価額マスタ一覧画面へ遷移）
				// メッセージは表示なし
				isGotoPrevPage = true;
			}
			if(true == isGotoPrevPage){
				// 現物支給価額マスタ一覧画面へ遷移
				deleteDto.setTransferPageInfo(TransferPageInfo.prevPage(FunctionIdConstant.SKF3090_SC001));
			}
		}
		return deleteDto;
	}
	
	/**
	 * 使用実績を取得する
	 * 
	 * @param deleteDto インプットDTO
	 * @param kaiteibi 改定日
	 * @return 処理結果
	 */
	private int getRireki(Skf3090Sc002DeleteDto deleteDto, String kaiteibi){
		List<Skf3090Sc002GetRirekiCountExp> resultData = new ArrayList<Skf3090Sc002GetRirekiCountExp>();
		Skf3090Sc002GetRirekiCountExpParameter param = new Skf3090Sc002GetRirekiCountExpParameter();
		param.setYearMonth(kaiteibi);
		resultData = skf3090Sc002GetRirekiCountExpRepository.getRirekiCount(param);
		return resultData.get(0).getRowCount();
	}
	
	
	/**
	 * 削除処理る
	 * 
	 * @param deleteDto インプットDTO
	 * @param effectiveDate 改定日
	 * @return 処理結果
	 */
	private int deleteGenbutsuShikyuKagakuList(Skf3090Sc002DeleteDto deleteDto, String effectiveDate){
		
		// 都道府県コード取得
		//Map<String, String> genericCodeMapPref = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		int resultCount = 0;
		try{
			// 現物支給価額テーブル削除
			for(int i = 0; i < skf3090Sc002SharedService.ABROAD_CD; i++){
				/*
				// 都道府県コードを取得
				String prefCd = String.format("%2s", String.valueOf(i + 1)).replace(" ", "0");
				genericCodeMapPref.get("01")
				*/
					String prefCd = String.format("%2s", String.valueOf(i + 1)).replace(" ", "0");
					List<Skf3090Sc002GetShikyuKagakuCountExp> resultShikyuKagakuCount = new ArrayList<Skf3090Sc002GetShikyuKagakuCountExp>();
					Skf3090Sc002GetShikyuKagakuCountExpParameter param = new Skf3090Sc002GetShikyuKagakuCountExpParameter();
					param.setEffectiveDateParam(effectiveDate);
					param.setPrefCd(prefCd);
					resultShikyuKagakuCount = skf3090Sc002GetShikyuKagakuCountExpRepository.getShikyuKagakuCount(param);			
					
					if(resultShikyuKagakuCount.get(0).getRowCount() > 0){
						// データがある場合のみ削除（データがない場合は何もしない）
						Skf3050MPayInKindKey key = new Skf3050MPayInKindKey();
						key.setEffectiveDate(effectiveDate);
						key.setPrefCd(prefCd);
						int retkaiteibiCount = skf3050MPayInKindRepository.deleteByPrimaryKey(key);
						if(0 == retkaiteibiCount){
							resultCount = 0;
							break;
						}else{
							resultCount ++;
						}
						
					}
				
			}
		}catch(Exception ex){
			LogUtils.debugByMsg("現物支給価額テーブル削除時に例外発生");
			resultCount = -1;
		}
		
		if(0 == resultCount || -1 == resultCount){
			// エラーなので、呼び出しもとへ戻す
			return resultCount;
		}		
		
		try{
			// 現物支給価額備考テーブル削除
			List<Skf3090Sc002GetBikokaiteibiCountExp> resultBikokaiteibiCount = new ArrayList<Skf3090Sc002GetBikokaiteibiCountExp>();
			Skf3090Sc002GetBikokaiteibiCountExpParameter param = new Skf3090Sc002GetBikokaiteibiCountExpParameter();
			param.setEffectiveDateParam(effectiveDate);
			resultBikokaiteibiCount = skf3090Sc002GetBikokaiteibiCountExpRepository.getBikokaiteibiCount(param);
			
			if(resultBikokaiteibiCount.get(0).getRowCount() > 0){
				// データがある場合のみ削除（データがない場合は何もしない
				int retBikoCount = skf3050MPayInKindBikoRepository.deleteByPrimaryKey(effectiveDate);
				if(0 == retBikoCount){
					resultCount = 0;
				}else{
					resultCount ++;
				}
			}
			
		}catch(Exception ex){
			LogUtils.debugByMsg("現物支給価額備考テーブル削除時に例外発生");
			resultCount = -1;
		}
		
		return resultCount;
		
	}
	
}
