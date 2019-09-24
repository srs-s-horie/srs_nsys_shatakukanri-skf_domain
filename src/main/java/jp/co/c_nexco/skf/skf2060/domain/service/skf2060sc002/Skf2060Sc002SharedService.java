/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageTeijiForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageTeijiForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiFile;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageTeijiForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiDetailRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;

/**
 * Skf2060Sc002SharedService 借上候補物件確認内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2060Sc002SharedService {


	@Autowired
	private Skf2060Sc001GetKariageBukkenExpRepository skf2060Sc001GetKariageBukkenExpRepository;
	@Autowired
	private Skf2060Sc001GetKariageBukkenFileExpRepository skf2060Sc001GetKariageBukkenFileExpRepository;
	@Autowired
	private Skf2060Sc001GetApplHistoryExpRepository skf2060Sc001GetApplHistoryExpRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2060TKariageTeijiRepository skf2060TKariageTeijiRepository;
	@Autowired
	private Skf2060TKariageTeijiDetailRepository skf2060TKariageTeijiDetailRepository;
	@Autowired
	private Skf2060TKariageTeijiFileRepository skf2060TKariageTeijiFileRepository;
	@Autowired
	private Skf2060Sc001GetApplHistoryInfoForUpdateExpRepository skf2060Sc001GetApplHistoryInfoForUpdateExpRepository;	
	@Autowired
	private Skf2060Sc001GetKariageTeijiForUpdateExpRepository skf2060Sc001GetKariageTeijiForUpdateExpRepository;	
	@Autowired
	private Skf2060Sc002GetKariageTeijiInfoExpRepository skf2060Sc002GetKariageTeijiInfoExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	
	public static List<Skf2060Sc001GetKariageBukkenExp> dataParam;
	public static List<Skf2060Sc001GetKariageBukkenFileExp> dataParam2;
	
	private String companyCd = CodeConstant.C001;

	

	
}
