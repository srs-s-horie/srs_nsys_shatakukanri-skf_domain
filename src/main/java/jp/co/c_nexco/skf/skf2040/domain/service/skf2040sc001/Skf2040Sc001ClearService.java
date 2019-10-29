/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001ClearDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の入力内容クリア処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001ClearService extends BaseServiceAbstract<Skf2040Sc001ClearDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2040Sc001SharedService skf2040Sc001SharedService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param clearDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001ClearDto index(Skf2040Sc001ClearDto clearDto) throws Exception {
		clearDto.setPageTitleKey(MessageIdConstant.SKF2040_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("入力内容をクリア", CodeConstant.C001, clearDto.getPageId());

		this.execClear(clearDto);

		// 画面ID保持
		clearDto.setPrePageId(FunctionIdConstant.SKF2060_SC004);
		return clearDto;
	}

	/**
	 * 画面クリア処理
	 * 
	 * @param clearDto
	 */
	private void execClear(Skf2040Sc001ClearDto clearDto) {

		// 入力情報のクリア
		this.clearInfo(clearDto);

		/*
		 * 保存状態の復帰
		 */
		// 登録済みデータの情報設定
		// 登録済の退居届情報が存在するかを判定 ※一時保存からの復帰時
		if (clearDto.getApplNo() != null) {
			// 申請書管理番号がDtoに存在する場合、登録済の退居届情報をDtoに設定する
			skf2040Sc001SharedService.setExistTaikyoInfo(clearDto);

		}
	}

	/**
	 * 入力項目の値をクリアする。
	 * 
	 * <pre>
	 * 新規申請時のみクリアを行う。
	 * 前画面が申請内容確認画面である場合（一時保存からの申請時など）は
	 * 申請内容の復元を行うためクリアを行わない。
	 * </pre>
	 * 
	 * @param clearDto
	 */
	private void clearInfo(Skf2040Sc001ClearDto clearDto) {
		// 前画面が申請内容確認でない場合は入力項目の値をクリアする
		if (NfwStringUtils.isNotEmpty(clearDto.getPrePageId())
				&& !FunctionIdConstant.SKF2010_SC002.equals(clearDto.getPrePageId())) {

			// 社宅名
			clearDto.setNowShatakuName(null);
			// 退居返還日
			clearDto.setTaikyoHenkanDate(null);
			// チェックボックス
			clearDto.setTaikyoType(null);

			// 退居返還理由ドロップダウン
			clearDto.setTaikyoRiyuKbn(null);
			// 退居返還理由その他
			clearDto.setTaikyoHenkanRiyu(null);
			// 社宅の状態
			clearDto.setShatakuJotai(null);
			// 退居後の連絡先
			clearDto.setTaikyogoRenrakuSaki(null);
			// 返却立合希望日
			clearDto.setSessionDay(null);
			// 返却立合希望日（時間）
			clearDto.setSessionTime(null);
			// 連絡先
			clearDto.setRenrakuSaki(null);
		}
	}
}
