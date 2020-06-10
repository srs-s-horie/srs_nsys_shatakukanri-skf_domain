/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetApplInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetApplInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinHenkyakuApplNoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinHenkyakuApplNoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetShatakuKanriIdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShatakuInfoUtils.SkfShatakuInfoUtilsGetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShatakuInfoUtils.SkfShatakuInfoUtilsGetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetApplInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinHenkyakuApplNoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetShatakuKanriIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfShatakuInfoUtils.SkfShatakuInfoUtilsGetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.core.constants.CommonConstant;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc001common.Skf2040Sc001CommonDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001SharedService {

	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	@Autowired
	// 退居届リポジトリ
	private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
	@Autowired
	// 申請書類履歴リポジトリ
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2040Sc001GetShainInfoExpRepository skf2040Sc001GetShainInfoExpRepository;
	@Autowired
	private Skf2040Sc001GetApplInfoExpRepository skf2040Sc001GetApplInfoExpRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	private Skf2040Sc001GetBihinHenkyakuApplNoInfoExpRepository skf2040Sc001GetBihinHenkyakuApplNoInfoExpRepository;
	@Autowired
	private Skf2040Sc001GetShatakuKanriIdExpRepository skf2040Sc001GetShatakuKanriIdExpRepository;
	@Autowired
	private Skf2040Sc001UpdateApplHistoryAgreeStatusExpRepository skf2040Sc001UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private SkfShatakuInfoUtilsGetShatakuInfoExpRepository skfShatakuInfoUtilsGetShatakuInfoExpRepository;

	private static final String SHATAKU_CHECKED = "shataku_checked";
	private static final String PARK1_CHECKED = "park1_checked";
	private static final String PARK2_CHECKED = "park2_checked";

	/**
	 * 退居（返還）理由ドロップダウンリスト取得
	 * 
	 * @param defaultSelectVal 初期選択値 ※汎用コードSKF1142のコード値を指定すること
	 * @return List<Map<String, Object>> 退居（返還）理由ドロップダウンリスト
	 */
	public List<Map<String, Object>> getTaikyoHenkanRiyuList(String defaultSelectCdVal) {
		// 退居（返還）理由プルダウン取得
		return skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TAIKYO_HENKAN_RIYU,
				defaultSelectCdVal, true);
	}

	/**
	 * 現住居社宅ドロップダウンリスト取得
	 * 
	 * @param shainNo 対象社員番号
	 * @param defaultSelectShatakuId 初期選択値 社宅管理IDを指定する事
	 * @return List<Map<String, Object>> 現住居社宅ドロップダウンリスト
	 */
	public List<Map<String, Object>> getNowShatakuNameList(String shainNo, long defaultSelectShatakuId) {
		// システム日付
		String sysDateYyyyMMdd = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
		return skfDropDownUtils.getDdlNowShatakuNameByCd(shainNo, sysDateYyyyMMdd, defaultSelectShatakuId, false);
	}

	/**
	 * 返却立会希望日(時)ドロップダウンリスト取得
	 * 
	 * @param defaultSelectTime 初期選択値
	 * @return List<Map<String, Object>> 返却立会希望日(時)ドロップダウンリスト
	 */
	public List<Map<String, Object>> getSessionTimeList(String defaultSelectTime) {
		// 返却立会希望日(時)ドロップダウンリストの設定
		return skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_REQUEST_TIME,
				defaultSelectTime, false);
	}

	/**
	 * 引数で指定された会社コード、申請書類管理番号に該当する退居届情報を取得する。
	 * 
	 * @param companyCd 会社コード
	 * @param applNo 申請書類管理番号
	 * @return 退居届情報
	 */
	public Skf2040TTaikyoReport getTaikyoInfo(String companyCd, String applNo) {
		Skf2040TTaikyoReportKey key = new Skf2040TTaikyoReportKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(applNo);
		return skf2040TTaikyoReportRepository.selectByPrimaryKey(key);
	}

	/**
	 * 引数で指定された退居届エンティティの情報をdtoに設定する。
	 * 
	 * @param dto 設定対象のDTO
	 * @param taikyoInfo 退居届情報
	 */
	private <DTO extends Skf2040Sc001CommonDto> void convTaikyoEntityToDto(DTO dto, Skf2040TTaikyoReport taikyoInfo) {
		dto.setShainNo(taikyoInfo.getShainNo());
		dto.setAgencyName(taikyoInfo.getAgency());
		dto.setAffiliation1Name(taikyoInfo.getAffiliation1());
		dto.setAffiliation2Name(taikyoInfo.getAffiliation2());
		dto.setShatakuNo(taikyoInfo.getShatakuNo());
		dto.setShatakuName(taikyoInfo.getTaikyoArea());
		dto.setHdnNowShatakuRoomKanriNo(String.valueOf(taikyoInfo.getRoomKanriNo()));
		dto.setNowAddress(taikyoInfo.getAddress());
		dto.setName(taikyoInfo.getName());

		// 性別
		dto.setGender(taikyoInfo.getGender());
		if(taikyoInfo.getGender() != null){
			switch (taikyoInfo.getGender()) {
			case CodeConstant.MALE:
				dto.setGenderName(CodeConstant.OUTPUT_MALE);
				break;
			case CodeConstant.FEMALE:
				dto.setGenderName(CodeConstant.OUTPUT_FEMALE);
				break;
			default:
				break;
			}
		}

		dto.setParking1stPlace(taikyoInfo.getParkingAddress1());
		dto.setParking2ndPlace(taikyoInfo.getParkingAddress2());
		dto.setTaikyoHenkanDate(taikyoInfo.getTaikyoDate());

		// 退居種別
		String taikyoShatakuCheckedStr = CommonConstant.C_EMPTY;
		String taikyoParking1CheckedStr = CommonConstant.C_EMPTY;
		String taikyoParking2CheckedStr = CommonConstant.C_EMPTY;

		if ("1".equals(taikyoInfo.getTaikyoShataku())) {
			taikyoShatakuCheckedStr = SHATAKU_CHECKED;
		}
		if ("1".equals(taikyoInfo.getTaikyoParking1())) {
			taikyoParking1CheckedStr = PARK1_CHECKED;
		}
		if ("1".equals(taikyoInfo.getTaikyoParking2())) {
			taikyoParking2CheckedStr = PARK2_CHECKED;
		}
		String[] taikyoTypeArray = new String[] { taikyoShatakuCheckedStr, taikyoParking1CheckedStr,
				taikyoParking2CheckedStr };
		dto.setTaikyoType(taikyoTypeArray);

		dto.setTaikyoRiyuKbn(taikyoInfo.getTaikyoRiyuKbn());
		if (CodeConstant.TAIKYO_RIYU_OTHERS.equals(dto.getTaikyoRiyuKbn())) {
			// 退居返還理由が「その他」である場合のみ退居理由をdtoに設定
			dto.setTaikyoHenkanRiyu(taikyoInfo.getTaikyoRiyu());
		}
		dto.setShatakuJotai(taikyoInfo.getShatakuJotai());
		dto.setTaikyogoRenrakuSaki(taikyoInfo.getTaikyogoRenrakusaki());
		dto.setSessionDay(taikyoInfo.getSessionDay());
		dto.setSessionTime(taikyoInfo.getSessionTime());
		dto.setRenrakuSaki(taikyoInfo.getRenrakuSaki());

		// 排他チェック用更新日付
		dto.addLastUpdateDate(dto.UPDATE_TABLE_PREFIX_TAIKYO_REPORT + taikyoInfo.getApplNo(),
				taikyoInfo.getUpdateDate());
	}

	/**
	 * 引数で指定されたDtoが保持している申請書類管理番号をもとに、 既存の退居届情報を取得する。
	 * 
	 * @param applNo 申請書類管理番号
	 * @return 退居届エンティティ
	 */
	public Skf2040TTaikyoReport getExistTaikyoInfo(String applNo) {
		Skf2040TTaikyoReport taikyoInfo = this.getTaikyoInfo(CodeConstant.C001, applNo);
		if (taikyoInfo == null) {
			LogUtils.debugByMsg("該当する退居届情報が存在しません。");
			return null;
		}
		return taikyoInfo;
	}

	/**
	 * 引数で指定された社宅管理ID、社員番号に該当する社宅情報エンティティリストを取得する。
	 * 
	 * @param shatakuKanriId
	 * @param shainNo
	 * @return List<SkfShatakuInfoUtilsGetShatakuInfoExp>
	 */
	protected List<SkfShatakuInfoUtilsGetShatakuInfoExp> getShatakuInfo(long shatakuKanriId, String shainNo) {
		List<SkfShatakuInfoUtilsGetShatakuInfoExp> shatakuList = new ArrayList<SkfShatakuInfoUtilsGetShatakuInfoExp>();
		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

		// データの取得
		SkfShatakuInfoUtilsGetShatakuInfoExpParameter param = new SkfShatakuInfoUtilsGetShatakuInfoExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(yearMonth);
		param.setShainNo(shainNo);
		shatakuList = skfShatakuInfoUtilsGetShatakuInfoExpRepository.getShatakuInfo(param);

		return shatakuList;
	}

	/**
	 * 引数で指定された会社コード、社員番号に該当する社員情報エンティティを取得する。
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @return 社員情報エンティティ
	 */
	public Skf2040Sc001GetShainInfoExp getShainInfo(String companyCd, String shainNo) {

		// 社員情報情報取得
		List<Skf2040Sc001GetShainInfoExp> resultList = new ArrayList<Skf2040Sc001GetShainInfoExp>();
		// DB検索処理
		Skf2040Sc001GetShainInfoExpParameter param = new Skf2040Sc001GetShainInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setShainNo(shainNo);
		resultList = skf2040Sc001GetShainInfoExpRepository.getShainInfo(param);

		if (resultList != null && resultList.size() > 0) {
			// 検索結果の1件目を取得
			return resultList.get(0);
		} else {
			// 取得できなかった場合nullを返す
			return null;
		}
	}

	/**
	 * 引数で指定された社宅情報エンティティの内容を、引数で指定されたDTOに設定する。
	 * 
	 * @param dto 退居届DTO
	 * @param shatakuInfo 現有社宅情報を保持した社宅情報エンティティ
	 */
	public <DTO extends Skf2040Sc001CommonDto> void setShatakuInfo(DTO dto,
			SkfShatakuInfoUtilsGetShatakuInfoExp shatakuInfo) {
		// 社宅名
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getShatakuName())) {
			dto.setHdnSelectedNowShatakuName(shatakuInfo.getShatakuName());
			LogUtils.debugByMsg("社宅名" + dto.getHdnSelectedNowShatakuName());
		}

		// 社宅住所
		// 都道府県名取得
		String prefName = CodeConstant.NONE;
		if(NfwStringUtils.isNotEmpty(shatakuInfo.getPrefCd())){
			prefName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_PREFCD, shatakuInfo.getPrefCd());
		}
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getAddress())) {
			dto.setNowAddress(prefName + shatakuInfo.getAddress());
			LogUtils.debugByMsg("社宅住所" + prefName + shatakuInfo.getAddress());
		}

		// 室番号
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getRoomNo())) {
			dto.setHdnNowShatakuRoomNo(shatakuInfo.getRoomNo());
			LogUtils.debugByMsg("室番号" + shatakuInfo.getRoomNo());
		}
		// 規格(間取り)
		// 規格があった場合は、貸与規格。それ以外は本来規格
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getKikaku())) {
			dto.setNowShatakuKikaku(shatakuInfo.getKikaku());// 貸与規格
			dto.setNowShatakuKikakuName(shatakuInfo.getKikakuName());
			LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
			LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
		} else {
			if (NfwStringUtils.isNotEmpty(shatakuInfo.getOriginalKikaku())) {
				dto.setNowShatakuKikaku(shatakuInfo.getOriginalKikaku());// 本来規格
				dto.setNowShatakuKikakuName(shatakuInfo.getOriginalKikakuName());
				LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
				LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
			}
		}

		// 面積
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getLendMenseki())) {
			dto.setNowShatakuMenseki(shatakuInfo.getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
			LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
		}

		// 駐車場 都道府県コード（保有社宅のみ設定される）
		String wkPrefName = CodeConstant.DOUBLE_QUOTATION;
		String prefCode = CodeConstant.DOUBLE_QUOTATION;
		// 取得できたら汎用コードマスタから名称を取得
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getPrefCdParking())) {
			prefCode = shatakuInfo.getPrefCdParking();
			wkPrefName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_PREFCD, prefCode);
		}

		// 駐車場 １台目 保管場所
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress1())) {
			dto.setParking1stPlace(wkPrefName + shatakuInfo.getParkingAddress1());
			LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());
		} else {
			// 駐車場１を借りていない場合は「駐車場１を返却する」チェックを非活性とする
			dto.setNowParking1TaikyoDisabled("true");
		}

		// 駐車場 １台目 位置番号
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingBlock1())) {
			dto.setHdnParking1stNumber(shatakuInfo.getParkingBlock1());
			LogUtils.debugByMsg("駐車場 １台目 位置番号" + dto.getHdnParking1stNumber());
		}

		// 駐車場 ２台目 保管場所
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress2())) {
			dto.setParking2ndPlace(wkPrefName + shatakuInfo.getParkingAddress2());
			LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2ndPlace());
		} else {
			// 駐車場２を借りていない場合は「駐車場２を返却する」チェックを非活性とする
			dto.setNowParking2TaikyoDisabled("true");
		}

		// 駐車場 ２台目 位置番号
		if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingBlock2())) {
			dto.setHdnParking2ndNumber(shatakuInfo.getParkingBlock2());
			LogUtils.debugByMsg("駐車場 2台目 位置番号" + dto.getHdnParking2ndNumber());
		}

		// 現在の社宅管理番号
		if (shatakuInfo.getShatakuKanriNo() != null) {
			dto.setHdnNowShatakuKanriNo(String.valueOf(shatakuInfo.getShatakuKanriNo()));
		}

		// 現在の部屋管理番号
		if (shatakuInfo.getShatakuRoomKanriNo() != null) {
			dto.setHdnNowShatakuRoomKanriNo(String.valueOf(shatakuInfo.getShatakuRoomKanriNo()));
		}
	}

	/**
	 * エラー発生時などに退居届画面のボタンを非活性とし、後続処理を行えないようにする。
	 * 
	 * @param dto
	 */
	public <DTO extends Skf2040Sc001CommonDto> void setDisableBtn(DTO dto) {
		// 確認ボタン非活性
		dto.setBtnCheckDisabled(true);
		// 一時保存ボタン非活性
		dto.setBtnSaveDisabled(true);
		// クリアボタン非表示
		dto.setBtnClearRemoved(true);
	}

	/**
	 * 引数で指定されたDtoが保持している申請書類管理番号をもとに、 既存の退居届情報を取得してDtoに設定する。
	 * 
	 * @param dto 申請書類管理番号を保持したDto
	 */
	public <DTO extends Skf2040Sc001CommonDto> void setExistTaikyoInfo(DTO dto) {
		Skf2040TTaikyoReport taikyoInfo = this.getTaikyoInfo(CodeConstant.C001, dto.getApplNo());
		if (taikyoInfo == null) {
			// 退居届情報の取得に失敗した場合はエラーメッセージを表示してボタンを使用不可にする
			this.setDisableBtn(dto);
			LogUtils.debugByMsg("該当する退居届情報が存在しません。");
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return;
		}
		this.convTaikyoEntityToDto(dto, taikyoInfo);

		// 選択した社宅の社宅管理IDを取得
		Skf2040Sc001GetShatakuKanriIdExpParameter param = new Skf2040Sc001GetShatakuKanriIdExpParameter();
		param.setShainNo(dto.getShainNo());
		param.setShatakuKanriNo(dto.getShatakuNo());
		param.setShatakuRoomKanriNo(Long.parseLong(NfwStringUtils.defaultString(dto.getHdnNowShatakuRoomKanriNo())));
		String shatakuKanriId = skf2040Sc001GetShatakuKanriIdExpRepository.getShatakuKanriId(param);
		if (!NfwStringUtils.isEmpty(shatakuKanriId)) {
			dto.setShatakuKanriId(Long.parseLong(shatakuKanriId));
			dto.setNowShatakuName(shatakuKanriId);
		}
	}

	/**
	 * 申請書情報の取得
	 * 
	 * @param dto
	 */
	protected <DTO extends Skf2040Sc001CommonDto> void setSkfApplInfo(DTO dto) {
		// 申請情報情報
		Skf2040Sc001GetApplInfoExp applInfo = new Skf2040Sc001GetApplInfoExp();
		Skf2040Sc001GetApplInfoExpParameter applParam = new Skf2040Sc001GetApplInfoExpParameter();
		applParam.setCompanyCd(CodeConstant.C001);
		applParam.setShainNo(dto.getShainNo());
		applParam.setApplNo(dto.getApplNo());
		applParam.setApplId(FunctionIdConstant.R0103);
		applInfo = skf2040Sc001GetApplInfoExpRepository.getApplInfo(applParam);
		if (applInfo != null) {
			// 申請書ID
			dto.setApplId(applInfo.getApplId());
			// 申請ステータス
			dto.setApplStatus(applInfo.getStatus());
			// 添付ファイル有無フラグ
			dto.setApplTacFlg(Integer.parseInt(applInfo.getApplTacFlg()));
			// 排他チェック用更新日付
			dto.addLastUpdateDate(dto.UPDATE_TABLE_PREFIX_APPL_HIST + applInfo.getApplNo(), applInfo.getUpdateDate());
		} else {
			// 申請ステータス
			dto.setApplStatus(CodeConstant.STATUS_MISAKUSEI);
		}

		// 添付ファイルの有無
		Map<String, String> applTacFlgMap = skfShinseiUtils.getApplAttachFlg(dto.getShainNo(), dto.getApplNo());
		if (applTacFlgMap != null && SkfCommonConstant.AVAILABLE.equals(applTacFlgMap.get("applTacFlg"))) {
			dto.setApplTacFlg(Integer.parseInt(SkfCommonConstant.AVAILABLE));
		} else {
			dto.setApplTacFlg(Integer.parseInt(SkfCommonConstant.UNAVAILABLE));
		}
		return;
	}

	/**
	 * バイト数カット処理
	 * 
	 * <pre>
	 * 引数で与えられたDtoの要素を既定のバイト数でカットしてセットし直す。
	 * </pre>
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	public <DTO extends Skf2040Sc001CommonDto> void cutByte(DTO dto) throws UnsupportedEncodingException {
		String Msg = "バイト数カット処理：　";
		// 退居(返還)日
		dto.setTaikyoHenkanDate(NfwStringUtils.rightTrimbyByte(dto.getTaikyoHenkanDate(), 8));
		LogUtils.debugByMsg(Msg + "退居(返還)日" + dto.getTaikyoHenkanDate());
		// 退居(返還)理由
		dto.setTaikyoHenkanRiyu(NfwStringUtils.rightTrimbyByte(dto.getTaikyoHenkanRiyu(), 256));
		LogUtils.debugByMsg(Msg + "退居(返還)理由" + dto.getTaikyoHenkanRiyu());
		// 社宅の状態
		dto.setShatakuJotai(NfwStringUtils.rightTrimbyByte(dto.getShatakuJotai(), 128));
		LogUtils.debugByMsg(Msg + "社宅の状態" + dto.getShatakuJotai());
		// 退居後の連絡先
		dto.setTaikyogoRenrakuSaki(NfwStringUtils.rightTrimbyByte(dto.getTaikyogoRenrakuSaki(), 128));
		LogUtils.debugByMsg(Msg + "退居後の連絡先" + dto.getTaikyogoRenrakuSaki());
		// 返却立合希望日（日）
		dto.setSessionDay(NfwStringUtils.rightTrimbyByte(dto.getSessionDay(), 8));
		LogUtils.debugByMsg(Msg + "返却立合希望日（日）" + dto.getSessionDay());
		// 連絡先
		dto.setRenrakuSaki(NfwStringUtils.rightTrimbyByte(dto.getRenrakuSaki(), 13));
		LogUtils.debugByMsg(Msg + "連絡先" + dto.getRenrakuSaki());
	}

	/**
	 * 退居届情報の新規保存処理
	 * 
	 * @param dto 保存する退居届情報を保持したDTO
	 * @return 登録成功 true 失敗 false
	 */
	protected <DTO extends Skf2040Sc001CommonDto> boolean saveNewTaikyoData(DTO dto) {
		// 申請書類管理番号を取得
		String newApplNo = skfShinseiUtils.getApplNo(CodeConstant.C001, dto.getShainNo(), FunctionIdConstant.R0103);
		LogUtils.debugByMsg("申請書類管理番号" + newApplNo);
		// 取得に失敗した場合
		if (newApplNo == null) {
			// エラーメッセージを表示用に設定
			ServiceHelper.addResultMessage(dto, null, MessageIdConstant.E_SKF_1094);
			// 保存処理を終了
			return false;
		}
		// 新規裁判した申請書類管理番号をDTOに設定
		dto.setApplNo(newApplNo);
		// ステータスを更新（一時保存）
		dto.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);

		// 登録時のシステム日付を取得
		dto.setApplDate(DateUtils.getSysDate());
		// 申請書類履歴テーブル登録処理
		this.insertApplHistory(dto);
		// 退居届申請テーブル登録値の設定
		Skf2040TTaikyoReport insertParam = this.makeTaikyoReportParam(dto);
		// 登録
		this.insertTaikyoTodoke(insertParam);
		// 排他チェック用更新日付を更新
		dto.addLastUpdateDate(dto.UPDATE_TABLE_PREFIX_TAIKYO_REPORT + insertParam.getApplNo(),
				insertParam.getUpdateDate());

		return true;
	}

	/**
	 * 退居届情報の更新処理
	 * 
	 * @param dto 保存する退居届情報を保持したDTO
	 * @return 登録成功 true 失敗 false
	 */
	protected <DTO extends Skf2040Sc001CommonDto> boolean updateTaikyoData(DTO dto) {

		// 申請履歴テーブルの更新
		this.updateApplHistoryStatusTempSave(dto, true);

		// 退居届申請テーブル更新値の設定
		Skf2040TTaikyoReport updateParam = this.makeTaikyoReportParam(dto);
		this.updateTaikyoData(updateParam);
		// 排他チェック用更新日付を更新
		dto.addLastUpdateDate(dto.UPDATE_TABLE_PREFIX_TAIKYO_REPORT + updateParam.getApplNo(),
				updateParam.getUpdateDate());
		// dtoのステータスを更新（一時保存）
		dto.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);

		return true;
	}

	/**
	 * 申請書履歴テーブルの更新処理
	 * 
	 * @param dto 更新情報を保持したDTO
	 * @param isUpdateApplDate 申請日付を更新するか否かのフラグ
	 */
	private <DTO extends Skf2040Sc001CommonDto> void updateApplHistoryStatusTempSave(DTO dto,
			boolean isUpdateApplDate) {

		Skf2010TApplHistory setApplValue = new Skf2010TApplHistory();

		// 更新値の設定
		setApplValue.setCompanyCd(CodeConstant.C001);
		setApplValue.setApplNo(dto.getApplNo());
		setApplValue.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
		setApplValue.setApplTacFlg(String.valueOf(dto.getApplTacFlg()));
		if (isUpdateApplDate) {
			setApplValue.setApplDate(DateUtils.getSysDate());
		}
		// 更新
		int resultCnt = 0;
		resultCnt = skf2040Sc001UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setApplValue);
		LogUtils.debugByMsg("申請情報履歴テーブル更新件数：" + resultCnt + "件");
	}

	/**
	 * 引数で指定された退居届エンティティに設定された情報で退居届テーブルの更新を行う。
	 * 
	 * @param taikyoInfo 更新データを格納した退居届エンティティ ※更新条件のPK情報を保持していること
	 */
	private void updateTaikyoData(Skf2040TTaikyoReport taikyoInfo) {
		int updateCount = 0;
		updateCount = skf2040TTaikyoReportRepository.updateByPrimaryKeySelective(taikyoInfo);
		LogUtils.debugByMsg("退居(自動車の保管場所返還)届テーブル登録件数：" + updateCount + "件");
	}

	/**
	 * 引数で指定された退居届DTOの情報をもとに、登録・更新用の退居届エンティティを作成する。
	 * 
	 * @param dto 登録・更新用のデータを保持した退居届DTO
	 * @return 退居届エンティティ
	 */
	private Skf2040TTaikyoReport makeTaikyoReportParam(Skf2040Sc001CommonDto dto) {

		Skf2040TTaikyoReport param = new Skf2040TTaikyoReport();

		boolean isShatakuTaikyoChecked = false;
		boolean isParking1stHenkanChecked = false;
		boolean isParking2ndHenkanChecked = false;

		// 社宅退居のチェック状況を取得する。
		String[] taikyoTypeArray = dto.getTaikyoType();
		if (taikyoTypeArray != null && taikyoTypeArray.length > 0) {
			for (String taikyoType : taikyoTypeArray) {
				if (taikyoType.equals(SHATAKU_CHECKED)) {
					isShatakuTaikyoChecked = true;
				}
				if (taikyoType.equals(PARK1_CHECKED)) {
					isParking1stHenkanChecked = true;
				}
				if (taikyoType.equals(PARK2_CHECKED)) {
					isParking2ndHenkanChecked = true;
				}
			}
		}
		// 会社コード
		param.setCompanyCd(CodeConstant.C001);
		// 申請書類管理番号
		param.setApplNo(dto.getApplNo());
		// 申請日付
		param.setApplDate(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));
		// 社員番号
		param.setShainNo(dto.getShainNo());
		// 機関名
		param.setAgency(dto.getAgencyName());
		// 部等名
		param.setAffiliation1(dto.getAffiliation1Name());
		// 課等名
		param.setAffiliation2(dto.getAffiliation2Name());
		// 現住所
		param.setAddress(dto.getNowAddress());
		// 氏名
		param.setName(dto.getName());
		// 性別
		param.setGender(dto.getGender());
		// 駐車場返還日
		if (isParking1stHenkanChecked || isParking2ndHenkanChecked) {
			// 駐車場返還を行う場合のみ退居返還日を設定する
			param.setParkingHenkanDate(dto.getTaikyoHenkanDate());
		}
		
		// 社宅名＋部屋番号
		param.setTaikyoArea(dto.getHdnSelectedNowShatakuName() + dto.getHdnNowShatakuRoomNo());
		
		// 退居（返還）日
		param.setTaikyoDate(dto.getTaikyoHenkanDate());

		// 退居理由区分
		param.setTaikyoRiyuKbn(dto.getTaikyoRiyuKbn());

		// 退居理由
		if (CodeConstant.TAIKYO_RIYU_OTHERS.equals(dto.getTaikyoRiyuKbn())) {
			// ドロップダウンリストが”その他”選択時：「退居（返還）理由」テキスト
			param.setTaikyoRiyu(dto.getTaikyoHenkanRiyu());
		} else {
			// ドロップダウンリストが”その他”以外を選択時：選択された理由区分のドロップダウンテキスト
			String taikyoRiyuKbnTxt = skfGenericCodeUtils
					.getGenericCodeNameReverse(FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, dto.getTaikyoRiyuKbn());
			param.setTaikyoRiyu(taikyoRiyuKbnTxt);
		}
		// 退居後連絡先
		param.setTaikyogoRenrakusaki(dto.getTaikyogoRenrakuSaki());

		// 退居する社宅区分
		// 社宅退居と駐車場１、２返還の組み合わせによって決まる。
		param.setShatakuTaikyoKbn(this.getShatakuTaikyoKbn1(isShatakuTaikyoChecked, isParking1stHenkanChecked,
				isParking2ndHenkanChecked));

		// 返還する駐車場区分
		// 駐車場１、２返還の組み合わせによって決まる。
		param.setShatakuTaikyoKbn2(this.getShatakuTaikyoKbn2(isParking1stHenkanChecked, isParking2ndHenkanChecked));

		// 社宅退居フラグ
		param.setTaikyoShataku(BooleanUtils.toString(isShatakuTaikyoChecked, "1", "0"));
		// 駐車場１返還フラグ
		param.setTaikyoParking1(BooleanUtils.toString(isParking1stHenkanChecked, "1", "0"));
		// 駐車場２返還フラグ
		param.setTaikyoParking2(BooleanUtils.toString(isParking2ndHenkanChecked, "1", "0"));

		// 社宅管理番号
		param.setShatakuNo(Long.parseLong(NfwStringUtils.defaultString(dto.getHdnNowShatakuKanriNo())));
		// 部屋管理番号
		param.setRoomKanriNo(Long.parseLong(NfwStringUtils.defaultString(dto.getHdnNowShatakuRoomKanriNo())));
		// 社宅状態
		param.setShatakuJotai(dto.getShatakuJotai());
		// 返却立合希望日
		param.setSessionDay(dto.getSessionDay());
		// 返却立合希望日（時間）
		param.setSessionTime(dto.getSessionTime());
		// 連絡先
		param.setRenrakuSaki(dto.getRenrakuSaki());
		// 駐車場保管場所１
		param.setParkingAddress1(dto.getParking1stPlace());
		// 駐車場保管場所２
		param.setParkingAddress2(dto.getParking2ndPlace());

		return param;
	}

	/**
	 * 引数で指定された「社宅を退居する」、「駐車場１を返還する」、「駐車場２を返還する」のチェック状態をもとに、
	 * 退居届テーブルの「退居する社宅区分」カラムに設定する値を取得する。
	 * 
	 * @param isShatakuTaikyoChecked 「社宅を退居する」チェック状態
	 * @param isParking1stHenkanChecked 「駐車場１を返還する」チェック状態
	 * @param isParking2ndHenkanChecked 「駐車場２を返還する」チェック状態
	 * @return 「退居する社宅区分」カラムに設定する値
	 * @see CodeConstant.TAIKYO_KBN_1
	 */
	private String getShatakuTaikyoKbn1(boolean isShatakuTaikyoChecked, boolean isParking1stHenkanChecked,
			boolean isParking2ndHenkanChecked) {
		CodeConstant.TAIKYO_KBN_1 taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.EMPTY;
		if (isShatakuTaikyoChecked) {
			if (isParking1stHenkanChecked || isParking2ndHenkanChecked) {
				// 社宅と駐車場両方を返還
				taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.BOTH;
			} else {
				// 社宅のみ返還
				taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.SHATAKU;
			}
		} else {
			if (isParking1stHenkanChecked || isParking2ndHenkanChecked) {
				// 駐車場のみ返還
				taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.PARKING;
			}
		}
		return taikyoKbn1.getVal();
	}

	/**
	 * 引数で指定された「駐車場１を返還する」、「駐車場２を返還する」のチェック状態をもとに、
	 * 退居届テーブルの「退居する社宅区分2」カラムに設定する値を取得する。
	 * 
	 * @param isParking1stHenkanChecked 「駐車場１を返還する」チェック状態
	 * @param isParking2ndHenkanChecked 「駐車場２を返還する」チェック状態
	 * @return 「退居する社宅区分」カラムに設定する値
	 * @see CodeConstant.TAIKYO_KBN_2
	 */
	private String getShatakuTaikyoKbn2(boolean isParking1stHenkanChecked, boolean isParking2ndHenkanChecked) {
		CodeConstant.TAIKYO_KBN_2 taikyoKbn2;

		if (isParking1stHenkanChecked && isParking2ndHenkanChecked) {
			// 駐車場１、２両方を返還
			taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.BOTH_PARKING;
		} else if (isParking1stHenkanChecked) {
			// 駐車場１のみを返還
			taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.PARKING_1ST;
		} else if (isParking2ndHenkanChecked) {
			// 駐車場２のみを返還
			taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.PARKING_2ND;
		} else {
			// 駐車場を返還しない
			taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.NON_RETURN;
		}
		return taikyoKbn2.getVal();
	}

	/**
	 * 退居(自動車の保管場所返還)届テーブルの登録処理を行う
	 * 
	 * @param param 登録する値を保持した退居(自動車の保管場所返還)届テーブルエンティティ
	 */
	private void insertTaikyoTodoke(Skf2040TTaikyoReport param) {
		// 登録処理
		int registCount = 0;
		registCount = skf2040TTaikyoReportRepository.insertSelective(param);
		LogUtils.debugByMsg("退居(自動車の保管場所返還)届テーブル登録件数：" + registCount + "件");
	}

	/**
	 * 申請履歴テーブルに退居届情報を新規登録する。
	 * 
	 * @param dto 保存する退居届情報を保持したDTO
	 */
	private <DTO extends Skf2040Sc001CommonDto> void insertApplHistory(DTO dto) {
		// 申請書類履歴テーブルの設定
		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		dto.setApplHistroyApplDate(DateUtils.getSysDate());
		// 登録項目をセット
		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 社員番号
		setValue.setShainNo(dto.getShainNo());
		setValue.setApplDate(dto.getApplHistroyApplDate());
		setValue.setApplNo(dto.getApplNo());
		setValue.setApplId(FunctionIdConstant.R0103); // 退居届
		setValue.setApplStatus(dto.getApplStatus());
		setValue.setApplTacFlg(String.valueOf(dto.getApplTacFlg()));
		setValue.setComboFlg(SkfCommonConstant.NOT_RENKEI);
		// 登録
		int registCount = 0;
		registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
		LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");
	}

	/**
	 * 備品返却申請テーブル登録or更新処理
	 * 
	 * @param dto
	 */
	protected <DTO extends Skf2040Sc001CommonDto> void registrationBihinShinsei(DTO dto) {

		// 備品返却申請テーブルから備品返却申請情報を取得
		Skf2040Sc001GetBihinHenkyakuApplNoInfoExp bihinHenkyakuInfo = getBihinHenkyaku(dto);

		// 情報が取得できた場合は、退居（自動車の保管場所返還）届管理番号を設定
		String bihinHenkaykuShinseiApplNo = null;
		if (bihinHenkyakuInfo != null) {
			bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getApplNo();
		}

		// 備品返却申請書番号がなければ退居（自動車の保管場所返還）届管理番号を新規発行
		if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
			// 備品返却申請用の申請書類管理番号を取得
			bihinHenkaykuShinseiApplNo = skfShinseiUtils.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001,
					dto.getShainNo());
			// 備品返却申請テーブルへ新規登録
			insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, dto);
		} else {
			// 更新処理
			updateBihinHenkyakuInfo(dto, dto.getApplNo(), bihinHenkyakuInfo);
		}
	}

	/**
	 * 備品返却申請の申請書類管理番号の情報取得
	 * 
	 * @param dto
	 * @return 備品返却申請の情報
	 */
	protected <DTO extends Skf2040Sc001CommonDto> Skf2040Sc001GetBihinHenkyakuApplNoInfoExp getBihinHenkyaku(DTO dto) {

		// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
		Skf2040Sc001GetBihinHenkyakuApplNoInfoExpParameter param = new Skf2040Sc001GetBihinHenkyakuApplNoInfoExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(dto.getApplNo());
		List<Skf2040Sc001GetBihinHenkyakuApplNoInfoExp> bihinHenkyakuInfoList = skf2040Sc001GetBihinHenkyakuApplNoInfoExpRepository
				.getBihinHenkyakuApplNoInfo(param);
		if (bihinHenkyakuInfoList == null || bihinHenkyakuInfoList.size() == 0) {
			return null;
		}

		return bihinHenkyakuInfoList.get(0);
	}

	/**
	 * 備品返却申請テーブルの新規登録
	 * 
	 * @param bihinHenkaykuShinseiApplNo
	 * @param dto
	 */
	protected <DTO extends Skf2040Sc001CommonDto> void insertBihinHenkyakuInfo(String bihinHenkaykuShinseiApplNo,
			DTO dto) {

		// 備品返却申請テーブルの設定
		Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
		setValue = setColumnInfoBihinList(setValue, dto, true, bihinHenkaykuShinseiApplNo, dto.getApplNo());
		// 登録
		int registCount = 0;
		registCount = skf2050TBihinHenkyakuShinseiRepository.insertSelective(setValue);
		LogUtils.debugByMsg("備品返却申請テーブル登録件数：" + registCount + "件");
	}

	/**
	 * 備品返却申請テーブルの更新処理
	 *
	 * @param dto
	 * @param applInfo
	 * @param bihinHenkyakuInfo
	 */
	protected <DTO extends Skf2040Sc001CommonDto> void updateBihinHenkyakuInfo(DTO dto, String taikyoApplNo,
			Skf2040Sc001GetBihinHenkyakuApplNoInfoExp bihinHenkyakuInfo) {

		int updateCnt = 0;
		// 備品返却申請テーブルの更新項目設定
		Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
		setValue = setColumnInfoBihinList(setValue, dto, false, bihinHenkyakuInfo.getApplNo(), taikyoApplNo);
		// 更新
		updateCnt = skf2050TBihinHenkyakuShinseiRepository.updateByPrimaryKeySelective(setValue);
		LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + updateCnt + "件");
	}

	/**
	 * 備品返却申請テーブルの登録値の設定
	 * 
	 * @param setValue 備品返却申請テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @param isNewRecord 新規登録情報である場合true
	 * @param bihinHenkaykuShinseiApplNo 備品返却申請の申請書類番号
	 * @param taikyoApplNo 備品返却申請の退居届申請書類番号
	 * @return 備品返却申請テーブルを更新する値
	 */
	protected <DTO extends Skf2040Sc001CommonDto> Skf2050TBihinHenkyakuShinsei setColumnInfoBihinList(
			Skf2050TBihinHenkyakuShinsei setValue, DTO dto, boolean isNewRecord, String bihinHenkaykuShinseiApplNo,
			String taikyoApplNo) {

		String applDate = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 申請書類番号
		setValue.setApplNo(bihinHenkaykuShinseiApplNo);
		// 更新SQLでは不要
		if (isNewRecord) {

			// 社員番号
			setValue.setShainNo(dto.getShainNo());
			// 所属 機関
			setValue.setAgency(dto.getAgencyName());
			// 所属 部等
			setValue.setAffiliation1(dto.getAffiliation1Name());
			// 所属 室・課等
			setValue.setAffiliation2(dto.getAffiliation2Name());
			// TEL
			setValue.setTel(dto.getTel());
			// 氏名
			setValue.setName(dto.getName());
			// 等級
			setValue.setTokyu(dto.getTokyuName());
			// 性別
			setValue.setGender(dto.getGender());

		}
		// 申請年月日
		setValue.setApplDate(applDate);
		// 退居届書類管理番号
		setValue.setTaikyoApplNo(taikyoApplNo);
		// 社宅管理番号
		setValue.setShatakuNo(Long.parseLong(NfwStringUtils.defaultString(dto.getHdnNowShatakuKanriNo())));
		// 部屋管理番号
		setValue.setRoomKanriNo(Long.parseLong(NfwStringUtils.defaultString(dto.getHdnNowShatakuRoomKanriNo())));
		// 社宅名
		setValue.setNowShatakuName(dto.getHdnSelectedNowShatakuName());
		// 号室
		setValue.setNowShatakuNo(dto.getHdnNowShatakuRoomNo());
		// 本来規格
		setValue.setNowShatakuKikaku(dto.getNowShatakuKikaku());
		// 面積
		if (!NfwStringUtils.isEmpty(dto.getNowShatakuMenseki())) {
			setValue.setNowShatakuMenseki(
					dto.getNowShatakuMenseki().replace(SkfCommonConstant.SQUARE_MASTER, CodeConstant.DOUBLE_QUOTATION));
		}
		// 返却立会希望日
		if (!NfwStringUtils.isEmpty(dto.getSessionDay())) {
			setValue.setSessionDay(dto.getSessionDay().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
		}
		// 返却立会希望日(時間)
		setValue.setSessionTime(dto.getSessionTime());
		// 連絡先
		setValue.setRenrakuSaki(dto.getRenrakuSaki());

		return setValue;

	}
}
