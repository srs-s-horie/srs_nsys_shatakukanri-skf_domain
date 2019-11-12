/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc005;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc005.Skf3090Sc005GetLedgerCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc005.Skf3090Sc005GetLedgerCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc005.Skf3090Sc005DeleteDto;

/**
 * Skf3090Sc005DeleteService 従業員マスタ登録削除ボタン押下時処理クラス.
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc005DeleteService extends BaseServiceAbstract<Skf3090Sc005DeleteDto> {

	@Autowired
	Skf3090Sc005GetLedgerCountExpRepository skf3090Sc005GetLedgerCountExpRepository;
	@Autowired
	Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * メインメソッド.
	 */
	@Override
	protected BaseDto index(Skf3090Sc005DeleteDto deleteDto) throws Exception {

		deleteDto.setPageTitleKey(MessageIdConstant.SKF3090_SC005_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("削除", companyCd, deleteDto.getPageId());

		/** 利用実績チェック */
		// 社宅管理台帳存在チェック
		if (getLedgerCount(deleteDto.getShainNo()) > 0) {
			// レコードが存在する場合エラー
			// TODO {0}の中のメッセージが出ない件
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_3031, deleteDto.getShainNo());

		} else {
			/** データ物理削除 */
			deleteShainInfo(CodeConstant.C001, deleteDto.getShainNo());

			/** 画面遷移 */
			deleteDto.setTransferPageInfo(TransferPageInfo.nextPage(FunctionIdConstant.SKF3090_SC004), true);

		}

		return deleteDto;
	}

	/**
	 * 社宅管理台帳テーブルより社員番号をキーにレコードカウントを取得する。.
	 * 
	 * @param shainNo 社員番号
	 * @return SQL結果レコード数
	 */
	private long getLedgerCount(String shainNo) {

		// パラメータセット
		Skf3090Sc005GetLedgerCountExpParameter setValue = new Skf3090Sc005GetLedgerCountExpParameter();
		setValue.setShainNo(shainNo);

		// SQL実行
		long resultCount = skf3090Sc005GetLedgerCountExpRepository.getLedgerCount(setValue).getLedgerCount();
		LogUtils.debugByMsg("社員番号：" + shainNo + " の、社宅管理台帳テーブル存在レコード数 = " + resultCount + "件");

		return resultCount;

	}

	/**
	 * 社員マスタテーブルから社員情報を削除する。.
	 * 
	 * @param companyCd 会社コード
	 * @param shainNo 社員番号
	 */
	private void deleteShainInfo(String companyCd, String shainNo) {

		// パラメータセット
		Skf1010MShainKey setValue = new Skf1010MShainKey();
		setValue.setCompanyCd(companyCd);
		setValue.setShainNo(shainNo);

		// SQL実行
		int deleteCount = skf1010MShainRepository.deleteByPrimaryKey(setValue);
		LogUtils.debugByMsg("社員番号：" + shainNo + " の、削除レコード数 = " + deleteCount + "件");
	}

}
