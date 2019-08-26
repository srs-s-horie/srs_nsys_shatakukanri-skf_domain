/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc007;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc007common.Skf2010Sc007CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010Sc007TransferDto 申請条件確認画面の「申請書を作成するボタン」押下時Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc007TransferDto extends Skf2010Sc007CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請書NO
	private String applNo;
	// 入退居区分
	private String nyutaikyoKbn;

	// 代行ログインフラグ
	private String alterLoginFlg;

	// 提示データ

	// 申請書情報リスト
	private List<Map<String, Object>> applList;
	// 申請書履歴リスト
	private List<Map<String, Object>> applHistoryList;

}
