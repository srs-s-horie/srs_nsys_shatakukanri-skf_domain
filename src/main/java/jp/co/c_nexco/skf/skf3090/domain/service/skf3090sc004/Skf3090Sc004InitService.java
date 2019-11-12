/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc004.Skf3090Sc004InitDto;

/**
 * Skf3090Sc004InitService 従業員マスタ一覧初期表示処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc004InitService extends BaseServiceAbstract<Skf3090Sc004InitDto> {

	@Autowired
	private Skf3090Sc004SharedService skf3090Sc004SharedService;
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// 会社コード
	private String companyCd = CodeConstant.C001;

	// リストテーブルの１ページ最大表示行数
	@Value("${skf3090.skf3090_sc004.max_row_count}")
	private String listTableMaxRowCount;

	/**
	 * 最初に呼び出されるメソッド
	 * 
	 */
	@Override
	public BaseDto index(Skf3090Sc004InitDto initDto) throws Exception {

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, initDto.getPageId());

		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
				&& initDto.getPrePageId().equals(FunctionIdConstant.SKF3090_SC005)) {
			/** 従業員マスタ登録画面からの遷移が、元々リストテーブルからの遷移での復帰だった場合、リストテーブルの情報を取得する */
			// 登録画面のhidden項目をinitDtoに詰めなおす
			initDto.setShainNo(initDto.getHdnShainNo());
			initDto.setName(initDto.getHdnName());
			initDto.setNameKk(initDto.getHdnNameKk());
			initDto.setOriginalCompanyCd(initDto.getHdnOriginalCompanyCd());
			initDto.setAgencyCd(initDto.getHdnAgencyCd());
			initDto.setAffiliation1Cd(initDto.getHdnAffiliation1Cd());
			initDto.setAffiliation2Cd(initDto.getHdnAffiliation2Cd());

			// リストテーブルの情報を取得
			int listCount = skf3090Sc004SharedService.getListTableData(initDto.getShainNo(), initDto.getName(),
					initDto.getNameKk(), initDto.getOriginalCompanyCd(), initDto.getAgencyCd(),
					initDto.getAffiliation1Cd(), initDto.getAffiliation2Cd(), listTableData);

			// エラーメッセージ設定
			if (listCount == 0) {
				// 取得レコード0件のワーニング
				ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1007, String.valueOf(listCount));
			}

		} else {
			// 画面連携のhidden項目を初期化
			initDto.setHdnShainNo(null);
			initDto.setHdnName(null);
			initDto.setHdnNameKk(null);
			initDto.setHdnOriginalCompanyCd(null);
			initDto.setHdnAgencyCd(null);
			initDto.setHdnAffiliation1Cd(null);
			initDto.setHdnAffiliation2Cd(null);

			/*
			 * // テキスト系の文言を初期化する initDto.setShainNo(null);
			 * initDto.setName(null); initDto.setNameKk(null);
			 */

		}

		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3090Sc004SharedService.getDropDownList(initDto.getOriginalCompanyCd(), companyList, initDto.getAgencyCd(),
				agencyList, initDto.getAffiliation1Cd(), affiliation1List, initDto.getAffiliation2Cd(),
				affiliation2List);

		// 戻り値をセット
		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC004_TITLE);
		initDto.setListTableMaxRowCount(listTableMaxRowCount);

		initDto.setCompanyList(companyList);
		initDto.setAgencyList(agencyList);
		initDto.setAffiliation1List(affiliation1List);
		initDto.setAffiliation2List(affiliation2List);
		initDto.setListTableData(listTableData);

		return initDto;

	}

}
