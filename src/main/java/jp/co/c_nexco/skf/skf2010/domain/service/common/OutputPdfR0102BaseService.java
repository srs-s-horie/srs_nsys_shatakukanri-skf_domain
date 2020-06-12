package jp.co.c_nexco.skf.skf2010.domain.service.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.PdfBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010OutputPdfBaseDto;
import jp.co.intra_mart.product.pdfmaker.net.CSVDoc;

/**
 * PdfBaseServiceAbstract PDF出力処理の基盤クラス。
 * 
 * @author NEXCOシステムズ
 */

public abstract class OutputPdfR0102BaseService<DTO extends Skf2010OutputPdfBaseDto>
		extends PdfBaseServiceAbstract<DTO> {

	@Autowired
	private SkfLoginUserInfoUtils skfLoginUserInfoUtils;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;

	@Value("${skf.pdf.skf2020rp003.pdf_file_name}")
	private String pdfFileName;
	@Value("${skf.pdf.skf2020rp003.pdf_temp_folder_path}")
	private String pdfTempFolderPath;

	private static final int KYOEKIHI = 7;
	private static final int AGENCY_BREAK_LENGTH = 32;
	private static final int AFFILIATION1_BREAK_LENGTH = 32;
	private static final int AFFILIATION2_BREAK_LENGTH = 32;
	private static final int CAR_NAME = 32;
	private static final int CAR_USER = 32;

	@Override
	protected String getPdfFileName() {
		return this.pdfFileName;
	}

	@Override
	protected List<PDF_INFO> getPdfInfoList(DTO dto) {
		List<PDF_INFO> pdfInfoList = new ArrayList<PDF_INFO>();
		Map<String, String> loginUser = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String roleId = loginUser.get("roleId");
		if (roleId.equals(CodeConstant.SHINSEISHA)) {
			pdfInfoList.add(PDF_INFO.NYUKYO_KETTEI_TSUCHI);
			if (dto.getCarNoInputFlg2() != null) {
				pdfInfoList.add(PDF_INFO.NYUKYO_KETTEI_TSUCHI);
			}
		} else {
			pdfInfoList.add(PDF_INFO.NYUKYO_KETTEI_TSUCHI_SHATAKU_KANRI_NO_ARI);
			if (dto.getCarNoInputFlg2() != null) {
				pdfInfoList.add(PDF_INFO.NYUKYO_KETTEI_TSUCHI_SHATAKU_KANRI_NO_ARI);
			}
		}
		return pdfInfoList;
	}

	@Override
	protected String getTempFolderPath() {
		return this.pdfTempFolderPath;
	}

	@Override
	protected void afterIndexProc(DTO dto) {
		/** 特に追加処理が無いため空実装とする */

	}

	@Override
	protected void setPdfData(DTO dto) {
		List<PDF_INFO> pdfInfoList = this.getPdfInfoList(dto);
		for (int i = 0; i < pdfInfoList.size(); i++) {
			CSVDoc pdfData = super.pdfDataList.get(i);

			// PDFの種類を判別して、対応するデータ設定メソッドを呼び出す
			switch (pdfInfoList.get(i)) {
			case NYUKYO_KETTEI_TSUCHI:
				this.setNyukyoKetteiTsuchi(pdfData, dto, i);
				break;
			case NYUKYO_KETTEI_TSUCHI_SHATAKU_KANRI_NO_ARI:
				this.setNyukyoKetteiTsuchi(pdfData, dto, i);
				break;
			default:
				break;
			}
		}
	}

	// 入居等決定通知書PDFのマッピング
	private void setNyukyoKetteiTsuchi(CSVDoc pdfData, DTO dto, int i) {
		pdfData.setData("applNo", NfwStringUtils.defaultString(dto.getApplNo()));
		pdfData.setData("nowAgency", NfwStringUtils.defaultString(dto.getNowAgency()));
		pdfData.setData("nowAffiliation1", NfwStringUtils.defaultString(dto.getNowAffiliation1()));
		pdfData.setData("nowAffiliation2", NfwStringUtils.defaultString(dto.getNowAffiliation2()));
		pdfData.setData("name", NfwStringUtils.defaultString(dto.getName()));
		pdfData.setData("applDate", NfwStringUtils.defaultString(dto.getApplDate()));
		pdfData.setData("hitsuyoRiyu", NfwStringUtils.defaultString(dto.getHitsuyoRiyu()));
		pdfData.setData("newShozaichi", NfwStringUtils.defaultString(dto.getNewShozaichi()));
		pdfData.setData("newShatakuName", NfwStringUtils.defaultString(dto.getNewShatakuName()));
		pdfData.setData("newShatakuNo", NfwStringUtils.defaultString(dto.getNewShatakuNo()));
		pdfData.setData("newShatakuKikaku", NfwStringUtils.defaultString(dto.getNewShatakuKikaku()));
		pdfData.setData("newShatakuMenseki", NfwStringUtils.defaultString(dto.getNewShatakuMenseki()));
		pdfData.setData("newRental", NfwStringUtils.defaultString(dto.getNewRental()));
		// 共益費
		if (NfwStringUtils.defaultString(dto.getNewKyoekihi())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= KYOEKIHI) {
			pdfData.setData("newKyoekihi", NfwStringUtils.defaultString(dto.getKetteiKyoekihi()));
		} else {
			// 後日お知らせを表示する
			pdfData.setData("newKyoekihi_long", NfwStringUtils.defaultString(dto.getKetteiKyoekihi()));
		}
		pdfData.setData("dokyoRelation1", NfwStringUtils.defaultString(dto.getDokyoRelation1()));
		pdfData.setData("dokyoName1", NfwStringUtils.defaultString(dto.getDokyoName1()));
		pdfData.setData("dokyoAge1", NfwStringUtils.defaultString(dto.getDokyoAge1()));
		pdfData.setData("dokyoRelation2", NfwStringUtils.defaultString(dto.getDokyoRelation2()));
		pdfData.setData("dokyoName2", NfwStringUtils.defaultString(dto.getDokyoName2()));
		pdfData.setData("dokyoAge2", NfwStringUtils.defaultString(dto.getDokyoAge2()));
		pdfData.setData("dokyoRelation3", NfwStringUtils.defaultString(dto.getDokyoRelation3()));
		pdfData.setData("dokyoName3", NfwStringUtils.defaultString(dto.getDokyoName3()));
		pdfData.setData("dokyoAge3", NfwStringUtils.defaultString(dto.getDokyoAge3()));
		pdfData.setData("dokyoRelation4", NfwStringUtils.defaultString(dto.getDokyoRelation4()));
		pdfData.setData("dokyoName4", NfwStringUtils.defaultString(dto.getDokyoName4()));
		pdfData.setData("dokyoAge4", NfwStringUtils.defaultString(dto.getDokyoAge4()));
		pdfData.setData("dokyoRelation5", NfwStringUtils.defaultString(dto.getDokyoRelation5()));
		pdfData.setData("dokyoName5", NfwStringUtils.defaultString(dto.getDokyoName5()));
		pdfData.setData("dokyoAge5", NfwStringUtils.defaultString(dto.getDokyoAge5()));
		pdfData.setData("dokyoRelation6", NfwStringUtils.defaultString(dto.getDokyoRelation6()));
		pdfData.setData("dokyoName6", NfwStringUtils.defaultString(dto.getDokyoName6()));
		pdfData.setData("dokyoAge6", NfwStringUtils.defaultString(dto.getDokyoAge6()));

		// 自動車1台目
		if (i == 0) {
			pdfData.setData("parkingArea", NfwStringUtils.defaultString(dto.getParkingArea()));
			// 自動車の車名
			if (NfwStringUtils.defaultString(dto.getCarName())
					.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= CAR_NAME) {
				pdfData.setData("carName", NfwStringUtils.defaultString(dto.getCarName()));
			} else {
				// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
				pdfData.setTextBoxStart("carName_long");
				pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getCarName()));
				pdfData.setTextBoxEnd();
			}
			pdfData.setData("carNo", NfwStringUtils.defaultString(dto.getCarNo()));
			// 自動車の使用者
			if (NfwStringUtils.defaultString(dto.getCarUser())
					.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= CAR_USER) {
				pdfData.setData("carUser", NfwStringUtils.defaultString(dto.getCarUser()));
			} else {
				// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
				pdfData.setTextBoxStart("carUser_long");
				pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getCarUser()));
				pdfData.setTextBoxEnd();
			}
			pdfData.setData("carExpirationDate", NfwStringUtils.defaultString(dto.getCarExpirationDate()));
			pdfData.setData("carIchiNo", CodeConstant.NONE);
			pdfData.setData("parkingRental", NfwStringUtils.defaultString(dto.getParkingRental()));
			if (NfwStringUtils.isNotEmpty(dto.getParkingSDateFlg())
					&& (SkfCommonConstant.DATE_CHANGE.equals(dto.getParkingSDateFlg())
							|| SkfCommonConstant.DATE_CHANGE_COM.equals(dto.getParkingSDateFlg()))) {
				// 駐車場使用開始日変更フラグが設定されており、かつ2台目に変更あり、または両方変更ありの場合は赤文字で表示
				pdfData.setData("parkingKanoDateRed", NfwStringUtils.defaultString(dto.getParkingKanoDate()));
			} else {
				pdfData.setData("parkingKanoDate", NfwStringUtils.defaultString(dto.getParkingKanoDate()));
			}
		}

		// 自動車2台目
		if (i == 1) {
			pdfData.setData("parkingArea", NfwStringUtils.defaultString(dto.getParkingArea2()));
			// 自動車の車名2
			if (NfwStringUtils.defaultString(dto.getCarName2())
					.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= CAR_NAME) {
				pdfData.setData("carName", NfwStringUtils.defaultString(dto.getCarName2()));
			} else {
				// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
				pdfData.setTextBoxStart("carName_long");
				pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getCarName2()));
				pdfData.setTextBoxEnd();
			}
			pdfData.setData("carNo", NfwStringUtils.defaultString(dto.getCarNo2()));
			// 自動車の使用者2
			if (NfwStringUtils.defaultString(dto.getCarUser2())
					.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= CAR_USER) {
				pdfData.setData("carUser", NfwStringUtils.defaultString(dto.getCarUser2()));
			} else {
				// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
				pdfData.setTextBoxStart("carUser_long");
				pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getCarUser2()));
				pdfData.setTextBoxEnd();
			}
			pdfData.setData("carExpirationDate", NfwStringUtils.defaultString(dto.getCarExpirationDate2()));
			pdfData.setData("carIchiNo", CodeConstant.NONE);
			pdfData.setData("parkingRental", NfwStringUtils.defaultString(dto.getParkingRental2()));
			if (NfwStringUtils.isNotEmpty(dto.getParkingSDateFlg())
					&& (SkfCommonConstant.DATE_CHANGE2.equals(dto.getParkingSDateFlg())
							|| SkfCommonConstant.DATE_CHANGE_COM.equals(dto.getParkingSDateFlg()))) {
				// 駐車場使用開始日変更フラグが設定されており、かつ2台目に変更あり、または両方変更ありの場合は赤文字で表示
				pdfData.setData("parkingKanoDateRed", NfwStringUtils.defaultString(dto.getParkingKanoDate2()));
			} else {
				pdfData.setData("parkingKanoDate", NfwStringUtils.defaultString(dto.getParkingKanoDate2()));
			}
		}

		if (NfwStringUtils.isNotEmpty(dto.getNyukyoDateFlg())
				&& SkfCommonConstant.DATE_CHANGE.equals(dto.getNyukyoDateFlg())) {
			// 入居日変更フラグが設定されていた場合は赤文字で表示
			pdfData.setData("nyukyoKanoDateRed", NfwStringUtils.defaultString(dto.getNyukyoKanoDate()));
		} else {
			pdfData.setData("nyukyoKanoDate", NfwStringUtils.defaultString(dto.getNyukyoKanoDate()));
		}

		// 社宅管理番号
		String newShatakuKanriNo = "";
		Skf2020TNyukyoChoshoTsuchi data = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey param = new Skf2020TNyukyoChoshoTsuchiKey();
		param.setCompanyCd("C001");
		param.setApplNo(dto.getApplNo());
		data = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(param);
		if (data != null) {
			newShatakuKanriNo = data.getNewShatakuKanriNo();
		}
		Map<String, String> loginUser = skfLoginUserInfoUtils.getSkfLoginUserInfo();
		String roleId = loginUser.get("roleId");
		if (!roleId.equals(CodeConstant.SHINSEISHA)) {
			pdfData.setData("newShatakuKanriNo", NfwStringUtils.defaultString(newShatakuKanriNo));
		}

		/** 誓約書 */
		pdfData.setData("seiyakuDate", NfwStringUtils.defaultString(dto.getSeiyakuDate()));
		// 現所属 機関
		if (NfwStringUtils.defaultString(dto.getNowAgency())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AGENCY_BREAK_LENGTH) {
			pdfData.setData("nowAgencyS", NfwStringUtils.defaultString(dto.getNowAgency()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAgency_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAgency()));
			pdfData.setTextBoxEnd();
		}
		// 現所属 部等
		if (NfwStringUtils.defaultString(dto.getNowAffiliation1())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AFFILIATION1_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation1S", NfwStringUtils.defaultString(dto.getNowAffiliation1()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation1_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation1()));
			pdfData.setTextBoxEnd();
		}
		// 現所属 室・課等
		if (NfwStringUtils.defaultString(dto.getNowAffiliation2())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AFFILIATION2_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation2S", NfwStringUtils.defaultString(dto.getNowAffiliation2()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation2()));
			pdfData.setTextBoxEnd();
		}

	}

}
