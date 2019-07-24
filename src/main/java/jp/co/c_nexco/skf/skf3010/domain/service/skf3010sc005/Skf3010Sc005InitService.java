/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc005.Skf3010Sc005InitDto;

/**
 * Skf3010Sc005InitService 社宅部屋登録画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3010Sc005InitService extends BaseServiceAbstract<Skf3010Sc005InitDto> {
	
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc005InitDto index(Skf3010Sc005InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC005_TITLE);

		// 登録画面のhidden項目をinitDtoに詰めなおす
		initDto.setHdnRoomKanriNo(initDto.getHdnRoomKanriNo());
		initDto.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
		initDto.setShatakuName(initDto.getHdnShatakuName());
		initDto.setAreaKbn(initDto.getHdnAreaKbn());
		initDto.setShatakuKbn(initDto.getHdnShatakuKbn());
		initDto.setEmptyParkingCount(initDto.getHdnEmptyParkingCount());

		// 'ヘッダ部の値をセット
		// Me.SetSearchInfo(DirectCast(ViewState(TARGET_SHATAKU_NAME), String),
		// _
		// DirectCast(ViewState(TARGET_AREA_KBN), String), _
		// DirectCast(ViewState(TARGET_SHATAKU_KBN), String), _
		// DirectCast(ViewState(TARGET_EMPTY_ROOM_COUNT), String), _
		// DirectCast(ViewState(TARGET_EMPTY_PARKING_COUNT), String))
		// '詳細ボタンから遷移する時
		// If Not
		// String.IsNullOrEmpty(DirectCast(ViewState(TARGET_SHATAKU_ROOM_KANRI_NO),
		// String)) Then
		//
		// 'データ操作区分（更新）
		// Me.hdnRegistFlg.Value = HttpUtility.HtmlEncode(DATA_UPDATE)
		//
		// '部屋情報をセット
		// Me.SetRoomInfo(DirectCast(ViewState(TARGET_SHATAKU_KANRI_NO),
		// String), _
		// DirectCast(ViewState(TARGET_SHATAKU_ROOM_KANRI_NO), String))
		// '備品情報をセット
		// Me.SetBihinInfo(DirectCast(ViewState(TARGET_SHATAKU_KANRI_NO),
		// String), _
		// DirectCast(ViewState(TARGET_SHATAKU_ROOM_KANRI_NO), String))
		//
		// Else
		// '新規ボタンから遷移する時
		//
		// 'データ操作区分（登録）
		// Me.hdnRegistFlg.Value() = HttpUtility.HtmlEncode(DATA_INSERT)
		//
		// '新規の備品情報をセット
		// Me.SetBihinInfoOfShinki(DirectCast(ViewState(TARGET_SHATAKU_KANRI_NO),
		// String))
		//
		// '削除ボタンを非活性にする
		// Me.btnDelete.Enabled = False
		//
		// End If

		// ' ========== 画面表示 ==========
		// ' 「本来規格」ドロップダウンリストの設定
		// Me.SetDdlValues(Me.ddlOriginalKikaku, ddlOriginalKikakuValues, True)
		// ' 「本来用途」ドロップダウンリストの設定
		// Me.SetDdlValues(Me.ddlOriginalAuse, ddlOriginalAuseValues, True)
		// ' 「貸与区分」ドロップダウンリストの設定
		// Me.SetDdlValues(Me.ddlLendKbn, ddlLendKbnValues, True)
		// ' 「寒冷地減免事由区分」ドロップダウンリストの設定
 		
		return initDto;
	}
	
	private void SetSearchInfo(Skf3010Sc005InitDto initDto) {

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

}
