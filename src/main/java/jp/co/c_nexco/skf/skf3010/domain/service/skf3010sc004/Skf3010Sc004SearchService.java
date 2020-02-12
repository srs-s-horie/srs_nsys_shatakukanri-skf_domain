/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc004.Skf3010Sc004SearchDto;

/**
 * Skf3010Sc004InitService 社宅部屋一覧初期表示処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc004SearchService extends BaseServiceAbstract<Skf3010Sc004SearchDto> {

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
	 * @param searchDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc004SearchDto index(Skf3010Sc004SearchDto searchDto) throws Exception {

		searchDto.setPageTitleKey(MessageIdConstant.SKF3010_SC004_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		// 社宅一覧から遷移
		// 画面連携のhidden項目を初期化
		searchDto.setHdnRoomKanriNo(null);
		// 登録画面のhidden項目をinitDtoに詰めなおす
		searchDto.setShatakuKanriNo(Long.parseLong(searchDto.getHdnShatakuKanriNo()));
		searchDto.setShatakuName(searchDto.getHdnShatakuName());
		// initDto.setAreaKbn(initDto.getHdnAreaKbn());
		// initDto.setShatakuKbn(initDto.getHdnShatakuKbn());
		// initDto.setEmptyParkingCount(initDto.getHdnEmptyParkingCount());
		// initDto.setEmptyRoomCount(initDto.getHdnEmptyRoomCount());

		// 検索部をセット
		//SetSearchInfo(searchDto);

		// // リストテーブルの情報を取得
		int listCount = skf3010Sc004SharedService.getListTableData(searchDto.getShatakuKanriNo(),
				searchDto.getOriginalAuse(), searchDto.getLendKbn(), listTableData);

		// エラーメッセージ設定
		if (listCount == 0) {
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
		}

		// ========== 画面表示 ==========
		// 「本来用途」ドロップダウンリストの設定
		// 「貸与区分」ドロップダウンリストの設定
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> auseList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> lendList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3010Sc004SharedService.getDoropDownList(searchDto.getOriginalAuse(), auseList, searchDto.getLendKbn(),
				lendList);

		// 戻り値をセット
		searchDto.setPageTitleKey(MessageIdConstant.SKF3010_SC004_TITLE);
		// searchDto.setListTableMaxRowCount(listTableMaxRowCount);

		searchDto.setAuseList(auseList);
		searchDto.setLendList(lendList);

		searchDto.setListTableData(listTableData);

		return searchDto;
	}

	/**
	 * 検索部のセット
	 * 
	 * @param searchDto
	 */
	private void SetSearchInfo(Skf3010Sc004SearchDto searchDto) {

		// 駐車場総数を取得する
		int parkingCouunt = skf3010Sc004SharedService.getParkingCount(searchDto.getShatakuKanriNo());

		// 「空き駐車場数」を設定する
		String emptyParkingCount = searchDto.getHdnEmptyParkingCount() + CodeConstant.SLASH + parkingCouunt;
		searchDto.setEmptyParkingCount(emptyParkingCount);

		// 社宅部屋総数を取得する
		int roomCount = skf3010Sc004SharedService.getRoomCount(searchDto.getShatakuKanriNo());
		String emptyRoomCount = searchDto.getHdnEmptyRoomCount() + CodeConstant.SLASH + roomCount;
		searchDto.setEmptyRoomCount(emptyRoomCount);

		// 「地域区分」の設定
		// 汎用コード取得
		Map<String, String> genericCodeMapArea = new HashMap<String, String>();
		genericCodeMapArea = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AREA_KBN);
		String areaKbn = "";
		if (searchDto.getHdnAreaKbn() != null) {
			areaKbn = genericCodeMapArea.get(searchDto.getHdnAreaKbn());
		}
		searchDto.setAreaKbn(areaKbn);

		// 「社宅区分」を設定
		// 汎用コード取得
		Map<String, String> genericCodeMapShataku = new HashMap<String, String>();
		genericCodeMapShataku = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		String shatakuKbn = "";
		if (searchDto.getHdnShatakuKbn() != null) {
			shatakuKbn = genericCodeMapShataku.get(searchDto.getHdnShatakuKbn());
		}
		searchDto.setShatakuKbn(shatakuKbn);

	}
}
