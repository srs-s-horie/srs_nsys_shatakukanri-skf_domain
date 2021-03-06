/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc003;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc003.Skf2010Sc003GetApplHistoryStatusInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc003.Skf2010Sc003InitDto;

/**
 * Skf2010Sc003 申請状況一覧初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc003InitService extends BaseServiceAbstract<Skf2010Sc003InitDto> {

	@Autowired
	private Skf2010Sc003SharedService skf2010Sc003SharedService;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	// 基準会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2010Sc003InitDto index(Skf2010Sc003InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", companyCd, initDto.getPageId());

		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC003_TITLE);

		// 申請日にシステム日付-1月、システム日付を設定
		setApplDateDefault(initDto);

		// オペレーションガイド取得
		String operationGuide = skfOperationGuideUtils.getOperationGuide(initDto.getPageId());
		initDto.setOperationGuide(operationGuide);

		// チェックボックスの選択
		List<String> applStatusList = getDefaultApplStatusValue();
		initDto.setApplStatus(applStatusList.toArray(new String[applStatusList.size()]));

		// 申請状況一覧を検索
		setStatusList(initDto);

		return initDto;
	}

	private void setStatusList(Skf2010Sc003InitDto initDto) {
		// ログインユーザー情報から社員番号取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String shainNo = loginUserInfo.get("shainNo").toString();

		String applDateFrom = initDto.getApplDateFrom();
		String applDateTo = initDto.getApplDateTo();
		String agreDateFrom = initDto.getAgreDateFrom();
		String agreDateTo = initDto.getAgreDateTo();

		String applName = initDto.getApplName();

		List<String> applStatus = getDefaultApplStatusValue();

		List<Skf2010Sc003GetApplHistoryStatusInfoExp> resultList = new ArrayList<Skf2010Sc003GetApplHistoryStatusInfoExp>();
		resultList = skf2010Sc003SharedService.getApplHistoryStatusInfo(shainNo, applDateFrom, applDateTo, agreDateFrom,
				agreDateTo, applName, applStatus);

		initDto.setLtResultList(skf2010Sc003SharedService.createListTable(resultList));
	}

	private void setApplDateDefault(Skf2010Sc003InitDto initDto) {
		Date applDateTo = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) applDateTo.clone());

		cal.add(Calendar.MONTH, -1);

		Date applDateFrom = cal.getTime();

		initDto.setApplDateFrom(skfDateFormatUtils.dateFormatFromDate(applDateFrom, "yyyy/MM/dd"));
		initDto.setApplDateTo(skfDateFormatUtils.dateFormatFromDate(applDateTo, "yyyy/MM/dd"));
	}

	/**
	 * チェックボックスの初期値を設定します
	 * 
	 * @return
	 */
	private List<String> getDefaultApplStatusValue() {
		List<String> rtnApplStatus = new ArrayList<String>();
		rtnApplStatus.add(CodeConstant.STATUS_ICHIJIHOZON); // 00：一時保存
		rtnApplStatus.add(CodeConstant.STATUS_SHINSEICHU); // 01：申請中
		rtnApplStatus.add(CodeConstant.STATUS_SHINSACHU); // 10：審査中
		rtnApplStatus.add(CodeConstant.STATUS_KAKUNIN_IRAI); // 30：確認依頼
		rtnApplStatus.add(CodeConstant.STATUS_DOI_SHINAI); // 21：同意しない
		rtnApplStatus.add(CodeConstant.STATUS_DOI_ZUMI); // 22：同意済み
		rtnApplStatus.add(CodeConstant.STATUS_SHONIN_ZUMI); // 40：承認
		rtnApplStatus.add(CodeConstant.STATUS_SASHIMODOSHI); // 50：修正依頼（差戻し）
		rtnApplStatus.add(CodeConstant.STATUS_HININ); // 99：差戻し（否認）

		rtnApplStatus.add(CodeConstant.STATUS_HANNYU_MACHI); // 23：搬入待ち
		rtnApplStatus.add(CodeConstant.STATUS_HANNYU_ZUMI); // 24：搬入済み
		rtnApplStatus.add(CodeConstant.STATUS_HANSYUTSU_MACHI); // 25：搬出待ち
		rtnApplStatus.add(CodeConstant.STATUS_HANSYUTSU_ZUMI); // 26：搬出済み
		rtnApplStatus.add(CodeConstant.STATUS_SENTAKU_SHINAI); // 27：選択しない
		rtnApplStatus.add(CodeConstant.STATUS_SENTAKU_ZUMI); // 28：選択済
		rtnApplStatus.add(CodeConstant.STATUS_KANRYOU); // 41：完了

		return rtnApplStatus;
	}
}
