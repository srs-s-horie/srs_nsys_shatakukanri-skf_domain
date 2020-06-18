/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2040.domain.service.skf2040sc001;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf2040TTaikyoReport;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.BaseForm;
import jp.co.c_nexco.nfw.webcore.app.FormHelper;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2040.domain.dto.skf2040sc001.Skf2040Sc001ConfirmDto;

/**
 * Skf2040Sc001 退居（自動車の保管場所返還）届画面の一時保存処理。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf2040Sc001ConfirmService extends SkfServiceAbstract<Skf2040Sc001ConfirmDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf2040Sc001SharedService skf2040Sc001SharedService;

	/**
	 * サービス処理を行う。
	 * 
	 * @param confirmDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2040Sc001ConfirmDto index(Skf2040Sc001ConfirmDto confirmDto) throws Exception {
		confirmDto.setPageTitleKey(MessageIdConstant.SKF2040_SC001_TITLE);

		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("申請内容を確認", CodeConstant.C001, FunctionIdConstant.SKF2040_SC001);

		// 申請書情報の取得
		skf2040Sc001SharedService.setSkfApplInfo(confirmDto);

		// バイトカット処理
		skf2040Sc001SharedService.cutByte(confirmDto);

		// 一時保存処理を行う
		if (!this.execSave(confirmDto)) {
			return confirmDto;
		}
		// プルダウンリストの再設定（選択初期値反映）
		this.setDropdownList(confirmDto);

		// 申請書類確認画面に渡すフォームデータを設定
		confirmDto.setPrePageId(confirmDto.getPageId());
		BaseForm form = new BaseForm();
		CopyUtils.copyProperties(form, confirmDto);
		FormHelper.setFormBean(FunctionIdConstant.SKF2010_SC002, form);

		// 申請書類確認画面に遷移
		TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF2010_SC002);
		Map<String, Object> attributeMap = new HashMap<String, Object>();
		attributeMap.put(SkfCommonConstant.KEY_APPL_NO, confirmDto.getApplNo());
		attributeMap.put(SkfCommonConstant.KEY_STATUS, confirmDto.getApplStatus());
		nextPage.setTransferAttributes(attributeMap);
		confirmDto.setTransferPageInfo(nextPage, true);
		return confirmDto;
	}

	/**
	 * 退居届画面での一時保存処理を実行する。
	 * 
	 * <pre>
	 * 下記のテーブルについて、すでにレコードが存在する場合は更新処理、
	 * レコードが存在しない場合は新規登録処理を行う。
	 *   ・申請履歴
	 *   ・退居届
	 *   ・備品返却申請
	 * </pre>
	 * 
	 * @param dto
	 * @return 登録成功 true 失敗 false
	 */
	private boolean execSave(Skf2040Sc001ConfirmDto confirmDto) {
		boolean isExecSave = false;

		if (CodeConstant.STATUS_MISAKUSEI.equals(confirmDto.getApplStatus())) {
			// 新規保存の場合

			// 申請履歴、退居届の登録
			isExecSave = skf2040Sc001SharedService.saveNewTaikyoData(confirmDto);

			// 退居社宅がある場合は備品返却の作成 ※旧システムでは駐車場返還のみの場合でも備品返却情報を作成している
			// 備品返却申請テーブル登録処理
			skf2040Sc001SharedService.registrationBihinShinsei(confirmDto);

		} else {
			// 更新の場合

			// 排他チェック（退居届テーブルの更新有無で判定）
			Skf2040TTaikyoReport taikyoInfo = skf2040Sc001SharedService.getExistTaikyoInfo(confirmDto.getApplNo());
			if (taikyoInfo == null) {
				// 退居届情報が見つからなかった場合エラーメッセージを表示して処理終了
				skf2040Sc001SharedService.setDisableBtn(confirmDto);
				ServiceHelper.addErrorResultMessage(confirmDto, null, MessageIdConstant.E_SKF_1077);
				return false;
			}

			super.checkLockException(
					confirmDto.getLastUpdateDate(confirmDto.UPDATE_TABLE_PREFIX_TAIKYO_REPORT + confirmDto.getApplNo()),
					taikyoInfo.getUpdateDate());

			// 申請履歴、退居届の更新
			isExecSave = skf2040Sc001SharedService.updateTaikyoData(confirmDto);

			// 備品返却申請テーブル登録処理
			skf2040Sc001SharedService.registrationBihinShinsei(confirmDto);
		}
		return isExecSave;
	}

	/**
	 * 退居届画面のドロップダウンリスト情報を設定する。
	 * 
	 * @param confirmDto
	 */
	private void setDropdownList(Skf2040Sc001ConfirmDto confirmDto) {
		// 退居(返還)理由ドロップダウンリストをDtoに設定
		confirmDto.setDdlTaikyoRiyuKbnList(
				skf2040Sc001SharedService.getTaikyoHenkanRiyuList(confirmDto.getTaikyoRiyuKbn()));

		// 現居住社宅名ドロップダウンリストをDtoに設定
		List<Map<String, Object>> nowShatakuNameList = skf2040Sc001SharedService.getNowShatakuNameList(
				confirmDto.getShainNo(), Long.parseLong(NfwStringUtils.defaultString(confirmDto.getNowShatakuName())));
		if (null != nowShatakuNameList && nowShatakuNameList.size() > 0) {
			confirmDto.setDdlNowShatakuNameList(nowShatakuNameList);
			// リスト一件目の物件の社宅管理IDを取得してDTOに設定しておく
			long firstShatakuKanriId = (long) nowShatakuNameList.get(0).get("value");
			confirmDto.setShatakuKanriId(firstShatakuKanriId);
		} else {
			// データが取得できなかった場合、エラーメッセージを表示して初期表示処理を終了
			skf2040Sc001SharedService.setDisableBtn(confirmDto);
			// エラーメッセージ表示
			ServiceHelper.addErrorResultMessage(confirmDto, null, MessageIdConstant.E_SKF_2015);
		}

		// 返却立合希望日（時）ドロップダウンリストをDtoに設定
		confirmDto.setDdlReturnWitnessRequestDateList(
				skf2040Sc001SharedService.getSessionTimeList(confirmDto.getSessionTime()));
	}
}
