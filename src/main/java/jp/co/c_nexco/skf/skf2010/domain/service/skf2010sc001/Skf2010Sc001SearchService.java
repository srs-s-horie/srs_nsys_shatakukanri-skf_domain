package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc001;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc001.Skf2010Sc001GetShainMasterInfoByParameterExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc001.Skf2010Sc001SearchDto;

@Service
public class Skf2010Sc001SearchService extends BaseServiceAbstract<Skf2010Sc001SearchDto> {

	@Autowired
	private Skf2010Sc001SharedService skf2010Sc001SharedService;

	private String companyCd = CodeConstant.C001;

	@Value("${skf2010.skf2010_sc001.search_max_count}")
	private String maxCount;
	@Value("${skf.common.validate_error}")
	private String errorClass;

	@Override
	public BaseDto index(Skf2010Sc001SearchDto searchDto) throws Exception {

		searchDto.setPageTitleKey(MessageIdConstant.SKF2010_SC001_TITLE);

		boolean nyukyoFlag = false;
		String nyukyoFlagStr = searchDto.getNyukyoFlag();
		if (nyukyoFlagStr != null && !CheckUtils.isEmpty(nyukyoFlagStr)) {
			nyukyoFlag = Boolean.valueOf(nyukyoFlagStr);
		}

		init(searchDto);

		List<Skf2010Sc001GetShainMasterInfoByParameterExp> shainInfoList = new ArrayList<Skf2010Sc001GetShainMasterInfoByParameterExp>();

		// 入力チェック
		if (!checkValidate(searchDto)) {
			// リストテーブルは表示する
			searchDto.setListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));
			return searchDto;
		}

		// 社員情報一覧検索
		shainInfoList = skf2010Sc001SharedService.getShainMasterInfo(companyCd, searchDto.getShainNo(),
				searchDto.getName(), searchDto.getNameKk(), searchDto.getAgency(), nyukyoFlag);
		if (shainInfoList.size() == 0) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.W_SKF_1007);
			shainInfoList.clear();
		} else if (shainInfoList.size() > Integer.parseInt(maxCount)) {
			ServiceHelper.addWarnResultMessage(searchDto, MessageIdConstant.E_SKF_1046, "最大値");
			shainInfoList.clear();
		}

		searchDto.setListTableList(skf2010Sc001SharedService.createListTable(shainInfoList));

		return searchDto;
	}

	private void init(Skf2010Sc001SearchDto dto) {
		dto.setErrShainNo("");
		dto.setErrAgency("");
		dto.setErrName("");
		dto.setErrNameKk("");
	}

	/**
	 * 入力チェック
	 * 
	 * @param dto
	 * @return
	 */
	private boolean checkValidate(Skf2010Sc001SearchDto dto) {
		boolean result = true;
		// 社員番号チェック
		String shainNo = dto.getShainNo();
		if (shainNo != null && !CheckUtils.isEmpty(shainNo)) {
			// 半全角スペースが入っていた場合、もしくは半角英数以外だった場合エラー
			if (shainNo.indexOf(CodeConstant.SPACE_CHAR) > 0 || shainNo.indexOf(CodeConstant.ZEN_SPACE) > 0 || false) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1052, "社員番号");
				dto.setErrShainNo(errorClass);
				result = false;
			}

		}

		// 氏名（カナ）チェック
		String nameKk = dto.getNameKk();
		if (nameKk != null && !CheckUtils.isEmpty(nameKk)) {
			// 全角カナ以外の場合エラー
			if (!CheckUtils.isFullWidthKatakana(nameKk)) {
				ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.E_SKF_1005, "社員名カナ");
				dto.setErrNameKk(errorClass);
				result = false;
			}
		}

		return result;
	}

}
