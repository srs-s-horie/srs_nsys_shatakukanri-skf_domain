/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006ChangeContractDrpDwnDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc002ChangeContractDrpDwnService 保有社宅登録の契約情報プルダウン変更サービス処理クラス。
 * 同期処理
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006ChangeContractDrpDwnService extends SkfServiceAbstract<Skf3010Sc006ChangeContractDrpDwnDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);
	// 日付フォーマット
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public Skf3010Sc006ChangeContractDrpDwnDto index(Skf3010Sc006ChangeContractDrpDwnDto initDto) throws Exception {
		// デバッグログ
		logger.info("契約情報プルダウン変更");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("契約情報プルダウン変更", CodeConstant.C001, FunctionIdConstant.SKF3010_SC006);

		// 選択契約番号
		String selectedContraceNo = initDto.getHdnChangeContractSelectedIndex();

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
		//更新日時
		Date updateDate = null;
		// 契約情報削除ボタン(非活性：true, 活性:false)
		Boolean contractDelDisableFlg = true;

		// エラーコントロール初期化
		skf3010Sc006SharedService.clearVaridateErr(initDto);
		// 一旦画面を戻す
		skf3010Sc006SharedService.setBeforeInfo(initDto);

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
		// 未登録契約番号取得
		int delContractIndex = -1;
		for (int i = 0; i < contractNoList.size(); i++) {
			Map<String, Object> contractNoMap = contractNoList.get(i);
			// 削除契約番号判定
			if (contractNoMap.get("value").toString().contains("M")) {
				// 削除インデックス取得
				delContractIndex = i;
				break;
			}
		}
		// 未登録契約番号削除
		if (delContractIndex != -1) {
			contractNoList.remove(delContractIndex);
		}
		// 選択契約情報
		Map<String, Object> contractMap = new HashMap<String, Object>();
		if (contractNoList.size() > 0) {
			// 削除ボタン活性
			contractDelDisableFlg = false;
			for (Map<String, Object> contractDataMap : contractList) {
				if (Objects.equals(contractDataMap.get("contractNo"), selectedContraceNo)) {
					contractMap = contractDataMap;
					break;
				}
			}
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
		// 更新日時
		if (contractMap.get("updateDate") != null && contractMap.get("updateDate").toString().length() > 0) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			updateDate = dateFormat.parse(contractMap.get("updateDate").toString());
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
		initDto.setContractUpdateDate(updateDate);
		initDto.setHdnDispContractSelectedIndex(selectedContraceNo);
		// 追加ボタン活性
		initDto.setContractAddDisableFlg(false);
		initDto.setContractDelDisableFlg(contractDelDisableFlg);

		return initDto;
	}
}
