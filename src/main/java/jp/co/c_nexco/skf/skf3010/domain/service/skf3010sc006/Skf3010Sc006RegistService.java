/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc006;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetNewRoomKanriNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc006.Skf3010Sc006GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuManege;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingContract;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingWithBLOBs;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuWithBLOBs;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006GetNewRoomKanriNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006UpdateMShatakuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006UpdateMShatakuParkingExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc006.Skf3010Sc006UpdateParkingContractExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuManegeRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingContractRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc002common.Skf3010Sc002CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010Sc006common.Skf3010Sc006CommonDto;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc006.Skf3010Sc006RegistDto;
import jp.co.intra_mart.common.platform.log.Logger;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3010Sc002RegistService 保有社宅登録の契約情報「登録」ボタン押下サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc006RegistService extends BaseServiceAbstract<Skf3010Sc006RegistDto> {

	@Autowired
	private Skf3010Sc006SharedService skf3010Sc006SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3010MShatakuManegeRepository skf3010MShatakuManegeRepository;
	@Autowired
	private Skf3010MShatakuContractRepository skf3010MShatakuContractRepository;
	@Autowired
	private Skf3010MShatakuParkingContractRepository skf3010MShatakuParkingContractRepository;
	@Autowired
	private Skf3010Sc006GetNewRoomKanriNoExpRepository skf3010Sc006GetNewRoomKanriNoExpRepository;
	@Autowired
	private Skf3010MShatakuRepository skf3010MShatakuRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010MShatakuRoomBihinRepository skf3010MShatakuRoomBihinRepository;
	@Autowired
	private Skf3010MShatakuParkingRepository skf3010MShatakuParkingRepository;
	@Autowired
	private Skf3010Sc006UpdateParkingContractExpRepository skf3010Sc006UpdateParkingContractExpRepository;
	@Autowired
	private Skf3010Sc006UpdateMShatakuExpRepository skf3010Sc006UpdateMShatakuExpRepository;
	@Autowired
	private Skf3010Sc006UpdateMShatakuParkingExpRepository skf3010Sc006UpdateMShatakuParkingExpRepository;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/** 定数 */
	// 駐車場契約形態：社宅と一括
	private static final String PARKING_CONTRACT_TYPE = "1";
	// 駐車場契約形態：社宅と別契約
	private static final String PARKING_CONTRACT_TYPE2 = "2";
	// 駐車場構造区分：なし
	private static final String PARKING_STRUCTURE_NASHI = "5";
	// メールアドレスチェック正規表現
	private static final String MAIL_ADDRESS_CHECK_REG = "[!-~]+@[!-~]+\\.[!-~]+";
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
	public Skf3010Sc006RegistDto index(Skf3010Sc006RegistDto registDto) throws Exception {
		// デバッグログ
		logger.debug("借上社宅情報登録");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("登録", CodeConstant.C001, registDto.getPageId());

		/** JSON(連携用) */
//		// 駐車場区画情報リスト
//		List<Map<String, Object>> parkingList = new ArrayList<Map<String, Object>>();
		// 備品情報リスト
		List<Map<String, Object>> bihinList = new ArrayList<Map<String, Object>>();
		// ドロップダウン選択値リスト
		List<Map<String, Object>> drpDwnSelectedList = new ArrayList<Map<String, Object>>();
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();

		/** 更新用パラメータ */
		// 社宅基本
		Skf3010MShatakuWithBLOBs mShataku = new Skf3010MShatakuWithBLOBs();
		// 社宅部屋
		Skf3010MShatakuRoom mShatakuRoom = new Skf3010MShatakuRoom();
		// 社宅駐車場
		Skf3010MShatakuParkingWithBLOBs mShatakuParking = new Skf3010MShatakuParkingWithBLOBs();
		// 駐車場区画情報
		Skf3010MShatakuParkingBlock mShatakuParkingBlock = new Skf3010MShatakuParkingBlock();
		// 社宅管理者情報リスト
		List<Skf3010MShatakuManege> mShatakuManageList = new ArrayList<Skf3010MShatakuManege>();
		// 社宅部屋備品リスト
		List<Skf3010MShatakuRoomBihin> mShatakuBihinList = new ArrayList<Skf3010MShatakuRoomBihin>();
		// 契約情報
		Skf3010MShatakuContract mShatakuContract = new Skf3010MShatakuContract();
		// 駐車場契約情報
		Skf3010MShatakuParkingContract mShatakuParkingContract = new Skf3010MShatakuParkingContract();
		
		// List変換
//		parkingList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(registDto.getJsonParking()));
		bihinList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(registDto.getJsonBihin()));
		drpDwnSelectedList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(registDto.getJsonDrpDwnList()));
		labelList.addAll(skf3010Sc006SharedService.jsonArrayToArrayList(registDto.getJsonLabelList()));
		// エラーコントロール初期化
		skf3010Sc006SharedService.clearVaridateErr(registDto);
		// 表示タブインデックス
		String tabIndex = (registDto.getHdnNowSelectTabIndex() != null) ? registDto.getHdnNowSelectTabIndex() : "999"; 
		// DTO初期化
		registDto.setHdnNowSelectTabIndex("999");

		// 入力チェック
		if (!checkTouroku(drpDwnSelectedList, registDto)) {
			// 元の画面状態に戻す
			skf3010Sc006SharedService.setBeforeInfo(registDto);
			return registDto;
		}
		// エラー発生時用に元の画面状態を設定
		skf3010Sc006SharedService.setBeforeInfo(registDto);

		/** 更新用データ作成 */
		// 社宅基本
		skf3010Sc006SharedService.setUpdateColumShatakuKihon(mShataku, labelList, registDto);
		// 社宅部屋
		skf3010Sc006SharedService.setUpdateColumShatakuRoom(mShatakuRoom, labelList, registDto);
		// 社宅駐車場
		skf3010Sc006SharedService.setUpdateColumParking(mShatakuParking, labelList, registDto);
		// 駐車場区画
		skf3010Sc006SharedService.setUpdateColumParkingBlock(mShatakuParkingBlock, labelList, registDto);
		// 社宅管理者
		skf3010Sc006SharedService.setUpdateColumShatakuManage(mShatakuManageList, registDto);
		// 社宅備品
		skf3010Sc006SharedService.setUpdateColumBihin(mShatakuBihinList, bihinList, registDto);
		// 契約情報
		skf3010Sc006SharedService.setUpdateColumContract(mShatakuContract, drpDwnSelectedList, registDto);
		// 駐車場契約情報
		skf3010Sc006SharedService.setUpdateColumParkingContract(mShatakuParkingContract, drpDwnSelectedList, registDto);
		// 更新カウンタ
		int updateCnt = 0;

		Map <String, Object> labelMap = (labelList.size() > 0) ? labelList.get(0) : null;

		// 新規登録判定:社宅管理番号なし
		if (SkfCheckUtils.isNullOrEmpty(registDto.getHdnShatakuKanriNo())) {
			// 新規社宅登録
			// 新規追加
			updateCnt = insertKariageShatakuInfo(mShataku, mShatakuRoom, mShatakuParking, mShatakuParkingBlock,
							mShatakuBihinList, mShatakuManageList, mShatakuContract, mShatakuParkingContract ,registDto.getPageId());
			// 更新結果判定
			if (updateCnt < 1) {
				//件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.W_SKF_1009);
				// ロールバック
				throwBusinessExceptionIfErrors(registDto.getResultMessages());
			}
			
			// 成功メッセージ
			ServiceHelper.addResultMessage(registDto, MessageIdConstant.I_SKF_1012);
		} else {
			// 更新社宅登録
			updateCnt = updateKariageShatakuInfo(
					drpDwnSelectedList,
					mShataku, 
					mShatakuRoom, 
					mShatakuParking,
					mShatakuParkingBlock,
					mShatakuBihinList,
					mShatakuManageList,
					mShatakuContract,
					mShatakuParkingContract,
					registDto.getPageId(),
					registDto);
			// 更新結果判定
			if (updateCnt < 1) {
				//件数が0未満（排他エラー）
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
		registDto.setCopyFlg("false");

		// 初期表示
		// 契約番号
		String contractNo = "";
		if (mShatakuContract.getContractPropertyId() != null) {
			contractNo = mShatakuContract.getContractPropertyId().toString();
		}
		//駐車場契約番号
		String parkingContractNo = "";
		if (mShatakuParkingContract.getContractPropertyId() != null) {
			parkingContractNo = mShatakuParkingContract.getContractPropertyId().toString();
		}
		skf3010Sc006SharedService.setShatakuInfo(contractNo, parkingContractNo, registDto);
		registDto.setHdnNowSelectTabIndex(tabIndex);
		return registDto;
	}

	/**
	 * 借上社宅更新
	 * @param drpDwnSelectedList		ドロップダウン選択値リスト
	 * @param mShataku					社宅基本
	 * @param mShatakuRoom				社宅部屋
	 * @param mShatakuParking			社宅駐車場
	 * @param mShatakuParkingBlock		駐車場区画
	 * @param mShatakuBihinList			社宅備品
	 * @param mShatakuManageList		社宅管理者
	 * @param mShatakuContract			社宅契約
	 * @param mShatakuParkingContract	駐車場契約
	 * @param pageId					ページID(プログラムID)
	 * @param registDto					DTO
	 * @return	更新数
	 */
	private int updateKariageShatakuInfo(
			List<Map<String, Object>> drpDwnSelectedList,
			Skf3010MShatakuWithBLOBs mShataku, 
			Skf3010MShatakuRoom mShatakuRoom, 
			Skf3010MShatakuParkingWithBLOBs mShatakuParking,
			Skf3010MShatakuParkingBlock mShatakuParkingBlock,
			List<Skf3010MShatakuRoomBihin> mShatakuBihinList,
			List<Skf3010MShatakuManege> mShatakuManageList,
			Skf3010MShatakuContract mShatakuContract, 
			Skf3010MShatakuParkingContract mShatakuParkingContract, 
			String pageId,
			Skf3010Sc006RegistDto registDto) {

		Map <String, Object> drpDwnSelected = (drpDwnSelectedList.size() > 0) ? drpDwnSelectedList.get(0) : null;

		// 更新カウント
		int updateCnt = 0;
		// 社宅管理番号取得
		Long shatakuKanriNo = Long.parseLong(registDto.getHdnShatakuKanriNo());
		// 部屋管理番号
		Long shatakuRoomKanriNo = Long.parseLong(registDto.getShatakuRoomKanriNo());

		// ユーザーID取得
		String userName = LoginUserInfoUtils.getUserCd();

		/** 社宅基本更新 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShataku.setUpdateUserId(userName);
		// プログラムID設定
		mShataku.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc006UpdateMShatakuExpRepository.updateByPrimaryKeySelective(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本更新エラー");
			return updateCnt;
		}
		/** 社宅部屋更新 */
		// 社宅管理番号設定
		mShatakuRoom.setShatakuKanriNo(shatakuKanriNo);
		updateCnt = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(mShatakuRoom);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅部屋更新エラー");
			return updateCnt;
		}
		/** 社宅駐車場更新 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		// ユーザーID設定
		mShatakuParking.setUpdateUserId(userName);
		// プログラムID設定
		mShatakuParking.setUpdateProgramId(pageId);
		updateCnt = skf3010Sc006UpdateMShatakuParkingExpRepository.updateByPrimaryKeySelective(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場更新エラー");
			return updateCnt;
		}
		/** 駐車場区画更新 */
		// 社宅管理番号設定
		mShatakuParkingBlock.setShatakuKanriNo(shatakuKanriNo);
		// データ更新
		updateCnt = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(mShatakuParkingBlock);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("駐車場区画更新エラー：" + mShatakuParkingBlock.getParkingBlock());
			return updateCnt;
		}
		
		/** 社宅部屋備品更新 */
		for (Skf3010MShatakuRoomBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			bihin.setShatakuRoomKanriNo(shatakuRoomKanriNo);
			// 更新日設定判定
			if (bihin.getLastUpdateDate() != null) {
				// 更新
				updateCnt = skf3010MShatakuRoomBihinRepository.updateByPrimaryKeySelective(bihin);
			} else {
				// 追加
				updateCnt = skf3010MShatakuRoomBihinRepository.insertSelective(bihin);
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
		Long delNo = 0L;
		// DB取得契約情報取得
		List<Map<String, Object>> dbContractList = new ArrayList<Map<String, Object>>();
		if (registDto.getContractInfoListTableData() != null) {
			dbContractList.addAll(registDto.getContractInfoListTableData());
		}
		// 削除契約情報削除
		if (delContractNo != null 
				&& !delContractNo.contains("M")
				&& !CodeConstant.STRING_ZERO.equals(delContractNo)) {
			delNo = Long.parseLong(delContractNo);
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
			if ( mShatakuContract.getContractPropertyId() > dbContractList.size() ||
					mShatakuContract.getContractPropertyId().equals(delNo) ) {
				// 追加
				updateCnt = skf3010MShatakuContractRepository.insertSelective(mShatakuContract);
			} else {
				// 更新
				updateCnt = skf3010MShatakuContractRepository.updateByPrimaryKeySelective(mShatakuContract);

			}
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("社宅契約情報更新エラー：" + mShatakuContract.getContractPropertyId());
				return updateCnt;
			}
		}
		/** 駐車場契約情報更新 */
		// 駐車場管理番号
		Long parkingKanriNo = mShatakuParkingBlock.getParkingKanriNo();
		// 削除契約番号取得
		String delParkingContractNo = registDto.getHdnDeleteParkingContractSelectedValue();
		Long parkingDelNo = 0L;
		// DB取得契約情報取得
		List<Map<String, Object>> dbParkingContractList = new ArrayList<Map<String, Object>>();
		if (registDto.getParkingContractInfoListTableData() != null) {
			dbParkingContractList.addAll(registDto.getParkingContractInfoListTableData());
		}
		// 削除契約情報削除
		//.contains("M")
		if (delParkingContractNo != null 
				&& !delParkingContractNo.contains("M")
				&& !CodeConstant.STRING_ZERO.equals(delParkingContractNo)) {
			parkingDelNo = Long.parseLong(delParkingContractNo);
			for (Map<String, Object> contract : dbParkingContractList) {
				// 削除契約番号判定
				Long tmpNo = Long.parseLong(contract.get("contractNo").toString());
				if (parkingDelNo > tmpNo) {
					// 削除契約番号より小さいため対象外
					continue;
				}
				// 削除対象のため削除
				Skf3010MShatakuParkingContract deleteContract = new Skf3010MShatakuParkingContract();
				deleteContract.setShatakuKanriNo(shatakuKanriNo);
				deleteContract.setContractPropertyId(tmpNo);
				deleteContract.setParkingKanriNo(parkingKanriNo);
				skf3010MShatakuParkingContractRepository.deleteByPrimaryKey(deleteContract);
				deleteContract = null;
			}
		}
		// 登録有無判定
		if (mShatakuParkingContract.getContractPropertyId() != null && mShatakuParkingContract.getContractPropertyId() > 0) {
			// 社宅管理番号設定
			mShatakuParkingContract.setShatakuKanriNo(shatakuKanriNo);
			mShatakuParkingContract.setParkingKanriNo(parkingKanriNo);
			// 契約番号設定
			mShatakuParkingContract.setContractPropertyId(mShatakuParkingContract.getContractPropertyId());
			// 追加更新判定
			if ( mShatakuParkingContract.getContractPropertyId() > dbParkingContractList.size() ||
					mShatakuParkingContract.getContractPropertyId().equals(parkingDelNo) ) {
				// 追加(契約情報リストより大きい、または削除番号と一致
				if(!PARKING_STRUCTURE_NASHI.equals(mShatakuParking.getParkingStructureKbn())){
					//駐車場構造が"なし"でない場合のみ登録する
					updateCnt = skf3010MShatakuParkingContractRepository.insertSelective(mShatakuParkingContract);
				}
			} else {
				// 更新
				updateCnt = skf3010Sc006UpdateParkingContractExpRepository.updateByPrimaryKeySelective(mShatakuParkingContract);
			}
			// 更新カウント判定
			if (updateCnt < 1) {
				LogUtils.debugByMsg("駐車場契約情報更新エラー：" + mShatakuParkingContract.getContractPropertyId());
				return updateCnt;
			}
		}
		return updateCnt;
	}

	/**
	 * 新規借上社宅登録
	 * 
	 * @param mShataku					社宅基本
	 * @param mShatakuRoom				社宅部屋
	 * @param mShatakuParking			社宅駐車場
	 * @param mShatakuParkingBlock		駐車場区画
	 * @param mShatakuBihinList			社宅備品リスト
	 * @param mShatakuManageList		社宅管理者リスト
	 * @param mShatakuContract			社宅契約
	 * @param mShatakuParkingContract	駐車場契約
	 * @param pageId					ページID(プログラムID)
	 * @return	登録件数
	 */
	private int insertKariageShatakuInfo(Skf3010MShatakuWithBLOBs mShataku, 
			Skf3010MShatakuRoom mShatakuRoom,
			Skf3010MShatakuParkingWithBLOBs mShatakuParking,
			Skf3010MShatakuParkingBlock mShatakuParkingBlock,
			List<Skf3010MShatakuRoomBihin> mShatakuBihinList, List<Skf3010MShatakuManege> mShatakuManageList,
			Skf3010MShatakuContract mShatakuContract, Skf3010MShatakuParkingContract mShatakuParkingContract, 
			String pageId) {

		// 更新カウント
		int updateCnt = 0;
		// 処理年月
		String syoriNengetu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		// 社宅管理番号取得
		Long shatakuKanriNo = skf3010Sc006SharedService.getNextShatakuKanriNo(syoriNengetu);
		Skf3010Sc006GetShatakuInfoExpParameter shatakuParam = new Skf3010Sc006GetShatakuInfoExpParameter();
		shatakuParam.setShatakuKanriNo(shatakuKanriNo);
		// 部屋管理番号
		List<Skf3010Sc006GetNewRoomKanriNoExp> getNewRoomKanriNoExp = new ArrayList<Skf3010Sc006GetNewRoomKanriNoExp>();
		Long shatakuRoomKanriNo = 1L;
		getNewRoomKanriNoExp= skf3010Sc006GetNewRoomKanriNoExpRepository.getNewRoomKanriNo(shatakuParam);
		if(getNewRoomKanriNoExp.size() > 0 && getNewRoomKanriNoExp.get(0) != null){
			shatakuRoomKanriNo = getNewRoomKanriNoExp.get(0).getShatakuRoomKanriNo();
		}

		/** 社宅基本登録 */
		// 社宅管理番号設定
		mShataku.setShatakuKanriNo(shatakuKanriNo);
		updateCnt = skf3010MShatakuRepository.insertSelective(mShataku);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅基本登録エラー");
			return updateCnt;
		}
		/** 社宅部屋登録 */
		// 社宅管理番号設定
		mShatakuRoom.setShatakuKanriNo(shatakuKanriNo);
		mShatakuRoom.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		// 貸与状況初期値設定
		mShatakuRoom.setLendJokyo("0");
		updateCnt = skf3010MShatakuRoomRepository.insertSelective(mShatakuRoom);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅部屋登録エラー");
			return updateCnt;
		}
		/** 社宅駐車場登録 */
		// 社宅管理番号設定
		mShatakuParking.setShatakuKanriNo(shatakuKanriNo);
		updateCnt = skf3010MShatakuParkingRepository.insertSelective(mShatakuParking);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("社宅駐車場登録エラー");
			return updateCnt;
		}
		/** 駐車場区画情報登録 */
		// 新規区画駐車場管理番号(1固定）
		Long parkingKanriNo = 1L;
		// 社宅管理番号設定
		mShatakuParkingBlock.setShatakuKanriNo(shatakuKanriNo);
		// 駐車場管理番号設定
		mShatakuParkingBlock.setParkingKanriNo(parkingKanriNo);
		// 貸与状況初期値設定
		mShatakuParkingBlock.setParkingLendJokyo("0");
		// 駐車場区画情報登録
		updateCnt = skf3010MShatakuParkingBlockRepository.insertSelective(mShatakuParkingBlock);
		// 更新カウント判定
		if (updateCnt < 1) {
			LogUtils.debugByMsg("駐車場区画登録エラー");
			return updateCnt;
		}
		
		/** 社宅部屋備品登録 */
		for (Skf3010MShatakuRoomBihin bihin : mShatakuBihinList) {
			// 社宅管理番号設定
			bihin.setShatakuKanriNo(shatakuKanriNo);
			bihin.setShatakuRoomKanriNo(shatakuRoomKanriNo);
			updateCnt = skf3010MShatakuRoomBihinRepository.insertSelective(bihin);
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
		
		// 駐車場契約情報登録
		if(!PARKING_STRUCTURE_NASHI.equals(mShatakuParking.getParkingStructureKbn())){
			//駐車場構造が"なし"でない場合のみ登録する
			if(!SkfCheckUtils.isNullOrEmpty(mShatakuParkingContract.getParkingContractType())){
				mShatakuParkingContract.setShatakuKanriNo(shatakuKanriNo);
				mShatakuParkingContract.setParkingKanriNo(parkingKanriNo);
				mShatakuParkingContract.setContractPropertyId(1L);
				updateCnt = skf3010MShatakuParkingContractRepository.insertSelective(mShatakuParkingContract);
				// 更新カウント判定
				if (updateCnt < 1) {
					LogUtils.debugByMsg("駐車場契約情報登録エラー：" + mShatakuParkingBlock.getParkingBlock());
					return updateCnt;
				}
			}
		}


		return updateCnt;
	}

	/**
	 * 入力チェック。<br>
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param drpDwnSelectedList	プルダウン選択値リスト
	 * @param registDto				*DTO
	 * @return
	 */
	private Boolean checkTouroku(List<Map<String, Object>> drpDwnSelectedList, 
			Skf3010Sc006RegistDto registDto) {
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
		if (SkfCheckUtils.isNullOrEmpty(registDto.getAreaKbn())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "地域区分");
			registDto.setAreaKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 地域区分";
		}
		// 利用区分
		if (SkfCheckUtils.isNullOrEmpty(registDto.getUseKbn())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "利用区分");
			registDto.setUseKbnKbnErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 利用区分";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理会社
		if (SkfCheckUtils.isNullOrEmpty(registDto.getManageCompany())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理会社");
			registDto.setManageCompanyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理会社";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理機関
		if (SkfCheckUtils.isNullOrEmpty(registDto.getManageAgency())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理機関");
			registDto.setManageAgencyErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理機関";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 管理事業領域
		if (SkfCheckUtils.isNullOrEmpty(registDto.getManageBusinessArea())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "管理事業領域");
			registDto.setManageBusinessAreaErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 管理事業領域";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 社宅所在地
		if (SkfCheckUtils.isNullOrEmpty(registDto.getPref())
				|| SkfCheckUtils.isNullOrEmpty(registDto.getShatakuAddress())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "所在地");
			registDto.setPrefErr(CodeConstant.NFW_VALIDATION_ERROR);
			registDto.setShatakuAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅所在地";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 構造
		if (SkfCheckUtils.isNullOrEmpty(registDto.getShatakuStructure())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "構造");
			registDto.setShatakuStructureErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 社宅構造";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		// 建築年月日の必須チェック
		if (SkfCheckUtils.isNullOrEmpty(registDto.getBuildDate())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "建築年月日");
			registDto.setBuildDateErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 建築年月日";
			// 選択タブインデックス設定：基本情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		//部屋情報
		// 部屋番号
		if (SkfCheckUtils.isNullOrEmpty(registDto.getRoomNo())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "部屋番号");
			registDto.setRoomNoError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "部屋番号";
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		// 本来規格
		if (SkfCheckUtils.isNullOrEmpty(registDto.getOriginalKikaku())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来規格");
			registDto.setOriginalKikakuError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来規格";
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		// 本来用途
		if (SkfCheckUtils.isNullOrEmpty(registDto.getOriginalAuse())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来用途");
			registDto.setOriginalAuseError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来用途";
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		// 貸与区分
		if (SkfCheckUtils.isNullOrEmpty(registDto.getLendKbn())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "貸与区分");
			registDto.setLendKbnError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "貸与区分";
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		// 本来延面積
		if (SkfCheckUtils.isNullOrEmpty(registDto.getOriginalMenseki())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "本来延面積");
			registDto.setOriginalMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　必須入力チェック - " + "本来延面積";
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		// 貸与延面積
		if (SkfCheckUtils.isNullOrEmpty(registDto.getLendMenseki())) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "貸与延面積");
			debugMessage += "　必須入力チェック - " + "貸与延面積";
			registDto.setLendMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		//駐車場情報
		// 駐車場構造
		if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingStructure())) {
			// 駐車場構造が未選択
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場構造");
			registDto.setParkingStructureErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 必須入力チェック - 駐車場構造";
			// 選択タブインデックス設定：駐車場情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
		} else if (!PARKING_STRUCTURE_NASHI.equals(registDto.getParkingStructure())) {
			// 駐車場構造が「なし」以外
			// 区画情報必須チェック
			if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingBlock())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "区画番号");
				// 区画番号エラーを追加
				registDto.setParkingBlockError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 区画番号";
				// 選択タブインデックス設定：駐車場情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
			}
			
			//駐車場契約情報
			if ((drpDwnSelectedMap != null && drpDwnSelectedMap.get("parkingContractNo") != null) 
					&& !SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("parkingContractNo").toString())) {
				//駐車場契約情報有の場合
				// 契約形態
				if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingContractType())) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約形態");
					registDto.setParkingContractTypeError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 必須入力チェック - 契約形態";
					// 選択タブインデックス設定：契約情報タブ
					setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
				}
				if(PARKING_CONTRACT_TYPE2.equals(registDto.getParkingContractType())){
					//契約形態が「社宅と別契約」の場合
					//賃貸人（代理人）
					if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingOwnerName())) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "賃貸人（代理人）");
						registDto.setParkingOwnerNameError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 賃貸人（代理人）";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					// 住所
					if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingContractAddress())) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "住所");
						registDto.setParkingAddressError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 住所";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					// 駐車場名
					if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingName())) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場名");
						registDto.setParkingNameError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 駐車場名";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					// 経理連携用管理番号
					if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingAssetRegisterNo())) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "経理連携用管理番号");
						registDto.setParkingAssetRegisterNoError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 経理連携用管理番号";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					// 契約開始日
					if (SkfCheckUtils.isNullOrEmpty(registDto.getParkingContractStartDay())) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約開始日");
						registDto.setParkingContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 契約開始日";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					// 駐車場代（地代） 
					String rentStr = registDto.getParkingLandRent();
					if(SkfCheckUtils.isNullOrEmpty(rentStr)){
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場代（地代） ");
						registDto.setParkingLandRentError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 必須入力チェック - 駐車場代（地代）";
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
				}
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
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 経理連携用管理番号
			if (SkfCheckUtils.isNullOrEmpty(registDto.getAssetRegisterNo())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "経理連携用管理番号");
				registDto.setAssetRegisterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 経理連携用管理番号";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 契約開始日
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractStartDay())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "契約開始日");
				registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 契約開始日";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 家賃
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractRent())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "家賃");
				registDto.setContractRentErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 家賃";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 共益費
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractKyoekihi())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "共益費");
				registDto.setContractKyoekihiErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 共益費";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
			// 駐車場料(地代)
			if (SkfCheckUtils.isNullOrEmpty(registDto.getContractLandRent())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1048, "駐車場料(地代)");
				registDto.setContractLandRentErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 必須入力チェック - 駐車場料(地代)";
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
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
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_KIHON, registDto);
		}
		
		//部屋情報
		// 本来延面積
		if (!(isValidateTxtFormatForMenseki(registDto.getOriginalMenseki(),true))) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "本来延面積");
			registDto.setOriginalMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　形式チェック - " + "本来延面積 - " + registDto.getOriginalMenseki();
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}

		//貸与延面積
		if (!(isValidateTxtFormatForMenseki(registDto.getLendMenseki(),true))) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "貸与延面積");
			registDto.setLendMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += "　形式チェック - " + "貸与延面積 - " + registDto.getLendMenseki();
			// 選択タブインデックス設定：部屋情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
		}
		
		//サンルーム延面積（任意入力項目なので、空の場合チェック不要）
		if(NfwStringUtils.isNotEmpty(registDto.getSunRoomMenseki())){
			if (!(isValidateTxtFormatForMenseki(registDto.getSunRoomMenseki(),false))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "サンルーム面積");
				registDto.setSunRoomMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "サンルーム面積 - " + registDto.getSunRoomMenseki();
				// 選択タブインデックス設定：部屋情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
			}
		}
		
		//階段延面積（任意入力項目なので、空の場合チェック不要）
		if(NfwStringUtils.isNotEmpty(registDto.getStairsMenseki())){
			if (!(isValidateTxtFormatForMenseki(registDto.getStairsMenseki(),false))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "階段面積");
				registDto.setStairsMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "階段面積 - " + registDto.getStairsMenseki();
				// 選択タブインデックス設定：部屋情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
			}
		}
		//物置面積（任意入力項目なので、空の場合チェック不要）
		if(NfwStringUtils.isNotEmpty(registDto.getBarnMenseki())){
			if (!(isValidateTxtFormatForMenseki(registDto.getBarnMenseki(),false))) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "物置面積");
				registDto.setBarnMensekiError(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += "　形式チェック - " + "物置面積 - " + registDto.getBarnMenseki();
				// 選択タブインデックス設定：部屋情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ROOM, registDto);
			}
		}
		
		// 駐車場調整金額
		String parkingRentalAdjust = registDto.getParkingRentalAdjust().trim().replace(",", "");
		// 入力値判定
		if (!SkfCheckUtils.isNullOrEmpty(parkingRentalAdjust) && !CheckUtils.isNumberFormat(parkingRentalAdjust)) {
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "駐車場調整金額");
			// 駐車場調整金額エラーを追加
			registDto.setParkingRentalAdjustError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 駐車場調整金額";
			// 選択タブインデックス設定：駐車場情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_PARKING, registDto);
		}
		
		String mailAddress = CodeConstant.DOUBLE_QUOTATION;
		// 管理会社 メールアドレス
		mailAddress = (registDto.getManageMailAddress() != null) ? registDto.getManageMailAddress() : "";
		if(!SkfCheckUtils.isNullOrEmpty(mailAddress) && !mailAddress.matches(MAIL_ADDRESS_CHECK_REG) ){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "管理会社 メールアドレス");
			registDto.setManageMailAddressError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 管理会社 メールアドレス";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 鍵管理者 メールアドレス
		mailAddress = (registDto.getKeyManagerMailAddress() != null) ? registDto.getKeyManagerMailAddress() : "";
		if( !SkfCheckUtils.isNullOrEmpty(mailAddress) && !mailAddress.matches(MAIL_ADDRESS_CHECK_REG)){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "鍵管理者 メールアドレス");
			registDto.setKeyManagerMailAddressErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 鍵管理者 メールアドレス";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}

		String telNo = CodeConstant.DOUBLE_QUOTATION;
		// 管理会社 電話番号
		telNo = (registDto.getManageTelNumber() != null) ? registDto.getManageTelNumber() : "";
		if( !SkfCheckUtils.isNullOrEmpty(telNo) && !telNo.matches(MANAGE_TELNO_CHECK_REG)){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "管理会社 電話番号");
			registDto.setManageTelNumberError(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 管理会社 電話番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}
		// 鍵管理者 電話番号
		telNo = (registDto.getKeyManagerTelNumber() != null) ? registDto.getKeyManagerTelNumber() : "";
		if( !SkfCheckUtils.isNullOrEmpty(telNo) && !telNo.matches(MANAGE_TELNO_CHECK_REG)){
			isCheckOk = false;
			ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "鍵管理者 電話番号");
			registDto.setKeyManagerTelNumberErr(CodeConstant.NFW_VALIDATION_ERROR);
			debugMessage += " 形式チェック - 鍵管理者 電話番号";
			// 選択タブインデックス設定:管理者情報タブ
			setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_ADMIN, registDto);
		}

		// 契約情報
		if ((drpDwnSelectedMap != null && drpDwnSelectedMap.get("contractNo") != null) 
				&& !SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("contractNo").toString())) {
			// 契約開始日
			String contractStartDate = registDto.getContractStartDay();
			if (!SkfCheckUtils.isSkfDateFormat(contractStartDate,CheckUtils.DateFormatType.YYYYMMDD)) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
				registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 形式チェック - 契約開始日 - " + contractStartDate;
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
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
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
				}catch(ParseException ex){
					isCheckOk = false;
					if(startDate == null){
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
						registDto.setContractStartDayErr(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 形式チェック - 契約開始日 - " + registDto.getContractStartDay();
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					if(endDate == null){
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
						registDto.setContractEndDayErr(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 形式チェック - 契約終了日 - " + registDto.getContractEndDay();
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
				}
			}
			
//			// 経理連携用管理番号
//			if (!CheckUtils.isAlphabetNumericSymbol(registDto.getAssetRegisterNo())) {
//				isCheckOk = false;
//				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "経理連携用管理番号");
//				registDto.setAssetRegisterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
//				debugMessage += " 形式チェック - 経理連携用管理番号 - " +registDto.getAssetRegisterNo();
//				// 選択タブインデックス設定：契約情報タブ
//				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
//			}

			// 経理連携用管理番号
			if (!CheckUtils.isHalfWidth(registDto.getAssetRegisterNo())) {
				isCheckOk = false;
				ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1002, "経理連携用管理番号");
				registDto.setAssetRegisterNoErr(CodeConstant.NFW_VALIDATION_ERROR);
				debugMessage += " 形式チェック - 経理連携用管理番号 - " +registDto.getAssetRegisterNo();
				// 選択タブインデックス設定：契約情報タブ
				setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
			}
		}
		
		//駐車場契約
		//駐車場契約情報
		if ((drpDwnSelectedMap != null && drpDwnSelectedMap.get("parkingContractNo") != null) 
				&& !SkfCheckUtils.isNullOrEmpty(drpDwnSelectedMap.get("parkingContractNo").toString())) {
			//駐車場契約情報有かつ別契約の場合
			if(PARKING_CONTRACT_TYPE2.equals(registDto.getParkingContractType())){
				// 郵便番号
				String parkingZipCode = (registDto.getParkingZipCd() != null) ? registDto.getParkingZipCd() : "";
				if(!CheckUtils.isNumeric(parkingZipCode) && parkingZipCode.length() != 7){
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "郵便番号");
					registDto.setParkingZipCdError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 駐車場郵便番号";
					// 選択タブインデックス設定：基本情報タブ
					setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
				}
//				// 経理連携用管理番号
//				if (!CheckUtils.isAlphabetNumericSymbol(registDto.getParkingAssetRegisterNo())) {
//					isCheckOk = false;
//					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1042, "経理連携用管理番号");
//					registDto.setParkingAssetRegisterNoError(CodeConstant.NFW_VALIDATION_ERROR);
//					debugMessage += " 形式チェック - 駐車場経理連携用管理番号 - " +registDto.getAssetRegisterNo();
//					// 選択タブインデックス設定：契約情報タブ
//					setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
//				}
				// 経理連携用管理番号
				if (!CheckUtils.isHalfWidth(registDto.getParkingAssetRegisterNo())) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1002, "経理連携用管理番号");
					registDto.setParkingAssetRegisterNoError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 駐車場経理連携用管理番号 - " +registDto.getAssetRegisterNo();
					// 選択タブインデックス設定：契約情報タブ
					setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
				}
				// 契約開始日
				String parkingContractStartDate = registDto.getParkingContractStartDay().replace("/", "");
				if (!SkfCheckUtils.isSkfDateFormat(parkingContractStartDate,CheckUtils.DateFormatType.YYYYMMDD)) {
					isCheckOk = false;
					ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
					registDto.setParkingContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
					debugMessage += " 形式チェック - 駐車場契約開始日 - " + parkingContractStartDate;
					// 選択タブインデックス設定：契約情報タブ
					setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
				}
				
				// 契約終了日
				String parkingContractEndDate = registDto.getParkingContractEndDay();
				if (!SkfCheckUtils.isNullOrEmpty(parkingContractEndDate)) {
					parkingContractEndDate = parkingContractEndDate.replace("/", "");
					//形式
					if (!SkfCheckUtils.isSkfDateFormat(parkingContractEndDate,CheckUtils.DateFormatType.YYYYMMDD)) {
						isCheckOk = false;
						ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
						registDto.setParkingContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
						debugMessage += " 形式チェック - 駐車場契約終了日 - " + parkingContractEndDate;
						// 選択タブインデックス設定：契約情報タブ
						setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
					}
					
					if(isCheckOk){
						//日付関係
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
						Date startDate = null;
						Date endDate = null;
						try{
							startDate = dateFormat.parse(parkingContractStartDate);
							endDate = dateFormat.parse(parkingContractEndDate);
							
							if(!startDate.before(endDate)){
								//契約開始日＞契約終了日の場合(開始日が終了日より前で無い場合）
								isCheckOk = false;
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
								registDto.setParkingContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック - 駐車場契約終了日(開始日以前) - " +parkingContractEndDate;
								// 選択タブインデックス設定：契約情報タブ
								setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
							}
							
						}catch(ParseException ex){
							isCheckOk = false;
							if(startDate == null){
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約開始日");
								registDto.setParkingContractStartDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック(変換) - 駐車場契約開始日 - " + parkingContractStartDate;
							}
							if(endDate == null){
								ServiceHelper.addErrorResultMessage(registDto, null, MessageIdConstant.E_SKF_1055, "契約終了日");
								registDto.setParkingContractEndDateError(CodeConstant.NFW_VALIDATION_ERROR);
								debugMessage += " 形式チェック(変換) - 駐車場契約終了日 - " + parkingContractEndDate;
							}
							// 選択タブインデックス設定：契約情報タブ
							setDisplayTabIndex(Skf3010Sc006CommonDto.SELECT_TAB_INDEX_CONTRACT, registDto);
						}
					}
					
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
	 * 面積値の形式チェックを行う.
	 * @param txtReq
	 * @param hissu
	 * @return
	 */
	private boolean isValidateTxtFormatForMenseki(String txtReq,boolean hissu){
		
		//半角数値記号チェック
		if (!(CheckUtils.isNumberFormat(txtReq))) {
			LogUtils.debugByMsg("半角数値記号チェックNG：" + txtReq);
			return false;
		}
		
		//数値形式チェック
		String matchPattern="^(0)$|^([1-9]+[0-9]*)$|^([0]\\.[0-9]+)$|^([1-9]+[0-9]*)+(\\.[0-9]+)$";
		if(hissu){
			//必須値の場合（0入力禁止の場合）
			matchPattern="^([1-9]+[0-9]*)$|^([0]\\.[0-9]+)$|^([1-9]+[0-9]*)+(\\.[0-9]+)$";
		}
		if(!txtReq.matches(matchPattern)){
			LogUtils.debugByMsg("数値形式チェックNG：" + txtReq);
			return false;
		}
		
		//整数部の許容桁数チェック
		BigDecimal mensekiValue = new BigDecimal(txtReq);
		mensekiValue = mensekiValue.setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal sen = new BigDecimal("1000");

		if(mensekiValue.compareTo(sen) >= 0){
			LogUtils.debugByMsg("整数部の許容桁数チェックNG：" + txtReq);
			return false;
		}
		//すべて正常
		return true;
	}

	/**
	 * 表示タブインデックス設定
	 * パラメータの指定タブインデックスが設定済みタブインデックスよりも小さい場合のみ、
	 * パラメータの指定タブインデックスを次回表示タブインデックスに設定する
	 * 
	 * @param nextIndex
	 * @param registDto
	 */
	private void setDisplayTabIndex(String nextIndex, Skf3010Sc006RegistDto registDto) {
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
	private Boolean checkAttachedFile(Skf3010Sc006RegistDto registDto) {
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
