/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc001;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetChoshoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc001.Skf3021Sc001GetTaikyoReportInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3010MShatakuRoom;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiDataKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc001.Skf3021Sc001GetTaikyoReportInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3010MShatakuRoomRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc001.Skf3021Sc001DeleteDto;

/**
 * 入退居予定一覧画面のDeleteサービス処理クラス。　 
 * 
 */
@Service
public class Skf3021Sc001DeleteService extends BaseServiceAbstract<Skf3021Sc001DeleteDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	@Autowired
	private Skf3010MShatakuRoomRepository skf3010MShatakuRoomRepository;
	@Autowired
	private Skf3021Sc001SharedService skf3021Sc001SharedService;
	@Autowired
	private Skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository;
	@Autowired
	private Skf3021Sc001GetTaikyoReportInfoExpRepository skf3021Sc001GetTaikyoReportInfoExpRepository;
	
	//ボタン押下区分：初期化
	private static final int BTNFLG_INIT = 0;
	//ボタン押下区分：検索ボタン押下
	private static final int BTNFLG_KENSAKU = 1;

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
	public Skf3021Sc001DeleteDto index(Skf3021Sc001DeleteDto deleteDto) throws Exception {
		
		deleteDto.setPageTitleKey(MessageIdConstant.SKF3021_SC001_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPageId());
		

		//日付形式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		
		//入退居区分リスト
		List<Map<String, Object>> nyutaikyoKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請状況リスト
		List<Map<String, Object>> nyuTaikyoShinseiJokyoList = new ArrayList<Map<String, Object>>();
		//特殊事情リスト
		List<Map<String, Object>> tokushuJijoList = new ArrayList<Map<String, Object>>();
		//提示データ作成区分リスト(提示対象)
		List<Map<String, Object>> teijiDetaSakuseiKbnList = new ArrayList<Map<String, Object>>();
		//入退居申請督促リスト
		List<Map<String, Object>> nyuTaikyoShinseiTokusokuList = new ArrayList<Map<String, Object>>();
		
		//getDropDownList
		skf3021Sc001SharedService.getDropDownList(deleteDto.getNyutaikyoKbn(), nyutaikyoKbnList,
				deleteDto.getTeijiDetaSakuseiKbn(), teijiDetaSakuseiKbnList,
				deleteDto.getNyuTaikyoShinseiJokyo() , nyuTaikyoShinseiJokyoList,
				deleteDto.getNyuTaikyoShinseiTokusoku() , nyuTaikyoShinseiTokusokuList,
				deleteDto.getTokushuJijo() , tokushuJijoList);
		//リスト情報
		deleteDto.setNyutaikyoKbnList(nyutaikyoKbnList);
		deleteDto.setTeijiDetaSakuseiKbnList(teijiDetaSakuseiKbnList);
		deleteDto.setNyuTaikyoShinseiJokyoList(nyuTaikyoShinseiJokyoList);
		deleteDto.setNyuTaikyoShinseiTokusokuList(nyuTaikyoShinseiTokusokuList);
		deleteDto.setTokushuJijoList(tokushuJijoList);
		

		//削除ボタン
		//入退居予定更新日時（hidden変数）
		String hdnNyutaikyoYoteiUpdateDate = deleteDto.getHdnNyutaikyoYoteiUpdateDate();
		//転任者調書更新日時（hidden変数）
		String hdnTenninshaChoshoUpdateDate = deleteDto.getHdnTenninshaChoshoUpdateDate();
		Date nyutaikyoYoteiUpdateDate = null;
		try{
			nyutaikyoYoteiUpdateDate = dateFormat.parse(hdnNyutaikyoYoteiUpdateDate);	
			LogUtils.debugByMsg("hdnUpdateDate：" + nyutaikyoYoteiUpdateDate);
		}	
		catch(ParseException ex){
			LogUtils.debugByMsg("入退居予定データ-更新日時変換エラー :" + hdnNyutaikyoYoteiUpdateDate);
		}
		Date tenninshaChoshoUpdateDate = null;
		if(!SkfCheckUtils.isNullOrEmpty(hdnTenninshaChoshoUpdateDate)){
			try{
				tenninshaChoshoUpdateDate = dateFormat.parse(hdnTenninshaChoshoUpdateDate);	
				LogUtils.debugByMsg("hdnUpdateDate：" + tenninshaChoshoUpdateDate);
			}	
			catch(ParseException ex){
				LogUtils.debugByMsg("転任者調書-更新日時変換エラー :" + hdnTenninshaChoshoUpdateDate);
			}
		}
		
		//削除処理
		int res = deleteNyutaikyoYoteiInfo(deleteDto.getDelShainNo(),
				deleteDto.getDelNyuTaikyoKbn(), deleteDto.getDelApplNo(), CodeConstant.C001, 
				nyutaikyoYoteiUpdateDate, tenninshaChoshoUpdateDate);
		
		if(res == 0){
			ServiceHelper.addErrorResultMessage(deleteDto, null,  MessageIdConstant.E_SKF_1076);
			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}else if(res == -1){
			ServiceHelper.addErrorResultMessage(deleteDto, null,  MessageIdConstant.W_SKF_1009);
			throwBusinessExceptionIfErrors(deleteDto.getResultMessages());
		}else if(res > 0){
			ServiceHelper.addResultMessage(deleteDto, MessageIdConstant.I_SKF_1013);
		}
		
		//再検索
		// リストデータ取得用
		List<Map<String, Object>> listTableData = new ArrayList<Map<String, Object>>();

		//入退居予定を取得（全件）
		skf3021Sc001SharedService.setGrvNyuTaikyoYoteiIchiran(BTNFLG_INIT, deleteDto, listTableData);

		//一覧情報
		deleteDto.setListTableData(listTableData);

		
		return deleteDto;
	}
	

	/**
	 * 入退居予定情報削除
	 * @param shainNo 社員番号
	 * @param nyuTaikyoKubun 入退居区分
	 * @param applNo 申請書類管理番号
	 * @param companyCd 会社コード
	 * @param nyutaikyoYoteiUpdateDate 入退居予定更新日
	 * @param tenninshaChoshoUpdateDate 転任者調書更新日
	 * @return
	 */
	private int deleteNyutaikyoYoteiInfo(String shainNo,
			String nyuTaikyoKubun, String applNo, String companyCd, 
			Date nyutaikyoYoteiUpdateDate, Date tenninshaChoshoUpdateDate){
		//入退居予定データ削除件数
		int delCount = 0;
		//転任者調書データ更新件数
		int updCount = 0;
		
		int updCountSR=0;
		
		Skf3021TNyutaikyoYoteiDataKey nyKey = new Skf3021TNyutaikyoYoteiDataKey();
		nyKey.setShainNo(shainNo);
		nyKey.setNyutaikyoKbn(nyuTaikyoKubun);
		
		//排他処理
		Skf3021TNyutaikyoYoteiData targetDtNY = skf3021TNyutaikyoYoteiDataRepository.selectByPrimaryKey(nyKey);
		if(targetDtNY != null){
			//データが存在する場合排他チェック
			super.checkLockException(nyutaikyoYoteiUpdateDate, targetDtNY.getUpdateDate());
			
			//入退居予定情報を削除
			delCount = skf3021TNyutaikyoYoteiDataRepository.deleteByPrimaryKey(nyKey);
		}else{
			//排他エラーメッセージ
			return -1;
		}
		
		
		if(tenninshaChoshoUpdateDate != null){
			//排他処理
			Skf3020TTenninshaChoshoData targetDtTC = skf3020TTenninshaChoshoDataRepository.selectByPrimaryKey(shainNo);
			if(targetDtTC != null){
				//データが存在する場合
				super.checkLockException(tenninshaChoshoUpdateDate, targetDtTC.getUpdateDate());
				
				//転任者調書データを更新
				updCount = updateTenninshaChoshoInfo(shainNo,nyuTaikyoKubun,tenninshaChoshoUpdateDate,targetDtTC);
			}else{
				//排他エラーメッセージ
				return -1;
			}
		}
		
		//入退居区分が「2：退居」かつ、申請書類管理番号が空白でない場合
		if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyuTaikyoKubun) && !SkfCheckUtils.isNullOrEmpty(applNo)){
			//入居希望等調書・入居決定通知情報を取得
			Skf3021Sc001GetChoshoInfoExpParameter choshoParam = new Skf3021Sc001GetChoshoInfoExpParameter();
			choshoParam.setCompanyCd(companyCd);
			choshoParam.setApplNo(applNo);
			List<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp> dtNCT = new ArrayList<Skf3021Sc001GetNyukyoChoshoTsuchiInfoExp>();
			//GetNyukyoChoshoTsuchiInfo
			dtNCT = skf3021Sc001GetNyukyoChoshoTsuchiInfoExpRepository.getNyukyoChoshoTsuchiInfo(choshoParam);
			//取得件数が0件の場合
			if(dtNCT.size() == 0){
				//退居（自動車の保管場所返還）届情報を取得
				List<Skf3021Sc001GetTaikyoReportInfoExp> dtTR = new ArrayList<Skf3021Sc001GetTaikyoReportInfoExp>();
				dtTR = skf3021Sc001GetTaikyoReportInfoExpRepository.getTaikyoReportInfo(choshoParam);
				
				if(dtTR.size() > 0){
					Skf3010MShatakuRoom roomData = new Skf3010MShatakuRoom();
					roomData.setShatakuKanriNo(dtTR.get(0).getShatakuNo());
					roomData.setShatakuRoomKanriNo(dtTR.get(0).getRoomKanriNo());
					roomData.setLastUpdateDate(dtTR.get(0).getUpdateDateRoom());
					roomData.setLendJokyo("2");
					updCountSR = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(roomData);
				}else{
					return -1;
				}
			}else{
				Skf3010MShatakuRoom roomData = new Skf3010MShatakuRoom();
				roomData.setShatakuKanriNo(dtNCT.get(0).getShatakuNo());
				roomData.setShatakuRoomKanriNo(dtNCT.get(0).getRoomKanriNo());
				roomData.setLastUpdateDate(dtNCT.get(0).getUpdateDateRoom());
				roomData.setLendJokyo("2");
				updCountSR = skf3010MShatakuRoomRepository.updateByPrimaryKeySelective(roomData);
			}
		}
		
		if(delCount <= 0){
			//削除エラーメッセージ
			return 0;
		}
		
		
		return delCount + updCount + updCountSR;
	}
	
	/**
	 * 転任者調書情報更新
	 * @param shainNo 社員番号
	 * @param nyuTaikyoKbn 入退居区分
	 * @param tenninshaChoshoUpdateDate 更新日
	 * @param targetDtTC
	 * @return 更新件数
	 */
	private int updateTenninshaChoshoInfo(String shainNo, String nyuTaikyoKbn, 
			Date tenninshaChoshoUpdateDate,Skf3020TTenninshaChoshoData targetDtTC){
		int updateCount = 0;
		Skf3020TTenninshaChoshoData updData = new Skf3020TTenninshaChoshoData();
		if(CodeConstant.NYUTAIKYO_KBN_NYUKYO.equals(nyuTaikyoKbn)){
			//--「入退居区分」が「1：入居」指定された場合
			updData.setNyukyoFlg("0");
		}else if(CodeConstant.NYUTAIKYO_KBN_TAIKYO.equals(nyuTaikyoKbn)){
			//--「入退居区分」が「2：退居」指定された場合
			updData.setTaikyoFlg("0");
		}else if(CodeConstant.NYUTAIKYO_KBN_HENKO.equals(nyuTaikyoKbn)){
			//--「入退居区分」が「3：変更」指定された場合
			updData.setHenkouFlg("0");
		}
		
		String nyutaikyoYoteiKbn = "1";
		int nyukyoFlg = 0;
		int taikyoFlg = 0;
		int henkouFlg = 0;
		if(targetDtTC.getNyukyoFlg() != null){
			nyukyoFlg = Integer.parseInt(targetDtTC.getNyukyoFlg()); 
		}
		if(targetDtTC.getTaikyoFlg() != null){
			taikyoFlg = Integer.parseInt(targetDtTC.getTaikyoFlg()); 
		}
		if(targetDtTC.getHenkouFlg() != null){
			henkouFlg = Integer.parseInt(targetDtTC.getHenkouFlg()); 
		}
		if((nyukyoFlg+taikyoFlg+henkouFlg)==1){
			nyutaikyoYoteiKbn  ="0";
		}
		updData.setNyutaikyoYoteiKbn(nyutaikyoYoteiKbn);
		//sql.Append("    ,   NYUTAIKYO_YOTEI_KBN = ")
		//sql.Append("        (case when (NVL(NYUKYO_FLG, 0)+NVL(TAIKYO_FLG, 0)+NVL(HENKOU_FLG, 0))=1 then '0' else '1' end)")
		
		updData.setShainNo(shainNo);
		updData.setLastUpdateDate(tenninshaChoshoUpdateDate);
		
		updateCount = skf3020TTenninshaChoshoDataRepository.updateByPrimaryKeySelective(updData);
		
		return updateCount;
	}
}
