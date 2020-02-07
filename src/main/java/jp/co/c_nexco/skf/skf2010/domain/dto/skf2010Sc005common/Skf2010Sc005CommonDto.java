/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc005common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201030CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc005 承認一覧の共通Dtoクラス
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc005CommonDto extends Skf201030CommonDto {

	private static final long serialVersionUID = 1L;

	// 画面の検索条件
	// 申請日From
	private String applDateFrom;
	// 申請日To
	private String applDateTo;
	// 承認日／修正依頼日From
	private String agreDateFrom;
	// 承認日／修正依頼日To
	private String agreDateTo;
	// 所属機関
	private String shozokuKikan;
	// 機関
	private String agency;
	// 部等
	private String affiliation1;
	// 室、チーム又は課
	private String affiliation2;
	// 社員番号
	private String shainNo;
	// 申請者名
	private String name;
	// 申請書類種別
	private String applCtgry;
	// 申請書類名
	private String applName;
	// 承認者名
	private String agreementName;
	// 申請状況
	private String[] applStatus;

	// ドロップダウン
	// ドロップダウン：機関リスト
	private List<Map<String, Object>> ddlAgencyList;
	// ドロップダウン：部等リスト
	private List<Map<String, Object>> ddlAffiliation1List;
	// ドロップダウン：室、チーム又は課リスト
	private List<Map<String, Object>> ddlAffiliation2List;
	// ドロップダウン：申請書類リスト
	// private List<Map<String, Object>> ddlApplCtgry;

	// リストテーブル
	// 検索結果一覧
	private List<Map<String, Object>> ltResultList;

	// 操作ガイド
	private String operationGuide;

	// 一括承認用変数
	private String submitApplNo;
	// エラー項目
	private String applDateFromErr;
	private String applDateToErr;
	private String agreDateFromErr;
	private String agreDateToErr;
	private String applStatusErr;

	private String backUrl;

}
