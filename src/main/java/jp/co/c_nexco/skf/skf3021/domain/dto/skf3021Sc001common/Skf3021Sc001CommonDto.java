/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.dto.skf3021Sc001common;

import java.util.*;

import jp.co.c_nexco.skf.skf3021.domain.dto.common.Skf302110CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3021Sc001CommonDto 入退居予定一覧画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3021Sc001CommonDto extends Skf302110CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 検索条件
	 */
	//社員番号
	private String shainNo;
	//社員名
	private String shainName;
	//入退居区分
	private String nyutaikyoKbn;
	//入退居区分リスト
	private List<Map<String, Object>> nyutaikyoKbnList;
	//提示対象
	private String teijiDetaSakuseiKbn;
	//提示対象リスト
	private List<Map<String, Object>> teijiDetaSakuseiKbnList;
	//入退居申請状況
	private String nyuTaikyoShinseiJokyo;
	//入退居申請状況リスト
	private List<Map<String, Object>> nyuTaikyoShinseiJokyoList;
	//入退居申請督促
	private String nyuTaikyoShinseiTokusoku;
	//入退居申請督促リスト
	private List<Map<String, Object>> nyuTaikyoShinseiTokusokuList;
	//特殊事情
	private String tokushuJijo;
	//特殊事情リスト
	private List<Map<String, Object>> tokushuJijoList;
	
	//検索結果一覧用
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	
	//システム連携区分
	private String jssLinkFlg;
	
	//メール送信リスト文字列
	private String mailListData;
	//提示データ作成リスト文字列
	private String teijiListData;
	
	//削除時パラメータ
	//削除社員番号
	private String delShainNo;
	//削除入退居区分
	private String delNyuTaikyoKbn;
	//削除申請番号
	private String delApplNo;
	//入退居予定更新日時（hidden変数）
	private String hdnNyutaikyoYoteiUpdateDate;
	//転任者調書更新日時（hidden変数）
	private String hdnTenninshaChoshoUpdateDate;
}
