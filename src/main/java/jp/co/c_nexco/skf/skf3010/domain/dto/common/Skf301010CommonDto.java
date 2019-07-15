package jp.co.c_nexco.skf.skf3010.domain.dto.common;

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
public class Skf301010CommonDto extends BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 会社コード
	private String selectedCompanyCd;
	// 機関コード
	private String agencyCd;
	// 社宅区分
	private String shatakuKbn;
	// 空き部屋
	private String emptyRoom;
	// 利用区分
	private String useKbn;
	// 空き駐車場
	private String emptyParking;
	// 社宅名
	private String shatakuName;
	// 社宅住所
	private String shatakuAddress;
	// 外部機関表示フラグ
	private Boolean agencyDispFlg;

	// 管理会社リスト
	List<Map<String, Object>> manageCompanyList;
	// 社宅区分リスト
	List<Map<String, Object>> shatakuKbnList;
	// 空き部屋リスト
	List<Map<String, Object>> emptyRoomList;
	// 管理機関リスト
	List<Map<String, Object>> manageAgencyList;
	// 利用区分リスト
	List<Map<String, Object>> useKbnList;
	// 空き駐車場リスト
	List<Map<String, Object>> emptyParkingList;
}
