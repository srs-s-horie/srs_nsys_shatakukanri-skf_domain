/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common;

import java.util.*;

import jp.co.c_nexco.skf.skf3030.domain.dto.common.Skf303010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3030Sc002CommonDto 入退居情報登録画面共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3030Sc002CommonDto extends Skf303010CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/** 定数 */
	// 選択タブインデックス(社宅情報タブ)
	public static final String SELECT_TAB_INDEX_SHATAKU = "0";
	// 選択タブインデックス(備品情報タブ)
	public static final String SELECT_TAB_INDEX_BIHIN = "1";
	// 選択タブインデックス(役員情報/相互利用情報タブ)
	public static final String SELECT_TAB_INDEX_YAKUIN = "2";
	// ハイフン
	public static final String HAIFUN = "―";
	// その他
	public static final String OTHER = "その他";
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
	// 提示ステータスフォントカラー
	public static final String TEIJI_FONT_SAKUSEI_CHU = "color:green;";
	public static final String TEIJI_FONT_SAKUSEI_SUMI = "color:green;";
	public static final String TEIJI_FONT_TEIJI_CHU = "color:green;";
	public static final String TEIJI_FONT_DOI_SUMI = "color:green;";
	public static final String TEIJI_FONT_SHONIN = "color:blue;";
	// 備品提示ステータスフォントカラー
	public static final String BIHIN_FONT_MI_SAKUSEI = "color:red;";
	public static final String BIHIN_FONT_SAKUSEI_CHU = "color:green;";
	public static final String BIHIN_FONT_SAKUSEI_SUMI = "color:green;";
	public static final String BIHIN_FONT_TEIJI_CHU = "color:green;";
	public static final String BIHIN_FONT_DOI_SUMI = "color:green;";
	public static final String BIHIN_FONT_HANNYU_MACHI = "color:green;";
	public static final String BIHIN_FONT_HANNYU_SUMI = "color:green;";
	public static final String BIHIN_FONT_HANSHUTSU_MACHI = "color:green;";
	public static final String BIHIN_FONT_HANSHUTSU_SUMI = "color:green;";
	public static final String BIHIN_FONT_SHONIN = "color:blue;";
	// メッセージボックス種類
	public static final int MSG_NASHI = 0;
	public static final int MSG_NYUKYO = 1;
	public static final int MSG_TAIKYO = 2;
	public static final int MSG_HEN_HAVE = 3;
	public static final int MSG_HEN_NONE = 4;
	/** メッセージボックススタイル */
	// 表示時ベーススタイル
	public static final String ON_MSGSTYLE_BASE = "border: solid 2px red; margin: 0px 20px; width:525px; height:33px; padding: 12px; position: absolute;color:red;";
	// 非表示
	public static final String OFF_MSG_STYLE = "display: none;";
	/** 使用料変更フラグ */
	// 使用料変更フラグ：変更なし
	public static final String SIYOURYOFLG_NONE = "0";
	// 使用料変更フラグ：変更あり
	public static final String SIYOURYOFLG_HAVE = "1";
	/** 社宅部屋フラグ */
	// 社宅部屋変更フラグ：なし
	public static final String SHATAKU_HEYA_FLG_NO = "0";
	// 社宅部屋変更フラグ：あり
	public static final String SHATAKU_HEYA_FLG_YES = "1";
	/** 備品指示書 */
	// 移動指示書背景色：ピンクrgb(255, 206, 207)
	public static final String COLOR_SHIJISHO_MOVE = "background-color:#FFCECF;";
	// 基本指示書背景色
	public static final String COLOR_SHIJISHO_BASE = "background-color:transparent;";
	// 指示書スタイル
//	public static final String SHIJISHO_STYLE_BASE = "text-align:center; border-top: 1px solid; height: 100%; border-right: 1px solid; width: 50px; border-bottom: 1px solid; padding-top: 3px; border-left: 1px solid; display: inline-block";
	public static final String SHIJISHO_STYLE_BASE = "text-align:center; width: 50px;";
	// 指示書文字列：搬入
	public static final String STRING_HANNYU = "搬入";
	// 指示書文字列：搬出
	public static final String STRING_HANSYUTSU = "搬出";
	// 指示書文字列：下取
	public static final String STRING_SHITADORI = "下取";
	/** 備品 */
	// 備品コード：下取り
	public static final String SHITADORI = "9";
	// 貸与ステータスエラー
	public static final String BIHIN_TAIYOSTTS_ERR = "'class='nfw-validation-error;'";

	// 社員番号変更フラグ：なし
	public static final String SHAINNO_CHANGE_UNAVAILABLE = "0";
	// 社員番号変更フラグ：あり
	public static final String SHAINNO_CHANGE_AVAILABLE = "1";
	// 相互利用
	public static final String SOGORIRYOU_ARI = "1";
	// 仮社員番号先頭文字
	public static final String KARI_K = "K";
	
	//社宅管理番号なし(0番)
	public static final String NO_SHATAKU_KANRI_ID = "0";
	//備品コード：単身
	public static final String TANSIN = "1";
	//備品コード：課税対象外
	public static final String NON_TAX = "2";
	//文字列："yyyyMM"
	public static final String NO_DATE = "____/__/__";
	//文字列："UPDATE"
	public static final String STR_UPDATE  = "UPDATE";
	//文字列："UPDATE"
	public static final String STR_UPDATE_TAIKYO = "UPDATE_TAIKYO";
	//文字列："DELETE"
	public static final String STR_DELETE = "DELETE";
	
	/** メッセージ引数 */
	// メッセージ引数：社宅　備考
	public static final String MSG_BICOU = "社宅　備考";
	// メッセージ引数：備品　備考
	public static final String MSG_BIHIN_BICOU = "備品情報備考";
	// メッセージ引数：区画１利用開始日
	public static final String MSG_RIYOU_START_DAY_ONE = "区画１利用開始日";
	// メッセージ引数：区画２利用開始日
	public static final String MSG_RIYOU_START_DAY_TWO = "区画２利用開始日";
	// メッセージ引数：区画１利用終了日
	public static final String MSG_RIYOU_END_DAY_ONE = "区画１利用終了日";
	// メッセージ引数：区画２利用終了日
	public static final String MSG_RIYOU_END_DAY_TWO = "区画２利用終了日";
	// メッセージ引数：区画番号１
	public static final String MSG_KUKAKU_NO_ONE = "区画１区画番号";
	// メッセージ引数：区画番号２
	public static final String MSG_KUKAKU_NO_TWO = "区画２区画番号";
	// メッセージ引数：備品貸与日
	public static final String MSG_TAIYO_DAY = "備品貸与日";
	// メッセージ引数：備品返却日
	public static final String MSG_HENKYAKU_DAY = "備品返却日";
	// メッセージ引数：搬入希望日
	public static final String MSG_KIBOU_DAY_IN = "搬入希望日";
	// メッセージ引数：搬出希望日
	public static final String MSG_KIBOU_DAY_OUT = "搬出希望日";
	// メッセージ引数：搬入日
	public static final String MSG_DAY_IN = "搬入日";
	// メッセージ引数：搬出日
	public static final String MSG_DAY_OUT = "搬出日";
	// メッセージ引数：搬入本人連絡先
	public static final String MSG_HONNIN_ADDR_IN = "搬入本人連絡先";
	// メッセージ引数：搬出本人連絡先
	public static final String MSG_HONNIN_ADDR_OUT = "搬出本人連絡先";
	// メッセージ引数：貸与規格（ヘッダ項目）
	public static final String MSG_SIYORYO_PAT_NAME = "貸与規格";
	// メッセージ引数：備品貸与状態
	public static final String MSG_BIHIN_TAIYO_STTS = "備品貸与状態";

	/** 処理モード */
	// 処理モード：一時保存
	public static final String TMP_SAVE = "0";
	// 処理モード：作成完了
	public static final String CREATE = "1";
	// 処理モード：社宅管理台帳登録
	public static final String SHATAKU_LOGIN = "2";
	// 処理モード：継続登録
	public static final String KEIZOKU_LOGIN = "3";

	/** 提示データパラメータ */
	public enum TEIJIDATA_PARAM {
		/**
		 * 提示番号:0
		 */
		TEIJI_NO,
		/**
		 * 社員番号:1
		 */
		SHAIN_NO,
		/**
		 * 入退居区分:2
		 */
		NYUTAIKYO_KBN,
		/**
		 * 社宅管理番号元:3
		 */
		SHATAKU_KANRINO_OLD,
		/**
		 * 部屋管理番号元:4
		 */
		SHATAKU_ROOMKANRINO_OLD,
		/**
		 * 駐車場管理番号1元:5
		 */
		CHUSHAJONO_ONE_OLD,
		/**
		 * 駐車場管理番号2元:6
		 */
		CHUSHAJONO_TWO_OLD,
		/**
		 * 社宅管理番号:7
		 */
		SHATAKU_KANRINO,
		/**
		 * 部屋管理番号:8
		 */
		SHATAKU_ROOMKANRINO,
		/**
		 * 駐車場管理番号1:9
		 */
		CHUSHAJONO_ONE,
		/**
		 * 駐車場管理番号2:10
		 */
		CHUSHAJONO_TWO,
		/**
		 * 入居予定日:11
		 */
		NYUKYOYOTEI_DATE,
		/**
		 * 退居予定日:12
		 */
		TAIKYOYOTEI_DATE,
		/**
		 * ボタン区分:13
		 */
		BUTTON_FLG,
		/**
		 * 提示データ更新日:14
		 */
		TEIJIDATA_UPDATEDATE,
		/**
		 * 入退居予定更新日:15
		 */
		NYUTAIKYOYOTEI_UPDATEDATE,
		/**
		 * 社宅部屋情報マスタ元更新日:16
		 */
		SHATAKUROOM_OLD_UPDATEDATE,
		/**
		 * 社宅部屋情報マスタ更新日:17
		 */
		SHATAKUROOM_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ元更新日（区画１）:18
		 */
		SHATAKU_PARKINGBLOCK_OLD1_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ元更新日（区画１）:19
		 */
		SHATAKU_PARKINGBLOCK_OLD11_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ更新日（区画１）:20
		 */
		SHATAKU_PARKINGBLOCK_1_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ元更新日（区画２）:21
		 */
		SHATAKU_PARKINGBLOCK_OLD2_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ元更新日（区画２）:22
		 */
		SHATAKU_PARKINGBLOCK_OLD21_UPDATEDATE,
		/**
		 * 社宅駐車場区画情報マスタ更新日（区画２）:23
		 */
		SHATAKU_PARKINGBLOCK_2_UPDATEDATE,
		/**
		 * 更新者:24
		 */
		UPDATE_USER_ID,
		/**
		 * 更新IPアドレス:25
		 */
		UPDATE_PROGRAM_ID,
		/**
		 * 使用料変更フラグ:26
		 */
		SHIYOURYO_FLG
	}

	/** 使用料パターン登録項目 */
	public enum RENTAL_PATTERN {
		/**
		 * 社宅管理番号:0
		 */
		SHATAKUKANRI_NO,
		/**
		 * 使用料パターンID:1
		 */
		RENTAL_PATTERNID,
		/**
		 * パターン名:2
		 */
		PATTERN_NAME,
		/**
		 * 規格:3
		 */
		KIKAKU,
		/**
		 * 規格(補足):4
		 */
		KIKAKU_HOSOKU,
		/**
		 * 基準使用料算定上延べ面積:5
		 */
		KIJUN_MENSEKI,
		/**
		 * 社宅使用料算定上延べ面積:6
		 */
		SHATAKU_MENSEKI,
		/**
		 * 経年残価率:7
		 */
		KEINEN_ZANKARITSU,
		/**
		 * 用途:8
		 */
		YOTO,
		/**
		 * 寒冷地:9
		 */
		KANREICHI,
		/**
		 * 狭小:10
		 */
		KYOUSYOU,
		/**
		 * 経年:11
		 */
		KEINEN,
		/**
		 * 基本使用料:12
		 */
		KIHON_SHIYORYO,
		/**
		 * 単価:13
		 */
		TANKA,
		/**
		 * 社宅使用料月額:14
		 */
		SHATAKU_GETSUGAKU,
		/**
		 * 備考:15
		 */
		BIKO,
		/**
		 * 補足資料名:16
		 */
		HOSOKU_SHIRYO_NAME,
		/**
		 * 補足資料サイズ:17
		 */
		HOSOKU_SHIRYO_SIZE,
		/**
		 * 補足ファイル:18
		 */
		HOSOKU_FILE,
		/**
		 * 削除フラグ:19
		 */
		DELETE_FLAG,
		/**
		 * 登録日時:20
		 */
		INSERT_DATE,
		/**
		 * 登録者ID:21
		 */
		INSERT_USER_ID,
		/**
		 * 登録機能ID:22
		 */
		INSERT_PROGRAM_ID,
		/**
		 * 更新日時:23
		 */
		UPDATE_DATE,
		/**
		 * 更新者ID:24
		 */
		UPDATE_USER_ID,
		/**
		 * 更新機能ID:25
		 */
		UPDATE_PROGRAM_ID,
		/**
		 * 延べ面積:26
		 */
		NOBE_MENSEKI,
		/**
		 * サンルーム面積:27
		 */
		SUNROOM_MENSEKI,
		/**
		 * 階段面積:28
		 */
		KAIDAN_MENSEKI,
		/**
		 * 物置面積:29
		 */
		MONOOKI_MENSEKI,
		/**
		 * 更新種別:30
		 */
		UPDATE_KIND
	}

	/** 更新カウンタ */
	public enum UPDATE_COUNTER {
		/**
		 * 提示データ更新件数
		 */
		UPD_COUNT_TJ,
		/**
		 * 入退居予定更新件数
		 */
		UPD_COUNT_NY,
		/**
		 * 提示備品更新件数
		 */
		UPD_COUNT_TBD,
		/**
		 * 社宅部屋情報元更新件数
		 */
		UPD_COUNT_OLD_SR,
		/**
		 * 社宅部屋情報更新件数
		 */
		UPD_COUNT_SR,
		/**
		 * 社宅駐車場区画情報元更新件数（区画１）
		 */
		UPD_COUNT_OLD_SPB_1,
		/**
		 * 社宅駐車場区画情報更新件数（区画１）
		 */
		UPD_COUNT_SPB_1,
		/**
		 * 社宅駐車場区画情報元更新件数（区画２）
		 */
		UPD_COUNT_OLD_SPB_2,
		/**
		 * 社宅駐車場区画情報更新件数（区画２）
		 */
		UPD_COUNT_SPB_2,
		/**
		 * 使用料パターン更新件数
		 */
		UPD_COUNT_RP
	}

	/** 提示データ一覧画面連携項目 */
//	// 提示番号
//	private String hdnTeijiNo;
//	// 入居予定日
//	private String hdnNyukyoDate;
//	// 退居予定日
//	private String hdnTaikyoDate;
//	// 申請書類管理番号
//	private String hdnShoruikanriNo;
//	// 入退居区分
//	private String hdnNyutaikyoKbn;
//	// 申請区分
//	private String hdnApplKbn;
//	// 社員番号変更フラグ
//	private String hdnShainNoChangeFlg;

	/** 提示データ一覧検索条件退避 */
	// 社員番号
	private String searchInfoShainNo;
	// 社員氏名
	private String searchInfoShainName;
	// 社宅名
	private String searchInfoShatakuName;
	// 入退居区分
	private String searchInfoNyutaikyoKbn;
	// 社宅提示状況
	private String searchInfoStJyokyo;
	// 社宅提示確認督促
	private String searchInfoStKakunin;
	// 備品提示状況
	private String searchInfoBhJyokyo;
	// 備品提示確認督促
	private String searchInfoBhKakunin;
	// 備品搬入搬出督促
	private String searchInfoMoveInout;

	/** 次月予約登録パラメータ */
	// 提示番号
	private String hdnJigetuYoyakuTeijiNo;
	// 基準年月
	private String hdnJigetuYoyakuYearMonth;
	// 社宅管理台帳ID
	private String hdnJigetuYoyakuShatakuKanriId;
	// 社宅使用料月額
	private String hdnJigetuYoyakuRental;
	// 個人負担共益費月額
	private String hdnJigetuYoyakuKyoekihiPerson;
	// 区画1_駐車場使用料月額
	private String hdnJigetuYoyakuParkingRentalOne;
	// 区画2_駐車場使用料月額
	private String hdnJigetuYoyakuParkingRentalTwo;

	/** 使用料計算入力支援戻り値 */
	// 規格
	private String hdnRateShienKikaku;
	// 用途
	private String hdnRateShienYoto;
	// 延べ面積
	private String hdnRateShienNobeMenseki;
	// 基準面積(基準使用料算定上延べ面積)
	private String hdnRateShienKijunMenseki;
	// 社宅面積(社宅使用料算定上延べ面積)
	private String hdnRateShienShatakuMenseki;
	// 規格名
	private String hdnRateShienKikakuName;
	// 用途名
	private String hdnRateShienYotoName;
	// サンルーム面積
	private String hdnRateShienSunroomMenseki;
	// 階段面積
	private String hdnRateShienKaidanMenseki;
	// 物置面積
	private String hdnRateShienMonookiMenseki;
	// 単価
	private String hdnRateShienTanka;
	// 経年
	private String hdnRateShienKeinen;
	// 経年残価率
	private String hdnRateShienKeinenZankaRitsu;
	// 使用料パターン名
	private String hdnRateShienPatternName;
	// 使用料パターン月額
	private String hdnRateShienPatternGetsugaku;
	// 社宅使用料月額
	private String hdnRateShienShatakuGetsugaku;
	// 社宅基本使用料
	private String hdnRateShienKihonShiyoryo;

	/** 非表示項目 */
	// 社宅管理台帳ID
	private String hdnShatakuKanriId;
	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 社宅管理番号元?
	private String hdnShatakuKanriNoOld;
	// 部屋管理番号
	private String hdnRoomKanriNo;
	// 部屋管理番号元?
	private String hdnRoomKanriNoOld;
	// 管理会社コード
	private String hdnCompanyCode;
	// 締め処理実行区分
	private String hdnBillingActKbn;
	// 旧提示番号
	private String hdnTeijiNoOld;
	// 入退居区分元?
	private String hdnNyutaikyoKbnOld;
	// 同一書類管理番号存在フラグ
	private Boolean hdnSameFlg;
	// 役員算定区分
	private String hdnYakuin;
	// 個人負担共益費月額
	private String hdnKojinFutan;
	// 区画番号1
	private String hdnKukakuNoOne;
	// 利用開始日1
	private String hdnRiyouStartDayOne;
	// 利用終了日1
	private String hdnRiyouEndDayOne;
	// 区画番号2
	private String hdnKukakuNoTwo;
	// 利用開始日2
	private String hdnRiyouStartDayTwo;
	// 利用終了日2
	private String hdnRiyouEndDayTwo;
	// 使用料計算パターンID元
	private String hdnSiyouryoIdOld;
	// 使用料パターンID
	private String hdnSiyouryoId;
	// 使用料計算パターンID
	private String hdnShiyoryoKeisanPatternId;
	// 駐車場区画番号元（区画１）
	private String hdnChushajoNoOneOld;
	//private String hdnChushajoNoOne;
	// 駐車場区画番号元（区画2）
	private String hdnChushajoNoTwoOld;
	//private String hdnChushajoNoTwo;
	// 継続登録フラグ
	private Boolean hdnKeizokuBtnFlg;
	// 次月予約存在フラグ
//	private Boolean hdnYoyakuFlg;
	// 原籍会社コード
	private String hdnGensekiKaishaCd;
	// 給与支給会社コード
	private String hdnKyuyoKaishaCd;
	// 貸付会社コード
	private String hdnKashitsukeKaishaCd;
	// 借受会社コード
	private String hdnKariukeKaishaCd;
	// 配属会社コード
	private String hdnHaizokuKaishaCd;
	// 社宅提示ステータス
	private String hdnShatakuTeijiStatus;
	// 備品提示ステータス
	private String hdnBihinTeijiStatus;
	// 備品貸与区分
	private String hdnBihinTaiyoKbn;
	// 提示データ更新日
	private String hdnTeijiDataUpdateDate;
	// 社宅部屋情報マスタ元更新日
	private String hdnShatakuRoomUpdateDate;
	private String hdnShatakuRoomOldUpdateDate;
	// 社宅駐車場区画情報マスタ元（区画１）更新日
	private String hdnShatakuParkingBlock1UpdateDate;
	private String hdnShatakuParkingBlockOld1UpdateDate;
	private String hdnShatakuParkingBlockOld11UpdateDate;
	// 社宅駐車場区画情報マスタ元（区画２）更新日
	private String hdnShatakuParkingBlock2UpdateDate;
	private String hdnShatakuParkingBlockOld2UpdateDate;
	private String hdnShatakuParkingBlockOld21UpdateDate;
	// 生年月日
	private String hdnBirthday;
	// 社宅部屋変更フラグ
	private String hdnShatakuHeyaFlg;
	// 使用料パターン排他処理用更新日付
	private String hdnRentalPatternUpdateDate;
	// 入退居予定データ更新日
	private String hdnNyutaikyoYoteiUpdateDate;
	// 備品搬出待ちフラグ
	private Boolean hdnBihinMoveOutFlg;
	// 使用料変更フラグ
	private String hdnSiyouryoFlg;
	// ?
	private String hdnFieldMessage;
	// 区画１ 終了日
	private String hdnEndDayOne;
	// 区画2 終了日
	private String hdnEndDayTwo;
	// ?
	private String hdnIsInfo;
	// 備品一覧再取得フラグ
	private Integer bihinItiranFlg;
	private Boolean bihinItiranReloadFlg;
	//次回TabIndex
	private String nextTabIndex;
	

	private String hdnShatakuRoomKanriNo;
	private String hdnNengetsu;
	private String hdnShainNo;
	private String hdnShainName;
	//社員番号変更フラグ
	private String hdnShainNoChangeFlg;
	private String hdnRentalPatternId;
	private String hdnChushajoKanriNo1;
	private String hdnChushajoKanriNo2;
	private String hdnBihinCd;
	private String hdnShatakuShiyoryoGetsugaku;
	private String hdnChangeBeforeRentalPatternId;
	private String hdnChangeBeforeNyukyoYoteibi;
	private String hdnChangeBeforeTaikyoYoteibi;
	private String hdnChangeBeforeYakuinSantei;
	private String hdnChangeBeforeShatakuShiyoryoChoseiKingaku;
	private String hdnChangeBeforeKojinFutanKyoekihiGetsugaku;
	private String hdnChangeBeforeKojinFutanKyoekihiChoseiKingaku;
	private String hdnChangeBeforeChushajoKanriNo1;
	private String hdnChangeBeforeKukaku1RiyoKaishibi;
	private String hdnChangeBeforeKukaku1RiyoShuryobi;
	private String hdnChangeBeforeChushajoKanriNo2;
	private String hdnChangeBeforeKukaku2RiyoKaishibi;
	private String hdnChangeBeforeKukaku2RiyoShuryobi;
	private String hdnChangeBeforeKaishibi;
	private String hdnChangeBeforeShuryobi;
	private String hdnChangeBeforeTaiyobi;
	private String hdnChangeBeforeHenkyakubi;
	private String hdnNYUTAIKYO_KBN;
	private String hdnRentalFlg;
	private String hdnShatakuKanriFlg1;
	private String hdnShatakuKanriFlg2;
	private String hdnKaiSanAfterShatakuShiyoryoHiwariKingaku;
	private String hdnKaiSanAfterShatakuShiyoryoGetsugakuChoseigo;
	private String hdnKaiSanAfterKukaku1ChushajoShiyoroHiwariKingaku;
	private String hdnKaiSanAfterKukaku2ChushajoShiyoroHiwariKingaku;
	private String hdnKaiSanAfterChushajoShiyoryoGetsugakuChoseigo;
	private String hdnKaiSanAfterShatakuShiyoryoGetsugaku;
	private String hdnKaiSanAfterKukaku1ChushajoShiyoroGetsugaku;
	private String hdnKaiSanAfterKukaku2ChushajoShiyoroGetsugaku;
	private String hdnKaiSanAfterKojinFutanKyoekihiGetsugakuChoseigo;
	//private String hdnEndDay1;
	//private String hdnEndDay2;
	//使用料パターン名
	private String litShiyoryoKeisanPattern;
	//寒冷地減免
	private String litKanreichiGenmen;
	//狭小減免
	private String litKyoshoGenmen;
	
	/** メッセージボックス */
	// スタイル
	private String sc006MsgBoxStyle;
	// メッセージ
	private String sc006Msg;

	/** 運用ガイド */
	private String operationGuidePath;

	/** タブステータス */
	// 社宅タブ
	private Boolean shatakuTabStatus;
	// 備品タブ
	private Boolean bihinTabStatus;
	// 役員情報/相互利用タブ
	private Boolean sogoTabStatu;

	/** サーバー処理連携用 */
	// 表示タブインデックス
	private String hdnTabIndex;
	// 作成完了ボタン押下時メッセージ
	private String litMessageCreate;
	// 一時保存ボタン押下時メッセージ
	private String litMessageTmpSave;
	// 社宅管理台帳登録
	private String litMessageShatakuLogin;
	// 継続登録ボタン押下時メッセージ
	private String litMessageKeizokuLogin;
	// 前に戻るボタン押下時メッセージ
	private String litMessageBack;
	// 処理状態
	private String sc006Status;
	// JSONラベルリスト
	private String jsonLabelList;
	// JSON備品情報 リスト
	private String jsonBihin;
	// 協議中フラグ状態
	private String sc006KyoekihiKyogichuCheckState;

	/** ラベル値 */
	// 社員番号
	private String sc006ShainNo;
	// 社宅名
	private String sc006ShatakuName;
	// 貸与規格(使用料計算パターン名)
	private String sc006SiyoryoPatName;
	// 社員氏名
	private String sc006ShainName;
	// 部屋番号
	private String sc006HeyaNo;
	// 社宅使用料月額（ヘッダ項目）
	private String sc006SiyoryoMonthPay;
	// 対象年月
	private String sc006TaishoNengetsu;
	// 入退居（ヘッダ項目）
	private String sc006NyutaikyoKbn;
	// 入退居(フォント色)
	private String sc006NyutaikyoKbnColor;
	// 社宅提示(ヘッダ項目)
	private String sc006ShatakuStts;
	// 社宅提示(フォント色)
	private String sc006ShatakuSttsColor;
	// 社宅提示(ヘッダ項目)
	private String sc006ShatakuSttsCd;
	// 備品提示(ヘッダ項目)
	private String sc006BihinStts;
	// 備品提示(フォント色)
	private String sc006BihinSttsColor;
	// 備品提示(ヘッダ項目)
	private String sc006BihinSttsCd;
	// 区画番号1
	private String sc006KukakuNoOne;
	// 駐車場使用料月額1
	private String sc006TyusyaMonthPayOne;
	// 駐車場使用料日割金額1
	private String sc006TyusyaDayPayOne;
	// 貸与用途
	private String sc006TaiyoYouto;
	// 区画番号2
	private String sc006KukakuNoTwo;
	// 貸与規格
	private String sc006TaiyoKikaku;
	// 社宅使用料月額
	private String sc006ShiyoryoTsukigaku;
	// 駐車場使用料月額2
	private String sc006TyusyaMonthPayTwo;
	// 社宅使用料日割金額
	private String sc006SiyoryoHiwariPay;
	// 駐車場使用料日割金額 2
	private String sc006TyusyaDayPayTwo;
	// 社宅使用料月額(調整後)
	private String sc006SyatauMonthPayAfter;
	// 駐車場使用料月額(調整後)
	private String sc006TyusyaMonthPayAfter;
	// 個人負担共益費月額(調整後)
	private String sc006KyoekihiPayAfter;
	// 管理会社
	private String sc006KanriKaisya;

	/** 入力エリア */
	// 入居予定日
	private String sc006NyukyoYoteiDay;
	// 退居予定日
	private String sc006TaikyoYoteiDay;
	// 利用開始日1
	private String sc006RiyouStartDayOne;
	// 利用終了日1
	private String sc006RiyouEndDayOne;
	// 利用開始日2
	private String sc006RiyouStartDayTwo;
	// 利用終了日2
	private String sc006RiyouEndDayTwo;
	// 社宅使用料調整金額
	private String sc006SiyoroTyoseiPay;
	// 駐車場使用料調整金額
	private String sc006TyusyaTyoseiPay;
	// 個人負担共益費 協議中
	private Boolean sc006KyoekihiKyogichuCheck;
	// 備考
	private String sc006Bicou;
	// 個人負担共益費月額
	private String sc006KyoekihiMonthPay;
	// 個人負担共益費調整金額
	private String sc006KyoekihiTyoseiPay;
	// 貸与日
	private String sc006TaiyoDay;
	// 返却日
	private String sc006HenkyakuDay;
	// 搬入希望日
	private String sc006KibouDayIn;
	// 搬入本人連絡先
	private String sc006HonninAddrIn;
	// 搬入受取代理人名
	private String sc006UketoriDairiInName;
	// 搬入受取代理人連絡先
	private String sc006UketoriDairiAddr;
	// 搬入レンタル指示書発行日
	private String sc006HannyuShijisyoHakkoubi;
	// 搬出希望日
	private String sc006KibouDayOut;
	// 搬出本人連絡先
	private String sc006HonninAddrOut;
	// 搬出立会代理人名
	private String sc006TachiaiDairi;
	// 搬出立会代理人連絡先
	private String sc006TachiaiDairiAddr;
	// 搬出レンタル指示書発行日
	private String sc006HanshutuShijisyoHakkoubi;
	// 代理人備考
	private String sc006DairiBiko;
	// 備品備考
	private String sc006BihinBiko;
	// 所属機関
	private String sc006SyozokuKikan;
	// 室・部名
	private String sc006SituBuName;
	// 課等名
	private String sc006KanadoMei;
	// 配属データコード番号
	private String sc006HaizokuNo;
	// 開始日
	private String sc006StartDay;
	// 終了日
	private String sc006EndDay;
	// 社宅賃貸料
	private String sc006ChintaiRyo;
	// 駐車場料金
	private String sc006TyusyajoRyokin;
	// 共益費(事業者負担)
	private String sc006Kyoekihi;

	/** ドロップダウンリスト */
	// 原籍会社選択値
	private String sc006OldKaisyaNameSelect;
	// 原籍会社選択リスト
	private List<Map<String, Object>> sc006OldKaisyaNameSelectList;
	// 給与支給会社名選択値
	private String sc006KyuyoKaisyaSelect;
	// 給与支給会社名選択リスト
	private List<Map<String, Object>> sc006KyuyoKaisyaSelectList;
	// 居住者区分選択値
	private String sc006KyojyusyaKbnSelect;
	// 居住者区分選択リスト
	private List<Map<String, Object>> sc006KyojyusyaKbnSelectList;
	// 役員算定選択値
	private String sc006YakuinSanteiSelect;
	// 役員算定選択リスト
	private List<Map<String, Object>> sc006YakuinSanteiSelectList;
	// 共益費支払月選択値
	private String sc006KyoekihiPayMonthSelect;
	// 共益費支払月選択リスト
	private List<Map<String, Object>> sc006KyoekihiPayMonthSelectList;
	// 搬入希望時間選択値
	private String sc006KibouTimeInSelect;
	// 搬入希望時間選択リスト
	private List<Map<String, Object>> sc006KibouTimeInSelectList;
	// 搬出希望時間選択値
	private String sc006KibouTimeOutSelect;
	// 搬出希望時間選択リスト
	private List<Map<String, Object>> sc006KibouTimeOutSelectList;
	// 配属会社名選択値
	private String sc006HaizokuKaisyaSelect;
	// 配属会社名選択リスト
	private List<Map<String, Object>> sc006HaizokuKaisyaSelectList;
	// 出向の有無(相互利用状況)選択値
	private String sc006SogoRyojokyoSelect;
	// 出向の有無(相互利用状況)選択リスト
	private List<Map<String, Object>> sc006SogoRyojokyoSelectList;
	// 貸付会社選択値
	private String sc006TaiyoKaisyaSelect;
	// 貸付会社選択リスト
	private List<Map<String, Object>> sc006TaiyoKaisyaSelectList;
	// 借受会社選択値
	private String sc006KariukeKaisyaSelect;
	// 借受会社選択リスト
	private List<Map<String, Object>> sc006KariukeKaisyaSelectList;
	// 相互利用判定区分選択値
	private String sc006SogoHanteiKbnSelect;
	// 相互利用判定区分選択リスト
	private List<Map<String, Object>> sc006SogoHanteiKbnSelectList;
	// 社宅使用料会社間送金区分選択値
	private String sc006SokinShatakuSelect;
	// 社宅使用料会社間送金区分選択リスト
	private List<Map<String, Object>> sc006SokinShatakuSelectList;
	// 共益費会社間送付区分選択値
	private String sc006SokinKyoekihiSelect;
	// 共益費会社間送付区分選択リスト
	private List<Map<String, Object>> sc006SokinKyoekihiSelectList;

	/** リストテーブル */
	// 備品情報リスト
	private List<Map<String, Object>> bihinInfoListTableData;

	/********************** 活性/非活性(仮) ****************************/
	/** 上部ボタン */
	// 申請内容
	//TODO 社員入力に直す
	private Boolean sc006ShinseiNaiyoDisableFlg;
	// 社宅入力支援
	private Boolean shayakuHeyaShienDisableFlg;
	// 社宅使用料入力支援
	private Boolean shiyoryoShienDisableFlg;

	/** タブ切替 */
	// 備品情報タブ
	private Boolean tbpBihinInfo;
	// 役員情報/相互利用情報タブ
	private Boolean tbpSougoRiyouInfo;

	/** 社宅情報 */
	// 原籍会社
	private Boolean sc006OldKaisyaNameSelectDisableFlg;
	// 給与支給会社
	private Boolean sc006KyuyoKaisyaSelectDisableFlg;
	// 入居予定日
	private Boolean sc006NyukyoYoteiDayDisableFlg;
	// 退居予定日
	private Boolean sc006TaikyoYoteiDayDisableFlg;
	// 居住者区分
	private Boolean sc006KyojyusyaKbnSelectDisableFlg;
	// 役員算定
	private Boolean sc006YakuinSanteiSelectDisableFlg;
	// 社宅使用料調整金額
	private Boolean sc006SiyoroTyoseiPayDisableFlg;
	// 社宅情報:個人負担共益費 協議中
	private Boolean sc006KyoekihiKyogichuCheckDisableFlg;
	// 個人負担共益費月額
	private Boolean sc006KyoekihiMonthPayDisableFlg;
	// 個人負担共益費調整金額
	private Boolean sc006KyoekihiTyoseiPayDisableFlg;
	// 共益費支払月
	private Boolean sc006KyoekihiPayMonthSelectDisableFlg;
	// 駐車場入力支援（区画１）
	private Boolean parkingShien1DisableFlg;
	// 利用開始日1
	private Boolean sc006RiyouStartDayOneDisableFlg;
	// 区画1クリア
	private Boolean clearParking1DisableFlg;
	// 利用終了日1
	private Boolean sc006RiyouEndDayOneDisableFlg;
	// 駐車場入力支援（区画2）
	private Boolean parkingShien2DisableFlg;
	// 利用開始日2
	private Boolean sc006RiyouStartDayTwoDisableFlg;
	// 区画2クリア
	private Boolean clearParking2DisableFlg;
	// 利用終了日2
	private Boolean sc006RiyouEndDayTwoDisableFlg;
	// 社宅情報:駐車場使用料調整金額
	private Boolean sc006TyusyaTyoseiPayDisableFlg;
	// 社宅情報:備考
	private Boolean sc006BicouDisableFlg;
	// 社宅情報
	private Boolean gShatakuInfoControlStatusFlg;

	/** 備品情報 */
	// 貸与日
	private Boolean sc006TaiyoDayDisableFlg;
	// 返却日
	private Boolean sc006HenkyakuDayDisableFlg;
	// 搬入希望日
	private Boolean sc006KibouDayInDisableFlg;
	// 搬入希望時間帯
	private Boolean sc006KibouTimeInSelectDisableFlg;
	// 搬入本人連絡先
	private Boolean sc006HonninAddrInDisableFlg;
	// 受取代理人
	private Boolean sc006UketoriDairiInNameDisableFlg;
	// 搬入社員入力支援
	private Boolean sc006UketoriDairiInShienDisableFlg;
	// 受取代理人連絡先
	private Boolean sc006UketoriDairiAddrDisableFlg;
	// 搬出希望日
	private Boolean sc006KibouDayOutDisableFlg;
	// 搬出希望日時時間帯
	private Boolean sc006KibouTimeOutSelectDisableFlg;
	// 搬出本人連絡先
	private Boolean sc006HonninAddrOutDisableFlg;
	// 搬出立会代理人
	private Boolean sc006TachiaiDairiDisableFlg;
	// 搬出社員入力支援
	private Boolean sc006TachiaiDairiShienDisableFlg;
	// 搬出立会代理人連絡先
	private Boolean sc006TachiaiDairiAddrDisableFlg;
	// 代理人備考
	private Boolean sc006DairiBikoDisableFlg;
	// 備品備考
	private Boolean sc006BihinBikoDisableFlg;
	// 備品情報
	private Boolean gBihinInfoControlStatusFlg;

	/** 相互利用/役員情報 */
	// 貸付会社
	private Boolean sc006TaiyoKaisyaSelectDisableFlg;
	// 借受会社
	private Boolean sc006KariukeKaisyaSelectDisableFlg;
	// 出向の有無(相互利用状況)
	private Boolean sc006SogoRyojokyoSelectDisableFlg;
	// 相互利用判定区分
	private Boolean sc006SogoHanteiKbnSelectDisableFlg;
	// 社宅使用料会社間送金区分
	private Boolean sc006SokinShatakuSelectDisableFlg;
	// 送金区分（共益費）
	private Boolean sc006SokinKyoekihiSelectDisableFlg;
	// 社宅賃貸料
	private Boolean sc006ChintaiRyoDisableFlg;
	// 駐車場料金
	private Boolean sc006TyusyajoRyokinDisableFlg;
	// 共益費(事業者負担)
	private Boolean sc006KyoekihiDisableFlg;
	// 開始日
	private Boolean sc006StartDayDisableFlg;
	// 終了日
	private Boolean sc006EndDayDisableFlg;
	// 配属会社名
	private Boolean sc006HaizokuKaisyaSelectDisableFlg;
	// 所属機関
	private Boolean sc006SyozokuKikanDisableFlg;
	// 室・部名
	private Boolean sc006SituBuNameDisableFlg;
	// 課等名
	private Boolean sc006KanadoMeiDisableFlg;
	// 配属データコード番号
	private Boolean sc006HaizokuNoDisableFlg;
	// 相互利用情報タブ項目制御
	//private String gSougoRiyouInfoControlStatusFlg;
	// 相互利用情報タブ項目制御
	private String hdnSougoRiyouFlg;


	/** 下部ボタン */
	// 運用ガイドボタン
	private Boolean btnUnyonGuideDisableFlg;
	// 登録ボタン
	private Boolean btnRegistDisableFlg;
	// 次月予約ボタン
	private Boolean btnJigetuYoyakuDisableFlg;
	// 削除ボタン
	private Boolean btnDeleteDisableFlg;

	/** エラー */
	// 原籍会社
	private String sc006OldKaisyaNameSelectErr;
	// 給与支給会社名
	private String sc006KyuyoKaisyaSelectErr;
	// 入居予定日
	private String sc006NyukyoYoteiDayErr;
	// 退居予定日
	private String sc006TaikyoYoteiDayErr;
	// 利用開始日1
	private String sc006RiyouStartDayOneErr;
	// 利用終了日1
	private String sc006RiyouEndDayOneErr;
	// 居住者区分
	private String sc006KyojyusyaKbnSelectErr;
	// 利用開始日2
	private String sc006RiyouStartDayTwoErr;
	// 利用終了日2
	private String sc006RiyouEndDayTwoErr;
	// 役員算定
	private String sc006YakuinSanteiSelectErr;
	// 社宅使用料調整金額
	private String sc006SiyoroTyoseiPayErr;
	// 駐車場使用料調整金額
	private String sc006TyusyaTyoseiPayErr;
	// 個人負担共益費月額
	private String sc006KyoekihiMonthPayErr;
	// 個人負担共益費調整金額
	private String sc006KyoekihiTyoseiPayErr;
	// 共益費支払月選択値
	private String sc006KyoekihiPayMonthSelectErr;
	// 貸与日
	private String sc006TaiyoDayErr;
	// 返却日
	private String sc006HenkyakuDayErr;
	// 搬入希望日
	private String sc006KibouDayInErr;
	// 搬入希望時間
	private String sc006KibouTimeInSelectErr;
	// 搬入本人連絡先
	private String sc006HonninAddrInErr;
	// 搬入受取代理人名
	private String sc006UketoriDairiInNameErr;
	// 搬入受取代理人連絡先
	private String sc006UketoriDairiAddrErr;
	// 搬出希望日
	private String sc006KibouDayOutErr;
	// 搬出希望時間
	private String sc006KibouTimeOutSelectErr;
	// 搬出本人連絡先
	private String sc006HonninAddrOutErr;
	// 搬出立会代理人名
	private String sc006TachiaiDairiErr;
	// 搬出立会代理人連絡先
	private String sc006TachiaiDairiAddrErr;
	// 配属会社名選択値
	private String sc006HaizokuKaisyaSelectErr;
	// 出向の有無(相互利用状況)
	private String sc006SogoRyojokyoSelectErr;
	// 所属機関
	private String sc006SyozokuKikanErr;
	// 貸付会社選択値
	private String sc006TaiyoKaisyaSelectErr;
	// 室・部名
	private String sc006SituBuNameErr;
	// 借受会社
	private String sc006KariukeKaisyaSelectErr;
	// 課等名
	private String sc006KanadoMeiErr;
	// 相互利用判定区分
	private String sc006SogoHanteiKbnSelectErr;
	// 配属データコード番号
	private String sc006HaizokuNoErr;
	// 社宅使用料会社間送金区分
	private String sc006SokinShatakuSelectErr;
	// 共益費会社間送付区分
	private String sc006SokinKyoekihiSelectErr;
	// 開始日
	private String sc006StartDayErr;
	// 終了日
	private String sc006EndDayErr;
	// 社宅賃貸料
	private String sc006ChintaiRyoErr;
	// 駐車場料金
	private String sc006TyusyajoRyokinErr;
	// 共益費(事業者負担)
	private String sc006KyoekihiErr;
	
	/** 検索条件保持 */
	private String serachKanriKaisha ;//(Skf303010CommonSharedService.SEARCH_INFO_KANRI_KAISHA_KEY);
	private String serachAgency ;//(Skf303010CommonSharedService.SEARCH_INFO_AGENCY_KEY);
	private String serachShainNo ;//(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NO_KEY);
	private String serachShainName ;//(Skf303010CommonSharedService.SEARCH_INFO_SHAIN_NAME_KEY);
	private String serachShatakName ;//(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_NAME_KEY);
	private String serachShatakKbn ;//(Skf303010CommonSharedService.SEARCH_INFO_SHATAK_KBN_KEY);
	private String serachSogoriyo ;//(Skf303010CommonSharedService.SEARCH_INFO_SOGORIYO_KEY);
	private String serachNengetsu ;//(Skf303010CommonSharedService.SEARCH_INFO_NENGETSU_KEY);
	private String serachShimeShori ;//(Skf303010CommonSharedService.SEARCH_INFO_SHIME_SHORI_KEY);
	private String serachPositiveRenkei ;//(Skf303010CommonSharedService.SEARCH_INFO_POSITIVE_RENKEI_KEY);
	private String serachKaishakanSokin ;//(Skf303010CommonSharedService.SEARCH_INFO_KAISHAKAN_SOKIN_KEY);
	private String serachGensekiKaisha ;//(Skf303010CommonSharedService.SEARCH_INFO_GENSEKI_KAISHA_KEY);
	private String serachKyuyoSikyuKaisha ;//(Skf303010CommonSharedService.SEARCH_INFO_SIKYU_KAISHA_KEY);
	private String serachKyojushaKbn ;//(Skf303010CommonSharedService.SEARCH_INFO_KYOJUSHA_KBN_KEY);
	private String serachAkiHeya ;//(Skf303010CommonSharedService.SEARCH_INFO_AKI_HEYA_KEY);
	private String serachParkingSiyoryo ;//(Skf303010CommonSharedService.SEARCH_INFO_PARKING_KEY);
	private String serachHonraiYoto ;//(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_YOTO_KEY);
	private String serachHonraiKikaku ;//(Skf303010CommonSharedService.SEARCH_INFO_HONRAI_KIKAKU_KEY);
	private String serachYakuin ;//(Skf303010CommonSharedService.SEARCH_INFO_YAKUIN_KEY);
	private String serachShukkosha ;//(Skf303010CommonSharedService.SEARCH_INFO_SHUKKOSHA_KEY);
	private String serachBiko ;//(Skf303010CommonSharedService.SEARCH_INFO_BIKO_KEY);

}
