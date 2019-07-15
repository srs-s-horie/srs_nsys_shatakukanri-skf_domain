/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3010Sc001InitService extends BaseServiceAbstract<Skf3010Sc001InitDto> {
	
	@Autowired
	private Skf3010Sc001SharedService skf3010Sc001SharedService;
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3010.skf3010_sc001.max_row_count}")
	private String listTableMaxRowCount;

	/**
	 * サービス処理を行う。 最初に呼び出されるメソッド
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3010Sc001InitDto index(Skf3010Sc001InitDto initDto) throws Exception {
		
		// 操作ログの出力
		// LogUtils.info(MessageIdConstant.L_SKF_INIT);

		// 初期表示フラグ
		Boolean initializeFlg = true;

		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyRoomList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyParkingList = new ArrayList<Map<String, Object>>();

		// 利用区分を「使用中」に設定
		initDto.setUseKbn("2");
		// 管理機関プルダウンを活性
		initDto.setAgencyDispFlg(true);
		// セッション情報存在判定
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& initDto.getPrePageId().equals(FunctionIdConstant.SKF3010_SC001)) {
			// セッション情報ありの場合：検索キー設定
			// 初期表示フラグをfalseに設定
			initializeFlg = false;
			// 管理会社の選択値を設定
			initDto.setSelectedCompanyCd(initDto.getHdnSelectedCompanyCd());
			// 管理会社選択値判定
			if (initDto.getSelectedCompanyCd() != null && initDto.getSelectedCompanyCd().equals("ZZZZ")) {
				// 外部機関の場合
				// 管理機関コードを空文字に設定
				initDto.setAgencyCd(null);
				// 外部機関選択時は管理機関プルダウンを非活性
				initDto.setAgencyDispFlg(false);
			} else {
				// 外部機関以外の場合
				// 外部機関選択時は管理機関プルダウンを活性
				initDto.setAgencyDispFlg(true);
				// 管理機関の選択値を設定
				initDto.setAgencyCd(initDto.getHdnAgencyCd());
			}
			// 社宅区分選択値設定
			initDto.setShatakuKbn(initDto.getHdnAgencyCd());
			// 利用区分選択値設定
			initDto.setUseKbn(initDto.getHdnUseKbn());
			// 空き部屋選択値設定
			initDto.setEmptyRoom(initDto.getHdnEmptyRoom());
			// 空き駐車場選択値設定
			initDto.setEmptyParking(initDto.getHdnEmptyParking());
			// 社宅名設定
			initDto.setShatakuName(initDto.getHdnShatakuName());
			// 社宅住所設定
			initDto.setShatakuAddress(initDto.getHdnShatakuAddress());
		} else {
			// 画面連携のhidden項目を初期化
			initDto.setHdnAgencyCd(null);
			initDto.setHdnEmptyParking(null);
			initDto.setHdnEmptyRoom(null);
			initDto.setHdnSelectedCompanyCd(null);
			initDto.setHdnShatakuAddress(null);
			initDto.setHdnShatakuKbn(null);
			initDto.setHdnShatakuName(null);
			initDto.setHdnUseKbn(null);
		}
		// ドロップダウンリストの値を設定
		skf3010Sc001SharedService.getDoropDownList(initDto.getSelectedCompanyCd(), manageCompanyList,
				initDto.getAgencyCd(), manageAgencyList, initDto.getShatakuKbn(), shatakuKbnList,
				initDto.getEmptyRoom(), emptyRoomList, initDto.getUseKbn(), useKbnList, initDto.getEmptyParking(),
				emptyParkingList);

		// 社宅一覧表示
		// リストテーブルの情報を取得
		int listCount = skf3010Sc001SharedService.getListTableData(initDto.getSelectedCompanyCd(),
				initDto.getAgencyCd(), initDto.getShatakuKbn(), initDto.getEmptyRoom(), initDto.getUseKbn(),
				initDto.getEmptyParking(), initDto.getShatakuName(), initDto.getShatakuAddress(), listTableData);

		// 初期表示に限り、0件メッセージは表示しない
		if (!initializeFlg && listCount == 0) {
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
		}

		// 戻り値をセット
		initDto.setPageTitleKey(MessageIdConstant.SKF3010_SC001_TITLE);
		initDto.setListTableMaxRowCount(listTableMaxRowCount);

		initDto.setManageCompanyList(manageCompanyList);
		initDto.setManageAgencyList(manageAgencyList);
		initDto.setEmptyRoomList(emptyRoomList);
		initDto.setShatakuKbnList(shatakuKbnList);
		initDto.setUseKbnList(useKbnList);
		initDto.setEmptyParkingList(emptyParkingList);
		initDto.setListTableData(listTableData);
 		
		return initDto;
	}
	
}
