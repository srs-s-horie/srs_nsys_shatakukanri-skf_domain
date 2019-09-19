/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinShinseiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinShinseiInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002UpdateApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TAttachedFile;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2030TBihin;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoForUpdateExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetBihinShinseiInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002UpdateApplHistoryExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TAttachedFileRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2030TBihinRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common.Skf2040Sc002CommonDto;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002InitDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002SharedService {

	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2040Sc002GetTeijiDataInfoExpRepository skf2040Sc002GetTeijiDataInfoExpRepository;
	@Autowired
	Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	Skf2040Sc002GetHenkyakuBihinInfoExpRepository skf2040Sc002GetHenkyakuBihinInfoExpRepository;
	@Autowired
	Skf2040Sc002GetShatakuInfoExpRepository skf2040Sc002GetShatakuInfoExpRepository;
	@Autowired
	Skf2040Sc002GetApplHistoryInfoForUpdateExpRepository skf2040Sc002GetApplHistoryInfoForUpdateExpRepository;
	@Autowired
	Skf2040Sc002UpdateApplHistoryExpRepository skf2040Sc002UpdateApplHistoryExpRepository;
	@Autowired
	Skf2010TAttachedFileRepository skf2010TAttachedFileRepository;
	@Autowired
	Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;
	@Autowired
	Skf2040Sc002GetBihinShinseiInfoExpRepository skf2040Sc002GetBihinShinseiInfoExpRepository;
	@Autowired
	Skf2030TBihinRepository skf2030TBihinRepository;

	private MenuScopeSessionBean menuScopeSessionBean;

	private Map<String, String> bihinStatusMap;// 備品状態
	private Map<String, String> bihinReturnMap;// 備品返却

	private String sTrue = "true";

	/**
	 * セッション情報を取得
	 * 
	 * @param bean
	 */
	protected void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		menuScopeSessionBean = bean;
	}

	/**
	 * セッションの添付資料情報の初期化
	 * 
	 * @param bean
	 */
	protected void clearMenuScopeSessionBean() {
		if (menuScopeSessionBean == null) {
			return;
		}
		skfAttachedFileUtils.clearAttachedFileBySessionData(menuScopeSessionBean,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
	}

	/**
	 * 添付資料の設定をします
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	protected void setAttachedFileList(Skf2040Sc002CommonDto dto) {
		// セッションの添付資料情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		dto.setAttachedFileList(attachedFileList);
		return;
	}

	/**
	 * 備品返却区分を書き換え
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param shatakuNo
	 * @param roomNo
	 * @param henkyakuDt
	 * @return henkyakuList(返却備品のリスト)
	 */
	protected List<Map<String, Object>> updateBihinReturnKbn(Skf2040Sc002InitDto dto, long shatakuNo, long roomNo,
			List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt) {

		List<Map<String, Object>> henkyakuList = new ArrayList<Map<String, Object>>();

		// 提示データ情報を取得
		Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = getTeijiDataInfo(dto.getShainNo(), dto.getApplNo());
		if (teijiDataInfo == null) {
			// 提示データの取得ができない場合、リストは空で返す
			return henkyakuList;
		}

		// mapに返却備品情報を設定
		for (Skf2040Sc002GetHenkyakuBihinInfoExp henkyakuBihin : henkyakuDt) {

			Map<String, Object> bihinInfoMap = new HashMap<String, Object>();
			bihinInfoMap.put("bihinCd", henkyakuBihin.getBihinCd());// 備品コード
			bihinInfoMap.put("bihinName", henkyakuBihin.getBihinName());// 備品名
			bihinInfoMap.put("bihinPayment", henkyakuBihin.getBihinPayment());// 現物支給額
			bihinInfoMap.put("bihinLentStatusKbn", henkyakuBihin.getBihinLentStatusKbn());// 備品貸与状態区分
			bihinInfoMap.put("bihinReturnKbn", henkyakuBihin.getBihinReturnKbn());// 備品返却区分
			bihinInfoMap.put("bihinApplKbn", henkyakuBihin.getBihinApplKbn());// 備品申請区分

			// 提示備品データの備品貸与状態区分を設定
			String teijiBihinStts = CodeConstant.DOUBLE_QUOTATION;
			if (NfwStringUtils.isEmpty(henkyakuBihin.getTeijiBihinLentStatusKbn())) {
				// 備品貸与状態がDBNullの場合、次のレコードへ。
				continue;
			} else {
				teijiBihinStts = henkyakuBihin.getTeijiBihinLentStatusKbn();
			}

			// 社宅部屋備品情報の備品貸与状態区分を設定
			String roomBihinStts = CodeConstant.DOUBLE_QUOTATION;
			if (NfwStringUtils.isEmpty(henkyakuBihin.getRoomBihinLentStatusKbn())) {
				// 部屋備付状態区分がDBNullの場合、次のレコードへ。
				continue;
			} else {
				roomBihinStts = henkyakuBihin.getRoomBihinLentStatusKbn();
			}

			// 更新する備品返却区分の設定
			String bihinReturnKbn = CodeConstant.DOUBLE_QUOTATION;
			// 提示備品データの備品貸与状態区分が社宅部屋備品情報の備品備付区分と同じ場合
			if (teijiBihinStts.equals(roomBihinStts)) {
				// 社宅部屋備品情報の備品備付区分が「保有」か「レンタル」の場合、備品返却区分を”0”（返却区分 返却不要）に設定する。
				if (CodeConstant.BIHIN_STATE_HOYU.equals(roomBihinStts)
						|| CodeConstant.BIHIN_STATE_RENTAL.equals(roomBihinStts)) {
					bihinReturnKbn = CodeConstant.BIHIN_HENKYAKU_KBN_FUYO;
				} else {
					bihinReturnKbn = CodeConstant.DOUBLE_QUOTATION;
				}
			}

			// 社宅部屋備品情報の備品備付区分が「なし」「共有」の場合も、返却備品として扱う。
			// 提示備品データの備品貸与状態区分が「レンタル」の場合、備品返却区分を”3”（レンタル返却）に設定する
			if ((CodeConstant.BIHIN_STATE_NONE.equals(roomBihinStts)
					|| CodeConstant.BIHIN_STATE_KYOYO.equals(roomBihinStts))
					&& CodeConstant.BIHIN_STATE_RENTAL.equals(teijiBihinStts)) {

				bihinReturnKbn = CodeConstant.BIHIN_HENKYAKU_KBN_RENTAL_HENKYAKU;
			}

			// 社宅部屋備品情報の備品備付区分が「なし」「共有」の場合も、返却備品として扱う。
			// 提示備品データの備品貸与状態区分が”会社保有”の場合、備品返却区分を”2”（会社保有返却）に設定する
			if ((CodeConstant.BIHIN_STATE_NONE.equals(roomBihinStts)
					|| CodeConstant.BIHIN_STATE_KYOYO.equals(roomBihinStts))
					&& CodeConstant.BIHIN_STATE_HOYU.equals(teijiBihinStts)) {

				bihinReturnKbn = CodeConstant.BIHIN_HENKYAKU_KBN_KAISHA_HOYU_HENKYAKU;
			}

			// 設定した値で備品返却区分を書き換え
			bihinInfoMap.put("bihinReturnKbn", bihinReturnKbn);// 備品返却区分

			// 備品状態 表示名
			String bihinStateText = henkyakuBihin.getBihinLentStatusKbn();
			if (NfwStringUtils.isEmpty(bihinStateText)) {
				// 返却区分がない場合はハイフン設定
				bihinStateText = CodeConstant.HYPHEN;
			}

			// 返却区分 表示名
			String bihinReturnText = bihinReturnKbn;
			if (NfwStringUtils.isEmpty(bihinReturnText)) {
				// 返却区分がない場合はハイフン設定
				bihinReturnText = CodeConstant.HYPHEN;
			}

			bihinInfoMap.put("bihinStateText", bihinStateText);
			bihinInfoMap.put("bihinReturnText", bihinReturnText);

			// リストに設定
			henkyakuList.add(bihinInfoMap);
		}

		return henkyakuList;
	}

	/**
	 * 提示データ情報の取得
	 * 
	 * @param shainNo
	 * @param applNo
	 * @return Skf2040Sc002GetTeijiDataInfoExp
	 */
	protected Skf2040Sc002GetTeijiDataInfoExp getTeijiDataInfo(String shainNo, String applNo) {

		Skf2040Sc002GetTeijiDataInfoExp teijiDataInfo = new Skf2040Sc002GetTeijiDataInfoExp();
		Skf2040Sc002GetTeijiDataInfoExpParameter param = new Skf2040Sc002GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(CodeConstant.SYS_TAIKYO_KBN);
		param.setApplNo(applNo);
		teijiDataInfo = skf2040Sc002GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);

		return teijiDataInfo;
	}

	/**
	 * 退居（自動車の保管場所返還）届の情報取得
	 * 
	 * @param 退居届の申請書類管理番号
	 * @return Skf2040TTaikyoReport
	 */
	protected Skf2040TTaikyoReport getTaikyoReport(String applNo) {

		Skf2040TTaikyoReport taikyoRepDt = new Skf2040TTaikyoReport();
		Skf2040TTaikyoReportKey setKey = new Skf2040TTaikyoReportKey();
		setKey.setCompanyCd(CodeConstant.C001);
		setKey.setApplNo(applNo);
		taikyoRepDt = skf2040TTaikyoReportRepository.selectByPrimaryKey(setKey);
		return taikyoRepDt;
	}

	/**
	 * 備品返却申請テーブルの情報取得
	 * 
	 * @param applNo
	 * @return Skf2050TBihinHenkyakuShinsei
	 */
	protected Skf2050TBihinHenkyakuShinsei getBihinHenkyakuShinsei(String applNo) {

		Skf2050TBihinHenkyakuShinsei bihinDt = new Skf2050TBihinHenkyakuShinsei();
		Skf2050TBihinHenkyakuShinseiKey setbihinKey = new Skf2050TBihinHenkyakuShinseiKey();
		setbihinKey.setCompanyCd(CodeConstant.C001);
		setbihinKey.setApplNo(applNo);
		bihinDt = skf2050TBihinHenkyakuShinseiRepository.selectByPrimaryKey(setbihinKey);

		return bihinDt;
	}

	/**
	 * 社宅管理提示データ、提示備品データ、備品項目設定から備品情報を取得
	 * 
	 * @param applNo
	 * @param shainNo
	 * @param shatakuNo
	 * @param roomNo
	 * @param teijiNo
	 * @return
	 */
	protected List<Skf2040Sc002GetHenkyakuBihinInfoExp> getHenkyakuBihinInfo(String applNo, String shainNo,
			long shatakuNo, long roomNo, long teijiNo) {

		List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = new ArrayList<Skf2040Sc002GetHenkyakuBihinInfoExp>();
		Skf2040Sc002GetHenkyakuBihinInfoExpParameter henkyakuDtParam = new Skf2040Sc002GetHenkyakuBihinInfoExpParameter();
		henkyakuDtParam.setShainNo(shainNo);
		henkyakuDtParam.setNyutaikyoKbn(CodeConstant.SYS_TAIKYO_KBN);
		henkyakuDtParam.setApplNo(applNo);
		henkyakuDtParam.setShatakuKanriNo(shatakuNo);
		henkyakuDtParam.setShatakuRoomKanriNo(roomNo);
		henkyakuDtParam.setTeijiNo(teijiNo);
		henkyakuDt = skf2040Sc002GetHenkyakuBihinInfoExpRepository.getHenkyakuBihinInfo(henkyakuDtParam);

		return henkyakuDt;
	}

	/**
	 * 備品返却時の表示項目
	 * 
	 * @param initDto
	 * @param taikyoRepDt
	 */
	protected void setBihinHenkyakuDisp(Skf2040Sc002InitDto initDto, Skf2040TTaikyoReport taikyoRepDt) {

		// 返却立会希望日（日）の取得
		String sessionDay = CodeConstant.DOUBLE_QUOTATION;
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getSessionDay())) {
			sessionDay = skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getSessionDay(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		}
		// 返却立会希望日（時）の取得
		String sessionTime = CodeConstant.DOUBLE_QUOTATION;
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getSessionTime())) {
			sessionTime = taikyoRepDt.getSessionTime();
			if (NfwStringUtils.isNotEmpty(sessionTime)) {
				// 返却立会希望日（時）の名称を取得
				sessionTime = skfGenericCodeUtils
						.getGenericCodeNameReverse(FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sessionTime);
			}
		}
		// 返却立会希望日時の表示
		initDto.setSessionDay(sessionDay + CodeConstant.SPACE_CHAR + sessionTime);

		// 連絡先の表示
		if (NfwStringUtils.isNotEmpty(taikyoRepDt.getRenrakuSaki())) {
			initDto.setRenrakuSaki(taikyoRepDt.getRenrakuSaki());
		}

	}

	/**
	 * ボタンの表示制御 true：表示、false：非表示
	 * 
	 * マスクパターンの種類 <br>
	 * PTN_A：【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】<br>
	 * PTN_B:【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】<br>
	 * PTN_C:【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】<br>
	 * PTN_D：【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：表示】<br>
	 * PTN_E：【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】<br>
	 * PTN_F:【全ボタン非表示】<br>
	 * 
	 * @param maslPtn
	 * @param shiryoBtn
	 * @param pdfDwnBtn
	 * @param dto
	 */
	protected void setButtonVisible(String maslPtn, String pdfDwnBtn, Skf2040Sc002CommonDto dto) {

		dto.setMaskPattern(maslPtn);// マスク（非表示）パターン
		dto.setTaikyoPdfViewFlg(pdfDwnBtn);// pdfダウンロードボタン
	}

	/**
	 * 社宅情報の取得
	 * 
	 * @param shatakuKanriNo
	 * @param shainNo
	 * @return
	 */
	protected Skf2040Sc002GetShatakuInfoExp getShatakuInfo(Long shatakuKanriNo, String shainNo) {
		Skf2040Sc002GetShatakuInfoExp shatakuInfo = new Skf2040Sc002GetShatakuInfoExp();
		Skf2040Sc002GetShatakuInfoExpParameter param = new Skf2040Sc002GetShatakuInfoExpParameter();

		Date nowDate = new Date();
		String yearMonth = skfDateFormatUtils.dateFormatFromDate(nowDate, "yyyyMM");
		param.setYearMonth(yearMonth);
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShainNo(shainNo);

		shatakuInfo = skf2040Sc002GetShatakuInfoExpRepository.getShatakuInfo(param);
		return shatakuInfo;
	}

	/**
	 * 退居届帳票イメージの情報設定
	 * 
	 * @param initDto
	 * @param taikyoRepDt
	 * @param shatakuInfo
	 */
	protected void setReportInfo(Skf2040Sc002CommonDto dto, Skf2040TTaikyoReport taikyoRepDt,
			Skf2040Sc002GetShatakuInfoExp shatakuInfo) {

		String sfontColor = "<font color='red'>";
		String eFontColoor = "</font>";

		// 申請書類タイトル表記設定
		dto.setShatakuTaikyoKbn(taikyoRepDt.getShatakuTaikyoKbn()); // 社宅退居
		dto.setShatakuTaikyoKbn2(taikyoRepDt.getShatakuTaikyoKbn2()); // 駐車場返還

		// 申請書類管理番号
		dto.setApplNo(taikyoRepDt.getApplNo());
		// 申請年月日
		String applDate = taikyoRepDt.getApplDate();
		String applDateText = skfDateFormatUtils.dateFormatFromString(applDate,
				SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
		dto.setApplDate(applDateText);
		// 機関
		dto.setNowAgency(taikyoRepDt.getAgency());
		// 部等
		dto.setNowAffiliation1(taikyoRepDt.getAffiliation1());
		// 室、チーム又は課
		dto.setNowAffiliation2(taikyoRepDt.getAffiliation2());
		// 現住所
		dto.setAddress(shatakuInfo.getAddress());
		// 氏名
		dto.setName(taikyoRepDt.getName());

		// 社宅退居区分
		taikyoRepDt.getShatakuTaikyoKbn();

		// 社宅
		dto.setShatakuName(shatakuInfo.getShatakuName());
		// 駐車場1
		dto.setParkingAddress1(shatakuInfo.getParkingAddress1());
		// 駐車場2
		dto.setParkingAddress2(shatakuInfo.getParkingAddress2());
		// 退居日 社宅等
		// 退居日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDate()))) {
			dto.setTaikyoDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getTaikyoDateFlg())) {

				String taikyoDate = skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
				// fontColorタグ設定
				taikyoDate = sfontColor + taikyoDate + eFontColoor;
				dto.setTaikyoDate(taikyoDate);
			}
		}
		// 駐車場返還日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingHenkanDate()))) {
			dto.setParkingHenkanDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getParkingHenkanDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingEDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getParkingEDateFlg())) {

				String parkingHenkanDate = skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getParkingHenkanDate(),
						SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
				// fontColorタグ設定
				parkingHenkanDate = sfontColor + parkingHenkanDate + eFontColoor;
				dto.setParkingHenkanDate(parkingHenkanDate);
			}
		} else {
			// 駐車場返還日がない場合は、退居日を設定
			dto.setParkingHenkanDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR));
		}
		// 退居（返還）理由
		Map<String, String> taikyoRiyuMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_TAIKYO_HENKAN_RIYU);
		String taikyoRiyu = CodeConstant.DOUBLE_QUOTATION;
		if (taikyoRiyuMap != null) {
			taikyoRiyu = taikyoRiyuMap.get(taikyoRepDt.getTaikyoRiyuKbn());
		}
		dto.setTaikyoRiyu(taikyoRiyu);
		// 退居後の連絡先
		dto.setTaikyogoRenrakusaki(taikyoRepDt.getTaikyogoRenrakusaki());

	}

	/**
	 * 添付ファイル情報の設定
	 * 
	 * @param dto
	 */
	protected void refreshHeaderAttachedFile(Skf2040Sc002CommonDto dto) {

		List<Map<String, Object>> tmpAttachedFileList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();

		// 添付資料がある場合、添付資料表示処理を行う
		if (NfwStringUtils.isNotEmpty(dto.getApplTacFlg()) && SkfCommonConstant.AVAILABLE.equals(dto.getApplTacFlg())) {

			// 添付ファイル情報の取得
			tmpAttachedFileList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, dto.getApplNo(),
					SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
			if (tmpAttachedFileList != null && tmpAttachedFileList.size() > 0) {
				int defaultAttachedNo = 0;
				for (Map<String, Object> tmpAttachedFileMap : tmpAttachedFileList) {
					skfAttachedFileUtils.addShatakuAttachedFile(tmpAttachedFileMap.get("attachedName").toString(),
							(byte[]) tmpAttachedFileMap.get("fileStream"),
							tmpAttachedFileMap.get("fileSize").toString(), defaultAttachedNo, attachedFileList);
				}
			}
		}
		// 添付ファイル情報設定
		dto.setAttachedFileList(attachedFileList);
	}

	/**
	 * 
	 * 備品状態と返却区分の設定
	 * 
	 * @param initDto
	 * @param henkyakuList
	 */
	protected void setBihinData(Skf2040Sc002CommonDto dto, List<Map<String, Object>> henkyakuList) {

		List<Map<String, Object>> bihinShinseiList = new ArrayList<Map<String, Object>>();
		bihinStatusMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_EQUIPMENT_STATE); // 備品状態
		bihinReturnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_LOAN_KBN); // 備品返却区分

		/*
		 * 備品情報
		 */
		Map<String, Object> bihinMap = new HashMap<String, Object>();
		for (Map<String, Object> list : henkyakuList) {

			// 備品状態
			String bihinStateText = bihinStatusMap.get(list.get("bihinLentStatusKbn").toString());
			if (NfwStringUtils.isEmpty(bihinStateText)) {
				// 返却区分がない場合はハイフン設定
				bihinStateText = CodeConstant.HYPHEN;
			}
			// 返却区分
			String bihinReturnText = bihinReturnMap.get(list.get("bihinReturnKbn").toString());
			if (NfwStringUtils.isEmpty(bihinReturnText)) {
				// 返却区分がない場合はハイフン設定
				bihinReturnText = CodeConstant.HYPHEN;
			}

			bihinMap = new HashMap<String, Object>();
			bihinMap.put("bihinCd", list.get("bihinCd").toString());
			bihinMap.put("bihinName", list.get("bihinName").toString());
			bihinMap.put("bihinState", list.get("bihinLentStatusKbn").toString());
			bihinMap.put("bihinReturn", list.get("bihinReturnKbn").toString());
			bihinMap.put("bihinStateText", bihinStateText);
			bihinMap.put("bihinReturnText", bihinReturnText);
			bihinShinseiList.add(bihinMap);

			// dtoに割り当て
			// switch (bihinCd) {
			// case CodeConstant.BIHIN_WASHER:
			// dto.setBihinState11(bihinStateText);
			// dto.setBihinReturn11(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_FREEZER:
			// dto.setBihinState12(bihinStateText);
			// dto.setBihinReturn12(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_OVEN:
			// dto.setBihinState13(bihinStateText);
			// dto.setBihinReturn13(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_CLENER:
			// dto.setBihinState14(bihinStateText);
			// dto.setBihinReturn14(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_RICE_COOKER:
			// dto.setBihinState15(bihinStateText);
			// dto.setBihinReturn15(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_TV:
			// dto.setBihinState16(bihinStateText);
			// dto.setBihinReturn16(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_TV_STANDS:
			// dto.setBihinState17(bihinStateText);
			// dto.setBihinReturn17(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_KOTATSU:
			// dto.setBihinState18(bihinStateText);
			// dto.setBihinReturn18(bihinReturnText);
			// break;
			// case CodeConstant.BIHIN_KICHEN_CABINET:
			// dto.setBihinState19(bihinStateText);
			// dto.setBihinReturn19(bihinReturnText);
			// break;
		}
		dto.setHenkyakuList(bihinShinseiList);
	}

	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @param reFlg 差戻し、修正依頼フラグ
	 * @return 正常：true 異常:false
	 * @throws Exception
	 */
	protected boolean checkValidation(Skf2040Sc002CommonDto dto, String reFlg) throws Exception {

		// コメント欄入力チェック
		String commentNote = StringUtils.strip(dto.getCommentNote());

		// 差戻し、修正依頼の場合必須入力チェック
		if (sTrue.equals(reFlg) && NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					"申請者へのコメント");
			return false;
		}

		// 文字数チェック
		if (NfwStringUtils.isNotBlank(commentNote) && CheckUtils.isMoreThanByteSize(commentNote.trim(), 4000)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1071,
					"申請者へのコメント", "2000");
			return false;
		}

		return true;
	}

	/**
	 * 申請書類履歴保存の処理
	 * 
	 * @param newStatus
	 * @param dto
	 * @param errorMsg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean saveApplInfo(String newStatus, Skf2040Sc002CommonDto dto, Map<String, String> errorMsg) {

		String shainNo = dto.getShainNo();
		String applNo = dto.getApplNo();

		// セッションの添付資料情報を取得
		List<Map<String, Object>> attachedFileList = (List<Map<String, Object>>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		// 添付ファイルの有無
		String applTacFlg = CodeConstant.DOUBLE_QUOTATION;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルあり
			applTacFlg = CodeConstant.YES;
		} else {
			applTacFlg = CodeConstant.NO;
		}

		// 申請書類情報の取得
		Skf2040Sc002GetApplHistoryInfoForUpdateExp applInfo = new Skf2040Sc002GetApplHistoryInfoForUpdateExp();
		Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter param = new Skf2040Sc002GetApplHistoryInfoForUpdateExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(applNo);
		applInfo = skf2040Sc002GetApplHistoryInfoForUpdateExpRepository.getApplHistoryInfoForUpdate(param);
		if (applInfo == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();

		dto.setApplId(applInfo.getApplId());

		switch (newStatus) {
		case CodeConstant.STATUS_SASHIMODOSHI:
		case CodeConstant.STATUS_HININ:
			// 承認者設定処理
			String shonin1 = CodeConstant.NONE;
			String shonin2 = CodeConstant.NONE;
			switch (applInfo.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_DOI_ZUMI:
				shonin1 = userInfo.get("userName");
				break;
			case CodeConstant.STATUS_SHONIN1:
				shonin2 = userInfo.get("userName");
				break;
			}

			// 申請書類履歴テーブルの更新
			boolean resultUpdateApplInfo = updateApplHistoryAgreeStatus(newStatus, shainNo, applNo, shonin1, shonin2,
					applInfo.getApplId());
			if (!resultUpdateApplInfo) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}

			break;
		}

		// 修正依頼、差戻し時はコメントテーブルを更新
		if (newStatus.equals(CodeConstant.STATUS_SASHIMODOSHI) || newStatus.equals(CodeConstant.STATUS_HININ)) {
			if (!insertCommentTable(userInfo, applNo, newStatus, errorMsg, dto.getCommentNote())) {
				return false;
			}
		}

		// 添付ファイル管理テーブル更新処理
		boolean resultUpdateFile = updateAttachedFileInfo(newStatus, applNo, shainNo, attachedFileList, applTacFlg,
				applInfo, errorMsg);
		if (!resultUpdateFile) {
			return false;
		}

		// TODO 社宅連携フラグ(0：社宅未連携、1：社宅連携)が「1」の場合に実行
		return true;
	}

	/**
	 * 申請コメントテーブル更新
	 * 
	 * @param userInfo
	 * @param applNo
	 * @param newStatus
	 * @param errorMsg
	 * @param comment
	 * @return
	 */
	protected boolean insertCommentTable(Map<String, String> userInfo, String applNo, String newStatus,
			Map<String, String> errorMsg, String comment) {

		String commentName = CodeConstant.NONE;
		// 室、チームまたは課名を追記
		List<String> tmpNameList = new ArrayList<String>();
		if (userInfo.get("affiliation2Name") != null) {
			tmpNameList.add(userInfo.get("affiliation2Name"));
		}
		tmpNameList.add(StringUtils.trim(userInfo.get("userName")));
		commentName = String.join("\r\n", tmpNameList); // ログインユーザーの名前を取得
		String commentNote = StringUtils.trim(comment);

		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applNo, newStatus, commentName,
				commentNote, errorMsg);
		if (!commentErrorMessage) {
			return false;
		}
		return true;
	}

	/**
	 * 申請履歴の承認者と申請状況を更新します
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param shonin1
	 * @param shonin2
	 * @param applInfo
	 * @return
	 */
	protected boolean updateApplHistoryAgreeStatus(String newStatus, String shainNo, String applNo, String shonin1,
			String shonin2, String applId) {

		Skf2040Sc002UpdateApplHistoryExp record = new Skf2040Sc002UpdateApplHistoryExp();
		if (NfwStringUtils.isNotEmpty(shonin1)) {
			record.setAgreName1(shonin1);
		}
		if (NfwStringUtils.isNotEmpty(shonin2)) {
			record.setAgreName1(shonin2);
		}
		record.setAgreDate(new Date());
		record.setApplStatus(newStatus);

		// 条件
		record.setCompanyCd(CodeConstant.C001);
		record.setApplNo(applNo);
		record.setShainNo(shainNo);
		record.setApplId(applId);

		int result = skf2040Sc002UpdateApplHistoryExpRepository.updateApplHistory(record);
		if (result <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * 添付ファイル管理テーブルの更新
	 * 
	 * @param newStatus
	 * @param applNo
	 * @param shainNo
	 * @param attachedFileList
	 * @param applTacFlg
	 * @param applInfo
	 * @param errorMsg
	 * @return
	 */
	protected boolean updateAttachedFileInfo(String newStatus, String applNo, String shainNo,
			List<Map<String, Object>> attachedFileList, String applTacFlg,
			Skf2040Sc002GetApplHistoryInfoForUpdateExp applInfo, Map<String, String> errorMsg) {

		// 添付ファイル管理テーブルを更新する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルの更新は削除→登録で行う
			skfAttachedFileUtils.deleteAttachedFile(applNo, shainNo, errorMsg);
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				Skf2010TAttachedFile insertData = new Skf2010TAttachedFile();
				insertData = mappingTAttachedFile(attachedFileMap, applNo, shainNo);
				skf2010TAttachedFileRepository.insertSelective(insertData);
			}
		}
		// 申請書類履歴テーブルの添付書類有無を更新
		String applId = applInfo.getApplId();
		Skf2040Sc002UpdateApplHistoryExp updateData = new Skf2040Sc002UpdateApplHistoryExp();
		updateData.setApplTacFlg(String.valueOf(applTacFlg));
		// 条件項目
		updateData.setCompanyCd(CodeConstant.C001);
		updateData.setApplNo(applNo);
		updateData.setApplId(applId);
		updateData.setShainNo(shainNo);
		int result = skf2040Sc002UpdateApplHistoryExpRepository.updateApplHistory(updateData);
		if (result <= 0) {
			errorMsg.put("error", MessageIdConstant.E_SKF_1075);
			return false;
		}
		return true;
	}

	/**
	 * 添付ファイル管理テーブルの登録値を設定する
	 * 
	 * @param attachedFileMap
	 * @param applNo
	 * @param shainNo
	 * @return
	 */
	private Skf2010TAttachedFile mappingTAttachedFile(Map<String, Object> attachedFileMap, String applNo,
			String shainNo) {
		Skf2010TAttachedFile resultData = new Skf2010TAttachedFile();

		// 会社コード
		resultData.setCompanyCd(CodeConstant.C001);
		// 社員番号
		resultData.setShainNo(shainNo);
		// 登録日時
		Date nowDate = new Date();
		resultData.setApplDate(nowDate);
		// 申請番号
		resultData.setApplNo(applNo);
		// 添付番号
		resultData.setAttachedNo(attachedFileMap.get("attachedNo").toString());
		// 添付資料名
		resultData.setAttachedName(attachedFileMap.get("attachedName").toString());
		// ファイル
		resultData.setFileStream((byte[]) attachedFileMap.get("fileStream"));
		// ファイルサイズ
		resultData.setFileSize(attachedFileMap.get("fileSize").toString());

		return resultData;
	}

	// /**
	// * 申請書類履歴テーブル情報取得
	// *
	// * @param setValue
	// * @param dto
	// * @return
	// */
	// protected Skf2010TApplHistory
	// selectByApplHistoryPrimaryKey(Skf2010TApplHistory setValue,
	// Skf2040Sc002CommonDto dto) {
	// // キー項目をセット
	// setValue.setCompanyCd(CodeConstant.C001);
	// setValue.setShainNo(dto.getShainNo());
	// setValue.setApplDate(dto.getApplHistoryDate());
	// setValue.setApplNo(dto.getApplNo());
	// setValue.setApplId(dto.getApplId());
	//
	// Skf2010TApplHistory resultInfo = new Skf2010TApplHistory();
	// resultInfo = skf2010TApplHistoryRepository.selectByPrimaryKey(setValue);
	//
	// return resultInfo;
	// }

	/**
	 * 申請書類履歴テーブルに備品返却申請を登録/更新する処理
	 * 
	 * @param nextStatus
	 * @param applTacFlg
	 * @param dto
	 * @param shoninName1
	 * @param shoninName2
	 * @param applId
	 * @return
	 */
	public boolean insertOrUpdateApplHistoryForBihinHenkyaku(String nextStatus, String applTacFlg,
			Skf2040Sc002CommonDto dto, String shoninName1, String shoninName2, String applId) {

		String mailKbn = CodeConstant.DOUBLE_QUOTATION;

		// 次のステータスを設定する
		switch (dto.getApplStatus()) {
		case CodeConstant.STATUS_SHINSACHU:
			// 申請状況が「審査中」
			nextStatus = CodeConstant.STATUS_KAKUNIN_IRAI;
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			break;
		case CodeConstant.STATUS_DOI_ZUMI:
			// 申請状況が「同意済」
			// 次のステータス、メール区分、承認者名1、次の階層を設定
			nextStatus = CodeConstant.STATUS_SHONIN1;
			mailKbn = CodeConstant.SHONIN_IRAI_TSUCHI;
			break;
		case CodeConstant.STATUS_SHONIN1:
			// 申請状況が「承認1済」
			// 次のステータス、メール区分、承認者名2、承認済みを設定
			nextStatus = CodeConstant.STATUS_SHONIN_ZUMI;
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			break;
		}

		dto.setMailKbn(mailKbn);

		// 申請書類履歴テーブルに備品返却申請があるかどうか確認
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		Skf2040Sc002GetApplHistoryInfoExpParameter param = new Skf2040Sc002GetApplHistoryInfoExpParameter();
		param.setApplNo(dto.getHdnBihinHenkyakuApplNo());
		applHistoryList = skf2040Sc002GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);

		if (applHistoryList != null && applHistoryList.size() > 0) {
			// あれば更新処理
			// 申請書類履歴テーブルの更新
			boolean resultUpdateApplInfo = updateApplHistoryAgreeStatus(nextStatus, dto.getShainNo(),
					dto.getHdnBihinHenkyakuApplNo(), shoninName1, shoninName2, applId);
			if (!resultUpdateApplInfo) {
				return false;
			}
		} else {
			// 新規作成
			boolean resutInsertApplInfo = insertApplHistory(dto.getShainNo(), dto.getHdnBihinHenkyakuApplNo(), applId,
					CodeConstant.STATUS_KAKUNIN_IRAI, applTacFlg, SkfCommonConstant.NOT_RENKEI);
			if (!resutInsertApplInfo) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 申請書類履歴テーブル登録処理
	 * 
	 * @param shainNo
	 * @param hdnBihinHenkyakuApplNo
	 * @param applId
	 * @param applStatus
	 * @param applTacFlg
	 * @param notRenkei
	 * @return
	 */
	protected boolean insertApplHistory(String shainNo, String hdnBihinHenkyakuApplNo, String applId, String applStatus,
			String applTacFlg, String notRenkei) {

		// 申請書類履歴テーブルの設定
		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		// 登録項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setShainNo(shainNo);
		setValue.setApplDate(DateUtils.getSysDate());
		setValue.setApplNo(hdnBihinHenkyakuApplNo);
		setValue.setApplId(applId);
		setValue.setApplStatus(applStatus);
		setValue.setApplTacFlg(applTacFlg);
		setValue.setComboFlg(notRenkei);
		// 登録
		int registCount = 0;
		registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
		LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");
		return true;
	}

	/**
	 * 備品申請テーブルの登録/更新処理
	 * 
	 * @param preDto
	 * @return
	 */
	public boolean insertOrUpdateBihinShinseiTable(Skf2040Sc002CommonDto dto) {
		boolean returnValue = true;

		// 備品申請テーブルのデータ存在チェック
		List<Skf2040Sc002GetBihinShinseiInfoExp> bihinList = new ArrayList<Skf2040Sc002GetBihinShinseiInfoExp>();
		Skf2040Sc002GetBihinShinseiInfoExpParameter param = new Skf2040Sc002GetBihinShinseiInfoExpParameter();
		// 条件項目
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(dto.getHdnBihinHenkyakuApplNo());
		bihinList = skf2040Sc002GetBihinShinseiInfoExpRepository.getBihinShinseiInfo(param);

		boolean insertFlg = false;
		if (bihinList == null && bihinList.size() < 0) {
			// 備品申請テーブルのデータが存在しない場合は新規追加
			insertFlg = true;
		}

		if (insertFlg) {
			// 備品申請テーブルの新規追加
			for (Map<String, Object> list : dto.getHenkyakuList()) {
				// 登録情報の設定
				Skf2030TBihin setValue = new Skf2030TBihin();
				setValue = setColumnInfoListForBihin(dto, insertFlg, setValue, list.get("bihinCd").toString(),
						list.get("bihinName").toString(), list.get("bihinState").toString(),
						list.get("bihinReturn").toString());
				skf2030TBihinRepository.insertSelective(setValue);
			}

		} else {
			// 備品申請テーブルの更新
			for (Map<String, Object> list : dto.getHenkyakuList()) {
				// 登録情報の設定
				Skf2030TBihin setValue = new Skf2030TBihin();
				setValue = setColumnInfoListForBihin(dto, insertFlg, setValue, list.get("bihinCd").toString(),
						list.get("bihinName").toString(), list.get("bihinState").toString(),
						list.get("bihinReturn").toString());
				skf2030TBihinRepository.updateByPrimaryKeySelective(setValue);
			}
		}

		return returnValue;
	}

	/**
	 * カラム情報の取得（備品申請）
	 * 
	 * @param dto
	 * @param insertFlg
	 * @param setValue
	 * @param bihinCd
	 * @param bihinName
	 * @param string3
	 * @param string4
	 * @return
	 */
	private Skf2030TBihin setColumnInfoListForBihin(Skf2040Sc002CommonDto dto, boolean insertFlg,
			Skf2030TBihin setValue, String bihinCd, String bihinName, String bihinState, String bihinReturn) {
		// 登録項目をセット

		if (!insertFlg) {
			// 更新SQLでは不要
			// 申請区分
			setValue.setBihinAppl(CodeConstant.STRING_ZERO);
		}

		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 申請書類番号
		setValue.setApplNo(dto.getHdnBihinHenkyakuApplNo());
		// 備品コード
		setValue.setBihinCd(bihinCd);
		setValue.setBihinName(bihinName);
		setValue.setBihinState(bihinState);
		setValue.setBihinAdjust(bihinReturn);

		return setValue;
	}

	// /**
	// * カラム情報の取得（備品申請）
	// *
	// * @param dto
	// * @param insertFlg
	// * @param setValue
	// * @param bihinCd
	// * @param bihinMap
	// * @return
	// */
	// private Skf2030TBihin setColumnInfoListForBihin(Skf2040Sc002CommonDto
	// dto, boolean insertFlg,
	// Skf2030TBihin setValue, String bihinCd, HashMap<String, Object> bihinMap)
	// {
	//
	// // 登録項目をセット
	//
	// if (!insertFlg) {
	// // 更新SQLでは不要
	// // 申請区分
	// setValue.setBihinAppl(CodeConstant.STRING_ZERO);
	// }
	//
	// // 会社コード
	// setValue.setCompanyCd(CodeConstant.C001);
	// // 申請書類番号
	// setValue.setApplNo(dto.getHdnBihinHenkyakuApplNo());
	// // 備品コード
	// setValue.setBihinCd(bihinCd);
	// setValue.setBihinName(b);
	// setValue.setBihinState(dto.getBihinState11());
	// setValue.setBihinAdjust(dto.getBihinReturn11());
	//
	// // switch (bihinCd) {
	// // case CodeConstant.BIHIN_WASHER:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("洗濯機");
	// // setValue.setBihinState(dto.getBihinState11());
	// // setValue.setBihinAdjust(dto.getBihinReturn11());
	// // break;
	// // case CodeConstant.BIHIN_FREEZER:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("冷蔵庫");
	// // setValue.setBihinState(dto.getBihinState12());
	// // setValue.setBihinAdjust(dto.getBihinReturn12());
	// // break;
	// // case CodeConstant.BIHIN_OVEN:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("オーブンレンジ");
	// // setValue.setBihinState(dto.getBihinState13());
	// // setValue.setBihinAdjust(dto.getBihinReturn13());
	// // break;
	// // case CodeConstant.BIHIN_CLENER:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("掃除機");
	// // setValue.setBihinState(dto.getBihinState14());
	// // setValue.setBihinAdjust(dto.getBihinReturn14());
	// // break;
	// // case CodeConstant.BIHIN_RICE_COOKER:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("電子炊飯ジャー");
	// // setValue.setBihinState(dto.getBihinState15());
	// // setValue.setBihinAdjust(dto.getBihinReturn15());
	// // break;
	// // case CodeConstant.BIHIN_TV:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("テレビ");
	// // setValue.setBihinState(dto.getBihinState16());
	// // setValue.setBihinAdjust(dto.getBihinReturn16());
	// // break;
	// // case CodeConstant.BIHIN_TV_STANDS:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("テレビ台");
	// // setValue.setBihinState(dto.getBihinState17());
	// // setValue.setBihinAdjust(dto.getBihinReturn17());
	// // break;
	// // case CodeConstant.BIHIN_KOTATSU:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("座卓（こたつ）");
	// // setValue.setBihinState(dto.getBihinState18());
	// // setValue.setBihinAdjust(dto.getBihinReturn18());
	// // break;
	// // case CodeConstant.BIHIN_KICHEN_CABINET:
	// // setValue.setBihinCd(bihinCd);
	// // setValue.setBihinName("キッチンキャビネット");
	// // setValue.setBihinState(dto.getBihinState19());
	// // setValue.setBihinAdjust(dto.getBihinReturn19());
	// // break;
	//
	// return setValue;
	//
	// }

	/**
	 * 差戻し、修正依頼以外のメール送付
	 * 
	 * 
	 * @param dto
	 * @param fromTeijiButton 提示ボタンから来たかフラグ
	 * @throws Exception
	 */
	public void sendMail(Skf2040Sc002CommonDto dto, boolean fromTeijiButton) throws Exception {

		// メール送信
		Map<String, String> applInfo = new HashMap<String, String>();
		applInfo.put("applNo", dto.getApplNo());
		applInfo.put("applId", dto.getApplId());
		applInfo.put("applShainNo", dto.getShainNo());

		// URL
		String urlBase = "skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
		// コメント
		String commentNote = dto.getCommentNote();
		// 送付者
		String sendUser = CodeConstant.DOUBLE_QUOTATION;
		// メール区分
		String mailKbn = CodeConstant.DOUBLE_QUOTATION;
		// 案内
		String annai = CodeConstant.DOUBLE_QUOTATION;

		if (CodeConstant.SHONIN_KANRYO_TSUCHI.equals(dto.getMailKbn())) {
			// 承認完了通知の場合
			// メール区分が承認完了通知だった場合は送信先の社員番号を設定
			mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
			sendUser = dto.getShainNo();

		} else if (fromTeijiButton) {
			// 提示ボタンからの処理の場合
			mailKbn = CodeConstant.BIHIN_HENKYAKU_ANNAI;
			sendUser = dto.getShainNo();
		}

		skfMailUtils.sendApplTsuchiMail(mailKbn, applInfo, commentNote, annai, sendUser, CodeConstant.DOUBLE_QUOTATION,
				urlBase);

	}

}
