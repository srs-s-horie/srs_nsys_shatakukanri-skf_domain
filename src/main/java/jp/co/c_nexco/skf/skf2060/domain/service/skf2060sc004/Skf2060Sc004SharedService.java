package jp.co.c_nexco.skf.skf2060.domain.service.skf2060sc004;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

/**
 * Skf2010Sc008 代行ログイン画面 内部処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2060Sc004SharedService {

    public List<Map<String, Object>> getSampleListTableData(){
        List<Map<String, Object>> tableDataList = new ArrayList<Map<String, Object>>();
        
        for(int i = 0 ; i < 200 ; i ++){
            Map<String, Object> tmpMap = new HashMap<String, Object>();
            tmpMap.put("col1", "<INPUT type='checkbox' name='completeChkVal' id='completeChkVal" + i + "' value='" + i + "'>");
            tmpMap.put("col2", "<INPUT type='checkbox' name='reminderChkVal' id='reminderChkVal" + i + "' value='" + i + "'>");

            tmpMap.put("col3", HtmlUtils.htmlEscape("選択済"));
            tmpMap.put("col4", HtmlUtils.htmlEscape("2019/04/01"));
            tmpMap.put("col5", HtmlUtils.htmlEscape("12345678"));
            tmpMap.put("col6", HtmlUtils.htmlEscape("中日本 0001"));
            tmpMap.put("col7", HtmlUtils.htmlEscape("みずほ台１"));
            tmpMap.put("col8", HtmlUtils.htmlEscape("東京都〇〇市××区■■1234-5"));
            tmpMap.put("col9", HtmlUtils.htmlEscape("備考"));
            
            tmpMap.put("col10", "<INPUT type='button' id='recandidate" + i + "' value='再提示'>");
            tmpMap.put("col11", "<INPUT type='button' id='confirm" + i + "' value='確認'>");
            tableDataList.add(tmpMap);
        }
        
        return tableDataList;
    }
}
