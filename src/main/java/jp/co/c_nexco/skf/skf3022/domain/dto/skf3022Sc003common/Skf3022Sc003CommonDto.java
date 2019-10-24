/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc003common;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


import jp.co.c_nexco.nfw.webcore.domain.model.GridRelationDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc003CommonDto 使用料入力支援共通DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc003CommonDto extends GridRelationDto {

	private static final long serialVersionUID = -1902278406295003652L;

	/** 呼び出しパラメータ */
	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 部屋管理番号
	private String hdnRoomKanriNo;
	// 社員番号
	private String hdnShainNo;
	// 役員区分
	private String hdnYakuinKbn;
	// 社宅賃貸料
	private String hdnShatakuChintairyo;
	// 提示データフラグ
	private String hdnTeijiFlag;
	// 申請書類管理番号
	private String hdnSyoruiKanriNo;

	/** 呼び出しパラメータ(部屋情報) & 戻り値 */
	// 規格
	private String hdnRateShienKikaku;
	// ①用途
	private String hdnRateShienYoto;
	// ②延べ面積
	private String hdnRateShienNobeMenseki;
	// ⑥基準使用料算定上延べ面積
	private String hdnRateShienKijunMenseki;
	// ⑦社宅使用料算定上延べ面積
	private String hdnRateShienShatakuMenseki;
	// 単価(親画面戻り値)
	private String sc003HdnRateShienTanka;
	// 算定経年(親画面への戻り値用)
	private String sc003HdnRateShienKeinen;
	// 経年残価率(親画面への戻り値用)
	private String sc003HdnRateShienKeinenZankaRitsu;

	/** 非表示項目 */
	// 物置調整面積
	private BigDecimal hdnBarnMensekiAdjust;
	// 地域区分
	private String hdnAreaKbn;
	// 用途区分
	private String hdnAuseKbn;
	// 建築年月日
	private String hdnBuildDate;
	// 構造区分
	private String hdnStructureKbn;
	// 生年月日
	private String hdnSeinengappi;

	/** ラベル */
	// 社宅名
	private String sc003ShatakuName;
	// 部屋番号
	private String sc003RoomNo;
	// 所在地
	private String sc003Address;
	// 所在地区分
	private String sc003AddressKbn;
	// 規格
	private String sc003Kikaku;
	// ①用途
	private String sc003Youto;
	// ②延べ面積
	private String sc003NobeMenseki;
	// ③サンルーム
	private String sc003SunRoom;
	// ④階段
	private String sc003Kaidan;
	// ⑤物置
	private String sc003Monooki;
	// ⑥基準使用料算定上延べ面積1
	private String sc003KijunMenseki1;
	// ⑥基準使用料算定上延べ面積2
	private String sc003KijunMenseki2;
	// ⑦社宅使用料算定上延べ面積1
	private String sc003ShatakuMenseki1;
	// ⑦社宅使用料算定上延べ面積2
	private String sc003ShatakuMenseki2;
	// ⑧基準単価1
	private String sc003KijunTanka1;
	// ⑧基準単価2
	private String sc003KijunTanka2;
	// ⑨経年調整なし使用料1
	private String sc003KeinenChouseinashiShiyoryo1;
	// ⑨経年調整なし使用料2
	private String sc003KeinenChouseinashiShiyoryo2;
	// ⑩経年
	private String sc003Keinen;
	// ⑪経年調整　残価率
	private String sc003KeinenZankaritsu;
	// ⑫使用料月額1
	private String sc003PatternShiyoryo1;
	// ⑫使用料月額2
	private String sc003PatternShiyoryo2;
	// ⑬年齢加算係数
	private String sc003NenreikasanKeisu;
	// 社宅使用料月額1
	private String sc003ShatakuShiyoryo2;
	// 社宅使用料月額2
	private String sc003ShatakuShiyoryo3;

	/** テキストボックス */
	// 延べ面積テキストボックス
	private String sc003InputNobeMenseki;

	/** ドロップダウン */
	// 規格ドロップダウン値
	private String sc003KikakuSelecte;
	// 規格ドロップダウンリスト
	private List<Map<String, Object>> sc003KikakuSelecteList;
	// 用途ドロップダウン値
	private String sc003YoutoSelect;
	// 用途ドロップダウンリスト
	private List<Map<String, Object>> sc003YoutoSelectList;

	/** エラー系 **/
	// 規格ドロップダウン
	private String sc003KikakuSelecteErr;
	// 用途ドロップダウン
	private String sc003YoutoSelectErr;
	// 延べ面積テキストボックス
	private String sc003InputNobeMensekiErr;
}
