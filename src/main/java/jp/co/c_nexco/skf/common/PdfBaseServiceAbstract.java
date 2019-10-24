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
 * multiPdfBaseServiceAbstract 結合PDF出力処理の基盤クラス。
 * 
 * @author NEXCOシステムズ
 */
public abstract class PdfBaseServiceAbstract<DTO extends FileDownloadDto> extends BaseServiceAbstract<DTO> {
    
    protected IOIntegration integratePdf;
    protected List<CSVDoc> pdfDataList;
    protected String pdfOutputPath;
    
    /**
     * 中間処理ファイル（IOD,DAT）生成に使用する文字エンコード （「✓」のような特殊記号を表示するため、UTF-16に設定）
     */
    private static final String PDF_PROCESS_ENCODE = "UTF-16";
    /** ✓マーク */
    public static final String CHECK_MARK = "✓";
    
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
     * PDF出力処理（１テンプレートによる単票出力）を実行する。
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
        this.setPdfOutputPath(this.getTempFolderPath());
        
        // 出力するPDFデータを設定する
        this.setPdfData(pdfDto);
        
        // PDFを結合する
        this.pdfIntegration();
        
        // PDFを出力する
        this.outputPdf();
        
        // PDF処理後追加処理を実行する
        this.afterIndexProc(pdfDto);
        
        // ファイル情報をDTOに設定する。
        SkfFileOutputUtils.fileOutput(pdfDto, this.pdfOutputPath);
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
     * @throws IOException
     */
    protected void setPdfOutputPath(String tempFolderPath) throws IOException{
        // 出力ファイルパスの設定
        // 一時ファイル名がユニークとなるようミリ秒を含めたシステム日付をファイル名に使用する
        String prefix     = "temp-";
        String sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
        String suffix     = ".pdf"; // 出力ファイル拡張子
        this.pdfOutputPath = tempFolderPath + prefix + sysDateMillisec + suffix;

        PublicStorage ps = new PublicStorage(this.pdfOutputPath);
        
        while (ps.exists()) {
            // ファイル名が重複した場合、重複しなくなるまでファイル名を取り直す
            sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
            this.pdfOutputPath = tempFolderPath + prefix + sysDateMillisec + suffix;
            ps = new PublicStorage(this.pdfOutputPath);
        }
        return;
    }
    
    /**
     * PDF結合前のIODファイル出力パスを設定する。
     * @param tempFolderPath 一時出力フォルダのパス
     * @return iodOutputPath 結合前IODファイルのパス
     * @throws IOException
     */
    protected String getIodOutputPath(String tempFolderPath) throws IOException{
        // 出力ファイルパスの設定
        // 一時ファイル名がユニークとなるようミリ秒を含めたシステム日付をファイル名に使用する
        String prefix     = "temp-";
        String sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
        String suffix     = ".iod"; // 出力ファイル拡張子
        
        String iodOutputPath = tempFolderPath + prefix + sysDateMillisec + suffix;

        PublicStorage ps = new PublicStorage(iodOutputPath);
        
        while (ps.exists()) {
            // ファイル名が重複した場合、重複しなくなるまでファイル名を取り直す
            sysDateMillisec = DateUtils.getSysDateString( "yyyyMMddhhmmss-SSS");
            iodOutputPath = tempFolderPath + prefix + sysDateMillisec + suffix;
            ps = new PublicStorage(iodOutputPath);
        }
        return iodOutputPath;
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
     */
    protected void outputPdf(){
        int resultCode = this.integratePdf.makePDF(this.pdfOutputPath);
        
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
