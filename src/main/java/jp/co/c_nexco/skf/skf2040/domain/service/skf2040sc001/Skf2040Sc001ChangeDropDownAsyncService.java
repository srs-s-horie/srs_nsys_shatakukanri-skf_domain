/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinItemToBeReturnExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinItemToBeReturnExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfShatakuInfoUtils.SkfShatakuInfoUtilsGetShatakuInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2040Sc001.Skf2040Sc001GetBihinItemToBeReturnExpRepository;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001ChangeDropDownAsyncDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の社宅ドロップダウンリスト変更時処理(非同期)
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001ChangeDropDownAsyncService
        extends AsyncBaseServiceAbstract<Skf2040Sc001ChangeDropDownAsyncDto> {

    @Autowired
    private Skf2040Sc001SharedService skf2040Sc001SharedService;
    @Autowired
    private SkfHtmlCreateUtils skfHtmlCreationUtils;
    @Autowired
    private Skf2040Sc001GetBihinItemToBeReturnExpRepository skf2040Sc001GetBihinItemToBeReturnExpRepository;

    @Override
    public AsyncBaseDto index(Skf2040Sc001ChangeDropDownAsyncDto dto) throws Exception {

        // 社宅情報の設定
        this.setShatakuInfo(dto);
        // 要返却備品情報を取得
        this.setReturnBihinInfo(dto, false);

        return dto;
    }

    /**
     * 
     * 社宅情報の設定
     * 
     * @param dto Skf2040Sc001ChangeDropDownAsyncDto
     */
    protected void setShatakuInfo(Skf2040Sc001ChangeDropDownAsyncDto dto) {
        if (dto.getShatakuKanriId() == 0) {
            return;
        }
        
        LogUtils.debugByMsg("社宅管理番号" + dto.getShatakuKanriId());
        // 現居住宅の選択された情報の取得
        List<SkfShatakuInfoUtilsGetShatakuInfoExp> shatakuList = 
                skf2040Sc001SharedService.getShatakuInfo(dto.getShatakuKanriId(), dto.getShainNo());

        // 取得できた場合は現居住社宅の情報設定(初期表示字は現有社宅の最初の一件目の情報を表示)
        if (shatakuList.size() > 0) {
            this.setShatakuInfo(dto, shatakuList.get(0));
        }
    }
    
    /**
     * 引数で指定された社宅情報エンティティの内容を、引数で指定されたDTOに設定する。
     * 
     * @param dto 退居届DTO
     * @param shatakuInfo 現有社宅情報を保持した社宅情報エンティティ
     */
    private void setShatakuInfo(Skf2040Sc001ChangeDropDownAsyncDto dto, SkfShatakuInfoUtilsGetShatakuInfoExp shatakuInfo){
        // 社宅名
        dto.setHdnSelectedNowShatakuName( ObjectUtils.defaultIfNull(shatakuInfo.getShatakuName(), "") );
        LogUtils.debugByMsg("社宅名" + dto.getHdnSelectedNowShatakuName());

        // 社宅住所
        dto.setNowAddress( ObjectUtils.defaultIfNull(shatakuInfo.getAddress(), "") );
        LogUtils.debugByMsg("社宅住所" + dto.getNowAddress());

        // 室番号
        dto.setNowShatakuNo( ObjectUtils.defaultIfNull(shatakuInfo.getRoomNo(), "") );
        LogUtils.debugByMsg("室番号" + dto.getNowShatakuNo());

        // 規格(間取り)
        // 規格があった場合は、貸与規格。それ以外は本来規格
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getKikaku())) {
            dto.setNowShatakuKikaku(shatakuInfo.getKikaku());// 貸与規格
            dto.setNowShatakuKikakuName(shatakuInfo.getKikakuName());
            LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
            LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
        } else {
            if (NfwStringUtils.isNotEmpty(shatakuInfo.getOriginalKikaku())) {
                dto.setNowShatakuKikaku(shatakuInfo.getOriginalKikaku());// 本来規格
                dto.setNowShatakuKikakuName(shatakuInfo.getOriginalKikakuName());
                LogUtils.debugByMsg("規格(間取り)" + dto.getNowShatakuKikaku());
                LogUtils.debugByMsg("規格(間取り)名称" + dto.getNowShatakuKikakuName());
            }
        }

        // 面積
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getLendMenseki())) {
            dto.setNowShatakuMenseki(shatakuInfo.getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
            LogUtils.debugByMsg("現居社宅-面積" + dto.getNowShatakuMenseki());
        }

        // 駐車場 都道府県コード（保有社宅のみ設定される）
        String wkPrefName = CodeConstant.DOUBLE_QUOTATION;
        String prefCode = CodeConstant.DOUBLE_QUOTATION;
        // 取得できたら汎用コードマスタから名称を取得
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getPrefCdParking())) {
            prefCode = shatakuInfo.getPrefCdParking();
            wkPrefName = codeCacheUtils.getElementCodeName(FunctionIdConstant.GENERIC_CODE_PREFCD, prefCode);
        }

        // 駐車場 １台目 保管場所
        dto.setParking1stPlace("");
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress1())) {
            dto.setParking1stPlace(wkPrefName + shatakuInfo.getParkingAddress1());
            LogUtils.debugByMsg("現在の保管場所" + dto.getParking1stPlace());

        }

        // 駐車場 １台目 位置番号
        dto.setHdnParking1stNumber("");
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingBlock1())) {
            dto.setHdnParking1stNumber(shatakuInfo.getParkingBlock1());
            LogUtils.debugByMsg("駐車場 １台目 位置番号" + dto.getHdnParking1stNumber());
        }

        // 駐車場 ２台目 保管場所
        dto.setParking2ndPlace("");
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress2())) {
            dto.setParking2ndPlace(wkPrefName + shatakuInfo.getParkingAddress2());
            LogUtils.debugByMsg("現在の保管場所2" + dto.getParking2ndPlace());
        }

        // 駐車場 ２台目 位置番号
        dto.setHdnParking2ndNumber("");
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingBlock2())) {
            dto.setHdnParking2ndNumber(shatakuInfo.getParkingBlock2());
            LogUtils.debugByMsg("駐車場 2台目 位置番号" + dto.getHdnParking2ndNumber());
        }

        // 現在の社宅管理番号
        if (shatakuInfo.getShatakuKanriNo() != null) {
            dto.setHdnNowShatakuKanriNo(shatakuInfo.getShatakuKanriNo());
        }

        // 現在の部屋管理番号
        if (shatakuInfo.getShatakuRoomKanriNo() != null) {
            dto.setHdnNowShatakuRoomKanriNo(shatakuInfo.getShatakuRoomKanriNo());
        }
        
        // 駐車場の保管場所が2台ともNULLではない場合、駐車場のみラジオボタンを非活性にするフラグを設定
        if (NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress1())
                && NfwStringUtils.isNotEmpty(shatakuInfo.getParkingAddress2())) {
            // 駐車場2台フラグ
            dto.setParkingFullFlg(true);
        }
    }
    
    /**
     * 
     * 返却備品の設定 備品状態が2:保有備品または3:レンタルの表示の備品情報を取得する。<br>
     * 返却備品有無に「1:備品返却する」を設定する。<br>
     * 件数が取得できた場合は、備品の表示用リストを作成する。<br>
     * List<String> bihinItemList →tr情報（列） List<List<String>>
     * bihinItemNameList→td情報(行）
     * 
     * htmlBihinCreateTableに作成したbihinItemNameListと、表示したい列数を渡す
     * 
     * @param asyncDto
     * @param isUpdate
     */
    protected void setReturnBihinInfo(Skf2040Sc001ChangeDropDownAsyncDto dto, boolean isUpdate) {

        // 返却備品有無に「0:備品返却しない」を設定
        dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SHINAI);
        String bihinItem = CodeConstant.DOUBLE_QUOTATION;
        // 社宅管理番号の設定
        long shatakuKanriId = dto.getShatakuKanriId();

        // 備品状態が2:保有備品または3:レンタルの表示の備品取得
        List<Skf2040Sc001GetBihinItemToBeReturnExp> resultBihinItemList = new ArrayList<Skf2040Sc001GetBihinItemToBeReturnExp>();
        resultBihinItemList = this.getBihinItemToBeReturn(shatakuKanriId, dto.getShainNo(), resultBihinItemList);

        // 件数が取得できた場合
        if (resultBihinItemList.size() > 0 && CollectionUtils.isNotEmpty(resultBihinItemList)) {

            // 返却備品有無に「1:備品返却する」を設定
            dto.setHdnBihinHenkyakuUmu(CodeConstant.BIHIN_HENKYAKU_SURU);

            // 【ラベル部分】
            // 要返却備品の取得
            List<String> bihinItemList = new ArrayList<String>();
            List<List<String>> bihinItemNameList = new ArrayList<List<String>>();
            for (Skf2040Sc001GetBihinItemToBeReturnExp bihinItemInfo : resultBihinItemList) {
                // 表示・値を設定
                bihinItemList = new ArrayList<String>();
                bihinItemList.add(bihinItemInfo.getBihinName());
                bihinItemNameList.add(bihinItemList);
            }

            // HTMLの作成
            bihinItem = skfHtmlCreationUtils.htmlBihinCreateTable(bihinItemNameList, 2);
            dto.setReturnEquipment(bihinItem);
        } else {
            dto.setReturnEquipment(bihinItem);
        }
    }
    
    /**
     * 要返却備品の取得
     * 
     * @param shatakuKanriId
     * @param shainNo
     * @param yearMonth
     * @param resultBihinItemList
     * @return resultBihinItemList
     */
    private List<Skf2040Sc001GetBihinItemToBeReturnExp> getBihinItemToBeReturn(long shatakuKanriId, String shainNo,
            List<Skf2040Sc001GetBihinItemToBeReturnExp> resultBihinItemList) {

        String yearMonth = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

        Skf2040Sc001GetBihinItemToBeReturnExpParameter param = new Skf2040Sc001GetBihinItemToBeReturnExpParameter();
        param.setShainNo(shainNo);
        param.setShatakuKanriId(shatakuKanriId);
        param.setYearMonth(yearMonth);
        resultBihinItemList = skf2040Sc001GetBihinItemToBeReturnExpRepository.getBihinItemToBeReturn(param);
        return resultBihinItemList;

    }

}
