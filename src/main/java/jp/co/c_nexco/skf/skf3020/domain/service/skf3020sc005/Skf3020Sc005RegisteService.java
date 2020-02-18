/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.ibm.icu.text.SimpleDateFormat;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005UpdateTenninshaInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc005.Skf3020Sc005UpdateTenninshaInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
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
	@Autowired
	private Skf3020Sc005UpdateTenninshaInfoExpRepository skf3020Sc005UpdateTenninshaInfoExpRepository;

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
//			TransferPageInfo nextPage = TransferPageInfo.nextPage(FunctionIdConstant.SKF3020_SC004, "init");
			TransferPageInfo nextPage = TransferPageInfo.prevPage(FunctionIdConstant.SKF3020_SC004, "init");
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

		if (!checkShainNo(registDto.getShainNo())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "shainNo" }, MessageIdConstant.E_SKF_1044,
					"社員番号");
			result = false;
		}

		if (NfwStringUtils.isEmpty(registDto.getTxtShainMei().trim())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "txtShainMei" }, MessageIdConstant.E_SKF_1044,
					"社員氏名");
			result = false;
		}
		if (!CheckUtils.isEmpty(registDto.getTxtNenrei()) && !CheckUtils.isHalfWidth(registDto.getTxtNenrei())) {
			ServiceHelper.addErrorResultMessage(registDto, new String[] { "txtNenrei" }, MessageIdConstant.E_SKF_1002,
					"年齢");
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

		String chkShainNoHenkoKbn = Skf302010CommonSharedService.NOT_CHECKED;
		String[] checkBoxArray = registDto.getId_check_shainNo();

		if (checkBoxArray.length != 0 && Skf302010CommonSharedService.CHECKED.equals(checkBoxArray[0])) {
			chkShainNoHenkoKbn = Skf302010CommonSharedService.CHECKED;
		}

		String dbErrMsgId = "";
		String oldShainNo = registDto.getHdnRowShainNo();

		//転任者情報がない場合（登録）転任者調書を登録する
		if (NfwStringUtils.isEmpty(oldShainNo)) {
			// DB登録
			dbErrMsgId = skf3020Sc005SharedService.insertTenninshaInfo(registDto, chkShainNoHenkoKbn);

		} else {
			//転任者情報がある場合（編集）転任者調書を更新する
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
		String newShainNo = registDto.getShainNo();

		// 転任者調書データを取得
		Skf3020TTenninshaChoshoData tenninshaInfo = skf3020TTenninshaChoshoDataRepository
				.selectByPrimaryKey(oldShainNo);

		if (tenninshaInfo != null) {

			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
			// 転任者調書データ排他チェック
			if (!NfwStringUtils.isEmpty(registDto.getHdnUpdateDateTenninsha())) {
				Date tenninChoshoLastUpdateDate = sdFormat.parse(registDto.getHdnUpdateDateTenninsha());
				registDto.addLastUpdateDate(Skf302010CommonSharedService.TENNIN_CHOSHO_DATA_UPDATE_KEY,
						tenninChoshoLastUpdateDate);
				super.checkLockException(
						registDto.getLastUpdateDate(Skf302010CommonSharedService.TENNIN_CHOSHO_DATA_UPDATE_KEY),
						tenninshaInfo.getUpdateDate());
			}
			
			// 変更先転任者調書データを取得
			Skf3020TTenninshaChoshoData newTenninshaInfo = skf3020TTenninshaChoshoDataRepository
					.selectByPrimaryKey(newShainNo);
			if(newTenninshaInfo != null && !Objects.equal(oldShainNo, newShainNo)){
				//変更先（同一社員番号が既にある）
				return MessageIdConstant.E_SKF_1020;
			}
			
			Skf3020Sc005UpdateTenninshaInfoExp updateData = skf3020Sc005SharedService.createUpdateTenninshaInfoData(registDto,
					newShainNo, chkShainNoHenkoKbn, tenninshaInfo);
			updateData.setOldShainNo(oldShainNo);
			// 転任者調書更新
			int rtn = skf3020Sc005UpdateTenninshaInfoExpRepository.updateTenninshaInfo(updateData);

			if (rtn <= 0) {
				return MessageIdConstant.W_SKF_1009;
			}

			if (!Skf302010CommonSharedService.KARI_K.equals(oldShainNo.substring(0, 1))) {//仮社員番号以外の場合
				// 社宅社員マスタデータ排他チェック
				if (!NfwStringUtils.isEmpty(registDto.getHdnUpdateDateShain())) {
					Date shatakuShainLastUpdateDate = sdFormat.parse(registDto.getHdnUpdateDateShain());
					registDto.addLastUpdateDate(Skf302010CommonSharedService.SHAIN_DATA_UPDATE_KEY, shatakuShainLastUpdateDate);
					Skf1010MShain shatakuShainData = skf3020Sc005SharedService.getShainData(oldShainNo);
					super.checkLockException(registDto.getLastUpdateDate(Skf302010CommonSharedService.SHAIN_DATA_UPDATE_KEY),
							shatakuShainData.getUpdateDate());
				}
				
				if (oldShainNo.equals(newShainNo)) {
					//社員番号が未変更
					rtn = skf3020Sc005SharedService.updateShatakuShain(oldShainNo,  chkShainNoHenkoKbn);

				} else {
					//社員番号が変更
					rtn = skf3020Sc005SharedService.updateShatakuShain(newShainNo,  chkShainNoHenkoKbn);
					
					if (rtn <= 0) {
						//更新できなかった場合
						return MessageIdConstant.W_SKF_1009;
					}
					
					//社員番号を変更した場合、社宅社員マスタ（TB_M_SHATAKU_SHAIN）の変更前の社員番号の下記設定を戻す。
					rtn = skf3020Sc005SharedService.updateShatakuShain(oldShainNo, "0");
				}

				
			}else {//仮社員番号の場合、
				if (!oldShainNo.equals(newShainNo)) {
					rtn = skf3020Sc005SharedService.updateShatakuShain(newShainNo, chkShainNoHenkoKbn);
				}
			}
			
			if (rtn <= 0) {
				return MessageIdConstant.W_SKF_1009;
			} else {
				dbErrMsgId = "";
			}

		} else {
			dbErrMsgId = MessageIdConstant.W_SKF_1009;
		}

		return dbErrMsgId;
	}

}
