package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc001;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc001.Skf2100Sc001GetApplHistoryInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2100Sc001.Skf2100Sc001GetApplHistoryInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TMobileRouterKiboShinsei;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2100TRouterLendingYoteiData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc002.Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2100Sc001.Skf2100Sc001GetApplHistoryInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TMobileRouterKiboShinseiRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2100TRouterLendingYoteiDataRepository;
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
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfRouterInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100Sc001common.Skf2100Sc001CommonDto;
import jp.co.intra_mart.foundation.service.client.file.PublicStorage;

/**
 * Skf2100Sc001 モバイルルーター借用希望申請書（申請者用)共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2100Sc001SharedService {
	
	private MenuScopeSessionBean menuScopeSessionBean;

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
	
	
	@Autowired
	private Skf2100Sc001GetApplHistoryInfoExpRepository skf2100Sc001GetApplHistoryInfoExpRepository;
	@Autowired
	private Skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository;
	@Autowired
	private Skf2020Sc002GetApplInfoExpRepository skf2020Sc002GetApplInfoExpRepository;
	
	@Autowired
	private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
	@Autowired
	private Skf2100TMobileRouterKiboShinseiRepository skf2100TMobileRouterKiboShinseiRepository;
	@Autowired
	private Skf2100TRouterLendingYoteiDataRepository skf2100TRouterLendingYoteiDataRepository;

	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfShinseiUtils skfShinseiUtils;
	@Autowired
	private SkfRouterInfoUtils skfRouterInfoUtils;
	@Autowired
	private SkfAttachedFileUtils skfAttachedFileUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;

	
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
	 * 
	 * 画面初期表示
	 * 
	 * @param Skf2020Sc002CommonDto
	 */
	protected void initializeDisp(Skf2100Sc001CommonDto dto) {


		// 申請書管理番号の有無
		if (dto.getApplNo() != null) {

			// 登録済みデータの情報設定
			setSinseiInfo(dto, true);


		} else {
			// 無い場合
			if (dto.getShainList() != null && dto.getShainList().size() > 0) {
				// 社員情報がある場合は申請者情報からの設定
				setShainList(dto);
			} else {
				//エラーメッセージ表示
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_2005);
				// データが取得できなかった場合は更新ボタンを使用不可にする
				setInitializeError(dto);
				
			}
		}
	}
	
	
	/**
	 * 社宅入居希望等調書の申請情報から初期表示項目を設定。
	 * 
	 * @param dto
	 * @param initializeErrorFlg 初期表示エラー判定フラグ true:実行 false:何もしない
	 */
	protected void setSinseiInfo(Skf2100Sc001CommonDto dto, boolean initializeErrorFlg) {

		/**
		 * 申請書類履歴テーブル情報の取得
		 */
		setApplHistoryUpdateDate(dto);
		
		// 申請状況を取得
		Map<String, String> applStatusMap = skfGenericCodeUtils.getGenericCode("SKF1001");
		dto.setApplStatusText(applStatusMap.get(dto.getApplStatus()));

		/**
		 * モバイルルーター借用希望申請書テーブルから申請情報を取得
		 */
		Skf2100TMobileRouterKiboShinsei routerKiboInfo = new Skf2100TMobileRouterKiboShinsei();
		routerKiboInfo = skfShinseiUtils.getSksRouterKiboShinseiInfo(CodeConstant.C001, dto.getApplNo());

		LogUtils.debugByMsg("モバイルルーター借用希望申請書： " + routerKiboInfo);

		// 初期表示エラー判定
		if (initializeErrorFlg) {
			// データが取得できなかった場合は更新ボタンを使用不可にする
			if (routerKiboInfo == null) {
				setInitializeError(dto);
			}
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
			}
			// 給与支給会社
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getPayCompanyCd())) {
				String payCompanyName = skfShinseiUtils.getCompanyName(routerKiboInfo.getPayCompanyCd());
				LogUtils.debugByMsg("給与支給会社コード：" + routerKiboInfo.getPayCompanyCd());
				LogUtils.debugByMsg("給与支給会社：" + payCompanyName);
				dto.setPayCompanyCd(routerKiboInfo.getPayCompanyCd());
				dto.setPayCompany(payCompanyName);
			}
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

			}else if(CodeConstant.STATUS_KAKUNIN_IRAI.equals(dto.getApplStatus())){
				//ステータスが搬入待ちの場合、受領日に利用開始希望日を設定する
				dto.setReceivedDate(routerKiboInfo.getUseStartHopeDay());
			}
			// モバイルルーター本体受領チェックフラグ
			dto.setHdnHandbookReceiptChecked(null);
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getBodyReceiptCheckFlag())) {
				LogUtils.debugByMsg("モバイルルーター本体受領チェックフラグ：" + routerKiboInfo.getBodyReceiptCheckFlag());
				dto.setBodyReceiptCheckFlag(routerKiboInfo.getBodyReceiptCheckFlag());
				if("1".equals(routerKiboInfo.getBodyReceiptCheckFlag())){
					dto.setHdnBodyReceiptChecked(sTrue);
				}
			}
			// モバイルルーター手引き受領チェックフラグ
			dto.setHdnHandbookReceiptChecked(null);
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getHandbookReceiptCheckFlag())) {
				LogUtils.debugByMsg("モバイルルーター手引き受領チェックフラグ：" + routerKiboInfo.getHandbookReceiptCheckFlag());
				dto.setHandbookReceiptCheckFlag(routerKiboInfo.getHandbookReceiptCheckFlag());
				if("1".equals(routerKiboInfo.getHandbookReceiptCheckFlag())){
					dto.setHdnHandbookReceiptChecked(sTrue);
				}
			}
			// 返却資材受領チェックフラグ
			dto.setHdnMaterialsReceivedChecked(null);
			if (NfwStringUtils.isNotEmpty(routerKiboInfo.getMaterialsReceivedCheckFlag())) {
				LogUtils.debugByMsg("返却資材受領チェックフラグ：" + routerKiboInfo.getMaterialsReceivedCheckFlag());
				dto.setMaterialsReceivedCheckFlag(routerKiboInfo.getMaterialsReceivedCheckFlag());
				if("1".equals(routerKiboInfo.getMaterialsReceivedCheckFlag())){
					dto.setHdnMaterialsReceivedChecked(sTrue);
				}
			}
			
			// 更新日時
			LogUtils.debugByMsg("更新日時" + routerKiboInfo.getUpdateDate());
			dto.addLastUpdateDate(ROUTER_KIBO_KEY_LAST_UPDATE_DATE, routerKiboInfo.getUpdateDate());
			
			// 「添付資料」欄の更新を行う
			List<Map<String, Object>> attachedFileList = new ArrayList<Map<String, Object>>();
			attachedFileList = refreshHeaderAttachedFile(dto.getApplNo(), attachedFileList);
			dto.setAttachedFileList(attachedFileList);

		}

		
	}
	
	/**
	 * 申請書類履歴テーブルの情報取得・排他制御更新日設定
	 * 
	 * @param dto
	 */
	
	protected void setApplHistoryUpdateDate(Skf2100Sc001CommonDto dto) {
		// 申請書類履歴テーブルから申請日の取得
		List<Skf2100Sc001GetApplHistoryInfoExp> historyInfo = new ArrayList<Skf2100Sc001GetApplHistoryInfoExp>();
		Skf2100Sc001GetApplHistoryInfoExpParameter parameter = new Skf2100Sc001GetApplHistoryInfoExpParameter();
		parameter.setCompanyCd(CodeConstant.C001);
		parameter.setApplNo(dto.getApplNo());
		historyInfo = skf2100Sc001GetApplHistoryInfoExpRepository.getApplHistoryInfo(parameter);
		// 申請書類履歴テーブル排他制御用更新日の設定
		if (historyInfo != null && historyInfo.size() > 0) {
			dto.setApplStatus(historyInfo.get(0).getApplStatus());
			dto.setApplDate(historyInfo.get(0).getApplDate());
			dto.addLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE, historyInfo.get(0).getUpdateDate());
		}
	}
	
	/**
	 * モバイルルーター借用希望申請書テーブルの情報取得・排他制御更新日設定
	 * 
	 * @param dto
	 */
	protected void setRouterKiboShinseiUpdateDate(Skf2100Sc001CommonDto dto) {
		// モバイルルーター借用希望申請書テーブルから申請情報を取得
		Skf2100TMobileRouterKiboShinsei routerKiboInfo = new Skf2100TMobileRouterKiboShinsei();
		routerKiboInfo = skfShinseiUtils.getSksRouterKiboShinseiInfo(CodeConstant.C001, dto.getApplNo());

		// モバイルルーター借用希望申請書テーブル排他制御用更新日の設定
		if (routerKiboInfo != null) {
			dto.addLastUpdateDate(ROUTER_KIBO_KEY_LAST_UPDATE_DATE, routerKiboInfo.getUpdateDate());
		}
	}
	
	/**
	 * 初期表示エラー時の処理 更新処理を行わせないようボタンを使用不可にする。
	 * 
	 * @param dto Skf2100Sc001CommonDto
	 */
	protected void setInitializeError(Skf2100Sc001CommonDto dto) {
		// 更新処理を行わせないよ う一時保存、申請ボタンを使用不可に
		// 一時保存
		dto.setBtnSaveDisabled(sTrue);
		// 申請
		dto.setBtnApplicationDisabled(sTrue);
		// 搬入完了
		dto.setBtnImportFinidhedDisabled(sTrue);
		// 取下げ
		dto.setBtnCancelDisabled(sTrue);
		
		ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1077);
	}
	
	
	/**
	 * 社員情報の設定
	 * 
	 * @param dto
	 */
	protected void setShainList(Skf2100Sc001CommonDto dto) {

		// 機関
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("agencyName"))) {
			dto.setAgency(dto.getShainList().get(0).get("agencyName"));
		}
		// 部等
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("affiliation1Name"))) {
			dto.setAffiliation1(dto.getShainList().get(0).get("affiliation1Name"));
		}
		// 室、チームまたは課
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("affiliation2Name"))) {
			dto.setAffiliation2(dto.getShainList().get(0).get("affiliation2Name"));
		}
		// 電話番号
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("tel"))) {
			dto.setTel(dto.getShainList().get(0).get("tel"));
		}
		// 社員番号
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("shainNo"))) {
			dto.setShainNo(dto.getShainList().get(0).get("shainNo"));
		}
		// 社員名
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("name"))) {
			dto.setName(dto.getShainList().get(0).get("name"));
		}
		// メールアドレス
		if (NfwStringUtils.isNotEmpty(dto.getShainList().get(0).get("mailAddress"))) {
			dto.setMailAddress(dto.getShainList().get(0).get("mailAddress"));
		}

		// 申請書ステータス
		dto.setApplStatus(CodeConstant.STATUS_MISAKUSEI);
		dto.setApplStatusText(CodeConstant.ZEN_HYPHEN);
	}

	/**
	 * 情報のクリア
	 * 
	 * @param dto
	 */
	protected void setClearInfo(Skf2100Sc001CommonDto dto) {

		String Msg = "クリア処理　：";

		// 前画面が申請内容確認以外の場合はクリア
		if (NfwStringUtils.isNotEmpty(dto.getPrePageId())
				&& !FunctionIdConstant.SKF2010_SC002.equals(dto.getPrePageId())) {

			// TEL
			dto.setTel(null);
			LogUtils.debugByMsg(Msg + "電話番号" + dto.getTel());
			// メールアドレス
			dto.setMailAddress(null);
			// 利用開始希望日
			dto.setUseStartHopeDay(null);
			// 受領日
			dto.setReceivedDate(null);
			// 受領チェック
			dto.setBodyReceiptCheckFlag(null);
			dto.setHandbookReceiptCheckFlag(null);
			dto.setMaterialsReceivedCheckFlag(null);
			dto.setHdnBodyReceiptChecked(null);
			dto.setHdnHandbookReceiptChecked(null);
			dto.setHdnMaterialsReceivedChecked(null);
			// コメント
			dto.setCommentNote(null);
			
			// 一時保存
			dto.setBtnSaveDisabled(null);
			// 申請
			dto.setBtnApplicationDisabled(null);
			// 搬入完了
			dto.setBtnImportFinidhedDisabled(null);
			// 取下げ
			dto.setBtnCancelDisabled(null);
		}
	}

	
	/**
	 * 表示項目の活性制御または表示制御を行う。
	 * 
	 * @param dto <br>
	 *            補足：設定値 TRUE:活性or表示 FALSE：非活性or非表示
	 */
	public void setControlValue(Skf2100Sc001CommonDto dto) {
		// ステータスによりコントロールの活性制御を行う
		
		//フラグ初期化
		//利用開始希望日編集
		dto.setUseStartHopeDayEditFlag(false);
		dto.setRouterInfoViewFlag(false);
		dto.setCommentInputFlag(false);
		dto.setMaskPattern("");
		
		if(dto.getApplStatus() == null){
			//未作成扱い
			dto.setBihinReadOnly(false);
			dto.setStatus01Flag(false);
			dto.setRouterInfoViewFlag(false);
			dto.setUseStartHopeDayEditFlag(true);
			dto.setCommentInputFlag(true);
			return;
		}
		
		// 画面権限等の設定による制御もあるため、非表示化・非活性化のみ行う
		switch (dto.getApplStatus()) {
		case CodeConstant.STATUS_ICHIJIHOZON:
		case CodeConstant.STATUS_MISAKUSEI:
		case CodeConstant.STATUS_SASHIMODOSHI:
			// 申請状況が「一時保存」「未作成」「修正依頼（差し戻し）」の場合
			// 「申請内容（入力用）」の活性・非活性制御
			dto.setBihinReadOnly(false);
			dto.setStatus01Flag(false);
			dto.setRouterInfoViewFlag(false);
			dto.setUseStartHopeDayEditFlag(true);
			dto.setCommentInputFlag(true);
			break;
		case CodeConstant.STATUS_SHINSEICHU:
			dto.setBihinReadOnly(false);
			// 「申請中」の場合は入力項目は全て非活性
			// コメント欄は非表示
			dto.setStatus01Flag(true);
			dto.setMaskPattern("ST01");
			dto.setRouterInfoViewFlag(false);
			break;
		case CodeConstant.STATUS_SHINSACHU:
		case CodeConstant.STATUS_HININ:
			dto.setBihinReadOnly(true);
			// 「審査中」「差戻し(否認)」の場合は入力項目は全て非活性
			// コメント欄は非表示
			dto.setStatus01Flag(true);
			dto.setStatus24Flag(true);
			dto.setMaskPattern("ST10");
			dto.setRouterInfoViewFlag(false);
			break;
		case CodeConstant.STATUS_KAKUNIN_IRAI:
			// 「確認依頼」
			dto.setBihinReadOnly(true);
			dto.setMaskPattern("ST20");
			dto.setCompletionDayDisabled(false);
			dto.setStatus24Flag(false);
			dto.setRouterInfoViewFlag(true);
			dto.setCommentInputFlag(true);
			break;
		default:
			dto.setBihinReadOnly(true);
			dto.setCompletionDayDisabled(true);
			dto.setStatus24Flag(true);
			dto.setRouterInfoViewFlag(true);
			dto.setMaskPattern("ST24");
			dto.setBodyReceiptCheckFlagDisabled(sTrue);
			dto.setHandbookReceiptCheckFlagDisabled(sTrue);
			dto.setMaterialsReceivedCheckFlagDisabled(sTrue);
			break;
		}

		// 機能ID
		String functionId = "skfapplrequirement";

		//ファイル存在チェック
		dto.setBtnCheckDisabled(null);
		try{
			String templateFilePath = SkfFileOutputUtils.getTemplateFilePath(functionId, "skf.skf_appl_requirement.FileId_RouterKibo");
			PublicStorage storage = SkfFileOutputUtils.getFilePublicStorage(templateFilePath);
			if (storage == null) {
				//テンプレートファイルが存在しません
				dto.setBtnCheckDisabled("true");
			}
		}
		catch(Exception e){
			dto.setBtnCheckDisabled("true");
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
	 * 申請書情報の取得
	 * 
	 * @param dto
	 * @return 申請書情報のMAP applInfoMap
	 */
	protected Map<String, String> getSkfApplInfo(Skf2100Sc001CommonDto dto) {
		Map<String, String> applInfoMap = new HashMap<String, String>();

		// 申請情報情報
		Skf2020Sc002GetApplInfoExp applInfo = new Skf2020Sc002GetApplInfoExp();
		Skf2020Sc002GetApplInfoExpParameter applParam = new Skf2020Sc002GetApplInfoExpParameter();
		applParam.setCompanyCd(CodeConstant.C001);
		applParam.setShainNo(dto.getShainNo());
		applParam.setApplNo(dto.getApplNo());
		applParam.setApplId(FunctionIdConstant.R0107);
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
	 * 保存処理
	 * 
	 * @param applInfo
	 * @param saveDto
	 * @throws UnsupportedEncodingException
	 */
	protected boolean saveInfo(Map<String, String> applInfo, Skf2100Sc001CommonDto dto)
			throws UnsupportedEncodingException {

		Map<String, String> errorMsg = new HashMap<String, String>();
		
		// 画面表示項目の保持
		setDispInfo(dto);

		// 画面表示制御再設定
		setControlValue(dto);

		// バイトカット処理
		cutByte(dto);

		// 社員番号を設定
		applInfo.put("shainNo", dto.getShainNo());
		// 添付ファイルの有無
		Map<String, String> applTacInfoMap = skfShinseiUtils.getApplAttachFlg(applInfo.get("shainNo"),
				applInfo.get("applNo"));
		applInfo.put("applTacFlg", applTacInfoMap.get("applTacFlg"));

		if (CodeConstant.STATUS_MISAKUSEI.equals(applInfo.get("status"))) {
			// 指定なし（新規）の場合
			dto.setApplStatus(CodeConstant.STATUS_MISAKUSEI);
			// 更新フラグを「0」に設定する
			applInfo.put("updateFlg", Skf2100Sc001SharedService.NO_UPDATE_FLG);
			// 新規登録処理
			if (!saveNewData(dto, applInfo)) {
				// 登録に失敗した場合は、戻り値をfalseとし処理中断
				return false;
			}
		} else {
			// 新規以外
			dto.setApplStatus(applInfo.get("status"));
			applInfo.put("updateFlg", Skf2100Sc001SharedService.UPDATE_FLG);

			// 申請履歴テーブルの更新
			switch (applInfo.get("newStatus")) {
			case CodeConstant.STATUS_ICHIJIHOZON:
				// 一時保存の場合
				if (!updateApplHistoryAgreeStatusIchiji(dto, applInfo)) {
					// 更新に失敗した場合は、戻り値をfalseとし処理中断
					return false;
				}
				break;
			case CodeConstant.STATUS_HININ: //差戻(否認)
			case CodeConstant.STATUS_SASHIMODOSHI: //修正依頼(差し戻し)
				break;
			default:
				// 申請ステータス更新（申請日時を更新しない)
				if (!updateApplHistoryAgreeStatus(dto, applInfo)) {
					// 更新に失敗した場合は、戻り値をfalseとし処理中断
					return false;
				}
			}
			
			//モバイルルーター貸出予定データのステータス更新
			if(!skfRouterInfoUtils.updateRouterLYoteiStatus(dto.getShainNo(), CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO
					, applInfo.get("newStatus"), null,null,null)){
				// 更新に失敗した場合は、戻り値をfalseとし処理中断
				// エラーメッセージを表示用に設定
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				return false;
			}

			// モバイルルーター借用希望申請書テーブルの更新処理
			if (!updateRouterKiboShinsei(dto, applInfo)) {
				// 更新に失敗した場合は、戻り値をfalseとし処理中断
				return false;
			}
			// ステータスを更新
			dto.setApplStatus(applInfo.get("newStatus"));
			
		}
		
		
		// 次が「申請中」「搬入済」の場合コメント登録処理
		switch (applInfo.get("newStatus")) {
		case CodeConstant.STATUS_SHINSEICHU:
		case CodeConstant.STATUS_HANNYU_ZUMI:
			// コメント登録
			String commentNote = dto.getCommentNote();
			boolean commentErrorMessage = skfCommentUtils.insertComment(CodeConstant.C001, applInfo.get("applNo"),
					applInfo.get("newStatus"), commentNote, errorMsg);
			if (!commentErrorMessage) {
				// エラーメッセージを表示用に設定
				if (NfwStringUtils.isNotEmpty(errorMsg.get("error"))) {
					ServiceHelper.addErrorResultMessage(dto, null, errorMsg.get("error"));
				}
				return false;
			}
			break;

		default:
			break;
		}

		
		return true;
	}
	
	/**
	 * 新規時の保存処理
	 * 
	 * @param dto
	 * @param applInfo
	 * @return 登録成功 true 失敗 false
	 */
	private boolean saveNewData(Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {
		// 申請書類管理番号を取得
		String newApplNo = skfShinseiUtils.getApplNo(CodeConstant.C001, applInfo.get("shainNo"),
				FunctionIdConstant.R0107);
		LogUtils.debugByMsg("申請書類管理番号" + newApplNo);
		// 取得に失敗した場合
		if (newApplNo == null) {
			// エラーメッセージを表示用に設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1024);
			// 保存処理を終了
			return false;
		} else {
			applInfo.put("applNo", newApplNo);
		}

		// 申請書類履歴テーブル登録処理
		boolean result = false;
		result = insertApplHistory(dto, applInfo);
		if(!result){
			// エラーメッセージを表示用に設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
			// 保存処理を終了
			return false;
		}

		// モバイルルーター借用希望申請書テーブル登録処理
		// バイルルーター借用希望申請書テーブルの設定
		Skf2100TMobileRouterKiboShinsei setValue = new Skf2100TMobileRouterKiboShinsei();
		applInfo.put("updateFlg", NO_UPDATE_FLG);
		setValue = setMobileRouterKiboShinsei(dto, setValue, applInfo);
		// 登録
		result =  insertRouterKiboShinsei(dto, setValue);
		if(!result){
			// エラーメッセージを表示用に設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
			// 保存処理を終了
			return false;
		}
		
		
		// 申請日をdtoに設定
		dto.setApplDate(dto.getApplHistroyApplDate());
		// ステータスを更新
		dto.setApplStatus(applInfo.get("newStatus"));
		// 申請書番号を設定
		dto.setApplNo(newApplNo);
		
		
		// モバイルルーター貸出予定データテーブルの登録・更新処理
		// 登録値設定
		Skf2100TRouterLendingYoteiData yoteiData = new Skf2100TRouterLendingYoteiData();
		yoteiData.setShainNo(dto.getShainNo());
		yoteiData.setTaiyoHenkyakuKbn(CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO);
		yoteiData.setApplNo(newApplNo);
		yoteiData.setName(dto.getName());
		yoteiData.setRouterApplStatus(applInfo.get("newStatus"));
		yoteiData.setGeneralEquipmentCd(CodeConstant.GECD_MOBILEROUTER);
		
		// モバイルルーター貸出予定データテーブルの登録状態チェック
		Skf2100TRouterLendingYoteiData checkData = skfRouterInfoUtils.getRouterLYoteiData(dto.getShainNo(),CodeConstant.TAIYO_HENKYAKU_KBN_TAIYO);
		int resCount= 0;
		if(checkData != null){
			//更新
			LogUtils.infoByMsg("モバイルルーター貸出予定データ更新:申請書類管理番号:" + newApplNo + ",社員番号:" + dto.getShainNo());
			resCount = skf2100TRouterLendingYoteiDataRepository.updateByPrimaryKeySelective(yoteiData);
			if(resCount <= 0){
				LogUtils.infoByMsg("モバイルルーター貸出予定データ更新異常:申請書類管理番号:" + newApplNo + ",社員番号:" + dto.getShainNo());
				// エラーメッセージを表示用に設定
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
				// 保存処理を終了
				return false;
			}
		}else{
			//登録
			LogUtils.infoByMsg("モバイルルーター貸出予定データ登録:申請書類管理番号:" + newApplNo + ",社員番号:" + dto.getShainNo());
			resCount = skf2100TRouterLendingYoteiDataRepository.insertSelective(yoteiData);
			if(resCount <= 0){
				LogUtils.infoByMsg("モバイルルーター貸出予定データ登録異常:申請書類管理番号:" + newApplNo + ",社員番号:" + dto.getShainNo());
				// エラーメッセージを表示用に設定
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1073);
				// 保存処理を終了
				return false;
			}
		}
		
		return true;
	}

	/**
	 * 申請書類履歴テーブル登録処理
	 * 
	 * @param dto
	 * @param applDate
	 * @param applInfo
	 * @param applTacFlg
	 */
	private boolean insertApplHistory(Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {

		// 申請書類履歴テーブルの設定
		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		dto.setApplHistroyApplDate(DateUtils.getSysDate());

		// 登録項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setShainNo(dto.getShainNo());
		setValue.setApplDate(dto.getApplHistroyApplDate());
		setValue.setApplNo(applInfo.get("applNo"));
		setValue.setApplId(FunctionIdConstant.R0107);
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		setValue.setComboFlg(NO_UPDATE_FLG);
		LogUtils.infoByMsg("申請書類履歴テーブル登録:申請書類管理番号:" + applInfo.get("applNo") + ",社員番号:" + dto.getShainNo());
		
		// 登録
		int registCount = 0;
		registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
		LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");

		// ステータスを設定
		dto.setApplStatus(applInfo.get("newStatus"));
		
		if(registCount<=0){
			LogUtils.infoByMsg("申請書類履歴テーブル登録異常:申請書類管理番号:" + applInfo.get("applNo") + ",社員番号:" + dto.getShainNo());
			//登録件数0以下はエラー
			return false;
		}
		return true;
	}
	
	/**
	 * モバイルルーター借用希望申請書テーブルの更新値を設定
	 * 
	 * @param dto
	 * @param setValue
	 * @param applInfo
	 * @return
	 */
	private Skf2100TMobileRouterKiboShinsei setMobileRouterKiboShinsei(Skf2100Sc001CommonDto dto,
			Skf2100TMobileRouterKiboShinsei setValue, Map<String, String> applInfo) {

		// 会社コード
		setValue.setCompanyCd(CodeConstant.C001);// 会社コード
		// 新規の場合セット
		if (CodeConstant.STRING_ZERO.equals(applInfo.get("updateFlg"))) {
			// 申請書番号の設定
			setValue.setApplNo(applInfo.get("applNo"));
			LogUtils.debugByMsg("モバイルルーター借用希望申請テーブルの更新値 申請書番号 ：" + applInfo.get("applNo"));

		} else {
			// 申請書番号の設定
			setValue.setApplNo(dto.getApplNo());
			LogUtils.debugByMsg("モバイルルーター借用希望テーブルの更新値 申請書番号 ：" + dto.getApplNo());
		}
		
		// 申請日時
		setValue.setApplDate(DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT));

		// 社員番号
		setValue.setShainNo(dto.getShainNo());
		// 氏名
		setValue.setName(dto.getName());
		// 機関
		setValue.setAgency(dto.getAgency());
		// 部等
		setValue.setAffiliation1(dto.getAffiliation1());
		// 室・課等
		setValue.setAffiliation2(dto.getAffiliation2());
		// 電話番号
		setValue.setTel(dto.getTel());
		// メールアドレス
		setValue.setMailaddress(dto.getMailAddress());
		// 利用開始希望日
		setValue.setUseStartHopeDay(dto.getUseStartHopeDay());
		
		// 原籍会社コード
		if(NfwStringUtils.isNotEmpty(dto.getOriginalCompanyCd())){
			setValue.setOriginalCompanyCd(dto.getOriginalCompanyCd());
		}
		// 給与支給会社コード
		if(NfwStringUtils.isNotEmpty(dto.getPayCompanyCd())){
			setValue.setPayCompanyCd(dto.getPayCompanyCd());
		}		
		// モバイルルーター通しNo
		if(dto.getMobileRouterNo() != null){
			setValue.setMobileRouterNo(dto.getMobileRouterNo());
		}
		// ICCID
		if(NfwStringUtils.isNotEmpty(dto.getIccid())){
			setValue.setIccid(dto.getIccid());
		}
		// IMEI
		if(NfwStringUtils.isNotEmpty(dto.getImei())){
			setValue.setImei(dto.getImei());
		}
		// 発送日
		if(NfwStringUtils.isNotEmpty(dto.getShippingDate())){
			setValue.setShippingDate(dto.getShippingDate());
		}
		// 受領日
		if(NfwStringUtils.isNotEmpty(dto.getReceivedDate())){
			setValue.setReceivedDate(dto.getReceivedDate());
		}
		// モバイルルーター本体受領チェックフラグ
		if(NfwStringUtils.isNotEmpty(dto.getBodyReceiptCheckFlag())){
			setValue.setBodyReceiptCheckFlag(dto.getBodyReceiptCheckFlag());
		}
		// モバイルルーター手引き受領チェックフラグ
		if(NfwStringUtils.isNotEmpty(dto.getHandbookReceiptCheckFlag())){
			setValue.setHandbookReceiptCheckFlag(dto.getHandbookReceiptCheckFlag());
		}
		// 返却資材受領チェックフラグ
		if(NfwStringUtils.isNotEmpty(dto.getMaterialsReceivedCheckFlag())){
			setValue.setMaterialsReceivedCheckFlag(dto.getMaterialsReceivedCheckFlag());
		}
		
		setValue.setUpdateUserId(dto.getUserId());
		setValue.setUpdateProgramId(FunctionIdConstant.SKF2100_SC001);
		
		
		return setValue;
	}
	
	
	/**
	 * モバイルルーター借用希望申請書テーブルの登録処理
	 * 
	 * @param dto
	 * @param setValue
	 */
	private boolean insertRouterKiboShinsei(Skf2100Sc001CommonDto dto, Skf2100TMobileRouterKiboShinsei setValue) {
		
		// 登録処理
		setValue.setInsertUserId(dto.getUserId());
		setValue.setInsertProgramId(FunctionIdConstant.SKF2100_SC001);
		LogUtils.infoByMsg("モバイルルーター借用希望申請書登録:申請書類管理番号:" + setValue.getApplNo()+ ",社員番号:" + setValue.getShainNo());
		
		int registCount = 0;
		registCount = skf2100TMobileRouterKiboShinseiRepository.insertSelective(setValue);
		LogUtils.debugByMsg("モバイルルーター借用希望申請書テーブル登録件数：" + registCount + "件");

		if(registCount<=0){
			LogUtils.infoByMsg("モバイルルーター借用希望申請書テーブル登録異常 件数：" + registCount + "件");
			return false;
		}
		
		return true;
	}
	
	/**
	 * モバイルルーター借用希望申請書テーブルの更新処理
	 *
	 * @param setValue
	 * @param dto
	 * @param applInfo
	 * @return
	 */
	private boolean updateRouterKiboShinsei(Skf2100Sc001CommonDto dto,
			Map<String, String> applInfo) {

		Skf2100TMobileRouterKiboShinsei setValue = new Skf2100TMobileRouterKiboShinsei();
		// モバイルルーター借用希望申請書情報取得
		Skf2100TMobileRouterKiboShinsei routerKiboInfo = new Skf2100TMobileRouterKiboShinsei();
		routerKiboInfo = skfShinseiUtils.getSksRouterKiboShinseiInfo(CodeConstant.C001, dto.getApplNo());

		LogUtils.infoByMsg("モバイルルーター借用希望申請書更新:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
		
		// 楽観的排他チェック（モバイルルーター借用希望申請書テーブル）
		if (!CheckUtils.isEqual(routerKiboInfo.getUpdateDate(), dto.getLastUpdateDate(ROUTER_KIBO_KEY_LAST_UPDATE_DATE))) {
			LogUtils.infoByMsg("モバイルルーター借用希望申請書更新排他チェックエラー:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			
			// エラー時は戻り値をfalseに設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1134,
					"skf2100_t_mobile_router_kibo_shinsei");
			return false;
		}

		// 更新値の設定
		setValue = setMobileRouterKiboShinsei(dto, setValue, applInfo);
		// 更新処理
		int updateCnt = skf2100TMobileRouterKiboShinseiRepository.updateByPrimaryKeySelective(setValue);
		if (updateCnt == 0) {
			LogUtils.infoByMsg("モバイルルーター借用希望申請書テーブル更新異常 件数：" + updateCnt + "件");
			// 更新できなかった場合はは戻り値をfalseに設定
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		return true;
	}
	
	/**
	 * 申請書履歴テーブルの更新処理
	 *
	 * @param saveDto
	 * @param applInfo
	 * @return
	 */
	private boolean updateApplHistoryAgreeStatusIchiji(Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {

		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		// 排他制御比較用更新日取得
		Skf2010TApplHistory resultUpdateDate = selectByApplHistoryPrimaryKey(setValue, dto);
		if (resultUpdateDate == null) {
			LogUtils.infoByMsg("申請書履歴更新日取得エラー（NULL):申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// 楽観的排他チェック（申請情報履歴）
		if (!CheckUtils.isEqual(resultUpdateDate.getUpdateDate(),
				dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE))) {
			// 排他チェックエラー
			LogUtils.infoByMsg("申請書履歴排他チェックエラー:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1134,"skf2010_t_appl_history");
			return false;
		}

		// 更新値の設定
		setValue = setUpdateApplHistoryAgreeStatusIchiji(setValue, dto, applInfo);
		LogUtils.infoByMsg("申請書履歴更新:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
		// 更新
		int resultCnt = skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setValue);
		if (resultCnt == 0) {
			LogUtils.infoByMsg("申請書履歴更新異常:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}
		return true;

	}
	
	/**
	 * 申請書履歴テーブルの更新処理
	 * 申請日更新しない
	 *
	 * @param saveDto
	 * @param applInfo
	 * @return
	 */
	private boolean updateApplHistoryAgreeStatus(Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {

		Skf2010TApplHistory setValue = new Skf2010TApplHistory();

		// 排他制御比較用更新日取得
		Skf2010TApplHistory resultUpdateDate = selectByApplHistoryPrimaryKey(setValue, dto);
		if (resultUpdateDate == null) {
			LogUtils.infoByMsg("申請書履歴更新日取得エラー（NULL):申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}

		// 楽観的排他チェック（申請情報履歴）
		if (!CheckUtils.isEqual(resultUpdateDate.getUpdateDate(),
				dto.getLastUpdateDate(APPL_HISTORY_KEY_LAST_UPDATE_DATE))) {
			// 排他チェックエラー
			LogUtils.infoByMsg("申請書履歴排他チェックエラー:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1134);
			return false;
		}

		// 更新値の設定
		setValue = setUpdateApplHistoryAgreeStatus(setValue, dto, applInfo);
		// 更新
		int resultCnt = skf2020Sc002UpdateApplHistoryAgreeStatusExpRepository.updateApplHistoryAgreeStatus(setValue);
		if (resultCnt == 0) {
			LogUtils.infoByMsg("申請書履歴更新異常:申請書類管理番号:" + dto.getApplNo() + ",社員番号:" + dto.getShainNo());
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1075);
			return false;
		}
		return true;

	}
	
	/**
	 * 申請書類履歴テーブル情報取得
	 * 
	 * @param keyValue 申請書類履歴テーブル
	 * @param dto Skf2100Sc001CommonDto
	 * @return
	 */
	private Skf2010TApplHistory selectByApplHistoryPrimaryKey(Skf2010TApplHistory keyValue, Skf2100Sc001CommonDto dto) {

		// キー項目をセット
		keyValue.setCompanyCd(CodeConstant.C001);
		keyValue.setShainNo(dto.getShainNo());
		keyValue.setApplDate(dto.getApplDate());
		keyValue.setApplNo(dto.getApplNo());
		keyValue.setApplId(FunctionIdConstant.R0107);

		Skf2010TApplHistory resultInfo = new Skf2010TApplHistory();
		resultInfo = skf2010TApplHistoryRepository.selectByPrimaryKey(keyValue);

		return resultInfo;
	}

	/**
	 * 申請書類履歴テーブル更新用値のセット
	 * 
	 * @param setValue 申請書類履歴テーブル
	 * @param dto Skf2100Sc001CommonDto
	 * @param applInfo 申請書情報Map
	 * @return setValue 申請書類履歴テーブル
	 */
	private Skf2010TApplHistory setUpdateApplHistoryAgreeStatusIchiji(Skf2010TApplHistory setValue,
			Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {
		
		// キー項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(dto.getApplNo());

		// 更新項目をセット
		setValue.setApplStatus(CodeConstant.STATUS_ICHIJIHOZON);
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		setValue.setApplDate(DateUtils.getSysDate());
		setValue.setUpdateUserId(dto.getUserId());
		setValue.setUpdateProgramId(FunctionIdConstant.SKF2100_SC001);
		return setValue;
	}
	
	/**
	 * 申請書類履歴テーブル更新用値のセット
	 * 
	 * @param setValue 申請書類履歴テーブル
	 * @param dto Skf2100Sc001CommonDto
	 * @param applInfo 申請書情報Map
	 * @return setValue 申請書類履歴テーブル
	 */
	private Skf2010TApplHistory setUpdateApplHistoryAgreeStatus(Skf2010TApplHistory setValue,
			Skf2100Sc001CommonDto dto, Map<String, String> applInfo) {
		
		// キー項目をセット
		setValue.setCompanyCd(CodeConstant.C001);
		setValue.setApplNo(dto.getApplNo());

		// 更新項目をセット
		setValue.setApplStatus(applInfo.get("newStatus"));
		setValue.setApplTacFlg(applInfo.get("applTacFlg"));
		setValue.setApplDate(null);//更新しない
		setValue.setUpdateUserId(dto.getUserId());
		setValue.setUpdateProgramId(FunctionIdConstant.SKF2100_SC001);
		return setValue;
	}
	
	/**
	 * 画面表示項目再設定
	 * 
	 * @param dto
	 */
	protected void setDispInfo(Skf2100Sc001CommonDto dto) {
		LogUtils.debugByMsg("保存");
		// 電話番号
		LogUtils.debugByMsg("電話番号：" + dto.getTel());
		dto.setTel(dto.getTel());

		// メールアドレス
		LogUtils.debugByMsg("メールアドレス：" + dto.getMailAddress());
		dto.setMailAddress(dto.getMailAddress());
		
		// 利用開始希望日
		LogUtils.debugByMsg("利用開始希望日：" + dto.getUseStartHopeDay());
		dto.setUseStartHopeDay(dto.getUseStartHopeDay());
		dto.setUseStartHopeDaylbl(skfDateFormatUtils.dateFormatFromString(dto.getUseStartHopeDay(), "yyyy/MM/dd"));

		// 受領日
		LogUtils.debugByMsg("受領日：" + dto.getReceivedDate());
		dto.setReceivedDate(dto.getReceivedDate());
		dto.setReceivedDatelbl(skfDateFormatUtils.dateFormatFromString(dto.getReceivedDate(), "yyyy/MM/dd"));
		
		// モバイルルーター本体受領チェックフラグ
		LogUtils.debugByMsg("モバイルルーター本体受領チェックフラグ：" + dto.getBodyReceiptCheckFlag());
		dto.setBodyReceiptCheckFlag(dto.getBodyReceiptCheckFlag());
		
		// モバイルルーター手引き受領チェックフラグ
		LogUtils.debugByMsg("モバイルルーター手引き受領チェックフラグ：" + dto.getHandbookReceiptCheckFlag());
		dto.setHandbookReceiptCheckFlag(dto.getHandbookReceiptCheckFlag());

		// 返却資材受領チェックフラグ
		LogUtils.debugByMsg("返却資材受領チェックフラグ：" + dto.getMaterialsReceivedCheckFlag());
		dto.setMaterialsReceivedCheckFlag(dto.getMaterialsReceivedCheckFlag());
		
	}
	
	/**
	 * バイト数カット処理
	 * 
	 * @param dto
	 * @throws UnsupportedEncodingException
	 */
	protected void cutByte(Skf2100Sc001CommonDto dto) throws UnsupportedEncodingException {

		String Msg = "バイト数カット処理：　";
		// 電話番号
		dto.setTel(NfwStringUtils.rightTrimbyByte(dto.getTel(), 13));
		LogUtils.debugByMsg(Msg + "電話番号" + dto.getTel());
		// メールアドレス
		dto.setMailAddress(NfwStringUtils.rightTrimbyByte(dto.getMailAddress(), 255));
		LogUtils.debugByMsg(Msg + "メールアドレス" + dto.getMailAddress());
		// 利用開始希望日
		dto.setUseStartHopeDay(NfwStringUtils.rightTrimbyByte(dto.getUseStartHopeDay(), 10));
		LogUtils.debugByMsg(Msg + "利用開始希望日" + dto.getUseStartHopeDay());
		// 受領日
		dto.setReceivedDate(NfwStringUtils.rightTrimbyByte(dto.getReceivedDate(), 10));
		LogUtils.debugByMsg(Msg + "受領日" + dto.getReceivedDate());
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
	 * チェックボックス状態設定
	 * 
	 * @param dto
	 * */
	public void setCheckFlag(Skf2100Sc001CommonDto dto){
		if(sTrue.equals(dto.getHdnBodyReceiptChecked())){
			dto.setBodyReceiptCheckFlag("1");
		}else{
			dto.setBodyReceiptCheckFlag("");
			dto.setHdnBodyReceiptChecked(null);
		}
		if(sTrue.equals(dto.getHdnHandbookReceiptChecked())){
			dto.setHandbookReceiptCheckFlag("1");
		}else{
			dto.setHandbookReceiptCheckFlag("");
			dto.setHdnHandbookReceiptChecked(null);
		}
		if(sTrue.equals(dto.getHdnMaterialsReceivedChecked())){
			dto.setMaterialsReceivedCheckFlag("1");
		}else{
			dto.setMaterialsReceivedCheckFlag("");
			dto.setHdnMaterialsReceivedChecked(null);
		}

	}
}