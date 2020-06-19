/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001InitDto;

/**
 * Skf3021Sc001InitService 入退居予定一覧画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3021Sc001InitService extends SkfServiceAbstract<Skf3021Sc001InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3021Sc001SharedService skf3021Sc001SharedService;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3021.skf3021_sc001.max_row_count}")
	private String listTableMaxRowCount;
	
	// システム連携フラグ(0固定でよいと思うが一応フラグのまま)
	@Value("${skf.common.jss_link_flg}")
	private String jssLinkFlg;
	
	//ボタン押下区分：初期化
	private static final int BTNFLG_INIT = 0;
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
	public Skf3021Sc001InitDto index(Skf3021Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3021_SC001);
		
		//システム連携フラグ
		initDto.setJssLinkFlg(jssLinkFlg);

		//入退居区分リスト
		String nyutaikyoKbn = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請状況リスト
		String nyuTaikyoShinseiJokyo = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> nyuTaikyoShinseiJokyoList = new ArrayList<Map<String, Object>>();
		//特殊事情リスト
		String tokushuJijo = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> tokushuJijoList = new ArrayList<Map<String, Object>>();
		//提示データ作成区分リスト(提示対象)
		String teijiDetaSakuseiKbn = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> teijiDetaSakuseiKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請督促リスト
		String nyuTaikyoShinseiTokusoku = CodeConstant.DOUBLE_QUOTATION;
		List<Map<String, Object>> nyuTaikyoShinseiTokusokuList = new ArrayList<Map<String, Object>>();
		
		//getDropDownList
		skf3021Sc001SharedService.getDropDownList(nyutaikyoKbn, nyutaikyoKbnList,
				 teijiDetaSakuseiKbn, teijiDetaSakuseiKbnList,
				 nyuTaikyoShinseiJokyo , nyuTaikyoShinseiJokyoList,
				 nyuTaikyoShinseiTokusoku , nyuTaikyoShinseiTokusokuList,
				 tokushuJijo , tokushuJijoList);
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		//入退居予定を取得（全件）
		skf3021Sc001SharedService.setGrvNyuTaikyoYoteiIchiran(BTNFLG_INIT, initDto, listTableData);

//        '入退居申請督促ボタンの制御
//        If Constant.JssLinkFlg.RENKEI_ARI.Equals(jssLinkFlg) Then
//            '「0：連携あり」の場合、督促メールの送信ボタンを活性
//            Me.btnNyuTaikyoShinseiTokusoku.Enabled = True
//        ElseIf Constant.JssLinkFlg.RENKEI_NASHI.Equals(jssLinkFlg) Then
//            '「1：連携なし」の場合、督促メールの送信ボタンを非活性
//            Me.btnNyuTaikyoShinseiTokusoku.Enabled = False
//        End If
		
		//一覧情報
		initDto.setListTableMaxRowCount(listTableMaxRowCount);
		initDto.setListTableData(listTableData);
		//リスト情報
		initDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		initDto.setTeijiDetaSakuseiKbnList(teijiDetaSakuseiKbnList);
		initDto.setNyuTaikyoShinseiJokyoList(nyuTaikyoShinseiJokyoList);
		initDto.setNyuTaikyoShinseiTokusokuList(nyuTaikyoShinseiTokusokuList);
		initDto.setTokushuJijoList(tokushuJijoList);
		
		return initDto;
	}
	
}
