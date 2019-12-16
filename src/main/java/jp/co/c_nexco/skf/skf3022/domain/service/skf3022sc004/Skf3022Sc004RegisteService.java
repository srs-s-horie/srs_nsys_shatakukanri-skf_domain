/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc004;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc004.Skf3022Sc004RegisteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc004.Skf3022Sc004DeleteShatakuYoyaku2ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc004.Skf3022Sc004DeleteShatakuYoyakuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TShatakuYoyakuData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc004.Skf3022Sc004DeleteShatakuYoyaku2ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc004.Skf3022Sc004DeleteShatakuYoyakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TShatakuYoyakuDataRepository;


/**
 * Skf3022Sc004RegistService 次月予約登録画面のRegistサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc004RegisteService extends BaseServiceAbstract<Skf3022Sc004RegisteDto>  {

	@Autowired
	Skf3022Sc004DeleteShatakuYoyakuExpRepository skf3022Sc004DeleteShatakuYoyakuExpRepository;

	@Autowired
	Skf3022Sc004DeleteShatakuYoyaku2ExpRepository skf3022Sc004DeleteShatakuYoyaku2ExpRepository;

	@Autowired
	Skf3022TShatakuYoyakuDataRepository skf3022TShatakuYoyakuDataRepository;
	
	@Autowired
	SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
		
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
    @Value("${skf.common.validate_error}")
    private String validationErrorCode;		

	/**
	 * 活性：0
	 */
	private String FALSE = "0";
	/**
	 * 非活性：1
	 */
	private String TRUE = "1";
	
	/**
	 * 社宅使用料調整金額
	 */
	private String SKF3022_SC004_STK_TYOSEI_PAY = "社宅使用料調整金額";
	/**
	 * 個人負担共益費調整金額
	 */
	private String SKF3022_SC004_COJIN_CHANGE_PAY = "個人負担共益費調整金額";
	/**
	 * 駐車場使用料調整金額
	 */
	private String SKF3022_SC004_PARKING_CHANGE_PAY = "駐車場使用料調整金額";	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3022Sc004RegisteDto index(Skf3022Sc004RegisteDto registeDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registeDto.getPageId());		
		
		String[] setOne = { "1" };
		// 次月チェックボックスのチェック状態を取得
		String jigetuStr = null;
		String[] jigetuChkValList = registeDto.getJigetu();
		if(jigetuChkValList != null && jigetuChkValList.length > 0){
			jigetuStr = jigetuChkValList[0];
			registeDto.setJigetu(setOne);
			registeDto.setTxtStkTyoseiPayJigetuDisable(FALSE);
			registeDto.setTxtCojinChangePayJigetuDisable(FALSE);
			registeDto.setTxtParkingChangePayJigetuDisable(FALSE);
		}else{
			registeDto.setJigetu(null);
			registeDto.setTxtStkTyoseiPayJigetuDisable(TRUE);
			registeDto.setTxtCojinChangePayJigetuDisable(TRUE);
			registeDto.setTxtParkingChangePayJigetuDisable(TRUE);
		}
		// 次々月チェックボックスのチェック状態を取得
		String jijigetuStr = null;
		String[] jijigetuChkValList = registeDto.getJijigetu();
		if(jijigetuChkValList != null && jijigetuChkValList.length > 0){
			jijigetuStr = jijigetuChkValList[0];
			registeDto.setJijigetu(setOne);
			registeDto.setTxtStkTyoseiPayJijigetuDisable(FALSE);
			registeDto.setTxtCojinChangePayJijigetuDisable(FALSE);
			registeDto.setTxtParkingChangePayJijigetuDisable(FALSE);
		}else{
			registeDto.setJijigetu(null);
			registeDto.setTxtStkTyoseiPayJijigetuDisable(TRUE);
			registeDto.setTxtCojinChangePayJijigetuDisable(TRUE);
			registeDto.setTxtParkingChangePayJijigetuDisable(TRUE);
		}
		
		// 入力チェック
		if(false == check(registeDto, jigetuStr, jijigetuStr)){
			// 登録処理
			
	        // 次月 社宅使用料調整金額
	        String stkTyoseiPayJigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtStkTyoseiPayJigetu())){
	        	stkTyoseiPayJigetu = registeDto.getTxtStkTyoseiPayJigetu();
	        }
	        // 次月個人負担共益費調整金額
	        String cojinChangePayJigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtCojinChangePayJigetu())){
	        	cojinChangePayJigetu = registeDto.getTxtCojinChangePayJigetu();
	        }
	        // 次月 駐車場使用料調整金額
	        String parkingChangePayJigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtParkingChangePayJigetu())){
	        	parkingChangePayJigetu = registeDto.getTxtParkingChangePayJigetu();
	        }

	        // 次々月 社宅使用料調整金額
	        String stkTyoseiPayJijigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtStkTyoseiPayJijigetu())){
	        	stkTyoseiPayJijigetu = registeDto.getTxtStkTyoseiPayJijigetu();
	        }
	        // 次々月個人負担共益費調整金額
	        String cojinChangePayJijigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtCojinChangePayJijigetu())){
	        	cojinChangePayJijigetu = registeDto.getTxtCojinChangePayJijigetu();
	        }
	        // 次々月 駐車場使用料調整金額
	        String parkingChangePayJijigetu = "0";
	        if(NfwStringUtils.isNotEmpty(registeDto.getTxtParkingChangePayJijigetu())){
	        	parkingChangePayJijigetu = registeDto.getTxtParkingChangePayJijigetu();
	        }
	        
	        // 次月・次々月両方にチェックがなかった場合
			if(NfwStringUtils.isEmpty(jigetuStr) && NfwStringUtils.isEmpty(jijigetuStr)){
				// error.skf.e_skf_3053=次月、次々月にチェックが入っていません。
				ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_3053);			
				registeDto.setJigetu(null);
				registeDto.setTxtStkTyoseiPayJigetuDisable(TRUE);
				registeDto.setTxtCojinChangePayJigetuDisable(TRUE);
				registeDto.setTxtParkingChangePayJigetuDisable(TRUE);
				registeDto.setJijigetu(null);
				registeDto.setTxtStkTyoseiPayJijigetuDisable(TRUE);
				registeDto.setTxtCojinChangePayJijigetuDisable(TRUE);
				registeDto.setTxtParkingChangePayJijigetuDisable(TRUE);
				return registeDto;
			}
			
			// 社宅利用料予約データの登録
			int result = insertShatakuRentalReserve(registeDto, jigetuStr, jijigetuStr, 
					stkTyoseiPayJigetu, cojinChangePayJigetu, parkingChangePayJigetu,
					stkTyoseiPayJijigetu, cojinChangePayJijigetu, parkingChangePayJijigetu);
			if(-1 == result){
				// error.skf.e_skf_1073=登録時にエラーが発生しました。ヘルプデスクへ連絡してください。
				ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_1073);			
				// 例外時はthrowしてRollBack
				throwBusinessExceptionIfErrors(registeDto.getResultMessages());
			}else{
				// 登録完了後、次月予約画面を閉じる
			}
		}		
		return registeDto;
	}
	
	
	/**
	 * 入力チェック
	 * 
	 * @param dto 
	 * @return　確認結果
	 */
	private boolean check(Skf3022Sc004RegisteDto registeDto, String jigetuStr, String jijigetuStr){
		
		boolean errorFlg = false;

		// 次月チェックボックス
		if(NfwStringUtils.isNotEmpty(jigetuStr)){
			// チェックあり
			// 次月 社宅使用料調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtStkTyoseiPayJigetu(), SKF3022_SC004_STK_TYOSEI_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtStkTyoseiPayJigetu(), SKF3022_SC004_STK_TYOSEI_PAY))){
				// エラーに背景色変更
				registeDto.setTxtStkTyoseiPayJigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtStkTyoseiPayJigetuErr(null);
			}
			// 次月 個人負担共益費調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtCojinChangePayJigetu(), SKF3022_SC004_COJIN_CHANGE_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtCojinChangePayJigetu(), SKF3022_SC004_COJIN_CHANGE_PAY))){
				// エラーに背景色変更
				registeDto.setTxtCojinChangePayJigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtCojinChangePayJigetuErr(null);
			}
			// 次月 駐車場使用料調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtParkingChangePayJigetu(), SKF3022_SC004_PARKING_CHANGE_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtParkingChangePayJigetu(), SKF3022_SC004_PARKING_CHANGE_PAY))){
				// エラーに背景色変更
				registeDto.setTxtParkingChangePayJigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtParkingChangePayJigetuErr(null);
			}
		}else{
			// チェックなし
			registeDto.setTxtStkTyoseiPayJigetuErr(null);
			registeDto.setTxtCojinChangePayJigetuErr(null);
			registeDto.setTxtParkingChangePayJigetuErr(null);
		}
		
		// 次々月チェックボックス
		if(NfwStringUtils.isNotEmpty(jijigetuStr)){
			// チェックあり
			// 次々月 社宅使用料調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtStkTyoseiPayJijigetu(), SKF3022_SC004_STK_TYOSEI_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtStkTyoseiPayJijigetu(), SKF3022_SC004_STK_TYOSEI_PAY))){
				// エラーに背景色変更
				registeDto.setTxtStkTyoseiPayJijigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtStkTyoseiPayJijigetuErr(null);
			}
			// 次々月 個人負担共益費調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtCojinChangePayJijigetu(), SKF3022_SC004_COJIN_CHANGE_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtCojinChangePayJijigetu(), SKF3022_SC004_COJIN_CHANGE_PAY))){
				// エラーに背景色変更
				registeDto.setTxtCojinChangePayJijigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtCojinChangePayJijigetuErr(null);
			}
			// 次々月 駐車場使用料調整金額
			if((true == validateTxtByte(registeDto, registeDto.getTxtParkingChangePayJijigetu(), SKF3022_SC004_PARKING_CHANGE_PAY, 6)) ||
					(true == validateTxtKind(registeDto, registeDto.getTxtParkingChangePayJijigetu(), SKF3022_SC004_PARKING_CHANGE_PAY))){
				// エラーに背景色変更
				registeDto.setTxtParkingChangePayJijigetuErr(validationErrorCode);
				errorFlg = true;
			}else{
				registeDto.setTxtParkingChangePayJijigetuErr(null);
			}
			
		}else{
			// チェックなし
			registeDto.setTxtStkTyoseiPayJijigetuErr(null);
			registeDto.setTxtCojinChangePayJijigetuErr(null);
			registeDto.setTxtParkingChangePayJijigetuErr(null);
		}
		
		return errorFlg;
	}
	
	
	/**
	 * バイト数チェック
	 * 
	 * @param dto 
	 * @param targetStr チェック対象の文字列 
	 * @param labelName ラベル名 
	 * @param maxByte 最大Byte 
	 * @return　確認結果
	 */
	private boolean validateTxtByte(Skf3022Sc004RegisteDto registeDto, String targetStr, String labelName, int maxByte){

		boolean isByteError = false;
		if(NfwStringUtils.isNotEmpty(targetStr) && CheckUtils.isMoreThanSize(targetStr.trim(), maxByte)){
			//error.skf.e_skf_1049={0}は{1}桁以内で入力してください。
			ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_1049, labelName, "半角" + String.valueOf(maxByte));			
			isByteError = true;
		}
		return isByteError;
	}
	
	
	/**
	 * 文字種チェック
	 * 
	 * @param dto 
	 * @param targetStr チェック対象の文字列 
	 * @param labelName ラベル名 
	 * @return　確認結果
	 */
	private boolean validateTxtKind(Skf3022Sc004RegisteDto registeDto, String targetStr, String labelName){
		boolean isKindError = false;

		// 空ならば正常で返す
		if(CheckUtils.isEmpty(targetStr)){
			LogUtils.debugByMsg("文字種チェックスキップ（入力なし）");
			return isKindError;
		}
		
		// カンマを外す
		String targetMoney = targetStr.replace(",", "");
		
		// 半角スペースチェック
		if(-1 != targetMoney.indexOf(" ")){
			//error.skf.e_skf_1050={0}は数字半角を入力してください。
			ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_1050, labelName);			
			
			isKindError = true;
			return isKindError;
		}
		
		// 全角スペースチェック
		if((false == isKindError) && (-1 != targetMoney.indexOf("　"))){
			//error.skf.e_skf_1050={0}は数字半角を入力してください。
			ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_1050, labelName);			
			
			isKindError = true;
			return isKindError;
		}
		
		// 半角のみかチェック（正常性判定）
		String matchPattern = "^[-0-9]*$";
		if((false == isKindError) && (!targetMoney.trim().matches(matchPattern))){
			//error.skf.e_skf_1050={0}は数字半角を入力してください。
			ServiceHelper.addErrorResultMessage(registeDto, null, MessageIdConstant.E_SKF_1050, labelName);			

			isKindError = true;
			return isKindError;
		}
		
		return isKindError;
	}
	
	
	/**
	 * 社宅利用料予約の登録メソッド
	 * 
	 * @param dto 
	 * @param jigetuStr 次月チェック状態 
	 * @param jijigetuStr 次々月チェック状態
	 * @param stkTyoseiPayJigetu 次月社宅使用料調整金額
	 * @param cojinChangePayJigetu 次月個人負担共益費調整金額
	 * @param parkingChangePayJigetu 次月駐車場使用料調整金額
	 * @param stkTyoseiPayJijigetu 次々月社宅使用料調整金額
	 * @param cojinChangePayJijigetu 次々月個人負担共益費調整金額
	 * @param parkingChangePayJijigetu 次々月駐車場使用料調整金額
	 * @return　処理結果
	 */
	private int insertShatakuRentalReserve(Skf3022Sc004RegisteDto registeDto, String jigetuStr, String jijigetuStr,
			String stkTyoseiPayJigetu, String cojinChangePayJigetu, String parkingChangePayJigetu,
			String stkTyoseiPayJijigetu, String cojinChangePayJijigetu, String parkingChangePayJijigetu){
		
        int retCountJigetu = 0;
        int retCountJijigetu = 0;
        
        // 社宅使用料予約データを削除
        try{
        	int delCount = 0;
        	if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuTeijiNo())){
        		Skf3022Sc004DeleteShatakuYoyaku2ExpParameter param = new Skf3022Sc004DeleteShatakuYoyaku2ExpParameter();
        		param.setTeijiNo(Long.parseLong(registeDto.getHdnJigetuYoyakuTeijiNo()));
        		delCount = skf3022Sc004DeleteShatakuYoyaku2ExpRepository.deleteShatakuYoyaku2(param);
        	}

        	if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuShatakuKanriId())){
        		Skf3022Sc004DeleteShatakuYoyakuExpParameter param = new Skf3022Sc004DeleteShatakuYoyakuExpParameter();
        		param.setShatakuKanriId(Long.parseLong(registeDto.getHdnJigetuYoyakuShatakuKanriId()));
        		delCount = skf3022Sc004DeleteShatakuYoyakuExpRepository.deleteShatakuYoyaku(param);
        	}
        }catch(Exception ex){
			LogUtils.debugByMsg("社宅使用料予約データ削除中にエラー:" + ex.toString());
        	return -1;
        }
		
        if(NfwStringUtils.isNotEmpty(jigetuStr)){
        	try{
        		// 社宅利用料予約データ（次月）を登録
        		Skf3022TShatakuYoyakuData record = new Skf3022TShatakuYoyakuData();
        		// 社宅使用料予約ＩＤ
        		record.setShatakuYoyakuId(skfBaseBusinessLogicUtils.getMaxShatakuYoyakuId());
        		// 年月
        		record.setYearMonth(registeDto.getHdnJigetu());
        		// 提示番号
        		if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuTeijiNo())){
        			record.setTeijiNo(Long.parseLong(registeDto.getHdnJigetuYoyakuTeijiNo()));
        		}
        		// 社宅管理台帳ID
            	if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuShatakuKanriId())){
            		record.setShatakuKanriId(Long.parseLong(registeDto.getHdnJigetuYoyakuShatakuKanriId()));
            	}
        		// 社宅使用料調整金額
            	record.setRentalAdjust(Integer.parseInt(stkTyoseiPayJigetu));
        		// 個人負担共益費調整金額
            	record.setKyoekihiPersonAdjust(Integer.parseInt(cojinChangePayJigetu));
        		// 駐車場使用料調整金額
            	record.setParkingRentalAdjust(Integer.parseInt(parkingChangePayJigetu));
        		// 更新済みフラグ(NULLで登録なので設定しない）
        		// 削除フラグ
        		record.setDeleteFlag("0");
        		retCountJigetu = skf3022TShatakuYoyakuDataRepository.insertSelective(record);
        	}catch(Exception ex){
    			LogUtils.debugByMsg("社宅使用料予約データ（次月）登録中にエラー:" + ex.toString());
        		return -1;
        	}
        }
        
        if(NfwStringUtils.isNotEmpty(jijigetuStr)){
        	try{
        		// 社宅利用料予約データ（次々月）を登録
        		Skf3022TShatakuYoyakuData record = new Skf3022TShatakuYoyakuData();
        		// 社宅使用料予約ＩＤ
        		record.setShatakuYoyakuId(skfBaseBusinessLogicUtils.getMaxShatakuYoyakuId());
        		// 年月
        		record.setYearMonth(registeDto.getHdnJijigetu());
        		// 提示番号
        		if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuTeijiNo())){
        			record.setTeijiNo(Long.parseLong(registeDto.getHdnJigetuYoyakuTeijiNo()));
        		}
        		// 社宅管理台帳ID
            	if(NfwStringUtils.isNotEmpty(registeDto.getHdnJigetuYoyakuShatakuKanriId())){
            		record.setShatakuKanriId(Long.parseLong(registeDto.getHdnJigetuYoyakuShatakuKanriId()));
            	}
        		// 社宅使用料調整金額
            	record.setRentalAdjust(Integer.parseInt(stkTyoseiPayJijigetu));
        		// 個人負担共益費調整金額
            	record.setKyoekihiPersonAdjust(Integer.parseInt(cojinChangePayJijigetu));
        		// 駐車場使用料調整金額
            	record.setParkingRentalAdjust(Integer.parseInt(parkingChangePayJijigetu));
        		// 更新済みフラグ(NULLで登録なので設定しない）
        		// 削除フラグ
        		record.setDeleteFlag("0");
        		retCountJijigetu = skf3022TShatakuYoyakuDataRepository.insertSelective(record);
        	}catch(Exception ex){
    			LogUtils.debugByMsg("社宅使用料予約データ（次々月）登録中にエラー:" + ex.toString());
        		return -1;
        	}
        }

        if(retCountJigetu > 0 || retCountJijigetu > 0){
			LogUtils.debugByMsg("社宅使用料予約データ登録成功");
        }else{
        	return -1;
        	
        }
        return retCountJigetu + retCountJijigetu;
	}
}
