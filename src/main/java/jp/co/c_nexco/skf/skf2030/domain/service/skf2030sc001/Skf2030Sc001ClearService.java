/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001.Skf2030Sc001ClearDto;

/**
 * Skf2030Sc001 備品希望申請（申請者用)入力内容をクリア処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc001ClearService extends BaseServiceAbstract<Skf2030Sc001ClearDto> {

	@Autowired
	private Skf2030Sc001SharedService skf2030Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	/**
	 * サービス処理を行う。
	 * 
	 * @param clearDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2030Sc001ClearDto index(Skf2030Sc001ClearDto clearDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("入力内容をクリア", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

		// タイトル設定
		clearDto.setPageTitleKey(MessageIdConstant.SKF2030_SC001_TITLE);

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", clearDto.getSendApplStatus());
		applInfo.put("applNo", clearDto.getApplNo());
		applInfo.put("applId", clearDto.getApplId());
		applInfo.put("shainNo", clearDto.getHdnShainNo());

		// 勤務先のTEL
		clearDto.setTel(CodeConstant.NONE);
		// 搬入希望日
		clearDto.setSessionDay(CodeConstant.NONE);
		// 搬入希望時間
		clearDto.setSessionTime(CodeConstant.NONE);
		// 連絡先
		clearDto.setRenrakuSaki(CodeConstant.NONE);

		// 画面内容の設定
		boolean result = skf2030Sc001SharedService.setDisplayData(clearDto);
		if (!result) {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			clearDto.setMaskPattern("ALLNG");
		}

		// 画面制御処理（活性／非活性）
		skf2030Sc001SharedService.setEnabled(clearDto, applInfo);

		return clearDto;
	}

}
