/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyCdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyCdExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyNameExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetNowShatakuNameExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetNowShatakuNameExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2050TBihinHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyCdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetAgensyNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetBihinItemToBeReturnExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetNowShatakuNameExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2050TBihinHenkyakuShinseiRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.CodeCacheUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;

/**
 * Skf2020Sc002 社宅入居希望等調書（申請者用） 共通処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc002SharedService {

	// 戻り値Map用定数
	public static final String KEY_AGENCY_LIST = "AGENCY_LIST";
	public static final String KEY_AFFILIATION1_LIST = "AFFILIATION1_LIST";
	public static final String KEY_AFFILIATION2_LIST = "AFFILIATION2_LIST";
	public static final String KEY_NOW_SHATAKU_NAME_LIST = "NOW_SHATAKU_NAME";
	public static final String KEY_TAIKYO_RIYU_KBN_LIST = "TAIKYO_RIYU_KBN";
	public static final String KEY_SESSION_TIME_LIST = "SESSION_TIME_LIST";

	public static final String FALSE = "false";
	public static final String TRUE = "true";
	// 更新フラグ
	public static final String UPDATE_FLG = "1";
	// 会社コード
	private String companyCd = CodeConstant.C001;

	// 最終更新日付のキャッシュキー
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf1010_t_appl_history_UpdateDate";
	public static final String NYUKYO_KEY_LAST_UPDATE_DATE = "skf2020_t_nyukyo_chosho_UpdateDate";
	public static final String BIHIN_HENKYAKU_KEY_LAST_UPDATE_DATE = "skf2050_t_bihin_henkyaku_UpdateDate";

	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreationUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2020Sc002GetNowShatakuNameExpRepository skf2020Sc002GetNowShatakuNameExpRepository;
	@Autowired
	private Skf2020Sc002GetShatakuInfoExpRepository skf2020Sc002GetShatakuInfoExpRepository;
	@Autowired
	private Skf2020Sc002GetBihinItemToBeReturnExpRepository skf2020Sc002GetBihinItemToBeReturnExpRepository;
	@Autowired
	private Skf2020Sc002GetApplInfoExpRepository skf2020Sc002GetApplInfoExpRepository;
	@Autowired
	private Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpRepository skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	private Skf2020Sc002GetAgensyCdExpRepository skf2020Sc002GetAgensyCdExpRepository;
	@Autowired
	private Skf2020Sc002GetAgensyNameExpRepository skf2020Sc002GetAgensyNameExpRepository;

	// 駐車場の有無チェック用
	private enum enmCheckParking {
		ParkingNone(0), Parking1st(1), Parking2nd(2), ParkingBoth(3);

		// フィールドの定義
		private int id;

		// コンストラクタの定義
		private enmCheckParking(int id) {
			this.id = id;
		}

		public int getInt() {
			return this.id;
		}
	};

	/**
	 * 
	 * 画面初期表示（社員情報、社宅情報まで）
	 * 
	 * @param Skf2020Sc002CommonDto
	 */
	protected void initializeDisp(Skf2020Sc002CommonDto dto) {

		// ドロップダウンの設定
		setControlDdl(dto);

		// 申請書管理番号の有無
		if (dto.getApplNo() != null) {
			// ある場合は社宅入居希望等調書の申請情報から初期表示項目を設定する。
			setSinseiInfo(dto);
		} else {
			// 無い場合
			if (dto.getShainList() != null && dto.getShainList().size() > 0) {
				// 社員情報がある場合は申請者情報からの設定
				setShainList(dto);
			}
		}
		// 社宅情報の設定
		setShatakuInfo(dto);
	}

	/**
	 * 社宅入居希望等調書の申請情報から初期表示項目を設定。
	 * 
	 * @param dto
	 */
	private void setSinseiInfo(Skf2020Sc002CommonDto dto) {

		// 社宅入居希望等調査・入居決定通知テーブル情報の取得
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoList = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey setValue = new Skf2020TNyukyoChoshoTsuchiKey();
		// 条件項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(dto.getApplNo());
		nyukyoChoshoList = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(setValue);
		LogUtils.debugByMsg("社宅入居希望等調査情報： " + nyukyoChoshoList);

		// データが取得できなかった場合は更新ボタンを使用不可にする
		if (nyukyoChoshoList == null) {
			setInitializeError(dto);
		}

		// 機関
		LogUtils.debugByMsg("機関：" + nyukyoChoshoList.getAgency());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAgency())) {
			dto.setAgencyName(nyukyoChoshoList.getAgency());
		}
		// 部等
		LogUtils.debugByMsg("部等：" + nyukyoChoshoList.getAffiliation1());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAgency())) {
			dto.setAffiliation1Name(nyukyoChoshoList.getAffiliation1());
		}
		// 室、チーム又は課
		LogUtils.debugByMsg("室、チーム又は課：" + nyukyoChoshoList.getAffiliation2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAffiliation2())) {
			dto.setAffiliation2Name(nyukyoChoshoList.getAffiliation2());
		}
		// 勤務先のTEL
		LogUtils.debugByMsg("勤務先のTEL：" + nyukyoChoshoList.getTel());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTel())) {
			dto.setTel(nyukyoChoshoList.getTel());
		}
		// 社員番号
		LogUtils.debugByMsg("社員番号：" + nyukyoChoshoList.getShainNo());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getShainNo())) {
			dto.setShainNo(nyukyoChoshoList.getShainNo());
		}
		// 等級
		LogUtils.debugByMsg("等級：" + nyukyoChoshoList.getTokyu());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTokyu())) {
			dto.setTokyuName(nyukyoChoshoList.getTokyu());
		}
		// 性別
		LogUtils.debugByMsg("性別：" + nyukyoChoshoList.getGender());
		if (NfwStringUtils.isNotEmpty(String.valueOf(nyukyoChoshoList.getGender()))) {
			dto.setGender(String.valueOf(nyukyoChoshoList.getGender()));
		}
		// 氏名
		LogUtils.debugByMsg("氏名：" + nyukyoChoshoList.getName());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getName())) {
			dto.setName(nyukyoChoshoList.getName());
		}
		// 社宅を必要としますか？
		LogUtils.debugByMsg("社宅を必要としますか？：" + nyukyoChoshoList.getTaiyoHitsuyo());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaiyoHitsuyo())) {
			dto.setTaiyoHituyo(nyukyoChoshoList.getTaiyoHitsuyo());
		}
		// 社宅を必要とする理由
		LogUtils.debugByMsg("社宅を必要とする理由：" + nyukyoChoshoList.getHitsuyoRiyu());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getHitsuyoRiyu())) {
			dto.setHitsuyoRiyu(nyukyoChoshoList.getHitsuyoRiyu());
		}
		// 社宅を必要としない理由
		LogUtils.debugByMsg("社宅を必要としない理由：" + nyukyoChoshoList.getFuhitsuyoRiyu());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getFuhitsuyoRiyu())) {
			dto.setFuhitsuyoRiyu(nyukyoChoshoList.getFuhitsuyoRiyu());
		}

		// 機関コードの取得
		Skf2020Sc002GetAgensyCdExp agensyList = new Skf2020Sc002GetAgensyCdExp();
		// DB検索処理
		Skf2020Sc002GetAgensyCdExpParameter param = new Skf2020Sc002GetAgensyCdExpParameter();
		param.setAgencyName(nyukyoChoshoList.getNewAgency());
		param.setAffiliation1Name(nyukyoChoshoList.getNewAffiliation1());
		param.setAffiliation2Name(nyukyoChoshoList.getNewAffiliation2());
		agensyList = skf2020Sc002GetAgensyCdExpRepository.getAgensyCd(param);
		// 機関
		LogUtils.debugByMsg("機関：" + nyukyoChoshoList.getNewAgency() + agensyList.getAgencyCd());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAgency())
				&& NfwStringUtils.isNotEmpty(agensyList.getAgencyCd())) {
			// 機関ドロップダウンリストの設定
			dto.setDdlAgencyList(skfDropDownUtils.getDdlAgencyByCd(CodeConstant.C001, agensyList.getAgencyCd(), true));
		}

		// 部等
		LogUtils.debugByMsg("部等：" + nyukyoChoshoList.getNewAffiliation1() + nyukyoChoshoList.getNewAffiliation1Other()
				+ agensyList.getAffiliation1Cd());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation1())
				&& NfwStringUtils.isNotEmpty(agensyList.getAffiliation1Cd())) {
			// 部等ドロップダウンリストの設定
			List<Map<String, Object>> afflication1List = new ArrayList<Map<String, Object>>();
			afflication1List = skfDropDownUtils.getDdlAffiliation1ByCd(CodeConstant.C001, agensyList.getAgencyCd(),
					agensyList.getAffiliation1Cd(), true);
			// その他を追加
			if (afflication1List.size() > 0) {
				Map<String, Object> soshikiMap = new HashMap<String, Object>();
				soshikiMap.put("value", "99");
				soshikiMap.put("label", "その他");
				afflication1List.add(soshikiMap);
			}
			dto.setDdlAffiliation1List(afflication1List);
		}

		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation1Other())) {
			dto.setNewAffiliation1Other(nyukyoChoshoList.getNewAffiliation1Other());
		}

		// 室、チーム又は課
		LogUtils.debugByMsg("室、チーム又は課：" + nyukyoChoshoList.getNewAffiliation2()
				+ nyukyoChoshoList.getNewAffiliation2Other() + agensyList.getAffiliation2Cd());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation2())
				&& NfwStringUtils.isNotEmpty(agensyList.getAffiliation2Cd())) {
			// 室、チーム又は課ドロップダウンをセット
			List<Map<String, Object>> afflication2List = new ArrayList<Map<String, Object>>();
			afflication2List = skfDropDownUtils.getDdlAffiliation2ByCd(CodeConstant.C001, agensyList.getAgencyCd(),
					agensyList.getAffiliation1Cd(), agensyList.getAffiliation2Cd(), true);
			// その他を追加
			if (afflication2List.size() > 0) {
				Map<String, Object> teamMap = new HashMap<String, Object>();
				teamMap.put("value", "99");
				teamMap.put("label", "その他");
				afflication2List.add(teamMap);
			}
			dto.setDdlAffiliation2List(afflication2List);
		}

		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation2Other())) {
			dto.setNewAffiliation2Other(nyukyoChoshoList.getNewAffiliation2Other());
		}

		// 必要とする社宅
		LogUtils.debugByMsg("必要とする社宅：" + nyukyoChoshoList.getHitsuyoShataku());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getHitsuyoShataku())) {
			dto.setHitsuyoShataku(nyukyoChoshoList.getHitsuyoShataku());
		}

		// 家族1
		LogUtils.debugByMsg("家族1：" + nyukyoChoshoList.getDokyoRelation1() + nyukyoChoshoList.getDokyoName1()
				+ nyukyoChoshoList.getDokyoAge1());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation1())) {
			dto.setDokyoRelation1(nyukyoChoshoList.getDokyoRelation1());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName1())) {
			dto.setDokyoName1(nyukyoChoshoList.getDokyoName1());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge1())) {
			dto.setDokyoAge1(nyukyoChoshoList.getDokyoAge1());
		}

		// 家族2
		LogUtils.debugByMsg("家族2：" + nyukyoChoshoList.getDokyoRelation2() + nyukyoChoshoList.getDokyoName2()
				+ nyukyoChoshoList.getDokyoAge2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation2())) {
			dto.setDokyoRelation2(nyukyoChoshoList.getDokyoRelation2());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName2())) {
			dto.setDokyoName2(nyukyoChoshoList.getDokyoName2());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge2())) {
			dto.setDokyoAge2(nyukyoChoshoList.getDokyoAge2());
		}

		// 家族3
		LogUtils.debugByMsg("家族3：" + nyukyoChoshoList.getDokyoRelation3() + nyukyoChoshoList.getDokyoName3()
				+ nyukyoChoshoList.getDokyoAge3());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation3())) {
			dto.setDokyoRelation3(nyukyoChoshoList.getDokyoRelation3());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName3())) {
			dto.setDokyoName3(nyukyoChoshoList.getDokyoName3());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge3())) {
			dto.setDokyoAge3(nyukyoChoshoList.getDokyoAge3());
		}

		// 家族4
		LogUtils.debugByMsg("家族4：" + nyukyoChoshoList.getDokyoRelation4() + nyukyoChoshoList.getDokyoName4()
				+ nyukyoChoshoList.getDokyoAge4());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation4())) {
			dto.setDokyoRelation4(nyukyoChoshoList.getDokyoRelation4());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName4())) {
			dto.setDokyoName4(nyukyoChoshoList.getDokyoName4());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge4())) {
			dto.setDokyoAge4(nyukyoChoshoList.getDokyoAge4());
		}

		// 家族5
		LogUtils.debugByMsg("家族5：" + nyukyoChoshoList.getDokyoRelation5() + nyukyoChoshoList.getDokyoName5()
				+ nyukyoChoshoList.getDokyoAge5());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation5())) {
			dto.setDokyoRelation5(nyukyoChoshoList.getDokyoRelation5());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName5())) {
			dto.setDokyoName5(nyukyoChoshoList.getDokyoName5());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge5())) {
			dto.setDokyoAge5(nyukyoChoshoList.getDokyoAge5());
		}

		// 家族6
		LogUtils.debugByMsg("家族6：" + nyukyoChoshoList.getDokyoRelation6() + nyukyoChoshoList.getDokyoName6()
				+ nyukyoChoshoList.getDokyoAge6());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation6())) {
			dto.setDokyoRelation6(nyukyoChoshoList.getDokyoRelation6());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName6())) {
			dto.setDokyoName6(nyukyoChoshoList.getDokyoName6());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge6())) {
			dto.setDokyoAge6(nyukyoChoshoList.getDokyoAge6());
		}

		// 入居希望日
		LogUtils.debugByMsg("入居希望日：" + nyukyoChoshoList.getNyukyoYoteiDate());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNyukyoYoteiDate())) {
			dto.setNyukyoYoteiDate(nyukyoChoshoList.getNyukyoYoteiDate());
		}

		// 自動者の保管場所
		LogUtils.debugByMsg("自動者の保管場所：" + nyukyoChoshoList.getParkingUmu());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUmu())) {
			dto.setParkingUmu(nyukyoChoshoList.getParkingUmu());
		}

		// 自動車の登録番号入力フラグ(1台目)
		LogUtils.debugByMsg("自動車の登録番号入力フラグ(1台目)：" + nyukyoChoshoList.getCarNoInputFlg());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNoInputFlg())) {
			dto.setCarNoInputFlg(nyukyoChoshoList.getCarNoInputFlg());
		}

		// 自動車の車名(１台目)
		LogUtils.debugByMsg("自動車の車名(１台目)：" + nyukyoChoshoList.getCarName());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarName())) {
			dto.setCarName(nyukyoChoshoList.getCarName());
		}

		// 自動車の登録番号(１台目)
		LogUtils.debugByMsg("自動車の登録番号(１台目)：" + nyukyoChoshoList.getCarNo());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNo())) {
			dto.setCarNo(nyukyoChoshoList.getCarNo());
		}

		// 車検の有効期間満了日(１台目)
		LogUtils.debugByMsg("車検の有効期間満了日(１台目)" + nyukyoChoshoList.getCarExpirationDate());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarExpirationDate())) {
			dto.setCarExpirationDate(nyukyoChoshoList.getCarExpirationDate());
		}

		// 自動車の使用者(１台目)
		LogUtils.debugByMsg("自動車の使用者(１台目)" + nyukyoChoshoList.getCarUser());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarUser())) {
			dto.setCarUser(nyukyoChoshoList.getCarUser());
		}

		// 自動車の保管場所 使用開始日(予定日)(１台目)
		LogUtils.debugByMsg("自動車の保管場所 使用開始日(予定日)(１台目)" + nyukyoChoshoList.getParkingUseDate());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUseDate())) {
			dto.setParkingUseDate(nyukyoChoshoList.getParkingUseDate());
		}

		// 自動車の登録番号入力フラグ(2台目)
		LogUtils.debugByMsg("自動車の登録番号入力フラグ(2台目)：" + nyukyoChoshoList.getCarNoInputFlg2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNoInputFlg2())) {
			dto.setCarNoInputFlg2(nyukyoChoshoList.getCarNoInputFlg2());
		}

		// 自動車の車名(2台目)
		LogUtils.debugByMsg("自動車の車名(2台目)：" + nyukyoChoshoList.getCarName2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarName2())) {
			dto.setCarName2(nyukyoChoshoList.getCarName2());
		}

		// 自動車の登録番号(2台目)
		LogUtils.debugByMsg("自動車の登録番号(2台目)：" + nyukyoChoshoList.getCarNo2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNo2())) {
			dto.setCarNo2(nyukyoChoshoList.getCarNo2());
		}

		// 車検の有効期間満了日(2台目)
		LogUtils.debugByMsg("車検の有効期間満了日(2台目)" + nyukyoChoshoList.getCarExpirationDate2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarExpirationDate2())) {
			dto.setCarExpirationDate2(nyukyoChoshoList.getCarExpirationDate2());
		}

		// 自動車の使用者(2台目)
		LogUtils.debugByMsg("自動車の使用者(2台目)" + nyukyoChoshoList.getCarUser2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarUser2())) {
			dto.setCarUser2(nyukyoChoshoList.getCarUser2());
		}

		// 自動車の保管場所 使用開始日(予定日)(2台目)
		LogUtils.debugByMsg("自動車の保管場所 使用開始日(予定日)(2台目)" + nyukyoChoshoList.getParkingUseDate2());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUseDate2())) {
			dto.setParkingUseDate2(nyukyoChoshoList.getParkingUseDate2());
		}

		// 現居住宅
		LogUtils.debugByMsg("現居住宅" + nyukyoChoshoList.getNowShataku());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNowShataku())) {
			dto.setNowShataku(nyukyoChoshoList.getNowShataku());
		}
		// 現保有の社宅名
		LogUtils.debugByMsg("現保有の社宅名" + nyukyoChoshoList.getNowShatakuName());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNowShatakuName())) {
			dto.setShatakuName(nyukyoChoshoList.getNowShatakuName());
		}
		// 特殊事情等
		LogUtils.debugByMsg("特殊事情等" + nyukyoChoshoList.getTokushuJijo());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTokushuJijo())) {
			dto.setTokushuJijo(nyukyoChoshoList.getTokushuJijo());
		}
		// 現保有の社宅
		LogUtils.debugByMsg("現保有の社宅" + nyukyoChoshoList.getTaikyoYotei());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoYotei())) {
			dto.setTaikyoYotei(nyukyoChoshoList.getTaikyoYotei());
		}
		// 退居予定日
		LogUtils.debugByMsg("退居予定日" + nyukyoChoshoList.getTaikyoYoteiDate());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoYoteiDate())) {
			dto.setTaikyoYoteiDate(nyukyoChoshoList.getTaikyoYoteiDate());
		}
		// 社宅の状態
		LogUtils.debugByMsg("社宅の状態" + nyukyoChoshoList.getShatakuJotai());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getShatakuJotai())) {
			dto.setShatakuJyotai(nyukyoChoshoList.getShatakuJotai());
		}
		//
		LogUtils.debugByMsg("退居理由" + nyukyoChoshoList.getTaikyoRiyuKbn() + nyukyoChoshoList.getTaikyoRiyu());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoRiyuKbn())) {
			// 退居理由ドロップダウンリストの設定

			dto.setDdlTaikyoRiyuKbnList(
					skfDropDownUtils.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, "", true));
		}
		// 退居後の連絡先
		LogUtils.debugByMsg("退居後の連絡先" + nyukyoChoshoList.getTaikyogoRenrakusaki());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyogoRenrakusaki())) {
			dto.setTaikyogoRenrakuSaki(nyukyoChoshoList.getTaikyogoRenrakusaki());
		}
		// 返却立会希望日(日)
		LogUtils.debugByMsg("返却立会希望日(日)" + nyukyoChoshoList.getSessionDay());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getSessionDay())) {
			dto.setSessionDay(nyukyoChoshoList.getSessionDay());
		}
		// 返却立会希望日(時)
		LogUtils.debugByMsg("返却立会希望日(時)" + nyukyoChoshoList.getSessionTime());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getSessionTime())) {
			dto.setSessionTime(nyukyoChoshoList.getSessionTime());
		}
		// 連絡先
		LogUtils.debugByMsg("連絡先" + nyukyoChoshoList.getRenrakuSaki());
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getRenrakuSaki())) {
			dto.setRenrakuSaki(nyukyoChoshoList.getRenrakuSaki());
		}

		// 社宅管理IDの取得
		if (nyukyoChoshoList.getNowShatakuKanriNo() > 0) {

		}

		// 更新日時
		LogUtils.debugByMsg("更新日時" + nyukyoChoshoList.getUpdateDate());
		dto.addLastUpdateDate(NYUKYO_KEY_LAST_UPDATE_DATE, nyukyoChoshoList.getUpdateDate());

	}

	/**
	 * 初期表示エラー時の処理 更新処理を行わせないようボタンを使用不可にする。
	 * 
	 * @param dto Skf2020Sc002CommonDto
	 */
	protected void setInitializeError(Skf2020Sc002CommonDto dto) {
		// 更新処理を行わせないよ う一時保存、申請要件を確認ボタンを使用不可に
		// 一時保存
		dto.setBtnSaveDisabeld(TRUE);
		// 申請内容を確認
		dto.setBtnCheckDisabled(TRUE);
		ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1077);
	}

	/**
	 * 社員情報の設定
	 * 
	 * @param dto
	 */
	private void setShainList(Skf2020Sc002CommonDto dto) {

		// 機関
		String agencyName = dto.getShainList().get(0).getAgencyName();
		dto.setAgencyName(agencyName);
		// 部等
		String affiliation1Name = dto.getShainList().get(0).getAffiliation1Name();
		dto.setAffiliation1Name(affiliation1Name);
		// 室、チームまたは課
		String affiliation2Name = dto.getShainList().get(0).getAffiliation2Name();
		dto.setAffiliation2Name(affiliation2Name);
		// 勤務先のTEL
		String tel = dto.getShainList().get(0).getTel();
		dto.setTel(tel);

		// 社員番号
		dto.setShainNo(dto.getShainList().get(0).getShainNo());
		// 社員名
		String name = dto.getShainList().get(0).getName();
		dto.setName(name);
		// 等級
		String tokyuName = dto.getShainList().get(0).getTokyuName();
		dto.setTokyuName(tokyuName);
		// 性別
		dto.setGender(dto.getShainList().get(0).getGender());
		switch (dto.getShainList().get(0).getGender()) {
		case CodeConstant.MALE:
			dto.setGenderName(CodeConstant.OUTPUT_MALE);
			break;
		case CodeConstant.FEMALE:
			dto.setGenderName(CodeConstant.OUTPUT_FEMALE);
			break;
		}
		// 申請書ステータス
		String status = CodeConstant.STATUS_MISAKUSEI;
		dto.setStatus(status);
	}

	/**
	 * 
	 * 現居社宅情報の設定
	 * 
	 * @param dto
	 */
	protected void setShatakuInfo(Skf2020Sc002CommonDto dto) {

		// Hidden
		Long hdnNowShatakuRoomKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅部屋管理番号
		Long hdnNowShatakuKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅管理番号
		String hdnShatakuKikakuKbn = "";// 規格(間取り)

		// 現保有社宅の全量取得
		List<Skf2020Sc002GetNowShatakuNameExp> resultNowShatakuNameList = new ArrayList<Skf2020Sc002GetNowShatakuNameExp>();
		Skf2020Sc002GetNowShatakuNameExpParameter param = new Skf2020Sc002GetNowShatakuNameExpParameter();
		param.setShainNo(dto.getShainNo());
		param.setNyukyoDate(dto.getYearMonthDay());
		resultNowShatakuNameList = skf2020Sc002GetNowShatakuNameExpRepository.getNowShatakuName(param);

		long shatakuKanriId = CodeConstant.LONG_ZERO;
		dto.setShatakuKanriId(shatakuKanriId);
		if (resultNowShatakuNameList.size() > 0) {
			shatakuKanriId = resultNowShatakuNameList.get(0).getShatakuKanriId();
			dto.setShatakuKanriId(shatakuKanriId);
			dto.setHdnSelectedNowShatakuName(resultNowShatakuNameList.get(0).getShatakuName());
		}

		// 現居住宅の選択された情報の取得
		List<Skf2020Sc002GetShatakuInfoExp> shatakuList = new ArrayList<Skf2020Sc002GetShatakuInfoExp>();
		shatakuList = getShatakuInfo(shatakuKanriId, dto.getShainNo(), shatakuList);

		// 取得できた場合は現居住社宅の情報設定
		if (shatakuList.size() > 0) {

			// 室番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getRoomNo())) {
				dto.setNowShatakuNo(shatakuList.get(0).getRoomNo());
			}
			// 規格(間取り)
			// 規格があった場合は、貸与規格。それ以外は本来規格
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getKikaku())) {
				hdnShatakuKikakuKbn = shatakuList.get(0).getKikaku();// 貸与規格
				dto.setHdnShatakuKikakuKbn(hdnShatakuKikakuKbn);
				dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
				dto.setNowShatakuKikakuName(shatakuList.get(0).getKikakuName());
			} else {
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getOriginalKikaku())) {
					hdnShatakuKikakuKbn = shatakuList.get(0).getOriginalKikaku();// 本来規格
					dto.setHdnShatakuKikakuKbn(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikakuName(shatakuList.get(0).getOriginalKikakuName());
				}
			}

			// 面積
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getLendMenseki())) {
				dto.setNowShatakuMenseki(shatakuList.get(0).getLendMenseki() + "㎡");
			}

			// 駐車場 都道府県コード（保有社宅のみ設定される）
			String wkPrefName = CodeConstant.DOUBLE_QUOTATION;
			String prefCode = CodeConstant.DOUBLE_QUOTATION;
			// 取得できたら汎用コードマスタから名称を取得
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getPrefCdParking())) {
				prefCode = shatakuList.get(0).getPrefCdParking();
				wkPrefName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_PREFCD, prefCode);
			}

			// 駐車場 １台目 保管場所
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress1())) {
				dto.setParking1stPlace(wkPrefName + shatakuList.get(0).getParkingAddress1());
			}

			// 駐車場 １台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock1())) {
				dto.setHdnParking1stNumber(shatakuList.get(0).getParkingBlock1());
			}

			// 駐車場 ２台目 保管場所
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress2())) {
				dto.setParking2stPlace(wkPrefName + shatakuList.get(0).getParkingAddress2());
			}

			// 駐車場 ２台目 位置番号
			if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock2())) {
				dto.setHdnParking2stNumber(shatakuList.get(0).getParkingBlock2());
			}

			// 現在の社宅管理番号
			if (shatakuList.get(0).getShatakuKanriNo() != null) {
				hdnNowShatakuKanriNo = shatakuList.get(0).getShatakuKanriNo();
				dto.setHdnNowShatakuKanriNo(hdnNowShatakuKanriNo);
				dto.setHdnShatakuKanriNo(hdnNowShatakuKanriNo);
			}

			// 現在の部屋管理番号
			if (shatakuList.get(0).getShatakuRoomKanriNo() != null) {
				hdnNowShatakuRoomKanriNo = shatakuList.get(0).getShatakuRoomKanriNo();
				dto.setHdnShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
				dto.setHdnShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
			}

			// リストに格納
			dto.setShatakuList(shatakuList);
		}
	}

	/**
	 * 
	 * 返却備品の設定 備品状態が2:保有備品または3:レンタルの表示の備品情報を取得する。<br>
	 * 返却備品有無に「1:備品返却する」を設定する。<br>
	 * 件数が取得できた場合は、備品の表示用リストを作成する。<br>
	 * List<String> bihinItemList →tr情報（列） List<List<String>>
	 * bihinItemNameList→td情報(行）
	 * 
	 * htmlBihinCreateTableに作成したbihinItemNameListと、表示したい列数を渡す
	 * 
	 * @param initDto
	 */
	protected void setReturnBihinInfo(Skf2020Sc002CommonDto dto) {

		// 返却備品有無に「0:備品返却しない」を設定
		dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SHINAI);

		// 備品状態が2:保有備品または3:レンタルの表示
		List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList = new ArrayList<Skf2020Sc002GetBihinItemToBeReturnExp>();
		resultBihinItemList = getBihinItemToBeReturn(dto.getShatakuKanriId(), dto.getShainNo(), resultBihinItemList);

		// 件数が取得できた場合
		if (resultBihinItemList.size() > 0 && CollectionUtils.isNotEmpty(resultBihinItemList)) {

			// 返却備品有無に「1:備品返却する」を設定
			dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SURU);

			// 【ラベル部分】
			// 要返却備品の取得
			List<String> bihinItemList = new ArrayList<String>();
			List<List<String>> bihinItemNameList = new ArrayList<List<String>>();
			for (Skf2020Sc002GetBihinItemToBeReturnExp dt : resultBihinItemList) {
				// 表示・値を設定
				bihinItemList = new ArrayList<String>();
				bihinItemList.add(dt.getBihinName());
				bihinItemNameList.add(bihinItemList);
			}
			// HTMLの作成
			String bihinItem = skfHtmlCreationUtils.htmlBihinCreateTable(bihinItemNameList, 2);
			dto.setReturnEquipment(bihinItem);
		}
	}

	/**
	 * 現居住社宅の取得
	 * 
	 * @param shatakuKanriId
	 * @param shainNo
	 * @param shatakuList
	 * @param yearMonth
	 * @return
	 */
	protected List<Skf2020Sc002GetShatakuInfoExp> getShatakuInfo(long shatakuKanriId, String shainNo,
			List<Skf2020Sc002GetShatakuInfoExp> shatakuList) {

		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

		// データの取得
		Skf2020Sc002GetShatakuInfoExpParameter param = new Skf2020Sc002GetShatakuInfoExpParameter();
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(yearMonth);
		param.setShainNo(shainNo);
		shatakuList = skf2020Sc002GetShatakuInfoExpRepository.getShatakuInfo(param);

		return shatakuList;
	}

	/**
	 * 
	 * 要返却備品の取得
	 * 
	 * @param shatakuKanriId
	 * @param shainNo
	 * @param yearMonth
	 * @param resultBihinItemList
	 * @return resultBihinItemList
	 */
	private List<Skf2020Sc002GetBihinItemToBeReturnExp> getBihinItemToBeReturn(long shatakuKanriId, String shainNo,
			List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList) {

		String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

		Skf2020Sc002GetBihinItemToBeReturnExpParameter param = new Skf2020Sc002GetBihinItemToBeReturnExpParameter();
		param.setShainNo(shainNo);
		param.setShatakuKanriId(shatakuKanriId);
		param.setYearMonth(yearMonth);
		resultBihinItemList = skf2020Sc002GetBihinItemToBeReturnExpRepository.getBihinItemToBeReturn(param);
		return resultBihinItemList;

	}

	/**
	 * デフォルト色に設定
	 * 
	 * @param dto
	 */
	protected void setDefultColor(Skf2020Sc002CommonDto dto) {

		// TEL
		dto.setTelErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅を必要としますか？
		dto.setTaiyoHituyoErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅を必要とする理由
		dto.setHitsuyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);
		// 社宅を必要としない理由
		dto.setFuhitsuyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);
		// 機関
		dto.setNewAgencyErr(CodeConstant.DOUBLE_QUOTATION);
		// 部等
		dto.setNewAffiliation1Err(CodeConstant.DOUBLE_QUOTATION);
		// 室、チーム又は課
		dto.setNewAffiliation2Err(CodeConstant.DOUBLE_QUOTATION);
		// 必要とする社宅
		dto.setHitsuyoShatakuErr(CodeConstant.DOUBLE_QUOTATION);
		// 続柄
		dto.setDokyoRelation1Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoRelation2Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoRelation3Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoRelation4Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoRelation5Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoRelation6Err(CodeConstant.DOUBLE_QUOTATION);

		// 氏名
		dto.setDokyoName1Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoName2Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoName3Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoName4Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoName5Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoName6Err(CodeConstant.DOUBLE_QUOTATION);

		// 年齢
		dto.setDokyoAge1Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoAge2Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoAge3Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoAge4Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoAge5Err(CodeConstant.DOUBLE_QUOTATION);
		dto.setDokyoAge6Err(CodeConstant.DOUBLE_QUOTATION);

		// 入居希望日
		dto.setNyukyoYoteiDateErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の保管場所
		dto.setParkingUmuErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の保管場所
		dto.setCarNoInputFlgErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の車名(１台目)
		dto.setCarNameErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の登録番号(１台目)
		dto.setCarNoErr(CodeConstant.DOUBLE_QUOTATION);

		// 車検の有効期間満了日(１台目)
		dto.setCarExpirationDateErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の使用者(１台目)
		dto.setCarUserErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の保管場所 使用開始日(１台目)
		dto.setParkingUseDateErr(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の車名(2台目)
		dto.setCarName2Err(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の登録番号(2台目)
		dto.setCarNo2Err(CodeConstant.DOUBLE_QUOTATION);

		// 車検の有効期間満了日(2台目)
		dto.setCarExpirationDate2Err(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の使用者(2台目)
		dto.setCarUser2Err(CodeConstant.DOUBLE_QUOTATION);

		// 自動車の保管場所 使用開始日(2台目)
		dto.setParkingUseDate2Err(CodeConstant.DOUBLE_QUOTATION);

		// 現居住宅
		dto.setNowShatakuErr(CodeConstant.DOUBLE_QUOTATION);

		// 保有社宅名
		dto.setNowShatakuNameErr(CodeConstant.DOUBLE_QUOTATION);

		// 特殊事情等
		dto.setTokushuJijoErr(CodeConstant.DOUBLE_QUOTATION);

		// 現保有の社宅(退居予定)
		dto.setTaikyoYoteiErr(CodeConstant.DOUBLE_QUOTATION);

		// 退居予定日
		dto.setTaikyoYoteiDateErr(CodeConstant.DOUBLE_QUOTATION);

		// 社宅の状態
		dto.setShatakuJyotaiErr(CodeConstant.DOUBLE_QUOTATION);

		// 退居理由
		dto.setDdlTaikyoRiyuKbnListErr(CodeConstant.DOUBLE_QUOTATION);
		dto.setTaikyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);

		// 退居連絡先
		dto.setTaikyogoRenrakuSakiErr(CodeConstant.DOUBLE_QUOTATION);

		// 返却立会希望日
		dto.setSessionDayErr(CodeConstant.DOUBLE_QUOTATION);
		dto.setSessionTimeErr(CodeConstant.DOUBLE_QUOTATION);

		// 連絡先
		dto.setRenrakuSakiErr(CodeConstant.DOUBLE_QUOTATION);

	}

	/**
	 * デフォルトの選択状態を設定
	 * 
	 * @param dto
	 */
	protected void setControlValue(Skf2020Sc002CommonDto dto) {

		// 必要とする社宅初期表示制御
		dto.setRdoKikonDisabled(TRUE);
		dto.setRdoHitsuyoSetaiDisabled(TRUE);
		dto.setRdoHitsuyoTanshinDisabled(TRUE);
		dto.setRdoHitsuyoDokushinDisabled(TRUE);

		// 駐車場のみは2台貸与されている場合には駐車場のみは申請不可
		if (checkParking(dto.getParking1stPlace(), dto.getParking2stPlace()) == 3) {
			dto.setRdoParkingOnlyDisabled(TRUE);
		}

		// 社宅を必要としますか？
		if (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が必要の場合
			// 入居日のカレンダーを活性
			dto.setNyukyoYoteiDateClDisabled(FALSE);
			// 退居項目を表示
			dto.setTaikyoViewFlag(TRUE);
			dto.setLblShatakuFuyouMsgRemove(FALSE);
		} else if (CodeConstant.ASKED_SHATAKU_FUYOU.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が不要
			// 退居項目を非表示
			dto.setTaikyoViewFlag(FALSE);
			// 退居届を促すメッセージを表示
			dto.setLblShatakuFuyouMsgRemove(TRUE);
		} else if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が駐車場のみの場合、以下項目をチェック状態にする
			dto.setHitsuyoRiyu(CodeConstant.HITUYO_RIYU_OTHERS);
			dto.setFuhitsuyoRiyu(CodeConstant.FUYO_RIYU_OTHERS);
			dto.setTaikyoYotei(CodeConstant.NOT_LEAVE);
			// 退居項目を表示
			dto.setTaikyoViewFlag(TRUE);
			dto.setLblShatakuFuyouMsgRemove(FALSE);
		} else {
			dto.setTaikyoViewFlag(TRUE);
		}

		// 社宅を必要とする理由→jsp dynamicMaskListで制御

		// 必要とする社宅
		if (CodeConstant.SETAI.equals(dto.getHitsuyoShataku())) {
			dto.setRdoKikonDisabled(TRUE);
			dto.setRdoHitsuyoSetaiDisabled(FALSE);
			dto.setRdoHitsuyoTanshinDisabled(FALSE);
			dto.setRdoHitsuyoDokushinDisabled(FALSE);
		} else if (CodeConstant.TANSHIN.equals(dto.getHitsuyoShataku())) {
			dto.setRdoKikonDisabled(TRUE);
			dto.setRdoHitsuyoSetaiDisabled(FALSE);
			dto.setRdoHitsuyoTanshinDisabled(FALSE);
			dto.setRdoHitsuyoDokushinDisabled(FALSE);
		} else if (CodeConstant.DOKUSHIN.equals(dto.getHitsuyoShataku())) {
			dto.setRdoKikonDisabled(FALSE);
			dto.setRdoHitsuyoSetaiDisabled(TRUE);
			dto.setRdoHitsuyoTanshinDisabled(TRUE);
			dto.setRdoHitsuyoDokushinDisabled(FALSE);
		}

		// 保管場所を必要とするか
		if (CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())) {
			// 必要の場合、カレンダーを活性
			dto.setCarExpirationDateClDisabled(FALSE);
			dto.setParkingUseDateClDisabled(FALSE);
			dto.setCarExpirationDate2ClDisabled(FALSE);
			dto.setParkingUseDate2ClDisabled(FALSE);

			if (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg())) {
				dto.setCarExpirationDateClDisabled(FALSE);
				dto.setParkingUseDateClDisabled(FALSE);
			} else if (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg())) {
				dto.setCarExpirationDateClDisabled(TRUE);
				dto.setParkingUseDateClDisabled(FALSE);
			}

			if (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg2())) {
				dto.setCarExpirationDate2ClDisabled(FALSE);
				dto.setParkingUseDate2ClDisabled(FALSE);
			} else if (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg2())) {
				dto.setCarExpirationDate2ClDisabled(TRUE);
				dto.setParkingUseDate2ClDisabled(FALSE);
			}
		} else {
			dto.setCarExpirationDateClDisabled(TRUE);
			dto.setParkingUseDateClDisabled(TRUE);
			dto.setCarExpirationDate2ClDisabled(TRUE);
			dto.setParkingUseDate2ClDisabled(TRUE);

		}

		// 保有社宅が存在する場合
		LogUtils.debugByMsg("保有社宅が存在する場合" + dto.getShatakuList());
		if (dto.getShatakuList() != null) {
			// 現居住宅 保有(会社借上を含む)をチェック状態にする
			dto.setNowShataku(CodeConstant.GENNYUKYO_SHATAKU_KBN_HOYU);
			// その他項目を非活性にする
			dto.setRdoNowJutakuJitakuDisabeld(TRUE);
			dto.setRdoNowJutakuKariageDisabled(TRUE);
			dto.setRdoNowJutakuSonotaDisabled(TRUE);

			// 退居予定の場合、カレンダーを活性
			if (CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
				dto.setTaikyoYoteiDateClDisabled(FALSE);
				// 退居届を促すメッセージを表示
				dto.setLblShatakuFuyouMsgRemove(TRUE);
			} else if (CodeConstant.NOT_LEAVE.equals(dto.getTaikyoYotei())) {
				dto.setTaikyoYoteiDateClDisabled(TRUE);
				// 退居届を促すメッセージを非表示
				dto.setLblShatakuFuyouMsgRemove(FALSE);
			}

		} else {
			// 現居住社宅が無い場合は駐車場のみ、現居住宅を非活性にする
			dto.setRdoParkingOnlyDisabled(TRUE);
			dto.setRdoNowJutakuHoyuDisabled(TRUE);
			dto.setRdoNowJutakuJitakuDisabeld(TRUE);
			dto.setRdoNowJutakuKariageDisabled(TRUE);
			dto.setRdoNowJutakuSonotaDisabled(TRUE);
			// 退居項目のカレンダーは非活性化させる
			dto.setTaikyoYoteiDateClDisabled(TRUE);
		}

		// 現居社宅→jsp dynamicMaskListで制御

		// 備品制御
		if (NfwStringUtils.isNotEmpty(dto.getReturnEquipment())) {
			dto.setSessionTimeDisabled(FALSE);
			dto.setSessionDayDisabled(FALSE);
			dto.setSessionDayClDisabled(FALSE);
			dto.setRenrakuSakiDisabled(FALSE);
		} else {
			dto.setSessionTimeDisabled(TRUE);
			dto.setSessionDayDisabled(TRUE);
			dto.setSessionDayClDisabled(TRUE);
			dto.setRenrakuSakiDisabled(TRUE);
		}

	}

	/**
	 * ラジオボタンのチェック状態をセットする
	 *
	 * @param dto
	 */
	protected void setCheckRadio(Skf2020Sc002CommonDto dto) {

		String msg = "ラジオボタンチェック：";
		String checkTrue = TRUE;

		LogUtils.debugByMsg(msg + dto.getTaiyoHituyo() + dto.getRdoHitsuyoChecked());

		// 社宅を必要としますか？ 必要
		if (dto.getTaiyoHituyo() != null && CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())) {
			dto.setRdoHitsuyoChecked(checkTrue);
		}

		// 社宅を必要としますか？ 必要としない
		if (dto.getTaiyoHituyo() != null && CodeConstant.ASKED_SHATAKU_FUYOU.equals(dto.getTaiyoHituyo())) {
			dto.setRdoFuyouChecked(checkTrue);
		}

		// 社宅を必要としますか？ 駐車場のみ
		if (dto.getTaiyoHituyo() != null && CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(dto.getTaiyoHituyo())) {
			dto.setRdoParkingOnlyChecked(checkTrue);
		}

		LogUtils.debugByMsg(msg + dto.getTaiyoHituyo() + dto.getRdoHitsuyoChecked());

		// 社宅を必要とする理由 異動のため
		if (dto.getHitsuyoRiyu() != null && CodeConstant.IDOU.equals(dto.getHitsuyoRiyu())) {
			dto.setRdoHitsuyoIdoChecked(checkTrue);
		}
		LogUtils.debugByMsg(msg + dto.getHitsuyoRiyu() + dto.getRdoHitsuyoIdoChecked());

		// ラジオボタン 社宅を必要とする理由 結婚のため
		if (dto.getHitsuyoRiyu() != null && CodeConstant.KEKKON.equals(dto.getHitsuyoRiyu())) {
			dto.setRdoHitsuyoKekkonChecked(checkTrue);
		}
		LogUtils.debugByMsg(msg + dto.getHitsuyoRiyu() + dto.getRdoHitsuyoKekkonChecked());

		// ラジオボタン 社宅を必要とする理由その他
		if (dto.getHitsuyoRiyu() != null && CodeConstant.HITUYO_RIYU_OTHERS.equals(dto.getHitsuyoRiyu())) {
			dto.setRdoHitsuyoSonotaChecked(checkTrue);
		}

		LogUtils.debugByMsg(msg + dto.getHitsuyoRiyu() + dto.getRdoHitsuyoSonotaChecked());

		// ラジオボタン 社宅を必要としない理由 自宅通勤
		if (dto.getFuhitsuyoRiyu() != null && CodeConstant.JITAKU_TSUKIN.equals(dto.getFuhitsuyoRiyu())) {
			dto.setRdoHitsuyoIdoChecked(checkTrue);
		}
		LogUtils.debugByMsg(msg + dto.getFuhitsuyoRiyu() + dto.getRdoHitsuyoIdoChecked());

		// ラジオボタン 社宅を必要としない理由 自己借上
		if (dto.getFuhitsuyoRiyu() != null && CodeConstant.JIKO_KARIAGE.equals(dto.getFuhitsuyoRiyu())) {
			dto.setRdoFuyouJikokariageChecked(checkTrue);
		}
		LogUtils.debugByMsg(msg + dto.getHitsuyoRiyu() + dto.getRdoHitsuyoKekkonChecked());

		// ラジオボタン 社宅を必要とする理由その他
		if (dto.getFuhitsuyoRiyu() != null && CodeConstant.FUYO_RIYU_OTHERS.equals(dto.getFuhitsuyoRiyu())) {
			dto.setRdoFuyouSonotaChecked(checkTrue);
		}

		LogUtils.debugByMsg(msg + dto.getFuhitsuyoRiyu() + dto.getRdoFuyouSonotaChecked());

		// ラジオボタン 必要とする社宅 世帯
		LogUtils.debugByMsg(msg + "必要とする社宅 世帯" + dto.getHitsuyoShataku() + dto.getRdoHitsuyoSetaiChecked());
		if (dto.getHitsuyoRiyu() != null && CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())
				&& (CodeConstant.SETAI.equals(dto.getHitsuyoShataku()))) {
			dto.setRdoHitsuyoSetaiChecked(checkTrue);
		}

		// ラジオボタン 必要とする社宅 単身
		LogUtils.debugByMsg(msg + "必要とする社宅 単身" + dto.getHitsuyoShataku() + dto.getRdoHitsuyoTanshinChecked());
		if (dto.getHitsuyoRiyu() != null && CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())
				&& (CodeConstant.TANSHIN.equals(dto.getHitsuyoShataku()))) {
			dto.setRdoHitsuyoTanshinChecked(checkTrue);
		}

		// ラジオボタン 必要とする社宅 独身
		LogUtils.debugByMsg(msg + "必要とする社宅 独身" + dto.getHitsuyoShataku() + dto.getRdoHitsuyoTanshinChecked());
		if (dto.getHitsuyoRiyu() != null && CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())
				&& (CodeConstant.DOKUSHIN.equals(dto.getHitsuyoShataku()))) {
			dto.setRdoHitsuyoDokushinChecked(checkTrue);
		}

		// ラジオボタン 自動車の保管場所 必要とする
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())) {
			dto.setRdoCarHitsuyoChecked(checkTrue);
			LogUtils.debugByMsg(msg + "自動車の保管場所 必要とする " + dto.getParkingUmu() + dto.getRdoCarHitsuyoChecked());
		}

		// ラジオボタン 自動車の保管場所 必要としない
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_FUYO.equals(dto.getParkingUmu())) {
			dto.setRdoCarFuyoChecked(checkTrue);
			LogUtils.debugByMsg(msg + "自動車の保管場所 必要としない " + dto.getParkingUmu() + dto.getRdoCarFuyoChecked());
		}

		// ラジオボタン 自動車の保有 保有している 1台目
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())
				&& (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg()))) {
			dto.setRdo1stCarHoyuChecked(checkTrue);
			LogUtils.debugByMsg(msg + "自動車の保有 保有している " + dto.getCarNoInputFlg() + dto.getRdo1stCarHoyuChecked());
		}

		// ラジオボタン 自動車の保有 購入予定 2台目
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())
				&& (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg()))) {
			dto.setRdo1stCarYoteiChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 購入予定" + dto.getCarNoInputFlg() + dto.getRdo1stCarYoteiChecked());
		}

		// ラジオボタン 自動車の保有 保有している 2台目
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())
				&& (CodeConstant.CAR_HOYU.equals(dto.getCarNoInputFlg2()))) {
			dto.setRdo2stCarHoyuChecked(checkTrue);
			LogUtils.debugByMsg(msg + "自動車の保有 保有している " + dto.getCarNoInputFlg2() + dto.getRdo1stCarHoyuChecked());
		}

		// ラジオボタン 自動車の保有 購入予定 2台目
		if (dto.getParkingUmu() != null && CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())
				&& (CodeConstant.CAR_YOTEI.equals(dto.getCarNoInputFlg2()))) {
			dto.setRdo2stCarYoteiChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 購入予定" + dto.getCarNoInputFlg2() + dto.getRdo2stCarYoteiChecked());
		}
		// ラジオボタン 現居住宅 保有（会社借上含む）
		if (dto.getNowShataku() != null && CodeConstant.GENNYUKYO_SHATAKU_KBN_HOYU.equals(dto.getNowShataku())) {
			dto.setRdoNowJutakuHoyuChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 現居住宅 保有（会社借上含む）" + dto.getNowShataku() + dto.getRdoNowJutakuHoyuChecked());
		}
		// ラジオボタン 現居住宅 自宅
		if (dto.getNowShataku() != null && CodeConstant.GENNYUKYO_SHATAKU_KBN_JITAKU.equals(dto.getNowShataku())) {
			dto.setRdoNowJutakuJitakuChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 現居住宅 自宅" + dto.getNowShataku() + dto.getRdoNowJutakuJitakuChecked());
		}
		// ラジオボタン 現居住宅 自己借上
		if (dto.getNowShataku() != null
				&& CodeConstant.GENNYUKYO_SHATAKU_KBN_JIKO_KARIAGE.equals(dto.getNowShataku())) {
			dto.setRdoNowJutakuKariageChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 現居住宅 自己借上" + dto.getNowShataku() + dto.getRdoNowJutakuKariageChecked());
		}
		// ラジオボタン 現居住宅 その他
		if (dto.getNowShataku() != null && CodeConstant.GENNYUKYO_SHATAKU_KBN_OTHERS.equals(dto.getNowShataku())) {
			dto.setRdoNowJutakuSonotaChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 現居住宅 その他" + dto.getNowShataku() + dto.getRdoNowJutakuSonotaChecked());
		}
		// ラジオボタン 現保有の社宅 退居する
		if (dto.getTaikyoYotei() != null && CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
			dto.setRdoNowHoyuShatakuTaikyoChecked(checkTrue);
			LogUtils.debugByMsg(msg + " 現保有の社宅 退居する" + dto.getTaikyoYotei() + dto.getRdoNowHoyuShatakuTaikyoChecked());
		}
		// ラジオボタン 現保有の社宅 継続利用する
		if (dto.getTaikyoYotei() != null && CodeConstant.NOT_LEAVE.equals(dto.getTaikyoYotei())) {
			dto.setRdoNowHoyuShatakuKeizokuChecked(checkTrue);
			LogUtils.debugByMsg(
					msg + " 現保有の社宅  継続利用する" + dto.getTaikyoYotei() + dto.getRdoNowHoyuShatakuKeizokuChecked());
		}

	}

	/**
	 * ドロップダウンリストの値設定
	 * 
	 * @param dto
	 */
	@SuppressWarnings("unchecked")
	protected void setControlDdl(Skf2020Sc002CommonDto dto) {

		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> nowShatakuNameList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> taikyoRiyuKbnList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> returnWitnessRequestDateList = new ArrayList<Map<String, Object>>();

		// ドロップダウンリストを取得する
		Map<String, Object> returnMap = getDropDownLists(dto);

		// 画面表示するドロップダウンリストを取得
		agencyList.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AGENCY_LIST));
		affiliation1List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AFFILIATION1_LIST));
		affiliation2List
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_AFFILIATION2_LIST));
		nowShatakuNameList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_NOW_SHATAKU_NAME_LIST));
		taikyoRiyuKbnList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_TAIKYO_RIYU_KBN_LIST));
		returnWitnessRequestDateList
				.addAll((List<Map<String, Object>>) returnMap.get(Skf2020Sc002SharedService.KEY_SESSION_TIME_LIST));

		// dtoに値をセット
		dto.setDdlAgencyList(agencyList);
		dto.setDdlAffiliation1List(affiliation1List);
		dto.setDdlAffiliation2List(affiliation2List);
		dto.setDdlNowShatakuNameList(nowShatakuNameList);
		dto.setDdlTaikyoRiyuKbnList(taikyoRiyuKbnList);
		dto.setDdlReturnWitnessRequestDateList(returnWitnessRequestDateList);
	}

	/**
	 * ドロップダウンリストの値を取得
	 * 
	 * @param dto
	 * @return Map
	 */
	private Map<String, Object> getDropDownLists(Skf2020Sc002CommonDto dto) {

		// 戻り値用Mapのインスタンス生成
		Map<String, Object> returnMap = new HashMap<String, Object>();

		// 機関ドロップダウンリスト
		List<Map<String, Object>> agencyList = new ArrayList<Map<String, Object>>();
		agencyList.addAll(skfDropDownUtils.getDdlAgencyByCd(CodeConstant.C001, dto.getAgencyCd(), true));
		returnMap.put(KEY_AGENCY_LIST, agencyList);

		// 部等ドロップダウンリスト
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		affiliation1List.addAll(skfDropDownUtils.getDdlAffiliation1ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), true));
		// その他を追加
		if (affiliation1List.size() > 1) {
			Map<String, Object> soshikiMap = new HashMap<String, Object>();
			soshikiMap.put("value", "99");
			soshikiMap.put("label", "その他");
			affiliation1List.add(soshikiMap);
		}
		returnMap.put(KEY_AFFILIATION1_LIST, affiliation1List);
		LogUtils.debugByMsg("返却する部等リスト：" + affiliation1List);

		// 室、チーム又は課ドロップダウンリスト
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();
		affiliation2List.addAll(skfDropDownUtils.getDdlAffiliation2ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), dto.getAffiliation2Cd(), true));
		// その他を追加
		if (affiliation2List.size() > 1) {
			Map<String, Object> teamMap = new HashMap<String, Object>();
			teamMap.put("value", "99");
			teamMap.put("label", "その他");
			affiliation2List.add(teamMap);
		}
		returnMap.put(KEY_AFFILIATION2_LIST, affiliation2List);

		// 保有社宅名ドロップダウンリストの設定
		List<Map<String, Object>> nowShatakuNameList = new ArrayList<Map<String, Object>>();
		nowShatakuNameList.addAll(skfDropDownUtils.getDdlNowShatakuNameByCd(dto.getShainNo(), dto.getYearMonthDay(),
				dto.getShatakuKanriNo(), false));
		returnMap.put(KEY_NOW_SHATAKU_NAME_LIST, nowShatakuNameList);

		// 退居理由ドロップダウンリストの設定
		List<Map<String, Object>> taikyoRiyuKbnList = new ArrayList<Map<String, Object>>();
		taikyoRiyuKbnList.addAll(skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, dto.getTaikyoRiyuKbn(), true));
		returnMap.put(KEY_TAIKYO_RIYU_KBN_LIST, taikyoRiyuKbnList);

		// 返却立会希望日(時)ドロップダウンリストの設定
		List<Map<String, Object>> returnWitnessRequestDateList = new ArrayList<Map<String, Object>>();
		returnWitnessRequestDateList.addAll(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, dto.getSessionTime(), true));
		returnMap.put(KEY_SESSION_TIME_LIST, returnWitnessRequestDateList);

		return returnMap;
	}

	/**
	 * 駐車場の有無判定
	 * 
	 * @return 判定結果(0:駐車場なし、1:駐車場1、2:駐車場2、3:駐車場1と2)
	 */
	protected int checkParking(String parking1stPlace, String parking2stPlace) {
		int retCheck = 0;
		LogUtils.debugByMsg("駐車場の有無判定" + parking1stPlace + parking2stPlace);
		if (NfwStringUtils.isNotEmpty(parking1stPlace)) {
			// 駐車場1が存在する
			if (NfwStringUtils.isNotEmpty(parking2stPlace)) {
				// 駐車場2が存在する
				retCheck = enmCheckParking.ParkingBoth.getInt();
			} else {
				// 駐車場2が存在しない
				retCheck = enmCheckParking.Parking1st.getInt();
			}
		} else {
			// 駐車場1が存在しない
			if (NfwStringUtils.isNotEmpty(parking2stPlace)) {
				// 駐車場2が存在する
				retCheck = enmCheckParking.Parking2nd.getInt();
			}
		}

		return retCheck;
	}

	/**
	 * 情報のクリア
	 * 
	 * @param dto
	 */
	protected void setClearInfo(Skf2020Sc002CommonDto dto) {

		String Msg = "クリア処理　：";

		// 前画面が申請内容確認以外の場合はクリア
		if (!dto.getPrePageId().equals(FunctionIdConstant.SKF2010_SC002)) {

			// TEL
			dto.setTel(null);
			LogUtils.debugByMsg(Msg + "新所属-機関" + dto.getTel());

			// 社宅を必要としますか？
			dto.setTaiyoHituyo(null);
			LogUtils.debugByMsg(Msg + "社宅を必要としますか？" + dto.getTaiyoHituyo());

			// 社宅を必要とする理由
			dto.setHitsuyoRiyu(null);
			LogUtils.debugByMsg(Msg + "社宅を必要とする理由" + dto.getHitsuyoRiyu());

			// 社宅を必要としない理由
			dto.setFuhitsuyoRiyu(null);
			LogUtils.debugByMsg(Msg + "社宅を必要としない理由" + dto.getFuhitsuyoRiyu());

			// 新所属-機関
			dto.setDdlAgencyList(null);
			LogUtils.debugByMsg(Msg + "新所属-機関" + dto.getDdlAgencyList());

			// 新所属-機関 その他
			dto.setNewAgencyOther(null);
			LogUtils.debugByMsg(Msg + " 新所属-機関 その他" + dto.getNewAgencyOther());

			// 新所属-部等
			dto.setDdlAffiliation1List(null);
			LogUtils.debugByMsg(Msg + "新所属-部等" + dto.getDdlAffiliation1List());

			// 新所属-部等 その他
			dto.setNewAffiliation1Other(null);
			LogUtils.debugByMsg(Msg + "新所属-部等 その他" + dto.getNewAffiliation1Other());

			// 新所属-室、チーム又は課
			dto.setDdlAffiliation2List(null);
			LogUtils.debugByMsg(Msg + "新所属-室、チーム又は課" + dto.getDdlAffiliation2List());

			// 新所属-室、チーム又は課 その他
			dto.setNewAffiliation2Other(null);
			LogUtils.debugByMsg(Msg + "新所属-室、チーム又は課 その他" + dto.getNewAffiliation2Other());

			// 必要とする社宅
			dto.setHitsuyoShataku(null);
			LogUtils.debugByMsg(Msg + "必要とする社宅" + dto.getHitsuyoShataku());

			// 続柄
			dto.setDokyoRelation1(null);
			dto.setDokyoRelation2(null);
			dto.setDokyoRelation3(null);
			dto.setDokyoRelation4(null);
			dto.setDokyoRelation5(null);
			dto.setDokyoRelation6(null);
			LogUtils.debugByMsg(Msg + "続柄" + dto.getDokyoRelation1() + dto.getDokyoRelation2() + dto.getDokyoRelation3()
					+ dto.getDokyoRelation4() + dto.getDokyoRelation5() + dto.getDokyoRelation6());

			// 氏名
			dto.setDokyoName1(null);
			dto.setDokyoName2(null);
			dto.setDokyoName3(null);
			dto.setDokyoName4(null);
			dto.setDokyoName5(null);
			dto.setDokyoName6(null);
			LogUtils.debugByMsg(Msg + "氏名" + dto.getDokyoName1() + dto.getDokyoName1() + dto.getDokyoName1()
					+ dto.getDokyoName1() + dto.getDokyoName1() + dto.getDokyoName1());

			// 年齢
			dto.setDokyoAge1(null);
			dto.setDokyoAge2(null);
			dto.setDokyoAge3(null);
			dto.setDokyoAge4(null);
			dto.setDokyoAge5(null);
			dto.setDokyoAge6(null);

			// 入居希望日
			dto.setNyukyoYoteiDate(null);

			// 自動車の保管場所
			dto.setParkingUmu(null);

			// 自動車の保有
			dto.setCarNoInputFlg(null);

			// １台目-車名
			dto.setCarName(null);
			// １台目-自動車の登録番号
			dto.setCarNo(null);
			// 1台目-車検の有効期間満了日
			dto.setCarExpirationDate(null);
			// １台目-自動車の使用者
			dto.setCarUser(null);
			// １台目-駐車場の使用開始日
			dto.setParkingUseDate(null);

			// 自動車の保有
			dto.setCarNoInputFlg2(null);

			// ２台目-車名
			dto.setCarName2(null);
			// ２台目-自動車の登録番号
			dto.setCarNo2(null);
			// ２台目-車検の有効期間満了日
			dto.setCarExpirationDate2(null);
			// ２台目-自動車の使用者
			dto.setCarUser2(null);
			// ２台目-駐車場の使用開始日
			dto.setParkingUseDate2(null);

			// 現居住宅
			dto.setNowShataku(null);

			// 現居住宅-保有社宅名

			// 特殊事情等
			dto.setTokushuJijo(null);

			// 現保有の社宅
			dto.setTaikyoYotei(null);

			// 退居予定日
			dto.setTaikyoYoteiDate(null);

			// 社宅の状態
			dto.setShatakuJyotai(null);

			// 退居理由
			dto.setTaikyoRiyu(null);

			// 退居後の連絡先
			dto.setTaikyogoRenrakuSaki(null);
			// 返却備品情報のクリア
			dto.setReturnEquipment(null);

			// 返却立会希望日
			dto.setSessionDay(null);
			dto.setSessionTime(null);

			// 連絡先
			dto.setRenrakuSaki(null);
		}
	}

	/**
	 * バイト数カット処理
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	protected void cutByte(Skf2020Sc002CommonDto dto) throws UnsupportedEncodingException {

		String Msg = "バイト数カット処理：　";
		// 新所属 機関
		dto.setNewAgency(NfwStringUtils.rightTrimbyByte(dto.getNewAgency(), 128));
		LogUtils.debugByMsg(Msg + "新所属-機関" + dto.getNewAgency());
		// 新所属 部等
		dto.setNewAffiliation1Other(NfwStringUtils.rightTrimbyByte(dto.getNewAffiliation1Other(), 128));
		LogUtils.debugByMsg(Msg + "新所属 部等" + dto.getNewAffiliation1Other());
		// 新所属 室、チーム又は課
		dto.setNewAffiliation2Other(NfwStringUtils.rightTrimbyByte(dto.getNewAffiliation2Other(), 128));
		LogUtils.debugByMsg(Msg + "新所属 室、チーム又は課" + dto.getNewAffiliation2Other());
		// 続柄
		cutByteZokugara(dto);
		// 氏名
		cutByteShimei(dto);
		// 年齢
		cutByteNenrei(dto);
		// 入居希望日
		dto.setNyukyoYoteiDate(NfwStringUtils.rightTrimbyByte(dto.getNyukyoYoteiDate(), 10));
		LogUtils.debugByMsg(Msg + "入居希望日" + dto.getNyukyoYoteiDate());
		// 自動車の車名(１台目)
		dto.setCarName(NfwStringUtils.rightTrimbyByte(dto.getCarName(), 66));
		LogUtils.debugByMsg(Msg + "自動車の車名(１台目)" + dto.getCarName());
		// 自動車の登録番号(１台目)
		dto.setCarNo(NfwStringUtils.rightTrimbyByte(dto.getCarNo(), 20));
		LogUtils.debugByMsg(Msg + "自動車の登録番号(１台目)" + dto.getCarNo());
		// 車検の有効期間満了日(１台目)
		dto.setCarExpirationDate(NfwStringUtils.rightTrimbyByte(dto.getCarExpirationDate(), 10));
		LogUtils.debugByMsg(Msg + "車検の有効期間満了日(１台目)" + dto.getCarExpirationDate());
		// 自動車の使用者(１台目)
		dto.setCarUser(NfwStringUtils.rightTrimbyByte(dto.getCarUser(), 66));
		LogUtils.debugByMsg(Msg + "自動車の使用者(１台目)" + dto.getCarUser());
		// 自動車の保管場所使用開始日(１台目)
		dto.setParkingUseDate(NfwStringUtils.rightTrimbyByte(dto.getParkingUseDate(), 10));
		LogUtils.debugByMsg(Msg + "自動車の保管場所使用開始日(１台目)" + dto.getParkingUseDate());
		// 自動車の車名(２台目)
		dto.setCarName2(NfwStringUtils.rightTrimbyByte(dto.getCarName2(), 66));
		LogUtils.debugByMsg(Msg + "自動車の車名(2台目)" + dto.getCarName2());
		// 自動車の登録番号(２台目)
		dto.setCarNo2(NfwStringUtils.rightTrimbyByte(dto.getCarNo2(), 20));
		LogUtils.debugByMsg(Msg + "自動車の登録番号(2台目)" + dto.getCarNo2());
		// 車検の有効期間満了日(２台目)
		dto.setCarExpirationDate2(NfwStringUtils.rightTrimbyByte(dto.getCarExpirationDate2(), 10));
		LogUtils.debugByMsg(Msg + "車検の有効期間満了日(2台目)" + dto.getCarExpirationDate2());
		// 自動車の使用者(２台目)
		dto.setCarUser2(NfwStringUtils.rightTrimbyByte(dto.getCarUser2(), 66));
		LogUtils.debugByMsg(Msg + "自動車の使用者(2台目)" + dto.getCarUser2());
		// 自動車の保管場所使用開始日(２台目)
		dto.setParkingUseDate2(NfwStringUtils.rightTrimbyByte(dto.getParkingUseDate2(), 10));
		LogUtils.debugByMsg(Msg + "自動車の保管場所使用開始日(2台目)" + dto.getParkingUseDate2());
		// 室番号
		dto.setNowShatakuNo(NfwStringUtils.rightTrimbyByte(dto.getNowShatakuNo(), 6));
		LogUtils.debugByMsg(Msg + "室番号" + dto.getNowShatakuNo());
		// 規格(間取り)
		dto.setNowShatakuKikaku(NfwStringUtils.rightTrimbyByte(dto.getNowShatakuKikaku(), 10));
		LogUtils.debugByMsg(Msg + "規格(間取り)" + dto.getNowShatakuKikaku());
		// 面積
		dto.setNowShatakuMenseki(NfwStringUtils.rightTrimbyByte(dto.getNowShatakuMenseki(), 6));
		LogUtils.debugByMsg(Msg + "面積" + dto.getNowShatakuMenseki());
		// 特殊事情等
		dto.setTokushuJijo(NfwStringUtils.rightTrimbyByte(dto.getTokushuJijo(), 256));
		LogUtils.debugByMsg(Msg + "特殊事情等" + dto.getTokushuJijo());
		// 退居予定日
		dto.setTaikyoYoteiDate(NfwStringUtils.rightTrimbyByte(dto.getTaikyoYoteiDate(), 10));
		LogUtils.debugByMsg(Msg + "退居予定日" + dto.getTaikyoYoteiDate());
		// 社宅の状態
		dto.setShatakuJyotai(NfwStringUtils.rightTrimbyByte(dto.getShatakuJyotai(), 256));
		LogUtils.debugByMsg(Msg + "社宅の状態" + dto.getShatakuJyotai());
		// 退居理由
		dto.setTaikyoRiyu(NfwStringUtils.rightTrimbyByte(dto.getTaikyoRiyu(), 256));
		LogUtils.debugByMsg(Msg + "退居理由" + dto.getTaikyoRiyu());
		// 退居後の連絡先
		dto.setTaikyogoRenrakuSaki(NfwStringUtils.rightTrimbyByte(dto.getTaikyogoRenrakuSaki(), 128));
		LogUtils.debugByMsg(Msg + "退居後の連絡先" + dto.getTaikyogoRenrakuSaki());
		// 返却立会希望日
		dto.setSessionDay(NfwStringUtils.rightTrimbyByte(dto.getSessionDay(), 10));
		LogUtils.debugByMsg(Msg + "返却立会希望日" + dto.getSessionDay());
	}

	/**
	 * 続柄項目のバイト数をカットするメソッド
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	private void cutByteZokugara(Skf2020Sc002CommonDto dto) throws UnsupportedEncodingException {
		String Msg = "バイト数カット処理：　";
		// 続柄のバイト数をカット
		dto.setDokyoRelation1(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation1(), 10));
		LogUtils.debugByMsg(Msg + "続柄1" + dto.getDokyoRelation1());
		dto.setDokyoRelation2(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation2(), 10));
		LogUtils.debugByMsg(Msg + "続柄2" + dto.getDokyoRelation2());
		dto.setDokyoRelation3(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation3(), 10));
		LogUtils.debugByMsg(Msg + "続柄3" + dto.getDokyoRelation3());
		dto.setDokyoRelation4(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation4(), 10));
		LogUtils.debugByMsg(Msg + "続柄4" + dto.getDokyoRelation4());
		dto.setDokyoRelation5(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation5(), 10));
		LogUtils.debugByMsg(Msg + "続柄5" + dto.getDokyoRelation5());
		dto.setDokyoRelation6(NfwStringUtils.rightTrimbyByte(dto.getDokyoRelation6(), 10));
		LogUtils.debugByMsg(Msg + "続柄6" + dto.getDokyoRelation5());
	}

	/**
	 * 氏名項目のバイト数をカットするメソッド
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	private void cutByteShimei(Skf2020Sc002CommonDto dto) throws UnsupportedEncodingException {
		String Msg = "バイト数カット処理：　";
		// 氏名のバイト数をカット
		dto.setDokyoName1(NfwStringUtils.rightTrimbyByte(dto.getDokyoName1(), 26));
		LogUtils.debugByMsg(Msg + "氏名1" + dto.getDokyoName1());
		dto.setDokyoName2(NfwStringUtils.rightTrimbyByte(dto.getDokyoName2(), 26));
		LogUtils.debugByMsg(Msg + "氏名2" + dto.getDokyoName2());
		dto.setDokyoName3(NfwStringUtils.rightTrimbyByte(dto.getDokyoName3(), 26));
		LogUtils.debugByMsg(Msg + "氏名3" + dto.getDokyoName3());
		dto.setDokyoName4(NfwStringUtils.rightTrimbyByte(dto.getDokyoName4(), 26));
		LogUtils.debugByMsg(Msg + "氏名4" + dto.getDokyoName4());
		dto.setDokyoName5(NfwStringUtils.rightTrimbyByte(dto.getDokyoName5(), 26));
		LogUtils.debugByMsg(Msg + "氏名5" + dto.getDokyoName5());
		dto.setDokyoName6(NfwStringUtils.rightTrimbyByte(dto.getDokyoName6(), 26));
		LogUtils.debugByMsg(Msg + "氏名6" + dto.getDokyoName6());
	}

	/**
	 * 年齢項目のバイト数をカットするメソッド
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	private void cutByteNenrei(Skf2020Sc002CommonDto dto) throws UnsupportedEncodingException {
		String Msg = "バイト数カット処理：　";
		// 年齢のバイト数をカット
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge1(), 3));
		LogUtils.debugByMsg(Msg + "年齢1" + dto.getDokyoAge1());
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge2(), 3));
		LogUtils.debugByMsg(Msg + "年齢2" + dto.getDokyoAge2());
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge3(), 3));
		LogUtils.debugByMsg(Msg + "年齢3" + dto.getDokyoAge3());
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge4(), 3));
		LogUtils.debugByMsg(Msg + "年齢4" + dto.getDokyoAge4());
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge5(), 3));
		LogUtils.debugByMsg(Msg + "年齢5" + dto.getDokyoAge5());
		dto.setDokyoAge1(NfwStringUtils.rightTrimbyByte(dto.getDokyoAge6(), 3));
		LogUtils.debugByMsg(Msg + "年齢6" + dto.getDokyoAge6());
	}

	/**
	 * 新規時の保存処理
	 * 
	 * @param dto
	 * @param applInfo
	 * @return 登録成功 true 失敗 false
	 */
	protected boolean saveNewData(Skf2020Sc002CommonDto dto, Map<String, String> applInfo) {
		// 申請書類管理番号を取得
		String newApplNo = skfShinseiUtils.getApplNo(CodeConstant.C001, applInfo.get("shainNo"),
				FunctionIdConstant.R0100);
		LogUtils.debugByMsg("申請書類管理番号" + newApplNo);
		// 取得に失敗した場合
		if (newApplNo == null) {
			// エラーメッセージを表示用に設定
			ServiceHelper.addResultMessage(dto, null, MessageIdConstant.E_SKF_1094);
			// 保存処理を終了
			return false;
		} else {
			applInfo.put("applNo", newApplNo);
		}

		// 申請書類履歴テーブル登録処理
		insertApplHistory(dto, applInfo);
		// 入居希望等調書申請テーブル登録処理
		int registCount = 0;
		applInfo.put("updateFlg", "0");
		// 入居希望等調書申請テーブルの設定
		Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
		setValue = setNyukyoChoshoTsuchi(dto, setValue, applInfo);
		// 登録
		registCount = skf2020TNyukyoChoshoTsuchiRepository.insertSelective(setValue);
		LogUtils.debugByMsg("入居希望等調書決定通知テーブル登録件数：" + registCount + "件");

		// ステータスを更新
		dto.setStatus(applInfo.get("newStatus"));
		// 申請書番号を設定
		dto.setApplNo(newApplNo);

		return true;
	}

	/**
	 * 備品返却申請の書類管理番号設定
	 * 
	 * @param dto
	 * @return 備品返却の書類管理番号
	 */
	protected String getBihinHenkyaku(Skf2020Sc002CommonDto dto) {

		// 退居社宅がある場合は備品返却の作成
		String bihinHenkaykuShinseiApplNo = CodeConstant.DOUBLE_QUOTATION;

		if (dto.getShatakuKanriId() > 0 && CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
			// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
			Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
			Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpParameter param = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpParameter();
			param.setCompanyCd(CodeConstant.C001);
			param.setApplNo(dto.getApplNo());
			bihinHenkyakuInfo = skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpRepository
					.getBihinHenkyakuShinseiApplNoInfo(param);
			if (bihinHenkyakuInfo != null) {
				bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getApplNo();
			}
		}
		return bihinHenkaykuShinseiApplNo;
	}

	/**
	 * 備品返却申請テーブルの新規登録
	 * 
	 * @param bihinHenkaykuShinseiApplNo
	 * @param applInfo
	 * @param applDate
	 * @param applDate2
	 */
	protected void insertBihinHenkyakuInfo(String bihinHenkaykuShinseiApplNo, Skf2020Sc002CommonDto dto,
			Map<String, String> applInfo) {

		// 備品返却申請テーブルの設定
		Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
		setValue = setColumnInfoBihinList(setValue, dto, applInfo, bihinHenkaykuShinseiApplNo);
		// 登録
		int registCount = 0;
		registCount = skf2050TBihinHenkyakuShinseiRepository.insertSelective(setValue);
		LogUtils.debugByMsg("備品返却申請テーブル登録件数：" + registCount + "件");
	}

	/**
	 * 備品返却申請テーブルの更新値を設定
	 * 
	 * @param setValue 備品返却申請テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @param applInfo 申請書情報Map
	 * @param bihinHenkaykuShinseiApplNo 備品返却申請の申請書類番号
	 * @return 備品返却申請テーブルを更新する値
	 */
	protected Skf2050TBihinHenkyakuShinsei setColumnInfoBihinList(Skf2050TBihinHenkyakuShinsei setValue,
			Skf2020Sc002CommonDto dto, Map<String, String> applInfo, String bihinHenkaykuShinseiApplNo) {

		String applDate = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		// 更新SQLでは不要
		if (!UPDATE_FLG.equals(applInfo.get("dateUpdateFlg"))) {

			// 会社コード
			setValue.setCompanyCd(CodeConstant.C001);
			// 申請書類番号
			setValue.setApplNo(dto.getApplNo());
			// 社員番号
			setValue.setShainNo(dto.getShainNo());
			// 所属 機関
			setValue.setAgency(dto.getAgencyName());
			// 所属 部等
			setValue.setAffiliation1(dto.getAffiliation1Name());
			// 所属 室、チーム又は課
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

		// 退居届書類管理番号
		setValue.setTaikyoApplNo(bihinHenkaykuShinseiApplNo);
		// 申請年月日
		setValue.setApplDate(applDate);
		// 社宅管理番号
		setValue.setShatakuNo(dto.getHdnNowShatakuKanriNo());
		// 部屋管理番号
		setValue.setRoomKanriNo(dto.getHdnNowShatakuRoomKanriNo());
		// 社宅名
		setValue.setNowShatakuName(dto.getHdnSelectedNowShatakuName());
		// 号室
		setValue.setNowShatakuNo(dto.getNowShatakuNo());
		// 本来規格
		setValue.setNowShatakuKikaku(dto.getHdnShatakuKikakuKbn());
		// 面積
		setValue.setNowShatakuMenseki(dto.getNowShatakuMenseki().replace("㎡", ""));
		// 返却立会希望日
		setValue.setSessionDay(dto.getSessionDay().replace("/", ""));
		// 返却立会希望日(時間)
		setValue.setSessionTime(dto.getSessionTime());
		// 連絡先
		setValue.setRenrakuSaki(dto.getRenrakuSaki());

		return setValue;

	}

	/**
	 * 入居希望等調査・入居決定通知テーブルの更新値を設定
	 * 
	 * @param dto
	 * @param setValue
	 * @param applInfo
	 * @return
	 */
	protected Skf2020TNyukyoChoshoTsuchi setNyukyoChoshoTsuchi(Skf2020Sc002CommonDto dto,
			Skf2020TNyukyoChoshoTsuchi setValue, Map<String, String> applInfo) {

		String applDate = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);

		// 登録項目をセット
		String msg = "入力項目　：";

		// 新規の場合セット
		if (CodeConstant.STRING_ZERO.equals(applInfo.get("updateFlg"))) {
			// 会社コード
			setValue.setCompanyCd(CodeConstant.C001);// 会社コード
			// 申請書番号の設定
			setValue.setApplNo(applInfo.get("applNo"));
			LogUtils.debugByMsg(msg + applInfo.get("applNo"));
		}
		// 入居希望等調書申請テーブルの設定
		// 登録項目をセット
		// 社宅を必要としますか？
		setValue.setTaiyoHitsuyo(dto.getTaiyoHituyo());
		// 社宅を必要とする理由
		setValue.setHitsuyoRiyu(dto.getHitsuyoRiyu());
		// 社宅を必要としない理由
		setValue.setFuhitsuyoRiyu(dto.getFuhitsuyoRiyu());
		// 社員番号
		setValue.setShainNo(dto.getShainNo());
		// 氏名
		setValue.setName(dto.getName());
		// 等級
		setValue.setTokyu(dto.getTokyuName());
		// 性別
		setValue.setGender(dto.getGender());
		// 現所属 機関
		setValue.setAgency(dto.getAgencyName());
		// 現所属 部等
		setValue.setAffiliation1(dto.getAffiliation1Name());
		// 現所属 室、チーム又は課
		setValue.setAffiliation2(dto.getAffiliation2Name());
		// TEL
		setValue.setTel(dto.getTel());
		// 申請日付
		setValue.setApplDate(applDate);
		// 新所属
		// 機関名称の取得
		Skf2020Sc002GetAgensyNameExp agensyList = new Skf2020Sc002GetAgensyNameExp();
		// DB検索処理
		Skf2020Sc002GetAgensyNameExpParameter param = new Skf2020Sc002GetAgensyNameExpParameter();
		param.setAgencyCd(dto.getAgencyCd());
		param.setAffiliation1Cd(dto.getAffiliation1Cd());
		param.setAffiliation2Cd(dto.getAffiliation2Cd());
		if (NfwStringUtils.isNotEmpty(dto.getAgencyCd())) {
			agensyList = skf2020Sc002GetAgensyNameExpRepository.getAgensyName(param);
		}

		// 機関ドロップダウンリストが選択されている場合
		LogUtils.debugByMsg(msg + dto.getAgencyCd() + agensyList.getAgencyName() + dto.getNewAgency());
		if (NfwStringUtils.isNotEmpty(dto.getAgencyCd())) {
			// 「新規追加支社」が選択されている場合は新所属-機関 その他をセット
			if (CodeConstant.OTHER_AGENCY_ITEM_VALUE.equals(dto.getAgencyCd())) {
				setValue.setNewAgency(dto.getNewAgency());
				setValue.setNewAgencyOther(CodeConstant.OTHER_AGENCY_FLG);
			} else {
				setValue.setNewAgency(agensyList.getAgencyName());
			}
		}
		// 新所属-部等
		// 新所属-部等ドロップダウンリストが選択されている場合
		LogUtils.debugByMsg(
				msg + dto.getAffiliation1Cd() + agensyList.getAffiliation1Name() + dto.getNewAffiliation1());
		if (NfwStringUtils.isNotEmpty(dto.getAffiliation1Cd())) {
			// 「新所属-部等」が選択されている場合は新所属-部等 その他をセット
			if (CodeConstant.OTHER_AGENCY_ITEM_VALUE.equals(dto.getAffiliation1Cd())) {
				setValue.setNewAffiliation1(dto.getNewAffiliation1Other());
				setValue.setNewAffiliation1Other(CodeConstant.OTHER_AGENCY_FLG);
			} else {
				setValue.setNewAffiliation1(agensyList.getAffiliation1Name());
			}
		}
		// 新所属-室、チーム又は課
		LogUtils.debugByMsg(
				msg + dto.getAffiliation2Cd() + agensyList.getAffiliation2Name() + dto.getNewAffiliation2());
		// 新所属-室、チーム又は課ドロップダウンリストが選択されている場合
		if (NfwStringUtils.isNotEmpty(dto.getAffiliation2Cd())) {
			// 「新所属-部等」が選択されている場合は新所属-部等 その他をセット
			if (CodeConstant.OTHER_AGENCY_ITEM_VALUE.equals(dto.getAffiliation2Cd())) {
				setValue.setNewAffiliation2(dto.getNewAffiliation2Other());
				setValue.setNewAffiliation2Other(CodeConstant.OTHER_AGENCY_FLG);
			} else {
				setValue.setNewAffiliation2(agensyList.getAffiliation2Name());
			}
		}
		// 必要とする社宅
		if (dto.getHitsuyoShataku() != null) {
			setValue.setHitsuyoShataku(dto.getHitsuyoShataku());
		}

		// 同居家族-続柄
		setValue.setDokyoRelation1(dto.getDokyoRelation1());
		setValue.setDokyoRelation2(dto.getDokyoRelation2());
		setValue.setDokyoRelation3(dto.getDokyoRelation3());
		setValue.setDokyoRelation4(dto.getDokyoRelation4());
		setValue.setDokyoRelation5(dto.getDokyoRelation5());
		setValue.setDokyoRelation6(dto.getDokyoRelation6());

		// 同居家族-氏名
		setValue.setDokyoName1(dto.getDokyoName1());
		setValue.setDokyoName2(dto.getDokyoName2());
		setValue.setDokyoName3(dto.getDokyoName3());
		setValue.setDokyoName4(dto.getDokyoName4());
		setValue.setDokyoName5(dto.getDokyoName5());
		setValue.setDokyoName6(dto.getDokyoName6());

		// 同居家族-年齢
		setValue.setDokyoAge1(dto.getDokyoAge1());
		setValue.setDokyoAge2(dto.getDokyoAge2());
		setValue.setDokyoAge3(dto.getDokyoAge3());
		setValue.setDokyoAge4(dto.getDokyoAge4());
		setValue.setDokyoAge5(dto.getDokyoAge5());
		setValue.setDokyoAge6(dto.getDokyoAge6());

		// 入居予定日
		if (NfwStringUtils.isNotEmpty(dto.getNyukyoYoteiDate())) {
			setValue.setNyukyoYoteiDate(dto.getNyukyoYoteiDate().replace("/", ""));
			LogUtils.debugByMsg(dto.getNyukyoYoteiDate());
		}
		// 保管場所を必要とするか
		setValue.setParkingUmu(dto.getParkingUmu());
		// 自動車の登録番号入力フラグ
		setValue.setCarNoInputFlg(dto.getCarNoInputFlg());
		// 自動車の車名(１台目)
		setValue.setCarName(dto.getCarName());
		// 自動車の登録番号(１台目)
		setValue.setCarNo(dto.getCarNo());
		// 車検の有効期間満了日(１台目)
		if (NfwStringUtils.isNotEmpty(dto.getCarExpirationDate())) {
			setValue.setCarExpirationDate(dto.getCarExpirationDate().replace("/", ""));
			LogUtils.debugByMsg(dto.getCarExpirationDate());
		}
		// 自動車の使用者(１台目)
		setValue.setCarUser(dto.getCarUser());
		// 保管場所使用開始日(１台目)
		if (NfwStringUtils.isNotEmpty(dto.getParkingUseDate())) {
			setValue.setParkingUseDate(dto.getParkingUseDate().replace("/", ""));
			LogUtils.debugByMsg(dto.getParkingUseDate());
		}
		// 自動車の登録番号入力フラグ2
		setValue.setCarNoInputFlg2(dto.getCarNoInputFlg2());
		// 自動車の登録番号入力フラグだけで、2台目の車両情報が入っていない場合
		if ((dto.getCarName2() == null && CheckUtils.isEmpty(dto.getCarName2()))
				|| (dto.getCarNo2() != null && !CheckUtils.isEmpty(dto.getCarNo2()))
				|| (dto.getCarExpirationDate2() != null && !CheckUtils.isEmpty(dto.getCarExpirationDate2()))
				|| (dto.getCarUser2() != null && !CheckUtils.isEmpty(dto.getCarUser2()))
				|| (dto.getParkingUseDate2() != null && !CheckUtils.isEmpty(dto.getParkingUseDate2()))) {
			setValue.setCarNoInputFlg2(null);
		}
		// 自動車の車名(2台目)
		setValue.setCarName2(dto.getCarName2());
		// 自動車の登録番号(2台目)
		setValue.setCarNo2(dto.getCarNo2());
		// 車検の有効期間満了日(2台目)
		if (NfwStringUtils.isNotEmpty(dto.getCarExpirationDate2())) {
			setValue.setCarExpirationDate2(dto.getCarExpirationDate2().replace("/", ""));
			LogUtils.debugByMsg(dto.getCarExpirationDate2());
		}
		// 自動車の使用者(2台目)
		setValue.setCarUser2(dto.getCarUser2());
		// 保管場所使用開始日(2台目)
		if (NfwStringUtils.isNotEmpty(dto.getParkingUseDate2())) {
			setValue.setParkingUseDate2(dto.getParkingUseDate2().replace("/", ""));
			LogUtils.debugByMsg(dto.getParkingUseDate2());
		}
		// 現居社宅
		setValue.setNowShataku(dto.getNowShataku());
		// 現居社宅-保有社宅名
		setValue.setNowShatakuName(dto.getHdnSelectedNowShatakuName());
		// 現居社宅-室番号
		setValue.setNowShatakuNo(dto.getNowShatakuNo());
		// 現居社宅-規格(間取り)
		setValue.setNowShatakuKikaku(dto.getNowShatakuKikaku());
		// 現居社宅-面積
		if (NfwStringUtils.isNotEmpty(dto.getNowShatakuMenseki())) {
			setValue.setNowShatakuMenseki(dto.getNowShatakuMenseki().replace("㎡", ""));
			LogUtils.debugByMsg(dto.getNowShatakuMenseki());
		}
		// 現保有の社宅
		setValue.setTaikyoYotei(dto.getTaikyoYotei());
		// 退居予定日
		if (NfwStringUtils.isNotEmpty(dto.getTaikyoYoteiDate())) {
			setValue.setTaikyoYoteiDate(dto.getTaikyoYoteiDate().replace("/", ""));
			LogUtils.debugByMsg(dto.getTaikyoYoteiDate());
		}
		// 退居理由
		setValue.setTaikyoRiyu(dto.getTaikyoRiyu());
		LogUtils.debugByMsg(dto.getTaikyoRiyu());
		// 退居理由区分
		setValue.setTaikyoRiyuKbn(dto.getTaikyoRiyuKbn());
		LogUtils.debugByMsg(dto.getTaikyoRiyuKbn());
		// 退居後連絡先
		setValue.setTaikyogoRenrakusaki(dto.getTaikyogoRenrakuSaki());
		// 特殊事情等
		setValue.setTokushuJijo(dto.getTokushuJijo());
		// 社宅管理番号
		setValue.setShatakuNo(dto.getHdnNowShatakuKanriNo());
		// 部屋管理番号
		setValue.setRoomKanriNo(dto.getHdnShatakuRoomKanriNo());
		// 社宅の状態
		setValue.setShatakuJotai(dto.getShatakuJyotai());
		// 返却立会希望日
		if (NfwStringUtils.isNotEmpty(dto.getSessionDay())) {
			setValue.setSessionDay(dto.getSessionDay().replace("/", ""));
		}
		// 返却立会希望日(時間)
		setValue.setSessionTime(dto.getSessionTime());
		// 連絡先
		setValue.setRenrakuSaki(dto.getRenrakuSaki());
		// 現在の保管場所
		setValue.setNowParkingArea(dto.getParking1stPlace());
		// 現在の位置番号
		setValue.setNowCarIchiNo(dto.getHdnParking1stNumber());
		// 現在の保管場所2
		setValue.setNowParkingArea2(dto.getParking2stPlace());
		// 現在の位置番号2
		setValue.setNowParkingArea2(dto.getHdnParking2stNumber());
		// 現在の社宅管理番号
		setValue.setNowShatakuKanriNo(dto.getHdnNowShatakuKanriNo());
		// 現在の部屋管理番号
		setValue.setNowRoomKanriNo(dto.getHdnShatakuRoomKanriNo());

		return setValue;
	}

	/**
	 * 申請書類履歴テーブル登録処理
	 * 
	 * @param dto
	 * @param applDate
	 * @param applInfo
	 * @param applTacFlg
	 */
	private void insertApplHistory(Skf2020Sc002CommonDto dto, Map<String, String> applInfo) {

		// 申請書類履歴テーブルの設定
		Skf2010TApplHistory setValue = new Skf2010TApplHistory();
		// 登録項目をセット
		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 社員番号
		setValue.setShainNo(dto.getShainNo());
		setValue.setApplDate(DateUtils.getSysDate());
		setValue.setApplNo(applInfo.get("applNo"));
		setValue.setApplId(FunctionIdConstant.R0100);
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		setValue.setComboFlg("1");
		// 登録
		int registCount = 0;
		registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
		LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");

		dto.setStatus(applInfo.get("newStatus"));
	}

	/**
	 * 申請書類履歴テーブル更新処理
	 * 
	 * @param setValue 申請書類履歴テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @param applInfo 申請書情報Map
	 * @return
	 */
	protected Skf2010TApplHistory updateApplHistoryAgreeStatusIchiji(Skf2010TApplHistory setValue,
			Skf2020Sc002CommonDto dto, Map<String, String> applInfo) {

		// 更新条件項目をセット
		setValue.setShainNo(dto.getShainNo());
		setValue.setApplDate(dto.getApplDate());
		setValue.setApplNo(dto.getApplNo());
		setValue.setApplId(dto.getApplId());
		if (UPDATE_FLG.equals(applInfo.get("dateUpdateFlg"))) {
			setValue.setLastUpdateDate(dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE));
		}
		// 更新項目をセット
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		return setValue;

	}

	/**
	 * 申請情報の取得
	 * 
	 * @param dto
	 * @return
	 */
	protected Map<String, String> getSkfApplInfo(Skf2020Sc002CommonDto dto) {
		Map<String, String> applInfoMap = new HashMap<String, String>();

		// 申請情報情報
		Skf2020Sc002GetApplInfoExp applInfo = new Skf2020Sc002GetApplInfoExp();
		Skf2020Sc002GetApplInfoExpParameter applParam = new Skf2020Sc002GetApplInfoExpParameter();
		applParam.setCompanyCd(CodeConstant.C001);
		applParam.setShainNo(dto.getShainNo());
		applParam.setApplNo(dto.getApplNo());
		applParam.setApplId(FunctionIdConstant.R0100);
		applInfo = skf2020Sc002GetApplInfoExpRepository.getApplInfo(applParam);
		if (applInfo != null) {
			// 社員番号
			applInfoMap.put("shainNo", applInfo.getShainNo());
			// 申請書ID
			applInfoMap.put("applId", applInfo.getApplId());
			// 申請書番号
			applInfoMap.put("applNo", applInfo.getApplNo());
			// 申請ステータス
			applInfoMap.put("status", applInfo.getStatus());
			// 申請書類有無フラグ
			applInfoMap.put("applTacFlg", applInfo.getApplTacFlg());
		} else {
			// 申請ステータス
			applInfoMap.put("status", CodeConstant.STATUS_MISAKUSEI);
		}
		return applInfoMap;
	}

	/**
	 * コメントボタンの表示非表示
	 * 
	 * @param dto
	 */
	protected void setCommentBtnDisabled(Skf2020Sc002CommonDto dto) {
		// コメントの設定
		List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentList = skfCommentUtils.getCommentInfo(companyCd, dto.getApplNo(), null);
		if (commentList == null || commentList.size() <= 0) {
			// コメントが無ければ非表示
			dto.setCommentViewFlag(FALSE);
		} else {
			// コメントがあれば表示
			dto.setCommentViewFlag(TRUE);
		}
	}

}
