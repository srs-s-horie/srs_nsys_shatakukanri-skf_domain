/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006AddContractListDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002AddContractListService 保有社宅登録の契約情報追加ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006AddContractListService extends BaseServiceAbstract<Skf3010Sc006AddContractListDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 保有社宅登録の契約情報追加ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc006AddContractListDto index(Skf3010Sc006AddContractListDto initDto) throws Exception {
		// デバッグログ
		logger.debug("契約情報追加");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報追加", CodeConstant.C001, initDto.getPageId());

//		/** JSON(連携用) */
//		// 駐車場区画情報リスト
//		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
//		// 備品情報リスト
//		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
//		// ドロップダウン選択値リスト
//		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
//		// 可変ラベルリスト
//		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();

		// List変換
//		parkingList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(initDto.getJsonParking()));
//		bihinList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(initDto.getJsonBihin()));
//		drpDwnSelectedList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(initDto.getJsonDrpDwnList()));
//		labelList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()));
		
		// エラーコントロール初期化
		skf3010Sc006SharedService.clearVaridateErr(initDto);
		// 一旦画面を戻す
		skf3010Sc006SharedService.setBeforeInfo(initDto);

		// 契約番号ドロップダウンリスト取得
		List<Map<String, Object>> contractNoList = new ArrayList<Map<String, Object>>();
		if (initDto.getContractNoList() != null) {
			contractNoList.addAll(initDto.getContractNoList());
		}
		// 選択設定解除
		for (Map<String, Object> contractNoMap : contractNoList) {
			if (contractNoMap.containsKey("selected")) {
				contractNoMap.remove("selected");
				break;
			}
		}
		// 契約番号最大値取得
		int maxContractNo = 0;
		if (contractNoList.size() > 0) {
			maxContractNo = Integer.parseInt(contractNoList.get(contractNoList.size() - 1).get("value").toString());
		}
		// 契約番号インクリメント
		maxContractNo++;
		// 新規契約番号を契約番号リストに追加し選択状態に設定する
		Map<String, Object> contractMap = new HashMap<String, Object>();
		contractMap.put("value", Integer.toString(maxContractNo) + "M");
		contractMap.put("label", Integer.toString(maxContractNo) + " ：");
		contractMap.put("selected", true);
		contractNoList.add(contractMap);
		// 戻り値設定
		initDto.setContractNoList(contractNoList);
		initDto.setContractOwnerName("");
		initDto.setContractOwnerNo("");
		initDto.setAssetRegisterNo("");
		initDto.setContractStartDay("");
		initDto.setContractEndDay("");
		initDto.setContractRent("");
		initDto.setContractKyoekihi("");
		initDto.setContractLandRent("");
		initDto.setContractBiko("");
		initDto.setHdnDispContractSelectedIndex(Integer.toString(maxContractNo) + "M");
		// 追加ボタン非活性
		initDto.setContractAddDisableFlg(true);
		// 削除ボタン活性
		initDto.setContractDelDisableFlg(false);

		return initDto;
	}
}
