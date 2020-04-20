/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3030.domain.service.skf3030sc002;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3022Sc006.Skf3022Sc006GetIdmPreUserMasterInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3030.domain.dto.skf3030sc002.Skf3030Sc002ShainSupportCallBackDto;


/**
 * Skf3030Sc002ShainSupportCallBackService 入退居情報登録画面:社員入力支援コールバック時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3030Sc002ShainSupportCallBackService extends SkfServiceAbstract<Skf3030Sc002ShainSupportCallBackDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3030Sc002SharedService skf3030Sc002SharedService;
	@Autowired
	private Skf3022Sc006GetIdmPreUserMasterInfoExpRepository skf3022Sc006GetIdmPreUserMasterInfoExpRepository;

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
	public Skf3030Sc002ShainSupportCallBackDto index(Skf3030Sc002ShainSupportCallBackDto initDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("社員入力支援後処理");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("社員入力支援後処理", CodeConstant.C001, FunctionIdConstant.SKF3030_SC002);
		// ドロップダウンリスト
		List<Map<String, Object>> sc006SogoRyojokyoSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006TaiyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KariukeKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SogoHanteiKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006HaizokuKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006OldKaisyaNameSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyuyoKaisyaSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyojyusyaKbnSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006YakuinSanteiSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KyoekihiPayMonthSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeInSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006KibouTimeOutSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinShatakuSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc006SokinKyoekihiSelectList = new ArrayList<Map<String, Object>>();
		//可変ラベル値
		skf3030Sc002SharedService.setVariableLabel(skf3030Sc002SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()), initDto);
				
		//備品一覧再取得フラグ(PageLoad)
		initDto.setBihinItiranFlg(0);
		
		//セッションの社員情報を設定されている場合
		if(!SkfCheckUtils.isNullOrEmpty(initDto.getHdnShainNo())){
			String shainNoChangeFlag = skf3030Sc002SharedService.getShainNoChangeFlag(initDto.getHdnShainNo());
			if("1".equals(shainNoChangeFlag)){
				initDto.setSc006ShainNo(initDto.getHdnShainNo() + CodeConstant.ASTERISK);
			}else{
				initDto.setSc006ShainNo(initDto.getHdnShainNo());
			}
			//社員名
			initDto.setSc006ShainName(initDto.getHdnShainName());
			
			// 原籍会社を初期設定
			initDto.setSc006OldKaisyaNameSelect(CodeConstant.DOUBLE_QUOTATION);
			// ①IdM_プレユーザマスタ（従業員区分）を取得
			List<Skf3022Sc006GetIdmPreUserMasterInfoExp> dtbIdmPreUserMasterInfo = new ArrayList<Skf3022Sc006GetIdmPreUserMasterInfoExp>();
			Skf3022Sc006GetIdmPreUserMasterInfoExpParameter param = new Skf3022Sc006GetIdmPreUserMasterInfoExpParameter();
			param.setPumHrNameCode(initDto.getHdnShainNo());
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
			
			//生年月日を設定する
			String birthDay = skf3030Sc002SharedService.getBirthDay(initDto.getHdnShainNo());
			initDto.setHdnBirthday(birthDay);
		}

		
		// ドロップダウンリスト作成
		skf3030Sc002SharedService.setDdlControlValues(
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
		
		//LoadCompleteイベントで備品状態の再読み込みを行わない
		initDto.setBihinItiranReloadFlg(true);
		
		//画面項目制御
		skf3030Sc002SharedService.setControlStatus(initDto);
		
		// 画面ステータス設定
		skf3030Sc002SharedService.setBihinListPageLoadComplete(initDto);

		return initDto;
	}
	
	
}

