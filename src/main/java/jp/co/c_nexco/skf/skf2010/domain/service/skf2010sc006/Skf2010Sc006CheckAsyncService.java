/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc006;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTaikyoShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2010Sc006.Skf2010Sc006GetTaikyoShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2010Sc006.Skf2010Sc006GetTaikyoShatakuInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc006.Skf2010Sc006CheckAsyncDto;

/**
 * Skf2010Sc006 申請書類承認／差戻し／通知 承認非同期 承認時チェック処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc006CheckAsyncService extends SkfAsyncServiceAbstract<Skf2010Sc006CheckAsyncDto> {

	@Autowired
	private Skf2010Sc006GetTaikyoShatakuInfoExpRepository skf2010Sc006GetTaikyoShatakuInfoExpRepository;

	/**
	 * サービス処理を行う。
	 * 
	 * @param dto 非同期チェック処理DTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2010Sc006CheckAsyncDto dto) throws Exception {

		List<Skf2010Sc006GetTaikyoShatakuInfoExp> dataList = new ArrayList<Skf2010Sc006GetTaikyoShatakuInfoExp>();

		// 承認が完了されていない社宅退居申請を取得する
		// パラメータの設定
		Skf2010Sc006GetTaikyoShatakuInfoExpParameter param = new Skf2010Sc006GetTaikyoShatakuInfoExpParameter();
		// 社宅管理番号
		if (NfwStringUtils.isNotBlank(dto.getCheckShatakuKanriNo())) {
			param.setShatakuKanriNo(Long.parseLong(dto.getCheckShatakuKanriNo()));
		}
		// 社宅部屋管理番号
		if (NfwStringUtils.isNotBlank(dto.getCheckRoomKanriNo())) {
			param.setRoomKanriNo(Long.parseLong(dto.getCheckRoomKanriNo()));
		}
		// 申請書類ID（退居申請固定）
		param.setApplId(FunctionIdConstant.R0103);
		// 申請書ステータス（承認固定）
		param.setApplStatus(CodeConstant.NYUTAIKYO_APPL_STATUS_SHONIN_ZUMI);
		// 社宅を退居するフラグ(社宅を退居。駐車場のみは含まない）
		param.setTaikyoShataku(CodeConstant.LEAVE);

		// データ取得
		dataList = skf2010Sc006GetTaikyoShatakuInfoExpRepository.getTaikyoShatakuInfo(param);

		// 取得できた場合は自動遷移のダイアログ表示用設定
		if (dataList.size() > 0) {

			dto.setApplNo(dataList.get(0).getApplNo());

			dto.setDialogFlg(true);
		}

		return dto;

	}

}
