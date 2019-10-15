/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuParkingTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManege;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContractKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockContractDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002UpdateMshatakuParkingTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002UpdateMshatakuTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuManegeRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingContractRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002RegistDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;
import jp.co.intra_mart.common.platform.log.Logger;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3010Sc002RegistService 保有社宅登録の契約情報「登録」ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002RegistService extends BaseServiceAbstract<Skf3010Sc002RegistDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDropDownUtils ddlUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3010Sc002UpdateMshatakuParkingTableDataExpRepository skf3010Sc002UpdateMshatakuParkingTableDataExpRepository;
	@Autowired
	private Skf3010Sc002UpdateMshatakuTableDataExpRepository skf3010Sc002UpdateMshatakuTableDataExpRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3010MShatakuBihinRepository skf3010MShatakuBihinRepository;
	@Autowired
	private Skf3010MShatakuManegeRepository skf3010MShatakuManegeRepository;
	@Autowired
	private Skf3010MShatakuContractRepository skf3010MShatakuContractRepository;
	@Autowired
	private Skf3010MShatakuParkingContractRepository skf3010MShatakuParkingContractRepository;
	@Autowired
	private Skf3010Sc002GetParkingBlockContractDataExpRepository skf3010Sc002GetParkingBlockContractDataExpRepository;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/** 定数 */
	// 契約情報区切り文字：「契約開始日 」
	private static final String CONTRACT_NO_SEPARATOR = "：契約開始日 ";
	// 駐車場契約形態：社宅と一括
	private static final String PARKING_CONTRACT_TYPE = "1";
	// 駐車場所在地区分：社宅と同一所在地
	private static final String PARKING_ADDRESS = "0";
	// 駐車場構造区分：なし
	private static final String PARKING_STRUCTURE_NASHI = "5";
	// メールアドレスチェック正規表現
	private static final String MAIL_ADDRESS_CHECK_REG = "[!-~]+@[!-~]+.[!-~]+";
	// 管理者電話番号チェック正規表現
	private static final String MANAGE_TELNO_CHECK_REG = "^[0-9-]*$";
	// 添付ファイル上限サイズ(10M)
	private static final Long TEMP_FILE_MAX_SIZE = 10485760L;
	
	/**
	 * 保有社宅登録の契約情報「登録」ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	@Transactional
	public Skf3010Sc002RegistDto index(Skf3010Sc002RegistDto registDto) throws Exception {
		// デバッグログ
		logger.info("保有社宅情報登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("保有社宅情報登録", CodeConstant.C001, registDto.getPageId());

		/** JSON(連携用) */
		// 駐車場区画情報リスト
		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// ドロップダウン選択値リスト
		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();

		/** 更新用パラメータ */
		// 社宅基本パラメータ
		Skf3010Sc002MShatakuTableDataExpParameter mShataku = new Skf3010Sc002MShatakuTableDataExpParameter();
		// 社宅駐車場パラメータ
		Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking = new Skf3010Sc002MShatakuParkingTableDataExpParameter();
		// 駐車場区画情報パラメータ
		List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList = new ArrayList<Skf3010MShatakuParkingBlock>();
		// 社宅管理者情報リスト
		List<Skf3010MShatakuManege> mShatakuManageList = new ArrayList<Skf3010MShatakuManege>();
		// 社宅備品リスト
		List<Skf3010MShatakuBihin> mShatakuBihinList = new ArrayList<Skf3010MShatakuBihin>();
		// 契約情報
		Skf3010MShatakuContract mShatakuContract = new Skf3010MShatakuContract();

		// List変換
		parkingList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(registDto.getJsonParking()));
		bihinList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(registDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(registDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(registDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc002SharedService.clearVaridateErr(registDto);
		// 表示タブインデックス
		String tabIndex = (registDto.getHdnNowSelectTabIndex() != null) ? registDto.getHdnNowSelectTabIndex() : "999"; 
		// DTO初期化
		registDto.setHdnNowSelectTabIndex("999");

		// 入力チェック
		if (!checkTouroku(parkingList, drpDwnSelectedList, registDto)) {
			// 元の画面状態に戻す
			setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, registDto);
			return registDto;
		}
		// エラー発生時用に元の画面状態を設定
		setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, registDto);

		/** 更新用データ作成 */
		// 社宅基本
		setUpdateColumShatakuKihon(mShataku, drpDwnSelectedList, labelList, registDto);
		// 社宅駐車場
		setUpdateColumParking(mShatakuParking, drpDwnSelectedList, labelList, registDto);
		// 駐車場区画
		setUpdateColumParkingBlock(mShatakuParkingBlockList, parkingList, registDto);
		// 社宅管理者
		setUpdateColumShatakuManage(mShatakuManageList, registDto);
		// 社宅備品
		setUpdateColumBihin(mShatakuBihinList, bihinList, registDto);
		// 契約情報
		setUpdateColumContract(mShatakuContract, registDto);
		// 更新カウンタ
		int updateCnt = 0;

		Map <String, Object> labelMap = (labelList.size() > 0) ? labelList.get(0) : null;

		// 新規登録判定:社宅管理番号なし
		if (SkfCheckUtils.isNullOrEmpty(registDto.getHdnShatakuKanriNo())) {
			// 新規社宅登録
			// 社宅重複チェック
			Boolean duplicate = checkSameShataku(registDto.getShatakuName(),
									registDto.getPref(), registDto.getShatakuAddress(), registDto);
			// 重複チェック結果判定
			if (!duplicate) {
				return registDto;
			}
			// 新規追加
			updateCnt = insertHoyuShatakuInfo(mShataku, mShatakuParking, mShatakuParkingBlockList,
							mShatakuBihinList, mShatakuManageList, mShatakuContract, registDto.getPageId(), registDto.getIttoFlg());
			// 更新結果判定
			if (updateCnt < 1) {
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			// 新規の為、空き部屋数を「0/0」に設定
			registDto.setEmptyRoomCount("0/0");
		} else {
			// 更新社宅登録
			updateCnt = updateHoyuShatakuInfo(
					drpDwnSelectedList,
					mShataku, 
					mShatakuParking,
					mShatakuParkingBlockList,
					mShatakuBihinList,
					mShatakuManageList,
					mShatakuContract,
					registDto.getPageId(),
					registDto);
			// 更新結果判定
			if (updateCnt < 1) {
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
		}

		// 成功メッセージ
		ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		// DTO設定
		registDto.setHdnShatakuKanriNo(mShataku.getShatakuKanriNo().toString());
		registDto.setHdnShatakuName(mShataku.getShatakuName());
		registDto.setAreaKbnCd(mShataku.getAddress());
		registDto.setHdnEmptyParkingCount(labelMap.get("lendPossibleCount").toString());
		// 初期表示
		// 契約番号
		String contractNo = "";
		if (mShatakuContract.getContractPropertyId() != null) {
			contractNo = mShatakuContract.getContractPropertyId().toString();
		}
		skf3010Sc002SharedService.setHoyuShatakuInfo(
				Skf3010Sc002CommonDto.CONTRACT_MODE_INIT, contractNo, registDto);
		registDto.setHdnNowSelectTabIndex(tabIndex);
		return registDto;
	}
	/**
	 * 保有社宅更新
	 * 
	 * @param drpDwnSelectedList		ドロップダウン選択値リスト
	 * @param mShataku					社宅基本
	 * @param mShatakuParking			社宅駐車場
	 * @param mShatakuParkingBlockList	駐車場区画
	 * @param mShatakuBihinList			社宅備品
	 * @param mShatakuManageList		社宅管理者
	 * @param mShatakuContract			社宅契約
	 * @param pageId					ページID(プログラムID)
	 * @param registDto					一棟借上フラグ(trueは一棟)
	 * @return	更新数
	 */
	private int updateHoyuShatakuInfo(
			List<Map<String, Object>> drpDwnSelectedList,
			Skf3010Sc002MShatakuTableDataExpParameter mShataku, 
			Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking,
			List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList,
			List<Skf3010MShatakuBihin> mShatakuBihinList,
			List<Skf3010MShatakuManege> mShatakuManageList,
			Skf3010MShatakuContract mShatakuContract,
			String pageId,
			Skf3010Sc002RegistDto registDto) {

		Map <String, Object> drpDwnSelected = (drpDwnSelectedList.size() > 0) ? drpDwnSelectedList.get(0) : null;

		// 更新カウント
		int updateCnt = 0;
		// 社宅管理番号取得
		Long shatakuKanriNo = Long.parseLong(registDto.getHdnShatakuKanriNo());
		// ユーザーID取得
		String userName = skfLoginUserInfoUtils.getSkfLoginUserInfo().get("userName");

		/** 社宅基本更新 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShataku.setUpdateUserId(userName);
		// プログラムID設定
		mShataku.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuTableDataExpRepository.updateShatakuKihon(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本更新エラー");
			return updateCnt;
		}
		/** 社宅駐車場更新 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShatakuParking.setUpdateUserId(userName);
		// プログラムID設定
		mShatakuParking.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuParkingTableDataExpRepository.updateShatakuParking(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場更新エラー");
			return updateCnt;
		}
		/** 駐車場区画更新 */
		// 更新前区画情報
		List<Map<String, Object>> startingParking = registDto.getHdnStartingParkingInfoListTableData();
		// 区画情報削除
		// DB取得数分ループ
		for(Map<String, Object> parkingBlock : startingParking) {
			// 画面リストに存在フラグ
			Boolean existFlg = false;
			// DB取得駐車場管理番号取得
			Long dbKanriNo = Long.parseLong(parkingBlock.get("parkingKanriNo").toString());
			for (Skf3010MShatakuParkingBlock mShatakuParkingBlock : mShatakuParkingBlockList) {
				// DB取得管理番号と画面から取得の管理番号を比較
				if (dbKanriNo.equals(mShatakuParkingBlock.getParkingKanriNo())) {
					// 存在したためループを抜ける
					existFlg = true;
					break;
				}
			}
			// 画面リスト存在判定
			if (existFlg) {
				// 存在したため次のループ処理へ
				continue;
			}
			// 存在しない為、DBから削除
			Skf3010MShatakuParkingBlockKey delParking = new Skf3010MShatakuParkingBlockKey();
			delParking.setShatakuKanriNo(shatakuKanriNo);
			delParking.setParkingKanriNo(dbKanriNo);
			skf3010MShatakuParkingBlockRepository.deleteByPrimaryKey(delParking);
			// 一棟借上判定
			if (registDto.getIttoFlg()) {
				// 一棟借上げの場合、区画契約情報も削除する
				List<Skf3010Sc002GetParkingBlockContractInfoTableDataExp> blockContractList =
							new ArrayList<Skf3010Sc002GetParkingBlockContractInfoTableDataExp>();
				Skf3010Sc002GetParkingBlockHistroyCountExpParameter param =
							new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
				param.setShatakuKanriNo(shatakuKanriNo);
				param.setParkingKanriNo(dbKanriNo);
				// 区画契約情報取得
				blockContractList = skf3010Sc002GetParkingBlockContractDataExpRepository.getParkingBlockContract(param);
				// 区画契約情報数分ループ
				for(Skf3010Sc002GetParkingBlockContractInfoTableDataExp blockContract : blockContractList) {
					Skf3010MShatakuParkingContractKey key = new Skf3010MShatakuParkingContractKey();
					key.setShatakuKanriNo(blockContract.getShatakuKanriNo());
					key.setParkingKanriNo(blockContract.getParkingKanriNo());
					key.setContractPropertyId(blockContract.getContractPropertyId());
					// 区画契約情報削除
					skf3010MShatakuParkingContractRepository.deleteByPrimaryKey(key);
					key = null;
				}
				blockContractList = null;
				param = null;
			}
			delParking = null;
		}
		// 次新規区画駐車場管理番号
		Long parkingKanriNo = skf3010Sc002SharedService.getNextParkingKanriNo(shatakuKanriNo.toString());
		// 区画情報登録更新ループ
		for (Skf3010MShatakuParkingBlock mShatakuParkingBlock : mShatakuParkingBlockList) {
			// 社宅管理番号設定
			mShatakuParkingBlock.setShatakuKanriNo(shatakuKanriNo);
			// 駐車場管理番号設定判定
			if (mShatakuParkingBlock.getParkingKanriNo() != 0L) {
				// 既存データ更新
				updateCnt = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(mShatakuParkingBlock);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場区画更新エラー：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
			} else {
				// 新規区画
				// 駐車場管理番号設定
				mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
				updateCnt = skf3010MShatakuParkingBlockRepository.insertSelective(mShatakuParkingBlock);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場区画登録エラー：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
				// 一棟借上判定
				if (registDto.getIttoFlg()) {
					// 一棟借上げの場合、区画契約情報も登録する
					// 駐車場契約情報登録
					Skf3010MShatakuParkingContract parkingContract = new Skf3010MShatakuParkingContract();
					parkingContract.setShatakuKanriNo(shatakuKanriNo);
					parkingContract.setParkingKanriNo(parkingKanriNo);
					parkingContract.setContractPropertyId(1L);
					parkingContract.setParkingContractType(PARKING_CONTRACT_TYPE);
					parkingContract.setParkingAddressKbn(PARKING_ADDRESS);
					updateCnt = skf3010MShatakuParkingContractRepository.insertSelective(parkingContract);
					// 更新カウント判定
					if (updateCnt < 1) {
						LogUtils.debugByMsg("駐車場契約情報登録エラー：" + mShatakuParkingBlock.getParkingBlock());
						return updateCnt;
					}
				}
				// 次新規区画駐車場管理番号更新
				parkingKanriNo++;
			}
		}
		/** 社宅備品更新 */
		for (Skf3010MShatakuBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			// 更新日設定判定
			if (bihin.getLastUpdateDate() != null) {
				// 更新
				updateCnt = skf3010MShatakuBihinRepository.updateByPrimaryKeySelective(bihin);
			} else {
				// 追加
				updateCnt = skf3010MShatakuBihinRepository.insertSelective(bihin);
			}
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("備品更新エラー：" + bihin.getBihinCd());
				return updateCnt;
			}
		}
		/** 社宅管理者更新 */
		for (Skf3010MShatakuManege manage : mShatakuManageList) {
			// 社宅管理番号設定
			manage.setShatakuKanriNo(shatakuKanriNo);
			updateCnt = skf3010MShatakuManegeRepository.updateByPrimaryKeySelective(manage);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅管理者更新エラー：" + manage.getManegeKbn());
				return updateCnt;
			}
		}
		/** 社宅契約情報更新 */
		// 削除契約番号取得
		String delContractNo = registDto.getHdnDeleteContractSelectedValue();
		// DB取得契約情報取得
		List<Map<String, Object>> dbContractList = registDto.getContractInfoListTableData();
		// 削除契約情報削除
		if (delContractNo != null && !CodeConstant.STRING_ZERO.equals(delContractNo)) {
			Long delNo = Long.parseLong(delContractNo);
			for (Map<String, Object> contract : dbContractList) {
				// 削除契約番号判定
				Long tmpNo = Long.parseLong(contract.get("contractNo").toString());
				if (delNo > tmpNo) {
					// 削除契約番号より小さいため対象外
					continue;
				}
				// 削除対象のため削除
				Skf3010MShatakuContract deleteContract = new Skf3010MShatakuContract();
				deleteContract.setShatakuKanriNo(shatakuKanriNo);
				deleteContract.setContractPropertyId(tmpNo);
				skf3010MShatakuContractRepository.deleteByPrimaryKey(deleteContract);
				deleteContract = null;
			}
		}
		// 経理連携用管理番号設定判定
		if (mShatakuContract.getAssetRegisterNo() != null && mShatakuContract.getAssetRegisterNo().length() > 0) {
			// 社宅管理番号設定
			mShatakuContract.setShatakuKanriNo(shatakuKanriNo);
			// 契約番号設定
			mShatakuContract.setContractPropertyId(1L);
			// 追加更新判定
			if (drpDwnSelected.get("contractText") != null
					&& drpDwnSelected.get("contractText").toString().contains(CONTRACT_NO_SEPARATOR)) {
				// 更新
				updateCnt = skf3010MShatakuContractRepository.updateByPrimaryKeySelective(mShatakuContract);
			} else {
				// 追加
				updateCnt = skf3010MShatakuContractRepository.insertSelective(mShatakuContract);
			}
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅契約情報更新エラー：" + mShatakuContract.getContractPropertyId());
				return updateCnt;
			}
		}
		return updateCnt;
	}

	/**
	 * 新規保有社宅登録
	 * 
	 * @param mShataku					社宅基本
	 * @param mShatakuParking			社宅駐車場
	 * @param mShatakuParkingBlockList	駐車場区画リスト
	 * @param mShatakuBihinList			社宅備品リスト
	 * @param mShatakuManageList		社宅管理者リスト
	 * @param mShatakuContract			社宅契約
	 * @param pageId					ページID(プログラムID)
	 * @return	登録件数
	 */
	private int insertHoyuShatakuInfo(Skf3010Sc002MShatakuTableDataExpParameter mShataku, 
			Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking,
			List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList,
			List<Skf3010MShatakuBihin> mShatakuBihinList, List<Skf3010MShatakuManege> mShatakuManageList,
			Skf3010MShatakuContract mShatakuContract, String pageId, Boolean ittoFlg) {

		// 更新カウント
		int updateCnt = 0;
		// 処理年月
		String syoriNengetu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		// 社宅管理番号取得
		Long shatakuKanriNo = skf3010Sc002SharedService.getNextShatakuKanriNo(syoriNengetu);
		// ユーザーID取得
		String userName = skfLoginUserInfoUtils.getSkfLoginUserInfo().get("userName");

		/** 社宅基本登録 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShataku.setUpdateUserId(userName);
		mShataku.setInsertUserId(userName);
		// プログラムID設定
		mShataku.setUpdateProgramId(pageId);
		mShataku.setInsertProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuTableDataExpRepository.insertShatakuKihon(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本登録エラー");
			return updateCnt;
		}
		/** 社宅駐車場登録 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShatakuParking.setUpdateUserId(userName);
		mShatakuParking.setInsertUserId(userName);
		// プログラムID設定
		mShatakuParking.setUpdateProgramId(pageId);
		mShatakuParking.setInsertProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuParkingTableDataExpRepository.insertShatakuParking(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場登録エラー");
			return updateCnt;
		}
		/** 駐車場区画情報登録 */
		// 次新規区画駐車場管理番号
		Long parkingKanriNo = skf3010Sc002SharedService.getNextParkingKanriNo(shatakuKanriNo.toString());
		for (Skf3010MShatakuParkingBlock mShatakuParkingBlock : mShatakuParkingBlockList) {
			// 社宅管理番号設定
			mShatakuParkingBlock.setShatakuKanriNo(shatakuKanriNo);
			// 駐車場管理番号設定
			mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
			// 駐車場区画情報登録
			updateCnt = skf3010MShatakuParkingBlockRepository.insertSelective(mShatakuParkingBlock);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("駐車場区画登録エラー：" + mShatakuParkingBlock.getParkingBlock());
				return updateCnt;
			}
			// 一棟借上判定
			if (ittoFlg) {
				// 駐車場契約情報登録
				Skf3010MShatakuParkingContract parkingContract = new Skf3010MShatakuParkingContract();
				parkingContract.setShatakuKanriNo(shatakuKanriNo);
				parkingContract.setParkingKanriNo(parkingKanriNo);
				parkingContract.setContractPropertyId(1L);
				parkingContract.setParkingContractType(PARKING_CONTRACT_TYPE);
				parkingContract.setParkingAddressKbn(PARKING_ADDRESS);
				updateCnt = skf3010MShatakuParkingContractRepository.insertSelective(parkingContract);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場契約情報登録エラー：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
			}
			parkingKanriNo++;
		}
		/** 社宅備品登録 */
		for (Skf3010MShatakuBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			updateCnt = skf3010MShatakuBihinRepository.insertSelective(bihin);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("備品登録エラー：" + bihin.getBihinCd());
				return updateCnt;
			}
		}
		/** 社宅管理者登録 */
		for (Skf3010MShatakuManege manage : mShatakuManageList) {
			// 社宅管理番号設定
			manage.setShatakuKanriNo(shatakuKanriNo);
			updateCnt = skf3010MShatakuManegeRepository.insertSelective(manage);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅管理者登録エラー：" + manage.getManegeKbn());
				return updateCnt;
			}
		}
		/** 社宅契約情報登録 */
		// 経理連携用管理番号設定判定
		if (mShatakuContract.getAssetRegisterNo() != null && mShatakuContract.getAssetRegisterNo().length() > 0) {
			// 社宅管理番号設定
			mShatakuContract.setShatakuKanriNo(shatakuKanriNo);
			// 契約番号設定
			mShatakuContract.setContractPropertyId(1L);
			updateCnt = skf3010MShatakuContractRepository.insertSelective(mShatakuContract);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅契約情報登録エラー：" + mShatakuContract.getContractPropertyId());
				return updateCnt;
			}
		}
		return updateCnt;
	}

	/**
	 * 社宅基本情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShataku				*社宅基本情報マスタエンティティ
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param registDto				DTO
	 */
	private void setUpdateColumShatakuKihon(
			Skf3010Sc002MShatakuTableDataExpParameter mShataku, List<Map<String, Object>> drpDwnSelectedList,
				List<Map<String, Object>> labelList, Skf3010Sc002RegistDto registDto) {

		Map <String, Object> drpDwnSelectedMap = drpDwnSelectedList.get(0);
		Map <String, Object> labelMap = labelList.get(0);

		/** 社宅基本情報マスタの更新データを作成 */
		// 郵便番号
		String zipCd = ("".equals(registDto.getZipCd())) ? null : registDto.getZipCd();
		// 社宅構造補足
		String structureSupplement = ("".equals(registDto.getShatakuStructureDetail())) ? null : registDto.getShatakuStructureDetail();
		// エレベーター区分
		String elevatorKbn = ("".equals(drpDwnSelectedMap.get("elevator")) ? null : drpDwnSelectedMap.get("elevator").toString());
		// 備考
		String biko = ("".equals(registDto.getBiko())) ? null : registDto.getBiko();

		// 社宅補足ファイル名
		String shatakuSupplementName1 = ("".equals(registDto.getShatakuHosokuFileName1())) ? null : registDto.getShatakuHosokuFileName1();
		String shatakuSupplementName2 = ("".equals(registDto.getShatakuHosokuFileName2())) ? null : registDto.getShatakuHosokuFileName2();
		String shatakuSupplementName3 = ("".equals(registDto.getShatakuHosokuFileName3())) ? null : registDto.getShatakuHosokuFileName3();
		// 社宅補足ファイルサイズ
		String shatakuSupplementSize1 = ("".equals(registDto.getShatakuHosokuSize1())) ? null : registDto.getShatakuHosokuSize1();
		String shatakuSupplementSize2 = ("".equals(registDto.getShatakuHosokuSize2())) ? null : registDto.getShatakuHosokuSize2();
		String shatakuSupplementSize3 = ("".equals(registDto.getShatakuHosokuSize3())) ? null : registDto.getShatakuHosokuSize3();

		// 社宅名
		mShataku.setShatakuName(registDto.getShatakuName());
		// 社宅区分
		mShataku.setShatakuKbn(drpDwnSelectedMap.get("shatakuKbn").toString());
		// 地域区分
		mShataku.setAreaKbn(drpDwnSelectedMap.get("areaKbn").toString());
		// 利用区分
		mShataku.setUseKbn(drpDwnSelectedMap.get("useKbn").toString());
		// 会社コード
		mShataku.setManegeCompanyCd(drpDwnSelectedMap.get("manageCompany").toString());
		// 管理会社コード
		mShataku.setManegeAgencyCd(drpDwnSelectedMap.get("manageAgency").toString());
		// 管理事業領域コード
		mShataku.setManegeBusinessAreaCd(drpDwnSelectedMap.get("manageBusinessArea").toString());
		// 郵便番号
		mShataku.setZipCd(zipCd);
		// 都道府県コード
		mShataku.setPrefCd(drpDwnSelectedMap.get("pref").toString());
		// 住所
		mShataku.setAddress(registDto.getShatakuAddress());
		// 建築年月日
		mShataku.setBuildDate(registDto.getBuildDate().replace("/", ""));
		// 社宅構造区分
		mShataku.setStructureKbn(drpDwnSelectedMap.get("shatakuStructure").toString());
		// 社宅構造補足
		mShataku.setStructureSupplement(structureSupplement);
		// エレベーター区分
		mShataku.setElevatorKbn(elevatorKbn);
		// 次回算定年月日
		mShataku.setNextCalculateDate(labelMap.get("nextCalculateDate").toString().replace("/", ""));
		// 社宅補足ファイル名
		mShataku.setShatakuSupplementName1(shatakuSupplementName1);
		mShataku.setShatakuSupplementName2(shatakuSupplementName2);
		mShataku.setShatakuSupplementName3(shatakuSupplementName3);
		// 社宅補足ファイルサイズ
		mShataku.setShatakuSupplementSize1(shatakuSupplementSize1);
		mShataku.setShatakuSupplementSize2(shatakuSupplementSize2);
		mShataku.setShatakuSupplementSize3(shatakuSupplementSize3);
		// 社宅補足ファイル
		mShataku.setShatakuSupplementFile1(registDto.getShatakuHosokuFile1());
		mShataku.setShatakuSupplementFile2(registDto.getShatakuHosokuFile2());
		mShataku.setShatakuSupplementFile3(registDto.getShatakuHosokuFile3());
		// 備考
		mShataku.setBiko(biko);
		// 更新日時
		mShataku.setLastUpdateDate(registDto.getKihonUpdateDate());
	}

	/**
	 * 社宅駐車場情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuParking		*社宅駐車場情報マスタエンティティ
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param registDto				DTO
	 */
	private void setUpdateColumParking(
			Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking, List<Map<String, Object>> drpDwnSelectedList,
			List<Map<String, Object>> labelList, Skf3010Sc002RegistDto registDto) {

		Map <String, Object> drpDwnSelectedMap = drpDwnSelectedList.get(0);
		Map <String, Object> labelMap = labelList.get(0);

		/** 社宅駐車場情報マスタの更新データを作成 */
		// 駐車場補足ファイル名
		String fileName1 = ("".equals(registDto.getParkingHosokuFileName1())) ? null: registDto.getParkingHosokuFileName1();
		String fileName2 = ("".equals(registDto.getParkingHosokuFileName2())) ? null: registDto.getParkingHosokuFileName2();
		String fileName3 = ("".equals(registDto.getParkingHosokuFileName3())) ? null: registDto.getParkingHosokuFileName3();
		// ファイルサイズ
		String fileSize1 = ("".equals(registDto.getParkingHosokuSize1())) ? null: registDto.getParkingHosokuSize1();
		String fileSize2 = ("".equals(registDto.getParkingHosokuSize2())) ? null: registDto.getParkingHosokuSize2();
		String fileSize3 = ("".equals(registDto.getParkingHosokuSize3())) ? null: registDto.getParkingHosokuSize3();
		// 備考
		String biko = ("".equals(registDto.getParkingBiko())) ? null : registDto.getParkingBiko();

		// 駐車場構造
		mShatakuParking.setParkingStructureKbn(drpDwnSelectedMap.get("parkingStructure").toString());
		// 駐車場補足ファイル名
		mShatakuParking.setParkingSupplementName1(fileName1);
		mShatakuParking.setParkingSupplementName2(fileName2);
		mShatakuParking.setParkingSupplementName3(fileName3);
		// 駐車王補足ファイルサイズ
		mShatakuParking.setParkingSupplementSize1(fileSize1);
		mShatakuParking.setParkingSupplementSize2(fileSize2);
		mShatakuParking.setParkingSupplementSize3(fileSize3);
		// 駐車場補足ファイル
		mShatakuParking.setParkingSupplementFile1(registDto.getParkingHosokuFile1());
		mShatakuParking.setParkingSupplementFile2(registDto.getParkingHosokuFile2());
		mShatakuParking.setParkingSupplementFile3(registDto.getParkingHosokuFile3());
		// 備考
		mShatakuParking.setParkingBiko(biko);
		// 駐車場基本使用料
		mShatakuParking.setParkingRental(Integer.parseInt(labelMap.get("parkingBasicRent").toString().replace(",", "")));
		// 更新日時
		mShatakuParking.setLastUpdateDate(registDto.getParkingUpdateDate());
	}

	/**
	 * 社宅駐車場区画情報マスタ更新データ作成
	 * 新規区画には駐車場管理番号に「0」を設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuParkingBlockList	*社宅駐車場区画情報マスタエンティティ
	 * @param parkingList				駐車場区画情報リスト
	 * @param registDto					DTO
	 * @throws ParseException 
	 */
	private void setUpdateColumParkingBlock(
			List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList, 
			List<Map<String, Object>> parkingList, Skf3010Sc002RegistDto registDto) throws ParseException {

		/** 社宅駐車場区画情報マスタの更新データ作成 */
		// 区画情報数分ループ
		for (Map<String, Object> parkingMap : parkingList) {
			Skf3010MShatakuParkingBlock mShatakuParkingBlock = new Skf3010MShatakuParkingBlock();
			// 駐車場管理番号
			Long parkingKanriNo = 0L;
			if (parkingMap.get("parkingKanriNo") != null
				&& parkingMap.get("parkingKanriNo").toString().length() > 0) {
				try {
					parkingKanriNo = Long.parseLong(parkingMap.get("parkingKanriNo").toString());
				} catch(NumberFormatException e) {}
			}
			mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
			// 区画番号
			mShatakuParkingBlock.setParkingBlock(parkingMap.get("parkingBlock").toString());
			// 貸与区分
			mShatakuParkingBlock.setParkingLendKbn(parkingMap.get("parkingLendKbn").toString());
			// 貸与状況
			mShatakuParkingBlock.setParkingLendJokyo(parkingMap.get("parkingLendStatus").toString());
			// 調整金額
			mShatakuParkingBlock.setParkingRentalAdjust(Integer.parseInt(parkingMap.get("parkingRentalAdjust").toString()));
			// 備考
			String biko = ("".equals(parkingMap.get("parkingBiko").toString())) ? null: parkingMap.get("parkingBiko").toString();
			mShatakuParkingBlock.setParkingBiko(biko);
			// 更新日時
			if (parkingMap.get("updateDate") != null && parkingMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuParkingBlock.setLastUpdateDate(dateFormat.parse(parkingMap.get("updateDate").toString()));
			}
			// リストに追加
			mShatakuParkingBlockList.add(mShatakuParkingBlock);
			mShatakuParkingBlock = null;
		}
	}

	/**
	 * 社宅管理者情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuManageList	*社宅管理者情報マスタエンティティ
	 * @param registDto				DTO
	 */
	private void setUpdateColumShatakuManage(
			List<Skf3010MShatakuManege> mShatakuManageList, Skf3010Sc002RegistDto registDto) {

		/** 社宅管理者情報マスタの更新データを作成 */
		String roomNo = null;
		String name = null;
		String mailAddress = null;
		String telNumber = null;
		String extensionNo = null;
		String biko = null;

		/** 寮長・自治会長 */
		Skf3010MShatakuManege domitoryLeaderMap = new Skf3010MShatakuManege();
		// 管理者区分
		domitoryLeaderMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_DOMITRY_LEADER);
		// 部屋番号：寮長・自治会長
		roomNo = ("".equals(registDto.getDormitoryLeaderRoomNo())) ? null: registDto.getDormitoryLeaderRoomNo();
		domitoryLeaderMap.setManegeShatakuNo(roomNo);
		// 氏名：寮長・自治会長
		name = ("".equals(registDto.getDormitoryLeaderName())) ? null: registDto.getDormitoryLeaderName();
		domitoryLeaderMap.setManegeName(name);
		// 電子メールアドレス：寮長・自治会長
		mailAddress = ("".equals(registDto.getDormitoryLeaderMailAddress())) ? null: registDto.getDormitoryLeaderMailAddress();
		domitoryLeaderMap.setManegeMailAddress(mailAddress);
		// 電話番号：寮長・自治会長
		telNumber = ("".equals(registDto.getDormitoryLeaderTelNumber())) ? null: registDto.getDormitoryLeaderTelNumber();
		domitoryLeaderMap.setManegeTelNo(telNumber);
		// 内線番号：寮長・自治会長
		extensionNo = ("".equals(registDto.getDormitoryLeaderExtentionNo())) ? null: registDto.getDormitoryLeaderExtentionNo();
		domitoryLeaderMap.setManegeExtensionNo(extensionNo);
		// 備考：寮長・自治会長
		biko = ("".equals(registDto.getDormitoryLeaderBiko())) ? null: registDto.getDormitoryLeaderBiko();
		domitoryLeaderMap.setBiko(biko);
		// 更新日時
		domitoryLeaderMap.setLastUpdateDate(registDto.getDormitoryLeaderUpdateDate());

		/** 鍵管理者 */
		Skf3010MShatakuManege keyMngMap = new Skf3010MShatakuManege();
		// 管理者区分
		keyMngMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_KEY_MANAGER);
		// 部屋番号 ：鍵管理者
		roomNo = ("".equals(registDto.getKeyManagerRoomNo())) ? null: registDto.getKeyManagerRoomNo();
		keyMngMap.setManegeShatakuNo(roomNo);
		// 氏名：鍵管理者
		name = ("".equals(registDto.getKeyManagerName())) ? null: registDto.getKeyManagerName();
		keyMngMap.setManegeName(name);
		// 電子メールアドレス：鍵管理者
		mailAddress = ("".equals(registDto.getKeyManagerMailAddress())) ? null: registDto.getKeyManagerMailAddress();
		keyMngMap.setManegeMailAddress(mailAddress);
		// 電話番号：鍵管理者
		telNumber = ("".equals(registDto.getKeyManagerTelNumber())) ? null: registDto.getKeyManagerTelNumber();
		keyMngMap.setManegeTelNo(telNumber);
		// 内線番号：鍵管理者
		extensionNo = ("".equals(registDto.getKeyManagerExtentionNo())) ? null: registDto.getKeyManagerExtentionNo();
		keyMngMap.setManegeExtensionNo(extensionNo);
		// 備考：鍵管理者
		biko = ("".equals(registDto.getKeyManagerBiko())) ? null: registDto.getKeyManagerBiko();
		keyMngMap.setBiko(biko);
		// 更新日時
		keyMngMap.setLastUpdateDate(registDto.getKeyManagerUpdateDate());

		/** 寮母・管理会社 */
		Skf3010MShatakuManege matronMap = new Skf3010MShatakuManege();
		// 管理者区分
		matronMap.setManegeKbn(Skf3010Sc002CommonDto.MANAGE_KBN_MATRON);
		// 部屋番号 ：鍵管理者
		roomNo = ("".equals(registDto.getMatronRoomNo())) ? null: registDto.getMatronRoomNo();
		matronMap.setManegeShatakuNo(roomNo);
		// 氏名：鍵管理者
		name = ("".equals(registDto.getMatronName())) ? null: registDto.getMatronName();
		matronMap.setManegeName(name);
		// 電子メールアドレス：鍵管理者
		mailAddress = ("".equals(registDto.getMatronMailAddress())) ? null: registDto.getMatronMailAddress();
		matronMap.setManegeMailAddress(mailAddress);
		// 電話番号：鍵管理者
		telNumber = ("".equals(registDto.getMatronTelNumber())) ? null: registDto.getMatronTelNumber();
		matronMap.setManegeTelNo(telNumber);
		// 内線番号：鍵管理者
		extensionNo = ("".equals(registDto.getMatronExtentionNo())) ? null: registDto.getMatronExtentionNo();
		matronMap.setManegeExtensionNo(extensionNo);
		// 備考：鍵管理者
		biko = ("".equals(registDto.getMatronBiko())) ? null: registDto.getMatronBiko();
		matronMap.setBiko(biko);
		// 更新日時
		matronMap.setLastUpdateDate(registDto.getMatronUpdateDate());
		// リストに追加
		mShatakuManageList.add(domitoryLeaderMap);
		mShatakuManageList.add(keyMngMap);
		mShatakuManageList.add(matronMap);
		domitoryLeaderMap = null;
		keyMngMap = null;
		matronMap = null;
	}

	/**
	 * 社宅備品情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuBihinList	*社宅備品情報マスタエンティティ
	 * @param bihinList			備品リスト
	 * @param registDto			DTO
	 * @throws ParseException 
	 */
	private void setUpdateColumBihin(
			List<Skf3010MShatakuBihin> mShatakuBihinList, 
			List<Map<String, Object>> bihinList, Skf3010Sc002RegistDto registDto) throws ParseException {

		/** 社宅備品情報マスタの更新データを作成 */
		// 備品情報数分ループ
		for (Map<String, Object> bihinMap : bihinList) {
			Skf3010MShatakuBihin mShatakuBihin = new Skf3010MShatakuBihin();
			// 備品コード
			mShatakuBihin.setBihinCd(bihinMap.get("bihinCd").toString());
			// 備付区分
			mShatakuBihin.setBihinStatusKbn(bihinMap.get("bihinStatusKbn").toString());
			// 更新日時
			if (bihinMap.get("updateDate") != null && bihinMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuBihin.setLastUpdateDate(dateFormat.parse(bihinMap.get("updateDate").toString()));
			}
			mShatakuBihinList.add(mShatakuBihin);
		}
		// 非表示備品数分ループ
		List<Map<String, Object>> noDispBihin = registDto.getHdnBihinInfoListTableData();
		for (Map<String, Object> bihinMap : noDispBihin) {
			Skf3010MShatakuBihin mShatakuBihin = new Skf3010MShatakuBihin();
			// 備品コード
			mShatakuBihin.setBihinCd(bihinMap.get("bihinCd").toString());
			// 備付区分
			mShatakuBihin.setBihinStatusKbn(bihinMap.get("bihinStatusKbn").toString());
			// 更新日時
			if (bihinMap.get("updateDate") != null && bihinMap.get("updateDate").toString().length() > 0) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
				mShatakuBihin.setLastUpdateDate(dateFormat.parse(bihinMap.get("updateDate").toString()));
			}
			mShatakuBihinList.add(mShatakuBihin);
		}
	}

	/**
	 * 社宅契約情報マスタ更新データ作成
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param mShatakuContract	*契約情報マスタエンティティ
	 * @param registDto			DTO
	 */
	private void setUpdateColumContract(Skf3010MShatakuContract mShatakuContract,
													Skf3010Sc002RegistDto registDto) {

		/** 社宅契約情報マスタの更新データを作成 */
		// 経理連携用管理番号設定判定
		if (registDto.getAssetRegisterNo() == null || registDto.getAssetRegisterNo().length() <= 0) {
			// 経理連携用管理番号無し＝契約情報なしのため処理なし
			return;
		}
		// 賃貸人番号
		mShatakuContract.setOwnerNo(Long.parseLong(registDto.getContractOwnerNo()));
		// 経理連携用管理番号
		mShatakuContract.setAssetRegisterNo(registDto.getAssetRegisterNo());
		// 契約開始日
		mShatakuContract.setContractStartDate(registDto.getContractStartDay().replace("/", ""));
		// 契約終了日
		mShatakuContract.setContractEndDate(registDto.getContractEndDay().replace("/", ""));
		// 家賃
		mShatakuContract.setRent(Integer.parseInt(registDto.getContractRent().replace(",", "")));
		// 共益費
		mShatakuContract.setKyoekihi(Integer.parseInt(registDto.getContractKyoekihi().replace(",", "")));
		// 駐車場料(地代)
		mShatakuContract.setLandRent(Integer.parseInt(registDto.getContractLandRent().replace(",", "")));
		// 備考
		mShatakuContract.setBiko(registDto.getContractBiko());
		// 更新日時
		mShatakuContract.setLastUpdateDate(registDto.getContractUpdateDate());
	}

	/**
	 * 社宅存在チェック
	 * 同名、同住所の社宅存在チェックを実施する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param shatakuName		社宅名
	 * @param pref				都道府県コード
	 * @param shatakuAddress	社宅住所
	 * @param registDto			*DTO
	 * @return	true:社宅無し、false:社宅有り
	 */
	private Boolean checkSameShataku(String shatakuName, String pref, String shatakuAddress,
															Skf3010Sc002RegistDto registDto) {
		// 社宅件数取得
		int shatakuCnt = skf3010Sc002SharedService.getShatakuCountFromNameAndAddress(
				registDto.getShatakuName(), registDto.getPref(), registDto.getShatakuAddress());
		if (shatakuCnt > 0) {
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3050);
			registDto.setShatakuNameErr(CodeConstant.NFW_VALIDATION_ERROR);
			LogUtils.debugByMsg("社宅重複チェックエラー：同名、同住所の社宅が既に存在する");
			return false;
		}
		return true;
	}

	/**
	 * 元の画面に戻す
	 * エラー検出コントロールは背景をエラー背景に設定する
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param drpDwnSelectedList	ドロップダウン選択値リスト
	 * @param labelList				可変ラベル値リスト
	 * @param bihinList				備品リスト
	 * @param parkingList			区画駐車場リスト
	 * @param registDto				*DTO
	 */
	private void setBeforeInfo(
			List<Map<String, Object>> drpDwnSelectedList,
			List<Map<String, Object>> labelList,
			List<Map<String, Object>> bihinList,
			List<Map<String, Object>> parkingList,
			Skf3010Sc002RegistDto registDto) {

		// ドロップダウンリスト復元
		setErrResultDrpDwn(drpDwnSelectedList, registDto);
		// 可変ラベル値復元、駐車場基本使用料取得
		String parkingRent = setErrVariableLabel(labelList, registDto);
		// 駐車場区画情報復元
		setErrParkingBlock(parkingRent, parkingList, registDto);
		// 備品情報復元
		setErrBihin(bihinList, registDto);
	}

	/**
	 * エラー時備品情報復元
	 * エラー時の備品情報を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param bihinList	備品情報リスト
	 * @param registDto	*DTO
	 */
	private void setErrBihin(List<Map<String, Object>> bihinList, Skf3010Sc002RegistDto registDto) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		List<Map<String, Object>> statusList = ddlUtils.getGenericForDoropDownList(
							FunctionIdConstant.GENERIC_CODE_BIHINSTATUS_KBN, "",false);

		int i = 0;
		for (Map <String, Object> tmpData : bihinList) {
			/** DTO設定用 */
			// 備品コード
			String bihinCode = HtmlUtils.htmlEscape(tmpData.get("bihinCd").toString());
			// 備品名
			String bihinName = HtmlUtils.htmlEscape(tmpData.get("bihinName").toString());
			// 備付状況選択値
			String bihinStatus = tmpData.get("bihinStatusKbn").toString();

			Map<String, Object> tmpMap = new HashMap<String, Object>();

			// 備品コード
			tmpMap.put("bihinCd", bihinCode);
			// 備品名
			tmpMap.put("bihinName", bihinName);
			// 備品備付状況
			String statusListCode = skf3010Sc002SharedService.createStatusSelect(bihinStatus, statusList);
			tmpMap.put("bihinStatusKbn","<select id='bihinStatus" + i 
						+ "' name='bihinStatus" + i + "'>" + statusListCode + "</select>");
			// 更新日時
			tmpMap.put("updateDate", HtmlUtils.htmlEscape(tmpData.get("updateDate").toString()));
			setViewList.add(tmpMap);
			i++;
		}
		// 解放
		statusList = null;

		registDto.setBihinInfoListTableData(null);
		registDto.setBihinInfoListTableData(setViewList);
	}

	/**
	 * エラー時駐車場区画情報復元
	 * エラー時の駐車場区画情報を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param parkingRent	駐車場月額使用料(基本)
	 * @param parkingList	駐車場区画情報リスト
	 * @param registDto		*DTO
	 */
	private void setErrParkingBlock(String parkingRent,
				List<Map<String, Object>> parkingList, Skf3010Sc002RegistDto registDto) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();
		// 貸与区分ドロップダウン
		List<Map<String, Object>> lendStatusList =
				ddlUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_LEND_KBN, "",false);
		// 駐車場月額使用料
		Long parkingRentMany = 0L;
		if (parkingRent != null && parkingRent.length() > 0) {
			parkingRentMany = Long.parseLong(parkingRent.replace(",", ""));
		}

		for (Map<String, Object> tmpData : parkingList) {
			// RelativeID
			String rId = tmpData.get("rId").toString();
			// 駐車場管理番号
			String parkingKanriNo =
					(tmpData.get("parkingKanriNo") != null) ? tmpData.get("parkingKanriNo").toString() : "";
			// 区画番号
			String blockNo =
					(tmpData.get("parkingBlock") != null) ? tmpData.get("parkingBlock").toString() : "";
			// 区画貸与区分
			String parkingLendKbn = tmpData.get("parkingLendKbn").toString();
			// 区画貸与状況
			String parkingLendStatus = tmpData.get("parkingLendStatus").toString();
			// 使用者
			String shainName = (tmpData.get("shainName") != null) ? tmpData.get("shainName").toString() : "";
			// 駐車場調整金額
			String parkingRentalAdjust = tmpData.get("parkingRentalAdjust").toString();
			// 区画駐車場月額使用料
			Long  parkingBlockRentMany = parkingRentMany;
			// 備考
			String biko = (tmpData.get("parkingBiko") != null) ? tmpData.get("parkingBiko").toString() : "";

			/** エスケープ設定 */
			// 区画番号
			blockNo = HtmlUtils.htmlEscape(blockNo);
			// 使用者
			shainName = HtmlUtils.htmlEscape(shainName);
			// 駐車場調整金額
			parkingRentalAdjust = HtmlUtils.htmlEscape(parkingRentalAdjust);
			// 備考
			biko = HtmlUtils.htmlEscape(biko);

			Map<String, Object> tmpMap = new HashMap<String, Object>();
			// RelativeID
			tmpMap.put("rId", rId);
			// 区画管理番号
			tmpMap.put("parkingKanriNo", HtmlUtils.htmlEscape(parkingKanriNo));
			// 区画番号エラー表示判定
			if (tmpData.containsKey("parkingBlockNoErr")) {
				// エラー表示設定
				tmpMap.put("parkingBlock", "<input id='parkingBlockNo" + rId + "' name='parkingBlockNo" + rId 
								+ "' placeholder='例　01（半角）' type='text' value='"
								+ blockNo + "' class='nfw-validation-error' style='width:140px;' maxlength='30'/>");
			} else {
				tmpMap.put("parkingBlock", "<input id='parkingBlockNo" + rId + "' name='parkingBlockNo" + rId 
								+ "' placeholder='例　01（半角）' type='text' value='"
								+ blockNo + "' style='width:140px;' maxlength='30'/>");
			}
			// 「貸与区分」プルダウンの設定
			String lendStatusListCode = 
					skf3010Sc002SharedService.createStatusSelect(parkingLendKbn,lendStatusList);
			// 貸与区分ステータス判定
			if ("1".equals(parkingLendKbn)) {
				// 貸与可能(アクティブ)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;'>"
												+ lendStatusListCode + "</select>");
			} else if (parkingKanriNo.length() > 0){
				// 貸与不可(非活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;' disabled>"
												+ lendStatusListCode + "</select>");
			} else {
				// 貸与不可(活性)
				tmpMap.put("parkingLendKbn", "<select id='parkingLendKbn" + rId 
						+ "' name='parkingLendKbn" + rId + "' style='width:90px;' >"
												+ lendStatusListCode + "</select>");
			}
			// 貸与状況
			tmpMap.put("parkingLendStatus", HtmlUtils.htmlEscape(parkingLendStatus));
			// 使用者
			tmpMap.put("shainName", shainName);
			// 駐車場調整金額エラー表示判定
			if (tmpData.containsKey("parkingRentalAdjustErr")) {
				// エラー表示設定
				tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + rId 
					+ "' name='parkingRentalAdjust" + rId + "' type='text' class='ime-off nfw-validation-error' value='"
					+ parkingRentalAdjust + "' placeholder='例　半角数字' style='width:75px;text-align: right;' maxlength='6'/> 円");
			} else {
				tmpMap.put("parkingRentalAdjust", "<input id='parkingRentalAdjust" + rId 
						+ "' name='parkingRentalAdjust" + rId + "' type='text' class='ime-off' value='"
						+ parkingRentalAdjust + "' placeholder='例　半角数字' style='width:75px;text-align: right;' maxlength='6'/> 円");
			}
			// 駐車場月額使用料
			if (CheckUtils.isNumeric(parkingRentalAdjust) && parkingRentalAdjust.length() > 0) {
				parkingBlockRentMany += Long.parseLong(parkingRentalAdjust);
			}
			tmpMap.put("parkingMonthRental", "<label id='parkingMonthRental" + rId
					+ "' name='parkingMonthRental" + rId + "' >"
					+ HtmlUtils.htmlEscape(String.format("%,d", parkingBlockRentMany) + " 円")
					+ "</label>");
			// 駐車場備考
			tmpMap.put("parkingBiko", "<input id='parkingBlockBiko" + rId 
					+ "' name='parkingBlockBiko" + rId + "' type='text' value='"
					+ biko + " ' style='width:215px;' maxlength='100'/>");
			// 削除ボタン
			tmpMap.put("parkingBlockDelete", "");
			// 更新日時
			tmpMap.put("updateDate", HtmlUtils.htmlEscape(tmpData.get("updateDate").toString()));
			setViewList.add(tmpMap);
		}
		// 解放
		lendStatusList = null;

		// 駐車場区画情報設定
		registDto.setParkingInfoListTableData(null);
		registDto.setParkingInfoListTableData(setViewList);
	}

	/**
	 * エラー時可変ラベル値復元
	 * エラー時の可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param registDto		*DTO
	 */
	private String setErrVariableLabel(List<Map<String, Object>> labelList, Skf3010Sc002RegistDto registDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
		// 実年数
		String jituAge =
				(labelMap.get("realYearCount") != null) ? labelMap.get("realYearCount").toString() : "0";
		// 次回算定年月日
		String nextCalcDate =
				(labelMap.get("nextCalculateDate") != null) ? labelMap.get("nextCalculateDate").toString() : "";
		// 経年
		String aging =
				(labelMap.get("aging") != null) ? labelMap.get("aging").toString() : "0";
		// 駐車場基本使用料
		String parkingRent =
				(labelMap.get("parkingBasicRent") != null) ? labelMap.get("parkingBasicRent").toString() : "0";
		// 駐車場台数
		String parkingBlockCount =
				(labelMap.get("parkingBlockCount") != null) ? labelMap.get("parkingBlockCount").toString() : "0";
		// 貸与可能総数
		String lendPossibleCount =
				(labelMap.get("lendPossibleCount") != null) ? labelMap.get("lendPossibleCount").toString() : "0";

		/** 戻り値設定 */
		// クリア
		registDto.setJituAge(null);
		registDto.setNextCalcDate(null);
		registDto.setAging(null);
		registDto.setParkingRent(null);
		registDto.setParkingBlockCount(null);
		registDto.setLendPossibleCount(null);
		// 設定
		registDto.setJituAge(jituAge);
		registDto.setNextCalcDate(nextCalcDate);
		registDto.setAging(aging);
		registDto.setParkingRent(parkingRent);
		registDto.setParkingBlockCount(parkingBlockCount);
		registDto.setLendPossibleCount(lendPossibleCount);

		return parkingRent;
	}


	/**
	 * 入力チェック。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param parkingList			*駐車場区画情報リスト
	 * @param drpDwnSelectedList	プルダウン選択値リスト
	 * @param registDto				*DTO
	 * @return
	 */
	private Boolean checkTouroku(List<Map<String, Object>> parkingList,
			List<Map<String, Object>> drpDwnSelectedList, Skf3010Sc002RegistDto registDto) {
		boolean isCheckOk = true;

		String debugMessage = "";
		Map <String, Object> drpDwnSelectedMap = (drpDwnSelectedList.size() > 0) ? drpDwnSelectedList.get(0) : null;

		/** 必須入力チェック */
		// 社宅名
		if (SkfCheckUtils.isNullOrEmpty(registDto.getShatakuName())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "社宅名");
			registDto.setShatakuNameErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅名";
		}
		// 地域区分
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("areaKbn") == null
				|| CheckUtils.isEmpty(drpDwnSelectedMap.get("areaKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "地域区分");
			registDto.setAreaKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 地域区分";
		}
		// 社宅区分
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("shatakuKbn") == null 
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("shatakuKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "社宅区分");
			registDto.setShatakuKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅区分";
		}
		// 利用区分
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("useKbn") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("useKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "利用区分");
			registDto.setUseKbnKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 利用区分";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理会社
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("manageCompany") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageCompany").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理会社");
			registDto.setManageCompanyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理会社";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理機関
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("manageAgency") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageAgency").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理機関");
			registDto.setManageAgencyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理機関";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理事業領域
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("manageBusinessArea") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageBusinessArea").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理事業領域");
			registDto.setManageBusinessAreaErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理事業領域";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 社宅所在地
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("pref") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("pref").toString())
				|| SkfCheckUtils.isNullOrEmpty(registDto.getShatakuAddress())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "所在地");
			registDto.setPrefErr(CodeConstant.NFW_VALIDATION_ERROR);
			registDto.setShatakuAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅所在地";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 構造
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("shatakuStructure") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("shatakuStructure").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "構造");
			registDto.setShatakuStructureErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅構造";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 建築年月日の必須チェック
		if (SkfCheckUtils.isNullOrEmpty(registDto.getBuildDate())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "建築年月日");
			registDto.setBuildDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 建築年月日";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 駐車場構造
		if (drpDwnSelectedMap == null || drpDwnSelectedMap.get("parkingStructure") == null
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("parkingStructure").toString())) {
			// 駐車場構造が未選択
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場構造");
			registDto.setParkingStructureErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 駐車場構造";
			// 選択タブインデックス設定：駐車場情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
		} else if (!PARKING_STRUCTURE_NASHI.equals(drpDwnSelectedMap.get("parkingStructure"))) {
			// 駐車場構造が「なし」以外
			// 区画情報必須チェック
			for (Map<String, Object> parkingMap : parkingList) {
				// 区画番号
				String parkingBlockNo = (parkingMap.get("parkingBlock") != null) ?
									parkingMap.get("parkingBlock").toString().trim().replace("　", "") : "";
				// 区画番号入力値判定
				if (parkingBlockNo.length() > 0) {
					// 入力されている為、次の区画情報へ
					continue;
				}
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "区画番号");
				// 区画番号エラーを追加
				parkingMap.put("parkingBlockNoErr", "");
				debugMessage += " 必須入力チェック - 区画番号";
				// 選択タブインデックス設定：駐車場情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
			}
		} else {
			// 駐車場構造が「なし」
			if (parkingList.size() > 0) {
				// 構造「なし」に対して区画登録ありのためエラーとする
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_3043);
				registDto.setParkingStructureErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 構造なし、区画あり";
				// 選択タブインデックス設定：駐車場情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
			}
		}
		// 契約情報
		if ((drpDwnSelectedMap != null && drpDwnSelectedMap.get("contractNo") != null) 
				&& !SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("contractNo").toString())) {
			// 契約情報が存在する場合
			// 賃貸人
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractOwnerName())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "賃貸人（代理人）");
				registDto.setContractOwnerNameErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 賃貸人（代理人）";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 経理連携用管理番号
			if (SkfCheckUtils.isNullOrEmpty(registDto.getAssetRegisterNo())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "経理連携用管理番号");
				registDto.setAssetRegisterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 経理連携用管理番号";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 契約開始日
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractStartDay())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約開始日");
				registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 契約開始日";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 家賃
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractRent())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "家賃");
				registDto.setContractRentErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 家賃";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 共益費
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractKyoekihi())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "共益費");
				registDto.setContractKyoekihiErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 共益費";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 駐車場料(地代)
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractLandRent())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場料(地代)");
				registDto.setContractLandRentErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 駐車場料(地代)";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
		}
		// 必須チェック結果判定
		if (!isCheckOk) {
			LogUtils.debugByMsg("必須入力チェックエラー：" + debugMessage);
			return isCheckOk;
		}
		/** 形式チェック */
		// 郵便番号
		String zipCode = (registDto.getZipCd() != null) ? registDto.getZipCd() : "";
		if(!CheckUtils.isNumeric(zipCode) && zipCode.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "郵便番号");
			registDto.setZipCdErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 郵便番号";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 駐車場調整金額
		for (Map<String, Object> parkingMap : parkingList) {
			// 駐車場調整金額取得
			String parkingRentalAdjust = (parkingMap.get("parkingRentalAdjust") != null) ?
								parkingMap.get("parkingRentalAdjust").toString().trim().replace("　", "") : "";
			// 入力値判定
			if (CheckUtils.isNumeric(parkingRentalAdjust) && parkingRentalAdjust.length() > 0) {
				// 数値の為、次の区画情報へ
				continue;
			}
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "駐車場調整金額");
			// 駐車場調整金額エラーを追加
			parkingMap.put("parkingRentalAdjustErr", "");
			debugMessage += " 形式チェック - 駐車場調整金額";
			// 選択タブインデックス設定：駐車場情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
		}
		// 寮長・自治会長 メールアドレス
		String mailAddress = (registDto.getDormitoryLeaderMailAddress() != null) ? registDto.getDormitoryLeaderMailAddress() : "";
		if(!mailAddress.matches(MAIL_ADDRESS_CHECK_REG) && mailAddress.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮長・自治会長 メールアドレス");
			registDto.setDormitoryLeaderMailAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮長・自治会長 メールアドレス";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 鍵管理者 メールアドレス
		mailAddress = (registDto.getKeyManagerMailAddress() != null) ? registDto.getKeyManagerMailAddress() : "";
		if(!mailAddress.matches(MAIL_ADDRESS_CHECK_REG) && mailAddress.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "鍵管理者 メールアドレス");
			registDto.setKeyManagerMailAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 鍵管理者 メールアドレス";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 寮母・管理会社 メールアドレス
		mailAddress = (registDto.getMatronMailAddress() != null) ? registDto.getMatronMailAddress() : "";
		if(!mailAddress.matches(MAIL_ADDRESS_CHECK_REG) && mailAddress.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮母・管理会社 メールアドレス");
			registDto.setMatronMailAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮母・管理会社 メールアドレス";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 寮長・自治会長 電話番号
		String telNo = (registDto.getDormitoryLeaderTelNumber() != null) ? registDto.getDormitoryLeaderTelNumber() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮長・自治会長 電話番号");
			registDto.setDormitoryLeaderTelNumberErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮長・自治会長 電話番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 鍵管理者 電話番号
		telNo = (registDto.getKeyManagerTelNumber() != null) ? registDto.getKeyManagerTelNumber() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "鍵管理者 電話番号");
			registDto.setKeyManagerTelNumberErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 鍵管理者 電話番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 寮母・管理会社 電話番号
		telNo = (registDto.getMatronTelNumber() != null) ? registDto.getMatronTelNumber() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮母・管理会社 電話番号");
			registDto.setMatronTelNumberErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮母・管理会社 電話番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 寮長・自治会長 内線番号
		telNo = (registDto.getDormitoryLeaderExtentionNo() != null) ? registDto.getDormitoryLeaderExtentionNo() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮長・自治会長 内線番号");
			registDto.setDormitoryLeaderExtentionNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮長・自治会長 内線番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 鍵管理者 内線番号
		telNo = (registDto.getKeyManagerExtentionNo() != null) ? registDto.getKeyManagerExtentionNo() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "鍵管理者 内線番号");
			registDto.setKeyManagerExtentionNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 鍵管理者 内線番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 寮母・管理会社 内線番号
		telNo = (registDto.getMatronExtentionNo() != null) ? registDto.getMatronExtentionNo() : "";
		if(!telNo.matches(MANAGE_TELNO_CHECK_REG) && telNo.length() > 0){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "寮母・管理会社 内線番号");
			registDto.setMatronExtentionNoErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 寮母・管理会社 内線番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 契約終了日
		if (!SkfCheckUtils.isNullOrEmpty(registDto.getContractEndDay())) {
			//日付関係
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			Date startDate = null;
			Date endDate = null;
			try{
				startDate = dateFormat.parse(registDto.getContractStartDay().replace("/", ""));
				endDate = dateFormat.parse(registDto.getContractEndDay().replace("/", ""));

				if(!startDate.before(endDate)){
					//契約開始日＞契約終了日の場合(開始日が終了日より前で無い場合）
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
					registDto.setContractEndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約終了日(開始日以前) - " + registDto.getContractEndDay();
				}
			}catch(ParseException ex){
				isCheckOk = false;
				if(startDate == null){
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
					registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約開始日 - " + registDto.getContractStartDay();
				}
				if(endDate == null){
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
					registDto.setContractEndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約終了日 - " + registDto.getContractEndDay();
				}
				
			}
		}
		// 形式チェック結果判定
		if (!isCheckOk) {
			LogUtils.debugByMsg("形式チェックエラー：" + debugMessage);
			return isCheckOk;
		}
		// 添付ファイルサイズチェック
		isCheckOk = checkAttachedFile(registDto);
		// デバッグメッセージ出力
		if (isCheckOk) {
			LogUtils.debugByMsg("入力チェックOK：" + debugMessage);
		} else {
			LogUtils.debugByMsg("入力チェックエラー：" + debugMessage);
		}

		return isCheckOk;
	}

	/**
	 * 表示タブインデックス設定
	 * パラメータの指定タブインデックスが設定済みタブインデックスよりも小さい場合のみ、
	 * パラメータの指定タブインデックスを次回表示タブインデックスに設定する
	 * 
	 * @param nextIndex
	 * @param registDto
	 */
	private void setDisplayTabIndex(String nextIndex, Skf3010Sc002RegistDto registDto) {
		int targetTabIndex = 999;
		try {
			targetTabIndex = Integer.parseInt(registDto.getHdnNowSelectTabIndex()); 
			// 設定タブインデックスが、次回表示タブインデックスよりも小さい場合、次回表示タブインデックスを更新する。
			if (targetTabIndex > Integer.parseInt(nextIndex)) {
				registDto.setHdnNowSelectTabIndex(nextIndex);
			}
		} catch (NumberFormatException e) {}
	}

	/**
	 * 添付ファイルサイズチェック
	 * 社宅補足1～3ファイル、駐車場補足1～3のファイルの合計が
	 * 最大値を超過する場合はfalseを返却する
	 * 
	 * @param registDto
	 * @return
	 */
	private Boolean checkAttachedFile(Skf3010Sc002RegistDto registDto) {
		Long sumFileSize = 0L;

		try {
			sumFileSize += Long.parseLong(registDto.getShatakuHosokuSize1());
		} catch(NumberFormatException e) {}
		try {
			sumFileSize += Long.parseLong(registDto.getShatakuHosokuSize2());
		} catch(NumberFormatException e) {}
		try {
			sumFileSize += Long.parseLong(registDto.getShatakuHosokuSize3());
		} catch(NumberFormatException e) {}
		try {
			sumFileSize += Long.parseLong(registDto.getParkingHosokuSize1());
		} catch(NumberFormatException e) {}
		try {
			sumFileSize += Long.parseLong(registDto.getParkingHosokuSize2());
		} catch(NumberFormatException e) {}
		try {
			sumFileSize += Long.parseLong(registDto.getParkingHosokuSize3());
		} catch(NumberFormatException e) {}

		if (TEMP_FILE_MAX_SIZE < sumFileSize) {
			return false;
		}

		return true;
	}

	/**
	 * エラー時ドロップダウンリスト設定処理
	 * ドロップダウンリストを作成し、エラー時の選択値を設定状態に設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param drpDwnSelectedList	エラー時ドロップダウン選択値リスト
	 * @param registDto				*DTO
	 */
	private void setErrResultDrpDwn(
			List<Map<String, Object>> drpDwnSelectedList,
			Skf3010Sc002RegistDto registDto) {

		/** DTO設定用 */
		// ドロップダウンリスト
		// 地域区分リスト
		List<Map<String, Object>> areaKbnList = new ArrayList<Map<String, Object>>();
		// 社宅区分リスト
		List<Map<String, Object>> shatakuKbnList = new ArrayList<Map<String, Object>>();
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
		// 駐車場構造リスト
		List<Map<String, Object>> parkingStructureList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリスト選択値
		Map <String, Object> drpDwnMap = drpDwnSelectedList.get(0);
		// 地域区分
		String areaKbn = (drpDwnMap.get("areaKbn") != null) ? drpDwnMap.get("areaKbn").toString() : "";
		// 社宅区分
		String shatakuKbn = (drpDwnMap.get("shatakuKbn") != null) ? drpDwnMap.get("shatakuKbn").toString() : "";
		// 利用区分
		String useKbn = (drpDwnMap.get("useKbn") != null) ? drpDwnMap.get("useKbn").toString() : "";
		// 管理会社
		String manageCompany = (drpDwnMap.get("manageCompany") != null) ? drpDwnMap.get("manageCompany").toString() : "";
		// 管理機関
		String manageAgency = (drpDwnMap.get("manageAgency") != null) ? drpDwnMap.get("manageAgency").toString() : "";
		// 管理事業領域
		String manageBusinessArea = (drpDwnMap.get("manageBusinessArea") != null) ? drpDwnMap.get("manageBusinessArea").toString() : "";
		// 都道府県
		String pref = (drpDwnMap.get("pref") != null) ? drpDwnMap.get("pref").toString() : "";
		// 社宅構造
		String shatakuStructure = (drpDwnMap.get("shatakuStructure") != null) ? drpDwnMap.get("shatakuStructure").toString() : "";
		// エレベーター
		String elevator = (drpDwnMap.get("elevator") != null) ? drpDwnMap.get("elevator").toString() : "";
		// 駐車場構造
		String parkingStructure = (drpDwnMap.get("parkingStructure") != null) ? drpDwnMap.get("parkingStructure").toString() : "";

		/** ドロップダウンリスト作成 */
		// 「地域区分」
		areaKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AREA_KBN, areaKbn, true));
		// 「社宅区分」
		shatakuKbnList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_SHATAKU_KBN, shatakuKbn, true));
		// 社宅区分ドロップダウンより借上削除
		skf3010Sc002SharedService.deleteShatakuKbnDrpDwKariage(registDto);
		// 管理系ドロップダウン取得
		skf3010Sc002SharedService.getDoropDownList(
				useKbn, useKbnList,
				manageCompany, manageCompanyList,
				manageAgency, manageAgencyList,
				manageBusinessArea, manageBusinessAreaList,
				pref, prefList,
				shatakuStructure, shatakuStructureList,
				elevator, elevatorList);
		// 「駐車場構造区分」
		parkingStructureList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PARKING_STRUCTURE_KBN, parkingStructure, true));
		/** 戻り値設定 */
		// クリア
		registDto.setAreaKbnList(null);
		registDto.setShatakuKbnList(null);
		registDto.setUseKbnList(null);
		registDto.setManageCompanyList(null);
		registDto.setManageAgencyList(null);
		registDto.setManageBusinessAreaList(null);
		registDto.setPrefList(null);
		registDto.setShatakuStructureList(null);
		registDto.setElevatorList(null);
		registDto.setParkingStructureList(null);
		// 設定
		registDto.setAreaKbnList(areaKbnList);
		registDto.setShatakuKbnList(shatakuKbnList);
		registDto.setUseKbnList(useKbnList);
		registDto.setManageCompanyList(manageCompanyList);
		registDto.setManageAgencyList(manageAgencyList);
		registDto.setManageBusinessAreaList(manageBusinessAreaList);
		registDto.setPrefList(prefList);
		registDto.setShatakuStructureList(shatakuStructureList);
		registDto.setElevatorList(elevatorList);
		registDto.setParkingStructureList(parkingStructureList);
	}
}
