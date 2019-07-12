/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc005common.Skf3090Sc005CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc005ResistDto 従業員マスタ登録-登録/更新用Dto
 * 
 * @author NEXCOシステムズ
 *
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc005RegistDto extends Skf3090Sc005CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** ドロップダウンリスト系 */
	// 会社ドロップダウンリスト
	List<Map<String, Object>> companyList;
	// 機関ドロップダウンリスト
	List<Map<String, Object>> agencyList;
	// 部等ドロップダウンリスト
	List<Map<String, Object>> affiliation1List;
	// 室、チーム又は課ドロップダウンリスト
	List<Map<String, Object>> affiliation2List;
	// 事業領域ドロップダウンリスト
	List<Map<String, Object>> businessAreaList;

	/** 処理に必要なメンバ */
	// 更新フラグ
	private String updateFlag;

}
