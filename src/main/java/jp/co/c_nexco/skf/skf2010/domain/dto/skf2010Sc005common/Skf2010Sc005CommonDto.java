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
