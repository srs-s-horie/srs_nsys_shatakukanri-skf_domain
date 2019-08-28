package jp.co.c_nexco.skf.skf2060.domain.service.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplication;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010MApplicationKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.SkfShinseiUtils.SkfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010MApplicationRepository;
import jp.co.c_nexco.nfw.common.utils.NfwSendMailUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;


/**
 * Skf206010 借上候補登録・確認・一覧画面 共通処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf206010CommonSharedService {

    @Autowired
    private SkfGetInfoUtilsGetShainInfoExpRepository skfGetInfoUtilsGetShainInfoExpRepository;
    @Autowired
    private SkfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository skfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository;
    @Autowired
    private Skf2010MApplicationRepository skf2010MApplicationRepository;
    
    @Autowired
    private SkfCommentUtils skfCommentUtils;
    @Autowired
    private SkfDateFormatUtils skfDateFormatUtils;
    
    /** 借上候補物件提示通知メールテンプレートID */
    private final String REMINDER_MAIL_TEMPLATE_ID = "SKF_ML32";
    /** 借上候補物件確認画面へのURL */
    private final String SKF2060SC002_URL_BASE = "/skf/Skf2060Sc002/init?SKF2060_SC002&menuflg=1&tokenCheck=0";
    
    /**
     * 借上候補物件提示通知メールを送付する。
     * @param applNo 申請書類管理番号
     * @throws Exception 
     */
    public void sendKariageTeijiMail(String applNo) throws Exception{

        // 処理対象の申請情報を取得する
        SkfShinseiUtilsGetApplHistoryInfoByApplNoExp applInfoExp;
        applInfoExp = this.getApplInfo(applNo);
        
        if (applInfoExp == null) {
            // 対応する申請履歴が取得できなかった場合処理を終了
            return;
        }
        
        // 申請書類名を取得する
        String applName = this.getApplName(applInfoExp.getApplId());
        
        // 申請者情報を取得する
        SkfGetInfoUtilsGetShainInfoExp shainInfoExp;
        shainInfoExp = this.getShainInfo(applInfoExp.getShainNo());

        if (shainInfoExp == null) {
            // 対応する社員情報が取得できなかった場合処理を終了
            return;
        }
        
        // 承認者コメントを取得
        String comment = this.getApproveCommentStr(applInfoExp.getApplNo());
        
        // URLを設定
        this.sendKariageTeijiMail(
                applInfoExp.getApplNo(),
                applInfoExp.getApplDate(),
                shainInfoExp.getName(),
                comment,
                this.SKF2060SC002_URL_BASE, 
                shainInfoExp.getMailAddress(),
                applName);
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
        
        return skfShinseiUtilsGetApplHistoryInfoByApplNoExpRepository.getApplHistoryInfoByApplNo(param);
    }

    /**
     * 引数で指定された申請IDに該当する申請の申請書名を取得する
     * @param applId 申請ID
     * @return 申請書名
     */
    private String getApplName(String applId){
        Skf2010MApplication skf2010MApplication = new Skf2010MApplication();
        Skf2010MApplicationKey key = new Skf2010MApplicationKey();
        key.setCompanyCd(CodeConstant.C001);
        key.setApplId(applId);
        skf2010MApplication = skf2010MApplicationRepository.selectByPrimaryKey(key);
        if (skf2010MApplication != null) {
            // 申請書名を返す
            return skf2010MApplication.getApplName();
        }else{
            // 申請書名が取れない時はエラーログを出力
            return "";
        }
    }
    
    /**
     * 引数で指定された社員番号に該当する社員情報を取得する
     * @param shainNo 社員番号
     * @return SkfGetInfoUtilsGetShainInfoExp 社員情報Exp
     */
    public SkfGetInfoUtilsGetShainInfoExp getShainInfo(String shainNo){
        SkfGetInfoUtilsGetShainInfoExpParameter param =
                new SkfGetInfoUtilsGetShainInfoExpParameter();
        param.setCompanyCd(CodeConstant.C001);
        param.setShainNo(shainNo);
        return skfGetInfoUtilsGetShainInfoExpRepository.getShainInfo(param);
    }

    /**
     * 引数で指定された申請書類管理番号、ステータスに該当する承認者コメント情報を取得する
     * @param applNo 申請書類管理番号
     * @return 承認者コメント文字列
     */
    private String getApproveCommentStr(String applNo){
        // 承認者コメント情報を取得する
        List<SkfCommentUtilsGetCommentInfoExp> commentList;
        commentList = skfCommentUtils.getCommentInfo(
                CodeConstant.C001,
                applNo,
                CodeConstant.STATUS_SHONIN_ZUMI
        );
        String comment = "";
        if (commentList != null && commentList.size() > 0) {
            comment = commentList.get(0).getCommentNote();
        }
        return comment;
    }
    
    /**
     * 確認督促メールを送信する
     * 
     * @param applNo 申請書類番号
     * @param applDate 申請日
     * @param applShainName 申請社員名
     * @param comment 承認者のコメント
     * @param urlBase 借上社宅候補確認画面へのURL
     * @param mailAddress 送信先メールアドレス
     * @param applName 申請書類名
     * @throws Exception
     */
    private void sendKariageTeijiMail(String applNo, Date applDate,
            String applShainName, String comment, String urlBase,
            String mailAddress, String applName) throws Exception {

        // メール送信テスト
        Map<String, String> replaceMap = new HashMap<String, String>();

        // メール件名
        replaceMap.put("【to】", StringUtils.defaultString(mailAddress)); // 送信先メールアドレス
        replaceMap.put("【appl_no】", StringUtils.defaultString(applNo)); // 申請書類番号
        replaceMap.put("【appl_name】", StringUtils.defaultString(applName)); // 申請書類名

        // メール本文
        replaceMap.put("【appl_user_name】", StringUtils.defaultString(applShainName)); // 申請社員名
        replaceMap.put("【comment】", StringUtils.defaultString(comment)); // コメント
        
        // 申請日のフォーマット(yyyy年MM月dd日)
        String applDateStr = skfDateFormatUtils.dateFormatFromDate(applDate, SkfCommonConstant.YMD_STYLE_YYYYMMDD_JP_STR);
        replaceMap.put("【appl_date】", StringUtils.defaultString(applDateStr)); // 申請日

        // 短縮URL作成
        if (urlBase != null) {
            Map<String, String> urlMap = new HashMap<String, String>();
            String url = NfwSendMailUtils.createShotcutUrl(urlBase, urlMap, 2);
            replaceMap.put("【url】", StringUtils.defaultString(url));
        }

        // メール送信
        NfwSendMailUtils.sendMail(this.REMINDER_MAIL_TEMPLATE_ID, replaceMap);
        return;
    }
    


}
