/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001InitDto;

/**
 * Skf3010Sc001InitService 社宅一覧画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001InitService extends BaseServiceAbstract<Skf3010Sc001InitDto> {

	@Autowired
	private Skf3010Sc001SharedService skf3010Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
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

		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		initDto.setPageId(FunctionIdConstant.SKF3010_SC001);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3010_SC001);
		// 初期化
		initialize(initDto);

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
		initDto.setUseKbnCd("2");
		// 管理機関プルダウンを活性
		initDto.setAgencyDispFlg(true);

		// 遷移元判定
		if (FunctionIdConstant.SKF3010_SC001.equals(initDto.getPrePageId())
				|| FunctionIdConstant.SKF3010_SC002.equals(initDto.getPrePageId())
				|| FunctionIdConstant.SKF3010_SC004.equals(initDto.getPrePageId())
				|| FunctionIdConstant.SKF3010_SC006.equals(initDto.getPrePageId())) {
			// 管理会社の選択値を設定
			initDto.setSelectedCompanyCd(initDto.getHdnSelectedCompanyCd());
			// 管理会社選択値判定
			if (Objects.equals(initDto.getHdnSelectedCompanyCd(), "ZZZZ")) {
				// デバッグログ
				LogUtils.debugByMsg("管理会社：外部機関");
				// 外部機関の場合
				// 管理機関コードを空文字に設定
				initDto.setAgencyCd(null);
				// 外部機関選択時は管理機関プルダウンを非活性
				initDto.setAgencyDispFlg(false);
			} else {
				// デバッグログ
				LogUtils.debugByMsg("管理会社：外部機関以外");
				// 外部機関以外の場合
				// 外部機関選択時は管理機関プルダウンを活性
				initDto.setAgencyDispFlg(true);
				// 管理機関の選択値を設定
				initDto.setAgencyCd(initDto.getHdnAgencyCd());
			}
			// 社宅区分選択値設定(検索キー)
			initDto.setShatakuKbnCd(initDto.getHdnShatakuKbnCd());
			// 利用区分選択値設定(検索キー)
			initDto.setUseKbnCd(initDto.getHdnUseKbnCd());
			// 空き部屋選択値設定(検索キー)
			initDto.setEmptyRoomCd(initDto.getHdnEmptyRoomCd());
			// 空き駐車場選択値設定(検索キー)
			initDto.setEmptyParkingCd(initDto.getHdnEmptyParkingCd());
			// 社宅名設定(検索キー)
			initDto.setShatakuName(initDto.getHdnSearchShatakuName());
			// 社宅住所設定(検索キー)
			initDto.setShatakuAddress(initDto.getHdnShatakuAddress());
		} else {
			// デバッグログ
			LogUtils.debugByMsg("セッション情報なし");
			// 画面連携のhidden項目を初期化
			initDto.setHdnAgencyCd(null);
			initDto.setHdnEmptyParkingCd(null);
			initDto.setHdnEmptyRoomCd(null);
			initDto.setHdnSelectedCompanyCd(null);
			initDto.setHdnShatakuAddress(null);
			initDto.setHdnShatakuKbnCd(null);
			initDto.setHdnSearchShatakuName(null);
			initDto.setHdnUseKbnCd(initDto.getUseKbnCd());

			//検索条件初期化
			initDto.setSelectedCompanyCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setAgencyCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setShatakuKbnCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setEmptyRoomCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setEmptyParkingCd(CodeConstant.DOUBLE_QUOTATION);
			initDto.setShatakuName(CodeConstant.DOUBLE_QUOTATION);
			initDto.setShatakuAddress(CodeConstant.DOUBLE_QUOTATION);
		}

		// ドロップダウンリストの値を設定
		skf3010Sc001SharedService.getDoropDownList(initDto.getSelectedCompanyCd(), manageCompanyList,
				initDto.getAgencyCd(), manageAgencyList, initDto.getShatakuKbnCd(), shatakuKbnList,
				initDto.getEmptyRoomCd(), emptyRoomList, initDto.getUseKbnCd(), useKbnList, initDto.getEmptyParkingCd(),
				emptyParkingList);

		// 社宅一覧取得
		// リストテーブルの情報を取得
		skf3010Sc001SharedService.getListTableData(initDto.getSelectedCompanyCd(),
				initDto.getAgencyCd(), initDto.getShatakuKbnCd(), initDto.getEmptyRoomCd(), initDto.getUseKbnCd(),
				initDto.getEmptyParkingCd(), initDto.getShatakuName(), initDto.getShatakuAddress(), listTableData);

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
		// 解放
		manageCompanyList = null;
		manageAgencyList = null;
		emptyRoomList = null;
		shatakuKbnList = null;
		useKbnList = null;
		emptyParkingList = null;
 
		return initDto;
	}

	/**
	 * 初期化処理
	 * 「*」項目はアドレスとして戻り値になる
	 * 
	 * @param comDto	*DTO
	 */
	private void initialize(Skf3010Sc001InitDto comDto) {

		comDto.setPageTitleKey(null);
		comDto.setListTableMaxRowCount(null);
		// リストテーブルデータ
		comDto.setListTableData(null);

		/** 画面連携用 */
		// 対象行の社宅区分
		comDto.setHdnRowShatakuKbn(null);
		// 対象行の社宅管理番号
		comDto.setHdnRowShatakuKanriNo(null);
		// 対象行の社宅名
		comDto.setHdnRowShatakuName(null);
		// 対象行の地域区分
		comDto.setHdnRowAreaKbn(null);
		// 対象行の空き部屋数
		comDto.setHdnRowEmptyRoomCount(null);
		// 対象行の空き駐車場数
		comDto.setHdnRowEmptyParkingCount(null);
		// 複写フラグ
		comDto.setCopyFlg(null);

		// 管理会社コード
		comDto.setSelectedCompanyCd(null);
		// 管理機関コード
		comDto.setAgencyCd(null);
		// 社宅区分コード
		comDto.setShatakuKbnCd(null);
		// 空き部屋コード
		comDto.setEmptyRoomCd(null);
		// 利用区分コード
		comDto.setUseKbnCd(null);
		// 空き駐車場コード
		comDto.setEmptyParkingCd(null);
		// 社宅名
		comDto.setShatakuName(null);
		// 社宅住所
		comDto.setShatakuAddress(null);
		// 外部機関表示フラグ
		comDto.setAgencyDispFlg(false);

		// 管理会社リスト
		comDto.setManageCompanyList(null);
		// 社宅区分リスト
		comDto.setShatakuKbnList(null);
		// 空き部屋リスト
		comDto.setEmptyRoomList(null);
		// 管理機関リスト
		comDto.setManageAgencyList(null);
		// 利用区分リスト
		comDto.setUseKbnList(null);
		// 空き駐車場リスト
		comDto.setEmptyParkingList(null);
	}
}
