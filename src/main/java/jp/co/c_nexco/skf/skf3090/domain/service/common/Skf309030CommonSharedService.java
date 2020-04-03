/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.common;

import org.springframework.stereotype.Service;

/**
 * Skf309030CommonSharedService Skf309030共通クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf309030CommonSharedService {

	// 一覧画面遷移時の更新フラグ
	public static final String UPDATE_FLAG_NEW = "0";
	public static final String UPDATE_FLAG_UPDATE = "1";
	
	// 検索フラグ
	public static final String SEARCH_FLAG_DO_NOT = "0";
	public static final String SEARCH_FLAG_DO = "1";

}
