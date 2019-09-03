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
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001.Skf2030Sc001InitDto;

/**
 * Skf2030Sc001 備品希望申請（申請者用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc001InitService extends BaseServiceAbstract<Skf2030Sc001InitDto> {

	@Autowired
	private Skf2030Sc001SharedService skf2030Sc001SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2030Sc001InitDto index(Skf2030Sc001InitDto initDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2030_SC001_TITLE);

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", initDto.getSendApplStatus());
		applInfo.put("applNo", initDto.getApplNo());
		applInfo.put("applId", initDto.getApplId());

		// 画面内容の設定
		boolean result = skf2030Sc001SharedService.setDisplayData(applInfo, initDto);
		if (!result) {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			initDto.setMaskPattern("ALLNG");
		}

		// 画面制御処理（活性／非活性）
		skf2030Sc001SharedService.setEnabled(initDto, applInfo);

		return initDto;
	}

}
