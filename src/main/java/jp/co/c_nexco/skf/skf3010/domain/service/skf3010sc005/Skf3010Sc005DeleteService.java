/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetHeyaHistoryCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetRoomInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc005.Skf3010Sc005GetTeijiDataCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomBihinKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetHeyaHistoryCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc005.Skf3010Sc005GetTeijiDataCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc005.Skf3010Sc005DeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3010Sc005DeleteService 社宅部屋削除ボタン押下時処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc005DeleteService extends BaseServiceAbstract<Skf3010Sc005DeleteDto> {

	@Autowired
	private Skf3010Sc005SharedService skf3010Sc005SharedService;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010Sc005GetHeyaHistoryCountExpRepository skf3010Sc005GetHeyaHistoryCountExpRepository;
	@Autowired
	private Skf3010Sc005GetTeijiDataCountExpRepository skf3010Sc005GetTeijiDataCountExpRepository;
	@Autowired
	private Skf3010Sc005GetBihinInfoExpRepository skf3010Sc005GetBihinInfoExpRepository;
	@Autowired
	private Skf3010MShatakuRoomBihinRepository skf3010MShatakuRoomBihinRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	/**
	 * 社宅部屋登録画面、削除処理メインメソッド.
	 */
	@Override
	@Transactional
	protected BaseDto index(Skf3010Sc005DeleteDto deleteDto) throws Exception {

		deleteDto.setPageTitleKey(MessageIdConstant.SKF3010_SC005_TITLE);
		int result = 0;
		
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());

		/** 利用実績チェック */
		// 社宅管理台帳存在チェック
		if (getLedgerCount(deleteDto.getHdnShatakuKanriNo(),deleteDto.getHdnRoomKanriNo()) > 0) {
			// レコードが存在する場合エラー
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_3011, deleteDto.getShatakuName(),deleteDto.getRoomNo());

		} 
		//提示データ件数チェック
		else if(getTeijiDataCount(deleteDto.getHdnShatakuKanriNo(),deleteDto.getHdnRoomKanriNo()) > 0){
			// レコードが存在する場合エラー
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_3011, deleteDto.getShatakuName(),deleteDto.getRoomNo());

		}else {
			/** データ物理削除 */
			result = deleteHoyuuSyatakuRoomInfo(deleteDto);
			
			if(result < 0){
				//削除件数が0未満（排他エラー）
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.W_SKF_1009);
			}
			else if(result == 0){
				//削除件数が0（処理エラー）
				ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
			}else{
				//削除成功時、保有社宅部屋一覧画面へ遷移
				//成功メッセージ
				ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);
				/** 画面遷移 */
				deleteDto.setTransferPageInfo(TransferPageInfo.prevPage(FunctionIdConstant.SKF3010_SC004));
			}

			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}

		return deleteDto;
	}

	/**
	 * 社宅管理台帳テーブルより社宅管理番号、社宅部屋管理番号をキーにレコードカウントを取得する。.
	 * 
	 * @param shatakuKanriNo
	 * @param shatakuRoomKanriNo
	 * @return　社宅管理台帳データ件数
	 */
	private long getLedgerCount(String shatakuKanriNo,String shatakuRoomKanriNo) {

		// パラメータセット
		Skf3010Sc005GetHeyaHistoryCountExpParameter param = new Skf3010Sc005GetHeyaHistoryCountExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));

		// SQL実行
		long resultCount = skf3010Sc005GetHeyaHistoryCountExpRepository.getHeyaHistoryCount(param).size();
		LogUtils.debugByMsg("社宅管理台帳テーブル存在レコード数 = " + resultCount + "件");

		return resultCount;

	}

	/**
	 * 提示データテーブルより社宅管理番号、社宅部屋管理番号をキーにレコードカウントを取得する。.
	 * 
	 * @param shatakuKanriNo
	 * @param shatakuRoomKanriNo
	 * @return　提示データ件数
	 */
	private long getTeijiDataCount(String shatakuKanriNo,String shatakuRoomKanriNo) {

		// パラメータセット
		Skf3010Sc005GetTeijiDataCountExpParameter param = new Skf3010Sc005GetTeijiDataCountExpParameter();
		param.setShatakuKanriNo(Long.parseLong(shatakuKanriNo));
		param.setShatakuRoomKanriNo(Long.parseLong(shatakuRoomKanriNo));

		// SQL実行
		long resultCount = skf3010Sc005GetTeijiDataCountExpRepository.getTeijiDataCount(param);
		LogUtils.debugByMsg("提示データテーブル存在レコード数 = " + resultCount + "件");

		return resultCount;

	}
	
	/**
	 * 社宅部屋情報と社宅部屋備品情報を削除する.
	 * 
	 * @param dto
	 * @return　削除件数（合計）
	 */
	private int deleteHoyuuSyatakuRoomInfo(Skf3010Sc005DeleteDto dto) {

		// パラメータセット
		Skf3010MShatakuRoomKey setValue = new Skf3010MShatakuRoomKey();
		setValue.setShatakuKanriNo(Long.parseLong(dto.getHdnShatakuKanriNo()));
		setValue.setShatakuRoomKanriNo(Long.parseLong(dto.getHdnRoomKanriNo()));
		
		//排他チェック
		Skf3010Sc005GetRoomInfoExp resultValue = new Skf3010Sc005GetRoomInfoExp();
		resultValue = skf3010Sc005SharedService.getRoomInfo(dto.getHdnShatakuKanriNo(),	dto.getHdnRoomKanriNo());
		if(resultValue == null){
			LogUtils.debugByMsg("社宅部屋情報取得結果NULL");
			//取得エラー時は排他エラーとして終了
			return -1;
		}
		LogUtils.debugByMsg("setValueLastUpdateDate：" + dto.getLastUpdateDate("Skf3010Sc005GetRoomInfoUpdateDate"));
		LogUtils.debugByMsg("resultValueUpdateDate：" + resultValue.getLastUpdateDate());
		super.checkLockException(dto.getLastUpdateDate("Skf3010Sc005GetRoomInfoUpdateDate"), resultValue.getLastUpdateDate());
		
		// 部屋情報削除SQL実行
		int deleteCount = skf3010MShatakuRoomRepository.deleteByPrimaryKey(setValue);
		LogUtils.debugByMsg("社宅管理番号：" + dto.getHdnShatakuKanriNo() + 
				",部屋番号:" + dto.getHdnRoomKanriNo() + " の、削除レコード数 = " + deleteCount + "件");
		
		if(0 >= deleteCount){
			return 0;
		}
		
		//備品削除
		//排他チェック処理
		boolean checkResult = checkRoomBihinInfoForUpdate(Long.parseLong(dto.getHdnShatakuKanriNo()),
				Long.parseLong(dto.getHdnRoomKanriNo()),dto.getBihinListData());
		boolean checkResultHdn = checkRoomBihinInfoForUpdate(Long.parseLong(dto.getHdnShatakuKanriNo()),
				Long.parseLong(dto.getHdnRoomKanriNo()),dto.getHdnBihinStatusList());
		
		if(!checkResult || !checkResultHdn){
			//排他エラー
			return -1;
		}
		
		//社宅部屋備品情報削除
		Skf3010Sc005GetBihinInfoExpParameter param = new Skf3010Sc005GetBihinInfoExpParameter();
		param.setShatakuKanriNo(Long.parseLong(dto.getHdnShatakuKanriNo()));
		param.setShatakuRoomKanriNo(Long.parseLong(dto.getHdnRoomKanriNo()));
		int bihinDeleteCount = skf3010Sc005GetBihinInfoExpRepository.deleteSyatakuRoomBihin(param);
		LogUtils.debugByMsg("社宅管理番号：" + dto.getHdnShatakuKanriNo() + 
				",部屋番号:" + dto.getHdnRoomKanriNo() + " の、削除備品レコード数 = " + bihinDeleteCount + "件");
		if(0 > bihinDeleteCount){
			return 0;
		}
		
		return deleteCount + deleteCount;
	}
	
	/**
	 * 社宅部屋備品情報の更新日を一括でチェックする.
	 * 不一致がある場合はfalseを返却する.
	 * @param bihinInfoList
	 * @return
	 */
	private boolean checkRoomBihinInfoForUpdate(Long shatakuKanriNo,Long shatakuRoomKanriNo,List<Map<String,Object>> bihinInfoList){
		

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
		
		//備品
		for(Map<String,Object> map : bihinInfoList){
			
			//更新処理を行う。
			//排他チェック
			Skf3010MShatakuRoomBihinKey bihinKey = new Skf3010MShatakuRoomBihinKey();
			bihinKey.setShatakuKanriNo(shatakuKanriNo);
			bihinKey.setShatakuRoomKanriNo(shatakuRoomKanriNo);
			bihinKey.setBihinCd(map.get("bihinCode").toString());
			Skf3010MShatakuRoomBihin bihinInfo = skf3010MShatakuRoomBihinRepository.selectByPrimaryKey(bihinKey);
			if(bihinInfo == null){
				LogUtils.debugByMsg("部屋備品情報取得結果NULL");
				return false;
			}
			try{
				Date mapDate = dateFormat.parse(map.get("updateDate").toString());	
				LogUtils.debugByMsg("mapUpdateDate：" + mapDate);
				LogUtils.debugByMsg("bihinInfoUpdateDate：" + bihinInfo.getUpdateDate());
				super.checkLockException(mapDate, bihinInfo.getUpdateDate());
			}	
			catch(ParseException ex){
				LogUtils.debugByMsg("部屋備品情報-更新日時変換エラー :" + map.get("updateDate").toString());
				return false;
			}			

		}
		
		//更新OK
		return true;
	}

}
