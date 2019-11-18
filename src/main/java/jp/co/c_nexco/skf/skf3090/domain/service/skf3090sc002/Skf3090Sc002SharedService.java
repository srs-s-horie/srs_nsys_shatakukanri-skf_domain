/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc001.Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090Sc002common.Skf3090Sc002CommonDto;

/**
 * Skf3090Sc002SharedService 現物支給価額マスタ登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc002SharedService {

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	
	// 都道府県コード
	public String PREF_01 = "01";
	public String PREF_02 = "02";
	public String PREF_03 = "03";
	public String PREF_04 = "04";
	public String PREF_05 = "05";
	public String PREF_06 = "06";
	public String PREF_07 = "07";
	public String PREF_08 = "08";
	public String PREF_09 = "09";
	public String PREF_10 = "10";
	public String PREF_11 = "11";
	public String PREF_12 = "12";
	public String PREF_13 = "13";
	public String PREF_14 = "14";
	public String PREF_15 = "15";
	public String PREF_16 = "16";
	public String PREF_17 = "17";
	public String PREF_18 = "18";
	public String PREF_19 = "19";
	public String PREF_20 = "20";
	public String PREF_21 = "21";
	public String PREF_22 = "22";
	public String PREF_23 = "23";
	public String PREF_24 = "24";
	public String PREF_25 = "25";
	public String PREF_26 = "26";
	public String PREF_27 = "27";
	public String PREF_28 = "28";
	public String PREF_29 = "29";
	public String PREF_30 = "30";
	public String PREF_31 = "31";
	public String PREF_32 = "32";
	public String PREF_33 = "33";
	public String PREF_34 = "34";
	public String PREF_35 = "35";
	public String PREF_36 = "36";
	public String PREF_37 = "37";
	public String PREF_38 = "38";
	public String PREF_39 = "39";
	public String PREF_40 = "40";
	public String PREF_41 = "41";
	public String PREF_42 = "42";
	public String PREF_43 = "43";
	public String PREF_44 = "44";
	public String PREF_45 = "45";
	public String PREF_46 = "46";
	public String PREF_47 = "47";
	public int ABROAD_CD = 48;
	
	/**
	 * 定数：状態区分:1(新規)
	 */
	public String JYOTAIKBN_1 = "1";
	/**
	 * 定数：状態区分:2(詳細)
	 */
	public String JYOTAIKBN_2 = "2";
	/**
	 * 定数：状態区分:3(複写)
	 */
	public String JYOTAIKBN_3 = "3";	
	
	/**
	 * 都道府県名称と金額を設定する
	 * 
	 * @param commonDto
	 * @param moneyData
	 * @param moneyCopyList
	 * 
	 */
	public boolean setPrefCdData(Skf3090Sc002CommonDto commonDto, List<Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp> moneyData, List<String> moneyCopyList){
		boolean isError = false;
		// 都道府県コード取得
		Map<String, String> genericCodeMapPref = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_PREFCD);

		// 金額データのチェック
		List<String> moneyList = new ArrayList<String>();
		if(CollectionUtils.isEmpty(moneyCopyList)){
			// moneyCopyListが空っぽ（複写じゃない）
			for(int i = 0; i < ABROAD_CD; i++){
				moneyList.add(i,null);
			}
			
			if(CollectionUtils.isNotEmpty(moneyData)){
				// 金額データが空ではない
				for(int i = 0; i < moneyData.size(); i++){
					Skf3090Sc001GetGenbutsuShikyuKagakuReportInfoExp getRowData = moneyData.get(i);
					int setRow = Integer.parseInt(getRowData.getPrefCd()) - 1;
					moneyList.set(setRow, getRowData.getKyojuRiekigaku());
				}
				// 複写用にコピー
				commonDto.setTaihiMoney(moneyList);
			}else{
				// 何かしらの原因で削除された
				//  - 現物支給価額マスタ一覧で一覧を表示してから、「詳細」ボタンを押下するまでの間に消されちゃった
				// 空のデータを各都道府県のデータとして渡す
				isError = true;
			}
		}else{
			// 複写の場合
			moneyList.addAll(moneyCopyList);
		}
		
		// 01 北海道
		commonDto.setLblTodoufukenn01(PREF_01 + " " + genericCodeMapPref.get(PREF_01));
		commonDto.setTxtTodoufukenn01(getGenbutsuShikyuKagaku(PREF_01, moneyList));
		// 02 青森県
		commonDto.setLblTodoufukenn02(PREF_02 + " " + genericCodeMapPref.get(PREF_02));
		commonDto.setTxtTodoufukenn02(getGenbutsuShikyuKagaku(PREF_02, moneyList));
		// 03 岩手県
		commonDto.setLblTodoufukenn03(PREF_03 + " " + genericCodeMapPref.get(PREF_03));
		commonDto.setTxtTodoufukenn03(getGenbutsuShikyuKagaku(PREF_03, moneyList));
		// 04 宮城県
		commonDto.setLblTodoufukenn04(PREF_04 + " " + genericCodeMapPref.get(PREF_04));
		commonDto.setTxtTodoufukenn04(getGenbutsuShikyuKagaku(PREF_04, moneyList));
		// 05 秋田県
		commonDto.setLblTodoufukenn05(PREF_05 + " " + genericCodeMapPref.get(PREF_05));
		commonDto.setTxtTodoufukenn05(getGenbutsuShikyuKagaku(PREF_05, moneyList));
		// 06 山形県
		commonDto.setLblTodoufukenn06(PREF_06 + " " + genericCodeMapPref.get(PREF_06));
		commonDto.setTxtTodoufukenn06(getGenbutsuShikyuKagaku(PREF_06, moneyList));
		// 07 福島県
		commonDto.setLblTodoufukenn07(PREF_07 + " " + genericCodeMapPref.get(PREF_07));
		commonDto.setTxtTodoufukenn07(getGenbutsuShikyuKagaku(PREF_07, moneyList));
		// 08 茨城県
		commonDto.setLblTodoufukenn08(PREF_08 + " " + genericCodeMapPref.get(PREF_08));
		commonDto.setTxtTodoufukenn08(getGenbutsuShikyuKagaku(PREF_08, moneyList));
		// 09 栃木県
		commonDto.setLblTodoufukenn09(PREF_09 + " " + genericCodeMapPref.get(PREF_09));
		commonDto.setTxtTodoufukenn09(getGenbutsuShikyuKagaku(PREF_09, moneyList));
		// 10 群馬県
		commonDto.setLblTodoufukenn10(PREF_10 + " " + genericCodeMapPref.get(PREF_10));
		commonDto.setTxtTodoufukenn10(getGenbutsuShikyuKagaku(PREF_10, moneyList));
		// 11 埼玉県
		commonDto.setLblTodoufukenn11(PREF_11 + " " + genericCodeMapPref.get(PREF_11));
		commonDto.setTxtTodoufukenn11(getGenbutsuShikyuKagaku(PREF_11, moneyList));
		// 12 千葉県
		commonDto.setLblTodoufukenn12(PREF_12 + " " + genericCodeMapPref.get(PREF_12));
		commonDto.setTxtTodoufukenn12(getGenbutsuShikyuKagaku(PREF_12, moneyList));
		// 13 東京都
		commonDto.setLblTodoufukenn13(PREF_13 + " " + genericCodeMapPref.get(PREF_13));
		commonDto.setTxtTodoufukenn13(getGenbutsuShikyuKagaku(PREF_13, moneyList));
		// 14 神奈川県
		commonDto.setLblTodoufukenn14(PREF_14 + " " + genericCodeMapPref.get(PREF_14));
		commonDto.setTxtTodoufukenn14(getGenbutsuShikyuKagaku(PREF_14, moneyList));
		// 15 新潟県
		commonDto.setLblTodoufukenn15(PREF_15 + " " + genericCodeMapPref.get(PREF_15));
		commonDto.setTxtTodoufukenn15(getGenbutsuShikyuKagaku(PREF_15, moneyList));
		// 16 富山県
		commonDto.setLblTodoufukenn16(PREF_16 + " " + genericCodeMapPref.get(PREF_16));
		commonDto.setTxtTodoufukenn16(getGenbutsuShikyuKagaku(PREF_16, moneyList));
		// 17 石川県
		commonDto.setLblTodoufukenn17(PREF_17 + " " + genericCodeMapPref.get(PREF_17));
		commonDto.setTxtTodoufukenn17(getGenbutsuShikyuKagaku(PREF_17, moneyList));
		// 18 福井県
		commonDto.setLblTodoufukenn18(PREF_18 + " " + genericCodeMapPref.get(PREF_18));
		commonDto.setTxtTodoufukenn18(getGenbutsuShikyuKagaku(PREF_18, moneyList));
		// 19 山梨県
		commonDto.setLblTodoufukenn19(PREF_19 + " " + genericCodeMapPref.get(PREF_19));
		commonDto.setTxtTodoufukenn19(getGenbutsuShikyuKagaku(PREF_19, moneyList));
		// 20 長野県
		commonDto.setLblTodoufukenn20(PREF_20 + " " + genericCodeMapPref.get(PREF_20));
		commonDto.setTxtTodoufukenn20(getGenbutsuShikyuKagaku(PREF_20, moneyList));
		// 21 岐阜県
		commonDto.setLblTodoufukenn21(PREF_21 + " " + genericCodeMapPref.get(PREF_21));
		commonDto.setTxtTodoufukenn21(getGenbutsuShikyuKagaku(PREF_21, moneyList));
		// 22 静岡県
		commonDto.setLblTodoufukenn22(PREF_22 + " " + genericCodeMapPref.get(PREF_22));
		commonDto.setTxtTodoufukenn22(getGenbutsuShikyuKagaku(PREF_22, moneyList));
		// 23 愛知県
		commonDto.setLblTodoufukenn23(PREF_23 + " " + genericCodeMapPref.get(PREF_23));
		commonDto.setTxtTodoufukenn23(getGenbutsuShikyuKagaku(PREF_23, moneyList));
		// 24 三重県
		commonDto.setLblTodoufukenn24(PREF_24 + " " + genericCodeMapPref.get(PREF_24));
		commonDto.setTxtTodoufukenn24(getGenbutsuShikyuKagaku(PREF_24, moneyList));
		// 25 滋賀県
		commonDto.setLblTodoufukenn25(PREF_25 + " " + genericCodeMapPref.get(PREF_25));
		commonDto.setTxtTodoufukenn25(getGenbutsuShikyuKagaku(PREF_25, moneyList));
		// 26 京都府
		commonDto.setLblTodoufukenn26(PREF_26 + " " + genericCodeMapPref.get(PREF_26));
		commonDto.setTxtTodoufukenn26(getGenbutsuShikyuKagaku(PREF_26, moneyList));
		// 27 大阪府
		commonDto.setLblTodoufukenn27(PREF_27 + " " + genericCodeMapPref.get(PREF_27));
		commonDto.setTxtTodoufukenn27(getGenbutsuShikyuKagaku(PREF_27, moneyList));
		// 28 兵庫県
		commonDto.setLblTodoufukenn28(PREF_28 + " " + genericCodeMapPref.get(PREF_28));
		commonDto.setTxtTodoufukenn28(getGenbutsuShikyuKagaku(PREF_28, moneyList));
		// 29 奈良県
		commonDto.setLblTodoufukenn29(PREF_29 + " " + genericCodeMapPref.get(PREF_29));
		commonDto.setTxtTodoufukenn29(getGenbutsuShikyuKagaku(PREF_29, moneyList));
		// 30 和歌山県
		commonDto.setLblTodoufukenn30(PREF_30 + " " + genericCodeMapPref.get(PREF_30));
		commonDto.setTxtTodoufukenn30(getGenbutsuShikyuKagaku(PREF_30, moneyList));
		// 31 鳥取県
		commonDto.setLblTodoufukenn31(PREF_31 + " " + genericCodeMapPref.get(PREF_31));
		commonDto.setTxtTodoufukenn31(getGenbutsuShikyuKagaku(PREF_31, moneyList));
		// 32 島根県
		commonDto.setLblTodoufukenn32(PREF_32 + " " + genericCodeMapPref.get(PREF_32));
		commonDto.setTxtTodoufukenn32(getGenbutsuShikyuKagaku(PREF_32, moneyList));
		// 33 岡山県
		commonDto.setLblTodoufukenn33(PREF_33 + " " + genericCodeMapPref.get(PREF_33));
		commonDto.setTxtTodoufukenn33(getGenbutsuShikyuKagaku(PREF_33, moneyList));
		// 34 広島県
		commonDto.setLblTodoufukenn34(PREF_34 + " " + genericCodeMapPref.get(PREF_34));
		commonDto.setTxtTodoufukenn34(getGenbutsuShikyuKagaku(PREF_34, moneyList));
		// 35 山口県
		commonDto.setLblTodoufukenn35(PREF_35 + " " + genericCodeMapPref.get(PREF_35));
		commonDto.setTxtTodoufukenn35(getGenbutsuShikyuKagaku(PREF_35, moneyList));
		// 36 徳島県
		commonDto.setLblTodoufukenn36(PREF_36 + " " + genericCodeMapPref.get(PREF_36));
		commonDto.setTxtTodoufukenn36(getGenbutsuShikyuKagaku(PREF_36, moneyList));
		// 37 香川県
		commonDto.setLblTodoufukenn37(PREF_37 + " " + genericCodeMapPref.get(PREF_37));
		commonDto.setTxtTodoufukenn37(getGenbutsuShikyuKagaku(PREF_37, moneyList));
		// 38 愛媛県
		commonDto.setLblTodoufukenn38(PREF_38 + " " + genericCodeMapPref.get(PREF_38));
		commonDto.setTxtTodoufukenn38(getGenbutsuShikyuKagaku(PREF_38, moneyList));
		// 39 高知県
		commonDto.setLblTodoufukenn39(PREF_39 + " " + genericCodeMapPref.get(PREF_39));
		commonDto.setTxtTodoufukenn39(getGenbutsuShikyuKagaku(PREF_39, moneyList));
		// 40 福岡圏
		commonDto.setLblTodoufukenn40(PREF_40 + " " + genericCodeMapPref.get(PREF_40));
		commonDto.setTxtTodoufukenn40(getGenbutsuShikyuKagaku(PREF_40, moneyList));
		// 41 佐賀県
		commonDto.setLblTodoufukenn41(PREF_41 + " " + genericCodeMapPref.get(PREF_41));
		commonDto.setTxtTodoufukenn41(getGenbutsuShikyuKagaku(PREF_41, moneyList));
		// 42 長崎県
		commonDto.setLblTodoufukenn42(PREF_42 + " " + genericCodeMapPref.get(PREF_42));
		commonDto.setTxtTodoufukenn42(getGenbutsuShikyuKagaku(PREF_42, moneyList));
		// 43 熊本県
		commonDto.setLblTodoufukenn43(PREF_43 + " " + genericCodeMapPref.get(PREF_43));
		commonDto.setTxtTodoufukenn43(getGenbutsuShikyuKagaku(PREF_43, moneyList));
		// 44 大分県
		commonDto.setLblTodoufukenn44(PREF_44 + " " + genericCodeMapPref.get(PREF_44));
		commonDto.setTxtTodoufukenn44(getGenbutsuShikyuKagaku(PREF_44, moneyList));
		// 45 宮崎県
		commonDto.setLblTodoufukenn45(PREF_45 + " " + genericCodeMapPref.get(PREF_45));
		commonDto.setTxtTodoufukenn45(getGenbutsuShikyuKagaku(PREF_45, moneyList));
		// 46 鹿児島県
		commonDto.setLblTodoufukenn46(PREF_46 + " " + genericCodeMapPref.get(PREF_46));
		commonDto.setTxtTodoufukenn46(getGenbutsuShikyuKagaku(PREF_46, moneyList));
		// 47 沖縄県
		commonDto.setLblTodoufukenn47(PREF_47 + " " + genericCodeMapPref.get(PREF_47));
		commonDto.setTxtTodoufukenn47(getGenbutsuShikyuKagaku(PREF_47, moneyList));

		return isError;
	}
	

	/**
	 * 現物支給価額データを設定する
	 * 
	 * @param prefCd
	 * @param moneyList
	 * 
	 */
	private String getGenbutsuShikyuKagaku(String prefCd, List<String> moneyList){
		int targetRow = Integer.parseInt(prefCd) - 1;
		return moneyList.get(targetRow);
	}
	
	
	/**
	 * 日付から"/"と"_"を取り除く
	 * 
	 * @param targetDate
	 * 			日付文字列
	 * @return Replace後の日付文字列
	 *  
	 */
	public String replaceDateFormat(String targetDate){
		return targetDate.replace("/", "").replace("_", "");
	}	 	
	

	/**
	 * エラーのスタイル（背景色ピンク）をクリアする
	 * 
	 * @param commonDto
	 *  
	 */
	public void errorStyleClear(Skf3090Sc002CommonDto commonDto){
		// 改定日
		commonDto.setEffectiveDateErr(null);
		// 01 北海道
		commonDto.setTxtTodoufukenn01Err(null);
		// 02 青森県
		commonDto.setTxtTodoufukenn02Err(null);
		// 03 岩手県
		commonDto.setTxtTodoufukenn03Err(null);
		// 04 宮城県
		commonDto.setTxtTodoufukenn04Err(null);
		// 05 秋田県
		commonDto.setTxtTodoufukenn05Err(null);
		// 06 山形県
		commonDto.setTxtTodoufukenn06Err(null);
		// 07 福島県
		commonDto.setTxtTodoufukenn07Err(null);
		// 08 茨城県
		commonDto.setTxtTodoufukenn08Err(null);
		// 09 栃木県
		commonDto.setTxtTodoufukenn09Err(null);
		// 10 群馬県
		commonDto.setTxtTodoufukenn10Err(null);
		// 11 埼玉県
		commonDto.setTxtTodoufukenn11Err(null);
		// 12 千葉県
		commonDto.setTxtTodoufukenn12Err(null);
		// 13 東京都
		commonDto.setTxtTodoufukenn13Err(null);
		// 14 神奈川県
		commonDto.setTxtTodoufukenn14Err(null);
		// 15 新潟県
		commonDto.setTxtTodoufukenn15Err(null);
		// 16 富山県
		commonDto.setTxtTodoufukenn16Err(null);
		// 17 石川県
		commonDto.setTxtTodoufukenn17Err(null);
		// 18 福井県
		commonDto.setTxtTodoufukenn18Err(null);
		// 19 山梨県
		commonDto.setTxtTodoufukenn19Err(null);
		// 20 長野県
		commonDto.setTxtTodoufukenn20Err(null);
		// 21 岐阜県
		commonDto.setTxtTodoufukenn21Err(null);
		// 22 静岡県
		commonDto.setTxtTodoufukenn22Err(null);
		// 23 愛知県
		commonDto.setTxtTodoufukenn23Err(null);
		// 24 三重県
		commonDto.setTxtTodoufukenn24Err(null);
		// 25 滋賀県
		commonDto.setTxtTodoufukenn25Err(null);
		// 26 京都府
		commonDto.setTxtTodoufukenn26Err(null);
		// 27 大阪府
		commonDto.setTxtTodoufukenn27Err(null);
		// 28 兵庫県
		commonDto.setTxtTodoufukenn28Err(null);
		// 29 奈良県
		commonDto.setTxtTodoufukenn29Err(null);
		// 30 和歌山県
		commonDto.setTxtTodoufukenn30Err(null);
		// 31 鳥取県
		commonDto.setTxtTodoufukenn31Err(null);
		// 32 島根県
		commonDto.setTxtTodoufukenn32Err(null);
		// 33 岡山県
		commonDto.setTxtTodoufukenn33Err(null);
		// 34 広島県
		commonDto.setTxtTodoufukenn34Err(null);
		// 35 山口県
		commonDto.setTxtTodoufukenn35Err(null);
		// 36 徳島県
		commonDto.setTxtTodoufukenn36Err(null);
		// 37 香川県
		commonDto.setTxtTodoufukenn37Err(null);
		// 38 愛媛県
		commonDto.setTxtTodoufukenn38Err(null);
		// 39 高知県
		commonDto.setTxtTodoufukenn39Err(null);
		// 40 福岡圏
		commonDto.setTxtTodoufukenn40Err(null);
		// 41 佐賀県
		commonDto.setTxtTodoufukenn41Err(null);
		// 42 長崎県
		commonDto.setTxtTodoufukenn42Err(null);
		// 43 熊本県
		commonDto.setTxtTodoufukenn43Err(null);
		// 44 大分県
		commonDto.setTxtTodoufukenn44Err(null);
		// 45 宮崎県
		commonDto.setTxtTodoufukenn45Err(null);
		// 46 鹿児島県
		commonDto.setTxtTodoufukenn46Err(null);
		// 47 沖縄県
		commonDto.setTxtTodoufukenn47Err(null);
		// 備考
		commonDto.setBikoErr(null);
		
	}
	
}
