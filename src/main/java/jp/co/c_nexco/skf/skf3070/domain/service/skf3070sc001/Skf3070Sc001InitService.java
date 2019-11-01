/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001InitDto;

/**
 * Skf3070Sc001 法定調書データ管理画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001InitService extends BaseServiceAbstract<Skf3070Sc001InitDto> {

	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070Sc001SheardService skf3070Sc001SheardService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3070_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// 初期表示リスト検索条件を取得
		setInitInfo(initDto);

		// 初期表示リスト設定
		Skf3070Sc001GetOwnerContractListExpParameter param = new Skf3070Sc001GetOwnerContractListExpParameter();
		// 初期検索条件をセッションに格納
		param = setDefaultSearchParam(initDto.getOwnerName(), initDto.getOwnerNameKk(), initDto.getAddress(),
				initDto.getBusinessKbn(), initDto.getShatakuName(), initDto.getShatakuAddress(),
				initDto.getTargetYear(), initDto.getAcceptStatus());
		// 検索結果をリストに格納
		initDto.setListTableData(skf3070Sc001SheardService.getListTableData(param, initDto));

		// ドロップダウンの設定
		skf3070Sc001SheardService.getDoropDownList();

		return initDto;
	}

	/**
	 * 初期表示時の画面情報をDtoに設定する
	 * 
	 * @param initDto
	 * @throws ParseException
	 */
	private void setInitInfo(Skf3070Sc001InitDto initDto) throws ParseException {

		// セッションに保持された検索条件が無いかを検索
		Skf3070Sc001GetOwnerContractListExpParameter sessionParam = (Skf3070Sc001GetOwnerContractListExpParameter) sessionBean
				.get(SessionCacheKeyConstant.SKF3070SC001_SEARCH_COND_SESSION_KEY);

		if (sessionParam != null) {
			// セッションから検索条件パラメータが取得できた場合、パラメータから画面に表示する情報を復元
			initDto.setOwnerName(sessionParam.getOwnerName());
			initDto.setOwnerNameKk(sessionParam.getOwnerNameKk());
			initDto.setAddress(sessionParam.getAddress());
			initDto.setBusinessKbn(sessionParam.getBusinessKbn());
			initDto.setShatakuName(sessionParam.getShatakuName());
			initDto.setShatakuAddress(sessionParam.getShatakuAddress());
			initDto.setTargetYear(sessionParam.getTargetYear());
			initDto.setAcceptStatus(sessionParam.getAcceptStatus());

		} else {
			// セッションから検索条件パラメータが取得できなかった場合、デフォルト検索条件を設定
			// 対象年度の取得
			String targetYear = getTargetYear();
			initDto.setTargetYear(targetYear);
		}
	}

	/**
	 * 対象年の取得
	 * 
	 * @return resultYear 対象年
	 * @throws ParseException
	 */
	private String getTargetYear() throws ParseException {

		// 比較用「月」の取得
		String compareMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_MM_FLAT);

		// 戻り値の年度
		String resultYear = CodeConstant.NONE;
		if (compareMonth.compareTo("01") == 0) {
			// 比較用「月」が01である場合は、システム年-1の値を返す。
			resultYear = DateUtils.addYearsString(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYY_FLAT),
					-1);
		} else {
			resultYear = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYY_FLAT);
		}

		return resultYear;
	}

	/**
	 * 初期表示時 検索条件設定
	 * 
	 * @param ownerName
	 * @param ownerNameKk
	 * @param address
	 * @param businessKbn
	 * @param shatakuName
	 * @param shatakuAddress
	 * @param targetYear
	 * @param acceptStatus
	 * @return
	 */
	private Skf3070Sc001GetOwnerContractListExpParameter setDefaultSearchParam(String ownerName, String ownerNameKk,
			String address, String businessKbn, String shatakuName, String shatakuAddress, String targetYear,
			String acceptStatus) {

		Skf3070Sc001GetOwnerContractListExpParameter parame = new Skf3070Sc001GetOwnerContractListExpParameter();

		parame.setOwnerName(ownerName);
		parame.setOwnerNameKk(ownerNameKk);
		parame.setAddress(address);
		parame.setBusinessKbn(businessKbn);
		parame.setShatakuName(shatakuName);
		parame.setShatakuAddress(shatakuAddress);
		parame.setTargetYear(targetYear);
		parame.setAcceptStatus(acceptStatus);

		// 初期検索条件をセッションに格納
		sessionBean.put(SessionCacheKeyConstant.SKF3070SC001_SEARCH_COND_SESSION_KEY, parame);

		return parame;
	}

}
