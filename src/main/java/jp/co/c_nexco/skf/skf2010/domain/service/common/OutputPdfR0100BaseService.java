/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
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
public abstract class OutputPdfR0100BaseService<DTO extends Skf2010OutputPdfBaseDto>
		extends PdfBaseServiceAbstract<DTO> {

	@Value("${skf.pdf.skf2020rp001.pdf_file_name}")
	private String pdfFileName;
	@Value("${skf.pdf.skf2020rp001.pdf_temp_folder_path}")
	private String pdfTempFolderPath;

	/**
	 * 新所属情報を改行して表示するバイト数 PDFデザイナーでは文字枠の上下中央寄せが表現できないため、
	 * 改行を要する場合のみ文字枠に表示するようにする。
	 */
	private static final int AFFILIATION_BREAK_LENGTH = 64;

	/**
	 * ダウンロード時のPDFファイル名を設定する。
	 * 
	 * @return PDFファイル名
	 */
	@Override
	protected String getPdfFileName() {
		return this.pdfFileName;
	}

	/**
	 * 出力するPDFのテンプレートリストを設定する。
	 * 
	 * <pre>
	 * PDF_INFOはテンプレートファイル（iodファイル）のパスを保持している。
	 * リストに複数の要素を設定した場合、設定した順番にPDFを作成し結合する。
	 * </pre>
	 * 
	 * @param dto
	 * @return PDF_INFOのリスト
	 * @see PDF_INFO
	 */
	@Override
	protected List<PDF_INFO> getPdfInfoList(DTO dto) {
		List<PDF_INFO> pdfInfoList = new ArrayList<PDF_INFO>();
		pdfInfoList.add(PDF_INFO.NYUKYO_KIBO_CHOSHO);
		return pdfInfoList;
	}

	/**
	 * PDFの一時出力フォルダパスを設定する。
	 * 
	 * @return PDF一時出力フォルダパス
	 */
	@Override
	protected String getTempFolderPath() {
		return this.pdfTempFolderPath;
	}

	/**
	 * PDF出力サービスで行う画面追加処理を記述する。
	 * 
	 * <pre>
	 * 本メソッドに記述された処理はPDF出力処理完了後に実行される。
	 * 特に行う処理が無い場合は空実装とすること。
	 * </pre>
	 * 
	 * @param dto
	 */
	@Override
	protected void afterIndexProc(DTO dto) {
		/** 特に追加処理が無いため空実装とする */
	}

	/**
	 * PDFに出力するデータを設定する。
	 * 
	 * <pre>
	 * super.pdfDataListには、getPdfInfoList()で指定したPDFの順番で
	 * CSVDocオブジェクトが作成されている。
	 * 要素を取り出してPDFに出力するデータを設定する事。
	 * </pre>
	 * 
	 * @param dto
	 */
	@Override
	protected void setPdfData(DTO dto) {

		List<PDF_INFO> pdfInfoList = this.getPdfInfoList(dto);
		for (int i = 0; i < pdfInfoList.size(); i++) {
			CSVDoc pdfData = super.pdfDataList.get(i);

			// PDFの種類を判別して、対応するデータ設定メソッドを呼び出す
			switch (pdfInfoList.get(i)) {
			case NYUKYO_KIBO_CHOSHO:
				this.setNyukyoKiboChosho(pdfData, dto);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 入居希望等調書PDFに表示するデータを設定する。
	 * 
	 * @param pdfData 入居希望等調書PDFのCSVDoc
	 * @param dto
	 */
	private void setNyukyoKiboChosho(CSVDoc pdfData, DTO dto) {
		pdfData.setData("shainNo", NfwStringUtils.defaultString(dto.getShainNo()));
		pdfData.setData("name", NfwStringUtils.defaultString(dto.getName()));
		pdfData.setData("tokyu", NfwStringUtils.defaultString(dto.getTokyu()));
		pdfData.setData("nowAgency", NfwStringUtils.defaultString(dto.getNowAgency()));
		pdfData.setData("nowAffiliation1", NfwStringUtils.defaultString(dto.getNowAffiliation1()));
		pdfData.setData("nowAffiliation2", NfwStringUtils.defaultString(dto.getNowAffiliation2()));
		pdfData.setData("nowTel", NfwStringUtils.defaultString(dto.getNowTel()));
		pdfData.setData("newAgency", NfwStringUtils.defaultString(dto.getNewAgency()));

		// 新所属1「部・事務所」
		if (NfwStringUtils.defaultString(dto.getNewAffiliation1())
				.getBytes(Charset.forName(PDF_PROCESS_ENCODE)).length <= AFFILIATION_BREAK_LENGTH) {
			pdfData.setData("newAffiliation1", NfwStringUtils.defaultString(dto.getNewAffiliation1()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("newAffiliation1_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNewAffiliation1()));
			pdfData.setTextBoxEnd();
		}
		// 新所属2「課・チーム」
		if (NfwStringUtils.defaultString(dto.getNewAffiliation2())
				.getBytes(Charset.forName(PDF_PROCESS_ENCODE)).length <= AFFILIATION_BREAK_LENGTH) {
			pdfData.setData("newAffiliation2", NfwStringUtils.defaultString(dto.getNewAffiliation2()));
		} else {
			// 64バイトを超える表示を行う場合は改行が必要となるため、文字枠に表示する
			pdfData.setTextBoxStart("newAffiliation2_long");
			pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getNewAffiliation2()));
			pdfData.setTextBoxEnd();
		}

		// チェックボックス_社宅の貸与を必要とするか
		super.setCheckMark(pdfData, "taiyoHitsuyoTrue", dto.getTaiyoHitsuyoTrue());
		super.setCheckMark(pdfData, "taiyoHitsuyoFalse", dto.getTaiyoHitsuyoFalse());
		super.setCheckMark(pdfData, "taikyoHitsuyoParking", dto.getTaiyoHitsuyoParking());

		// チェックボックス_社宅を必要とする理由
		super.setCheckMark(pdfData, "hitsuyoRiyuIdou", dto.getHitsuyoRiyuIdou());
		super.setCheckMark(pdfData, "fuhitsuyoRiyuMyHome", dto.getFuhitsuyoRiyuMyHome());
		super.setCheckMark(pdfData, "hitsuyoRiyuMarry", dto.getHitsuyoRiyuMarry());

		// チェックボックス_社宅を必要としない理由
		super.setCheckMark(pdfData, "fuhitsuyoRiyuSelfRental", dto.getFuhitsuyoRiyuSelfRental());
		super.setCheckMark(pdfData, "hitsuyoRiyuOther", dto.getHitsuyoRiyuOther());
		super.setCheckMark(pdfData, "fuhitsuyoRiyuOther", dto.getFuhitsuyoRiyuOther());

		pdfData.setData("applNo", NfwStringUtils.defaultString(dto.getApplNo()));

		// チェックボックス_必要とする社宅（世帯、単身、独身）
		super.setCheckMark(pdfData, "hitsuyouShatakuFamily", dto.getHitsuyouShatakuFamily());
		super.setCheckMark(pdfData, "hitsuyouShatakuOnly", dto.getHitsuyouShatakuOnly());
		super.setCheckMark(pdfData, "hitsuyouShatakuSingle", dto.getHitsuyouShatakuSingle());

		pdfData.setData("nyukyoYoteiDate", NfwStringUtils.defaultString(dto.getNyukyoYoteiDate()));

		// チェックボックス_自動車の保管場所を必要とするか
		super.setCheckMark(pdfData, "parkingUmuTrue", dto.getParkingUmuTrue());
		super.setCheckMark(pdfData, "parkingUmuFalse", dto.getParkingUmuFalse());

		pdfData.setData("carName", NfwStringUtils.defaultString(dto.getCarName()));
		pdfData.setData("carUser", NfwStringUtils.defaultString(dto.getCarUser()));
		pdfData.setData("carNo", NfwStringUtils.defaultString(dto.getCarNo()));
		pdfData.setData("carExpirationDate", NfwStringUtils.defaultString(dto.getCarExpirationDate()));
		pdfData.setData("parkingUserDate", NfwStringUtils.defaultString(dto.getParkingUserDate()));
		pdfData.setData("carName2", NfwStringUtils.defaultString(dto.getCarName2()));
		pdfData.setData("carUser2", NfwStringUtils.defaultString(dto.getCarUser2()));
		pdfData.setData("carNo2", NfwStringUtils.defaultString(dto.getCarNo2()));
		pdfData.setData("carExpirationDate2", NfwStringUtils.defaultString(dto.getCarExpirationDate2()));
		pdfData.setData("parkingUserDate2", NfwStringUtils.defaultString(dto.getParkingUserDate2()));

		// チェックボックス_現入居社宅（保有、自宅、自己借上、その他）
		super.setCheckMark(pdfData, "nowShatakuPossession", dto.getNowShatakuPossession());
		super.setCheckMark(pdfData, "nowShatakuMyHome", dto.getNowShatakuMyHome());
		super.setCheckMark(pdfData, "nowShatakuSelfRental", dto.getNowShatakuSelfRental());
		super.setCheckMark(pdfData, "nowShatakuOther", dto.getNowShatakuOther());

		pdfData.setData("nowShatakuName", NfwStringUtils.defaultString(dto.getNowShatakuName()));
		pdfData.setData("nowShatakuKikakuName", NfwStringUtils.defaultString(dto.getNowShatakuKikakuName()));
		pdfData.setData("nowShatakuMenseki", NfwStringUtils.defaultString(dto.getNowShatakuMenseki()));
		pdfData.setData("nowShatakuNo", NfwStringUtils.defaultString(dto.getNowShatakuNo()));

		// チェックボックス_退居予定（退居する、継続利用する）
		super.setCheckMark(pdfData, "taikyoYoteiTrue", dto.getTaikyoYoteiTrue());
		super.setCheckMark(pdfData, "taikyoYoteiFalse", dto.getTaikyoYoteiFalse());

		pdfData.setData("taikyoYoteiDate", NfwStringUtils.defaultString(dto.getTaikyoYoteiDate()));

		// 同居家族情報
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

		// 文字枠データの埋め込み
		// 改行を要するデータの場合はこのように記述する
		pdfData.setTextBoxStart("tokushuJijo");
		pdfData.setTextBoxData(NfwStringUtils.defaultString(dto.getTokushuJijo()));
		pdfData.setTextBoxEnd();
	}
}
