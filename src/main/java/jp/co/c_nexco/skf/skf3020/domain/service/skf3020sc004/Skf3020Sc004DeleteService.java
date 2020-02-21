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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004DeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3020Sc004DeleteService 転任者一覧画面のDeleteサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004DeleteService extends BaseServiceAbstract<Skf3020Sc004DeleteDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;	
	
	@Autowired
	Skf3020Sc004SharedService skf3020Sc004SharedService;
	
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param deleteDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Skf3020Sc004DeleteDto index(Skf3020Sc004DeleteDto deleteDto) throws Exception {
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());		
		
		// Delete後の再検索のために検索条件を保持
		String[] setone = { "1" };
		// 社員番号設定(検索キー)
		deleteDto.setHdnShainNo(deleteDto.getShainNo());
		// 社員氏名設定（検索キー）
		deleteDto.setHdnShainName(deleteDto.getShainName());
		// 入居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(deleteDto.getNyukyo()) != null) {
			deleteDto.setHdnNyukyoChkFlg(true);
			deleteDto.setHdnNyukyo(setone);
		} else {
			deleteDto.setHdnNyukyoChkFlg(false);
			deleteDto.setHdnNyukyo(null);
		}
		
		// 退居設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(deleteDto.getTaikyo()) != null) {
			deleteDto.setHdnTaikyoChkFlg(true);
			deleteDto.setHdnTaikyo(setone);
		} else {
			deleteDto.setHdnTaikyoChkFlg(false);
			deleteDto.setHdnTaikyo(null);
		}			
		// 入居状態変更設定（検索キー）
		if (skf3020Sc004SharedService.checkBoxcheck(deleteDto.getHenko()) != null) {
			deleteDto.setHdnHenkoChkFlg(true);
			deleteDto.setHdnHenko(setone);
		} else {
			deleteDto.setHdnHenkoChkFlg(false);
			deleteDto.setHdnHenko(null);
		}			
		// 現社宅区分設定（検索キー）
		deleteDto.setHdnGenShataku(deleteDto.getGenShatakuKubun());
		// 現所属設定（検索キー）
		deleteDto.setHdnGenShozoku(deleteDto.getGenShozoku());
		// 新所属設定（検索キー）
		deleteDto.setHdnShinShozoku(deleteDto.getShinShozoku());
		// 入退居予定作成区分（検索キー）
		deleteDto.setHdnNyutaikyoYoteiSakuseiKubun(deleteDto.getNyutaikyoYoteiSakuseiKubun());
		// 備考設定（検索キー）
		deleteDto.setHdnBiko(deleteDto.getBiko());
		
		List<Map<String, Object>> genShatakuKubunList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yoteiSakuseiList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3020Sc004SharedService.getDoropDownList(deleteDto.getGenShatakuKubun(), genShatakuKubunList,
				deleteDto.getNyutaikyoYoteiSakuseiKubun(), yoteiSakuseiList);

		deleteDto.setGenShatakuKubunList(genShatakuKubunList);
		deleteDto.setYoteiSakuseiList(yoteiSakuseiList);				
		
		// リストチェック状態を解除
		deleteDto.setNyukyoChkVal(null);
		deleteDto.setTaikyoChkVal(null);
		deleteDto.setHenkouChkVal(null);
		
		// 更新処理
		int delCount = deleteTenninsyaInfo(deleteDto);

		if(delCount == 0){
			// 削除エラーメッセージ（error.skf.e_skf_1076）
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}else if(delCount == -1){
			// 排他エラーメッセージ（warning.skf.w_skf_1009）
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.W_SKF_1009);			
			// 例外時はthrowしてRollBack
			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}else{
			// 削除完了メッセージ（infomation.skf.i_skf_1013）
			ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);
			// 画面遷移(転任者情報一覧の初期化処理へ遷移)
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			deleteDto.setTransferPageInfo(nextPage);		
		}
		
		return deleteDto;
	}
	
	
	/**
	 * 転任者調書テーブルから社員番号をキーに削除処理<br>
	 * 
	 * @param dto indexメソッドの引数であるDto
	 * @return　更新件数
	 */
	private int deleteTenninsyaInfo(Skf3020Sc004DeleteDto dto) {

		int delCount = 0;
		
		String shainNo = dto.getHdnRowShainNo().replace(CodeConstant.ASTERISK ,"");
		
		// 日付変換
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date mapDate = null;
		try{
			mapDate = dateFormat.parse(dto.getHdnRowUpdateDate().toString());
		}catch(ParseException ex){
			LogUtils.debugByMsg("転任者情報-更新日時変換エラー :" + dto.getHdnRowUpdateDate().toString());
			return -1;
		}
		
		// 排他チェック
		Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository.selectByPrimaryKey(shainNo);
		if(tenninshaInfo == null){
			LogUtils.debugByMsg("転任者調書データ取得結果NULL");
			return -1;
		}
		super.checkLockException(mapDate, tenninshaInfo.getUpdateDate());
		
		// 転任者調書データ削除
		delCount = skf3020TTenninshaChoshoDataRepository.deleteByPrimaryKey(shainNo);
	
		return delCount;
	}
	

}
