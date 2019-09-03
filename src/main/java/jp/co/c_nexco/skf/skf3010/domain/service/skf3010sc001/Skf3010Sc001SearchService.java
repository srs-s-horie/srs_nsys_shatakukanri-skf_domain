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
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001SearchDto;

/**
 * Skf3010Sc001SearchService 社宅一覧検索ボタン押下時処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc001SearchService extends BaseServiceAbstract<Skf3010Sc001SearchDto> {

	@Value("${skf3010.skf3010_sc001.max_search_count}")
	private Integer maxGetRecordCount;
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3010.skf3010_sc001.max_row_count}")
	private String listTableMaxRowCount;

	@Autowired
	private Skf3010Sc001SharedService skf3010Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	public BaseDto index(Skf3010Sc001SearchDto searchDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());

		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyRoomList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyParkingList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3010Sc001SharedService.getDoropDownList(searchDto.getSelectedCompanyCd(), manageCompanyList,
				searchDto.getAgencyCd(), manageAgencyList, searchDto.getShatakuKbnCd(), shatakuKbnList,
				searchDto.getEmptyRoomCd(), emptyRoomList, searchDto.getUseKbnCd(), useKbnList, searchDto.getEmptyParkingCd(),
				emptyParkingList);

		// 社宅一覧表示
		// リストテーブルの情報を取得
		int listCount = skf3010Sc001SharedService.getListTableData(searchDto.getSelectedCompanyCd(),
				searchDto.getAgencyCd(), searchDto.getShatakuKbnCd(), searchDto.getEmptyRoomCd(), searchDto.getUseKbnCd(),
				searchDto.getEmptyParkingCd(), searchDto.getShatakuName(), searchDto.getShatakuAddress(), listTableData);

		// エラーメッセージ設定
		if (listCount == 0) {
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
		} else if (listCount > maxGetRecordCount.intValue()) {
			// 取得レコード限界値超のエラー
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.E_SKF_1046,
					maxGetRecordCount.toString());
			// ServiceHelper.addErrorResultMessage(searchDto, new String[] {
			// "Skf3090Sc004SearchService}" },
			// MessageIdConstant.E_SKF_1046, maxGetRecordCount.toString());
		}

		// セッション情報削除
		searchDto.setPageTitleKey(null);
		searchDto.setListTableMaxRowCount(null);
		searchDto.setManageCompanyList(null);
		searchDto.setManageAgencyList(null);
		searchDto.setEmptyRoomList(null);
		searchDto.setShatakuKbnList(null);
		searchDto.setUseKbnList(null);
		searchDto.setEmptyParkingList(null);
		searchDto.setListTableData(null);

		// 戻り値をセット
		// 画面タイトル
		searchDto.setPageTitleKey(MessageIdConstant.SKF3010_SC001_TITLE);
		// 最大行数
		searchDto.setListTableMaxRowCount(listTableMaxRowCount);
		// ドロップダウン系
		searchDto.setManageCompanyList(manageCompanyList);
		searchDto.setManageAgencyList(manageAgencyList);
		searchDto.setEmptyRoomList(emptyRoomList);
		searchDto.setShatakuKbnList(shatakuKbnList);
		searchDto.setUseKbnList(useKbnList);
		searchDto.setEmptyParkingList(emptyParkingList);
		// リストテーブル
		searchDto.setListTableData(listTableData);

		// // 管理会社の選択値を設定
		// searchDto.setSelectedCompanyCd(searchDto.getHdnSelectedCompanyCd());
		// 管理会社選択値判定
		if (searchDto.getSelectedCompanyCd() != null && searchDto.getSelectedCompanyCd().equals("ZZZZ")) {
			// 外部機関の場合
			// 管理機関コードを空文字に設定
			searchDto.setAgencyCd(null);
			// 外部機関選択時は管理機関プルダウンを非活性
			searchDto.setAgencyDispFlg(false);
		} else {
			// 外部機関以外の場合
			// 外部機関選択時は管理機関プルダウンを活性
			searchDto.setAgencyDispFlg(true);
		}
		// 管理会社選択値設定(検索キー)
		searchDto.setHdnSelectedCompanyCd(searchDto.getSelectedCompanyCd());
		// 管理機関選択値設定(検索キー)
		searchDto.setHdnAgencyCd(searchDto.getAgencyCd());
		// 社宅区分選択値設定(検索キー)
		searchDto.setHdnShatakuKbnCd(searchDto.getShatakuKbnCd());
		// 利用区分選択値設定(検索キー)
		searchDto.setHdnUseKbnCd(searchDto.getUseKbnCd());
		// 空き部屋選択値設定(検索キー)
		searchDto.setHdnEmptyRoomCd(searchDto.getEmptyRoomCd());
		// 空き駐車場選択値設定(検索キー)
		searchDto.setHdnEmptyParkingCd(searchDto.getEmptyParkingCd());
		// 社宅名設定(検索キー)
		searchDto.setHdnShatakuName(searchDto.getShatakuName());
		// 社宅住所設定(検索キー)
		searchDto.setHdnShatakuAddress(searchDto.getShatakuAddress());

		// 解放
		listTableData = null;
		manageCompanyList = null;
		manageAgencyList = null;
		emptyRoomList = null;
		shatakuKbnList = null;
		useKbnList = null;
		emptyParkingList = null;
		return searchDto;
	}

}
