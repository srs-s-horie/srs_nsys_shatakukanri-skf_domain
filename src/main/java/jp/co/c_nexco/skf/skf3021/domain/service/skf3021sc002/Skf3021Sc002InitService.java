/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3021.domain.service.skf3021sc002;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc002.Skf3021Sc002GetGenShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc002.Skf3021Sc002GetGenShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc002.Skf3021Sc002GetTaikyoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc002.Skf3021Sc002GetTaikyoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3021Sc002.Skf3021Sc002GetTaikyoInfoRoomExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MTokyu;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MTokyuKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3021TNyutaikyoYoteiDataKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc002.Skf3021Sc002GetGenShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc002.Skf3021Sc002GetNyukyoChoshoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3021Sc002.Skf3021Sc002GetTaikyoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MTokyuRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3021TNyutaikyoYoteiDataRepository;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3021.domain.dto.skf3021sc002.Skf3021Sc002InitDto;

/**
 * Skf3021Sc002InitService 入退居申請照会のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3021Sc002InitService extends BaseServiceAbstract<Skf3021Sc002InitDto> {
	
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf1010MTokyuRepository skf1010MTokyuRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf3021TNyutaikyoYoteiDataRepository skf3021TNyutaikyoYoteiDataRepository;
	@Autowired
	private Skf3021Sc002GetGenShatakuInfoExpRepository skf3021Sc002GetGenShatakuInfoExpRepository;
	@Autowired
	private Skf3021Sc002GetTaikyoInfoExpRepository skf3021Sc002GetTaikyoInfoExpRepository;
	@Autowired
	private Skf3021Sc002GetNyukyoChoshoInfoExpRepository skf3021Sc002GetNyukyoChoshoInfoExpRepository;
	
	//社員番号変更フラグ
	private static final String SHAIN_NO_CHANGE_FLG = "1";
	//社員番号変更フラグの値
	private static final String DATA_SHAIN_NO_CHANGE_FLG = "*";
	//年齢
	private static final String DATA_NENREI = "歳";
	//面積の単位
	private static final String MENSEKI_TANI = "㎡";
	//社宅の必要・不要の値：0
	private static final String SHATAKU_HITSUYO0 = "0";
	//社宅の必要・不要の値：0⇒必要としない
	private static final String SHATAKU_HITSUYO_0 = "必要としない";
	//社宅の必要・不要の値：1
	private static final String SHATAKU_HITSUYO1 = "1";
	//社宅の必要・不要の値：1⇒必要とする
	private static final String SHATAKU_HITSUYO_1 = "必要とする";
	//社宅の必要・不要の値：2
	private static final String SHATAKU_HITSUYO2 = "2";
	//社宅の必要・不要の値：2⇒駐車場のみ
	private static final String SHATAKU_HITSUYO_2 = "駐車場のみ";
	//社宅を必要とする理由の値：1⇒異動のため
	private static final String HITSUYO_RIYU_1 = "異動のため";
	//社宅を必要とする理由の値：2⇒結婚のため
	private static final String HITSUYO_RIYU_2 = "結婚のため";
	//社宅を必要とする理由の値：9⇒その他
	private static final String HITSUYO_RIYU_9 = "その他";
	//社宅を必要としない理由の値：1⇒自宅通勤
	private static final String FUHITSUYO_RIYU_1 = "自宅通勤";
	//社宅を必要としない理由の値：2⇒自己借上
	private static final String FUHITSUYO_RIYU_2 = "自己借上";
	//社宅を必要としない理由の値：9⇒その他
	private static final String FUHITSUYO_RIYU_9 = "その他";
	//必要とする社宅の値：1
	private static final String HITSUYO_SHATAKU1 = "1";
	//必要とする社宅の値：1⇒世帯
	private static final String HITSUYO_SHATAKU_1 = "世帯";
	//必要とする社宅の値：2
	private static final String HITSUYO_SHATAKU2 = "2";
	//必要とする社宅の値：2⇒単身
	private static final String HITSUYO_SHATAKU_2 = "単身";
	//必要とする社宅の値：3
	private static final String HITSUYO_SHATAKU3 = "3";
	//必要とする社宅の値：3⇒独身
	private static final String HITSUYO_SHATAKU_3 = "独身";
	//保管場所の値：0⇒必要としない
	private static final String PARKING_UMU_0 = "必要としない";
	//保管場所の値：1⇒必要とする
	private static final String PARKING_UMU_1 = "必要とする";
	//文字列：社宅
	private static final String SHATAKU = "社宅";
	//文字列：駐車場１
	private static final String PARKING_1 = "駐車場１";
	//文字列：駐車場２
	private static final String PARKING_2 = "駐車場２";
		
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3021Sc002InitDto index(Skf3021Sc002InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF3021_SC002_TITLE);
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		
		//SetContrlorValues()
		//申請書類管理番号の取得
		String applNo = initDto.getHdnRowApplNo();
		//社員番号の取得
		String shainNo = initDto.getHdnRowShainNo().replace(CodeConstant.ASTERISK, "");
		//入退居区分の取得
		String nyutaikyokbn = initDto.getHdnRowNyutaikyoKbn();
		
		//申請者情報
		setShinseishaInfo(shainNo,initDto);
		
		//入退居申請情報を設定する
		setNyuTaikyoShinseishaInfo(applNo,initDto);
		
		//特殊事情の設定
		setTokushujijoFromNyutaikyoYotei(shainNo,nyutaikyokbn,initDto);
		
		return initDto;
	}
	

	
	/**
	 * 同居家族リストテーブルデータ作成
	 * 
	 * @param shainInfoList
	 * @return
	 */
	private List<Map<String, Object>> createDokyoKazokuListTable(Skf2020TNyukyoChoshoTsuchi dt_TSUCHI) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		if(dt_TSUCHI == null){
			for(int i=1; i < 7; i++){
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("colDokyoNo", i); 
				tmpMap.put("colDokyoName", ""); 
				tmpMap.put("colZokugara", ""); 
				tmpMap.put("colNenrei", "");

				returnList.add(tmpMap);
			}
			return returnList;
		}
		//氏名1, 続柄1, 年齢1
		String dokyoName1 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation1 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge1 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName1() != null ){
			dokyoName1 = dt_TSUCHI.getDokyoName1();
		}
		if(dt_TSUCHI.getDokyoRelation1() != null ){
			dokyoRelation1 = dt_TSUCHI.getDokyoRelation1();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge1())){
			dokyoAge1 = dt_TSUCHI.getDokyoAge1() + DATA_NENREI;
		}
		Map<String, Object> tmpMap1 = new HashMap<String, Object>();
		tmpMap1.put("colDokyoNo", "1"); 
		tmpMap1.put("colDokyoName", dokyoName1); 
		tmpMap1.put("colZokugara", dokyoRelation1);
		tmpMap1.put("colNenrei", dokyoAge1);

		returnList.add(tmpMap1);
		
		//氏名2, 続柄2, 年齢2
		String dokyoName2 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation2 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge2 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName2() != null ){
			dokyoName2 = dt_TSUCHI.getDokyoName2();
		}
		if(dt_TSUCHI.getDokyoRelation2() != null ){
			dokyoRelation2 = dt_TSUCHI.getDokyoRelation2();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge2())){
			dokyoAge2 = dt_TSUCHI.getDokyoAge2() + DATA_NENREI;
		}
		Map<String, Object> tmpMap2 = new HashMap<String, Object>();
		tmpMap2.put("colDokyoNo", "2"); 
		tmpMap2.put("colDokyoName", dokyoName2); 
		tmpMap2.put("colZokugara", dokyoRelation2);
		tmpMap2.put("colNenrei", dokyoAge2);

		returnList.add(tmpMap2);
		
		//氏名3, 続柄3, 年齢3
		String dokyoName3 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation3 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge3 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName3() != null ){
			dokyoName3 = dt_TSUCHI.getDokyoName3();
		}
		if(dt_TSUCHI.getDokyoRelation3() != null ){
			dokyoRelation3 = dt_TSUCHI.getDokyoRelation3();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge3())){
			dokyoAge3 = dt_TSUCHI.getDokyoAge3() + DATA_NENREI;
		}
		Map<String, Object> tmpMap3 = new HashMap<String, Object>();
		tmpMap3.put("colDokyoNo", "3"); 
		tmpMap3.put("colDokyoName", dokyoName3); 
		tmpMap3.put("colZokugara", dokyoRelation3);
		tmpMap3.put("colNenrei", dokyoAge3);

		returnList.add(tmpMap3);
		
		//氏名4, 続柄4, 年齢4
		String dokyoName4 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation4 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge4 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName4() != null ){
			dokyoName4 = dt_TSUCHI.getDokyoName4();
		}
		if(dt_TSUCHI.getDokyoRelation4() != null ){
			dokyoRelation4 = dt_TSUCHI.getDokyoRelation4();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge4())){
			dokyoAge4 = dt_TSUCHI.getDokyoAge4() + DATA_NENREI;
		}
		Map<String, Object> tmpMap4 = new HashMap<String, Object>();
		tmpMap4.put("colDokyoNo", "4"); 
		tmpMap4.put("colDokyoName", dokyoName4); 
		tmpMap4.put("colZokugara", dokyoRelation4);
		tmpMap4.put("colNenrei", dokyoAge4);

		returnList.add(tmpMap4);
		
		//氏名5, 続柄5, 年齢5
		String dokyoName5 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation5 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge5 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName5() != null ){
			dokyoName5 = dt_TSUCHI.getDokyoName5();
		}
		if(dt_TSUCHI.getDokyoRelation5() != null ){
			dokyoRelation5 = dt_TSUCHI.getDokyoRelation5();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge5())){
			dokyoAge5 = dt_TSUCHI.getDokyoAge5() + DATA_NENREI;
		}
		Map<String, Object> tmpMap5 = new HashMap<String, Object>();
		tmpMap5.put("colDokyoNo", "5"); 
		tmpMap5.put("colDokyoName", dokyoName5); 
		tmpMap5.put("colZokugara", dokyoRelation5);
		tmpMap5.put("colNenrei", dokyoAge5);

		returnList.add(tmpMap5);
		
		//氏名6, 続柄6, 年齢6
		String dokyoName6 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoRelation6 = CodeConstant.DOUBLE_QUOTATION;
		String dokyoAge6 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getDokyoName6() != null ){
			dokyoName6 = dt_TSUCHI.getDokyoName6();
		}
		if(dt_TSUCHI.getDokyoRelation6() != null ){
			dokyoRelation6 = dt_TSUCHI.getDokyoRelation6();
		}
		if(!SkfCheckUtils.isNullOrEmpty(dt_TSUCHI.getDokyoAge6())){
			dokyoAge6 = dt_TSUCHI.getDokyoAge6() + DATA_NENREI;
		}
		Map<String, Object> tmpMap6 = new HashMap<String, Object>();
		tmpMap6.put("colDokyoNo", "6"); 
		tmpMap6.put("colDokyoName", dokyoName6); 
		tmpMap6.put("colZokugara", dokyoRelation6);
		tmpMap6.put("colNenrei", dokyoAge6);

		returnList.add(tmpMap6);

		return returnList;
	}
	
	/**
	 * 自動車リストテーブルデータ作成
	 * 
	 * @param shainInfoList
	 * @return
	 */
	private List<Map<String, Object>> createJidoshaListTable(Skf2020TNyukyoChoshoTsuchi dt_TSUCHI) {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		if(dt_TSUCHI == null){
			for(int i=1; i < 3; i++){
				Map<String, Object> tmpMap = new HashMap<String, Object>();
				tmpMap.put("colCarNo", i); 
				tmpMap.put("colCarName", ""); 
				tmpMap.put("colTorokuNo", ""); 
				tmpMap.put("colCarUser", "");
				tmpMap.put("colShiyokaishibi", "");

				returnList.add(tmpMap);
			}
			return returnList;
		}
		
		//車名1，登録番号1，使用者1,使用開始日1
		String carName = CodeConstant.DOUBLE_QUOTATION;
		String carNo = CodeConstant.DOUBLE_QUOTATION;
		String carUser = CodeConstant.DOUBLE_QUOTATION;
		String useDate = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getCarName() != null){
			carName = dt_TSUCHI.getCarName();
		}
		if(dt_TSUCHI.getCarNo() != null){
			carNo = dt_TSUCHI.getCarNo();
		}
		if(dt_TSUCHI.getCarUser() != null){
			carUser = dt_TSUCHI.getCarUser();
		}
		if(dt_TSUCHI.getParkingUseDate() != null){
			useDate = skfDateFormatUtils.dateFormatFromString(dt_TSUCHI.getParkingUseDate() , "yyyy/MM/dd");
		}
		Map<String, Object> tmpMap1 = new HashMap<String, Object>();
		tmpMap1.put("colCarNo", "1"); 
		tmpMap1.put("colCarName", carName); 
		tmpMap1.put("colTorokuNo", carNo); 
		tmpMap1.put("colCarUser", carUser);
		tmpMap1.put("colShiyokaishibi", useDate);

		returnList.add(tmpMap1);
		
		//車名2，登録番号2，使用者2,使用開始日2
		String carName2 = CodeConstant.DOUBLE_QUOTATION;
		String carNo2 = CodeConstant.DOUBLE_QUOTATION;
		String carUser2 = CodeConstant.DOUBLE_QUOTATION;
		String useDate2 = CodeConstant.DOUBLE_QUOTATION;
		if(dt_TSUCHI.getCarName2() != null){
			carName2 = dt_TSUCHI.getCarName2();
		}
		if(dt_TSUCHI.getCarNo2() != null){
			carNo2 = dt_TSUCHI.getCarNo2();
		}
		if(dt_TSUCHI.getCarUser2() != null){
			carUser2 = dt_TSUCHI.getCarUser2();
		}
		if(dt_TSUCHI.getParkingUseDate2() != null){
			useDate2 = skfDateFormatUtils.dateFormatFromString(dt_TSUCHI.getParkingUseDate2() , "yyyy/MM/dd");
		}
		Map<String, Object> tmpMap2 = new HashMap<String, Object>();
		tmpMap2.put("colCarNo", "2"); 
		tmpMap2.put("colCarName", carName2); 
		tmpMap2.put("colTorokuNo", carNo2); 
		tmpMap2.put("colCarUser", carUser2);
		tmpMap2.put("colShiyokaishibi", useDate2);

		returnList.add(tmpMap2);
		
		return returnList;
	}
	
	
	/**
	 * 社宅社員マスタ情報を表示する
	 * @param shainNo
	 * @return
	 */
	private void setShinseishaInfo(String shainNo, Skf3021Sc002InitDto dto) {
				
		String shainName = CodeConstant.DOUBLE_QUOTATION;
		String tokyu = CodeConstant.DOUBLE_QUOTATION;
		String nenrei = CodeConstant.DOUBLE_QUOTATION;
		
		Skf1010MShain dt_Getshain = null;
		if(!SkfCheckUtils.isNullOrEmpty(shainNo)){
			Skf1010MShainKey shainKey = new Skf1010MShainKey();
			shainKey.setCompanyCd(CodeConstant.C001);
			shainKey.setShainNo(shainNo);
			dt_Getshain = skf1010MShainRepository.selectByPrimaryKey(shainKey);
		}
		
		if(dt_Getshain != null){
			//社員番号
			if(dt_Getshain.getShainNoChangeFlg() != null){
				if(SHAIN_NO_CHANGE_FLG.equals(dt_Getshain.getShainNoChangeFlg())){
					shainNo = shainNo + CodeConstant.ASTERISK;
				}
			}
			
			//社員氏名
			if(dt_Getshain.getName() != null){
				shainName = dt_Getshain.getName();
			}
			String birthday_Year = CodeConstant.DOUBLE_QUOTATION;
			String birthday_Month = CodeConstant.DOUBLE_QUOTATION;
			String birthday_Day = CodeConstant.DOUBLE_QUOTATION;
			
			//年
			if(dt_Getshain.getBirthdayYear() != null){
				birthday_Year = dt_Getshain.getBirthdayYear().toString();
			}
			//月
			if(dt_Getshain.getBirthdayMonth() != null){
				if(dt_Getshain.getBirthdayMonth() < 10){
					birthday_Month = CodeConstant.STRING_ZERO + dt_Getshain.getBirthdayMonth().toString();
				}else{
					birthday_Month = dt_Getshain.getBirthdayMonth().toString();
				}
				
			}
			//日
			if(dt_Getshain.getBirthdayDay() != null){
				if(dt_Getshain.getBirthdayDay() < 10){
					birthday_Day = CodeConstant.STRING_ZERO + dt_Getshain.getBirthdayDay().toString();
				}else{
					birthday_Day = dt_Getshain.getBirthdayDay().toString();
				}
				
			}
			//年,月,日
			String birthday = birthday_Year + birthday_Month + birthday_Day;
			//年齢
			if(!SkfCheckUtils.isNullOrEmpty(birthday)){
				nenrei = skfBaseBusinessLogicUtils.getBirthdayToAge(birthday) + DATA_NENREI;
			}
			
			//等級
			if(dt_Getshain.getTokyuCd() != null){
				Skf1010MTokyuKey tokyuKey = new Skf1010MTokyuKey();
				tokyuKey.setCompanyCd(CodeConstant.C001);
				tokyuKey.setTokyuCd(dt_Getshain.getTokyuCd());
				Skf1010MTokyu dt_Tokyu = skf1010MTokyuRepository.selectByPrimaryKey(tokyuKey);
				
				if(dt_Tokyu != null){
					tokyu = dt_Tokyu.getTokyuName();
				}
			}
		}
		dto.setShainNo(shainNo);
		dto.setShainName(shainName);
		dto.setTokyu(tokyu);
		dto.setNenrei(nenrei);
		
		
	}
	
	/**
	 * 現居社宅情報以外の入退居申請情報を設定する
	 * @param applNo 申請書類管理番号
	 * @param dto
	 */
	private void setNyuTaikyoShinseishaInfo(String applNo, Skf3021Sc002InitDto dto){
		
		Skf2020TNyukyoChoshoTsuchi dt_TSUCHI = null;
		List<Skf2020TNyukyoChoshoTsuchi> dt_TSUCHIList = new ArrayList<Skf2020TNyukyoChoshoTsuchi>();
		if(!SkfCheckUtils.isNullOrEmpty(applNo)){
//			Skf2020TNyukyoChoshoTsuchiKey nctKey = new Skf2020TNyukyoChoshoTsuchiKey();
//			nctKey.setCompanyCd(CodeConstant.C001);
//			nctKey.setApplNo(applNo);
//			dt_TSUCHI = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(nctKey);
			dt_TSUCHIList = skf3021Sc002GetNyukyoChoshoInfoExpRepository.getNyukyoChoshoInfo(applNo);
		}
		
		if(dt_TSUCHIList.size() > 0 ){
			dt_TSUCHI = dt_TSUCHIList.get(0);
			//社宅の必要・不要
			String hitsuyofuyo = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getTaiyoHitsuyo() != null){
				if(SHATAKU_HITSUYO1.equals(dt_TSUCHI.getTaiyoHitsuyo())){
					hitsuyofuyo = SHATAKU_HITSUYO_1;
				}else if(SHATAKU_HITSUYO2.equals(dt_TSUCHI.getTaiyoHitsuyo())){
					hitsuyofuyo = SHATAKU_HITSUYO_2;
				}else if(SHATAKU_HITSUYO0.equals(dt_TSUCHI.getTaiyoHitsuyo())){
					hitsuyofuyo = SHATAKU_HITSUYO_0;
				}
			}
			dto.setHitsuyofuyo(hitsuyofuyo);
			
			//社宅を必要とする理由
			String hitsuyoriyu = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getHitsuyoRiyu() != null){
				if(CodeConstant.IDOU.equals(dt_TSUCHI.getHitsuyoRiyu())){
					hitsuyoriyu = HITSUYO_RIYU_1;
				}else if(CodeConstant.KEKKON.equals(dt_TSUCHI.getHitsuyoRiyu())){
					hitsuyoriyu = HITSUYO_RIYU_2;
				}else if(CodeConstant.HITUYO_RIYU_OTHERS.equals(dt_TSUCHI.getHitsuyoRiyu())){
					hitsuyoriyu = HITSUYO_RIYU_9;
				}
			}
			dto.setHitsuyoriyu(hitsuyoriyu);
			
			//社宅を必要としない理由
			String fuyoriyu = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getFuhitsuyoRiyu() != null){
				if(CodeConstant.JITAKU_TSUKIN.equals(dt_TSUCHI.getFuhitsuyoRiyu())){
					fuyoriyu = FUHITSUYO_RIYU_1;
				}else if(CodeConstant.JIKO_KARIAGE.equals(dt_TSUCHI.getFuhitsuyoRiyu())){
					fuyoriyu = FUHITSUYO_RIYU_2;
				}else if(CodeConstant.FUYO_RIYU_OTHERS.equals(dt_TSUCHI.getFuhitsuyoRiyu())){
					fuyoriyu = FUHITSUYO_RIYU_9;
				}
			}
			dto.setFuyoriyu(fuyoriyu);
			
			//必要とする社宅
			String hitsuyoShataku = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getHitsuyoShataku() != null){
				if(HITSUYO_SHATAKU1.equals(dt_TSUCHI.getHitsuyoShataku())){
					hitsuyoShataku = HITSUYO_SHATAKU_1;
				}else if(HITSUYO_SHATAKU2.equals(dt_TSUCHI.getHitsuyoShataku())){
					hitsuyoShataku = HITSUYO_SHATAKU_2;
				}else if(HITSUYO_SHATAKU3.equals(dt_TSUCHI.getHitsuyoShataku())){
					hitsuyoShataku = HITSUYO_SHATAKU_3;
				}
			}
			dto.setHitsuyoShataku(hitsuyoShataku);
			
			//入居希望日（予定日）
			String nyukyoKibobi = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getNyukyoYoteiDate() != null){
				//文字列を日付型に変換し、日付フォーマット(形式：yyyy/MM/dd)に変換する。
				nyukyoKibobi = skfDateFormatUtils.dateFormatFromString(dt_TSUCHI.getNyukyoYoteiDate() , "yyyy/MM/dd");
			}
			dto.setNyukyoKibobi(nyukyoKibobi);
			
			//自動車の保管場所
			String jidoshaHokan = CodeConstant.DOUBLE_QUOTATION;
			if(dt_TSUCHI.getParkingUmu() != null){
				if(CodeConstant.CAR_PARK_FUYO.equals(dt_TSUCHI.getParkingUmu())){
					jidoshaHokan = PARKING_UMU_0;
				}else if(CodeConstant.CAR_PARK_HITUYO.equals(dt_TSUCHI.getParkingUmu())){
					jidoshaHokan = PARKING_UMU_1;
				}
			}
			dto.setJidoshaHokan(jidoshaHokan);
			
//			//特殊事情
//			String tokushujijo = CodeConstant.DOUBLE_QUOTATION;
//			if(dt_TSUCHI.getTokushuJijo() != null){
//				tokushujijo = dt_TSUCHI.getTokushuJijo();
//			}
//			dto.setTokushujijo(tokushujijo);
			
			//同居家族一覧表
			dto.setDokyoKazokuIchiranList(createDokyoKazokuListTable(dt_TSUCHI));
			
			//自動車一覧
			dto.setJidoshaIchiranList(createJidoshaListTable(dt_TSUCHI));
			
			//入退居希望等調書の、現在社宅管理番号、部屋番号から、現居社宅情報を取得する
			String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
			Skf3021Sc002GetGenShatakuInfoExpParameter genParam = new Skf3021Sc002GetGenShatakuInfoExpParameter();
			genParam.setShatakuKanriNo(dt_TSUCHI.getNowShatakuKanriNo());
			genParam.setShatakuRoomKanriNo(dt_TSUCHI.getNowRoomKanriNo());
			genParam.setYearMonth(yearMonth);
			List<Skf3021Sc002GetGenShatakuInfoExp> dt_GenShataku = skf3021Sc002GetGenShatakuInfoExpRepository.getGenShatakuInfo(genParam);
			
			//用途を取得する
			String yoto = CodeConstant.DOUBLE_QUOTATION;
			if(dt_GenShataku.size() > 0){
				yoto = dt_GenShataku.get(0).getAuse();
			}
			
			//現居住宅情報
			setGenkyoJutakuInfo(dt_TSUCHI.getNowShatakuName()
					,dt_TSUCHI.getNowShatakuNo()
					,dt_TSUCHI.getNowShatakuMenseki()
					,dt_TSUCHI.getNowShatakuKikaku()
					,yoto
					,dt_TSUCHI.getTaikyoYotei()
					,dt_TSUCHI.getTaikyoYoteiDate()
					,dt_TSUCHI.getTaikyoRiyu()
					,dt_TSUCHI.getTaikyogoRenrakusaki()
					,dto);
			
			//現保有の社宅から、退居（返還）する社宅又は駐車場を設定する。
			dto.setTaikyoShatakuOrChushajo(setTaikyoShatakuOrChushajoWithNyukyoInfo(dt_TSUCHI.getTaikyoYotei()));
			
		}else{
			//現居社宅情報以外の入退居申請情報を設定する(空白を設定)
			clearNyuTaikyoShinseishaInfo(dto);
			//退居届情報を取得して、現居住宅情報を設定する
			setGenShatakuInfoWithTaikyoInfo(applNo,dto);
		}
	}
	
	/**
	 * 現居住宅一覧情報の値を設定
	 * @param shatakuMei 社宅名
	 * @param heyaNo 部屋番号
	 * @param menseki 面積
	 * @param kikaku 規格（間取り）
	 * @param yoto 用途
	 * @param shiyojokyo 使用状况
	 * @param taikyoYoteibi 退居予定日
	 * @param taikyoRiyu 退居理由
	 * @param taikyogoRenrakusaki 退居後の連絡先
	 */
	private void setGenkyoJutakuInfo(
			String shatakuMei
			,String heyaNo
			,String menseki
			,String kikaku
			,String yoto
			,String shiyojokyo
			,String taikyoYoteibi
			,String taikyoRiyu
			,String taikyogoRenrakusaki
			,Skf3021Sc002InitDto dto){
		
		//規格区分
		Map<String, String> genericCodeKikakuKbn = new HashMap<String, String>();
		genericCodeKikakuKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);

		//用途区分
		Map<String, String> genericCodeAuseKbn = new HashMap<String, String>();
		genericCodeAuseKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_AUSE_KBN);

		//使用状况(退居予定区分)GENERIC_CODE_TAIKYO_YOTEI_KBN
		Map<String, String> genericCodeTaikyoYoteiKbn = new HashMap<String, String>();
		genericCodeTaikyoYoteiKbn = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_TAIKYO_YOTEI_KBN);

		
		//社宅名
		if(!SkfCheckUtils.isNullOrEmpty(shatakuMei)){
			dto.setShatakuName(shatakuMei);
		}else{
			dto.setShatakuName(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//部屋番号
		if(!SkfCheckUtils.isNullOrEmpty(heyaNo)){
			dto.setRoomNo(heyaNo);
		}else{
			dto.setRoomNo(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//面積
		if(!SkfCheckUtils.isNullOrEmpty(menseki)){
			dto.setMenseki(menseki + MENSEKI_TANI);
		}else{
			dto.setMenseki(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//規格（間取り）
		if(!SkfCheckUtils.isNullOrEmpty(kikaku)){
			dto.setKikaku(genericCodeKikakuKbn.get(kikaku));
		}else{
			dto.setKikaku(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//用途
		if(!SkfCheckUtils.isNullOrEmpty(yoto)){
			dto.setYoto(genericCodeAuseKbn.get(yoto));
		}else{
			dto.setYoto(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//使用状况
		if(!SkfCheckUtils.isNullOrEmpty(shiyojokyo)){
			dto.setShiyojokyo(genericCodeTaikyoYoteiKbn.get(shiyojokyo));
		}else{
			dto.setShiyojokyo(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//退居予定日
		if(!SkfCheckUtils.isNullOrEmpty(taikyoYoteibi)){
			dto.setTaikyoYoteibi(skfDateFormatUtils.dateFormatFromString(taikyoYoteibi , "yyyy/MM/dd"));
		}else{
			dto.setTaikyoYoteibi(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//退居理由
		if(!SkfCheckUtils.isNullOrEmpty(taikyoRiyu)){
			dto.setTaikyoRiyu(taikyoRiyu);
		}else{
			dto.setTaikyoRiyu(CodeConstant.DOUBLE_QUOTATION);
		}
		
		//退居後の連絡先
		if(!SkfCheckUtils.isNullOrEmpty(taikyogoRenrakusaki)){
			dto.setTaikyogoRenrakusaki(taikyogoRenrakusaki);
		}else{
			dto.setTaikyogoRenrakusaki(CodeConstant.DOUBLE_QUOTATION);
		}
	}
	
	/**
	 * 現保有の社宅から、退居（返還）する社宅又は駐車場を設定する。
	 * @param shiyojokyo 使用状況
	 * @return
	 */
	private String setTaikyoShatakuOrChushajoWithNyukyoInfo(String shiyojokyo){
		
		String resStr = CodeConstant.DOUBLE_QUOTATION;
		if(!SkfCheckUtils.isNullOrEmpty(shiyojokyo)){
			switch (shiyojokyo){
				case "1":
					resStr = SHATAKU;
					break;
			}
		}
		
		return resStr;
	}
	
	/**
	 * 現居社宅情報以外の入退居申請情報をクリアする
	 * @param dto
	 */
	private void clearNyuTaikyoShinseishaInfo(Skf3021Sc002InitDto dto){
		//社宅の必要・不要
		dto.setHitsuyofuyo(CodeConstant.DOUBLE_QUOTATION);
		//社宅を必要とする理由
		dto.setHitsuyoriyu(CodeConstant.DOUBLE_QUOTATION);
		//社宅を必要としない理由
		dto.setFuyoriyu(CodeConstant.DOUBLE_QUOTATION);
		//必要とする社宅
		dto.setHitsuyoShataku(CodeConstant.DOUBLE_QUOTATION);
		//入居希望日（予定日）
		dto.setNyukyoKibobi(CodeConstant.DOUBLE_QUOTATION);
		//自動車の保管場所
		dto.setJidoshaHokan(CodeConstant.DOUBLE_QUOTATION);
		//特殊事情
		dto.setTokushujijo(CodeConstant.DOUBLE_QUOTATION);

		//同居家族一覧表
		dto.setDokyoKazokuIchiranList(createDokyoKazokuListTable(null));
		
		//自動車一覧
		dto.setJidoshaIchiranList(createJidoshaListTable(null));
		
		//現居住宅情報
		setGenkyoJutakuInfo(CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,CodeConstant.DOUBLE_QUOTATION
				,dto);
	}
	
	/**
	 * 退居届情報を取得して、現居住宅情報を設定する
	 * @param applNo 申請書類管理番号
	 * @param dto
	 */
	private void setGenShatakuInfoWithTaikyoInfo(String applNo, Skf3021Sc002InitDto dto){
		
		if(SkfCheckUtils.isNullOrEmpty(applNo)){
			return;
		}
		
		//退居届情報を取得する
		Skf3021Sc002GetTaikyoInfoExpParameter taikyoParam = new Skf3021Sc002GetTaikyoInfoExpParameter();
		taikyoParam.setApplNo(applNo);
		List<Skf3021Sc002GetTaikyoInfoExp> dt_REPORT = skf3021Sc002GetTaikyoInfoExpRepository.getTaikyoInfo(taikyoParam);
		
		if(dt_REPORT.size() > 0){
			if(dt_REPORT.get(0).getShainNo() != null){
				List<Skf3021Sc002GetTaikyoInfoRoomExp> dt_shatakuLedger = skf3021Sc002GetTaikyoInfoExpRepository.getTaikyoInfoRoom(dt_REPORT.get(0).getShainNo());
				if(dt_shatakuLedger.size() > 0){
					if(dt_shatakuLedger.get(0).getShatakuRoomKanriNo() != null){
						String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
						Skf3021Sc002GetGenShatakuInfoExpParameter genParam = new Skf3021Sc002GetGenShatakuInfoExpParameter();
						genParam.setShatakuKanriNo(dt_REPORT.get(0).getShatakuNo());
						genParam.setShatakuRoomKanriNo(dt_REPORT.get(0).getRoomKanriNo());
						genParam.setYearMonth(yearMonth);
						List<Skf3021Sc002GetGenShatakuInfoExp> dt_GenShataku = skf3021Sc002GetGenShatakuInfoExpRepository.getGenShatakuInfo(genParam);
						
						String taikyoKbn = CodeConstant.DOUBLE_QUOTATION;
						if(dt_REPORT.get(0).getShatakuTaikyoKbn() != null){
							if(CodeConstant.SHATAKU_PARK_TAIKYO.equals(dt_REPORT.get(0).getShatakuTaikyoKbn()) ||
									CodeConstant.SHATAKU_TAIKYO.equals(dt_REPORT.get(0).getShatakuTaikyoKbn())	){
								taikyoKbn = CodeConstant.LEAVE;
							}else if(CodeConstant.PARK_TAIKYO.equals(dt_REPORT.get(0).getShatakuTaikyoKbn())){
								taikyoKbn = CodeConstant.NOT_LEAVE;
							}
						}
						
						if(dt_GenShataku.size() > 0){
							setGenkyoJutakuInfo(dt_GenShataku.get(0).getShatakuName()
									,dt_GenShataku.get(0).getRoomNo()
									,dt_GenShataku.get(0).getShatakuCalcMenseki().toPlainString()
									,dt_GenShataku.get(0).getKikaku()
									,dt_GenShataku.get(0).getAuse()
									,taikyoKbn
									,dt_REPORT.get(0).getTaikyoDate()
									,dt_REPORT.get(0).getTaikyoRiyu()
									,dt_REPORT.get(0).getTaikyogoRenrakusaki()
									,dto);
						}else{
							setGenkyoJutakuInfo(CodeConstant.DOUBLE_QUOTATION
									,CodeConstant.DOUBLE_QUOTATION
									,CodeConstant.DOUBLE_QUOTATION
									,CodeConstant.DOUBLE_QUOTATION
									,CodeConstant.DOUBLE_QUOTATION
									,taikyoKbn
									,dt_REPORT.get(0).getTaikyoDate()
									,dt_REPORT.get(0).getTaikyoRiyu()
									,dt_REPORT.get(0).getTaikyogoRenrakusaki()
									,dto);
						}
					}
				}
				
				//退居届から、退居（返還）する社宅又は駐車場を設定する。
				String taikyoShatakuOrChushajo = setTaikyoShatakuOrChushajoWithTaikyoInfo(dt_REPORT.get(0).getTaikyoShataku()
						, dt_REPORT.get(0).getTaikyoParking1(), dt_REPORT.get(0).getTaikyoParking2());
				dto.setTaikyoShatakuOrChushajo(taikyoShatakuOrChushajo);
			}
		}
	}
	
	/**
	 * 退居届から、退居（返還）する社宅又は駐車場を設定する。
	 * @param taikyoShataku 社宅を退居する
	 * @param taikyoParking1 駐車場1を退居する
	 * @param taikyoParking2 駐車場2を退居する
	 * @return
	 */
	private String setTaikyoShatakuOrChushajoWithTaikyoInfo(String taikyoShataku, String taikyoParking1, String taikyoParking2){
		String str = CodeConstant.DOUBLE_QUOTATION;
		
		//駐車場1を返還する
		if(!SkfCheckUtils.isNullOrEmpty(taikyoParking1)){
			if("1".equals(taikyoParking1)){
				str = PARKING_1;
			}
		}
		//駐車場2を返還する
		if(!SkfCheckUtils.isNullOrEmpty(taikyoParking2)){
			if("1".equals(taikyoParking2)){
				if(str.length() > 0){
					str = str + CodeConstant.SPACE + PARKING_2;
				}else{
					str = PARKING_2;
				}
			}
		}
		//社宅を退居する
		if(!SkfCheckUtils.isNullOrEmpty(taikyoShataku)){
			if("1".equals(taikyoShataku)){
				str = SHATAKU;
			}
		}
		
		return str;
	}

	/**
	 * 入退居予定データから特殊事情を設定する
	 * @param shainNo 社員番号
	 * @param nyutaikyoKbn 入退居区分
	 * @param dto
	 */
	private void setTokushujijoFromNyutaikyoYotei(String shainNo, String nyutaikyoKbn, Skf3021Sc002InitDto dto){
		
		Skf3021TNyutaikyoYoteiDataKey key = new Skf3021TNyutaikyoYoteiDataKey();
		key.setShainNo(shainNo);
		key.setNyutaikyoKbn(nyutaikyoKbn);
		Skf3021TNyutaikyoYoteiData record = skf3021TNyutaikyoYoteiDataRepository.selectByPrimaryKey(key);
		if(record != null){
			dto.setTokushujijo(record.getTokushuJijo());
		}
	}
}
