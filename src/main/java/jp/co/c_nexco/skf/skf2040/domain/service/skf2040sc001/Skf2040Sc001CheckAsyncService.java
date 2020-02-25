/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.io.UnsupportedEncodingException;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001CheckAsyncDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面のチェック処理(非同期)
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001CheckAsyncService extends AsyncBaseServiceAbstract<Skf2040Sc001CheckAsyncDto> {

	private static final String TRUE = "true";
	private static final String FALSE = "false";

	/**
	 * サービス処理を行う。
	 * 
	 * @param clearDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public AsyncBaseDto index(Skf2040Sc001CheckAsyncDto checkDto) throws Exception {

		boolean isEmptyError = false;
		boolean isValueTypeError = false;
		boolean isByteCountError = false;
		boolean isDateFormatError = false;
		boolean isRelationError = false;

		// 未入力確認
		isEmptyError = this.checkControlEmpty(checkDto);

		// 文字種確認
		isValueTypeError = this.checkValueType(checkDto);

		// バイト数確認
		isByteCountError = this.checkByteCount(checkDto);

		// 日付の整合性確認
		isDateFormatError = this.checkDateFormat(checkDto);

		// 相関チェック
		isRelationError = this.checkRelation(checkDto);

		// エラーがあった場合処理終了
		if (isEmptyError || isValueTypeError || isByteCountError || isDateFormatError || isRelationError) {
			throwBusinessExceptionIfErrors(checkDto.getResultMessages());
		}
		return checkDto;
	}

	/**
	 * 未入力確認
	 * 
	 * @param clearDto
	 * @return isError エラーが存在する場合True
	 */
	private boolean checkControlEmpty(Skf2040Sc001CheckAsyncDto checkDto) {

		boolean isError = false;
		String msg = "必須チェック　値確認：";

		/** 単項目 */
		// 社宅名
		LogUtils.debugByMsg(msg + "保有社宅名" + checkDto.getNowShatakuName());
		if (NfwStringUtils.isBlank(checkDto.getNowShatakuName())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "nowShatakuName" },
					MessageIdConstant.E_SKF_1054, "保有社宅名");
			isError = true;
		}

		// 「退居(返還)日」
		LogUtils.debugByMsg(msg + "退居(返還)日" + checkDto.getTaikyoHenkanDate());
		if (NfwStringUtils.isBlank(checkDto.getTaikyoHenkanDate())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoHenkanDate" },
					MessageIdConstant.E_SKF_1048, "退居(返還)日");
			isError = true;
		}

		// 「退居（返還）理由」
		LogUtils.debugByMsg(msg + "退居(返還)理由" + checkDto.getTaikyoRiyuKbn());
		if (NfwStringUtils.isBlank(checkDto.getTaikyoRiyuKbn())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoRiyuKbn" },
					MessageIdConstant.E_SKF_1054, "退居(返還)理由");
			isError = true;
		}

		// 「社宅を退居する」チェックフラグ
		boolean isShatakuReturnChecked = false;
		if (TRUE.equals(checkDto.getTaikyoTypeShataku())) {
			isShatakuReturnChecked = true;
		}

		if (isShatakuReturnChecked) {
			// 「社宅を退居する」がチェックされている場合に以下のチェックを行う
			// 「退居後の連絡先」
			LogUtils.debugByMsg(msg + "退居後の連絡先" + checkDto.getTaikyogoRenrakuSaki());
			if (NfwStringUtils.isBlank(checkDto.getTaikyogoRenrakuSaki())) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyogoRenrakuSaki" },
						MessageIdConstant.E_SKF_1048, "退居後の連絡先");
				isError = true;
			}

			if (CodeConstant.BIHIN_HENKYAKU_SURU.equals(checkDto.getHdnBihinHenkyakuUmu())) {
				// 返却する備品が存在する場合

				// 「返却立会希望日（日）」
				LogUtils.debugByMsg(msg + "返却立会希望日（日）" + checkDto.getSessionDay());
				if (NfwStringUtils.isBlank(checkDto.getSessionDay())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionDay" },
							MessageIdConstant.E_SKF_1048, "返却立会希望日（日）");
					isError = true;
				}

				// 「返却立会希望日（時）」
				LogUtils.debugByMsg(msg + "返却立会希望日（時）" + checkDto.getSessionTime());
				if (NfwStringUtils.isBlank(checkDto.getSessionTime())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionTime" },
							MessageIdConstant.E_SKF_1054, "返却立会希望日（時）");
					isError = true;
				}

				// 「連絡先」
				LogUtils.debugByMsg(msg + "連絡先" + checkDto.getRenrakuSaki());
				if (NfwStringUtils.isBlank(checkDto.getRenrakuSaki())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" },
							MessageIdConstant.E_SKF_1048, "連絡先");
					isError = true;
				}
			}
		}

		/** 関連項目 */
		// 「社宅を退居する」、「駐車場１を返還する」、「駐車場２を返還する」の全て
		LogUtils.debugByMsg(msg + "退居（返還）する社宅又は、自動車の保管場所:社宅" + checkDto.getTaikyoTypeShataku());
		LogUtils.debugByMsg(msg + "退居（返還）する社宅又は、自動車の保管場所:駐車場1" + checkDto.getTaikyoTypeParking1());
		LogUtils.debugByMsg(msg + "退居（返還）する社宅又は、自動車の保管場所:駐車場2" + checkDto.getTaikyoTypeParking2());
		if (FALSE.equals(checkDto.getTaikyoTypeShataku()) && FALSE.equals(checkDto.getTaikyoTypeParking1())
				&& FALSE.equals(checkDto.getTaikyoTypeParking2())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoTypeTable" },
					MessageIdConstant.E_SKF_1054, "退居（返還）する社宅又は、自動車の保管場所");
			isError = true;
		}

		// 「退居（返還）理由 その他」
		// 「退居（返還）理由」でその他が選択されている場合に必須
		LogUtils.debugByMsg(msg + "退居（返還）理由" + checkDto.getTaikyoRiyuKbn());
		LogUtils.debugByMsg(msg + "退居（返還）理由　その他" + checkDto.getTaikyoHenkanRiyu());
		if (CodeConstant.TAIKYO_RIYU_OTHERS.equals(checkDto.getTaikyoRiyuKbn())
				&& NfwStringUtils.isBlank(checkDto.getTaikyoHenkanRiyu())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoHenkanRiyu" },
					MessageIdConstant.E_SKF_1048, "退居（返還）理由");
			isError = true;
		}

		return isError;
	}

	/**
	 * 文字種を判定
	 * 
	 * @param checkDto
	 * @return isError エラーが存在する場合True
	 */
	private boolean checkValueType(Skf2040Sc001CheckAsyncDto checkDto) {

		boolean isError = false;
		String msg = "文字種類判定";

		// 連絡先
		LogUtils.debugByMsg(msg + "連絡先" + checkDto.getRenrakuSaki());
		if (NfwStringUtils.isNotBlank(checkDto.getRenrakuSaki())
				&& !SkfCheckUtils.isSkfPhoneFormat(checkDto.getRenrakuSaki())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" }, MessageIdConstant.E_SKF_1003,
					"連絡先");
			isError = true;
		}

		return isError;
	}

	/**
	 * バイト数を判定
	 * 
	 * @param checkDto
	 * @return isError エラーが存在する場合True
	 */
	private boolean checkByteCount(Skf2040Sc001CheckAsyncDto checkDto) throws UnsupportedEncodingException {

		boolean isError = false;

		// 「退居（返還）理由 その他」
		LogUtils.debugByMsg("桁数チェック " + "退居（返還）理由　その他" + checkDto.getTaikyoHenkanRiyu());
		if (NfwStringUtils.isNotBlank(checkDto.getTaikyoHenkanRiyu())
				&& CheckUtils.isMoreThanByteSize(checkDto.getTaikyoHenkanRiyu().trim(), 256)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoHenkanRiyu" },
					MessageIdConstant.E_SKF_1071, "退居（返還）理由　その他", "全角128");
			isError = true;
		}

		// 「社宅の状態」
		LogUtils.debugByMsg("桁数チェック " + "社宅の状態" + checkDto.getShatakuJotai());
		if (NfwStringUtils.isNotBlank(checkDto.getShatakuJotai())
				&& CheckUtils.isMoreThanByteSize(checkDto.getShatakuJotai().trim(), 128)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "shatakuJotai" }, MessageIdConstant.E_SKF_1071,
					"社宅の状態", "全角64");
			isError = true;
		}

		// 「退居後の連絡先」
		LogUtils.debugByMsg("桁数チェック " + "退居後の連絡先" + checkDto.getTaikyogoRenrakuSaki());
		if (NfwStringUtils.isNotBlank(checkDto.getTaikyogoRenrakuSaki())
				&& CheckUtils.isMoreThanByteSize(checkDto.getTaikyogoRenrakuSaki().trim(), 128)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyogoRenrakuSaki" },
					MessageIdConstant.E_SKF_1071, "退居後の連絡先", "全角64");
			isError = true;
		}

		// 連絡先
		LogUtils.debugByMsg("桁数チェック " + "連絡先" + checkDto.getRenrakuSaki());
		if (NfwStringUtils.isNotBlank(checkDto.getRenrakuSaki())
				&& CheckUtils.isMoreThanByteSize(checkDto.getRenrakuSaki().trim(), 13)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" }, MessageIdConstant.E_SKF_1049,
					"連絡先", "13");
			isError = true;
		}
		return isError;
	}

	/**
	 * 日付の整合性、形式を判定
	 * 
	 * @param checkDto
	 * @return isError エラーが存在する場合True
	 */
	private boolean checkDateFormat(Skf2040Sc001CheckAsyncDto checkDto) {

		boolean isError = false;

		// 「退居(返還)日」
		LogUtils.debugByMsg("形式チェック " + "退居(返還)日" + checkDto.getTaikyoHenkanDate());
		if (NfwStringUtils.isNotBlank(checkDto.getTaikyoHenkanDate()) && !SkfCheckUtils
				.isSkfDateFormat(checkDto.getTaikyoHenkanDate().trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoHenkanDate" },
					MessageIdConstant.E_SKF_1055, "退居(返還)日");
			isError = true;
		}

		// 「返却希望立会日（日）」
		LogUtils.debugByMsg("形式チェック " + "返却希望立会日（日）" + checkDto.getSessionDay());
		if (NfwStringUtils.isNotBlank(checkDto.getSessionDay()) && !SkfCheckUtils
				.isSkfDateFormat(checkDto.getSessionDay().trim(), CheckUtils.DateFormatType.YYYYMMDD)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionDay" }, MessageIdConstant.E_SKF_1055,
					"返却希望立会日（日）");
			isError = true;
		}

		return isError;
	}

	/**
	 * 関連項目チェックを実施
	 * 
	 * @param checkDto
	 * @return isError エラーが存在する場合True
	 */
	private boolean checkRelation(Skf2040Sc001CheckAsyncDto checkDto) {
		boolean isError = false;

		// 駐車場を借りているが、「社宅を退居する」チェックのみの場合
		LogUtils.debugByMsg("関連項目チェック " + "駐車場を借りているが、「社宅を退居する」のみチェックしていないか");
		if (( // 駐車場保管場所1または2が空でない
		NfwStringUtils.isNotBlank(checkDto.getParking1stPlace())
				|| NfwStringUtils.isNotBlank(checkDto.getParking2ndPlace())) && ( // かつ、駐車場1または2の返還チェックがされていない
		NfwStringUtils.isBlank(checkDto.getTaikyoTypeParking1())
				&& NfwStringUtils.isBlank(checkDto.getTaikyoTypeParking2()))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoType" }, MessageIdConstant.E_SKF_3004);
		}

		// 選択した駐車場を借りていない場合
		LogUtils.debugByMsg("関連項目チェック " + "駐車場1を借りていないのに「駐車場1を返還」がチェックされていないか");
		if (NfwStringUtils.isBlank(checkDto.getParking1stPlace())
				&& NfwStringUtils.isNotBlank(checkDto.getTaikyoTypeParking1())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoType" }, MessageIdConstant.E_SKF_2014);
		}

		LogUtils.debugByMsg("関連項目チェック " + "駐車場2を借りていないのに「駐車場2を返還」がチェックされていないか");
		if (NfwStringUtils.isBlank(checkDto.getParking2ndPlace())
				&& NfwStringUtils.isNotBlank(checkDto.getTaikyoTypeParking2())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoType" }, MessageIdConstant.E_SKF_2014);
		}

		return isError;
	}
}
