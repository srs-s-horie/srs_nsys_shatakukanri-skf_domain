/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001InitDto;

/**
 * Skf3070Sc001 法定調書データ管理画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001InitService extends SkfServiceAbstract<Skf3070Sc001InitDto> {

	@Autowired
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070Sc001SharedService skf3070Sc001SheardService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	// リストテーブルの１ページ最大表示行数
	@Value("${skf3070.skf3070_sc001.max_row_count}")
	private String listTableMaxRowCount;
	// 最大検索数
	@Value("${skf3070.skf3070_sc001.search_max_count}")
	private String listTableSearchMaxCount;

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

		// 対象年の基準年取得
		String standardYear = getStandardYear();
		initDto.setStandardYear(standardYear);

		// 初期表示リスト検索条件を取得
		setInitInfo(initDto);

		// 初期表示リスト設定
		Skf3070Sc001GetOwnerContractListExpParameter param = new Skf3070Sc001GetOwnerContractListExpParameter();
		// 初期検索条件をセッションに格納
		param = skf3070Sc001SheardService.setDefaultSearchParam(initDto.getOwnerName(), initDto.getOwnerNameKk(),
				initDto.getAddress(), initDto.getBusinessKbn(), initDto.getShatakuName(), initDto.getShatakuAddress(),
				initDto.getRecodeDadefrom(), initDto.getRecodeDadeto(), initDto.getAcceptFlg());
		// 検索結果をリストに格納
		initDto.setListTableData(skf3070Sc001SheardService.getListTableData(param, initDto));
		initDto.setListTableMaxRowCount(listTableMaxRowCount);

		// ドロップダウンの設定
		skf3070Sc001SheardService.getDropDownList(initDto);

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
			initDto.setRecodeDadefrom(sessionParam.getRecodeDadefrom());
			initDto.setRecodeDadeto(sessionParam.getRecodeDadeto());
			initDto.setAcceptFlg(sessionParam.getAcceptFlg());

		} else {
			// セッションから検索条件パラメータが取得できなかった場合、デフォルト検索条件を設定
			// 検索条件の対象年を元に対象年開始月と終了月情報を作成する。
			initDto.setRecodeDadefrom(initDto.getStandardYear() + "02");
			initDto.setRecodeDadeto(DateUtils.addYearsString(initDto.getStandardYear(), 1,
					SkfCommonConstant.YMD_STYLE_YYYY_FLAT, SkfCommonConstant.YMD_STYLE_YYYY_FLAT) + "01");
		}
	}

	/**
	 * 基準年の取得
	 * 
	 * @return resultYear 対象年
	 * @throws ParseException
	 */
	private String getStandardYear() throws ParseException {

		// 比較用「月」の取得
		String compareMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_MM_FLAT);

		// 戻り値の年度
		String resultYear = CodeConstant.NONE;
		if (compareMonth.compareTo("01") == 0) {
			// 比較用「月」が01である場合は、システム年-1の値を返す。
			String resultYYYYMMDD = DateUtils.addYearsString(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT),
					-1);
			resultYear = skfDateFormatUtils.dateFormatFromString(resultYYYYMMDD, SkfCommonConstant.YMD_STYLE_YYYY_FLAT);
		} else {
			resultYear = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYY_FLAT);
		}
		return resultYear;
	}

}
