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

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExpRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc002.Skf2060Sc002InitDto;

/**
 * 借上候補物件確認画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc002InitService extends BaseServiceAbstract<Skf2060Sc002InitDto> {
	
	@Autowired
	private Skf2060Sc002GetApplHistoryExpRepository skf2060Sc002GetApplHistoryExpRepository;
	@Autowired
	private Skf2060Sc002GetShainSoshikiInfoExpRepository skf2060Sc002GetShainSoshikiInfoExpRepository;
	@Autowired
	private Skf2060Sc002GetKariageTeijiFileDataExpRepository skf2060Sc002GetKariageTeijiFileDataExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private Skf2060Sc002SharedService skf2060Sc002SharedService;
	
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
	public Skf2060Sc002InitDto index(Skf2060Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC002_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, initDto.getPageId());
		// 操作ガイドの設定
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));
		
		//更新日を設定
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();
		
        // 提示状況汎用コード取得(反転)
        Map<String, String> applStatusGenCodeMap = new HashMap<String, String>();
        applStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
        
		//申請書類情報の取得
		Skf2060Sc002GetApplHistoryExp applHistoryData = this.getApplHistory(companyCd, initDto.getApplNo());
		//申請書類情報が取得出来なかった場合
		if(applHistoryData == null){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		//申請書類履歴テーブル用更新日
		lastUpdateDateMap.put(initDto.applHistoryLastUpdateDate, applHistoryData.getUpdateDate());
		
		String applNo = applHistoryData.getApplNo();
		String applStatus = applHistoryData.getApplStatus();
		//「申請状況」にステータス（コード変換済み）を設定
		initDto.setApplStatus(applStatusGenCodeMap.get(applStatus));
		
		//社員・組織情報の取得
		Skf2060Sc002GetShainSoshikiInfoExp shainSohikiData = this.getShainSoshikiInfo(companyCd, applNo);
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
		List<Skf2060Sc002GetKariageTeijiInfoExp> kariageTeijiDataList = skf2060Sc002SharedService.getKariageTeijiInfo(companyCd, applNo);
		//借上候補物件提示情報が存在しない場合
		if(kariageTeijiDataList.size() <= 0){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}

		for(Skf2060Sc002GetKariageTeijiInfoExp kariageTeijiData:kariageTeijiDataList){
			long candidateNo = kariageTeijiData.getCandidateNo();
			short teijiKaisu = (short)kariageTeijiData.getTeijiKaisu();
			if(candidateNo != 0){
				//借上候補物件テーブル用更新日
				Skf2060TKariageBukken kbData = skf2060Sc002SharedService.getKariageBukkenForUpdate(companyCd, candidateNo);
				if(kbData != null){
					lastUpdateDateMap.put(initDto.KariageBukkenLastUpdateDate + String.valueOf(candidateNo), kbData.getUpdateDate());
					//借上候補物件提示明細テーブル用更新日
					Skf2060TKariageTeijiDetail ktdData = skf2060Sc002SharedService.getKariageTeijiDetailForUpdate(companyCd, applNo, teijiKaisu, candidateNo);
					lastUpdateDateMap.put(initDto.KariageTeijiDetailLastUpdateDate + String.valueOf(candidateNo), ktdData.getUpdateDate());
				}
			}
		}

		//借上候補物件提示テーブル用更新日
		Skf2060TKariageTeiji ktData = skf2060Sc002SharedService.getKariageTeijiForUpdate(companyCd, applNo, (short)kariageTeijiDataList.get(0).getTeijiKaisu());
		lastUpdateDateMap.put(initDto.KariageTeijiLastUpdateDate, ktData.getUpdateDate());


		//現行ではラベルに提示回数を保存していたので隠し要素で設定
		initDto.setTeijiKaisu(String.valueOf(kariageTeijiDataList.get(0).getTeijiKaisu()));
		
		List<Map<String, String>> kariageTeijiList = new ArrayList<Map<String, String>>();
		for(Skf2060Sc002GetKariageTeijiInfoExp kariageTeijiData : kariageTeijiDataList){
			Map<String, String> kariageTeijiMap = new HashMap<String, String>();
			kariageTeijiMap = this.getKariageTeijiMap(kariageTeijiData, applNo);
			kariageTeijiList.add(kariageTeijiMap);
		}
		//借上物件リストの最後のデータを削除(SQLにて「選択しない」用のデータも取ってきているため）
		kariageTeijiList.remove(kariageTeijiList.size() - 1);	
		initDto.setKariageTeijiList(kariageTeijiList);
		
		//ドロップダウン作成
		List<Map<String, Object>> riyuList = new ArrayList<Map<String, Object>>();
		Map<String, Object> kariageRiyu = new HashMap<String, Object>();
		kariageRiyu.put("value", CodeConstant.JIKO_KARIAGE);
		kariageRiyu.put("label", "自己借上のため");
		Map<String, Object> otherRiyu = new HashMap<String, Object>();
		otherRiyu.put("value", CodeConstant.FUYO_RIYU_OTHERS);
		otherRiyu.put("label", "その他");
		riyuList.add(kariageRiyu);
		riyuList.add(otherRiyu);
		
		initDto.setRiyuList(riyuList);
		
		//提示ステータスが41のとき（完了）
		if(applStatus.equals(CodeConstant.STATUS_KANRYOU)){
			//「コメントを表示」ボタンを非表示
			initDto.setCommentViewFlag(false);
		}else{
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
		}
		
		//「理由」テキストボックスを非活性
		initDto.setBikoDisabled("true");
		//ステータスによる表示制御、使用可否設定
		//申請状況が確認依頼の場合、ラジオボタン・ドロップダウンリストを活性・「選択」ボタンを表示
		if(applStatus.equals(CodeConstant.STATUS_KAKUNIN_IRAI)){
			initDto.setRiyuDropdownDisabled("false");
			initDto.setSelectViewFlag(true);
			//申請状況が確認依頼以外の場合、ラジオボタン・ドロップダウンリストを非活性・「選択」ボタンを非表示
		}else{
			initDto.setRiyuDropdownDisabled("true");
			initDto.setSelectViewFlag(false);
		}
		
		//更新日設定
		initDto.setLastUpdateDateMap(lastUpdateDateMap);
		
		return initDto;
	}
	
	/**
	 * 申請書類情報を取得する
	 * 
	 * @param initDto
	 * @return 申請書類情報
	 */
	public Skf2060Sc002GetApplHistoryExp getApplHistory(String companyCd, String applNo){
		
		//申請書類情報の取得
		Skf2060Sc002GetApplHistoryExp applHistoryData = new Skf2060Sc002GetApplHistoryExp();
		Skf2060Sc002GetApplHistoryExpParameter param = new Skf2060Sc002GetApplHistoryExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		applHistoryData = skf2060Sc002GetApplHistoryExpRepository.getApplHistory(param);
		
		return applHistoryData;
	}
	
	/**
	 * 社員・組織情報を取得する
	 * 
	 * @param initDto
	 * @return 社員・組織情報
	 */
	public Skf2060Sc002GetShainSoshikiInfoExp getShainSoshikiInfo(String companyCd, String applNo){
		
		//社員・組織情報の取得
		Skf2060Sc002GetShainSoshikiInfoExp shainSoshikiData = new Skf2060Sc002GetShainSoshikiInfoExp();
		Skf2060Sc002GetShainSoshikiInfoExpParameter param = new Skf2060Sc002GetShainSoshikiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shainSoshikiData = skf2060Sc002GetShainSoshikiInfoExpRepository.getShainSoshikiInfo(param);
	
		return shainSoshikiData;
	}
	
	
	/**
	 * 添付ファイル情報を取得する
	 * 
	 * @param initDto
	 * @return 添付ファイル情報
	 */
	public List<Skf2060Sc002GetKariageTeijiFileDataExp> getKariageTeijiFileData(String companyCd, String applNo, long teijiKaisu, long candidateNo){
		
		//添付ファイル情報の取得
		List<Skf2060Sc002GetKariageTeijiFileDataExp> kariageTeijiFileData = new ArrayList<Skf2060Sc002GetKariageTeijiFileDataExp>();
		Skf2060Sc002GetKariageTeijiFileDataExpParameter param = new Skf2060Sc002GetKariageTeijiFileDataExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setTeijiKaisu(teijiKaisu);
		param.setCandidateNo(candidateNo);
		
		kariageTeijiFileData = skf2060Sc002GetKariageTeijiFileDataExpRepository.getKariageTeijiFileData(param);
		
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
	public Map<String, String> getKariageTeijiMap(Skf2060Sc002GetKariageTeijiInfoExp kariageTeijiData, String applNo){
		Map<String, String> kariageTeijiMap = new HashMap<String, String>();
		String linkTag = CodeConstant.NONE;
		List<String> linkTagList = new ArrayList<String>();
		//添付ファイル情報の取得
		List<Skf2060Sc002GetKariageTeijiFileDataExp> KariageTeijiFileDataList= this.getKariageTeijiFileData(companyCd, applNo, kariageTeijiData.getTeijiKaisu(), kariageTeijiData.getCandidateNo());
		//添付ファイルが存在する場合
		if(KariageTeijiFileDataList.size() > 0){
			//リンクタグを作成する
			for(Skf2060Sc002GetKariageTeijiFileDataExp KariageTeijiFileData : KariageTeijiFileDataList){
				//リンクタグを作成
				String baseLinkTag = this.getLinkTag(String.valueOf(KariageTeijiFileData.getCandidateNo()), KariageTeijiFileData.getAttachedNo(), KariageTeijiFileData.getAttachedName());
				linkTagList.add(baseLinkTag);
			}
			linkTag = String.join("<br />", linkTagList);
		}
		
		kariageTeijiMap.put("shatakuName", kariageTeijiData.getShatakuName());
		kariageTeijiMap.put("shatakuNameAddress", kariageTeijiData.getAddress());
		kariageTeijiMap.put("attachedFile", linkTag);
		kariageTeijiMap.put("candidateNo", String.valueOf(kariageTeijiData.getCandidateNo()));
		kariageTeijiMap.put("teijiKaisu", String.valueOf(kariageTeijiData.getTeijiKaisu()));
		
		return kariageTeijiMap;
	}
	
}
