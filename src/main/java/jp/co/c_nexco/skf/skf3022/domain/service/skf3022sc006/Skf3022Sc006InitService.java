/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetTeijiDataExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetNyutaikyoYoteiDataExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
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
	@Autowired
	private Skf3022Sc006GetIdmPreUserMasterInfoExpRepository skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
	@Autowired
	private Skf3022Sc006GetNyutaikyoYoteiDataExpRepository skf3022Sc006GetNyutaikyoYoteiDataExpRepository;

	/** IdM_プレユーザマスタ（従業員区分）定数 */
	// 役員
	private static final String IDM_YAKUIN = "1";
	// 職員
	private static final String IDM_SHOKUIN = "2";
	// 常勤嘱託員
	private static final String IDM_JOKIN_SHOKUTAKU = "3";
	// 非常勤嘱託員
	private static final String IDM_HI_JOKIN_SHOKUTAKU = "4";
	// 再任用職員
	private static final String IDM_SAININ_SHOKUIN = "5";
	// 再任用短時間勤務職員
	private static final String IDM_SAININ_TANJIKAN_SHOKUIN = "6";
	// 有機事務員
	private static final String IDM_YUKI_JIMUIN = "7";

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

		initDto.setPageId(FunctionIdConstant.SKF3022_SC006);
		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC006_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, FunctionIdConstant.SKF3022_SC006);

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

		// 戻るボタンメッセージ
		initDto.setLitMessageBack(PropertyUtils.getValue(MessageIdConstant.I_SKF_1009));
		// 備品一覧初期値(空っぽ)
		initDto.setBihinInfoListTableData(new ArrayList<Map<String, Object>>());
		// 提示データ取得
		getTeijiData = skf3022Sc006SharedService.getTeijiData(hdnTeijiNo);
		// 取得結果判定
		if (Objects.equals(getTeijiData, null)) {
			// 項目非活性
			skf3022Sc006SharedService.setDisableCtrlAll(true, initDto);
			// Mapがnullの場合、0件エラー表示
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1135);
			return initDto;
		}
		// 入退居予定データを取得
		List<Skf3022Sc006GetNyutaikyoYoteiDataExp> nyutaikyoYoteiDataList = new ArrayList<Skf3022Sc006GetNyutaikyoYoteiDataExp>();
		Skf3022Sc006GetNyutaikyoYoteiDataExpParameter nyutaikyoParam = new Skf3022Sc006GetNyutaikyoYoteiDataExpParameter();
		nyutaikyoParam.setShainNo(getTeijiData.getShainNo().replace("＊", "").replace("*", ""));
		nyutaikyoParam.setNyutaikyoKbn(getTeijiData.getNyutaikyoKbn());
		nyutaikyoYoteiDataList = skf3022Sc006GetNyutaikyoYoteiDataExpRepository.getNyutaikyoYoteiData(nyutaikyoParam);
		// 取得結果判定
		if (nyutaikyoYoteiDataList.size() > 0) {
			hdnNyukyoDate = nyutaikyoYoteiDataList.get(0).getNyukyoYoteiDate();			// 入居予定日
			hdnTaikyoDate = nyutaikyoYoteiDataList.get(0).getTaikyoYoteiDate();			// 退居予定日
			initDto.setHdnNyukyoDate(hdnNyukyoDate);				// 入居予定日
			initDto.setHdnTaikyoDate(hdnTaikyoDate);				// 退居予定日
		}
		// 非活性制御クリア
		skf3022Sc006SharedService.setDisableCtrlAll(false, initDto);
		// 非表示項目設定
		skf3022Sc006SharedService.setHiddenValues(hdnTeijiNo, hdnNyukyoDate, hdnTaikyoDate,
				hdnShoruikanriNo, hdnNyutaikyoKbn, hdnApplKbn, hdnShainNoChangeFlg, getTeijiData, initDto);
		// 画面項目表示を設定
		skf3022Sc006SharedService.setControlValues(getTeijiData, initDto);

		/** 原籍会社 */
		// 原籍会社を初期設定
		if (CheckUtils.isEmpty(initDto.getSc006OldKaisyaNameSelect())) {
			// ①IdM_プレユーザマスタ（従業員区分）を取得
			List<Skf3022Sc006GetIdmPreUserMasterInfoExp> dtbIdmPreUserMasterInfo = new ArrayList<Skf3022Sc006GetIdmPreUserMasterInfoExp>();
			Skf3022Sc006GetIdmPreUserMasterInfoExpParameter param = new Skf3022Sc006GetIdmPreUserMasterInfoExpParameter();
			param.setPumHrNameCode(getTeijiData.getShainNo());
			dtbIdmPreUserMasterInfo = skf3022Sc006GetIdmPreUserMasterInfoExpRepository.getIdmPreUserMasterInfo(param);
			// ②従業員区分が「1:役員、2:職員、3:常勤嘱託員、4:非常勤嘱託員、5:再任用職員、6:再任用短時間勤務職員、7:有機事務員」の場合
			if (dtbIdmPreUserMasterInfo.size() > 0) {
				if (Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_YAKUIN)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SHOKUIN)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_JOKIN_SHOKUTAKU)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_HI_JOKIN_SHOKUTAKU)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SAININ_SHOKUIN)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_SAININ_TANJIKAN_SHOKUIN)
						|| Objects.equals(dtbIdmPreUserMasterInfo.get(0).getPumHrEmployeeClass_0(), IDM_YUKI_JIMUIN)) {
					// 原籍会社に「NEXCO中日本（C001）」を設定
					initDto.setSc006OldKaisyaNameSelect(CodeConstant.C001);
				}
			}
		}
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

		// 選択タブインデックス初期値
		initDto.setHdnTabIndex("999");
		// 備品一覧再取得フラグ
		initDto.setBihinItiranFlg(true);
//
//	        '
		/** 運用ガイド保留(仕様確認中) */
		// 運用ガイドのパスを設定
		initDto.setOperationGuidePath("/skf/template/skf3022/skf3022mn006/"
				+ PropertyUtils.getValue("skf3022.skf3022_sc006.operationGuideFile"));
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
		// 処理状態クリア
		initDto.setSc006Status("");
		return initDto;
	}
}

