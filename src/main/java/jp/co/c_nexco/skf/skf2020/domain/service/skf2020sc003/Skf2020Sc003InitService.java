/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBatchUtils.SkfBatchUtilsGetMultipleTablesUpdateDateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.common.util.batch.SkfBatchUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003InitDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003InitService extends SkfServiceAbstract<Skf2020Sc003InitDto> {

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfBatchUtils skfBatchUtils;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf2020Sc003InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF2020_SC003);

		initDto.setPageTitleKey(MessageIdConstant.SKF2020_SC003_TITLE);

		// セッション情報引き渡し
		skf2020sc003SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		// セッション情報初期化
		skf2020sc003SharedService.clearMenuScopeSessionBean();
		// 初期情報セット
		boolean res = skf2020sc003SharedService.setDispInfo(initDto);
		if (!res) {
			throwBusinessExceptionIfErrors(initDto.getResultMessages());
		}
		// コメントボタン表示チェック
		boolean commentFlg = checkComment(initDto);
		initDto.setCommentViewFlag(commentFlg);

		// オペレーションガイド取得
		String operationGuide = skfOperationGuideUtils.getOperationGuide(FunctionIdConstant.SKF2020_SC003);
		initDto.setOperationGuide(operationGuide);

		// データ連携用の排他制御用更新日を取得
		// 申請者の社員番号
		String shainNo = initDto.getShainNo();
		Map<String, List<SkfBatchUtilsGetMultipleTablesUpdateDateExp>> dateLinkageMap = skfBatchUtils
				.getUpdateDateForUpdateSQL(shainNo);
		menuScopeSessionBean.put(SessionCacheKeyConstant.DATA_LINKAGE_KEY_SKF2020SC003, dateLinkageMap);

		// バナー戻るボタン遷移先調整
		String pageId = initDto.getPageId();
		String prePageId = initDto.getPrePageId();
		String backUrl = "skf/" + FunctionIdConstant.SKF2010_SC005 + "/init";
		if (!CheckUtils.isEqual(pageId, FunctionIdConstant.SKF2020_SC003)
				|| CheckUtils.isEqual(prePageId, FunctionIdConstant.SKF2010_SC006)) {
			initDto.setPageId(null);
			backUrl = "skf/" + FunctionIdConstant.SKF1010_SC001 + "/init";
		}
		initDto.setBackUrl(backUrl);

		return initDto;
	}

	private boolean checkComment(Skf2020Sc003InitDto initDto) {
		// コメント取得
		List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001, initDto.getApplNo(), null);
		if (commentList != null && commentList.size() > 0) {
			return true;
		}

		return false;
	}

}
