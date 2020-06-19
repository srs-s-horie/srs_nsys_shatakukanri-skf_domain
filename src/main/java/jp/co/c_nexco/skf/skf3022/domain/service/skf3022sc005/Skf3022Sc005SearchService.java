/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc005;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005.Skf3022Sc005SearchDto;

/**
 * Skf3022Sc005SearchService 提示データ一覧検索処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc005SearchService extends SkfServiceAbstract<Skf3022Sc005SearchDto> {

	@Autowired
	private Skf3022Sc005SharedService skf3022Sc005SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3022.skf3022_sc005.max_row_count}")
	private String listTableMaxRowCount;

	/**
	 * サービス処理を行う。
	 * 
	 * @param searchDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3022Sc005SearchDto index(Skf3022Sc005SearchDto searchDto) throws Exception {

		searchDto.setPageTitleKey(MessageIdConstant.SKF3022_SC005_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, FunctionIdConstant.SKF3022_SC005);
		
		//入退居区分リスト
		String nyutaikyoKbn = searchDto.getNyutaikyoKbn();
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//社宅提示状況リスト
		String stJyokyo = searchDto.getStJyokyo();
		List<Map<String, Object>> stJyokyoList = new ArrayList<Map<String, Object>>();
		//社宅提示確認督促リスト
		String stKakunin = searchDto.getStKakunin();
		List<Map<String, Object>> stKakuninList = new ArrayList<Map<String, Object>>();
		//備品提示状況リスト
		String bhJyokyo = searchDto.getBhJyokyo();
		List<Map<String, Object>> bhJyokyoList = new ArrayList<Map<String, Object>>();
		//備品提示確認督促リスト
		String bhKakunin = searchDto.getBhKakunin();
		List<Map<String, Object>> bhKakuninList = new ArrayList<Map<String, Object>>();
		//備品搬入搬出督促リスト
		String moveInOut = searchDto.getMoveInOut();
		List<Map<String, Object>> moveInOutList = new ArrayList<Map<String, Object>>();
//      'コントロールの設定
		skf3022Sc005SharedService.getDropDownList(nyutaikyoKbn, nyutaikyoKbnList, stJyokyo, stJyokyoList, stKakunin, stKakuninList, 
				bhJyokyo, bhJyokyoList, bhKakunin, bhKakuninList, moveInOut, moveInOutList);
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		Skf3022Sc005GetTeijiDataInfoExpParameter param = new Skf3022Sc005GetTeijiDataInfoExpParameter();
		//検索条件の設定
		param.setShainNo(skf3022Sc005SharedService.escapeParameter(searchDto.getShainNo()));
		param.setShainName(skf3022Sc005SharedService.escapeParameter(searchDto.getShainName()));
		param.setShatakuName(skf3022Sc005SharedService.escapeParameter(searchDto.getShatakuName()));
		param.setNyutaikyoKbn(searchDto.getNyutaikyoKbn());
		param.setStJyokyo(searchDto.getStJyokyo());
		param.setStKakunin(searchDto.getStKakunin());
		param.setBhJyokyo(searchDto.getBhJyokyo());
		param.setBhKakunin(searchDto.getBhKakunin());
		param.setMoveInOut(searchDto.getMoveInOut());
		
		// リストテーブルの情報を取得
		int listCount = skf3022Sc005SharedService.getListTableData(param, listTableData);
		
		// エラーメッセージ設定
		if (listCount == 0) {
			// 取得レコード0件のワーニング
			ServiceHelper.addErrorResultMessage(searchDto, null, MessageIdConstant.W_SKF_1007);
		}

		//検索条件退避用
		searchDto.setSearchInfoShainNo(param.getShainNo());
		searchDto.setSearchInfoShainName(param.getShainName());
		searchDto.setSearchInfoShatakuName(param.getShatakuName());
		searchDto.setSearchInfoNyutaikyoKbn(param.getNyutaikyoKbn());
		searchDto.setSearchInfoStJyokyo(param.getStJyokyo());
		searchDto.setSearchInfoStKakunin(param.getStKakunin());
		searchDto.setSearchInfoBhJyokyo(param.getBhJyokyo());
		searchDto.setSearchInfoBhKakunin(param.getBhKakunin());
		searchDto.setSearchInfoMoveInout(param.getMoveInOut());
		
		//督促ボタンは使用不可に設定
		searchDto.setBtnShatakuTeijiDisabled("true");
		searchDto.setBtnBihinTeijiDisabled("true");
		searchDto.setBtnBihinInOutDisabled("true");
		
		searchDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		searchDto.setStJyokyoList(stJyokyoList);
		searchDto.setStKakuninList(stKakuninList);
		searchDto.setBhJyokyoList(bhJyokyoList);
		searchDto.setBhKakuninList(bhKakuninList);
		searchDto.setMoveInOutList(moveInOutList);
		searchDto.setListTableData(listTableData);

		return searchDto;
	}
}
