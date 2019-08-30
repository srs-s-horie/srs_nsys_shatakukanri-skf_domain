/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001ChangeDropDownAsyncDto;

/**
 * Skf3010Sc001ChangeDropDownAsyncService 社宅一覧ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc001ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc001ChangeDropDownAsyncDto> {

	@Autowired
	private Skf3010Sc001SharedService skf3010Sc001SharedService;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc001ChangeDropDownAsyncDto asyncDto) throws Exception {

		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyRoomList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> useKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> emptyParkingList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		skf3010Sc001SharedService.getDoropDownList(asyncDto.getSelectedCompanyCd(), manageCompanyList,
				asyncDto.getAgencyCd(), manageAgencyList, asyncDto.getShatakuKbnCd(), shatakuKbnList,
				asyncDto.getEmptyRoomCd(), emptyRoomList, asyncDto.getUseKbnCd(), useKbnList, asyncDto.getEmptyParkingCd(),
				emptyParkingList);

		// セッション情報削除
		asyncDto.setManageCompanyList(null);
		asyncDto.setManageAgencyList(null);
		asyncDto.setEmptyRoomList(null);
		asyncDto.setShatakuKbnList(null);
		asyncDto.setUseKbnList(null);
		asyncDto.setEmptyParkingList(null);

		// ドロップダウンリストをDTOにセット
		asyncDto.setManageCompanyList(manageCompanyList);
		asyncDto.setManageAgencyList(manageAgencyList);
		asyncDto.setEmptyRoomList(emptyRoomList);
		asyncDto.setShatakuKbnList(shatakuKbnList);
		asyncDto.setUseKbnList(useKbnList);
		asyncDto.setEmptyParkingList(emptyParkingList);
		// 解放
		manageCompanyList = null;
		manageAgencyList = null;
		emptyRoomList = null;
		shatakuKbnList = null;
		useKbnList = null;
		emptyParkingList = null;

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("管理会社コード:" + asyncDto.getSelectedCompanyCd());
		return asyncDto;
	}

}
