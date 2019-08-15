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
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
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
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Override
	public BaseDto index(Skf2020Sc002ConfirmDto dto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請内容を確認する", CodeConstant.C001, dto.getPageId());

		// バイトカット処理
		skf2020Sc002SharedService.cutByte(dto);

		// 登録処理
		// ステータスの設定
		Map<String, String> applInfo = skf2020Sc002SharedService.getSkfApplInfo(dto);
		// 申請者用のステータスをデフォルト設定
		String newStatus = CodeConstant.STATUS_ICHIJIHOZON;
		applInfo.put("newStatus", newStatus);

		// 画面表示項目の保持
		skf2020Sc002SharedService.setInfo(dto);
		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(dto);
		// 画面表示制御再設定
		skf2020Sc002SharedService.setControlValue(dto);

		// 一時保存処理を実行
		if (!saveInfo(applInfo, dto)) {
			return dto;
		}

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

		return dto;
	}

	/**
	 * 一時保存処理
	 * 
	 * @param applInfo
	 * @param dto
	 */
	private boolean saveInfo(Map<String, String> applInfo, Skf2020Sc002ConfirmDto dto) {

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
			dto.setHdnStatus(CodeConstant.STATUS_MISAKUSEI);
			// 更新フラグを「0」に設定する
			applInfo.put("updateFlg", Skf2020Sc002SharedService.NO_UPDATE_FLG);
			// 新規登録処理
			if (skf2020Sc002SharedService.saveNewData(dto, applInfo)) {
				// 退居社宅がある場合は備品返却の作成
				if (NfwStringUtils.isNotEmpty(dto.getNowShatakuNo())) {
					if (dto.getTaikyoYotei() != null && CodeConstant.LEAVE.equals(dto.getTaikyoYotei())
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(dto.getHdnBihinHenkyakuUmu())) {
						// 備品返却申請テーブル登録処理
						skf2020Sc002SharedService.registrationBihinShinsei(dto, applInfo);
					}
				}
			} else {
				ret = false;
			}
		} else {
			// 新規以外
			dto.setHdnStatus(applInfo.get("status"));
			applInfo.put("updateFlg", Skf2020Sc002SharedService.UPDATE_FLG);

			// 排他制御の比較用更新日を設定
			Skf2020TNyukyoChoshoTsuchi key = new Skf2020TNyukyoChoshoTsuchi();
			// 条件項目をセット
			key.setCompanyCd(CodeConstant.C001);
			key.setApplNo(dto.getApplNo());
			Skf2020TNyukyoChoshoTsuchi reUpdateDate = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
			dto.addLastUpdateDate(Skf2020Sc002SharedService.KEY_LAST_UPDATE_DATE, reUpdateDate.getUpdateDate());

			// 申請履歴テーブルの更新
			int registHistoryCount = 0;
			Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存の場合
				applInfo.put("dateUpdateFlg", "1");
				registHistoryCount = updateApplHistoryAgreeStatusIchiji(setApplValue, dto, applInfo);
				LogUtils.debugByMsg("申請情報履歴テーブル更新件数：" + registHistoryCount + "件");
				break;
			case CodeConstant.STATUS_HININ:
			case CodeConstant.STATUS_SASHIMODOSHI:
				break;
			default:
				// 申請日時を更新しない
				applInfo.put("dateUpdateFlg", "0");
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
				if (dto.getTaikyoYotei() != null && CodeConstant.LEAVE.equals(dto.getTaikyoYotei())
						&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(dto.getHdnBihinHenkyakuUmu())) {
					// 備品返却申請テーブル登録処理
					skf2020Sc002SharedService.registrationBihinShinsei(dto, applInfo);
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
		setValue.setUpdateDate(resultUpdateDate.getUpdateDate());
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
