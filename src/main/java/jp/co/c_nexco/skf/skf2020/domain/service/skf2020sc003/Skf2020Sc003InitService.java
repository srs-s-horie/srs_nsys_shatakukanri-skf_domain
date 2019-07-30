/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfCommentUtils.SkfCommentUtilsGetCommentInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCommentUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationGuideUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003InitDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003InitService extends BaseServiceAbstract<Skf2020Sc003InitDto> {

	// セッションキー
	@Value("${skf.common.shataku_attached_file_session_key}")
	private String sessionKey;

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;
	@Autowired
	private SkfOperationGuideUtils skfOperationGuideUtils;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfCommentUtils skfCommentUtils;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

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
	public BaseDto index(Skf2020Sc003InitDto initDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示処理開始", CodeConstant.C001, initDto.getPageId());

		initDto.setPageTitleKey(MessageIdConstant.SKF2020_SC003_TITLE);

		// 初期表示
		skf2020sc003SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		skf2020sc003SharedService.setDispInfo(initDto);
		// コメントボタン表示チェック
		boolean commentFlg = checkComment(initDto);
		initDto.setCommentViewFlag(commentFlg);

		// オペレーションガイド取得
		String operationGuide = skfOperationGuideUtils.getOperationGuide(initDto.getPageId());
		initDto.setOperationGuide(operationGuide);

		return initDto;
	}

	private boolean checkComment(Skf2020Sc003InitDto initDto) {
		// コメント取得
		List<SkfCommentUtilsGetCommentInfoExp> commentList = new ArrayList<SkfCommentUtilsGetCommentInfoExp>();
		commentList = skfCommentUtils.getCommentInfo(CodeConstant.C001, initDto.getApplNo(), initDto.getApplStatus());
		if (commentList != null && commentList.size() > 0) {
			return true;
		}

		return false;
	}

}
