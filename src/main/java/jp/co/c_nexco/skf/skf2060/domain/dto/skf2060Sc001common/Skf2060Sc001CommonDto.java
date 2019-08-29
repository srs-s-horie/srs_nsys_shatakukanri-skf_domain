/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc001common;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.app.DownloadFile;
import jp.co.c_nexco.skf.skf2060.domain.dto.common.Skf206010CommonDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc001CommonDto extends Skf206010CommonDto {
	
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
	
	/** 借上候補物件登録画面hidden項目連携用 */
	//提示対象者番号
	private String presentedNo;
	//更新日時
	private String updateDate;
	
	//添付ファイル用
	private List<DownloadFile> dlFile;
	
}
