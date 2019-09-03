/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
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
		skfOperationLogUtils.setAccessLog("入力内容をクリア処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

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
		boolean result = skf2030Sc001SharedService.setDisplayData(applInfo, clearDto);
		if (!result) {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			clearDto.setMaskPattern("ALLNG");
		}

		// 画面制御処理（活性／非活性）
		skf2030Sc001SharedService.setEnabled(clearDto, applInfo);

		return clearDto;
	}

	private int saveDispInfo(Map<String, String> applInfo, Skf2030Sc001ClearDto clearDto) {
		String applNo = applInfo.get("applNo");
		String applId = applInfo.get("applId");
		String applStatus = applInfo.get("status");
		String shainNo = applInfo.get("shainNo");
		// 申請履歴情報を更新する
		// 更新対象の申請履歴情報を取得
		Skf2030Sc001GetApplHistoryInfoForUpdateExp tmpData = new Skf2030Sc001GetApplHistoryInfoForUpdateExp();
		tmpData = skf2030Sc001SharedService.getApplHistoryInfoForUpdate(applNo, applId, applStatus, shainNo);
		if (tmpData == null) {
			ServiceHelper.addWarnResultMessage(clearDto, null, MessageIdConstant.W_SKF_1009);
			return -1;
		}
		// 排他チェック
		checkLockException(clearDto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE), tmpData.getUpdateDate());

		// 更新ステータス等の設定
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		// 主キー設定
		updateData.setCompanyCd(tmpData.getCompanyCd());
		updateData.setApplNo(tmpData.getApplNo());
		updateData.setShainNo(tmpData.getShainNo());
		updateData.setApplId(tmpData.getApplId());
		updateData.setApplDate(tmpData.getApplDate());
		// 更新内容設定
		updateData.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
		updateData.setApplTacFlg("0");

		boolean updApplHistoryResult = skf2030Sc001SharedService.updateApplHistory(updateData);
		if (!updApplHistoryResult) {
			ServiceHelper.addErrorResultMessage(clearDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		// 備品希望申請テーブルを更新
		Skf2030Sc001GetBihinShinseiInfoForUpdateExp bihinShinseiData = new Skf2030Sc001GetBihinShinseiInfoForUpdateExp();
		bihinShinseiData = skf2030Sc001SharedService.getBihinShinseiInfoForUpdate(applNo);
		if (bihinShinseiData == null) {
			ServiceHelper.addWarnResultMessage(clearDto, null, MessageIdConstant.W_SKF_1009);
			return -1;
		}
		// 排他チェック
		checkLockException(clearDto.getLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE),
				bihinShinseiData.getUpdateDate());

		// 更新データ作成
		Skf2030TBihinKiboShinsei updBihinShinseiData = new Skf2030TBihinKiboShinsei();
		// 主キー設定
		updBihinShinseiData.setCompanyCd(companyCd);
		updBihinShinseiData.setApplNo(applNo);
		// 更新データ設定
		if (NfwStringUtils.isNotEmpty(clearDto.getSessionDay())) {
			updBihinShinseiData.setSessionDay(clearDto.getSessionDay());
		}
		if (NfwStringUtils.isNotEmpty(clearDto.getSessionTime())) {
			updBihinShinseiData.setSessionTime(clearDto.getSessionTime());
		}
		if (NfwStringUtils.isNotEmpty(clearDto.getTel())) {
			updBihinShinseiData.setTel(clearDto.getTel());
		}
		if (NfwStringUtils.isNotEmpty(clearDto.getRenrakuSaki())) {
			updBihinShinseiData.setRenrakuSaki(clearDto.getRenrakuSaki());
		}
		boolean bihinKiboShinseiRes = skf2030Sc001SharedService.updateBihinKiboShinsei(updBihinShinseiData);
		if (!bihinKiboShinseiRes) {
			ServiceHelper.addErrorResultMessage(clearDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		// 備品申請テーブルを更新
		boolean bihinRes = skf2030Sc001SharedService.updateDispInfoOfBihin(applNo, clearDto);
		if (!bihinRes) {
			ServiceHelper.addErrorResultMessage(clearDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		clearDto.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
		// TODO:社宅管理データ連携処理実行

		return 1;
	}

}
