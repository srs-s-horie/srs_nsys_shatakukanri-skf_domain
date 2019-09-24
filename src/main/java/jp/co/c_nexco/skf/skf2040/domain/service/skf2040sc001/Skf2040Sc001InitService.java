/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetShatakuBihinInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;
import jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002.Skf2020Sc002SharedService;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001InitDto;
import jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002.Skf2040Sc002SharedService;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面のInit処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001InitService extends BaseServiceAbstract<Skf2040Sc001InitDto> {
	
    @Autowired
    private SkfOperationLogUtils skfOperationLogUtils;
    @Autowired
    private SkfOperationGuideUtils skfOperationGuideUtils;
    @Autowired
    private SkfCommentUtils skfCommentUtils;
    @Autowired
    private Skf2040Sc001SharedService skf2040Sc001SharedService;
    @Autowired
    private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
    
    @Autowired
    private Skf2040Sc001GetShatakuBihinInfoExpRepository skf2040Sc001GetShatakuBihinInfoExpRepository;
	/**
	 * サービス処理を行う。　
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001InitDto index(Skf2040Sc001InitDto initDto) throws Exception {
		initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC001_TITLE);
 		
		// 操作ログを出力する
        skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
        
        // 初期表示情報を取得
        this.setDispInfo(initDto);
        
		// 操作ガイド取得
        LogUtils.debugByMsg("操作ガイド" + initDto.getPageId());
        initDto.setOperationGuide(skfOperationGuideUtils.getOperationGuide(initDto.getPageId()));

        // 画面ID保持
        initDto.setPrePageId(FunctionIdConstant.SKF2060_SC004);
		return initDto;
	}
	
	/**
	 * 代行ログインを含めたログインユーザ情報を取得する。
	 * <pre>
	 * セッションを参照し、代行ログイン情報が見つかれば代行対象ユーザの情報をDTOにセットする。
	 * 代行ログイン情報が見つからなかった場合、ログインユーザの情報をDTOにセットする。
	 * </pre>
	 * @param initDto 退居（自動車の保管場所返還）届画面のInitDTO
	 */
    public void setLoginUserInfo(Skf2040Sc001InitDto initDto){
        Map<String, String> loginUserInfoMap = 
                skfLoginUserInfoUtils.getAlterLoginUserInfoMap(menuScopeSessionBean);
        if (loginUserInfoMap != null) {
            // 代行ログイン情報が取得できた場合
            // DTOに代行対象ユーザ情報をセット
            initDto.setName(loginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_SHAIN_NAME));
            initDto.setAgencyName(loginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AGENCY_NAME));
            initDto.setAffiliation1Name(loginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AFFILIATION1_NAME));
            initDto.setAffiliation2Name(loginUserInfoMap.get(CodeConstant.ALTER_LOGIN_USER_AFFILIATION2_NAME));
            
        } else {
            // 代行ログイン情報が取得できなかった場合はログインユーザ情報を取得
            loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();

            // DTOにユーザ情報をセット
            initDto.setName(loginUserInfoMap.get("userName"));
            initDto.setAgencyName(loginUserInfoMap.get("agencyName"));
            initDto.setAffiliation1Name(loginUserInfoMap.get("affiliation1Name"));
            initDto.setAffiliation2Name(loginUserInfoMap.get("affiliation2Name"));
        }
    }

    private void setTaikyoInfo(Skf2040Sc001InitDto initDto){
        // 登録済の退居届情報が存在するかを判定 ※一時保存からの復帰時
        if (initDto.getApplNo() != null) {
            // 申請書管理番号がDtoに存在する場合、登録済の退居届情報をDtoに設定する
            skf2040Sc001SharedService.setExistTaikyoInfo(initDto);
        } else {
            // 登録済退居情報が存在しない場合は初期表示を行う

            // 代行ログインを含めたログインユーザ情報を取得
            this.setLoginUserInfo(initDto);
            // 
            
        }
        
        
        // 要返却備品情報を取得（getShatakuBihinInfo
        //skf2040Sc001GetShatakuBihinInfoExpRepository.getApplHistoryInfo(parameter)
        
        //入力項目の活性制御
        
        
    }
    
	/**
	 * 初期表示時の画面情報をDtoに設定する
     * @param initDto
	 */
	private void setDispInfo(Skf2040Sc001InitDto initDto){
	    // ドロップダウンリスト情報を設定
	    this.setDropdownList(initDto);
	    
	    // 退居届情報を設定
	    this.setTaikyoInfo(initDto);
	    
	    // コメント設定の有無
        this.setCommentBtnDisabled(initDto);
	}
	
	/**
	 * 退居届画面のドロップダウンリスト情報を設定する。
	 * @param initDto
	 */
	private void setDropdownList(Skf2040Sc001InitDto initDto){
	    // 退居(返還)理由ドロップダウンリストをDtoに設定
        initDto.setDdlTaikyoRiyuKbnList(
                skf2040Sc001SharedService.getTaikyoHenkanRiyuList(initDto.getTaikyoRiyuKbn()));
        
        // 現居住社宅名ドロップダウンリストをDtoに設定
        List<Map<String, Object>> nowShatakuNameList = 
                skf2040Sc001SharedService.getNowShatakuNameList(initDto.getShainNo(), initDto.getShatakuNo());
        if (null!=nowShatakuNameList){
            initDto.setDdlNowShatakuNameList(nowShatakuNameList);
        } else {
            // データが取得できなかった場合、エラーメッセージを表示して初期表示処理を終了
        }
        
        // 返却立合希望日（時）ドロップダウンリストをDtoに設定
        initDto.setDdlReturnWitnessRequestDateList(
                skf2040Sc001SharedService.getSessionTimeList(initDto.getSessionTime()));
	}
	
	/**
     * コメントボタンの表示非表示
     * 
     * @param dto
     */
    protected void setCommentBtnDisabled(Skf2040Sc001InitDto initDto) {
        // コメントの設定
        List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
        commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001, initDto.getApplNo(), null);
        if (commentList == null || commentList.size() <= 0) {
            // コメントが無ければ非表示
            initDto.setCommentViewFlag(Skf2020Sc002SharedService.sFalse);
        } else {
            // コメントがあれば表示
            initDto.setCommentViewFlag(Skf2020Sc002SharedService.sTrue);
        }
    }
}
