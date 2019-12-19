/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc003;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukkenKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc003.Skf2060Sc003GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc003.Skf2060Sc003CompleteDto;

/**
 * 借上候補物件提示確認画面のCompleteサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc003CompleteService extends BaseServiceAbstract<Skf2060Sc003CompleteDto> {
	
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private Skf2060Sc003GetApplHistoryInfoForUpdateExpRepository skf2060Sc003GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。　
	 * 
	 * @param CompleteDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2060Sc003CompleteDto index(Skf2060Sc003CompleteDto completeDto) throws Exception {
		
		completeDto.setPageTitleKey(MessageIdConstant.SKF2060_SC003_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("完了", companyCd, completeDto.getPageId());
		
        // 提示状況汎用コード取得
        Map<String, String> candidateStatusGenCodeReverseMap = new HashMap<String, String>();
        candidateStatusGenCodeReverseMap = skfGenericCodeUtils.getGenericCodeReverse(FunctionIdConstant.GENERIC_CODE_STATUS);
		
		
		//「選択」ラジオボタンにて選択された物件の借上候補物件番号（選択しないの場合は'0')
		String candidateNo = completeDto.getRadioCandidateNo();
		//申請書類番号
		String applNo = completeDto.getApplNo();
		//申請状況(番号)
		String applStatus = candidateStatusGenCodeReverseMap.get(completeDto.getApplStatus());
		
		//申請書類情報を取得(更新用)
		Skf2060Sc003GetApplHistoryInfoForUpdateExp applHistoryData = this.getApplHistoryInfoForUpdate(companyCd, applNo);
		if(applHistoryData == null){
			ServiceHelper.addErrorResultMessage(completeDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(completeDto.getResultMessages());
		}
		
		// 楽観的排他チェック
        super.checkLockException(completeDto.getLastUpdateDate(completeDto.applHistoryLastUpdateDate), applHistoryData.getUpdateDate());
		
		//申請書類履歴テーブルを更新する
		Map<String, String> applHistoryMap = new HashMap<String, String>();
		applHistoryMap.put("companyCd", companyCd);
		applHistoryMap.put("shainNo", applHistoryData.getShainNo());
		applHistoryMap.put("applNo", applNo);
		applHistoryMap.put("applId", applHistoryData.getApplId());
		applHistoryMap.put("applStatus", CodeConstant.STATUS_KANRYOU);	
		boolean ahUpdateCheck = this.UpdateApplHistoryAgreeStatus(applHistoryMap, applHistoryData.getApplDate());
		//更新失敗
		if(!(ahUpdateCheck)){
			ServiceHelper.addErrorResultMessage(completeDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(completeDto.getResultMessages());
		}
		
		//申請状況が"28"（選択済）の場合、提示物件を他の社員に提示不可能にする処理
		if(applStatus.equals(CodeConstant.STATUS_SENTAKU_ZUMI)){
			List<Map<String, String>> kariageTeijiList = completeDto.getKariageTeijiList();
			//「選択しない」を含む行は処理を飛ばす
			kariageTeijiList.remove(kariageTeijiList.size() - 1);
			
			for(Map<String, String> kariageTeijiData:kariageTeijiList){
				//未選択行の場合、提示フラグを"0"(選択可)
				String teijiFlg = CodeConstant.TEIJI_FLG_SELECTABLE;
				//選択行の場合、提示フラグを"2"(選択済)
				if(candidateNo.equals(kariageTeijiData.get("candidateNo"))){
					teijiFlg = CodeConstant.TEIJI_FLG_SELECTED;
				}

				// 楽観的排他チェック
				long longCandidateNo = Long.parseLong(kariageTeijiData.get("candidateNo"));
				Skf2060TKariageBukken kbData = this.getKariageBukkenForUpdate(companyCd, longCandidateNo);
				if(kbData != null){
					super.checkLockException(completeDto.getLastUpdateDate(completeDto.KariageBukkenLastUpdateDate + longCandidateNo), kbData.getUpdateDate());
				}
				//借上候補物件テーブルの更新を行う
				Map<String, String> kariageBukkenMap = new HashMap<String, String>();
				kariageBukkenMap.put("companyCd", companyCd);
				kariageBukkenMap.put("candidateNo", kariageTeijiData.get("candidateNo"));
				kariageBukkenMap.put("teijiFlg", teijiFlg);

				boolean kbUpdateCheck = this.UpdateKariageBukken(kariageBukkenMap);
				//更新失敗
				if(!(kbUpdateCheck)){
					ServiceHelper.addErrorResultMessage(completeDto, null, MessageIdConstant.E_SKF_1075);
					throwBusinessExceptionIfErrors(completeDto.getResultMessages());
				}
			}
			
		}
		
		//「借上候補物件状況一覧」に画面遷移する
		TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf2060Sc004");
		completeDto.setTransferPageInfo(prevPage);
		
		return completeDto;

	}
	
	/**
	 * 申請書類管理テーブルから情報が取得する(更新用）
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return 申請書類管理情報
	 */
	public Skf2060Sc003GetApplHistoryInfoForUpdateExp getApplHistoryInfoForUpdate(String companyCd, String applNo){
		
		Skf2060Sc003GetApplHistoryInfoForUpdateExp resultData = new Skf2060Sc003GetApplHistoryInfoForUpdateExp();
		Skf2060Sc003GetApplHistoryInfoForUpdateExpParameter param = new Skf2060Sc003GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		
		resultData = skf2060Sc003GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		
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
	 * 借上候補物件情報を取得する（更新用)
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * @return 借上候補物件情報
	 */
	public Skf2060TKariageBukken getKariageBukkenForUpdate(String companyCd, long candidateNo){
		
		//借上候補物件情報の取得
		Skf2060TKariageBukken kariageBukkenData = new Skf2060TKariageBukken();
		Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
		key.setCompanyCd(companyCd);
		key.setCandidateNo(candidateNo);
		kariageBukkenData = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);

		return kariageBukkenData;
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
