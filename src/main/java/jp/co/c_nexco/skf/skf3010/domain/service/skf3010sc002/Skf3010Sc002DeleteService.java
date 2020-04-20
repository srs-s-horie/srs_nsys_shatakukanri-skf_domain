/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuParkingTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetHoyuShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockContractInfoTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockHistroyCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetRentalPatternTableDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuRoomExlusiveCntrlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002MShatakuTableDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManege;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContractKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihinKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TRentalPatternKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetParkingBlockContractDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRentalPtternIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetShatakuRentalHistroyCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetTeijiDataCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002UpdateMshatakuParkingTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002UpdateMshatakuTableDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuManegeRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TRentalPatternRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002DeleteDto;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002.Skf3010Sc002SharedService;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3010Sc002DeleteService 保有社宅登録の契約情報「削除」ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc002DeleteService extends SkfServiceAbstract<Skf3010Sc002DeleteDto> {

	@Autowired
	private Skf3010Sc002SharedService skf3010Sc002SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
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
	private Skf3010Sc002GetShatakuRentalHistroyCountExpRepository skf3010Sc002GetShatakuRentalHistroyCountExpRepository;
	@Autowired
	private Skf3010Sc002GetTeijiDataCountExpRepository skf3010Sc002GetTeijiDataCountExpRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010MShatakuRoomBihinRepository skf3010MShatakuRoomBihinRepository;
	@Autowired
	private Skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository;
	@Autowired
	private Skf3010Sc002GetRentalPtternIdExpRepository skf3010Sc002GetRentalPtternIdExpRepository;
	@Autowired
	private Skf3030TRentalPatternRepository skf3030TRentalPatternRepository;

	/**
	 * 保有社宅登録の契約情報「削除」ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	@Transactional
	public Skf3010Sc002DeleteDto index(Skf3010Sc002DeleteDto delDto) throws Exception {
		// デバッグログ
		LogUtils.debugByMsg("保有社宅情報削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("保有社宅情報削除", CodeConstant.C001, FunctionIdConstant.SKF3010_SC002);

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
		parkingList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(delDto.getJsonParking()));
		bihinList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(delDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(delDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc002SharedService.jsonArrayToArrayList(delDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc002SharedService.clearVaridateErr(delDto);
		// 社宅管理番号
		Long shatakuKanriNo = Long.parseLong(delDto.getHdnShatakuKanriNo());

		// 削除チェック
		if (!deleteCheck(shatakuKanriNo)) {
			// 元の画面状態に戻す
			skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, delDto);
			// 入居実績、または提示中データ存在
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_3010, delDto.getShatakuName());
			// ロールバック
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}
		// エラー発生時用に元の画面状態を設定
		skf3010Sc002SharedService.setBeforeInfo(drpDwnSelectedList, labelList, bihinList, parkingList, delDto);

		/** 更新用データ作成 */
		// 社宅基本
		skf3010Sc002SharedService.setUpdateColumShatakuKihon(mShataku, drpDwnSelectedList, labelList, delDto);
		// 社宅駐車場
		skf3010Sc002SharedService.setUpdateColumParking(mShatakuParking, drpDwnSelectedList, labelList, delDto);
		// 駐車場区画
		skf3010Sc002SharedService.setUpdateColumParkingBlock(mShatakuParkingBlockList, parkingList);
		// 社宅管理者
		skf3010Sc002SharedService.setUpdateColumShatakuManage(mShatakuManageList, delDto);
		// 社宅備品
		skf3010Sc002SharedService.setUpdateColumBihin(mShatakuBihinList, bihinList, delDto);
		// 契約情報
		skf3010Sc002SharedService.setUpdateColumContract(mShatakuContract, drpDwnSelectedList, delDto);

		// 保有社宅削除
		deleteHoyuShatakuInfo(shatakuKanriNo, mShataku, mShatakuParking, mShatakuParkingBlockList,
									mShatakuBihinList, mShatakuManageList, mShatakuContract, delDto);
		// DTO初期化
		delDto.setHdnNowSelectTabIndex(null);
		delDto.setHdnShatakuKanriNo(null);
		delDto.setHdnShatakuName(null);
		delDto.setHdnShatakuKbn(null);
		delDto.setHdnAreaKbn(null);
		delDto.setHdnEmptyParkingCount(null);
		delDto.setHdnRowShatakuKanriNo(null);
		delDto.setHdnRowEmptyParkingCount(null);
		delDto.setHdnRowAreaKbn(null);
		delDto.setHdnRowEmptyRoomCount(null);
		delDto.setHdnRowShatakuKbn(null);
		delDto.setHdnRowShatakuName(null);

		// 解放
		parkingList = null;
		bihinList = null;
		drpDwnSelectedList = null;
		labelList = null;
		mShataku = null;
		mShatakuParking = null;
		mShatakuParking = null;
		mShatakuParkingBlockList = null;
		mShatakuManageList = null;
		mShatakuBihinList = null;
		mShatakuContract = null;
		// 成功メッセージ
		ServiceHelper.addResultMessage(delDto, MessageIdConstant.I_SKF_1013);
		// 画面遷移（社宅一覧へ）
		TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3010_SC001, "init");
		delDto.setTransferPageInfo(nextPage);

		return delDto;
	}
	/**
	 * 保有社宅削除
	 * 
	 * @param shatakuKanriNo			社宅管理番号
	 * @param mShataku					社宅基本
	 * @param mShatakuParking			社宅駐車場
	 * @param mShatakuParkingBlockList	駐車場区画
	 * @param mShatakuBihinList			社宅備品
	 * @param mShatakuManageList		社宅管理者
	 * @param mShatakuContract			社宅契約
	 * @param delDto					DTO
	 * @return	更新数
	 */
	private int deleteHoyuShatakuInfo(Long shatakuKanriNo,
			Skf3010Sc002MShatakuTableDataExpParameter mShataku, 
			Skf3010Sc002MShatakuParkingTableDataExpParameter mShatakuParking,
			List<Skf3010MShatakuParkingBlock> mShatakuParkingBlockList,
			List<Skf3010MShatakuBihin> mShatakuBihinList,
			List<Skf3010MShatakuManege> mShatakuManageList,
			Skf3010MShatakuContract mShatakuContract,
			Skf3010Sc002DeleteDto delDto) {

		// 更新カウント
		int cntSum = 0;
		int delCnt = 0;
		// パラメータ設定
		Skf3010Sc002GetHoyuShatakuInfoExpParameter hoyuShatakuInfoParam = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
		hoyuShatakuInfoParam.setShatakuKanriNo(shatakuKanriNo);

		/** 社宅基本削除 */
		delCnt = skf3010Sc002UpdateMshatakuTableDataExpRepository.deleteShatakuKihon(hoyuShatakuInfoParam);
		LogUtils.debugByMsg("社宅基本登録削除件数：" + Integer.toString(delCnt));
		cntSum += delCnt;

		/** 社宅駐車場削除 */
		delCnt = skf3010Sc002UpdateMshatakuParkingTableDataExpRepository.deleteShatakuParking(hoyuShatakuInfoParam);
		cntSum += delCnt;
		LogUtils.debugByMsg("駐車場削除時累計：" + Integer.toString(cntSum));

		/** 駐車場区画削除 */
		// 更新前区画情報
		List<Map<String, Object>> startingParking = new ArrayList<Map<String, Object>>();
		if (delDto.getHdnStartingParkingInfoListTableData() != null) {
			startingParking.addAll(delDto.getHdnStartingParkingInfoListTableData());
		}
		// 区画情報削除
		// DB取得数分ループ
		for(Map<String, Object> parkingBlock : startingParking) {
			// DB取得駐車場管理番号取得
			Long dbKanriNo = Long.parseLong(parkingBlock.get("parkingKanriNo").toString());
			// 削除
			Skf3010MShatakuParkingBlockKey delParking = new Skf3010MShatakuParkingBlockKey();
			delParking.setShatakuKanriNo(shatakuKanriNo);
			delParking.setParkingKanriNo(dbKanriNo);
			delCnt = skf3010MShatakuParkingBlockRepository.deleteByPrimaryKey(delParking);
			cntSum += delCnt;
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
				key.setShatakuKanriNo(shatakuKanriNo);
				key.setParkingKanriNo(blockContract.getParkingKanriNo());
				key.setContractPropertyId(blockContract.getContractPropertyId());
				// 区画契約情報削除
				delCnt = skf3010MShatakuParkingContractRepository.deleteByPrimaryKey(key);
				cntSum += delCnt;
				key = null;
			}
			blockContractList = null;
			param = null;
			delParking = null;
		}
		LogUtils.debugByMsg("駐車場区画削除時累計：" + Integer.toString(cntSum));

		/** 社宅備品削除 */
		for (Skf3010MShatakuBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			delCnt = skf3010MShatakuBihinRepository.deleteByPrimaryKey(bihin);
			cntSum += delCnt;
			LogUtils.debugByMsg("社宅備品削除時累計：" + Integer.toString(cntSum));
		}
		/** 社宅管理者削除 */
		for (Skf3010MShatakuManege manage : mShatakuManageList) {
			// 社宅管理番号設定
			manage.setShatakuKanriNo(shatakuKanriNo);
			delCnt = skf3010MShatakuManegeRepository.deleteByPrimaryKey(manage);
			cntSum += delCnt;
			LogUtils.debugByMsg("駐車場区画削除時累計：" + Integer.toString(cntSum));
		}
		/** 社宅契約情報削除 */
		// DB取得契約情報取得
		List<Map<String, Object>> dbContractList = new ArrayList<Map<String, Object>>();
		if (delDto.getContractInfoListTableData() != null) {
			dbContractList.addAll(delDto.getContractInfoListTableData());
		}
		// 削除契約情報削除
		for (Map<String, Object> contract : dbContractList) {
			// 削除契約番号判定
			Long tmpNo = Long.parseLong(contract.get("contractNo").toString());
			// 削除
			Skf3010MShatakuContract deleteContract = new Skf3010MShatakuContract();
			deleteContract.setShatakuKanriNo(shatakuKanriNo);
			deleteContract.setContractPropertyId(tmpNo);
			delCnt = skf3010MShatakuContractRepository.deleteByPrimaryKey(deleteContract);
			cntSum += delCnt;
			deleteContract = null;
		}
		LogUtils.debugByMsg("社宅契約削除時累計：" + Integer.toString(cntSum));
		/** 社宅部屋削除 */
		// 社宅部屋情報取得
		List<Skf3010Sc002GetShatakuRoomExlusiveCntrlExp> roomList = new ArrayList<Skf3010Sc002GetShatakuRoomExlusiveCntrlExp>();
		roomList.addAll(skf3010Sc002GetRoomExlusiveCntrTableDataExpRepository.getRoomExlusiveCntr(hoyuShatakuInfoParam));
		// 削除
		for (Skf3010Sc002GetShatakuRoomExlusiveCntrlExp room : roomList) {
			Skf3010MShatakuRoomKey roomKey = new Skf3010MShatakuRoomKey();
			roomKey.setShatakuKanriNo(shatakuKanriNo);
			roomKey.setShatakuRoomKanriNo(room.getShatakuRoomKanriNo());
			delCnt = skf3010MShatakuRoomRepository.deleteByPrimaryKey(roomKey);
			cntSum += delCnt;
			roomKey = null;
		}
		LogUtils.debugByMsg("部屋情報削除時累計：" + Integer.toString(cntSum));
		/** 社宅部屋備品削除 */
		// 社宅部屋備品取得
		List<Skf3010Sc002GetRoomBihinExlusiveCntrlExp> roomBihinList = new ArrayList<Skf3010Sc002GetRoomBihinExlusiveCntrlExp>();
		roomBihinList.addAll(skf3010Sc002GetRoomBihinExlusiveCntrTableDataExpRepository.getRoomBihinExlusiveCntr(hoyuShatakuInfoParam));
		// 削除
		for (Skf3010Sc002GetRoomBihinExlusiveCntrlExp roomBihin : roomBihinList) {
			Skf3010MShatakuRoomBihinKey roomBihinKey = new Skf3010MShatakuRoomBihinKey();
			roomBihinKey.setShatakuKanriNo(shatakuKanriNo);
			roomBihinKey.setShatakuRoomKanriNo(roomBihin.getShatakuRoomKanriNo());
			roomBihinKey.setBihinCd(roomBihin.getBihinCd());
			delCnt = skf3010MShatakuRoomBihinRepository.deleteByPrimaryKey(roomBihinKey);
			cntSum += delCnt;
			roomBihinKey = null;
		}
		LogUtils.debugByMsg("社宅備品削除時累計：" + Integer.toString(cntSum));
		/** 使用料パターン削除 */
		// 使用料ID取得
		List<Skf3010Sc002GetRentalPatternTableDataExp> rentalPatternList = new ArrayList<Skf3010Sc002GetRentalPatternTableDataExp>();
		rentalPatternList.addAll(skf3010Sc002GetRentalPtternIdExpRepository.getRentalPttern(hoyuShatakuInfoParam));
		// 削除
		for (Skf3010Sc002GetRentalPatternTableDataExp rentalPttern : rentalPatternList) {
			Skf3030TRentalPatternKey rentalKey = new Skf3030TRentalPatternKey();
			rentalKey.setShatakuKanriNo(shatakuKanriNo);
			rentalKey.setRentalPatternId(rentalPttern.getRentalPatternId());
			delCnt = skf3030TRentalPatternRepository.deleteByPrimaryKey(rentalKey);
			cntSum += delCnt;
		}
		rentalPatternList = null;
		roomBihinList = null;
		roomList = null;
		dbContractList = null;
		hoyuShatakuInfoParam = null;
		startingParking = null;
		LogUtils.debugByMsg("削除累計：" + Integer.toString(cntSum));
		return cntSum;
	}

	/**
	 * 削除チェック。<br>
	 * 入居実績がある、または提示中データがある場合はfalseを返却する
	 * 
	 * @param delDto				DTO
	 * @return true(入居実績& 提示中データなし)/false(入居実績 or 提示中データあり)
	 */
	private Boolean deleteCheck(Long shatakuKanriNo) {

		// 取得件数
		int cnt = 0;
		// 入居実績件数取得
		Skf3010Sc002GetHoyuShatakuInfoExpParameter param = new Skf3010Sc002GetHoyuShatakuInfoExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);
		cnt = skf3010Sc002GetShatakuRentalHistroyCountExpRepository.getShatakuRentalHistroyCount(param);
		// 入居実績件数判定
		if (cnt > 0) {
			LogUtils.debugByMsg("入居実績ありー：" + shatakuKanriNo.toString());
			return false;
		}
		// 提示件数取得
		cnt = skf3010Sc002GetTeijiDataCountExpRepository.getTeijiDataCount(param);
		// 提示件数判定
		if (cnt > 0) {
			LogUtils.debugByMsg("提示データあり：" + shatakuKanriNo.toString());
			return false;
		}
		return true;
	}
}
