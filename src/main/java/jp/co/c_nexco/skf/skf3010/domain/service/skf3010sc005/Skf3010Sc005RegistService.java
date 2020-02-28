/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetMaxShatakuRoomNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetMaxShatakuRoomNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc005.Skf3010Sc005RegistDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc004.Skf3010Sc004SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;


/**
 * Skf3010Sc005RegistService 社宅部屋登録登録ボタン押下時処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc005RegistService extends BaseServiceAbstract<Skf3010Sc005RegistDto> {

	@Autowired
	private Skf3010Sc005SharedService skf3010Sc005SharedService;

	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	
	@Autowired
	private Skf3010MShatakuRoomBihinRepository skf3010MShatakuRoomBihinRepository;
	
	@Autowired
	private Skf3010Sc005GetMaxShatakuRoomNoExpRepository skf3010Sc005GetMaxShatakuRoomNoExpRepository;

	@Autowired
	private Skf3010Sc004SharedService skf3010Sc004SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	/**
	 * 登録処理メインメソッド.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	protected BaseDto index(Skf3010Sc005RegistDto registDto) throws Exception {

		registDto.setPageTitleKey(MessageIdConstant.SKF3010_SC005_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());

		// エラー系のDto値を初期化
		registDto.setRoomNoError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalMensekiError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setLendMensekiError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalKikakuError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalAuseError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setLendKbnError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setSunRoomMensekiError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setStairsMensekiError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setBarnMensekiError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalKikakuHosokuError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setOriginalAuseHosokuError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setLendKbnHosokuError(CodeConstant.DOUBLE_QUOTATION);
		registDto.setBikoError(CodeConstant.DOUBLE_QUOTATION);

		// ドロップダウンリスト用リストのインスタンス生成
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
		
		registDto.setBarnMensekiAdjust(registDto.getHdnBarnMensekiAdjust());
		registDto.setHdnBarnMensekiAdjust(registDto.getHdnBarnMensekiAdjust());
		
		// 入力値チェック
		if (isValidateInput(registDto) == false) {
			// 入力チェックエラーの場合、ドロップダウンリストを再検索して処理を終了する
			// ドロップダウンリスト取得
			//ドロップダウンリストの設定
			skf3010Sc005SharedService.getDropDownList(registDto.getOriginalKikaku(), originalKikakuList, 
					registDto.getOriginalAuse(), originalAuseList,
					registDto.getLendKbn(), lendKbnList, 
					registDto.getColdExemptionKbn(), coldExemptionKbnList);
			// 一旦DTOにリストセット（エラー時用）
			registDto.setOriginalKikakuList(originalKikakuList);
			registDto.setOriginalAuseList(originalAuseList);
			registDto.setLendKbnList(lendKbnList);
			registDto.setColdExemptionKbnList(coldExemptionKbnList);
			
			return registDto;
		}

		if (registDto.getHdnRegistFlg().equals(Skf3010Sc005CommonSharedService.DATA_INSERT)) {
			/** 登録処理 */
			LogUtils.debugByMsg("社宅部屋登録");
			
			int resultCount = registHoyuuSyatakuHeyaInfo(registDto);
			
			if(resultCount < 0){
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			else if(resultCount == 0){
				//登録エラー
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			
			//成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
			
            //'データ操作区分を「更新」にする
			registDto.setHdnRegistFlg(Skf3010Sc005CommonSharedService.DATA_UPDATE);

		} else {
			/** 更新処理 */
			LogUtils.debugByMsg("社宅部屋更新");
						
			int resultCount = registHoyuuSyatakuHeyaInfo(registDto);
			
			if(resultCount < 0){
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			else if(resultCount == 0){
				//更新エラー
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			//成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);

		}
		
		//RedisplayShatakuRoomRegistPage
		// '部屋情報をセット
		setRoomInfo(registDto);
		// '備品情報をセット
		List<Map<String, Object>> bihinListData = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> hdnBihinStatusList = new ArrayList<Map<String, Object>>();
		skf3010Sc005SharedService.setBihinInfo(registDto.getHdnShatakuKanriNo(), registDto.getHdnRoomKanriNo(),bihinListData,hdnBihinStatusList);
		//「削除」ボタンを活性化
		registDto.setMaskPattern("");
		registDto.setDeleteBtnFlg("false");
		
		//ドロップダウンリストの設定
		skf3010Sc005SharedService.getDropDownList(registDto.getOriginalKikaku(), originalKikakuList, 
				registDto.getOriginalAuse(), originalAuseList,
				registDto.getLendKbn(), lendKbnList, 
				registDto.getColdExemptionKbn(), coldExemptionKbnList);
		
		//ヘッダ部の空き社宅数を再描写
		// 社宅部屋総数を取得する
		int roomCount = skf3010Sc004SharedService.getRoomCount(Long.parseLong(registDto.getHdnShatakuKanriNo()));

		// 空き社宅部屋総数を取得する
		int emptyRoomCount = skf3010Sc004SharedService.getEmptyRoomCount(Long.parseLong(registDto.getHdnShatakuKanriNo()));

		String emptyRoomStr = emptyRoomCount + CodeConstant.SLASH + roomCount;

		//「空き部屋数」に現在の空き社宅数を設定する
		registDto.setEmptyRoomCount(emptyRoomStr);

		// DTOにセット
		registDto.setOriginalKikakuList(originalKikakuList);
		registDto.setOriginalAuseList(originalAuseList);
		registDto.setLendKbnList(lendKbnList);
		registDto.setColdExemptionKbnList(coldExemptionKbnList);
		registDto.setHdnBihinStatusList(hdnBihinStatusList);
		registDto.setBihinListData(bihinListData);

		return registDto;
	}

	/**
	 * 入力チェックを行う.
	 * 
	 * @param registDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 桁数チェック時の例外
	 */
	private boolean isValidateInput(Skf3010Sc005RegistDto registDto) throws UnsupportedEncodingException {

		boolean isCheckOk = true;

		String debugMessage = "";

		/** 必須入力チェック */
		// 部屋番号
		if (registDto.getRoomNo() == null || CheckUtils.isEmpty(registDto.getRoomNo().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "部屋番号");
			registDto.setRoomNoError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "部屋番号";
		}
		// 本来延面積
		if (registDto.getOriginalMenseki() == null || CheckUtils.isEmpty(registDto.getOriginalMenseki().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来延面積");
			registDto.setOriginalMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来延面積";
		}
		// 貸与延面積
		if (registDto.getLendMenseki() == null || CheckUtils.isEmpty(registDto.getLendMenseki().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "貸与延面積");
			debugMessage += "　必須入力チェック - " + "貸与延面積";
			registDto.setLendMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
		}
		// 本来用途
		if (registDto.getOriginalAuse() == null || CheckUtils.isEmpty(registDto.getOriginalAuse().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来用途");
			registDto.setOriginalAuseError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来用途";
		}
		// 本来規格
		if (registDto.getOriginalKikaku() == null || CheckUtils.isEmpty(registDto.getOriginalKikaku().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来規格");
			registDto.setOriginalKikakuError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来規格";
		}
		// 貸与区分
		if (registDto.getLendKbn() == null || CheckUtils.isEmpty(registDto.getLendKbn().trim())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "貸与区分");
			registDto.setLendKbnError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "貸与区分";
		}		
			
		/** 必須チェックOKなら形式チェック */
		if (isCheckOk) {
			// 本来延面積
			if (!(isValidateTxtFormatForMenseki(registDto.getOriginalMenseki(),true))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "本来延面積");
				registDto.setOriginalMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "本来延面積 - " + registDto.getOriginalMenseki();
			}

			//貸与延面積
			if (!(isValidateTxtFormatForMenseki(registDto.getLendMenseki(),true))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "貸与延面積");
				registDto.setLendMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "貸与延面積 - " + registDto.getLendMenseki();
			}
			
			//サンルーム延面積（任意入力項目なので、空の場合チェック不要）
			if(NfwStringUtils.isNotEmpty(registDto.getSunRoomMenseki())){
				if (!(isValidateTxtFormatForMenseki(registDto.getSunRoomMenseki(),false))) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "サンルーム面積");
					registDto.setSunRoomMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += "　形式チェック - " + "サンルーム面積 - " + registDto.getSunRoomMenseki();
				}
			}
			
			//階段延面積（任意入力項目なので、空の場合チェック不要）
			if(NfwStringUtils.isNotEmpty(registDto.getStairsMenseki())){
				if (!(isValidateTxtFormatForMenseki(registDto.getStairsMenseki(),false))) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "階段面積");
					registDto.setStairsMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += "　形式チェック - " + "階段面積 - " + registDto.getStairsMenseki();
				}
			}
			//物置面積（任意入力項目なので、空の場合チェック不要）
			if(NfwStringUtils.isNotEmpty(registDto.getBarnMenseki())){
				if (!(isValidateTxtFormatForMenseki(registDto.getBarnMenseki(),false))) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "物置面積");
					registDto.setBarnMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += "　形式チェック - " + "物置面積 - " + registDto.getBarnMenseki();
				}
			}
			
		}
		
		/** 必須チェック、形式チェックOKなら桁数チェック */
		if (isCheckOk) {
//			// 部屋番号
//			if (CheckUtils.isMoreThanByteSize(registDto.getRoomNo().trim(), 10)) {
//				isCheckOk = false;
//				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "部屋番号", "10");
//				registDto.setRoomNoError(CodeConstant.NFW_VALIDATION_ERROR);
//				debugMessage += "　桁数チェック - " + "部屋番号 - " + registDto.getRoomNo();
//			} else {
//				debugMessage += "　部屋番号入力桁数 - " + registDto.getRoomNo().trim().getBytes("MS932").length;
//			}
			
			// 本来延面積
			if (CheckUtils.isMoreThanByteSize(registDto.getOriginalMenseki().trim(), 8)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "本来延面積", "8");
				registDto.setOriginalMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "本来延面積 - " + registDto.getOriginalMenseki();
			} else {
				debugMessage += "　本来延面積入力桁数 - " + registDto.getOriginalMenseki().trim().getBytes("MS932").length;
			}
			
			// 本来規格（補助）（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getOriginalKikakuHosoku())
					&& CheckUtils.isMoreThanSize(registDto.getOriginalKikakuHosoku().trim(), 5)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "本来規格補足", "5");
				registDto.setOriginalKikakuHosokuError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "本来規格補足 - " + registDto.getOriginalKikakuHosoku();
			} else {
				debugMessage += "　本来規格補足 入力桁数 - ";
				if (registDto.getOriginalKikakuHosoku() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getOriginalKikakuHosoku().trim().length();
				}
			}
			
			// 貸与延面積
			if (CheckUtils.isMoreThanByteSize(registDto.getLendMenseki().trim(), 8)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "貸与延面積", "8");
				registDto.setLendMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "貸与延面積 - " + registDto.getLendMenseki();
			} else {
				debugMessage += "　貸与延面積入力桁数 - " + registDto.getLendMenseki().trim().getBytes("MS932").length;
			}
			
			// 本来用途（補助）（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getOriginalAuseHosoku())
					&& CheckUtils.isMoreThanSize(registDto.getOriginalAuseHosoku().trim(), 5)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "本来用途補足", "5");
				registDto.setOriginalAuseHosokuError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "本来用途補足 - " + registDto.getOriginalAuseHosoku();
			} else {
				debugMessage += "　本来用途補足 入力桁数 - ";
				if (registDto.getOriginalAuseHosoku() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getOriginalAuseHosoku().trim().length();
				}
			}
			
			// サンルーム延面積（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getSunRoomMenseki())
					&& CheckUtils.isMoreThanByteSize(registDto.getSunRoomMenseki().trim(), 8)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "サンルーム面積", "8");
				registDto.setSunRoomMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "サンルーム面積 - " + registDto.getSunRoomMenseki();
			} else {
				debugMessage += "　サンルーム面積 入力バイト数 - ";
				if (registDto.getSunRoomMenseki() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getSunRoomMenseki().trim().getBytes("MS932").length;
				}
			}
			
			// 貸与区分（補助）（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getLendKbnHosoku())
					&& CheckUtils.isMoreThanSize(registDto.getLendKbnHosoku().trim(), 5)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "貸与区分補足", "5");
				registDto.setLendKbnHosokuError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "貸与区分補足 - " + registDto.getLendKbnHosoku();
			} else {
				debugMessage += "　貸与区分補足 入力桁数 - ";
				if (registDto.getLendKbnHosoku() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getLendKbnHosoku().trim().length();
				}
			}
			
			// 階段面積（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getStairsMenseki())
					&& CheckUtils.isMoreThanByteSize(registDto.getStairsMenseki().trim(), 8)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "階段面積", "8");
				registDto.setStairsMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "階段面積 - " + registDto.getStairsMenseki();
			} else {
				debugMessage += "　階段面積 入力バイト数 - ";
				if (registDto.getStairsMenseki() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getStairsMenseki().trim().getBytes("MS932").length;
				}
			}
			// 物置面積（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getBarnMenseki())
					&& CheckUtils.isMoreThanByteSize(registDto.getBarnMenseki().trim(), 8)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "物置面積", "8");
				registDto.setBarnMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "物置面積 - " + registDto.getBarnMenseki();
			} else {
				debugMessage += "　物置面積 入力バイト数 - ";
				if (registDto.getBarnMenseki() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getBarnMenseki().trim().getBytes("MS932").length;
				}
			}
			
			// 備考（任意入力項目なので、空の場合チェック不要）
			if (NfwStringUtils.isNotEmpty(registDto.getBiko())
					&& CheckUtils.isMoreThanSize(registDto.getBiko().trim(), 100)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1049, "備考", "100");
				registDto.setBikoError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　桁数チェック - " + "備考 - " + registDto.getBiko();
			} else {
				debugMessage += "　備考 入力桁数 - ";
				if (registDto.getBiko() == null) {
					debugMessage += "入力なし";
				} else {
					debugMessage += registDto.getBiko().trim().length();
				}
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
	 * 面積値の形式チェックを行う.
	 * @param txtReq
	 * @param hissu
	 * @return
	 */
	private boolean isValidateTxtFormatForMenseki(String txtReq,boolean hissu){
		
		//半角数値記号チェック
		if (!(CheckUtils.isNumberFormat(txtReq))) {
			LogUtils.debugByMsg("半角数値記号チェックNG：" + txtReq);
			return false;
		}
		
		//数値形式チェック
		String matchPattern="^(0)$|^([1-9]+[0-9]*)$|^([0]\\.[0-9]+)$|^([1-9]+[0-9]*)+(\\.[0-9]+)$";
		if(hissu){
			//必須値の場合（0入力禁止の場合）
			matchPattern="^([1-9]+[0-9]*)$|^([0]\\.[0-9]+)$|^([1-9]+[0-9]*)+(\\.[0-9]+)$";
		}
		if(!txtReq.matches(matchPattern)){
			LogUtils.debugByMsg("数値形式チェックNG：" + txtReq);
			return false;
		}
		
		//整数部の許容桁数チェック
		BigDecimal mensekiValue = new BigDecimal(txtReq);
		mensekiValue = mensekiValue.setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal sen = new BigDecimal("1000");

		if(mensekiValue.compareTo(sen) >= 0){
			LogUtils.debugByMsg("整数部の許容桁数チェックNG：" + txtReq);
			return false;
		}
		//すべて正常
		return true;
	}
	

	/**
	 * 社宅部屋情報マスタ、社宅部屋備品情報マスタテーブルに対して登録または更新処理を行う。<br>
	 * 登録、更新の判断は、RegistDtoに含まれる更新フラグによって判断する。.
	 * 
	 * @param dto indexメソッドの引数であるDto
	 * @return　登録または更新件数
	 */
	private int registHoyuuSyatakuHeyaInfo(Skf3010Sc005RegistDto dto) {

		Skf3010MShatakuRoom  setValue = new Skf3010MShatakuRoom();
		

		/** 登録項目をセット */
		//社宅管理番号
		setValue.setShatakuKanriNo(Long.parseLong(dto.getHdnShatakuKanriNo()));
		//　部屋番号
		setValue.setRoomNo(dto.getRoomNo());
		
		// 本来規格
		setValue.setOriginalKikaku(dto.getOriginalKikaku());
		
		// 本来規格補足
		if(NfwStringUtils.isNotEmpty(dto.getOriginalKikakuHosoku())){
			setValue.setOriginalKikakuHosoku(dto.getOriginalKikakuHosoku());
		}
		
		// 本来用途
		setValue.setOriginalAuse(dto.getOriginalAuse());
		
		//本来用途補足
		if(NfwStringUtils.isNotEmpty(dto.getOriginalAuseHosoku())){
			setValue.setOriginalAuseHosoku(dto.getOriginalAuseHosoku());
		}
		
		//貸与区分
		setValue.setLendKbn(dto.getLendKbn());
		
		//貸与区分補足
		if(NfwStringUtils.isNotEmpty(dto.getLendKbnHosoku())){
			setValue.setLendKbnHosoku(dto.getLendKbnHosoku());
		}
		
		//備考
		if(NfwStringUtils.isNotEmpty(dto.getBiko())){
			setValue.setBiko(dto.getBiko());
		}
		
		// 本来延面積
		if(NfwStringUtils.isNotEmpty(dto.getOriginalMenseki())){
			setValue.setOriginalMenseki(new BigDecimal(dto.getOriginalMenseki().trim()));
		}else{
			setValue.setOriginalMenseki(BigDecimal.ZERO);
		}
		
		// 貸与延面積
		if(NfwStringUtils.isNotEmpty(dto.getLendMenseki())){
			setValue.setLendMenseki(new BigDecimal(dto.getLendMenseki().trim()));
		}else{
			setValue.setLendMenseki(BigDecimal.ZERO);
		}
		
		//サンルーム面積
		if(NfwStringUtils.isNotEmpty(dto.getSunRoomMenseki())){
			setValue.setSunRoomMenseki(new BigDecimal(dto.getSunRoomMenseki().trim()));
		}else{
			setValue.setSunRoomMenseki(BigDecimal.ZERO);
		}
		
		//寒冷地減免区分事由区分
		if(NfwStringUtils.isNotEmpty(dto.getColdExemptionKbn())){
			setValue.setColdExemptionKbn(dto.getColdExemptionKbn());
		}
		
		//階段面積
		if(NfwStringUtils.isNotEmpty(dto.getStairsMenseki())){
			setValue.setStairsMenseki(new BigDecimal(dto.getStairsMenseki().trim()));
		}else{
			setValue.setStairsMenseki(BigDecimal.ZERO);
		}
		
		//物置面積
		if(NfwStringUtils.isNotEmpty(dto.getBarnMenseki())){
			setValue.setBarnMenseki(new BigDecimal(dto.getBarnMenseki().trim()));
		}else{
			setValue.setBarnMenseki(BigDecimal.ZERO);
		}
		
		//物置調整面積
		if(NfwStringUtils.isNotEmpty(dto.getHdnBarnMensekiAdjust())){
			setValue.setBarnMensekiAdjust(new BigDecimal(dto.getHdnBarnMensekiAdjust().trim()));
		}else{
			setValue.setBarnMensekiAdjust(BigDecimal.ZERO);
		}
		
		//備品情報取得
		List<Map<String,Object>> bihinInfoList = new ArrayList<Map<String,Object>>();
		String registBihinData = dto.getRegistBihinData();
		bihinInfoList =  skf3010Sc005SharedService.createShatakuRoomBihinInfo(registBihinData, dto.getHdnBihinStatusList());
		
		/** 登録 */
		LogUtils.debugByMsg("DBCommonItems：" + this.dbCommonItems.toString());
		int registCount = 0;
		if (Skf3010Sc005CommonSharedService.DATA_INSERT.equals(dto.getHdnRegistFlg())) {
			// 新規登録の場合はINSERT
			//社宅部屋管理番号の最大値を取得
			Long maxShatakuRoomKanriNo = 0L;
			Skf3010Sc005GetMaxShatakuRoomNoExpParameter param = new Skf3010Sc005GetMaxShatakuRoomNoExpParameter();
			param.setShatakuKanriNo(Long.parseLong(dto.getHdnShatakuKanriNo()));
			maxShatakuRoomKanriNo = skf3010Sc005GetMaxShatakuRoomNoExpRepository.getMaxShatakuRoomNo(param);
			//社宅部屋管理番号を設定
			if(maxShatakuRoomKanriNo != null){
				maxShatakuRoomKanriNo += 1;
			}else{
				maxShatakuRoomKanriNo = 1L;
			}
			
			setValue.setShatakuRoomKanriNo(maxShatakuRoomKanriNo);
			LogUtils.debugByMsg("新社宅部屋管理番号：" + setValue.getShatakuRoomKanriNo());
			//デフォルト値
			setValue.setLendJokyo("0");
			//登録処理
			registCount = insertShatakuRoom(setValue,bihinInfoList);
			
			if(registCount>0){
				//登録成功時は、部屋管理番号をHdn項目に設定する。
				dto.setHdnRoomKanriNo(setValue.getShatakuRoomKanriNo().toString());
			}
			
		} else {
			// 更新の場合はUPDATE
			//社宅部屋管理番号を設定
			setValue.setShatakuRoomKanriNo(Long.parseLong(dto.getHdnRoomKanriNo()));
			setValue.setDeleteFlag("0");
			// 最終更新時間を取得
			setValue.setLastUpdateDate(dto.getLastUpdateDate("Skf3010Sc005GetRoomInfoUpdateDate"));
			//更新処理
			registCount = updateShatakuRoom(setValue,bihinInfoList);
		}
		
		return registCount;

	}

	/**
	 * 社宅部屋情報、社宅部屋備品情報を登録する.
	 * 
	 * @param setValue 更新値を含むSkf3010MShatakuRoomインスタンス
	 * @param bihinInfoList 備品情報リスト
	 * @return 更新件数
	 */
	private int insertShatakuRoom(Skf3010MShatakuRoom setValue,List<Map<String,Object>> bihinInfoList) {

		int registCount=0;
		int registBihinCount = 0;
		
		//登録処理
		registCount = skf3010MShatakuRoomRepository.insertSelective(setValue);
		
		if(registCount <= 0){
			//登録件数0以下の場合終了
			return 0;
		}
		
		//備品
		for(Map<String,Object> map : bihinInfoList){
			Skf3010MShatakuRoomBihin bihinSetValue = new Skf3010MShatakuRoomBihin();
			//主キー
			bihinSetValue.setShatakuKanriNo(setValue.getShatakuKanriNo());
			bihinSetValue.setShatakuRoomKanriNo(setValue.getShatakuRoomKanriNo());
			bihinSetValue.setBihinCd(map.get("bihinCode").toString());
			//データ
			bihinSetValue.setBihinStatusKbn(map.get("bihinStatus").toString());
				
			//登録処理を行う。
			if(!bihinSetValue.getBihinCd().isEmpty() && !bihinSetValue.getBihinStatusKbn().isEmpty()){
				registBihinCount += insertShatakuRoomBihin(bihinSetValue);
			}						
		}
		
		if(registBihinCount <= 0){
			//登録件数0以下の場合エラーとして終了
			return 0;
		}
	
		return registCount + registBihinCount;

	}
	
	
	/**
	 * 社宅部屋情報を更新する.
	 * 
	 * @param setValue 更新値を含むSkf3010MShatakuRoomインスタンス
	 * @param bihinInfoList 備品情報リスト
	 * @return 更新件数
	 */
	private int updateShatakuRoom(Skf3010MShatakuRoom setValue,List<Map<String,Object>> bihinInfoList) {

		int updateCount = 0;
		int updateRoomBihinCount = 0;
		
		// 排他チェック
		// 更新
		updateCount = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(setValue);
		
		LogUtils.debugByMsg("部屋情報更新件数：" + updateCount);
		if(updateCount <= 0){
			//更新件数0以下の場合エラーとして終了
			return 0;
		}

		//備品
		for(Map<String,Object> map : bihinInfoList){
			Skf3010MShatakuRoomBihin bihinSetValue = new Skf3010MShatakuRoomBihin();
			//主キー
			bihinSetValue.setShatakuKanriNo(setValue.getShatakuKanriNo());
			bihinSetValue.setShatakuRoomKanriNo(setValue.getShatakuRoomKanriNo());
			bihinSetValue.setBihinCd(map.get("bihinCode").toString());
			//データ
			bihinSetValue.setBihinStatusKbn(map.get("bihinStatus").toString());
			
			if(map.get("updateDate") == null){
				//更新日が存在しない場合（欠損レコード）、登録処理を行う。
				if(!bihinSetValue.getBihinCd().isEmpty() && !bihinSetValue.getBihinStatusKbn().isEmpty()){
					updateRoomBihinCount += insertShatakuRoomBihin(bihinSetValue);
				}
				
			}else{
				//更新処理を行う。
				//排他チェック
				bihinSetValue.setLastUpdateDate((Date) map.get("updateDate"));
				// 更新
				updateRoomBihinCount += updateShatakuRoomBihin(bihinSetValue);
			}
		}
		
		LogUtils.debugByMsg("部屋備品情報更新件数：" + updateRoomBihinCount);
		
		if(updateRoomBihinCount <= 0){
			//更新件数0以下の場合エラーとして終了
			return 0;
		}
			
		return updateCount + updateRoomBihinCount;
	}
	
	/**
	 * 社宅部屋情報マスタ登録（Insert)リポジトリ呼び出し
	 * @param setValue
	 * @return 登録件数
	 */
	private int insertShatakuRoomBihin(Skf3010MShatakuRoomBihin setValue){
		
		int insertCount=0;
		
		insertCount = skf3010MShatakuRoomBihinRepository.insertSelective(setValue);
				
		return insertCount;
		
	}
	
	/**
	 * 社宅部屋情報マスタ更新（Update)リポジトリ呼び出し
	 * @param setValue
	 * @return　更新件数
	 */
	private int updateShatakuRoomBihin(Skf3010MShatakuRoomBihin setValue){
		
		int updateCount=0;
		
		updateCount = skf3010MShatakuRoomBihinRepository.updateByPrimaryKeySelective(setValue);
		
		return updateCount;
		
	}
	
	/**
	 * 部屋情報取得
	 * @param registDto
	 */
	private void setRoomInfo(Skf3010Sc005RegistDto registDto) {
		
		//部屋情報取得
		Skf3010Sc005GetRoomInfoExp roomInfo = new Skf3010Sc005GetRoomInfoExp();
		roomInfo = skf3010Sc005SharedService.getRoomInfo(registDto.getHdnShatakuKanriNo(), registDto.getHdnRoomKanriNo());
		
		//部屋情報設定
		//部屋番号
		registDto.setRoomNo(roomInfo.getRoomNo());
		//本来規格
		registDto.setOriginalKikaku(roomInfo.getOriginalKikaku());
		//本来規格補足
		registDto.setOriginalKikakuHosoku(roomInfo.getOriginalKikakuHosoku());
		//本来用途
		registDto.setOriginalAuse(roomInfo.getOriginalAuse());
		//本来用途補足
		registDto.setOriginalAuseHosoku(roomInfo.getOriginalAuseHosoku());
		//貸与区分
		registDto.setLendKbn(roomInfo.getLendKbn());
		//貸与区分補足
		registDto.setLendKbnHosoku(roomInfo.getLendKbnHosoku());
		//備考
		registDto.setBiko(roomInfo.getBiko());
		//本来延面積
		registDto.setOriginalMenseki(roomInfo.getOriginalMenseki().toPlainString());
		//貸与面積
		registDto.setLendMenseki(roomInfo.getLendMenseki().toPlainString());
		//サンルーム面積
		registDto.setSunRoomMenseki(roomInfo.getSunRoomMenseki().toPlainString());
		//寒冷地減免事由区分
		registDto.setColdExemptionKbn(roomInfo.getColdExemptionKbn());
		//階段面積
		registDto.setStairsMenseki(roomInfo.getStairsMenseki().toPlainString());
		//物置面積
		registDto.setBarnMenseki(roomInfo.getBarnMenseki().toPlainString());
		//物置調整面積
		registDto.setBarnMensekiAdjust(roomInfo.getBarnMensekiAdjust().toPlainString());
		//社宅部屋情報マスタ排他用更新日付
		
		registDto.addLastUpdateDate("Skf3010Sc005GetRoomInfoUpdateDate", roomInfo.getLastUpdateDate());
	}


}
