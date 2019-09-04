/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuRoomBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuRoomBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiBihinDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiBihinDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinseiKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetHenkyakuBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetShatakuRoomBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiBihinDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc002common.Skf2040Sc002CommonDto;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002InitDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002SharedService {

	private MenuScopeSessionBean menuScopeSessionBean;
	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private Skf2040Sc002GetTeijiDataInfoExpRepository skf2040Sc002GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2040Sc002GetTeijiBihinDataExpRepository skf2040Sc002GetTeijiBihinDataExpRepository;
	@Autowired
	private Skf2040Sc002GetShatakuRoomBihinDataExpRepository skf2040Sc002GetShatakuRoomBihinDataExpRepository;
	@Autowired
	Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	Skf2040Sc002GetHenkyakuBihinInfoExpRepository skf2040Sc002GetHenkyakuBihinInfoExpRepository;
	@Autowired
	Skf2040Sc002GetShatakuInfoExpRepository skf2040Sc002GetShatakuInfoExpRepository;

	private Map<String, String> bihinStatusMap;// 備品状態
	private Map<String, String> bihinReturnMap;// 備品返却

	/**
	 * セッション情報を取得
	 * 
	 * @param bean
	 */
	public void setMenuScopeSessionBean(MenuScopeSessionBean bean) {
		menuScopeSessionBean = bean;
	}

	/**
	 * セッションの添付資料情報の初期化
	 * 
	 * @param bean
	 */
	public void clearMenuScopeSessionBean() {
		if (menuScopeSessionBean == null) {
			return;
		}
		skfAttachedFileUtiles.clearAttachedFileBySessionData(menuScopeSessionBean,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
	}

	/**
	 * 添付資料データを設定します
	 * 
	 * @param fileName
	 * @param file
	 * @param fileSize
	 */
	@SuppressWarnings({ "static-access" })
	protected void addAttachedFile(String fileName, byte[] file, String fileSize, int attachedNo,
			List<Map<String, Object>> shatakuAttachedFileList) {
		// 添付資料のコレクションをSessionより取得

		// リンクリストチェック
		boolean findFlg = false;
		if (shatakuAttachedFileList != null) {
			for (Map<String, Object> attachedFileMap : shatakuAttachedFileList) {
				if (fileName.equals(attachedFileMap.get("attachedName"))) {
					findFlg = true;
					break;
				}
			}
		} else {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		// 添付ファイルリストに無い場合
		if (!findFlg) {
			Map<String, Object> addAttachedFileInfo = new HashMap<String, Object>();

			addAttachedFileInfo.put("attachedNo", attachedNo);

			// 添付資料名
			addAttachedFileInfo.put("attachedName", fileName);
			// ファイルサイズ
			addAttachedFileInfo.put("attachedFileSize", fileSize);
			// 更新日
			addAttachedFileInfo.put("registDate", new Date());
			// 添付資料
			addAttachedFileInfo.put("fileStream", file);
			// 添付ファイルステータス
			// ファイルタイプ
			addAttachedFileInfo.put("fileType", skfAttachedFileUtiles.getFileTypeInfo(fileName));

			shatakuAttachedFileList.add(addAttachedFileInfo);
		}

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
		long teijiNo = CodeConstant.LONG_ZERO;
		if (teijiDataInfo == null) {
			// 提示データの取得ができない場合、リストは空で返す
			return henkyakuList;
		} else {
			teijiNo = teijiDataInfo.getTeijiNo();
		}

		// 備品提示データリストの取得
		List<Skf2040Sc002GetTeijiBihinDataExp> dtTeijiBihin = new ArrayList<Skf2040Sc002GetTeijiBihinDataExp>();
		Skf2040Sc002GetTeijiBihinDataExpParameter teijiBihinParam = new Skf2040Sc002GetTeijiBihinDataExpParameter();
		teijiBihinParam.setTeijiNo(teijiNo);
		dtTeijiBihin = skf2040Sc002GetTeijiBihinDataExpRepository.getTeijiBihinData(teijiBihinParam);

		// 社宅部屋備品情報の取得
		List<Skf2040Sc002GetShatakuRoomBihinDataExp> dtShatakuRoomBihin = new ArrayList<Skf2040Sc002GetShatakuRoomBihinDataExp>();
		Skf2040Sc002GetShatakuRoomBihinDataExpParameter dtShatakuRoomBihinParam = new Skf2040Sc002GetShatakuRoomBihinDataExpParameter();
		dtShatakuRoomBihinParam.setShatakuKanriNo(shatakuNo);
		dtShatakuRoomBihinParam.setShatakuRoomKanriNo(roomNo);
		dtShatakuRoomBihin = skf2040Sc002GetShatakuRoomBihinDataExpRepository
				.getShatakuRoomBihinData(dtShatakuRoomBihinParam);

		bihinStatusMap = skfGenericCodeUtils.getGenericCode("SKF1051"); // 備品状態
		bihinReturnMap = skfGenericCodeUtils.getGenericCode("SKF1049"); // 備品返却

		// mapに返却備品情報を設定
		Map<String, Object> bihinInfoMap = new HashMap<String, Object>();

		// 備品提示データリストとの比較
		for (Skf2040Sc002GetTeijiBihinDataExp teijiBihin : dtTeijiBihin) {

		}

		// 備品申請情報をレイアウトに合うように整形
		for (

		Map<String, Object> bihinInfo : henkyakuList) {
			setBihinData(dto, bihinInfo);
		}

		return null;

	}

	/**
	 * それぞれの備品に合うようにdtoの変数に値を割り当てます
	 * 
	 * @param dto
	 * @param bihinInfo
	 */
	private void setBihinData(Skf2040Sc002InitDto dto, Map<String, Object> bihinInfo) {

		// // String bihinCd = bihinInfo.getBihinCd();
		// // String bihinWish = bihinInfo
		// // String bihinAppl = bihinInfo.getBihinAppl();
		//
		// boolean btnDisabled = false;
		// //
		// // String bihinStateText =
		// bihinInfoMap.get(bihinInfo.getBihinState());
		// // String bihinApplText = bihinApplMap.get(bihinAppl);
		// // String bihinAdjustText =
		// // bihinAdjustMap.get(bihinInfo.getBihinAdjust());
		//
		// switch (bihinCd) {
		// case CodeConstant.BIHIN_WASHER:
		// dto.setBihinState11(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_FREEZER:
		// dto.setBihinState12(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_OVEN:
		// dto.setBihinState13(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_CLENER:
		// dto.setBihinState14(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_RICE_COOKER:
		// dto.setBihinState15(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_TV:
		// dto.setBihinState16(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_TV_STANDS:
		// dto.setBihinState17(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_KOTATSU:
		// dto.setBihinState18(bihinStateText);
		//
		// break;
		// case CodeConstant.BIHIN_KICHEN_CABINET:
		// dto.setBihinState19(bihinStateText);
		//
		// break;
		// }
		return;

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

	private String useStreamTeijiBihin(String bihinCd, List<Skf2040Sc002GetTeijiBihinDataExp> dtTeijiBihin) {
		return "false";
		// return dtTeijiBihin.stream().filter(v ->
		// v.equals(bihinCd)).findFirst().orElse("");

	}

	/**
	 * 退居届の情報取得
	 * 
	 * @param 退居届の申請書類管理番号
	 * @return Skf2040TTaikyoReport
	 */
	public Skf2040TTaikyoReport getTaikyoReport(String applNo) {

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
	public Skf2050TBihinHenkyakuShinsei getBihinHenkyakuShinsei(String applNo) {

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
	 * @return
	 */
	public List<Skf2040Sc002GetHenkyakuBihinInfoExp> getHenkyakuBihinInfo(String applNo, String shainNo, long shatakuNo,
			long roomNo) {

		List<Skf2040Sc002GetHenkyakuBihinInfoExp> henkyakuDt = new ArrayList<Skf2040Sc002GetHenkyakuBihinInfoExp>();
		Skf2040Sc002GetHenkyakuBihinInfoExpParameter henkyakuDtParam = new Skf2040Sc002GetHenkyakuBihinInfoExpParameter();
		henkyakuDtParam.setApplNo(applNo);
		henkyakuDtParam.setShainNo(shainNo);
		henkyakuDtParam.setNyutaikyoKbn(CodeConstant.SYS_TAIKYO_KBN);
		henkyakuDtParam.setShatakuKanriNo(shatakuNo);
		henkyakuDtParam.setShatakuRoomKanriNo(roomNo);
		henkyakuDt = skf2040Sc002GetHenkyakuBihinInfoExpRepository.getHenkyakuBihinInfo(henkyakuDtParam);

		return henkyakuDt;
	}

	/**
	 * 備品返却時の表示項目
	 * 
	 * @param initDto
	 * @param taikyoRepDt
	 */
	public void setBihinHenkyakuDisp(Skf2040Sc002InitDto initDto, Skf2040TTaikyoReport taikyoRepDt) {

		// 返却立会希望日（日）の取得
		String sessionDay = CodeConstant.DOUBLE_QUOTATION;
		if (NfwStringUtils.isEmpty(taikyoRepDt.getSessionDay())) {
			sessionDay = taikyoRepDt.getSessionDay();
		}
		// 返却立会希望日（時）の取得
		String sessionTime = CodeConstant.DOUBLE_QUOTATION;
		if (NfwStringUtils.isEmpty(taikyoRepDt.getSessionTime())) {
			sessionTime = taikyoRepDt.getSessionTime();
			// 取得したコードから表示内容を取得
			List<Map<String, Object>> henkyakukiboTime = skfDropDownUtils
					.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, sessionTime, false);
			if (henkyakukiboTime.size() > 0) {
				sessionTime = henkyakukiboTime.toString();
			}
		}
		// 返却立会希望日時の表示
		initDto.setSessionDay(sessionDay + CodeConstant.SPACE_CHAR + sessionTime);

		// 連絡先の表示
		if (NfwStringUtils.isEmpty(taikyoRepDt.getRenrakuSaki())) {
			initDto.setRenrakuSaki(taikyoRepDt.getRenrakuSaki());
		}

	}

	/**
	 * ボタンの表示制御 true：表示、false：非表示
	 * 
	 * @param PresentBtn 提示ボタン
	 * @param approveBtn 承認ボタン
	 * @param revisionBtn 修正依頼ボタン
	 * @param remandBtn 差戻しボタン
	 * @param shiryoBtn 添付資料ボタン
	 * @param pdfDwnBtn pdfダウンロードボタン
	 * @param initDto
	 *
	 */
	public void setButtonVisible(boolean PresentBtn, boolean approveBtn, boolean revisionBtn, boolean remandBtn,
			boolean shiryoBtn, boolean pdfDwnBtn, Skf2040Sc002CommonDto dto) {

		dto.setPresentBtnViewFlag(PresentBtn); // 提示ボタン
		dto.setApproveBtnViewFlag(approveBtn); // 承認ボタン
		dto.setRevisionBtnViewFlag(revisionBtn); // 修正依頼ボタン
		dto.setRemandBtnViewFlag(remandBtn); // 差戻しボタン
		dto.setShiryoBtnViewFlag(shiryoBtn); // 添付資料ボタン
		dto.setTaikyoPdfViewBtnFlag(pdfDwnBtn);// pdfダウンロードボタン
	}

	/**
	 * 退居届帳票イメージの情報設定
	 * 
	 * @param initDto
	 * @param taikyoRepDt
	 * @param shatakuInfo
	 */
	public void setReportInfo(Skf2040Sc002CommonDto dto, Skf2040TTaikyoReport taikyoRepDt,
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
		String applDateText = skfDateFormatUtils.dateFormatFromString(applDate, "yyyy年MM月dd日");
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

		// 自動車の保管場所返還取消線フラグ
		// 社宅
		dto.setShatakuName(shatakuInfo.getShatakuName());
		// 駐車場1
		dto.setParkingAddress1(shatakuInfo.getParkingAddress1());
		// 駐車場2
		dto.setParkingAddress2(shatakuInfo.getParkingAddress2());
		// 退居日 社宅等
		// 退居日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDate()))) {
			dto.setTaikyoDate(skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(), "yyyy年MM月dd日"));
			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getTaikyoDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getTaikyoDateFlg())) {

				String taikyoDate = skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(), "yyyy年MM月dd日");
				// fontColorタグ設定
				taikyoDate = sfontColor + taikyoDate + eFontColoor;
				dto.setTaikyoDate(taikyoDate);
			}
		}
		// 駐車場返還日
		if ((NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingHenkanDate()))) {
			dto.setParkingHenkanDate(
					skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getParkingHenkanDate(), "yyyy年MM月dd日"));
			// 日付変更フラグが1:変更ありなら赤文字にする
			if (NfwStringUtils.isNotEmpty(taikyoRepDt.getParkingEDateFlg())
					&& SkfCommonConstant.DATE_CHANGE.equals(taikyoRepDt.getParkingEDateFlg())) {

				String parkingHenkanDate = skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getParkingHenkanDate(),
						"yyyy年MM月dd日");
				// fontColorタグ設定
				parkingHenkanDate = sfontColor + parkingHenkanDate + eFontColoor;
				dto.setParkingHenkanDate(parkingHenkanDate);
			}
		} else {
			// 駐車場返還日がない場合は、退居日を設定
			dto.setParkingHenkanDate(
					skfDateFormatUtils.dateFormatFromString(taikyoRepDt.getTaikyoDate(), "yyyy年MM月dd日"));
		}
		// 退居（返還）理由
		Map<String, String> taikyoRiyuMap = skfGenericCodeUtils
				.getGenericCode(FunctionIdConstant.GENERIC_CODE_TAIKYO_HENKAN_RIYU);
		String taikyoRiyu = "";
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
	 * @param initDto
	 */
	public void refreshHeaderAttachedFile(Skf2040Sc002CommonDto dto) {

		List<Map<String, Object>> tmpAttachedFileList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();

		// 添付資料がある場合、添付資料表示処理を行う
		if (NfwStringUtils.isNotEmpty(dto.getApplTacFlg()) && SkfCommonConstant.AVAILABLE.equals(dto.getApplTacFlg())) {

			// 添付ファイル情報の取得
			tmpAttachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean, dto.getApplNo(),
					SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
			if (tmpAttachedFileList != null && tmpAttachedFileList.size() > 0) {
				int defaultAttachedNo = 0;
				for (Map<String, Object> tmpAttachedFileMap : tmpAttachedFileList) {
					// skfAttachedFileUtiles.addShatakuAttachedFile(tmpAttachedFileMap.get("attachedName").toString(),
					// (byte[]) tmpAttachedFileMap.get("fileStream"),
					// tmpAttachedFileMap.get("fileSize").toString(),
					// defaultAttachedNo, attachedFileList);
				}
			}
		}
		// 添付ファイル情報設定
		dto.setAttachedFileList(attachedFileList);
	}

	/**
	 * 社宅情報を取得します
	 * 
	 * @param shatakuKanriNo
	 * @param shainNo
	 * @return
	 */
	public Skf2040Sc002GetShatakuInfoExp getShatakuInfo(Long shatakuKanriNo, String shainNo) {
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
}
