/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc009;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010Sc009common.Skf2010Sc009CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf2010_Sc009画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf2010Sc009InitDto extends Skf2010Sc009CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 申請書類番号
	private String applNo;
	// 申請書類ID
	private String applId;
	// 申請書類名
	private String applName;
	// 添付書類
	@JsonIgnore
	private MultipartFile attachedFile;
	// 添付書類リスト
	private List<Map<String, Object>> attachedFileList;

	// エラー表示
	private String errorAttachedFile;

}
