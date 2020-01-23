/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc009common;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf201060CommonAsyncDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009CommonAsyncDto extends Skf201060CommonAsyncDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請書類番号
	private String popApplNo;
	// 申請書類ID
	private String popApplId;
	// 申請書類名
	private String popApplName;
	// 借上候補物件番号
	private String popCandidateNo;

	// 添付書類
	private MultipartFile attachedFile;
	// 添付書類リスト
	private List<Map<String, Object>> popAttachedFileList;

	// エラー表示
	private String errorAttachedFile;
}
