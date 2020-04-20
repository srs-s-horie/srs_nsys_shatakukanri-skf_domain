/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004SearchDto;

/**
 * Skf3020Sc004SearchService 転任者一覧画面のSearchサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004SearchService extends SkfServiceAbstract<Skf3020Sc004SearchDto> {

	@Autowired
	Skf3020Sc004SharedService skf3020Sc004SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3020.skf3020_sc004.max_row_count}")
	private String listTableMaxRowCount;	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param searchDto 検索DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004SearchDto index(Skf3020Sc004SearchDto searchDto) throws Exception {

		searchDto.setPageTitleKey(MessageIdConstant.SKF3020_SC004_TITLE);
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
		
		// リストデータ取得用
		//List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		// - 現社宅区分
		List<Map<String, Object>> genShatakuKubunList = new ArrayList<Map<String, Object>>();
		// - 入退居予定作成区分
		List<Map<String, Object>> yoteiSakuseiList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3020Sc004SharedService.getDoropDownList(searchDto.getGenShatakuKubun(), genShatakuKubunList,
				searchDto.getNyutaikyoYoteiSakuseiKubun(), yoteiSakuseiList);

		searchDto.setGenShatakuKubunList(genShatakuKubunList);
		searchDto.setYoteiSakuseiList(yoteiSakuseiList);
		
        // グリッドビューデータの転任者一覧取得
		SetGridViewData(searchDto);
		
		// ドロップダウン系
		searchDto.setGenShatakuKubunList(genShatakuKubunList);
		searchDto.setYoteiSakuseiList(yoteiSakuseiList);
		
		String[] setone = { "1" };
		// 社員番号設定(検索キー)
		searchDto.setHdnShainNo(searchDto.getShainNo());
		// 社員氏名設定（検索キー）
		searchDto.setHdnShainName(searchDto.getShainName());
		// 入居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(searchDto.getNyukyo()) != null) {
			searchDto.setHdnNyukyoChkFlg(true);
			searchDto.setHdnNyukyo(setone);
		} else {
			searchDto.setHdnNyukyoChkFlg(false);
			searchDto.setHdnNyukyo(null);
		}
		
		// 退居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(searchDto.getTaikyo()) != null) {
			searchDto.setHdnTaikyoChkFlg(true);
			searchDto.setHdnTaikyo(setone);
		} else {
			searchDto.setHdnTaikyoChkFlg(false);
			searchDto.setHdnTaikyo(null);
		}			
		// 入居状態変更設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(searchDto.getHenko()) != null) {
			searchDto.setHdnHenkoChkFlg(true);
			searchDto.setHdnHenko(setone);
		} else {
			searchDto.setHdnHenkoChkFlg(false);
			searchDto.setHdnHenko(null);
		}			
		// 現社宅区分設定（検索キー）
		searchDto.setHdnGenShataku(searchDto.getGenShatakuKubun());
		// 現所属設定（検索キー）
		searchDto.setHdnGenShozoku(searchDto.getGenShozoku());
		// 新所属設定（検索キー）
		searchDto.setHdnShinShozoku(searchDto.getShinShozoku());
		// 入退居予定作成区分（検索キー）
		searchDto.setHdnNyutaikyoYoteiSakuseiKubun(searchDto.getNyutaikyoYoteiSakuseiKubun());
		// 備考設定（検索キー）
		searchDto.setHdnBiko(searchDto.getBiko());
		
		return searchDto;
	}

	/**
	 * 検索部のセット
	 * 
	 * @param initDto
	 */
	private void SetGridViewData(Skf3020Sc004SearchDto searchDto){
		
        // 検索条件をもとに検索結果カウントの取得
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// チェックボックスの状態を取得
		// ― 入居
		String nyukyoStr = null;
		String[] nyukyoArr = searchDto.getNyukyo();
		if(nyukyoArr != null && nyukyoArr.length != 0){
			nyukyoStr = nyukyoArr[0];
		}
		// ― 退居
		String taikyoStr = null;
		String[] taikyoArr = searchDto.getTaikyo();
		if(taikyoArr !=  null && taikyoArr.length != 0){
			taikyoStr = taikyoArr[0];
		}
		// - 変更
		String henkoStr = null;
		String[] henkoArr = searchDto.getHenko();
		if(henkoArr != null && henkoArr.length != 0){
			henkoStr = henkoArr[0];
		}
		int listCount = skf3020Sc004SharedService.getListTableData(searchDto.getShainNo(),
				searchDto.getShainName(),
				nyukyoStr,
				taikyoStr,
				henkoStr,
				searchDto.getGenShatakuKubun(),
				searchDto.getGenShozoku(),
				searchDto.getShinShozoku(),
				searchDto.getNyutaikyoYoteiSakuseiKubun(),
				searchDto.getBiko(),
				listTableData);
		
		if(listCount == 0){
			// 取得レコード0件のワーニング
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
		}else{
			// 最大行数を設定
			searchDto.setListTableMaxRowCount(listTableMaxRowCount);
		}
		searchDto.setListTableData(listTableData);
	}
	
}
