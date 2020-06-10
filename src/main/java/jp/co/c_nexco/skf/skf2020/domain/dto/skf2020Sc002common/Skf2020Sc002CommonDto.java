/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common;

import java.util.Date;
import java.util.List;
import java.util.Map;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.skf.skf2020.domain.dto.common.Skf202010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * 入居希望等調書申請画面の共通Dto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc002CommonDto extends Skf202010CommonDto {

	private static final long serialVersionUID = 1L;

	private List<Skf2020Sc002GetShainInfoExp> getShainInfo;

	/**
	 * ドロップダウン
	 */
	/** ドロップダウン：機関リスト */
	private List<Map<String, Object>> ddlAgencyList;
	/** ドロップダウン：部等リスト */
	private List<Map<String, Object>> ddlAffiliation1List;
	/** ドロップダウン：室・課等リスト */
	private List<Map<String, Object>> ddlAffiliation2List;
	/** ドロップダウン：退居理由リスト */
	private List<Map<String, Object>> ddlTaikyoRiyuKbnList;
	/** ドロップダウン：返却立会希望日(時)リスト */
	private List<Map<String, Object>> ddlReturnWitnessRequestDateList;
	/** ドロップダウン：現居住社宅名リスト */
	private List<Map<String, Object>> ddlNowShatakuNameList;

	/**
	 * 所属値設定用
	 */
	// 機関
	private String agency;
	// 部等コード
	private String affiliation1;
	// 室、チームまたは課コード
	private String affiliation2;

	/**
	 * リストテーブル
	 */
	// 検索結果一覧
	private List<Map<String, Object>> ltResultList;
	// 社員情報のリスト
	// private List<Skf2020Sc002GetShainInfoExp> shainList;
	private List<Map<String, String>> shainList;
	// 現在居住社宅のリスト
	private List<Skf2020Sc002GetShatakuInfoExp> shatakuList;
	// 機関リスト
	private List<Map<String, Object>> agencyList;
	// 部等リスト
	private List<Map<String, Object>> affiliation1List;
	// 室・課等リスト
	private List<Map<String, Object>> affiliation2List;
	// 備品返却リスト
	List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList;
	// 備品表示用
	private List<Map<String, Object>> bihinItemList;

	/**
	 * Hidden
	 */

	// 選択された社宅名
	private String hdnSelectedNowShatakuName;
	// 現在の位置番号
	private String hdnParking1stNumber;
	// 現在の位置番号2
	private String hdnParking2stNumber;
	// 現居住社宅管理番号
	private long hdnNowShatakuKanriNo;
	// 現居住社宅部屋管理番号
	private long hdnNowShatakuRoomKanriNo;
	// 備品返却有無
	private String hdnBihinHenkyakuUmu;

	// 可否フラグ
	private String hdnConfirmFlg;
	// ステータス
	// private String hdnStatus;

	/**
	 * ラジオボタンチェック状態判定用
	 */
	private String rdoHitsuyoChecked;
	// ラジオボタン社宅を必要としますか？ 不要
	private String rdoFuyouChecked;
	// ラジオボタン 社宅を必要としますか？ 駐車場のみ
	private String rdoParkingOnlyChecked;

	// ラジオボタン 社宅を必要とする理由 異動のため
	private String rdoHitsuyoIdoChecked;
	// ラジオボタン 社宅を必要とする理由 結婚のため
	private String rdoHitsuyoKekkonChecked;
	// ラジオボタン 社宅を必要とする理由その他
	private String rdoHitsuyoSonotaChecked;

	// ラジオボタン 社宅を必要としない理由 自宅通勤
	private String rdoFuyouJitakutsuukinnChecked;
	// ラジオボタン 社宅を必要としない理由 自己借上
	private String rdoFuyouJikokariageChecked;
	// ラジオボタン 社宅を必要としない理由 その他
	private String rdoFuyouSonotaChecked;

	// ラジオボタン必要とする社宅 既婚
	private String rdoKikonChecked;
	// ラジオボタン必要とする社宅 世帯
	private String rdoHitsuyoSetaiChecked;
	// ラジオボタン必要とする社宅 単身
	private String rdoHitsuyoTanshinChecked;
	// ラジオボタン必要とする社宅 独身
	private String rdoHitsuyoDokushinChecked;

	// ラジオボタン自動車の保管場所 必要とする
	private String rdoCarHitsuyoChecked;
	// ラジオボタン自動車の保管場所 必要としない
	private String rdoCarFuyoChecked;

	// 1台目
	// ラジオボタン自動車の保有 保有している
	private String rdo1stCarHoyuChecked;
	// ラジオボタン自動車の保有 購入を予定している
	private String rdo1stCarYoteiChecked;
	// 2台目
	// ラジオボタン自動車の保有 保有している
	private String rdo2stCarHoyuChecked;
	// ラジオボタン自動車の保有 購入を予定している
	private String rdo2stCarYoteiChecked;

	// ラジオボタン現住居情報 保有(会社借上を含む)
	private String rdoNowJutakuHoyuChecked;
	// ラジオボタン現住居情報 自宅
	private String rdoNowJutakuJitakuChecked;
	// ラジオボタン現住居情報 保有(会社借上を含む)
	private String rdoNowJutakuKariageChecked;
	// ラジオボタン現住居情報 自宅
	private String rdoNowJutakuSonotaChecked;
	// ラジオボタン退居理由 退居する
	private String rdoNowHoyuShatakuTaikyoChecked;
	// ラジオボタン退居理由 継続使用する
	private String rdoNowHoyuShatakuKeizokuChecked;

	/**
	 * 表示
	 */
	// 退居届を促すメッセージ
	private String lblShatakuFuyouMsgRemove;

	/**
	 * 活性非活性
	 */
	// 社宅を必要としますか？ 駐車場のみ
	private String rdoParkingOnlyDisabled;

	// 必要とする理由
	private String rdoHitsuyoIdoDisabled;
	private String rdoHitsuyoKekkonDisabled;
	private String rdoHitsuyoSonotaDisabled;

	// 必要としない理由
	private String rdoFuyouJitakuTsuukinnDisabled;
	private String rdoFuyouJikoKariageDisabled;
	private String rdoFuyouSonotaDisabled;

	// 新所属
	private String newAffiliation1OtherDisabled;
	private String newAffiliation2OtherDisabled;

	// 既婚
	private String rdoKikonDisabled;
	// 必要とする社宅 世帯
	private String rdoHitsuyoSetaiDisabled;
	// 必要とする社宅 単身
	private String rdoHitsuyoTanshinDisabled;
	// 必要とする社宅 独身
	private String rdoHitsuyoDokushinDisabled;

	// 駐車場を必要とするか？
	private String rdoCarHitsuyoDisabled;
	private String rdoCarFuyoDisabled;

	// 自動車の保有（1台目）
	private String rdo1stCarHoyuDisabled;
	private String rdo1stCarYoteiDisabled;

	// 自動車の保有（2台目）
	private String rdo2stCarHoyuDisabled;
	private String rdo2stCarYoteiDisabled;

	// 現居住宅 保有(会社借上を含む)
	private String rdoNowJutakuHoyuDisabled;
	// 現居住宅 自宅
	private String rdoNowJutakuJitakuDisabeld;
	// 現居住宅 自己借上
	private String rdoNowJutakuKariageDisabled;
	// 現居住宅 保有(会社借上を含む)
	private String rdoNowJutakuSonotaDisabled;

	// 現保有の社宅（退居予定）
	private String rdoNowHoyuShatakuTaikyoDisabled;
	private String rdoNowHoyuShatakuKeizokuDisabled;

	// 返却立会希望日
	private String sessionTimeDisabled;
	private String sessionDayDisabled;
	// 連絡先
	private String renrakuSakiDisabled;

	// 申請内容を確認ボタン
	private String btnCheckDisabled;
	// 一時保存ボタン
	private String btnSaveDisabeld;

	/**
	 * 申請情報
	 */

	// 申請書類履歴テーブル受け取り用申請日
	private Date applDate;
	// 申請書類履歴テーブル申請日
	private Date applHistroyApplDate;
	// 入居希望等調書テーブル申請日
	private String nyukyoApplDate;
	// 備品返却申請テーブル申請日
	private String bihinHenkyakuApplDate;
	// 申請書番号
	private String applNo;
	// 申請ID
	private String applId;
	// 添付書類有無フラグ
	private Integer applTacFlg;
	// ステータス
	private String applStatus;

	/**
	 * 添付ファイル作業
	 */
	// 添付ファイル名
	private String attachedName;
	// 添付ファイル番号
	private String attachedNo;
	// ファイルサイズ
	private String fileSize;
	// ファイル
	private byte fileStram;

	/**
	 * 退居届作成
	 */
	// 退居届フラグ
	private String bihinHenkaykuShinseiApplNo;
	// 退居申請書番号
	private String taikyoApplNo;

	/**
	 * その他
	 */
	// 年月
	// private String yearMonth;
	// 日付
	private String yearMonthDay;
	// 退居情報エリア表示フラグ（true:表示、false:非表示）
	private String taikyoViewFlag;
	// コメントボタン表示フラグ（true:表示、false:非表示）
	private String commentViewFlag;
	// 更新フラグ
	private String updateFlg;

}
