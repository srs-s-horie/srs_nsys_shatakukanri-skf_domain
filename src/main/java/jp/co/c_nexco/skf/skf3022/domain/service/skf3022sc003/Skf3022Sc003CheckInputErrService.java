/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc003.Skf3022Sc003CheckInputErrDto;

/**
 * Skf3022Sc003CheckInputErrService 使用料入力支援画面の入力チェックエラー時処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc003CheckInputErrService
	extends BaseServiceAbstract<Skf3022Sc003CheckInputErrDto> {

	@Autowired
	private SkfDropDownUtils ddlUtils;

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
	public Skf3022Sc003CheckInputErrDto index(Skf3022Sc003CheckInputErrDto initDto) throws Exception {

		/** JSON(連携用) */
		// 可変ラベルリスト
		List<Map<String, Object>> labelList = new ArrayList<Map<String, Object>>();
		/** ドロップダウン作成 */
		// 規格ドロップダウン
		List<Map<String, Object>> kikakuSelecteList = new ArrayList<Map<String, Object>>();
		// 用途ドロップダウン
		List<Map<String, Object>> youtoSelecteList = new ArrayList<Map<String, Object>>();
		// 規格ドロップダウン作成
		kikakuSelecteList.clear();
		kikakuSelecteList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN, initDto.getSc003KikakuSelect(), true));
		initDto.setSc003KikakuSelectList(kikakuSelecteList);
		// ②用途ドロップダウン
		youtoSelecteList.clear();
		youtoSelecteList.addAll(ddlUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_AUSE_KBN, initDto.getSc003YoutoSelect(), true));
		initDto.setSc003YoutoSelectList(youtoSelecteList);

		// エラーメッセージ設定
		// 規格
		if (!CheckUtils.isEmpty(initDto.getSc003KikakuSelectErr())) {
			LogUtils.debugByMsg("規格未設定");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1054, "規格");
		}
		// 用途
		if (!CheckUtils.isEmpty(initDto.getSc003YoutoSelectErr())) {
			LogUtils.debugByMsg("用途未設定");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1054, "①用途");
		}
		// 延べ面積
		if (CodeConstant.STRING_ZERO.equals(initDto.getSc003InputNobeMensekiErr())) {
			LogUtils.debugByMsg("②延べ面積が未入力");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1048, "②延べ面積");
			initDto.setSc003InputNobeMensekiErr(CodeConstant.NFW_VALIDATION_ERROR);
		} else if ("1".equals(initDto.getSc003InputNobeMensekiErr())) {
			LogUtils.debugByMsg("②延べ面積形式不正");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1042, "②延べ面積");
			initDto.setSc003InputNobeMensekiErr(CodeConstant.NFW_VALIDATION_ERROR);
		}
		// JSONList変換
		labelList.addAll(jsonArrayToArrayList(initDto.getSc003JsonLabelList()));
		Map <String, Object> labelMap = labelList.get(0);
		// 可変ラベル再設定
		initDto.setSc003KijunMenseki2(labelMap.get("sc003KijunMenseki2").toString());
		initDto.setSc003ShatakuMenseki2(labelMap.get("sc003ShatakuMenseki2").toString());
		initDto.setSc003KijunTanka2(labelMap.get("sc003KijunTanka2").toString());
		initDto.setSc003KeinenChouseinashiShiyoryo2(labelMap.get("sc003KeinenChouseinashiShiyoryo2").toString());
		initDto.setSc003PatternShiyoryo2(labelMap.get("sc003PatternShiyoryo2").toString());
		initDto.setSc003NenreikasanKeisu(labelMap.get("sc003NenreikasanKeisu").toString());
		initDto.setSc003ShatakuShiyoryo2(labelMap.get("sc003ShatakuShiyoryo2").toString());
		return initDto;
	}

	/**
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (Objects.equals(jsonStr, null) || jsonStr.length() <= 0) {
			LogUtils.debugByMsg("文字列未設定");
			// 文字列未設定のため処理しない
			return listData;
		}
		try {
			JSONArray arr = null;
			arr = new JSONArray(jsonStr);
			if (arr != null) {
				int arrCnt = arr.length();
				ObjectMapper mapper = new ObjectMapper();
				for(int i = 0; i < arrCnt; i++) {
					listData.add(mapper.readValue(arr.get(i).toString(), new TypeReference<Map<String, Object>>(){}));
				}
				arr = null;
				mapper = null;
			}
		} catch (JSONException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.debugByMsg(e.getMessage());
		} catch (IOException e) {
			LogUtils.debugByMsg(e.getMessage());
		}
		return listData;
	}
}
