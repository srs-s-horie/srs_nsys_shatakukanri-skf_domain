/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
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
 		
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& initDto.getPrePageId().equals(FunctionIdConstant.SKF3010_SC005)) {
			// 転任者情報登録画面からの遷移
			// 登録画面からのhidden項目(検索条件)をinitDtoに詰めなおす
			initDto.setShainNo(initDto.getHdnShainNo());
			initDto.setShainName(initDto.getHdnShainName());
			initDto.setNyukyo(initDto.getHdnNyukyo());
			initDto.setTaikyo(initDto.getHdnTaikyo());
			initDto.setHenko(initDto.getHdnHenko());
			initDto.setGenShatakuKubun(initDto.getHdnGenShataku());
			initDto.setGenShozoku(initDto.getHdnGenShozoku());
			initDto.setShinShozoku(initDto.getHdnShinShozoku());
			initDto.setNyutaikyoYoteiSakuseiKubun(initDto.getHdnNyutaikyoYoteiSakuseiKubun());
			initDto.setBiko(initDto.getHdnBiko());

			// '入居
			// If
			// ("1").Equals(HttpUtility.HtmlEncode(tenninshaInfoEntity.Nyukyo()))
			// Then
			// Me.chkNyukyo.Checked = True
			// End If
			// '退居
			// If
			// ("1").Equals(HttpUtility.HtmlEncode(tenninshaInfoEntity.Taikyo()))
			// Then
			// Me.chkTaikyo.Checked = True
			// End If
			// '入居状態変更
			// If
			// ("1").Equals(HttpUtility.HtmlEncode(tenninshaInfoEntity.Henko()))
			// Then
			// Me.chkHenko.Checked = True
			// End If

		} else {
			initDto.setShainNo(null);
			initDto.setShainName(null);
			initDto.setNyukyo("1");
			initDto.setTaikyo(null);
			initDto.setHenko(null);
			initDto.setGenShatakuKubun(null);
			initDto.setGenShozoku(null);
			initDto.setShinShozoku(null);
			initDto.setNyutaikyoYoteiSakuseiKubun(null);
			initDto.setBiko(null);

			String[] setone = { "1" };
			if (initDto.getNyukyo() == "1") {
				initDto.setChkNyukyo(setone);
			}
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

		return initDto;
	}
	
}
