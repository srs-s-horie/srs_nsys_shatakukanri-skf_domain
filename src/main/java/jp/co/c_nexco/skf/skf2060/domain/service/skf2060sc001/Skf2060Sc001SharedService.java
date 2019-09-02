/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001UpdateKariageBukkenTeijiFlgExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001UpdateKariageKohoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukkenKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeiji;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiDetail;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageTeijiFile;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenFileExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001UpdateKariageBukkenTeijiFlgExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetKariageBukkenExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001UpdateKariageKohoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiDetailRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageTeijiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;

/**
 * Skf3090Sc004SharedService 従業員マスタ一覧内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2060Sc001SharedService {


	@Autowired
	private Skf2060Sc001GetKariageBukkenExpRepository skf2060Sc001GetKariageBukkenExpRepository;
	@Autowired
	private Skf2060Sc001GetKariageBukkenFileExpRepository skf2060Sc001GetKariageBukkenFileExpRepository;
	@Autowired
	private Skf2060Sc001GetApplHistoryExpRepository skf2060Sc001GetApplHistoryExpRepository;
	@Autowired
	private Skf2060Sc001UpdateKariageKohoExpRepository skf2060Sc001UpdateKariageKohoExpRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2060TKariageTeijiRepository skf2060TKariageTeijiRepository;
	@Autowired
	private Skf2060TKariageTeijiDetailRepository skf2060TKariageTeijiDetailRepository;
	@Autowired
	private Skf2060TKariageTeijiFileRepository skf2060TKariageTeijiFileRepository;
	@Autowired
	private Skf2060Sc001GetApplHistoryInfoForUpdateExpRepository skf2060Sc001GetApplHistoryInfoForUpdateExpRepository;	
	@Autowired
	private Skf2060Sc001UpdateKariageBukkenTeijiFlgExpRepository skf2060Sc001UpdateKariageBukkenTeijiFlgExpRepository;	
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private MenuScopeSessionBean sessionBean;
	
	public static List<Skf2060Sc001GetKariageBukkenExp> dataParam;
	public static List<Skf2060Sc001GetKariageBukkenFileExp> dataParam2;
	
	private String companyCd = CodeConstant.C001;

	
	/**
	 * 申請書書類履歴テーブルを取得する。
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applNo
	 * @return 申請書類履歴エンティティ
	 */
	public Skf2060Sc001GetApplHistoryExp getApplHistoryInfo(String companyCd, String shainNo, String applNo){
	
		List<Skf2060Sc001GetApplHistoryExp> resultListTableData = new ArrayList<Skf2060Sc001GetApplHistoryExp>();
		Skf2060Sc001GetApplHistoryExp resultData = new Skf2060Sc001GetApplHistoryExp();
		Skf2060Sc001GetApplHistoryExpParameter param = new Skf2060Sc001GetApplHistoryExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplNo(applNo);

		resultListTableData = skf2060Sc001GetApplHistoryExpRepository.getApplHistory(param);
		
		if(resultListTableData.size() != 0){
			resultData = resultListTableData.get(0);
		}
		
		return resultData;
	}
	
	
	/**
	 * 借上候補物件一覧テーブルリストを取得する。 
	 * 
	 * @param itiranFlg
	 * @return List<Map<String, Object>>型のテーブルリスト
	 */
	public List<Map<String, Object>> getDataParamList(boolean itiranFlg) {
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		// リストテーブルに格納するデータを取得する
		List<Skf2060Sc001GetKariageBukkenExp> resultListTableData = new ArrayList<Skf2060Sc001GetKariageBukkenExp>();
		Skf2060Sc001GetKariageBukkenExpParameter param = new Skf2060Sc001GetKariageBukkenExpParameter();
		String shainNo = CodeConstant.DOUBLE_QUOTATION;
		String applNo = CodeConstant.DOUBLE_QUOTATION;
		//TODO セッション名(´・ω・｀)
		Skf2060Sc001GetApplHistoryExp applInforesultData = (Skf2060Sc001GetApplHistoryExp)sessionBean.get("getApplHistoryResultData");
		if(applInforesultData != null){
			shainNo = applInforesultData.getShainNo();
			applNo = applInforesultData.getApplNo();
		}
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setApplNo(applNo);
		param.setItiranFlg(true);
		resultListTableData = skf2060Sc001GetKariageBukkenExpRepository.getKariageBukken(param);

		// リストテーブルに出力するリストを取得する
		dataParamList.clear();
		dataParamList.addAll(getListTableDataViewColumn(resultListTableData));
		
		return dataParamList;
	}

	/**
	 * リストテーブルに出力するリストを取得する。
	 * 
	 * @param originList
	 * @return リストテーブルに出力するリスト
	 */
	private List<Map<String, Object>> getListTableDataViewColumn(List<Skf2060Sc001GetKariageBukkenExp> originList) {

		List<Map<String, Object>> setViewList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < originList.size(); i++) {
			String linkTag = CodeConstant.NONE;
			List<String> linkTagList = new ArrayList<String>();
			Skf2060Sc001GetKariageBukkenExp tmpData = originList.get(i);
			Map<String, Object> tmpMap = new HashMap<String, Object>();
			//添付ファイル取得
			List<Skf2060Sc001GetKariageBukkenFileExp> fileDataList = new ArrayList<Skf2060Sc001GetKariageBukkenFileExp> ();
			Skf2060Sc001GetKariageBukkenFileExpParameter param = new Skf2060Sc001GetKariageBukkenFileExpParameter();
			param.setCompanyCd(companyCd);
			param.setCandidateNo(tmpData.getCandidateNo());
			fileDataList = skf2060Sc001GetKariageBukkenFileExpRepository.getKariageBukkenFile(param);
			if(fileDataList != null){
				for(Skf2060Sc001GetKariageBukkenFileExp fileData : fileDataList){
					//リンクタグを作成
					String baseLinkTag = this.getLinkTag(String.valueOf(fileData.getCandidateNo()), fileData.getAttachedNo(), fileData.getAttachedName());
					linkTagList.add(baseLinkTag);
				}
				
				linkTag = String.join("<br />", linkTagList);
				
			}
			String insertDate = skfDateFormatUtils.dateFormatFromDate(tmpData.getInsertDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			String deleteBukken = "<span class='im-ui-icon-common-16-trashbox'></span>";
			if(tmpData.getCandidateDate() != null){
				deleteBukken = CodeConstant.DOUBLE_QUOTATION;
			}
			String candidateDate = skfDateFormatUtils.dateFormatFromDate(tmpData.getCandidateDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
			tmpMap.put("teiji", "<input type='checkbox' name='teijiVal' id='teijiVal" + i + "' value='" + i + "'>");
			tmpMap.put("insertDate", HtmlUtils.htmlEscape(insertDate));
			tmpMap.put("candidateDate", HtmlUtils.htmlEscape(candidateDate));
			tmpMap.put("shatakuName", HtmlUtils.htmlEscape(tmpData.getShatakuName()));
			tmpMap.put("address", HtmlUtils.htmlEscape(tmpData.getAddress()));
			tmpMap.put("attachedName", linkTag);
			tmpMap.put("attachedFile", "");
			tmpMap.put("deleteBukken", deleteBukken);
			tmpMap.put("companyCd", HtmlUtils.htmlEscape(tmpData.getCompanyCd()));
			tmpMap.put("candidateNo", tmpData.getCandidateNo());
			tmpMap.put("money", tmpData.getMoney());

			setViewList.add(tmpMap);
		}

		return setViewList;
	}
	
	/**
	 * 借上候補物件が重複していないかチェックを行う。
	 * 
	 * @param companyCd
	 * @param shatakuName
	 * @param address
	 * @return 重複していない場合true　重複している場合false
	 */
	public boolean getKariageBukken(String companyCd, String shatakuName, String address){
		
		boolean repeatCheck = true;
		
		List<Skf2060Sc001GetKariageBukkenExp> resultListTableData = new ArrayList<Skf2060Sc001GetKariageBukkenExp>();
		Skf2060Sc001GetKariageBukkenExpParameter param = new Skf2060Sc001GetKariageBukkenExpParameter();
		param.setCompanyCd(companyCd);
		param.setShatakuName(shatakuName);
		param.setAddress(address);
		
		resultListTableData = skf2060Sc001GetKariageBukkenExpRepository.getKariageBukken(param);
		
		if(resultListTableData.size() != 0){
			repeatCheck = false;
		}
		
		return repeatCheck;
	}
	
	/**
	 * 借上候補物件が存在しているかチェックを行う。
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * @return 存在している場合true　存在していない場合false
	 */
	public boolean getKariageBukken(String companyCd, long candidateNo){
		
		boolean existCheck = false;
		
		List<Skf2060Sc001GetKariageBukkenExp> resultListTableData = new ArrayList<Skf2060Sc001GetKariageBukkenExp>();
		Skf2060Sc001GetKariageBukkenExpParameter param = new Skf2060Sc001GetKariageBukkenExpParameter();
		param.setCompanyCd(companyCd);
		param.setCandidateNo(candidateNo);

		
		resultListTableData = skf2060Sc001GetKariageBukkenExpRepository.getKariageBukken(param);
		
		if(resultListTableData.size() != 0){
			existCheck = true;
		}
		
		return existCheck;
	}
	
	/**
	 * 借上候補物件テーブルへ登録を行う
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * @param address
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertKariageKohoInfo(String companyCd, String shatakuName, String address){	
		boolean insertCheck = true;
		int insertCount = 0;
		
		Skf2060TKariageBukken insertData = new Skf2060TKariageBukken();
		
		insertData.setCompanyCd(companyCd);
		insertData.setShatakuName(shatakuName);
		insertData.setAddress(address);
		insertData.setTeijiFlg("0");
		
		insertCount = skf2060TKariageBukkenRepository.insertSelective(insertData);
		
		if (insertCount <= 0) {
			insertCheck = false;
		}
		
		return insertCheck;
		
	}
	
	/**
	 * 申請書類管理番号を取得する
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applDate
	 * @param applId
	 * @return 作成した申請書類管理番号
	 */
	public String getApplNo(String companyCd, String shainNo, String applDate, String applId){
		String applNo = new String();
		
		List<Skf2060Sc001GetApplHistoryExp> resultListTableData = new ArrayList<Skf2060Sc001GetApplHistoryExp>();
		Skf2060Sc001GetApplHistoryExpParameter param = new Skf2060Sc001GetApplHistoryExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applId+"-"+applDate+"-"+shainNo);
		
		resultListTableData = skf2060Sc001GetApplHistoryExpRepository.getApplHistory(param);
		
		//申請書類管理番号が存在した時
		if(resultListTableData.size() != 0){
			//最新の申請書類管理番号を取得
			String nowApplNo = resultListTableData.get(0).getApplNo();
			System.out.println("最新の申請書類管理番号(´・ω・｀)："+nowApplNo);
		
			//申請書類管理番号の後ろ２桁の番号を取得
			String back2Words = nowApplNo.substring(nowApplNo.length()-2);
			String otherWords = nowApplNo.substring(0,nowApplNo.length()-2);
			int back2Number = Integer.parseInt(back2Words);
			//申請書類管理番号の後ろ２桁に1足す
			back2Number += 1;
			//TODO 最大ページって９９なの？？？？
			int maxPage = 99;
			//申請書類管理番号のページが超えた場合
			if(back2Number > maxPage){
				//TODO エラー処理(とりあえず空にする）
				applNo = new String();
				
			//申請書類管理番号のページが超えなかった場合
			}else{
				applNo = otherWords+back2Number;
			}
		
		//申請書類管理番号が存在しなかった時
		}else{
			applNo = applId+"-"+shainNo+"-"+applDate+"-01";
		}
		return applNo;
	}
	
	/**
	 * 申請書類履歴テーブルへ登録を行う
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applNo
	 * @param applId
	 * @param applStatus
	 * @param applTacFlg
	 * @param agreName1
	 * @param comboFlg
	 * 
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertApplHistory(String companyCd, String shainNo, String applNo, String applId, String applStatus,
			String applTacFlg, String agreName1, String comboFlg){
		
		boolean insertCheck = true;
		int insertCount =0;
		
		Skf2010TApplHistory insertData = new Skf2010TApplHistory();
		insertData.setCompanyCd(companyCd);
		insertData.setShainNo(shainNo);
		insertData.setApplNo(applNo);
		insertData.setApplId(applId);
		insertData.setApplStatus(applStatus);
		insertData.setApplTacFlg(applTacFlg);
		insertData.setAgreName1(agreName1);
		insertData.setComboFlg(comboFlg);
		
		insertCount = skf2010TApplHistoryRepository.insertSelective(insertData);
		
		if (insertCount <= 0) {
			insertCheck = false;
		}
		
		return insertCheck;
	}
	
	/**
	 * 借上候補物件提示テーブルへ登録を行う
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applNo
	 * @param applId
	 * @param applStatus
	 * @param applTacFlg
	 * @param agreName1
	 * @param comboFlg
	 * 
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertKatiageTeiji(String companyCd, String applNo, short teijiKaisu, long checkCandidateNo,
			 Date candidateDate, String riyu, String biko){
		
		boolean insertCheck = true;
		int insertCount =0;
		
		Skf2060TKariageTeiji insertData = new Skf2060TKariageTeiji();
		insertData.setCompanyCd(companyCd);
		insertData.setApplNo(applNo);
		insertData.setTeijiKaisu(teijiKaisu);
		insertData.setCheckCandidateNo(checkCandidateNo);
		insertData.setRiyu(riyu);
		insertData.setBiko(biko);
		insertData.setCandidateDate(candidateDate);
		
		insertCount = skf2060TKariageTeijiRepository.insertSelective(insertData);
		
		if (insertCount <= 0) {
			insertCheck = false;
		}
		
		return insertCheck;
	}
	
	/**
	 * 借上候補物件提示明細テーブルへ登録を行う
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param teijiKaisu
	 * @param candidateNo
	 * @param shatakuName
	 * @param address
	 * @param money
	 * @param applCheckFlg
	 * @param shainNo
	 * 
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertKatiageTeijiDetail(String companyCd, String applNo, short teijiKaisu, long candidateNo,
			String shatakuName, String address, String money, String applCheckFlg, String shainNo){
		
		boolean insertCheck = true;
		int insertCount =0;
		
		Skf2060TKariageTeijiDetail insertData = new Skf2060TKariageTeijiDetail();
		insertData.setCompanyCd(companyCd);
		insertData.setApplNo(applNo);
		insertData.setTeijiKaisu(teijiKaisu);
		insertData.setCandidateNo(candidateNo);
		insertData.setShatakuName(shatakuName);
		insertData.setAddress(address);
		insertData.setMoney(money);
		insertData.setApplCheckFlg(applCheckFlg);
		insertData.setShainNo(shainNo);
		
		insertCount = skf2060TKariageTeijiDetailRepository.insertSelective(insertData);
		
		if (insertCount <= 0) {
			insertCheck = false;
		}
		
		return insertCheck;
	}
	
	/**
	 * 添付ファイル管理テーブル (提示物件)へ登録を行う
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param teijiKaisu
	 * @param candidateNo
	 * 
	 * @return 登録できた場合true　登録できなかった場合false
	 */
	public boolean insertKatiageTeijiFile(String companyCd, String applNo, short teijiKaisu, long candidateNo){
		
		boolean insertCheck = true;
		int insertCount =0;
		
		Skf2060TKariageTeijiFile insertData = new Skf2060TKariageTeijiFile();
		insertData.setCompanyCd(companyCd);
		insertData.setApplNo(applNo);
		insertData.setTeijiKaisu(teijiKaisu);
		insertData.setCandidateNo(candidateNo);
		
		insertCount = skf2060TKariageTeijiFileRepository.insertSelective(insertData);
		
		if (insertCount <= 0) {
			insertCheck = false;
		}
		
		return insertCheck;
	}
	
	/**
	 * 借上候補物件テーブルへ更新を行う
	 * 
	 * @param companyCd
	 * @param candidateNo
	 * 
	 * @return 更新できた場合true　更新できなかった場合false
	 */
	public boolean updateKariageKoho(String companyCd, long candidateNo){
		
		boolean updateCheck = true;
		int updateCount =0;
		String teijiFlg = "1";
		
		Skf2060TKariageBukken updateData = new Skf2060TKariageBukken();
		updateData.setCompanyCd(companyCd);
		updateData.setCandidateNo(candidateNo);
		updateData.setTeijiFlg(teijiFlg);
		
		updateCount = skf2060TKariageBukkenRepository.updateByPrimaryKeySelective(updateData);
		
		if (updateCount <= 0) {
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 申請書類管理テーブルを取得する
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return 申請書類管理円エンティティ
	 */
	public Skf2060Sc001GetApplHistoryInfoForUpdateExp getApplHistoryInfoForUpdate(String companyCd, String applNo){
		
		boolean existCheck = false;
		Skf2060Sc001GetApplHistoryInfoForUpdateExp resultData = new Skf2060Sc001GetApplHistoryInfoForUpdateExp();
		Skf2060Sc001GetApplHistoryInfoForUpdateExpParameter param = new Skf2060Sc001GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		
		resultData = skf2060Sc001GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		
		return resultData;
	}
	
	/**
	 * 申請書類管理テーブルへ更新を行う
	 * 
	 * @param companyCd
	 * @param applNo
	 * 
	 * @return 更新できた場合true　更新できなかった場合false
	 */
	public boolean updateApplHistory(String companyCd, String applNo){
		
		boolean updateCheck = true;
		int updateCount =0;
		String applStatus = "20";
		
		Skf2010TApplHistory updateData = new Skf2010TApplHistory();
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);
		updateData.setApplStatus(applStatus);
		
		updateCount = skf2010TApplHistoryRepository.updateByPrimaryKeySelective(updateData);
		
		if (updateCount <= 0) {
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 借上候補物件テーブルへ提示フラグの更新を行う
	 * 
	 * @param companyCd
	 * @param applNo
	 * @param teijiKaisu
	 * 
	 * @return 更新できた場合true　更新できなかった場合false
	 */
	//TODO 自作なのでupdateUserIdなんかが入ってない
	public boolean updateKariageBukkenTeijiFlg(String companyCd, String applNo, short teijiKaisu){
		
		boolean updateCheck = true;
		int updateCount =0;
		String teijiFlg = "0";
		
		Skf2060Sc001UpdateKariageBukkenTeijiFlgExp updateData = new Skf2060Sc001UpdateKariageBukkenTeijiFlgExp();
		updateData.setCompanyCd(companyCd);
		updateData.setApplNo(applNo);
		updateData.setTeijiKaisu(teijiKaisu);
		updateData.setTeijiFlg(teijiFlg);
		
		updateCount = skf2060Sc001UpdateKariageBukkenTeijiFlgExpRepository.updateKariageBukkenTeijiFlg(updateData);
		
		if (updateCount <= 0) {
			updateCheck = false;
		}
		
		return updateCheck;
	}
	
	/**
	 * 添付ファイルのリンクタグを取得する
	 * 
	 * @param candidateNo
	 * @param attachedNo
	 * @param candidateName
	 * 
	 * @return 添付ファイルリンクタグ
	 */
	public String getLinkTag(String candidateNo, String attachedNo, String attachedName){
		String baseLinkTag = "<a id=\"attached_$CANDIDATENO$_$ATACCHEDNO$\">$ATTACHEDNAME$</a>";
		
		baseLinkTag = baseLinkTag.replace("$CANDIDATENO$", candidateNo);
		baseLinkTag = baseLinkTag.replace("$ATACCHEDNO$", attachedNo);
		baseLinkTag = baseLinkTag.replace("$ATTACHEDNAME$", attachedName);
		
		return baseLinkTag;
	}
	
}
