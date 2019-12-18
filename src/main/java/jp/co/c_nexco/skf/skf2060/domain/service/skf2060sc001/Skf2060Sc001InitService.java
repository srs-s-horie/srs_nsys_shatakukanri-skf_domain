/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InitDto;

/**
 * TestPrjTop画面のInitサービス処理クラス。　 
 * 
 */
@Service
public class Skf2060Sc001InitService extends BaseServiceAbstract<Skf2060Sc001InitDto> {
	
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf2060Sc001SharedService skf2060Sc001SharedService;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
	
	private String companyCd = CodeConstant.C001;
	

	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2060Sc001InitDto index(Skf2060Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		//前画面から送られてくる値 shainNo, applNo, applStatus
		
		//更新日を設定
		Map<String, Date> lastUpdateDateMap = new HashMap<String, Date>();
		
		//申請書類情報が取得できた場合
		if(!(initDto.getShainNo() == null || CheckUtils.isEmpty(initDto.getShainNo().trim())) && !(initDto.getApplNo() == null || CheckUtils.isEmpty(initDto.getApplNo().trim()))){
			// 操作ログを出力
			skfOperationLogUtils.setAccessLog("再提示", companyCd, FunctionIdConstant.SKF2060_SC004);
			
			//申請書類管理番号からコメントを取得
			List<SkfCommentUtilsGetCommentInfoExp> commentList =  skfCommentUtils.getCommentInfo(companyCd, initDto.getApplNo(), null);
			initDto.setCommentViewFlag(false);
			//コメントが存在する場合
			if(commentList != null && commentList.size() > 0){
				//「コメント表示」ボタンを表示
				initDto.setCommentViewFlag(true);
			}
			//セッションの申請書類情報をもとに申請履歴テーブルより申請情報を取得
			Skf2060Sc001GetApplHistoryExp applHistoryData = new Skf2060Sc001GetApplHistoryExp();
			applHistoryData = skf2060Sc001SharedService.getApplHistoryInfo(companyCd, initDto.getShainNo(), initDto.getApplNo());
			//申請履歴テーブルより申請情報を取得出来た場合
			if(applHistoryData != null){
				//申請書類履歴テーブル用更新日
				lastUpdateDateMap.put(initDto.applHistoryLastUpdateDate, applHistoryData.getUpdateDate());
				//社宅社員マスタから社員情報を取得
				Skf1010MShain shainData = new Skf1010MShain();
				Skf1010MShainKey param = new Skf1010MShainKey();
				param.setCompanyCd(companyCd);
				param.setShainNo(initDto.getShainNo());
				shainData = skf1010MShainRepository.selectByPrimaryKey(param);
				//社員情報を取得できた場合
				if(shainData != null){
			        // 提示状況汎用コード取得
			        Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
			        candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
					//借上提示先情報の「提示対象者名」に社員情報の社員名を、「提示状況」に申請情報のステータスを、「提示日」に申請情報の申請日時を設定
					initDto.setPresentedName(shainData.getName());
					initDto.setPresentedStatus(candidateStatusGenCodeMap.get(applHistoryData.getApplStatus()));
					initDto.setPresentedDate(skfDateFormatUtils.dateFormatFromDate(applHistoryData.getApplDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
				}
			}
			//支援ボタンを非活性にする
			initDto.setSupportDisabled("true");
		}else{
			// 操作ログを出力
			skfOperationLogUtils.setAccessLog("新規作成", companyCd, FunctionIdConstant.SKF2060_SC004);
			
			// 項目初期化
			initDto.setPresentedName(CodeConstant.NONE);
			initDto.setPresentedStatus(CodeConstant.NONE);
			initDto.setPresentedDate(CodeConstant.NONE);
			initDto.setAddress(CodeConstant.NONE);
			initDto.setPostalCd(CodeConstant.NONE);
			initDto.setShatakuName(CodeConstant.NONE);
			initDto.setComment(CodeConstant.NONE);
		}
		
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, FunctionIdConstant.SKF2060_SC001);
		
		//隠し要素として現在日時を設定
		Date updateDate = new Date();
		String updateDateFormat = skfDateFormatUtils.dateFormatFromDate(updateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		initDto.setUpdateDate(updateDateFormat);
		
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg ,initDto.getShainNo(), initDto.getApplNo());
		

		//借上候補物件テーブル用更新日
		for(Map<String, Object> dataParam:dataParamList){
			SimpleDateFormat sdf = new SimpleDateFormat(SkfCommonConstant.YMD_STYLE_YYYYMMDDHHMMSS_SSS);
			Date lastUpdateDate = sdf.parse(dataParam.get("lastUpdateDate").toString());
			lastUpdateDateMap.put(initDto.KariageBukkenLastUpdateDate + dataParam.get("candidateNo").toString(), lastUpdateDate);
		}
		
		//更新日設定
		initDto.setLastUpdateDateMap(lastUpdateDateMap);
		
		initDto.setListTableData(dataParamList);
		
		return initDto;
	}
	
}
