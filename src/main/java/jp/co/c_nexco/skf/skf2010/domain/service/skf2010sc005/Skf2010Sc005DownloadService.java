package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExpParameter;
import jp.co.c_nexco.nfw.common.entity.base.BaseCodeEntity;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput.BeanOutputCsv;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput.OutputFileCsvProperties;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005DownloadDto;

/**
 * Skf2010Sc005 承認一覧CSV出力処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005DownloadService extends BaseServiceAbstract<Skf2010Sc005DownloadDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;

	private Map<String, BaseCodeEntity> baseProperty;

	private String templateFilePropertyKeyR0100 = "skf2010.skf2010_sc005.r0100_csv";
	private String templateFilePropertyKeyR0103 = "skf2010.skf2010_sc005.r0103_csv";
	private String templateFilePropertyKeyR0104 = "skf2010.skf2010_sc005.r0104_csv";
	private String templateFilePropertyKeyR0105 = "skf2010.skf2010_sc005.r0105_csv";

	@Value("${skf.common.company_cd}")
	private String companyCd;
	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf2010.skf2010_sc005.appl_data_zip}")
	private String outputZipFileName;

	@Override
	public BaseDto index(Skf2010Sc005DownloadDto dlDto) throws Exception {
		// 操作ログ出力
		// 汎用コード取得
		baseProperty = codeCacheUtils.getGenericCode("SKF1001");

		// 出力情報取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> resultList = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		resultList = searchApplList(dlDto);

		if (resultList == null || resultList.size() <= 0) {
			ServiceHelper.addErrorResultMessage(dlDto, null, MessageIdConstant.E_SKF_1019);
			return dlDto;
		}

		// Csv作成
		// 申請書類事の列情報リスト
		// 社宅希望等申請調書
		List<String[]> r0100List = new ArrayList<String[]>();
		// 社宅（自動車保管場所）退居届
		List<String[]> r0103List = new ArrayList<String[]>();
		// 備品希望申請
		List<String[]> r0104List = new ArrayList<String[]>();
		// 備品返却確認
		List<String[]> r0105List = new ArrayList<String[]>();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		String[] rowData = {};

		Map<String, String[]> columnMap = new HashMap<String, String[]>();

		// CSV
		for (Skf2010Sc005GetShoninIchiranShoninExp applData : resultList) {
			columnMap = new HashMap<String, String[]>();

			rowData = setApplDataForCsv(applData, applData.getApplId(), columnMap);
			switch (applData.getApplId()) {
			case "R0100":
				r0100List.add(rowData);
				break;
			case "R0103":
				r0103List.add(rowData);
				break;
			case "R0104":
				r0104List.add(rowData);
				break;
			case "R0105":
				r0105List.add(rowData);
				break;
			}
		}

		BeanOutputCsv beanOutputCsvR0100 = new FileOutput().new BeanOutputCsv(r0100List, templateFilePropertyKeyR0100,
				"skf2010r0100", 1, null);
		BeanOutputCsv beanOutputCsvR0103 = new FileOutput().new BeanOutputCsv(r0103List, templateFilePropertyKeyR0103,
				"skf2010r0103", 1, null);
		BeanOutputCsv beanOutputCsvR0104 = new FileOutput().new BeanOutputCsv(r0104List, templateFilePropertyKeyR0104,
				"skf2010r0104", 1, null);
		BeanOutputCsv beanOutputCsvR0105 = new FileOutput().new BeanOutputCsv(r0105List, templateFilePropertyKeyR0105,
				"skf2010r0105", 1, null);
		List<BeanOutputCsv> beanOutputCsvList = new ArrayList<BeanOutputCsv>();
		beanOutputCsvList.add(beanOutputCsvR0100);
		beanOutputCsvList.add(beanOutputCsvR0103);
		beanOutputCsvList.add(beanOutputCsvR0104);
		beanOutputCsvList.add(beanOutputCsvR0105);

		OutputFileCsvProperties properties = new FileOutput().new OutputFileCsvProperties(
				FileOutput.LineSeparatorType.LINE_SEPARATOR_CRLF, FileOutput.FileEncode.BOM_UTF8,
				FileOutput.DelimiterType.COMMA, FileOutput.QuoteType.DOUBLE_QUOTATION);
		FileOutput.fileOutputCsvForZip(beanOutputCsvList, dlDto, "申請書類.zip", properties);

		return dlDto;
	}

	/**
	 * 申請書データの取得
	 * 
	 * @param dto
	 * @return
	 */
	private List<Skf2010Sc005GetShoninIchiranShoninExp> searchApplList(Skf2010Sc005DownloadDto dto) {
		// 承認一覧を条件から取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> tApplHistoryData = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();

		// 申請状況チェック
		if (dto.getApplStatus() == null || dto.getApplStatus().length == 0) {
			ServiceHelper.addErrorResultMessage(dto, null, MessageIdConstant.W_SKF_1048, "申請状況");
			throwBusinessExceptionIfErrors(dto.getResultMessages());
		} else {
			// 検索条件セット
			param = setParam(dto);
			// 検索処理
			tApplHistoryData = skf2010Sc005SharedService.SearchApplList(param);

		}

		return tApplHistoryData;
	}

	/**
	 * 検索条件をセットします。
	 * 
	 * @param dto
	 * @return
	 */
	private Skf2010Sc005GetShoninIchiranShoninExpParameter setParam(Skf2010Sc005DownloadDto dto) {
		Skf2010Sc005GetShoninIchiranShoninExpParameter param = new Skf2010Sc005GetShoninIchiranShoninExpParameter();
		// 会社コード
		param.setCompanyCd(companyCd);

		// 機関
		if (dto.getAgency() != null && !CheckUtils.isEmpty(dto.getAgency())) {
			param.setAgencyName(skf2010Sc005SharedService.getAgencyName(companyCd, dto.getAgency()));
		}
		// 部等
		if (dto.getAffiliation1() != null && !CheckUtils.isEmpty(dto.getAffiliation1())) {
			param.setAffiliation1Name(
					skf2010Sc005SharedService.getAffiliation1Name(companyCd, dto.getAgency(), dto.getAffiliation1()));

		}
		// 室、チーム
		if (dto.getAffiliation2() != null && !CheckUtils.isEmpty(dto.getAffiliation2())) {
			param.setAffiliation2Name(skf2010Sc005SharedService.getAffiliation2Name(companyCd, dto.getAgency(),
					dto.getAffiliation1(), dto.getAffiliation2()));

		}
		// 所属機関
		param.setShozokuKikan(dto.getShozokuKikan());
		// 申請日時（FROM）
		if (dto.getApplDateFrom() != null && !CheckUtils.isEmpty(dto.getApplDateFrom())) {
			param.setApplDateFrom(dto.getApplDateFrom().replace("/", ""));
		}
		// 申請日時（TO）
		if (dto.getApplDateTo() != null && !CheckUtils.isEmpty(dto.getApplDateTo())) {
			param.setApplDateTo(dto.getApplDateTo().replace("/", ""));
		}
		// 承認日／修正依頼日（From）
		if (dto.getAgreDateFrom() != null && !CheckUtils.isEmpty(dto.getAgreDateFrom())) {
			param.setAgreDateFrom(dto.getAgreDateFrom().replace("/", ""));
		}
		// 承認日／修正依頼日（To）
		if (dto.getAgreDateTo() != null && !CheckUtils.isEmpty(dto.getAgreDateTo())) {
			param.setAgreDateTo(dto.getAgreDateTo().replace("/", ""));
		}
		// 申請者名
		if (dto.getName() != null && !CheckUtils.isEmpty(dto.getName())) {
			param.setName(dto.getName());
		}
		// 申請書類種別
		param.setApplCtgryId(dto.getApplCtgry());
		// 申請状況
		if (dto.getApplStatus() != null && dto.getApplStatus().length > 0) {
			List<String> applStatusList = new ArrayList<String>();
			List<String> tmpApplStatus = Arrays.asList(dto.getApplStatus());
			applStatusList.addAll(tmpApplStatus);
			// 申請状況に「30：承認中」がセットされていた場合、承認1～4まで全て設定する
			if (applStatusList.contains(CodeConstant.STATUS_SHONIN)) {
				applStatusList.add(CodeConstant.STATUS_SHONIN1);
				applStatusList.add(CodeConstant.STATUS_SHONIN2);
				applStatusList.add(CodeConstant.STATUS_SHONIN3);
				applStatusList.add(CodeConstant.STATUS_SHONIN4);
			}
			param.setApplStatus(applStatusList);
		}
		// 承認者名
		if (dto.getAgreementName() != null && !CheckUtils.isEmpty(dto.getAgreementName())) {
			param.setAgreeName(dto.getAgreementName());
		}
		return param;
	}

	/**
	 * CSV用各種申請書データを取得
	 * 
	 * @param applData
	 * @param applId
	 * @return
	 */
	private String[] setApplDataForCsv(Skf2010Sc005GetShoninIchiranShoninExp applData, String applId,
			Map<String, String[]> columnMap) {
		List<String> tmpList = new ArrayList<String>();

		// 日付型を文字列型に変更する
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String applDate = "";
		if (applData.getApplDate() != null) {
			applDate = sdf.format(applData.getApplDate()).toString();
		}
		String agreDate = "";
		if (applData.getAgreDate() != null) {
			agreDate = sdf.format(applData.getAgreDate()).toString();
		}

		// 申請状況をコードから汎用コードに変更
		String applStatus = "";
		if (applData.getApplStatus() != null) {
			BaseCodeEntity baseCodeEntity = new BaseCodeEntity();
			baseCodeEntity = baseProperty.get(applData.getApplStatus());
			applStatus = baseCodeEntity.getCodeName();
		}

		List<String> titleList = new ArrayList<String>();

		// 共通項目
		titleList.add("appl_status");
		tmpList.add(applStatus); // 申請状況
		titleList.add("appl_No");
		tmpList.add(applData.getApplNo()); // 申請書番号
		titleList.add("appl_date");
		tmpList.add(applDate); // 申請日
		titleList.add("shain_no");
		tmpList.add(applData.getShainNo()); // 社員番号
		titleList.add("name");
		tmpList.add(applData.getName()); // 申請者名
		titleList.add("appl_name");
		tmpList.add(applData.getApplName()); // 申請書類名
		titleList.add("agre_date");
		tmpList.add(agreDate); // 承認/修正依頼日
		titleList.add("agre_name1");
		tmpList.add(applData.getAgreName1()); // 承認者名１/修正依頼社名
		titleList.add("agre_name2");
		tmpList.add(applData.getAgreName2()); // 承認者名２/修正依頼社名

		switch (applId) {
		case "R0100":
			skf2010Sc005SharedService.setTNyukyoChoshoTsuchi(companyCd, applData.getApplNo(), titleList, tmpList);
			break;
		case "R0103":
			skf2010Sc005SharedService.setTTaikyoReport(companyCd, applData.getApplNo(), titleList, tmpList);
			break;
		case "R0104":
			skf2010Sc005SharedService.setTBihinKibouShinsei(companyCd, applData.getApplNo(), titleList, tmpList);
			break;
		case "R0105":
			skf2010Sc005SharedService.setTBihinHenkyakuShinsei(companyCd, applData.getApplNo(), titleList, tmpList);
			break;
		}

		// columnMap.put(applId, titleList.toArray(new
		// String[titleList.size()]));
		return tmpList.toArray(new String[tmpList.size()]);
	}
}
