/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc004;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc004.Skf3022Sc004GetShatakuYoyakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc004.Skf3022Sc004GetShatakuYoyakuInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc004.Skf3022Sc004GetShatakuYoyakuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc004.Skf3022Sc004InitDto;

/**
 * Skf3022Sc004InitService　次月予約登録画面のInitサービス処理クラス。　 
 * 
 *  @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc004InitService extends BaseServiceAbstract<Skf3022Sc004InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private Skf3022Sc004GetShatakuYoyakuInfoExpRepository skf3022Sc004GetShatakuYoyakuInfoExpRepository;
	
	/**
	 * 提示データ登録
	 */
	private String SKF3022_SC006 = "1";
	/**
	 * 入退居情報登録 
	 */
	private String SKF3030_SC002 = "2";

	/**
	 * 活性：0
	 */
	private String FALSE = "0";
	/**
	 * 非活性：1
	 */
	private String TRUE = "1";
	
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
	public Skf3022Sc004InitDto index(Skf3022Sc004InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC004_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		SimpleDateFormat dateFormatFrom = new SimpleDateFormat("yyyyMM");
		Date dateJigetu = null;
		dateJigetu = dateFormatFrom.parse(initDto.getHdnJigetuYoyakuYearMonth());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateJigetu);
		// 1カ月加算
		calendar.add(Calendar.MONTH, 1);
		// 基準年月の次月
		String jigetu = dateFormatFrom.format(calendar.getTime()).toString();
		initDto.setHdnJigetu(jigetu);
		// 1カ月加算
		calendar.add(Calendar.MONTH, 1);
		// 基準年月の次々月
		String jijigetu = dateFormatFrom.format(calendar.getTime()).toString();
		initDto.setHdnJijigetu(jijigetu);
		
		// 次月予約データの取得
		Skf3022Sc004GetShatakuYoyakuInfoExpParameter paramJigetu = new Skf3022Sc004GetShatakuYoyakuInfoExpParameter();
		List<Skf3022Sc004GetShatakuYoyakuInfoExp> jigetuShatakuYoyaku = new ArrayList<Skf3022Sc004GetShatakuYoyakuInfoExp>();
		// - システム処理年月
		paramJigetu.setYearMonth(jigetu);
		// - 提示番号
		if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuTeijiNo())){
			paramJigetu.setTeijiNo(Long.parseLong(initDto.getHdnJigetuYoyakuTeijiNo()));
			initDto.setHdnRegistMessage(SKF3022_SC006);
		}else{
			paramJigetu.setTeijiNo(null);
		}
		// - 社宅管理番号
		if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuShatakuKanriId())){
			paramJigetu.setShatakuKanriId(Long.parseLong(initDto.getHdnJigetuYoyakuShatakuKanriId()));
			initDto.setHdnRegistMessage(SKF3030_SC002);
		}else{
			paramJigetu.setShatakuKanriId(null);
		}
		jigetuShatakuYoyaku = skf3022Sc004GetShatakuYoyakuInfoExpRepository.getShatakuYoyaku(paramJigetu);
		
		
		// 次々月予約データの取得
		Skf3022Sc004GetShatakuYoyakuInfoExpParameter paramJijigetu = new Skf3022Sc004GetShatakuYoyakuInfoExpParameter();
		List<Skf3022Sc004GetShatakuYoyakuInfoExp> jijigetuShatakuYoyaku = new ArrayList<Skf3022Sc004GetShatakuYoyakuInfoExp>();
		// - システム処理年月
		paramJijigetu.setYearMonth(jijigetu);
		// - 提示番号
		if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuTeijiNo())){
			paramJijigetu.setTeijiNo(Long.parseLong(initDto.getHdnJigetuYoyakuTeijiNo()));
		}else{
			paramJijigetu.setTeijiNo(null);
		}
		// - 社宅管理番号
		if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuShatakuKanriId())){
			paramJijigetu.setShatakuKanriId(Long.parseLong(initDto.getHdnJigetuYoyakuShatakuKanriId()));
		}else{
			paramJijigetu.setShatakuKanriId(null);
		}
		jijigetuShatakuYoyaku = skf3022Sc004GetShatakuYoyakuInfoExpRepository.getShatakuYoyaku(paramJijigetu);
		
		// 画面項目の設定
		setControlValues(initDto, jigetuShatakuYoyaku, jijigetuShatakuYoyaku);
		
		// 画面項目制御
		setControlStatus(initDto, jigetuShatakuYoyaku, jijigetuShatakuYoyaku);
		
		return initDto;
	}
	
	/**
	 * 画面項目の設定
	 * 
	 * @param initDto DTO
	 * @param jigetuShatakuYoyaku 次月データ
	 * @param jijigetuShatakuYoyaku 次々月データ
	 * @return 
	 */		
	private void setControlValues(Skf3022Sc004InitDto initDto,
			List<Skf3022Sc004GetShatakuYoyakuInfoExp> jigetuShatakuYoyaku,
			List<Skf3022Sc004GetShatakuYoyakuInfoExp> jijigetuShatakuYoyaku){
		
		// 数値のカンマ区切り設定
		NumberFormat nfNum = NumberFormat.getNumberInstance();
		
        int rental = 0;
        int kyoekihiPerson = 0;
        int parkingRentalOne = 0;
        int parkingRentalTwo = 0;
		
        // 社宅使用料月額
        try{
            if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuRental())){
            	rental = Integer.parseInt(initDto.getHdnJigetuYoyakuRental());
            }
        }catch(Exception ex){
        	// エラーの場合なにもしない（初期値：0を画面へ表示する）
			LogUtils.infoByMsg("setControlValues, 社宅使用料月額：数値変換で例外発生");
        }
        
        // 個人負担共益費月額
        try{
            if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuKyoekihiPerson())){
            	kyoekihiPerson = Integer.parseInt(initDto.getHdnJigetuYoyakuKyoekihiPerson());
            }
        }catch(Exception ex){
        	// エラーの場合なにもしない（初期値：0を画面へ表示する）
			LogUtils.infoByMsg("setControlValues, 個人負担共益費月額：数値変換で例外発生");
        }
        
        // 駐車場1月額
        try{
            if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuParkingRentalOne())){
            	parkingRentalOne = Integer.parseInt(initDto.getHdnJigetuYoyakuParkingRentalOne());
            }
        }catch(Exception ex){
        	// エラーの場合なにもしない（初期値：0を画面へ表示する）
			LogUtils.infoByMsg("setControlValues, 駐車場1月額：数値変換で例外発生");
        }
        
        // 駐車場2月額
        try{
            if(NfwStringUtils.isNotEmpty(initDto.getHdnJigetuYoyakuParkingRentalTwo())){
            	parkingRentalTwo = Integer.parseInt(initDto.getHdnJigetuYoyakuParkingRentalTwo());
            }
        }catch(Exception ex){
        	// エラーの場合なにもしない（初期値：0を画面へ表示する）
			LogUtils.infoByMsg("setControlValues, 駐車場2月額：数値変換で例外発生");
        }
        
        // 表示内容を設定
        // - 社宅使用料月額
        initDto.setShatakuPayJigetu(nfNum.format(rental));
        initDto.setShatakuPayJijigetu(nfNum.format(rental));
        // - 個人負担共益費月額
        initDto.setCojinPayJigetu(nfNum.format(kyoekihiPerson));
        initDto.setCojinPayJijigetu(nfNum.format(kyoekihiPerson));
        // - 区画１_駐車場使用料月額
        initDto.setParkingPayOneJigetu(nfNum.format(parkingRentalOne));
        initDto.setParkingPayOneJijigetu(nfNum.format(parkingRentalOne));
        // - 区画２_駐車場使用料月額
        initDto.setParkingPayTwoJigetu(nfNum.format(parkingRentalTwo));
        initDto.setParkingPayTwoJijigetu(nfNum.format(parkingRentalTwo));

        // 次月データ
        if(CollectionUtils.isNotEmpty(jigetuShatakuYoyaku) && jigetuShatakuYoyaku.size() > 0){
        	
        	Skf3022Sc004GetShatakuYoyakuInfoExp jigetuData = new Skf3022Sc004GetShatakuYoyakuInfoExp();
        	jigetuData = jigetuShatakuYoyaku.get(0); 
        	
    		// 次月 社宅使用料調整金額
    		initDto.setTxtStkTyoseiPayJigetu(String.valueOf(jigetuData.getRentalAdjust()));
    		// 次月 社宅使用料月額（調整後）
    		int adjustPay = rental + jigetuData.getRentalAdjust();
    		initDto.setStkFinalPayJigetu(nfNum.format(adjustPay));
        	/*if(0 != jigetuData.getRentalAdjust()){
        		// 次月 社宅使用料調整金額
        		initDto.setTxtStkTyoseiPayJigetu(nfNum.format(jigetuData.getRentalAdjust()) );
        		// 次月 社宅使用料月額（調整後）
        		int adjustPay = rental + jigetuData.getRentalAdjust();
        		initDto.setStkFinalPayJigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtStkTyoseiPayJigetu(ZERO);
        		initDto.setStkFinalPayJigetu(ZERO);
        	}*/

    		// 次月 個人負担共益費調整金額
    		initDto.setTxtCojinChangePayJigetu(String.valueOf(jigetuData.getKyoekihiPersonAdjust()));
    		// 次月 個人負担共益費月額（調整後）
    		adjustPay = kyoekihiPerson + jigetuData.getKyoekihiPersonAdjust();
    		initDto.setCojinFinalPayJigetu(nfNum.format(adjustPay));
    		/*
    		if(0 != jigetuData.getKyoekihiPersonAdjust()){
        		// 次月 個人負担共益費調整金額
        		initDto.setTxtCojinChangePayJigetu(nfNum.format(jigetuData.getKyoekihiPersonAdjust()));
        		// 次月 個人負担共益費月額（調整後）
        		int adjustPay = kyoekihiPerson + jigetuData.getKyoekihiPersonAdjust();
        		initDto.setCojinFinalPayJigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtCojinChangePayJigetu(ZERO);
        		initDto.setCojinFinalPayJigetu(ZERO);
        	}*/

    		// 次月 駐車場使用料調整金額
    		initDto.setTxtParkingChangePayJigetu(String.valueOf(jigetuData.getParkingRentalAdjust()));
    		// 次月 駐車場使用料月額（調整後）
    		adjustPay = parkingRentalOne + parkingRentalTwo + jigetuData.getParkingRentalAdjust();
    		initDto.setParkingFinalPayJigetu(nfNum.format(adjustPay));
    		/*
    		if(0 != jigetuData.getParkingRentalAdjust()){
        		// 次月 駐車場使用料調整金額
        		initDto.setTxtParkingChangePayJigetu(nfNum.format(jigetuData.getParkingRentalAdjust()));
        		// 次月 駐車場使用料月額（調整後）
        		int adjustPay = parkingRentalOne + parkingRentalTwo + jigetuData.getParkingRentalAdjust();
        		initDto.setParkingFinalPayJigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtParkingChangePayJigetu(ZERO);
        		initDto.setParkingFinalPayJigetu(ZERO);
        	}*/
        }
        /*else{
    		initDto.setTxtStkTyoseiPayJigetu(ZERO);
    		initDto.setStkFinalPayJigetu(ZERO);
    		initDto.setTxtCojinChangePayJigetu(ZERO);
    		initDto.setCojinFinalPayJigetu(ZERO);
    		initDto.setTxtParkingChangePayJigetu(ZERO);
    		initDto.setParkingFinalPayJigetu(ZERO);
        }*/

        // 次々月データ
        if(CollectionUtils.isNotEmpty(jijigetuShatakuYoyaku) && jijigetuShatakuYoyaku.size() > 0){

        	Skf3022Sc004GetShatakuYoyakuInfoExp jijigetuData = new Skf3022Sc004GetShatakuYoyakuInfoExp();
        	jijigetuData = jijigetuShatakuYoyaku.get(0); 

    		// 次々月 社宅使用料調整金額
    		initDto.setTxtStkTyoseiPayJijigetu(String.valueOf(jijigetuData.getRentalAdjust()));
    		// 次々月 社宅使用料月額（調整後）
    		int adjustPay = rental + jijigetuData.getRentalAdjust();
    		initDto.setStkFinalPayJijigetu(nfNum.format(adjustPay));
    		/*
        	if(0 != jijigetuData.getRentalAdjust()){
        		// 次々月 社宅使用料調整金額
        		initDto.setTxtStkTyoseiPayJijigetu(nfNum.format(jijigetuData.getRentalAdjust()));
        		// 次々月 社宅使用料月額（調整後）
        		int adjustPay = rental + jijigetuData.getRentalAdjust();
        		initDto.setStkFinalPayJijigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtStkTyoseiPayJijigetu(ZERO);
        		initDto.setStkFinalPayJijigetu(ZERO);
        	}*/

    		// 次々月 個人負担共益費調整金額
    		initDto.setTxtCojinChangePayJijigetu(String.valueOf(jijigetuData.getKyoekihiPersonAdjust()));
    		// 次々月 個人負担共益費月額（調整後）
    		adjustPay = kyoekihiPerson + jijigetuData.getKyoekihiPersonAdjust();
    		initDto.setCojinFinalPayJijigetu(nfNum.format(adjustPay));
    		/*
    		if(0 != jijigetuData.getKyoekihiPersonAdjust()){
        		// 次々月 個人負担共益費調整金額
        		initDto.setTxtCojinChangePayJijigetu(nfNum.format(jijigetuData.getKyoekihiPersonAdjust()));
        		// 次々月 個人負担共益費月額（調整後）
        		int adjustPay = kyoekihiPerson + jijigetuData.getKyoekihiPersonAdjust();
        		initDto.setCojinFinalPayJijigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtCojinChangePayJijigetu(ZERO);
        		initDto.setCojinFinalPayJijigetu(ZERO);
        	}*/
    		
    		// 次々月 駐車場使用料調整金額
    		initDto.setTxtParkingChangePayJijigetu(String.valueOf(jijigetuData.getParkingRentalAdjust()));
    		// 次々月 駐車場使用料月額（調整後）
    		adjustPay = parkingRentalOne + parkingRentalTwo + jijigetuData.getParkingRentalAdjust();
    		initDto.setParkingFinalPayJijigetu(nfNum.format(adjustPay));
    		/*
        	if(0 != jijigetuData.getParkingRentalAdjust()){
        		// 次々月 駐車場使用料調整金額
        		initDto.setTxtParkingChangePayJijigetu(nfNum.format(jijigetuData.getParkingRentalAdjust()));
        		// 次々月 駐車場使用料月額（調整後）
        		int adjustPay = parkingRentalOne + parkingRentalTwo + jijigetuData.getParkingRentalAdjust();
        		initDto.setParkingFinalPayJijigetu(nfNum.format(adjustPay));
        	}else{
        		initDto.setTxtParkingChangePayJijigetu(ZERO);
        		initDto.setParkingFinalPayJijigetu(ZERO);
        	}*/
        }
        /*else{
    		initDto.setTxtStkTyoseiPayJijigetu(ZERO);
    		initDto.setStkFinalPayJijigetu(ZERO);
    		initDto.setTxtCojinChangePayJijigetu(ZERO);
    		initDto.setCojinFinalPayJijigetu(ZERO);
    		initDto.setTxtParkingChangePayJijigetu(ZERO);
    		initDto.setParkingFinalPayJijigetu(ZERO);
        }*/
	}
	
	/**
	 * 画面項目制御の設定
	 * 
	 * @param initDto DTO
	 * @param jigetuShatakuYoyaku 次月データ
	 * @param jijigetuShatakuYoyaku 次々月データ
	 * @return 
	 */		
	private void setControlStatus(Skf3022Sc004InitDto initDto,
			List<Skf3022Sc004GetShatakuYoyakuInfoExp> jigetuShatakuYoyaku,
			List<Skf3022Sc004GetShatakuYoyakuInfoExp> jijigetuShatakuYoyaku){
		
		String[] setOne = { "1" };
		
		if(CollectionUtils.isNotEmpty(jigetuShatakuYoyaku) && jigetuShatakuYoyaku.size() > 0){
			// 次月予約データがある場合
			// 次月チェックボックス → チェックする
			initDto.setJigetu(setOne);
            // 次月 社宅使用料調整金額 → 活性
			initDto.setTxtStkTyoseiPayJigetuDisable(FALSE);
            // 次月 個人負担共益費調整金額 → 活性
			initDto.setTxtCojinChangePayJigetuDisable(FALSE);
            // 次月 駐車場使用料調整金額 → 活性
         	initDto.setTxtParkingChangePayJigetuDisable(FALSE);
		}else{
			// 次月予約データがない場合
			// 次月チェックボックス → チェックしない
			initDto.setJigetu(null);
            // 次月 社宅使用料調整金額 → 非活性
			initDto.setTxtStkTyoseiPayJigetuDisable(TRUE);
            // 次月 個人負担共益費調整金額 → 非活性
			initDto.setTxtCojinChangePayJigetuDisable(TRUE);
            // 次月 駐車場使用料調整金額 → 非活性
         	initDto.setTxtParkingChangePayJigetuDisable(TRUE);
		}
		
		if(CollectionUtils.isNotEmpty(jijigetuShatakuYoyaku) && jijigetuShatakuYoyaku.size() > 0){
			// 次々月予約データがある場合
            // 次々月チェックボックス → チェックする
			initDto.setJijigetu(setOne);
            //次々月 社宅使用料調整金額 → 活性
			initDto.setTxtStkTyoseiPayJijigetuDisable(FALSE);
            // 次々月 個人負担共益費調整金額 → 活性
			initDto.setTxtCojinChangePayJijigetuDisable(FALSE);
            // 次々月 駐車場使用料調整金額 → 活性
         	initDto.setTxtParkingChangePayJijigetuDisable(FALSE);
		}else{
			// 次々月予約データがない場合
			// 次々月チェックボックス → チェックしない
			initDto.setJijigetu(null);
            //次々月 社宅使用料調整金額 → 非活性
			initDto.setTxtStkTyoseiPayJijigetuDisable(TRUE);
            // 次々月 個人負担共益費調整金額 → 非活性
			initDto.setTxtCojinChangePayJijigetuDisable(TRUE);
            // 次々月 駐車場使用料調整金額 → 非活性
         	initDto.setTxtParkingChangePayJijigetuDisable(TRUE);
			
		}
	}
}
