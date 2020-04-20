/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc005;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005.Skf3022Sc005InitDto;

/**
 * Skf3022Sc005InitService　提示データ一覧画面のInitサービス処理クラス。　 
 * 
 *  @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc005InitService extends SkfServiceAbstract<Skf3022Sc005InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	@Autowired
	private Skf3022Sc005SharedService skf3022Sc005SharedService;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3022.skf3022_sc005.max_row_count}")
	private String listTableMaxRowCount;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3022Sc005InitDto index(Skf3022Sc005InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC005_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		// 初期化
		initialize(initDto);

		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

//        '給与厚生申請システムとの連携フラグ（0：連携あり、1：連携なし）⇒現行ソースで未使用
//        Me.hdnJssLinkFlg.Value = Com_SettingManager.GetSettingInfo(Constant.SettingsId.JSS_LINK_FLG)
		
		//入退居区分リスト
		String nyutaikyoKbn = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//社宅提示状況リスト
		String stJyokyo = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> stJyokyoList = new ArrayList<Map<String, Object>>();
		//社宅提示確認督促リスト
		String stKakunin = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> stKakuninList = new ArrayList<Map<String, Object>>();
		//備品提示状況リスト
		String bhJyokyo = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> bhJyokyoList = new ArrayList<Map<String, Object>>();
		//備品提示確認督促リスト
		String bhKakunin = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> bhKakuninList = new ArrayList<Map<String, Object>>();
		//備品搬入搬出督促リスト
		String moveInOut = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> moveInOutList = new ArrayList<Map<String, Object>>();

		Skf3022Sc005GetTeijiDataInfoExpParameter param = new Skf3022Sc005GetTeijiDataInfoExpParameter();
		
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& (Objects.equals(initDto.getPrePageId(), FunctionIdConstant.SKF3022_SC005)
						|| Objects.equals(initDto.getPrePageId(), FunctionIdConstant.SKF3022_SC006))) {
//	        '登録画面から、検索条件の取得
			//パラメータ設定
			param.setShainNo(initDto.getSearchInfoShainNo());
			param.setShainName(initDto.getSearchInfoShainName());
			param.setShatakuName(initDto.getSearchInfoShatakuName());
			param.setNyutaikyoKbn(initDto.getSearchInfoNyutaikyoKbn());
			nyutaikyoKbn = initDto.getSearchInfoNyutaikyoKbn();
			param.setStJyokyo(initDto.getSearchInfoStJyokyo());
			stJyokyo = initDto.getSearchInfoStJyokyo();
			param.setStKakunin(initDto.getSearchInfoStKakunin());
			stKakunin = initDto.getSearchInfoStKakunin();
			param.setBhJyokyo(initDto.getSearchInfoBhJyokyo());
			bhJyokyo = initDto.getSearchInfoBhJyokyo();
			param.setBhKakunin(initDto.getSearchInfoBhKakunin());
			bhKakunin = initDto.getSearchInfoBhKakunin();
			param.setMoveInOut(initDto.getSearchInfoMoveInout());
			moveInOut = initDto.getSearchInfoMoveInout();
			//提示データ一覧を取得
			skf3022Sc005SharedService.getListTableData(param, listTableData);
		}else{
//          'メニューから
//          '提示データ一覧を取得
			skf3022Sc005SharedService.getListTableData(param, listTableData);
		}

		initDto.setListTableMaxRowCount(listTableMaxRowCount);
		
//        '督促ボタンは使用不可に設定
		initDto.setBtnShatakuTeijiDisabled("true");
		initDto.setBtnBihinTeijiDisabled("true");
		initDto.setBtnBihinInOutDisabled("true");
//      'コントロールの設定
		skf3022Sc005SharedService.getDropDownList(nyutaikyoKbn, nyutaikyoKbnList, stJyokyo, stJyokyoList, stKakunin, stKakuninList, 
				bhJyokyo, bhJyokyoList, bhKakunin, bhKakuninList, moveInOut, moveInOutList);

		// 社員番号
		initDto.setShainNo(param.getShainNo());
		// 社員名
		initDto.setShainName(param.getShainName());
		// 社宅名
		initDto.setShatakuName(param.getShatakuName());
		initDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		initDto.setStJyokyoList(stJyokyoList);
		initDto.setStKakuninList(stKakuninList);
		initDto.setBhJyokyoList(bhJyokyoList);
		initDto.setBhKakuninList(bhKakuninList);
		initDto.setMoveInOutList(moveInOutList);
		initDto.setListTableData(listTableData);
 		
		return initDto;
	}

	/**
	 * 初期化処理
	 * 「*」項目はアドレスとして戻り値になる
	 * 
	 * @param comDto	*DTO
	 */
	private void initialize(Skf3022Sc005InitDto comDto) {

		//提示番号
		comDto.setHdnTeijiNo(null);
		//入居予定日
		comDto.setHdnNyukyoDate(null);
		//退居予定日
		comDto.setHdnTaikyoDate(null);
		//申請書類管理番号
		comDto.setHdnShoruikanriNo(null);
		//入退居区分
		comDto.setHdnNyutaikyoKbn(null);
		//申請区分
		comDto.setHdnApplKbn(null);
		//社員番号変更フラグ
		comDto.setHdnShainNoChangeFlg(null);
		//検索条件
		// 入退居区分
		comDto.setNyutaikyoKbn(null);
		// 入退居区分リスト
		comDto.setNyutaikyoKbnList(null);
		// 社宅提示
		// 社宅提示状況
		comDto.setStJyokyo(null);
		// 社宅提示状況リスト
		comDto.setStJyokyoList(null);
		// 社宅提示確認督促
		comDto.setStKakunin(null);
		// 社宅提示確認督促リスト
		comDto.setStKakuninList(null);
		// 備品提示
		// 備品提示状況
		comDto.setBhJyokyo(null);
		// 備品提示状況リスト
		comDto.setBhJyokyoList(null);
		// 備品提示確認督促
		comDto.setBhKakunin(null);
		// 備品提示確認督促リスト
		comDto.setBhKakuninList(null);
		// 備品搬入搬出督促
		comDto.setMoveInOut(null);
		// 備品搬入搬出督促リスト
		comDto.setMoveInOutList(null);

		//検索結果一覧用
		comDto.setListTableData(null);
		comDto.setListTableMaxRowCount(null);

		//ボタン制御
		comDto.setBtnShatakuTeijiDisabled(null);
		comDto.setBtnBihinTeijiDisabled(null);
		comDto.setBtnBihinInOutDisabled(null);
		//督促カウント
		comDto.setHdnStTeijiCnt(null);
		comDto.setHdnBhTeijiCnt(null);
		comDto.setHdnMoveInOutCnt(null);
	}
}
