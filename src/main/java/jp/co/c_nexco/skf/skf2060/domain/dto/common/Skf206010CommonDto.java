package jp.co.c_nexco.skf.skf2060.domain.dto.common;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * 借上候補物件提示機能の共通Dto
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf206010CommonDto extends BaseDto {
    private static final long serialVersionUID = 1L;

     /**
     * 借上候補物件提示_画面間受け渡しパラメータ
     */
    // 対象者名
    private String targetUserName;
    // 対象者CD
    private String targetUserCd;
}
