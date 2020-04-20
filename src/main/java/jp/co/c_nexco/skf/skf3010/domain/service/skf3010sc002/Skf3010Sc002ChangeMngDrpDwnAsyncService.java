/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002ChangeMngDrpDwnAsyncDto;

/**
 * Skf3010Sc002ChangeMngDrpDwnAsyncService 保有社宅登録管理系ドロップダウン変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002ChangeMngDrpDwnAsyncService
		extends SkfAsyncServiceAbstract<Skf3010Sc002ChangeMngDrpDwnAsyncDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002ChangeMngDrpDwnAsyncDto asyncDto) throws Exception {

		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		// 利用区分リスト
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		// 管理会社リスト
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		// 管理機関リスト
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		// 管理事業領域リスト
		List<Map<String, Object>> manageBusinessAreaList = new ArrayList<Map<String, Object>>();
		// 都道府県リスト
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		// 社宅構造リスト
		List<Map<String, Object>> shatakuStructureList = new ArrayList<Map<String, Object>>();
		// エレベーターリスト
		List<Map<String, Object>> elevatorList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3010Sc002SharedService.getDoropDownList(
				asyncDto.getUseKbnCd(), useKbnList,
				asyncDto.getSelectedCompanyCd(), manageCompanyList,
				asyncDto.getAgencyCd(), manageAgencyList,
				asyncDto.getManageBusinessAreaCd(), manageBusinessAreaList,
				asyncDto.getPrefCd(), prefList,
				asyncDto.getStructureKbn(), shatakuStructureList,
				asyncDto.getElevatorKbn(), elevatorList);

		// セッション情報削除
		asyncDto.setUseKbnList(null);
		asyncDto.setManageCompanyList(null);
		asyncDto.setManageAgencyList(null);
		asyncDto.setManageBusinessAreaList(null);
		asyncDto.setPrefList(null);
		asyncDto.setShatakuStructureList(null);
		asyncDto.setElevatorList(null);

		// ドロップダウンリストをDTOにセット
		asyncDto.setUseKbnList(useKbnList);
		asyncDto.setManageCompanyList(manageCompanyList);
		asyncDto.setManageAgencyList(manageAgencyList);
		asyncDto.setManageBusinessAreaList(manageBusinessAreaList);
		asyncDto.setPrefList(prefList);
		asyncDto.setShatakuStructureList(shatakuStructureList);
		asyncDto.setElevatorList(elevatorList);
		// 解放
		useKbnList = null;
		manageCompanyList = null;
		manageAgencyList = null;
		manageBusinessAreaList = null;
		prefList = null;
		shatakuStructureList = null;
		elevatorList = null;

		return asyncDto;
	}
}
