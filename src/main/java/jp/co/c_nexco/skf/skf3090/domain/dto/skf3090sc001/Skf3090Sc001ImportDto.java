/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc001;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc001common.Skf3090Sc001CommonDto;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import lombok.EqualsAndHashCode;

/**
 * Skf3090Sc001 現物支給価額マスタ一覧DTO
 *BaseDto
 * @author NEXCOシステムズ
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Sc001ImportDto extends Skf3090Sc001CommonDto{

	private static final long serialVersionUID = -1902278406295003652L;
	

	/** 現物支給価額リスト */
	@JsonIgnore
	private MultipartFile listGenbutsuShikyuKagaku;	
	
}
