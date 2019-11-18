/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc002common;

import java.util.*;

import jp.co.c_nexco.skf.skf3090.domain.dto.common.Skf309020CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090_Sc002画面のCommonDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc002CommonDto extends Skf309020CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	// 前画面からの引継ぎパラメータ
	// - 改定日
	private String hdnEffectiveDate;
	// - 状態区分
	private String hdnJyotaiKbn;
	// - (活性・非活性）
	private boolean effectiveDateDisabled;
	
	// 改定日
	private String effectiveDate;
	private String effectiveDateErr;
	
	// 01 北海道
	private String lblTodoufukenn01;
	private String txtTodoufukenn01;
	private String txtTodoufukenn01Err;
	// 02 青森県
	private String lblTodoufukenn02;
	private String txtTodoufukenn02;
	private String txtTodoufukenn02Err;
	// 03 岩手県
	private String lblTodoufukenn03;
	private String txtTodoufukenn03;
	private String txtTodoufukenn03Err;
	// 04 宮城県
	private String lblTodoufukenn04;
	private String txtTodoufukenn04;
	private String txtTodoufukenn04Err;
	// 05 秋田県
	private String lblTodoufukenn05;
	private String txtTodoufukenn05;
	private String txtTodoufukenn05Err;
	// 06 山形県
	private String lblTodoufukenn06;
	private String txtTodoufukenn06;
	private String txtTodoufukenn06Err;
	// 07 福島県
	private String lblTodoufukenn07;
	private String txtTodoufukenn07;
	private String txtTodoufukenn07Err;
	// 08 茨城県
	private String lblTodoufukenn08;
	private String txtTodoufukenn08;
	private String txtTodoufukenn08Err;
	// 09 栃木県
	private String lblTodoufukenn09;
	private String txtTodoufukenn09;
	private String txtTodoufukenn09Err;
	// 10 群馬県
	private String lblTodoufukenn10;
	private String txtTodoufukenn10;
	private String txtTodoufukenn10Err;
	// 11 埼玉県
	private String lblTodoufukenn11;
	private String txtTodoufukenn11;
	private String txtTodoufukenn11Err;
	// 12 千葉県
	private String lblTodoufukenn12;
	private String txtTodoufukenn12;
	private String txtTodoufukenn12Err;
	// 13 東京都
	private String lblTodoufukenn13;
	private String txtTodoufukenn13;
	private String txtTodoufukenn13Err;
	// 14 神奈川県
	private String lblTodoufukenn14;
	private String txtTodoufukenn14;
	private String txtTodoufukenn14Err;
	// 15 新潟県
	private String lblTodoufukenn15;
	private String txtTodoufukenn15;
	private String txtTodoufukenn15Err;
	// 16 富山県
	private String lblTodoufukenn16;
	private String txtTodoufukenn16;
	private String txtTodoufukenn16Err;
	// 17 石川県
	private String lblTodoufukenn17;
	private String txtTodoufukenn17;
	private String txtTodoufukenn17Err;
	// 18 福井県
	private String lblTodoufukenn18;
	private String txtTodoufukenn18;
	private String txtTodoufukenn18Err;
	// 19 山梨県
	private String lblTodoufukenn19;
	private String txtTodoufukenn19;
	private String txtTodoufukenn19Err;
	// 20 長野県
	private String lblTodoufukenn20;
	private String txtTodoufukenn20;
	private String txtTodoufukenn20Err;
	// 21 岐阜県
	private String lblTodoufukenn21;
	private String txtTodoufukenn21;
	private String txtTodoufukenn21Err;
	// 22 静岡県
	private String lblTodoufukenn22;
	private String txtTodoufukenn22;
	private String txtTodoufukenn22Err;
	// 23 愛知県
	private String lblTodoufukenn23;
	private String txtTodoufukenn23;
	private String txtTodoufukenn23Err;
	// 24 三重県
	private String lblTodoufukenn24;
	private String txtTodoufukenn24;
	private String txtTodoufukenn24Err;
	// 25 滋賀県
	private String lblTodoufukenn25;
	private String txtTodoufukenn25;
	private String txtTodoufukenn25Err;
	// 26 京都府
	private String lblTodoufukenn26;
	private String txtTodoufukenn26;
	private String txtTodoufukenn26Err;
	// 27 大阪府
	private String lblTodoufukenn27;
	private String txtTodoufukenn27;
	private String txtTodoufukenn27Err;
	// 28 兵庫県
	private String lblTodoufukenn28;
	private String txtTodoufukenn28;
	private String txtTodoufukenn28Err;
	// 29 奈良県
	private String lblTodoufukenn29;
	private String txtTodoufukenn29;
	private String txtTodoufukenn29Err;
	// 30 和歌山県
	private String lblTodoufukenn30;
	private String txtTodoufukenn30;
	private String txtTodoufukenn30Err;
	// 31 鳥取県
	private String lblTodoufukenn31;
	private String txtTodoufukenn31;
	private String txtTodoufukenn31Err;
	// 32 島根県
	private String lblTodoufukenn32;
	private String txtTodoufukenn32;
	private String txtTodoufukenn32Err;
	// 33 岡山県
	private String lblTodoufukenn33;
	private String txtTodoufukenn33;
	private String txtTodoufukenn33Err;
	// 34 広島県
	private String lblTodoufukenn34;
	private String txtTodoufukenn34;
	private String txtTodoufukenn34Err;
	// 35 山口県
	private String lblTodoufukenn35;
	private String txtTodoufukenn35;
	private String txtTodoufukenn35Err;
	// 36 徳島県
	private String lblTodoufukenn36;
	private String txtTodoufukenn36;
	private String txtTodoufukenn36Err;
	// 37 香川県
	private String lblTodoufukenn37;
	private String txtTodoufukenn37;
	private String txtTodoufukenn37Err;
	// 38 愛媛県
	private String lblTodoufukenn38;
	private String txtTodoufukenn38;
	private String txtTodoufukenn38Err;
	// 39 高知県
	private String lblTodoufukenn39;
	private String txtTodoufukenn39;
	private String txtTodoufukenn39Err;
	// 40 福岡圏
	private String lblTodoufukenn40;
	private String txtTodoufukenn40;
	private String txtTodoufukenn40Err;
	// 41 佐賀県
	private String lblTodoufukenn41;
	private String txtTodoufukenn41;
	private String txtTodoufukenn41Err;
	// 42 長崎県
	private String lblTodoufukenn42;
	private String txtTodoufukenn42;
	private String txtTodoufukenn42Err;
	// 43 熊本県
	private String lblTodoufukenn43;
	private String txtTodoufukenn43;
	private String txtTodoufukenn43Err;
	// 44 大分県
	private String lblTodoufukenn44;
	private String txtTodoufukenn44;
	private String txtTodoufukenn44Err;
	// 45 宮崎県
	private String lblTodoufukenn45;
	private String txtTodoufukenn45;
	private String txtTodoufukenn45Err;
	// 46 鹿児島県
	private String lblTodoufukenn46;
	private String txtTodoufukenn46;
	private String txtTodoufukenn46Err;
	// 47 沖縄県
	private String lblTodoufukenn47;
	private String txtTodoufukenn47;
	private String txtTodoufukenn47Err;
	// 備考
	private String biko;
	private String bikoErr;
	
	// 退避用（複写した時に反映する情報を保持）
	// - 金額
	private List<String> taihiMoney;
	// - 備考
	private String taihiBiko;
	
	// ボタン制御(活性・非活性）
	// - 登録ボタン
	private boolean registButtonDisabled;
	// - 複写ボタン
	private boolean copyButtonDisabled;
	// - 削除ボタン
	private boolean deleteButtonDisabled;
	
}
