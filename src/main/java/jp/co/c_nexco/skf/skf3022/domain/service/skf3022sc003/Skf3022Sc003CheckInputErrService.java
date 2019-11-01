/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc003;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc003.Skf3022Sc003CheckInputErrDto;
import jp.co.intra_mart.common.platform.log.Logger;

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
	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

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
			logger.debug("規格未設定");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1054, "規格");
		}
		// 用途
		if (!CheckUtils.isEmpty(initDto.getSc003YoutoSelectErr())) {
			logger.debug("用途未設定");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1054, "①用途");
		}
		// 延べ面積
		if (CodeConstant.STRING_ZERO.equals(initDto.getSc003InputNobeMensekiErr())) {
			logger.debug("②延べ面積が未入力");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1048, "②延べ面積");
			initDto.setSc003InputNobeMensekiErr(CodeConstant.NFW_VALIDATION_ERROR);
		} else if ("1".equals(initDto.getSc003InputNobeMensekiErr())) {
			logger.debug("②延べ面積形式不正");
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1042, "②延べ面積");
			initDto.setSc003InputNobeMensekiErr(CodeConstant.NFW_VALIDATION_ERROR);
		}
		return initDto;
	}
}
