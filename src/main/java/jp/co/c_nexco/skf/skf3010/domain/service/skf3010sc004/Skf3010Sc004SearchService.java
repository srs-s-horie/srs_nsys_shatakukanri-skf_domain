/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc004.Skf3010Sc004SearchDto;

/**
 * Skf3010Sc004InitService 社宅部屋一覧初期表示処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc004SearchService extends SkfServiceAbstract<Skf3010Sc004SearchDto> {

	@Autowired
	private Skf3010Sc004SharedService skf3010Sc004SharedService;
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
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF3010_SC004);
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		// 社宅一覧から遷移
		// 画面連携のhidden項目を初期化
		searchDto.setHdnRoomKanriNo(null);
		// 登録画面のhidden項目をinitDtoに詰めなおす
		searchDto.setShatakuKanriNo(Long.parseLong(searchDto.getHdnShatakuKanriNo()));
		searchDto.setShatakuName(searchDto.getHdnShatakuName());

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
		// 社宅部屋総数を取得する(戻るボタンで戻った際更新されないので、検索時に更新する)
		int roomCount = skf3010Sc004SharedService.getRoomCount(searchDto.getShatakuKanriNo());
		// 空き社宅部屋総数を取得する
		int emptyRoomCount = skf3010Sc004SharedService.getEmptyRoomCount(searchDto.getShatakuKanriNo());

		String emptyRoomCountStr = emptyRoomCount + CodeConstant.SLASH + roomCount;
		searchDto.setEmptyRoomCount(emptyRoomCountStr);

		searchDto.setAuseList(auseList);
		searchDto.setLendList(lendList);

		searchDto.setListTableData(listTableData);

		return searchDto;
	}

}
