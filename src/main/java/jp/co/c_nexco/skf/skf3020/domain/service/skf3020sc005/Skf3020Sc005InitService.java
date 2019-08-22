/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001.Skf3010Sc001SharedService;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005InitDto;

/**
 * Skf3020Sc005InitService 転任者登録画面のInitサービス処理クラス。　 
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc005InitService extends BaseServiceAbstract<Skf3020Sc005InitDto> {

	@Autowired
	private Skf3020Sc005SharedService skf3020Sc005SharedService;

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
	public Skf3020Sc005InitDto index(Skf3020Sc005InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3020_SC005_TITLE);

		// 操作ログ出力

		// アラート出力有無をtrueに設定

		// 社員入力支援画面を別ウィンドウで表示する。（モーダル表示）

		// '変更フラグ(false：新規、true：変更)
		Boolean changeFlg = false;

		// 社員番号取得
		String hdnRowShainNo = initDto.getHdnRowShainNo();	
		// セッション情報存在判定(社員番号存在判定)
		if (NfwStringUtils.isNotEmpty(hdnRowShainNo)) {
			/** セッション情報存在(変更) */
			// 転任者情報取得
			Map<String, Object> resultTableData = skf3020Sc005SharedService.getTenninshaShatakuInfo(hdnRowShainNo);
			/** 戻り値(転任者情報)設定 */
			// 社員番号
			initDto.setShainNo((String)resultTableData.get("shainNo"));
			// 仮社員番号設定(社員番号の変更が必要)
			initDto.setChkShainNoHenkoKbn((Boolean)resultTableData.get("shainNoHenkoKbn"));
			// 社員氏名
			initDto.setShainName((String)resultTableData.get("shainName"));
			// 等級
			initDto.setTokyu((String)resultTableData.get("tokyu"));
			// 年齢
			initDto.setAge((String)resultTableData.get("age"));
			// 新所属
			initDto.setNewAffiliation((String)resultTableData.get("ewAffiliation"));
			// 現所属
			initDto.setNowAffiliation((String)resultTableData.get("nowAffiliation"));
			// 備考
			initDto.setBiko((String)resultTableData.get("biko"));
			// 入退居予定作成区分
			initDto.setHdnNyutaikyoYoteiKbn((String)resultTableData.get("hdnNyutaikyoYoteiKbn"));
			// 更新日時(転任者)
			initDto.setHdnUpdateDateTenninsha((String)resultTableData.get("hdnUpdateDateTenninsha"));
			// 更新日時(社員)
			initDto.setHdnUpdateDateShain((String)resultTableData.get("hdnUpdateDateShain"));
			// 変更フラグをtrue
			changeFlg = true;
		} else {
			// 画面初期化設定(新規)
			/** 戻り値(転任者情報)設定 */
			// 社員番号
			initDto.setShainNo("");
			// 仮社員番号設定(社員番号の変更が必要)
			initDto.setChkShainNoHenkoKbn(false);
			// 社員氏名
			initDto.setShainName("");
			// 等級
			initDto.setTokyu("");
			// 年齢
			initDto.setAge("");
			// 新所属
			initDto.setNewAffiliation("");
			// 現所属
			initDto.setNowAffiliation("");
			// 備考
			initDto.setBiko("");
			// 入退居予定作成区分
			initDto.setHdnNyutaikyoYoteiKbn("");
			// 更新日時(転任者)
			initDto.setHdnUpdateDateTenninsha("");
			// 更新日時(社員)
			initDto.setHdnUpdateDateShain("");
		}
		// 項目制御設定(変更 or 新規)
		if (changeFlg) {
			// 変更
			// 入退居予定作成区分判定
			if (initDto.getHdnNyutaikyoYoteiKbn() != null && !"0".equals(initDto.getHdnNyutaikyoYoteiKbn())) {
				initDto.setMaskPattern("CLEATED");
			} else {
				initDto.setMaskPattern("NOTCLEATED");
			}
			// 登録ボタン押下時のメッセージ設定
			initDto.setEnterMsg(MessageIdConstant.I_SKF_3039);
		} else {
			initDto.setMaskPattern("NEW");
			// 登録ボタン押下時のメッセージ設定
			initDto.setEnterMsg(MessageIdConstant.I_SKF_3062);
		}

		// 前に戻るボタン押下時のメッセージ設定（編集）
		initDto.setEnterMsg(MessageIdConstant.I_SKF_3031);

		return initDto;
	}
}
