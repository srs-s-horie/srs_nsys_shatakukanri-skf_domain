/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetShainInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002InitDto;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.user_context.model.UserContext;
import jp.co.intra_mart.foundation.user_context.model.UserProfile;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用)のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */

@Service
public class Skf2020Sc002InitService extends BaseServiceAbstract<Skf2020Sc002InitDto> {

	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;
	@Autowired
	private Skf2020Sc002GetShainInfoExpRepository skf2020Sc002GetShainInfoExpRepository;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2020Sc002InitDto index(Skf2020Sc002InitDto initDto) throws Exception {

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2020_SC002_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// フォントカラーをデフォルトに設定
		skf2020Sc002SharedService.setDefultColor(initDto);
		// 情報の初期化
		// skf2020Sc002SharedService.setClearInfo(initDto);

		// 必要情報の取得
		setInfo(initDto);

		// 画面初期表示
		skf2020Sc002SharedService.initializeDisp(initDto);

		// 返却備品の設定
		skf2020Sc002SharedService.setReturnBihinInfo(initDto);

		// デフォルトの選択状態を設定
		skf2020Sc002SharedService.setControlValue(initDto);

		// 操作ガイドの設定
		LogUtils.debugByMsg("操作ガイド" + initDto.getPageId());
		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));

		// コメント設定の有無)
		skf2020Sc002SharedService.setCommentBtnDisabled(initDto);

		return initDto;

	}

	/**
	 * ユーザ情報の取得
	 * 
	 * @param initDto
	 */
	private void setInfo(Skf2020Sc002InitDto initDto) {
		// ユーザコンテキストを取得
		UserContext userContext = Contexts.get(UserContext.class);
		UserProfile profile = userContext.getUserProfile();
		// ユーザIDの取得
		String userId = profile.getUserCd();
		if (NfwStringUtils.isNotEmpty(userId)) {
			initDto.setUserId(userId);
		}

		// 社員マスタから社員情報取得
		List<Skf2020Sc002GetShainInfoExp> shainList = new ArrayList<Skf2020Sc002GetShainInfoExp>();
		shainList = getShainInfo(CodeConstant.C001, userId, shainList);
		if (shainList.size() > 0) {
			// リストに値を格納
			initDto.setShainList(shainList);
			// 社員番号の設定
			String shainNo = shainList.get(0).getShainNo();
			if (NfwStringUtils.isNotEmpty(shainNo)) {
				initDto.setShainNo(shainNo);
			}
		}

		// 現在日付の設定
		initDto.setYearMonthDay(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));

		// 可否区分の設定
		initDto.setHdnConfirmFlg(CodeConstant.YES);
		LogUtils.debugByMsg("可否区分の設定" + initDto.getHdnConfirmFlg());
		// 申請区分の設定
		initDto.setApplKbn(CodeConstant.SINSE_INPUT);
		LogUtils.debugByMsg("申請区分の設定" + initDto.getApplKbn());
	}

	/**
	 * 社員情報の設定(取得)
	 * 
	 * @param companyCd
	 * @param userId
	 * @param shainList
	 * @return 取得結果
	 */
	private List<Skf2020Sc002GetShainInfoExp> getShainInfo(String companyCd, String userId,
			List<Skf2020Sc002GetShainInfoExp> shainList) {
		// DB検索処理
		Skf2020Sc002GetShainInfoExpParameter param = new Skf2020Sc002GetShainInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setUserId(userId);
		shainList = skf2020Sc002GetShainInfoExpRepository.getShainInfo(param);
		return shainList;
	}

}
