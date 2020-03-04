/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc004.Skf3020Sc004InitDto;

/**
 * Skf3020Sc004InitService 転任者一覧画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc004InitService extends BaseServiceAbstract<Skf3020Sc004InitDto> {
	
	@Autowired
	Skf3020Sc004SharedService skf3020Sc004SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3020.skf3020_sc004.max_row_count}")
	private String listTableMaxRowCount;	
	
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3020Sc004InitDto index(Skf3020Sc004InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC004_TITLE);
 		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		// リストチェック状態を解除
		initDto.setNyukyoChkVal(null);
		initDto.setTaikyoChkVal(null);
		initDto.setHenkouChkVal(null);
		
//		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
//				&& initDto.getPrePageId().equals(FunctionIdConstant.SKF3020_SC005)) {
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())) {
			// 子画面からの遷移
			// 登録画面からのhidden項目(検索条件)をinitDtoに詰めなおす
			initDto.setShainNo(initDto.getHdnShainNo());
			initDto.setShainName(initDto.getHdnShainName());
			// 値を入れる前に１回初期化
			initDto.setNyukyo(null);
			initDto.setNyukyoChkFlg(false);
			initDto.setTaikyo(null);
			initDto.setTaikyoChkFlg(false);
			initDto.setHenko(null);
			initDto.setHenkoChkFlg(false);
			String[] setone = { "1" };
			
			if (skf3020Sc004SharedService.checkBoxcheck(initDto.getHdnNyukyo()) != null || initDto.getHdnNyukyoChkFlg() == true) {
				initDto.setNyukyoChkFlg(true);
				initDto.setNyukyo(setone);
			} else {
				initDto.setNyukyoChkFlg(false);
				initDto.setNyukyo(null);
			}			
			if (skf3020Sc004SharedService.checkBoxcheck(initDto.getHdnTaikyo()) != null || initDto.getHdnTaikyoChkFlg() == true) {
				initDto.setTaikyoChkFlg(true);
				initDto.setTaikyo(setone);
			} else {
				initDto.setTaikyoChkFlg(false);
				initDto.setTaikyo(null);
			}			
			if (skf3020Sc004SharedService.checkBoxcheck(initDto.getHdnHenko()) != null || initDto.getHdnHenkoChkFlg() == true) {
				initDto.setHenkoChkFlg(true);
				initDto.setHenko(setone);
			} else {
				initDto.setHenkoChkFlg(false);
				initDto.setHenko(null);
			}			
			initDto.setGenShatakuKubun(initDto.getHdnGenShataku());
			initDto.setGenShozoku(initDto.getHdnGenShozoku());
			initDto.setShinShozoku(initDto.getHdnShinShozoku());
			initDto.setNyutaikyoYoteiSakuseiKubun(initDto.getHdnNyutaikyoYoteiSakuseiKubun());
			initDto.setBiko(initDto.getHdnBiko());
			
		} else {
			initDto.setShainNo(null);
			initDto.setShainName(null);
			initDto.setNyukyo(null);
			initDto.setNyukyoChkFlg(false);
			initDto.setTaikyo(null);
			initDto.setTaikyoChkFlg(false);
			initDto.setHenko(null);
			initDto.setHenkoChkFlg(false);
//			String[] setone = { "1" };			
//			initDto.setHenko(setone);
//			initDto.setHenkoChkFlg(true);
			initDto.setGenShatakuKubun(null);
			initDto.setGenShozoku(null);
			initDto.setShinShozoku(null);
			initDto.setNyutaikyoYoteiSakuseiKubun(null);
			initDto.setBiko(null);

		}

		// ========== 画面表示 ==========
		// 「現社宅区分」ドロップダウンリストの設定
		// 「入退居予定作成区分」ドロップダウンリストの設定
		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> genShatakuKubunList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> yoteiSakuseiList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3020Sc004SharedService.getDoropDownList(initDto.getGenShatakuKubun(), genShatakuKubunList,
				initDto.getNyutaikyoYoteiSakuseiKubun(), yoteiSakuseiList);

		initDto.setGenShatakuKubunList(genShatakuKubunList);
		initDto.setYoteiSakuseiList(yoteiSakuseiList);

        // グリッドビューデータの転任者一覧取得
		setGridViewData(initDto);
		
		return initDto;
	}
	
	
	/**
	 * 検索部のセット
	 * 
	 * @param initDto
	 */
	private void setGridViewData(Skf3020Sc004InitDto initDto){
		
        // 検索条件をもとに検索結果カウントの取得
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		// チェックボックスの状態を取得
		// ― 入居
		String nyukyoStr = skf3020Sc004SharedService.checkBoxcheck(initDto.getNyukyo());
		// ― 退居
		String taikyoStr = skf3020Sc004SharedService.checkBoxcheck(initDto.getTaikyo());
		// - 変更
		String henkoStr = skf3020Sc004SharedService.checkBoxcheck(initDto.getHenko());
		
		int listCount = skf3020Sc004SharedService.getListTableData(initDto.getShainNo(),
				initDto.getShainName(),
				nyukyoStr,
				taikyoStr,
				henkoStr,
				initDto.getGenShatakuKubun(),
				initDto.getGenShozoku(),
				initDto.getShinShozoku(),
				initDto.getNyutaikyoYoteiSakuseiKubun(),
				initDto.getBiko(),
				listTableData);
		
		if(listCount == 0){
			// 取得レコード0件のワーニング
			//ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
			//初期表示時はエラー表示しない
		}else{
			// 最大行数を設定
			initDto.setListTableMaxRowCount(listTableMaxRowCount);
		}
		
		initDto.setListTableData(listTableData);
		
	}
	
}
