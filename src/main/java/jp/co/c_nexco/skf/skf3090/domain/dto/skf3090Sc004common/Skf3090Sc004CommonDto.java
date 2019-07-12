/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc004common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309030CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc004CommonDto 従業員マスタ一覧同期処理共通Dto.
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc004CommonDto extends Skf309030CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;

	/** 従業員マスタ登録画面hidden項目連携用 */
	// 社員番号
	private String hdnShainNo;
	// 氏名
	private String hdnName;
	// 氏名カナ
	private String hdnNameKk;
	// 原籍会社コード
	private String hdnOriginalCompanyCd;
	// 機関コード
	private String hdnAgencyCd;
	// 部等コード
	private String hdnAffiliation1Cd;
	// 室、チーム又は課コード
	private String hdnAffiliation2Cd;
	// 更新フラグ
	private String updateFlag;

}
