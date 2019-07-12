/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.dto.skf3090test03popupaddfile;

import java.util.*;

import lombok.EqualsAndHashCode;

import jp.co.c_nexco.nfw.webcore.app.DownloadFile;
import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090test03.Skf3090Test03CommonDto;


/**
 * TestPrjTop画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf3090Test03PopupAddFileInitDto extends Skf3090Test03CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;
	
	private String preOpenPagePopup;
	private String shainNoPopup;
	
	//private List<DownloadFile> dlFile;
}