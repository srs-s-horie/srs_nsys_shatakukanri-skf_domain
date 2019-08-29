/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc002.Skf2040Sc002InitDto;

/**
 * Skf2040Sc002 退居（自動車の保管場所返還（アウトソース用））届のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc002InitService extends BaseServiceAbstract<Skf2040Sc002InitDto> {

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
	public Skf2040Sc002InitDto index(Skf2040Sc002InitDto initDto) throws Exception {

		// タイトル設定
		initDto.setPageTitleKey(MessageIdConstant.SKF2040_SC002_TITLE);

		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// 画面内容の設定
		if (setDisplayData(initDto)) {
			// 「添付資料」欄の更新を行う
			refreshHeaderAttachedFile();
		} else {
			// 初期表示に失敗した場合次処理のボタンを押せなくする。
		}

		return initDto;
	}

	/**
	 * 「添付資料」欄の更新を行う
	 */
	private void refreshHeaderAttachedFile() {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * 画面情報の設定を行う
	 * 
	 * @param initDto
	 * @return true=表示OK、false=表示NG
	 */
	private boolean setDisplayData(Skf2040Sc002InitDto initDto) {

		boolean returnValue = true;
		String applTacFlg = CodeConstant.DOUBLE_QUOTATION;

		// セッション情報をチェック

		// セッション申請書類情報を取得

		// 申請書類履歴取得
		// データ件数0件の場合はメッセージ
		// 申請書類履歴情報を画面の隠し項目に設定

		// 申請状況を設定

		// 添付資料がある場合、添付資料表示処理を行う
		// 添付ファイルの設定

		// 承認2済以降の場合
		// コメント入力欄の設定
		// コメント一覧の設定

		// コントロール制御

		// ◆備品返却希望
		// （レポートは非表示）
		// （添付ファイルは非表示）

		// 備品情報の表示
		// 初期表示エラー

		// ボタン制御
		// 申請状況が「審査中」【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】
		// 申請状況が「搬入済」【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：非表示】【差戻しボタン：非表示】【添付資料ボタン：非表示】
		// 申請状況が上記以外【全ボタン非表示】

		// ◆退居（自動車の保管場所返還）届
		// レポート表示
		// 返却備品があるかどうか

		// 申請状況が「審査中」のみ備品情報を表示する
		// 備品情報の表示
		// 備品情報の表示エラーは警告表示で継続
		// それ以外提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

		// 申請状況が「審査中」以外は「社宅の状態」を表示する
		// 退居届テーブルから退居情報を取得
		// 退居届情報の退居する社宅区分が１（社宅、駐車場を退居および返還）または２（社宅を退居）の場合
		// データテーブルに項目名、データを格納する列を作成
		// 社宅の状態の取得
		// 社宅の状態

		// ボタン制御
		// 返却備品なし
		// 申請状況が【提示ボタン：非表示】【承認ボタン：表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

		// 返却備品あり
		// 【提示ボタン：表示】【承認ボタン：非表示】【修正依頼ボタン：表示】【差戻しボタン：表示】【添付資料ボタン：表示】

		// 表示正常終了
		return returnValue;

	}

}
