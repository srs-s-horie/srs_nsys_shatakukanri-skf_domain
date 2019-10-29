/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.core.constants.CommonConstant;
import jp.co.c_nexco.nfw.webcore.domain.model.FileDownloadDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.intra_mart.foundation.service.client.file.PublicStorage;
import jp.co.intra_mart.product.pdfmaker.PDFException;
import jp.co.intra_mart.product.pdfmaker.net.CSVDoc;
import jp.co.intra_mart.product.pdfmaker.net.IOIntegration;

/**
 * PdfBaseServiceAbstract PDF出力処理の基盤クラス。
 * 
 * @author NEXCOシステムズ
 */
public abstract class PdfBaseServiceAbstract<DTO extends FileDownloadDto> extends BaseServiceAbstract<DTO> {
    
    protected IOIntegration integratePdf;
    protected List<CSVDoc> pdfDataList;
    
    /**
     * 中間処理ファイル（IOD,DAT）生成に使用する文字エンコード （「✓」のような特殊記号を表示するため、UTF-16に設定）
     */
    //private static final String PDF_PROCESS_ENCODE = "UTF-16";
    private static final String PDF_PROCESS_ENCODE = "MS932";
    /** チェック済のチェックボックスに表示するマーク */
    public static final String CHECK_MARK = "■";
    /** ファイル拡張子：PDF */
    public static final String FILE_EXTENTION_PDF = ".pdf";
    /** ファイル拡張子：IOD */
    public static final String FILE_EXTENTION_IOD = ".iod";
    
    
    /**
     * PDFに出力するデータを設定する。
     * @param dto
     */
    abstract protected void setPdfData(DTO dto);
    
    /**
     * 出力するPDFのタイトルを設定する。
     * @return 出力するPDFのタイトル
     */
    abstract protected String getPdfTitle();
    
    /**
     * PDF_INFOのリストを設定する。
     * <pre>
     * PDF_INFOはテンプレートファイル（iodファイル）のパスを保持している。
     * リストに複数の要素を設定した場合、設定した順番にPDFを作成し結合する。
     * </pre>
     * @param dto
     * @return PDF_INFOのリスト
     * @see PDF_INFO
     */
    abstract protected List<PDF_INFO> getPdfInfoList(DTO dto);
    
    /**
     * PDFの一時出力フォルダパスを設定する。
     * @return PDF一時出力フォルダパス
     */
    abstract protected String getTempFolderPath();
    
    /**
     * ダウンロード時のPDFファイル名を設定する。
     * @return PDFファイル名
     */
    abstract protected String getPdfFileName();
    
    /**
     * PDF出力前に行う画面追加処理を記述する。
     * <pre>
     * 本メソッドに記述された処理はPDF出力処理実行前に実行される。
     * 特に行う処理が無い場合は空実装とすること。
     * </pre>
     * @param dto
     */
    abstract protected void beforeIndexProc(DTO dto);
    
    /**
     * PDF出力後に行う画面追加処理を記述する。
     * <pre>
     * 本メソッドに記述された処理はPDF出力処理完了後に実行される。
     * 特に行う処理が無い場合は空実装とすること。
     * </pre>
     * @param dto
     */
    abstract protected void afterIndexProc(DTO dto);
    
    /**
     * PDF出力処理を実行する。
     */
    @Override
    protected DTO index(DTO pdfDto) throws Exception {
        // PDF処理前追加処理を実行する。
        this.beforeIndexProc(pdfDto);
        
        // インスタンス生成
        this.integratePdf = new IOIntegration();
        
        this.pdfDataList = new ArrayList<CSVDoc>();
        for (PDF_INFO pdfInfo : this.getPdfInfoList(pdfDto)) {
            CSVDoc pdfData = new CSVDoc(pdfInfo.getIodPath(), CommonConstant.C_EMPTY);
            pdfData.setCharset(PDF_PROCESS_ENCODE);
            this.pdfDataList.add(pdfData);
        }
        // PDFの基本設定
        this.setPdfBaseSettings();
        
        // 出力ファイルパスの設定
        String pdfOutputPath;
        pdfOutputPath = this.getPdfOutputPath(this.getTempFolderPath());
        
        // 出力するPDFデータを設定する
        this.setPdfData(pdfDto);
        
        // PDFを結合する
        this.pdfIntegration();
        
        // PDFを出力する
        this.outputPdf(pdfOutputPath);
        
        // PDF処理後追加処理を実行する
        this.afterIndexProc(pdfDto);
        
        // ファイル情報をDTOに設定する。
        SkfFileOutputUtils.fileOutput(pdfDto, pdfOutputPath);
        pdfDto.setUploadFileName(this.getPdfFileName());
        
        // TODO PublicStorageに作成したPDFファイルを削除する
        
        return pdfDto;
    }
    
    /**
     * 出力するPDFファイルの設定を行う。
     * <pre>
     * <印刷許可設定>
     *     printSecurity(PDFLibSecurity.PRINT_ENABLE)  印刷許可
     *     printSecurity(PDFLibSecurity.PRINT_DISABLE) 印刷不許可
     * 
     * <変更許可設定>
     *     modifySecurity(PDFLibSecurity.MODIFY_DISABLE)
     *         変更不許可
     *     modifySecurity(PDFLibSecurity.MODIFY_ALL)
     *         変更許可 (ページの抽出を除くすべての変更を許可)
     *     modifySecurity(PDFLibSecurity.MODIFY_FORM_AND_ANNOTATION)
     *         変更許可 ("注釈の作成","フォームフィールドの入力", "既存の署名フィールドに署名"を許可)
     *     modifySecurity(PDFLibSecurity.MODIFY_FORM_AND_ASSEMBLY)
     *         変更許可 ("ページレイアウト", "フォームフィールドの入力", "既存の署名フィールドに署名"を許可)
     * 
     * <テキスト文字抽出許可及びアクセシビリティ許可設定>
     *     copySecurity(PDFLibSecurity.COPY_AND_ACCESSBILITY_DISABLE) 不許可
     *     copySecurity(PDFLibSecurity.COPY_AND_ACCESSBILITY_ENABLE)  許可
     * </pre>
     * @throws PDFException
     */
    protected void setPdfBaseSettings() throws PDFException{
//        this.pdfData.setSecurityPassword("test");
//        // 印刷可否：印刷可
//        this.pdfData.printSecurity(PDFLibSecurity.PRINT_ENABLE);
//        // 変更可否：変更許可 ("注釈の作成","フォームフィールドの入力", "既存の署名フィールドに署名"を許可)
//        this.pdfData.modifySecurity(PDFLibSecurity.MODIFY_FORM_AND_ANNOTATION);
//        // 複製可否:可
//        this.pdfData.copySecurity(PDFLibSecurity.COPY_AND_ACCESSBILITY_ENABLE);
        // 中間処理ファイル文字エンコード
        this.integratePdf.setCharset(PDF_PROCESS_ENCODE);
        // 出力した一時IODを削除するか
        this.integratePdf.setDelete(true);
        // PDFタイトル設定
        this.integratePdf.defineTitle(this.getPdfTitle());
    }
    
    /**
     * PDFの一時ファイルの出力パスを設定する。
     * @param tempFolderPath 一時出力フォルダのパス
     * @return iodOutputPath PDFファイルの出力パス
     * @throws IOException
     */
    protected String getPdfOutputPath(String tempFolderPath) throws IOException{
        // 出力ファイルパスの設定
        return this.getOutputPath(tempFolderPath, FILE_EXTENTION_PDF);
    }
    
    /**
     * PDF結合前のIODファイル出力パスを設定する。
     * @param tempFolderPath 一時出力フォルダのパス
     * @return iodOutputPath 結合前IODファイルの出力パス
     * @throws IOException
     */
    protected String getIodOutputPath(String tempFolderPath) throws IOException{
        // 出力ファイルパスの設定
        return this.getOutputPath(tempFolderPath, FILE_EXTENTION_IOD);
    }
    
    /**
     * 引数で指定されたPublicStorage配下の一時フォルダに
     * ユニークな名称の一時ファイルを作成し、パスを取得する。
     * <pre>
     * 本メソッドは以下の命名規則に則り一時ファイルを作成する。
     *   固定文字列："temp-" + ミリ秒3桁までのシステム日付(yyyyMMddhhmmss-SSS) + ファイル拡張子
     * 
     * 同名ファイルが既に存在する場合、重複が解消されるまでファイル名を再取得する。
     * ※厳密な一意性を保証するものでないことに注意。
     * </pre>
     * 
     * @param tempFolderPath 一時出力フォルダのパス
     * @param fileExtention ファイル拡張子
     * @return iodOutputPath 一時ファイルの出力パス
     * @throws IOException
     */
    protected String getOutputPath(String tempFolderPath, String fileExtention) throws IOException{
        // 出力ファイルパスの設定
        // 一時ファイル名がユニークとなるようミリ秒を含めたシステム日付をファイル名に使用する
        String prefix     = "temp-";
        String sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
        String outputPath = tempFolderPath + prefix + sysDateMillisec + fileExtention;
        
        PublicStorage ps = new PublicStorage(outputPath);
        
        while (ps.exists()) {
            // 同名ファイル名が既に存在する場合、重複しなくなるまでファイル名を取り直す
            sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
            outputPath = tempFolderPath + prefix + sysDateMillisec + fileExtention;
            ps = new PublicStorage(outputPath);
        }
        return outputPath;
    }
    
    /**
     * PDFの結合を行う。
     * <pre>
     * テンプレートごとにIODファイルを出力し、結合対象として追加する。
     * </pre>
     * @throws IOException
     */
    protected void pdfIntegration() throws IOException {
        for(CSVDoc pdfData : this.pdfDataList){
            String tempIodPath = this.getIodOutputPath(this.getTempFolderPath());
            pdfData.makeIOD(tempIodPath);
            this.integratePdf.add(tempIodPath);
        }
    }
    
    /**
     * PDFファイルを出力する。
     * <pre>
     * 出力に失敗した場合はCSVDocが投げたエラーメッセージを表示する。
     * </pre>
     * @param PDF出力先パス
     */
    protected void outputPdf(String pdfOutputPath){
        int resultCode = this.integratePdf.makePDF(pdfOutputPath);
        
        String resultMessage = CommonConstant.C_EMPTY;
        if (resultCode == 0) {
            resultMessage = "PDF出力成功";
        } else {
            // PDF作成処理の戻り値が0以外である場合は、処理中で何らかのエラーが発生している(出力ファイルは生成されない)
            resultMessage = this.integratePdf.lastMessage();
        }
        LogUtils.debugByMsg(resultMessage);
    }
    
    /**
     * 引数で指定されたIDのPDF要素に「✓」記号を設定する。
     * <pre>
     * 第三引数に指定された値（DTOのチェック要素値を想定）がnullまたは空でなく、かつ
     * 値がCodeConstant.CHECKEDに等しい場合に「✓」記号を設定する。
     * </pre>
     * @param pdfData 設定対象のPDF
     * @param targetId PDF要素ID
     * @param flag Dtoのチェック要素値
     * @see CodeConstant.CHECKED
     */
    protected void setCheckMark(CSVDoc pdfData, String targetId, String flag){
        if(!NfwStringUtils.isEmpty(flag) && CodeConstant.CHECKED.equals(flag)){
            pdfData.setData(targetId, PdfBaseServiceAbstract.CHECK_MARK);
        }
    }
    
    /**
     * PDF_INFO 社宅管理システムで出力するPDF帳票をテンプレート単位で管理する。
     * <pre>
     * このenumは以下の要素を保持する。
     *   ・テンプレートIODファイルへのPublicStorageパス
     * </pre>
     * @author NEXCOシステムズ
     */
    public enum PDF_INFO{
        /** 社宅入居希望等調書 */
        NYUKYO_KIBO_CHOSHO("skf/template/skf2020/skf2020rp001/R0100_ShatakuNyukyoKibo.iod"),
        /** 貸与（予定）社宅等のご案内 */
        TAIYO_SHATAKU_ANNAI("skf/template/skf2020/skf2020rp002/R0101_TaiyoShatakuAnnai.iod"),
        /** 誓約書 */
        SEIYAKUSHO("skf/template/skf2020/skf2020rp002/R0101_Seiyakusho.iod"),
        /** 入居等決定通知書 */
        NYUKYO_KETTEI_TSUCHI("skf/template/skf2020/skf2020rp003/R0102_NyukyoKetteiTsuchi.iod"),
        /** 退居（自動車の保管場所返還）届 */
        TAIKYO_HENKAN_TODOKE("skf/template/skf2040/skf2040rp001/R0103_TaikyoHenkanTodoke.iod"),
        ;
        
        private String iodPath;
        
        private PDF_INFO(String iodPath){
            this.iodPath = iodPath;
        }
        
        /**
         * テンプレートIODファイルへのPublicStorageパスを取得する。
         * @return テンプレートIODファイルへのパス
         */
        public String getIodPath(){
            return this.iodPath;
        }
    }
}
