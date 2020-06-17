/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.emory.mathcs.backport.java.util.Arrays;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004RegistDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiDataKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;


/**
 * Skf3020Sc004RegistService 転任者一覧画面のRegistサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004RegistService extends SkfServiceAbstract<Skf3020Sc004RegistDto>  {

	@Autowired
	Skf3020Sc004SharedService skf3020Sc004SharedService;	
	
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
		
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3020Sc004RegistDto index(Skf3020Sc004RegistDto registDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, FunctionIdConstant.SKF3020_SC004);		
		
		// Delete後の再検索のために検索条件を保持
		String[] setone = { "1" };
		// 社員番号設定(検索キー)
		registDto.setHdnShainNo(registDto.getShainNo());
		// 社員氏名設定（検索キー）
		registDto.setHdnShainName(registDto.getShainName());
		// 入居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(registDto.getNyukyo()) != null) {
			registDto.setHdnNyukyoChkFlg(true);
			registDto.setHdnNyukyo(setone);
		} else {
			registDto.setHdnNyukyoChkFlg(false);
			registDto.setHdnNyukyo(null);
		}
		
		// 退居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(registDto.getTaikyo()) != null) {
			registDto.setHdnTaikyoChkFlg(true);
			registDto.setHdnTaikyo(setone);
		} else {
			registDto.setHdnTaikyoChkFlg(false);
			registDto.setHdnTaikyo(null);
		}			
		// 入居状態変更設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(registDto.getHenko()) != null) {
			registDto.setHdnHenkoChkFlg(true);
			registDto.setHdnHenko(setone);
		} else {
			registDto.setHdnHenkoChkFlg(false);
			registDto.setHdnHenko(null);
		}			
		// 現社宅区分設定（検索キー）
		registDto.setHdnGenShataku(registDto.getGenShatakuKubun());
		// 現所属設定（検索キー）
		registDto.setHdnGenShozoku(registDto.getGenShozoku());
		// 新所属設定（検索キー）
		registDto.setHdnShinShozoku(registDto.getShinShozoku());
		// 入退居予定作成区分（検索キー）
		registDto.setHdnNyutaikyoYoteiSakuseiKubun(registDto.getNyutaikyoYoteiSakuseiKubun());
		// 備考設定（検索キー）
		registDto.setHdnBiko(registDto.getBiko());
		
		List<Map<String, Object>> genShatakuKubunList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yoteiSakuseiList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3020Sc004SharedService.getDoropDownList(registDto.getGenShatakuKubun(), genShatakuKubunList,
				registDto.getNyutaikyoYoteiSakuseiKubun(), yoteiSakuseiList);

		registDto.setGenShatakuKubunList(genShatakuKubunList);
		registDto.setYoteiSakuseiList(yoteiSakuseiList);		
		
		// 入居チェックボックスのチェック状態を取得
		List<String> nyukyoChkValList = Arrays.asList(registDto.getNyukyoChkVal());			
		// 退居チェックボックスのチェック状態を取得
		List<String> taikyoChkValList = Arrays.asList(registDto.getTaikyoChkVal());			
		// 変更チェックボックスのチェック状態を取得
		List<String> henkouChkValList = Arrays.asList(registDto.getHenkouChkVal());			
		
		// 入力チェック
		int checkCnt = 0;
		boolean chkFlg = false;
		for(int rowIndex = 0; rowIndex < registDto.getListTableData().size() ; rowIndex++){
			
			int indexNyukyo = nyukyoChkValList.indexOf(Integer.toString(rowIndex));
			int indexTaikyo = taikyoChkValList.indexOf(Integer.toString(rowIndex));
			int indexHenkou = henkouChkValList.indexOf(Integer.toString(rowIndex));
			Map<String, Object> listTableData = registDto.getListTableData().get(rowIndex);
			
			Boolean nyukyoEnabled = true;
			Boolean taikyoEnabled = true;
			Boolean henkouEnabled = true;
			
			if(listTableData.get("col1").toString().indexOf("disabled") != -1){
				nyukyoEnabled = false;
			}
			if(listTableData.get("col2").toString().indexOf("disabled") != -1){
				taikyoEnabled = false;
			}
			if(listTableData.get("col3").toString().indexOf("disabled") != -1){
				henkouEnabled = false;
			}
			
			if((nyukyoEnabled && (indexNyukyo != -1)) || ( taikyoEnabled && (indexTaikyo != -1)) || (henkouEnabled && (indexHenkou != -1))){
				checkCnt = checkCnt + 1;
			}
			
			// すべてチェックが入っている
			if((indexNyukyo != -1) && (indexTaikyo != -1) && (indexHenkou != -1)){
				String nyukyoStr = nyukyoChkValList.get(indexNyukyo);
				String taikyoStr = taikyoChkValList.get(indexTaikyo);
				String henkouStr = henkouChkValList.get(indexHenkou);
				
				if((Objects.equals(nyukyoStr, taikyoStr)) && (Objects.equals(taikyoStr, henkouStr)) && (Objects.equals(nyukyoStr, henkouStr))){
					chkFlg = true;
					break;
				}
			}
		}

		// リストチェック状態を解除
		registDto.setNyukyoChkVal(null);
		registDto.setTaikyoChkVal(null);
		registDto.setHenkouChkVal(null);
		
		// 入居・退居・変更にチェックが1つも入っていない（error.skf.e_skf_3040）
		if(checkCnt == 0){
			ServiceHelper.addWarnResultMessage(registDto, MessageIdConstant.E_SKF_3040);			
			return registDto;
		}
		
		
		// 入居・退居・変更のすべてにチェックが入っている（error.skf.e_skf_3016）
		if(chkFlg == true){
			ServiceHelper.addWarnResultMessage(registDto, MessageIdConstant.E_SKF_3016);			
			return registDto;
		}
		
		// 更新処理
		int updCount = registTenninsyaInfo(registDto, nyukyoChkValList, taikyoChkValList, henkouChkValList);
				
		if(updCount == 0){
			// 更新エラーメッセージ（error.skf.e_skf_1073）
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1073);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else if(updCount == -1){
			// 排他エラーメッセージ（warning.skf.w_skf_1010）
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1010);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(registDto.getResultMessages());
		}else{
			// 更新完了メッセージ（infomation.skf.i_skf_1012）
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
			// 画面遷移(転任者情報一覧の初期化処理へ遷移)
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			registDto.setTransferPageInfo(nextPage);	
		}
		
		return registDto;
	}
	
	
	/**
	 * 転任者調書テーブルに社員番号をキーに更新処理<br>
	 * 
	 * @param dto indexメソッドの引数であるDto
	 * @return　更新件数
	 */
	private int registTenninsyaInfo(Skf3020Sc004RegistDto dto, List<String> nyukyoChkValList, List<String> taikyoChkValList, List<String> henkouChkValList) {

		// 全行更新
		int updateCount = 0;
		for (int rowIndex = 0; rowIndex < dto.getListTableData().size(); rowIndex++){		
		
			Skf3020TTenninshaChoshoData setVal = new Skf3020TTenninshaChoshoData();
			
			int indexNyukyo = nyukyoChkValList.indexOf(Integer.toString(rowIndex));
			int indexTaikyo = taikyoChkValList.indexOf(Integer.toString(rowIndex));
			int indexHenkou = henkouChkValList.indexOf(Integer.toString(rowIndex));

			if(indexNyukyo == -1 && indexTaikyo == -1 && indexHenkou == -1 ){
				//すべてチェック無しの場合、次の行へ
				continue;
			}
			
			/** 更新項目をセット **/
			// - 入居フラグ
			String nyukyoFlgStr = null;
			// - 退居フラグ
			String taikyoFlgStr = null;;
			// - 変更フラグ
			String henkouFlgStr = null;
			// 入居フラグ
			if(indexNyukyo == -1){
				// チェックなし
				setVal.setNyukyoFlg("0");
				nyukyoFlgStr = "0";
			}else{
				// チェックあり
				setVal.setNyukyoFlg("1");
				nyukyoFlgStr = "1";
			}
			// 退居フラグ
			if(indexTaikyo == -1){
				// チェックなし
				setVal.setTaikyoFlg("0");
				taikyoFlgStr = "0";
			}else{
				setVal.setTaikyoFlg("1");
				taikyoFlgStr = "1";
			}
			// 変更フラグ
			if(indexHenkou == -1){
				setVal.setHenkouFlg("0");
				henkouFlgStr = "0";
			}else{
				setVal.setHenkouFlg("1");
				henkouFlgStr = "1";
			}
			
			Map<String, Object> listTableData = dto.getListTableData().get(rowIndex);
			
			if(!Objects.equals(nyukyoFlgStr, listTableData.get("col17")) ||
				!Objects.equals(taikyoFlgStr, listTableData.get("col18")) ||
				!Objects.equals(henkouFlgStr, listTableData.get("col19"))){
				
				// Mapからキーをもとに値をとる
				// - 社員番号
				String shaiNoStr = listTableData.get("col4").toString();
				int indexAster = shaiNoStr.indexOf(CodeConstant.ASTERISK);
				if(indexAster >= 0){
					shaiNoStr = shaiNoStr.replace(CodeConstant.ASTERISK, "");
				}
				// - 社員氏名
				String shaiNameStr = listTableData.get("col5").toString();
				// - 取込日
				String takingDateStr = listTableData.get("col11").toString();
				takingDateStr = takingDateStr.replace("/", "");
				
				// 排他チェック
				Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository.selectByPrimaryKey(shaiNoStr);
				if(tenninshaInfo == null){
					LogUtils.debugByMsg("転任者調書データ取得結果NULL");
					return -1;
				}
				
				// 日付変換
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				Date mapDate = null;
				try{
					mapDate = dateFormat.parse(listTableData.get("col16").toString());
				}catch(ParseException ex){
					LogUtils.infoByMsg("registTenninsyaInfo, 転任者情報-更新日時変換NG :" + listTableData.get("col16").toString());
					return -1;
				}			
				super.checkLockException(mapDate, tenninshaInfo.getUpdateDate());			

				// 転任者調書データ更新
				// - 社員番号
				setVal.setShainNo(shaiNoStr);
				// - 社員名
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getName())){
					setVal.setName(tenninshaInfo.getName());
				}
				// - 等級
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getTokyu())){
					setVal.setTokyu(tenninshaInfo.getTokyu());
				}
				// - 年齢
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getAge())){
					setVal.setAge(tenninshaInfo.getAge());
				}
				// - 現所属
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getNowAffiliation())){
					setVal.setNowAffiliation(tenninshaInfo.getNowAffiliation());
				}
				// - 新所属
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getNewAffiliation())){
					setVal.setNewAffiliation(tenninshaInfo.getNewAffiliation());
				}
				// - 備考
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getBiko())){
					setVal.setBiko(tenninshaInfo.getBiko());
				}
				// - 社員番号変更区分
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getShainNoHenkoKbn())){
					setVal.setShainNoHenkoKbn(tenninshaInfo.getShainNoHenkoKbn());
				}
				// - 入居フラグ、退居フラグ、変更フラグ ：設定済み
				// - 現社宅区分
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getNowShatakuKbn())){
					setVal.setNowShatakuKbn(tenninshaInfo.getNowShatakuKbn());
				}
				// - 入退居予定作成区分
				setVal.setNyutaikyoYoteiKbn("1");
				// - 転任者調書取込日
				if(NfwStringUtils.isNotEmpty(tenninshaInfo.getDataTakinginDate())){
					setVal.setDataTakinginDate(tenninshaInfo.getDataTakinginDate());
				}
				// - 削除フラグ
				setVal.setDeleteFlag("0");

				int resultTenninsha = updateTenninshaChoshoData(setVal); 
				if(resultTenninsha > 0){
					updateCount += resultTenninsha;
				}else{
					return 0;
				}

				// 入退居予定データ更新
				// - 入居("1")
				if(Objects.equals(nyukyoFlgStr, "1")){
					int resultNyutaikyo = updateNyutaikyoKbn(shaiNoStr, shaiNameStr, "1", takingDateStr);
					if(resultNyutaikyo > 0){
						updateCount += resultNyutaikyo;
					}else{
						return 0;
					}
				}
				// - 退居("2")
				if(Objects.equals(taikyoFlgStr, "1")){
					int resultNyutaikyo = updateNyutaikyoKbn(shaiNoStr, shaiNameStr, "2", takingDateStr);
					if(resultNyutaikyo > 0){
						updateCount += resultNyutaikyo;
					}else{
						return 0;
					}
				}
				// - 変更("3")
				if(Objects.equals(henkouFlgStr, "1")){
					int resultNyutaikyo = updateNyutaikyoKbn(shaiNoStr, shaiNameStr, "3", takingDateStr);
					if(resultNyutaikyo > 0){
						updateCount += resultNyutaikyo;
					}else{
						return 0;
					}
				}
				setVal = null;
			}
			

		}
		
		return updateCount;
	}
	
	
	/**
	 * 入退居予定データ更新（Update)リポジトリ呼び出し
	 * @param setValue
	 * @return　更新件数
	 */
	private int updateNyutaikyoKbn(String shaiNoStr, String shaiNameStr, String flag, String takingDateStr){
		
		int updateCount=0;

		// 入退居予定テーブルに登録するデータ準備
		Skf3021TNyutaikyoYoteiData yoteiData = new Skf3021TNyutaikyoYoteiData();
		// - 社員番号
		yoteiData.setShainNo(shaiNoStr);
		// - 社員氏名
		yoteiData.setName(shaiNameStr);
		// - 入退居区分
		yoteiData.setNyutaikyoKbn(flag);
		// - 転任者調書取込日
		yoteiData.setDataTakinginDate(takingDateStr);
		// - 削除フラグ
		yoteiData.setDeleteFlag("0");

		// - 入退居予定データの登録状態をチェック
		Skf3021TNyutaikyoYoteiDataKey yoteiDataKey = new Skf3021TNyutaikyoYoteiDataKey();
		yoteiDataKey.setShainNo(shaiNoStr);
		yoteiDataKey.setNyutaikyoKbn(flag);
		
		Skf3021TNyutaikyoYoteiData nyutaikyoInfo = skf3021TNyutaikyoYoteiDataRepository.selectByPrimaryKey(yoteiDataKey);
		if(nyutaikyoInfo == null){
			LogUtils.debugByMsg("入退居予定データ取得結果NULL");
			// - 提示データ作成区分
			yoteiData.setTeijiCreateKbn("0");
			
			updateCount = skf3021TNyutaikyoYoteiDataRepository.insertSelective(yoteiData);
		}else{
			// 入退居予定データを更新する	
			updateCount = skf3021TNyutaikyoYoteiDataRepository.updateByPrimaryKeySelective(yoteiData);
		}
		
		return updateCount;
	}
	
	
	/**
	 * 転任者調書データ更新（Update)リポジトリ呼び出し
	 * @param setValue
	 * @return　更新件数
	 */
	private int updateTenninshaChoshoData(Skf3020TTenninshaChoshoData setVal){
		int updateCount=0;
		
		//updateCount = skf3020TTenninshaChoshoDataRepository.updateByPrimaryKey(setVal);
		updateCount = skf3020TTenninshaChoshoDataRepository.updateByPrimaryKeySelective(setVal);
		
		return updateCount;
	}
	
}
