/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetZipToAddressExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3010Sc002.Skf3010Sc002GetZipToAddressExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3010Sc002.Skf3010Sc002GetZipToAddressExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc002.Skf3010Sc002AddressSearchAsyncDto;


/**
 * Skf3010Sc002AddressSearchService 保有社宅登録画面の住所検索サービス処理クラス。　 
 * 
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3010Sc002AddressSearchAsyncService extends AsyncBaseServiceAbstract<Skf3010Sc002AddressSearchAsyncDto> {
	
	
	@Autowired
	private Skf3010Sc002GetZipToAddressExpRepository skf3010Sc002GetZipToAddressExpRepository;
	@Autowired
	private SkfDropDownUtils ddlUtils;

	/**
	 * サービス処理を行う。　
	 * 住所検索ボタン押下時処理
	 * 
	 * @param initDto	インプットDTO
	 * @return 処理結果
	 * @throws Exception	例外
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc002AddressSearchAsyncDto asyncDto) throws Exception {

		// Debugログで出力
		LogUtils.debugByMsg("基本情報-郵便番号：" + asyncDto.getZipCd());
		//
		asyncDto.setZipCdErr(CodeConstant.DOUBLE_QUOTATION);
		// 都道府県リスト
		List<Map<String, Object>> prefList = new ArrayList<Map<String, Object>>();
		String adderss = "";
		String prefNm = "";

		// 郵便番号
		String zipCode = asyncDto.getZipCd();
		// 郵便番号エラー
		String[] errCtrl = {"zipCd"};
		
		//必須チェック
		if (SkfCheckUtils.isNullOrEmpty(zipCode)) {
			ServiceHelper.addErrorResultMessage(asyncDto, errCtrl, MessageIdConstant.E_SKF_1048, "郵便番号");
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		//形式チェック
//		if(!CheckUtils.isNumeric(zipCode) || zipCode.length() != 7){
		if(!CheckUtils.isNumeric(zipCode)){
			ServiceHelper.addErrorResultMessage(asyncDto, errCtrl, MessageIdConstant.E_SKF_1042, "郵便番号");
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}

		//住所情報取得
		Skf3010Sc002GetZipToAddressExpParameter param = new Skf3010Sc002GetZipToAddressExpParameter();
		param.setPost7Cd(zipCode);
		List<Skf3010Sc002GetZipToAddressExp> resultList = new ArrayList<Skf3010Sc002GetZipToAddressExp>();
		resultList = skf3010Sc002GetZipToAddressExpRepository.getZipToAddress(param);
		
		// 取得データレコード数判定
		if (resultList.size() <= 0) {
			// 取得データレコード数が0件場合、エラー設定して処理終了
			ServiceHelper.addErrorResultMessage(asyncDto, errCtrl, MessageIdConstant.E_SKF_1047);
			throwBusinessExceptionIfErrors(asyncDto.getResultMessages());
		}
		// 都道府県名
		if (resultList.get(0).getPrefNm() != null) {
			prefNm = resultList.get(0).getPrefNm();
		}
		// 住所
		if (resultList.get(0).getAddress() != null) {
			adderss = resultList.get(0).getAddress();
		}
		// 都道府県
		prefList.clear();
		prefList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_PREFCD, "", true));

		// 都道府県名から選択値を設定
		for (Map<String, Object> entity : prefList) {
			if (entity.containsKey("label") && prefNm.length() > 0) {
				if (entity.containsValue(prefNm)) {
					entity.put("selected", true);
					break;
				}
			}
		}
		// セッション情報削除
		asyncDto.setPrefList(null);
		asyncDto.setShatakuAddress(null);
		asyncDto.setZipCd(null);

		//住所設定
		asyncDto.setPrefList(prefList);
		asyncDto.setShatakuAddress(adderss);

		return asyncDto;
	}
}
