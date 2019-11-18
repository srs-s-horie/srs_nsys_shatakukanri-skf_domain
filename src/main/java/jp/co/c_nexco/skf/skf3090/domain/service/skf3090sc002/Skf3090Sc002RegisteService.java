/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetkaiteibiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc002.Skf3090Sc002GetkaiteibiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKind;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKindBiko;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MPayInKindKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetBikokaiteibiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetShikyuKagakuCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc002.Skf3090Sc002GetkaiteibiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindBikoRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MPayInKindRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc002.Skf3090Sc002RegisteDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002.Skf3090Sc002SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3090Sc002RegistService 現物支給価額マスタ登録画面のRegistサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3090Sc002RegisteService extends BaseServiceAbstract<Skf3090Sc002RegisteDto>{

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
	@Autowired
	Skf3090Sc002SharedService skf3090Sc002SharedService;	

	@Autowired
	Skf3090Sc002GetkaiteibiCountExpRepository skf3090Sc002GetkaiteibiCountExpRepository;
	
	@Autowired
	Skf3090Sc002GetBikokaiteibiCountExpRepository skf3090Sc002GetBikokaiteibiCountExpRepository;
	
	@Autowired
	Skf3090Sc002GetShikyuKagakuCountExpRepository skf3090Sc002GetShikyuKagakuCountExpRepository;
	
	@Autowired
	Skf3050MPayInKindRepository skf3050MPayInKindRepository;
	
	@Autowired
	Skf3050MPayInKindBikoRepository skf3050MPayInKindBikoRepository;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	

    @Value("${skf.common.validate_error}")
    private String validationErrorCode;	
    
    private int EXIST_DATA = -9;
    
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
	public Skf3090Sc002RegisteDto index(Skf3090Sc002RegisteDto registDto) throws Exception {
	
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());		
		
		// エラースタイルをクリア
		skf3090Sc002SharedService.errorStyleClear(registDto);
		
		// 入力チェック
		List<String> registMoneyData = new ArrayList <String>();
		if(false == isValidateInput(registDto, registMoneyData)){
			// エラーの場合は何もしない
			LogUtils.debugByMsg("入力チェックエラーのため処理終了");
		}else{
			boolean isGotoPrevPage = false;
			if(skf3090Sc002SharedService.JYOTAIKBN_1.equals(registDto.getHdnJyotaiKbn()) ||
					skf3090Sc002SharedService.JYOTAIKBN_3.equals(registDto.getHdnJyotaiKbn())){
				// 新規 or 複写（新規登録と同じ）
				int insertCount = insertDataInfo(registDto, registMoneyData);
				if(0 == insertCount){
					// システムエラー（error.skf.e_skf_1075=更新時にエラーが発生しました。ヘルプデスクへ連絡してください。）
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
					// 例外時はthrowしてRollBack
					throwBusinessExceptionIfErrors(registDto.getResultMessages());
				}else if(-1 == insertCount){
					// 登録エラー（warning.skf.w_skf_1010=他のユーザによって登録されています。もう一度画面を更新し、登録をやり直してください。）
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1010);
					// 例外時はthrowしてRollBack
					throwBusinessExceptionIfErrors(registDto.getResultMessages());
				}else if(-9 == insertCount){
					// 登録前のチェックでデータが既に存在（そのまま画面をもとに戻す）
					// DB登録していないので、ロールバックは必要なし
					LogUtils.debugByMsg("登録前のデータチェックですでに存在していたため、登録せずに処理終了");
				}else{
					// 登録完了（現物支給価額マスタ一覧画面へ遷移）
					// メッセージは表示なし
					isGotoPrevPage = true;
				}
			}else if(skf3090Sc002SharedService.JYOTAIKBN_2.equals(registDto.getHdnJyotaiKbn())){
				// 詳細（更新）
				int updateCount = updateDataInfo(registDto, registMoneyData);
				if(0 == updateCount){
					// システムエラー
					// error.skf.e_skf_1075=更新時にエラーが発生しました。ヘルプデスクへ連絡してください。
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
					// 例外時はthrowしてRollBack
					throwBusinessExceptionIfErrors(registDto.getResultMessages());
				}else if(-1 == updateCount){
					// 更新エラー
					// warning.skf.w_skf_1010=他のユーザによって登録されています。もう一度画面を更新し、登録をやり直してください。
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1010);
					// 例外時はthrowしてRollBack
					throwBusinessExceptionIfErrors(registDto.getResultMessages());
				}else{
					// 更新完了
					// メッセージは表示なし
					isGotoPrevPage = true;
				}
			}
			if(true == isGotoPrevPage){
				// 現物支給価額マスタ一覧画面へ遷移
				registDto.setTransferPageInfo(TransferPageInfo.prevPage(FunctionIdConstant.SKF3090_SC001));
			}
		}
		
		return registDto;
	}
	
	/**
	 * 入力チェックを行う.
	 * 
	 * @param registDto
	 * @param registMoneyData
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */	
	private boolean isValidateInput(Skf3090Sc002RegisteDto registDto, List<String> registMoneyData) throws UnsupportedEncodingException, ParseException {
		boolean isCheckOk = true;
		String debugMessage = "";
		
		// 改定日（必須チェック）
		if(NfwStringUtils.isEmpty(registDto.getEffectiveDate())){
			// 入力されていない
			isCheckOk = false;
			// error.skf.e_skf_1048={0}が未入力です。
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "改定日");
			debugMessage += "　必須入力チェック - " + "改定日";
			registDto.setEffectiveDateErr(validationErrorCode);
		}else{
			// 入力されている
			// バイトサイズチェック
			if(CheckUtils.isMoreThanByteSize(skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim()), 8)){
				isCheckOk = false;
				// error.skf.e_skf_1049={0}は{1}桁以内で入力してください。
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "改定日", "8");
				debugMessage += "　桁数チェック - " + "改定日 - " + registDto.getEffectiveDate();
				registDto.setEffectiveDateErr(validationErrorCode);
			}else{
				// 日付チェック
				if(!SkfCheckUtils.isSkfDateFormat(skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim()),CheckUtils.DateFormatType.YYYYMMDD)){
					isCheckOk = false;
					// error.skf.e_skf_1055={0}は日付を正しく入力してください。
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "改定日");
					debugMessage += "　日付形式チェック - " + "改定日";
					registDto.setEffectiveDateErr(validationErrorCode);
				}else{
					// 過去日かのチェック
					// システム処理年月(YYYYMM)を取得
					String sysShoriNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();

					// targetDate(画面の改定日) < compareDate（比較対象システム処理年月）
			        Date targetDate = null;
			        Date compareDate = null;
			        int diff = 0;
			        if (true == isCheckOk) {
			        	// isCheckOkがTrue（エラーがない）の時のみ実施
			            SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);
			            targetDate = sdf.parse(skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim()).substring(0, 6));
			            compareDate = sdf.parse(sysShoriNenGetsu);
			            diff = targetDate.compareTo(compareDate);
			            if (diff < 0) {
							isCheckOk = false;
							// error.skf.e_skf_1055={0}は日付を正しく入力してください。
							ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "改定日");
							debugMessage += "　日付過去日チェック - " + "改定日";
							registDto.setEffectiveDateErr(validationErrorCode);
			            }
			        }
				}
			}
		}
		
		// 現物支給価額　文字種チェック、バイトチェック
		// 都道府県コード取得
		Map<String, String> genericCodeMapPref = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		int emptyCount = 0;
		// 01 北海道
		String money = null;
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn01())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn01().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn01(), registDto, skf3090Sc002SharedService.PREF_01, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn01Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(0,money);
		// 02 青森県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn02())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn02().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn02(), registDto, skf3090Sc002SharedService.PREF_02, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn02Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(1,money);
		// 03 岩手県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn03())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn03().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn03(), registDto, skf3090Sc002SharedService.PREF_03, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn03Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(2,money);
		// 04 宮城県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn04())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn04().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn04(), registDto, skf3090Sc002SharedService.PREF_04, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn04Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(3,money);
		// 05 秋田県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn05())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn05().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn05(), registDto, skf3090Sc002SharedService.PREF_05, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn05Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(4,money);
		// 06 山形県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn06())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn06().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn06(), registDto, skf3090Sc002SharedService.PREF_06, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn06Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(5,money);
		// 07 福島県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn07())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn07().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn07(), registDto, skf3090Sc002SharedService.PREF_07, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn07Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(6,money);
		// 08 茨城県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn08())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn08().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn08(), registDto, skf3090Sc002SharedService.PREF_08, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn08Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(7,money);
		// 09 栃木県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn09())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn09().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn09(), registDto, skf3090Sc002SharedService.PREF_09, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn09Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(8,money);
		// 10 群馬県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn10())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn10().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn10(), registDto, skf3090Sc002SharedService.PREF_10, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn10Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(9,money);
		// 11 埼玉県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn11())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn11().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn11(), registDto, skf3090Sc002SharedService.PREF_11, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn11Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(10,money);
		// 12 千葉県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn12())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn12().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn12(), registDto, skf3090Sc002SharedService.PREF_12, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn12Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(11,money);
		// 13 東京都
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn13())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn13().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn13(), registDto, skf3090Sc002SharedService.PREF_13, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn13Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(12,money);
		// 14 神奈川県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn14())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn14().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn14(), registDto, skf3090Sc002SharedService.PREF_14, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn14Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(13,money);
		// 15 新潟県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn15())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn15().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn15(), registDto, skf3090Sc002SharedService.PREF_15, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn15Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(14,money);
		// 16 富山県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn16())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn16().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn16(), registDto, skf3090Sc002SharedService.PREF_16, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn16Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(15,money);
		// 17 石川県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn17())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn17().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn17(), registDto, skf3090Sc002SharedService.PREF_17, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn17Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(16,money);
		// 18 福井県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn18())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn18().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn18(), registDto, skf3090Sc002SharedService.PREF_18, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn18Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(17,money);
		// 19 山梨県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn19())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn19().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn19(), registDto, skf3090Sc002SharedService.PREF_19, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn19Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(18,money);
		// 20 長野県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn20())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn20().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn20(), registDto, skf3090Sc002SharedService.PREF_20, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn20Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(19,money);
		// 21 岐阜県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn21())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn21().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn21(), registDto, skf3090Sc002SharedService.PREF_21, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn21Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(20,money);
		// 22 静岡県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn22())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn22().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn22(), registDto, skf3090Sc002SharedService.PREF_22, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn22Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(21,money);
		// 23 愛知県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn23())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn23().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn23(), registDto, skf3090Sc002SharedService.PREF_23, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn23Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(22,money);
		// 24 三重県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn24())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn24().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn24(), registDto, skf3090Sc002SharedService.PREF_24, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn24Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(23,money);
		// 25 滋賀県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn25())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn25().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn25(), registDto, skf3090Sc002SharedService.PREF_25, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn25Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(24,money);
		// 26 京都府
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn26())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn26().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn26(), registDto, skf3090Sc002SharedService.PREF_26, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn26Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(25,money);
		// 27 大阪府
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn27())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn27().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn27(), registDto, skf3090Sc002SharedService.PREF_27, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn27Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(26,money);
		// 28 兵庫県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn28())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn28().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn28(), registDto, skf3090Sc002SharedService.PREF_28, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn28Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(27,money);
		// 29 奈良県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn29())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn29().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn29(), registDto, skf3090Sc002SharedService.PREF_29, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn29Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(28,money);
		// 30 和歌山県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn30())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn30().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn30(), registDto, skf3090Sc002SharedService.PREF_30, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn30Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(29,money);
		// 31 鳥取県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn31())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn31().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn31(), registDto, skf3090Sc002SharedService.PREF_31, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn31Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(30,money);
		// 32 島根県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn32())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn32().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn32(), registDto, skf3090Sc002SharedService.PREF_32, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn32Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(31,money);
		// 33 岡山県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn33())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn33().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn33(), registDto, skf3090Sc002SharedService.PREF_33, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn33Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(32,money);
		// 34 広島県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn34())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn34().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn34(), registDto, skf3090Sc002SharedService.PREF_34, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn34Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(33,money);
		// 35 山口県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn35())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn35().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn35(), registDto, skf3090Sc002SharedService.PREF_35, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn35Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(34,money);
		// 36 徳島県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn36())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn36().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn36(), registDto, skf3090Sc002SharedService.PREF_36, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn36Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(35,money);
		// 37 香川県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn37())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn37().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn37(), registDto, skf3090Sc002SharedService.PREF_37, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn37Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(36,money);
		// 38 愛媛県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn38())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn38().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn38(), registDto, skf3090Sc002SharedService.PREF_38, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn38Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(37,money);
		// 39 高知県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn39())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn39().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn39(), registDto, skf3090Sc002SharedService.PREF_39, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn39Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(38,money);
		// 40 福岡圏
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn40())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn40().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn40(), registDto, skf3090Sc002SharedService.PREF_40, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn40Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(39,money);
		// 41 佐賀県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn41())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn41().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn41(), registDto, skf3090Sc002SharedService.PREF_41, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn41Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(40,money);
		// 42 長崎県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn42())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn42().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn42(), registDto, skf3090Sc002SharedService.PREF_42, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn42Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(41,money);
		// 43 熊本県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn43())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn43().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn43(), registDto, skf3090Sc002SharedService.PREF_43, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn43Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(42,money);
		// 44 大分県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn44())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn44().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn44(), registDto, skf3090Sc002SharedService.PREF_44, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn44Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(43,money);
		// 45 宮崎県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn45())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn45().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn45(), registDto, skf3090Sc002SharedService.PREF_45, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn45Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(44,money);
		// 46 鹿児島県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn46())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn46().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn46(), registDto, skf3090Sc002SharedService.PREF_46, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn46Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(45,money);
		// 47 沖縄県
		if(NfwStringUtils.isNotEmpty(registDto.getTxtTodoufukenn47())){
			emptyCount ++;
			money = registDto.getTxtTodoufukenn47().replace(",", "");
			if(!isMoneyCheck(registDto.getTxtTodoufukenn47(), registDto, skf3090Sc002SharedService.PREF_47, genericCodeMapPref)){
				isCheckOk = false;
				registDto.setTxtTodoufukenn47Err(validationErrorCode);
				money = null;
			}
		}else{
			money = null;
		}
		registMoneyData.add(46,money);
		
		if(0 == emptyCount){
			// 現物支給価額未入力
			isCheckOk = false;
			// error.skf.e_skf_3052=現物支給価額を入力してください。
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3052);
			debugMessage += "　未入力チェック - " + "現物支給価額";
		}
		
		// 備考　バイト数チェック
		if(NfwStringUtils.isNotEmpty(registDto.getBiko())){
			if(CheckUtils.isMoreThanSize(registDto.getBiko().trim(), 400)){
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "備考", "400");
				debugMessage += "　桁数チェック - " + "備考 - " + registDto.getBiko();
				registDto.setBikoErr(validationErrorCode);
			}	
		}
		
		
		// デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		}
		
		return isCheckOk;
	}

	
	/**
	 * 金額の入力チェック
	 * 
	 * @param money 金額
	 * @param registDto
	 * @param prefCd 都道府県コード
	 * @param genericCodeMapPref　都道府県コードリスト 
	 * @return チェック結果
	 */	
	private boolean isMoneyCheck(String money, Skf3090Sc002RegisteDto registDto, String prefCd, Map<String, String> genericCodeMapPref) throws UnsupportedEncodingException{
		boolean isCheckOk = true;
		// 金額からカンマを外す 
		String targetMoney = money.replace(",", "");
		// 半角数字かチェック
		String matchPattern = "^[-0-9]*$";
		if(!targetMoney.trim().matches(matchPattern)){
			isCheckOk = false;
			// error.skf.e_skf_1050={0}は数字半角を入力してください。
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1050, prefCd + " " + genericCodeMapPref.get(prefCd));
		}else{
			// マイナスチェック
			int intMoney = Integer.parseInt(targetMoney);
			if(intMoney < 0){
				isCheckOk = false;
				//error.skf.e_skf_3029=現物支給価額は正数を入力してください
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3029);
			}else{
				// バイト数チェック
				if(CheckUtils.isMoreThanByteSize(targetMoney.trim(), 4)){
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, prefCd + " " + genericCodeMapPref.get(prefCd), "4");
				}					
			}
		}
		return isCheckOk;
	}
	
	
	/**
	 * 登録処理
	 * 
	 * @param registDto
	 * @param registMoneyData 登録する金額データ
	 * @return 登録結果
	 */	
	private int insertDataInfo(Skf3090Sc002RegisteDto registDto, List<String> registMoneyData){
	
		String targetEffectiveDate = skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim());
		// 現物支給価額設定にテーブル改定日の存在チェック
		List<Skf3090Sc002GetkaiteibiCountExp> resultKind = new ArrayList<Skf3090Sc002GetkaiteibiCountExp>();
		Skf3090Sc002GetkaiteibiCountExpParameter param = new Skf3090Sc002GetkaiteibiCountExpParameter();
		param.setEffectiveDateParam(targetEffectiveDate);
		resultKind = skf3090Sc002GetkaiteibiCountExpRepository.getkaiteibiCount(param);
		if(CollectionUtils.isNotEmpty(resultKind) && resultKind.get(0).getRowCount() > 0){
			// error.skf.e_skf_3027={0}の現物支給価額が登録済です。
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3027, "改定日");
			return EXIST_DATA;
		}

		// 現物支給価額備考にテーブル改定日の存在チェック
		List<Skf3090Sc002GetBikokaiteibiCountExp> resultKindBiko = new ArrayList<Skf3090Sc002GetBikokaiteibiCountExp>();
		Skf3090Sc002GetBikokaiteibiCountExpParameter paramBiko = new Skf3090Sc002GetBikokaiteibiCountExpParameter();
		paramBiko.setEffectiveDateParam(targetEffectiveDate);
		resultKindBiko = skf3090Sc002GetBikokaiteibiCountExpRepository.getBikokaiteibiCount(paramBiko);
		if(CollectionUtils.isNotEmpty(resultKindBiko) && resultKindBiko.get(0).getRowCount() > 0){
			// error.skf.e_skf_3028={0}の備考が登録済です。
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3028, "改定日");
			return EXIST_DATA;
		}
		
		// 登録
		int resultCount = 0;
		if(0 == resultKind.get(0).getRowCount() && 0 == resultKindBiko.get(0).getRowCount()){
			
			String effectiveDate = skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim());
			// 現物支給価額テーブル登録
			try{
				for(int i = 0; i < registMoneyData.size(); i++){
					// 登録対象の現物支給価額
					String insertMoney = registMoneyData.get(i);
					// 登録対象の都道府県コード
					String prefCd = String.valueOf(i + 1);
					prefCd = String.format("%2s", prefCd).replace(" ", "0");
					
					if(NfwStringUtils.isEmpty(insertMoney)){
						// nullや空の場合は登録しない
						continue;
					}
					// 登録
					int retkaiteibiCount = insertPayInKind(effectiveDate, prefCd, insertMoney);
					if(0 >= retkaiteibiCount){
						resultCount = -1;
						break;
					}else{
						resultCount ++;
					}
				}
			}catch(Exception ex){
				LogUtils.debugByMsg("現物支給価額テーブル登録時に例外発生");
				resultCount = 0;
			}
			
			if(0 == resultCount || -1 == resultCount){
				// エラーなので、呼び出しもとへ戻す
				return resultCount;
			}
			
			// 現物支給価額備考テーブル登録
			if(NfwStringUtils.isNotEmpty(registDto.getBiko())){
				// 備考 が空でないときに限り登録する
				Skf3050MPayInKindBiko recordBiko = new Skf3050MPayInKindBiko();
				// - 改定日
				recordBiko.setEffectiveDate(effectiveDate);
				// - 備考
				recordBiko.setBiko(registDto.getBiko());
				// - 削除フラグ
				recordBiko.setDeleteFlag("0");
				try{
					int retBikoCount = skf3050MPayInKindBikoRepository.insertSelective(recordBiko);
					if(0 >= retBikoCount){
						resultCount = -1;
						return resultCount;
					}else{
						resultCount ++;
					}
				}catch(Exception ex){
					LogUtils.debugByMsg("現物支給価額備考テーブル登録時に例外発生");
					resultCount = 0;
					return resultCount;
				}
			}
			
			if(0 == resultCount){
				// 一応、現行にあるので記載しておく（1つもデータ登録がなかったとしても正常終了）
				// - データがないと、この登録処理まで来ないので、通らない
				resultCount = 1;
			}
		}
		return resultCount;
	}
	
	
	/**
	 * 現物支給価額設定登録処理
	 * 
	 * @param effectiveDate　改定日
	 * @param prefCd　都道府県コード
	 * @param insertMoney 登録する金額データ
	 * @return 更新結果
	 */	
	private int insertPayInKind(String effectiveDate, String prefCd, String insertMoney){
		// 登録パラメータの設定
		Skf3050MPayInKind record = new Skf3050MPayInKind();
		// - 改定日
		record.setEffectiveDate(effectiveDate);
		// - 都道府県コード
		record.setPrefCd(prefCd);
		// - 現物支給価額
		record.setKyojuRiekigaku(Short.valueOf(insertMoney));
		// - 削除フラグ
		record.setDeleteFlag("0");
		// 登録
		return skf3050MPayInKindRepository.insertSelective(record);
	}
	
	
	/**
	 * 更新処理
	 * 
	 * @param registDto
	 * @param registMoneyData 登録する金額データ
	 * @return 更新結果
	 */	
	private int updateDataInfo(Skf3090Sc002RegisteDto registDto, List<String> registMoneyData){
		
		String targetEffectiveDate = skf3090Sc002SharedService.replaceDateFormat(registDto.getEffectiveDate().trim());		
		int resultCount = 0;
		try{
			for(int i = 0; i < registMoneyData.size(); i++){
				// 更新対象の現物支給価額
				String insertMoney = registMoneyData.get(i);
				// 更新対象の都道府県コード
				String prefCd = String.valueOf(i + 1);
				prefCd = String.format("%2s", prefCd).replace(" ", "0");
				/*
				List<Skf3090Sc002GetShikyuKagakuCountExp> resultShikyuKagakuCount = new ArrayList<Skf3090Sc002GetShikyuKagakuCountExp>();
				Skf3090Sc002GetShikyuKagakuCountExpParameter param = new Skf3090Sc002GetShikyuKagakuCountExpParameter();
				param.setEffectiveDate(targetEffectiveDate);
				param.setPrefCd(prefCd);
				resultShikyuKagakuCount = skf3090Sc002GetShikyuKagakuCountExpRepository.getShikyuKagakuCount(param);
				*/
				Skf3050MPayInKind shikyuKagakuData = new Skf3050MPayInKind();
				Skf3050MPayInKindKey key = new Skf3050MPayInKindKey();
				key.setEffectiveDate(targetEffectiveDate);
				key.setPrefCd(prefCd);
				shikyuKagakuData = skf3050MPayInKindRepository.selectByPrimaryKey(key);
				
				int retkaiteibiCount = 0;
				if(null != shikyuKagakuData){
					// 金額がない場合は削除する
					if(NfwStringUtils.isEmpty(insertMoney)){
						// 削除
						retkaiteibiCount = skf3050MPayInKindRepository.deleteByPrimaryKey(key);
					}else{
						// 更新
						Skf3050MPayInKind record = new Skf3050MPayInKind();
						// - 更新データ
						// -- 金額
						record.setKyojuRiekigaku(Short.valueOf(insertMoney));
						// -- 削除フラグ
						record.setDeleteFlag("0");
						// - 絞り込みキー
						// -- 改定日
						record.setEffectiveDate(targetEffectiveDate);
						// -- 都道府県コード
						record.setPrefCd(prefCd);
						retkaiteibiCount = skf3050MPayInKindRepository.updateByPrimaryKeySelective(record);
					}
				}else if(null == shikyuKagakuData && NfwStringUtils.isNotEmpty(insertMoney)){
					// 登録
					retkaiteibiCount = insertPayInKind(targetEffectiveDate, prefCd, insertMoney);
							
				}else{
					// もともと未入力の都道府県は何もしない
					continue;
				}
				if(0 >= retkaiteibiCount){
					resultCount = -1;
					break;
				}else{
					resultCount ++;
				}
			}
		}catch(Exception ex){
			LogUtils.debugByMsg("現物支給価額備考テーブル更新時に例外発生");
			resultCount = 0;
		}
		
		if(0 == resultCount || -1 == resultCount){
			// この時点でエラーなので、呼び出しもとへ戻す
			return resultCount;
		}
		
		try{
			// 現物支給価額備考テーブル更新
			Skf3050MPayInKindBiko bikoData = new Skf3050MPayInKindBiko();
			bikoData = skf3050MPayInKindBikoRepository.selectByPrimaryKey(targetEffectiveDate);
			int retBikoCount = 0;
			Skf3050MPayInKindBiko recordBiko = new Skf3050MPayInKindBiko();
			// - 改定日
			recordBiko.setEffectiveDate(targetEffectiveDate);
			// - 備考
			recordBiko.setBiko(registDto.getBiko());
			// - 削除フラグ
			recordBiko.setDeleteFlag("0");					
			if(null != bikoData){
				// 更新
				retBikoCount = skf3050MPayInKindBikoRepository.updateByPrimaryKeySelective(recordBiko);
			}else{
				// 登録
				retBikoCount = skf3050MPayInKindBikoRepository.insertSelective(recordBiko);
			}
			if(0 >= retBikoCount){
				resultCount = -1;
			}else{
				resultCount ++;
			}
		}catch(Exception ex){
			LogUtils.debugByMsg("現物支給価額備考テーブル更新時に例外発生");
			resultCount = 0;
		}
		
		return resultCount;
	}

}
