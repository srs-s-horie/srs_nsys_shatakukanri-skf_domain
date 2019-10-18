/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc005;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetParkLendJokyoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetParkLendJokyoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetRoomLendJokyoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetRoomLendJokyoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc005.Skf3022Sc005GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiDataKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3022TTeijiData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetParkLendJokyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005GetRoomLendJokyoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc005.Skf3022Sc005UpdateNyutaiyoYoteiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3022TTeijiDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc005.Skf3022Sc005DeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3022Sc005DeleteService 提示データ一覧削除処理クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3022Sc005DeleteService extends BaseServiceAbstract<Skf3022Sc005DeleteDto> {

	@Autowired
	private Skf3022Sc005SharedService skf3022Sc005SharedService;
	@Autowired
	private Skf3022Sc005GetRoomLendJokyoExpRepository skf3022Sc005GetRoomLendJokyoExpRepository;
	@Autowired
	private Skf3022Sc005GetParkLendJokyoExpRepository skf3022Sc005GetParkLendJokyoExpRepository;
	@Autowired
	private Skf3022Sc005UpdateNyutaiyoYoteiExpRepository skf3022Sc005UpdateNyutaiyoYoteiExpRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3022TTeijiDataRepository skf3022TTeijiDataRepository;
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// リストテーブルの１ページ最大表示行数
	@Value("${skf3022.skf3022_sc005.max_row_count}")
	private String listTableMaxRowCount;
	/**
	 * サービス処理を行う。
	 * 
	 * @param deleteDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	@Transactional
	public Skf3022Sc005DeleteDto index(Skf3022Sc005DeleteDto deleteDto) throws Exception {

		deleteDto.setPageTitleKey(MessageIdConstant.SKF3022_SC005_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());
		
		//入退居区分リスト
		String nyutaikyoKbn = deleteDto.getNyutaikyoKbn();
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//社宅提示状況リスト
		String stJyokyo = deleteDto.getStJyokyo();
		List<Map<String, Object>> stJyokyoList = new ArrayList<Map<String, Object>>();
		//社宅提示確認督促リスト
		String stKakunin = deleteDto.getStKakunin();
		List<Map<String, Object>> stKakuninList = new ArrayList<Map<String, Object>>();
		//備品提示状況リスト
		String bhJyokyo = deleteDto.getBhJyokyo();
		List<Map<String, Object>> bhJyokyoList = new ArrayList<Map<String, Object>>();
		//備品提示確認督促リスト
		String bhKakunin = deleteDto.getBhKakunin();
		List<Map<String, Object>> bhKakuninList = new ArrayList<Map<String, Object>>();
		//備品搬入搬出督促リスト
		String moveInOut = deleteDto.getMoveInOut();
		List<Map<String, Object>> moveInOutList = new ArrayList<Map<String, Object>>();
//      'コントロールの設定
		skf3022Sc005SharedService.getDoropDownList(nyutaikyoKbn, nyutaikyoKbnList, stJyokyo, stJyokyoList, stKakunin, stKakuninList, 
				bhJyokyo, bhJyokyoList, bhKakunin, bhKakuninList, moveInOut, moveInOutList);
		
		
		Long delTeijiNo = CodeConstant.LONG_ZERO;
		String delShainNo = CodeConstant.DOUBLE_QUOTATION;
		String delNyutaikyoKbn = CodeConstant.DOUBLE_QUOTATION;
        String updateDate = CodeConstant.DOUBLE_QUOTATION;
        String updateDateNtk = CodeConstant.DOUBLE_QUOTATION;
        String updateDateShataku = CodeConstant.DOUBLE_QUOTATION;
        String updateDateParkOne = CodeConstant.DOUBLE_QUOTATION;
        String updateDateParkTwo = CodeConstant.DOUBLE_QUOTATION;
        Long shatakuNo = CodeConstant.LONG_ZERO;
        Long roomNo = CodeConstant.LONG_ZERO;
        Long parkOne = CodeConstant.LONG_ZERO;
        Long parkTwo = CodeConstant.LONG_ZERO;
		
        delTeijiNo = Long.parseLong(deleteDto.getDelTeijiNo());
        delShainNo = deleteDto.getDelShainNo();
        delNyutaikyoKbn = deleteDto.getDelNyutaikyoKbn();
        updateDate = deleteDto.getDelUpdateDate();
        updateDateNtk = deleteDto.getDelUpdateDateNtk();
        updateDateShataku = deleteDto.getDelUpdateDateShataku();
        updateDateParkOne = deleteDto.getDelUpdateDateParkOne();
        updateDateParkTwo = deleteDto.getDelUpdateDateParkTwo();
        shatakuNo = Long.parseLong(deleteDto.getDelShatakuNo());
        roomNo = Long.parseLong(deleteDto.getDelRoomNo());
        parkOne = Long.parseLong(deleteDto.getDelParkOne());
        parkTwo = Long.parseLong(deleteDto.getDelParkTwo());
		
		int retCount = DeleteTeijiDataCount(delTeijiNo, delShainNo, delNyutaikyoKbn, updateDate,
				updateDateNtk, updateDateShataku, updateDateParkOne, updateDateParkTwo,
				shatakuNo, roomNo, parkOne, parkTwo);
		if(retCount <= 0){
			//排他エラー
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.W_SKF_1009);
		}
		else{
			//成功メッセージ
			ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);
		}
		throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		
		
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();
		
		Skf3022Sc005GetTeijiDataInfoExpParameter param = new Skf3022Sc005GetTeijiDataInfoExpParameter();
		//検索条件の設定
		param.setShainNo(skf3022Sc005SharedService.escapeParameter(deleteDto.getShainNo()));
		param.setShainName(skf3022Sc005SharedService.escapeParameter(deleteDto.getShainName()));
		param.setShatakuName(skf3022Sc005SharedService.escapeParameter(deleteDto.getShatakuName()));
		param.setNyutaikyoKbn(deleteDto.getNyutaikyoKbn());
		param.setStJyokyo(deleteDto.getStJyokyo());
		param.setStKakunin(deleteDto.getStKakunin());
		param.setBhJyokyo(deleteDto.getBhJyokyo());
		param.setBhKakunin(deleteDto.getBhKakunin());
		param.setMoveInOut(deleteDto.getMoveInOut());
		
		
		//再検索
		// リストテーブルの情報を取得
		skf3022Sc005SharedService.getListTableData(param, listTableData);
		//削除時は取得件数見ない
		deleteDto.setListTableMaxRowCount(listTableMaxRowCount);

		//督促ボタンは使用不可に設定
		deleteDto.setBtnShatakuTeijiDisabled("true");
		deleteDto.setBtnBihinTeijiDisabled("true");
		deleteDto.setBtnBihinInOutDisabled("true");
		
		deleteDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		deleteDto.setStJyokyoList(stJyokyoList);
		deleteDto.setStKakuninList(stKakuninList);
		deleteDto.setBhJyokyoList(bhJyokyoList);
		deleteDto.setBhKakuninList(bhKakuninList);
		deleteDto.setMoveInOutList(moveInOutList);
		deleteDto.setListTableData(listTableData);

		return deleteDto;
	}

	/**
	 * 提示データテーブルを削除するメソッド
	 * @param teijiNo
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param updateDateForRock
	 * @param updateDateNtk
	 * @param updateDateShataku
	 * @param updateDateParkOne
	 * @param updateDateParkTwo
	 * @param shatakuNo
	 * @param roomNo
	 * @param parkOne
	 * @param parkTwo
	 * @return 件数
	 */
	private int DeleteTeijiDataCount(Long teijiNo, String shainNo, String nyutaikyoKbn, String updateDateForRock,
			String updateDateNtk, String updateDateShataku, String updateDateParkOne, String updateDateParkTwo,
			Long shatakuNo, Long roomNo, Long parkOne, Long parkTwo) throws Exception{
        //'更新件数
        int retCount = 0;
        int ret = 0;
        String roomCount = CodeConstant.DOUBLE_QUOTATION;
        String parkOneCount = CodeConstant.DOUBLE_QUOTATION;
        String parkTwoCount = CodeConstant.DOUBLE_QUOTATION;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        
//        '社宅部屋マスタテーブル更新
        if(!SkfCheckUtils.isNullOrEmpty(updateDateShataku)){
			
        	String lendJokyo = CodeConstant.DOUBLE_QUOTATION;
        	List<Skf3022Sc005GetRoomLendJokyoExp> lendJokyoList = new ArrayList<Skf3022Sc005GetRoomLendJokyoExp>();
        	Skf3022Sc005GetRoomLendJokyoExpParameter lendParam = new Skf3022Sc005GetRoomLendJokyoExpParameter();
        	lendParam.setShatakuNo(shatakuNo);
        	lendParam.setRoomNo(roomNo);
        	//貸与状況取得
        	lendJokyoList = skf3022Sc005GetRoomLendJokyoExpRepository.getRoomLendJokyo(lendParam);
        	lendJokyo = lendJokyoList.get(0).getLendJokyoKbn();
        	//社宅部屋情報更新
        	Skf3010MShatakuRoom roomRecord = new Skf3010MShatakuRoom();
        	roomRecord.setShatakuKanriNo(shatakuNo);
        	roomRecord.setShatakuRoomKanriNo(roomNo);
        	roomRecord.setLendJokyo(lendJokyo);
    		try{
    			//更新日
    			Date shatakuUpdateDate = dateFormat.parse(updateDateShataku);	
    			LogUtils.debugByMsg("updateDateShataku：" + shatakuUpdateDate);
    			roomRecord.setLastUpdateDate(shatakuUpdateDate);
    		}	
    		catch(ParseException ex){
    			return 0;
    		}
        	
        	ret = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(roomRecord);
//          '更新できなかった場合
        	if(ret <= 0){
//              'トランザクションをロールバックする
        		return 0;
        	}else{
        		retCount = ret;
        	}
        }
        

//        '社宅駐車場区画マスタテーブル更新
        if(!SkfCheckUtils.isNullOrEmpty(updateDateParkOne)){

        	String lendJokyo = CodeConstant.DOUBLE_QUOTATION;
        	List<Skf3022Sc005GetParkLendJokyoExp> lendJokyoList = new ArrayList<Skf3022Sc005GetParkLendJokyoExp>();
        	Skf3022Sc005GetParkLendJokyoExpParameter parkParam = new Skf3022Sc005GetParkLendJokyoExpParameter();
        	parkParam.setShatakuNo(shatakuNo);
        	parkParam.setParkingNo(parkOne);
        	//貸与状況取得
        	lendJokyoList = skf3022Sc005GetParkLendJokyoExpRepository.getParkLendJokyo(parkParam);
        	lendJokyo = lendJokyoList.get(0).getLendJokyoKbn();
        	
        	Skf3010MShatakuParkingBlock parkRecord = new Skf3010MShatakuParkingBlock();
        	parkRecord.setShatakuKanriNo(shatakuNo);
        	parkRecord.setParkingKanriNo(parkOne);
        	parkRecord.setParkingLendJokyo(lendJokyo);
    		try{
    			//更新日
    			Date parkOneUpdateDate = dateFormat.parse(updateDateParkOne);	
    			LogUtils.debugByMsg("updateDateParkOne：" + parkOneUpdateDate);
    			parkRecord.setLastUpdateDate(parkOneUpdateDate);
    		}	
    		catch(ParseException ex){
    			return 0;
    		}
        	//社宅駐車場区画更新
        	ret = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(parkRecord);
        	
//          '更新できなかった場合
        	if(ret <= 0){
//              'トランザクションをロールバックする
        		return 0;
        	}else{
        		retCount = ret;
        	}
        }
        
//        '社宅駐車場区画マスタテーブル更新
        if(!SkfCheckUtils.isNullOrEmpty(updateDateParkTwo)){

        	String lendJokyo = CodeConstant.DOUBLE_QUOTATION;
        	List<Skf3022Sc005GetParkLendJokyoExp> lendJokyoList = new ArrayList<Skf3022Sc005GetParkLendJokyoExp>();
        	Skf3022Sc005GetParkLendJokyoExpParameter parkParam = new Skf3022Sc005GetParkLendJokyoExpParameter();
        	parkParam.setShatakuNo(shatakuNo);
        	parkParam.setParkingNo(parkTwo);
        	//貸与状況取得
        	lendJokyoList = skf3022Sc005GetParkLendJokyoExpRepository.getParkLendJokyo(parkParam);
        	lendJokyo = lendJokyoList.get(0).getLendJokyoKbn();
        	
        	Skf3010MShatakuParkingBlock parkRecord = new Skf3010MShatakuParkingBlock();
        	parkRecord.setShatakuKanriNo(shatakuNo);
        	parkRecord.setParkingKanriNo(parkTwo);
        	parkRecord.setParkingLendJokyo(lendJokyo);
    		try{
    			//更新日
    			Date parkTwoUpdateDate = dateFormat.parse(updateDateParkTwo);	
    			LogUtils.debugByMsg("updateDateParkTwo：" + parkTwoUpdateDate);
    			parkRecord.setLastUpdateDate(parkTwoUpdateDate);
    		}	
    		catch(ParseException ex){
    			return 0;
    		}
        	//社宅駐車場区画更新
        	ret = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(parkRecord);
        	
//          '更新できなかった場合
        	if(ret <= 0){
//              'トランザクションをロールバックする
        		return 0;
        	}else{
        		retCount = ret;
        	}
        }

        //提示データ削除
        Skf3022TTeijiData teijiData = new Skf3022TTeijiData();
        teijiData = skf3022TTeijiDataRepository.selectByPrimaryKey(teijiNo);
        
        if(teijiData == null){
        	//取得0件の場合排他エラー
        	return 0;
        }
        
        Date teijiUpdateDate = null;
        try{
			//更新日
			teijiUpdateDate = dateFormat.parse(updateDateForRock);	
			LogUtils.debugByMsg("updateDateForRock：" + teijiUpdateDate);
		}	
		catch(ParseException ex){
			return 0;
		}
//      '楽観的排他を行う(提示データテーブル)
		LogUtils.debugByMsg("teijiDataupdateDate：" + teijiData.getUpdateDate());
		super.checkLockException(teijiUpdateDate, teijiData.getUpdateDate());
		
		ret = skf3022TTeijiDataRepository.deleteByPrimaryKey(teijiNo);
		
//      '更新できなかった場合
    	if(ret <= 0){
//          'トランザクションをロールバックする
    		return 0;
    	}else{
    		retCount = ret;
    	}

    	//入退居予定データ更新
    	Skf3021TNyutaikyoYoteiDataKey key = new Skf3021TNyutaikyoYoteiDataKey();
    	key.setShainNo(shainNo);
    	key.setNyutaikyoKbn(nyutaikyoKbn);
    	//排他確認用データ取得
    	Skf3021TNyutaikyoYoteiData tmpRecord = new Skf3021TNyutaikyoYoteiData();
    	tmpRecord = skf3021TNyutaikyoYoteiDataRepository.selectByPrimaryKey(key);
    	if(tmpRecord == null){
    		//取得0件の場合排他エラー
    		return 0;
    	}
    	
    	Skf3021TNyutaikyoYoteiData yoteiRecord = new Skf3021TNyutaikyoYoteiData();
    	yoteiRecord.setShainNo(shainNo);
    	yoteiRecord.setNyutaikyoKbn(nyutaikyoKbn);

        Date nykUpdateDate = null;
        try{
			//更新日
			nykUpdateDate = dateFormat.parse(updateDateNtk);	
			LogUtils.debugByMsg("updateDateNtk：" + nykUpdateDate);
		}	
		catch(ParseException ex){
			return 0;
		}
		LogUtils.debugByMsg("NyutaikyoDataUpdateDate：" + tmpRecord.getUpdateDate());
		super.checkLockException(nykUpdateDate, tmpRecord.getUpdateDate());
		
    	yoteiRecord.setLastUpdateDate(nykUpdateDate);
    	
    	//入退居予定データ更新
    	ret = skf3022Sc005UpdateNyutaiyoYoteiExpRepository.updateNyutaiyoYotei(yoteiRecord);

//      '更新できなかった場合
    	if(ret <= 0){
//          'トランザクションをロールバックする
    		return 0;
    	}else{
    		retCount = ret;
    	}

//        '更新できた場合
        if( 0 < retCount){
        	return retCount;
        }

		return retCount;
	}
}
