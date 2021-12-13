package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc002;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfAttachedFileUtils.SkfAttachedFileUtilsInsertAttachedFileExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfRouterInfoUtils.SkfRouterInfoUtilsGetEquipmentPaymentExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100MMobileRouterWithBLOBs;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRireki;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterRentalRirekiMeisai;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TRouterLendingYoteiData;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3050MGeneralEquipmentItem;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002UpdateApplHistoryApplTacFlgExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiMeisaiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterRentalRirekiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3050MGeneralEquipmentItemRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc002common.Skf2100Sc002CommonDto;
/**
 * Skf2100Sc002 モバイルルーター借用希望申請書（アウトソース用)共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc002SharedService {

	private String companyCd = CodeConstant.C001;
	
	private MenuScopeSessionBean menuScopeSessionBean;

	private static final String REASON_LABEL = "申請者へのコメント";
	// 更新内容
	private static final String UPDATE_TYPE_PRESENT = "present"; // 提示
	private static final String UPDATE_TYPE_APPLY = "apply"; // 承認
	private static final String UPDATE_TYPE_SENDBACK = "sendback"; // 差戻し
	private static final String UPDATE_TYPE_REVISION = "revision"; // 修正依頼

	public static final String sFalse = "false";
	public static final String sTrue = "true";
	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String BIHIN_KIBO_SHINSEI_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_kibo_shinsei_UpdateDate";
	public static final String BIHIN_KEY_LAST_UPDATE_DATE = "skf2030_t_bihin_UpdateDate";
	public static final String ROUTER_KIBO_KEY_LAST_UPDATE_DATE = "skf2100_t_mobile_router_kibo_shisei_UpdateDate";

	// 更新フラグ
	protected static final String NO_UPDATE_FLG = "0";
	protected static final String UPDATE_FLG = "1";
	
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
	
	
	
	@Autowired
	private Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf3022Sc006GetCompanyAgencyListExpRepository skf3022Sc006GetCompanyAgencyListExpRepository;
	@Autowired
	private Skf2040Sc002UpdateApplHistoryApplTacFlgExpRepository skf2040Sc002UpdateApplHistoryApplTacFlgExpRepository;
	@Autowired
	private Skf3022Sc006GetIdmPreUserMasterInfoExpRepository skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
	
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiRepository skf2100TMobileRouterRentalRirekiRepository;
	@Autowired
	private Skf2100TMobileRouterRentalRirekiMeisaiRepository skf2100TMobileRouterRentalRirekiMeisaiRepository;
	@Autowired
	private Skf3050MGeneralEquipmentItemRepository skf3050MGeneralEquipmentItemRepository;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
	@Autowired
	private SkfApplHistoryInfoUtils skfApplHistoryInfoUtils;
	@Autowired
	private SkfMailUtils skfMailUtils;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;


	
	/**
	 * セッション情報を保持します
	 * 
	 * @param sessionBean
	 */
	public void setMenuScopeSessionBean(MenuScopeSessionBean sessionBean) {
		this.menuScopeSessionBean = sessionBean;
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
	 * 申請情報から初期表示項目を設定。
	 * 
	 * @param dto
	 * @param initializeErrorFlg 初期表示エラー判定フラグ true:実行 false:何もしない
	 */
	protected boolean setDisplayData(Skf2100Sc002CommonDto dto) {

		boolean returnValue = true;
		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanySelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanySelectList = new ArrayList<Map<String, Object>>();
		
		// ログインユーザー情報取得
		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		
		/**
		 * 申請書類履歴テーブル情報の取得
		 */
		// 申請書類履歴取得
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		applHistoryList = getApplHistoryList(dto.getApplNo());
		// 取得できなかった場合エラー
		if (applHistoryList == null || applHistoryList.size() <= 0) {
			returnValue = false;
			dto.setAllButtonEscape(true);
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1078,"初期表示中に");
			return returnValue;
		}
		// 申請書類履歴情報を画面に設定
		dto.setShainNo(applHistoryList.get(0).getShainNo());
		dto.setHdnApplShainNo(applHistoryList.get(0).getShainNo());
		dto.setApplHistoryDate(applHistoryList.get(0).getApplDate());
		
		String agreeName1 = applHistoryList.get(0).getAgreName1();
		// 承認者１がログインユーザーと同じ場合は閲覧のみ
		dto.setViewFlag(false);
		if (CheckUtils.isEqual(agreeName1, loginUserInfo.get("userName"))) {
			dto.setViewFlag(true); 
		}

		// 添付書類有無が取得できた場合は設定
		if (NfwStringUtils.isNotEmpty(applHistoryList.get(0).getApplTacFlg())) {
			dto.setApplTacFlg(applHistoryList.get(0).getApplTacFlg());
		}
		
		// 排他処理用に最終更新日取得
		dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, applHistoryList.get(0).getUpdateDate());
		
		// 申請状況を取得
		dto.setApplStatus(applHistoryList.get(0).getApplStatus());
		Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
		dto.setApplStatusText(applStatusMap.get(dto.getApplStatus()));

		/**
		 * モバイルルーター借用希望申請書テーブルから申請情報を取得
		 */
		Skf2100TMobileRouterKiboShinsei routerKiboInfo = new Skf2100TMobileRouterKiboShinsei();
		routerKiboInfo = skfShinseiUtils.getSksRouterKiboShinseiInfo(CodeConstant.C001, dto.getApplNo());

		LogUtils.debugByMsg("モバイルルーター借用希望申請書： " + routerKiboInfo);

		// 初期表示エラー判定
		dto.setBtnApproveDisabled(null);
		// データが取得できなかった場合は更新ボタンを使用不可にする
		if (routerKiboInfo == null) {
				dto.setAllButtonEscape(true);
				setInitializeError(dto);
				returnValue = false;
				return returnValue;
		}
		
		if (routerKiboInfo != null) {
			// 機関
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getAgency())) {
				LogUtils.debugByMsg("機関：" + routerKiboInfo.getAgency());
				dto.setAgency(routerKiboInfo.getAgency());
			}
			// 部等
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getAgency())) {
				LogUtils.debugByMsg("部等：" + routerKiboInfo.getAffiliation1());
				dto.setAffiliation1(routerKiboInfo.getAffiliation1());
			}
			// 室・課等
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getAffiliation2())) {
				LogUtils.debugByMsg("室・課等：" + routerKiboInfo.getAffiliation2());
				dto.setAffiliation2(routerKiboInfo.getAffiliation2());
			}
			// 社員番号
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getShainNo())) {
				LogUtils.debugByMsg("社員番号：" + routerKiboInfo.getShainNo());
				dto.setShainNo(routerKiboInfo.getShainNo());
			}
			// 氏名
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getName())) {
				LogUtils.debugByMsg("氏名：" + routerKiboInfo.getName());
				dto.setName(routerKiboInfo.getName());
			}
			// 電話番号
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getTel())) {
				LogUtils.debugByMsg("電話番号：" + routerKiboInfo.getTel());
				dto.setTel(routerKiboInfo.getTel());
			}
			// メールアドレス
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getMailaddress())) {
				LogUtils.debugByMsg("メールアドレス：" + routerKiboInfo.getMailaddress());
				dto.setMailAddress(routerKiboInfo.getMailaddress());
			}
			// 利用開始希望日
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getUseStartHopeDay())) {
				LogUtils.debugByMsg("利用開始希望日：" + routerKiboInfo.getUseStartHopeDay());
				dto.setUseStartHopeDay(routerKiboInfo.getUseStartHopeDay());
				dto.setUseStartHopeDaylbl(skfDateFormatUtils.dateFormatFromString(routerKiboInfo.getUseStartHopeDay(), "yyyy/MM/dd"));
			}
			// 原籍会社
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getOriginalCompanyCd())) {
				String originalCompanyName = skfShinseiUtils.getCompanyName(routerKiboInfo.getOriginalCompanyCd());
				LogUtils.debugByMsg("原籍会社コード：" + routerKiboInfo.getOriginalCompanyCd());
				LogUtils.debugByMsg("原籍会社：" + originalCompanyName);
				dto.setOriginalCompanyCd(routerKiboInfo.getOriginalCompanyCd());
				dto.setOriginalCompany(originalCompanyName);
			}else if((NfwStringUtils.isNotEmpty(routerKiboInfo.getShainNo())) ){
				// 原籍会社未設定かつ社員番号設定有りの場合
				// ①IdM_プレユーザマスタ（従業員区分）を取得
				List<Skf3022Sc006GetIdmPreUserMasterInfoExp> dtbIdmPreUserMasterInfo = new ArrayList<Skf3022Sc006GetIdmPreUserMasterInfoExp>();
				Skf3022Sc006GetIdmPreUserMasterInfoExpParameter param = new Skf3022Sc006GetIdmPreUserMasterInfoExpParameter();
				param.setPumHrNameCode(dto.getShainNo());
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
						String originalCompanyName = skfShinseiUtils.getCompanyName(CodeConstant.C001);
						LogUtils.debugByMsg("原籍会社コード：" + CodeConstant.C001);
						LogUtils.debugByMsg("原籍会社：" + originalCompanyName);
						dto.setOriginalCompanyCd(CodeConstant.C001);
						dto.setOriginalCompany(originalCompanyName);
						routerKiboInfo.setOriginalCompanyCd(CodeConstant.C001);
					}
				}
			}
			// 給与支給会社
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getPayCompanyCd())) {
				String payCompanyName = skfShinseiUtils.getCompanyName(routerKiboInfo.getPayCompanyCd());
				LogUtils.debugByMsg("給与支給会社コード：" + routerKiboInfo.getPayCompanyCd());
				LogUtils.debugByMsg("給与支給会社：" + payCompanyName);
				dto.setPayCompanyCd(routerKiboInfo.getPayCompanyCd());
				dto.setPayCompany(payCompanyName);
			}
			// ドロップダウンの設定
			 setDdlControlValues( routerKiboInfo.getOriginalCompanyCd(), originalCompanySelectList,
					 routerKiboInfo.getPayCompanyCd(), payCompanySelectList);
			 dto.setOriginalCompanySelectList(originalCompanySelectList);
			 dto.setPayCompanySelectList(payCompanySelectList);
			// 通しNo
			if (routerKiboInfo.getMobileRouterNo() != null) {
				LogUtils.debugByMsg("通しNo：" + routerKiboInfo.getMobileRouterNo());
				dto.setMobileRouterNo(routerKiboInfo.getMobileRouterNo());
			}
			// ICCID
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getIccid())) {
				LogUtils.debugByMsg("ICCID：" + routerKiboInfo.getIccid());
				dto.setIccid(routerKiboInfo.getIccid());
			}
			// IMEI
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getImei())) {
				LogUtils.debugByMsg("IMEI：" + routerKiboInfo.getImei());
				dto.setImei(routerKiboInfo.getImei());
			}
			// 発送日
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getShippingDate())) {
				LogUtils.debugByMsg("発送日：" + routerKiboInfo.getShippingDate());
				dto.setShippingDate(routerKiboInfo.getShippingDate());
				dto.setShippingDatelbl(skfDateFormatUtils.dateFormatFromString(routerKiboInfo.getShippingDate(), "yyyy/MM/dd"));
			}
			// 受領日
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getReceivedDate())) {
				LogUtils.debugByMsg("受領日：" + routerKiboInfo.getReceivedDate());
				dto.setReceivedDate(routerKiboInfo.getReceivedDate());
				dto.setReceivedDatelbl(skfDateFormatUtils.dateFormatFromString(routerKiboInfo.getReceivedDate(), "yyyy/MM/dd"));
			}
			// モバイルルーター本体受領チェックフラグ
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getBodyReceiptCheckFlag())) {
				LogUtils.debugByMsg("モバイルルーター本体受領チェックフラグ：" + routerKiboInfo.getBodyReceiptCheckFlag());
				dto.setBodyReceiptCheckFlag(routerKiboInfo.getBodyReceiptCheckFlag());
			}
			// モバイルルーター手引き受領チェックフラグ
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getHandbookReceiptCheckFlag())) {
				LogUtils.debugByMsg("モバイルルーター手引き受領チェックフラグ：" + routerKiboInfo.getHandbookReceiptCheckFlag());
				dto.setHandbookReceiptCheckFlag(routerKiboInfo.getHandbookReceiptCheckFlag());
			}
			// 返却資材受領チェックフラグ
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getMaterialsReceivedCheckFlag())) {
				LogUtils.debugByMsg("返却資材受領チェックフラグ：" + routerKiboInfo.getMaterialsReceivedCheckFlag());
				dto.setMaterialsReceivedCheckFlag(routerKiboInfo.getMaterialsReceivedCheckFlag());
			}
			
			// 更新日時
			LogUtils.debugByMsg("更新日時" + routerKiboInfo.getUpdateDate());
			dto.addLastUpdateDate(ROUTER_KIBO_KEY_LAST_UPDATE_DATE, routerKiboInfo.getUpdateDate());
			
			// 「添付資料」欄の更新を行う
			List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
			attachedFileList = refreshHeaderAttachedFile(dto.getApplNo(), attachedFileList);
			dto.setAttachedFileList(attachedFileList);

		}
		
		// コメント設定の有無
		if (setCommentBtnDisabled(dto.getApplNo())) {
			// コメントがあれば表示
			dto.setCommentViewFlag(true);
		} else {
			// なければ非表示
			dto.setCommentViewFlag(false);
		}
		
		//承認中の場合、承認者コメントを表示
		if(CodeConstant.STATUS_SHONIN1.equals(dto.getApplStatus())){
			List<SkfCommentUtilsGetCommentInfoExp> commentInfo = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
			commentInfo = skfCommentUtils.getCommentInfo(CodeConstant.C001, dto.getApplNo(),
					dto.getApplStatus());
			if (commentInfo != null && commentInfo.size() > 0) {
				String commentNote = commentInfo.get(0).getCommentNote();
				dto.setCommentNote(commentNote);
			}
		}
		
		

		// 承認可能ロールのみの処理		
		boolean result = skfLoginUserInfoUtils.isAgreeAuthority( CodeConstant.C001, FunctionIdConstant.R0107, loginUserInfo.get("roleId"), dto.getApplStatus());
		if(!result){
			//前へ戻るとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			dto.setHdnApprovalFlg(true);
		}


		return returnValue;
	}
	
	/**
	 * ドロップダウンリスト作成
	 * @param originalCompanyCd			原籍会社選択値
	 * @param originalCompanySelectList		原籍会社ドロップダウンリスト
	 * @param payCompanyCd			給与支給会社名選択値
	 * @param payCompanySelectList		給与支給会社名ドロップダウンリスト
	 */
	public void setDdlControlValues(
			String originalCompanyCd, List<Map<String, Object>> originalCompanySelectList,
			String payCompanyCd, List<Map<String, Object>> payCompanySelectList)
	{
		// 会社リスト（外部機関含む）取得
		List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList = new ArrayList<Skf3022Sc006GetCompanyAgencyListExp>();
		companyAgencyList = skf3022Sc006GetCompanyAgencyListExpRepository.getCompanyAgencyList();
		// 原籍会社
		originalCompanySelectList.clear();
		originalCompanySelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, originalCompanyCd, true));
		// 給与支給会社名
		payCompanySelectList.clear();
		payCompanySelectList.addAll(getCompanyAgencyDoropDownList(companyAgencyList, payCompanyCd, true));
	}
	
	/**
	 * 会社リスト（外部機関含む）ドロップダウンリスト作成
	 * 
	 * @param companyAgencyList	会社リスト（外部機関含む）
	 * @param selectedValue		選択値
	 * @param isFirstRowEmpty	先頭行の空行有無。true:空行あり false：空行なし
	 * @return	会社リスト（外部機関含む）ドロップダウンリスト
	 */
	private List<Map<String, Object>> getCompanyAgencyDoropDownList(
			List<Skf3022Sc006GetCompanyAgencyListExp> companyAgencyList, String selectedValue, boolean isFirstRowEmpty) {

		// リスト定義
		List<Map<String, Object>> doropDownList = new ArrayList<Map<String, Object>>();
		Map<String, Object> forListMap = new HashMap<String, Object>();
		if (isFirstRowEmpty) {
			forListMap.put("value", CodeConstant.DOUBLE_QUOTATION);
			forListMap.put("label", CodeConstant.DOUBLE_QUOTATION);
			doropDownList.add(forListMap);
		}

		for (Skf3022Sc006GetCompanyAgencyListExp entity : companyAgencyList) {

			// 表示・値を設定
			forListMap = new HashMap<String, Object>();
			forListMap.put("value", entity.getCompanyCd());
			forListMap.put("label", entity.getCompanyName());
			if (!CheckUtils.isEmpty(selectedValue)) {
				if (Objects.equals(entity.getCompanyCd(), selectedValue)) {
					forListMap.put("selected", true);
				}
			}
			doropDownList.add(forListMap);
		}
		return doropDownList;
	}
	/**
	 * 申請書履歴から社員番号を取得
	 * 
	 * @param userId ユーザーID
	 * @param applId 申請書類番号
	 * @return 取得結果
	 */
	public List<Skf2040Sc002GetApplHistoryInfoExp> getApplHistoryList(String applNo) {

		// DB検索処理
		List<Skf2040Sc002GetApplHistoryInfoExp> applHistoryList = new ArrayList<Skf2040Sc002GetApplHistoryInfoExp>();
		Skf2040Sc002GetApplHistoryInfoExpParameter param = new Skf2040Sc002GetApplHistoryInfoExpParameter();
		param.setApplNo(applNo);
		applHistoryList = skf2040Sc002GetApplHistoryInfoExpRepository.getApplHistoryInfo(param);

		// 返却するリストをDebugログで出力
		LogUtils.debugByMsg("社員情報情報のリスト：" + applHistoryList.toString());

		return applHistoryList;
	}
	
	
	/**
	 * 初期表示エラー時の処理 更新処理を行わせないようボタンを使用不可にする。
	 * 
	 * @param dto Skf2100Sc001CommonDto
	 */
	protected void setInitializeError(Skf2100Sc002CommonDto dto) {
		// 更新処理を行わせないよ う一時保存、申請ボタンを使用不可に
		// 承認
		dto.setBtnApproveDisabled(sTrue);
		
		ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1077);
	}

	/**
	 * DTO情報のクリア
	 * 
	 * @param dto
	 */
	protected void setClearInfo(Skf2100Sc002CommonDto dto) {
		
		// 機関
		dto.setAgency(null);
		// 部等
		dto.setAffiliation1(null);
		// 室・課等
		dto.setAffiliation2(null);
		// 社員番号
		dto.setShainNo(null);
		// 氏名
		dto.setName(null);
		// 電話番号
		dto.setTel(null);
		// メールアドレス
		dto.setMailAddress(null);
		// 利用開始希望日
		dto.setUseStartHopeDay(null);
		dto.setUseStartHopeDaylbl(null);
		// 原籍会社
		dto.setOriginalCompanyCd(null);
		dto.setOriginalCompany(null);
		// 給与支給会社
		dto.setPayCompanyCd(null);
		dto.setPayCompany(null);
		// 通しNo
		dto.setMobileRouterNo(null);
		// ICCID
		dto.setIccid(null);
		// IMEI
		dto.setImei(null);
		// 発送日
		dto.setShippingDate(null);
		dto.setShippingDatelbl(null);
		// 受領日
		dto.setReceivedDate(null);
		dto.setReceivedDatelbl(null);
		// モバイルルーター本体受領チェックフラグ
		dto.setBodyReceiptCheckFlag(null);
		// モバイルルーター手引き受領チェックフラグ
		dto.setHandbookReceiptCheckFlag(null);
		// 返却資材受領チェックフラグ
		dto.setMaterialsReceivedCheckFlag(null);
		// 「添付資料」欄
		dto.setAttachedFileList(null);
		// コメント
		dto.setCommentNote(null);
	}
	
	/**
	 * 表示項目の活性制御または表示制御を行う。
	 * 
	 * @param dto <br>
	 *            補足：設定値 TRUE:活性or表示 FALSE：非活性or非表示
	 */
	public void setControlValue(Skf2100Sc002CommonDto dto) {
		// ステータスによりコントロールの活性制御を行う
		
		//フラグ初期化
		//利用開始希望日編集
		dto.setUseStartHopeDayEditFlag(false);
		dto.setRouterInfoViewFlag(false);
		dto.setCommentInputFlag(false);
		
		if(dto.getApplStatus() == null){
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			return;
		}
		
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		switch (dto.getApplStatus()) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_HININ:
		case CodeConstant.STATUS_SASHIMODOSHI:
			// 申請状況が「一時保存」「否認(差し戻し)」「修正依頼（差し戻し）」の場合
			// 入力項目は全て非活性
			dto.setStatus00Flag(true);//一時保存フラグ（承認者欄を非入力項目）
			dto.setRouterInfoViewFlag(false);//貸与情報以下非表示、承認者入力欄表示
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			break;
		case CodeConstant.STATUS_SHINSACHU:
			// 「審査中」の場合
			dto.setRouterInfoViewFlag(false);//貸与情報以下非表示、承認者入力欄表示
			// 「前の画面へ」「差戻し」「修正依頼」「確認依頼」「資料を添付」ボタンを表示
			dto.setDispMode(CodeConstant.VIEW_LEVEL_2);
			dto.setCommentInputFlag(true);
			setApprovalDisp(dto);
			break;
		case CodeConstant.STATUS_KAKUNIN_IRAI:
			// 「確認依頼」の場合
			dto.setRouterInfoViewFlag(true);//申請情報表示、承認者非表示
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			break;
		case CodeConstant.STATUS_HANNYU_ZUMI:
		case CodeConstant.STATUS_SHONIN1:
		case CodeConstant.STATUS_SHONIN2:
			// 「24：搬入済み」「31：承認１」「32：承認２」の場合
			dto.setRouterInfoViewFlag(true);//申請情報表示、承認者非表示
			//「前の画面へ」「承認」ボタン表示
			dto.setDispMode(CodeConstant.VIEW_LEVEL_3);
			dto.setCommentInputFlag(true);
			setApprovalDisp(dto);
			break;
		default:
			dto.setRouterInfoViewFlag(true);//申請情報表示、承認者非表示
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			break;
		}
		
		
		if(dto.isViewFlag()){
			// 閲覧のみ
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
		}

	}
	
	/**
	 * 承認可状態での権限による制御
	 * 
	 * @param dto
	 */
	private void setApprovalDisp(Skf2100Sc002CommonDto dto) {
		if(dto.isHdnApprovalFlg()){
			//承認権限無し
			dto.setRouterInfoViewFlag(true);//申請情報表示、承認者非表示
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
		}
	}
	
	
	/**
	 * コメントボタンの表示非表示
	 * 
	 * @param dto
	 */
	protected boolean setCommentBtnDisabled(String applNo) {
		// コメントの設定
		List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001, applNo, null);
		if (commentList == null || commentList.size() == 0) {
			// コメントが無ければ非表示
			return false;
		} else {
			// コメントがあれば表示
			return true;
		}
	}

	
	public void getSupplementInfo(String applNo,Long routerNo){
		
		// モバイルルーター補足名1
		String routerSupplementName1 = "";
		// モバイルルーター補足サイズ1
		String routerSupplementSize1 = "";
		// モバイルルーター補足ファイル1
		byte[] routerSupplementFile1 = null;
		// モバイルルーター補足名2
		String routerSupplementName2 = "";
		// モバイルルーター補足サイズ2
		String routerSupplementSize2 = "";
		// モバイルルーター補足ファイル2
		byte[] routerSupplementFile2 = null;
		// モバイルルーター補足名3
		String routerSupplementName3 = "";
		// モバイルルーター補足サイズ3
		String routerSupplementSize3 = "";
		// モバイルルーター補足ファイル3
		byte[] routerSupplementFile3 = null;
		
		//モバイルルーターマスタ情報取得
		Skf2100MMobileRouterWithBLOBs routerInfo = skfRouterInfoUtils.getMobileRouterInfo(routerNo);
		
		if(routerInfo == null){
			return;
		}
		
		// 補足1
		routerSupplementSize1 = (routerInfo.getRouterSupplementSize1() != null) ?
				routerInfo.getRouterSupplementSize1() : "";
		routerSupplementFile1 = routerInfo.getRouterSupplementFile1();
		// 補足1表示判定
		if (routerInfo.getRouterSupplementName1() != null
						&& routerSupplementSize1.length() > 0
						&& routerSupplementFile1 != null) {
			routerSupplementName1 = routerInfo.getRouterSupplementName1();
			String fileType1 = skfAttachedFileUtils.getFileTypeInfo(routerSupplementName1);
			// セッションの添付ファイル情報に追加
			setAttachedFileList(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY, 
					applNo, routerSupplementName1, routerSupplementFile1, routerSupplementSize1, fileType1);
		}


		// 補足2
		routerSupplementSize2 = (routerInfo.getRouterSupplementSize2() != null) ?
				routerInfo.getRouterSupplementSize2() : "";
		routerSupplementFile2 = routerInfo.getRouterSupplementFile2();
		// 補足2表示判定
		if (routerInfo.getRouterSupplementName2() != null
						&& routerSupplementSize2.length() > 0
						&& routerSupplementFile2 != null) {
			routerSupplementName2 = routerInfo.getRouterSupplementName2();
			String fileType2 = skfAttachedFileUtils.getFileTypeInfo(routerSupplementName1);
			// セッションの添付ファイル情報に追加
			setAttachedFileList(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY, 
					applNo, routerSupplementName2, routerSupplementFile2, routerSupplementSize2, fileType2);
		}
		
		// 補足3
		routerSupplementSize3 = (routerInfo.getRouterSupplementSize3() != null) ?
				routerInfo.getRouterSupplementSize3() : "";
		routerSupplementFile3 = routerInfo.getRouterSupplementFile3();
		// 補足3表示判定
		if (routerInfo.getRouterSupplementName3() != null
						&& routerSupplementSize3.length() > 0
						&& routerSupplementFile3 != null) {
			routerSupplementName3 = routerInfo.getRouterSupplementName3();
			String fileType3 = skfAttachedFileUtils.getFileTypeInfo(routerSupplementName1);
			// セッションの添付ファイル情報に追加
			setAttachedFileList(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY, 
					applNo, routerSupplementName3, routerSupplementFile3, routerSupplementSize3, fileType3);
		}

	}
	

	/**
	 * 添付ファイルをセッションから取得します
	 * 
	 * @param attachedInfo
	 * 
	 * @param applNo
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAttachedFileInfo(String sessionKey) {
		List<Map<String, Object>> resultAttachedFileList = null;

		resultAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		if (resultAttachedFileList != null) {
			return resultAttachedFileList;
		}

		// 初期化
		resultAttachedFileList = new ArrayList<Map<String, Object>>();
		return resultAttachedFileList;
	}
	
	/**
	 * 添付ファイル情報をセッションに保存します
	 * 
	 * @param sessionKey
	 * @param applNo
	 * @param fileName
	 * @param fileStream
	 * @param fileSize
	 * @param fileType
	 * @return
	 */
	public List<Map<String, Object>> setAttachedFileList(String sessionKey, String applNo, String fileName,
			byte[] fileStream, String fileSize, String fileType) {
		List<Map<String, Object>> attachedFileList = getAttachedFileInfo(sessionKey);
		if (attachedFileList == null) {
			attachedFileList = new ArrayList<Map<String, Object>>();
		}
		Map<String, Object> attachedFileMap = new HashMap<String, Object>();
		int attachedNo = attachedFileList.size();
		attachedFileMap = new HashMap<String, Object>();
		// 添付資料番号
		attachedFileMap.put("attachedNo", String.valueOf(attachedNo));
		// 添付資料名
		attachedFileMap.put("attachedName", fileName);
		// ファイルサイズ
		attachedFileMap.put("fileSize", fileSize);
		// 更新日
		String applDate = skfDateFormatUtils.dateFormatFromDate(new Date(),
				SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SLASH);
		attachedFileMap.put("applDate", applDate);
		// 添付資料
		attachedFileMap.put("fileStream", fileStream);
		// ファイルタイプ
		attachedFileMap.put("fileType", fileType);

		attachedFileList.add(attachedFileMap);

		// セッションにデータを保存
		menuScopeSessionBean.put(sessionKey, attachedFileList);

		return attachedFileList;
	}
	


	
	
	/**
	 * 「添付資料」欄の更新を行う
	 * 
	 * @param initDto
	 */
	private List<Map<String, Object>> refreshHeaderAttachedFile(String applNo,
			List<Map<String, Object>> attachedFileList) {

		// 添付ファイルを取得し、セッションに保存
		attachedFileList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean, applNo,
				SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		
		return attachedFileList;
	}
	

	/**
	 * コメント欄入力チェック
	 * 
	 * @param dto
	 * @param checkFlag
	 * @return
	 * @throws Exception
	 */
	public boolean validateReason(Skf2100Sc002CommonDto dto, boolean checkFlag) throws Exception {

		String commentNote = dto.getCommentNote();

		// コメント必須で未入力の場合
		if (checkFlag && NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					REASON_LABEL);
			return false;
		}

		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2100.skf2100_sc002.comment_max_count"));
		if (NfwStringUtils.isNotEmpty(commentNote) && commentMaxLength < commentNote.getBytes("UTF-8").length) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1049,
					REASON_LABEL, "全角"+commentMaxLength / 2);
			return false;
		}

		return true;
	}
	/**
	 * 更新処理を行います
	 * 
	 * @param execName
	 * @param dto
	 * @param applInfo
	 * @param loginUserInfo
	 * @return
	 * @throws Exception
	 */
	public boolean updateDispInfo(String execName, Skf2100Sc002CommonDto dto, Map<String, String> applInfo,
			Map<String, String> loginUserInfo) throws Exception {
		// 申請社員番号を設定
		applInfo.put("applShainNo", dto.getShainNo());

		String updateStatus = null;
		String shoninName1 = null;
		String shoninName2 = null;
		Date agreDate = null;
		String mailKbn = null;
		Date sysDateTime = new Date();

		String sendUserId = CodeConstant.NONE;
		
		// 一般添付資料取得
		List<Map<String, Object>> attachedFileList = skfAttachedFileUtils.getAttachedFileInfo(menuScopeSessionBean,
				applInfo.get("applNo"), SessionCacheKeyConstant.COMMON_ATTACHED_FILE_SESSION_KEY);
		String applTacFlg = CodeConstant.NONE;
		if (attachedFileList != null && attachedFileList.size() > 0) {
			// 添付ファイルあり
			applTacFlg = CodeConstant.YES;
		} else {
			applTacFlg = CodeConstant.NO;
		}

		String applStatus = applInfo.get("status");
//		switch (applStatus) {
//		case CodeConstant.STATUS_SHINSACHU:
//		case CodeConstant.STATUS_SHINSEICHU:
//			// 申請中、審査中
//			// 次のステータスを設定
//			if (CheckUtils.isEqual(execName, UPDATE_TYPE_APPLY)) {
//				// 次のステータス、メール区分、承認者名1、次の階層を設定
//				updateStatus = CodeConstant.STATUS_SHONIN1;
//				shoninName1 = loginUserInfo.get("userName");
//				nextWorkflow = CodeConstant.WORK_FLOW_LEVEL_2;
//			}
//			break;
//		case CodeConstant.STATUS_HANNYU_ZUMI:
//			// 搬入済
//			// 次のステータス、メール区分、承認者名1、次の階層を設定
//			updateStatus = CodeConstant.STATUS_SHONIN1;
//			shoninName1 = loginUserInfo.get("userName");
//			nextWorkflow = CodeConstant.WORK_FLOW_LEVEL_2;
//			break;
//		case CodeConstant.STATUS_SHONIN1:
//			// 承認1済
//			// 次のステータス、メール区分、承認者名2、次の階層を設定
//			updateStatus = CodeConstant.STATUS_SHONIN_ZUMI;
//			shoninName1 = CodeConstant.NONE;
//			shoninName2 = loginUserInfo.get("userName");
//			agreDate = sysDateTime;
//			break;
//		}

		switch (execName) {
		case UPDATE_TYPE_APPLY:
			switch (applStatus) {
			case CodeConstant.STATUS_HANNYU_ZUMI:
				// 押下されたボタンが承認 かつステータスが搬入済み場合、更新ステータスを承認1に変更
				updateStatus = CodeConstant.STATUS_SHONIN1;
				shoninName1 = loginUserInfo.get("userName");
				break;
			case CodeConstant.STATUS_SHONIN1:
				// 押下されたボタンが承認 かつステータスが承認1済の場合、更新ステータスを承認済みに変更
				// 次のステータス、メール区分、承認者名2、次の階層を設定
				updateStatus = CodeConstant.STATUS_SHONIN_ZUMI;
				shoninName1 = CodeConstant.NONE;
				shoninName2 = loginUserInfo.get("userName");
				agreDate = sysDateTime;
				mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
				break;
			}
			break;
		case UPDATE_TYPE_REVISION:
			// 修正依頼
			switch (applStatus) {
			case CodeConstant.STATUS_HANNYU_ZUMI:
			case CodeConstant.STATUS_SHONIN1:
				// 押下されたボタンが修正依頼 かつステータスが搬入済み、承認中の場合、更新ステータスを強制的に確認依頼に変更
				updateStatus = CodeConstant.STATUS_KAKUNIN_IRAI;
				mailKbn = CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI;
				agreDate = sysDateTime;
				sendUserId = applInfo.get("applShainNo");
				break;
			case CodeConstant.STATUS_SHINSACHU:
				// 押下されたボタンが修正依頼 かつステータスが審査中の場合、更新ステータスを強制的に修正依頼に変更
				updateStatus = CodeConstant.STATUS_SASHIMODOSHI;
				mailKbn = CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI;
				agreDate = sysDateTime;
				sendUserId = applInfo.get("applShainNo");
				break;
			}
			break;
		case UPDATE_TYPE_SENDBACK:
			// 押下されたボタンが差戻しの場合、更新ステータスを強制的に差戻しに変更
			updateStatus = CodeConstant.STATUS_HININ;
			mailKbn = CodeConstant.HININ_KANRYO_TSUCHI;
			agreDate = sysDateTime;
			sendUserId = applInfo.get("applShainNo");
			break;
		case UPDATE_TYPE_PRESENT:
			// 押下されたボタンが確認依頼の場合、更新ステータスを強制的に確認依頼に変更
			updateStatus = CodeConstant.STATUS_KAKUNIN_IRAI;
			agreDate = sysDateTime;
			sendUserId = applInfo.get("applShainNo");
			mailKbn = CodeConstant.ROUTER_PRESENT_TSUCHI;
			break;
		default:
			// ボタン指定なしエラー
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1079);
			return false;
		}

		boolean result = true;
		// 申請書類履歴テーブル更新処理（利用クラス：SkfApplHistoryInfoUtils、利用メソッド：UpdateApplHistoryAgreeStatus）
		Map<String, String> errorMsg = new HashMap<String, String>();
		SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp updApplInfo = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp();
		LogUtils.infoByMsg("申請書類履歴取得:applNo:" + applInfo.get("applNo"));
		updApplInfo = skfApplHistoryInfoUtils.getApplHistoryInfoForUpdate(companyCd, applInfo.get("applShainNo"),
				applInfo.get("applNo"), applInfo.get("applId"));
		if (updApplInfo == null) {
			LogUtils.infoByMsg("申請書類履歴取得異常(NULL):applNo:" + applInfo.get("applNo"));
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}
		// 楽観的排他チェック（申請情報履歴）
		if (!CheckUtils.isEqual(updApplInfo.getUpdateDate(),
				dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE))) {
			LogUtils.infoByMsg("申請書類履歴排他チェックエラー:" + applInfo.get("applNo"));
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}

		// 承認者２が更新対象であり、承認者１の値がすでに入っていた場合は更新用の変数に投入
		if (NfwStringUtils.isNotEmpty(shoninName2) && NfwStringUtils.isNotEmpty(updApplInfo.getAgreName1())) {
			shoninName1 = updApplInfo.getAgreName1();
		}
		Date lastUpdateDate = dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE);
		LogUtils.infoByMsg("申請書類履歴更新:applNo:" + applInfo.get("applNo") + ",ステータス:" + updateStatus);
		result = skfApplHistoryInfoUtils.updateApplHistoryAgreeStatusForBihin(companyCd, applInfo.get("applShainNo"),
				applInfo.get("applNo"), applInfo.get("applId"), null, applTacFlg, updateStatus, agreDate, shoninName1,
				shoninName2, lastUpdateDate, errorMsg);
		if (!result) {
			LogUtils.infoByMsg("申請書類履歴更新エラー:" + applInfo.get("applNo"));
			// エラーメッセージ（メッセージID：.E_SKF_1075)）を設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// モバイルルーター借用希望申請書テーブルのデータを更新する
		result = updateKiboShinsei( dto, applInfo, updateStatus);
		if (!result) {
			return false;
		}

		// コメント更新
		String commentNote = dto.getCommentNote();
		boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applInfo.get("applNo"),
				updateStatus, commentNote, errorMsg);
		if (!commentErrorMessage) {
			ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
			return false;
		}
		
		// 申請履歴の更新後データを取り直す
		updApplInfo = new SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp();
		updApplInfo = skfApplHistoryInfoUtils.getApplHistoryInfoForUpdate(companyCd, applInfo.get("shainNo"),
				applInfo.get("applNo"), applInfo.get("applId"));
		if (updApplInfo == null) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// 押下されたボタンが確認依頼の場合
		if (UPDATE_TYPE_PRESENT.equals(execName)) {
			// 添付ファイル管理テーブル更新処理
			boolean resultUpdateFile = updateAttachedFileInfo(updateStatus, applInfo.get("applNo"),
				dto.getShainNo(), attachedFileList, applTacFlg, updApplInfo, errorMsg, loginUserInfo.get("userCd"),
				FunctionIdConstant.SKF2100_SC002);
			if (!resultUpdateFile) {
				// 更新エラー
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}
		}

		// 差戻完了通知・修正依頼完了通知・承認完了の場合のみ
		if (mailKbn != null) {
			switch (mailKbn) {
			case CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI:
			case CodeConstant.HININ_KANRYO_TSUCHI:
			case CodeConstant.SHONIN_KANRYO_TSUCHI:
			case CodeConstant.ROUTER_PRESENT_TSUCHI:
				// 掲載URL
				String urlBase = "/skf/Skf2010Sc003/init?SKF2010_SC003&menuflg=1&tokenCheck=0";
				skfMailUtils.sendApplTsuchiMailToAddresss(mailKbn, applInfo, commentNote, null, sendUserId, CodeConstant.NONE,
						urlBase,dto.getMailAddress());
				break;

			}
		}
		// ステータス値を更新
		applInfo.put("status", updateStatus);
		dto.setApplStatus(updateStatus);


		return true;
	}

	
	/**
	 * モバイルルーター借用希望申請関連テーブルを更新する(承認）
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applId
	 * @param nextStatus
	 * @return 更新処理成否
	 */
	private boolean updateKiboShinsei(Skf2100Sc002CommonDto dto, Map<String, String> applInfo,String nextStatus){
		boolean result = false;
		
		String applStatus = applInfo.get("status");
		String shainNo = applInfo.get("applShainNo");
		String applNo = applInfo.get("applNo");
		
		// モバイルルーター貸出予定テーブルのステータス情報を更新する
		result = skfRouterInfoUtils.updateRouterLYoteiStatus(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO, nextStatus,null,null,null);
		if(!result){
			// 更新失敗					
			// エラーメッセージ（メッセージID：E_SKF_1075）を設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}
		
		// モバイルルーター借用希望申請情報取得
		// モバイルルーター借用希望申請書テーブルのデータ取得
		Skf2100TMobileRouterKiboShinsei kiboShinsei = new Skf2100TMobileRouterKiboShinsei();
		kiboShinsei = skfShinseiUtils.getSksRouterKiboShinseiInfo(companyCd,applNo);
		if(kiboShinsei == null){
			LogUtils.infoByMsg("モバイルルーター借用希望申請情報取得エラー:applNo:" + applNo);
			// 取得失敗
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}
		switch (applStatus) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
			// 申請中、審査中
			if(CodeConstant.STATUS_KAKUNIN_IRAI.equals(nextStatus)){
				//次のステータスが確認依頼
				Date lastUpdateDateRouterShinsei = dto.getLastUpdateDate(ROUTER_KIBO_KEY_LAST_UPDATE_DATE);
				if (!CheckUtils.isEqual(kiboShinsei.getUpdateDate(), lastUpdateDateRouterShinsei)) {
					// 排他チェックエラー
					LogUtils.infoByMsg("モバイルルーター借用希望申請排他チェックエラー:applNo:" + applNo);
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
					return false;
				}
				// モバイルルーター借用希望申請書テーブルのデータを更新する
				result = skfRouterInfoUtils.updateRouterKiboShinsei(companyCd, applNo, dto.getOriginalCompanyCd(), 
						dto.getPayCompanyCd(), dto.getMobileRouterNo(),dto.getIccid(),
						dto.getImei(), dto.getShippingDate(),null);
				if (!result) {
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				// モバイルルーター貸出管理簿IDの取得
				Long newKanriId = skfRouterInfoUtils.getMaxRouterKanriId();
				// モバイルルーター貸出管理簿登録
				Skf2100TMobileRouterLedger routerLRecord = new Skf2100TMobileRouterLedger();
				routerLRecord.setMobileRouterKanriId(newKanriId);
				routerLRecord.setMobileRouterNo(dto.getMobileRouterNo());
				routerLRecord.setShainNo(shainNo);
				routerLRecord.setOriginalCompanyCd(dto.getOriginalCompanyCd());
				routerLRecord.setPayCompanyCd(dto.getPayCompanyCd());
				routerLRecord.setHannyuTel(kiboShinsei.getTel());
				routerLRecord.setHannyuMailaddress(kiboShinsei.getMailaddress());
				routerLRecord.setHannyuApplDay(kiboShinsei.getApplDate());
				routerLRecord.setUseStartHopeDay(kiboShinsei.getUseStartHopeDay());
				routerLRecord.setShippingDate(dto.getShippingDate());
				LogUtils.infoByMsg("モバイルルーター貸出管理簿登録(確認依頼):管理簿ID:" + newKanriId + ",社員番号:" + shainNo);
				
				int inCount = skf2100TMobileRouterLedgerRepository.insertSelective(routerLRecord);
				if(inCount <= 0){
					// 登録失敗
					LogUtils.infoByMsg("モバイルルーター貸出管理簿登録異常(確認依頼):管理簿ID:" + newKanriId + ",社員番号:" + shainNo);
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
					return false;
				}
				
				// モバイルルーター貸出予定データテーブルのデータを更新する
				// モバイルルーター貸出予定テーブルのステータス情報を更新する
				result = skfRouterInfoUtils.updateRouterLYoteiStatus(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO, 
						nextStatus, CodeConstant.ROUTER_LEND_JOKYO_TEIJI, dto.getMobileRouterNo(),newKanriId);
				if(!result){
					// 更新失敗
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				// モバイルルーターマスタの更新
				result = skfRouterInfoUtils.updateMobileRouterLJudment(dto.getMobileRouterNo(), CodeConstant.ROUTER_LENDING_JUDGMENT_USE, null);
				if(!result){
					// 更新失敗
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
			}
			break;
		case CodeConstant.STATUS_SHONIN1:
			//ステータスが承認中
			// 次のステータスが「40:承認済み」の場合、貸出管理簿、使用料履歴を登録する
			if(CodeConstant.STATUS_SHONIN_ZUMI.equals(nextStatus)){
				// モバイルルーター貸出管理簿更新
				
				// 予定データから管理簿ID取得
				Skf2100TRouterLendingYoteiData yoteiData = skfRouterInfoUtils.getRouterLYoteiData(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO);
				
				Long newKanriId = yoteiData.getMobileRouterKanriId();
				
				Skf2100TMobileRouterLedger routerLRecord = new Skf2100TMobileRouterLedger();
				routerLRecord.setMobileRouterKanriId(newKanriId);
				routerLRecord.setReceivedDate(kiboShinsei.getReceivedDate());
				LogUtils.infoByMsg("モバイルルーター貸出管理簿更新(承認):管理簿ID:" + newKanriId + ",社員番号:" + shainNo);
				
				// モバイルルーター貸出管理簿更新
				int inCount = skf2100TMobileRouterLedgerRepository.updateByPrimaryKeySelective(routerLRecord);
				if(inCount <= 0){
					// 更新失敗
					LogUtils.infoByMsg("モバイルルーター貸出管理簿更新異常(承認):管理簿ID:" + newKanriId + ",社員番号:" + shainNo);
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				// モバイルルーター貸出予定データ更新
				result = skfRouterInfoUtils.updateRouterLYoteiStatus(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO, 
						CodeConstant.STATUS_SHONIN_ZUMI, CodeConstant.ROUTER_LEND_JOKYO_USE, null,null);
				if(!result){
					// 更新失敗
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				
				// 月次処理月管理テーブルを読み込み当月処理月取得
				String yearMonth = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
				
				// 月別モバイルルーター使用料明細、月別モバイルルーター使用料履歴登録
				// 汎用備品項目設定取得
//				Skf3050MGeneralEquipmentItem equipment = new Skf3050MGeneralEquipmentItem();
//				equipment = skf3050MGeneralEquipmentItemRepository.selectByPrimaryKey();
				SkfRouterInfoUtilsGetEquipmentPaymentExp equipment = new SkfRouterInfoUtilsGetEquipmentPaymentExp();
				equipment = skfRouterInfoUtils.getEquipmentPayment(CodeConstant.GECD_MOBILEROUTER, yearMonth);
				if(equipment == null){
					// 取得失敗
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1118,"汎用備品項目設定テーブル");
					return false;
				}
				
				// 月別モバイルルーター使用料明細登録
				Skf2100TMobileRouterRentalRirekiMeisai meisaiRecord = new Skf2100TMobileRouterRentalRirekiMeisai();
				meisaiRecord.setMobileRouterKanriId(newKanriId);
				meisaiRecord.setYearMonth(yearMonth);
				meisaiRecord.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
				meisaiRecord.setMobileRouterGenbutsuGaku(equipment.getEquipmentPayment());
				meisaiRecord.setMobileRouterApplKbn(CodeConstant.BIHIN_SHINSEI_KBN_ARI);//申請有
				meisaiRecord.setMobileRouterReturnKbn(CodeConstant.BIHIN_HENKYAKU_SURU);//要返却
				LogUtils.infoByMsg("月別モバイルルーター使用料明細登録:管理簿ID:" + newKanriId + ",処理年月:" + yearMonth);
				
				inCount = skf2100TMobileRouterRentalRirekiMeisaiRepository.insertSelective(meisaiRecord);
				if(inCount <= 0){
					// 登録失敗
					LogUtils.infoByMsg("月別モバイルルーター使用料明細登録異常:管理簿ID:" + newKanriId + ",処理年月:" + yearMonth);
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
					return false;
				}
				
				// 月別モバイルルーター使用料登録
				Skf2100TMobileRouterRentalRireki rirekiRecord = new Skf2100TMobileRouterRentalRireki();
				rirekiRecord.setMobileRouterKanriId(newKanriId);
				rirekiRecord.setYearMonth(yearMonth);
				rirekiRecord.setMobileRouterGenbutuGoukei(equipment.getEquipmentPayment());
				LogUtils.infoByMsg("月別モバイルルーター使用料履歴登録:管理簿ID:" + newKanriId + ",処理年月:" + yearMonth);
				inCount = skf2100TMobileRouterRentalRirekiRepository.insertSelective(rirekiRecord);
				if(inCount <= 0){
					// 登録失敗
					LogUtils.infoByMsg("月別モバイルルーター使用料履歴登録異常:管理簿ID:" + newKanriId + ",処理年月:" + yearMonth);
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
					return false;
				}
				
				// モバイルルーターマスタ更新
				// モバイルルーターマスタの更新
				result = skfRouterInfoUtils.updateMobileRouterLJudment(dto.getMobileRouterNo(), CodeConstant.ROUTER_LENDING_JUDGMENT_USE, null);
				if(!result){
					// 更新失敗
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
			}
			break;
		}
		
		return true;
		
	}
	
	/**
	 * 添付ファイル管理テーブルの更新
	 * 
	 * 
	 * @param newStatus
	 * @param applNo 申請書類管理番号
	 * @param shainNo 申請書の社員番号
	 * @param attachedFileList 添付ファイルリスト
	 * @param applTacFlg 添付資料有無 有：1 無：0
	 * @param applInfo 申請書情報
	 * @param errorMsg エラーメッセージ用Map
	 * @param userId 更新者のuserId
	 * @param programId 機能ID
	 * @return
	 * @throws Exception
	 * @throws IllegalAccessException
	 */
	protected boolean updateAttachedFileInfo(String newStatus, String applNo, String shainNo,
			List<Map<String, Object>> attachedFileList, String applTacFlg,
			SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp applInfo, Map<String, String> errorMsg, String userId,
			String pageId) throws IllegalAccessException, Exception {

		// 添付ファイルの更新は削除→登録で行う
		if(!skfAttachedFileUtils.deleteAttachedFile(applNo, shainNo, errorMsg)){
			LogUtils.infoByMsg("添付ファイルの更新削除失敗 ");
			return false;
		}
		
		// 添付ファイル管理テーブルを更新する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				SkfAttachedFileUtilsInsertAttachedFileExp insertData = new SkfAttachedFileUtilsInsertAttachedFileExp();
				insertData = mappingTAttachedFile(attachedFileMap, applNo, shainNo);
				boolean insRes = skfAttachedFileUtils.insertAttachedFile(insertData,errorMsg);
					if (!insRes) {
						LogUtils.infoByMsg(" 添付ファイル管理テーブルを登録失敗");
						return false;
					}
			}
		}

		// 申請書類履歴テーブルの添付書類有無を更新
		LogUtils.debugByMsg("申請書類履歴テーブルの添付書類有無を更新");

		String applId = applInfo.getApplId();
		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		// キー項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(applNo);
		setValue.setApplId(applId);
		setValue.setShainNo(shainNo);

		// 更新項目をセット
		setValue.setApplTacFlg(String.valueOf(applTacFlg));
		setValue.setUpdateUserId(userId);
		setValue.setUpdateProgramId(pageId);

		int cnt = skf2040Sc002UpdateApplHistoryApplTacFlgExpRepository.updateApplHistoryApplTacFlg(setValue);
		if (cnt <= 0) {
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
	private SkfAttachedFileUtilsInsertAttachedFileExp mappingTAttachedFile(Map<String, Object> attachedFileMap, String applNo,
			String shainNo) {
		SkfAttachedFileUtilsInsertAttachedFileExp resultData = new SkfAttachedFileUtilsInsertAttachedFileExp();

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
		String fileSize = CodeConstant.NONE;
		if (attachedFileMap.get("fileSize") != null) {
			fileSize = attachedFileMap.get("fileSize").toString();
		} else if (attachedFileMap.get("attachedFileSize") != null) {
			fileSize = attachedFileMap.get("attachedFileSize").toString();
		}
		resultData.setFileSize(fileSize);
		// 登録者
		String userCd = LoginUserInfoUtils.getUserCd();
		resultData.setUserId(userCd);
		// 登録機能
		resultData.setProgramId(FunctionIdConstant.SKF2100_SC002);
		return resultData;
	}
	
	/**
	 * パラメータのJSON文字列配列をリスト形式に変換して返却する
	 * 
	 * @param jsonStr	JSON文字列配列
	 * @return			List<Map<String, Object>>
	 */
	public List<Map<String, Object>> jsonArrayToArrayList (String jsonStr) {

		// 返却用リスト
		List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		// JSON文字列判定
		if (Objects.equals(jsonStr, null) || jsonStr.length() <= 0) {
			LogUtils.debugByMsg("文字列未設定");
			// 文字列未設定のため処理しない
			return listData;
		}
		try {
			JSONArray arr = null;
			arr = new JSONArray(jsonStr);
			if (arr != null) {
				int arrCnt = arr.length();
				ObjectMapper mapper = new ObjectMapper();
				for(int i = 0; i < arrCnt; i++) {
					listData.add(mapper.readValue(arr.get(i).toString(), new TypeReference<Map<String, Object>>(){}));
				}
				arr = null;
				mapper = null;
			}
		} catch (JSONException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonParseException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (JsonMappingException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		} catch (IOException e) {
			LogUtils.infoByMsg("jsonArrayToArrayList, " + e.getMessage());
		}
		return listData;
	}
	
	/**
	 * 可変ラベル値を復元
	 * エラー時の可変ラベルの値を設定する。
	 * 
	 * 「※」項目はアドレスとして戻り値になる。
	 * 
	 * @param labelList		可変ラベルリスト
	 * @param comDto		*DTO
	 */
	public void setVariableLabel(List<Map<String, Object>> labelList, Skf2100Sc002CommonDto comDto) {
		// 可変ラベル値
		Map <String, Object> labelMap = labelList.get(0);
		// 通しNo
		String mobileRouterNo = (labelMap.get("mobileRouterNo") != null) ? labelMap.get("mobileRouterNo").toString() : "0";
		// ICCID
		String iccid = (labelMap.get("iccid") != null) ? labelMap.get("iccid").toString() : "";
		// IMEI
		String imei = (labelMap.get("imei") != null) ? labelMap.get("imei").toString() : "";

		
		/** 戻り値設定 */
		comDto.setMobileRouterNo(Long.parseLong(mobileRouterNo));
		comDto.setIccid(iccid);
		comDto.setImei(imei);
		comDto.setHdnMobileRouterNo(Long.parseLong(mobileRouterNo));
		comDto.setHdnIccid(iccid);
		comDto.setHdnImei(imei);
		
	}
}
