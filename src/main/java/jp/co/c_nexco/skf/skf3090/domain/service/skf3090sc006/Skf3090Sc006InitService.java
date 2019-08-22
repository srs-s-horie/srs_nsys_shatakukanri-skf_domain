/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc006.Skf3090Sc006InitDto;

/**
 * 組織マスタ一覧画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3090Sc006InitService extends BaseServiceAbstract<Skf3090Sc006InitDto> {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;

	@Autowired
	private Skf3090Sc006SharedService skf3090Sc006SharedService;

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Value("${skf3090.skf3090_sc006.max_row_count}")
	private String listTableMaxRowCount;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3090Sc006InitDto initDto) throws Exception {

		// デリゲート処理の設定

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", CodeConstant.C001, initDto.getPageId());

		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC006_TITLE);

		// 初期表示設定
		setInitDisp(initDto);

		initDto.setListTableMaxRowCount(listTableMaxRowCount);

		return initDto;
	}

	private void setInitDisp(Skf3090Sc006InitDto initDto) {
		// コントロール名の設定

		// パンくず

		// マスタの設定

		// 「会社」ドロップダウンリストの設定
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList = skfDropDownUtils.getDdlCompanyByCd(initDto.getCompanyCd(), true);
		initDto.setCompanyList(companyList);

		// 組織登録画面から遷移時、ドロップダウンリストの設定
		setListValue(initDto);

		// 確認ダイアログメッセージ設定
	}

	private void setListValue(Skf3090Sc006InitDto initDto) {

		// TODO 次画面からの戻り制御

		// // リストテーブルデータ取得用
		// List<Map<String, Object>> createTableList = new ArrayList<Map<String,
		// Object>>();
		// if (NfwStringUtils.isNotEmpty(initDto.getPrePageId())
		// && initDto.getPrePageId().equals(FunctionIdConstant.SKF3090_SC007)) {
		// /** 組織マスタ登録画面からの遷移が、元々リストテーブルからの遷移での復帰だった場合、リストテーブルの情報を取得する */
		// // 登録画面のhidden項目をinitDtoに詰めなおす
		// initDto.setHdnCompanyCd(initDto.getHdnCompanyCd());
		// initDto.setHdnAgencyCd(initDto.getHdnAgencyCd());
		// initDto.setHdnAffiliation1Cd(initDto.getHdnAffiliation1Cd());
		// initDto.setHdnAffiliation2Cd(initDto.getAffiliation2Cd());
		// initDto.setHdnBusinessAreaCd(initDto.getHdnBusinessAreaCd());
		// }

		String companyCd = initDto.getCompanyCd();
		String agencyCd = initDto.getAgencyCd();
		String affiliation1Cd = initDto.getAffiliation1Cd();
		String affiliation2Cd = initDto.getAffiliation2Cd();
		String businessAreaCd = initDto.getBusinessAreaCd();

		// 「機関」ドロップダウンリストの設定
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);
		initDto.setAgencyList(agencyList);

		// 「部等」ドロップダウンの設定
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
		initDto.setAffiliation1List(affiliation1List);

		// 「室、チーム又は課」ドロップダウンの設定
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd, affiliation2Cd,
				true);
		initDto.setAffiliation2List(affiliation2List);

		// 「事業領域」ドロップダウンの設定
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList = skfDropDownUtils.getDdlBusinessAreaByCd(companyCd, agencyCd, businessAreaCd, true);
		initDto.setBusinessAreaList(businessAreaList);

		// 画面データの設定
		List<Skf3090Sc006GetSoshikiInfoExp> soshikiInfoList = skf3090Sc006SharedService.getSoshikiInfo(companyCd,
				agencyCd, affiliation1Cd, affiliation2Cd, businessAreaCd);

		// リストテーブルに組織情報を表示
		List<Map<String, Object>> createTableList = new ArrayList<Map<String, Object>>();
		createTableList = skf3090Sc006SharedService.createListTableData(soshikiInfoList);
		initDto.setCreateTableList(createTableList);
	}

}
