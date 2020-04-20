/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002SaveDto;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)の一時保存押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc002SaveService extends SkfServiceAbstract<Skf2020Sc002SaveDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020Sc002GetApplHistoryInfoExpRepository skf2020Sc002GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	// 更新フラグ
	protected static final String NO_UPDATE_FLG = "0";
	protected static final String UPDATE_FLG = "1";

	@Override
	public BaseDto index(Skf2020Sc002SaveDto saveDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, saveDto.getPageId());

		// 申請書情報の取得
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2020Sc002SharedService.getSkfApplInfo(saveDto);
		// 次のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		applInfo.put("newStatus", newStatus);

		// 一時保存処理
		if (!skf2020Sc002SharedService.saveInfo(applInfo, saveDto)) {
			return saveDto;
		}

		// 入力情報のクリア
		skf2020Sc002SharedService.setClearInfo(saveDto);

		// ドロップダウンの設定
		skf2020Sc002SharedService.setControlDdl(saveDto);
		// 登録済みデータの情報設定
		skf2020Sc002SharedService.setSinseiInfo(saveDto, true);
		// 社宅情報の設定
		skf2020Sc002SharedService.setShatakuInfo(saveDto, UPDATE_FLG);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(saveDto, Skf2020Sc002SharedService.UPDATE_FLG);
		// 画面表示制御再設定
		skf2020Sc002SharedService.setControlValue(saveDto);

		// 正常終了
		if (CodeConstant.STATUS_MISAKUSEI.equals(applInfo.get("status"))) {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
		} else {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
		}

		return saveDto;
	}

	/**
	 * 排他制御用の値再取得
	 * 
	 * @param saveDto
	 */
	private void setLastUpdateDate(Skf2020Sc002SaveDto saveDto) {
		/**
		 * 申請書類履歴テーブル情報の取得
		 */
		// 申請書類履歴テーブルから申請日の取得
		List<Skf2020Sc002GetApplHistoryInfoExp> historyInfo = new ArrayList<Skf2020Sc002GetApplHistoryInfoExp>();
		Skf2020Sc002GetApplHistoryInfoExpParameter parameter = new Skf2020Sc002GetApplHistoryInfoExpParameter();
		parameter.setCompanyCd(CodeConstant.C001);
		parameter.setApplNo(saveDto.getApplNo());
		historyInfo = skf2020Sc002GetApplHistoryInfoExpRepository.getApplHistoryInfo(parameter);
		// 申請書類履歴テーブル排他制御用更新日の取得
		if (historyInfo != null && historyInfo.size() > 0) {
			saveDto.addLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY,
					historyInfo.get(0).getUpdateDate());
		}

		/**
		 * 備品返却申請テーブル情報の取得
		 */
		// 備品返却申請テーブルから備品返却申請情報を取得
		Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
		bihinHenkyakuInfo = skf2020Sc002SharedService.getBihinHenkyaku(saveDto.getApplNo());

		// 備品返却申請テーブル排他制御用更新日の取得
		if (bihinHenkyakuInfo != null) {
			saveDto.addLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE_BIHIN,
					bihinHenkyakuInfo.getUpdateDate());
		}

		/**
		 * 社宅入居希望等調査・入居決定通知テーブル情報の取得
		 */
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoList = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey setValue = new Skf2020TNyukyoChoshoTsuchiKey();
		// 条件項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(saveDto.getApplNo());
		nyukyoChoshoList = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(setValue);

		// 社宅入居希望等調査・入居決定通知テーブル排他制御用更新日の取得
		if (nyukyoChoshoList != null) {
			saveDto.addLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE_NYUKYO,
					nyukyoChoshoList.getUpdateDate());
		}
	}
}
