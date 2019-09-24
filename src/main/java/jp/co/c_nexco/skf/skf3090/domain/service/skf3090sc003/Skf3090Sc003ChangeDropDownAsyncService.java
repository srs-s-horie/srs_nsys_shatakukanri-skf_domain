/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc003;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc003.Skf3090Sc003ChangeDropDownAsyncDto;

/**
 * Skf3090Sc003ChangeDropDownAsyncService 事業領域マスタ登録ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc003ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3090Sc003ChangeDropDownAsyncDto> {

	@Autowired
	private Skf3090Sc003SharedService skf3090Sc003SharedService;

	/**
	 * ドロップダウンリスト表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3090Sc003ChangeDropDownAsyncDto asyncDto) throws Exception {

		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> manageCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> manageAgencyList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストの値を設定
		boolean isFirstRowEmpty = true;
		skf3090Sc003SharedService.getDoropDownManageCompanyList(asyncDto.getSelectedAddManageCompanyCd(), manageCompanyList);
		manageAgencyList = skf3090Sc003SharedService.getAgencyCdList(asyncDto.getSelectedAddManageCompanyCd(), isFirstRowEmpty);
		
		// セッション情報削除
		asyncDto.setAddManageCompanyList(null);
		asyncDto.setAddManageAgencyList(null);

		// ドロップダウンリストをDTOにセット
		asyncDto.setAddManageCompanyList(manageCompanyList);
		asyncDto.setAddManageAgencyList(manageAgencyList);
		// 解放
		manageCompanyList = null;
		manageAgencyList = null;

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("管理会社コード:" + asyncDto.getAddManageCompanyList());
		LogUtils.debugByMsg("管理機関コード:" + asyncDto.getAddManageAgencyList());
		
		return asyncDto;
	}

}
