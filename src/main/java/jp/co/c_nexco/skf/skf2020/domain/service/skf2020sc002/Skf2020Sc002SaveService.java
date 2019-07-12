/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.HashMap;
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
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
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

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020Sc002UpdateNyukyoKiboInfoExpRepository skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;

	@Override
	public BaseDto index(Skf2020Sc002SaveDto saveDto) throws Exception {

		// if (NfwStringUtils.isEmpty(saveDto.getTel())) {
		// ServiceHelper.addErrorResultMessage(saveDto, null,
		// MessageIdConstant.E_SKF_1048, "勤務先のTEL");
		// saveDto.setTelErr(validationErrorCode);
		// return saveDto;
		// }

		// ステータスの設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo = skf2020Sc002SharedService.getSkfApplInfo(saveDto);
		// 申請者用のステータスをデフォルト設定
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

		// コントロールをデフォルト色に戻す
		skf2020Sc002SharedService.setDefultColor(saveDto);

		// 一時保存処理
		saveInfo(applInfo, saveDto);

		// 正常終了
		if (CodeConstant.STATUS_MISAKUSEI.equals(saveDto.getHdnStatus())) {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1012);
		} else {
			ServiceHelper.addResultMessage(saveDto, MessageIdConstant.I_SKF_1011);
		}

		// 情報の再描画
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(saveDto);
		// ラジオボタンのチェック状態とデフォルトの状態を設定
		skf2020Sc002SharedService.setCheckRadio(saveDto);
		skf2020Sc002SharedService.setControlDdl(saveDto);
		skf2020Sc002SharedService.setControlValue(saveDto);

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
			if (skf2020Sc002SharedService.saveNewData(saveDto, applInfo)) {

				// 退居社宅がある場合は備品返却の作成
				if (NfwStringUtils.isNotEmpty(saveDto.getNowShatakuNo())) {
					if (saveDto.getTaikyoYotei() != null
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(saveDto.getHdnBihinHenkyakuUmu())
							&& CodeConstant.LEAVE.equals(saveDto.getTaikyoYotei())) {
						// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
						String bihinHenkaykuShinseiApplNo = skf2020Sc002SharedService.getBihinHenkyaku(saveDto);
						// なければ備品返却申請の書類管理番号を新規発行
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
							Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
							setValue.setLastUpdateDate(saveDto
									.getLastUpdateDate(Skf2020Sc002SharedService.BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE));
							registBihinCount = updateBihinHenkyakuInfo(setValue, saveDto, applInfo,
									bihinHenkaykuShinseiApplNo);
							LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + registBihinCount + "件");
						}
					}

				}

			}

		} else {
			// 新規以外
			saveDto.setHdnStatus(applInfo.get("status"));
			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存時、実行時点の日付時刻を使用
				applInfo.put("dateUpdateFlg", "更新する");

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
				applInfo.put("dateUpdateFlg", "");
				setApplValue.setLastUpdateDate(
						saveDto.getLastUpdateDate(Skf2020Sc002SharedService.APPL_HISTORY_KEY_LAST_UPDATE_DATE));
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, saveDto, applInfo);
			}

			// 入居希望等調書申請テーブルの設定
			int registNyukyoCount = 0;
			Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
			setValue.setLastUpdateDate(
					saveDto.getLastUpdateDate(Skf2020Sc002SharedService.NYUKYO_KEY_LAST_UPDATE_DATE));
			registNyukyoCount = updateNyukyoChoshoTsuchi(setValue, saveDto);
			LogUtils.debugByMsg("入居希望等調書申請テーブル更新件数：" + registNyukyoCount + "件");
			// ステータスを更新
			saveDto.setStatus(applInfo.get("newStatus"));
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
	 * @param setValue
	 * @param dto
	 * @param applInfo
	 * @param bihinHenkaykuShinseiApplNo
	 * @return
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
