package jp.co.c_nexco.skf.skf3022.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf302210CommonDto 提示データ共通DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf302210CommonDto extends BaseDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//提示データ一覧画面連携項目
	//提示番号
	private String hdnTeijiNo;
	//入居予定日
	private String hdnNyukyoDate;
	//退居予定日
	private String hdnTaikyoDate;
	//申請書類管理番号
	private String hdnShoruikanriNo;
	//入退居区分
	private String hdnNyutaikyoKbn;
	//申請区分
	private String hdnApplKbn;
	//社員番号変更フラグ
	private String hdnShainNoChangeFlg;

	//提示データ一覧検索条件退避
	//社員番号
	private String searchInfoShainNo;
	//社員氏名
	private String searchInfoShainName;
	//社宅名
	private String searchInfoShatakuName;
	//入退居区分
	private String searchInfoNyutaikyoKbn;
	//社宅提示状況
	private String searchInfoStJyokyo;
	//社宅提示確認督促
	private String searchInfoStKakunin;
	//備品提示状況
	private String searchInfoBhJyokyo;
	//備品提示確認督促
	private String searchInfoBhKakunin;
	//備品搬入搬出督促
	private String searchInfoMoveInout;
}
