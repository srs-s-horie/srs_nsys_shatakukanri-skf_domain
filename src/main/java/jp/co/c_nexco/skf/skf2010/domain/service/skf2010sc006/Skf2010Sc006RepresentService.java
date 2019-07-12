package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006RepresentDto;

/**
 * Skf2010Sc006 申請書類承認／修正依頼／通知 再提示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006RepresentService extends BaseServiceAbstract<Skf2010Sc006RepresentDto> {

	@Autowired
	private Skf2010Sc006SharedService skf2010Sc006SharedService;

	private String companyCd = CodeConstant.C001;

	@Override
	protected BaseDto index(Skf2010Sc006RepresentDto reDto) throws Exception {

		// 添付ファイル有無を取得

		// 申請情報の取得を行う
		Map<String, String> errMap = new HashMap<String, String>();
		String applNo = reDto.getApplNo();
		String comment = reDto.getCommentNote();
		String applUpdateDate = reDto.getApplUpdateDate();
		boolean res = skf2010Sc006SharedService.representData(companyCd, applNo, comment, applUpdateDate, errMap);
		if (!res) {
			ServiceHelper.addErrorResultMessage(reDto, null, errMap.get("error"));
			throwBusinessExceptionIfErrors(reDto.getResultMessages());
		}

		// TODO 添付ファイル管理テーブル更新処理

		// TODO 社宅管理データ連携処理実行（オンラインバッチ）

		// 画面遷移

		TransferPageInfo tpi = TransferPageInfo.nextPage(FunctionIdConstant.SKF2020_SC003);
		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("applNo", applNo);
		tpi.addResultMessage(MessageIdConstant.I_SKF_2048);
		tpi.setTransferAttributes(attribute);
		reDto.setTransferPageInfo(tpi);
		return reDto;
	}

}
