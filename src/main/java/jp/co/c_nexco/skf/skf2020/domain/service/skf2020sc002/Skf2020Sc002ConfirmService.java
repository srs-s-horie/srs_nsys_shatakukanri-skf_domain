/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002ConfirmDto;

/**
 * 
 * 入居希望等調書申請画面の申請内容を確認ボタンの登録処理
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2020Sc002ConfirmService extends BaseServiceAbstract<Skf2020Sc002ConfirmDto> {

	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2020Sc002UpdateNyukyoKiboInfoExpRepository skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	// 更新フラグ
	public static final String UPDATE_FLG = "1";
	public static final String NO_UPDATE_FLG = "0";

	// 最終更新日付のキャッシュキー
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf1010_t_appl_history_UpdateDate";
	public static final String NYUKYO_KEY_LAST_UPDATE_DATE = "skf2020_t_nyukyo_chosho_UpdateDate";
	public static final String BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE = "skf2050_t_bihin_henkyaku_UpdateDate";

	@Override
	public BaseDto index(Skf2020Sc002ConfirmDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請内容を確認する", CodeConstant.C001, dto.getPageId());

		// バイトカット処理
		skf2020Sc002SharedService.cutByte(dto);

		// 登録処理
		confirmClickProcess(dto);
		return dto;
	}

	/**
	 * btnConfirm（申請内容を確認する）が押下された時の処理
	 *
	 * @param applKbn
	 * @param status
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void confirmClickProcess(Skf2020Sc002ConfirmDto dto)
			throws IllegalAccessException, InvocationTargetException {

		// ステータスの設定
		Map<String, String> applInfo = skf2020Sc002SharedService.getSkfApplInfo(dto);
		// 申請者用のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		// 申請対象区分の取得
		String applKbn = dto.getApplKbn();
		// 申請対象区分の判定
		if (CodeConstant.OUT_INPUT.equals(applKbn)) {
			// アウトソース用の場合、ステータスを変更しない
			// 頻出データをセッションから変数に取得
			newStatus = applInfo.get("status");
		}
		applInfo.put("newStatus", newStatus);

		// 一時保存処理を実行
		saveInfo(applInfo, dto);

		// 申請書類確認に遷移
		// 次の画面に渡すパラメータをセッションに格納
		List<Map<String, Object>> resultApplList = null;
		resultApplList = new ArrayList<Map<String, Object>>();
		Map<String, Object> applMap = new HashMap<String, Object>();
		applMap.put(SkfCommonConstant.KEY_STATUS, dto.getStatus());
		applMap.put(SkfCommonConstant.KEY_APPL_NO, dto.getApplNo());
		resultApplList.add(applMap);
		menuScopeSessionBean.put(SessionCacheKeyConstant.APPL_INFO_SESSION_KEY, resultApplList);

		// フォームデータを設定
		dto.setPrePageId(dto.getPageId());
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, dto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC002, form);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC002);
		dto.setTransferPageInfo(nextPage, true);
	}

	/**
	 * 一時保存処理
	 * 
	 * @param applInfo
	 * @param dto
	 */
	private void saveInfo(Map<String, String> applInfo, Skf2020Sc002ConfirmDto dto) {

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
			dto.setHdnStatus(CodeConstant.STATUS_MISAKUSEI);
			// 新規登録処理
			applInfo.put("updateFlg", NO_UPDATE_FLG);
			if (skf2020Sc002SharedService.saveNewData(dto, applInfo)) {

				// 退居社宅がある場合は備品返却の作成
				if (NfwStringUtils.isNotEmpty(dto.getNowShatakuNo())) {
					if (dto.getTaikyoYotei() != null
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(dto.getHdnBihinHenkyakuUmu())
							&& CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {

						// 備品返却申請テーブルから備品返却申請情報を取得
						Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
						bihinHenkyakuInfo = skf2020Sc002SharedService.getBihinHenkyaku(dto);

						// 情報が取得できた場合は、備品返却申請書番号の設定
						String bihinHenkaykuShinseiApplNo = null;
						if (bihinHenkyakuInfo != null) {
							bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getTaikyoApplNo();
						}

						// 備品返却申請書番号がなければ備品返却申請の書類管理番号を新規発行
						if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
							// 備品返却申請用の申請書類管理番号を取得
							bihinHenkaykuShinseiApplNo = skfShinseiUtils
									.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001, dto.getShainNo());
							// 備品返却申請テーブルを新規発行
							skf2020Sc002SharedService.insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, dto,
									applInfo);
						} else {
							// 備品返却申請テーブルを更新
							int registBihinCount = 0;
							// 項目の値設定
							Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
							// 更新処理
							registBihinCount = skf2020Sc002SharedService.updateBihinHenkyakuInfo(setValue, dto,
									applInfo, bihinHenkyakuInfo);
							LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + registBihinCount + "件");
						}
					}
				}
			}
		} else {
			// 新規以外
			dto.setHdnStatus(applInfo.get("status"));
			applInfo.put("updateFlg", UPDATE_FLG);
			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存の場合
				applInfo.put("dateUpdateFlg", "1");
				setApplValue.setLastUpdateDate(
						dto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, dto, applInfo);
				LogUtils.debugByMsg("申請情報履歴テーブル更新件数：" + registHistoryCount + "件");
				break;
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SASHIMODOSHI:
				break;
			default:
				// 申請日時を更新しない
				applInfo.put("dateUpdateFlg", "0");
				setApplValue.setLastUpdateDate(
						dto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, dto, applInfo);
			}

			// 入居希望等調書申請テーブルの設定
			int registNyukyoCount = 0;
			Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
			// 入居希望等調書申請の更新処理
			registNyukyoCount = skf2020Sc002SharedService.updateNyukyoChoshoTsuchi(setValue, dto, applInfo);
			LogUtils.debugByMsg("入居希望等調書申請テーブル更新件数：" + registNyukyoCount + "件");
			// ステータスを更新
			dto.setStatus(applInfo.get("newStatus"));

			// 退居社宅がある場合は備品返却の作成
			if (NfwStringUtils.isNotEmpty(dto.getNowShatakuNo())) {
				if (dto.getTaikyoYotei() != null
						&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(dto.getHdnBihinHenkyakuUmu())
						&& CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {

					// 備品返却申請テーブルから備品返却申請情報を取得
					Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
					bihinHenkyakuInfo = skf2020Sc002SharedService.getBihinHenkyaku(dto);

					// 情報が取得できた場合は、備品返却申請書番号の設定
					String bihinHenkaykuShinseiApplNo = null;
					if (bihinHenkyakuInfo != null) {
						bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getTaikyoApplNo();
					}

					// 備品返却申請書番号がなければ備品返却申請の書類管理番号を新規発行
					if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
						// 備品返却申請用の申請書類管理番号を取得
						bihinHenkaykuShinseiApplNo = skfShinseiUtils.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001,
								dto.getShainNo());
						// 備品返却申請テーブルを新規発行
						skf2020Sc002SharedService.insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, dto, applInfo);
					} else {
						// 備品返却申請テーブルを更新
						int registBihinCount = 0;
						// 項目の値設定
						Skf2050TBihinHenkyakuShinsei setBihinValue = new Skf2050TBihinHenkyakuShinsei();
						// 更新処理
						registBihinCount = skf2020Sc002SharedService.updateBihinHenkyakuInfo(setBihinValue, dto,
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
	//
	// /**
	// * 入居希望等調書テーブルの更新処理
	// *
	// * @param setValue
	// * @param Skf2020Sc002CommonDto
	// * @param applInfo
	// * @return 登録件数
	// */
	// private int updateNyukyoChoshoTsuchi(Skf2020TNyukyoChoshoTsuchi setValue,
	// Skf2020Sc002CommonDto dto,
	// Map<String, String> applInfo) {
	//
	// // 社宅入居希望等調査・入居決定通知テーブル情報の取得
	// Skf2020TNyukyoChoshoTsuchi nyukyoChoshoList = new
	// Skf2020TNyukyoChoshoTsuchi();
	// Skf2020TNyukyoChoshoTsuchiKey setKey = new
	// Skf2020TNyukyoChoshoTsuchiKey();
	// // 条件項目をセット
	// setKey.setCompanyCd(CodeConstant.C001);
	// setKey.setApplNo(dto.getApplNo());
	// nyukyoChoshoList =
	// skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(setKey);
	// LogUtils.debugByMsg("社宅入居希望等調査情報： " + nyukyoChoshoList);
	//
	// // 設定値
	// setValue = skf2020Sc002SharedService.setNyukyoChoshoTsuchi(dto, setValue,
	// applInfo);
	// // 排他チェック
	// super.checkLockException(dto.getLastUpdateDate(NYUKYO_KEY_LAST_UPDATE_DATE),
	// nyukyoChoshoList.getUpdateDate());
	// return
	// skf2020Sc002UpdateNyukyoKiboInfoExpRepository.updateNyukyoKiboInfo(setValue);
	// }
	//
	// /**
	// * 備品返却申請テーブルの更新処理
	// *
	// * @param setValue
	// * @param dto
	// * @param applInfo
	// * @param bihinHenkyakuInfo
	// * @return
	// */
	// private int updateBihinHenkyakuInfo(Skf2050TBihinHenkyakuShinsei
	// setValue, Skf2020Sc002CommonDto dto,
	// Map<String, String> applInfo,
	// Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo) {
	//
	// // 備品返却申請テーブルの設定
	// // 設定値
	// setValue = skf2020Sc002SharedService.setColumnInfoBihinList(setValue,
	// dto, applInfo,
	// bihinHenkyakuInfo.getTaikyoApplNo());
	// // 排他チェック
	// super.checkLockException(dto.getLastUpdateDate(BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE),
	// bihinHenkyakuInfo.getUpdateDate());
	// return
	// skf2020Sc002UpdateBihinHenkyakuInfoExpRepository.updateBihinHenkyakuInfo(setValue);
	//
	// }

}
