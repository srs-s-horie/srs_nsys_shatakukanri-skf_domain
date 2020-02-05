/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */

package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002ClearDto;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)のクリアボタン押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc002ClearService extends BaseServiceAbstract<Skf2020Sc002ClearDto> {

	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	protected BaseDto index(Skf2020Sc002ClearDto clearDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("入力内容をクリア", CodeConstant.C001, clearDto.getPageId());

		// クリア処理を呼び出す。
		clearDisp(clearDto);

		return clearDto;
	}

	/**
	 * 画面クリア処理
	 * 
	 * @param clearDto
	 */
	private void clearDisp(Skf2020Sc002ClearDto clearDto) {

		// 入力情報のクリア
		skf2020Sc002SharedService.setClearInfo(clearDto);

		/*
		 * 保存状態の復帰
		 */
		// ドロップダウンの設定
		skf2020Sc002SharedService.setControlDdl(clearDto);
		// 登録済みデータの情報設定
		skf2020Sc002SharedService.setSinseiInfo(clearDto, false);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(clearDto, Skf2020Sc002SharedService.UPDATE_FLG);
		// 表示項目の活性制御または表示制御
		skf2020Sc002SharedService.setControlValue(clearDto);

	}

}
