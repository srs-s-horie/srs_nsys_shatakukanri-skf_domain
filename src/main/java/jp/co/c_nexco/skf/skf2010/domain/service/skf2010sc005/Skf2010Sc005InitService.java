package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005InitDto;

/**
 * Skf2010Sc005 承認一覧初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005InitService extends BaseServiceAbstract<Skf2010Sc005InitDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	@Override
	public BaseDto index(Skf2010Sc005InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF2010_SC005);

		// 初期化処理
		init(initDto);

		// 所属機関の初期値セット
		initDto.setShozokuKikan(CodeConstant.SHOZOKU_SHINSEI);

		// ドロップダウン作成
		setDropDown(initDto);

		// チェックボックスの初期値セット
		List<String> defaultApplStatusList = getDefaultApplStatusValue();
		initDto.setApplStatus(defaultApplStatusList.toArray(new String[defaultApplStatusList.size()]));

		// 検索処理（リストテーブル作成）
		initDto.setLtResultList(searchApplList(initDto, defaultApplStatusList));

		return initDto;
	}

	/**
	 * ドロップダウンを作成します
	 * 
	 * @param dto
	 */
	private void setDropDown(Skf2010Sc005InitDto dto) {
		// ドロップダウン作成
		String agencyCd = dto.getAgency();
		String affiliation1Cd = dto.getAffiliation1();
		String affiliation2Cd = dto.getAffiliation2();
		String shozoku = dto.getShozokuKikan();
		skf2010Sc005SharedService.setDropDown(dto, shozoku, agencyCd, affiliation1Cd, affiliation2Cd);

		return;
	}

	/**
	 * 初期化処理
	 * 
	 * @param dto
	 */
	private void init(Skf2010Sc005InitDto dto) {
		// タイトル設定
		dto.setPageTitleKey(MessageIdConstant.SKF2010_SC005_TITLE);

		// 操作ガイド取得
		dto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(dto.getPageId()));

		// 社員番号初期化
		dto.setShainNo(null);

		// セッション情報初期化
		menuScopeSessionBean.remove(SessionCacheKeyConstant.SKF2010SC005_SEARCH_RESULT_SESSION_KEY);

		// 画面ID保持
		dto.setPrePageId(FunctionIdConstant.SKF2010_SC005);

		// 戻るボタンで遷移する画面を設定
		String backUrl = "skf/" + FunctionIdConstant.SKF1010_SC001 + "/init";
		dto.setBackUrl(backUrl);
	}

	/**
	 * 申請情報一覧を取得します
	 * 
	 * @param dto
	 * @param applStatus
	 * @return
	 */
	private List<Map<String, Object>> searchApplList(Skf2010Sc005InitDto dto, List<String> applStatus) {
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		// 社宅連携用セッション情報をセット
		skf2010Sc005SharedService.setMenuScopeSessionBean(menuScopeSessionBean);

		// 承認一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		param = skf2010Sc005SharedService.setParam(dto);
		tApplHistoryData = skf2010Sc005SharedService.searchApplList(param);
		// 検索結果をセッションに保存
		menuScopeSessionBean.put(SessionCacheKeyConstant.SKF2010SC005_SEARCH_RESULT_SESSION_KEY, tApplHistoryData);

		// グリッド表示（リストテーブル）作成
		rtnList = skf2010Sc005SharedService.createListTable(tApplHistoryData);

		return rtnList;
	}

	/**
	 * チェックボックスの初期値を設定します
	 * 
	 * @return
	 */
	private List<String> getDefaultApplStatusValue() {
		List<String> rtnApplStatus = new ArrayList<String>();
		rtnApplStatus.add(CodeConstant.STATUS_SHINSACHU); // 10：審査中
		rtnApplStatus.add(CodeConstant.STATUS_SHONIN1); // 31：承認中
		rtnApplStatus.add(CodeConstant.STATUS_SHINSEICHU); // 01：申請中
		rtnApplStatus.add(CodeConstant.STATUS_DOI_SHINAI); // 21：同意しない
		rtnApplStatus.add(CodeConstant.STATUS_DOI_ZUMI); // 22：同意済み
		rtnApplStatus.add(CodeConstant.STATUS_SHOZOKUCHO_KAKUNINZUMI); // 05：所属長確認済み
		rtnApplStatus.add(CodeConstant.STATUS_TORIKOMI_KANRYO); // 15：取込完了
		rtnApplStatus.add(CodeConstant.STATUS_HANNYU_ZUMI); // 24：搬入済み
		rtnApplStatus.add(CodeConstant.STATUS_HANSYUTSU_ZUMI); // 26：搬出済み

		return rtnApplStatus;
	}

}
