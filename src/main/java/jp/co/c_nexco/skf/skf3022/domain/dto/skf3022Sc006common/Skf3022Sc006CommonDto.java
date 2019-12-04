/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.dto.skf3022Sc006common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.skf.skf3022.domain.dto.common.Skf302210CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3022Sc006CommonDto　提示データ登録画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3022Sc006CommonDto extends Skf302210CommonDto {
	
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
	/** 備品コード */
	// 備品コード：下取り
	public static final String SHITADORI = "9";

	// 相互利用
	public static final String SOGORIRYOU_ARI = "1";

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
	// 社宅管理番号
	private String hdnShatakuKanriNo;
	// 社宅管理番号元?
	private String hdnShatakuKanriNoOld;
	// 部屋管理番号
	private String hdnRoomKanriNo;
	// 部屋管理番号元?
	private String hdnRoomKanriNoOld;
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
	private String hdnChushajoNoOne;
	// 駐車場区画番号元（区画2）
	private String hdnChushajoNoTwoOld;
	private String hdnChushajoNoTwo;
	// 継続登録フラグ
	private String hdnKeizokuBtnFlg;
	// 次月予約存在フラグ
	private Boolean hdnYoyakuFlg;
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
	private Boolean bihinItiranFlg;

	/** メッセージボックス */
	// スタイル
	private String sc006MsgBoxStyle;
	// メッセージ
	private String sc006Msg;

	/** 運用ガイド */
//	private String operationGuide

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
	// 一次保存ボタン押下時メッセージ
	private String litMessageTmpSave;
	// 社宅管理台帳登録
	private String litMessageShatakuLogin;
	// 継続登録ボタン押下時メッセージ
	private String litMessageKeizokuLogin;
	// 前に戻るボタン押下時メッセージ
	private String litMessageBack;
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
	// 入退居（ヘッダ項目）
	private String sc006NyutaikyoKbn;
	// 入退居(フォント色)
	private String sc006NyutaikyoKbnColor;
	// 社宅提示(ヘッダ項目)
	private String sc006ShatakuStts;
	// 社宅提示(フォント色)
	private String sc006ShatakuSttsColor;
	// 備品提示(ヘッダ項目)
	private String sc006BihinStts;
	// 備品提示(フォント色)
	private String sc006BihinSttsColor;
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
	// 搬出希望日
	private String sc006KibouDayOut;
	// 搬出本人連絡先
	private String sc006HonninAddrOut;
	// 搬出立会代理人名
	private String sc006TachiaiDairi;
	// 搬出立会代理人連絡先
	private String sc006TachiaiDairiAddr;
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


	/** 下部ボタン */
	// 運用ガイドボタン
	private Boolean btnUnyonGuideDisableFlg;
	// 一時保存ボタン
	private Boolean btnTmpSaveDisableFlg;
	// 作成完了ボタン
	private Boolean btnCreateDisableFlg;
	// 次月予約ボタン
	private Boolean btnJigetuYoyakuDisableFlg;
	// 台帳登録ボタン
	private Boolean btnShatakuLoginDisableFlg;
	// 継続登録ボタン
	private Boolean btnKeizokuLoginDisableFlg;

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
}

