/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002;

import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * 入居希望等調書申請画面の申請内容を確認ボタン処理Dto。
 * 
 * @author NEXCOシステムズ
 */

@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2020Sc002CheckAsyncDto extends Skf2020Sc002CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 勤務先のTEL
	private String tel;

	/**
	 * 新たに社宅を必要としますか
	 */
	// 社宅貸与必要
	private String taiyoHituyo;

	/**
	 * 社宅を必要とする理由
	 */
	// 社宅を必要とする理由
	private String hitsuyoRiyu;

	/**
	 * 社宅を必要としない理由
	 */
	// 社宅を不必要とする理由
	private String fuhitsuyoRiyu;

	/**
	 * 必要とする社宅
	 */
	// 必要理由
	private String hitsuyoShataku;

	/**
	 * 新所属
	 */
	// 新機関 名称
	private String newAgency;
	// 新部等 名称
	private String newAffiliation1;
	// 新室、チームまたは課 名称
	private String newAffiliation2;

	// 新機関(フラグ)
	private String newAgencyOther;
	// 新部等（フラグ）
	private String newAffiliation1Other;
	// 新室、チームまたは課（フラグ)
	private String newAffiliation2Other;

	/**
	 * 同居家族
	 */
	// 続柄1
	private String dokyoRelation1;
	// 氏名1
	private String dokyoName1;
	// 年齢1
	private String dokyoAge1;

	// 続柄2
	private String dokyoRelation2;
	// 氏名2
	private String dokyoName2;
	// 年齢2
	private String dokyoAge2;

	// 続柄3
	private String dokyoRelation3;
	// 氏名3
	private String dokyoName3;
	// 年齢3
	private String dokyoAge3;

	// 続柄4
	private String dokyoRelation4;
	// 氏名4
	private String dokyoName4;
	// 年齢4
	private String dokyoAge4;

	// 続柄5
	private String dokyoRelation5;
	// 氏名5
	private String dokyoName5;
	// 年齢5
	private String dokyoAge5;

	// 続柄6
	private String dokyoRelation6;
	// 氏名6
	private String dokyoName6;
	// 年齢6
	private String dokyoAge6;

	/**
	 * 入居希望日（予定日）
	 */
	// 入居希望日（予定日）
	private String nyukyoYoteiDate;

	/**
	 * 自動車の保管場所
	 */
	/**
	 * 自動車1台目
	 */
	// 自動車の車名
	private String carName;
	// 自動車の登録番号
	private String carNo;
	// 自動車の有効期間満了日
	private String carExpirationDate;
	// 自動車の利用者
	private String carUser;
	// 自動車の保管場所使用開始日
	private String parkingUseDate;

	/**
	 * 自動車2台目
	 */
	// 自動車の車名
	private String carName2;
	// 自動車の登録番号
	private String carNo2;
	// 自動車の有効期間満了日
	private String carExpirationDate2;
	// 自動車の利用者
	private String carUser2;
	// 自動車の保管場所使用開始日
	private String parkingUseDate2;

	/**
	 * 特殊事情など
	 */
	// 特殊事情など
	private String tokushuJijo;

	/**
	 * 現保有の社宅（退居予定）
	 */
	// 退居する
	private String taikyoYotei;

	/**
	 * 退居予定日
	 */
	// 退居予定日
	private String taikyoYoteiDate;

	/**
	 * 社宅の状態
	 */
	// 社宅の状態
	private String shatakuJotai;

	/**
	 * 退居理由
	 */
	// 退居理由
	private String taikyoRiyu;
	// 退居理由区分
	private String taikyoRiyuKbn;

	/**
	 * 退居後の連絡先
	 */
	// 退居後の連絡先
	private String taikyogoRenrakuSaki;

	/**
	 * 返却立会希望日
	 */
	// 返却立会希望日
	private String sessionDay;
	// 返却立会希望時間
	private String sessionTime;

	/**
	 * 連絡先
	 */
	// 連絡先
	private String renrakuSaki;

	// 駐車場1台目
	private String hdnParking1stPlace;
	// 駐車場2台目
	private String hdnParking2stPlace;

	/**
	 * エラーフラグ
	 */
	// 更新不可フラグ
	private String updateErrorFlg;

}
