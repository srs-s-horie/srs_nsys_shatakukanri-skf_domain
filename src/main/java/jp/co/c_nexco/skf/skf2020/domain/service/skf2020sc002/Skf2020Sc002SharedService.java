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
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuKanriIdExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuKanriIdExpParameter;
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
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetShatakuKanriIdExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
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
	public static final String NO_UPDATE_FLG = "0";
	public static final String UPDATE_FLG = "1";

	// 最終更新日付のキャッシュキー
	public static final String KEY_LAST_UPDATE_DATE = "skf2020_t_nyukyo_chosho_tsuchi";

	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreationUtils;
	@Autowired
	private CodeCacheUtils codeCacheUtils;

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
	private Skf2020Sc002GetAgensyCdExpRepository skf2020Sc002GetAgensyCdExpRepository;
	@Autowired
	private Skf2020Sc002GetAgensyNameExpRepository skf2020Sc002GetAgensyNameExpRepository;
	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;
	@Autowired
	private Skf2050TBihinHenkyakuShinseiRepository skf2050TBihinHenkyakuShinseiRepository;
	@Autowired
	private Skf2020Sc002UpdateNyukyoKiboInfoExpRepository skf2020Sc002UpdateNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc002GetShatakuKanriIdExpRepository skf2020Sc002GetShatakuKanriIdExpRepository;
	@Autowired
	private Skf2020Sc002UpdateBihinHenkyakuInfoExpRepository skf2020Sc002UpdateBihinHenkyakuInfoExpRepository;

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

			// 登録済みデータの情報設定
			setSinseiInfo(dto);

		} else {
			// 無い場合
			if (dto.getShainList() != null && dto.getShainList().size() > 0) {
				// 社員情報がある場合は申請者情報からの設定
				setShainList(dto);
			} else {
				// データが取得できなかった場合は更新ボタンを使用不可にする
				setInitializeError(dto);
			}
		}
		// 社宅情報の設定
		setShatakuInfo(dto, NO_UPDATE_FLG);
	}

	/**
	 * 社宅入居希望等調書の申請情報から初期表示項目を設定。
	 * 
	 * @param dto
	 */
	protected void setSinseiInfo(Skf2020Sc002CommonDto dto) {

		/**
		 * 社宅入居希望等調査・入居決定通知テーブル情報の取得
		 */
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
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAgency())) {
			LogUtils.debugByMsg("機関：" + nyukyoChoshoList.getAgency());
			dto.setAgencyName(nyukyoChoshoList.getAgency());
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAgency())) {
			LogUtils.debugByMsg("部等：" + nyukyoChoshoList.getAffiliation1());
			dto.setAffiliation1Name(nyukyoChoshoList.getAffiliation1());
		}
		// 室、チーム又は課
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getAffiliation2())) {
			LogUtils.debugByMsg("室、チーム又は課：" + nyukyoChoshoList.getAffiliation2());
			dto.setAffiliation2Name(nyukyoChoshoList.getAffiliation2());
		}
		// 勤務先のTEL
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTel())) {
			LogUtils.debugByMsg("勤務先のTEL：" + nyukyoChoshoList.getTel());
			dto.setTel(nyukyoChoshoList.getTel());
		}
		// 社員番号
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getShainNo())) {
			LogUtils.debugByMsg("社員番号：" + nyukyoChoshoList.getShainNo());
			dto.setShainNo(nyukyoChoshoList.getShainNo());
		}
		// 等級
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTokyu())) {
			LogUtils.debugByMsg("等級：" + nyukyoChoshoList.getTokyu());
			dto.setTokyuName(nyukyoChoshoList.getTokyu());
		}
		// 性別
		if (NfwStringUtils.isNotEmpty(String.valueOf(nyukyoChoshoList.getGender()))) {
			LogUtils.debugByMsg("性別：" + nyukyoChoshoList.getGender());
			dto.setGender(String.valueOf(nyukyoChoshoList.getGender()));
		}
		// 氏名
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getName())) {
			LogUtils.debugByMsg("氏名：" + nyukyoChoshoList.getName());
			dto.setName(nyukyoChoshoList.getName());
		}
		// 社宅を必要としますか？
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaiyoHitsuyo())) {
			LogUtils.debugByMsg("社宅を必要としますか？：" + nyukyoChoshoList.getTaiyoHitsuyo());
			dto.setTaiyoHituyo(nyukyoChoshoList.getTaiyoHitsuyo());
		}
		// 社宅を必要とする理由
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getHitsuyoRiyu())) {
			LogUtils.debugByMsg("社宅を必要とする理由：" + nyukyoChoshoList.getHitsuyoRiyu());
			dto.setHitsuyoRiyu(nyukyoChoshoList.getHitsuyoRiyu());
		}
		// 社宅を必要としない理由
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getFuhitsuyoRiyu())) {
			LogUtils.debugByMsg("社宅を必要としない理由：" + nyukyoChoshoList.getFuhitsuyoRiyu());
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
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAgency())
				&& NfwStringUtils.isNotEmpty(agensyList.getAgencyCd())) {
			LogUtils.debugByMsg("機関：" + nyukyoChoshoList.getNewAgency() + agensyList.getAgencyCd());
			// 機関ドロップダウンリストの設定
			dto.setDdlAgencyList(skfDropDownUtils.getDdlAgencyByCd(CodeConstant.C001, agensyList.getAgencyCd(), true));
		}

		// 部等
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation1())
				&& NfwStringUtils.isNotEmpty(agensyList.getAffiliation1Cd())) {
			LogUtils.debugByMsg("部等：" + nyukyoChoshoList.getNewAffiliation1()
					+ nyukyoChoshoList.getNewAffiliation1Other() + agensyList.getAffiliation1Cd());
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
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNewAffiliation2())
				&& NfwStringUtils.isNotEmpty(agensyList.getAffiliation2Cd())) {
			LogUtils.debugByMsg("室、チーム又は課：" + nyukyoChoshoList.getNewAffiliation2()
					+ nyukyoChoshoList.getNewAffiliation2Other() + agensyList.getAffiliation2Cd());
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
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getHitsuyoShataku())) {
			LogUtils.debugByMsg("必要とする社宅：" + nyukyoChoshoList.getHitsuyoShataku());
			dto.setHitsuyoShataku(nyukyoChoshoList.getHitsuyoShataku());
		}

		// 家族1
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoRelation1())) {
			LogUtils.debugByMsg("家族1：" + nyukyoChoshoList.getDokyoRelation1());
			dto.setDokyoRelation1(nyukyoChoshoList.getDokyoRelation1());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoName1())) {
			LogUtils.debugByMsg("家族1：" + nyukyoChoshoList.getDokyoName1());
			dto.setDokyoName1(nyukyoChoshoList.getDokyoName1());
		}
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getDokyoAge1())) {
			LogUtils.debugByMsg("家族1：" + nyukyoChoshoList.getDokyoAge1());
			dto.setDokyoAge1(nyukyoChoshoList.getDokyoAge1());
		}

		// 家族2
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
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNyukyoYoteiDate())) {
			LogUtils.debugByMsg("入居希望日：" + nyukyoChoshoList.getNyukyoYoteiDate());
			dto.setNyukyoYoteiDate(nyukyoChoshoList.getNyukyoYoteiDate());
		}

		// 自動者の保管場所
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUmu())) {
			LogUtils.debugByMsg("自動者の保管場所：" + nyukyoChoshoList.getParkingUmu());
			dto.setParkingUmu(nyukyoChoshoList.getParkingUmu());
		}

		// 自動車の登録番号入力フラグ(1台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNoInputFlg())) {
			LogUtils.debugByMsg("自動車の登録番号入力フラグ(1台目)：" + nyukyoChoshoList.getCarNoInputFlg());
			dto.setCarNoInputFlg(nyukyoChoshoList.getCarNoInputFlg());
		}

		// 自動車の車名(１台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarName())) {
			LogUtils.debugByMsg("自動車の車名(１台目)：" + nyukyoChoshoList.getCarName());
			dto.setCarName(nyukyoChoshoList.getCarName());
		}

		// 自動車の登録番号(１台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNo())) {
			LogUtils.debugByMsg("自動車の登録番号(１台目)：" + nyukyoChoshoList.getCarNo());
			dto.setCarNo(nyukyoChoshoList.getCarNo());
		}

		// 車検の有効期間満了日(１台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarExpirationDate())) {
			LogUtils.debugByMsg("車検の有効期間満了日(１台目)" + nyukyoChoshoList.getCarExpirationDate());
			dto.setCarExpirationDate(nyukyoChoshoList.getCarExpirationDate());
		}

		// 自動車の使用者(１台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarUser())) {
			LogUtils.debugByMsg("自動車の使用者(１台目)" + nyukyoChoshoList.getCarUser());
			dto.setCarUser(nyukyoChoshoList.getCarUser());
		}

		// 自動車の保管場所 使用開始日(予定日)(１台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUseDate())) {
			LogUtils.debugByMsg("自動車の保管場所 使用開始日(予定日)(１台目)" + nyukyoChoshoList.getParkingUseDate());
			dto.setParkingUseDate(nyukyoChoshoList.getParkingUseDate());
		}

		// 自動車の登録番号入力フラグ(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNoInputFlg2())) {
			LogUtils.debugByMsg("自動車の登録番号入力フラグ(2台目)：" + nyukyoChoshoList.getCarNoInputFlg2());
			dto.setCarNoInputFlg2(nyukyoChoshoList.getCarNoInputFlg2());
		}

		// 自動車の車名(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarName2())) {
			LogUtils.debugByMsg("自動車の車名(2台目)：" + nyukyoChoshoList.getCarName2());
			dto.setCarName2(nyukyoChoshoList.getCarName2());
		}

		// 自動車の登録番号(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarNo2())) {
			LogUtils.debugByMsg("自動車の登録番号(2台目)：" + nyukyoChoshoList.getCarNo2());
			dto.setCarNo2(nyukyoChoshoList.getCarNo2());
		}

		// 車検の有効期間満了日(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarExpirationDate2())) {
			LogUtils.debugByMsg("車検の有効期間満了日(2台目)" + nyukyoChoshoList.getCarExpirationDate2());
			dto.setCarExpirationDate2(nyukyoChoshoList.getCarExpirationDate2());
		}

		// 自動車の使用者(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getCarUser2())) {
			LogUtils.debugByMsg("自動車の使用者(2台目)" + nyukyoChoshoList.getCarUser2());
			dto.setCarUser2(nyukyoChoshoList.getCarUser2());
		}

		// 自動車の保管場所 使用開始日(予定日)(2台目)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getParkingUseDate2())) {
			LogUtils.debugByMsg("自動車の保管場所 使用開始日(予定日)(2台目)" + nyukyoChoshoList.getParkingUseDate2());
			dto.setParkingUseDate2(nyukyoChoshoList.getParkingUseDate2());
		}

		// 現居住宅
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNowShataku())) {
			LogUtils.debugByMsg("現居住宅" + nyukyoChoshoList.getNowShataku());
			dto.setNowShataku(nyukyoChoshoList.getNowShataku());
		}
		// 現保有の社宅名
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getNowShatakuName())) {
			LogUtils.debugByMsg("現保有の社宅名" + nyukyoChoshoList.getNowShatakuName());
			dto.setShatakuName(nyukyoChoshoList.getNowShatakuName());
		}
		// 特殊事情等
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTokushuJijo())) {
			LogUtils.debugByMsg("特殊事情等" + nyukyoChoshoList.getTokushuJijo());
			dto.setTokushuJijo(nyukyoChoshoList.getTokushuJijo());
		}
		// 現保有の社宅
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoYotei())) {
			LogUtils.debugByMsg("現保有の社宅" + nyukyoChoshoList.getTaikyoYotei());
			dto.setTaikyoYotei(nyukyoChoshoList.getTaikyoYotei());
		}
		// 退居予定日
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoYoteiDate())) {
			LogUtils.debugByMsg("退居予定日" + nyukyoChoshoList.getTaikyoYoteiDate());
			dto.setTaikyoYoteiDate(nyukyoChoshoList.getTaikyoYoteiDate());
		}
		// 社宅の状態
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getShatakuJotai())) {
			LogUtils.debugByMsg("社宅の状態" + nyukyoChoshoList.getShatakuJotai());
			dto.setShatakuJyotai(nyukyoChoshoList.getShatakuJotai());
		}
		//
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyoRiyuKbn())) {
			LogUtils.debugByMsg("退居理由" + nyukyoChoshoList.getTaikyoRiyuKbn() + nyukyoChoshoList.getTaikyoRiyu());
			// 退居理由ドロップダウンリストの設定
			dto.setTaikyoRiyuKbn(nyukyoChoshoList.getTaikyoRiyuKbn());
			dto.setDdlTaikyoRiyuKbnList(skfDropDownUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, dto.getTaikyoRiyuKbn(), true));
		}
		// 退居後の連絡先
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getTaikyogoRenrakusaki())) {
			LogUtils.debugByMsg("退居後の連絡先" + nyukyoChoshoList.getTaikyogoRenrakusaki());
			dto.setTaikyogoRenrakuSaki(nyukyoChoshoList.getTaikyogoRenrakusaki());
		}
		// 返却立会希望日(日)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getSessionDay())) {
			LogUtils.debugByMsg("返却立会希望日(日)" + nyukyoChoshoList.getSessionDay());
			dto.setSessionDay(nyukyoChoshoList.getSessionDay());
		}
		// 返却立会希望日(時)
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getSessionTime())) {
			LogUtils.debugByMsg("返却立会希望日(時)" + nyukyoChoshoList.getSessionTime());
			dto.setSessionTime(nyukyoChoshoList.getSessionTime());
			dto.setDdlReturnWitnessRequestDateList(skfDropDownUtils.getGenericForDoropDownList(
					FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, dto.getSessionTime(), true));
		}
		// 連絡先
		if (NfwStringUtils.isNotEmpty(nyukyoChoshoList.getRenrakuSaki())) {
			LogUtils.debugByMsg("連絡先" + nyukyoChoshoList.getRenrakuSaki());
			dto.setRenrakuSaki(nyukyoChoshoList.getRenrakuSaki());
		}

		// 社宅管理IDの取得
		if (nyukyoChoshoList.getNowShatakuKanriNo() > 0) {
			// 社宅管理IDの取得
			Skf2020Sc002GetShatakuKanriIdExp nowShatakuNameList = new Skf2020Sc002GetShatakuKanriIdExp();
			// DB検索処理
			Skf2020Sc002GetShatakuKanriIdExpParameter par = new Skf2020Sc002GetShatakuKanriIdExpParameter();
			par.setShatakuName(nyukyoChoshoList.getNowShatakuName());
			par.setShainNo(nyukyoChoshoList.getShainNo());
			par.setShatakuKanriNo(nyukyoChoshoList.getNowShatakuKanriNo());
			par.setShatakuRoomKanriNo(nyukyoChoshoList.getNowRoomKanriNo());
			nowShatakuNameList = skf2020Sc002GetShatakuKanriIdExpRepository.getShatakuKanriId(par);
			if (nowShatakuNameList.getShatakuKanriId() > 0) {
				dto.setDdlNowShatakuNameList(skfDropDownUtils.getDdlNowShatakuNameByCd(dto.getShainNo(),
						dto.getYearMonthDay(), nowShatakuNameList.getShatakuKanriId(), false));
			}
		}

		// 更新日時
		LogUtils.debugByMsg("更新日時" + nyukyoChoshoList.getUpdateDate());
		dto.addLastUpdateDate(KEY_LAST_UPDATE_DATE, nyukyoChoshoList.getUpdateDate());
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
	 * @param updateFlg
	 */
	protected void setShatakuInfo(Skf2020Sc002CommonDto dto, String updateFlg) {

		// Hidden
		Long hdnNowShatakuRoomKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅部屋管理番号
		Long hdnNowShatakuKanriNo = CodeConstant.LONG_ZERO;// 現居住社宅管理番号
		String hdnShatakuKikakuKbn = "";// 規格(間取り)

		long shatakuKanriId = CodeConstant.LONG_ZERO;
		if (updateFlg.equals(NO_UPDATE_FLG)) {
			// 現保有社宅の全量取得
			List<Skf2020Sc002GetNowShatakuNameExp> resultNowShatakuNameList = new ArrayList<Skf2020Sc002GetNowShatakuNameExp>();
			Skf2020Sc002GetNowShatakuNameExpParameter param = new Skf2020Sc002GetNowShatakuNameExpParameter();
			param.setShainNo(dto.getShainNo());
			param.setNyukyoDate(dto.getYearMonthDay());
			resultNowShatakuNameList = skf2020Sc002GetNowShatakuNameExpRepository.getNowShatakuName(param);

			dto.setShatakuKanriId(shatakuKanriId);
			if (resultNowShatakuNameList.size() > 0) {
				shatakuKanriId = resultNowShatakuNameList.get(0).getShatakuKanriId();
				dto.setShatakuKanriId(shatakuKanriId);
				// dto.setHdnSelectedNowShatakuName(resultNowShatakuNameList.get(0).getShatakuName());
			}

			// 現居住宅の選択された情報の取得
			List<Skf2020Sc002GetShatakuInfoExp> shatakuList = new ArrayList<Skf2020Sc002GetShatakuInfoExp>();
			shatakuList = getSelectShatakuInfo(shatakuKanriId, dto.getShainNo(), shatakuList);

			// 取得できた場合は現居住社宅の情報設定
			if (shatakuList.size() > 0) {

				// 社宅名
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getShatakuName())) {
					dto.setHdnSelectedNowShatakuName(shatakuList.get(0).getShatakuName());
					LogUtils.debugByMsg("社宅名" + dto.getHdnSelectedNowShatakuName());
				}

				// 室番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getRoomNo())) {
					dto.setNowShatakuNo(shatakuList.get(0).getRoomNo());
					LogUtils.debugByMsg("室番号" + dto.getNowShatakuNo());
				}
				// 規格(間取り)
				// 規格があった場合は、貸与規格。それ以外は本来規格
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getKikaku())) {
					hdnShatakuKikakuKbn = shatakuList.get(0).getKikaku();// 貸与規格
					dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikakuName(shatakuList.get(0).getKikakuName());
					LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
					LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
				} else {
					if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getOriginalKikaku())) {
						hdnShatakuKikakuKbn = shatakuList.get(0).getOriginalKikaku();// 本来規格
						dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
						dto.setNowShatakuKikakuName(shatakuList.get(0).getOriginalKikakuName());
						LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
						LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
					}
				}

				// 面積
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getLendMenseki())) {
					dto.setNowShatakuMenseki(shatakuList.get(0).getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
					LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
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
					LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());

				}

				// 駐車場 １台目 位置番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock1())) {
					dto.setHdnParking1stNumber(shatakuList.get(0).getParkingBlock1());
					LogUtils.debugByMsg("駐車場 １台目 位置番号" + dto.getHdnParking1stNumber());
				}

				// 駐車場 ２台目 保管場所
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress2())) {
					dto.setParking2stPlace(wkPrefName + shatakuList.get(0).getParkingAddress2());
					LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2stPlace());
				}

				// 駐車場 ２台目 位置番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock2())) {
					dto.setHdnParking2stNumber(shatakuList.get(0).getParkingBlock2());
					LogUtils.debugByMsg("駐車場 2台目 位置番号" + dto.getHdnParking2stNumber());
				}

				// 現在の社宅管理番号
				if (shatakuList.get(0).getShatakuKanriNo() != null) {
					hdnNowShatakuKanriNo = shatakuList.get(0).getShatakuKanriNo();
					dto.setHdnNowShatakuKanriNo(hdnNowShatakuKanriNo);
				}

				// 現在の部屋管理番号
				if (shatakuList.get(0).getShatakuRoomKanriNo() != null) {
					hdnNowShatakuRoomKanriNo = shatakuList.get(0).getShatakuRoomKanriNo();
					dto.setHdnNowShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
				}

				// リストに格納
				dto.setShatakuList(shatakuList);
			}

		} else {
			// 更新

			shatakuKanriId = Long.parseLong(dto.getNowShatakuName());
			if (shatakuKanriId > 0) {
				dto.setShatakuKanriId(shatakuKanriId);
				LogUtils.debugByMsg("社宅管理ID" + shatakuKanriId);
			}

			// 現居住宅の選択された情報の取得
			List<Skf2020Sc002GetShatakuInfoExp> shatakuList = new ArrayList<Skf2020Sc002GetShatakuInfoExp>();
			shatakuList = getSelectShatakuInfo(shatakuKanriId, dto.getShainNo(), shatakuList);

			// 取得できた場合は現居住社宅の情報設定
			if (shatakuList.size() > 0) {

				// 社宅名
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getShatakuName())) {
					dto.setHdnSelectedNowShatakuName(shatakuList.get(0).getShatakuName());
					LogUtils.debugByMsg("社宅名" + dto.getHdnSelectedNowShatakuName());
				}

				// 室番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getRoomNo())) {
					dto.setNowShatakuNo(shatakuList.get(0).getRoomNo());
					LogUtils.debugByMsg("室番号" + dto.getNowShatakuNo());
				}
				// 規格(間取り)
				// 規格があった場合は、貸与規格。それ以外は本来規格
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getKikaku())) {
					hdnShatakuKikakuKbn = shatakuList.get(0).getKikaku();// 貸与規格
					dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
					dto.setNowShatakuKikakuName(shatakuList.get(0).getKikakuName());
					LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
					LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
				} else {
					if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getOriginalKikaku())) {
						hdnShatakuKikakuKbn = shatakuList.get(0).getOriginalKikaku();// 本来規格
						dto.setNowShatakuKikaku(hdnShatakuKikakuKbn);
						dto.setNowShatakuKikakuName(shatakuList.get(0).getOriginalKikakuName());
						LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
						LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
					}
				}

				// 面積
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getLendMenseki())) {
					dto.setNowShatakuMenseki(shatakuList.get(0).getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
					LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
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
					LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());

				}

				// 駐車場 １台目 位置番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock1())) {
					dto.setHdnParking1stNumber(shatakuList.get(0).getParkingBlock1());
					LogUtils.debugByMsg("駐車場 １台目 位置番号" + dto.getHdnParking1stNumber());
				}

				// 駐車場 ２台目 保管場所
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingAddress2())) {
					dto.setParking2stPlace(wkPrefName + shatakuList.get(0).getParkingAddress2());
					LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2stPlace());
				}

				// 駐車場 ２台目 位置番号
				if (NfwStringUtils.isNotEmpty(shatakuList.get(0).getParkingBlock2())) {
					dto.setHdnParking2stNumber(shatakuList.get(0).getParkingBlock2());
					LogUtils.debugByMsg("駐車場 2台目 位置番号" + dto.getHdnParking2stNumber());
				}

				// 現在の社宅管理番号
				if (shatakuList.get(0).getShatakuKanriNo() != null) {
					hdnNowShatakuKanriNo = shatakuList.get(0).getShatakuKanriNo();
					dto.setHdnNowShatakuKanriNo(hdnNowShatakuKanriNo);
				}

				// 現在の部屋管理番号
				if (shatakuList.get(0).getShatakuRoomKanriNo() != null) {
					hdnNowShatakuRoomKanriNo = shatakuList.get(0).getShatakuRoomKanriNo();
					dto.setHdnNowShatakuRoomKanriNo(hdnNowShatakuRoomKanriNo);
				}

			}
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
	protected void setReturnBihinInfo(Skf2020Sc002CommonDto dto, String updateFlg) {

		// 返却備品有無に「0:備品返却しない」を設定
		dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SHINAI);

		// 備品状態が2:保有備品または3:レンタルの表示
		long shatakuKanriId = CodeConstant.LONG_ZERO;
		if (updateFlg.equals(NO_UPDATE_FLG)) {
			shatakuKanriId = dto.getShatakuKanriId();
		} else {
			// 選択したプルダウンの値
			shatakuKanriId = Long.parseLong(dto.getNowShatakuName());
		}
		List<Skf2020Sc002GetBihinItemToBeReturnExp> resultBihinItemList = new ArrayList<Skf2020Sc002GetBihinItemToBeReturnExp>();
		resultBihinItemList =

				getBihinItemToBeReturn(shatakuKanriId, dto.getShainNo(), resultBihinItemList);

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
	 * @return List<Skf2020Sc002GetShatakuInfoExp>
	 */
	protected List<Skf2020Sc002GetShatakuInfoExp> getSelectShatakuInfo(long shatakuKanriId, String shainNo,
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
	 * 表示項目の活性制御または表示制御を行う。
	 * 
	 * @param dto 補足：設定値 TRUE:活性or表示 FALSE：非活性or非表示
	 */
	protected void setControlValue(Skf2020Sc002CommonDto dto) {

		/*
		 * 必要とする社宅 初期表示活性制御
		 */
		dto.setRdoKikonDisabled(TRUE);
		dto.setRdoHitsuyoSetaiDisabled(TRUE);
		dto.setRdoHitsuyoTanshinDisabled(TRUE);
		dto.setRdoHitsuyoDokushinDisabled(TRUE);

		// 新たに社宅を必要としますか？の「駐車場のみ」は2台貸与されている場合または保有社宅が無い場合は非活性
		if (checkParking(dto.getParking1stPlace(), dto.getParking2stPlace()) == 3) {
			dto.setRdoParkingOnlyDisabled(TRUE);
		}

		/*
		 * ラジオボタンのチェック状態による画面の制御
		 */
		if (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が「必要とする」の場合

			// 社宅を必要とする理由を活性化
			dto.setRdoHitsuyoIdoDisabled(FALSE);
			dto.setRdoHitsuyoKekkonDisabled(FALSE);
			dto.setRdoHitsuyoSonotaDisabled(FALSE);

			// 社宅を必要としない理由を非活性化
			dto.setRdoFuyouJitakuTsuukinnDisabled(TRUE);
			dto.setRdoFuyouJikoKariageDisabled(TRUE);
			dto.setRdoFuyouSonotaDisabled(TRUE);

			/*
			 * 必要とする社宅
			 */
			if (CodeConstant.SETAI.equals(dto.getHitsuyoShataku())) {
				// 「世帯」選択時
				dto.setRdoKikonDisabled(TRUE);
				dto.setRdoHitsuyoSetaiDisabled(FALSE);
				dto.setRdoHitsuyoTanshinDisabled(FALSE);
				dto.setRdoHitsuyoDokushinDisabled(FALSE);
			} else if (CodeConstant.TANSHIN.equals(dto.getHitsuyoShataku())) {
				// 「単身」選択時
				dto.setRdoKikonDisabled(TRUE);
				dto.setRdoHitsuyoSetaiDisabled(FALSE);
				dto.setRdoHitsuyoTanshinDisabled(FALSE);
				dto.setRdoHitsuyoDokushinDisabled(FALSE);
			} else if (CodeConstant.DOKUSHIN.equals(dto.getHitsuyoShataku())) {
				// 「独身」選択時
				dto.setRdoKikonDisabled(FALSE);
				dto.setRdoHitsuyoSetaiDisabled(TRUE);
				dto.setRdoHitsuyoTanshinDisabled(TRUE);
				dto.setRdoHitsuyoDokushinDisabled(FALSE);
			} else {
				// それ以外
				dto.setRdoKikonDisabled(FALSE);
				dto.setRdoHitsuyoSetaiDisabled(TRUE);
				dto.setRdoHitsuyoTanshinDisabled(TRUE);
				dto.setRdoHitsuyoDokushinDisabled(FALSE);
			}

			/*
			 * 自動車の保管場所
			 */
			dto.setRdoCarHitsuyoDisabled(TRUE);
			dto.setRdoCarFuyoDisabled(TRUE);

			// 1台目 自動車の保有
			dto.setRdo1stCarHoyuDisabled(TRUE);
			dto.setRdo1stCarYoteiDisabled(TRUE);

			// 2台目 自動車の保有
			dto.setRdo2stCarHoyuDisabled(TRUE);
			dto.setRdo2stCarYoteiDisabled(TRUE);

			/*
			 * 退居時必要項目表示フラグと退居届を促すメッセージの設定
			 */
			dto.setTaikyoViewFlag(TRUE);
			dto.setLblShatakuFuyouMsgRemove(FALSE);

		} else if (CodeConstant.ASKED_SHATAKU_FUYOU.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が「必要としない」の場合

			// 社宅を必要とする理由
			dto.setRdoHitsuyoIdoDisabled(TRUE);
			dto.setRdoHitsuyoKekkonDisabled(TRUE);
			dto.setRdoHitsuyoSonotaDisabled(TRUE);

			// 社宅を必要としない理由
			dto.setRdoFuyouJitakuTsuukinnDisabled(FALSE);
			dto.setRdoFuyouJikoKariageDisabled(FALSE);
			dto.setRdoFuyouSonotaDisabled(FALSE);

			/*
			 * 自動車の保管場所
			 */
			dto.setRdoCarHitsuyoDisabled(FALSE);
			dto.setRdoCarFuyoDisabled(TRUE);

			// 1台目 自動車の保有
			dto.setRdo1stCarHoyuDisabled(FALSE);
			dto.setRdo1stCarYoteiDisabled(FALSE);

			// 2台目 自動車の保有
			dto.setRdo2stCarHoyuDisabled(FALSE);
			dto.setRdo2stCarYoteiDisabled(FALSE);

			/*
			 * 退居時必要項目表示フラグと退居届を促すメッセージの設定
			 */
			dto.setTaikyoViewFlag(FALSE);
			dto.setLblShatakuFuyouMsgRemove(TRUE);

		} else if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(dto.getTaiyoHituyo())) {
			// 社宅を必要としますか？が「駐車場のみ」の場合

			// 社宅を必要とする理由
			dto.setRdoHitsuyoIdoDisabled(TRUE);
			dto.setRdoHitsuyoKekkonDisabled(TRUE);
			dto.setRdoHitsuyoSonotaDisabled(FALSE);

			// 社宅を必要としない理由
			dto.setRdoFuyouJitakuTsuukinnDisabled(TRUE);
			dto.setRdoFuyouJikoKariageDisabled(TRUE);
			dto.setRdoFuyouSonotaDisabled(FALSE);

			/*
			 * 社宅を必要とする理由「その他」 社宅を必要としない理由「その他」 現保有の社宅「継続利用する」 をチェック状態にする
			 */
			dto.setHitsuyoRiyu(CodeConstant.HITUYO_RIYU_OTHERS);
			dto.setFuhitsuyoRiyu(CodeConstant.FUYO_RIYU_OTHERS);
			dto.setTaikyoYotei(CodeConstant.NOT_LEAVE);

			/*
			 * 自動車の保管場所
			 */
			dto.setRdoCarHitsuyoDisabled(FALSE);
			dto.setRdoCarFuyoDisabled(TRUE);

			// 1台目 自動車の保有
			dto.setRdo1stCarHoyuDisabled(TRUE);
			dto.setRdo1stCarYoteiDisabled(TRUE);

			// 2台目 自動車の保有
			dto.setRdo2stCarHoyuDisabled(TRUE);
			dto.setRdo2stCarYoteiDisabled(TRUE);

			// 現保有の社宅「継続利用する」
			dto.setRdoNowHoyuShatakuKeizokuDisabled(TRUE);

			/*
			 * 退居時必要項目表示フラグと退居届を促すメッセージの設定
			 */
			dto.setTaikyoViewFlag(TRUE);
			dto.setLblShatakuFuyouMsgRemove(FALSE);

		} else {
			// それ以外

			// 社宅を必要とする理由
			dto.setRdoHitsuyoIdoDisabled(TRUE);
			dto.setRdoHitsuyoKekkonDisabled(TRUE);
			dto.setRdoHitsuyoSonotaDisabled(TRUE);

			// 社宅を必要としない理由
			dto.setRdoFuyouJitakuTsuukinnDisabled(TRUE);
			dto.setRdoFuyouJikoKariageDisabled(TRUE);
			dto.setRdoFuyouSonotaDisabled(TRUE);

			/*
			 * 自動車の保管場所
			 */
			dto.setRdoCarHitsuyoDisabled(TRUE);
			dto.setRdoCarFuyoDisabled(TRUE);

			// 1台目 自動車の保有
			dto.setRdo1stCarHoyuDisabled(TRUE);
			dto.setRdo1stCarYoteiDisabled(TRUE);

			// 2台目 自動車の保有
			dto.setRdo2stCarHoyuDisabled(TRUE);
			dto.setRdo2stCarYoteiDisabled(TRUE);

			/*
			 * 退居時必要項目表示フラグと退居届を促すメッセージの設定
			 */
			dto.setTaikyoViewFlag(TRUE);
			dto.setLblShatakuFuyouMsgRemove(FALSE);
		}

		// 保管場所を必要とするか
		if (CodeConstant.CAR_PARK_HITUYO.equals(dto.getParkingUmu())) {

			// 1台目
			dto.setRdo1stCarHoyuDisabled(FALSE);
			dto.setRdo1stCarYoteiDisabled(FALSE);

			// 2台目
			dto.setRdo2stCarHoyuDisabled(FALSE);
			dto.setRdo2stCarYoteiDisabled(FALSE);

		} else {
			// 1台目
			dto.setRdo1stCarHoyuDisabled(TRUE);
			dto.setRdo1stCarYoteiDisabled(TRUE);

			// 2台目
			dto.setRdo2stCarHoyuDisabled(TRUE);
			dto.setRdo2stCarYoteiDisabled(TRUE);
		}

		// 現保有の社宅
		if (CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
			// 現保有の社宅を活性にする
			dto.setRdoNowHoyuShatakuTaikyoDisabled(FALSE);
			dto.setRdoNowHoyuShatakuKeizokuDisabled(FALSE);
			// 退居届を促すメッセージを表示
			dto.setLblShatakuFuyouMsgRemove(TRUE);

		} else if (CodeConstant.NOT_LEAVE.equals(dto.getTaikyoYotei())) {
			// 現保有の社宅を活性にする
			dto.setRdoNowHoyuShatakuTaikyoDisabled(FALSE);
			dto.setRdoNowHoyuShatakuKeizokuDisabled(FALSE);
			// 退居届を促すメッセージを非表示
			dto.setLblShatakuFuyouMsgRemove(FALSE);
		} else {
			LogUtils.debugByMsg("保有社宅が存在する場合" + dto.getShatakuList());
			if (dto.getShatakuList() != null) {
				// 現居住宅 保有(会社借上を含む)をチェック状態にする
				dto.setNowShataku(CodeConstant.GENNYUKYO_SHATAKU_KBN_HOYU);
				dto.setRdoNowJutakuHoyuDisabled(FALSE);
				// その他項目を非活性にする
				dto.setRdoNowJutakuJitakuDisabeld(TRUE);
				dto.setRdoNowJutakuKariageDisabled(TRUE);
				dto.setRdoNowJutakuSonotaDisabled(TRUE);
				// 現保有の社宅を活性にする
				dto.setRdoNowHoyuShatakuTaikyoDisabled(FALSE);
				dto.setRdoNowHoyuShatakuKeizokuDisabled(FALSE);

				if (CodeConstant.LEAVE.equals(dto.getTaikyoYotei())) {
					// 退居予定の場合
					// 退居届を促すメッセージを表示
					dto.setLblShatakuFuyouMsgRemove(TRUE);
				} else if (CodeConstant.NOT_LEAVE.equals(dto.getTaikyoYotei())) {

					// 退居届を促すメッセージを非表示
					dto.setLblShatakuFuyouMsgRemove(FALSE);
				}

			} else {

				// 現居住社宅が無い場合は駐車場のみ、現居住宅を非活性にする
				dto.setRdoParkingOnlyDisabled(TRUE);
				dto.setRdoNowJutakuHoyuDisabled(TRUE);
				dto.setRdoNowJutakuJitakuDisabeld(FALSE);
				dto.setRdoNowJutakuKariageDisabled(FALSE);
				dto.setRdoNowJutakuSonotaDisabled(FALSE);
				// 現保有の社宅を非活性にする
				dto.setRdoNowHoyuShatakuTaikyoDisabled(TRUE);
				dto.setRdoNowHoyuShatakuKeizokuDisabled(TRUE);
			}
		}

		// 備品制御
		if (NfwStringUtils.isNotEmpty(dto.getReturnEquipment())) {
			// 返却立会希望日、連絡先を活性にする
			dto.setSessionTimeDisabled(FALSE);
			dto.setSessionDayDisabled(FALSE);
			dto.setRenrakuSakiDisabled(FALSE);
		} else {
			// 返却立会希望日、連絡先を非活性にする
			dto.setSessionTimeDisabled(TRUE);
			dto.setSessionDayDisabled(TRUE);
			dto.setRenrakuSakiDisabled(TRUE);
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
			LogUtils.debugByMsg(Msg + "勤務先のTEL" + dto.getTel());

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
		// 新所属 部等 その他
		dto.setNewAffiliation1Other(NfwStringUtils.rightTrimbyByte(dto.getNewAffiliation1Other(), 128));
		LogUtils.debugByMsg(Msg + "新所属 部等" + dto.getNewAffiliation1Other());
		// 新所属 室、チーム又は課 その他
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
		applInfo.put("updateFlg", NO_UPDATE_FLG);
		// 入居希望等調書申請テーブルの設定
		Skf2020TNyukyoChoshoTsuchi setValue = new Skf2020TNyukyoChoshoTsuchi();
		setValue = setNyukyoChoshoTsuchi(dto, setValue, applInfo);
		// 登録
		insertNyukyoChoshoTsuchi(dto, setValue);

		// 申請日をdtoに設定
		dto.setApplDate(dto.getApplHistroyApplDate());
		// ステータスを更新
		dto.setStatus(applInfo.get("newStatus"));
		// 申請書番号を設定
		dto.setApplNo(newApplNo);

		return true;
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

		dto.setNyukyoApplDate(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));

		// 登録項目をセット
		String msg = "入力項目　：";

		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);// 会社コード
		// 新規の場合セット
		if (CodeConstant.STRING_ZERO.equals(applInfo.get("updateFlg"))) {
			// 申請書番号の設定
			setValue.setApplNo(applInfo.get("applNo"));
			LogUtils.debugByMsg(msg + applInfo.get("applNo"));
		} else {
			// 申請書番号の設定
			setValue.setApplNo(dto.getApplNo());
			LogUtils.debugByMsg(msg + dto.getApplNo());
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
		setValue.setApplDate(dto.getNyukyoApplDate());
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
			setValue.setNyukyoYoteiDate(
					dto.getNyukyoYoteiDate().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
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
			setValue.setCarExpirationDate(
					dto.getCarExpirationDate().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
			LogUtils.debugByMsg(dto.getCarExpirationDate());
		}
		// 自動車の使用者(１台目)
		setValue.setCarUser(dto.getCarUser());
		// 保管場所使用開始日(１台目)
		if (NfwStringUtils.isNotEmpty(dto.getParkingUseDate())) {
			setValue.setParkingUseDate(
					dto.getParkingUseDate().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
			LogUtils.debugByMsg(dto.getParkingUseDate());
		}
		// 自動車の登録番号入力フラグ2
		setValue.setCarNoInputFlg2(dto.getCarNoInputFlg2());

		// 自動車の登録番号入力フラグだけで、2台目の車両情報が入っていない場合
		if (CheckUtils.isEmpty(dto.getCarName2()) && CheckUtils.isEmpty(dto.getCarNo2())
				&& CheckUtils.isEmpty(dto.getCarExpirationDate2()) && CheckUtils.isEmpty(dto.getCarUser2())
				&& CheckUtils.isEmpty(dto.getParkingUseDate2())) {
			setValue.setCarNoInputFlg2(null);

		}
		// 自動車の車名(2台目)
		setValue.setCarName2(dto.getCarName2());
		// 自動車の登録番号(2台目)
		setValue.setCarNo2(dto.getCarNo2());
		// 車検の有効期間満了日(2台目)
		if (NfwStringUtils.isNotEmpty(dto.getCarExpirationDate2())) {
			setValue.setCarExpirationDate2(
					dto.getCarExpirationDate2().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
			LogUtils.debugByMsg(dto.getCarExpirationDate2());
		}
		// 自動車の使用者(2台目)
		setValue.setCarUser2(dto.getCarUser2());
		// 保管場所使用開始日(2台目)
		if (NfwStringUtils.isNotEmpty(dto.getParkingUseDate2())) {
			setValue.setParkingUseDate2(
					dto.getParkingUseDate2().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
			LogUtils.debugByMsg(dto.getParkingUseDate2());
		}
		// 現居社宅
		setValue.setNowShataku(dto.getNowShataku());
		// 現居社宅-保有社宅名
		setValue.setNowShatakuName(dto.getHdnSelectedNowShatakuName());
		LogUtils.debugByMsg("現居社宅-保有社宅名" + dto.getHdnSelectedNowShatakuName());
		// 現居社宅-室番号
		setValue.setNowShatakuNo(dto.getNowShatakuNo());
		LogUtils.debugByMsg("現居社宅-室番号" + dto.getNowShatakuNo());
		// 現居社宅-規格(間取り)
		setValue.setNowShatakuKikaku(dto.getNowShatakuKikaku());
		LogUtils.debugByMsg("現居社宅-規格(間取り)" + dto.getNowShatakuKikaku());
		// 現居社宅-面積
		if (NfwStringUtils.isNotEmpty(dto.getNowShatakuMenseki())) {
			setValue.setNowShatakuMenseki(
					dto.getNowShatakuMenseki().replace(SkfCommonConstant.SQUARE_MASTER, CodeConstant.DOUBLE_QUOTATION));
			LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
		}
		// 現保有の社宅
		setValue.setTaikyoYotei(dto.getTaikyoYotei());
		// 退居予定日
		if (NfwStringUtils.isNotEmpty(dto.getTaikyoYoteiDate())) {
			setValue.setTaikyoYoteiDate(
					dto.getTaikyoYoteiDate().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
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
		setValue.setRoomKanriNo(dto.getHdnNowShatakuRoomKanriNo());
		// 社宅の状態
		setValue.setShatakuJotai(dto.getShatakuJyotai());
		// 返却立会希望日
		if (NfwStringUtils.isNotEmpty(dto.getSessionDay())) {
			setValue.setSessionDay(dto.getSessionDay().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
		}
		// 返却立会希望日(時間)
		setValue.setSessionTime(dto.getSessionTime());
		// 連絡先
		setValue.setRenrakuSaki(dto.getRenrakuSaki());
		// 現在の保管場所
		setValue.setNowParkingArea(dto.getParking1stPlace());
		;
		LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());
		// 現在の位置番号
		setValue.setNowCarIchiNo(dto.getHdnParking1stNumber());
		LogUtils.debugByMsg("現在の位置番号" + dto.getHdnParking1stNumber());
		// 現在の保管場所2
		setValue.setNowParkingArea2(dto.getParking2stPlace());
		LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2stPlace());
		// 現在の位置番号2
		setValue.setNowParkingArea2(dto.getHdnParking2stNumber());
		LogUtils.debugByMsg("現在の位置番号2" + dto.getHdnParking2stNumber());
		// 現在の社宅管理番号
		setValue.setNowShatakuKanriNo(dto.getHdnNowShatakuKanriNo());
		LogUtils.debugByMsg("現在の社宅管理番号" + dto.getHdnNowShatakuKanriNo());
		// 現在の部屋管理番号
		setValue.setNowRoomKanriNo(dto.getHdnNowShatakuRoomKanriNo());
		LogUtils.debugByMsg("現在の部屋管理番号" + dto.getHdnNowShatakuRoomKanriNo());

		return setValue;
	}

	/**
	 * 入居希望等調査・入居決定通知テーブルの登録処理
	 * 
	 * @param dto
	 * @param setValue
	 */
	private void insertNyukyoChoshoTsuchi(Skf2020Sc002CommonDto dto, Skf2020TNyukyoChoshoTsuchi setValue) {

		// 登録処理
		int registCount = 0;
		registCount = skf2020TNyukyoChoshoTsuchiRepository.insertSelective(setValue);
		LogUtils.debugByMsg("入居希望等調書決定通知テーブル登録件数：" + registCount + "件");

	}

	/**
	 * 入居希望等調査・入居決定通知テーブルの更新処理
	 *
	 * @param setValue
	 * @param Skf2020Sc002CommonDto
	 * @param applInfo
	 * @return 更新件数
	 */
	protected int updateNyukyoChoshoTsuchi(Skf2020TNyukyoChoshoTsuchi setValue, Skf2020Sc002CommonDto dto,
			Map<String, String> applInfo) {

		int updateCnt = 0;

		// 社宅入居希望等調査・入居決定通知テーブル情報の取得
		Skf2020TNyukyoChoshoTsuchi nyukyoChoshoList = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey setKey = new Skf2020TNyukyoChoshoTsuchiKey();
		// 条件項目をセット
		setKey.setCompanyCd(CodeConstant.C001);
		setKey.setApplNo(dto.getApplNo());
		nyukyoChoshoList = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(setKey);
		LogUtils.debugByMsg("社宅入居希望等調査情報： " + nyukyoChoshoList);

		// 更新値の設定
		setValue = setNyukyoChoshoTsuchi(dto, setValue, applInfo);
		// 更新処理
		updateCnt = skf2020Sc002UpdateNyukyoKiboInfoExpRepository.updateNyukyoKiboInfo(setValue);

		return updateCnt;
	}

	/**
	 * 備品返却申請の申請書類管理番号の情報取得
	 * 
	 * @param dto
	 * @return 備品返却申請の情報
	 */
	protected Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp getBihinHenkyaku(Skf2020Sc002CommonDto dto) {

		// 備品返却申請テーブルから備品返却申請の書類管理番号を取得
		Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
		Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpParameter param = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpParameter();
		param.setCompanyCd(CodeConstant.C001);
		param.setApplNo(dto.getApplNo());
		bihinHenkyakuInfo = skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExpRepository
				.getBihinHenkyakuShinseiApplNoInfo(param);

		return bihinHenkyakuInfo;
	}

	/**
	 * 備品返却申請テーブルの登録値の設定
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

		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 申請書類番号
		setValue.setApplNo(dto.getApplNo());
		// 更新SQLでは不要
		if (!UPDATE_FLG.equals(applInfo.get("dateUpdateFlg"))) {

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
		setValue.setNowShatakuKikaku(dto.getNowShatakuKikaku());
		// 面積
		setValue.setNowShatakuMenseki(
				dto.getNowShatakuMenseki().replace(SkfCommonConstant.SQUARE_MASTER, CodeConstant.DOUBLE_QUOTATION));
		// 返却立会希望日
		setValue.setSessionDay(dto.getSessionDay().replace(CodeConstant.SLASH, CodeConstant.DOUBLE_QUOTATION));
		// 返却立会希望日(時間)
		setValue.setSessionTime(dto.getSessionTime());
		// 連絡先
		setValue.setRenrakuSaki(dto.getRenrakuSaki());

		return setValue;

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
	 * 備品返却申請テーブルの更新処理
	 *
	 * @param setValue
	 * @param dto
	 * @param applInfo
	 * @param bihinHenkyakuInfo
	 * @return
	 */
	protected void updateBihinHenkyakuInfo(Skf2050TBihinHenkyakuShinsei setValue, Skf2020Sc002CommonDto dto,
			Map<String, String> applInfo, Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo) {

		int updateCnt = 0;
		// 備品返却申請テーブルの更新項目設定
		setValue = setColumnInfoBihinList(setValue, dto, applInfo, bihinHenkyakuInfo.getTaikyoApplNo());
		// 更新
		updateCnt = skf2020Sc002UpdateBihinHenkyakuInfoExpRepository.updateBihinHenkyakuInfo(setValue);
		LogUtils.debugByMsg("備品返却申請テーブル更新件数：" + updateCnt + "件");
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

		dto.setApplHistroyApplDate(DateUtils.getSysDate());

		// 登録項目をセット
		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);
		// 社員番号
		setValue.setShainNo(dto.getShainNo());
		setValue.setApplDate(dto.getApplHistroyApplDate());
		setValue.setApplNo(applInfo.get("applNo"));
		setValue.setApplId(FunctionIdConstant.R0100);
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		setValue.setComboFlg(UPDATE_FLG);
		// 登録
		int registCount = 0;
		registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
		LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");

		// ステータスを設定
		dto.setStatus(applInfo.get("newStatus"));
	}

	/**
	 * 申請書類履歴テーブル更新用値のセット
	 * 
	 * @param setValue 申請書類履歴テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @param applInfo 申請書情報Map
	 * @return setValue 申請書類履歴テーブル
	 */
	protected Skf2010TApplHistory setUpdateApplHistoryAgreeStatusIchiji(Skf2010TApplHistory setValue,
			Skf2020Sc002CommonDto dto, Map<String, String> applInfo) {

		// キー項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(dto.getApplNo());

		// 更新項目をセット
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		if (UPDATE_FLG.equals(applInfo.get("dateUpdateFlg").toString())) {
			setValue.setApplDate(dto.getApplHistroyApplDate());
		}
		return setValue;
	}

	/**
	 * 申請書類履歴テーブル情報取得
	 * 
	 * @param keyValue 申請書類履歴テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @return
	 */
	protected Skf2010TApplHistory selectByApplHistoryPrimaryKey(Skf2010TApplHistory keyValue,
			Skf2020Sc002CommonDto dto) {

		// キー項目をセット
		keyValue.setCompanyCd(CodeConstant.C001);
		keyValue.setShainNo(dto.getShainNo());
		keyValue.setApplDate(dto.getApplDate());
		keyValue.setApplNo(dto.getApplNo());
		keyValue.setApplId(FunctionIdConstant.R0100);

		Skf2010TApplHistory resultInfo = new Skf2010TApplHistory();
		resultInfo = skf2010TApplHistoryRepository.selectByPrimaryKey(keyValue);

		return resultInfo;

	}

	/**
	 * 申請書情報の取得
	 * 
	 * @param dto
	 * @return 申請書情報のMAP applInfoMap
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
			// 添付ファイル有無フラグ
			applInfoMap.put("applTacFlg", applInfo.getApplTacFlg());
		} else {
			// 申請ステータス
			applInfoMap.put("status", CodeConstant.STATUS_MISAKUSEI);
		}
		return applInfoMap;
	}

	/**
	 * 備品返却申請テーブル更新日付取得
	 * 
	 * @param keyValue 申請書類履歴テーブル
	 * @param dto Skf2020Sc002CommonDto
	 * @param applInfo 申請書情報Map
	 * @return
	 */
	protected Skf2050TBihinHenkyakuShinsei selectBihinHenkyakuByPrimaryKey(Skf2020Sc002CommonDto dto) {

		Skf2050TBihinHenkyakuShinsei keyValue = new Skf2050TBihinHenkyakuShinsei();
		// キー項目をセット
		keyValue.setCompanyCd(CodeConstant.C001);
		keyValue.setApplNo(dto.getApplNo());

		Skf2050TBihinHenkyakuShinsei resultUpdateDate = new Skf2050TBihinHenkyakuShinsei();
		resultUpdateDate = skf2050TBihinHenkyakuShinseiRepository.selectByPrimaryKey(keyValue);

		return resultUpdateDate;
	}

	/**
	 * 備品返却申請テーブル登録or更新処理
	 * 
	 * @param dto
	 * @param applInfo
	 */
	protected void registrationBihinShinsei(Skf2020Sc002CommonDto dto, Map<String, String> applInfo) {

		// 備品返却申請テーブルから備品返却申請情報を取得
		Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp bihinHenkyakuInfo = new Skf2020Sc002GetBihinHenkyakuShinseiApplNoInfoExp();
		bihinHenkyakuInfo = getBihinHenkyaku(dto);

		// 情報が取得できた場合は、退居（自動車の保管場所返還）届管理番号を設定
		String bihinHenkaykuShinseiApplNo = null;
		if (bihinHenkyakuInfo != null) {
			bihinHenkaykuShinseiApplNo = bihinHenkyakuInfo.getTaikyoApplNo();
		}

		// 備品返却申請書番号がなければ居（自動車の保管場所返還）届管理番号を新規発行
		if (NfwStringUtils.isEmpty(bihinHenkaykuShinseiApplNo)) {
			// 備品返却申請用の申請書類管理番号を取得
			bihinHenkaykuShinseiApplNo = skfShinseiUtils.getBihinHenkyakuShinseiNewApplNo(CodeConstant.C001,
					dto.getShainNo());
			// 備品返却申請テーブルへ新規登録
			insertBihinHenkyakuInfo(bihinHenkaykuShinseiApplNo, dto, applInfo);
		} else {
			// 項目の値設定
			Skf2050TBihinHenkyakuShinsei setValue = new Skf2050TBihinHenkyakuShinsei();
			// 更新処理
			updateBihinHenkyakuInfo(setValue, dto, applInfo, bihinHenkyakuInfo);
		}

	}

	/**
	 * 画面表示項目再設定
	 * 
	 * @param dto
	 */
	protected void setInfo(Skf2020Sc002CommonDto dto) {

		String Msg = "保存　：";

		// TEL
		dto.setTel(dto.getTel());
		LogUtils.debugByMsg(Msg + "勤務先のTEL" + dto.getTel());

		// 社宅を必要としますか？
		dto.setTaiyoHituyo(dto.getTaiyoHituyo());
		LogUtils.debugByMsg(Msg + "社宅を必要としますか？" + dto.getTaiyoHituyo());

		// 社宅を必要とする理由
		dto.setHitsuyoRiyu(dto.getHitsuyoRiyu());
		LogUtils.debugByMsg(Msg + "社宅を必要とする理由" + dto.getHitsuyoRiyu());

		// 社宅を必要としない理由
		dto.setFuhitsuyoRiyu(null);
		LogUtils.debugByMsg(Msg + "社宅を必要としない理由" + dto.getFuhitsuyoRiyu());

		// 新所属-機関
		// 機関ドロップダウンリストの設定
		dto.setDdlAgencyList(skfDropDownUtils.getDdlAgencyByCd(CodeConstant.C001, dto.getAgencyCd(), true));

		// 新所属-部等
		// 部等ドロップダウンリストの設定
		List<Map<String, Object>> afflication1List = new ArrayList<Map<String, Object>>();
		afflication1List = skfDropDownUtils.getDdlAffiliation1ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), true);
		// その他を追加
		if (afflication1List.size() > 0) {
			Map<String, Object> soshikiMap = new HashMap<String, Object>();
			soshikiMap.put("value", "99");
			soshikiMap.put("label", "その他");
			afflication1List.add(soshikiMap);
		}

		// 新所属-部等 その他
		dto.setNewAffiliation1Other(dto.getNewAffiliation1Other());
		LogUtils.debugByMsg(Msg + "新所属-部等 その他" + dto.getNewAffiliation1Other());

		// 新所属-室、チーム又は課
		// 室、チーム又は課ドロップダウンをセット
		List<Map<String, Object>> afflication2List = new ArrayList<Map<String, Object>>();
		afflication2List = skfDropDownUtils.getDdlAffiliation2ByCd(CodeConstant.C001, dto.getAgencyCd(),
				dto.getAffiliation1Cd(), dto.getAffiliation2Cd(), true);
		// その他を追加
		if (afflication2List.size() > 0) {
			Map<String, Object> teamMap = new HashMap<String, Object>();
			teamMap.put("value", "99");
			teamMap.put("label", "その他");
			afflication2List.add(teamMap);
		}

		// 新所属-室、チーム又は課 その他
		dto.setNewAffiliation2Other(dto.getNewAffiliation2Other());
		LogUtils.debugByMsg(Msg + "新所属-室、チーム又は課 その他" + dto.getNewAffiliation2Other());

		// 必要とする社宅
		dto.setHitsuyoShataku(dto.getHitsuyoShataku());
		LogUtils.debugByMsg(Msg + "必要とする社宅" + dto.getHitsuyoShataku());

		// 続柄
		dto.setDokyoRelation1(dto.getDokyoRelation1());
		dto.setDokyoRelation2(dto.getDokyoRelation2());
		dto.setDokyoRelation3(dto.getDokyoRelation3());
		dto.setDokyoRelation4(dto.getDokyoRelation4());
		dto.setDokyoRelation5(dto.getDokyoRelation5());
		dto.setDokyoRelation6(dto.getDokyoRelation6());

		// 氏名
		dto.setDokyoName1(dto.getDokyoName1());
		dto.setDokyoName2(dto.getDokyoName2());
		dto.setDokyoName3(dto.getDokyoName3());
		dto.setDokyoName4(dto.getDokyoName4());
		dto.setDokyoName5(dto.getDokyoName5());
		dto.setDokyoName6(dto.getDokyoName6());

		// 年齢
		dto.setDokyoAge1(dto.getDokyoAge1());
		dto.setDokyoAge2(dto.getDokyoAge2());
		dto.setDokyoAge3(dto.getDokyoAge3());
		dto.setDokyoAge4(dto.getDokyoAge4());
		dto.setDokyoAge5(dto.getDokyoAge5());
		dto.setDokyoAge6(dto.getDokyoAge6());

		// 入居希望日
		dto.setNyukyoYoteiDate(dto.getNyukyoYoteiDate());

		// 自動車の保管場所
		dto.setParkingUmu(dto.getParkingUmu());

		// 自動車の保有
		dto.setCarNoInputFlg(dto.getCarNoInputFlg());

		// １台目-車名
		dto.setCarName(dto.getCarName());
		// １台目-自動車の登録番号
		dto.setCarNo(dto.getCarNo());
		// 1台目-車検の有効期間満了日
		dto.setCarExpirationDate(dto.getCarExpirationDate());
		// １台目-自動車の使用者
		dto.setCarUser(dto.getCarUser());
		// １台目-駐車場の使用開始日
		dto.setParkingUseDate(dto.getParkingUseDate());

		// 自動車の保有
		dto.setCarNoInputFlg2(dto.getCarNoInputFlg2());

		// ２台目-車名
		dto.setCarName2(dto.getCarName2());
		// ２台目-自動車の登録番号
		dto.setCarNo2(dto.getCarNo2());
		// ２台目-車検の有効期間満了日
		dto.setCarExpirationDate2(dto.getCarExpirationDate2());
		// ２台目-自動車の使用者
		dto.setCarUser2(dto.getCarUser2());
		// ２台目-駐車場の使用開始日
		dto.setParkingUseDate2(dto.getParkingUseDate2());

		// 現居住宅
		dto.setNowShataku(dto.getNowShataku());

		// 社宅管理IDの取得
		long shatakuKanriId = CodeConstant.LONG_ZERO;
		shatakuKanriId = Long.parseLong(dto.getNowShatakuName());
		if (shatakuKanriId > 0) {
			dto.setDdlNowShatakuNameList(skfDropDownUtils.getDdlNowShatakuNameByCd(dto.getShainNo(),
					dto.getYearMonthDay(), shatakuKanriId, false));
			// 社宅情報の設定
			setShatakuInfo(dto, Skf2020Sc002SharedService.UPDATE_FLG);
		}

		// 特殊事情等
		dto.setTokushuJijo(dto.getTokushuJijo());

		// 現保有の社宅
		dto.setTaikyoYotei(dto.getTaikyoYotei());

		// 退居予定日
		dto.setTaikyoYoteiDate(dto.getTaikyoYoteiDate());

		// 社宅の状態
		dto.setShatakuJyotai(dto.getShatakuJyotai());

		// 退居理由
		dto.setDdlTaikyoRiyuKbnList(skfDropDownUtils
				.getGenericForDoropDownList(FunctionIdConstant.GENERIC_CODE_TAIKYO_RIYU, dto.getTaikyoRiyuKbn(), true));
		dto.setTaikyoRiyu(dto.getTaikyoRiyu());

		// 退居後の連絡先
		dto.setTaikyogoRenrakuSaki(dto.getTaikyogoRenrakuSaki());

		// 返却立会希望日
		dto.setSessionDay(dto.getSessionDay());
		dto.setDdlReturnWitnessRequestDateList(skfDropDownUtils.getGenericForDoropDownList(
				FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, dto.getSessionTime(), true));

		// 連絡先
		dto.setRenrakuSaki(dto.getRenrakuSaki());
	}

}
