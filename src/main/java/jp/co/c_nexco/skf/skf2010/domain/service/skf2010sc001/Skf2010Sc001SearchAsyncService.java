package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetAllShainInfoExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc001.Skf2010Sc001SearchAsyncDto;

@Service
public class Skf2010Sc001SearchAsyncService extends SkfAsyncServiceAbstract<Skf2010Sc001SearchAsyncDto> {

	@Autowired
	private Skf2010Sc001SharedService skf2010Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc001.search_max_count}")
	private String maxCount;
	@Value("${skf.common.validate_error}")
	private String errorClass;

	@Override
	public AsyncBaseDto index(Skf2010Sc001SearchAsyncDto searchDto) throws Exception {
		// 操作ログ登録
		skfOperationLogUtils.setAccessLog("検索", companyCd, FunctionIdConstant.SKF2010_SC001);

		List<Skf2010Sc001GetAllShainInfoExp> shainInfoList = new ArrayList<Skf2010Sc001GetAllShainInfoExp>();

		// 入力チェック
		if (!checkValidate(searchDto)) {
			// リストテーブルは表示する
			throwBusinessExceptionIfErrors(searchDto.getResultMessages());
			// searchDto.setPopListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));
			return searchDto;
		}

		// 社員情報一覧検索
		shainInfoList = skf2010Sc001SharedService.getShainMasterInfo(companyCd, searchDto.getPopShainNo(),
				searchDto.getPopName(), searchDto.getPopNameKk(), searchDto.getPopAgency(),
				searchDto.getPopShatakuKanriNo());
		if (shainInfoList.size() == 0) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
			shainInfoList.clear();
		} else if (shainInfoList.size() > Integer.parseInt(maxCount)) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.E_SKF_1046, maxCount);
			shainInfoList.clear();
		}

		searchDto.setPopListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));

		return searchDto;
	}

	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @return
	 */
	private boolean checkValidate(Skf2010Sc001SearchAsyncDto dto) {
		boolean result = true;
		// 社員番号チェック
		String shainNo = dto.getPopShainNo();
		if (NfwStringUtils.isNotEmpty(shainNo)) {
			// 半全角スペースが入っていた場合、もしくは半角英数以外だった場合エラー
			if (shainNo.indexOf(CodeConstant.SPACE_CHAR) > 0 || shainNo.indexOf(CodeConstant.ZEN_SPACE) > 0 || false) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "popShainNo" }, MessageIdConstant.E_SKF_1052,
						"社員番号");
				result = false;
			}

		}

		// 氏名（カナ）チェック
		String nameKk = dto.getPopNameKk();
		if (NfwStringUtils.isNotEmpty(nameKk)) {
			// 全角カナ以外の場合エラー
			if (!CheckUtils.isFullWidthKatakana(nameKk)) {
				ServiceHelper.addErrorResultMessage(dto, new String[] { "popNameKk" }, MessageIdConstant.E_SKF_1005,
						"社員名カナ");
				result = false;
			}
		}

		return result;
	}

}
