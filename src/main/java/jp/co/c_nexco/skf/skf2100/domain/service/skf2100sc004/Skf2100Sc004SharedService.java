package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc004;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfApplHistoryInfoUtils.SkfApplHistoryInfoUtilsGetApplHistoryInfoForUpdateExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterHenkyakuShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterLedger;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TRouterLendingYoteiData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc002.Skf2040Sc002GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetCompanyAgencyListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterHenkyakuShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterLedgerRepository;
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
import jp.co.c_nexco.skf.common.util.SkfApplHistoryInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtils;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc004common.Skf2100Sc004CommonDto;
/**
 * Skf2100Sc004SharedService モバイルルーター返却申請書（アウトソース）共通処理Service
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc004SharedService {

	private String companyCd = CodeConstant.C001;
	
	private MenuScopeSessionBean menuScopeSessionBean;

	private static final String REASON_LABEL = "申請者へのコメント";
	// 更新内容
	private static final String UPDATE_TYPE_APPLY = "apply"; // 承認
	private static final String UPDATE_TYPE_SENDBACK = "sendback"; // 差戻し
	private static final String UPDATE_TYPE_REVISION = "revision"; // 修正依頼

	public static final String sFalse = "false";
	public static final String sTrue = "true";
	// 排他処理用最終更新日付
	public static final String APPL_HISTORY_KEY_LAST_UPDATE_DATE = "skf2010_t_appl_history_UpdateDate";
	public static final String ROUTER_HENKYAKU_KEY_LAST_UPDATE_DATE = "skf2100_t_mobile_router_henkyaku_shisei_UpdateDate";

	// 更新フラグ
	protected static final String NO_UPDATE_FLG = "0";
	protected static final String UPDATE_FLG = "1";
	
	
	@Autowired
	private Skf2020Sc002GetApplHistoryInfoExpRepository skf2020Sc002GetApplHistoryInfoExpRepository;

	@Autowired
	private Skf2040Sc002GetApplHistoryInfoExpRepository skf2040Sc002GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf3022Sc006GetCompanyAgencyListExpRepository skf3022Sc006GetCompanyAgencyListExpRepository;
	
	@Autowired
	private Skf2100TMobileRouterLedgerRepository skf2100TMobileRouterLedgerRepository;
	@Autowired
	private Skf2100TMobileRouterHenkyakuShinseiRepository skf2100TMobileRouterHenkyakuShinseiRepository;

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
		menuScopeSessionBean.remove(SessionCacheKeyConstant.COMMON_ATTACHED_FILE_CONFLICT_SESSION_KEY);
	}

	
	
	/**
	 * 申請情報から初期表示項目を設定。
	 * 
	 * @param dto
	 * @param initializeErrorFlg 初期表示エラー判定フラグ true:実行 false:何もしない
	 */
	protected boolean setDisplayData(Skf2100Sc004CommonDto dto) {

		boolean returnValue = true;

		dto.setBtnApproveDisabled(null);
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
			setInitializeError(dto);
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
		 * モバイルルーター返却申請書テーブルから申請情報を取得
		 */
		Skf2100TMobileRouterHenkyakuShinsei routerHenkyakuInfo = new Skf2100TMobileRouterHenkyakuShinsei();
		routerHenkyakuInfo = skfShinseiUtils.getSksRouterHenkyakuShinseiInfo(CodeConstant.C001, dto.getApplNo());

		LogUtils.debugByMsg("モバイルルーター返却申請書： " + routerHenkyakuInfo);

		// 初期表示エラー判定
		// データが取得できなかった場合は承認ボタンを使用不可にする
		if (routerHenkyakuInfo == null) {
				setInitializeError(dto);
				returnValue = false;
				return returnValue;
		}
		
		if (routerHenkyakuInfo != null) {
			// 機関
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getAgency())) {
				LogUtils.debugByMsg("機関：" + routerHenkyakuInfo.getAgency());
				dto.setAgency(routerHenkyakuInfo.getAgency());
			}
			// 部等
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getAgency())) {
				LogUtils.debugByMsg("部等：" + routerHenkyakuInfo.getAffiliation1());
				dto.setAffiliation1(routerHenkyakuInfo.getAffiliation1());
			}
			// 室・課等
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getAffiliation2())) {
				LogUtils.debugByMsg("室・課等：" + routerHenkyakuInfo.getAffiliation2());
				dto.setAffiliation2(routerHenkyakuInfo.getAffiliation2());
			}
			// 社員番号
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getShainNo())) {
				LogUtils.debugByMsg("社員番号：" + routerHenkyakuInfo.getShainNo());
				dto.setShainNo(routerHenkyakuInfo.getShainNo());
			}
			// 氏名
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getName())) {
				LogUtils.debugByMsg("氏名：" + routerHenkyakuInfo.getName());
				dto.setName(routerHenkyakuInfo.getName());
			}
			// 電話番号
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getTel())) {
				LogUtils.debugByMsg("電話番号：" + routerHenkyakuInfo.getTel());
				dto.setTel(routerHenkyakuInfo.getTel());
			}
			// 通しNo
			if (routerHenkyakuInfo.getMobileRouterNo() != null) {
				LogUtils.debugByMsg("通しNo：" + routerHenkyakuInfo.getMobileRouterNo());
				dto.setMobileRouterNo(routerHenkyakuInfo.getMobileRouterNo());
			}
			// ICCID
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getIccid())) {
				LogUtils.debugByMsg("ICCID：" + routerHenkyakuInfo.getIccid());
				dto.setIccid(routerHenkyakuInfo.getIccid());
			}
			// IMEI
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getImei())) {
				LogUtils.debugByMsg("IMEI：" + routerHenkyakuInfo.getImei());
				dto.setImei(routerHenkyakuInfo.getImei());
			}
			// 故障フラグ
			dto.setHdnChkFaultSelect(null);
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getFaultFlag())) {
				LogUtils.debugByMsg("故障フラグ：" + routerHenkyakuInfo.getFaultFlag());
				dto.setFaultFlag(routerHenkyakuInfo.getFaultFlag());
				if(CodeConstant.ROUTER_FAULT_FLAG_FAULT.equals(routerHenkyakuInfo.getFaultFlag())){
					dto.setHdnChkFaultSelect("true");
				}
			}
			// 最終利用日
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getLastUseDay())) {
				LogUtils.debugByMsg("最終利用日：" + routerHenkyakuInfo.getLastUseDay());
				dto.setLastUseDay(routerHenkyakuInfo.getLastUseDay());
				dto.setLastUseDaylbl(skfDateFormatUtils.dateFormatFromString(routerHenkyakuInfo.getLastUseDay(), "yyyy/MM/dd"));
			}
			// 窓口返却日
			if (NfwStringUtils.isNotEmpty(routerHenkyakuInfo.getReturnDay())) {
				LogUtils.debugByMsg("窓口返却日：" + routerHenkyakuInfo.getReturnDay());
				dto.setReturnDay(routerHenkyakuInfo.getReturnDay());
				dto.setReturnDaylbl(skfDateFormatUtils.dateFormatFromString(routerHenkyakuInfo.getReturnDay(), "yyyy/MM/dd"));
			}
			
			// 更新日時
			LogUtils.debugByMsg("更新日時" + routerHenkyakuInfo.getUpdateDate());
			dto.addLastUpdateDate(ROUTER_HENKYAKU_KEY_LAST_UPDATE_DATE, routerHenkyakuInfo.getUpdateDate());
			

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
		boolean result = skfLoginUserInfoUtils.isAgreeAuthority( CodeConstant.C001, FunctionIdConstant.R0108, loginUserInfo.get("roleId"), dto.getApplStatus());
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
	 * 申請書類履歴テーブルの情報取得・排他制御更新日設定
	 * 
	 * @param dto
	 */
	
	protected void setApplHistoryUpdateDate(Skf2100Sc004CommonDto dto) {
		// 申請書類履歴テーブルから申請日の取得
		List<Skf2020Sc002GetApplHistoryInfoExp> historyInfo = new ArrayList<Skf2020Sc002GetApplHistoryInfoExp>();
		Skf2020Sc002GetApplHistoryInfoExpParameter parameter = new Skf2020Sc002GetApplHistoryInfoExpParameter();
		parameter.setCompanyCd(CodeConstant.C001);
		parameter.setApplNo(dto.getApplNo());
		historyInfo = skf2020Sc002GetApplHistoryInfoExpRepository.getApplHistoryInfo(parameter);
		// 申請書類履歴テーブル排他制御用更新日の設定
		if (historyInfo != null && historyInfo.size() > 0) {
			dto.setApplDate(historyInfo.get(0).getApplDate());
			dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, historyInfo.get(0).getUpdateDate());
		}
	}

	/**
	 * 初期表示エラー時の処理 更新処理を行わせないようボタンを使用不可にする。
	 * 
	 * @param dto Skf2100Sc001CommonDto
	 */
	protected void setInitializeError(Skf2100Sc004CommonDto dto) {
		// 更新処理を行わせないよ うボタンを使用不可に
		// 承認
		dto.setBtnApproveDisabled(sTrue);
		
		ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1077);
	}
	
	
	

	/**
	 * 情報のクリア
	 * 
	 * @param dto
	 */
	protected void setClearInfo(Skf2100Sc004CommonDto dto) {

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
		// 通しNo
		dto.setMobileRouterNo(null);
		// ICCID
		dto.setIccid(null);
		// IMEI
		dto.setImei(null);
		// 故障フラグ
		dto.setHdnChkFaultSelect(null);
		// 最終利用日
		dto.setLastUseDay(null);
		dto.setLastUseDaylbl(null);
		// 返却日
		dto.setReturnDay(null);
		// コメント
		dto.setCommentNote(null);
	
	}

	
	/**
	 * 表示項目の活性制御または表示制御を行う。
	 * 
	 * @param dto <br>
	 *            補足：設定値 TRUE:活性or表示 FALSE：非活性or非表示
	 */
	protected void setControlValue(Skf2100Sc004CommonDto dto) {
		// ステータスによりコントロールの活性制御を行う
		
		// フラグ初期化
		// 窓口返却日編集
		dto.setReturnDayEditFlag(false);
		dto.setRouterInfoViewFlag(false);
		dto.setCommentInputFlag(false);
		
		if(dto.getApplStatus() == null){
			//前の画面へとコメントボタン以外非表示
			dto.setAllButtonEscape(true);
			return;
		}
		
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		switch (dto.getApplStatus()) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_SHONIN1:
			case CodeConstant.STATUS_SHONIN2:
				// 「審査中」「31：承認１」「32：承認２」の場合
				dto.setAllButtonEscape(false);//承認、差戻、修正依頼ボタン表示
				dto.setReturnDayEditFlag(true); //窓口返却日編集
				dto.setCommentInputFlag(true); // コメント編集
				setApprovalDisp(dto);
				break;
			default:
				// 上記以外
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
	private void setApprovalDisp(Skf2100Sc004CommonDto dto) {
		// 承認権限無
		if(dto.isHdnApprovalFlg()){
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

	/**
	 * コメント欄入力チェック
	 * 
	 * @param dto
	 * @param checkFlag
	 * @return
	 * @throws Exception
	 */
	public boolean validateReason(Skf2100Sc004CommonDto dto, boolean checkFlag) throws Exception {

		String commentNote = dto.getCommentNote();

		// コメント必須で未入力の場合
		if (checkFlag && NfwStringUtils.isEmpty(commentNote)) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "commentNote" }, MessageIdConstant.E_SKF_1048,
					REASON_LABEL);
			return false;
		}

		// 値が4000バイトを超えた際はエラー
		int commentMaxLength = Integer.parseInt(PropertyUtils.getValue("skf2100.skf2100_sc004.comment_max_count"));
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
	public boolean updateDispInfo(String execName, Skf2100Sc004CommonDto dto, Map<String, String> applInfo,
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

		String applStatus = applInfo.get("status");
		switch (execName) {
		case UPDATE_TYPE_APPLY:
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_SHINSEICHU:
				// 申請中、審査中の場合、更新ステータスを承認中1に変更
				// 次のステータス、メール区分、承認者名1、次の階層を設定
				updateStatus = CodeConstant.STATUS_SHONIN1;
				shoninName1 = loginUserInfo.get("userName");
				break;
			case CodeConstant.STATUS_SHONIN1:
				// 押下されたボタンが承認 かつステータスが承認1済の場合、更新ステータスを承認済みに変更
				// 次のステータス、メール区分、承認者名2、次の階層を設定
				updateStatus = CodeConstant.STATUS_SHONIN_ZUMI;
				mailKbn = CodeConstant.SHONIN_KANRYO_TSUCHI;
				shoninName1 = CodeConstant.NONE;
				shoninName2 = loginUserInfo.get("userName");
				sendUserId = applInfo.get("applShainNo");
				agreDate = sysDateTime;
				break;
			}
			break;
		case UPDATE_TYPE_REVISION:
			// 修正依頼
			switch (applStatus) {
			case CodeConstant.STATUS_SHINSACHU:
			case CodeConstant.STATUS_SHONIN1:
				// 押下されたボタンが修正依頼 かつステータスが審査中、承認中の場合、更新ステータスを強制的に修正依頼に変更
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
				applInfo.get("applNo"), applInfo.get("applId"), null, null, updateStatus, agreDate, shoninName1,
				shoninName2, lastUpdateDate, errorMsg);
		if (!result) {
			LogUtils.infoByMsg("申請書類履歴更新エラー:" + applInfo.get("applNo"));
			// エラーメッセージ（メッセージID：.E_SKF_1075)）を設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// モバイルルーター返却申請書関連テーブルのデータを更新する
		result = updateHenkyakuShinsei( dto, applInfo, updateStatus);
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

		// 修正依頼完了・差戻完了通知の場合のみ
		if (mailKbn != null) {
			switch (mailKbn) {
			case CodeConstant.SASHIMODOSHI_KANRYO_TSUCHI:
			case CodeConstant.HININ_KANRYO_TSUCHI:
			case CodeConstant.SHONIN_KANRYO_TSUCHI:
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
	 * モバイルルーター返却申請関連テーブルを更新する(承認）
	 * 
	 * @param companyCd
	 * @param shainNo
	 * @param applId
	 * @param nextStatus
	 * @return 更新処理成否
	 */
	private boolean updateHenkyakuShinsei(Skf2100Sc004CommonDto dto, Map<String, String> applInfo,String nextStatus){
		boolean result = false;
		
		String applStatus = applInfo.get("status");
		String shainNo = applInfo.get("applShainNo");
		String applNo = applInfo.get("applNo");
		
		// モバイルルーター貸出予定データテーブルのデータを更新する
		// モバイルルーター貸出予定テーブルのステータス情報を更新する
		result = skfRouterInfoUtils.updateRouterLYoteiStatus(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU, 
				nextStatus, CodeConstant.ROUTER_LEND_JOKYO_HENKYAKU,null,null,null);
		if(!result){
			// 更新失敗
			// エラーメッセージ（メッセージID：E_SKF_1075）を設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}
		
		// モバイルルーター返却申請情報取得
		// モバイルルーター返却申請書テーブルのデータ取得
		Skf2100TMobileRouterHenkyakuShinsei henkyakuShinsei = new Skf2100TMobileRouterHenkyakuShinsei();
		henkyakuShinsei = skfShinseiUtils.getSksRouterHenkyakuShinseiInfo(companyCd,applNo);
		if(henkyakuShinsei == null){
			// 取得失敗
			LogUtils.infoByMsg("モバイルルーター返却申請情報取得エラー:applNo:" + applNo);
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
			return false;
		}
		switch (applStatus) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_SHINSACHU:
			// 申請中、審査中
			if(CodeConstant.STATUS_SHONIN1.equals(nextStatus)){
				//次のステータスが承認1
				Date lastUpdateDateRouterShinsei = dto.getLastUpdateDate(ROUTER_HENKYAKU_KEY_LAST_UPDATE_DATE);
				if (!CheckUtils.isEqual(henkyakuShinsei.getUpdateDate(), lastUpdateDateRouterShinsei)) {
					// 排他チェックエラー
					LogUtils.infoByMsg("モバイルルーター返却申請排他チェックエラー:applNo:" + applNo);
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1135);
					return false;
				}
				// モバイルルーター返却申請書テーブルのデータを更新する
				Skf2100TMobileRouterHenkyakuShinsei updData = new Skf2100TMobileRouterHenkyakuShinsei();
				updData.setCompanyCd(companyCd);
				updData.setApplNo(applNo);
				updData.setReturnDay(dto.getReturnDay());
				updData.setUpdateUserId(LoginUserInfoUtils.getUserCd());
				updData.setUpdateProgramId(FunctionIdConstant.SKF2100_SC004);
				LogUtils.infoByMsg("モバイルルーター返却申請書更新(承認中):applNo:" + applNo);
				int updCount = skf2100TMobileRouterHenkyakuShinseiRepository.updateByPrimaryKeySelective(updData);
				
				if (updCount <= 0) {
					LogUtils.infoByMsg("モバイルルーター返却申請書更新異常:applNo:" + applNo);
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
			}

			break;
		case CodeConstant.STATUS_SHONIN1:
			//ステータスが承認中
			// 次のステータスが「40:承認済み」の場合、貸出管理簿、貸出予定データ、マスタを更新する
			if(CodeConstant.STATUS_SHONIN_ZUMI.equals(nextStatus)){
				// モバイルルーター貸出管理簿IDの取得
				// モバイルルーター貸出予定データより取得
				Skf2100TRouterLendingYoteiData yoteiData = new Skf2100TRouterLendingYoteiData();
				yoteiData = skfRouterInfoUtils.getRouterLYoteiData(shainNo,CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU);
				if(yoteiData == null){
					// 取得失敗
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1118, "モバイルルーター貸出予定データ");
					return false;
				}
				
				// 貸出管理簿ID
				Long routerKanriId = yoteiData.getMobileRouterKanriId();
				
				Skf2100TMobileRouterLedger routerLRecord = new Skf2100TMobileRouterLedger();
				routerLRecord.setMobileRouterKanriId(routerKanriId);
				routerLRecord.setHanshutuApplDay(henkyakuShinsei.getApplDate());
				routerLRecord.setHansyutuTel(henkyakuShinsei.getTel());
				routerLRecord.setUseStopDay(henkyakuShinsei.getLastUseDay());
				routerLRecord.setReturnDay(henkyakuShinsei.getReturnDay());
				routerLRecord.setUpdateUserId(LoginUserInfoUtils.getUserCd());
				routerLRecord.setUpdateProgramId(FunctionIdConstant.SKF2100_SC004);
				LogUtils.infoByMsg("モバイルルーター貸出管理簿更新(返却承認):管理簿ID:" + routerKanriId
						+",最終利用日:"+henkyakuShinsei.getLastUseDay());
				
				int inCount = skf2100TMobileRouterLedgerRepository.updateByPrimaryKeySelective(routerLRecord);
				if(inCount <= 0){
					// 登録失敗
					LogUtils.infoByMsg("モバイルルーター貸出管理簿更新異常:管理簿ID:" + routerKanriId);
					// エラーメッセージを設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				// モバイルルーター貸出予定データテーブルのデータを更新する
				// モバイルルーター貸出予定テーブルのステータス情報を更新する
				result = skfRouterInfoUtils.updateRouterLYoteiStatus(shainNo, CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU, 
						nextStatus, CodeConstant.ROUTER_LEND_JOKYO_TAIYO,null,null,null);
				if(!result){
					// 更新失敗
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				// モバイルルーターマスタ更新
				// モバイルルーターマスタの更新
				String faultFlag = CodeConstant.ROUTER_FAULT_FLAG_NORMAL;
				if("true".equals(dto.getHdnChkFaultSelect())){
					faultFlag = CodeConstant.ROUTER_FAULT_FLAG_FAULT;
				}
				result = skfRouterInfoUtils.updateMobileRouterLJudment(dto.getMobileRouterNo(), 
						CodeConstant.ROUTER_LENDING_JUDGMENT_TAIYO, faultFlag);
				if(!result){
					// 更新失敗
					// エラーメッセージ（メッセージID：E_SKF_1075）を設定
					ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
					return false;
				}
				
				// 通知用メールアドレスの設定
				Skf2100TMobileRouterLedger ledgerData = skf2100TMobileRouterLedgerRepository.selectByPrimaryKey(routerKanriId);
				if(ledgerData != null){
					String mailAddress = ledgerData.getHannyuMailaddress();
					dto.setMailAddress(mailAddress);
				}
			}
			break;
		default:
			break;
		}
		

		
		
		if(CodeConstant.STATUS_SASHIMODOSHI.equals(nextStatus)
				|| CodeConstant.STATUS_HININ.equals(nextStatus)){
			// モバイルルーター貸出管理簿IDの取得
			// モバイルルーター貸出予定データより取得
			Skf2100TRouterLendingYoteiData yoteiData = new Skf2100TRouterLendingYoteiData();
			yoteiData = skfRouterInfoUtils.getRouterLYoteiData(shainNo,CodeConstant.TAIYO_HENKYAKU_KBN_HENKYAKU);
			if(yoteiData == null){
				// 取得失敗
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1118, "モバイルルーター貸出予定データ");
				return false;
			}
			
			// 貸出管理簿ID
			Long routerKanriId = yoteiData.getMobileRouterKanriId();
			// 通知用メールアドレスの設定
			Skf2100TMobileRouterLedger ledgerData = skf2100TMobileRouterLedgerRepository.selectByPrimaryKey(routerKanriId);
			if(ledgerData != null){
				String mailAddress = ledgerData.getHannyuMailaddress();
				dto.setMailAddress(mailAddress);
			}
		}


		return true;
		
	}
	
}
