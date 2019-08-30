/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc001.Skf3010Sc001ContractDownLoadDto;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3010Sc001ContractDownLoadService 社宅一覧の契約情報出力ボタン処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc001ContractDownLoadService extends BaseServiceAbstract<Skf3010Sc001ContractDownLoadDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;
	// 搬入@Value("${skf3010.skf3010_sc001.excelOutPutStartLine}")
	private Integer excelOutPutStartLine;
	// @Value("${skf3010.skf3010_sc001.excelPreFileName}")
	private String excelPreFileName;
	// @Value("${skf3010.skf3010_sc001.excelWorkSheetNameShataku}")
	private String excelWorkSheetNameShataku;
	// @Value("${skf3010.skf3010_sc001.excelWorkSheetNameParking}")
	private String excelWorkSheetNameParking;

	/**
	 * 社宅一覧 契約情報出力ボタン押下処理
	 */
	// 社宅区分:借上
	private static final String SHATAKU_KBN_KARIAGE = "2";
	// 社宅区分:一棟
	private static final String SHATAKU_KBN_ITTO = "4";

	/** ロガー。 */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	@Value("${skf.common.validate_error}")
	private String validationErrorCode;

	/**
	 * 契約情報帳票出力
	 * 
	 * Dtoのリストから出力対象を抽出し、DBから帳票に出力に必要なデータを取得する。
	 * DBからの取得データが社宅契約情報、駐車場契約情報の双方が存在しない場合は帳票の出力は行わない。
	 * DBからの取得データ、Dtoから抽出した出力対象データから帳票出力データを作成し 社宅契約情報、駐車場契約情報をExcelファイルに帳票出力する。
	 * 処理結果はメッセージに設定する。
	 */
	@Override
	public BaseDto index(Skf3010Sc001ContractDownLoadDto downloadDto) throws Exception {

		return downloadDto;
	}

}
