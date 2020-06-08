package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc005.Skf2010Sc005GetShoninIchiranShoninExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfBihinInfoUtils.SkfBihinInfoUtilsGetBihinInfoExp;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput.BeanOutputCsv;
import jp.co.c_nexco.nfw.webcore.utils.filetransfer.FileOutput.OutputFileCsvProperties;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SessionCacheKeyConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfBihinInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005DownloadDto;

/**
 * Skf2010Sc005 承認一覧CSV出力処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005DownloadService extends SkfServiceAbstract<Skf2010Sc005DownloadDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	@Autowired
	private SkfBihinInfoUtils skfBihinInfoUtils;

	private Map<String, String> applStatusMap;

	private String templateFilePropertyKeyR0100 = "skf2010.skf2010_sc005.r0100_csv";
	private String templateFilePropertyKeyR0103 = "skf2010.skf2010_sc005.r0103_csv";
	private String templateFilePropertyKeyR0104 = "skf2010.skf2010_sc005.r0104_csv";
	private String templateFilePropertyKeyR0105 = "skf2010.skf2010_sc005.r0105_csv";

	private final String FILE_TEMPLETE_FUNCTION_CD = "skf2010fl001";

	@Value("${skf.common.company_cd}")
	private String companyCd;
	@Value("${skf2010.skf2010_sc005.search_max_count}")
	private String searchMaxCount;
	@Value("${skf2010.skf2010_sc005.appl_data_zip}")
	private String outputZipFileName;

	@SuppressWarnings("unchecked")
	@Override
	public BaseDto index(Skf2010Sc005DownloadDto dlDto) throws Exception {
		// 操作ログ出力
		skfOperationLogUtils.setAccessLog("承認一覧をCSV出力する", CodeConstant.C001, FunctionIdConstant.SKF2010_SC005);
		// 汎用コード取得
		applStatusMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_STATUS);

		// 出力情報取得
		List<Skf2010Sc005GetShoninIchiranShoninExp> resultList = new ArrayList<Skf2010Sc005GetShoninIchiranShoninExp>();
		resultList = (List<Skf2010Sc005GetShoninIchiranShoninExp>) menuScopeSessionBean
				.get(SessionCacheKeyConstant.SKF2010SC005_SEARCH_RESULT_SESSION_KEY);

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
		// 備品申請
		List<SkfBihinInfoUtilsGetBihinInfoExp> bihinInfoList = new ArrayList<SkfBihinInfoUtilsGetBihinInfoExp>();

		// 汎用コードマスタより名称一覧を取得する
		// 性別
		Map<String, String> genderMap = new HashMap<String, String>();
		genderMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_GENDER);
		skf2010Sc005SharedService.setChangeWordMap("gender", genderMap);
		// 規格区分
		Map<String, String> kikakuKbnMap = new HashMap<String, String>();
		kikakuKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_KIKAKU_KBN);
		skf2010Sc005SharedService.setChangeWordMap("kikakuKbn", kikakuKbnMap);
		// 希望時間
		Map<String, String> kiboTimeMap = new HashMap<String, String>();
		kiboTimeMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_REQUEST_TIME);
		skf2010Sc005SharedService.setChangeWordMap("requestTime", kiboTimeMap);
		// 申請区分
		Map<String, String> applKbnMap = new HashMap<String, String>();
		applKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_APPL_KBN);
		skf2010Sc005SharedService.setChangeWordMap("applKbn", applKbnMap);
		// 状態区分
		Map<String, String> jotaiKbnMap = new HashMap<String, String>();
		jotaiKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_EQUIPMENT_STATE);
		skf2010Sc005SharedService.setChangeWordMap("jotaiKbn", jotaiKbnMap);
		// 調整区分（搬入区分）
		Map<String, String> hannyuKbnMap = new HashMap<String, String>();
		hannyuKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_CARRING_IN_KBN);
		skf2010Sc005SharedService.setChangeWordMap("hannyuKbn", hannyuKbnMap);
		// 調整区分（搬出区分）
		Map<String, String> hanshutsuKbnMap = new HashMap<String, String>();
		hanshutsuKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_CARRING_OUT_KBN);
		skf2010Sc005SharedService.setChangeWordMap("hanshutsuKbn", hanshutsuKbnMap);
		// 希望可否区分
		Map<String, String> wishKbnMap = new HashMap<String, String>();
		wishKbnMap = skfGenericCodeUtils.getGenericCode(FunctionIdConstant.GENERIC_CODE_BIHIN_HOPE);
		skf2010Sc005SharedService.setChangeWordMap("wishKbn", wishKbnMap);

		String[] rowData = {};

		// 各書類毎のCSVファイルデータを作成
		for (Skf2010Sc005GetShoninIchiranShoninExp applData : resultList) {

			switch (applData.getApplId()) {
			case "R0100":
				rowData = setApplDataForCsv(applData, applData.getApplId(), null);
				r0100List.add(rowData);
				break;
			case "R0103":
				rowData = setApplDataForCsv(applData, applData.getApplId(), null);
				r0103List.add(rowData);
				break;
			case "R0104":
				bihinInfoList = skfBihinInfoUtils.getBihinInfo(companyCd, applData.getApplNo());
				if (bihinInfoList != null && bihinInfoList.size() > 0) {
					for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinInfoList) {
						rowData = setApplDataForCsv(applData, applData.getApplId(), bihinInfo);
						r0104List.add(rowData);
					}
				}

				break;
			case "R0105":
				bihinInfoList = skfBihinInfoUtils.getBihinInfo(companyCd, applData.getApplNo());
				if (bihinInfoList != null && bihinInfoList.size() > 0) {
					for (SkfBihinInfoUtilsGetBihinInfoExp bihinInfo : bihinInfoList) {
						rowData = setApplDataForCsv(applData, applData.getApplId(), bihinInfo);
						r0105List.add(rowData);
					}
				}

				break;
			}
		}

		// 各書類毎のオブジェクトに格納する
		BeanOutputCsv beanOutputCsvR0100 = new FileOutput().new BeanOutputCsv(r0100List, templateFilePropertyKeyR0100,
				FILE_TEMPLETE_FUNCTION_CD, 2, null);
		BeanOutputCsv beanOutputCsvR0103 = new FileOutput().new BeanOutputCsv(r0103List, templateFilePropertyKeyR0103,
				FILE_TEMPLETE_FUNCTION_CD, 2, null);
		BeanOutputCsv beanOutputCsvR0104 = new FileOutput().new BeanOutputCsv(r0104List, templateFilePropertyKeyR0104,
				FILE_TEMPLETE_FUNCTION_CD, 2, null);
		BeanOutputCsv beanOutputCsvR0105 = new FileOutput().new BeanOutputCsv(r0105List, templateFilePropertyKeyR0105,
				FILE_TEMPLETE_FUNCTION_CD, 2, null);
		List<BeanOutputCsv> beanOutputCsvList = new ArrayList<BeanOutputCsv>();

		/** データがある書類のみ作成 */
		if (beanOutputCsvR0100.getCsvDataList().size() > 0) {
			beanOutputCsvList.add(beanOutputCsvR0100);
		}
		if (beanOutputCsvR0103.getCsvDataList().size() > 0) {
			beanOutputCsvList.add(beanOutputCsvR0103);
		}
		if (beanOutputCsvR0104.getCsvDataList().size() > 0) {
			beanOutputCsvList.add(beanOutputCsvR0104);
		}
		if (beanOutputCsvR0105.getCsvDataList().size() > 0) {
			beanOutputCsvList.add(beanOutputCsvR0105);
		}
		// CSVファイルを作成し、Zipファイルに圧縮する
		String fileName = skfDateFormatUtils.dateFormatFromDate(new Date(), SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT)
				+ "_承認一覧.zip";
		OutputFileCsvProperties properties = new FileOutput().new OutputFileCsvProperties(
				FileOutput.LineSeparatorType.LINE_SEPARATOR_CRLF, FileOutput.FileEncode.BOM_UTF8,
				FileOutput.DelimiterType.COMMA, FileOutput.QuoteType.DOUBLE_QUOTATION);
		FileOutput.fileOutputCsvForZip(beanOutputCsvList, dlDto, fileName, properties);

		return dlDto;
	}

	/**
	 * CSV用各種申請書データをセットします
	 * 
	 * @param applData
	 * @param applId
	 * @param columnMap
	 * @return
	 */
	private String[] setApplDataForCsv(Skf2010Sc005GetShoninIchiranShoninExp applData, String applId,
			SkfBihinInfoUtilsGetBihinInfoExp bihinInfo) {
		List<String> tmpList = new ArrayList<String>();

		// 日付型を文字列型に変更する
		String applDate = CodeConstant.NONE;
		if (applData.getApplDate() != null) {
			applDate = skfDateFormatUtils.dateFormatFromDate(applData.getApplDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		}
		String agreDate = CodeConstant.NONE;
		if (applData.getAgreDate() != null) {
			agreDate = skfDateFormatUtils.dateFormatFromDate(applData.getAgreDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);
		}

		// 申請状況をコードから汎用コードに変更
		String applStatus = CodeConstant.NONE;
		if (applData.getApplStatus() != null) {
			applStatus = applStatusMap.get(applData.getApplStatus());
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
			// 備品申請件数分行を作成
			skf2010Sc005SharedService.setTBihinKibouShinsei(companyCd, applData.getApplNo(), bihinInfo, titleList,
					tmpList);
			break;
		case "R0105":
			// 備品申請件数分行を作成
			skf2010Sc005SharedService.setTBihinHenkyakuShinsei(companyCd, applData.getApplNo(), bihinInfo, titleList,
					tmpList);
			break;
		}

		// columnMap.put(applId, titleList.toArray(new
		// String[titleList.size()]));
		return tmpList.toArray(new String[tmpList.size()]);
	}
}
