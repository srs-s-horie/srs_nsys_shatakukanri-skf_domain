/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001SearchDto;

/**
 * 入退居予定一覧画面のSearchサービス処理クラス。　 
 * 
 */
@Service
public class Skf3021Sc001SearchService extends BaseServiceAbstract<Skf3021Sc001SearchDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3021Sc001SharedService skf3021Sc001SharedService;
	
	//ボタン押下区分：検索ボタン押下
	private static final int BTNFLG_KENSAKU = 1;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param searchDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3021Sc001SearchDto index(Skf3021Sc001SearchDto searchDto) throws Exception {
		
		searchDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());
		
		//入退居区分リスト
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請状況リスト
		List<Map<String, Object>> nyuTaikyoShinseiJokyoList = new ArrayList<Map<String, Object>>();
		//特殊事情リスト
		List<Map<String, Object>> tokushuJijoList = new ArrayList<Map<String, Object>>();
		//提示データ作成区分リスト(提示対象)
		List<Map<String, Object>> teijiDetaSakuseiKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請督促リスト
		List<Map<String, Object>> nyuTaikyoShinseiTokusokuList = new ArrayList<Map<String, Object>>();
		
		//getDropDownList
		skf3021Sc001SharedService.getDropDownList(searchDto.getNyutaikyoKbn(), nyutaikyoKbnList,
				searchDto.getTeijiDetaSakuseiKbn(), teijiDetaSakuseiKbnList,
				searchDto.getNyuTaikyoShinseiJokyo() , nyuTaikyoShinseiJokyoList,
				searchDto.getNyuTaikyoShinseiTokusoku() , nyuTaikyoShinseiTokusokuList,
				searchDto.getTokushuJijo() , tokushuJijoList);
		//リスト情報
		searchDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		searchDto.setTeijiDetaSakuseiKbnList(teijiDetaSakuseiKbnList);
		searchDto.setNyuTaikyoShinseiJokyoList(nyuTaikyoShinseiJokyoList);
		searchDto.setNyuTaikyoShinseiTokusokuList(nyuTaikyoShinseiTokusokuList);
		searchDto.setTokushuJijoList(tokushuJijoList);
		
		//社員番号の判定
		if(!SkfCheckUtils.isNullOrEmpty(searchDto.getShainNo())){
			if(!validateShainNo(searchDto.getShainNo())){
				ServiceHelper.addErrorResultMessage(searchDto,  new String[] { "shainNo" }, MessageIdConstant.E_SKF_1052, "社員番号");
				return searchDto;
			}
		}
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		//入退居予定を取得（全件）
		skf3021Sc001SharedService.setGrvNyuTaikyoYoteiIchiran(BTNFLG_KENSAKU, searchDto, listTableData);

		
		//一覧情報
		searchDto.setListTableData(listTableData);

		
		return searchDto;
	}
	
	/**
	 * 社員番号形式チェック
	 * @param shainNo
	 * @return
	 */
	private Boolean validateShainNo(String shainNo){
		//半角英数チェック
		if (!CheckUtils.isAlphabetNumeric(shainNo)) {
			return false;
		}
		return true;
	}
}
