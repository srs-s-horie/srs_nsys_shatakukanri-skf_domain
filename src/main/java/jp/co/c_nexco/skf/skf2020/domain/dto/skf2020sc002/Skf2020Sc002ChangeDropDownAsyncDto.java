package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc002ChangeDropDownAsyncDto extends Skf2020Sc002CommonAsyncDto {

	/**
	 * シリアルナンバー
	 */
	private static final long serialVersionUID = 1L;

	// 新機関
	private String newAgencyErr;

	/**
	 * ドロップダウン
	 */
	/** ドロップダウン：機関リスト */
	private List<Map<String, Object>> ddlAgencyList;
	/** ドロップダウン：部等リスト */
	private List<Map<String, Object>> ddlAffiliation1List;
	/** ドロップダウン：室、チーム又は課リスト */
	private List<Map<String, Object>> ddlAffiliation2List;
	/** ドロップダウン：退居理由リスト */
	private List<Map<String, Object>> ddlTaikyoRiyuKbnList;
	/** ドロップダウン：返却立会希望日(時)リスト */
	private List<Map<String, Object>> ddlReturnWitnessRequestDateList;
	/** ドロップダウン：現居住社宅名リスト */
	private List<Map<String, Object>> ddlNowShatakuNameList;
	private List<Skf2020Sc002GetShatakuInfoExp> shatakuList;

	/**
	 * 申請者
	 */
	// 社員番号
	private String shainNo;

	/**
	 * 表示
	 */
	// // 退居届を促すメッセージ
	// private String lblShatakuFuyouMsgRemove;
	//
	// /**
	// * 活性非活性
	// */
	// // 社宅を必要としますか？ 駐車場のみ
	// private String rdoParkingOnlyDisabled;
	//
	// // 既婚
	// private String rdoKikonDisabled;
	// // 必要とする社宅 世帯
	// private String rdoHitsuyoSetaiDisabled;
	// // 必要とする社宅 単身
	// private String rdoHitsuyoTanshinDisabled;
	// // 必要とする社宅 独身
	// private String rdoHitsuyoDokushinDisabled;
	//
	// // 現居住宅 保有(会社借上を含む)
	// private String rdoNowJutakuHoyuDisabled;
	// // 現居住宅 自宅
	// private String rdoNowJutakuJitakuDisabeld;
	// // 現居住宅 自己借上
	// private String rdoNowJutakuKariageDisabled;
	// // 現居住宅 保有(会社借上を含む)
	// private String rdoNowJutakuSonotaDisabled;
	//
	// // 返却立会希望日
	// private String sessionTimeDisabled;
	// private String sessionDayDisabled;
	// // 連絡先
	// private String renrakuSakiDisabled;
	//
	// // 入居希望日カレンダー
	// private String nyukyoYoteiDateClDisabled;
	// // 自動社の有効期間満了日 1台目カレンダー
	// private String carExpirationDateClDisabled;
	// // 自動車の使用開始日 1台目カレンダー
	// private String parkingUseDateClDisabled;
	// // 自動社の有効期間満了日 2台目カレンダー
	// private String carExpirationDate2ClDisabled;
	// // 自動車の使用開始日 2台目カレンダー
	// private String parkingUseDate2ClDisabled;
	// // 退居予定日 2台目カレンダー
	// private String taikyoYoteiDateClDisabled;
	// // 返却予定日カレンダー
	// private String sessionDayClDisabled;
	//
	// // 退居情報エリア表示フラグ（true:表示、false:非表示）
	// private String taikyoViewFlag;

	/**
	 * Hidden
	 */

	// 社宅管理部屋番号
	private long hdnShatakuRoomKanriNo;
	// 社宅管理番号
	private long hdnShatakuKanriNo;
	// 現居住社宅部屋管理番号
	private long hdnNowShatakuRoomKanriNo;
	// 現居住社宅管理番号
	private long hdnNowShatakuKanriNo;
	// 規格(間取り)
	private String hdnShatakuKikakuKbn;

	// 現在の位置番号
	private String hdnParking1stNumber;
	// 現在の保管場所
	private String hdnParking1stPlace;
	// 現在の位置番号2
	private String hdnParking2stNumber;
	// 現在の保管場所2
	private String hdnParking2stPlace;
	// 選択された社宅名
	private String hdnSelectedNowShatakuName;

}
