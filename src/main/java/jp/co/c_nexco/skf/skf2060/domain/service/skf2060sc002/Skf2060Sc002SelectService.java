/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageTeijiForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageTeijiForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiDetailRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc002.Skf2060Sc002SelectDto;

import static jp.co.c_nexco.nfw.core.constants.CommonConstant.NFW_DATA_UPLOAD_FILE_DOWNLOAD_COMPONENT_PATH;

/**
 * TestPrjTop画面のSelectサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc002SelectService extends BaseServiceAbstract<Skf2060Sc002SelectDto> {
	
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2060TKariageTeijiDetailRepository skf2060TKariageTeijiDetailRepository;
	@Autowired
	private Skf2060TKariageTeijiRepository skf2060TKariageTeijiRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2060Sc002SharedService skf2060Sc002SharedService;
	@Autowired
	private Skf2060Sc002GetApplHistoryInfoForUpdateExpRepository skf2060Sc002GetApplHistoryInfoForUpdateExpRepository;
	// 会社コード
	private String companyCd = CodeConstant.C001;
	
	//applCheckFlg用
	private String SELECT = "1";
	private String NOSELECT = "0";
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param selectDto　DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc002SelectDto index(Skf2060Sc002SelectDto selectDto) throws Exception {
		
		selectDto.setPageTitleKey(MessageIdConstant.SKF2060_SC002_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("選択", companyCd, selectDto.getPageId());
		
		//入力チェックを行う
		//いずれの物件の「選択内容」ラジオボタンも選択されていない場合
		this.inputCheck(selectDto);
		
		String candidateNo = selectDto.getRadioCandidateNo();
		String applNo = selectDto.getApplNo();
		String applStatus = CodeConstant.NONE;
		String teijiKaisu = selectDto.getTeijiKaisu();
		String riyu = null;
		String biko = null;
		
		//申請状況を設定する
		//「選択しない」ラベルが表示されている行が選択されている場合,申請状況を"選択しない"に設定
		if(candidateNo.equals("0")){    //applCgeckFlgとは関係ない
			applStatus = CodeConstant.STATUS_SENTAKU_SHINAI;
			riyu = selectDto.getRiyuDropdown();
			biko = selectDto.getBiko();
		//「選択しない」ラベル以外が表示されている行が選択されている場合,申請状況を"選択済み"に設定
		}else{
			applStatus = CodeConstant.STATUS_SENTAKU_ZUMI;
		}
		
		//TODO 先に申請書類情報を持ってくる必要がある？？？
		Skf2060Sc002GetApplHistoryInfoForUpdateExp applHistoryData = this.getApplHistoryInfoForUpdate(companyCd, applNo);
		if(applHistoryData == null){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
		}
		
		//申請書類履歴テーブルを更新する（怪しい）
		Map<String, String> applHistoryMap = new HashMap<String, String>();
		applHistoryMap.put("companyCd", companyCd);
		applHistoryMap.put("shainNo", applHistoryData.getShainNo());
		applHistoryMap.put("applNo", applNo);
		applHistoryMap.put("applId", applHistoryData.getApplId());
		applHistoryMap.put("applStatus", applStatus);	
		boolean ahUpdateCheck = UpdateApplHistoryAgreeStatus(applHistoryMap, applHistoryData.getApplDate());
		//更新失敗
		if(!(ahUpdateCheck)){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
		}
		
		List<Map<String, String>> kariageTeijiList = selectDto.getKariageTeijiList();
		//提示データが存在しないとき（そんなことないと思うけど(´・ω・｀)）
		if(kariageTeijiList.size() <= 0){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
		}
		
		for(Map<String, String> kariageTeijiData : kariageTeijiList){
			String applCheckFlg = NOSELECT;
			
			//ラジオボタン行チェック判定
			if(candidateNo.equals(String.valueOf(kariageTeijiData.get("candidateNo")))){
				applCheckFlg = SELECT;
			}
			
			//TODO 借上候補物件提示明細テーブルを更新する（選択しない以外の場合？　選択しない行だとつじつまが合わない）元場所
			
			//選択された行の場合
			if(applCheckFlg.equals(SELECT)){
				//借上候補物件提示テーブルの更新
				Map<String, String> kariageTeijiMap = new HashMap<String, String>();
				kariageTeijiMap.put("companyCd", companyCd);
				kariageTeijiMap.put("applNo", applNo);
				kariageTeijiMap.put("teijiKaisu", teijiKaisu);
				kariageTeijiMap.put("candidateNo", candidateNo);
				kariageTeijiMap.put("riyu", riyu);
				kariageTeijiMap.put("biko", biko);
				
				boolean ktUpdateCheck = this.UpdateKariageTeiji(kariageTeijiMap);
				//更新失敗
				if(!(ktUpdateCheck)){
					ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}
				
				//TODO 借上候補物件提示明細テーブルを更新する（選択しない以外の場合？　選択しない行だとつじつまが合わない）
				Map<String, String> kariageTeijiDetailMap = new HashMap<String, String>();
				kariageTeijiDetailMap.put("companyCd", companyCd);
				kariageTeijiDetailMap.put("applNo", applNo);
				kariageTeijiDetailMap.put("teijiKaisu", teijiKaisu);
				kariageTeijiDetailMap.put("candidateNo", candidateNo);
				kariageTeijiDetailMap.put("applCheckFlg", applCheckFlg);
				
				boolean ktdUpdateCheck = this.UpdateKariageTeijiDetail(kariageTeijiDetailMap);
				//更新失敗
				if(!(ktdUpdateCheck)){
					ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}

				//選択されていない行の場合
			}else{
				//借上候補物件テーブルの更新を行う
				//提示された物件が選択されなかったため、同じ物件を他の人に提示できるように、提示フラグを0:選択可に更新する
				Map<String, String> kariageBukkenMap = new HashMap<String, String>();
				kariageBukkenMap.put("companyCd", companyCd);
				kariageBukkenMap.put("candidateNo", String.valueOf(kariageTeijiData.get("candidateNo")));
				kariageBukkenMap.put("teijiFlg", "0");

				boolean kbUpdateCheck = this.UpdateKariageBukken(kariageBukkenMap);
				//更新失敗
				if(!(kbUpdateCheck)){
					ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}	
			}
		}
		return selectDto;
	}
	
	/**
	 * 入力チェックを行う
	 * 
	 * @param initDto
	 * @return なし
	 * @throws UnsupportedEncodingException 
	 */
	public void inputCheck(Skf2060Sc002SelectDto selectDto) throws UnsupportedEncodingException{
		//いずれの物件の「選択内容」ラジオボタンも選択されていない場合
		if(selectDto.getRadioCandidateNo() == null){
			ServiceHelper.addErrorResultMessage(selectDto, new String[] { "radioCandidateNo" }, MessageIdConstant.E_SKF_1054, "物件");
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
			
			//「選択しない」ラベルが表示されている行が選択されている場合
			}else if(selectDto.getRadioCandidateNo().equals("0")){
				//理由ドロップダウンにて「その他」が選択されている場合
				if(selectDto.getRiyuDropdown().equals(CodeConstant.FUYO_RIYU_OTHERS)){
				//備考入力欄入力チェック
				if(selectDto.getBiko() == null || CheckUtils.isEmpty(selectDto.getBiko().trim())){
					ServiceHelper.addErrorResultMessage(selectDto, new String[] { "biko" }, MessageIdConstant.E_SKF_1048, "理由入力欄");
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				//備考入力欄桁数チェック
				}else if(CheckUtils.isMoreThanByteSize(selectDto.getBiko().trim(), 256)){
					ServiceHelper.addErrorResultMessage(selectDto, new String[] { "biko" }, MessageIdConstant.E_SKF_1071, "理由入力欄","128");
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}
			}
		}
	}
	
	/**
	 * 申請書類管理テーブルから情報が取得する(更新用）
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return 申請書類管理情報
	 */
	public Skf2060Sc002GetApplHistoryInfoForUpdateExp getApplHistoryInfoForUpdate(String companyCd, String applNo){
		
		Skf2060Sc002GetApplHistoryInfoForUpdateExp resultData = new Skf2060Sc002GetApplHistoryInfoForUpdateExp();
		Skf2060Sc002GetApplHistoryInfoForUpdateExpParameter param = new Skf2060Sc002GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		
		resultData = skf2060Sc002GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		
		return resultData;
	}
	
	/**
	 * 申請書類履歴テーブルを更新する
	 * 
	 * @param Map型更新情報
	 * @return 更新チェック判定<br>trueの場合OK<br>falseの場合NG
	 */
	public boolean UpdateApplHistoryAgreeStatus(Map<String, String> applHistoryMap, Date applDate){
		boolean updateCheck = true;
		int updateCount = 0;
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		updateData.setCompanyCd(applHistoryMap.get("companyCd"));
		updateData.setShainNo(applHistoryMap.get("shainNo"));
		updateData.setApplDate(applDate);
		updateData.setApplNo(applHistoryMap.get("applNo"));
		updateData.setApplId(applHistoryMap.get("applId"));
		updateData.setApplStatus(applHistoryMap.get("applStatus"));
		
		updateCount = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		
		if(updateCount <= 0){
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 借上候補物件提示明細テーブルを更新する
	 * 
	 * @param Map型更新情報
	 * @return 更新チェック判定<br>trueの場合OK<br>falseの場合NG
	 */
	public boolean UpdateKariageTeijiDetail(Map<String, String> kariageTeijiDetailyMap){
		boolean updateCheck = true;
		int updateCount = 0;
		Skf2060TKariageTeijiDetail updateData = new Skf2060TKariageTeijiDetail();
		updateData.setCompanyCd(kariageTeijiDetailyMap.get("companyCd"));
		updateData.setApplNo(kariageTeijiDetailyMap.get("applNo"));
		updateData.setTeijiKaisu(Short.parseShort(kariageTeijiDetailyMap.get("teijiKaisu")));
		updateData.setCandidateNo(Long.parseLong(kariageTeijiDetailyMap.get("candidateNo")));
		updateData.setApplCheckFlg(kariageTeijiDetailyMap.get("applCheckFlg"));
		
		updateCount = skf2060TKariageTeijiDetailRepository.updateByPrimaryKeySelective(updateData);
		
		if(updateCount <= 0){
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 借上候補物件提示テーブルを更新する
	 * 
	 * @param Map型更新情報
	 * @return 更新チェック判定<br>trueの場合OK<br>falseの場合NG
	 */
	public boolean UpdateKariageTeiji(Map<String, String> kariageTeijiMap){
		boolean updateCheck = true;
		int updateCount = 0;
		Skf2060TKariageTeiji updateData = new Skf2060TKariageTeiji();
		updateData.setCompanyCd(kariageTeijiMap.get("companyCd"));
		updateData.setApplNo(kariageTeijiMap.get("applNo"));
		updateData.setTeijiKaisu(Short.parseShort(kariageTeijiMap.get("teijiKaisu")));
		updateData.setCheckCandidateNo(Long.parseLong(kariageTeijiMap.get("candidateNo")));
		updateData.setRiyu(kariageTeijiMap.get("riyu"));
		updateData.setBiko(kariageTeijiMap.get("biko"));
		
		updateCount = skf2060TKariageTeijiRepository.updateByPrimaryKeySelective(updateData);
		
		if(updateCount <= 0){
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 借上候補物件テーブルを更新する
	 * 
	 * @param Map型更新情報
	 * @return 更新チェック判定<br>trueの場合OK<br>falseの場合NG
	 */
	public boolean UpdateKariageBukken(Map<String, String> kariageTeijiMap){
		boolean updateCheck = true;
		int updateCount = 0;
		Skf2060TKariageBukken updateData = new Skf2060TKariageBukken();
		updateData.setCompanyCd(kariageTeijiMap.get("companyCd"));
		updateData.setCandidateNo(Long.parseLong(kariageTeijiMap.get("candidateNo")));
		updateData.setTeijiFlg(kariageTeijiMap.get("teijiFlg"));
		
		updateCount = skf2060TKariageBukkenRepository.updateByPrimaryKeySelective(updateData);
		
		if(updateCount <= 0){
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
}
