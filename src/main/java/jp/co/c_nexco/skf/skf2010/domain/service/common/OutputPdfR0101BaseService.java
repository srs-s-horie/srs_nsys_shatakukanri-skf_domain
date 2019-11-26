package jp.co.c_nexco.skf.skf2010.domain.service.common;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.skf.common.PdfBaseServiceAbstract;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010OutputPdfBaseDto;
import jp.co.intra_mart.product.pdfmaker.net.CSVDoc;

/**
 * PdfBaseServiceAbstract PDF出力処理の基盤クラス。
 * 
 * @author NEXCOシステムズ
 */

public abstract class OutputPdfR0101BaseService<DTO extends Skf2010OutputPdfBaseDto>
		extends PdfBaseServiceAbstract<DTO> {

	@Value("${skf.pdf.skf2020rp002.pdf_file_name}")
	private String pdfFileName;
	@Value("${skf.pdf.skf2020rp002.pdf_temp_folder_path}")
	private String pdfTempFolderPath;
	
	private static final int AGECNY_BREAK_LENGTH = 32;
	private static final int CAR_NAME = 32;
	private static final int KYOEKIHI = 7;

	@Override
	protected String getPdfFileName() {
		return this.pdfFileName;
	}

	@Override
	protected List<PDF_INFO> getPdfInfoList(DTO dto) {
		List<PDF_INFO> pdfInfoList = new ArrayList<PDF_INFO>();
		pdfInfoList.add(PDF_INFO.TAIYO_SHATAKU_ANNAI);
		pdfInfoList.add(PDF_INFO.SEIYAKUSHO);
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
			case TAIYO_SHATAKU_ANNAI:
			case SEIYAKUSHO:
				this.setTaiyoAnnaiChosho(pdfData, dto);
				this.setSeiyakusho(pdfData, dto);
				break;
			default:
				break;
			}
		}
	}

	private void setTaiyoAnnaiChosho(CSVDoc pdfData, DTO dto) {
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
			pdfData.setData("newKyoekihi", NfwStringUtils.defaultString(dto.getNewKyoekihi()));
		} else {
			// 後日お知らせを表示する
			pdfData.setData("newKyoekihi_long", NfwStringUtils.defaultString(dto.getNewKyoekihi()));
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
		pdfData.setData("carIchiNo", NfwStringUtils.defaultString(dto.getCarIchiNo()));
		pdfData.setData("carNo", NfwStringUtils.defaultString(dto.getCarNo()));
		pdfData.setData("parkingRental", NfwStringUtils.defaultString(dto.getParkingRental()));
		pdfData.setData("carUser", NfwStringUtils.defaultString(dto.getCarUser()));
		pdfData.setData("carExpirationDate", NfwStringUtils.defaultString(dto.getCarExpirationDate()));
		pdfData.setData("parkingArea2", NfwStringUtils.defaultString(dto.getParkingArea2()));
		// 自動車の車名2
		if (NfwStringUtils.defaultString(dto.getCarName2())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= CAR_NAME) {
			pdfData.setData("carName2", NfwStringUtils.defaultString(dto.getCarName2()));
		} else {
			// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("carName2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getCarName2()));
			pdfData.setTextBoxEnd();
		}
		pdfData.setData("carIchiNo2", NfwStringUtils.defaultString(dto.getCarIchiNo2()));
		pdfData.setData("carNo2", NfwStringUtils.defaultString(dto.getCarNo2()));
		pdfData.setData("parkingRental2", NfwStringUtils.defaultString(dto.getParkingRental2()));
		pdfData.setData("carUser2", NfwStringUtils.defaultString(dto.getCarUser2()));
		pdfData.setData("carExpirationDate2", NfwStringUtils.defaultString(dto.getCarExpirationDate2()));
		pdfData.setData("nyukyoYoteiDate", NfwStringUtils.defaultString(dto.getNyukyoYoteiDate()));
		pdfData.setData("parkingKanoDate", NfwStringUtils.defaultString(dto.getParkingKanoDate()));
		pdfData.setData("parkingKanoDate2", NfwStringUtils.defaultString(dto.getParkingKanoDate2()));

	}
	
	private void setSeiyakusho(CSVDoc pdfData, DTO dto) {
		pdfData.setData("tsuchiDate", NfwStringUtils.defaultString(dto.getTsuchiDate()));
		// 現所属　「機関」
		if (NfwStringUtils.defaultString(dto.getNowAgency())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AGECNY_BREAK_LENGTH) {
			pdfData.setData("nowAgencyS", NfwStringUtils.defaultString(dto.getNowAgency()));
		} else {
			// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAgency_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAgency()));
			pdfData.setTextBoxEnd();
		}
		// 現所属　「部等」
		if (NfwStringUtils.defaultString(dto.getNowAffiliation1())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AGECNY_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation1S", NfwStringUtils.defaultString(dto.getNowAffiliation1()));
		} else {
			// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation1_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation1()));
			pdfData.setTextBoxEnd();
		}
		// 現所属　「室、チーム又は課」
		if (NfwStringUtils.defaultString(dto.getNowAffiliation2())
				.getBytes(Charset.forName(STR_BYTE_LENGTH_ENCODE)).length <= AGECNY_BREAK_LENGTH) {
			pdfData.setData("nowAffiliation2S", NfwStringUtils.defaultString(dto.getNowAffiliation2()));
		} else {
			// 32バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("nowAffiliation2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNowAffiliation2()));
			pdfData.setTextBoxEnd();
		}
		pdfData.setData("name", NfwStringUtils.defaultString(dto.getName()));
	}

}
