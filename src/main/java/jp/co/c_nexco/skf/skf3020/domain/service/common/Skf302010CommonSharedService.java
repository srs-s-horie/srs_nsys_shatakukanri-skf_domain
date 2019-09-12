/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.common;

import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;

/**
 * Skf302010 共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf302010CommonSharedService {

	private final int NOT_EXIST_SPACE = -1;
	public final String NOT_CHECKED = "0";
	public final String CHECKED = "1";
	public final int COUNT_ZERO = 0;
	public final int COUNT_1 = 1;
	public final int DB_RESULT_NORMAL = 1;
	public final String KARI_K = "K";

	/** 現社宅区分：申請中 */
	public final String SINSEI_CHU = "2";
	/** 現社宅区分：入居中 */
	public final String NYUKYO_CHU = "1";
	/** 現社宅区分：入居中 */
	public final String MI_NYUKYO = "0";
	/** 入退居予定作成区分：未作成 */
	public final String MI_SAKUSEI = "0";

	public final String INSERT = "1";
	public final String UPDATE = "2";

	/** 画面表示用の改行 */
	public final String DISPLAY_LS = "\n";
	/** DB登録時に使用する改行コード */
	public final String DB_LS = "&lt;br /&gt;";
	/** 転任調書テーブルのupdateData保持用key */
	public final String TENNIN_CHOSHO_DATA_UPDATE_KEY = "Skf3020TTenninshaChoshoDataUpdateDate";
	/** 社宅社員マスタテーブルのupdateData保持用key */
	public final String SHAIN_DATA_UPDATE_KEY = "Skf1010MShainUpdateDate";

	/**
	 * スペースが含まれているかチェックする。
	 * 
	 * @param targetVal
	 *            チェック対象
	 * @return 結果
	 */
	public boolean isExistSpace(String targetVal) {

		if (NfwStringUtils.isEmpty(targetVal)) {
			return false;
		}

		if (targetVal.indexOf(CodeConstant.SPACE) != NOT_EXIST_SPACE
				|| targetVal.indexOf(CodeConstant.ZEN_SPACE) != NOT_EXIST_SPACE) {
			return true;
		}

		return false;
	}

	/**
	 * 文字列がnullの場合、空文字を返却する。
	 * 
	 * @param inVal
	 *            対象文字列
	 * @return 文字列
	 */
	public String cnvString(String inVal) {

		String outStr = "";

		if (NfwStringUtils.isEmpty(inVal)) {
			outStr = "";
		} else {
			outStr = inVal;
		}

		return outStr;
	}

}
