package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc010;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc010.Skf2010Sc010GetCommentListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc010.Skf2010Sc010GetCommentListExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc010.Skf2010Sc010GetCommentListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc010.Skf2010Sc010GetShainInfoExpRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;

@Service
public class Skf2010Sc010SharedService {

	@Autowired
	private Skf2010Sc010GetCommentListExpRepository skf2010Sc010GetCommentListExpRepository;
	@Autowired
	private Skf2010Sc010GetShainInfoExpRepository skf2010Sc010GetShainInfoExpRepository;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	@Value("${skf.common.company_cd}")
	private String companyCd;

	public List<Map<String, String>> getCommentList(String applNo) {
		List<Map<String, String>> commentList = new ArrayList<Map<String, String>>();

		List<Skf2010Sc010GetCommentListExp> resultList = new ArrayList<Skf2010Sc010GetCommentListExp>();
		Skf2010Sc010GetCommentListExpParameter param = new Skf2010Sc010GetCommentListExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		String applStatus = "";

		Map<String, String> loginUserInfoMap = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String roleId = loginUserInfoMap.get("roleId");
		switch (roleId) {
		case CodeConstant.NAKASA_SHATAKU_TANTO:
		case CodeConstant.NAKASA_SHATAKU_KANRI:
		case CodeConstant.SYSTEM_KANRI:
			applStatus = "";
			break;
		default:
			applStatus = CodeConstant.STATUS_SHONIN1;
			break;
		}
		param.setApplStatus(applStatus);

		Map<String, String> genericCodeMap = skfGenericCodeUtils.getGenericCode("SKF1001");

		resultList = skf2010Sc010GetCommentListExpRepository.getCommentList(param);
		if (resultList != null && resultList.size() > 0) {
			Map<String, String> commentMap = null;
			for (Skf2010Sc010GetCommentListExp result : resultList) {

				commentMap = new HashMap<String, String>();

				commentMap.put("applShainName", result.getApplShainName());

				commentMap.put("commentName", result.getCommentName().replace("\r\n", "</br>"));

				commentMap.put("commentNote", result.getCommentNote().replace("\r\n", "</br>"));

				Date commentDate = result.getCommentDate();
				String commentDateStr = skfDateFormatUtils.dateFormatFromDate(commentDate, "yyyy/MM/dd HH:mm:ss");
				commentMap.put("commentDate", commentDateStr);

				commentMap.put("applStatus", genericCodeMap.get(result.getApplStatus()));

				commentMap.put("applStatusCd", result.getApplStatus());

				commentList.add(commentMap);
			}
		}

		return commentList;
	}

}
