/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2100.domain.service.skf2100sc006;

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
import jp.co.c_nexco.skf.skf2100.domain.dto.skf2100sc006.Skf2100Sc006ShainSupportCallBackDto;


/**
 * Skf3030Sc002ShainSupportCallBackService 入退居情報登録画面:社員入力支援コールバック時処理クラス。　 
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf2100Sc006ShainSupportCallBackService extends SkfServiceAbstract<Skf2100Sc006ShainSupportCallBackDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2100Sc006SharedService skf2100Sc006SharedService;
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
	public Skf2100Sc006ShainSupportCallBackDto index(Skf2100Sc006ShainSupportCallBackDto initDto) throws Exception {

		// デバッグログ
		LogUtils.debugByMsg("社員入力支援後処理");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("社員入力支援後処理", CodeConstant.C001, FunctionIdConstant.SKF2100_SC006);
		// ドロップダウンリスト
		List<Map<String, Object>> originalCompanyList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> payCompanyList = new ArrayList<Map<String, Object>>();

		//可変ラベル値
		skf2100Sc006SharedService.setVariableLabel(skf2100Sc006SharedService.jsonArrayToArrayList(initDto.getJsonLabelList()), initDto);
				
		
		//セッションの社員情報を設定されている場合
		if(!SkfCheckUtils.isNullOrEmpty(initDto.getHdnShainNo())){
			String shainNoChangeFlag = skf2100Sc006SharedService.getShainNoChangeFlag(initDto.getHdnShainNo());
			if("1".equals(shainNoChangeFlag)){
				initDto.setShainNo(initDto.getHdnShainNo() + CodeConstant.ASTERISK);
			}else{
				initDto.setShainNo(initDto.getHdnShainNo());
			}
			//社員名
			initDto.setShainName(initDto.getHdnShainName());
			
			// 原籍会社を初期設定
			initDto.setOriginalCompanySelect(CodeConstant.DOUBLE_QUOTATION);
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
					initDto.setOriginalCompanySelect(CodeConstant.C001);
				}
			}
			
		}
		
		// ドロップダウンリストを設定
		skf2100Sc006SharedService.setDdlControlValues(
				initDto.getOriginalCompanySelect(), originalCompanyList,
				CodeConstant.DOUBLE_QUOTATION, payCompanyList);
		
		//画面項目制御
		skf2100Sc006SharedService.setControlStatus(initDto);
		
		// ドロップダウンリスト設定
		initDto.setOriginalCompanyList(originalCompanyList);
		initDto.setPayCompanyList(payCompanyList);

		return initDto;
	}
	
	
}

