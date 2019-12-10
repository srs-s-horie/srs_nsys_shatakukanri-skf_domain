/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc006.Skf3022Sc006InitDto;

/**
 * Skf3022Sc006InitService 提示データ登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3022Sc006InitService extends BaseServiceAbstract<Skf3022Sc006InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3022Sc006SharedService skf3022Sc006SharedService;

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
	public Skf3022Sc006InitDto index(Skf3022Sc006InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// 取得提示データ
		Skf3022Sc006GetTeijiDataExp getTeijiData = new Skf3022Sc006GetTeijiDataExp();

		// パラメータ取得
		String hdnTeijiNo = initDto.getHdnTeijiNo();					// 提示番号
		String hdnNyukyoDate = initDto.getHdnNyukyoDate();				// 入居予定日
		String hdnTaikyoDate = initDto.getHdnTaikyoDate();				// 退居予定日
		String hdnShoruikanriNo = initDto.getHdnShoruikanriNo();		// 申請書類管理番号
		String hdnNyutaikyoKbn = initDto.getHdnNyutaikyoKbn();			// 入退居区分
		String hdnApplKbn = initDto.getHdnApplKbn();					// 申請区分
		String hdnShainNoChangeFlg = initDto.getHdnShainNoChangeFlg();	// 社員番号変更フラグ

		// ドロップダウンリスト
		List<Map<String, Object>> sc006KyojyusyaKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006YakuinSanteiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyoekihiPayMonthSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeInSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeOutSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoRyojokyoSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoHanteiKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinShatakuSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinKyoekihiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006OldKaisyaNameSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyuyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006HaizokuKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006TaiyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KariukeKaisyaSelectList = new ArrayList<Map<String, Object>>();

		// 非活性制御クリア
		skf3022Sc006SharedService.clearDisable(initDto);
		// 提示データ取得
		getTeijiData = skf3022Sc006SharedService.getTeijiData(hdnTeijiNo);
		// 非表示項目設定
		skf3022Sc006SharedService.setHiddenValues(hdnTeijiNo, hdnNyukyoDate, hdnTaikyoDate,
				hdnShoruikanriNo, hdnNyutaikyoKbn, hdnApplKbn, hdnShainNoChangeFlg, getTeijiData, initDto);
		// 画面項目表示を設定
		skf3022Sc006SharedService.setControlValues(getTeijiData, initDto);
		// ドロップダウンリスト作成
		skf3022Sc006SharedService.setDdlControlValues(
				initDto.getSc006KyojyusyaKbnSelect(), sc006KyojyusyaKbnSelectList,
				initDto.getSc006YakuinSanteiSelect(), sc006YakuinSanteiSelectList,
				initDto.getSc006KyoekihiPayMonthSelect(), sc006KyoekihiPayMonthSelectList,
				initDto.getSc006KibouTimeInSelect(), sc006KibouTimeInSelectList,
				initDto.getSc006KibouTimeOutSelect(), sc006KibouTimeOutSelectList,
				initDto.getSc006SogoRyojokyoSelect(), sc006SogoRyojokyoSelectList,
				initDto.getSc006SogoHanteiKbnSelect(), sc006SogoHanteiKbnSelectList,
				initDto.getSc006SokinShatakuSelect(), sc006SokinShatakuSelectList,
				initDto.getSc006SokinKyoekihiSelect(), sc006SokinKyoekihiSelectList,
				initDto.getSc006OldKaisyaNameSelect(), sc006OldKaisyaNameSelectList,
				initDto.getSc006KyuyoKaisyaSelect(), sc006KyuyoKaisyaSelectList,
				initDto.getSc006HaizokuKaisyaSelect(), sc006HaizokuKaisyaSelectList,
				initDto.getSc006TaiyoKaisyaSelect(), sc006TaiyoKaisyaSelectList,
				initDto.getSc006KariukeKaisyaSelect(), sc006KariukeKaisyaSelectList);

		// ドロップダウンリスト設定
		initDto.setSc006KyojyusyaKbnSelectList(sc006KyojyusyaKbnSelectList);
		initDto.setSc006YakuinSanteiSelectList(sc006YakuinSanteiSelectList);
		initDto.setSc006KyoekihiPayMonthSelectList(sc006KyoekihiPayMonthSelectList);
		initDto.setSc006KibouTimeInSelectList(sc006KibouTimeInSelectList);
		initDto.setSc006KibouTimeOutSelectList(sc006KibouTimeOutSelectList);
		initDto.setSc006SogoRyojokyoSelectList(sc006SogoRyojokyoSelectList);
		initDto.setSc006SogoHanteiKbnSelectList(sc006SogoHanteiKbnSelectList);
		initDto.setSc006SokinShatakuSelectList(sc006SokinShatakuSelectList);
		initDto.setSc006SokinKyoekihiSelectList(sc006SokinKyoekihiSelectList);
		initDto.setSc006OldKaisyaNameSelectList(sc006OldKaisyaNameSelectList);
		initDto.setSc006KyuyoKaisyaSelectList(sc006KyuyoKaisyaSelectList);
		initDto.setSc006HaizokuKaisyaSelectList(sc006HaizokuKaisyaSelectList);
		initDto.setSc006TaiyoKaisyaSelectList(sc006TaiyoKaisyaSelectList);
		initDto.setSc006KariukeKaisyaSelectList(sc006KariukeKaisyaSelectList);

		/** 原籍会社保留(仕様確認中) */
//	        '原籍会社を初期設定
//	        If String.IsNullOrEmpty(Me.ddlOldKaisya.SelectedValue) Then
//	            '①IdM_プレユーザマスタ（従業員区分）を取得
//	            Dim dtbIdmPreUserMasterInfo As New IDM_PRE_USER_MASTERDataTable()
//	            dtbIdmPreUserMasterInfo = S2007_TeijiDataRegistBusinessLogic.GetIdmPreUserMasterInfo(dt(0).SHAIN_NO)
//
//	            '②従業員区分が「1:役員、2:職員、3:常勤嘱託員、4:非常勤嘱託員、5:再任用職員、6:再任用短時間勤務職員、7:有機事務員」の場合
//	            If dtbIdmPreUserMasterInfo.Rows.Count > 0 Then
//	                If Not dtbIdmPreUserMasterInfo(0).IsPUM_HR_EMPLOYEE_CLASS_0Null AndAlso _
//	                    (dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.YAKUIN) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.SHOKUIN) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.JOKIN_SHOKUTAKU) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.HI_JOKIN_SHOKUTAKU) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.SAININ_SHOKUIN) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.SAININ_TANJIKAN_SHOKUIN) Or _
//	                     dtbIdmPreUserMasterInfo(0).PUM_HR_EMPLOYEE_CLASS_0.Equals(JUGYOIN_KBN.YUKI_JIMUIN)) Then
//
//	                    '原籍会社に「NEXCO中日本（C001）」を設定
//	                    Me.ddlOldKaisya.SelectedValue = COMPANY_CD
//	                End If
//	            End If
//	        End If
//
//	        '
		// 備品一覧再取得フラグ
		initDto.setBihinItiranFlg(true);
//
//	        '
		/** 運用ガイド保留(仕様確認中) */
		// 運用ガイドのパスを設定
//	        Dim unyonGuide As String = String.Empty
		// 運用ガイド取得
//		initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));

//	        Try
//	            unyonGuide = ConfigurationManager.AppSettings(APP_SETTINGS_OPE_GUIDE)
//
//	        Catch ex As ConfigurationErrorsException
//	            If Me.log.IsWarnEnabled Then
//	                Me.log.Warn(ERR_WEB_CONFIG_OPE_GUIDE_PATH + ex.Message)
//	                Me.log.Warn(ex)
//	            End If
//	        Finally
//	            Me.btnUnyonGuide.Attributes.Add(JS_ON_CLICK, String.Format(OPEN_WINDOW_ON_CLICK, unyonGuide))
//	        End Try
//

		skf3022Sc006SharedService.pageLoadComplete(initDto);
		return initDto;
	}
}

