/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.dto.skf3021Sc002common;

import java.util.*;

import jp.co.c_nexco.nfw.webcore.domain.model.GridRelationDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3021Sc002CommonDto 入退居申請照会画面の共通Dto。
 * 
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3021Sc002CommonDto extends GridRelationDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	/**
	 * 表示
	 */
	//社員番号
	private String shainNo;
	//社員名
	private String shainName;
	//入退居区分
	private String nyutaikyoKbn;
	//等級
	private String tokyu;
	//年齢
	private String nenrei;
	//社宅の必要・不要
	private String hitsuyofuyo;
	//社宅を必要とする理由
	private String hitsuyoriyu;
	//社宅の不要理由
	private String fuyoriyu;
	//必要とする社宅
	private String hitsuyoShataku;
	//入居希望日（予定日）
	private String nyukyoKibobi;
	//自動車の保管場所
	private String jidoshaHokan;
	//退居（返還）する社宅又は駐車場
	private String taikyoShatakuOrChushajo;
	//現居住宅
	private String genkyoJutaku;
	//社宅名
	private String shatakuName;
	//部屋番号
	private String roomNo;
	//規格（間取り）
	private String kikaku;
	//用途
	private String yoto;
	//面積
	private String menseki;
	//使用状況
	private String shiyojokyo;
	//退居予定日
	private String taikyoYoteibi;
	//退居理由
	private String taikyoRiyu;
	//退居後連絡先
	private String taikyogoRenrakusaki;
	//特殊事情
	private String tokushujijo;
	
	//同居家族リスト
	private List<Map<String, Object>> dokyoKazokuIchiranList;
	//駐車場リスト
	private List<Map<String, Object>> jidoshaIchiranList;

	//親画面パラメータ
	//社員番号
	private String hdnRowShainNo;
	//申請書類管理番号
	private String hdnRowApplNo;
	//入退居区分
	private String hdnRowNyutaikyoKbn;
	
}
