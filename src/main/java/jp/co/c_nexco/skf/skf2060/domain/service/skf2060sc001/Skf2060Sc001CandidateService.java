/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001CandidateDto;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001CandidateService extends BaseServiceAbstract<Skf2060Sc001CandidateDto> {
	
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2060Sc001GetMaxTeijiKaisuExpRepository skf2060Sc001GetMaxTeijiKaisuExpRepository;
	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	
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
	@SuppressWarnings("unused")
	@Override
	public Skf2060Sc001CandidateDto index(Skf2060Sc001CandidateDto candidateDto) throws Exception {
		
		candidateDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		String shainNo = candidateDto.getPresentedNo();
		String shainName = candidateDto.getPresentedName();
		
		//TODO 提示対象セッション情報から申請書類番号とステータスと提示回数を取得
		Skf2060Sc001GetApplHistoryExp applInforesultData = (Skf2060Sc001GetApplHistoryExp)sessionBean.get("getApplHistoryResultData");
		String applNo = new String();
		String applStatus = new String();
		//提示対象セッション情報の提示対象者の情報
		if(applInforesultData != null){
			applNo = applInforesultData.getApplNo();
			applStatus = applInforesultData.getApplStatus();
		}
		//TODO ログインセッションのユーザ情報（ログインしている奴？　tenant？ どうやって持ってくんの？）
		Map<String, String> userInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		//ログインセッションユーザ情報のユーザ名
		String userName = userInfoMap.get("userName");
		//選択物件番号
		long checkCandidateNo =0;
		//TODO 提示対象セッションの再掲示フラグ
		boolean itiranFlg = true;
		
		String updateDate = candidateDto.getUpdateDate();
		updateDate = skfDateFormatUtils.dateFormatFromString(updateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		//提示回数
		short teijiKaisu = 1;
		//新規作成フラグ
		boolean newCreateFlg = false;
		boolean presentedNameflg = true;
		boolean tijiValFlg = true;

		//申請書類ID(R0106:確認依頼)
		String applId = "R0106";
		//添付書類有無
		String applTacFlg ="0";
		//連携済フラグ
		String comboFlg ="0";
		
		//システム日付
		Date candidateDate = DateUtils.getSysDate();
		
		//TODO 提示対象セッション情報を取得できた場合（再掲示）
		if(applInforesultData != null && applNo != null && applStatus != null ){
			//提示ステータスが20のとき（確認依頼）
			if(applStatus.equals(CodeConstant.STATUS_KAKUNIN_IRAI)){
				//エラーメッセージを設定
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_2016);
				return candidateDto;
			//提示ステータスが41のとき（完了）
			}else if(applStatus.equals(CodeConstant.STATUS_KANRYOU)){
				//申請書類管理番号を作成する
				applNo = skf2060Sc001SharedService.getApplNo(companyCd, candidateDto.getPresentedNo(), updateDate, applId);
				//提示回数に"1"を設定する
				teijiKaisu = 1; 
				//新規作成フラグをTrueに設定する
				newCreateFlg = true;
			//提示ステータスが20,41以外のとき
			}else{
				//申請書類管理番号をもとに「提示回数」を取得する
				teijiKaisu = this.getMaxTeijiNum(companyCd, applNo);
			}

		//取得できなかった場合（新規掲示）
		}else{
			//申請書類管理番号を作成する
			applNo = skf2060Sc001SharedService.getApplNo(companyCd, candidateDto.getPresentedNo(), updateDate, applId);
			//提示回数に"1"を設定する
			teijiKaisu = 1; 
			//新規作成フラグをTrueに設定する
			newCreateFlg = true;
		}
		
		//入力チェック
		//提示対象者名が未選択の時
		if(candidateDto.getPresentedName() == null || CheckUtils.isEmpty(candidateDto.getPresentedName().trim())){
			presentedNameflg = false;
			ServiceHelper.addErrorResultMessage(candidateDto, new String[] { "presentedName" }, MessageIdConstant.E_SKF_1054,"提示対象者");
		//提示チェックボックスが未選択の時
		}else if(candidateDto.getTeijiVal() == null){	
			tijiValFlg = false;
			ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1054,"提示対象物件");
		}else if(candidateDto.getTeijiVal() != null){
			if(candidateDto.getTeijiVal().length == 0){
				tijiValFlg = false;
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1054,"提示対象物件");
			}
		}
		//入力チェックでエラーが生じたとき
		if(!(presentedNameflg && tijiValFlg)){
			return candidateDto;
		}
		
		//新規作成フラグがTrueの場合
		if(newCreateFlg){
			applStatus = "20";
			//申請書類履歴テーブルに登録
			boolean insertCheck = skf2060Sc001SharedService.insertApplHistory(companyCd, shainNo, applNo, applId, applStatus, applTacFlg, userName, comboFlg);
			
			//申請書類登録に失敗
			if(!(insertCheck)){
				System.out.println("insertCheckに失敗(´・ω・｀)");
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
				return candidateDto;
			}
			
			//(CreateKariageBukkenTeiji)
			//借上候補物件提示データの作成を行う
			//借上候補物件提示テーブルへ情報を登録する
			boolean kariageTeijiCheck = skf2060Sc001SharedService.insertKatiageTeiji(companyCd, applNo, teijiKaisu, checkCandidateNo, candidateDate, CodeConstant.DOUBLE_QUOTATION, CodeConstant.DOUBLE_QUOTATION);
			//登録に失敗した場合
			if(!(kariageTeijiCheck)){
				System.out.println("kariageTeijiCheckに失敗(´・ω・｀)");
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
				return candidateDto;
			}
			
			//借上候補物件提示明細テーブルに情報を登録する
			//チェックが入っているデータを取得
			List<Map<String, Object>> dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
			List<Map<String, Object>> checkDataParamList = new ArrayList<Map<String, Object>>();
			System.out.println("candidateDto.getTeijiVal().length:"+candidateDto.getTeijiVal().length);
			for(int i = 0; i < candidateDto.getTeijiVal().length; i++){
				int checkDataNum = Integer.parseInt(candidateDto.getTeijiVal()[i]);
				checkDataParamList.add(dataParamList.get(checkDataNum));
			}
			//借上候補物件提示明細テーブルに情報を登録
			for(int j = 0; j < checkDataParamList.size(); j++){
				boolean kariageTeijiDetailCheck = skf2060Sc001SharedService.insertKatiageTeijiDetail(companyCd, applNo, teijiKaisu, (long)checkDataParamList.get(j).get("candidateNo"), (String)checkDataParamList.get(j).get("shatakuName"), (String)checkDataParamList.get(j).get("address"), (String)checkDataParamList.get(j).get("money"), 
												"0", candidateDto.getPresentedNo());
				//登録に失敗した場合
				if(!(kariageTeijiDetailCheck))	{
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					return candidateDto;
				}
			}
			
			//添付ファイル管理テーブル(提示物件)に情報を登録
			for(int j = 0; j < checkDataParamList.size(); j++){
				if(!(checkDataParamList.get(j).get("attachedName").equals(""))){
					boolean kariageTeijiFileCheck = skf2060Sc001SharedService.insertKatiageTeijiFile(companyCd, applNo, teijiKaisu, (long)checkDataParamList.get(j).get("candidateNo"));
					//登録に失敗した場合
					if(!(kariageTeijiFileCheck))	{
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						System.out.println("kariageTeijiFileCheckに失敗(´・ω・｀)");
						return candidateDto;
					}
				}
			}
			
			//借上候補物件番号をもとに借上候補物件テーブルの提示可能フラグを"1"（提示不可）に更新する
			for(int j = 0; j < checkDataParamList.size(); j++){
				boolean updateKariageKohoCheck = skf2060Sc001SharedService.updateKariageKoho(companyCd, (long)checkDataParamList.get(j).get("candidateNo"));
				//登録に失敗した場合
				if(!(updateKariageKohoCheck))	{
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					System.out.println("updateKariageKohoCheckに失敗(´・ω・｀)");
					return candidateDto;
				}
				System.out.println("updateKariageKohoCheckに成功(´・ω・｀)");
			}
			
			////(CreateKariageBukkenTeiji)おわり
			
		//新規作成フラグがFalseの時
		}else{
			//排他チェック
			Skf2060Sc001GetApplHistoryInfoForUpdateExp applHistoryData = skf2060Sc001SharedService.getApplHistoryInfoForUpdate(companyCd, applNo);
			//該当する「申請書類履歴テーブル」のデータが取得できた場合
			if(applHistoryData != null){
				boolean updateApplHistoryCheck = skf2060Sc001SharedService.updateApplHistory(companyCd, applNo);
				//更新に失敗した場合
				if(updateApplHistoryCheck){
					//エラーメッセージの設定
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					return candidateDto;
				}
				
				//「申請書類管理番号」と「提示回数」をもとに借上候補物件提示明細テーブルに存在する全ての借上候補物件データの「提示フラグ」を"0"(提示可)に更新する。
				boolean updateKariageBukkenTeijiFlgCheck = skf2060Sc001SharedService.updateKariageBukkenTeijiFlg(companyCd, applNo, teijiKaisu);
				//更新に失敗した場合
				if(updateKariageBukkenTeijiFlgCheck){
					//エラーメッセージの設定
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					return candidateDto;
				}
				
				//(CreateKariageBukkenTeiji)
				//借上候補物件提示データの作成を行う
				//借上候補物件提示テーブルへ情報を登録する
				boolean kariageTeijiCheck = skf2060Sc001SharedService.insertKatiageTeiji(companyCd, applNo, teijiKaisu, checkCandidateNo, candidateDate, CodeConstant.DOUBLE_QUOTATION, CodeConstant.DOUBLE_QUOTATION);
				//登録に失敗した場合
				if(!(kariageTeijiCheck)){
					System.out.println("kariageTeijiCheckに失敗(´・ω・｀)");
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					return candidateDto;
				}
				
				//借上候補物件提示明細テーブルに情報を登録する
				//チェックが入っているデータを取得
				List<Map<String, Object>> dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
				List<Map<String, Object>> checkDataParamList = new ArrayList<Map<String, Object>>();
				System.out.println("candidateDto.getTeijiVal().length:"+candidateDto.getTeijiVal().length);
				for(int i = 0; i < candidateDto.getTeijiVal().length; i++){
					int checkDataNum = Integer.parseInt(candidateDto.getTeijiVal()[i]);
					checkDataParamList.add(dataParamList.get(checkDataNum));
				}
				//借上候補物件提示明細テーブルに情報を登録
				for(int j = 0; j < checkDataParamList.size(); j++){
					boolean kariageTeijiDetailCheck = skf2060Sc001SharedService.insertKatiageTeijiDetail(companyCd, applNo, teijiKaisu, (long)checkDataParamList.get(j).get("candidateNo"), (String)checkDataParamList.get(j).get("shatakuName"), (String)checkDataParamList.get(j).get("address"), (String)checkDataParamList.get(j).get("money"), 
													"0", candidateDto.getPresentedNo());
					//登録に失敗した場合
					if(!(kariageTeijiDetailCheck))	{
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						return candidateDto;
					}
				}
				
				//添付ファイル管理テーブル(提示物件)に情報を登録
				for(int j = 0; j < checkDataParamList.size(); j++){
					if(!(checkDataParamList.get(j).get("attachedName").equals(""))){
						boolean kariageTeijiFileCheck = skf2060Sc001SharedService.insertKatiageTeijiFile(companyCd, applNo, teijiKaisu, (long)checkDataParamList.get(j).get("candidateNo"));
						//登録に失敗した場合
						if(!(kariageTeijiFileCheck))	{
							ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
							System.out.println("kariageTeijiFileCheckに失敗(´・ω・｀)");
							return candidateDto;
						}
					}
				}
				
				//借上候補物件番号をもとに借上候補物件テーブルの提示可能フラグを"1"（提示不可）に更新する
				for(int j = 0; j < checkDataParamList.size(); j++){
					boolean updateKariageKohoCheck = skf2060Sc001SharedService.updateKariageKoho(companyCd, (long)checkDataParamList.get(j).get("candidateNo"));
					//登録に失敗した場合
					if(!(updateKariageKohoCheck))	{
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						System.out.println("updateKariageKohoCheckに失敗(´・ω・｀)");
						return candidateDto;
					}
					System.out.println("updateKariageKohoCheckに成功(´・ω・｀)");
				}
				
				////(CreateKariageBukkenTeiji)おわり
				
				
			}
		}
		
		//TODO コメントを取得
		
		//TODO　メールの送信処理
		
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		candidateDto.setListTableData(dataParamList);
		
		//TODO 「借上候補物件状況一覧」に画面遷移する
		//TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf2060Sc004");
		//candidateDto.setTransferPageInfo(prevPage);
		
		return candidateDto;
	}
	
	public short getMaxTeijiNum(String companyCd, String applNo){
		short maxTeijiKaisu = 0;
		
		Skf2060Sc001GetMaxTeijiKaisuExp resultData = new Skf2060Sc001GetMaxTeijiKaisuExp();
		Skf2060Sc001GetMaxTeijiKaisuExpParameter param = new Skf2060Sc001GetMaxTeijiKaisuExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		
		resultData = skf2060Sc001GetMaxTeijiKaisuExpRepository.getMaxTeijiKaisu(param);
		if(resultData != null){
			maxTeijiKaisu = resultData.getTeijiKaisu();
		}
		return maxTeijiKaisu;
	}
	
}
