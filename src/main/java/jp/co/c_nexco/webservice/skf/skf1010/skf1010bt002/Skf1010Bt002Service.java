/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.webservice.skf.skf1010.skf1010bt002;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShainMstExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShainMstRetiredExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShainMstRetiredExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsUpdateBatchControlExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShatakuShainRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TBatchControl;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010WShain;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoWkExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShainMstExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShainMstRetiredExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertShatakuShainMasterInfoWkExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsInsertWkShainExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsUpdateBatchControlExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfRollBack.SkfRollBackExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShatakuShainRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010TBatchControlRepository;
import jp.co.c_nexco.nfw.common.bean.SpringContext;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webservice.BaseWebServiceAbstract;
import jp.co.c_nexco.nfw.webservice.exception.NotFoundException;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfStringUtils;
import jp.co.c_nexco.skf.skf1010.domain.dto.common.SkfPersonnalBatchCommonDto;
import jp.co.intra_mart.common.platform.util.DateUtil;
import jp.co.intra_mart.foundation.web_api_maker.annotation.BasicAuthentication;
import jp.co.intra_mart.foundation.web_api_maker.annotation.GET;
import jp.co.intra_mart.foundation.web_api_maker.annotation.PUT;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Path;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Response;
import jp.co.intra_mart.framework.system.exception.SystemException;

/**
 * SKF1010_BT002_社員マスタデータ取込のWebService用サービス。
 * 
 * @author NEXCOシステムズ
 *
 */
@BasicAuthentication(pathPrefix = "api/skf/v1.0/skf1010bt002")
public class Skf1010Bt002Service extends BaseWebServiceAbstract {

	private String companyCd = CodeConstant.C001;
	private String batchResult;

	// 変換用ひらがな
	private final String TL_HIRAGANA = "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよらりるれろわをんがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽぁぃぅぇぉっゃゅょーヰゐゑヵヶ";
	// 変換用カタカナ
	private final String TL_KATAKANA = "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワヲンガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポァィゥェォッャュョーイイエヵヶ";
	// 補正ステータス
	private final String CORRECTION_STATUS = "2000";

	// ユーザ種別
	private final String USER_TYPE = "10";

	// ユーザ種別
	private final String USE_NOTES = "0";

	// バッチ処理名
	private final String BATCH_NAME_KYUYOKOUSEI = "社員データ取込";
	private final String BATCH_NAME_SHATAKU = "従業員データ取込";

	// 処理区分
	private final String OPERATION_KBN_RIREKI = "0"; // 社宅社員異動履歴
	private final String OPERATION_KBN_SHAIN = "1"; // 社宅社員マスタ

	// 登録区分
	private final String INSERT_KBN = "1";
	private final String UPDATE_KBN = "2";

	/**
	 * コンストラクタ。
	 */
	public Skf1010Bt002Service() {
		super(Skf1010Bt002Service.class);
	}

	/**
	 * 従業員データ取込バッチの実行。
	 * 
	 * @return バッチ処理結果
	 * @throws NotFoundException 例外
	 */
	@Path("")
	@GET
	@Response(code = 200)
	public SkfPersonnalBatchCommonDto get() {

		// 開始ログ出力
		LogUtils.info(MessageIdConstant.I_SKF_1022, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002, "従業員データ取込");

		// 返却用Dto
		SkfPersonnalBatchCommonDto dto = new SkfPersonnalBatchCommonDto();

		// バッチ処理結果
		// 0:処理中
		// 1：処理完了
		// 2：異常終了
		batchResult = SkfCommonConstant.PROCESSING;
		// ログ管理ファイル名作成
		String logFileName = getBatchLogFileName();

		List<String> logList = new ArrayList<String>();

		try {
			// 二重起動チェック
			boolean doubleExecRes = doubleExecuteCheck();
			if (!doubleExecRes) {
				// 二重起動されていない場合、バッチ制御テーブルにデータを投入する
				boolean doubleExecInsRes = insertBatchControlFunc(logFileName);
				if (!doubleExecInsRes) {
					// エラーログ出力
					batchResult = SkfCommonConstant.ABNORMAL;
					// 処理結果：バッチ制御テーブルへのデータ投入エラー
					LogUtils.error(MessageIdConstant.E_SKF_1073);
					logList.add(getMsg(MessageIdConstant.E_SKF_1073));
					LogUtils.error(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
							batchResult);
					logList.add(getMsg(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
							batchResult));
					dto.setStatus(batchResult);
					dto.setMessage(String.join(CodeConstant.ESC_LF, logList.toArray(new String[logList.size()])));
					return dto;
				}
				boolean kyuyoKouseiRes = updateKyuyoKousei(logList);
				if (!kyuyoKouseiRes) {
					// エラーログ出力
					batchResult = SkfCommonConstant.ABNORMAL;
					LogUtils.error(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
							batchResult);
				} else {
					boolean shatakuRes = updateShataku(logList);
					if (!shatakuRes) {
						// エラーログ出力
						batchResult = SkfCommonConstant.ABNORMAL;
						LogUtils.error(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
								batchResult);
					}
				}
			} else {
				// エラーログ出力
				batchResult = SkfCommonConstant.ABNORMAL;
				// 処理結果:重複起動エラー
				LogUtils.error(MessageIdConstant.E_SKF_1080, companyCd, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002);
				LogUtils.error(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002, batchResult);
			}

		} catch (Exception ex) {
			// 異常の場合
			// エラーログ出力
			batchResult = SkfCommonConstant.ABNORMAL;

			if (!endPrc(String.valueOf(batchResult), FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
					SkfCommonConstant.PROCESSING)) {
				return dto;
			}
			// 処理結果
			LogUtils.error(MessageIdConstant.E_SKF_1131, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002, batchResult);
			logList.add(
					getMsg(MessageIdConstant.E_SKF_1131, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002, batchResult));
			logList.add(ex.getMessage());

		}
		// エラーが無ければ完了フラグを入れる
		if (!CheckUtils.isEqual(String.valueOf(batchResult), SkfCommonConstant.ABNORMAL)) {
			batchResult = SkfCommonConstant.COMPLETE;
		}

		// 処理結果返却
		if (!endPrc(String.valueOf(batchResult), FunctionIdConstant.BATCH_CLASS_SKF1010_BT002,
				SkfCommonConstant.PROCESSING)) {
			dto.setStatus(batchResult);
			dto.setMessage(String.join(CodeConstant.ESC_LF, logList.toArray(new String[logList.size()])));
			return dto;
		}

		// 終了ログ出力
		LogUtils.info(MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT002, batchResult);
		dto.setStatus(batchResult);
		dto.setMessage(String.join(CodeConstant.ESC_LF, logList.toArray(new String[logList.size()])));
		// 処理結果返却
		// return skfPersonnalBatchCommonDto;
		return dto;
	}

	private boolean endPrc(String endFlg, String programId, String searchEndFlg) {
		Date endDate = new Date();
		SkfPerssonalBatchUtilsUpdateBatchControlExp updData = new SkfPerssonalBatchUtilsUpdateBatchControlExp();
		updData.setCompanyCd(companyCd);
		updData.setEndDate(endDate);
		updData.setEndFlg(endFlg);
		updData.setProgramId(programId);
		updData.setSearchEndFlg(searchEndFlg);
		SkfPerssonalBatchUtilsUpdateBatchControlExpRepository repository = (SkfPerssonalBatchUtilsUpdateBatchControlExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsUpdateBatchControlExpRepository");
		int res = repository.updateBatchControl(updData);
		if (res <= 0) {
			return false;
		}
		return true;
	}

	// 給与厚生側の処理
	/**
	 * 給与厚生側の社員データ取込処理
	 * 
	 * @param logList
	 * 
	 * @return
	 */
	private boolean updateKyuyoKousei(List<String> logList) throws Exception {
		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME_KYUYOKOUSEI);

		int shainDelCnt = 0;
		int shainInsCnt = 0;

		// ワーク社員マスタに複写
		if (!insertWkShain()) {
			return false;
		}

		// 社員マスタのデータを全件削除
		shainDelCnt = deleteShatakuShainMasterInfo(null, true);

		// 条件で社員マスタを作成
		shainInsCnt = insertShainMst();

		// 退職社員を社員マスタに追加
		shainInsCnt += insertShainMstRetied();

		// ログ出力
		// 社員マスタ削除件数
		LogUtils.info(MessageIdConstant.I_SKF_3093, shainDelCnt);
		logList.add(getMsg(MessageIdConstant.I_SKF_3093, shainDelCnt));
		// 社員マスタ登録件数
		LogUtils.info(MessageIdConstant.I_SKF_3094, shainInsCnt);
		logList.add(getMsg(MessageIdConstant.I_SKF_3094, shainInsCnt));

		return true;
	}

	/**
	 * 現在の社員データをワーク社宅社員マスタにコピーします
	 * 
	 * @return
	 */
	private boolean insertWkShain() {
		// ワーク社員マスタを初期化
		int delRes = deleteShatakuShainMasterInfoWk();
		if (delRes <= 0) {
			return false;
		}
		// ワーク社宅社員マスタに現社員マスタのデータを複写する
		SkfPerssonalBatchUtilsInsertWkShainExpRepository repository = (SkfPerssonalBatchUtilsInsertWkShainExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsInsertWkShainExpRepository");
		int res = repository.insertWkShain();
		if (res < 0) {
			return false;
		}

		return true;
	}

	/**
	 * 社員マスタ登録
	 * 
	 * @return
	 * @throws Exception
	 */
	private int insertShainMst() throws Exception {
		SkfPerssonalBatchUtilsInsertShainMstExpRepository repository = (SkfPerssonalBatchUtilsInsertShainMstExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsInsertShainMstExpRepository");
		SkfPerssonalBatchUtilsInsertShainMstExpParameter parameter = new SkfPerssonalBatchUtilsInsertShainMstExpParameter();
		parameter.setCompanyCd(companyCd);
		parameter.setHiragana(TL_HIRAGANA);
		parameter.setKatakana(TL_KATAKANA);
		parameter.setCorrectionStatus(CORRECTION_STATUS);
		parameter.setUserType(USER_TYPE);
		parameter.setUseNotes(USE_NOTES);
		int res = repository.insertShainMst(parameter);
		return res;
	}

	/**
	 * 退職社員を登録します
	 * 
	 * @return
	 */
	private int insertShainMstRetied() {
		// 月末日取得
		String importMonths = "2";

		SkfPerssonalBatchUtilsInsertShainMstRetiredExpRepository repository = (SkfPerssonalBatchUtilsInsertShainMstRetiredExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsInsertShainMstRetiredExpRepository");
		SkfPerssonalBatchUtilsInsertShainMstRetiredExpParameter param = new SkfPerssonalBatchUtilsInsertShainMstRetiredExpParameter();
		param.setCompanyCd(companyCd);
		param.setHiragana(TL_HIRAGANA);
		param.setKatakana(TL_KATAKANA);
		param.setCorrectionStatus(CORRECTION_STATUS);
		param.setUserType(USER_TYPE);
		param.setUseNotes(USE_NOTES);
		param.setImportMonths(importMonths);
		int res = repository.insertShainMstRetired(param);
		return res;
	}

	// 社宅側の処理
	/**
	 * 社宅側の従業員データ取込処理
	 * 
	 * @param logList
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	private boolean updateShataku(List<String> logList) throws Exception {
		// 開始ログ出力
		LogUtils.info(MessageIdConstant.I_SKF_1022, BATCH_NAME_SHATAKU);

		String firstDate = CodeConstant.NONE;
		String lastDate = DateUtils.getLastDate(new Date());
		String nowDate = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		Pattern pattern = Pattern.compile("^([0-2][0-9]{3}/[0-1][0-9])");
		Matcher matcher = pattern.matcher(nowDate);
		if (matcher.find()) {
			firstDate = matcher.group() + "/01";
		}
		// 月末月初の場合
		String beginingEndKbn = CodeConstant.NONE;
		if (CheckUtils.isEqual(nowDate, firstDate)) {
			beginingEndKbn = CodeConstant.BEGINNING_END_KBN_MONTH_BEGIN;
		} else if (CheckUtils.isEqual(nowDate, lastDate)) {
			beginingEndKbn = CodeConstant.BEGINNING_END_KBN_MONTH_END;
		}
		if (NfwStringUtils.isNotEmpty(beginingEndKbn)) {
			boolean res = createShatakuShainRireki(beginingEndKbn);
			if (!res) {
				batchResult = SkfCommonConstant.ABNORMAL;
				// 管理ログ出力
				// S00001：予期せぬエラーが発生しました。ヘルプデスクへ連絡してください。
				LogUtils.error(MessageIdConstant.E_SKF_1079);
				logList.add(getMsg(MessageIdConstant.E_SKF_1079));
				return false;
			}
		}
		updateShatakuShain();
		return true;
	}

	/**
	 * バッチ制御テーブルにデータを投入する
	 * 
	 * @param logFileName
	 * @return
	 */
	private boolean insertBatchControlFunc(String logFileName) {
		Skf1010TBatchControl insertData = new Skf1010TBatchControl();
		Date execDate = new Date();
		insertData.setCompanyCd(companyCd); // 会社コード
		insertData.setStartDate(execDate); // 開始日時
		insertData.setProgramId(FunctionIdConstant.BATCH_CLASS_SKF1010_BT002); // プログラムID
		insertData.setUserId(SkfCommonConstant.FIXED_NAME_BATCH); // ユーザーID
		insertData.setEndFlg(SkfCommonConstant.PROCESSING); // 終了フラグ（0：処理中）
		insertData.setLogFileName(logFileName);
		Skf1010TBatchControlRepository repository = (Skf1010TBatchControlRepository) SpringContext
				.getBean("skf1010TBatchControlRepository");
		int res = repository.insertSelective(insertData);
		if (res > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 二重起動チェック
	 * 
	 * @return
	 */
	private boolean doubleExecuteCheck() {
		SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExp res = new SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExp();
		SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpParameter parameter = new SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpParameter();
		parameter.setCompanyCd(companyCd);
		parameter.setEndFlg(SkfCommonConstant.PROCESSING);
		parameter.setProgramId(FunctionIdConstant.BATCH_CLASS_SKF1010_BT002);
		SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpRepository repository = (SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsGetNijuuKidouCheckCountExpRepository");
		res = repository.getNijuuKidouCheckCount(parameter);
		if (res != null) {
			long cnt = res.getExpr1();
			if (cnt > 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 社宅社員異動履歴登録
	 * 
	 * @param beginningEndKbn
	 * @return
	 */
	private boolean createShatakuShainRireki(String beginningEndKbn) {
		List<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExp> preUserMasterList = new ArrayList<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExp>();
		SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExpRepository getBaseShainInfoRepository = (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsGetIdmPreUserMasterInfoExpRepository");
		// 人事連携用従業員データの取得
		preUserMasterList = getBaseShainInfoRepository.getIdmPreUserMasterInfo();
		if (preUserMasterList == null || preUserMasterList.size() <= 0) {
			for (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExp preUserMasterInfo : preUserMasterList) {
				// 年月を取得
				Date inputDate = preUserMasterInfo.getInputDate();
				String yearMonth = DateUtils.format(inputDate, SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);
				// ●社宅社員異動履歴データの存在チェック
				int cnt = getShatakuShainRirekiCount(preUserMasterInfo.getShainNo(), yearMonth, beginningEndKbn);
				if (cnt > 0) {
					// ●データが存在する場合、該当データを物理削除する。
					boolean delRes = deleteShatakuShainRirekiInfo(preUserMasterInfo.getShainNo(), yearMonth,
							beginningEndKbn);
					if (!delRes) {
						return false;
					}

					// 社宅社員異動履歴テーブルへデータを登録する
					Skf1010MShatakuShainRireki insertRireki = new Skf1010MShatakuShainRireki();
					setInsertShatakuShainRireki(insertRireki, preUserMasterInfo, beginningEndKbn);
					boolean rirekiInsRes = insertShatakuShainRirekiInfo(insertRireki);
					if (!rirekiInsRes) {
						return false;
					}
				}

			}
		}
		return true;
	}

	private boolean updateShatakuShain() throws Exception {
		// １．ワーク社宅社員マスタ(WK_M_SHATAKU_SHAIN)のデータを全件削除する。
		if (0 > deleteShatakuShainMasterInfoWk()) {
			return false;
		}
		// ２．ワーク社宅社員マスタ(WK_M_SHATAKU_SHAIN)に社宅社員マスタ(skf1010_m_shain)へコピーする。
		if (0 > insertShatakuShainMasterInfoWk()) {
			return false;
		}

		// ３．IdM_プレユーザマスタより、社宅社員マスタデータを準備する。
		List<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp> baseShainInfoList = new ArrayList<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp>();
		// 人事連携用従業員データの取得
		baseShainInfoList = getIdmPreUserMasterInfo();

		// ４．社宅社員マスタ(SKF1010_M_SHAIN)から、３で取得した社員を物理削除する。
		if (baseShainInfoList != null && baseShainInfoList.size() > 0) {
			// ５．（３）で取得した社員一覧の情報で、社宅社員マスタ(skf1010_m_shain)に登録する。
			for (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp idmShain : baseShainInfoList) {
				// 社員番号で社員情報を削除する
				if (0 > deleteShatakuShainMasterInfo(idmShain.getShainNo(), false)) {
					return false;
				}
			}

		}
		// ５．（３）で取得した社員一覧の情報で、社宅社員マスタ(skf1010_m_shain)に登録する。
		if (baseShainInfoList != null && baseShainInfoList.size() > 0) {
			Skf1010MShainRepository shainRepository = (Skf1010MShainRepository) SpringContext
					.getBean("skf1010MShainRepository");
			for (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp idmShain : baseShainInfoList) {
				// 登録データをマッピングする
				Skf1010MShain insertShain = new Skf1010MShain();
				getColumnInfoList(OPERATION_KBN_SHAIN, INSERT_KBN, insertShain, null, idmShain);
				if (0 > shainRepository.insertSelective(insertShain)) {
					return false;
				}
			}
		}
		// ６．２でバックアップした社員一覧の情報で、社宅社員マスタ(skf1010_m_shain)を更新する。
		List<SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp> wShainList = new ArrayList<SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp>();
		SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExpRepository wkShainRepository = (SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExpRepository");
		wShainList = wkShainRepository.getWorkShatakuShainMasterInfo();
		if (wShainList != null && wShainList.size() > 0) {
			for (SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp wShainInfo : wShainList) {
				Skf1010MShain shainInfo = new Skf1010MShain();
				getColumnInfoList(OPERATION_KBN_SHAIN, UPDATE_KBN, shainInfo, null, wShainInfo);
				if (!updateShatakuShainMasterInfo(shainInfo)) {
					return false;
				}
			}
		}
		return true;
	}

	private void getColumnInfoList(String flg, String updateKbn, Skf1010MShain shainInfo,
			Skf1010MShatakuShainRireki rirekiInfo, Object dataEntity) {
		if (CheckUtils.isEqual(flg, OPERATION_KBN_SHAIN)) {
			if (shainInfo == null) {
				return;
			}

			if (CheckUtils.isEqual(updateKbn, INSERT_KBN)) {
				SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp idmShain = (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp) dataEntity;
				// 会社コード
				shainInfo.setCompanyCd(idmShain.getCompanyCd());
				// 社員番号
				shainInfo.setShainNo(idmShain.getShainNo());
				// 氏名カナ
				shainInfo.setNameKk(idmShain.getNameKk());
				// 氏名
				shainInfo.setName(idmShain.getName());
				// 機関コード
				shainInfo.setAgencyCd(idmShain.getAgencyCd());
				// 部等コード
				shainInfo.setAffiliation1Cd(idmShain.getAffiliation1Cd());
				// 室、チーム又は課コード
				shainInfo.setAffiliation2Cd(idmShain.getAffiliation2Cd());
				// 誕生日：年
				String btYYYY = idmShain.getBtYYYY();
				if (NfwStringUtils.isNotEmpty(btYYYY)) {
					shainInfo.setBirthdayYear(Short.parseShort(btYYYY));
				}
				// 誕生日：月
				String btMM = idmShain.getBtMM();
				if (NfwStringUtils.isNotEmpty(btMM)) {
					shainInfo.setBirthdayMonth(Short.parseShort(btMM));
				}
				// 誕生日：日
				String btDD = idmShain.getBtDD();
				if (NfwStringUtils.isNotEmpty(btDD)) {
					shainInfo.setBirthdayDay(Short.parseShort(btDD));
				}
				// ロールID
				shainInfo.setRoleId("SKF_001");
				// 登録フラグ
				shainInfo.setRegistFlg(SkfCommonConstant.REGIST_SHINJO);
			} else if (CheckUtils.isEqual(updateKbn, UPDATE_KBN)) {
				SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp wShainInfo = (SkfPerssonalBatchUtilsGetWorkShatakuShainMasterInfoExp) dataEntity;
				// プライマリキー
				shainInfo.setCompanyCd(wShainInfo.getCompanyCd());
				shainInfo.setShainNo(wShainInfo.getShainNo());
				// 更新データ
				shainInfo.setUserId(wShainInfo.getUserId());
				shainInfo.setRoleId(wShainInfo.getRoleId());
				shainInfo.setRetireFlg(wShainInfo.getRetireFlg());
				shainInfo.setRetireDate(wShainInfo.getRetireDate());
				shainInfo.setShainNoChangeFlg(wShainInfo.getShainNoChangeFlg());
				shainInfo.setShainNoChangeDate(wShainInfo.getShainNoChangeDate());

			}
		}

	}

	/**
	 * ワーク社宅社員マスタデータを使用して社宅社員マスタデータを更新する
	 * 
	 * @param shainInfo
	 * @return
	 */
	private boolean updateShatakuShainMasterInfo(Skf1010MShain shainInfo) {
		Skf1010MShainRepository repository = (Skf1010MShainRepository) SpringContext.getBean("skf1010MShainRepository");
		int res = repository.updateByPrimaryKeySelective(shainInfo);
		if (res < 0) {
			return false;
		}
		return true;
	}

	private void mappingShainData(Skf1010MShain insertShain,
			SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp idmShain) {
		// 会社コード
		insertShain.setCompanyCd(idmShain.getCompanyCd());
		// 社員番号
		insertShain.setShainNo(idmShain.getShainNo());
		// 氏名カナ
		insertShain.setNameKk(idmShain.getNameKk());
		// 氏名
		insertShain.setName(idmShain.getName());
		// 機関コード
		insertShain.setAgencyCd(idmShain.getAgencyCd());
		// 部等コード
		insertShain.setAffiliation1Cd(idmShain.getAffiliation1Cd());
		// 室、チーム又は課コード
		insertShain.setAffiliation2Cd(idmShain.getAffiliation2Cd());
		// 誕生日：年
		String btYYYY = idmShain.getBtYYYY();
		if (NfwStringUtils.isNotEmpty(btYYYY)) {
			insertShain.setBirthdayYear(Short.parseShort(btYYYY));
		}
		// 誕生日：月
		String btMM = idmShain.getBtMM();
		if (NfwStringUtils.isNotEmpty(btMM)) {
			insertShain.setBirthdayMonth(Short.parseShort(btMM));
		}
		// 誕生日：日
		String btDD = idmShain.getBtDD();
		if (NfwStringUtils.isNotEmpty(btDD)) {
			insertShain.setBirthdayDay(Short.parseShort(btDD));
		}
		// ロールID
		insertShain.setRoleId("SKF_001");
		// 登録フラグ
		insertShain.setRegistFlg(SkfCommonConstant.REGIST_SHINJO);

	}

	private List<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp> getIdmPreUserMasterInfo() {
		List<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp> result = new ArrayList<SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2Exp>();
		SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpRepository repository = (SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpRepository");
		SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpParameter param = new SkfPerssonalBatchUtilsGetIdmPreUserMasterInfo2ExpParameter();
		param.setCompanyCd(companyCd);
		param.setHiragana(TL_HIRAGANA);
		param.setKatakana(TL_KATAKANA);
		// 人事連携用従業員データの取得
		result = repository.getIdmPreUserMasterInfo2(param);

		return result;
	}

	/**
	 * 現在の社宅社員マスタのデータをワーク社宅社員マスタにコピーします
	 * 
	 * @return
	 */
	private int insertShatakuShainMasterInfoWk() {
		SkfPerssonalBatchUtilsInsertShatakuShainMasterInfoWkExpRepository repository = (SkfPerssonalBatchUtilsInsertShatakuShainMasterInfoWkExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsInsertShatakuShainMasterInfoWkExpRepository");
		int res = repository.insertShatakuShainMasterInfoWk();
		return res;
	}

	private int getShatakuShainRirekiCount(String shainNo, String yearMonth, String beginingEndKbn) {
		SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExp result = new SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExp();
		SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpParameter rirekiParam = new SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpParameter();
		rirekiParam.setCompanyCd(companyCd);
		rirekiParam.setShainNo(shainNo);
		rirekiParam.setYearMonth(yearMonth);
		rirekiParam.setBeginingEndKbn(beginingEndKbn);
		SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpRepository rirekiRepository = (SkfPerssonalBatchUtilsGetShatakuShainRirekiCountExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsGetShatakuShainRirekiCountExpRepository");
		result = rirekiRepository.getShatakuShainRirekiCount(rirekiParam);
		if (result != null) {
			return Integer.parseInt(result.getCnt());
		}
		return 0;
	}

	private boolean deleteShatakuShainRirekiInfo(String shainNo, String yearMonth, String beginingEndKbn) {
		SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExp param = new SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExp();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		param.setYearMonth(yearMonth);
		param.setBeginingEndKbn(beginingEndKbn);
		SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExpRepository delRepository = (SkfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsDeleteShatakuShainRirekiInfoExpRepository");
		int result = delRepository.deleteShatakuShainRirekiInfo(param);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * ログファイル名作成
	 * 
	 * @return
	 */
	private String getBatchLogFileName() {
		StringBuilder fileName = new StringBuilder();
		fileName.append(FunctionIdConstant.BATCH_CLASS_SKF1010_BT002);
		fileName.append(CodeConstant.UNDER_SCORE);
		fileName.append(String.valueOf(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH)));

		String logFileName = fileName.toString();
		return logFileName;
	}

	/**
	 * 社員異動履歴テーブル登録データマッピング
	 * 
	 * @param insertRireki
	 * @param preUserMasterInfo
	 * @param beginningEndKbn
	 */
	private void setInsertShatakuShainRireki(Skf1010MShatakuShainRireki insertRireki,
			SkfPerssonalBatchUtilsGetIdmPreUserMasterInfoExp preUserMasterInfo, String beginningEndKbn) {
		// 会社コード
		insertRireki.setCompanyCd(companyCd);
		// 社員番号
		insertRireki.setShainNo(preUserMasterInfo.getShainNo());
		// 年月
		Date inputDate = preUserMasterInfo.getInputDate();
		String yearMonth = DateUtils.format(inputDate, SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);
		insertRireki.setYearMonth(yearMonth);
		// 月初月末区分
		insertRireki.setBeginningEndKbn(beginningEndKbn);
		// 機関コード
		insertRireki.setAgencyCd(preUserMasterInfo.getAgencyCdAsSoshikiagencycd());
		// 機関名称
		insertRireki.setAgencyName(preUserMasterInfo.getAgencyName());
		// 属 Ⅰコード
		insertRireki.setAffiliation1Cd(preUserMasterInfo.getAffiliation1Cd());
		// 部等名称
		insertRireki.setAffiliation1Name(preUserMasterInfo.getAffiliation1Name());
		// 属 Ⅱコード
		insertRireki.setAffiliation2Cd(preUserMasterInfo.getAffiliation2Cd());
		// 室課名称
		insertRireki.setAffiliation2Name(preUserMasterInfo.getAffiliation2Name());
		// 事業領域コード
		insertRireki.setBusinessAreaCd(preUserMasterInfo.getBusinessAreaCdAsBusinessareabusinessareacd());
		// 事業領域名称
		insertRireki.setBusinessAreaName(preUserMasterInfo.getBusinessAreaName());

	}

	/**
	 * 社員異動履歴テーブルへデータを登録します
	 * 
	 * @param insertRireki
	 * @return
	 */
	private boolean insertShatakuShainRirekiInfo(Skf1010MShatakuShainRireki insertRireki) {
		Skf1010MShatakuShainRirekiRepository repository = (Skf1010MShatakuShainRirekiRepository) SpringContext
				.getBean("skf1010MShatakuShainRirekiRepository");
		int result = repository.insertSelective(insertRireki);
		if (result > 0) {
			return true;
		}
		return false;
	}

	///////////////////////// 共通処理
	///////////////////////// //////////////////////////////////////////////
	/**
	 * ワーク社宅社員マスタデータ削除
	 * 
	 * @return
	 */
	private int deleteShatakuShainMasterInfoWk() {
		SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoWkExpRepository repository = (SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoWkExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsDeleteShatakuShainMasterInfoWkExpRepository");
		int res = repository.deleteShatakuShainMasterInfoWk();

		return res;
	}

	/**
	 * 社宅社員マスタデータ削除
	 * 
	 * @return
	 */
	private int deleteShatakuShainMasterInfo(String shainNo, boolean isSetRegistFlg) throws Exception {
		SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExpRepository repository = (SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExpRepository) SpringContext
				.getBean("skfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExpRepository");
		SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExp param = new SkfPerssonalBatchUtilsDeleteShatakuShainMasterInfoExp();
		if (isSetRegistFlg) {
			param.setRegistFlg(SkfCommonConstant.REGIST_SHINSEI);
		}
		if (NfwStringUtils.isNotEmpty(shainNo)) {
			param.setCompanyCd(companyCd);
			param.setShainNo(shainNo);
		}
		int res = repository.deleteShatakuShainMasterInfo(param);

		return res;
	}

	/**
	 * 終了メッセージを取得する。
	 * 
	 * @param messageId メッセージID
	 * @param params メッセージパラメータ
	 * @return 終了メッセージ
	 */
	private static String getMsg(String messageId, Object... params) {
		return ResultMessageUtils.resolveMessage(ResultMessage.fromCode(messageId, params),
				(MessageSource) SpringContext.getBean("messageSource"));
	}

}
