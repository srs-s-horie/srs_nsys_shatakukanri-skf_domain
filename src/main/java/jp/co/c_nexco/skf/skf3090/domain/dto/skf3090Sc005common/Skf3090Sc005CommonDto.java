/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc005common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309030CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc005CommonDto 従業員マスタ登録同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc005CommonDto extends Skf309030CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** エラー系 **/
	// 社員番号
	private String shainNoError;
	// 氏名
	private String nameError;
	// 氏名カナ
	private String nameKkError;
	// メールアドレス
	private String mailAddressError;
	// 退職日
	private String retireDateError;
	// 原籍会社コード（ドロップダウン選択値）
	private String originalCompanyCdError;

	/** 画面表示系 */
	// メールアドレス
	private String mailAddress;
	// 退職日
	private String retireDate;
	// 原籍会社コード（ドロップダウン選択値）
	private String originalCompanyCd;
	// 事業領域コード（ドロップダウン選択値）
	private String businessAreaCd;

	// 事業領域ドロップダウンリスト
	List<Map<String, Object>> businessAreaList;

}
