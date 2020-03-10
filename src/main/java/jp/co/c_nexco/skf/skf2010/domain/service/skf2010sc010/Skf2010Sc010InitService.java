/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc010;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc010.Skf2010Sc010InitDto;

/**
 * Skf2010Sc010 申請書コメント初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc010InitService extends BaseServiceAbstract<Skf2010Sc010InitDto> {

	@Value("${skf.common.company_cd}")
	private String companyCd;

	@Autowired
	private Skf2010Sc010SharedService skf2010Sc010SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc010InitDto index(Skf2010Sc010InitDto initDto) throws Exception {
		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2010_SC010_TITLE);

		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		String applNo = initDto.getApplNo();

		// 申請コメント一覧を取得
		List<Map<String, String>> commentList = new ArrayList<Map<String, String>>();
		commentList = skf2010Sc010SharedService.getCommentList(applNo, menuScopeSessionBean);

		initDto.setCommentList(commentList);

		return initDto;
	}

}
