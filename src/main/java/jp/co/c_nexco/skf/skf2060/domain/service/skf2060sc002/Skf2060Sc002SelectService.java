/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc002;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002GetKariageTeijiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc002.Skf2060Sc002UpdateKariageTeijiExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc002.Skf2060Sc002UpdateKariageTeijiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiDetailRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc002.Skf2060Sc002SelectDto;

/**
 * 借上候補物件確認画面のSelectサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc002SelectService extends BaseServiceAbstract<Skf2060Sc002SelectDto> {
	
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2060TKariageTeijiDetailRepository skf2060TKariageTeijiDetailRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private Skf2060Sc002UpdateKariageTeijiExpRepository skf2060Sc002UpdateKariageTeijiExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2060Sc002GetApplHistoryInfoForUpdateExpRepository skf2060Sc002GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	private Skf2060Sc002SharedService ｓkf2060Sc002SharedService;
	
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
		this.inputCheck(selectDto);
		
		//「選択」ラジオボタンにて選択された物件の借上候補物件番号（選択しないの場合は'0')
		String candidateNo = selectDto.getRadioCandidateNo();
		//申請書類番号
		String applNo = selectDto.getApplNo();
		//更新ステータス
		String updateApplStatus = CodeConstant.NONE;
		//提示回数
		String teijiKaisu = selectDto.getTeijiKaisu();
		//「選択しない理由」ドロップダウン
		String riyu = null;
		//「備考」テキストボックス
		String biko = null;
		
		//ログインセッションのユーザ情報
		Map<String, String> userInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		//ログインセッションユーザ情報のユーザ名
		String updateUserId = userInfoMap.get("userCd");
		String updateProgramId = selectDto.getPageId();
		
		//更新ステータスを設定する
		//「選択しない」ラベルが表示されている行が選択されている場合,更新ステータスを"選択しない"に設定
		if(candidateNo.equals("0")){    //applCgeckFlgとは関係ない
			updateApplStatus = CodeConstant.STATUS_SENTAKU_SHINAI;
			riyu = selectDto.getRiyuDropdown();
			//「備考」テキストボックスに入力があった場合
			if(!(selectDto.getBiko() == null || CheckUtils.isEmpty(selectDto.getBiko().trim()))){
				biko = selectDto.getBiko();
			}
		//「選択しない」ラベル以外が表示されている行が選択されている場合,更新ステータスを"選択済み"に設定
		}else{
			updateApplStatus = CodeConstant.STATUS_SENTAKU_ZUMI;
		}
		
		//申請書類情報を取得(更新用)
		Skf2060Sc002GetApplHistoryInfoForUpdateExp applHistoryData = this.getApplHistoryInfoForUpdate(companyCd, applNo);
		if(applHistoryData == null){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);


		}
		
		// 楽観的排他チェック
        super.checkLockException(selectDto.getLastUpdateDate(selectDto.applHistoryLastUpdateDate), applHistoryData.getUpdateDate());
		
		//申請書類履歴テーブルを更新する
		Map<String, String> applHistoryMap = new HashMap<String, String>();
		applHistoryMap.put("companyCd", companyCd);
		applHistoryMap.put("shainNo", applHistoryData.getShainNo());
		applHistoryMap.put("applNo", applNo);
		applHistoryMap.put("applId", applHistoryData.getApplId());
		applHistoryMap.put("applStatus", updateApplStatus);	
		boolean ahUpdateCheck = this.UpdateApplHistoryAgreeStatus(applHistoryMap, applHistoryData.getApplDate());
		//更新失敗
		if(!(ahUpdateCheck)){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
		}
		
		//借上候補物件提示情報の取得
		List<Skf2060Sc002GetKariageTeijiInfoExp> kariageTeijiList = ｓkf2060Sc002SharedService.getKariageTeijiInfo(companyCd, applNo);
		if(kariageTeijiList.size() <= 0){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
			throwBusinessExceptionIfErrors(selectDto.getResultMessages());
		}
		for(Skf2060Sc002GetKariageTeijiInfoExp kariageTeijiData : kariageTeijiList){
			String applCheckFlg = NOSELECT;
			
			//ラジオボタン行チェック判定
			if(candidateNo.equals(String.valueOf(kariageTeijiData.getCandidateNo()))){
				applCheckFlg = SELECT;
			}
					
			//借上候補物件提示明細テーブルを更新する（「選択しない」ラベル行以外の場合）
			//「選択しない」ラベル行以外の場合（現行だと借上候補物件番号が空じゃない場合）
			if(kariageTeijiData.getCandidateNo() != 0){
				
				// 楽観的排他チェック
				Skf2060TKariageTeijiDetail ktdData = ｓkf2060Sc002SharedService.getKariageTeijiDetailForUpdate(companyCd, applNo, Short.parseShort(teijiKaisu), kariageTeijiData.getCandidateNo());
				super.checkLockException(selectDto.getLastUpdateDate(selectDto.KariageTeijiDetailLastUpdateDate + ktdData.getCandidateNo()), ktdData.getUpdateDate());
				
				Map<String, String> kariageTeijiDetailMap = new HashMap<String, String>();
				kariageTeijiDetailMap.put("companyCd", companyCd);
				kariageTeijiDetailMap.put("applNo", applNo);
				kariageTeijiDetailMap.put("teijiKaisu", teijiKaisu);
				kariageTeijiDetailMap.put("candidateNo", String.valueOf(kariageTeijiData.getCandidateNo()));
				kariageTeijiDetailMap.put("applCheckFlg", applCheckFlg);

				boolean ktdUpdateCheck = this.UpdateKariageTeijiDetail(kariageTeijiDetailMap);
				//更新失敗
				if(!(ktdUpdateCheck)){
					ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}
			}
			
			
			//選択された行の場合
			if(applCheckFlg.equals(SELECT)){
				
				// 楽観的排他チェック
				Skf2060TKariageTeiji ktData = ｓkf2060Sc002SharedService.getKariageTeijiForUpdate(companyCd, applNo, Short.parseShort(teijiKaisu));
				super.checkLockException(selectDto.getLastUpdateDate(selectDto.KariageTeijiLastUpdateDate), ktData.getUpdateDate());
				
				//借上候補物件提示テーブルの更新
				Map<String, String> kariageTeijiMap = new HashMap<String, String>();
				kariageTeijiMap.put("companyCd", companyCd);
				kariageTeijiMap.put("applNo", applNo);
				kariageTeijiMap.put("teijiKaisu", teijiKaisu);
				kariageTeijiMap.put("candidateNo", String.valueOf(kariageTeijiData.getCandidateNo()));
				kariageTeijiMap.put("riyu", riyu);
				kariageTeijiMap.put("biko", biko);
				kariageTeijiMap.put("updateUserId", updateUserId);
				kariageTeijiMap.put("updateProgramId", updateProgramId);
				
				boolean ktUpdateCheck = this.UpdateKariageTeiji(kariageTeijiMap);
				//更新失敗
				if(!(ktUpdateCheck)){
					ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
					throwBusinessExceptionIfErrors(selectDto.getResultMessages());
				}
				
				//選択されていない行の場合
			}else{
				//借上候補物件テーブルの更新を行う
				//提示された物件が選択されなかったため、同じ物件を他の人に提示できるように、提示フラグを0:選択可に更新する
				//「選択しない」ラベル行以外の場合（現行だと借上候補物件番号が空じゃない場合）
				if(kariageTeijiData.getCandidateNo() != 0){
					
					// 楽観的排他チェック
					Skf2060TKariageBukken kbData = ｓkf2060Sc002SharedService.getKariageBukkenForUpdate(companyCd, (kariageTeijiData.getCandidateNo()));
					super.checkLockException(selectDto.getLastUpdateDate(selectDto.KariageBukkenLastUpdateDate + kbData.getCandidateNo()), kbData.getUpdateDate());
					
					Map<String, String> kariageBukkenMap = new HashMap<String, String>();
					kariageBukkenMap.put("companyCd", companyCd);
					kariageBukkenMap.put("candidateNo", String.valueOf(kariageTeijiData.getCandidateNo()));
					kariageBukkenMap.put("teijiFlg", "0");

					boolean kbUpdateCheck = this.UpdateKariageBukken(kariageBukkenMap);
					//更新失敗
					if(!(kbUpdateCheck)){
						ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1078);
						throwBusinessExceptionIfErrors(selectDto.getResultMessages());
					}	
				}
			}
		}
		
		// 申請状況一覧画面へ遷移させる
		TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf2010Sc003");
		selectDto.setTransferPageInfo(prevPage);
		
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
		//いずれの物件の「選択」ラジオボタンも選択されていない場合
		if(selectDto.getRadioCandidateNo() == null){
			ServiceHelper.addErrorResultMessage(selectDto, null, MessageIdConstant.E_SKF_1054, "物件");
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
	public boolean UpdateKariageTeijiDetail(Map<String, String> kariageTeijiDetailMap){
		boolean updateCheck = true;
		int updateCount = 0;
		Skf2060TKariageTeijiDetail updateData = new Skf2060TKariageTeijiDetail();
		updateData.setCompanyCd(kariageTeijiDetailMap.get("companyCd"));
		updateData.setApplNo(kariageTeijiDetailMap.get("applNo"));
		updateData.setTeijiKaisu(Short.parseShort(kariageTeijiDetailMap.get("teijiKaisu")));
		updateData.setCandidateNo(Long.parseLong(kariageTeijiDetailMap.get("candidateNo")));
		updateData.setApplCheckFlg(kariageTeijiDetailMap.get("applCheckFlg"));
		
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
		Skf2060Sc002UpdateKariageTeijiExp updateData = new Skf2060Sc002UpdateKariageTeijiExp();
		updateData.setCompanyCd(kariageTeijiMap.get("companyCd"));
		updateData.setApplNo(kariageTeijiMap.get("applNo"));
		updateData.setTeijiKaisu(Short.parseShort(kariageTeijiMap.get("teijiKaisu")));
		updateData.setCheckCandidateNo(Long.parseLong(kariageTeijiMap.get("candidateNo")));
		updateData.setRiyu(kariageTeijiMap.get("riyu"));
		updateData.setBiko(kariageTeijiMap.get("biko"));
		updateData.setUpdateUserId(kariageTeijiMap.get("updateUserId"));
		updateData.setUpdateProgramId(kariageTeijiMap.get("updateProgramId"));
		
		updateCount = skf2060Sc002UpdateKariageTeijiExpRepository.updateKariageTeiji(updateData);
		
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
