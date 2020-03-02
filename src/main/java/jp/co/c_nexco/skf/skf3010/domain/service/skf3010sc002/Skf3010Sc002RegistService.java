/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuParkingTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManege;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManegeKey;
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
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002RegistDto;
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

	/** 定数 */
	// 駐車場契約形態：社宅と一括
	private static final String PARKING_CONTRACT_TYPE = "1";
	// 駐車場所在地区分：社宅と同一所在地
	private static final String PARKING_ADDRESS = "0";
	// 駐車場構造区分：なし
	private static final String PARKING_STRUCTURE_NASHI = "5";
	// メールアドレスチェック正規表現
	private static final String MAIL_ADDRESS_CHECK_REG = "[!-~]+@[!-~]+\\.[!-~]+";
	// 管理者電話番号チェック正規表現
	private static final String MANAGE_TELNO_CHECK_REG = "^[0-9-]*$";
	// 添付ファイル上限サイズ(10M)
	private static final Long TEMP_FILE_MAX_SIZE = 10485760L;
	
	/**
	 * 保有社宅登録の「登録」ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	@Transactional
	public Skf3010Sc002RegistDto index(Skf3010Sc002RegistDto registDto) throws Exception {
		// デバッグログ
		LogUtils.debugByMsg("保有社宅情報登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("保有社宅登録", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);

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
			skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, registDto);
			return registDto;
		}
		// エラー発生時用に元の画面状態を設定
		skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, registDto);

		/** 更新用データ作成 */
		// 社宅基本
		skf3010Sc002SharedService.setUpdateColumShatakuKihon(mShataku, drpDwnSelectedList, labelList, registDto);
		// 社宅駐車場
		skf3010Sc002SharedService.setUpdateColumParking(mShatakuParking, drpDwnSelectedList, labelList, registDto);
		// 駐車場区画
		skf3010Sc002SharedService.setUpdateColumParkingBlock(mShatakuParkingBlockList, parkingList);
		// 社宅管理者
		skf3010Sc002SharedService.setUpdateColumShatakuManage(mShatakuManageList, registDto);
		// 社宅備品
		skf3010Sc002SharedService.setUpdateColumBihin(mShatakuBihinList, bihinList, registDto);
		// 契約情報
		skf3010Sc002SharedService.setUpdateColumContract(mShatakuContract, drpDwnSelectedList, registDto);
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
							mShatakuBihinList, mShatakuManageList, mShatakuContract, registDto.getPageId());
			// 更新結果判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("保有社宅新規登録失敗");
				// 件数が0未満（排他エラー） ※ロールバックさせるためエラーで表示する。
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			// 新規の為、空き部屋数を「0/0」に設定
			registDto.setEmptyRoomCount("0/0");
			
			// 成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
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
				LogUtils.debugByMsg("保有社宅更新失敗");
				// 件数が0未満（排他エラー） ※ロールバックさせるためエラーで表示する。
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			// 成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1011);
		}

		// 成功メッセージ
		//ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		// DTO設定
		registDto.setHdnShatakuKanriNo(mShataku.getShatakuKanriNo().toString());
		registDto.setHdnShatakuName(mShataku.getShatakuName());
		registDto.setHdnShatakuKbn(mShataku.getShatakuKbn());
		registDto.setHdnAreaKbn(mShataku.getAreaKbn());
		registDto.setHdnEmptyParkingCount(labelMap.get("lendPossibleCount").toString());
		// 初期表示
		// 契約番号
		String contractNo = "";
		if (mShatakuContract.getContractPropertyId() != null) {
			contractNo = mShatakuContract.getContractPropertyId().toString();
		}
		skf3010Sc002SharedService.setHoyuShatakuInfo(contractNo, registDto);
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
	 * @param registDto					DTO
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
		String userId = LoginUserInfoUtils.getUserCd();

		/** 社宅基本更新 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShataku.setUpdateUserId(userId);
		// プログラムID設定
		mShataku.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuTableDataExpRepository.updateShatakuKihon(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本更新失敗");
			return updateCnt;
		}
		/** 社宅駐車場更新 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShatakuParking.setUpdateUserId(userId);
		// プログラムID設定
		mShatakuParking.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuParkingTableDataExpRepository.updateShatakuParking(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場更新失敗");
			return updateCnt;
		}
		/** 駐車場区画更新 */
		// 更新前区画情報
		List<Map<String, Object>> startingParking = new ArrayList<Map<String, Object>>();
		if (registDto.getHdnStartingParkingInfoListTableData() != null) {
			startingParking.addAll(registDto.getHdnStartingParkingInfoListTableData());
		}
		// 区画情報削除
		// DB取得数分ループ
		for(Map<String, Object> parkingBlock : startingParking) {
			// 画面リストに存在フラグ
			Boolean existFlg = false;
			// DB取得駐車場管理番号取得
			Long dbKanriNo = Long.parseLong(parkingBlock.get("parkingKanriNo").toString());
			for (Skf3010MShatakuParkingBlock mShatakuParkingBlock : mShatakuParkingBlockList) {
				// DB取得管理番号と画面から取得の管理番号を比較
				if (Objects.equals(dbKanriNo, mShatakuParkingBlock.getParkingKanriNo())) {
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
//			// 一棟借上判定
//			if (registDto.getIttoFlg()) {
				// 区画契約情報も削除する
				List<Skf3010Sc002GetParkingBlockContractInfoTableDataExp> blockContractList =
							new ArrayList<Skf3010Sc002GetParkingBlockContractInfoTableDataExp>();
				Skf3010Sc002GetParkingBlockHistroyCountExpParameter param =
							new Skf3010Sc002GetParkingBlockHistroyCountExpParameter();
				param.setShatakuKanriNo(shatakuKanriNo);
				param.setParkingKanriNo(dbKanriNo);
				// 区画契約情報取得
				blockContractList.addAll(skf3010Sc002GetParkingBlockContractDataExpRepository.getParkingBlockContract(param));
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
//			}
			delParking = null;
		}
		// 次新規区画駐車場管理番号
		Long parkingKanriNo = skf3010Sc002SharedService.getNextParkingKanriNo(shatakuKanriNo.toString());
		// 区画情報登録更新ループ
		for (Skf3010MShatakuParkingBlock mShatakuParkingBlock : mShatakuParkingBlockList) {
			// 社宅管理番号設定
			mShatakuParkingBlock.setShatakuKanriNo(shatakuKanriNo);
			// 駐車場管理番号設定判定
			if (!Objects.equals(mShatakuParkingBlock.getParkingKanriNo(), 0L)) {
				// 既存データ更新
				updateCnt = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(mShatakuParkingBlock);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場区画更新失敗：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
			} else {
				// 新規区画
				// 駐車場管理番号設定
				mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
				updateCnt = skf3010MShatakuParkingBlockRepository.insertSelective(mShatakuParkingBlock);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場区画登録失敗：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
//				// 一棟借上判定
//				if (registDto.getIttoFlg()) {
					// 区画契約情報も登録する
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
						LogUtils.debugByMsg("駐車場契約情報登録失敗：" + mShatakuParkingBlock.getParkingBlock());
						return updateCnt;
					}
//				}
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
				LogUtils.debugByMsg("備品更新失敗：" + bihin.getBihinCd());
				return updateCnt;
			}
		}
		/** 社宅管理者更新 */
		for (Skf3010MShatakuManege manage : mShatakuManageList) {
			// 社宅管理番号設定
			manage.setShatakuKanriNo(shatakuKanriNo);
			
			//社宅管理者有無チェック
			Skf3010MShatakuManegeKey manageKey = new Skf3010MShatakuManegeKey();
			manageKey.setShatakuKanriNo(shatakuKanriNo);
			manageKey.setManegeKbn(manage.getManegeKbn());
			Skf3010MShatakuManege checkMange = null;
			checkMange = skf3010MShatakuManegeRepository.selectByPrimaryKey(manageKey);
			if(checkMange != null){
				//データ有の場合更新
				updateCnt = skf3010MShatakuManegeRepository.updateByPrimaryKeySelective(manage);
			}else{
				//データ無しの場合追加
				updateCnt = skf3010MShatakuManegeRepository.insertSelective(manage);
			}

			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅管理者更新失敗：" + manage.getManegeKbn());
				return updateCnt;
			}
		}
		/** 社宅契約情報更新 */
		// 削除契約番号取得
		String delContractNo = registDto.getHdnDeleteContractSelectedValue();
		// DB取得契約情報取得
		List<Map<String, Object>> dbContractList = new ArrayList<Map<String, Object>>();
		if (registDto.getContractInfoListTableData() != null) {
			dbContractList.addAll(registDto.getContractInfoListTableData());
		}
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
			mShatakuContract.setContractPropertyId(mShatakuContract.getContractPropertyId());
			// 追加更新判定
			if (drpDwnSelected.get("contractText") != null
					&& drpDwnSelected.get("contractText").toString().contains(Skf3010Sc002CommonDto.CONTRACT_NO_SEPARATOR)) {
				// 更新
				updateCnt = skf3010MShatakuContractRepository.updateByPrimaryKeySelective(mShatakuContract);
			} else {
				// 追加
				updateCnt = skf3010MShatakuContractRepository.insertSelective(mShatakuContract);
			}
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅契約情報更新失敗：" + mShatakuContract.getContractPropertyId());
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
			Skf3010MShatakuContract mShatakuContract, String pageId) {

		// 更新カウント
		int updateCnt = 0;
		// 処理年月
		String syoriNengetu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		// 社宅管理番号取得
		Long shatakuKanriNo = skf3010Sc002SharedService.getNextShatakuKanriNo(syoriNengetu);
		// ユーザーID取得
		String userId = LoginUserInfoUtils.getUserCd();

		/** 社宅基本登録 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShataku.setUpdateUserId(userId);
		mShataku.setInsertUserId(userId);
		// プログラムID設定
		mShataku.setUpdateProgramId(pageId);
		mShataku.setInsertProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuTableDataExpRepository.insertShatakuKihon(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本登録失敗");
			return updateCnt;
		}
		/** 社宅駐車場登録 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShatakuParking.setUpdateUserId(userId);
		mShatakuParking.setInsertUserId(userId);
		// プログラムID設定
		mShatakuParking.setUpdateProgramId(pageId);
		mShatakuParking.setInsertProgramId(pageId);
		updateCnt = skf3010Sc002UpdateMshatakuParkingTableDataExpRepository.insertShatakuParking(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場登録失敗");
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
				LogUtils.debugByMsg("駐車場区画登録失敗：" + mShatakuParkingBlock.getParkingBlock());
				return updateCnt;
			}
//			// 一棟借上判定
//			if (ittoFlg) {
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
					LogUtils.debugByMsg("駐車場契約情報登録失敗：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
//			}
			parkingKanriNo++;
		}
		/** 社宅備品登録 */
		for (Skf3010MShatakuBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			updateCnt = skf3010MShatakuBihinRepository.insertSelective(bihin);
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("備品登録失敗：" + bihin.getBihinCd());
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
				LogUtils.debugByMsg("社宅管理者登録失敗：" + manage.getManegeKbn());
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
				LogUtils.debugByMsg("社宅契約情報登録失敗：" + mShatakuContract.getContractPropertyId());
				return updateCnt;
			}
		}
		return updateCnt;
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
			LogUtils.debugByMsg("社宅重複チェックあり：同名、同住所の社宅が既に存在する");
			return false;
		}
		return true;
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
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("areaKbn"), null)
				|| CheckUtils.isEmpty(drpDwnSelectedMap.get("areaKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "地域区分");
			registDto.setAreaKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 地域区分";
		}
		// 社宅区分
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("shatakuKbn"), null)
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("shatakuKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "社宅区分");
			registDto.setShatakuKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅区分";
		}
		// 利用区分
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("useKbn"), null)
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("useKbn").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "利用区分");
			registDto.setUseKbnKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 利用区分";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理会社
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("manageCompany"), null)
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageCompany").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理会社");
			registDto.setManageCompanyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理会社";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理機関
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("manageAgency"), null)
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageAgency").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理機関");
			registDto.setManageAgencyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理機関";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理事業領域
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("manageBusinessArea"), null)
				|| SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("manageBusinessArea").toString())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理事業領域");
			registDto.setManageBusinessAreaErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理事業領域";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 社宅所在地
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("pref"), null)
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
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("shatakuStructure"), null)
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
		if (Objects.equals(drpDwnSelectedMap, null) || Objects.equals(drpDwnSelectedMap.get("parkingStructure"), null)
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
			LogUtils.debugByMsg("必須入力チェック検知：" + debugMessage);
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
			if (CheckUtils.isNumberFormat(parkingRentalAdjust) && parkingRentalAdjust.length() > 0) {
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
				if(Objects.equals(startDate, null)){
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
					registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約開始日 - " + registDto.getContractStartDay();
				}
				if(Objects.equals(endDate, null)){
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
					registDto.setContractEndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 契約終了日 - " + registDto.getContractEndDay();
				}
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
		}
		// 契約情報
		if ((drpDwnSelectedMap != null && drpDwnSelectedMap.get("contractNo") != null) 
				&& !SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("contractNo").toString())) {
			// 契約情報が存在する場合
			// 経理連携用管理番号
			if (!CheckUtils.isHalfWidth(registDto.getAssetRegisterNo())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1002, "経理連携用管理番号");
				registDto.setAssetRegisterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 形式チェック - 経理連携用管理番号 - " +registDto.getAssetRegisterNo();
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc002CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
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
			LogUtils.debugByMsg("ファイル合計サイズ超過：" + sumFileSize);
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1039);
			return false;
		}
		return true;
	}
}
