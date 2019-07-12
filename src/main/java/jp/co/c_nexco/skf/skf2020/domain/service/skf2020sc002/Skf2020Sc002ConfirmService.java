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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
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
	private Skf2020Sc002UpdateNyukyoKiboInfoExpRepository skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	@Override
	public BaseDto index(Skf2020Sc002ConfirmDto checkDto) throws Exception {

		// ServiceHelper.addErrorResultMessage(checkDto, null,
		// MessageIdConstant.E_SKF_1077);

		// バイトカット処理
		skf2020Sc002SharedService.cutByte(checkDto);

		// 登録処理
		confirmClickProcess(checkDto);
		return checkDto;
	}

	/**
	 * btnConfirm（申請内容を確認する）が押下された時の処理
	 *
	 * @param applKbn
	 * @param status
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public void confirmClickProcess(Skf2020Sc002ConfirmDto checkDto)
			throws IllegalAccessException, InvocationTargetException {

		// ステータスの設定
		Map<String, String> applInfo = skf2020Sc002SharedService.getSkfApplInfo(checkDto);
		// 申請者用のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		// 申請対象区分の取得
		String applKbn = checkDto.getApplKbn();
		// 申請対象区分の判定
		if (CodeConstant.OUT_INPUT.equals(applKbn)) {
			// アウトソース用の場合、ステータスを変更しない
			// 頻出データをセッションから変数に取得
			newStatus = applInfo.get("status");
		}
		applInfo.put("newStatus", newStatus);

		// 一時保存処理を実行
		saveInfo(applInfo, checkDto);

		// 申請書類確認に遷移
		List<Map<String, Object>> resultApplList = null;
		// 次の画面に渡すパラメータをセッションに格納
		resultApplList = new ArrayList<Map<String, Object>>();
		Map<String, Object> applMap = new HashMap<String, Object>();
		applMap.put(SkfCommonConstant.KEY_STATUS, checkDto.getStatus());
		applMap.put(SkfCommonConstant.KEY_APPL_NO, checkDto.getApplNo());
		resultApplList.add(applMap);
		menuScopeSessionBean.put(SessionCacheKeyConstant.APPL_INFO_SESSION_KEY, resultApplList);

		// フォームデータを設定
		checkDto.setPrePageId(checkDto.getPageId());
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, checkDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC002, form);

		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC002);
		checkDto.setTransferPageInfo(nextPage);
	}

	/**
	 * 一時保存処理
	 * 
	 * @param applInfo
	 * @param checkDto
	 */
	private void saveInfo(Map<String, String> applInfo, Skf2020Sc002ConfirmDto checkDto) {

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
			checkDto.setHdnStatus(CodeConstant.STATUS_MISAKUSEI);
			// 新規登録処理
			if (skf2020Sc002SharedService.saveNewData(checkDto, applInfo)) {

				if (checkDto.getShatakuList() != null
						&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(checkDto.getHdnBihinHenkyakuUmu())
						&& CodeConstant.LEAVE.equals(checkDto.getTaikyoYotei())) {
					// 退居社宅かつ返却備品ががある場合は備品返却の作成
					if (checkDto.getNowShatakuNo() != null || NfwStringUtils.isNotEmpty(checkDto.getNowShatakuNo())) {
						// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
						String bihinHenkaykuShinseiApplNo = skf2020Sc002SharedService.getBihinHenkyaku(checkDto);
						// なければ備品返却申請の書類管理番号を新規発行
						if (checkDto.getNowShatakuNo() == null || NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
							// 備品返却申請用の申請書類管理番号を取得
							bihinHenkaykuShinseiApplNo = skfShinseiUtils
									.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001, checkDto.getShainNo());
							// 備品返却申請テーブルを新規発行
							skf2020Sc002SharedService.insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, checkDto,
									applInfo);
						} else {
							// 備品返却申請テーブルを更新
							int registBihinCount = 0;
							applInfo.put("dateUpdateFlg", "1");
							Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
							setValue.setLastUpdateDate(checkDto
									.getLastUpdateDate(Skf2020Sc002SharedService.BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE));
							registBihinCount = updateBihinHenkyakuInfo(setValue, checkDto, applInfo,
									bihinHenkaykuShinseiApplNo);
							LogUtils.debugByMsg("申請書類履歴テーブル更新件数：" + registBihinCount + "件");
						}
					}
				}
			}

		} else {
			// 新規以外
			checkDto.setHdnStatus(applInfo.get("status"));
			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存時、実行時点の日付時刻を使用
				applInfo.put("dateUpdateFlg", "1");

				setApplValue.setLastUpdateDate(
						checkDto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, checkDto, applInfo);
				LogUtils.debugByMsg("申請書類履歴テーブル更新件数：" + registHistoryCount + "件");
				break;
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SASHIMODOSHI:

				break;
			default:
				// 申請日時を更新しない
				applInfo.put("dateUpdateFlg", "");
				setApplValue.setLastUpdateDate(
						checkDto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, checkDto, applInfo);
			}

			// 入居希望等調書申請テーブルの設定
			int registNyukyoCount = 0;
			Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
			setValue.setLastUpdateDate(
					checkDto.getLastUpdateDate(Skf2020Sc002SharedService.NYUKYO_KEY_LAST_UPDATE_DATE));
			registNyukyoCount = updateNyukyoChoshoTsuchi(setValue, checkDto);
			LogUtils.debugByMsg("入居希望等調書申請テーブル更新件数：" + registNyukyoCount + "件");
			// ステータスを更新
			checkDto.setStatus(applInfo.get("newStatus"));
		}

	}

	/**
	 * 申請書履歴の更新処理
	 * 
	 * @param setValue
	 * @param saveDto
	 * @param applInfo
	 * @return
	 */
	private int updateApplHistoryAgreeStatusIchiji(Skf2010TApplHistory setValue, Skf2020Sc002CommonDto saveDto,
			Map<String, String> applInfo) {
		// 申請書類履歴テーブルの設定
		Skf2010TApplHistory resultVal = new Skf2010TApplHistory();
		// 設定値
		setValue = skf2020Sc002SharedService.updateApplHistoryAgreeStatusIchiji(setValue, saveDto, applInfo);
		// 排他チェック
		super.checkLockException(setValue.getLastUpdateDate(), resultVal.getUpdateDate());
		return skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setValue);
	}

	/**
	 * 入居希望等調書テーブルの更新処理
	 * 
	 * @param setValue
	 * @param saveDto
	 * @return 登録件数
	 */
	private int updateNyukyoChoshoTsuchi(Skf2020TNyukyoChoshoTsuchi setValue, Skf2020Sc002CommonDto dto) {
		// 入居希望等調書テーブルの設定
		Map<String, String> applInfo = null;
		Skf2020TNyukyoChoshoTsuchi resultVal = new Skf2020TNyukyoChoshoTsuchi();
		// 設定値
		setValue = skf2020Sc002SharedService.setNyukyoChoshoTsuchi(dto, setValue, applInfo);
		// 排他チェック
		super.checkLockException(setValue.getLastUpdateDate(), resultVal.getUpdateDate());

		return skf2020Sc002UpdateNyukyoKiboInfoExpRepository.updateNyukyoKiboInfo(setValue);
	}

	/**
	 * 備品返却申請テーブルの更新処理
	 * 
	 * @param dto
	 * @param applInfo
	 * @param bihinHenkaykuShinseiApplNo
	 * @return 登録件数
	 */
	private int updateBihinHenkyakuInfo(Skf2050TBihinHenkyakuShinsei setValue, Skf2020Sc002CommonDto dto,
			Map<String, String> applInfo, String bihinHenkaykuShinseiApplNo) {

		// 備品返却申請テーブルの設定
		Skf2050TBihinHenkyakuShinsei resultVal = new Skf2050TBihinHenkyakuShinsei();

		// 設定値
		setValue = skf2020Sc002SharedService.setColumnInfoBihinList(setValue, dto, applInfo,
				bihinHenkaykuShinseiApplNo);
		// 排他チェック
		super.checkLockException(setValue.getLastUpdateDate(), resultVal.getUpdateDate());

		return skf2020Sc002UpdateBihinHenkyakuInfoExpRepository.updateBihinHenkyakuInfo(setValue);

	}

}
