/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetKariageShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006DeleteInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetSyatakuRentalHistoryNumExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetTejiDataNumExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006DeleteDto;
import jp.co.intra_mart.common.platform.log.Logger;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3010Sc006DeleteService 借上社宅登録の契約情報「削除」ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006DeleteService extends SkfServiceAbstract<Skf3010Sc006DeleteDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3010Sc006GetSyatakuRentalHistoryNumExpRepository skf3010Sc006GetSyatakuRentalHistoryNumExpRepository;
	@Autowired
	private Skf3010Sc006GetTejiDataNumExpRepository skf3010Sc006GetTejiDataNumExpRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010MShatakuRepository skf3010MShatakuRepository;
	@Autowired
	private Skf3010MShatakuParkingRepository skf3010MShatakuParkingRepository;
	@Autowired
	private Skf3010Sc006DeleteInfoExpRepository skf3010Sc006DeleteInfoExpRepository;
	@Autowired
	private Skf3010Sc006GetBihinInfoExpRepository skf3010Sc006GetBihinInfoExpRepository;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	
	/**
	 * 保有社宅登録の契約情報「削除」ボタン押下時処理を行う。　
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	@Transactional
	public Skf3010Sc006DeleteDto index(Skf3010Sc006DeleteDto delDto) throws Exception {
		// デバッグログ
		logger.debug("借上社宅情報削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, FunctionIdConstant.SKF3010_SC006);

		/** JSON(連携用) */
		// 駐車場区画情報リスト
//		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// ドロップダウン選択値リスト
		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();

		// List変換
//		parkingList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(delDto.getJsonParking()));
		bihinList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(delDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(delDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(delDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc006SharedService.clearVaridateErr(delDto);
		// 社宅管理番号
		Long shatakuKanriNo = Long.parseLong(delDto.getHdnShatakuKanriNo());
		// 部屋管理番号
		Long shatakuRoomKanriNo = Long.parseLong(delDto.getShatakuRoomKanriNo());

		// 削除チェック
		if (!deleteCheck(shatakuKanriNo, shatakuRoomKanriNo)) {
			// 元の画面状態に戻す
			skf3010Sc006SharedService.setBeforeInfo(delDto);
			// 入居実績、または提示中データ存在
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_3011, 
					delDto.getHdnShatakuName(),delDto.getRoomNo());
			// ロールバック
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}
		// エラー発生時用に元の画面状態を設定
		skf3010Sc006SharedService.setBeforeInfo(delDto);

		// 保有社宅削除
		int delCount = deleteKariageShatakuInfo(shatakuKanriNo, delDto);
		if(delCount <= 0){
			// 元の画面状態に戻す
			skf3010Sc006SharedService.setBeforeInfo(delDto);
			//他のユーザによって更新されています。一覧画面へ戻って再度検索してください。
			ServiceHelper.addErrorResultMessage(delDto, null, MessageIdConstant.E_SKF_1135);
			// ロールバック
			throwBusinessExceptionIfErrors(delDto.getResultMessages());
			return delDto;
		}
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
	private int deleteKariageShatakuInfo(Long shatakuKanriNo,
			Skf3010Sc006DeleteDto delDto) {

		// 更新カウント
		int cntSum = 0;
		int delCnt = 0;
		// パラメータ設定
		Skf3010Sc006GetShatakuInfoExpParameter shatakuInfoParam = new Skf3010Sc006GetShatakuInfoExpParameter();
		shatakuInfoParam.setShatakuKanriNo(shatakuKanriNo);

		/** 社宅基本削除 */
		delCnt = skf3010MShatakuRepository.deleteByPrimaryKey(shatakuKanriNo);
		LogUtils.debugByMsg("社宅基本削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;
		
		/** 社宅部屋削除 */
		Long shatakuRoomKanriNo = Long.parseLong(delDto.getShatakuRoomKanriNo());
		Skf3010MShatakuRoomKey roomKey = new Skf3010MShatakuRoomKey();
		roomKey.setShatakuKanriNo(shatakuKanriNo);
		roomKey.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		delCnt = skf3010MShatakuRoomRepository.deleteByPrimaryKey(roomKey);
		LogUtils.debugByMsg("社宅部屋削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;

		/** 社宅駐車場削除 */
		delCnt = skf3010MShatakuParkingRepository.deleteByPrimaryKey(shatakuKanriNo);
		LogUtils.debugByMsg("駐車場削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;
		

		/** 駐車場区画削除 */
		// 区画情報
		Long dbKanriNo = Long.parseLong(delDto.getParkingKanriNo());
		Skf3010MShatakuParkingBlockKey delParking = new Skf3010MShatakuParkingBlockKey();
		delParking.setShatakuKanriNo(shatakuKanriNo);
		delParking.setParkingKanriNo(dbKanriNo);
		// 区画情報削除
		delCnt = skf3010MShatakuParkingBlockRepository.deleteByPrimaryKey(delParking);
		LogUtils.debugByMsg("駐車場区画削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;
		
		/** 社宅部屋備品削除 */
		Skf3010Sc006GetBihinInfoExpParameter delBihinParam = new Skf3010Sc006GetBihinInfoExpParameter();
		delBihinParam.setShatakuKanriNo(shatakuKanriNo);
		delBihinParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		delCnt = skf3010Sc006GetBihinInfoExpRepository.deleteBihinInfo(delBihinParam);
		LogUtils.debugByMsg("社宅部屋備品削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;
		
		
		/** 社宅管理者削除 */
		delCnt = skf3010Sc006DeleteInfoExpRepository.deleteShatakuManegeMaster(shatakuInfoParam);
		LogUtils.debugByMsg("社宅管理者削除件数：" + Integer.toString(delCnt));
		if(delCnt <= 0){
			return delCnt;
		}
		cntSum += delCnt;

		/** 社宅契約情報削除 */
		if(delDto.getContractInfoListTableData().size() > 0){
			delCnt = skf3010Sc006DeleteInfoExpRepository.deleteContract(shatakuInfoParam);
			LogUtils.debugByMsg("社宅契約削除件数：" + Integer.toString(delCnt));
			if(delCnt <= 0){
				return delCnt;
			}
			cntSum += delCnt;
		}

		/** 駐車場契約情報削除 */
		if(delDto.getParkingContractInfoListTableData().size() > 0){
			delCnt = skf3010Sc006DeleteInfoExpRepository.deleteParkingContract(shatakuInfoParam);
			LogUtils.debugByMsg("駐車場契約削除件数：" + Integer.toString(delCnt));
			if(delCnt <= 0){
				return delCnt;
			}
			cntSum += delCnt;
		}
		
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
	private Boolean deleteCheck(Long shatakuKanriNo,Long shatakuRoomKanriNo) {

		// 取得件数
		int cnt = 0;
		// 入居実績件数取得
		Skf3010Sc006GetKariageShatakuInfoExpParameter param = new Skf3010Sc006GetKariageShatakuInfoExpParameter();
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		cnt = skf3010Sc006GetSyatakuRentalHistoryNumExpRepository.getSyatakuRentalHistoryNum(param);
		// 入居実績件数判定
		if (cnt > 0) {
			LogUtils.infoByMsg("deleteCheck, 入居実績あり：" + shatakuKanriNo.toString());
			return false;
		}
		// 提示件数取得
		cnt = skf3010Sc006GetTejiDataNumExpRepository.getTejiDataNum(param);
		param = null;
		// 提示件数判定
		if (cnt > 0) {
			LogUtils.infoByMsg("deleteCheck, 提示データあり：" + shatakuKanriNo.toString());
			return false;
		}
		
		return true;
	}
}
