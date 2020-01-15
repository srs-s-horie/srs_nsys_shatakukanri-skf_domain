/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.datalinkage.Skf2020Fc001NyukyoKiboSinseiDataImport;
import jp.co.c_nexco.skf.common.util.datalinkage.SkfBatchBusinessLogicUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003UpdateDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003UpdateService extends BaseServiceAbstract<Skf2020Sc003UpdateDto> {

	// 会社コード
	private String companyCd = CodeConstant.C001;

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfBatchBusinessLogicUtils skfBatchBusinessLogicUtils;
	@Autowired
	private SkfRollBackExpRepository skfRollBackExpRepository;
	
	@Autowired
	private Skf2020Fc001NyukyoKiboSinseiDataImport skf2020Fc001NyukyoKiboSinseiDataImport;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	
	@Autowired
	private Skf2020Sc003GetApplHistoryInfoForUpdateExpRepository skf2020Sc003GetApplHistoryInfoForUpdateExpRepository;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param updDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public BaseDto index(Skf2020Sc003UpdateDto updDto) throws Exception {
		
		updDto.setPageTitleKey(MessageIdConstant.SKF2020_SC003_TITLE);

        // 提示状況汎用コード取得
        Map<String, String> hitsuyouShatakuGenCodeMap = new HashMap<String, String>();
        hitsuyouShatakuGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_SHATAKU_STATUS_KBN);
        String defaultHitsuyouShataku = CodeConstant.NONE;
        if(updDto.getDefaultHitsuyoShataku() != null){
        	defaultHitsuyouShataku = hitsuyouShatakuGenCodeMap.get(updDto.getDefaultHitsuyoShataku());
        }
		
		// 必要とする社宅理由更新メソッド
		boolean updResult = updateHitsuyoShataku(updDto);
		if (!updResult) {
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
		}

		// 社宅管理データ連携処理実行
		String applNo = updDto.getApplNo();
		String applId = updDto.getApplId();
		String shainNo = updDto.getShainNo();
		Skf2020Sc003GetApplHistoryInfoForUpdateExp applInfo = new Skf2020Sc003GetApplHistoryInfoForUpdateExp();
		Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter param = new Skf2020Sc003GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		applInfo = skf2020Sc003GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		if(applInfo == null){
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
		}
		String afterApplStatus = applInfo.getApplStatus();
		List<String> resultBatch = new ArrayList<String>();
		resultBatch = skf2020sc003SharedService.doShatakuRenkei(menuScopeSessionBean, shainNo, applNo, afterApplStatus, applId, FunctionIdConstant.SKF2020_SC003);
		menuScopeSessionBean.remove(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2020SC003);
		if(resultBatch != null){
			skfBatchBusinessLogicUtils.addResultMessageForDataLinkage(updDto, resultBatch);
			skfRollBackExpRepository.rollBack();
		}
		
		// 操作ログを出力する
		String afterHitsuyouShataku = CodeConstant.NONE;
        if(updDto.getHitsuyoShataku() != null){
        	afterHitsuyouShataku = hitsuyouShatakuGenCodeMap.get(updDto.getHitsuyoShataku());
        }
		StringJoiner halfSpace = new StringJoiner(" ");
		halfSpace.add("確定");
		halfSpace.add(shainNo);
		halfSpace.add(defaultHitsuyouShataku+"から"+afterHitsuyouShataku+"に変更");
		skfOperationLogUtils.setAccessLog(halfSpace.toString(), CodeConstant.C001, updDto.getPageId());

		// 初期表示
		skf2020sc003SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		skf2020sc003SharedService.setDispInfo(updDto);

		// 提示データが存在するかチェックする。
		if (NfwStringUtils.isNotEmpty(updDto.getNewShatakuKanriNo())) {
			// 提示データが存在する場合、更新完了メッセージ表示
			ServiceHelper.addResultMessage(updDto, MessageIdConstant.I_SKF_1011);
		} else {
			// 提示データが存在しない場合、社宅の使用量変更を促すメッセージ表示
			ServiceHelper.addResultMessage(updDto, MessageIdConstant.I_SKF_2057);
		}

		return updDto;
	}

	/**
	 * 必要とする社宅理由更新
	 * 
	 * @param updDto
	 */
	private boolean updateHitsuyoShataku(Skf2020Sc003UpdateDto updDto) {
		boolean result = true;
		// 更新対象のデータを取得
		Skf2020TNyukyoChoshoTsuchi updData = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(updDto.getApplNo());

		updData = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		if (updData == null) {
			return false;
		}
		Date lastUpdateDate = updDto.getLastUpdateDate(skf2020sc003SharedService.SHATAKU_CHOSHO_TSUCHI_UPDATE_DATE);
		// 排他チェック
		checkLockException(lastUpdateDate, updData.getUpdateDate());

		String hitsuyoShataku = updDto.getHitsuyoShataku();
		updData.setHitsuyoShataku(hitsuyoShataku);
		int updResult = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKeySelective(updData);
		if (updResult <= 0) {
			result = false;
		}

		return result;
	}
	
	/**
	 * 社宅連携処理を実施する
	 * 
	 * @param menuScopeSessionBean
	 * @param applNo
	 * @param applStatus
	 * @param applId
	 * @param pageId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> doShatakuRenkei(MenuScopeSessionBean menuScopeSessionBean, String shainNo, String applNo,
			String applStatus, String applId, String pageId) {
		// ログインユーザー情報取得
		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String userId = loginUserInfoMap.get("userCd");
		// 排他チェック用データ取得
		Map<String, Object> forUpdateObject = (Map<String, Object>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2020SC003);

		List<String> resultBatch = new ArrayList<String>();
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMapR0100 = skf2020Fc001NyukyoKiboSinseiDataImport
				.forUpdateMapDownCaster(forUpdateObject);
		skf2020Fc001NyukyoKiboSinseiDataImport.setUpdateDateForUpdateSQL(forUpdateMapR0100);

		// 連携処理開始
		resultBatch = skf2020Fc001NyukyoKiboSinseiDataImport.doProc(companyCd, shainNo, applNo, CodeConstant.NONE,
				applStatus, userId, pageId);

		return resultBatch;
	}

}
