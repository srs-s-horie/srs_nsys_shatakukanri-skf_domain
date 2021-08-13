package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc010;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentListExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc010.Skf2010Sc010GetCommentListExpRepository;
import jp.co.c_nexco.nfw.common.bean.MenuScopeSessionBean;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

@Service
public class Skf2010Sc010SharedService {

	@Autowired
	private Skf2010Sc010GetCommentListExpRepository skf2010Sc010GetCommentListExpRepository;
	@Autowired
	private SkfCommentUtils skfCommentUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	@Value("${skf.common.company_cd}")
	private String companyCd;

	public List<Map<String, String>> getCommentList(String applNo, MenuScopeSessionBean bean) {
		List<Map<String, String>> commentList = new ArrayList<Map<String, String>>();

		List<SkfCommentUtilsGetCommentListExp> resultList = new ArrayList<SkfCommentUtilsGetCommentListExp>();

		Map<String, String> loginUserInfo = skfLoginUserInfoUtils.getSkfLoginUserInfoFromAlterLogin(bean);

		Map<String, String> genericCodeMap = new HashMap<String, String>();
		
		// コメント一覧を取得する
		resultList = skfCommentUtils.getCommentList(companyCd, applNo, CodeConstant.STATUS_SHONIN1);
		
		boolean res = skfLoginUserInfoUtils.isAgreeAuthority(companyCd, null, loginUserInfo.get("roleId"), CodeConstant.NONE);
		if(res){
			genericCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_COMMENT_APPL_STATUS_ADMIN);
		}else{
			genericCodeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_COMMENT_APPL_STATUS_USER);	
		}
		
		
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> commentMap = null;
			for (SkfCommentUtilsGetCommentListExp result : resultList) {

				commentMap = new HashMap<String, String>();

				commentMap.put("applShainName", result.getApplShainName());

				String commentName = result.getCommentName().replace("\r\n", "<br />");
				commentMap.put("commentName", commentName);
				String[] commentNames = commentName.split("<br />");
				String titleCommentName = CodeConstant.NONE;
				if (commentNames.length > 1) {
					titleCommentName = commentNames[1];
				} else if (commentNames.length == 1) {
					titleCommentName = commentNames[0];
				}
				commentMap.put("titleCommentName", titleCommentName);

				commentMap.put("commentNote", result.getCommentNote().replace("\r\n", "<br />"));

				Date commentDate = result.getCommentDate();
				String commentDateStr = skfDateFormatUtils.dateFormatFromDate(commentDate, "yyyy/MM/dd HH:mm:ss");
				commentMap.put("commentDate", commentDateStr);

				commentMap.put("applStatus", genericCodeMap.get(result.getApplStatus()));

				String isToShouninTitle = "false";
				switch (result.getApplStatus()) {
				case CodeConstant.STATUS_SHINSEICHU:
				case CodeConstant.STATUS_DOI_ZUMI:
				case CodeConstant.STATUS_DOI_SHINAI:
				case CodeConstant.STATUS_HANNYU_ZUMI:
				case CodeConstant.STATUS_HANSYUTSU_ZUMI:
				case CodeConstant.STATUS_SENTAKU_ZUMI:
					isToShouninTitle = "true";
					break;
				}
				commentMap.put("isShouninTitle", isToShouninTitle);

				commentList.add(commentMap);
			}
		}

		return commentList;
	}

}
