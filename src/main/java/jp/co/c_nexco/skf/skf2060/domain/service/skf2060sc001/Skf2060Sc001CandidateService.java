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
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplComment;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukken;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2060TKariageBukkenKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc001.Skf2060Sc001GetMaxTeijiKaisuExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplCommentRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2060TKariageBukkenRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001CandidateDto;
import jp.co.c_nexco.skf.skf2060.domain.service.common.Skf206010CommonSendMailService;

/**
 * TestPrjTop画面のCandidateDtoサービス処理クラス。
 * 
 */
@Service
public class Skf2060Sc001CandidateService extends SkfServiceAbstract<Skf2060Sc001CandidateDto> {

	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2060Sc001GetMaxTeijiKaisuExpRepository skf2060Sc001GetMaxTeijiKaisuExpRepository;
	@Autowired
	private Skf2010TApplCommentRepository skf2010TApplCommentRepository;
	@Autowired
	private Skf2060TKariageBukkenRepository skf2060TKariageBukkenRepository;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf206010CommonSendMailService skf206010CommonSendMailService;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	private String companyCd = CodeConstant.C001;

	/**
	 * サービス処理を行う。
	 * 
	 * @param candidateDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001CandidateDto index(Skf2060Sc001CandidateDto candidateDto) throws Exception {

		candidateDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("提示", companyCd, candidateDto.getPageId());

		// 提示対象者社員番号
		String shainNo = candidateDto.getShainNo();
		// 提示対象者名
		String shainName = candidateDto.getPresentedName();

		// 申請書類番号とステータス
		String applNo = candidateDto.getApplNo();
		String applStatus = candidateDto.getPresentedStatus();
		// 提示状況汎用コード取得
		Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
		candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		// ログインセッションのユーザ情報
		Map<String, String> userInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		// ログインセッションユーザ情報のユーザ名
		String userName = userInfoMap.get("userName");
		// ユーザCD
		String updateUserId = userInfoMap.get("userCd");
		// ページID
		String updateProgramId = candidateDto.getPageId();
		// 選択物件番号
		long checkCandidateNo = 0;
		// 一覧フラグ
		boolean itiranFlg = true;
		// 更新日時
		String updateDate = candidateDto.getUpdateDate();
		updateDate = skfDateFormatUtils.dateFormatFromString(updateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		// 提示回数
		short teijiKaisu = 1;
		// 新規作成フラグ
		boolean newCreateFlg = false;
		boolean presentedNameflg = true;
		boolean tijiValFlg = true;

		// 申請書類ID(R0106:確認依頼)
		String applId = "R0106";
		// 添付書類有無
		String applTacFlg = "0";
		// 連携済フラグ
		String comboFlg = "0";

		// システム日付
		Date candidateDate = DateUtils.getSysDate();

		// 入力チェック
		// 提示対象者名が未選択の時
		if (candidateDto.getPresentedName() == null || CheckUtils.isEmpty(candidateDto.getPresentedName().trim())) {
			presentedNameflg = false;
			ServiceHelper.addErrorResultMessage(candidateDto, new String[] { "presentedName" },
					MessageIdConstant.E_SKF_1054, "提示対象者");
			// 提示チェックボックスが未選択の時
		} else if (candidateDto.getTeijiVal() == null) {
			tijiValFlg = false;
			ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1054, "提示対象物件");
		} else if (candidateDto.getTeijiVal() != null) {
			if (candidateDto.getTeijiVal().length == 0) {
				tijiValFlg = false;
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1054, "提示対象物件");
			}
		}
		// 入力チェックでエラーが生じたとき
		if (!(presentedNameflg && tijiValFlg)) {
			// リストデータ再取得
			List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
			dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg ,candidateDto.getShainNo(), candidateDto.getApplNo());
			candidateDto.setListTableData(dataParamList);
			
			return candidateDto;
		}

		// 申請書類履歴情報が取得できた場合
		if (!(applNo == null || CheckUtils.isEmpty(applNo.trim()))
				&& !(applStatus == null || CheckUtils.isEmpty(applStatus.trim()))) {
			// 提示ステータスが20のとき（確認依頼）
			if (applStatus.equals(candidateStatusGenCodeMap.get(CodeConstant.STATUS_KAKUNIN_IRAI))) {
				// エラーメッセージを設定
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_2016);
				throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				// 提示ステータスが41のとき（完了）
			} else if (applStatus.equals(candidateStatusGenCodeMap.get(CodeConstant.STATUS_KANRYOU))) {
				// 申請書類管理番号を作成する
				applNo = skf2060Sc001SharedService.getApplNo(companyCd, shainNo, updateDate, applId);
				// 申請書類管理番号を作成に失敗した場合
				if (applNo == null || CheckUtils.isEmpty(applNo.trim())) {
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1024);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
				// 提示回数に"1"を設定する
				teijiKaisu = 1;
				// 新規作成フラグをTrueに設定する
				newCreateFlg = true;
				// 提示ステータスが20,41以外のとき
			} else {
				// 申請書類管理番号をもとに「提示回数」を取得する
				teijiKaisu = this.getMaxTeijiKaisu(companyCd, applNo);
			}

			// 取得できなかった場合（新規掲示）
		} else {
			// 申請書類管理番号を作成する
			applNo = skf2060Sc001SharedService.getApplNo(companyCd, shainNo, updateDate, applId);
			// 申請書類管理番号を作成に失敗した場合
			if (applNo == null || CheckUtils.isEmpty(applNo.trim())) {
				// エラーメッセージを設定
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1024);
				throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
			}
			// 提示回数に"1"を設定する
			teijiKaisu = 1;
			// 新規作成フラグをTrueに設定する
			newCreateFlg = true;
		}

		applStatus = CodeConstant.STATUS_KAKUNIN_IRAI;

		// 申請書類履歴更新処理
		// 新規作成フラグがTrueの場合
		if (newCreateFlg) {
			// 申請書類履歴テーブルに登録
			boolean insertCheck = skf2060Sc001SharedService.insertApplHistory(companyCd, shainNo, applNo, applId,
					applStatus, applTacFlg, userName, comboFlg);

			// 申請書類登録に失敗
			if (!(insertCheck)) {
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
			}

			// (CreateKariageBukkenTeiji)
			// 借上候補物件提示データの作成を行う
			// 借上候補物件提示テーブルへ情報を登録する
			boolean kariageTeijiCheck = skf2060Sc001SharedService.insertKatiageTeiji(companyCd, applNo, teijiKaisu,
					checkCandidateNo, candidateDate);
			// 登録に失敗した場合
			if (!(kariageTeijiCheck)) {
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
			}

			// チェックが入っているデータを取得
			List<Map<String, Object>> dataParamList = candidateDto.getListTableData();
			List<Map<String, Object>> checkDataParamList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < candidateDto.getTeijiVal().length; i++) {
				int checkDataNum = Integer.parseInt(candidateDto.getTeijiVal()[i]);
				checkDataParamList.add(dataParamList.get(checkDataNum));
				long candidateNo = (long) dataParamList.get(checkDataNum).get("candidateNo");

				LogUtils.debugByMsg("チェックされた物件の借上候補物件番号：" + candidateNo);

				// データ削除排他チェック
				Skf2060TKariageBukken data = new Skf2060TKariageBukken();
				Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
				key.setCompanyCd(companyCd);
				key.setCandidateNo(candidateNo);
				data = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);
				if (data == null) {
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1134, "");
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
			}

			// 借上候補物件提示明細テーブルに情報を登録
			for (int j = 0; j < checkDataParamList.size(); j++) {
				boolean kariageTeijiDetailCheck = skf2060Sc001SharedService.insertKatiageTeijiDetail(companyCd, applNo,
						teijiKaisu, (long) checkDataParamList.get(j).get("candidateNo"),
						(String) checkDataParamList.get(j).get("shatakuName"),
						(String) checkDataParamList.get(j).get("address"),
						(String) checkDataParamList.get(j).get("money"), "0", shainNo);
				// 登録に失敗した場合
				if (!(kariageTeijiDetailCheck)) {
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
			}

			// 添付ファイル管理テーブル(提示物件)に情報を登録
			for (Map<String, Object> dataMap : checkDataParamList) {
				if (dataMap.get("candidateNo") == null) {
					continue;
				}
				long candidateNo = Long.parseLong(dataMap.get("candidateNo").toString());

				// 添付ファイルが存在する場合
				boolean kariageTeijiFileCheck = skf2060Sc001SharedService.insertKatiageTeijiFile(companyCd, applNo,
						teijiKaisu, candidateNo);
				// 登録に失敗した場合
				if (!(kariageTeijiFileCheck)) {
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
			}

			// 借上候補物件番号をもとに借上候補物件テーブルの提示可能フラグを"1"（提示不可）に更新する

			for (int j = 0; j < checkDataParamList.size(); j++) {

				// 楽観的排他チェック用データ取得
				Skf2060TKariageBukken data = new Skf2060TKariageBukken();
				Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
				key.setCompanyCd(companyCd);
				key.setCandidateNo((long) checkDataParamList.get(j).get("candidateNo"));
				data = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);
				String lastUpdateDateString = skfDateFormatUtils.dateFormatFromDate(data.getUpdateDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SSS);
				SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SSS);
				Date lastUpdateDate = sdf.parse(lastUpdateDateString);
				// 楽観的排他チェック
				super.checkLockException(candidateDto.getLastUpdateDate(candidateDto.KariageBukkenLastUpdateDate
						+ checkDataParamList.get(j).get("candidateNo").toString()), lastUpdateDate);
				boolean updateKariageKohoCheck = skf2060Sc001SharedService.updateKariageKoho(companyCd,
						(long) checkDataParamList.get(j).get("candidateNo"));
				// 登録に失敗した場合
				if (!(updateKariageKohoCheck)) {
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
			}

			//// (CreateKariageBukkenTeiji)おわり

			// 新規作成フラグがFalseの時
		} else {
			// 楽観的排他チェック用データ取得
			Skf2060Sc001GetApplHistoryInfoForUpdateExp existCheckData = skf2060Sc001SharedService
					.getApplHistoryInfoForUpdate(companyCd, applNo);
			// 該当する「申請書類履歴テーブル」のデータが取得できた場合
			if (existCheckData != null) {
				// 楽観的排他チェック
				super.checkLockException(candidateDto.getLastUpdateDate(candidateDto.applHistoryLastUpdateDate),
						existCheckData.getUpdateDate());
				// 申請書類履歴テーブルよりステータスを更新
				boolean updateApplHistoryCheck = skf2060Sc001SharedService.updateApplHistory(companyCd, shainNo,
						existCheckData.getApplDate(), applNo, existCheckData.getApplId(), updateUserId,
						updateProgramId);
				// 更新に失敗した場合
				if (!(updateApplHistoryCheck)) {
					// エラーメッセージの設定
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}

				// 「申請書類管理番号」と「提示回数」をもとに借上候補物件提示明細テーブルに存在する全ての借上候補物件データの「提示フラグ」を"0"(提示可)に更新する。
				Map<String, Date> lastUpdateDateMap = candidateDto.getLastUpdateDateMap();
				Map<String, String> errorMsg = new HashMap<String, String>();
				boolean updateKariageBukkenTeijiFlgCheck = skf2060Sc001SharedService
						.updateKariageBukkenTeijiFlg(companyCd, applNo, teijiKaisu, lastUpdateDateMap, errorMsg);
				// 更新に失敗した場合
				if (!(updateKariageBukkenTeijiFlgCheck)) {
					// エラーメッセージの設定
					if (errorMsg.get("error") != null) {
						ServiceHelper.addErrorResultMessage(candidateDto, null, errorMsg.get("error"));
					} else {
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					}
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}
				// 提示回数を＋１する
				teijiKaisu++;

				// (CreateKariageBukkenTeiji)
				// 借上候補物件提示データの作成を行う
				// 借上候補物件提示テーブルへ情報を登録する
				boolean kariageTeijiCheck = skf2060Sc001SharedService.insertKatiageTeiji(companyCd, applNo, teijiKaisu,
						checkCandidateNo, candidateDate);
				// 登録に失敗した場合
				if (!(kariageTeijiCheck)) {
					ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
					throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
				}

				// チェックが入っているデータを取得
				List<Map<String, Object>> dataParamList = candidateDto.getListTableData();
				List<Map<String, Object>> checkDataParamList = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < candidateDto.getTeijiVal().length; i++) {
					int checkDataNum = Integer.parseInt(candidateDto.getTeijiVal()[i]);
					checkDataParamList.add(dataParamList.get(checkDataNum));
					long candidateNo = (long) dataParamList.get(checkDataNum).get("candidateNo");

					LogUtils.debugByMsg("チェックされた物件の借上候補物件番号：" + candidateNo);

					// データ削除排他チェック
					Skf2060TKariageBukken data = new Skf2060TKariageBukken();
					Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
					key.setCompanyCd(companyCd);
					key.setCandidateNo(candidateNo);
					data = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);
					if (data == null) {
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1134, "");
						throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
					}
				}

				// 借上候補物件提示明細テーブルに情報を登録
				for (int j = 0; j < checkDataParamList.size(); j++) {
					boolean kariageTeijiDetailCheck = skf2060Sc001SharedService.insertKatiageTeijiDetail(companyCd,
							applNo, teijiKaisu, (long) checkDataParamList.get(j).get("candidateNo"),
							(String) checkDataParamList.get(j).get("shatakuName"),
							(String) checkDataParamList.get(j).get("address"),
							(String) checkDataParamList.get(j).get("money"), "0", shainNo);
					// 登録に失敗した場合
					if (!(kariageTeijiDetailCheck)) {
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
					}
				}

				// 添付ファイル管理テーブル(提示物件)に情報を登録
				for (Map<String, Object> dataMap : checkDataParamList) {
					if (dataMap.get("candidateNo") == null) {
						continue;
					}
					long candidateNo = Long.parseLong(dataMap.get("candidateNo").toString());
					// 添付ファイルが存在する場合
					boolean kariageTeijiFileCheck = skf2060Sc001SharedService.insertKatiageTeijiFile(companyCd, applNo,
							teijiKaisu, candidateNo);
					// 登録に失敗した場合
					if (!(kariageTeijiFileCheck)) {
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
					}

				}

				// 借上候補物件番号をもとに借上候補物件テーブルの提示可能フラグを"1"（提示不可）に更新する

				for (int j = 0; j < checkDataParamList.size(); j++) {

					// 楽観的排他チェック用データ取得
					Skf2060TKariageBukken data = new Skf2060TKariageBukken();
					Skf2060TKariageBukkenKey key = new Skf2060TKariageBukkenKey();
					key.setCompanyCd(companyCd);
					key.setCandidateNo((long) checkDataParamList.get(j).get("candidateNo"));
					data = skf2060TKariageBukkenRepository.selectByPrimaryKey(key);
					// 楽観的排他チェック
					String lastUpdateDateKey = candidateDto.KariageBukkenLastUpdateDate
							+ checkDataParamList.get(j).get("candidateNo");
					String lastUpdateDateString = skfDateFormatUtils.dateFormatFromDate(data.getUpdateDate(),
							SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SSS);
					SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SSS);
					Date lastUpdateDate = sdf.parse(lastUpdateDateString);
					super.checkLockException(lastUpdateDateMap.get(lastUpdateDateKey), lastUpdateDate);
					boolean updateKariageKohoCheck = skf2060Sc001SharedService.updateKariageKoho(companyCd,
							(long) checkDataParamList.get(j).get("candidateNo"));
					// 登録に失敗した場合
					if (!(updateKariageKohoCheck)) {
						ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
						throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
					}
				}

				//// (CreateKariageBukkenTeiji)おわり

			}
		}

		// コメントが記載されている場合
		if (!(candidateDto.getComment() == null || CheckUtils.isEmpty(candidateDto.getComment().trim()))) {
			// 申請書類コメントテーブルへコメントを追加
			Map<String, String> errorMsg = new HashMap<String, String>();
			boolean insertCommentCheck = skfCommentUtils.insertComment(companyCd, applNo, applStatus, candidateDto.getComment(),
					errorMsg);
			// コメント追加に失敗した場合
			if (!(insertCommentCheck)) {
				ServiceHelper.addErrorResultMessage(candidateDto, null, MessageIdConstant.E_SKF_1073);
				throwBusinessExceptionIfErrors(candidateDto.getResultMessages());
			}
		}

		// メールの送信処理
		skf206010CommonSendMailService.sendKariageTeijiMail(applNo);

		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg, shainNo, applNo);
		candidateDto.setListTableData(dataParamList);

		// 「借上候補物件状況一覧」に画面遷移する
		TransferPageInfo prevPage = TransferPageInfo.nextPage("Skf2060Sc004");
		candidateDto.setTransferPageInfo(prevPage);

		return candidateDto;
	}

	/**
	 * 申請書類番号をもとに提示回数を取得する
	 * 
	 * @param companyCd
	 * @param applNo
	 * @return 取得した提示回数
	 */
	public short getMaxTeijiKaisu(String companyCd, String applNo) {
		short TeijiKaisu = 0;

		Skf2060Sc001GetMaxTeijiKaisuExp resultData = new Skf2060Sc001GetMaxTeijiKaisuExp();
		Skf2060Sc001GetMaxTeijiKaisuExpParameter param = new Skf2060Sc001GetMaxTeijiKaisuExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);

		resultData = skf2060Sc001GetMaxTeijiKaisuExpRepository.getMaxTeijiKaisu(param);
		if (resultData != null) {
			TeijiKaisu = resultData.getTeijiKaisu();
		}
		return TeijiKaisu;
	}
}
