/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc002.Skf2020Sc002GetApplInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2010TApplHistory;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReportKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetApplInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2010TApplHistoryRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2040TTaikyoReportRepository;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfLoginUserInfoUtils;
import jp.co.c_nexco.skf.common.util.SkfShinseiUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020Sc002common.Skf2020Sc002CommonDto;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040Sc001common.Skf2040Sc001CommonDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届の共通サービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001SharedService{
    @Autowired
    private SkfDropDownUtils skfDropDownUtils;
    @Autowired
    private SkfShinseiUtils skfShinseiUtils;
    @Autowired
    private SkfGenericCodeUtils skfGenericCodeUtils;
    @Autowired
    private SkfLoginUserInfoUtils skfLoginUserInfoUtils;
    
    
    
    @Autowired
    // 退居届リポジトリ
    private Skf2040TTaikyoReportRepository skf2040TTaikyoReportRepository;
    @Autowired
    // 申請書類履歴リポジトリ
    private Skf2010TApplHistoryRepository skf2010TApplHistoryRepository;
    @Autowired
    private Skf2040Sc001GetApplInfoExpRepository skf2040Sc001GetApplInfoExpRepository;
    /**
     * 退居（返還）理由ドロップダウンリスト取得
     * @param defaultSelectVal 初期選択値 ※汎用コードSKF1142のコード値を指定すること
     * @return List<Map<String, Object>> 退居（返還）理由ドロップダウンリスト
     */
    public List<Map<String, Object>> getTaikyoHenkanRiyuList(String defaultSelectCdVal){
        // 退居（返還）理由プルダウン取得
        return skfDropDownUtils.getGenericForDoropDownList(
                FunctionIdConstant.GENERIC_CODE_TAIKYO_HENKAN_RIYU, defaultSelectCdVal, true);
    }
    
    /**
     * 現住居社宅ドロップダウンリスト取得
     * @param shainNo 対象社員番号
     * @param defaultSelectShatakuId 初期選択値 社宅管理IDを指定する事
     * @return List<Map<String, Object>> 現住居社宅ドロップダウンリスト
     */
    public List<Map<String, Object>> getNowShatakuNameList(String shainNo, long defaultSelectShatakuId){
        //システム日付
        String sysDateYyyyMMdd = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_FLAT);
        return skfDropDownUtils.getDdlNowShatakuNameByCd(shainNo, sysDateYyyyMMdd, defaultSelectShatakuId, false);
    }
    
    /**
     * 返却立会希望日(時)ドロップダウンリスト取得
     * @param defaultSelectTime 初期選択値
     * @return List<Map<String, Object>> 返却立会希望日(時)ドロップダウンリスト
     */
    public List<Map<String, Object>> getSessionTimeList(String defaultSelectTime){
        // 返却立会希望日(時)ドロップダウンリストの設定
        return skfDropDownUtils.getGenericForDoropDownList(
                FunctionIdConstant.GENERIC_CODE_REQUESTTIME_KBN, defaultSelectTime, true);
    }
    
    /**
     * 引数で指定された会社コード、申請書類管理番号に該当する退居届情報を取得する。
     * @param companyCd 会社コード
     * @param applNo 申請書類管理番号
     * @return 退居届情報
     */
    public Skf2040TTaikyoReport getTaikyoInfo(String companyCd, String applNo){
        Skf2040TTaikyoReportKey key = new Skf2040TTaikyoReportKey();
        key.setCompanyCd(companyCd);
        key.setApplNo(applNo);
        return skf2040TTaikyoReportRepository.selectByPrimaryKey(key);
    }
    
    /**
     * 引数で指定された退居届エンティティの情報をdtoに設定する。
     * @param dto 設定対象のDTO
     * @param taikyoInfo 退居届情報
     */
    public <DTO extends Skf2040Sc001CommonDto> void convTaikyoEntityToDto(DTO dto, Skf2040TTaikyoReport taikyoInfo){
        dto.setAgencyName(taikyoInfo.getAgency());
        dto.setAffiliation1Name(taikyoInfo.getAffiliation1());
        dto.setAffiliation2Name(taikyoInfo.getAffiliation2());
        dto.setShatakuNo(taikyoInfo.getShatakuNo());
        dto.setNowAddress(taikyoInfo.getAddress());
        dto.setName(taikyoInfo.getName());
        dto.setGender(taikyoInfo.getGender());
        dto.setParking1stPlace(taikyoInfo.getParkingAddress1());
        dto.setParking2ndPlace(taikyoInfo.getParkingAddress2());
        dto.setTaikyoHenkanDate(taikyoInfo.getTaikyoDate());
        
        // 退居種別
        String[] taikyoTypeArray = new String[] {
                taikyoInfo.getTaikyoShataku(),
                taikyoInfo.getTaikyoParking1(),
                taikyoInfo.getTaikyoParking2()
        };
        dto.setTaikyoType(taikyoTypeArray);
        
        dto.setTaikyoRiyuKbn(taikyoInfo.getTaikyoRiyuKbn());
        dto.setTaikyoHenkanRiyu(taikyoInfo.getTaikyoRiyu());
        dto.setShatakuJyotai(taikyoInfo.getShatakuJotai());
        dto.setTaikyogoRenrakuSaki(taikyoInfo.getTaikyogoRenrakusaki());
        dto.setSessionDay(taikyoInfo.getSessionDay());
        dto.setSessionTime(taikyoInfo.getSessionTime());
        dto.setRenrakuSaki(taikyoInfo.getRenrakuSaki());
    }
    
    /**
     * 引数で指定されたDtoが保持している申請書類管理番号をもとに、
     * 既存の退居届情報を取得してDtoに設定する。
     * @param dto 申請書類管理番号を保持したDto
     */
    public <DTO extends Skf2040Sc001CommonDto> void setExistTaikyoInfo(DTO dto){
        Skf2040TTaikyoReport taikyoInfo = this.getTaikyoInfo(CodeConstant.C001, dto.getApplNo());
        if (taikyoInfo == null) {
            // TODO 退居届情報の取得に失敗した場合はエラーメッセージを表示してボタンを使用不可にする
            LogUtils.debugByMsg("該当する退居届情報が存在しません。");
            return;
        }
        this.convTaikyoEntityToDto(dto, taikyoInfo);
    }
    
    /**
     * 申請書情報の取得
     * 
     * @param dto
     * @return 申請書情報のMAP applInfoMap
     */
    protected <DTO extends Skf2040Sc001CommonDto> Map<String, String> getSkfApplInfoMap(DTO dto) {
        Map<String, String> applInfoMap = new HashMap<String, String>();

        // 申請情報情報
        Skf2020Sc002GetApplInfoExp applInfo = new Skf2020Sc002GetApplInfoExp();
        Skf2020Sc002GetApplInfoExpParameter applParam = new Skf2020Sc002GetApplInfoExpParameter();
        applParam.setCompanyCd(CodeConstant.C001);
        applParam.setShainNo(dto.getShainNo());
        applParam.setApplNo(dto.getApplNo());
        applParam.setApplId(FunctionIdConstant.R0100);
        applInfo = skf2040Sc001GetApplInfoExpRepository.getApplInfo(applParam);
        if (applInfo != null) {
            // 社員番号
            applInfoMap.put("shainNo", applInfo.getShainNo());
            // 申請書ID
            applInfoMap.put("applId", applInfo.getApplId());
            // 申請書番号
            applInfoMap.put("applNo", applInfo.getApplNo());
            // 申請ステータス
            applInfoMap.put("status", applInfo.getStatus());
            // 添付ファイル有無フラグ
            applInfoMap.put("applTacFlg", applInfo.getApplTacFlg());
        } else {
            // 社員番号
            applInfoMap.put("shainNo", dto.getShainNo());
            // 申請ステータス
            applInfoMap.put("status", CodeConstant.STATUS_MISAKUSEI);
        }
        return applInfoMap;
    }
    
    /**
     * バイト数カット処理
     * <pre>
     * 引数で与えられたDtoの要素を既定のバイト数でカットしてセットし直す。
     * </pre>
     * @param dto
     * @throws UnsupportedEncodingException
     */
    public <DTO extends Skf2040Sc001CommonDto> void cutByte(DTO dto) throws UnsupportedEncodingException {
        String Msg = "バイト数カット処理：　";
        // 退居(返還)日 TODO スラッシュを除いた状態でカット
        dto.setTaikyoHenkanDate(NfwStringUtils.rightTrimbyByte(dto.getTaikyoHenkanDate(), 8));
        LogUtils.debugByMsg(Msg + "退居(返還)日" + dto.getTaikyoHenkanDate());
        // 退居(返還)理由
        dto.setTaikyoHenkanRiyu(NfwStringUtils.rightTrimbyByte(dto.getTaikyoHenkanRiyu(), 256));
        LogUtils.debugByMsg(Msg + "退居(返還)理由" + dto.getTaikyoHenkanRiyu());
        // 社宅の状態
        dto.setShatakuJyotai(NfwStringUtils.rightTrimbyByte(dto.getShatakuJyotai(), 128));
        LogUtils.debugByMsg(Msg + "社宅の状態" + dto.getShatakuJyotai());
        // 退居後の連絡先
        dto.setTaikyogoRenrakuSaki(NfwStringUtils.rightTrimbyByte(dto.getTaikyogoRenrakuSaki(), 128));
        LogUtils.debugByMsg(Msg + "退居後の連絡先" + dto.getTaikyogoRenrakuSaki());
        // 返却立合希望日（日） TODO スラッシュを除いた状態でカット
        dto.setSessionDay(NfwStringUtils.rightTrimbyByte(dto.getSessionDay(), 8));
        LogUtils.debugByMsg(Msg + "返却立合希望日（日）" + dto.getSessionDay());
        // 連絡先
        dto.setRenrakuSaki(NfwStringUtils.rightTrimbyByte(dto.getRenrakuSaki(), 13));
        LogUtils.debugByMsg(Msg + "連絡先" + dto.getRenrakuSaki());
    }
    
    
    /**
     * 新規時の保存処理
     * 
     * @param dto
     * @param applInfoMap 申請情報を保持したMap
     * @return 登録成功 true 失敗 false
     */
    protected <DTO extends Skf2040Sc001CommonDto> boolean saveNewData(DTO dto, Map<String, String> applInfoMap) {
        // 申請書類管理番号を取得
        String newApplNo = skfShinseiUtils.getApplNo(CodeConstant.C001, applInfoMap.get("shainNo"),
                FunctionIdConstant.R0103);
        LogUtils.debugByMsg("申請書類管理番号" + newApplNo);
        // 取得に失敗した場合
        if (newApplNo == null) {
            // エラーメッセージを表示用に設定
            ServiceHelper.addResultMessage(dto, null, MessageIdConstant.E_SKF_1094);
            // 保存処理を終了
            return false;
        } else {
            applInfoMap.put("applNo", newApplNo);
        }

        // 登録時のシステム日付を取得
        dto.setApplDate(DateUtils.getSysDate());
        // 申請書類履歴テーブル登録処理
        insertApplHistory(dto, applInfoMap);
        // 退居届申請テーブルの設定
        Skf2040TTaikyoReport insertParam = new Skf2040TTaikyoReport();
        insertParam = this.makeTaikyoReportParam(dto, applInfoMap);
        // 登録
        this.insertTaikyoTodoke(insertParam);

        // ステータスを更新
        dto.setStatus(applInfoMap.get("newStatus"));
        // 申請書番号を設定
        dto.setApplNo(newApplNo);

        return true;
    }
    
    private Skf2040TTaikyoReport makeTaikyoReportParam(
            Skf2040Sc001CommonDto dto, Map<String, String> applInfo){
        
        Skf2040TTaikyoReport insertParam = new Skf2040TTaikyoReport();
        
        boolean isShatakuTaikyoChecked = false;
        boolean isParking1stHenkanChecked = false;
        boolean isParking2ndHenkanChecked = false;
        
        // 社宅退居のチェック状況を取得する。 //TODO 定数化
        String[] taikyoTypeArray = dto.getTaikyoType();
        if (taikyoTypeArray!=null && taikyoTypeArray.length > 0) {
            for(String taikyoType : taikyoTypeArray){
                if (taikyoType.equals("shataku_checked")) {
                    isShatakuTaikyoChecked = true;
                }
                if (taikyoType.equals("park1_checked")) {
                    isParking1stHenkanChecked = true;
                }
                if (taikyoType.equals("park2_checked")) {
                    isParking2ndHenkanChecked = true;
                }
            }
        }
        // 会社コード
        insertParam.setCompanyCd(CodeConstant.C001);
        // 申請書類管理番号
        insertParam.setApplNo(dto.getApplNo());
        // 申請日付
        insertParam.setApplDate(""); //TODO 何を設定する？
        // 社員番号
        insertParam.setShainNo(dto.getShainNo());
        // 機関名
        insertParam.setAgency(dto.getAgencyName());
        // 部等名
        insertParam.setAffiliation1(dto.getAffiliation1Name());
        // 課等名
        insertParam.setAffiliation2(dto.getAffiliation2Name());
        // 現住所
        insertParam.setAddress(dto.getNowShatakuName()); // TODO 社宅住所を表示する事
        // 氏名
        insertParam.setName(dto.getName());
        // 退居返還日
        insertParam.setTaikyoDate(dto.getTaikyoHenkanDate());
        
        // 退居理由
        // 退居理由区分
        if (dto.getTaikyoRiyuKbn().equals(CodeConstant.TAIKYO_RIYU_OTHERS)) {
            // ドロップダウンリストが”その他”選択時：「退居（返還）理由」テキスト
            insertParam.setTaikyoRiyuKbn(dto.getTaikyoHenkanRiyu());
            // ドロップダウンリストが”その他”選択時：”1”
            insertParam.setTaikyoRiyuKbn(CodeConstant.TAIKYO_RIYU_KBN_OTHERS);
        } else {
            // ドロップダウンリストが”その他”以外を選択時：「退居（返還）理由」ドロップダウン選択値
            insertParam.setTaikyoRiyuKbn(dto.getTaikyoRiyuKbn());
            // ドロップダウンリストが”その他”以外を選択時”2”
            insertParam.setTaikyoRiyuKbn(CodeConstant.TAIKYO_RIYU_KBN_NON_OTHERS);
        }
        
        insertParam.setTaikyogoRenrakusaki(dto.getTaikyogoRenrakuSaki());
        
        // 退居場所
        if (isShatakuTaikyoChecked) {
            //  「社宅を退居する」選択時:社宅名＋部屋番号 TODO 選択された退居対象社宅の部屋番号を取得しておくこと
            insertParam.setTaikyoArea(dto.getNowShatakuName() + dto.getHdnNowShatakuRoomKanriNo());
        }
//        if (isParking1stHenkanChecked){
//            //  「駐車場１を返還する」選択時:1台目の保管場所＋位置番号 TODO いらない疑惑
//            insertParam.setTaikyoArea(dto.getParking1stPlace() + dto.getHdnParking1stNumber());
//        }
//        if (isParking2ndHenkanChecked) {
//            //  「駐車場２を返還する」選択時:2台目の保管場所＋位置番号 TODO いらない疑惑
//            insertParam.setTaikyoArea(dto.getParking2ndPlace() + dto.getHdnParking2ndNumber());
//        }
        
        // 退居する社宅区分
        // 社宅退居と駐車場１、２返還の組み合わせによって決まる。
        insertParam.setShatakuTaikyoKbn(this.getShatakuTaikyoKbn1(
                isShatakuTaikyoChecked, isParking1stHenkanChecked, isParking2ndHenkanChecked));

        // 返還する駐車場区分
        // 駐車場１、２返還の組み合わせによって決まる。
        insertParam.setShatakuTaikyoKbn2(this.getShatakuTaikyoKbn2(
                isParking1stHenkanChecked, isParking2ndHenkanChecked));
        
        // 社宅退居フラグ
        insertParam.setTaikyoShataku(String.valueOf(isShatakuTaikyoChecked));
        // 駐車場１返還フラグ
        insertParam.setTaikyoParking1(String.valueOf(isParking1stHenkanChecked));
        // 駐車場２返還フラグ
        insertParam.setTaikyoParking2(String.valueOf(isParking2ndHenkanChecked));
        
        // 社宅管理番号
        insertParam.setShatakuNo(dto.getShatakuNo());
        // 部屋管理番号
        insertParam.setRoomKanriNo(dto.getHdnNowShatakuRoomKanriNo());
        // 社宅状態
        insertParam.setShatakuJotai(dto.getShatakuJyotai());
        // 返却立合希望日
        insertParam.setSessionDay(dto.getSessionDay());
        // 返却立合希望日（時間）
        insertParam.setSessionTime(dto.getSessionTime());
        // 連絡先
        insertParam.setRenrakuSaki(dto.getRenrakuSaki());
        
        
        return insertParam;
    }

    /**
     * 引数で指定された「社宅を退居する」、「駐車場１を返還する」、「駐車場２を返還する」のチェック状態をもとに、
     * 退居届テーブルの「退居する社宅区分」カラムに設定する値を取得する。
     * @param isShatakuTaikyoChecked 「社宅を退居する」チェック状態
     * @param isParking1stHenkanChecked 「駐車場１を返還する」チェック状態
     * @param isParking2ndHenkanChecked 「駐車場２を返還する」チェック状態
     * @return 「退居する社宅区分」カラムに設定する値
     * @see CodeConstant.TAIKYO_KBN_1
     */
    private String getShatakuTaikyoKbn1(boolean isShatakuTaikyoChecked,
            boolean isParking1stHenkanChecked, boolean isParking2ndHenkanChecked) {
        CodeConstant.TAIKYO_KBN_1 taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.EMPTY;
        if (isShatakuTaikyoChecked) {
            if (isParking1stHenkanChecked || isParking2ndHenkanChecked) {
                // 社宅と駐車場両方を返還
                taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.BOTH;
            } else {
                // 社宅のみ返還
                taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.SHATAKU;
            }
        } else {
            if (isParking1stHenkanChecked || isParking2ndHenkanChecked) {
                // 駐車場のみ返還
                taikyoKbn1 = CodeConstant.TAIKYO_KBN_1.PARKING;
            }
        }
        return taikyoKbn1.getVal();
    }
    /**
     * 引数で指定された「駐車場１を返還する」、「駐車場２を返還する」のチェック状態をもとに、
     * 退居届テーブルの「退居する社宅区分2」カラムに設定する値を取得する。
     * @param isParking1stHenkanChecked 「駐車場１を返還する」チェック状態
     * @param isParking2ndHenkanChecked 「駐車場２を返還する」チェック状態
     * @return 「退居する社宅区分」カラムに設定する値
     * @see CodeConstant.TAIKYO_KBN_2
     */
    private String getShatakuTaikyoKbn2(
            boolean isParking1stHenkanChecked, boolean isParking2ndHenkanChecked) {
        CodeConstant.TAIKYO_KBN_2 taikyoKbn2;
        
        if (isParking1stHenkanChecked && isParking2ndHenkanChecked) {
            // 駐車場１、２両方を返還
            taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.BOTH_PARKING;
        } else if (isParking1stHenkanChecked) {
            // 駐車場１のみを返還
            taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.PARKING_1ST;
        } else if (isParking2ndHenkanChecked) {
            // 駐車場２のみを返還
            taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.PARKING_2ND;
        } else {
            // 駐車場を返還しない
            taikyoKbn2 = CodeConstant.TAIKYO_KBN_2.NON_RETURN;
        }
        return taikyoKbn2.getVal();
    }
    
    /**
     * 退居(自動車の保管場所返還)届テーブルの登録処理を行う
     * 
     * @param param 登録する値を保持した退居(自動車の保管場所返還)届テーブルエンティティ
     */
    private void insertTaikyoTodoke(Skf2040TTaikyoReport param) {
        // 登録処理
        int registCount = 0;
        registCount = skf2040TTaikyoReportRepository.insertSelective(param);
        LogUtils.debugByMsg("退居(自動車の保管場所返還)届テーブル登録件数：" + registCount + "件");

    }
    
    /**
     * 申請書類履歴テーブル登録処理
     * 
     * @param dto
     * @param applInfo
     */
    private <DTO extends Skf2040Sc001CommonDto> void insertApplHistory(DTO dto, Map<String, String> applInfo) {

        // 申請書類履歴テーブルの設定
        Skf2010TApplHistory setValue = new Skf2010TApplHistory();

        // 登録項目をセット
        // 会社コード
        setValue.setCompanyCd(CodeConstant.C001);
        // 社員番号
        setValue.setShainNo(dto.getShainNo());
        setValue.setApplDate(dto.getApplDate());
        setValue.setApplNo(applInfo.get("applNo"));
        setValue.setApplId(FunctionIdConstant.R0103);
        setValue.setApplStatus(applInfo.get("newStatus"));
        setValue.setApplTacFlg(applInfo.get("applTacFlg"));
        setValue.setComboFlg(SkfCommonConstant.NOT_RENKEI);
        // 登録
        int registCount = 0;
        registCount = skf2010TApplHistoryRepository.insertSelective(setValue);
        LogUtils.debugByMsg("申請書類履歴テーブル登録件数：" + registCount + "件");
    }
}
