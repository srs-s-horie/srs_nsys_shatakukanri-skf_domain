/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoJissekiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoRouterJissekiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001GetShatakuShainShozokuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001UpdateShatakuShainNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShozokuRireki;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoJissekiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001GetShainBangoRouterJissekiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001GetShatakuShainShozokuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001UpdateShainNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc001.Skf3050Sc001RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3050Sc001ShainConfirmService 社員番号一括設定の登録サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc001RegistService extends SkfServiceAbstract<Skf3050Sc001RegistDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3050Sc001SharedService skf3050Sc001SharedService;
	@Autowired
	private Skf3050Sc001GetShainBangoJissekiCountExpRepository skf3050Sc001GetShainBangoJissekiCountExpRepository;
	@Autowired
	private Skf3050Sc001GetShainBangoRouterJissekiCountExpRepository skf3050Sc001GetShainBangoRouterJissekiCountExpRepository;
	@Autowired
	private Skf3050Sc001UpdateShainNoExpRepository skf3050Sc001UpdateShainNoExpRepository;
	@Autowired
	private Skf3050Sc001GetShatakuShainShozokuInfoExpRepository skf3050Sc001GetShatakuShainShozokuInfoExpRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	
	//旧社員フラグ
	private static final String OLD = "1";
	//仮社員フラグ
	private static final String KARI = "2";
	//一括変更後の社員（HR連携データ確定処理されるまで ）フラグ
	private static final String OLD2 = "3";
	//本人同居
	private static final String HONNIN_DOKYO = "1";
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	@Transactional
	public Skf3050Sc001RegistDto index(Skf3050Sc001RegistDto registDto) throws Exception {
		 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, FunctionIdConstant.SKF3050_SC001);
		
		Map<String,Object> resultMap = updateShainNo(registDto.getShainListData());
		int resultNo = Integer.parseInt(resultMap.get("errorNo").toString());
		if(resultNo == -1){
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1075);
		}else if(resultNo == -2){
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
		}else if(resultNo == -3){
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3036, resultMap.get("errorShainNo").toString());
		}else{
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
		}
		throwBusinessExceptionIfErrors(registDto.getResultMessages());
		
		//検索結果一覧用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		int count = skf3050Sc001SharedService.setGrvShainBangoList(listTableData);
		
		if(count == 0){
			//社員情報確認ボタンを非活性
			registDto.setBtnShainInfoCheckDisabled("true");
		}else{
			//社員情報確認ボタンを活性
			registDto.setBtnShainInfoCheckDisabled("false");
		}
		
		//登録ボタンを非活性に設定
		registDto.setBtnRegistDisabled("true");
		
		registDto.setListTableData(listTableData);
		registDto.setListPage("1");
		
		return registDto;
	}
	
	/**
	 * 登録処理メソッド
	 * @param shainListData
	 * @return 更新件数/エラー番号
	 */
	private Map<String,Object> updateShainNo(String shainListData){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		
		//提示データ更新件数
		int updateCountTJ = 0;
		//転任者調書データ更新件数
		int updateCountTC = 0;
		//社宅管理台帳基本更新件数
		int updateCountSL = 0;
		//入退居予定データ更新件数
		int updateCountNY = 0;
		//社宅社員マスタ更新件数
		int updateCountSS = 0;
		//所属履歴データ更新件数
		int updateCountSR = 0;
		//モバイルルーター貸出管理簿更新件数
		int updateCountRL = 0;
		
		//更新件数
		int count = 0;
		
		
		//社員情報確認処理
		List<Map<String,Object>> grvShainBangoList = createShainInfoList(shainListData);
		
		for(Map<String,Object> row : grvShainBangoList){
			//社員番号
			String shainNo = row.get("shainNo").toString();
			//社員氏名
			//String shainName = row.get("shainName").toString();
			//社員番号（txt）
			String txtShainNo = row.get("txtShainNo").toString();
			//社員氏名（従業員）
			//String shainNameJugyoin = row.get("shainNameJugyoin").toString();
			//所属（従業員）
			//String shozokuJugyoin = row.get("shozokuJugyoin").toString();
			//社員番号変更フラグ
			//String shainNoChangeFlg = row.get("shainNoChangeFlg").toString();
			//社宅管理台帳ID
			String shatakuKanriId = row.get("shatakuKanriId").toString();
			//会社コード
			String companyCd = row.get("companyCd").toString();
			//社員フラグ
			String flg = row.get("flg").toString();
			//退居日
			String taikyoDate = row.get("taikyoDate").toString();
			//居住者区分
			String kyojushaKbn = row.get("kyojushaKbn").toString();
			
			//更新日（社宅管理台帳基本）
			String updateDateSL = row.get("updateDateSL").toString();
			//更新日（社宅社員マスタ）
			String updateDateSS= row.get("updateDateSS").toString();
			//更新日(従業員)
			String updateDateNew = row.get("updateDateNew").toString();
			//対象のテキストボックス名
			//String boxName = row.get("boxName").toString();
			
			//システム日付の取得
			SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
			String sysDate = dateFormatYYYYMMDD.format(skfBaseBusinessLogicUtils.getSystemDateTime());
			
			//社員番号が未入力の場合
			if(SkfCheckUtils.isNullOrEmpty(txtShainNo)){
				continue;
			}
			
			//社宅管理台帳基本テーブルの社員番号存在チェック
			List<Skf3050Sc001GetShainBangoJissekiCountExp> taSL = new ArrayList<Skf3050Sc001GetShainBangoJissekiCountExp>();
			taSL = skf3050Sc001GetShainBangoJissekiCountExpRepository.getShainBangoJissekiCount(txtShainNo);
			if(taSL.size() > 0){
				resultMap.put("errorShainNo", txtShainNo);
				resultMap.put("errorNo", -3);
				return resultMap;
			}
			
			// 202109 モバイルルーター機能追加対応 add start
			//モバイルルーター貸出管理簿テーブルの社員番号存在チェック
			List<Skf3050Sc001GetShainBangoRouterJissekiCountExp> taRL = new ArrayList<Skf3050Sc001GetShainBangoRouterJissekiCountExp>();
			taRL = skf3050Sc001GetShainBangoRouterJissekiCountExpRepository.getShainBangoRouterJissekiCount(txtShainNo);
			if(taRL.size() > 0){
				resultMap.put("errorShainNo", txtShainNo);
				resultMap.put("errorNo", -3);
				return resultMap;
			}
			// 202109 モバイルルーター機能追加対応 add end
			
			//社員番号
			String shainBangoStr = CodeConstant.DOUBLE_QUOTATION;
			if(shainNo.endsWith(CodeConstant.ASTERISK)){
				shainBangoStr = shainNo.replace(CodeConstant.ASTERISK, "");
			}else{
				shainBangoStr = shainNo;
			}
			
			//更新者情報
			String updateUserId = LoginUserInfoUtils.getUserCd();
			String updateProgramId = "Skf3050Sc001";
			
			//仮社員／旧社員（入居&本人同居）／一括変更後の社員（HR連携データ確定処理されるまで ）の場合
			if(!SkfCheckUtils.isNullOrEmpty(shatakuKanriId) && 
					(KARI.equals(flg) || 
						(OLD.equals(flg) && 
						 SkfCheckUtils.isNullOrEmpty(taikyoDate) &&
						 HONNIN_DOKYO.equals(kyojushaKbn)
						) ||
						(OLD2.equals(flg) && 
						 SkfCheckUtils.isNullOrEmpty(taikyoDate) &&
						 HONNIN_DOKYO.equals(kyojushaKbn)
						)
					)){
				//社宅管理台帳基本を更新
				Skf3050Sc001UpdateShatakuShainNoExp updSLRecord = new Skf3050Sc001UpdateShatakuShainNoExp();
				updSLRecord.setShainNo(txtShainNo);
				updSLRecord.setKeyShainNo(shainBangoStr);
				updSLRecord.setLastUpdateDate(convertDateFromString(updateDateSL));
				updSLRecord.setUpdateUserId(updateUserId);
				updSLRecord.setUpdateProgramId(updateProgramId);
				updateCountSL = skf3050Sc001UpdateShainNoExpRepository.updateShatakuShainNo(updSLRecord);
				if(updateCountSL <= 0){
					//排他エラーメッセージ
					resultMap.put("errorNo", -2);
					return resultMap;
				}
				
			}
			
			// 202109 モバイルルーター機能追加対応 add start
			//モバイルルーター貸出管理簿テーブルの社員番号存在チェック(更新対象有無）
			taRL = new ArrayList<Skf3050Sc001GetShainBangoRouterJissekiCountExp>();
			taRL = skf3050Sc001GetShainBangoRouterJissekiCountExpRepository.getShainBangoRouterJissekiCount(shainBangoStr);
			if(taRL.size() > 0){
				// 更新対象社員番号有の場合更新処理実行
				Skf3050Sc001UpdateShatakuShainNoExp updSLRecord = new Skf3050Sc001UpdateShatakuShainNoExp();
				updSLRecord.setShainNo(txtShainNo);
				updSLRecord.setKeyShainNo(shainBangoStr);
				updSLRecord.setUpdateUserId(updateUserId);
				updSLRecord.setUpdateProgramId(updateProgramId);
				updateCountRL = skf3050Sc001UpdateShainNoExpRepository.updateRouterShainNo(updSLRecord);
				if(updateCountRL <= 0){
					//排他エラーメッセージ
					resultMap.put("errorNo", -1);
					return resultMap;
				}
			}
			// 202109 モバイルルーター機能追加対応 add end
			
			
			if(!SkfCheckUtils.isNullOrEmpty(companyCd)){
				//旧社員番号を変更した場合／一括変更後の社員（HR連携データ確定処理されるまで ）を変更した場合
				if(OLD.equals(flg) || OLD2.equals(flg)){
					//社宅社員マスタを更新（変更後データ)
					Skf1010MShain updShainRecord = new Skf1010MShain();
					updShainRecord.setShainNoChangeFlg("2");
					updShainRecord.setShainNoChangeDate(sysDate);
					updShainRecord.setLastUpdateDate(convertDateFromString(updateDateNew));
					updShainRecord.setShainNo(txtShainNo);
					updShainRecord.setCompanyCd(CodeConstant.C001);
					updateCountSS = skf1010MShainRepository.updateByPrimaryKeySelective(updShainRecord);
					if(updateCountSS <= 0){
						//排他エラーメッセージ
						resultMap.put("errorNo", -2);
						return resultMap;
					}
					
					//所属履歴の更新
					List<Skf3050Sc001GetShatakuShainShozokuInfoExp> rirekiDt = new ArrayList<Skf3050Sc001GetShatakuShainShozokuInfoExp>();
					rirekiDt = skf3050Sc001GetShatakuShainShozokuInfoExpRepository.getShatakuShainShozokuInfo(txtShainNo);
					//データが存在する場合
					if(rirekiDt.size() > 0){
						//所属履歴を更新
						Skf3030TShozokuRireki shozokuRecord = new Skf3030TShozokuRireki();
						shozokuRecord.setCompanyCd(rirekiDt.get(0).getOriginalCompanyCd());
						shozokuRecord.setCompanyName(rirekiDt.get(0).getCompanyName());
						shozokuRecord.setAgencyCd(rirekiDt.get(0).getAgencyCd());
						shozokuRecord.setAgencyName(rirekiDt.get(0).getAgencyName());
						shozokuRecord.setAffiliation1Cd(rirekiDt.get(0).getAffiliation1Cd());
						shozokuRecord.setAffiliation1(rirekiDt.get(0).getAffiliation1Name());
						shozokuRecord.setAffiliation2Cd(rirekiDt.get(0).getAffiliation2Cd());
						shozokuRecord.setAffiliation2(rirekiDt.get(0).getAffiliation2Name());
						shozokuRecord.setBusinessAreaCd(rirekiDt.get(0).getBusinessAreaCd());
						shozokuRecord.setBusinessAreaName(rirekiDt.get(0).getBusinessAreaName());
						shozokuRecord.setUpdateUserId(updateUserId);
						shozokuRecord.setUpdateProgramId(updateProgramId);
						shozokuRecord.setShatakuKanriId(Long.parseLong(shatakuKanriId));
						updateCountSR = skf3050Sc001UpdateShainNoExpRepository.updateShozokuRirekiInfo(shozokuRecord);
						
					}else{
						//排他エラーメッセージ
						resultMap.put("errorNo", -2);
						return resultMap;
					}
					
					//社宅社員マスタを更新（変更前データ）
					Skf1010MShain updShainRecordBef = new Skf1010MShain();
					updShainRecordBef.setShainNoChangeFlg("0");
					updShainRecordBef.setShainNoChangeDate(sysDate);
					updShainRecordBef.setShainNo(shainBangoStr);
					updShainRecordBef.setCompanyCd(CodeConstant.C001);
					updShainRecordBef.setLastUpdateDate(convertDateFromString(updateDateSS));
					updateCountSS = skf1010MShainRepository.updateByPrimaryKeySelective(updShainRecordBef);
				}
			}else{
				//仮社員番号を変更した場合
				if(KARI.equals(flg)){
					//社宅社員マスタを更新(変更後の社員番号)
					Skf1010MShain updShainRecord = new Skf1010MShain();
					updShainRecord.setShainNoChangeFlg("2");
					updShainRecord.setShainNoChangeDate(sysDate);
					updShainRecord.setLastUpdateDate(convertDateFromString(updateDateNew));
					updShainRecord.setShainNo(txtShainNo);
					updShainRecord.setCompanyCd(CodeConstant.C001);
					updateCountSS = skf1010MShainRepository.updateByPrimaryKeySelective(updShainRecord);
					if(updateCountSS <= 0){
						//排他エラーメッセージ
						resultMap.put("errorNo", -2);
						return resultMap;
					}
					
					//所属履歴の更新
					List<Skf3050Sc001GetShatakuShainShozokuInfoExp> rirekiDt = new ArrayList<Skf3050Sc001GetShatakuShainShozokuInfoExp>();
					rirekiDt = skf3050Sc001GetShatakuShainShozokuInfoExpRepository.getShatakuShainShozokuInfo(txtShainNo);
					//データが存在する場合
					if(rirekiDt.size() > 0){
						//所属履歴を更新
						Skf3030TShozokuRireki shozokuRecord = new Skf3030TShozokuRireki();
						shozokuRecord.setCompanyCd(rirekiDt.get(0).getOriginalCompanyCd());
						shozokuRecord.setCompanyName(rirekiDt.get(0).getCompanyName());
						shozokuRecord.setAgencyCd(rirekiDt.get(0).getAgencyCd());
						shozokuRecord.setAgencyName(rirekiDt.get(0).getAgencyName());
						shozokuRecord.setAffiliation1Cd(rirekiDt.get(0).getAffiliation1Cd());
						shozokuRecord.setAffiliation1(rirekiDt.get(0).getAffiliation1Name());
						shozokuRecord.setAffiliation2Cd(rirekiDt.get(0).getAffiliation2Cd());
						shozokuRecord.setAffiliation2(rirekiDt.get(0).getAffiliation2Name());
						shozokuRecord.setBusinessAreaCd(rirekiDt.get(0).getBusinessAreaCd());
						shozokuRecord.setBusinessAreaName(rirekiDt.get(0).getBusinessAreaName());
						shozokuRecord.setUpdateUserId(updateUserId);
						shozokuRecord.setUpdateProgramId(updateProgramId);
						shozokuRecord.setShatakuKanriId(Long.parseLong(shatakuKanriId));
						updateCountSR = skf3050Sc001UpdateShainNoExpRepository.updateShozokuRirekiInfo(shozokuRecord);
						
					}else{
						//排他エラーメッセージ
						resultMap.put("errorNo", -2);
						return resultMap;
					}
					
				}
			}
			
			if(0 < updateCountTJ ||
				0 < updateCountTC ||
				0 < updateCountSL ||
				0 < updateCountNY ||
				0 < updateCountSS ||
				0 < updateCountSR ||
				0 < updateCountRL){
				count += 1;
			}
		}
		
		//更新できた場合
		if(count > 0){
			resultMap.put("errorNo", count);
		}else{
			resultMap.put("errorNo", -1);
		}
		
		return resultMap;
	}
	
	/**
	 * 社員情報リスト文字列読込
	 * @param shainListData
	 * @return 社員情報リスト
	 */
	private List<Map<String,Object>> createShainInfoList(String shainListData){
		//返却リスト初期化
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		//備品情報リスト（表示分）
		//画面からは文字列で取得するので、まず行ごとに分割
		String[] infoList = shainListData.split(";");
		for(String info : infoList){
			LogUtils.debugByMsg("送信情報文字列:"+info);
			//行データを項目ごとに分割
			String[] items = info.split(",");
			if(items.length >= 15){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", items[0]);//社員番号
				forListMap.put("shainName", items[1]);//社員氏名
				forListMap.put("txtShainNo", items[2]);//社員番号(txt)
				forListMap.put("shainNameJugyoin", items[3]);//社員氏名（従業員）
				forListMap.put("shozokuJugyoin", items[4]);//所属（従業員）
				forListMap.put("shainNoChangeFlg", items[5]);//社員番号変更フラグ
				forListMap.put("shatakuKanriId", items[6]);//社宅管理台帳ID
				forListMap.put("companyCd", items[7]);//会社コード
				forListMap.put("flg", items[8]);//
				forListMap.put("taikyoDate", items[9]);//退居日
				forListMap.put("kyojushaKbn", items[10]);//居住者区分
				forListMap.put("updateDateSL", items[11]);//更新日(社宅社員)
				forListMap.put("updateDateSS", items[12]);//更新日(社宅管理台帳基本)
				forListMap.put("updateDateNew", items[13]);//更新日(従業員)
				forListMap.put("boxName", items[14]);//textbox名
				
				resultList.add(forListMap);
			}
		}
		
		return resultList;
	}
	
	/**
	 * Date型を文字列に変換
	 * @param dateStr
	 * @return
	 */
	private Date convertDateFromString(String dateStr){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		if(!SkfCheckUtils.isNullOrEmpty(dateStr)){
			try{
				Date mapDate = dateFormat.parse(dateStr);	
				LogUtils.debugByMsg("dateStr：" + mapDate);
				return mapDate;
			}	
			catch(ParseException ex){
				LogUtils.debugByMsg("日時変換失敗 :" + dateStr);
			}
		}
		return null;
	}
}
