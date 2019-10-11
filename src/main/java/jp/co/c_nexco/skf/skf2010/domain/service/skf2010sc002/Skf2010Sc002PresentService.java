/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetTeijiShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc002.Skf2010Sc002GetTeijiShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc002.Skf2010Sc002GetTeijiShatakuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc002.Skf2010Sc002PresentDto;

/**
 * Skf2010Sc002PresentService 申請書類確認の提示ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc002PresentService extends BaseServiceAbstract<Skf2010Sc002PresentDto> {

	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2010Sc002SharedService skf2010Sc002SharedService;
	@Autowired
	private Skf2010Sc002GetTeijiShatakuInfoExpRepository skf2010Sc002GetTeijiShatakuInfoExpRepository;

	@Override
	protected BaseDto index(Skf2010Sc002PresentDto preDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("提示", CodeConstant.C001, preDto.getPageId());

		// 申請書類IDの有無チェック
		if (preDto.getApplId() == null) {
			ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1078, "");
			return preDto;
		}
		// ステータスの有無チェック
		if (preDto.getApplStatus() == null) {
			ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1078, "");
			return preDto;
		}

		// コメント欄の入力チェック
		if (!(skf2010Sc002SharedService.validateComment(preDto.getCommentNote()))) {
			// エラーがある場合はメッセージを出力して処理を中断
			ServiceHelper.addErrorResultMessage(preDto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1071,
					"申請者へのコメント", "2000");
			return preDto;
		}
		String comment = preDto.getCommentNote();
		if (NfwStringUtils.isEmpty(comment)) {
			comment = CodeConstant.NONE;
		}

		// 次のステータスを設定
		String status = CodeConstant.STATUS_KAKUNIN_IRAI;

		// 「申請書類履歴テーブル」よりステータスを更新 + 申請者へのコメントを更新
		Map<String, String> applMap = new HashMap<String, String>();
		applMap.put("shainNo", preDto.getShainNo());
		applMap.put("applNo", preDto.getApplNo());
		applMap.put("name", preDto.getName());
		applMap.put("status", status);
		applMap.put("commentNote", preDto.getCommentNote());
		String res = skf2010Sc002SharedService.updateShinseiHistory(applMap,
				preDto.getLastUpdateDate(Skf2010Sc002SharedService.KEY_LAST_UPDATE_DATE_HISTORY));
		if ("updateError".equals(res)) {
			// 更新エラー
			ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1075);
			return preDto;
		} else if ("exclusiveError".equals(res)) {
			// 排他チェックエラー
			ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_1134, "skf2010_t_appl_history");
			return preDto;
		}

		// 社宅提示案内情報の取得
		// 提示社宅データの取得
		List<Skf2010Sc002GetTeijiShatakuInfoExp> tSkSTeijiShatakuInfo = new ArrayList<Skf2010Sc002GetTeijiShatakuInfoExp>();
		Skf2010Sc002GetTeijiShatakuInfoExpParameter param = new Skf2010Sc002GetTeijiShatakuInfoExpParameter();
		param.setApplNo(preDto.getApplNo());
		param.setShainNo(preDto.getShainNo());
		param.setNyutaikyoKbn(CodeConstant.NYUTAIKYO_KBN_NYUKYO);
		tSkSTeijiShatakuInfo = skf2010Sc002GetTeijiShatakuInfoExpRepository.getTeijiShatakuInfo(param);

		// 提示社宅データが取得できた場合
		if (tSkSTeijiShatakuInfo != null && tSkSTeijiShatakuInfo.size() > 0) {
			// 案内に社宅の管理者情報を追加
			String annai = CodeConstant.DOUBLE_QUOTATION;

			// メールの案内内容の作成
			if (CodeConstant.KARIAGE.equals(tSkSTeijiShatakuInfo.get(0).getShatakuKbn())) {
				// 借上社宅の場合
				annai = getAnnaiShatakuManage(CodeConstant.KARIAGE, tSkSTeijiShatakuInfo, annai);
			} else {
				// 借上社宅以外の場合
				annai = getAnnaiShatakuManage(CodeConstant.HOYU, tSkSTeijiShatakuInfo, annai);
			}

			// 同意確認通知のメールを送信する
			Map<String, String> applInfoAnnai = new HashMap<String, String>();
			applInfoAnnai.put("applNo", preDto.getApplNo());
			applInfoAnnai.put("applId", FunctionIdConstant.R0100);
			applInfoAnnai.put("applShainNo", preDto.getShainNo());

			String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";

			skfMailUtils.sendApplTsuchiMail(CodeConstant.TEJI_TSUCHI, applInfoAnnai, comment, annai,
					preDto.getShainNo(), CodeConstant.NONE, urlBase);

			// TODO 社宅管理データ連携処理実行

			// 画面遷移（承認一覧へ）
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC005, "init");
			nextPage.addResultMessage(MessageIdConstant.I_SKF_2047);
			preDto.setTransferPageInfo(nextPage);

		} else {
			ServiceHelper.addErrorResultMessage(preDto, null, MessageIdConstant.E_SKF_2011);
			throwBusinessExceptionIfErrors(preDto.getResultMessages());
		}

		return preDto;
	}

	/**
	 * 貸与（予定）社宅等のご案内のご案内内容作成
	 * 
	 * @param shatakuKbn
	 * @param tSkSTeijiShatakuInfo
	 * @param annai
	 * @return
	 */
	private String getAnnaiShatakuManage(String shatakuKbn,
			List<Skf2010Sc002GetTeijiShatakuInfoExp> tSkSTeijiShatakuInfo, String annai) {

		// 本文変数
		List<String> annaiList = new ArrayList<String>();
		String manageShatakuNo1 = CodeConstant.DOUBLE_QUOTATION;
		String manageName1 = CodeConstant.DOUBLE_QUOTATION;
		String manageMailAddress1 = CodeConstant.DOUBLE_QUOTATION;
		String manageTelNo1 = CodeConstant.DOUBLE_QUOTATION;
		String manageExtensionNo1 = CodeConstant.DOUBLE_QUOTATION;

		String manageShatakuNo2 = CodeConstant.DOUBLE_QUOTATION;
		String manageName2 = CodeConstant.DOUBLE_QUOTATION;
		String manageMailAddress2 = CodeConstant.DOUBLE_QUOTATION;
		String manageTelNo2 = CodeConstant.DOUBLE_QUOTATION;
		String manageExtensionNo2 = CodeConstant.DOUBLE_QUOTATION;

		String manageShatakuNo3 = CodeConstant.DOUBLE_QUOTATION;
		String manageName3 = CodeConstant.DOUBLE_QUOTATION;
		String manageMailAddress3 = CodeConstant.DOUBLE_QUOTATION;
		String manageTelNo3 = CodeConstant.DOUBLE_QUOTATION;
		String manageExtensionNo3 = CodeConstant.DOUBLE_QUOTATION;

		// 借上社宅の場合
		if (CodeConstant.KARIAGE.equals(shatakuKbn)) {
			// 管理会社
			// 会社名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo3())) {
				manageShatakuNo1 = tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo3();
			}
			annaiList.add(manageShatakuNo1);

			// 担当者名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeName3())) {
				manageName1 = tSkSTeijiShatakuInfo.get(0).getManegeName3();
			}
			annaiList.add(manageName1);

			// 電子メールアドレス
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeMailAddress3())) {
				manageMailAddress1 = tSkSTeijiShatakuInfo.get(0).getManegeMailAddress3();
			}
			annaiList.add(manageMailAddress1);

			// 電話番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeTelNo3())) {
				manageTelNo1 = tSkSTeijiShatakuInfo.get(0).getManegeTelNo3();
			}
			annaiList.add(manageTelNo1);

			// 鍵管理者
			// 会社名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo2())) {
				manageShatakuNo2 = tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo2();
			}
			annaiList.add(manageShatakuNo2);

			// 担当者名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeName2())) {
				manageName2 = tSkSTeijiShatakuInfo.get(0).getManegeName2();
			}
			annaiList.add(manageName2);

			// 電子メールアドレス
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeMailAddress2())) {
				manageMailAddress2 = tSkSTeijiShatakuInfo.get(0).getManegeMailAddress2();
			}
			annaiList.add(manageMailAddress2);

			// 電話番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeTelNo2())) {
				manageTelNo2 = tSkSTeijiShatakuInfo.get(0).getManegeTelNo2();
			}
			annaiList.add(manageTelNo2);

			annai = getMessage("skf2010.skf2010_sc002.mail_manage_kariage", annaiList);

		} else {
			// 借上社宅以外の場合
			// 寮長・自治会長
			// 部屋番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo1())) {
				manageShatakuNo1 = tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo1();
			}
			annaiList.add(manageShatakuNo1);

			// 氏名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeName1())) {
				manageName1 = tSkSTeijiShatakuInfo.get(0).getManegeName1();
			}
			annaiList.add(manageName1);

			// 電子メールアドレス
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeMailAddress1())) {
				manageMailAddress1 = tSkSTeijiShatakuInfo.get(0).getManegeMailAddress1();
			}
			annaiList.add(manageMailAddress1);

			// 電話番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeTelNo1())) {
				manageTelNo1 = tSkSTeijiShatakuInfo.get(0).getManegeTelNo1();
			}
			annaiList.add(manageTelNo1);

			// 内線番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo1())) {
				manageExtensionNo1 = tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo1();
			}
			annaiList.add(manageExtensionNo1);

			// 鍵管理者
			// 部屋番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo2())) {
				manageShatakuNo2 = tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo2();
			}
			annaiList.add(manageShatakuNo2);

			// 氏名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeName2())) {
				manageName2 = tSkSTeijiShatakuInfo.get(0).getManegeName2();
			}
			annaiList.add(manageName2);

			// 電子メールアドレス
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeMailAddress2())) {
				manageMailAddress2 = tSkSTeijiShatakuInfo.get(0).getManegeMailAddress2();
			}
			annaiList.add(manageMailAddress2);

			// 電話番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeTelNo2())) {
				manageTelNo2 = tSkSTeijiShatakuInfo.get(0).getManegeTelNo2();
			}
			annaiList.add(manageTelNo2);

			// 内線番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo2())) {
				manageExtensionNo2 = tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo2();
			}
			annaiList.add(manageExtensionNo2);

			// 寮母・管理会社
			// 部屋番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo3())) {
				manageShatakuNo3 = tSkSTeijiShatakuInfo.get(0).getManegeShatakuNo3();
			}
			annaiList.add(manageShatakuNo3);

			// 氏名
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeName3())) {
				manageName3 = tSkSTeijiShatakuInfo.get(0).getManegeName3();
			}
			annaiList.add(manageName3);

			// 電子メールアドレス
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeMailAddress3())) {
				manageMailAddress3 = tSkSTeijiShatakuInfo.get(0).getManegeMailAddress3();
			}
			annaiList.add(manageMailAddress3);

			// 電話番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeTelNo3())) {
				manageTelNo3 = tSkSTeijiShatakuInfo.get(0).getManegeTelNo3();
			}
			annaiList.add(manageTelNo3);

			// 内線番号
			if (NfwStringUtils.isNotEmpty(tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo3())) {
				manageExtensionNo3 = tSkSTeijiShatakuInfo.get(0).getManegeExtensionNo3();
			}
			annaiList.add(manageExtensionNo3);

			annai = getMessage("skf2010.skf2010_sc002.mail_manage_hoyu", annaiList);

		}

		return annai;
	}

	/**
	 * 引数.IDに紐づくメッセージを返却します。<br>
	 * （可変項目は引数.置換文字列に置き換えられます）
	 *
	 * @param id メッセージID
	 * @param param 置換文字列
	 * @return メッセージ
	 */
	public static String getMessage(String id, List<String> param) {
		// メッセージ取得
		String msg = PropertyUtils.getValue(id);

		// 可変項目の置換え6666
		for (int idx = 0; idx < param.size(); idx++) {

			String sParam = param.get(idx);
			String rep = "{" + idx + "}";
			msg = msg.replace(rep, sParam);
		}

		return msg;
	}

}
