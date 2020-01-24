/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3050.domain.service.skf3050sc001;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3050Sc001.Skf3050Sc001GetShatakuShainNameExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3050Sc001.Skf3050Sc001GetShatakuShainNameExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3050.domain.dto.skf3050sc001.Skf3050Sc001ShainConfirmDto;

/**
 * Skf3050Sc001ShainConfirmService 社員番号一括設定の社員情報確認サービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3050Sc001ShainConfirmService extends BaseServiceAbstract<Skf3050Sc001ShainConfirmDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3050Sc001GetShatakuShainNameExpRepository skf3050Sc001GetShatakuShainNameExpRepository;
	
	//社員情報確認フラグ:未実施
	private static final int CONFIRMFLG_MI_JISSHI = 0;
	//社員情報確認フラグ:実施
	private static final int CONFIRMFLG_JISSHI = 1;
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param confirmDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3050Sc001ShainConfirmDto index(Skf3050Sc001ShainConfirmDto confirmDto) throws Exception {
		 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("社員情報確認", CodeConstant.C001, confirmDto.getPageId());
		
		//社員情報確認フラグ
		int shainInfoConfirmFlg = CONFIRMFLG_JISSHI;
		//社員番号整合チェックフラグ
		boolean shainNoCheckFlg = false;
		//社員氏名整合チェックフラグ
		boolean shainNameCheckFlg = false;
		//社員番号未入力フラグ
		boolean blankFlg = true;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		//社員情報確認処理
		List<Map<String,Object>> grvShainBangoList = createShainInfoList(confirmDto.getShainListData());
		
		for(Map<String,Object> row : grvShainBangoList){
			//社員番号
			//String shainNo = row.get("shainNo").toString();
			//社員氏名
			//String shainName = row.get("shainName").toString();
			//社員番号（txt）
			String txtShainNo = row.get("txtShainNo").toString();
			//社員氏名（従業員）
			String shainNameJugyoin = row.get("shainNameJugyoin").toString();
			//所属（従業員）
			String shozokuJugyoin = row.get("shozokuJugyoin").toString();
			//更新日(従業員)
			//String updateDateNew = row.get("updateDateNew").toString();
			//対象のテキストボックス名
			String boxName = row.get("boxName").toString();
			
			//社員番号が未入力の場合
			if(SkfCheckUtils.isNullOrEmpty(txtShainNo)){
				if(!SkfCheckUtils.isNullOrEmpty(shainNameJugyoin) || !SkfCheckUtils.isNullOrEmpty(shozokuJugyoin)){
					row.put("shainNameJugyoin", CodeConstant.DOUBLE_QUOTATION);
					row.put("shozokuJugyoin", CodeConstant.DOUBLE_QUOTATION);
				}
				//背景色をクリア
				continue;
			}else{
				blankFlg = false;
			}
			
			//社員番号の妥当性判定
			if(!validateShainNo(txtShainNo)){
				//背景色
				//エラーメッセージ設定
				ServiceHelper.addErrorResultMessage(confirmDto,  new String[] { boxName }, MessageIdConstant.E_SKF_1050, "社員番号");
				//社員情報確認フラグ
				shainInfoConfirmFlg = CONFIRMFLG_MI_JISSHI;
				break;
			}
			
			//社員番号の整合チェック
			for(Map<String,Object> row1 : grvShainBangoList){
				//社員番号（txt）
				String txtShainNo1 = row1.get("txtShainNo").toString();
				//対象のテキストボックス名
				String boxName1 = row1.get("boxName").toString();
				if(txtShainNo.equals(txtShainNo1) && !boxName.equals(boxName1)){
					shainNoCheckFlg = true;
					break;
				}
			}
			
			if(shainNoCheckFlg){
				//エラーメッセージ設定
				ServiceHelper.addErrorResultMessage(confirmDto,  null, MessageIdConstant.E_SKF_3037,txtShainNo);
				//社員情報確認フラグ
				shainInfoConfirmFlg = CONFIRMFLG_MI_JISSHI;
				break;
			}
			
			//社宅社員マスタから社員情報取得
			List<Skf3050Sc001GetShatakuShainNameExp> dt = new ArrayList<Skf3050Sc001GetShatakuShainNameExp>();
			dt = skf3050Sc001GetShatakuShainNameExpRepository.getShatakuShainName(txtShainNo);
			
			if(dt.size() > 0){
				//社員氏名を設定
				row.put("shainNameJugyoin", dt.get(0).getName());
				//所属を設定
				String agencyName = (dt.get(0).getAgencyName() != null) ? dt.get(0).getAgencyName() : CodeConstant.DOUBLE_QUOTATION;
				String affiliation1Name = (dt.get(0).getAffiliation1Name() != null) ? dt.get(0).getAffiliation1Name() : CodeConstant.DOUBLE_QUOTATION;
				String affiliation2Name = (dt.get(0).getAffiliation2Name() != null) ? dt.get(0).getAffiliation2Name() : CodeConstant.DOUBLE_QUOTATION;
				
				String shozoku = agencyName + affiliation1Name + affiliation2Name;
				row.put("shozokuJugyoin", shozoku);
				//更新日
				if(dt.get(0).getUpdateDateShain() != null){
					row.put("updateDateNew", dateFormat.format(dt.get(0).getUpdateDateShain()));
				}
			}else{
				row.put("shainNameJugyoin", CodeConstant.DOUBLE_QUOTATION);
				row.put("shozokuJugyoin", CodeConstant.DOUBLE_QUOTATION);
				//エラーメッセージ設定
				ServiceHelper.addErrorResultMessage(confirmDto, null, MessageIdConstant.E_SKF_3038,txtShainNo);
				//社員情報確認フラグ
				shainInfoConfirmFlg = CONFIRMFLG_MI_JISSHI;
				break;
			}
		}
		
		if(shainNameCheckFlg){
			ServiceHelper.addWarnResultMessage(confirmDto, MessageIdConstant.W_SKF_3001, "");
		}
		
		if(shainInfoConfirmFlg == CONFIRMFLG_JISSHI && blankFlg == false){
			//登録ボタンを活性に設定
			confirmDto.setBtnRegistDisabled("false");
		}else{
			//登録ボタンを非活性に設定
			confirmDto.setBtnRegistDisabled("true");
		}
		
		confirmDto.setListTableData(updateListTableDataViewColumn(grvShainBangoList,confirmDto.getListTableData()));
		confirmDto.setListPage(confirmDto.getListPage());
		
		return confirmDto;
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
			if(items.length >= 7){
				Map<String, Object> forListMap = new HashMap<String, Object>();
				forListMap.put("shainNo", items[0]);//社員番号
				forListMap.put("shainName", items[1]);//社員氏名
				forListMap.put("txtShainNo", items[2]);//社員番号(txt)
				forListMap.put("shainNameJugyoin", items[3]);//社員氏名（従業員）
				forListMap.put("shozokuJugyoin", items[4]);//所属（従業員）
				forListMap.put("updateDateNew", items[5]);//更新日(従業員)
				forListMap.put("boxName", items[6]);//textbox名
				
				resultList.add(forListMap);
			}
		}
		
		return resultList;
	}
	
	/**
	 * 社員番号の妥当性を判定するメソッド
	 * @param txtShainNo 社員番号テキストボックス値
	 * @return True:正常、False:異常
	 */
	private boolean validateShainNo(String txtShainNo){
		
		
		if(txtShainNo.indexOf(CodeConstant.SPACE) > -1){
			//半角スペースチェック
			return false;
		}else if(txtShainNo.indexOf(CodeConstant.ZEN_SPACE) > -1){
			//全角スペースチェック
			return false;
		}else{
			//社員番号の半角数字判定
			if(!CheckUtils.isNumeric(txtShainNo)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> updateListTableDataViewColumn(List<Map<String,Object>> shainListData,List<Map<String, Object>> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originList.size(); i++) {

			Map<String, Object> tmpData = originList.get(i);
			
			//値セット
			for(Map<String,Object> shainData : shainListData){
				if(shainData.get("shainNo").equals(tmpData.get("colShainNo"))){
					//従業員マスタ:社員氏名
					tmpData.put("colShainNameJugyoin", shainData.get("shainNameJugyoin"));
					//従業員マスタ:所属
					tmpData.put("colShozokuJugyoin", shainData.get("shozokuJugyoin"));
					//社宅社員マスタ更新日
					tmpData.put("hdnUpdateDateNew", shainData.get("updateDateNew"));
					//新社員番号
					tmpData.put("colTxtShainNo", "<input id='" + shainData.get("boxName") 
							+ "' name='" + shainData.get("boxName")  + "' type='text' value='"+shainData.get("txtShainNo") +"' style='ime-mode: disabled;width: 98%' maxlength='8' tabindex='1' pattern='^[0-9]+$' /> ");
					
					break;
				}
			}
			
			setViewList.add(tmpData);
		}

		return setViewList;
	}
}
