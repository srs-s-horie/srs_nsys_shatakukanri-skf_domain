/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc005.Skf3010Sc005InitDto;

/**
 * Skf3010Sc005InitService 社宅部屋登録画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3010Sc005InitService extends BaseServiceAbstract<Skf3010Sc005InitDto> {
	
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	@Autowired
	private Skf3010Sc005SharedService skf3010Sc005SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	/**
	 * 初期表示サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc005InitDto index(Skf3010Sc005InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC005_TITLE);

		//操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
				
		// 画面のhidden項目をinitDtoに詰めなおす
		initDto.setHdnRoomKanriNo(initDto.getHdnRoomKanriNo());
		initDto.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
		initDto.setShatakuName(initDto.getHdnShatakuName());
		initDto.setAreaKbn(initDto.getHdnAreaKbn());
		initDto.setShatakuKbn(initDto.getHdnShatakuKbn());
		initDto.setEmptyRoomCount(initDto.getHdnEmptyRoomCount());
		initDto.setEmptyParkingCount(initDto.getHdnEmptyParkingCount());

		// 'ヘッダ部の値をセット
		setSearchInfo(initDto);

		// ' ========== 画面表示 ==========
		// ' 「本来規格」ドロップダウンリストの設定
		// ' 「本来用途」ドロップダウンリストの設定
		// ' 「貸与区分」ドロップダウンリストの設定
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> originalAuseList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lendKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> originalKikakuList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> coldExemptionKbnList = new ArrayList<Map<String, Object>>();
		
		//備品情報リスト
		List<Map<String, Object>> bihinListData = new ArrayList<Map<String, Object>>();
		//非表示備品情報リスト
		List<Map<String, Object>> hdnBihinStatusList = new ArrayList<Map<String, Object>>();
		
		// '詳細ボタンから遷移する時
		if(!NfwStringUtils.isEmpty(initDto.getHdnRoomKanriNo())){
			//部屋管理番号設定ありの場合、データ更新
			LogUtils.debugByMsg("社宅部屋登録(更新)" + ":部屋管理番号=" + initDto.getHdnRoomKanriNo());
			// 'データ操作区分（更新）
			initDto.setHdnRegistFlg(Skf3010Sc005CommonSharedService.DATA_UPDATE);
			//
			// '部屋情報をセット
			setRoomInfo(initDto);

			// '備品情報をセット
			skf3010Sc005SharedService.setBihinInfo(initDto.getHdnShatakuKanriNo(), initDto.getHdnRoomKanriNo(),bihinListData,hdnBihinStatusList);
			initDto.setMaskPattern("");
			initDto.setDeleteBtnFlg("false");
			//ドロップダウンリストの設定
			skf3010Sc005SharedService.getDoropDownList(initDto.getOriginalKikaku(), originalKikakuList, 
					initDto.getOriginalAuse(), originalAuseList,
					initDto.getLendKbn(), lendKbnList, 
					initDto.getColdExemptionKbn(), coldExemptionKbnList);
		}else{
			//新規
			LogUtils.debugByMsg("社宅部屋登録(登録)");
			// 'データ操作区分（登録）
			initDto.setHdnRegistFlg(Skf3010Sc005CommonSharedService.DATA_INSERT);
			//部屋情報デフォルト（空文字）セット
			setRoomInfoDefault(initDto);
			// '新規の備品情報をセット
			skf3010Sc005SharedService.setBihinInfoOfShinki(initDto.getHdnShatakuKanriNo(), initDto.getHdnRoomKanriNo(),bihinListData,hdnBihinStatusList);
			
			// '削除ボタンを非活性にする
			initDto.setMaskPattern("INSERT");
			initDto.setDeleteBtnFlg("true");
			
			//ドロップダウンリストの設定
			skf3010Sc005SharedService.getDoropDownList("", originalKikakuList, 
					"", originalAuseList,
					"", lendKbnList, 
					"", coldExemptionKbnList);
		}
		
		initDto.setOriginalKikakuList(originalKikakuList);
		initDto.setOriginalAuseList(originalAuseList);
		initDto.setLendKbnList(lendKbnList);
		initDto.setColdExemptionKbnList(coldExemptionKbnList);
		initDto.setBihinListData(bihinListData);
		initDto.setHdnBihinStatusList(hdnBihinStatusList);
		
		// エラー系のDto値を初期化
		initDto.setRoomNoError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalMensekiError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setLendMensekiError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalKikakuError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalAuseError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setLendKbnError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setSunRoomMensekiError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setStairsMensekiError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setBarnMensekiError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalKikakuHosokuError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setOriginalAuseHosokuError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setLendKbnHosokuError(CodeConstant.DOUBLE_QUOTATION);
		initDto.setBikoError(CodeConstant.DOUBLE_QUOTATION);
 		
		return initDto;
	}
	
	/**
	 * ヘッダ部情報セット
	 * @param initDto
	 */
	private void setSearchInfo(Skf3010Sc005InitDto initDto) {

		// 「地域区分」の設定
		// 汎用コード取得
		Map<String, String> genericCodeMapArea = new HashMap<String, String>();
		genericCodeMapArea = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
		String areaKbn = "";
		if (initDto.getHdnAreaKbn() != null) {
			areaKbn = genericCodeMapArea.get(initDto.getHdnAreaKbn());
		}
		initDto.setAreaKbn(areaKbn);

		// 「社宅区分」を設定
		// 汎用コード取得
		Map<String, String> genericCodeMapShataku = new HashMap<String, String>();
		genericCodeMapShataku = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		String shatakuKbn = "";
		if (initDto.getHdnShatakuKbn() != null) {
			shatakuKbn = genericCodeMapShataku.get(initDto.getHdnShatakuKbn());
		}
		initDto.setShatakuKbn(shatakuKbn);
	}
	
	/**
	 * 部屋情報取得（詳細）
	 * @param initDto
	 */
	private void setRoomInfo(Skf3010Sc005InitDto initDto) {
		
		//部屋情報取得
		Skf3010Sc005GetRoomInfoExp roomInfo = new Skf3010Sc005GetRoomInfoExp();
		roomInfo = skf3010Sc005SharedService.getRoomInfo(initDto.getHdnShatakuKanriNo(), initDto.getHdnRoomKanriNo());
		
		//部屋情報設定
		//部屋番号
		initDto.setRoomNo(roomInfo.getRoomNo());
		//本来規格
		initDto.setOriginalKikaku(roomInfo.getOriginalKikaku());
		//本来規格補足
		initDto.setOriginalKikakuHosoku(roomInfo.getOriginalKikakuHosoku());
		//本来用途
		initDto.setOriginalAuse(roomInfo.getOriginalAuse());
		//本来用途補足
		initDto.setOriginalAuseHosoku(roomInfo.getOriginalAuseHosoku());
		//貸与区分
		initDto.setLendKbn(roomInfo.getLendKbn());
		//貸与区分補足
		initDto.setLendKbnHosoku(roomInfo.getLendKbnHosoku());
		//備考
		initDto.setBiko(roomInfo.getBiko());
		//本来延面積
		initDto.setOriginalMenseki(roomInfo.getOriginalMenseki().toPlainString());
		//貸与面積
		initDto.setLendMenseki(roomInfo.getLendMenseki().toPlainString());
		//サンルーム面積
		initDto.setSunRoomMenseki(roomInfo.getSunRoomMenseki().toPlainString());
		//寒冷地減免事由区分
		initDto.setColdExemptionKbn(roomInfo.getColdExemptionKbn());
		//階段面積
		initDto.setStairsMenseki(roomInfo.getStairsMenseki().toPlainString());
		//物置面積
		initDto.setBarnMenseki(roomInfo.getBarnMenseki().toPlainString());
		//物置調整面積
		initDto.setBarnMensekiAdjust(roomInfo.getBarnMensekiAdjust().toPlainString());
		//社宅部屋情報マスタ排他用更新日付
		
		initDto.addLastUpdateDate("Skf3010Sc005GetRoomInfoUpdateDate", roomInfo.getLastUpdateDate());
	}
	
	/**
	 * 画面項目初期化（新規）
	 * @param initDto
	 */
	private void setRoomInfoDefault(Skf3010Sc005InitDto initDto) {
		
		//部屋情報設定
		//部屋番号
		initDto.setRoomNo(CodeConstant.DOUBLE_QUOTATION);
		//本来規格
		initDto.setOriginalKikaku(CodeConstant.DOUBLE_QUOTATION);
		//本来規格補足
		initDto.setOriginalKikakuHosoku(CodeConstant.DOUBLE_QUOTATION);
		//本来用途
		initDto.setOriginalAuse(CodeConstant.DOUBLE_QUOTATION);
		//本来用途補足
		initDto.setOriginalAuseHosoku(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分
		initDto.setLendKbn(CodeConstant.DOUBLE_QUOTATION);
		//貸与区分補足
		initDto.setLendKbnHosoku(CodeConstant.DOUBLE_QUOTATION);
		//備考
		initDto.setBiko(CodeConstant.DOUBLE_QUOTATION);
		//本来延面積
		initDto.setOriginalMenseki(CodeConstant.DOUBLE_QUOTATION);
		//貸与面積
		initDto.setLendMenseki(CodeConstant.DOUBLE_QUOTATION);
		//サンルーム面積
		initDto.setSunRoomMenseki(CodeConstant.DOUBLE_QUOTATION);
		//寒冷地減免事由区分
		initDto.setColdExemptionKbn(CodeConstant.DOUBLE_QUOTATION);
		//階段面積
		initDto.setStairsMenseki(CodeConstant.DOUBLE_QUOTATION);
		//物置面積
		initDto.setBarnMenseki(CodeConstant.DOUBLE_QUOTATION);
		//物置調整面積
		initDto.setBarnMensekiAdjust(CodeConstant.DOUBLE_QUOTATION);
		
	}

}
