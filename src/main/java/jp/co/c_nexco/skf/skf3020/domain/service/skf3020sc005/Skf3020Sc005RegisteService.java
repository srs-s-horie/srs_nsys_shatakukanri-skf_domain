/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.app.TransferPageInfo;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020sc005.Skf3020Sc005RegisteDto;
import jp.co.c_nexco.skf.skf3020.domain.service.common.Skf302010CommonSharedService;

/**
 * Skf3020Sc005RegisteService 転任者登録処理クラス
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3020Sc005RegisteService extends BaseServiceAbstract<Skf3020Sc005RegisteDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	@Autowired
	private Skf302010CommonSharedService skf302010CommonSharedService;
	@Autowired
	private Skf3020Sc005SharedService skf3020Sc005SharedService;
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;

	@Override
	public Skf3020Sc005RegisteDto index(Skf3020Sc005RegisteDto registDto) throws Exception {

		skfOperationLogUtils.setAccessLog("転任者情報の登録", CodeConstant.C001, registDto.getPageId());

		// 入力チェック
		if (!checkToroku(registDto)) {
			return registDto;
		}

		// 転任者調書登録
		registDto = registTenninshaInfo(registDto);

		if (registDto.getResultMessages() == null) {
			// 画面遷移(転任者一覧画面へ遷移)
			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			registDto.setTransferPageInfo(nextPage);
		}

		return registDto;
	}

	/**
	 * 入力チェックを行う。
	 * 
	 * @param registDto
	 *            Skf3020Sc005RegisteDto
	 * @return 結果
	 */
	private boolean checkToroku(Skf3020Sc005RegisteDto registDto) {

		boolean result = true;
		registDto.setResultMessages(null);

		if (!checkShainNo(registDto.getTxtShainNo())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "txtShainNo" }, MessageIdConstant.E_SKF_1044,
					"社員番号");
			result = false;
		}

		if (NfwStringUtils.isEmpty(registDto.getTxtShainMei().trim())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "txtShainMei" }, MessageIdConstant.E_SKF_1044,
					"社員氏名");
			result = false;
		}

		return result;
	}

	/**
	 * 社員番号をチェック
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 結果
	 */
	private boolean checkShainNo(String shainNo) {

		boolean rtn = true;

		if (NfwStringUtils.isEmpty(shainNo.trim())) {
			rtn = false;
		}

		if (skf302010CommonSharedService.isExistSpace(shainNo.trim())) {
			rtn = false;
		}

		if (!shainNo.matches("^[a-zA-Z0-9]*$")) {
			rtn = false;
		}

		return rtn;
	}

	/**
	 * 転任者情報の登録を行う。
	 * 
	 * @param registDto
	 *            Skf3020Sc005RegisteDto
	 * @return Skf3020Sc005RegisteDto
	 * @throws ParseException
	 */
	private Skf3020Sc005RegisteDto registTenninshaInfo(Skf3020Sc005RegisteDto registDto) throws ParseException {

		String chkShainNoHenkoKbn = skf302010CommonSharedService.NOT_CHECKED;
		String[] checkBox = registDto.getId_check_shainNo();

		if (skf302010CommonSharedService.CHECKED.equals(checkBox[0])) {
			chkShainNoHenkoKbn = skf302010CommonSharedService.CHECKED;
		}

		String dbErrMsgId = "";
		String oldShainNo = registDto.getHdnRowShainNo();

		if (NfwStringUtils.isEmpty(oldShainNo)) {
			// DB登録
			dbErrMsgId = skf3020Sc005SharedService.insertTenninshaInfo(registDto, chkShainNoHenkoKbn);

		} else {
			// DB更新
			dbErrMsgId = updateTenninshaInfo(registDto, chkShainNoHenkoKbn);
		}

		registDto.setResultMessages(null);

		if (!"".equals(dbErrMsgId)) {
			ServiceHelper.addErrorResultMessage(registDto, null, dbErrMsgId);
		}

		return registDto;
	}

	/**
	 * 転任者情報の更新を行う。
	 * 
	 * @param registDto
	 *            Skf3020Sc005RegisteDto
	 * @param chkShainNoHenkoKbn
	 *            社員番号変更対象区分
	 * @return DB処理時のエラーメッセージID
	 * @throws ParseException
	 */
	@Transactional
	private String updateTenninshaInfo(Skf3020Sc005RegisteDto registDto, String chkShainNoHenkoKbn)
			throws ParseException {

		String dbErrMsgId = "";
		String oldShainNo = registDto.getHdnRowShainNo();
		String newShainNo = registDto.getTxtShainNo();

		// 転任者調書データを取得
		Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository
				.selectByPrimaryKey(oldShainNo);

		if (tenninshaInfo != null) {

			Skf3020TTenninshaChoshoData updateData = skf3020Sc005SharedService.createUpdateTenninshaInfoData(registDto,
					newShainNo, chkShainNoHenkoKbn, tenninshaInfo);

			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			// 転任者調書データ排他チェック
			if (!NfwStringUtils.isEmpty(registDto.getHdnUpdateDateTenninsha())) {
				Date tenninChoshoLastUpdateDate = sdFormat.parse(registDto.getHdnUpdateDateTenninsha());
				registDto.addLastUpdateDate(skf302010CommonSharedService.TENNIN_CHOSHO_DATA_UPDATE_KEY,
						tenninChoshoLastUpdateDate);
				super.checkLockException(
						registDto.getLastUpdateDate(skf302010CommonSharedService.TENNIN_CHOSHO_DATA_UPDATE_KEY),
						tenninshaInfo.getUpdateDate());
			}

			// 転任者調書更新
			int rtn = skf3020TTenninshaChoshoDataRepository.updateByPrimaryKey(updateData);

			if (rtn <= 0) {
				return MessageIdConstant.W_SKF_1009;
			}

			// 社宅社員マスタデータ排他チェック
			if (!NfwStringUtils.isEmpty(registDto.getHdnUpdateDateShain())) {
				Date shatakuShainLastUpdateDate = sdFormat.parse(registDto.getHdnUpdateDateShain());
				registDto.addLastUpdateDate(skf302010CommonSharedService.SHAIN_DATA_UPDATE_KEY, shatakuShainLastUpdateDate);
				Skf1010MShain shatakuShainData = skf3020Sc005SharedService.getShainData(newShainNo);
				super.checkLockException(registDto.getLastUpdateDate(skf302010CommonSharedService.SHAIN_DATA_UPDATE_KEY),
						shatakuShainData.getUpdateDate());
			}

			// 「社宅社員マスタ」更新
			rtn = skf3020Sc005SharedService.updateShatakuShain(oldShainNo, newShainNo, chkShainNoHenkoKbn);

			if (rtn <= 0) {
				return MessageIdConstant.E_SKF_1009;
			}

			switch (rtn) {
			case -1:
				dbErrMsgId = MessageIdConstant.E_SKF_1075;
				break;
			case 0:
				dbErrMsgId = MessageIdConstant.W_SKF_1009;
				break;
			default:
				dbErrMsgId = "";
				break;
			}

		} else {
			dbErrMsgId = MessageIdConstant.W_SKF_1009;
		}

		return dbErrMsgId;
	}

}
