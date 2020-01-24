/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002DelContractListDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;

/**
 * Skf3010Sc002DelContractListService 保有社宅登録の契約情報削除ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002DelContractListService extends BaseServiceAbstract<Skf3010Sc002DelContractListDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * 保有社宅登録の契約情報削除ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc002DelContractListDto index(Skf3010Sc002DelContractListDto initDto) throws Exception {
		// デバッグログ
		LogUtils.debugByMsg("契約情報削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報削除", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);

		/** DTO設定値 */
		// 賃貸人（代理人）名
		String contractOwnerName = "";
		// 賃貸人（代理人）番号
		String contractOwnerNo = "";
		// 経理連携用管理番号
		String assetRegisterNo = "";
		// 契約開始日
		String contractStartDate = "";
		// 契約終了日
		String contractEndDate = "";
		// 家賃
		String rent = "";
		// 共益費
		String contractKyoekihi = "";
		// 駐車場料(地代)
		String landRent = "";
		// 備考
		String biko = "";
		// 契約情報追加ボタン(非活性：true, 活性:false)
		Boolean contractAddDisableFlg = false;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;
		// 駐車場契約情報ボタン(非活性：true, 活性:false)
		Boolean parkingContractDisableFlg = true;

		/** JSON(連携用) */
		// 駐車場区画情報リスト
		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// ドロップダウン選択値リスト
		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();

		// List変換
		parkingList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(initDto.getJsonParking()));
		bihinList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(initDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(initDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc002SharedService.clearVaridateErr(initDto);
		// 一旦画面を戻す
		skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, initDto);

		// 契約情報リスト取得
		List<Map<String, Object>> contractList = new ArrayList<Map<String, Object>>();
		if (initDto.getContractInfoListTableData() != null) {
			contractList.addAll(initDto.getContractInfoListTableData());
		}
		// 契約番号ドロップダウンリスト取得
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		if (initDto.getContractNoList() != null) {
			contractNoList.addAll(initDto.getContractNoList());
		}
		// 削除済契約番号
		String deletedConstractNo = CodeConstant.STRING_ZERO;
		if (initDto.getHdnDeleteContractSelectedValue() != null
				&& initDto.getHdnDeleteContractSelectedValue().length() > 0) {
			deletedConstractNo = initDto.getHdnDeleteContractSelectedValue();
		}
		// 削除契約番号判定
		if (!CodeConstant.STRING_ZERO.equals(deletedConstractNo)) {
			// 削除契約番号インデックス
			int delContractIndex = -1;
			// 選択設定解除 & 削除契約情報取得
			for (int i = 0; i < contractNoList.size(); i++) {
				Map<String, Object> contractNoMap = contractNoList.get(i);
				// 選択状態判定
				if (contractNoMap.containsKey("selected")) {
					// 選択状態解除
					contractNoMap.remove("selected");
				}
				// 削除契約番号判定
				if (Objects.equals(deletedConstractNo, contractNoMap.get("value"))) {
					// 削除インデックス取得
					delContractIndex = i;
				}
			}
			// 契約番号リストから削除契約番号を削除
			if (delContractIndex != -1) {
				contractNoList.remove(delContractIndex);
			}
		}
		// 選択契約情報
		Map<String, Object> contractMap = new HashMap<String, Object>();
		if (contractNoList.size() > 0) {
			// 削除ボタン活性
			contractDelDisableFlg = false;
			parkingContractDisableFlg = false;
			// 契約情報リストから最大契約番号の契約情報を取得
			String maxContractNo = contractNoList.get(contractNoList.size() - 1).get("value").toString();
			for (Map<String, Object> contractDataMap : contractList) {
				if (Objects.equals(contractDataMap.get("contractNo"), maxContractNo)) {
					contractMap = contractDataMap;
					break;
				}
			}
			// 選択値を最大値に設定
			contractNoList.get(contractNoList.size() - 1).put("selected", "true");
		} else {
			
		}

		// 「賃貸人(代理人)氏名または名称」取得
		if (contractMap.get("ownerName") != null) {
			contractOwnerName = contractMap.get("ownerName").toString();
		}
		// 「賃貸人(代理人)番号」取得
		if (contractMap.get("ownerNo") != null) {
			contractOwnerNo = contractMap.get("ownerNo").toString();
		}
		// 「経理連携用管理番号」取得
		if (contractMap.get("assetRegisterNo") != null) {
			assetRegisterNo = contractMap.get("assetRegisterNo").toString();
		}
		// 「契約開始日」取得
		if (contractMap.get("contractStartDate") != null) {
			contractStartDate = contractMap.get("contractStartDate").toString();
		}
		// 「契約終了日」取得
		if (contractMap.get("contractEndDate") != null) {
			contractEndDate = contractMap.get("contractEndDate").toString();
		}
		// 「家賃」取得
		if (contractMap.get("rent") != null) {
			rent = contractMap.get("rent").toString();
		}
		// 「共益費」取得
		if (contractMap.get("contractKyoekihi") != null) {
			contractKyoekihi = contractMap.get("contractKyoekihi").toString();
		}
		// 「駐車場料(地代)」取得
		if (contractMap.get("landRent") != null) {
			landRent = contractMap.get("landRent").toString();
		}
		// 「備考」取得
		if (contractMap.get("biko") != null) {
			biko = contractMap.get("biko").toString();
		}

		// 戻り値設定
		initDto.setContractNoList(contractNoList);
		initDto.setContractOwnerName(contractOwnerName);
		initDto.setContractOwnerNo(contractOwnerNo);
		initDto.setAssetRegisterNo(assetRegisterNo);
		initDto.setContractStartDay(contractStartDate);
		initDto.setContractEndDay(contractEndDate);
		initDto.setContractRent(rent);
		initDto.setContractKyoekihi(contractKyoekihi);
		initDto.setContractLandRent(landRent);
		initDto.setContractBiko(biko);
		initDto.setContractAddDisableFlg(contractAddDisableFlg);
		initDto.setContractDelDisableFlg(contractDelDisableFlg);
		initDto.setParkingContractDisableFlg(parkingContractDisableFlg);

		return initDto;
	}
}
