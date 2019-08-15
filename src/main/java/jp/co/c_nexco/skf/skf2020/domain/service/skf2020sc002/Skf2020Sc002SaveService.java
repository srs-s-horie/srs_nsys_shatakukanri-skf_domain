/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
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

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
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
		applInfo.put("newStatus", newStatus);

		// 画面表示項目の保持
		skf2020Sc002SharedService.setInfo(saveDto);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(saveDto);
		// 画面表示制御再設定
		skf2020Sc002SharedService.setControlValue(saveDto);

		// バイトカット処理
		skf2020Sc002SharedService.cutByte(saveDto);

		// 一時保存処理
		if (!saveInfo(applInfo, saveDto)) {
			return saveDto;
		}

		// 画面表示項目の保持
		skf2020Sc002SharedService.setInfo(saveDto);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(saveDto);
		// 画面表示制御再設定
		skf2020Sc002SharedService.setControlValue(saveDto);

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
	private boolean saveInfo(Map<String, String> applInfo, Skf2020Sc002SaveDto saveDto) {

		boolean ret = true;

		// 社員番号を設定
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		applInfo.put("shainNo", loginUserInfoMap.get("shainNo"));
		// 添付ファイルの有無
		Map<String, String> applTacInfoMap = skfShinseiUtils.getApplAttachFlg(applInfo.get("shainNo"),
				applInfo.get("applNo"));
		applInfo.put("applTacFlg", applTacInfoMap.get("applTacFlg"));

		if (CodeConstant.STATUS_MISAKUSEI.equals(applInfo.get("status"))) {
			// 指定なし（新規）の場合
			saveDto.setHdnStatus(CodeConstant.STATUS_MISAKUSEI);
			// 更新フラグを「0」に設定する
			applInfo.put("updateFlg", Skf2020Sc002SharedService.NO_UPDATE_FLG);
			// 新規登録処理
			if (skf2020Sc002SharedService.saveNewData(saveDto, applInfo)) {
				// 退居社宅がある場合は備品返却の作成
				if (NfwStringUtils.isNotEmpty(saveDto.getNowShatakuNo())) {
					if (saveDto.getTaikyoYotei() != null && CodeConstant.LEAVE.equals(saveDto.getTaikyoYotei())
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(saveDto.getHdnBihinHenkyakuUmu())) {
						// 備品返却申請テーブル登録処理
						skf2020Sc002SharedService.registrationBihinShinsei(saveDto, applInfo);
					}
				}
			} else {
				ret = false;
			}
		} else {
			// 新規以外
			saveDto.setHdnStatus(applInfo.get("status"));
			applInfo.put("updateFlg", Skf2020Sc002SharedService.UPDATE_FLG);

			// 排他制御の比較用更新日を設定
			Skf2020TNyukyoChoshoTsuchi key = new Skf2020TNyukyoChoshoTsuchi();
			// 条件項目をセット
			key.setCompanyCd(CodeConstant.C001);
			key.setApplNo(saveDto.getApplNo());
			Skf2020TNyukyoChoshoTsuchi reUpdateDate = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
			saveDto.addLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE, reUpdateDate.getUpdateDate());

			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存の場合
				applInfo.put("dateUpdateFlg", "1");
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, saveDto, applInfo);
				LogUtils.debugByMsg("申請情報履歴テーブル更新件数：" + registHistoryCount + "件");
				break;
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SASHIMODOSHI:
				break;
			default:
				// 申請日時を更新しない
				applInfo.put("dateUpdateFlg", "0");
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
				if (saveDto.getTaikyoYotei() != null && CodeConstant.LEAVE.equals(saveDto.getTaikyoYotei())
						&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(saveDto.getHdnBihinHenkyakuUmu())) {
					// 備品返却申請テーブル登録処理
					skf2020Sc002SharedService.registrationBihinShinsei(saveDto, applInfo);
				}
			}
		}
		return ret;
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
		setValue.setLastUpdateDate(dto.getLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE));
		// 排他チェック
		super.checkLockException(dto.getLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE),
				resultUpdateDate.getUpdateDate());

		// 更新値の設定
		setValue = skf2020Sc002SharedService.setUpdateApplHistoryAgreeStatusIchiji(setValue, dto, applInfo);
		// 更新
		resultCnt = skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setValue);
		return resultCnt;
	}

}
