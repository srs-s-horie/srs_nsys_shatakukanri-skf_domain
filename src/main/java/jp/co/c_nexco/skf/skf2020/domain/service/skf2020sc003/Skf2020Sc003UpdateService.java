/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchi;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2020TNyukyoChoshoTsuchiKey;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf2020TNyukyoChoshoTsuchiRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003UpdateDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003UpdateService extends BaseServiceAbstract<Skf2020Sc003UpdateDto> {

	// 会社コード
	private String companyCd = CodeConstant.C001;

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;

	@Autowired
	private Skf2020TNyukyoChoshoTsuchiRepository skf2020TNyukyoChoshoTsuchiRepository;

	// カンマ区切りフォーマット
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param updDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public BaseDto index(Skf2020Sc003UpdateDto updDto) throws Exception {

		updDto.setPageTitleKey(MessageIdConstant.SKF2020_SC003_TITLE);

		// 必要とする社宅理由更新メソッド
		boolean updResult = updateHitsuyoShataku(updDto);
		if (!updResult) {
			ServiceHelper.addErrorResultMessage(updDto, null, MessageIdConstant.E_SKF_1075);
			throwBusinessExceptionIfErrors(updDto.getResultMessages());
		}

		// TODO 社宅管理データ連携処理実行

		// 初期表示
		skf2020sc003SharedService.setMenuScopeSessionBean(menuScopeSessionBean);
		skf2020sc003SharedService.setDispInfo(updDto);

		// 提示データが存在するかチェックする。
		if (NfwStringUtils.isNotEmpty(updDto.getNewShatakuKanriNo())) {
			// 提示データが存在する場合、更新完了メッセージ表示
			ServiceHelper.addResultMessage(updDto, MessageIdConstant.I_SKF_1011);
		} else {
			// 提示データが存在しない場合、社宅の使用量変更を促すメッセージ表示
			ServiceHelper.addResultMessage(updDto, MessageIdConstant.I_SKF_2057);
		}

		return updDto;
	}

	/**
	 * 必要とする社宅理由更新
	 * 
	 * @param updDto
	 */
	private boolean updateHitsuyoShataku(Skf2020Sc003UpdateDto updDto) {
		boolean result = true;
		// 更新対象のデータを取得
		Skf2020TNyukyoChoshoTsuchi updData = new Skf2020TNyukyoChoshoTsuchi();
		Skf2020TNyukyoChoshoTsuchiKey key = new Skf2020TNyukyoChoshoTsuchiKey();
		key.setCompanyCd(companyCd);
		key.setApplNo(updDto.getApplNo());

		updData = skf2020TNyukyoChoshoTsuchiRepository.selectByPrimaryKey(key);
		if (updData == null) {
			return false;
		}
		Date lastUpdateDate = updDto.getLastUpdateDate("Skf2020TShatakuChoshoTsuchiUpdateDate");
		// 排他チェック
		checkLockException(lastUpdateDate, updData.getUpdateDate());

		String hitsuyoShataku = updDto.getHitsuyoShataku();
		updData.setHitsuyoShataku(hitsuyoShataku);
		int updResult = skf2020TNyukyoChoshoTsuchiRepository.updateByPrimaryKeySelective(updData);
		if (updResult <= 0) {
			result = false;
		}

		return result;
	}

}
