/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc004.Skf3010Sc004InitDto;

/**
 * Skf3010Sc004InitService 社宅部屋一覧初期表示処理クラス
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3010Sc004InitService extends SkfServiceAbstract<Skf3010Sc004InitDto> {
	
	@Autowired
	private Skf3010Sc004SharedService skf3010Sc004SharedService;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3010.skf3010_sc004.max_row_count}")
	private String listTableMaxRowCount;
		
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc004InitDto index(Skf3010Sc004InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC004_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3010_SC004);
		initialize(initDto);
				
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& (Objects.equals(initDto.getPrePageId(), FunctionIdConstant.SKF3010_SC005)
						|| Objects.equals(initDto.getPrePageId(), FunctionIdConstant.SKF3010_SC004))) {

			/** 保有社宅部屋登録画面からの遷移 */
			LogUtils.debugByMsg("保有社宅部屋登録画面からの遷移");
			// 登録画面のhidden項目をinitDtoに詰めなおす
			initDto.setShatakuKanriNo(Long.parseLong(initDto.getHdnShatakuKanriNo()));
			initDto.setShatakuName(initDto.getHdnShatakuName());
			initDto.setAreaKbn(initDto.getHdnAreaKbn());
			initDto.setShatakuKbn(initDto.getHdnShatakuKbn());
			initDto.setHdnEmptyParkingCount(initDto.getHdnEmptyParkingCount());
			// initDto.setEmptyRoomCount(initDto.getHdnEmptyRoomCount());
			initDto.setOriginalAuse(initDto.getHdnOriginalAuse());
			initDto.setLendKbn(initDto.getHdnLendKbn());
			// 画面連携のhidden項目を初期化
			initDto.setHdnRoomKanriNo(null);

			setSearchInfoBack(initDto);

			// リストテーブルの情報を取得
			skf3010Sc004SharedService.getListTableData(initDto.getShatakuKanriNo(), initDto.getOriginalAuse(),
					initDto.getLendKbn(), listTableData);
			// 初期表示に限り、0件メッセージは表示しない

		} else {
			LogUtils.debugByMsg("社宅一覧から遷移");
			// 社宅一覧から遷移
			// 画面連携のhidden項目を初期化
			initDto.setHdnRoomKanriNo(null);
			// 検索条件クリア
			initDto.setOriginalAuse(null);
			initDto.setLendKbn(null);
			// 呼び出し元のhidden項目をinitDtoに詰める
			initDto.setShatakuKanriNo(Long.parseLong(initDto.getHdnRowShatakuKanriNo()));
			initDto.setShatakuName(initDto.getHdnRowShatakuName());
			initDto.setHdnShatakuKanriNo(initDto.getHdnRowShatakuKanriNo());
			initDto.setHdnShatakuName(initDto.getHdnRowShatakuName());
			initDto.setHdnShatakuKbn(initDto.getHdnRowShatakuKbn());
			initDto.setHdnAreaKbn(initDto.getHdnRowAreaKbn());
			initDto.setHdnEmptyRoomCount(initDto.getHdnRowEmptyRoomCount());
			initDto.setHdnEmptyParkingCount(initDto.getHdnRowEmptyParkingCount());

			// 検索部をセット
			setSearchInfo(initDto);

			// // リストテーブルの情報を取得
			skf3010Sc004SharedService.getListTableData(initDto.getShatakuKanriNo(), "", "",
					listTableData);
			// 初期表示に限り、0件メッセージは表示しない			

		}

		// ========== 画面表示 ==========
		// 「本来用途」ドロップダウンリストの設定
		// 「貸与区分」ドロップダウンリストの設定
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> auseList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lendList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3010Sc004SharedService.getDoropDownList(initDto.getOriginalAuse(), auseList, initDto.getLendKbn(), lendList);

		// 戻り値をセット
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC004_TITLE);
		initDto.setListTableMaxRowCount(listTableMaxRowCount);

		initDto.setAuseList(auseList);
		initDto.setLendList(lendList);

		initDto.setListTableData(listTableData);
 		
		return initDto;
	}
	
	/**
	 * 検索部のセット
	 * 
	 * @param initDto
	 */
	private void setSearchInfo(Skf3010Sc004InitDto initDto) {

		// 駐車場総数を取得する
		int parkingCouunt = skf3010Sc004SharedService.getParkingCount(initDto.getShatakuKanriNo());

		// 「空き駐車場数」を設定する
		String emptyParkingCount = initDto.getHdnEmptyParkingCount() + CodeConstant.SLASH + parkingCouunt;
		initDto.setEmptyParkingCount(emptyParkingCount);

		// 社宅部屋総数を取得する
		int roomCount = skf3010Sc004SharedService.getRoomCount(initDto.getShatakuKanriNo());

		// 空き社宅部屋総数を取得する
		int emptyRoomCount = skf3010Sc004SharedService.getEmptyRoomCount(initDto.getShatakuKanriNo());

		String emptyRoomStr = emptyRoomCount + CodeConstant.SLASH + roomCount;
		initDto.setEmptyRoomCount(emptyRoomStr);

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
	 * 検索部のセット(SKF3010_SC005社宅部屋登録画面から戻る)
	 * 
	 * @param initDto
	 */
	private void setSearchInfoBack(Skf3010Sc004InitDto initDto) {

		// 社宅部屋総数を取得する
		int roomCount = skf3010Sc004SharedService.getRoomCount(initDto.getShatakuKanriNo());
		// 空き社宅部屋総数を取得する
		int emptyRoomCount = skf3010Sc004SharedService.getEmptyRoomCount(initDto.getShatakuKanriNo());

		String emptyRoomCountStr = emptyRoomCount + CodeConstant.SLASH + roomCount;
		initDto.setEmptyRoomCount(emptyRoomCountStr);
		
		// 駐車場総数を取得する
		int parkingCouunt = skf3010Sc004SharedService.getParkingCount(initDto.getShatakuKanriNo());
		// 「空き駐車場数」を設定する
		String emptyParkingCount = initDto.getHdnEmptyParkingCount() + CodeConstant.SLASH + parkingCouunt;
		initDto.setEmptyParkingCount(emptyParkingCount);

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
	 * 初期化処理
	 * 「*」項目はアドレスとして戻り値になる
	 * @param comDto	*DTO
	 */
	private void initialize(Skf3010Sc004InitDto comDto) {

		// リストテーブルデータ
		comDto.setListTableData(null);
		// 部屋管理番号
		comDto.setShatakuRoomKanriNo(null);
		// 部屋名ROOM_NO
		comDto.setRoomNo(null);
		// 本来社宅用途ORIGINAL_AUSE
		comDto.setOriginalAuse(null);
		// 本来規格ORIGINAL_KIKAKU
		comDto.setOriginalKikaku(null);
		// 本来延面積ORIGINAL_MENSEKI
		comDto.setOriginalMenseki(null);
		// 貸与区分LEND_KBN
		comDto.setLendKbn(null);
		// 備考BIKO
		comDto.setBiko(null);
		// 氏名漢字
		comDto.setName(null);
		// 社宅名
		comDto.setShatakuName(null);
		// 地域区分
		comDto.setAreaKbn(null);
		// 社宅区分
		comDto.setShatakuKbn(null);
		// 空き部屋数
		comDto.setEmptyRoomCount(null);
		// 空き駐車場数
		comDto.setEmptyParkingCount(null);
		// 社宅管理番号
		comDto.setShatakuKanriNo(null);
		// 更新フラグ
		comDto.setUpdateFlag(null);
		// 本来用途リスト
		comDto.setAuseList(null);
		// 貸与区分リスト
		comDto.setLendList(null);
	}
}
