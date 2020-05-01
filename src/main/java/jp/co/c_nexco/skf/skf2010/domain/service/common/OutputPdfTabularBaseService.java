/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.common;

import java.util.ArrayList;
import java.util.List;
import jp.co.c_nexco.skf.common.PdfBaseServiceAbstract;
import jp.co.c_nexco.skf.skf2010.domain.dto.common.Skf2010OutputPdfBaseDto;
import jp.co.intra_mart.product.pdfmaker.net.DBDoc;

/**
 * OutputPdfTabularBaseService 表形式PDFの出力処理基盤クラス。
 * 
 * @author NEXCOシステムズ
 */
public abstract class OutputPdfTabularBaseService<DTO extends Skf2010OutputPdfBaseDto>
		extends PdfBaseServiceAbstract<DTO> {

	// 本実装するわけではないのでプロパティ値は使わない
	private String pdfFileName = "表形式テストPDF.pdf";
	private String pdfTempFolderPath = "skf/temp/skf2020/skf2020test/";

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
		pdfInfoList.add(PDF_INFO.TABULAR_TEST);
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
			DBDoc pdfData = super.ddlDataList.get(i);

			// PDFの種類を判別して、対応するデータ設定メソッドを呼び出す
			switch (pdfInfoList.get(i)) {
			case TABULAR_TEST:
				this.setTabularPdf(pdfData, dto);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 表形式PDFに表示するデータを設定する。
	 * 
	 * @param pdfData 表形式PDFのCSVDoc
	 * @param dto
	 */
	private void setTabularPdf(DBDoc pdfData, DTO dto) {
		
		// setGlobal()を行う（getErrorCode()が1になる）
		pdfData.setGlobal("orderto", "社宅チーム");
		pdfData.setGlobal("orderno", "20050808-08");
		pdfData.setGlobal("subject", "PDF出力");
		pdfData.setGlobal("paycond", "御社指定の通り");
		pdfData.setGlobal("deliveryday", "特に指定なし");
		
		pdfData.setGlobal("checkBox1", "✓");
		pdfData.setGlobal("checkBox2", "");
		
		String[]	data = {
				"#品名",				"数量",	"単価",		"備考",
				"社員番号出力テスト",		"1",	"304500",	"社員番号："+dto.getShainNo(),
				"氏名出力テスト",		"2",	"456750",	"氏名："+dto.getName(),
				"申請日出力テスト",		"3",	"514500",	"申請日："+dto.getApplDate(),
				"申請書類ID出力テスト",		"4",	"771750",	"申請書類ID："+dto.getApplId(),
				"申請状況出力テスト",	"5",	"500000",	"申請状況："+dto.getApplStatus(),
				"社宅名(shatakuName)出力テスト",	"10",	"750000",	"社宅名："+dto.getShatakuName(),
				"社宅名(taikyoArea)出力テスト",			"100",	"4500",		"社宅名："+dto.getTaikyoArea(),
				"PDFコンバータ 年間保守",		"100",	"67500",	"備考3",
				"PDF製本工房 V1.5",			"10",	"17850",	"",
				"PDF製本工房 年間保守",			"10",	"26775",	"",
					};
		int count = 0;
		
		// setCol()を行う（getErrorCode()が0になる）
		// なのでsetCol()の後にsetGlobal()を行うとエラーになる
		for(int i = 0; i < (data.length/4); i++) {
			// データ行の開始 
			pdfData.setColStart();

			// 品名データを設定 
			pdfData.setCol(data[count++]);

			// 数量データを設定 
			pdfData.setCol(data[count++]);

			// 単価データを設定 
			pdfData.setCol(data[count++]);

			// 備考データを設定 
			pdfData.setCol(data[count++]);

			// データ行の終わり 
			pdfData.setColEnd();
			
		}
	}
}
