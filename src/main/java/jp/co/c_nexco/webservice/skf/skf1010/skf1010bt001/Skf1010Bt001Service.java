/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.webservice.skf.skf1010.skf1010bt001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessageUtils;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsUpdateBatchControlExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteTokyuMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteWkSoshikiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetInsertSoshikMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetInsertSoshikMasterExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertTokyuMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertWkSoshikiMasterExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010TBatchControl;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsGetNijuuKidouCheckCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfPerssonalBatchUtils.SkfPerssonalBatchUtilsUpdateBatchControlExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteTokyuMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsDeleteWkSoshikiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetInsertSoshikMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertTokyuMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsInsertWkSoshikiMasterExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfSoshikiBatchUtils.SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010TBatchControlRepository;
import jp.co.c_nexco.nfw.common.bean.SpringContext;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webservice.BaseWebServiceAbstract;
import jp.co.c_nexco.nfw.webservice.exception.NotFoundException;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.skf1010.domain.dto.common.SkfPersonnalBatchCommonDto;
import jp.co.intra_mart.foundation.web_api_maker.annotation.BasicAuthentication;
import jp.co.intra_mart.foundation.web_api_maker.annotation.GET;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Path;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Response;

/**
 * SKF1010_BT001_組織マスタデータ取込のWebService用サービス。
 * 
 * @author NEXCOシステムズ
 *
 */
@BasicAuthentication(pathPrefix = "api/skf/v1.0/skf1010bt001")
public class Skf1010Bt001Service extends BaseWebServiceAbstract {

	private String companyCd = CodeConstant.C001;
	private String batchResult;
	private List<String> logList = new ArrayList<String>();

	// カウンター
	// 全件数カウンター
	private int recordCount = 0;
	// 組織マスタの削除件数
	private int deleteCount = 0;
	// 組織マスタの登録件数
	private int insertCount = 0;
	// 組織マスタの住所情報変更件数
	private int updateCount = 0;
	// 身上データシステム：機関コード（役員）
	private String SHINJO_AGENCY_CD_YAKUIN = "00";

	// 保存世代
	private final String SAVE_GENERATION = "3";

	// 保存世代初期値
	private final String DEFAULT_GENERATION_NO = "0";

	// I-SKF-1037情報メッセージの{0}置換文字列
	private final String I_SKF_1037_INFO_PARAM_0 = "社員";

	// I-SKF-1037情報メッセージの{1}置換文字列
	private final String I_SKF_1037_INFO_PARAM_1 = "組織情報";

	// I-SKF-1037情報メッセージの{2}置換文字列
	private final String I_SKF_1037_INFO_PARAM_2 = "（社員コード：**）";

	// テーブル名
	private final String TABLE_NAME_SOSHIKI = "組織マスタ";
	private final String TABLE_NAME_TOKYU = "等級マスタ";

	// バッチ処理名
	private final String BATCH_NAME_MAIN = "組織データ取込";
	private final String BATCH_NAME_TOKYU = "等級データ取込";

	// ログ区分
	private final String INFO = "info";
	private final String ERROR = "error";

	/**
	 * コンストラクタ。
	 */
	public Skf1010Bt001Service() {
		super(Skf1010Bt001Service.class);
	}

	/**
	 * 組織データ取込バッチの実行。
	 * 
	 * @return バッチ処理結果
	 * @throws NotFoundException 例外
	 */
	@Path("")
	@GET
	@Response(code = 200)
	public SkfPersonnalBatchCommonDto get() {

		// 開始ログ出力
		setLog(INFO, MessageIdConstant.I_SKF_1022, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001, BATCH_NAME_MAIN);

		// 返却用Dto
		SkfPersonnalBatchCommonDto dto = new SkfPersonnalBatchCommonDto();

		// バッチ処理結果
		// 0:処理中
		// 1：処理完了
		// 2：異常終了
		batchResult = SkfCommonConstant.PROCESSING;

		try {
			// 二重起動チェック
			boolean doubleExecRes = doubleExecuteCheck();
			if (!doubleExecRes) {
				// 二重起動されていない場合、バッチ制御テーブルにデータを投入する
				boolean doubleExecInsRes = insertBatchControlFunc();
				if (!doubleExecInsRes) {
					// エラーログ出力
					batchResult = SkfCommonConstant.ABNORMAL;
					// 処理結果：バッチ制御テーブルへのデータ投入エラー
					setLog(ERROR, MessageIdConstant.E_SKF_1073);
					setLog(INFO, MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001,
							batchResult);
					dto.setStatus(batchResult);
					dto.setMessage(String.join(CodeConstant.DOUBLE_SPACE, logList.toArray(new String[logList.size()])));
					return dto;
				}
				boolean soshikiRes = updateSoshikiMaster();
				if (!soshikiRes) {
					// エラーログ出力
					batchResult = SkfCommonConstant.ABNORMAL;
					setLog(INFO, MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001,
							batchResult);
				}

				// 等級データ取込処理
				boolean tokyuRes = updateTokyu();
				if (!tokyuRes) {
					// エラーログ出力
					batchResult = SkfCommonConstant.ABNORMAL;
					setLog(INFO, MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001,
							batchResult);
				}
			} else {
				// 処理結果を異常終了に設定
				batchResult = SkfCommonConstant.ABNORMAL;
				// エラーログ出力 処理結果:重複起動エラー
				setLog(ERROR, MessageIdConstant.E_SKF_1080, companyCd, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001);
				setLog(ERROR, MessageIdConstant.E_SKF_1131, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001, batchResult);
			}

		} catch (Exception ex) {
			// 異常の場合
			// エラーログ出力
			batchResult = SkfCommonConstant.ABNORMAL;

			if (!endPrc(String.valueOf(batchResult), FunctionIdConstant.BATCH_CLASS_SKF1010_BT001,
					SkfCommonConstant.PROCESSING)) {
				return dto;
			}
			// 処理結果
			setLog(ERROR, MessageIdConstant.E_SKF_1131, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001, batchResult);
			logList.add(ex.getMessage());

		}
		// エラーが無ければ完了フラグを入れる
		if (!CheckUtils.isEqual(String.valueOf(batchResult), SkfCommonConstant.ABNORMAL)) {
			batchResult = SkfCommonConstant.COMPLETE;
		}

		// 処理結果返却
		endPrc(String.valueOf(batchResult), FunctionIdConstant.BATCH_CLASS_SKF1010_BT001, SkfCommonConstant.PROCESSING);

		// 終了ログ出力
		if (!CheckUtils.isEqual(batchResult, SkfCommonConstant.ABNORMAL)) {
			setLog(INFO, MessageIdConstant.I_SKF_1041, FunctionIdConstant.BATCH_CLASS_SKF1010_BT001, batchResult);
		}
		dto.setStatus(batchResult);
		dto.setMessage(String.join(CodeConstant.DOUBLE_SPACE, logList.toArray(new String[logList.size()])));
		// 処理結果返却
		return dto;
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
		parameter.setProgramId(FunctionIdConstant.BATCH_CLASS_SKF1010_BT001);
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
	 * バッチ制御テーブルにデータを投入する
	 * 
	 * @return
	 */
	private boolean insertBatchControlFunc() {
		Skf1010TBatchControl insertData = new Skf1010TBatchControl();
		Date execDate = new Date();
		insertData.setCompanyCd(companyCd); // 会社コード
		insertData.setStartDate(execDate); // 開始日時
		insertData.setProgramId(FunctionIdConstant.BATCH_CLASS_SKF1010_BT001); // プログラムID
		insertData.setUserId(SkfCommonConstant.FIXED_NAME_BATCH); // ユーザーID
		insertData.setEndFlg(SkfCommonConstant.PROCESSING); // 終了フラグ（0：処理中）
		Skf1010TBatchControlRepository repository = (Skf1010TBatchControlRepository) SpringContext
				.getBean("skf1010TBatchControlRepository");
		int res = repository.insertSelective(insertData);
		if (res > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 組織データ取込処理
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	private boolean updateSoshikiMaster() throws Exception {
		setLog(INFO, MessageIdConstant.I_SKF_1022, BATCH_NAME_MAIN);

		// 更新要/不要チェック
		boolean soshikiMasterChanged = false;
		try {
			soshikiMasterChanged = isSoshikiMasterChanged();

			if (soshikiMasterChanged) {
				// 既存の組織マスタバックアップの保存世代を更新
				updateSoshikiMasterBackUp();

				// 既存の組織マスタを退避
				// ワーク組織テーブルのデータを初期化
				deleteWkSoshiki();
				// 既存の組織テーブルデータをワーク組織テーブルに移行
				insertWkSoshikiMaster();
				// 組織マスタから身上データシステムからの登録データのみ削除
				deleteShinjoDataFromSoshikiMaster();
				// 給与厚生側登録の組織データの内、身上システム登録の組織データとの重複を削除
				deleteShinseiDataFromSoshikiMaster();
				// 組織マスタを登録
				insertShinseiSoshikiMaster();

				// 変更ログ用の組織情報
				List<String> deleteSoshikiList = new ArrayList<String>();
				List<String> updateSoshikiList = new ArrayList<String>();
				List<String> insertSoshikiList = new ArrayList<String>();

				// 以前のマスタから削除された組織をログに出力
				List<SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp> delSoshikiList = new ArrayList<SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp>();
				delSoshikiList = getDeleteSoshikMaster();

				if (delSoshikiList != null) {
					// 削除件数を設定
					deleteCount = delSoshikiList.size();

					for (SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp delSoshiki : delSoshikiList) {
						// 変更内容を設定
						StringBuilder temp = new StringBuilder();
						// 機関名称
						temp.append(delSoshiki.getAgencyName());
						temp.append(CodeConstant.SPACE);
						// 属1名称
						temp.append(delSoshiki.getAffiliation1Name());
						temp.append(CodeConstant.SPACE);
						// 属2名称
						temp.append(delSoshiki.getAffiliation2Name());
						temp.append(CodeConstant.SPACE);

						deleteSoshikiList.add(temp.toString());
					}
				}

				// 以前のマスタから追加された組織をログに出力
				List<SkfSoshikiBatchUtilsGetInsertSoshikMasterExp> insSoshikiList = new ArrayList<SkfSoshikiBatchUtilsGetInsertSoshikMasterExp>();
				insSoshikiList = getInsertSoshikMaster();
				if (insSoshikiList != null) {
					// 登録件数を設定
					insertCount = insSoshikiList.size();
					recordCount = insertCount;

					for (SkfSoshikiBatchUtilsGetInsertSoshikMasterExp insSoshiki : insSoshikiList) {
						// 変更内容を設定
						StringBuilder temp = new StringBuilder();
						// 機関名称
						temp.append(insSoshiki.getAgencyName());
						temp.append(CodeConstant.SPACE);
						// 属1名称
						temp.append(insSoshiki.getAffiliation1Name());
						temp.append(CodeConstant.SPACE);
						// 属2名称
						temp.append(insSoshiki.getAffiliation2Name());
						temp.append(CodeConstant.SPACE);

						insertSoshikiList.add(temp.toString());
					}
				}

				// 住所情報がクリアされた組織をログに出力
				List<SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp> updSoshikiList = new ArrayList<SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp>();
				updSoshikiList = getClearAddressSoshikMasterExp();
				if (updSoshikiList != null) {
					// 変更件数を設定
					updateCount = updSoshikiList.size();
					recordCount += updateCount;

					for (SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp updSoshiki : updSoshikiList) {
						// 変更内容を設定
						StringBuilder temp = new StringBuilder();
						// 機関名称
						temp.append(updSoshiki.getAgencyName());
						temp.append(CodeConstant.SPACE);
						// 属1名称
						temp.append(updSoshiki.getAffiliation1Name());
						temp.append(CodeConstant.SPACE);
						// 属2名称
						temp.append(updSoshiki.getAffiliation2Name());
						temp.append(CodeConstant.SPACE);

						updateSoshikiList.add(temp.toString());
					}
				}
				// ワーク組織テーブルを削除する
				deleteWkSoshiki();

				// 組織マスタバックアップテーブルへ登録
				insertSoshikiMasterBackUp();

				// 組織マスタに何らかの変更があった場合
				if (insertSoshikiList.size() > 0 || deleteSoshikiList.size() > 0 || updateSoshikiList.size() > 0) {
					// システムログを出力する
					for (String deleteSoshiki : deleteSoshikiList) {
						setLog("INFO", MessageIdConstant.I_SKF_3103, deleteSoshiki);
					}
					for (String insertSoshiki : insertSoshikiList) {
						setLog("INFO", MessageIdConstant.I_SKF_3002, insertSoshiki);
					}
					for (String updateSoshiki : updateSoshikiList) {
						setLog("INFO", MessageIdConstant.I_SKF_3098, updateSoshiki);
					}
				}

				// 処理正常であれば、無効社員の検出を行う
				if (!CheckUtils.isEqual(batchResult, SkfCommonConstant.ABNORMAL)) {
					// 無効社員検出処理
					List<SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp> shainInfoList = new ArrayList<SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp>();
					shainInfoList = getShainHasDeletedSoshiki();
					if (shainInfoList != null) {
						for (SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp shainInfo : shainInfoList) {
							String replaceText = I_SKF_1037_INFO_PARAM_2.replace("**", shainInfo.getShainNo());
							setLog("INFO", MessageIdConstant.I_SKF_1037, I_SKF_1037_INFO_PARAM_0,
									I_SKF_1037_INFO_PARAM_1, replaceText);
						}
					}
				}

			}
		} catch (Exception ex) {
			throw ex;
		}
		// 管理ログに【フッタ部】管理情報を出力
		// (メッセージID：I00001、{0}：全件数カウンタ)を取得
		setLog("INFO", MessageIdConstant.I_SKF_1015, recordCount);
		// (メッセージID：I00011、{0}：テーブル名、{1}：削除件数)を取得
		setLog("INFO", MessageIdConstant.I_SKF_1024, TABLE_NAME_SOSHIKI, deleteCount);
		// (メッセージID：I00003、{0}：テーブル名、{1}：登録件数)を取得
		setLog("INFO", MessageIdConstant.I_SKF_1017, TABLE_NAME_SOSHIKI, insertCount);
		// (メッセージID：I00052、{0}：テーブル名、{1}：更新件数)を取得
		setLog("INFO", MessageIdConstant.I_SKF_3104, TABLE_NAME_SOSHIKI, updateCount);

		return true;
	}

	/**
	 * 等級マスタの差し替えを行います
	 * 
	 * @return
	 */
	@Transactional
	private boolean updateTokyu() throws Exception {
		setLog(INFO, MessageIdConstant.I_SKF_1022, BATCH_NAME_TOKYU);

		// 等級マスタを初期化
		SkfSoshikiBatchUtilsDeleteTokyuMasterExp delRecord = new SkfSoshikiBatchUtilsDeleteTokyuMasterExp();
		delRecord.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsDeleteTokyuMasterExpRepository delRepository = (SkfSoshikiBatchUtilsDeleteTokyuMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsDeleteTokyuMasterExpRepository");
		int delRes = delRepository.deleteTokyuMaster(delRecord);
		if (delRes < 0) {
			return false;
		}

		// 等級マスタを登録します
		SkfSoshikiBatchUtilsInsertTokyuMasterExp insRecord = new SkfSoshikiBatchUtilsInsertTokyuMasterExp();
		insRecord.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsInsertTokyuMasterExpRepository insRepository = (SkfSoshikiBatchUtilsInsertTokyuMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsInsertTokyuMasterExpRepository");
		int insRes = insRepository.insertTokyuMaster(insRecord);
		if (insRes < 0) {
			return false;
		}
		// (メッセージID：I00003、{0}：テーブル名、{1}：登録件数)を取得
		setLog("INFO", MessageIdConstant.I_SKF_1017, TABLE_NAME_TOKYU, insRes);

		return true;
	}

	/**
	 * 無効社員一覧取得メソッド
	 * 
	 * @return
	 */
	private List<SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp> getShainHasDeletedSoshiki() {
		List<SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp> resultList = new ArrayList<SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExp>();
		SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpParameter param = new SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpParameter();
		param.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpRepository repository = (SkfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetShainHasDeletedSoshikiExpRepository");
		resultList = repository.getShainHasDeletedSoshiki(param);
		return resultList;
	}

	private int insertSoshikiMasterBackUp() {
		SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExp record = new SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExp();
		record.setCompanyCd(companyCd);
		record.setUserName(SkfCommonConstant.FIXED_NAME_BATCH);
		record.setGenerationNo(DEFAULT_GENERATION_NO);
		SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExpRepository repository = (SkfSoshikiBatchUtilsInsertSoshikiMasterBackUpExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsInsertSoshikiMasterBackUpExpRepository");
		int result = repository.insertSoshikiMasterBackUp(record);
		return result;
	}

	/**
	 * ワーク組織マスタ削除メソッド
	 * 
	 * @return
	 */
	private int deleteWkSoshiki() {
		SkfSoshikiBatchUtilsDeleteWkSoshikiExp record = new SkfSoshikiBatchUtilsDeleteWkSoshikiExp();
		record.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsDeleteWkSoshikiExpRepository repository = (SkfSoshikiBatchUtilsDeleteWkSoshikiExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsDeleteWkSoshikiExpRepository");
		int result = repository.deleteWkSoshiki(record);
		return result;
	}

	/**
	 * 住所情報クリア組織情報取得メソッド
	 * 
	 * @return
	 */
	private List<SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp> getClearAddressSoshikMasterExp() {
		List<SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp> resultList = new ArrayList<SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExp>();
		SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpParameter param = new SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpParameter();
		param.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpRepository repository = (SkfSoshikiBatchUtilsGetClearAddressSoshikMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetClearAddressSoshikMasterExpRepository");
		resultList = repository.getClearAddressSoshikMaster(param);

		return resultList;
	}

	/**
	 * 追加組織情報取得メソッド
	 * 
	 * @return
	 */
	private List<SkfSoshikiBatchUtilsGetInsertSoshikMasterExp> getInsertSoshikMaster() {
		List<SkfSoshikiBatchUtilsGetInsertSoshikMasterExp> resultList = new ArrayList<SkfSoshikiBatchUtilsGetInsertSoshikMasterExp>();
		SkfSoshikiBatchUtilsGetInsertSoshikMasterExpParameter param = new SkfSoshikiBatchUtilsGetInsertSoshikMasterExpParameter();
		param.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsGetInsertSoshikMasterExpRepository repository = (SkfSoshikiBatchUtilsGetInsertSoshikMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetInsertSoshikMasterExpRepository");
		resultList = repository.getInsertSoshikMaster(param);

		return resultList;
	}

	/**
	 * 削除組織情報取得メソッド
	 * 
	 * @return
	 */
	private List<SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp> getDeleteSoshikMaster() {
		List<SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp> resultList = new ArrayList<SkfSoshikiBatchUtilsGetDeleteSoshikMasterExp>();
		SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpParameter param = new SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpParameter();
		param.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpRepository repository = (SkfSoshikiBatchUtilsGetDeleteSoshikMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetDeleteSoshikMasterExpRepository");
		resultList = repository.getDeleteSoshikMaster(param);

		return resultList;
	}

	/**
	 * 組織マスタを登録します
	 * 
	 * @return
	 */
	private int insertShinseiSoshikiMaster() {
		SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExp record = new SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExp();
		record.setCompanyCd(companyCd);
		record.setAgencyCd(SHINJO_AGENCY_CD_YAKUIN);
		record.setUserName(SkfCommonConstant.FIXED_NAME_BATCH);
		SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExpRepository repository = (SkfSoshikiBatchUtilsInsertShinseiSoshikiMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsInsertShinseiSoshikiMasterExpRepository");
		int result = repository.insertShinseiSoshikiMaster(record);
		return result;
	}

	/**
	 * 組織マスタから身上データシステムからの登録データのみ削除します
	 * 
	 * @return
	 */
	private int deleteShinjoDataFromSoshikiMaster() {
		SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExp record = new SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExp();
		record.setCompanyCd(companyCd);
		record.setRegistFlg(SkfCommonConstant.REGIST_SHINJO);
		SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExpRepository repository = (SkfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsDeleteShinjoDataFromSoshikiMasterExpRepository");
		int result = repository.deleteShinjoDataFromSoshikiMaster(record);
		return result;
	}

	/**
	 * 給与厚生側登録の組織データの内、身上システム登録の組織データとの重複を削除する
	 * 
	 * @return
	 */
	private int deleteShinseiDataFromSoshikiMaster() {
		SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExp record = new SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExp();
		record.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExpRepository repository = (SkfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsDeleteShinseiDataFromSoshikiMasterExpRepository");
		int result = repository.deleteShinseiDataFromSoshikiMaster(record);
		return result;
	}

	/**
	 * 既存の組織テーブルデータをワーク組織テーブルに移行します
	 * 
	 * @return
	 */
	private int insertWkSoshikiMaster() {
		SkfSoshikiBatchUtilsInsertWkSoshikiMasterExp insRecord = new SkfSoshikiBatchUtilsInsertWkSoshikiMasterExp();
		insRecord.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsInsertWkSoshikiMasterExpRepository repository = (SkfSoshikiBatchUtilsInsertWkSoshikiMasterExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsInsertWkSoshikiMasterExpRepository");
		int result = repository.insertWkSoshikiMaster(insRecord);

		return result;
	}

	/**
	 * 既存の組織マスタバックアップの保存世代を更新
	 */
	private void updateSoshikiMasterBackUp() {
		// 指定された保存世代以上のデータを削除
		SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExp delRecord = new SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExp();
		delRecord.setCompanyCd(companyCd);
		delRecord.setGenerationNo(SAVE_GENERATION);
		SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExpRepository delRepository = (SkfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsDeleteSoshikiMasterBackUpExpRepository");
		delRepository.deleteSoshikiMasterBackUp(delRecord);

		// 組織バックアップデータ取得
		List<SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExp> soshikiBkList = new ArrayList<SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExp>();
		SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpParameter bkParam = new SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpParameter();
		bkParam.setCompanyCd(companyCd);
		SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpRepository getRepository = (SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetSoshikiMasterBackUpExpRepository");
		soshikiBkList = getRepository.getSoshikiMasterBackUp(bkParam);
		if (soshikiBkList != null) {
			for (SkfSoshikiBatchUtilsGetSoshikiMasterBackUpExp soshikiBkInfo : soshikiBkList) {
				// 保存世代を＋1して更新
				int generationNo = 0;
				if (NfwStringUtils.isNotEmpty(soshikiBkInfo.getGenerationNo())) {
					generationNo = Integer.parseInt(soshikiBkInfo.getGenerationNo()) + 1;
				}

				SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExp updRecord = new SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExp();
				updRecord.setCompanyCd(companyCd);
				updRecord.setAgencyCd(soshikiBkInfo.getAgencyCd());
				updRecord.setAffiliation1Cd(soshikiBkInfo.getAffiliation1Cd());
				updRecord.setAffiliation2Cd(soshikiBkInfo.getAffiliation2Cd());
				updRecord.setBeforeGenerationNo(soshikiBkInfo.getGenerationNo());
				// ＋１の世代コードを投入
				updRecord.setGenerationNo(String.valueOf(generationNo));
				SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExpRepository updRepository = (SkfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExpRepository) SpringContext
						.getBean("skfSoshikiBatchUtilsUpdateSoshikiMasterBackUpExpRepository");
				updRepository.updateSoshikiMasterBackUp(updRecord);
			}
		}

		return;
	}

	/**
	 * 組織マスタ更新要/不要チェックメソッド
	 * 
	 * @return
	 */
	private boolean isSoshikiMasterChanged() {
		boolean result = false;
		SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExp data = new SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExp();
		SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpParameter param = new SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpParameter();
		param.setCompanyCd(companyCd);
		param.setAgencyCd(SHINJO_AGENCY_CD_YAKUIN);
		param.setRegistFlg(SkfCommonConstant.REGIST_SHINJO);
		SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpRepository repository = (SkfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpRepository) SpringContext
				.getBean("skfSoshikiBatchUtilsGetSoshikiMasterChangedCountExpRepository");
		data = repository.getSoshikiMasterChangedCount(param);
		if (data != null) {
			// 1件以上の差異があれば、更新が必要と判断
			if (data.getCnt() > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * 終了処理を行います
	 * 
	 * @param endFlg
	 * @param programId
	 * @param searchEndFlg
	 * @return
	 */
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

	/**
	 * ログ情報をセットします
	 * 
	 * @param logId
	 * @param messageId
	 * @param params
	 */
	private void setLog(String logId, String messageId, Object... params) {
		switch (logId) {
		case INFO:
			LogUtils.info(messageId, params);
			break;
		case ERROR:
			LogUtils.error(messageId, params);
		}

		logList.add(getMsg(messageId, params));
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
