/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Bt002.Skf1010MShainExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Bt002.Skf1010MShainExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc001.Skf2060Sc001GetApplHistoryExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Bt002.Skf1010MShainExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2060.domain.dto.skf2060sc001.Skf2060Sc001InitDto;
import jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc004.Skf3090Sc004SharedService;

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
	private MenuScopeSessionBean sessionBean;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	private String companyCd = CodeConstant.C001;
	

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
	public Skf2060Sc001InitDto index(Skf2060Sc001InitDto initDto) throws Exception {
		
		initDto.setPageTitleKey(MessageIdConstant.SKF2060_SC001_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", companyCd, initDto.getPageId());

		//TODO セッション情報を取得(セッション名(´・ω・｀)）
		Skf2060Sc001GetApplHistoryExp applInforesultData = (Skf2060Sc001GetApplHistoryExp)sessionBean.get("getApplHistoryResultData");
		//Dtoへ直接から貰ってくる場合
		
		//セッション情報が取得できた場合　もしくは　initDto.getApplNoが空っぽじゃない場合(if(initDto.getApplNo() != null || CheckUtils.isEmpty(initDto.getApplNo().trim())
		if(applInforesultData != null){
			//申請書類管理番号からコメントを取得
			List<SkfCommentUtilsGetCommentInfoExp> commentList =  skfCommentUtils.getCommentInfo(companyCd, applInforesultData.getApplNo(), null);
			initDto.setCommentViewFlag(false);
			//コメントが存在する場合
			if(commentList != null && commentList.size() > 0){
				//「コメント表示」ボタンを表示
				initDto.setCommentViewFlag(true);
			}
			//セッションの申請書類情報をもとに申請履歴テーブルより申請情報を取得
			Skf2060Sc001GetApplHistoryExp applHistoryData = new Skf2060Sc001GetApplHistoryExp();
			applHistoryData = skf2060Sc001SharedService.getApplHistoryInfo(companyCd, applInforesultData.getShainNo(), applInforesultData.getApplNo());
			//申請履歴テーブルより申請情報を取得出来た場合
			if(applHistoryData != null){
				//社宅社員マスタから社員情報を取得
				Skf1010MShain shainData = new Skf1010MShain();
				Skf1010MShainKey param = new Skf1010MShainKey();
				param.setCompanyCd(companyCd);
				param.setShainNo(applInforesultData.getShainNo());
				shainData = skf1010MShainRepository.selectByPrimaryKey(param);
				//社員情報を取得できた場合
				if(shainData != null){
					//借上提示先情報の「提示対象者名」に社員情報の社員名を、「提示状況」に申請情報のステータスを、「提示日」に申請情報の申請日時を設定
					initDto.setPresentedName(shainData.getName());
					initDto.setPresentedStatus(applHistoryData.getApplStatus());
					initDto.setPresentedDate(skfDateFormatUtils.dateFormatFromDate(applHistoryData.getApplDate(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
				}
			}
		}
		
		//隠し要素として現在日時を設定
		Date updateDate = new Date();
		String updateDateFormat = skfDateFormatUtils.dateFormatFromDate(updateDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		initDto.setUpdateDate(updateDateFormat);
		
		
		// リストデータ取得用
		List<Map<String, Object>> dataParamList = new ArrayList<Map<String, Object>>();
		boolean itiranFlg = true;
		dataParamList = skf2060Sc001SharedService.getDataParamList(itiranFlg);
		initDto.setListTableData(dataParamList);
		
		return initDto;
	}
	
}
