/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003AttachedFileAreaAsyncDto;

/**
 * Skf2020Sc003 申請書類承認／差戻し／通知初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003AttachedFileAreaAsyncService
		extends AsyncBaseServiceAbstract<Skf2020Sc003AttachedFileAreaAsyncDto> {

	@Autowired
	private SkfAttachedFileUtiles skfAttachedFileUtiles;

	@Value("${skf.common.attached_file_session_key}")
	private String sessionKeyAttached;
	@Value("${skf.common.shataku_attached_file_session_key}")
	private String sessionKeyAttachedShataku;

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
	public AsyncBaseDto index(Skf2020Sc003AttachedFileAreaAsyncDto dto) {

		// 申請書類番号
		String applNo = dto.getApplNo();

		// 社宅向け添付資料取得
		List<Map<String, Object>> shatakuAttachedFileList = skfAttachedFileUtiles
				.getAttachedFileInfo(menuScopeSessionBean, applNo, sessionKeyAttachedShataku);
		// 一般添付資料取得
		List<Map<String, Object>> attachedFileList = skfAttachedFileUtiles.getAttachedFileInfo(menuScopeSessionBean,
				applNo, sessionKeyAttached);
		// 社宅向け添付資料が無い場合配列のインスタンス化だけ行う
		if (shatakuAttachedFileList == null) {
			shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		}

		String baseLinkTag = "<a id=\"attached_$ATTACHEDNO$\">$ATTACHEDNAME$</a>";
		List<String> listTagList = new ArrayList<String>();

		// 添付ファイルがあればリンクタグを生成する
		if (attachedFileList != null && attachedFileList.size() > 0) {
			int attachedNo = shatakuAttachedFileList.size();
			for (Map<String, Object> attachedFileMap : attachedFileList) {
				String linkTag = baseLinkTag;
				linkTag = linkTag.replace("$ATTACHEDNO$", String.valueOf(attachedNo));
				linkTag = linkTag.replace("$ATTACHEDNAME$", attachedFileMap.get("attachedName").toString());
				listTagList.add(linkTag);
				attachedNo++;
			}
		}

		dto.setAttachedFileArea(String.join("&nbsp;", listTagList));

		return dto;
	}

}
