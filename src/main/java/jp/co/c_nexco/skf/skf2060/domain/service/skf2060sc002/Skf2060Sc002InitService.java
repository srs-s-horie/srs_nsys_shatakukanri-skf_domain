/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiFileDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetShainSoshikiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoExpRepository;
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
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc002InitService extends BaseServiceAbstract<Skf2060Sc002InitDto> {
	
	@Autowired
	private SkfApplHistoryInfoUtilsGetApplHistoryInfoExpRepository skfApplHistoryInfoUtilsGetApplHistoryInfoExpRepository;
	@Autowired
	private Skf2060Sc002GetShainSoshikiInfoExpRepository skf2060Sc002GetShainSoshikiInfoExpRepository;
	@Autowired
	private Skf2060Sc002GetKariageTeijiFileDataExpRepository skf2060Sc002GetKariageTeijiFileDataExpRepository;
	@Autowired
	private Skf2060Sc002GetKariageTeijiInfoExpRepository skf2060Sc002GetKariageTeijiInfoExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	
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
		
        // 提示状況汎用コード取得(反転)
        Map<String, String> applStatusGenCodeReverseMap = new HashMap<String, String>();
        applStatusGenCodeReverseMap = skfGenericCodeUtils.getGenericCodeReverse(FunctionIdConstant.GENERIC_CODE_STATUS);
		
		//TODO 申請書類管理番号と申請状況が来るといいなぁ
		//TODO 後々いらない
		//initDto.setHdnApplNo("R0106-00731784-20190307-01");
		//initDto.setApplStatus("完了");
		initDto.setApplNo("R0106-20190906-20190924-01");
		initDto.setApplStatus("確認依頼");
		
		String applNo = initDto.getApplNo();
		//申請状況はコードへ変換?
		String applStatus = applStatusGenCodeReverseMap.get(initDto.getApplStatus());
		
		//申請書類情報の取得
		SkfApplHistoryInfoUtilsGetApplHistoryInfoExp applHistoryInfo = this.getApplHistoryInfo(companyCd, applNo);
		//申請書類情報が取得出来なかった場合
		if(applHistoryInfo == null){
			//エラーメッセージ
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		
		//社員・組織情報の取得
		Skf2060Sc002GetShainSoshikiInfoExp shainSohikiData = this.getShainSoshikiInfo(companyCd, applNo);
		//社員・組織情報が存在しない場合
		if(shainSohikiData ==null){
			//TODO エラーの時はどうしよう(´・ω・｀)
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		//TODO 性別コードで引っ張ってこれるやつ無いかなぁ(´・ω・｀)
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
		
		//借上候補物件情報の取得
		List<Skf2060Sc002GetKariageTeijiInfoExp> kariageTeijiDataList = this.getKariageTeijiInfo(companyCd, applNo);
		//社員・組織情報が存在しない場合
		if(kariageTeijiDataList.size() <= 0){
			//TODO エラーの時はどうしよう(´・ω・｀)
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1078, "初期表示中に");
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}else{
			//TODO 現行ではラベルに提示回数を保存というよくわかんないことをしていたので隠し要素で設定(´・ω・｀)
			initDto.setTeijiKaisu(String.valueOf(kariageTeijiDataList.get(0).getTeijiKaisu()));
		}
		
		
		
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
			//initDto.setRadioCandidateNoDisabled("false");
			initDto.setRiyuDropdownDisabled("false");
			initDto.setSelectViewFlag(true);
			//申請状況が確認依頼以外の場合、ラジオボタン・ドロップダウンリストを非活性・「選択」ボタンを非表示
		}else{
			//initDto.setRadioCandidateNoDisabled("true");
			initDto.setRiyuDropdownDisabled("true");
			initDto.setSelectViewFlag(false);
		}
		return initDto;
	}
	
	/**
	 * 借上候補物件情報を取得する
	 * 
	 * @param initDto
	 * @return 借上候補物件情報
	 */
	public List<Skf2060Sc002GetKariageTeijiInfoExp> getKariageTeijiInfo(String companyCd, String applNo){
		
		//TODO ここの固定文字列は定数化するかXMLから取得するように変更必要があるらしい(´・ω・｀) by現行
		String applCheckFlgTrue = "1";
		String applCheckFlgFalse = "0";
		String noSelShatakuName = "選択しない";
		
		//借上候補物件情報の取得
		List<Skf2060Sc002GetKariageTeijiInfoExp> kariageTeijiDataList = new ArrayList<Skf2060Sc002GetKariageTeijiInfoExp>();
		Skf2060Sc002GetKariageTeijiInfoExpParameter param = new Skf2060Sc002GetKariageTeijiInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		param.setApplCheckFlgTrue(applCheckFlgTrue);
		param.setApplCheckFlgFalse(applCheckFlgFalse);
		param.setNoSelShatakuName(noSelShatakuName);
		kariageTeijiDataList = skf2060Sc002GetKariageTeijiInfoExpRepository.getKariageTeijiInfo(param);

		return kariageTeijiDataList;
	}
	
	/**
	 * 申請書類情報を取得する
	 * 
	 * @param initDto
	 * @return 申請書類情報
	 */
	public SkfApplHistoryInfoUtilsGetApplHistoryInfoExp getApplHistoryInfo(String companyCd, String applNo){
		
		//申請書類情報の取得
		List<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp> applDataList = new ArrayList<SkfApplHistoryInfoUtilsGetApplHistoryInfoExp>();
		SkfApplHistoryInfoUtilsGetApplHistoryInfoExpParameter param = new SkfApplHistoryInfoUtilsGetApplHistoryInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		applDataList = skfApplHistoryInfoUtilsGetApplHistoryInfoExpRepository.getApplHistoryInfo(param);
		
		//申請書類情報が存在しない場合
		if(applDataList.size() <= 0){
			return null;
		}
		return applDataList.get(0);
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
	 * 借上候補物件情報をマッピングする
	 * 
	 * @param kariageTeijiData
	 * @param applNo
	 * 
	 * @return Map型の借上候補物件情報
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
