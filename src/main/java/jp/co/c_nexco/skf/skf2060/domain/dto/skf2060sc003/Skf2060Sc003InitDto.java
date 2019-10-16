/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc003;

import java.util.*;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060Sc003common.Skf2060Sc003CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2060_Sc003画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2060Sc003InitDto extends Skf2060Sc003CommonDto {
	
	private static final long serialVersionUID = -1902278406295003652L;
	
	//機関
	private String agency;
	//部等
	private String affiliation1;
	//室、チームまたは課
	private String affiliation2;
	//勤務先のTEL
	private String tel;
	//社員番号
	private String shainNo;
	//氏名
	private String name;
	//等級
	private String tokyu;
	//性別
	private String gender;
	
    // 操作ガイド
    private String operationGuide;
	//コメントボタン表示切替用
	private boolean commentViewFlag;
	//「完了」ボタン、「再提示」ボタン表示切替用
	private boolean buttonViewFlag;
    
    //隠し要素
    //選択物件番号
    private String checkCandidateNo;
    

    

}
