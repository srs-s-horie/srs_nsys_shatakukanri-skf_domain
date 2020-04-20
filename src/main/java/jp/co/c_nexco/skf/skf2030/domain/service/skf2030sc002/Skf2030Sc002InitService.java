/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2030.domain.service.skf2030sc002;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2030.domain.dto.skf2030sc002.Skf2030Sc002InitDto;

/**
 * Skf2030Sc002 備品希望申請（アウトソース用)初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2030Sc002InitService extends SkfServiceAbstract<Skf2030Sc002InitDto> {

	@Autowired
	private Skf2030Sc002SharedService skf2030Sc002SharedService;

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2030Sc002InitDto initDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2030_SC002);
		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2030_SC002_TITLE);

		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		// 申請情報設定
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("status", initDto.getApplStatus());
		applInfo.put("applNo", initDto.getApplNo());
		applInfo.put("applId", initDto.getApplId());

		// 社宅連携バッチ更新排他処理用
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> forUpdateMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(initDto.getShainNo());
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2030SC002, forUpdateMap);

		// 初期表示
		boolean result = skf2030Sc002SharedService.setDisplayData(initDto, loginUserInfo, applInfo);
		if (!result) {
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
			initDto.setMaskPattern("ERR");
			initDto.setCommentDisabled("true");
		}

		skf2030Sc002SharedService.setEnabled(initDto, applInfo);

		// 操作ガイド取得
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2030_SC002));

		return initDto;
	}

}
