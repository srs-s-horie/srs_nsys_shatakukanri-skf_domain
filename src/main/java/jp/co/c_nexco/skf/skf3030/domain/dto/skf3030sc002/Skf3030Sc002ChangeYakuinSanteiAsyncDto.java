/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002;

import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc002ChangeYakuinSanteiAsyncDto 入退居情報登録画面：役員算定変更非同期呼出処理Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc002ChangeYakuinSanteiAsyncDto extends Skf3030Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	//役員算定
	private String yakuinSanteiSelectedValue;
	//社宅賃貸料
	private String txtShatakuChintairyo;
	//区画1  区画番号
	private String litKukaku1No;
	//区画2  区画番号
	private String litKukaku2No;
	//駐車場料金
	private String txtChishajoRyokin;
	//チェック結果
	private String sc006ChintaiRyoErr;
	//チェック結果
	private String sc006TyusyajoRyokinErr;
}
