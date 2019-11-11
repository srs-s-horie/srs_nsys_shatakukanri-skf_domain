/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc001.Skf3070Sc001GetOwnerContractListExpParameter;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc001.Skf3070Sc001SearchDto;

/**
 * Skf3070Sc001 法定調書データ管理画面の検索処理サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc001SearchService extends BaseServiceAbstract<Skf3070Sc001SearchDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070Sc001SheardService skf3070Sc001SheardService;

	// リストテーブルの１ページ最大表示行数
	@Value("${skf3070.skf3070_sc001.max_row_count}")
	private String listTableMaxRowCount;
	// 最大検索数
	@Value("${skf3070.skf3070_sc001.search_max_count}")
	private String listTableSearchMaxCount;

	/**
	 * サービス処理を行う。
	 * 
	 * @param searchDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc001SearchDto searchDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("検索", CodeConstant.C001, searchDto.getPageId());

		// ドロップダウンの設定
		skf3070Sc001SheardService.getDropDownList(searchDto);

		// 検索条件の対象年を元に対象年開始月と終了月情報を再設定する。
		searchDto.setRecodeDadefrom(searchDto.getTargetYear() + "02");
		searchDto.setRecodeDadeto(DateUtils.addYearsString(searchDto.getTargetYear(), 1,
				SkfCommonConstant.YMD_STYLE_YYYY_FLAT, SkfCommonConstant.YMD_STYLE_YYYY_FLAT) + "01");

		// 検索条件をセット
		Skf3070Sc001GetOwnerContractListExpParameter param = new Skf3070Sc001GetOwnerContractListExpParameter();
		param = skf3070Sc001SheardService.setDefaultSearchParam(searchDto.getOwnerName(), searchDto.getOwnerNameKk(),
				searchDto.getAddress(), searchDto.getBusinessKbn(), searchDto.getShatakuName(),
				searchDto.getShatakuAddress(), searchDto.getRecodeDadefrom(), searchDto.getRecodeDadeto(),
				searchDto.getAcceptFlg());
		// 検索実行
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		listTableData = skf3070Sc001SheardService.getListTableData(param, searchDto);

		if (listTableData == null || listTableData.size() == 0) {
			// 検索結果0件
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
		} else if (listTableData.size() > Integer.parseInt(listTableSearchMaxCount)) {
			// 検索結果表示最大数を超過
			searchDto.setListTableData(null); // 表示されているリスト内容をクリア
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1002, listTableSearchMaxCount,
					"抽出条件を変更してください。");
		}

		searchDto.setListTableData(listTableData);
		searchDto.setListTableMaxRowCount(listTableMaxRowCount);

		return searchDto;
	}

}
