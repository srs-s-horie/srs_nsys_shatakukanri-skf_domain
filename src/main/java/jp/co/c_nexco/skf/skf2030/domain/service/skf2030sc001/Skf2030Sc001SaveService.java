/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc001;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2030Sc001.Skf2030Sc001GetBihinShinseiInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihinKiboShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2050Fc001BihinHenkyakuSinseiDataImport;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc001.Skf2030Sc001SaveDto;

/**
 * Skf2030Sc001 備品希望申請（申請者用)一時保存処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc001SaveService extends BaseServiceAbstract<Skf2030Sc001SaveDto> {

	@Autowired
	private Skf2030Sc001SharedService skf2030Sc001SharedService;
	@Autowired
	private Skf2050Fc001BihinHenkyakuSinseiDataImport skf2050Fc001BihinHenkyakuSinseiDataImport;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;

	private String companyCd = CodeConstant.C001;

	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";

	/**
	 * サービス処理を行う。
	 * 
	 * @param saveDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2030Sc001SaveDto index(Skf2030Sc001SaveDto saveDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("一時保存処理開始", CodeConstant.C001, FunctionIdConstant.SKF2030_SC001);

		// タイトル設定
		saveDto.setPageTitleKey(MessageIdConstant.SKF2030_SC001_TITLE);

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", saveDto.getSendApplStatus());
		applInfo.put("applNo", saveDto.getApplNo());
		applInfo.put("applId", saveDto.getApplId());
		applInfo.put("shainNo", saveDto.getHdnShainNo());

		// 備品情報を保存する
		int res = saveDispInfo(applInfo, saveDto);
		if (res < 0) {
			throwBusinessExceptionIfErrors(saveDto.getResultMessages());
			return saveDto;
		}

		// 画面内容の設定
		boolean result = skf2030Sc001SharedService.setDisplayData(applInfo, saveDto);
		if (!result) {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
			saveDto.setMaskPattern("ALLNG");
		}

		// 画面制御処理（活性／非活性）
		skf2030Sc001SharedService.setEnabled(saveDto, applInfo);

		return saveDto;
	}

	private int saveDispInfo(Map<String, String> applInfo, Skf2030Sc001SaveDto saveDto) {
		String applNo = applInfo.get("applNo");
		String applId = applInfo.get("applId");
		String applStatus = applInfo.get("status");
		String shainNo = applInfo.get("shainNo");

		Map<String, String> errorMsg = new HashMap<String, String>();

		// 申請履歴情報を更新する
		// 更新対象の申請履歴情報を取得
		SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp tmpData = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp();
		tmpData = skfApplHistoryInfoUtils.getApplHistoryInfoForUpdate(companyCd, shainNo, applNo, applId);
		if (tmpData == null) {
			ServiceHelper.addWarnResultMessage(saveDto, MessageIdConstant.W_SKF_1009);
			return -1;
		}
		// 排他チェック
		checkLockException(saveDto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE), tmpData.getUpdateDate());

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
			ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		// 備品希望申請テーブルを更新
		Skf2030Sc001GetBihinShinseiInfoForUpdateExp bihinShinseiData = new Skf2030Sc001GetBihinShinseiInfoForUpdateExp();
		bihinShinseiData = skf2030Sc001SharedService.getBihinShinseiInfoForUpdate(applNo);
		if (bihinShinseiData == null) {
			ServiceHelper.addWarnResultMessage(saveDto, MessageIdConstant.W_SKF_1009);
			return -1;
		}
		// 排他チェック
		checkLockException(saveDto.getLastUpdateDate(BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE),
				bihinShinseiData.getUpdateDate());

		// 更新データ作成
		Skf2030TBihinKiboShinsei updBihinShinseiData = new Skf2030TBihinKiboShinsei();
		// 主キー設定
		updBihinShinseiData.setCompanyCd(companyCd);
		updBihinShinseiData.setApplNo(applNo);
		// 更新データ設定
		if (NfwStringUtils.isNotEmpty(saveDto.getSessionDay())) {
			updBihinShinseiData.setSessionDay(saveDto.getSessionDay());
		}
		if (NfwStringUtils.isNotEmpty(saveDto.getSessionTime())) {
			updBihinShinseiData.setSessionTime(saveDto.getSessionTime());
		}
		if (NfwStringUtils.isNotEmpty(saveDto.getTel())) {
			updBihinShinseiData.setTel(saveDto.getTel());
		}
		if (NfwStringUtils.isNotEmpty(saveDto.getRenrakuSaki())) {
			updBihinShinseiData.setRenrakuSaki(saveDto.getRenrakuSaki());
		}
		boolean bihinKiboShinseiRes = skf2030Sc001SharedService.updateBihinKiboShinsei(updBihinShinseiData);
		if (!bihinKiboShinseiRes) {
			ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		// 備品申請テーブルを更新
		boolean bihinRes = updateDispInfoOfBihin(applNo, saveDto);
		if (!bihinRes) {
			ServiceHelper.addErrorResultMessage(saveDto, null, MessageIdConstant.E_SKF_1075);
			return -1;
		}

		saveDto.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);

		// TODO:社宅管理データ連携処理実行
		String status = CodeConstant.STATUS_ICHIJIHOZON;
		String pageId = FunctionIdConstant.SKF2030_SC001;
		List<String> resultBatch = skf2030Sc001SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo,
				status, pageId);
		if (resultBatch != null) {
			skf2050Fc001BihinHenkyakuSinseiDataImport.addResultMessageForDataLinkage(saveDto, resultBatch);
			throwBusinessExceptionIfErrors(saveDto.getResultMessages());
			skfRollBackExpRepository.rollBack();
			return -1;
		}

		return 1;
	}

	/**
	 * 備品申請テーブルを更新します
	 * 
	 * @param applNo
	 * @param dto
	 * @return
	 */
	private boolean updateDispInfoOfBihin(String applNo, Skf2030Sc001SaveDto dto) {
		// 登録備品情報を設定
		for (int bihinCd = 11; bihinCd <= 19; bihinCd++) {
			Skf2030TBihin updData = new Skf2030TBihin();
			updData.setCompanyCd(companyCd);
			updData.setApplNo(applNo);
			updData.setBihinCd(String.valueOf(bihinCd));
			String bihinAppl = CodeConstant.NONE;
			switch (String.valueOf(bihinCd)) {
			case CodeConstant.BIHIN_WASHER:
				bihinAppl = dto.getBihinAppl11();
				break;
			case CodeConstant.BIHIN_FREEZER:
				bihinAppl = dto.getBihinAppl12();
				break;
			case CodeConstant.BIHIN_OVEN:
				bihinAppl = dto.getBihinAppl13();
				break;
			case CodeConstant.BIHIN_CLENER:
				bihinAppl = dto.getBihinAppl14();
				break;
			case CodeConstant.BIHIN_RICE_COOKER:
				bihinAppl = dto.getBihinAppl15();
				break;
			case CodeConstant.BIHIN_TV:
				bihinAppl = dto.getBihinAppl16();
				break;
			case CodeConstant.BIHIN_TV_STANDS:
				bihinAppl = dto.getBihinAppl17();
				break;
			case CodeConstant.BIHIN_KOTATSU:
				bihinAppl = dto.getBihinAppl18();
				break;
			case CodeConstant.BIHIN_KICHEN_CABINET:
				bihinAppl = dto.getBihinAppl19();
				break;
			}
			updData.setBihinAppl(bihinAppl);
			if (!skf2030Sc001SharedService.updateBihinInfo(updData)) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
		}

		return true;
	}

}
