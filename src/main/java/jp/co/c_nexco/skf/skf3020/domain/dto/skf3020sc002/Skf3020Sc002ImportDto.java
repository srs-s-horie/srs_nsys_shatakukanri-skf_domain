/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc002;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3020Sc002ImportDto 取り込み転任者調書情報DTO
 *
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3020Sc002ImportDto extends BaseDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	/** 転任者調書 */
	@JsonIgnore
	private MultipartFile fuTenninsha;
	
}
