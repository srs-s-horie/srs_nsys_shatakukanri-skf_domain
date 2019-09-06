/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.app.DownloadFile;
import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonAsyncDto;
import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001CommonAsyncDto extends Skf206010CommonAsyncDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//提示対象者名
	private String presentedName;
	//提示状況
	private String presentedStatus;
	//提示日
	private String presentedDate;
	//借上社宅名
	private String shatakuName;
	//郵便番号
	private String postalCd;
	//住所
	private String address;
	//コメント
	private String comment;
	
	// リストテーブルデータ
	private List<Map<String, Object>> listTableData;
	//　リストテーブルチェックボックス要素
	private String[] teijiVal;
	
	//支援ボタン制御
	private String supportDisabled;
	
	//コメントボタン表示切替用
	private boolean commentViewFlag;
	
	/** 借上候補物件登録画面hidden項目連携用 */
	//提示対象者番号
	private String shainNo;
	//更新日時
	private String updateDate;
	//申請書類管理番号
	private String applNo;
	// 会社コード
	private String hdnCompanyCd;
	// 借上候補物件番号
	private String hdnCandidateNo;
	//添付ファイル番号
	private String hdnAttachedNo;
	
	//申請書類ID
	private String applId;
	
	//添付ファイル用
	private List<DownloadFile> dlFile;
	
}
