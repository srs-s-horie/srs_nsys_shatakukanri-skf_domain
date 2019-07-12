/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc004;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc004common.Skf3090Sc004CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc004ChangeDropDownAsyncDto 従業員マスタ一覧ドロップダウン変更時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc004ChangeDropDownAsyncDto extends Skf3090Sc004CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 社員番号
	private String shainNo;
	// 社員名
	private String name;
	// 社員名（フリガナ）
	private String nameKk;
	// 会社コード
	private String companyCd;
	private String selectedCompanyCd;
	// 機関コード
	private String agencyCd;
	private String agencyName;
	// 部等コード
	private String affiliation1Cd;
	// 室、チームまたは課コード
	private String affiliation2Cd;

	// 会社リスト
	private List<Map<String, Object>> companyList; // IM標準版
	private String companyCdListJson; // 共通FW版

	// 機関リスト
	private List<Map<String, Object>> agencyList; // IM標準版
	private String agencyCdListJson; // 共通FW版

	// 部等リスト
	private List<Map<String, Object>> affiliation1List; // IM標準版
	private String affiliation1CdListJson; // 共通FW版

	// 室、チーム又は課リスト
	private List<Map<String, Object>> affiliation2List; // IM標準版
	private String affiliation2CdListJson; // 共通FW版

	private String updateFlag;

	private String searchFlag;

	// TODO 共通FWが改修してくれなかった場合はこれで実装
	private String localPrePageId;

}
