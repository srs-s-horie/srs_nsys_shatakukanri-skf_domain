/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc004.Skf2010Sc004InsertBihinHenkyakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001PostalCodeAddressExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc004.Skf3090Sc004GetListTableDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008SelectDto;
import jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008.Skf2010Sc008SharedService;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InsertKariageDto;

/**
 * TestPrjTop画面のSearchAddressサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001InsertKariageService extends BaseServiceAbstract<Skf2060Sc001InsertKariageDto> {
	
	@Autowired
	private Skf2060Sc001GetKariageBukkenExpRepository skf2060Sc001GetKariageBukkenExpRepository;
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	
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
	public Skf2060Sc001InsertKariageDto index(Skf2060Sc001InsertKariageDto insertDto) throws Exception {
		
		insertDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		boolean insertCheck = false;
		
		if(isValidateInput(insertDto)){
			if(isValidateRepeat(insertDto)){
				insertCheck = skf2060Sc001SharedService.insertKariageKohoInfo(companyCd, insertDto.getShatakuName(), insertDto.getAddress());
			}
		}
		
		if(!(insertCheck)){
			System.out.println("insertできてないだとぉ");
		}else{
			System.out.println("よっしゃあああああああああああ");
			insertDto.setShatakuName("");
			insertDto.setAddress("");
		}
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		insertDto.setListTableData(dataParamList);
		
		return insertDto;
	}
	
	/**
	 * 入力チェックを行う。.
	 * 
	 * @param insertDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める。
	 * @return 入力チェック結果。trueの場合チェックOK。
	 */
	public boolean isValidateInput(Skf2060Sc001InsertKariageDto insertDto){
		boolean inputCheck = true;
		
		insertDto.setShatakuNameError("");
		insertDto.setAddressError("");
		
		//借上社宅名の未入力チェック
		if(insertDto.getShatakuName() == null || CheckUtils.isEmpty(insertDto.getShatakuName().trim())){
			ServiceHelper.addErrorResultMessage(insertDto, null, MessageIdConstant.E_SKF_1048, "借上社宅名");
			insertDto.setShatakuNameError(CodeConstant.NFW_VALIDATION_ERROR);
			inputCheck = false;
		}
		//住所の未入力チェック
		if(insertDto.getAddress() == null || CheckUtils.isEmpty(insertDto.getAddress().trim())){
			ServiceHelper.addErrorResultMessage(insertDto, null, MessageIdConstant.E_SKF_1048, "住所");
			insertDto.setAddressError(CodeConstant.NFW_VALIDATION_ERROR);
			inputCheck = false;
		}
	    return inputCheck;
	}
	
	/**
	 * 重複チェックを行う。.
	 * 
	 * @param insertDto *indexメソッドの引数のDTO。入力チェックエラーの場合、エラーメッセージを詰める?
	 * @return 入力チェック結果。trueの場合チェックOK。
	 */
	public boolean isValidateRepeat(Skf2060Sc001InsertKariageDto insertDto){
		boolean repeatCheck = true;
		
		List<Skf2060Sc001GetKariageBukkenExp> resultListTableData = new ArrayList<Skf2060Sc001GetKariageBukkenExp>();
		Skf2060Sc001GetKariageBukkenExpParameter param = new Skf2060Sc001GetKariageBukkenExpParameter();
		param.setCompanyCd(companyCd);
		param.setShatakuName(insertDto.getShatakuName());
		param.setAddress(insertDto.getAddress());
		param.setItiranFlg(false);
		
		resultListTableData = skf2060Sc001GetKariageBukkenExpRepository.getKariageBukken(param);
		//借上候補物件テーブルに重複するデータが存在する場合
		if(resultListTableData != null){
			//多分１件しか出ない
			for(int i = 0; i < resultListTableData.size(); i++){
				//提示可能or提示済の物件であれば重複エラーメッセージを表示する
				if(resultListTableData.get(i).getTeijiFlg().equals("0")||resultListTableData.get(i).getTeijiFlg().equals("1")){
					ServiceHelper.addErrorResultMessage(insertDto, null, MessageIdConstant.E_SKF_1033,"借上社宅名");
					repeatCheck = false;
				//選択済の物件であれば確認メッセージを表示したいなぁ
				}else if(resultListTableData.get(i).getTeijiFlg().equals("2")){
				//TODO 確認ダイアログを表示するようななにかをしたい
					System.out.println("選択済物件(´・ω・｀)");
				}
			}
		}	
		return repeatCheck;
	}
}
