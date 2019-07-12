/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc006.Skf3090Sc006GetSoshikiInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc006.Skf3090Sc006InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3090Sc006InitService extends BaseServiceAbstract<Skf3090Sc006InitDto> {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private Skf3090Sc006SharedService skf3090Sc006SharedService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf3090Sc006InitDto index(Skf3090Sc006InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3090_SC006_TITLE);

		// デリゲート処理の設定

		// 操作ログを出力する
		// MyBase.SetAccessLog(Constant.OperationLog.INIT)

		// 初期表示設定
		setInitDisp(initDto);
		return initDto;
	}

	private void setInitDisp(Skf3090Sc006InitDto initDto) {
		// コントロール名の設定

		// パンくず

		// マスタの設定

		// 「管理会社」ドロップダウンリストの設定
		List<Map<String, Object>> companyList = new ArrayList<Map<String, Object>>();
		companyList = skfDropDownUtils.getDdlCompanyByCd(initDto.getCompanyCd(), true);
		initDto.setCompanyList(companyList);
		// 組織登録画面から遷移時、ドロップダウンリストを設定
		setListValue(initDto);

		// 確認ダイアログメッセージ設定
	}

	private void setListValue(Skf3090Sc006InitDto initDto) {
		String companyCd = initDto.getCompanyCd();
		String agencyCd = initDto.getAgencyCd();
		String affiliation1Cd = initDto.getAffiliation1Cd();
		String affiliation2Cd = initDto.getAffiliation2Cd();
		String businessAreaCd = initDto.getBusinessAreaCd();

		// 「機関」ドロップダウンリストを設定
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList = skfDropDownUtils.getDdlAgencyByCd(companyCd, agencyCd, true);
		initDto.setAgencyList(agencyList);

		// 「部等」ドロップダウンをセット
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, affiliation1Cd, true);
		initDto.setAgencyList(affiliation1List);

		// 「室、チーム又は課」ドロップダウンをセット
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd, affiliation2Cd,
				true);
		initDto.setAgencyList(affiliation2List);

		// 「事業領域」ドロップダウンをセット
		List<Map<String, Object>> businessAreaList = new ArrayList<Map<String, Object>>();
		businessAreaList = skfDropDownUtils.getDdlBusinessAreaByCd(companyCd, agencyCd, businessAreaCd, true);
		initDto.setAgencyList(businessAreaList);

		// 画面データの設定
		List<Skf3090Sc006GetSoshikiInfoExp> soshikiInfoList = skf3090Sc006SharedService.getSoshikiInfo(companyCd,
				agencyCd, affiliation1Cd, affiliation2Cd, businessAreaCd);
	}
}
