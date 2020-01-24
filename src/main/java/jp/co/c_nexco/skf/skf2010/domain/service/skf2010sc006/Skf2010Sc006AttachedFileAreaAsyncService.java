/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006AttachedFileAreaAsyncDto;

/**
 * Skf2010Sc006 申請書類承認／差戻し／通知初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006AttachedFileAreaAsyncService
		extends AsyncBaseServiceAbstract<Skf2010Sc006AttachedFileAreaAsyncDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;
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
	public AsyncBaseDto index(Skf2010Sc006AttachedFileAreaAsyncDto dto) {
		
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("資料を添付", CodeConstant.C001, FunctionIdConstant.SKF2010_SC006);

		String applNo = dto.getApplNo();
		List<Map<String, Object>> attachedFileList = skf2010Sc006SharedService.getAttachedFileInfo(applNo);

		String baseLinkTag = "<a id=\"attached_$ATTACHEDNO$\">$ATTACHEDNAME$</a>";
		List<String> listTagList = new ArrayList<String>();

		if (attachedFileList != null && attachedFileList.size() > 0) {
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				String linkTag = baseLinkTag;
				linkTag = linkTag.replace("$ATTACHEDNO$", attachedFileMap.get("attachedNo").toString());
				linkTag = linkTag.replace("$ATTACHEDNAME$", attachedFileMap.get("attachedName").toString());
				listTagList.add(linkTag);
			}
		}

		dto.setAttachedFileArea(String.join("&nbsp;", listTagList));

		return dto;
	}

}
