/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002SaveDto;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)の一時保存押下時のサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc002SaveService extends BaseServiceAbstract<Skf2020Sc002SaveDto> {

	// 更新フラグ
	public static final String UPDATE_FLG = "1";
	public static final String NO_UPDATE_FLG = "0";

	// 最終更新日付のキャッシュキー
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf1010_t_appl_history_UpdateDate";
	public static final String NYUKYO_KEY_LAST_UPDATE_DATE = "skf2020_t_nyukyo_chosho_UpdateDate";
	public static final String BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE = "skf2050_t_bihin_henkyaku_UpdateDate";

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;

	@Override
	public BaseDto index(Skf2020Sc002SaveDto saveDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("一時保存", CodeConstant.C001, saveDto.getPageId());

		// 申請書情報の取得
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2020Sc002SharedService.getSkfApplInfo(saveDto);
		// 次のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		// 申請対象区分の取得
		String applKbn = saveDto.getApplKbn();
		// 申請対象区分の判定
		if (CodeConstant.OUT_INPUT.equals(applKbn)) {
			// アウトソース用の場合、ステータスを変更しない
			// 頻出データをセッションから変数に取得
			newStatus = applInfo.get("status");
		}
		applInfo.put("newStatus", newStatus);

		// 画面情報の保持
		// ドロップダウンの設定
		skf2020Sc002SharedService.setControlDdl(saveDto);
		// 画面表示制御再設定
		skf2020Sc002SharedService.setControlValue(saveDto);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(saveDto);

		// バイトカット処理
		skf2020Sc002SharedService.cutByte(saveDto);

		// 一時保存処理
		saveInfo(applInfo, saveDto);

		// 正常終了
		if (CodeConstant.STATUS_MISAKUSEI.equals(saveDto.getHdnStatus())) {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
		} else {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
		}

		return saveDto;
	}

	/**
	 * 一時保存処理
	 * 
	 * @param applInfo
	 * @param saveDto
	 */
	private void saveInfo(Map<String, String> applInfo, Skf2020Sc002SaveDto saveDto) {

		// 頻出データをセッションから変数に取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		// 更新者情報取得
		applInfo.put("shainNo", loginUserInfoMap.get("shainNo"));
		// 添付ファイルの有無
		Map<String, String> applTacInfoMap = skfShinseiUtils.getApplAttachFlg(applInfo.get("shainNo"),
				applInfo.get("applNo"));
		applInfo.put("applTacFlg", applTacInfoMap.get("applTacFlg"));

		if (CodeConstant.STATUS_MISAKUSEI.equals(applInfo.get("status"))) {
			// 指定なし（新規）の場合
			saveDto.setHdnStatus(CodeConstant.STATUS_MISAKUSEI);
			// 新規登録処理
			applInfo.put("updateFlg", NO_UPDATE_FLG);
			if (skf2020Sc002SharedService.saveNewData(saveDto, applInfo)) {

				// 退居社宅がある場合は備品返却の作成
				if (NfwStringUtils.isNotEmpty(saveDto.getNowShatakuNo())) {
					if (saveDto.getTaikyoYotei() != null
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(saveDto.getHdnBihinHenkyakuUmu())
							&& CodeConstant.LEAVE.equals(saveDto.getTaikyoYotei())) {

						// 備品返却申請テーブルから備品返却申請情報を取得
						Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
						bihinHenkyakuInfo = skf2020Sc002SharedService.getBihinHenkyaku(saveDto);

						// 情報が取得できた場合は、備品返却申請書番号の設定
						String bihinHenkaykuShinseiApplNo = null;
						if (bihinHenkyakuInfo != null) {
							bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getTaikyoApplNo();
						}

						// 備品返却申請書番号がなければ備品返却申請の書類管理番号を新規発行
						if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
							// 備品返却申請用の申請書類管理番号を取得
							bihinHenkaykuShinseiApplNo = skfShinseiUtils
									.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001, saveDto.getShainNo());
							// 備品返却申請テーブルを新規発行
							skf2020Sc002SharedService.insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, saveDto,
									applInfo);
						} else {
							// 備品返却申請テーブルを更新
							int registBihinCount = 0;
							// 項目の値設定
							Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
							// 更新処理
							registBihinCount = skf2020Sc002SharedService.updateBihinHenkyakuInfo(setValue, saveDto,
									applInfo, bihinHenkyakuInfo);
							LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + registBihinCount + "件");
						}
					}
				}
			}
		} else {
			// 新規以外
			saveDto.setHdnStatus(applInfo.get("status"));
			applInfo.put("updateFlg", UPDATE_FLG);
			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存の場合
				applInfo.put("dateUpdateFlg", "1");
				setApplValue.setLastUpdateDate(
						saveDto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, saveDto, applInfo);
				LogUtils.debugByMsg("申請情報履歴テーブル更新件数：" + registHistoryCount + "件");
				break;
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SASHIMODOSHI:
				break;
			default:
				// 申請日時を更新しない
				applInfo.put("dateUpdateFlg", "0");
				setApplValue.setLastUpdateDate(
						saveDto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, saveDto, applInfo);
			}

			// 入居希望等調書申請テーブルの設定
			int registNyukyoCount = 0;
			Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
			// 入居希望等調書申請の更新処理
			registNyukyoCount = skf2020Sc002SharedService.updateNyukyoChoshoTsuchi(setValue, saveDto, applInfo);
			LogUtils.debugByMsg("入居希望等調書申請テーブル更新件数：" + registNyukyoCount + "件");
			// ステータスを更新
			saveDto.setStatus(applInfo.get("newStatus"));

			// 退居社宅がある場合は備品返却の作成
			if (NfwStringUtils.isNotEmpty(saveDto.getNowShatakuNo())) {
				if (saveDto.getTaikyoYotei() != null
						&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(saveDto.getHdnBihinHenkyakuUmu())
						&& CodeConstant.LEAVE.equals(saveDto.getTaikyoYotei())) {

					// 備品返却申請テーブルから備品返却申請情報を取得
					Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
					bihinHenkyakuInfo = skf2020Sc002SharedService.getBihinHenkyaku(saveDto);

					// 情報が取得できた場合は、備品返却申請書番号の設定
					String bihinHenkaykuShinseiApplNo = null;
					if (bihinHenkyakuInfo != null) {
						bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getTaikyoApplNo();
					}

					// 備品返却申請書番号がなければ備品返却申請の書類管理番号を新規発行
					if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
						// 備品返却申請用の申請書類管理番号を取得
						bihinHenkaykuShinseiApplNo = skfShinseiUtils.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001,
								saveDto.getShainNo());
						// 備品返却申請テーブルを新規発行
						skf2020Sc002SharedService.insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, saveDto,
								applInfo);
					} else {
						// 備品返却申請テーブルを更新
						int registBihinCount = 0;
						// 項目の値設定
						Skf2050TBihinHenkyakuShinsei setBihinValue = new Skf2050TBihinHenkyakuShinsei();
						// 更新処理
						registBihinCount = skf2020Sc002SharedService.updateBihinHenkyakuInfo(setBihinValue, saveDto,
								applInfo, bihinHenkyakuInfo);
						LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + registBihinCount + "件");
					}
				}
			}
		}
	}

	/**
	 * 申請書履歴テーブルの更新処理
	 *
	 * @param setValue
	 * @param saveDto
	 * @param applInfo
	 * @return
	 */
	private int updateApplHistoryAgreeStatusIchiji(Skf2010TApplHistory setValue, Skf2020Sc002CommonDto dto,
			Map<String, String> applInfo) {

		int resultCnt = 0;
		// 排他制御用更新日取得
		// 更新日付を取得する
		Skf2010TApplHistory resultUpdateDate = skf2020Sc002SharedService.selectByApplHistoryPrimaryKey(setValue, dto);
		// 排他チェック
		super.checkLockException(dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE),
				resultUpdateDate.getUpdateDate());

		// 更新値の設定
		setValue = skf2020Sc002SharedService.setUpdateApplHistoryAgreeStatusIchiji(setValue, dto, applInfo);
		// 更新
		resultCnt = skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setValue);

		// 排他制御の比較用更新日を再設定
		// 更新日付を取得する
		Skf2010TApplHistory reResultUpdateDate = skf2020Sc002SharedService.selectByApplHistoryPrimaryKey(setValue, dto);
		dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, reResultUpdateDate.getUpdateDate());

		return resultCnt;
	}
}
