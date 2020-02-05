/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002DeleteRirekiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiDataTeijiNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiDataTeijiNoExpParameter;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlock;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuParkingBlockKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoomKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutual;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuMutualRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShatakuRentalRirekiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShozokuRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3030TShozokuRirekiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002DeleteNyutaikyoDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002DeleteTeijiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002DeleteTsukibetsuBihinRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002DeleteTsukibetsuChushajoRirekiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3030Sc002.Skf3030Sc002GetTeijiDataTeijiNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuParkingBlockRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuMutualRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShatakuRentalRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3030TShozokuRirekiRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030Sc002common.Skf3030Sc002CommonDto;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002DeleteDto;
import jp.co.intra_mart.mirage.integration.guice.Transactional;

/**
 * Skf3030Sc002DeleteService 入退居情報登録画面の削除処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3030Sc002DeleteService extends BaseServiceAbstract<Skf3030Sc002DeleteDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3010MShatakuParkingBlockRepository skf3010MShatakuParkingBlockRepository;
	@Autowired
	private Skf3030TShatakuLedgerRepository skf3030TShatakuLedgerRepository;
	@Autowired
	private Skf3030TShatakuRentalRirekiRepository skf3030TShatakuRentalRirekiRepository;
	@Autowired
	private Skf3030TShatakuBihinRepository skf3030TShatakuBihinRepository;
	@Autowired
	private Skf3030TShatakuMutualRepository skf3030TShatakuMutualRepository;
	@Autowired
	private Skf3030TShatakuMutualRirekiRepository skf3030TShatakuMutualRirekiRepository;
	@Autowired
	private Skf3030TShozokuRirekiRepository skf3030TShozokuRirekiRepository;
	@Autowired
	private Skf3030Sc002DeleteTsukibetsuBihinRirekiExpRepository skf3030Sc002DeleteTsukibetsuBihinRirekiExpRepository;
	@Autowired
	private Skf3030Sc002DeleteTsukibetsuChushajoRirekiExpRepository skf3030Sc002DeleteTsukibetsuChushajoRirekiExpRepository;
	@Autowired
	private Skf3030Sc002GetTeijiDataTeijiNoExpRepository skf3030Sc002GetTeijiDataTeijiNoExpRepository;
	@Autowired
	private Skf3030Sc002DeleteNyutaikyoDataExpRepository skf3030Sc002DeleteNyutaikyoDataExpRepository;
	@Autowired
	private Skf3030Sc002DeleteTeijiBihinDataExpRepository skf3030Sc002DeleteTeijiBihinDataExpRepository;
	
	
	/**
	 * サービス処理を行う。　
	 * 
	 * @param deleteDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	@Transactional
	public Skf3030Sc002DeleteDto index(Skf3030Sc002DeleteDto deleteDto) throws Exception {
		
		deleteDto.setPageTitleKey(MessageIdConstant.SKF3030_SC002_TITLE);
 		
		// デバッグログ
		LogUtils.debugByMsg("削除");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);
		// ドロップダウンリスト
		List<Map<String, Object>> sc006SogoRyojokyoSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006TaiyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KariukeKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoHanteiKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006HaizokuKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006OldKaisyaNameSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyuyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyojyusyaKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006YakuinSanteiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyoekihiPayMonthSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeInSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeOutSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinShatakuSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinKyoekihiSelectList = new ArrayList<Map<String, Object>>();
		//可変ラベル値
		skf3030Sc002SharedService.setVariableLabel(skf3030Sc002SharedService.jsonArrayToArrayList(deleteDto.getJsonLabelList()), deleteDto);
		// ドロップダウンリスト作成
		skf3030Sc002SharedService.setDdlControlValues(
		deleteDto.getSc006KyojyusyaKbnSelect(), sc006KyojyusyaKbnSelectList,
		deleteDto.getSc006YakuinSanteiSelect(), sc006YakuinSanteiSelectList,
		deleteDto.getSc006KyoekihiPayMonthSelect(), sc006KyoekihiPayMonthSelectList,
		deleteDto.getSc006KibouTimeInSelect(), sc006KibouTimeInSelectList,
		deleteDto.getSc006KibouTimeOutSelect(), sc006KibouTimeOutSelectList,
		deleteDto.getSc006SogoRyojokyoSelect(), sc006SogoRyojokyoSelectList,
		deleteDto.getSc006SogoHanteiKbnSelect(), sc006SogoHanteiKbnSelectList,
		deleteDto.getSc006SokinShatakuSelect(), sc006SokinShatakuSelectList,
		deleteDto.getSc006SokinKyoekihiSelect(), sc006SokinKyoekihiSelectList,
		deleteDto.getSc006OldKaisyaNameSelect(), sc006OldKaisyaNameSelectList,
		deleteDto.getSc006KyuyoKaisyaSelect(), sc006KyuyoKaisyaSelectList,
		deleteDto.getSc006HaizokuKaisyaSelect(), sc006HaizokuKaisyaSelectList,
		deleteDto.getSc006TaiyoKaisyaSelect(), sc006TaiyoKaisyaSelectList,
		deleteDto.getSc006KariukeKaisyaSelect(), sc006KariukeKaisyaSelectList);

		// ドロップダウンリスト設定
		deleteDto.setSc006KyojyusyaKbnSelectList(sc006KyojyusyaKbnSelectList);
		deleteDto.setSc006YakuinSanteiSelectList(sc006YakuinSanteiSelectList);
		deleteDto.setSc006KyoekihiPayMonthSelectList(sc006KyoekihiPayMonthSelectList);
		deleteDto.setSc006KibouTimeInSelectList(sc006KibouTimeInSelectList);
		deleteDto.setSc006KibouTimeOutSelectList(sc006KibouTimeOutSelectList);
		deleteDto.setSc006SogoRyojokyoSelectList(sc006SogoRyojokyoSelectList);
		deleteDto.setSc006SogoHanteiKbnSelectList(sc006SogoHanteiKbnSelectList);
		deleteDto.setSc006SokinShatakuSelectList(sc006SokinShatakuSelectList);
		deleteDto.setSc006SokinKyoekihiSelectList(sc006SokinKyoekihiSelectList);
		deleteDto.setSc006OldKaisyaNameSelectList(sc006OldKaisyaNameSelectList);
		deleteDto.setSc006KyuyoKaisyaSelectList(sc006KyuyoKaisyaSelectList);
		deleteDto.setSc006HaizokuKaisyaSelectList(sc006HaizokuKaisyaSelectList);
		deleteDto.setSc006TaiyoKaisyaSelectList(sc006TaiyoKaisyaSelectList);
		deleteDto.setSc006KariukeKaisyaSelectList(sc006KariukeKaisyaSelectList);

		//削除処理
		int returnCount = deleteData(Long.parseLong(deleteDto.getHdnShatakuKanriId()),
				deleteDto.getHdnNengetsu(),
				Long.parseLong(deleteDto.getHdnShatakuKanriNo()),
				Long.parseLong(deleteDto.getHdnShatakuRoomKanriNo()),
				skf3030Sc002SharedService.getTbtPublicShatakuRoomDataColumnInfoList(Skf3030Sc002CommonDto.STR_DELETE,deleteDto),
				skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(1, true, deleteDto),
				skf3030Sc002SharedService.getTbtPublicShatakuParkingBlockDataColumnInfoList(2, true, deleteDto),
				deleteDto.getSc006ShainNo());
		
		if(returnCount == 0){
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}else{
			ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);
			
			//削除後動作エラー回避のためDTOクリア
			deleteDto.setHdnShatakuKanriId("");
			deleteDto.setHdnShatakuKanriNo("");
			deleteDto.setHdnShatakuRoomKanriNo("");
			deleteDto.setSc006ShainNo("");
			deleteDto.setSc006SiyoryoPatName("");
			deleteDto.setSc006ShainName("");
			deleteDto.setSc006SiyoryoMonthPay("");
			
			//社宅管理台帳画面へ遷移
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3030_SC001, "init");
			deleteDto.setTransferPageInfo(nextPage);
		}
		
		
		return deleteDto;
	}
	
	/**
	 * 削除処理メソッド
	 * @param shatakuKanriId
	 * @param nengetsu
	 * @param shatakuKanriNo
	 * @param shatakuRoomKanriNo
	 * @param shatakuRoomDataColumnInfoList
	 * @param shatakuParkingBlockDataColumnInfoList1
	 * @param shatakuParkingBlockDataColumnInfoList2
	 * @param shainNo
	 * @return
	 */
	private int deleteData(Long shatakuKanriId,
			String nengetsu,
			Long shatakuKanriNo,
			Long shatakuRoomKanriNo,
			Skf3010MShatakuRoom shatakuRoomDataColumnInfoList,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList1,
			Skf3010MShatakuParkingBlock shatakuParkingBlockDataColumnInfoList2,
			String shainNo){
		int selectCount = 0;
		int deleteCount = 0;
		int returnCount = 0;
		
		//「社宅管理台帳基本テーブル」のデータ削除
		Skf3030TShatakuLedger selectSL = skf3030TShatakuLedgerRepository.selectByPrimaryKey(shatakuKanriId);
		if(selectSL != null){
			deleteCount = skf3030TShatakuLedgerRepository.deleteByPrimaryKey(shatakuKanriId);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「社宅管理台帳備品基本テーブル」のデータ削除
		Skf3030TShatakuBihin selectSB = skf3030TShatakuBihinRepository.selectByPrimaryKey(shatakuKanriId);
		if(selectSB != null){
			deleteCount = skf3030TShatakuBihinRepository.deleteByPrimaryKey(shatakuKanriId);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「社宅管理台帳相互利用基本テーブル」のデータ削除
		Skf3030TShatakuMutual selectSM = skf3030TShatakuMutualRepository.selectByPrimaryKey(shatakuKanriId);
		if(selectSM != null){
			deleteCount = skf3030TShatakuMutualRepository.deleteByPrimaryKey(shatakuKanriId);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別使用料履歴テーブル」のデータ削除
		Skf3030TShatakuRentalRirekiKey srRirekiKey = new Skf3030TShatakuRentalRirekiKey();
		srRirekiKey.setShatakuKanriId(shatakuKanriId);
		srRirekiKey.setYearMonth(nengetsu);
		Skf3030TShatakuRentalRireki selectSRRireki = skf3030TShatakuRentalRirekiRepository.selectByPrimaryKey(srRirekiKey);
		if(selectSRRireki != null){
			deleteCount = skf3030TShatakuRentalRirekiRepository.deleteByPrimaryKey(srRirekiKey);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別所属情報履歴テーブル」のデータ削除
		Skf3030TShozokuRirekiKey shozokuRirekiKey = new Skf3030TShozokuRirekiKey();
		shozokuRirekiKey.setShatakuKanriId(shatakuKanriId);
		shozokuRirekiKey.setYearMonth(nengetsu);
		Skf3030TShozokuRireki selectShozoku = skf3030TShozokuRirekiRepository.selectByPrimaryKey(shozokuRirekiKey);
		if(selectShozoku != null){
			deleteCount = skf3030TShozokuRirekiRepository.deleteByPrimaryKey(shozokuRirekiKey);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別備品使用料明細テーブル」のデータ削除
		Skf3030Sc002DeleteRirekiExpParameter delRirekiKey = new Skf3030Sc002DeleteRirekiExpParameter();
		delRirekiKey.setShatakuKanriId(shatakuKanriId);
		delRirekiKey.setYearMonth(nengetsu);
		selectCount = skf3030Sc002DeleteTsukibetsuBihinRirekiExpRepository.getShatakuBihinRirikiCount(delRirekiKey);
		if(selectCount > 0){
			deleteCount = skf3030Sc002DeleteTsukibetsuBihinRirekiExpRepository.deleteTsukibetsuBihinRireki(delRirekiKey);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別駐車場履歴テーブル」のデータ削除
		selectCount = skf3030Sc002DeleteTsukibetsuChushajoRirekiExpRepository.getMonthParkingRirekiCount(delRirekiKey);
		if(selectCount > 0){
			deleteCount = skf3030Sc002DeleteTsukibetsuChushajoRirekiExpRepository.deleteTsukibetsuChushajoRireki(delRirekiKey);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「月別相互利用履歴テーブル」のデータ削除
		Skf3030TShatakuMutualRirekiKey smRirekiKey = new Skf3030TShatakuMutualRirekiKey();
		smRirekiKey.setShatakuKanriId(shatakuKanriId);
		smRirekiKey.setYearMonth(nengetsu);
		Skf3030TShatakuMutualRireki selectSMRireki = skf3030TShatakuMutualRirekiRepository.selectByPrimaryKey(smRirekiKey);
		if(selectSMRireki != null){
			deleteCount = skf3030TShatakuMutualRirekiRepository.deleteByPrimaryKey(smRirekiKey);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//「社宅部屋情報マスタ」のデータ更新
		Skf3010MShatakuRoomKey sRoomKey = new Skf3010MShatakuRoomKey();
		sRoomKey.setShatakuKanriNo(shatakuRoomDataColumnInfoList.getShatakuKanriNo());
		sRoomKey.setShatakuRoomKanriNo(shatakuRoomDataColumnInfoList.getShatakuRoomKanriNo());
		Skf3010MShatakuRoom selectRoom = skf3010MShatakuRoomRepository.selectByPrimaryKey(sRoomKey);
		if(selectRoom != null){
			deleteCount = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(shatakuRoomDataColumnInfoList);
			if(0 >= deleteCount ){
				return 0;
			}else{
				returnCount += 1;
			}
		}
		
		//区画１駐車場管理番号（hidden）が値を持つ場合、社宅駐車場区画マスタ更新
		if(shatakuParkingBlockDataColumnInfoList1.getParkingKanriNo() != null){
			Skf3010MShatakuParkingBlockKey spBlock1Key = new Skf3010MShatakuParkingBlockKey();
			spBlock1Key.setShatakuKanriNo(shatakuParkingBlockDataColumnInfoList1.getShatakuKanriNo());
			spBlock1Key.setParkingKanriNo(shatakuParkingBlockDataColumnInfoList1.getParkingKanriNo());
			
			Skf3010MShatakuParkingBlock selectPBlock1 = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(spBlock1Key);
			if(selectPBlock1 != null){
				deleteCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList1);
				if(0 >= deleteCount ){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		
		//区画２駐車場管理番号（hidden）が値を持つ場合、社宅駐車場区画マスタ更新
		if(shatakuParkingBlockDataColumnInfoList2.getParkingKanriNo() != null){
			Skf3010MShatakuParkingBlockKey spBlock2Key = new Skf3010MShatakuParkingBlockKey();
			spBlock2Key.setShatakuKanriNo(shatakuParkingBlockDataColumnInfoList2.getShatakuKanriNo());
			spBlock2Key.setParkingKanriNo(shatakuParkingBlockDataColumnInfoList2.getParkingKanriNo());
			
			Skf3010MShatakuParkingBlock selectPBlock2 = skf3010MShatakuParkingBlockRepository.selectByPrimaryKey(spBlock2Key);
			if(selectPBlock2 != null){
				deleteCount = skf3010MShatakuParkingBlockRepository.updateByPrimaryKeySelective(shatakuParkingBlockDataColumnInfoList2);
				if(0 >= deleteCount ){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		
		//削除対象の「提示データテーブル」取得
		Skf3030Sc002GetTeijiDataTeijiNoExpParameter teijiParam = new Skf3030Sc002GetTeijiDataTeijiNoExpParameter();
		teijiParam.setShatakuKanriId(shatakuKanriId);
		teijiParam.setShainNo(shainNo);
		teijiParam.setShatakuKanriNo(shatakuKanriNo);
		teijiParam.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		List<Skf3030Sc002GetTeijiDataTeijiNoExp> dtTeiji = new ArrayList<Skf3030Sc002GetTeijiDataTeijiNoExp>();
		dtTeiji = skf3030Sc002GetTeijiDataTeijiNoExpRepository.getTeijiDataTeijiNo(teijiParam);
		if(dtTeiji.size() > 0){
			//「提示データテーブル」のデータ削除
			selectCount = skf3030Sc002GetTeijiDataTeijiNoExpRepository.getTeijiDataCount(teijiParam);
			if(selectCount > 0){
				deleteCount = skf3030Sc002GetTeijiDataTeijiNoExpRepository.deleteTeijiData(teijiParam);
				if(0 >= deleteCount ){
					return 0;
				}else{
					returnCount += 1;
				}
			}
			//「入退居予定データテーブル」のデータ削除
			selectCount = skf3030Sc002DeleteNyutaikyoDataExpRepository.getNyutaikyoDataCount(dtTeiji.get(0).getTeijiNo());
			if(selectCount > 0){
				deleteCount = skf3030Sc002DeleteNyutaikyoDataExpRepository.deleteNyutaikyoData(dtTeiji.get(0).getTeijiNo());
				if(0 >= deleteCount ){
					return 0;
				}else{
					returnCount += 1;
				}
			}
			//「提示備品データテーブル」のデータ削除
			selectCount = skf3030Sc002DeleteTeijiBihinDataExpRepository.getTeijiBihinDataCount(dtTeiji.get(0).getTeijiNo());
			if(selectCount > 0){
				deleteCount = skf3030Sc002DeleteTeijiBihinDataExpRepository.deleteTeijiBihinData(dtTeiji.get(0).getTeijiNo());
				if(0 >= deleteCount ){
					return 0;
				}else{
					returnCount += 1;
				}
			}
		}
		
		return returnCount;
	}
	

}
