/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKindBiko;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindBikoRepository;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc002.Skf3090Sc002InitDto;

/**
 * Skf3090Sc002InitService 現物支給価額マスタ登録画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc002InitService extends SkfServiceAbstract<Skf3090Sc002InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf3090Sc002SharedService skf3090Sc002SharedService;
	
	@Autowired
	private Skf3050MPayInKindBikoRepository skf3050MPayInKindBikoRepository; 
	
	@Autowired
	private Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository;
	
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
	public Skf3090Sc002InitDto index(Skf3090Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC002_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3090_SC002);
		
		// エラースタイルをクリア
		skf3090Sc002SharedService.errorStyleClear(initDto);
		
		// 状態区分チェック（1：新規、2：詳細）
		if(skf3090Sc002SharedService.JYOTAIKBN_1.equals(initDto.getHdnJyotaiKbn())){
			// 前画面で新規ボタンが押された
			
			// 退避（複写）用のデータを初期化
			initDto.setTaihiMoney(null);
			initDto.setTaihiBiko(null);

			// 改定日空白
			initDto.setEffectiveDate(null);
			// 改定日編集可
			initDto.setEffectiveDateDisabled(false);

			// 都道府県名と現物支給価額を設定
			skf3090Sc002SharedService.setPrefCdData(initDto, null, null);
            // 備考空白
            initDto.setBiko(null);
            // 登録ボタン有効化
            initDto.setRegistButtonDisabled(false);
            // 複写ボタン無効化
            initDto.setCopyButtonDisabled(true);
            //削除ボタン無効化
            initDto.setDeleteButtonDisabled(true);
            
            // 状態区分を一応詰めなおし（登録時に使用）
            initDto.setHdnJyotaiKbn(skf3090Sc002SharedService.JYOTAIKBN_1);
		}else if(skf3090Sc002SharedService.JYOTAIKBN_2.equals(initDto.getHdnJyotaiKbn())){
			// 前画面で詳細ボタンが押された
			
			// 退避（複写）用のデータを初期化
			initDto.setTaihiMoney(null);
			initDto.setTaihiBiko(null);
			
			// 遷移元画面からの改定日
			initDto.setEffectiveDate(initDto.getHdnEffectiveDate());
			// 改定日編集不可
			initDto.setEffectiveDateDisabled(true);
			
			// 現物支給価額データを取得する(現物支給価額マスタ一覧画面（SKF3090SC001）のSQL文を使用する）
			// - 検索条件セット
			Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter param = new Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpParameter();
			param.setEffectiveDateParam(skf3090Sc002SharedService.replaceDateFormat(initDto.getHdnEffectiveDate()));
			// -　検索
			List<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp> resultData = new ArrayList<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp>();
			resultData = skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExpRepository.getGenbutsuShikyuKagakuReport(param);

			// 都道府県名と現物支給価額を設定
			boolean isError = skf3090Sc002SharedService.setPrefCdData(initDto, resultData, null);			
			
			// 備考データを現物支給価額備考テーブルから取得する
			Skf3050MPayInKindBiko bikoData = new Skf3050MPayInKindBiko();
			if(false == isError){
				bikoData = skf3050MPayInKindBikoRepository.selectByPrimaryKey(skf3090Sc002SharedService.replaceDateFormat(initDto.getHdnEffectiveDate()));
			}else{
				// 	排他エラー(warning.skf.w_skf_1006=他のユーザによって{0}されています。もう一度画面を更新し、{1}をやり直してください。)
				ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.W_SKF_1006, "削除", "検索");
				bikoData = null;
			}
			if(null == bikoData){
				initDto.setBiko(null);
				initDto.setTaihiBiko(null);
			}else{
				initDto.setBiko(bikoData.getBiko());
				initDto.setTaihiBiko(bikoData.getBiko());
			}
			
			if(false == isError){
	            // 登録ボタン有効化
	            initDto.setRegistButtonDisabled(false);
	            // 複写ボタン有効化
	            initDto.setCopyButtonDisabled(false);
	            //削除ボタン有効化
	            initDto.setDeleteButtonDisabled(false);			
			}else{
				// エラーの場合は無効にして、前画面へ戻らせる
	            // 登録ボタン無効化
	            initDto.setRegistButtonDisabled(true);
	            // 複写ボタン無効化
	            initDto.setCopyButtonDisabled(true);
	            //削除ボタン無効化
	            initDto.setDeleteButtonDisabled(true);
			}

            // 状態区分を一応詰めなおし（登録時に使用）
            initDto.setHdnJyotaiKbn(skf3090Sc002SharedService.JYOTAIKBN_2);
		}else{
			// 複写ボタンが押下された
			
			// 改定日空白
			initDto.setEffectiveDate(null);
			// 改定日編集可
			initDto.setEffectiveDateDisabled(false);			

			// 都道府県名と現物支給価額を設定
			skf3090Sc002SharedService.setPrefCdData(initDto, null, initDto.getTaihiMoney());

			// 備考
			initDto.setBiko(initDto.getTaihiBiko());
            // 登録ボタン有効化
            initDto.setRegistButtonDisabled(false);
            // 複写ボタン無効化
            initDto.setCopyButtonDisabled(true);
            //削除ボタン無効化
            initDto.setDeleteButtonDisabled(true);			
			
            // 状態区分を一応詰めなおし（登録時に使用）
            initDto.setHdnJyotaiKbn(skf3090Sc002SharedService.JYOTAIKBN_3);
		}
		
		return initDto;
	}
	
}
