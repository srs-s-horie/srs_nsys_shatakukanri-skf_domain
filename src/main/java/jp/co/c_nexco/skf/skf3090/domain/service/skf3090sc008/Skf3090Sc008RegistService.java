/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc008;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TInformation;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010TInformationRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc008.Skf3090Sc008RegistDto;

/**
 * お知らせマスタメンテナンス画面のRegistサービス処理クラス。　 
 * 
 */
@Service
public class Skf3090Sc008RegistService extends SkfServiceAbstract<Skf3090Sc008RegistDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3090Sc008SharedService skf3090Sc008SharedService;
	@Autowired
	private Skf1010TInformationRepository skf1010TInformationRepository;
	// 会社コード
	private String companyCd = CodeConstant.C001;

	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param registDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3090Sc008RegistDto index(Skf3090Sc008RegistDto registDto) throws Exception {
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("お知らせ登録", companyCd, FunctionIdConstant.SKF3090_SC008);
		
		//更新日を設定
		Map<String, Date> lastUpdateDateMap = registDto.getLastUpdateDateMap();
		
		String openDate = registDto.getOpenDateBox().replace("/", "");
		String note = registDto.getNote();
		boolean dialogFlg = Boolean.valueOf(registDto.getDialogFlg());

		if(!(registDto.getNote() == null || CheckUtils.isEmpty(registDto.getNote().trim()))){				
			LogUtils.debugByMsg("お知らせ内容："+note);
			LogUtils.debugByMsg("お知らせ内容文字数："+note.length());
			
			String escapeNote = note.replaceAll("\\<.*?>","").replaceAll("\\&.*?;"," ");
			LogUtils.debugByMsg("お知らせ内容（エスケープ)："+escapeNote);
			LogUtils.debugByMsg("お知らせ内容文字数（エスケープ)："+escapeNote.length());
			//「お知らせ内容」桁数チェック
			if(NfwStringUtils.isNotEmpty(escapeNote) && CheckUtils.isMoreThanByteSize(note, 4000)){
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1071, "お知らせ","2000");
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
		}
		
		//公開開始日が重複していた場合
		if(dialogFlg){
			LogUtils.debugByMsg("お知らせテーブル更新：	公開開始日:" + openDate + "    内容:" + note);
			
			//楽観的排他チェック
			Skf1010TInformation informationData = skf3090Sc008SharedService.getInformation(companyCd, openDate);
			if(informationData == null){
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1134, "");
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			super.checkLockException(registDto.getLastUpdateDate(registDto.informationLastUpdateDate + openDate), informationData.getUpdateDate());
			
			//お知らせテーブル更新
			boolean updateCheck = this.updateInformationData(companyCd, openDate, note);
			//更新失敗
			if(!(updateCheck)){
		 		ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
		 		throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			
			//楽観的排他チェック用更新日時を更新
			Skf1010TInformation informationDataAfterUpdate = skf3090Sc008SharedService.getInformation(companyCd, openDate);
			lastUpdateDateMap.put(registDto.informationLastUpdateDate + openDate, informationDataAfterUpdate.getUpdateDate());
			
		//公開開始日が重複していなかった場合
		}else{
			LogUtils.debugByMsg("お知らせテーブル登録：	公開開始日:" + openDate + "    内容:" + note);
			//お知らせテーブル登録
			boolean insertCheck = this.insertInformationData(companyCd, openDate, note);
			//更新失敗
			if(!(insertCheck)){
		 		ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);
		 		throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
		}
		
		//お知らせリストテーブルの設定
		List<Map<String, Object>> informationdataListMap = skf3090Sc008SharedService.getInformationDataListMap(companyCd);
		registDto.setListTableData(informationdataListMap);
		
		//「公開開始日」とお知らせリッチエディターの内容を空白に
		registDto.setOpenDateBox("");
		registDto.setNote("");
		
		registDto.setLastUpdateDateMap(lastUpdateDateMap);
		
		return registDto;
	}
	
	/**
	 * お知らせテーブルへ更新を行う
	 * 
	 * @param companyCd
	 * @param openDate
	 * 
	 * @return 更新できた場合true　更新できなかった場合false
	 */
	public boolean updateInformationData(String companyCd, String openDate, String note){

		Skf1010TInformation informationData = new Skf1010TInformation();
		informationData.setCompanyCd(companyCd);
		informationData.setOpenDate(openDate);
		informationData.setNote(note);
		int updateCount = skf1010TInformationRepository.updateByPrimaryKeySelective(informationData);
		
		if (updateCount <= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * お知らせテーブルへ登録を行う
	 * 
	 * @param companyCd
	 * @param openDate
	 * 
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertInformationData(String companyCd, String openDate, String note){

		Skf1010TInformation informationData = new Skf1010TInformation();
		informationData.setCompanyCd(companyCd);
		informationData.setOpenDate(openDate);
		informationData.setNote(note);
		int insertCount = skf1010TInformationRepository.insertSelective(informationData);
		
		if (insertCount <= 0) {
			return false;
		}
		return true;
	}
}
