package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MSoshikiKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MSoshikiRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007DeleteDto;

/**
 * Skf3090Sc007DeleteService 組織マスタ削除ボタン押下時の処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007DeleteService extends SkfServiceAbstract<Skf3090Sc007DeleteDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf1010MSoshikiRepository skf1010MSoshikiRepository;

	@Override
	public Skf3090Sc007DeleteDto index(Skf3090Sc007DeleteDto deleteDto) throws Exception {

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("削除", CodeConstant.C001, deleteDto.getPrePageId());

		// 組織データ削除
		boolean deleteCheck = deleteSoshikiInfo(deleteDto.getRegistCompanyCd(), deleteDto.getRegistAgencyCd(),
				deleteDto.getRegistAffiliation1Cd(), deleteDto.getRegistAffiliation2Cd());

		// 削除が失敗した場合
		if (!(deleteCheck)) {
			ServiceHelper.addErrorResultMessage(deleteDto, null, MessageIdConstant.E_SKF_1076);
			LogUtils.infoByMsg("組織マスタ削除ボタン押下時削除失敗　会社コード： "+deleteDto.getRegistCompanyCd()
				+" 機関コード： "+deleteDto.getRegistAgencyCd()
				+" 部等コード： "+deleteDto.getRegistAffiliation1Cd()
				+" 室・課コード： " +deleteDto.getRegistAffiliation2Cd());
		} else {
			// 画面遷移
			deleteDto.setTransferPageInfo(TransferPageInfo.prevPage(FunctionIdConstant.SKF3090_SC006));
		}

		return deleteDto;

	}

	/**
	 * Delete処理
	 * 
	 * @param companyCd
	 * @param agencyCd
	 * @param affiliation1Cd
	 * @param affiliation2Cd
	 * @return
	 */
	private boolean deleteSoshikiInfo(String companyCd, String agencyCd, String affiliation1Cd, String affiliation2Cd) {
		int deleteCount = 0;
		boolean deleteCheck = true;

		// パラメータセット
		Skf1010MSoshikiKey setValue = new Skf1010MSoshikiKey();
		setValue.setCompanyCd(companyCd);
		setValue.setAgencyCd(agencyCd);
		setValue.setAffiliation1Cd(affiliation1Cd);
		setValue.setAffiliation2Cd(affiliation2Cd);

		// SQL実行
		deleteCount = skf1010MSoshikiRepository.deleteByPrimaryKey(setValue);

		if (deleteCount <= 0) {
			deleteCheck = false;
		}
		return deleteCheck;
	}
}
