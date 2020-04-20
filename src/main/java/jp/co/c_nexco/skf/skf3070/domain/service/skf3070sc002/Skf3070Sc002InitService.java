/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc002;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3070TOwnerInfo;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3070TOwnerInfoRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc002.Skf3070Sc002InitDto;

/**
 * Skf3070_Sc002 賃貸人（代理人）情報登録画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 * 
 */
@Service
public class Skf3070Sc002InitService extends SkfServiceAbstract<Skf3070Sc002InitDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf3070TOwnerInfoRepository skf3070TOwnerInfoRepository;
	@Autowired
	private Skf3070Sc002SharedService skf3070Sc002SheardService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public BaseDto index(Skf3070Sc002InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3070_SC002_TITLE);
		initDto.setPageId(FunctionIdConstant.SKF3070_SC002);

		if (NfwStringUtils.isNotEmpty(initDto.getOwnerNo())) {
			// 賃貸人（代理人）番号がある場合
			// 操作ログを出力
			skfOperationLogUtils.setAccessLog("編集", CodeConstant.C001, initDto.getPrePageId());

			// 賃貸人（代理人）情報を取得
			long ownerNoL = Long.parseLong(initDto.getOwnerNo());
			Skf3070TOwnerInfo ownerInfo = new Skf3070TOwnerInfo();
			ownerInfo = skf3070TOwnerInfoRepository.selectByPrimaryKey(ownerNoL);

			if (ownerInfo != null) {
				// 賃貸人（代理人）情報が取得できた場合、画面項目を設定
				initDto.setOwnerName(ownerInfo.getOwnerName());
				initDto.setOwnerNameKk(ownerInfo.getOwnerNameKk());
				initDto.setZipCode(ownerInfo.getZipCd());
				initDto.setAddress(ownerInfo.getAddress());
				initDto.setBusinessKbn(ownerInfo.getBusinessKbn());
				initDto.setAcceptFlg(ownerInfo.getAcceptFlg());
				initDto.setAcceptStatus(ownerInfo.getAcceptStatus());
				initDto.setRemarks(ownerInfo.getRemarks());

				// 最終更新日を設定
				initDto.addLastUpdateDate(Skf3070Sc002SharedService.KEY_LAST_UPDATE_DATE, ownerInfo.getUpdateDate());

			}

		} else {
			// 操作ログを出力
			skfOperationLogUtils.setAccessLog("新規", CodeConstant.C001, initDto.getPrePageId());
			// 項目初期化
			initDto.setOwnerName(CodeConstant.NONE);
			initDto.setOwnerNameKk(CodeConstant.NONE);
			initDto.setZipCode(CodeConstant.NONE);
			initDto.setAddress(CodeConstant.NONE);
			initDto.setBusinessKbn(CodeConstant.NONE);
			initDto.setAcceptFlg(CodeConstant.NONE);
			initDto.setAcceptStatus(CodeConstant.NONE);
			initDto.setRemarks(CodeConstant.NONE);
			
		}

		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());
		// ドロップダウンリスト用リストの生成
		skf3070Sc002SheardService.getDropDownList(initDto);

		return initDto;
	}

}
