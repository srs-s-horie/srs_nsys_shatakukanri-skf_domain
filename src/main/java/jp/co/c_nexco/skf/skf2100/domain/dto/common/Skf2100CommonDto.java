/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.dto.common;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2100CommonDto Skf2100同期処理共通Dto
 * 
 * @author NEXCOシステムズ
 *
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2100CommonDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// 操作ガイド
	private String operationGuide;
	// 代行ログインフラグ
	private String alterLoginFlg;
	// 申請書類番号
	private String applNo;
	// 申請書類ID
	private String applId;
	// 申請状況
	private String applStatus;
	private String sendApplStatus;
	private String applStatusText;
	// 申請日時
	private Date applDate;
	// 申請書類履歴テーブル申請日
	private Date applHistroyApplDate;
	// 社員番号
	private String hdnShainNo;
	// 承認日/修正依頼日
	private Date agreDate;
	// 修正依頼済みフラグ
	private boolean revisionFlg = false;

	// 【所属】
	// 機関
	private String agency;
	// 部等
	private String affiliation1;
	// 室、チームまたは課
	private String affiliation2;
	// 勤務先のTEL
	private String tel;
	// 申請者社員番号
	private String shainNo;
	// 氏名
	private String name;
	// メールアドレス
	private String mailAddress;
	// ユーザID
	private String userId;
	
	// 原籍会社
	private String originalCompanyCd;
	private String originalCompany;
	// 給与支給会社
	private String payCompanyCd;
	private String payCompany;
	
	/*
	 * 添付資料
	 */
	// 添付資料情報
	private List<Map<String, Object>> attachedFileList; // 添付資料
	// 添付資料番号
	private String attachedNo;
	// 添付資料種別
	private String attachedType;
	// 添付書類有無
	private String applTacFlg;
	
	
	/**
	 * 戻るボタンの遷移先
	 */
	private String backUrl;
}
