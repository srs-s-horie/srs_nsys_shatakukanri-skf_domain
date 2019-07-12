package jp.co.c_nexco.skf.skf3090.domain.dto.common;

import java.util.List;
import java.util.Map;

import jp.co.c_nexco.nfw.webcore.app.DownloadFile;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf309020CommonDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** エラーコード関係 */
	private String nameErr;
	private String nameKkErr;
	private String shainNoErr;
	private String companyCdErr;

	

	private List<DownloadFile> dlFile;
	
	private String preOpenPage;

	private Integer limit;
	private Integer offset;
	private Integer page;
}
