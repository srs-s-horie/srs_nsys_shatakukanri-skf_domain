/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3060.domain.dto.skf3060Sc001common;

import java.util.*;

import jp.co.c_nexco.skf.skf3060.domain.dto.common.Skf306010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3060Sc001CommonDto 事業領域名マスタ登録画面CommonDto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3060Sc001CommonDto extends Skf306010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;

	/**
	 * 操作ガイド用
	 */
	// 操作説明
	private String operationGuide;
	
	/**
	 * listTable用
	 */
	// - 検索結果一覧
	private List<Map<String, Object>> listTableData;
	private String listTableMaxRowCount;
	private String listTableSearchMaxCount;
	// チェックされたメール送信ェックボックス値の配列
	private String[] sendMailChkVal;
	
	/**
	 * 検索フォーム用
	 */	
	// 基準期間（From To）
	private String baseTermFrom;
	private String baseTermTo;
	private String baseTermFromErr;
	private String baseTermToErr;
	// 原籍会社(選択値)	
	private String selectedOriginalCompanyCd;
	// 社員番号
	private String shainNo;
	// 給与支給会社名（選択値）
	private String selectedSalaryCompanyCd;
	// 社員名
	private String name;
	// 送信状態（選択値）
	private String selectedSendMailStatus;
	// リスト系
	// - 原籍会社リスト
	List<Map<String, Object>> originalCompanyCdList;
	// - 給与支給会社名リスト
	List<Map<String, Object>> salaryCompanyCdList;
	// - 送信状態リスト
	List<Map<String, Object>> sendMailStatusList;
	// - 年齢加算社宅使用加算対象月
	private String hdnKasanStartDate;
	
	/**
	 *  ボタンの活性制御
	　*/	
	// CSV出力ボタン
	private boolean csvOutButtonDisabled;
	// メール送信ボタン
	private boolean sendMailButtonDisabled;
	
	/**
	 *  hidden項目(いる？)
	　*/	
	// 基準期間（From To）
	private String hdnBaseTermFrom;
	private String hdnBaseTermTo;
	// 社員番号
	private String hdnShainNo;
	// 社員名
	private String hdnName;
	// 原籍会社
	private String hdnOriginalCompanyCd;
	// 給与支給会社名
	private String hdnSalaryCompanyCd;
	// 送信状態
	private String hdnSendMailStatus;

	// メール送信用和暦
	private String hdnWareki;
	// メール送信対象データ
	private String mailSendData;
	
}
