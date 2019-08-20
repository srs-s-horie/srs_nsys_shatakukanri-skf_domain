package jp.co.c_nexco.skf.skf2010.domain.dto.common;

import java.util.List;
import java.util.Map;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010CommonDto extends BaseDto {

	private static final long serialVersionUID = 1L;

	/** 基本データ **/
	private String applNo; // 申請番号
	private String applId; // 申請ID
	private String applStatus; // 申請状況
	private String applStatusText; // 申請状況（テキスト）
	private String applUpdateDate;

	/** 画面表示用 **/
	private int displayLevel; // 表示レベル（１～３）
	// アコーディオン初期表示用フラグ
	private String level1Open;
	private String level2Open;
	private String level3Open;
	private String level4Open;

	/** 入居希望申請調書 **/
	// 社宅必要可否
	private String taiyoHitsuyoTrue; // 必要
	private String taiyoHitsuyoFalse; // 不要
	private String taiyoHitsuyoParking; // 駐車場のみ

	// 社宅必要理由
	private String hitsuyoRiyuIdou; // 異動のため
	private String hitsuyoRiyuMarry; // 結婚のため
	private String hitsuyoRiyuOther; // その他

	// 社宅不必要理由
	private String fuhitsuyoRiyuMyHome; // 自宅通勤
	private String fuhitsuyoRiyuSelfRental; // 自己借上
	private String fuhitsuyoRiyuOther; // その他

	// 社員番号
	private String shainNo;
	// 氏名
	private String name;
	// 等級
	private String tokyu;
	// 所属
	// 現所属：機関
	private String nowAgency;
	// 現所属：部等
	private String nowAffiliation1;
	// 現所属：室、チーム又は課
	private String nowAffiliation2;
	// 現所属：勤務先のTEL
	private String nowTel;
	// 新所属：機関
	private String newAgency;
	// 新所属：部等
	private String newAffiliation1;
	// 新所属：室、チーム又は課
	private String newAffiliation2;
	// 必要とする社宅：世帯
	private String hitsuyouShatakuFamily;
	// 必要とする社宅：単身
	private String hitsuyouShatakuOnly;
	// 必要とする社宅：独身
	private String hitsuyouShatakuSingle;
	// 同居家族続柄１
	private String dokyoRelation1;
	// 同居家族氏名１
	private String dokyoName1;
	// 同居家族年齢１
	private String dokyoAge1;
	// 同居家族続柄2
	private String dokyoRelation2;
	// 同居家族氏名2
	private String dokyoName2;
	// 同居家族年齢2
	private String dokyoAge2;
	// 同居家族続柄3
	private String dokyoRelation3;
	// 同居家族氏名3
	private String dokyoName3;
	// 同居家族年齢3
	private String dokyoAge3;
	// 同居家族続柄4
	private String dokyoRelation4;
	// 同居家族氏名4
	private String dokyoName4;
	// 同居家族年齢4
	private String dokyoAge4;
	// 同居家族続柄5
	private String dokyoRelation5;
	// 同居家族氏名5
	private String dokyoName5;
	// 同居家族年齢5
	private String dokyoAge5;
	// 同居家族続柄6
	private String dokyoRelation6;
	// 同居家族氏名6
	private String dokyoName6;
	// 同居家族年齢6
	private String dokyoAge6;
	// 入居予定日
	private String nyukyoYoteiDate;
	// 保管場所：必要
	private String parkingUmuTrue;
	// 保管場所：不要
	private String parkingUmuFalse;
	// 自動車登録番号入力フラグ
	private String carNoInputFlg;
	// 自動車の車名
	private String carName;
	// 自動車の登録番号
	private String carNo;
	// 自動車の使用者
	private String carUser;
	// 車検の有効期間満了日
	private String carExpirationDate;
	// 自動車の保管場所使用開始日
	private String parkingUserDate;
	// 自動車登録番号入力フラグ2
	private String carNoInputFlg2;
	// 自動車の車名2
	private String carName2;
	// 自動車の登録番号2
	private String carNo2;
	// 自動車の使用者2
	private String carUser2;
	// 車検の有効期間満了日2
	private String carExpirationDate2;
	// 自動車の保管場所使用開始日2
	private String parkingUserDate2;
	// 入居社宅：保有
	private String nowShatakuPossession;
	// 入居社宅：自宅
	private String nowShatakuMyHome;
	// 入居社宅：自己借上
	private String nowShatakuSelfRental;
	// 入居社宅：その他
	private String nowShatakuOther;
	// 保有社宅名
	private String nowShatakuName;
	// 保有社宅号室
	private String nowShatakuNo;
	// 保有社宅規格
	private String nowShatakuKikaku;
	// 保有社宅規格名称
	private String nowShatakuKikakuName;
	// 保有社宅面積
	private String nowShatakuMenseki;
	// 現入居社宅：退居する
	private String taikyoYoteiTrue;
	// 現入居社宅：継続使用する
	private String taikyoYoteiFalse;
	// 保有社宅退居予定日
	private String taikyoYoteiDate;
	// 特殊事情
	private String tokushuJijo;
	// 備品希望
	private String bihinKibo;

	/** 貸与（予定）社宅等のご案内 **/
	// 案内日
	private String tsuchiDate;
	// 制約日
	private String seiyakuDate;
	// 機関（nowAgencyを使用）
	// 部等（nowAffiliation1を使用）
	// 室、チーム又は課（nowAffiliation2を使用）
	// 氏名（name使用）
	// 申請日
	private String applDate;
	// 社宅を必要とする理由（テキスト）
	private String hitsuyoRiyu;
	// 社宅所在地
	private String newShozaichi;
	// 社宅名
	private String newShatakuName;
	// 室番号
	private String newShatakuNo;
	// 規格
	private String newShatakuKikaku;
	// 面積
	private String newShatakuMenseki;
	// 使用料
	private String newRental;
	// 共益費
	private String newKyoekihi;
	// 同居家族関連（入居希望申請を使用）
	// 自動車の保管場所
	private String parkingArea;
	// 自動車の車名（carName使用）
	// 自動車の位置番号
	private String carIchiNo;
	// 自動車の登録番号（carNo使用）
	// 車検の有効期間満了日（carExpirationDate使用）
	// 保管場所使用料
	private String parkingRental;
	// 自動車の使用者（carUser使用）
	// 自動車の保管場所2
	private String parkingArea2;
	// 自動車の車名2（carName2使用）
	// 自動車の位置番号2
	private String carIchiNo2;
	// 自動車の登録番号2（carNo2使用）
	// 車検の有効期間満了日2（carExpirationDate2使用）
	// 保管場所使用料2
	private String parkingRental2;
	// 自動車の使用者（carUser2使用）
	// 入居予定日（nyukyoYoteiDate使用）
	// 自動車保管場所（１台目）の使用開始予定日
	private String parkingKanoDate;
	// 自動車保管場所（2台目）の使用開始予定日
	private String parkingKanoDate2;

	/** 退居（自動車の保管場所返還）届 **/
	// 現住所
	private String address;
	// 社宅名
	private String shatakuName;
	// 駐車場１
	private String parkingAddress1;
	// 駐車場2
	private String parkingAddress2;
	// 退居日 社宅等
	private String taikyoDate;
	// 退居日 駐車場
	private String parkingHenkanDate;
	// 退居（返還）理由（区分）
	private String taikyoRiyu;
	// 退居後の連絡先
	private String taikyogoRenrakusaki;
	// コメント
	private String commentNote;

	// 添付資料情報
	private List<Map<String, Object>> attachedFileList;
	// 添付資料番号
	private String attachedNo;

	private String backUrl;
}
