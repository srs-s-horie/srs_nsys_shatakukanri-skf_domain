/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc003;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetShainSoshikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetShainSoshikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiFileDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiFileDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetCheckCandidateNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetCheckCandidateNoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetKariageTeijiFileDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetShainSoshikiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetCheckCandidateNoExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc003.Skf2060Sc003InitDto;

/**
 * 借上候補物件提示確認画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc003InitService extends BaseServiceAbstract<Skf2060Sc003InitDto> {
	
	@Autowired
	private Skf2060Sc003GetApplHistoryExpRepository skf2060Sc003GetApplHistoryExpRepository;
	@Autowired
	private Skf2060Sc003GetShainSoshikiInfoExpRepository skf2060Sc003GetShainSoshikiInfoExpRepository;
	@Autowired
	private Skf2060Sc003GetKariageTeijiFileDataExpRepository skf2060Sc003GetKariageTeijiFileDataExpRepository;
	@Autowired
	private Skf2060Sc003GetCheckCandidateNoExpRepository skf2060Sc003GetCheckCandidateNoExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private Skf2060Sc003SharedService skf2060Sc003SharedService;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2060Sc003InitDto index(Skf2060Sc003InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC003_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, initDto.getPageId());
		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));
		
        // 提示状況汎用コード取得(反転)
        Map<String, String> applStatusGenCodeMap = new HashMap<String, String>();
        applStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
 		
		//申請書類情報の取得
		Skf2060Sc003GetApplHistoryExp applHistoryData = this.getApplHistory(companyCd, initDto.getApplNo());
		//申請書類情報が取得出来なかった場合
		if(applHistoryData == null){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		
		String applNo = applHistoryData.getApplNo();
		String applStatus = applHistoryData.getApplStatus();
		//「申請状況」にステータス（コード変換済み）を設定
		initDto.setApplStatus(applStatusGenCodeMap.get(applStatus));
		
		//社員・組織情報の取得
		Skf2060Sc003GetShainSoshikiInfoExp shainSohikiData = this.getShainSoshikiInfo(companyCd, applNo);
		//社員・組織情報が存在しない場合
		if(shainSohikiData ==null){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}

		String gender = CodeConstant.NONE;
		switch(shainSohikiData.getGender()){
		case "1":
			gender = "男";
			break;
		case "2":
			gender = "女";
			break;
		}
		
		//「借上物件提示対象者」に表示させる項目の設定
		initDto.setAgency(shainSohikiData.getAgencyName());
		initDto.setAffiliation1(shainSohikiData.getAffiliation1Name());
		initDto.setAffiliation2(shainSohikiData.getAffiliation2Name());
		initDto.setTel(shainSohikiData.getTel());
		initDto.setShainNo(shainSohikiData.getShainNo());
		initDto.setName(shainSohikiData.getName());
		initDto.setTokyu(shainSohikiData.getTokyuName());
		initDto.setGender(gender);
		
		//借上候補物件提示情報の取得
		List<Skf2060Sc003GetKariageTeijiInfoExp> kariageTeijiDataList = skf2060Sc003SharedService.getKariageTeijiInfo(companyCd, applNo);
		//借上候補物件提示情報が存在しない場合
		if(kariageTeijiDataList.size() <= 0){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		
		//提示回数
		short teijiKaisu =(short)kariageTeijiDataList.get(0).getTeijiKaisu();
		
		List<Map<String, String>> kariageTeijiList = new ArrayList<Map<String, String>>();
		for(Skf2060Sc003GetKariageTeijiInfoExp kariageTeijiData : kariageTeijiDataList){
			Map<String, String> kariageTeijiMap = new HashMap<String, String>();
			kariageTeijiMap = this.getKariageTeijiMap(kariageTeijiData, applNo);
			kariageTeijiList.add(kariageTeijiMap);
		}
	
		initDto.setKariageTeijiList(kariageTeijiList);
		
		//申請書類管理番号からコメントを取得
		List<SkfCommentUtilsGetCommentInfoExp> commentList =  skfCommentUtils.getCommentInfo(companyCd, applNo, null);
		//コメントが存在する場合
		if(commentList != null && commentList.size() > 0){
			//「コメント表示」ボタンを表示
			initDto.setCommentViewFlag(true);
		//コメントが存在しない場合
		}else{
			//「コメントを表示」ボタンを非表示
			initDto.setCommentViewFlag(false);
		}
		
		
		//ステータスによる表示制御、使用可否設定
		//申請状況が"選択しない"もしくは"選択済み"の場合
		if(applStatus.equals(CodeConstant.STATUS_SENTAKU_SHINAI) || applStatus.equals(CodeConstant.STATUS_SENTAKU_ZUMI)){
			//「完了」ボタン、「再掲示」ボタンを表示
			initDto.setButtonViewFlag(true);
			
			//選択された項目のラジオボタンをチェック済みにするための選択物件番号を取得する
			Long checkCandidateNo = this.getCheckCandidateNo(companyCd, applNo, teijiKaisu);
			if(checkCandidateNo != null){
				initDto.setCheckCandidateNo(String.valueOf(checkCandidateNo));
			}
			
		//申請状況が"選択しない"もしくは"選択済み"以外の場合
		}else{
			//「完了」ボタン、「再掲示」ボタンを非表示
			initDto.setButtonViewFlag(false);
		}
		  
		return initDto;
	}
	
	
	
	/**
	 * 申請書類情報を取得する
	 * 
	 * @param initDto
	 * @return 申請書類情報
	 */
	public Skf2060Sc003GetApplHistoryExp getApplHistory(String companyCd, String applNo){
		
		//申請書類情報の取得
		Skf2060Sc003GetApplHistoryExp applHistoryData = new Skf2060Sc003GetApplHistoryExp();
		Skf2060Sc003GetApplHistoryExpParameter param = new Skf2060Sc003GetApplHistoryExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		applHistoryData = skf2060Sc003GetApplHistoryExpRepository.getApplHistory(param);
		
		return applHistoryData;
	}
	
	/**
	 * 社員・組織情報を取得する
	 * 
	 * @param initDto
	 * @return 社員・組織情報
	 */
	public Skf2060Sc003GetShainSoshikiInfoExp getShainSoshikiInfo(String companyCd, String applNo){
		
		//社員・組織情報の取得
		Skf2060Sc003GetShainSoshikiInfoExp shainSoshikiData = new Skf2060Sc003GetShainSoshikiInfoExp();
		Skf2060Sc003GetShainSoshikiInfoExpParameter param = new Skf2060Sc003GetShainSoshikiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shainSoshikiData = skf2060Sc003GetShainSoshikiInfoExpRepository.getShainSoshikiInfo(param);
	
		return shainSoshikiData;
	}
	
	/**
	 * 添付ファイル情報を取得する
	 * 
	 * @param initDto
	 * @return 添付ファイル情報
	 */
	public List<Skf2060Sc003GetKariageTeijiFileDataExp> getKariageTeijiFileData(String companyCd, String applNo, long teijiKaisu, long candidateNo){
		
		//添付ファイル情報の取得
		List<Skf2060Sc003GetKariageTeijiFileDataExp> kariageTeijiFileData = new ArrayList<Skf2060Sc003GetKariageTeijiFileDataExp>();
		Skf2060Sc003GetKariageTeijiFileDataExpParameter param = new Skf2060Sc003GetKariageTeijiFileDataExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setTeijiKaisu(teijiKaisu);
		param.setCandidateNo(candidateNo);
		
		kariageTeijiFileData = skf2060Sc003GetKariageTeijiFileDataExpRepository.getKariageTeijiFileData(param);
		
		return kariageTeijiFileData;
	}
	
	
	/**
	 * 添付ファイルのリンクタグを取得する
	 * 
	 * @param candidateNo
	 * @param attachedNo
	 * @param candidateName
	 * 
	 * @return 添付ファイルリンクタグ
	 */
	public String getLinkTag(String candidateNo, String attachedNo, String attachedName){
		String baseLinkTag = "<a id=\"attached_$CANDIDATENO$_$ATACCHEDNO$\">$ATTACHEDNAME$</a>";
		
		baseLinkTag = baseLinkTag.replace("$CANDIDATENO$", candidateNo);
		baseLinkTag = baseLinkTag.replace("$ATACCHEDNO$", attachedNo);
		baseLinkTag = baseLinkTag.replace("$ATTACHEDNAME$", attachedName);
		
		return baseLinkTag;
	}
	
	/**
	 * 借上候補物件提示情報をマッピングする
	 * 
	 * @param kariageTeijiData
	 * @param applNo
	 * 
	 * @return Map型の借上候補物件提示情報
	 */
	public Map<String, String> getKariageTeijiMap(Skf2060Sc003GetKariageTeijiInfoExp kariageTeijiData, String applNo){
		Map<String, String> kariageTeijiMap = new HashMap<String, String>();
		String linkTag = CodeConstant.NONE;
		List<String> linkTagList = new ArrayList<String>();
		//添付ファイル情報の取得
		List<Skf2060Sc003GetKariageTeijiFileDataExp> KariageTeijiFileDataList= this.getKariageTeijiFileData(companyCd, applNo, kariageTeijiData.getTeijiKaisu(), kariageTeijiData.getCandidateNo());
		//添付ファイルが存在する場合
		if(KariageTeijiFileDataList.size() > 0){
			//リンクタグを作成する
			for(Skf2060Sc003GetKariageTeijiFileDataExp KariageTeijiFileData : KariageTeijiFileDataList){
				//リンクタグを作成
				String baseLinkTag = this.getLinkTag(String.valueOf(KariageTeijiFileData.getCandidateNo()), KariageTeijiFileData.getAttachedNo(), KariageTeijiFileData.getAttachedName());
				linkTagList.add(baseLinkTag);
			}
			linkTag = String.join("<br />", linkTagList);
		}
		
		kariageTeijiMap.put("shatakuName", kariageTeijiData.getShatakuName());
		kariageTeijiMap.put("shatakuNameAddress", kariageTeijiData.getAddress());
		kariageTeijiMap.put("biko", kariageTeijiData.getBiko());
		kariageTeijiMap.put("attachedFile", linkTag);
		kariageTeijiMap.put("candidateNo", String.valueOf(kariageTeijiData.getCandidateNo()));
		
		return kariageTeijiMap;
	}
	
	/**
	 * 選択物件番号を取得する
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param teijiKaisu
	 * 
	 * @return 選択物件番号
	 */
	public Long getCheckCandidateNo(String companyCd, String applNo, short teijiKaisu){

		//選択物件番号の取得
		Skf2060Sc003GetCheckCandidateNoExp getCheckCandidateNoData = new Skf2060Sc003GetCheckCandidateNoExp();
		Skf2060Sc003GetCheckCandidateNoExpParameter param = new Skf2060Sc003GetCheckCandidateNoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setTeijiKaisu(teijiKaisu);
		
		getCheckCandidateNoData = skf2060Sc003GetCheckCandidateNoExpRepository.getCheckCandidateNo(param);
		
		if(getCheckCandidateNoData != null){		
			return getCheckCandidateNoData.getCheckCandidateNo();
		}else{
			return null;
		}
	}
}
