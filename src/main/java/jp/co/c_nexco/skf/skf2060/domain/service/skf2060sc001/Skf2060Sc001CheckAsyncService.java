/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetTeijiShainDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetTeijiShainDataExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetTeijiShainDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001CheckAsyncDto;

/**
 * TestPrjTop画面のCheckAsyncサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001CheckAsyncService extends AsyncBaseServiceAbstract<Skf2060Sc001CheckAsyncDto> {
	
	@Autowired
	private Skf2060Sc001GetKariageBukkenExpRepository skf2060Sc001GetKariageBukkenExpRepository;
	@Autowired
	private Skf2060Sc001GetTeijiShainDataExpRepository skf2060Sc001GetTeijiShainDataExpRepository;
	
	private String companyCd = CodeConstant.C001;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param checkDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001CheckAsyncDto index(Skf2060Sc001CheckAsyncDto checkDto) throws Exception {
	
		boolean checkFlg = false;
		boolean dialogFlg = false;
		
		boolean inputCheck = isValidateInput(checkDto);
		
		//入力チェック
		if(inputCheck){
			//入力チェックOK時
			int repeatCheck = isValidateRepeat(checkDto);
			//重複チェック
			if(repeatCheck == 0){
				//重複チェックOK時
				checkFlg = true;
			}else if(repeatCheck == 1){
				//重複チェックNG時(提示可or提示済(提示不可)の物件)
			}else if(repeatCheck == 2){
				//重複チェックNG時(選択済の物件)
				checkFlg = true;
				dialogFlg = true;
			}
		}
		
		if(!checkFlg){
			throwBusinessExceptionIfErrors(checkDto.getResultMessages());
		}
		
		checkDto.setDialogFlg(dialogFlg);
		
		return checkDto;
	}
	
	/**
	 * 入力チェックを行う。.
	 * 
	 * @param checkDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 * @throws UnsupportedEncodingException 
	 */
	public boolean isValidateInput(Skf2060Sc001CheckAsyncDto checkDto) throws UnsupportedEncodingException{
		boolean inputCheck = true;
		
		//借上社宅名の未入力チェック
		if(checkDto.getShatakuName() == null || CheckUtils.isEmpty(checkDto.getShatakuName().trim())){
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "shatakuName" }, MessageIdConstant.E_SKF_1048, "借上社宅名");
			inputCheck = false;
		//借上社宅名の桁数チェック
		}else if(CheckUtils.isMoreThanByteSize(checkDto.getShatakuName().trim(), 30)){
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "shatakuName" }, MessageIdConstant.E_SKF_1071, "借上社宅名","15");
			inputCheck = false;
		}
		
		//住所の未入力チェック
		if(checkDto.getAddress() == null || CheckUtils.isEmpty(checkDto.getAddress().trim())){
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "address" }, MessageIdConstant.E_SKF_1048, "社宅所在地");
			inputCheck = false;
		//住所の桁数チェック
		}else if(CheckUtils.isMoreThanByteSize(checkDto.getAddress().trim(), 100)){
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "address" }, MessageIdConstant.E_SKF_1071, "社宅所在地","50");
			inputCheck = false;
		}
	    return inputCheck;
	}
	
	/**
	 * 重複チェックを行う。.
	 * 
	 * @param checkDto *indexメソッドの引数のDTO。重複チェックエラーの場合、エラーメッセージを詰める
	 * @return 重複チェック結果。0:チェックOK,1:重複チェックNG(提示可or提示済),2:重複チェックNG(選択済)
	 */
	public int isValidateRepeat(Skf2060Sc001CheckAsyncDto checkDto){
		int repeatNum = 0;
		
		//借上候補物件テーブルから重複するデータを取得する
		List<Skf2060Sc001GetKariageBukkenExp> resultListTableData = new ArrayList<Skf2060Sc001GetKariageBukkenExp>();
		Skf2060Sc001GetKariageBukkenExpParameter param = new Skf2060Sc001GetKariageBukkenExpParameter();
		param.setCompanyCd(companyCd);
		param.setShatakuName(checkDto.getShatakuName());
		param.setAddress(checkDto.getAddress());
		param.setItiranFlg(false);
		resultListTableData = skf2060Sc001GetKariageBukkenExpRepository.getKariageBukken(param);
		
		//借上候補物件テーブルに重複するデータが存在する場合
		if(resultListTableData.size() > 0){
			//借上候補物件提示明細テーブルから重複する物件を取得する
			List<Skf2060Sc001GetTeijiShainDataExp> shainData = new ArrayList<Skf2060Sc001GetTeijiShainDataExp>();
			Skf2060Sc001GetTeijiShainDataExpParameter param2 = new Skf2060Sc001GetTeijiShainDataExpParameter();
			param2.setCompanyCd(companyCd);
			param2.setCandidateNo(resultListTableData.get(0).getCandidateNo());
			shainData = skf2060Sc001GetTeijiShainDataExpRepository.getTeijiShainData(param2);
			//借上候補物件提示明細テーブルに重複する物件が存在する場合
			if(shainData.size() > 0){
				//重複物件の提示されている社員名を取得
				checkDto.setDialogShainName(shainData.get(0).getName());
			}
			//提示可or提示済(提示不可)の物件の場合
			if(resultListTableData.get(0).getTeijiFlg().equals(CodeConstant.TEIJI_FLG_SELECTABLE)||resultListTableData.get(0).getTeijiFlg().equals(CodeConstant.TEIJI_FLG_SELECT_IMPOSSIBLE)){
				//エラーメッセージを表示する
				ServiceHelper.addErrorResultMessage(checkDto, null, MessageIdConstant.E_SKF_1033,"借上社宅名");
				repeatNum = 1;
			//選択済の物件の場合
			}else if(resultListTableData.get(0).getTeijiFlg().equals(CodeConstant.TEIJI_FLG_SELECTED)){
				repeatNum = 2;
			}
		}
		return repeatNum;
	}
}
