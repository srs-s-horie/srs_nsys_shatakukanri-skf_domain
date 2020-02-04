/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3020.domain.service.skf3020sc005;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.icu.text.SimpleDateFormat;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc003.Skf3020Sc003GetShatakuNyukyoCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005GetShatakuShainCountExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3020Sc005.Skf3020Sc005UpdateTenninshaInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf3020TTenninshaChoshoData;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc003.Skf3020Sc003GetShatakuNyukyoCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc005.Skf3020Sc005GetShatakuShainCountExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3020Sc005.Skf3020Sc005GetTenninshaShatakuInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf3020TTenninshaChoshoDataRepository;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.common.utils.PropertyUtils;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfBaseBusinessLogicUtils;
import jp.co.c_nexco.skf.common.util.SkfFileOutputUtils;
import jp.co.c_nexco.skf.skf3020.domain.dto.skf3020Sc005common.Skf3020Sc005CommonDto;
import jp.co.c_nexco.skf.skf3020.domain.service.common.Skf302010CommonSharedService;
import jp.co.intra_mart.common.platform.log.Logger;

/**
 * Skf3020Sc005SharedService 転任者登録内共通クラス
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3020Sc005SharedService {

	@Autowired
	private Skf302010CommonSharedService skf302010CommonSharedService;
	@Autowired
	private Skf3020Sc005GetTenninshaShatakuInfoExpRepository skf3020Sc005GetTenninshaShatakuInfoExpRepository;
	@Autowired
	private SkfBaseBusinessLogicUtils skfBaseBusinessLogicUtils;
	@Autowired
	private Skf3020TTenninshaChoshoDataRepository skf3020TTenninshaChoshoDataRepository;
	@Autowired
	private Skf3020Sc003GetShatakuNyukyoCountExpRepository skf3020Sc003GetShatakuNyukyoCountExpRepository;
	@Autowired
	private Skf3020Sc005GetShatakuShainCountExpRepository skf3020Sc005GetShatakuShainCountExpRepository;
	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;

	/** ロガー */
	private static Logger logger = LogUtils.getLogger(SkfFileOutputUtils.class);

	/**
	 * 変更フラグ false：新規 true：変更
	 */
	private Boolean changeFlg;

	/**
	 * 転任者情報を取得する。 パラメータの社員番号と一致する転任者情報を取得する。
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 転任者情報
	 * @throws ParseException
	 */
	private Skf3020Sc005GetTenninshaShatakuInfoExp getTenninshaShatakuInfo(String shainNo) throws ParseException {

		logger.debug("getTenninshaShatakuInfo, 社員番号:" + shainNo);

		// リストテーブルに格納するデータを取得する
		List<Skf3020Sc005GetTenninshaShatakuInfoExp> resultListTableData = new ArrayList<Skf3020Sc005GetTenninshaShatakuInfoExp>();
		Skf3020Sc005GetTenninshaShatakuInfoExpParameter param = new Skf3020Sc005GetTenninshaShatakuInfoExpParameter();
		// SQLパラメータ設定
		param.setShainNo(shainNo);
		resultListTableData = skf3020Sc005GetTenninshaShatakuInfoExpRepository.getTenninshaShatakuInfo(param);

		Skf3020Sc005GetTenninshaShatakuInfoExp resultTableData = null;
		// 取得データレコード数判定
		if (resultListTableData.size() == 0) {
			// 取得データレコード数が0件の場合、何もせず処理終了
			return resultTableData;
		}

		resultTableData = resultListTableData.get(0);
		resultListTableData.clear();

		return resultTableData;
	}

	/**
	 * 転任者情報を設定
	 * 
	 * @param initDto
	 *            Skf3020Sc005InitDto
	 * @param shainNo
	 *            社員番号
	 * @throws ParseException
	 */
	public void setTenninshaInfo(Skf3020Sc005CommonDto inDto, String shainNo) throws ParseException {
		shainNo = shainNo.replace(CodeConstant.ASTERISK,"");
		// 転任者情報取得
		Skf3020Sc005GetTenninshaShatakuInfoExp resultTableData = getTenninshaShatakuInfo(shainNo);

		if (resultTableData != null) {

			inDto.setShainNo(skf302010CommonSharedService.cnvString(resultTableData.getShainNo()));

			String shainNoHenkoKbn = resultTableData.getShainNoHenkoKbn();
			String chkShainNoHenkoKbn = ""; // 仮社員番号設定(社員番号の変更の要否)

			if (NfwStringUtils.isEmpty(shainNoHenkoKbn)
					|| shainNoHenkoKbn.equals(Skf302010CommonSharedService.MI_SAKUSEI)) {
				chkShainNoHenkoKbn = "";
			} else {
				chkShainNoHenkoKbn = Skf302010CommonSharedService.CHECKED;
			}
			// 仮社員番号設定
			String[] checkBox = new String[] { chkShainNoHenkoKbn };
			inDto.setId_check_shainNo(checkBox);

			inDto.setTxtShainMei(skf302010CommonSharedService.cnvString(resultTableData.getShainName()));
			inDto.setTxtTokyu(skf302010CommonSharedService.cnvString(resultTableData.getTokyu()));
			inDto.setTxtNenrei(skf302010CommonSharedService.cnvString(resultTableData.getAge()));
			String newAffiliation = skf302010CommonSharedService.cnvString(resultTableData.getNewAffiliation());
			inDto.setTxtShinShozoku(newAffiliation.replaceAll(Skf302010CommonSharedService.DB_LS,
					Skf302010CommonSharedService.DISPLAY_LS));
			String nowAffiliation = skf302010CommonSharedService.cnvString(resultTableData.getNowAffiliation());
			inDto.setTxtGenShozoku(nowAffiliation.replaceAll(Skf302010CommonSharedService.DB_LS,
					Skf302010CommonSharedService.DISPLAY_LS));
			inDto.setTxtBiko(skf302010CommonSharedService.cnvString(resultTableData.getBiko()));

			String nyutaikyoKbn = resultTableData.getNyutaikyoYoteiKbn();
			if (nyutaikyoKbn == null || StringUtils.isEmpty(nyutaikyoKbn)) {
				nyutaikyoKbn = "";
			}
			inDto.setHdnNyutaikyoYoteiKbn(nyutaikyoKbn);

			inDto.setHdnUpdateDateTenninsha(
					skf302010CommonSharedService.cnvString(resultTableData.getUpdateDateTenninsha()));
			inDto.setHdnUpdateDateShain(skf302010CommonSharedService.cnvString(resultTableData.getUpdateDateShain()));
			// 変更の場合
			changeFlg = true;

		} else {
			setInitInfo(inDto);
		}

	}

	/**
	 * 画面初期化の設定(新規)
	 * 
	 * @param initDto
	 *            Skf3020Sc005InitDto
	 * @return Skf3020Sc005InitDto
	 */
	public void setInitInfo(Skf3020Sc005CommonDto inDto) {

		inDto.setShainNo("");
		String[] checkBox = new String[] { "" };
		inDto.setId_check_shainNo(checkBox);
		inDto.setTxtShainMei("");
		inDto.setTxtTokyu("");
		inDto.setTxtNenrei("");
		inDto.setTxtShinShozoku("");
		inDto.setTxtGenShozoku("");
		inDto.setTxtBiko("");
		inDto.setHdnNyutaikyoYoteiKbn("");
		inDto.setHdnUpdateDateTenninsha("");
		inDto.setHdnUpdateDateShain("");
		changeFlg = false;
	}

	/**
	 * 画面項目制御用のhidden初期化
	 * 
	 * @param inDto
	 *            Skf3020Sc005CommonDto
	 */
	private void initDisplayItems(Skf3020Sc005CommonDto inDto) {
		inDto.setMaskFlg(false);
		inDto.setMaskPattern("");
		inDto.setHdnBtnTorokuDisabled(false);
		inDto.setHdnChkShainNoHenkoKbnDisabled(false);
	}

	/**
	 * 画面項目制御の判定用のhidden設定
	 * 
	 * @param initDto
	 *            Skf3020Sc005InitDto
	 * @return Skf3020Sc005InitDto
	 */
	public void setControlStatus(Skf3020Sc005CommonDto inDto) {

		initDisplayItems(inDto);

		if (changeFlg) {
			inDto.setMaskFlg(true);
			inDto.setMaskPattern("CHANGE");

			String hdnNyutaikyoYoteiKbn = inDto.getHdnNyutaikyoYoteiKbn();
			if (NfwStringUtils.isEmpty(hdnNyutaikyoYoteiKbn)
					|| Skf302010CommonSharedService.MI_SAKUSEI.equals(hdnNyutaikyoYoteiKbn)) {
				// 入退居予定作成区分：未作成
				inDto.setTxtDisabled(false);

			} else {
				// 入退居予定作成区分：作成済
				inDto.setTxtDisabled(true);
				inDto.setHdnChkShainNoHenkoKbnDisabled(true);
				inDto.setHdnBtnTorokuDisabled(true);
				inDto.setMaskPattern("CREATED");
			}

		} else {
			// 入退居予定作成区分：未作成
			inDto.setTxtDisabled(false);
		}

	}

	/**
	 * ボタン押下時に表示するメッセージ設定
	 * 
	 * @param initDto
	 *            Skf3020Sc005InitDto
	 */
	public void setBtnEnterMsg(Skf3020Sc005CommonDto inDto) {
		String enterMsg = "";

		if (changeFlg) {
			// 更新の場合
			enterMsg = PropertyUtils.getValue(MessageIdConstant.I_SKF_3039);

		} else {
			// 新規の場合
			enterMsg = PropertyUtils.getValue(MessageIdConstant.I_SKF_3062);
		}

		inDto.setEnterMsg(enterMsg);

		String backMsg = PropertyUtils.getValue(MessageIdConstant.I_SKF_1009);
		inDto.setBackMsg(backMsg);
	}

	/**
	 * 転任者調書を登録する。
	 * 
	 * @param inDto
	 *            Skf3020Sc005CommonDto
	 * @param chkShainNoHenkoKbn
	 *            社員番号変更対象区分
	 * @return メッセージID
	 * @throws ParseException
	 */
	@Transactional
	public String insertTenninshaInfo(Skf3020Sc005CommonDto inDto, String chkShainNoHenkoKbn) throws ParseException {

		String outMsgId = "";
		String shainNo = inDto.getShainNo();
		int result = Skf302010CommonSharedService.DB_RESULT_NORMAL;
		Skf3020Sc005GetTenninshaShatakuInfoExp existData = getTenninshaShatakuInfo(shainNo);

		if (existData == null) {
			Skf3020TTenninshaChoshoData insertData = createInsertTenninshaInfoData(inDto, shainNo, chkShainNoHenkoKbn);
			result = skf3020TTenninshaChoshoDataRepository.insert(insertData);

			if (result <= 0) {
				outMsgId = MessageIdConstant.W_SKF_1010;
				return outMsgId;
			}

			Skf3020Sc005GetShatakuShainCountExpParameter param = new Skf3020Sc005GetShatakuShainCountExpParameter();
			param.setShainNo(shainNo);
			// 「社宅社員マスタ」からデータを取得する
			int keyCntShain = skf3020Sc005GetShatakuShainCountExpRepository.getShatakuShainCount(param);

			if (keyCntShain != 0) {
				// 「社宅社員マスタ」へ更新
				Skf1010MShain updateData = new Skf1010MShain();
				updateData.setShainNoChangeFlg(chkShainNoHenkoKbn);
				updateData.setShainNo(shainNo);
				int updateResult = skf1010MShainRepository.updateByPrimaryKeySelective(updateData);

				if (updateResult <= 0) {
					result = 0;
				}
			}

			switch (result) {
			case 0:
				outMsgId = MessageIdConstant.W_SKF_1010;
				break;
			case -1:
				outMsgId = MessageIdConstant.E_SKF_1073;
				break;
			case -2:
				outMsgId = MessageIdConstant.E_SKF_1063;
				break;
			default:
				outMsgId = "";
				break;
			}

		} else {
			outMsgId = MessageIdConstant.E_SKF_1063;
		}

		return outMsgId;
	}

	/**
	 * 社宅社員マスタへの更新
	 * 
	 * @param shainNo
	 *            変更社員番号
	 * @param chkShainNoHenkoKbn
	 *            社員番号変更対象区分
	 * @return DB処理結果
	 */
	public int updateShatakuShain(String shainNo, String chkShainNoHenkoKbn) {

		// 「社宅社員マスタ」へ更新
		Skf1010MShain updateData = new Skf1010MShain();
		updateData.setCompanyCd(CodeConstant.C001);
		updateData.setShainNo(shainNo);
		updateData.setShainNoChangeFlg(chkShainNoHenkoKbn);
		int result = skf1010MShainRepository.updateByPrimaryKeySelective(updateData);

		if (result <= 0) {
			return 0;
		}

		return result;
	}

	/**
	 * 現社宅区分を取得する。
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 現社宅区分
	 */
	public String getGenShatakuKbn(String shainNo) {

		if (NfwStringUtils.isEmpty(shainNo)) {
			return Skf302010CommonSharedService.MI_NYUKYO;
		}

		Skf3020Sc003GetShatakuNyukyoCountExpParameter param = new Skf3020Sc003GetShatakuNyukyoCountExpParameter();
		param.setShainNo(shainNo);
		String sysShoriNenGetsu = skfBaseBusinessLogicUtils.getSystemProcessNenGetsu();
		param.setSysShoriNenGetsu(sysShoriNenGetsu);
		// 社宅入居情報の件数取得
		int shatakuNyukyoCnt = skf3020Sc003GetShatakuNyukyoCountExpRepository.getShatakuNyukyoCount(param);

		if (shatakuNyukyoCnt > Skf302010CommonSharedService.COUNT_1) {
			return Skf302010CommonSharedService.SINSEI_CHU;

		} else if (shatakuNyukyoCnt == Skf302010CommonSharedService.COUNT_1) {
			return Skf302010CommonSharedService.NYUKYO_CHU;

		} else {
			return Skf302010CommonSharedService.MI_NYUKYO;
		}

	}

	/**
	 * 登録する転任者調書データを作成する。
	 * 
	 * @param inDto
	 *            Skf3020Sc005CommonDto
	 * @param shainNo
	 *            社員番号
	 * @param chkShainNoHenkoKbn
	 *            社員番号変更対象区分
	 * @return 登録する転任者調書データ
	 */
	private Skf3020TTenninshaChoshoData createInsertTenninshaInfoData(Skf3020Sc005CommonDto inDto, String shainNo,
			String chkShainNoHenkoKbn) {

		Skf3020TTenninshaChoshoData tenninshaChoshoData = new Skf3020TTenninshaChoshoData();

		tenninshaChoshoData.setShainNo(shainNo.trim());
		tenninshaChoshoData.setName(inDto.getTxtShainMei().trim());
		tenninshaChoshoData.setTokyu(inDto.getTxtTokyu().trim());
		tenninshaChoshoData.setAge(inDto.getTxtNenrei().trim());

		String nowAffiliation = inDto.getTxtGenShozoku().replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNowAffiliation(nowAffiliation);

		String newAffiliation = inDto.getTxtShinShozoku().replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNewAffiliation(newAffiliation);

		tenninshaChoshoData.setBiko(inDto.getTxtBiko().trim());
		tenninshaChoshoData.setShainNoHenkoKbn(chkShainNoHenkoKbn);
		tenninshaChoshoData.setNyukyoFlg(null);
		tenninshaChoshoData.setTaikyoFlg(null);
		tenninshaChoshoData.setHenkouFlg(null);

		String genShatakuKbn = getGenShatakuKbn(shainNo);
		tenninshaChoshoData.setNowShatakuKbn(genShatakuKbn);

		tenninshaChoshoData.setNyutaikyoYoteiKbn(Skf302010CommonSharedService.MI_SAKUSEI);

		Date nowTime = skfBaseBusinessLogicUtils.getSystemDateTime();
		tenninshaChoshoData.setDataTakinginDate(new SimpleDateFormat("yyyyMMdd").format(nowTime));

		return tenninshaChoshoData;
	}

	/**
	 * 更新する転任者調書データを作成する。
	 * 
	 * @param inDto
	 *            Skf3020Sc005CommonDto
	 * @param inData
	 *            既存の転任者調書データ
	 * @param shainNo
	 *            社員番号
	 * @param chkShainNoHenkoKbn
	 *            社員番号変更対象区分
	 * @return 登録する転任者調書データ
	 */
	public Skf3020Sc005UpdateTenninshaInfoExp createUpdateTenninshaInfoData(Skf3020Sc005CommonDto inDto, String shainNo,
			String chkShainNoHenkoKbn, Skf3020TTenninshaChoshoData inData) {

		Skf3020Sc005UpdateTenninshaInfoExp tenninshaChoshoData = new Skf3020Sc005UpdateTenninshaInfoExp();

		tenninshaChoshoData.setShainNo(shainNo.trim());
		tenninshaChoshoData.setName(inDto.getTxtShainMei().trim());
		tenninshaChoshoData.setTokyu(inDto.getTxtTokyu().trim());
		tenninshaChoshoData.setAge(inDto.getTxtNenrei().trim());

		String nowAffiliation = inDto.getTxtGenShozoku().replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNowAffiliation(nowAffiliation);

		String newAffiliation = inDto.getTxtShinShozoku().replaceAll(Skf302010CommonSharedService.DISPLAY_LS,
				Skf302010CommonSharedService.DB_LS);
		tenninshaChoshoData.setNewAffiliation(newAffiliation);

		tenninshaChoshoData.setBiko(inDto.getTxtBiko().trim());
		tenninshaChoshoData.setShainNoHenkoKbn(chkShainNoHenkoKbn);

		//String genShatakuKbn = getGenShatakuKbn(shainNo);
		//tenninshaChoshoData.setNowShatakuKbn(genShatakuKbn);

		//tenninshaChoshoData.setNyutaikyoYoteiKbn(inData.getNyutaikyoYoteiKbn());
		//tenninshaChoshoData.setDataTakinginDate(inData.getDataTakinginDate());
		//tenninshaChoshoData.setDeleteFlag(inData.getDeleteFlag());
		
		tenninshaChoshoData.setUpdateProgramId(inDto.getPageId());
		tenninshaChoshoData.setUpdateUserId(LoginUserInfoUtils.getUserCd());

		return tenninshaChoshoData;
	}

	/**
	 * 社宅社員データ取得
	 * 
	 * @param shainNo
	 *            社員番号
	 * @return 社宅社員データ
	 */
	public Skf1010MShain getShainData(String shainNo) {
		Skf1010MShainKey param = new Skf1010MShainKey();
		param.setCompanyCd(CodeConstant.C001);
		param.setShainNo(shainNo);
		Skf1010MShain outData = skf1010MShainRepository.selectByPrimaryKey(param);

		return outData;
	}

}
