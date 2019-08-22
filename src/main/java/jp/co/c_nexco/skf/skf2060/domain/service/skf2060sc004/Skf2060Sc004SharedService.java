package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsUpdateApplHistoryAgreeStatusExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2060Sc004.Skf2060Sc004GetKariageListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfShinseiUtils.SkfShinseiUtilsUpdateApplHistoryAgreeStatusExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfMailUtils;


/**
 * Skf2010Sc008 代行ログイン画面 内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004SharedService {

    //Repository
    @Autowired
    private Skf2060Sc004GetKariageListExpRepository skf2060Sc004GetKariageListExpRepository;
    @Autowired
    private Skf2010MApplicationRepository skf2010MApplicationRepository;
    @Autowired
    private SkfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository skfShinseiUtilsGetApplHistoryInfoByApplNoRepository;
    @Autowired
    private SkfShinseiUtilsUpdateApplHistoryAgreeStatusExpRepository skfShinseiUtilsUpdateApplHistoryStatusRepository;
    
    //Utils
    @Autowired
    private SkfMailUtils skfMailUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
    
    /**
     * 借上候補物件状況一覧を取得して、画面のlistTableに表示するための情報を作成する。
     * @param param
     * @return listTableに表示する情報を格納したList
     */
    public List<Map<String, Object>> getListTableData(
            Skf2060Sc004GetKariageListExpParameter param){
        List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();
        
        // 検索を実行
        List<Skf2060Sc004GetKariageListExp> kariageExpList  = this.searchKariageList(param);
        
        // 提示状況汎用コード取得
        Map<String, String> candidateStatusGenCodeMap = new HashMap<String, String>();
        candidateStatusGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);
        
        // 同意しない理由区分汎用コード取得
        Map<String, String> riyuGenCodeMap = new HashMap<String, String>();
        riyuGenCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_NO_AGREE_REASON);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < kariageExpList.size(); i++) {
            Skf2060Sc004GetKariageListExp tmpData = kariageExpList.get(i);
            Map<String, Object> tmpMap = new HashMap<String, Object>();
            
            // 完了チェックボックス非活性制御
            String completeChkDisabled = "";
            // 督促チェックボックス非活性制御
            String reminderChkDisabled = "";
            
            // 完了、督促、再提示ボタン
            if( !StringUtils.isEmpty(tmpData.getCandidateStatus()) ){
                if( tmpData.getCandidateStatus().equals(CodeConstant.STATUS_SENTAKU_ZUMI)
                        || tmpData.getCandidateStatus().equals(CodeConstant.STATUS_SENTAKU_SHINAI)
                ){
                    // ステータスが「選択済」または「選択しない」である場合
                    // 再提示ボタンを表示する
                    
                    tmpMap.put("recandidate", "");
                }
                
                if( tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KAKUNIN_IRAI)
                        || tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KANRYOU) ){
                    // ステータスが「確認依頼」または「完了」である場合
                    // 完了チェックボックスを非活性とする
                    completeChkDisabled = "disabled";
                }
                
                if( !tmpData.getCandidateStatus().equals(CodeConstant.STATUS_KAKUNIN_IRAI) ){
                    // ステータスが「確認依頼」でない場合
                    // 督促チェックボックスを非活性とする
                    reminderChkDisabled = "disabled";
                }
            }
            String applNo = tmpData.getApplNo();
            tmpMap.put("completeChk", "<INPUT type='checkbox' name='completeChkVal' id='completeChkVal" + i + "' value='" + applNo + "'" + completeChkDisabled + ">");
            tmpMap.put("reminderChk", "<INPUT type='checkbox' name='reminderChkVal' id='reminderChkVal" + i + "' value='" + applNo + "'" + reminderChkDisabled + ">");
    
            // 提示状況表示文言を汎用コードから取得
            String candidateStatus = candidateStatusGenCodeMap.get(tmpData.getCandidateStatus());
            tmpMap.put("candidateStatus", HtmlUtils.htmlEscape(candidateStatus));
            // 提示日をyyyy/MM/dd形式で表示
            tmpMap.put("candidateDate", HtmlUtils.htmlEscape(sdf.format(tmpData.getCandidateDate()).toString()));
            tmpMap.put("candidatePersonNo", HtmlUtils.htmlEscape(tmpData.getCandidatePersonNo()));
            tmpMap.put("candidatePersonName", HtmlUtils.htmlEscape(tmpData.getCandidatePersonName()));
            tmpMap.put("shatakuName", HtmlUtils.htmlEscape(tmpData.getShatakuName()));
            tmpMap.put("shatakuAddress", HtmlUtils.htmlEscape(tmpData.getShatakuAddressName()));
            
            // 備考
            if( !StringUtils.isEmpty(tmpData.getRiyu()) ){
                if(tmpData.getRiyu().equals(CodeConstant.RELATION_OTHERS) ){
                    // 選択しない理由が「99:その他」の場合
                    tmpMap.put("biko", HtmlUtils.htmlEscape("その他：" + tmpData.getBiko()));
                }else{
                    // 選択しない理由が「99:その他」以外の場合
                    // 理由区分文言を汎用コードから取得
                    String riyuStr = riyuGenCodeMap.get(tmpData.getRiyu());
                    tmpMap.put("biko", HtmlUtils.htmlEscape(riyuStr));
                }
            }

            tmpMap.put("confirm", "");
            // 隠し項目
            tmpMap.put("applNo", tmpData.getApplNo());
            tmpMap.put("applId", tmpData.getApplId());
            tmpMap.put("applStatusCd", tmpData.getCandidateStatus());
            tableDataList.add(tmpMap);
        }
        
        return tableDataList;
    }
    
    /**
     * 借上候補物件状況一覧情報を取得する。
     * @param param
     * @return 借上候補物件状況一覧Exp
     */
    public List<Skf2060Sc004GetKariageListExp> searchKariageList(
            Skf2060Sc004GetKariageListExpParameter param) {
        List<Skf2060Sc004GetKariageListExp> kariageExpList = new ArrayList<Skf2060Sc004GetKariageListExp>();
        kariageExpList = skf2060Sc004GetKariageListExpRepository.getKariageList(param);

        return kariageExpList;
    }

    /**
     * 引数で指定された申請書類管理番号に対応する申請履歴情報を取得する。
     * @param applNo 申請書類管理番号
     * @param SkfShinseiUtilsGetApplHistoryInfoByApplNoExp
     */
    public SkfShinseiUtilsGetApplHistoryInfoByApplNoExp getApplInfo(String applNo){
        SkfShinseiUtilsGetApplHistoryInfoByApplNoExpParameter param = 
                new SkfShinseiUtilsGetApplHistoryInfoByApplNoExpParameter();
        param.setCompanyCd(CodeConstant.C001);
        param.setApplNo(applNo);
        
        return skfShinseiUtilsGetApplHistoryInfoByApplNoRepository.getApplHistoryInfoByApplNo(param);
    }
    
    /**
     * 申請履歴情報の更新
     * @param inputExp 検索キー情報を保持したExp
     * @param status 更新するステータス
     * @return 更新件数
     */
    public int updateApplStatus(SkfShinseiUtilsGetApplHistoryInfoByApplNoExp inputExp, String status) {
        SkfShinseiUtilsUpdateApplHistoryAgreeStatusExpParameter param 
                = new SkfShinseiUtilsUpdateApplHistoryAgreeStatusExpParameter();
        // キー情報
        param.setCompanyCd(inputExp.getCompanyCd());
        param.setShainNo(inputExp.getShainNo());
        param.setApplDate(inputExp.getApplDate());
        param.setApplNo(inputExp.getApplNo());
        param.setApplId(inputExp.getApplId());

        // ステータス更新(完了とする)
        param.setApplStatus(status);

        // DB更新実行
        return skfShinseiUtilsUpdateApplHistoryStatusRepository.updateApplHistoryAgreeStatus(param);
    }
    /**
     * 確認督促メール送信のメイン処理を行います
     * 
     * @param sendMailKbn
     * @param applInfo
     * @param comment
     * @param annai
     * @param furikomiStartDate
     * @param sendUser
     * @param sendGroundId
     * @throws Exception
     */
//    private void sendReminderMail(String sendMailKbn, Map<String, String> applInfo, String comment, String annai,
//            String furikomiStartDate, String sendUser, String sendGroundId) throws Exception {
//
//        // 申請書類名を取得
//        Skf2010MApplication skf2010MApplication = new Skf2010MApplication();
//        Skf2010MApplicationKey key = new Skf2010MApplicationKey();
//        key.setCompanyCd(CodeConstant.C001);
//        key.setApplId(applInfo.get("applId"));
//        skf2010MApplication = skf2010MApplicationRepository.selectByPrimaryKey(key);
//        if (skf2010MApplication == null) {
//            // 申請書類名が取れない時はエラーログを出力
//            return;
//        }
//        String applName = skf2010MApplication.getApplName();
//
//        // 申請社員名、申請日を取得
//        String applShainName = "";
//        String applDate = "";
//        String applAgencyCd = "";
//
//        // 申請通知メール情報の取得
//        Skf2010Sc005GetSendApplMailInfoExp skfMShainData = new Skf2010Sc005GetSendApplMailInfoExp();
//        List<Skf2010Sc005GetSendApplMailInfoExp> skfMShainList = new ArrayList<Skf2010Sc005GetSendApplMailInfoExp>();
//        Skf2010Sc005GetSendApplMailInfoExpParameter mShainParam = new Skf2010Sc005GetSendApplMailInfoExpParameter();
//        mShainParam.setCompanyCd(CodeConstant.C001);
//        mShainParam.setShainNo(applInfo.get("applShainNo"));
//        mShainParam.setApplNo(applInfo.get("applNo"));
//        skfMShainList = skf2010Sc005GetSendApplMailInfoExpRepository.getSendApplMailInfo(mShainParam);
//        if (skfMShainList == null || skfMShainList.size() <= 0) {
//            // 申請通知メール情報が取れない時はエラーログを出力
//            return;
//        }
//        skfMShainData = skfMShainList.get(0);
//
//        if (!CheckUtils.isEmpty(skfMShainData.getName())) {
//            applShainName = skfMShainData.getName();
//        }
//        if (skfMShainData.getApplDate() != null) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//            applDate = sdf.format(skfMShainData.getApplDate());
//        }
//        if (!CheckUtils.isEmpty(skfMShainData.getAgencyCd())) {
//            applAgencyCd = skfMShainData.getAgencyCd();
//        }
//
//        // メール送信
//        if (!CheckUtils.isEmpty(sendUser)) {
//            String mailAddress = skfMailUtils.getSendMailAddressByShainNo(CodeConstant.C001, sendUser);
//
//            // URLを設定
//            String urlBase = "/skf/Skf2010Sc005/init?SKF2010_SC005&menuflg=1&tokenCheck=0";
//            sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
//                    annai, furikomiStartDate, urlBase, sendUser, mailAddress, null, applName);
//        } else {
//            sendApplTsuchiMailExist(companyCd, sendMailKbn, applInfo.get("applNo"), applDate, applShainName, comment,
//                    annai, furikomiStartDate, null, sendUser, outMailAddress, null, applName);
//
//        }
//
//        return;
//    }
    
    /**
     * 確認督促メールを送信します
     * 
     * @param companyCd
     * @param mailKbn
     * @param applNo
     * @param applDate
     * @param applShainName
     * @param comment
     * @param annai
     * @param furikomiStartDate
     * @param urlBase
     * @param sendUser
     * @param mailAddress
     * @param bccAddress
     * @param applName
     * @throws Exception
     */
    private void sendReminderMail(String companyCd, String mailKbn, String applNo, String applDate,
            String applShainName, String comment, String annai, String furikomiStartDate, String urlBase,
            String sendUser, String mailAddress, String bccAddress, String applName) throws Exception {

        // メール送信テスト
        Map<String, String> replaceMap = new HashMap<String, String>();

        // メール件名
        replaceMap.put("【to】", mailAddress); // 送信先メールアドレス
        replaceMap.put("【appl_no】", applNo); // 申請書類番号
        replaceMap.put("【appl_name】", applName); // 申請書類名

        // メール本文
        replaceMap.put("【appl_user_name】", applShainName); // 申請社員名
        replaceMap.put("【appl_date】", applDate); // 申請日
        replaceMap.put("【comment】", comment); // 承認者からのコメント

        // 短縮URL作成
        if (urlBase != null) {
            Map<String, String> urlMap = new HashMap<String, String>();
            String url = NfwSendMailUtils.createShotcutUrl(urlBase, urlMap, 2);
            replaceMap.put("【url】", url);
        }

        // メール送信
        NfwSendMailUtils.sendMail("SKF_ML32", replaceMap);
        return;
    }

}
