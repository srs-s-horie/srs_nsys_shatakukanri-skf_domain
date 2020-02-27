package jp.co.c_nexco.skf.skf2040.domain.service.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.PdfBaseServiceAbstract;
import jp.co.c_nexco.skf.skf2040.domain.dto.common.Skf2040OutputPdfBaseDto;
import jp.co.intra_mart.product.pdfmaker.net.CSVDoc;

/**
 * PdfBaseServiceAbstract PDF出力処理の基盤クラス。
 * 
 * @author NEXCOシステムズ
 */

public abstract class OutputPdfR0103BaseService<DTO extends Skf2040OutputPdfBaseDto>
		extends PdfBaseServiceAbstract<DTO> {

	@Value("${skf.pdf.skf2040rp001.pdf_file_name}")
	private String pdfFileName;
	@Value("${skf.pdf.skf2040rp001.pdf_temp_folder_path}")
	private String pdfTempFolderPath;
	
	private static final int AGENCY_BREAK_LENGTH = 26;
	private static final int AFFILIATION1_BREAK_LENGTH = 26;
	private static final int AFFILIAITON2_BREAK_LENGTH = 26;
	private static final int ADDRESS_BREAK_LENGTH = 26;
	private static final int TAIKYO_AREA_BREAK_LRNGTH = 64;
	private static final int PARKING_AREA_BREAK_LENGTH = 64;
	private static final int TAIKYOGO_RIYU_BREAK_LENGTH = 64;
	private static final int TAIKYOGO_RENRAKUSAKI_BREAK_LENGTH = 64;		
	
	@Override
	protected String getPdfFileName() {
		return this.pdfFileName;
	}

	@Override
	protected List<PDF_INFO> getPdfInfoList(DTO dto) {
		List<PDF_INFO> pdfInfoList = new ArrayList<PDF_INFO>();
		if (dto.getShatakuTaikyoKbn().equals("3")) {
			pdfInfoList.add(PDF_INFO.TAIKYO_HENKAN_TODOKE_PARKING_ONLY);
		} else if (dto.getShatakuTaikyoKbn().equals("2")) {
			pdfInfoList.add(PDF_INFO.TAIKYO_HENKAN_TODOKE_SHATAKU_ONLY);
		} else {
			pdfInfoList.add(PDF_INFO.TAIKYO_HENKAN_TODOKE);
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
			case TAIKYO_HENKAN_TODOKE:
				this.setTaikyoTodoke(pdfData, dto);
				break;
			case TAIKYO_HENKAN_TODOKE_PARKING_ONLY:
				this.setTaikyoTodoke(pdfData, dto);
				break;
			case TAIKYO_HENKAN_TODOKE_SHATAKU_ONLY:
				this.setTaikyoTodoke(pdfData, dto);
				break;
			default:
				break;
			}
		}
	}

	private void setTaikyoTodoke(CSVDoc pdfData, DTO dto) {
		pdfData.setData("applNo", NfwStringUtils.defaultString(dto.getApplNo()));
		pdfData.setData("applDate", NfwStringUtils.defaultString(dto.getApplDate()));
		
		// 現所属　機関
		if (NfwStringUtils.defaultString(dto.getNowAgency())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AGENCY_BREAK_LENGTH) {
			pdfData.setData("nowAgency", NfwStringUtils.defaultString(dto.getNowAgency()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAgency_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAgency()));
			pdfData.setTextBoxEnd();
		}
		
		// 現所属　部等
		if (NfwStringUtils.defaultString(dto.getNowAffiliation1())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AFFILIATION1_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation1", NfwStringUtils.defaultString(dto.getNowAffiliation1()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation1_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation1()));
			pdfData.setTextBoxEnd();
		}
		
		// 現所属　室、チーム又は課
		if (NfwStringUtils.defaultString(dto.getNowAffiliation2())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AFFILIAITON2_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation2", NfwStringUtils.defaultString(dto.getNowAffiliation2()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation2()));
			pdfData.setTextBoxEnd();
		}
		
		// 現住所
		if (NfwStringUtils.defaultString(dto.getAddress())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= ADDRESS_BREAK_LENGTH) {
			pdfData.setData("address", NfwStringUtils.defaultString(dto.getAddress()));
		} else {
			// 26バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("address_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getAddress()));
			pdfData.setTextBoxEnd();
		}
		pdfData.setData("name", NfwStringUtils.defaultString(dto.getName()));
		
		// 社宅
		if (NfwStringUtils.defaultString(dto.getTaikyoArea())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= TAIKYO_AREA_BREAK_LRNGTH) {
			pdfData.setData("taikyoArea", NfwStringUtils.defaultString(dto.getTaikyoArea()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("taikyoArea_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getTaikyoArea()));
			pdfData.setTextBoxEnd();
		}
		
		// 駐車場1
		if (NfwStringUtils.defaultString(dto.getParkingAddress1())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= PARKING_AREA_BREAK_LENGTH) {
			pdfData.setData("parkingAddress1", NfwStringUtils.defaultString(dto.getParkingAddress1()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("parkingAddress1_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getParkingAddress1()));
			pdfData.setTextBoxEnd();
		}
		
		// 駐車場2
		if (NfwStringUtils.defaultString(dto.getParkingAddress2())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= PARKING_AREA_BREAK_LENGTH) {
			pdfData.setData("parkingAddress2", NfwStringUtils.defaultString(dto.getParkingAddress2()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("parkingAddress2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getParkingAddress2()));
			pdfData.setTextBoxEnd();
		}
		pdfData.setData("taikyoDate", NfwStringUtils.defaultString(dto.getTaikyoDate()));
		pdfData.setData("parkingHenkanDate", NfwStringUtils.defaultString(dto.getParkingHenkanDate()));
		
		// 退居（返還）理由
		if (NfwStringUtils.defaultString(dto.getTaikyoRiyu())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= TAIKYOGO_RIYU_BREAK_LENGTH) {
			pdfData.setData("taikyoRiyu", NfwStringUtils.defaultString(dto.getTaikyoRiyu()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("taikyoRiyu_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getTaikyoRiyu()));
			pdfData.setTextBoxEnd();
		}
		
		// 退居後連絡先
		if (NfwStringUtils.defaultString(dto.getTaikyogoRenrakusaki())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= TAIKYOGO_RENRAKUSAKI_BREAK_LENGTH) {
			pdfData.setData("taikyogoRenrakusaki", NfwStringUtils.defaultString(dto.getTaikyogoRenrakusaki()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("taikyogoRenrakusaki_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getTaikyogoRenrakusaki()));
			pdfData.setTextBoxEnd();
		}
	
	}
	
	
}
