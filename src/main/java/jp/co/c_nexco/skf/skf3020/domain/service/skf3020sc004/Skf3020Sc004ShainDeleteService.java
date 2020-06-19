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

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004ShainDeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3020Sc004ShainDeleteService 転任者一覧画面のShainDeleteサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004ShainDeleteService extends SkfServiceAbstract<Skf3020Sc004ShainDeleteDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	
	@Autowired
	Skf3020Sc004SharedService skf3020Sc004SharedService;
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param registDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	@Transactional
	public Skf3020Sc004ShainDeleteDto index(Skf3020Sc004ShainDeleteDto shainDelete) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("仮社員番号のデータ削除", CodeConstant.C001, FunctionIdConstant.SKF3020_SC004);		
		
		// Delete後の再検索のために検索条件を保持
		String[] setone = { "1" };

		// 社員番号設定(検索キー)
		shainDelete.setHdnShainNo(shainDelete.getShainNo());
		// 社員氏名設定（検索キー）
		shainDelete.setHdnShainName(shainDelete.getShainName());
		// 入居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(shainDelete.getNyukyo()) != null) {
			shainDelete.setHdnNyukyoChkFlg(true);
			shainDelete.setHdnNyukyo(setone);
		} else {
			shainDelete.setHdnNyukyoChkFlg(false);
			shainDelete.setHdnNyukyo(null);
		}
		
		// 退居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(shainDelete.getTaikyo()) != null) {
			shainDelete.setHdnTaikyoChkFlg(true);
			shainDelete.setHdnTaikyo(setone);
		} else {
			shainDelete.setHdnTaikyoChkFlg(false);
			shainDelete.setHdnTaikyo(null);
		}			
		// 入居状態変更設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(shainDelete.getHenko()) != null) {
			shainDelete.setHdnHenkoChkFlg(true);
			shainDelete.setHdnHenko(setone);
		} else {
			shainDelete.setHdnHenkoChkFlg(false);
			shainDelete.setHdnHenko(null);
		}			
		// 現社宅区分設定（検索キー）
		shainDelete.setHdnGenShataku(shainDelete.getGenShatakuKubun());
		// 現所属設定（検索キー）
		shainDelete.setHdnGenShozoku(shainDelete.getGenShozoku());
		// 新所属設定（検索キー）
		shainDelete.setHdnShinShozoku(shainDelete.getShinShozoku());
		// 入退居予定作成区分（検索キー）
		shainDelete.setHdnNyutaikyoYoteiSakuseiKubun(shainDelete.getNyutaikyoYoteiSakuseiKubun());
		// 備考設定（検索キー）
		shainDelete.setHdnBiko(shainDelete.getBiko());
		
		List<Map<String, Object>> genShatakuKubunList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yoteiSakuseiList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3020Sc004SharedService.getDoropDownList(shainDelete.getGenShatakuKubun(), genShatakuKubunList,
				shainDelete.getNyutaikyoYoteiSakuseiKubun(), yoteiSakuseiList);

		shainDelete.setGenShatakuKubunList(genShatakuKubunList);
		shainDelete.setYoteiSakuseiList(yoteiSakuseiList);
		
		// 検索結果一覧のリストチェック状態を解除
		shainDelete.setNyukyoChkVal(null);
		shainDelete.setTaikyoChkVal(null);
		shainDelete.setHenkouChkVal(null);
		
		int delCount = deleteKariShainNoInfo(shainDelete);
		
		if(delCount == -9999){
			// 削除対象データなし
			LogUtils.debugByMsg("削除対象の仮社員なし");
		}else if(delCount == 0){
			// 削除エラーメッセージ（error.skf.e_skf_1076）
			ServiceHelper.addErrorResultMessage(shainDelete, null, MessageIdConstant.E_SKF_1076);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(shainDelete.getResultMessages());
		}else if(delCount == -1){
			// 排他エラーメッセージ（warning.skf.w_skf_1009）
			ServiceHelper.addErrorResultMessage(shainDelete, null, MessageIdConstant.W_SKF_1009);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(shainDelete.getResultMessages());
		}else{
			// 削除完了メッセージ（infomation.skf.i_skf_1013）
			ServiceHelper.addResultMessage(shainDelete, MessageIdConstant.I_SKF_1013);
			// 画面遷移(転任者情報一覧の初期化処理へ遷移)
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			shainDelete.setTransferPageInfo(nextPage);	
		}
		
		return shainDelete;
	}

	/**
	 * 転任者調書テーブルから社員番号をキーに削除処理<br>
	 * 
	 * @param dto indexメソッドの引数であるDto
	 * @return　更新件数
	 */
	private int deleteKariShainNoInfo(Skf3020Sc004ShainDeleteDto dto) {
		int delCount = 0;
		boolean deleteFlag = false;
		// 全行チェック
		for (int rowIndex = 0; rowIndex < dto.getListTableData().size(); rowIndex++){		

			Map<String, Object> listTableData = dto.getListTableData().get(rowIndex);
			
			// Mapからキーをもとに値をとる
			// - 社員番号
			String shaiNoStr = listTableData.get("col4").toString();
			if(StringUtils.isEmpty(shaiNoStr)){
				LogUtils.debugByMsg("社員番号が空 or NULL");
				return -1;
			}
			
			// 入退居予定作成区分が"1"だったら次のループへ
			String nyutaikyoYoteiKbnStr = listTableData.get("col12").toString();
			if(StringUtils.isEmpty(nyutaikyoYoteiKbnStr) == false){
				if(Objects.equals(nyutaikyoYoteiKbnStr, "作成済")){
					continue;
				}
			}
			
			// 仮社員番号かチェック
			String kariShaiNoStr = shaiNoStr.substring(0, 1);
			if(Objects.equals(kariShaiNoStr, "K")){
				// 排他チェック
				Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository.selectByPrimaryKey(shaiNoStr);
				if(tenninshaInfo == null){
					LogUtils.debugByMsg("転任者調書データ取得結果NULL");
					return -1;
				}
				
				deleteFlag = true;
				
				// 日付変換
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
				Date mapDate = null;
				try{
					mapDate = dateFormat.parse(listTableData.get("col16").toString());
				}catch(ParseException ex){
					LogUtils.infoByMsg("deleteKariShainNoInfo, 転任者情報-更新日時変換NG:" + listTableData.get("col16").toString());
					return -1;
				}			
				super.checkLockException(mapDate, tenninshaInfo.getUpdateDate());			
				
				// 転任者調書データ削除
				delCount += skf3020TTenninshaChoshoDataRepository.deleteByPrimaryKey(shaiNoStr);				
				
			}else{
				// 仮社員番号でない場合は次の社員番号をチェック
				continue;
			}
		}
		
		if(delCount == 0 && deleteFlag == false){
			delCount = -9999;
		}
		
		return delCount;
	}
	

}
