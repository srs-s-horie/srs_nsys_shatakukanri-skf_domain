/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc001;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc001.Skf3020Sc001GetNowShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc001.Skf3020Sc001GetNowShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc001.Skf3020Sc001GetNowShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc001.Skf3020Sc001InitDto;

/**
 * Skf3020Sc001InitService 現社宅照会画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc001InitService extends BaseServiceAbstract<Skf3020Sc001InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;	
		
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;	

	@Autowired
	private Skf3020Sc001GetNowShatakuInfoExpRepository skf3020Sc001GetNowShatakuInfoExpRepository;	
	
	// 都道府県コード(その他)
	private final String CD_PREF_OTHER = "48";
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc001InitDto index(Skf3020Sc001InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// 社員情報の設定
		// - 社員番号
		String shaiNoStr = initDto.getHdnRowShainNo();
		int indexAster = shaiNoStr.indexOf(CodeConstant.ASTERISK);
		if(indexAster >= 0){
			shaiNoStr = shaiNoStr.replace(CodeConstant.ASTERISK, "");
		}		
		initDto.setShainNo(shaiNoStr);
		
		// - 社員氏名
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(CodeConstant.C001);
		key.setShainNo(shaiNoStr);
		Skf1010MShain resultValue = skf1010MShainRepository.selectByPrimaryKey(key);
		if(NfwStringUtils.isNotEmpty(resultValue.getName())){
			initDto.setShainName(resultValue.getName());
		}
		
		// 現社宅情報の取得
		int resultCount = SetGenShatakuData(initDto, shaiNoStr);
		
		if(resultCount == 0){
			// 取得レコード0件のエラー
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
		}

		return initDto;
	}
	
	/**
	 * 現社宅情報の取得
	 * 
	 * @param initDto
	 * @param shaiNoStr
	 */	
	private int SetGenShatakuData(Skf3020Sc001InitDto initDto, String shaiNoStr){
		
		// 社宅区分の取得
		Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository.selectByPrimaryKey(shaiNoStr);
		String nowShatakuKbn = null;
		if(tenninshaInfo != null){
			nowShatakuKbn = tenninshaInfo.getNowShatakuKbn();
		}
		
		// 現社宅照会情報取得
		// システム年月を取得
		String sysDate = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		List<Skf3020Sc001GetNowShatakuInfoExp> resultListTableData = new ArrayList<Skf3020Sc001GetNowShatakuInfoExp>();
		Skf3020Sc001GetNowShatakuInfoExpParameter param = new Skf3020Sc001GetNowShatakuInfoExpParameter();
		param.setShainNo(shaiNoStr);
		param.setNowShatakuKbn(nowShatakuKbn);
		param.setSystemProcessNenGetsu(sysDate);
		resultListTableData = skf3020Sc001GetNowShatakuInfoExpRepository.getGenShatakuInfo(param);
		
		// 取得データレコード数判定
		int resultCount = resultListTableData.size();
		if(resultCount == 0){
			return resultCount;
		}
		
		// 居住者区分
		Map<String, String> kyojyuKbnList = new HashMap<String, String>();
		kyojyuKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KYOJUSHA_KBN);
		// 社宅区分
		Map<String, String> shatakuKbnList = new HashMap<String, String>();
		shatakuKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN);
		// 用途
		Map<String, String> auseKbnList = new HashMap<String, String>();
		auseKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);
		// 規格区分
		Map<String, String> kikakuKbnList = new HashMap<String, String>();
		kikakuKbnList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
		// 都道府県
		Map<String, String> prefCdList = new HashMap<String, String>();
		prefCdList = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);
		
		// 画面に検索結果を設定（1件 or 2件）
		for (int rowIndex = 0; rowIndex < resultListTableData.size(); rowIndex++){
			Skf3020Sc001GetNowShatakuInfoExp nowShatakuInfoRowData = new Skf3020Sc001GetNowShatakuInfoExp();
			nowShatakuInfoRowData = resultListTableData.get(rowIndex);
			
			// 居住者区分
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getKyojushaKbn())){
				if(rowIndex > 0){
					initDto.setRemove(false); // 2つ目を表示する
					initDto.setJyukyoKbn2(kyojyuKbnList.get(nowShatakuInfoRowData.getKyojushaKbn()));
				}else{
					initDto.setRemove(true);
					initDto.setJyukyoKbn(kyojyuKbnList.get(nowShatakuInfoRowData.getKyojushaKbn()));
				}
			}
			// 社宅区分
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getShatakuKbn())){
				if(rowIndex > 0){
					initDto.setShatakuKbn2(shatakuKbnList.get(nowShatakuInfoRowData.getShatakuKbn()));
				}else{
					initDto.setShatakuKbn(shatakuKbnList.get(nowShatakuInfoRowData.getShatakuKbn()));
				}
			}
			// 社宅名
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getShatakuName())){
				if(rowIndex > 0){
					initDto.setShatakuName2(nowShatakuInfoRowData.getShatakuName());
				}else{
					initDto.setShatakuName(nowShatakuInfoRowData.getShatakuName());
				}
			}
			// 住所
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getPrefCd())){
				// 都道府県名を取得
				String prefName = prefCdList.get(nowShatakuInfoRowData.getPrefCd());
				String addressStr = "";
				if(!NfwStringUtils.isNotEmpty(prefName) || !prefName.equals(CD_PREF_OTHER)){
					addressStr = prefName + nowShatakuInfoRowData.getAddress();
				}else{
					addressStr = nowShatakuInfoRowData.getAddress();
				}
				if(rowIndex > 0){
					initDto.setAddress2(addressStr);
				}else{
					initDto.setAddress(addressStr);
				}
			}
			// 部屋番号
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getRoomNo())){
				if(rowIndex > 0){
					initDto.setRoomNo2(nowShatakuInfoRowData.getRoomNo());
				}else{
					initDto.setRoomNo(nowShatakuInfoRowData.getRoomNo());
				}
			}
			// 用途
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getAuse())){
				if(rowIndex > 0){
					initDto.setAuse2(auseKbnList.get(nowShatakuInfoRowData.getAuse()));
				}else{
					initDto.setAuse(auseKbnList.get(nowShatakuInfoRowData.getAuse()));
				}
			}
			// 規格
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getKikaku())){
				if(rowIndex > 0){
					initDto.setKikaku2(kikakuKbnList.get(nowShatakuInfoRowData.getKikaku()));
				}else{
					initDto.setKikaku(kikakuKbnList.get(nowShatakuInfoRowData.getKikaku()));
				}
			}
			// 面積（###.##㎡）
			if(NfwStringUtils.isNotEmpty(nowShatakuInfoRowData.getMenseki())){
				String menseki = nowShatakuInfoRowData.getMenseki() + SkfCommonConstant.SQUARE_MASTER;
				if(rowIndex > 0){
					initDto.setMenseki2(menseki);
				}else{
					initDto.setMenseki(menseki);
				}
			}
		}
		return resultCount;
	}
	
}
