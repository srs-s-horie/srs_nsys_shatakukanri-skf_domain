package jp.co.c_nexco.skf.skf3070.domain.service.skf3070sc004;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3070Sc004.Skf3070Sc004GetOwnerInfoExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3070.domain.dto.skf3070sc004.Skf3070Sc004SearchAsyncDto;

/**
 * 賃貸人(代理人)名称入力支援画面のInitサービス処理クラス。
 * 
 */
@Service
public class Skf3070Sc004SearchAsyncService extends SkfAsyncServiceAbstract<Skf3070Sc004SearchAsyncDto> {

	@Autowired
	private Skf3070Sc004SharedService skf3070Sc004SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	// 最大表示件数
	@Value("${skf3070.skf3070_sc004.search_max_count}")
	private String maxCount;
	

	@Override
	public AsyncBaseDto index(Skf3070Sc004SearchAsyncDto searchDto) throws Exception {
		// 操作ログ登録
		skfOperationLogUtils.setAccessLog("検索", companyCd, FunctionIdConstant.SKF3070_SC004);

		List<Skf3070Sc004GetOwnerInfoExp> ownerInfoList = new ArrayList<Skf3070Sc004GetOwnerInfoExp>();

		// 入力チェック
		if (!checkValidate(searchDto)) {
			// リストテーブルは表示する
			throwBusinessExceptionIfErrors(searchDto.getResultMessages());
			return searchDto;
		}

		// 社員情報一覧検索
		ownerInfoList = skf3070Sc004SharedService.getOwnerInfo(searchDto.getPopOwnerName(),
				searchDto.getPopOwnerNameKk(), searchDto.getPopAddress(), searchDto.getPopBusinessKbn());
		if (ownerInfoList.size() == 0) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
			ownerInfoList.clear();
		} else if (ownerInfoList.size() > Integer.parseInt(maxCount)) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.E_SKF_1046, maxCount);
			ownerInfoList.clear();
		}
		
		//リストテーブルデータ取得
		List<Map<String, Object>> popOwnerInfoList = new ArrayList<Map<String, Object>>();
		popOwnerInfoList = skf3070Sc004SharedService.createListTable(ownerInfoList);
		searchDto.setPopListTableList(popOwnerInfoList);

		return searchDto;
		
	}

	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @return
	 */
	private boolean checkValidate(Skf3070Sc004SearchAsyncDto dto) {
		boolean result = true;


		// 氏名又は名称（フリガナ）
		if (NfwStringUtils.isNotBlank(dto.getPopOwnerNameKk())
				&& !CheckUtils.isFullWidthKatakanaSpace(dto.getPopOwnerNameKk())) {
			ServiceHelper.addErrorResultMessage(dto, new String[] { "ownerNameKk" }, MessageIdConstant.E_SKF_1005,
					"氏名又は名称（フリガナ）");
			result = false;
		}

		return result;
	}

}
