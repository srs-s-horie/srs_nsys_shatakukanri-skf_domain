/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinShinseiInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfAttachedFileUtiles;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfHtmlCreateUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc003.Skf2020Sc003InitDto;

/**
 * Skf2020Sc003 社宅入居希望等調書（アウトソース用）初期表示処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2020Sc003InitService extends BaseServiceAbstract<Skf2020Sc003InitDto> {

	// 会社コード
	private String companyCd = CodeConstant.C001;

	// セッションキー
	@Value("${skf.common.shataku_attached_file_session_key}")
	private String sessionKey;

	private final String SHATAKU_TEIJI_MSG = "社宅管理システムで提示データを確認";
	private final String SHATAKU_TEIJI_NONE = "（社宅提示データが取得できませんでした。）";
	private final String SHATAKU_TEIJI_COMP = "（社宅提示データが作成完了されていません。）";

	@Autowired
	private Skf2020Sc003SharedService skf2020sc003SharedService;

	@Autowired
	private Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfHtmlCreateUtils skfHtmlCreateUtils;

	NumberFormat nfNum = NumberFormat.getNumberInstance();

	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf2020Sc003InitDto index(Skf2020Sc003InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF2020_SC003_TITLE);

		// 初期表示
		setDispInfo(initDto);

		//

		// 備品申請要否の表示制御

		return initDto;
	}

	private void setDispInfo(Skf2020Sc003InitDto initDto) {
		// 申請番号取得
		String applNo = initDto.getApplNo();

		// 申請情報の設定（社宅入居希望等調書の表示）
		getSinseiInfo(applNo, initDto);

		// 駐車場のみは返却備品表示不要
		String taiyoHitsuyo = initDto.getTaiyoHituyo();
		boolean bihinVisible = true;
		if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(taiyoHitsuyo)) {
			bihinVisible = false;
		} else {
			setBihinItemToBeReturn(applNo, initDto);
		}

		// 提示情報の設定
		String shainNo = initDto.getShainNo();
		if (!setTeijiDataInfo(shainNo, applNo, initDto)) {
			// 備品申請要否の非活性制御
			// 備品非表示制御
			bihinVisible = false;
		}
		initDto.setBihinVisible(bihinVisible);

		if (!NfwStringUtils.isEmpty(shainNo)) {
			setBihinInfo(applNo, initDto);
		}

		initDto.setBihinVisible(bihinVisible);

		setDispVisible(initDto);

		return;
	}

	/**
	 * 備品情報の設定
	 * 
	 * @param applNo
	 * @param initDto
	 */
	private void setBihinInfo(String applNo, Skf2020Sc003InitDto initDto) {
		boolean isShonin = false;
		// 遷移元パスがS0010（承認一覧）の場合、社宅部屋マスタの備品備付状態から取得するフラグを立てる。
		if (initDto.getPrePageId() != null && initDto.getPrePageId().equals(FunctionIdConstant.SKF2010_SC005)) {
			isShonin = true;
		}

		// 備品申請情報を取得
		List<Skf2020Sc003GetBihinShinseiInfoExp> bihinShinseiInfoList = new ArrayList<Skf2020Sc003GetBihinShinseiInfoExp>();
		bihinShinseiInfoList = skf2020sc003SharedService.getBihinShinseiInfo(companyCd, applNo);

		if (bihinShinseiInfoList != null && bihinShinseiInfoList.size() > 0) {
			String shatakuKanriNo = String.valueOf(initDto.getShatakuKanriNo());
			String shatakuRoomKanriNo = initDto.getNowShatakuRoomKanriNo();
			String yearMonth = skfDateFormatUtils.dateFormatFromDate(new Date(),
					SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);

			List<Skf2020Sc003GetBihinInfoExp> bihinInfoList = skf2020sc003SharedService.getBihinInfo(shatakuKanriNo,
					shatakuRoomKanriNo, yearMonth);
			// 備品状態が会社保有またはレンタルの場合にドロップボックスの初期値を選択不可にするための処理
			if (bihinInfoList != null && bihinInfoList.size() > 0) {

				for (Skf2020Sc003GetBihinInfoExp bihinInfo : bihinInfoList) {
					if (bihinInfo.getBihinState().equals(CodeConstant.BIHIN_STATE_HOYU)
							|| bihinInfo.getBihinState().equals(CodeConstant.BIHIN_STATE_RENTAL)) {

					}
				}

			}
		}

	}

	/**
	 * 提示データの設定を行います
	 * 
	 * @param shainNo
	 * @param applNo
	 * @param initDto
	 */
	@SuppressWarnings("unchecked")
	private boolean setTeijiDataInfo(String shainNo, String applNo, Skf2020Sc003InitDto initDto) {
		List<Skf2020Sc003GetTeijiDataInfoExp> teijiDataInfoList = new ArrayList<Skf2020Sc003GetTeijiDataInfoExp>();
		String nyutaikyoKbn = CodeConstant.NYUTAIKYO_KBN_NYUKYO;
		teijiDataInfoList = skf2020sc003SharedService.getTeijiDataInfo(shainNo, nyutaikyoKbn, applNo);
		if (teijiDataInfoList == null || teijiDataInfoList.size() <= 0) {
			// 社宅提示データが取得できなかった場合
			ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, SHATAKU_TEIJI_MSG,
					SHATAKU_TEIJI_NONE);

			// TODO この時点では添付ファイルは取得されていないため不要？
			// 提示データが存在しない場合は添付資料を表示させない。（クリア処理を行う。）
			// initDto.setShatakuAttachedFileList(null);
			return false;
		}

		Skf2020Sc003GetTeijiDataInfoExp teijiDataInfo = new Skf2020Sc003GetTeijiDataInfoExp();
		teijiDataInfo = teijiDataInfoList.get(0);

		String createCompKbn = teijiDataInfo.getCreateCompleteKbn();
		if (createCompKbn != null && !NfwStringUtils.isEmpty(createCompKbn)) {
			switch (createCompKbn) {
			case CodeConstant.MI_SAKUSEI:
				ServiceHelper.addWarnResultMessage(initDto, MessageIdConstant.W_SKF_1001, SHATAKU_TEIJI_MSG,
						SHATAKU_TEIJI_COMP);
				// 未作成は提示不可で継続

				break;
			}
		}

		// 提示内容
		// 社宅情報及び駐車場 都道府県コード（保有社宅のみ設定される）
		String wkPrefName = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getPrefCd())) {
			wkPrefName = skf2020sc003SharedService.getShatakuPrefName(teijiDataInfo.getPrefCd());
		}
		// 社宅情報 社宅所在地
		String newShozaichi = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getAddress())) {
			newShozaichi = wkPrefName + teijiDataInfo.getAddress();
			initDto.setNewShozaichi(newShozaichi);
		}
		// 社宅情報 社宅名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getShatakuName())) {
			initDto.setNewShatakuName(teijiDataInfo.getShatakuName());
		}
		// 社宅情報 室番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getRoomNo())) {
			initDto.setNewShatakuNo(teijiDataInfo.getRoomNo());
		}
		// 社宅情報 規格(間取り)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getKikaku())) {
			// 貸与規格
			String newShatakuKikaku = skf2020sc003SharedService.getShatakuKikakuKBN(teijiDataInfo.getKikaku());
			initDto.setNewShatakuKikaku(newShatakuKikaku);
		} else {
			// 本来規格
			if (!NfwStringUtils.isEmpty(teijiDataInfo.getOriginalKikaku())) {
				String newShatakuKikaku = skf2020sc003SharedService
						.getShatakuKikakuKBN(teijiDataInfo.getOriginalKikaku());
				initDto.setNewShatakuKikaku(newShatakuKikaku);
			}
		}
		// 社宅情報 面積
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getLendMenseki())) {
			initDto.setNewShatakuMenseki(teijiDataInfo.getLendMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}
		// 社宅情報 使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getRentalAdjust())) {
			Long rentalAdjust = Long.parseLong(teijiDataInfo.getRentalAdjust());
			initDto.setNewRental(nfNum.format(rentalAdjust) + SkfCommonConstant.FORMAT_EN);
		}
		// 個人負担金共益費協議中フラグ
		String kyoekihiKyogichuFlg = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getKyoekihiPersonKyogichuFlg())) {
			kyoekihiKyogichuFlg = teijiDataInfo.getKyoekihiPersonKyogichuFlg();
		}
		// 社宅情報 共益費
		// 個人負担共益費協議中フラグが1だったら「協議中」を表示する
		if (kyoekihiKyogichuFlg.equals(SkfCommonConstant.KYOGICHU)) {
			initDto.setNewKyoekihi("協議中");
		} else {
			if (!NfwStringUtils.isEmpty(teijiDataInfo.getKyoekihiPersonAdjust())) {
				Long kyoekihiAdjust = Long.parseLong(teijiDataInfo.getKyoekihiPersonAdjust());
				initDto.setNewKyoekihi(nfNum.format(kyoekihiAdjust) + SkfCommonConstant.FORMAT_EN);
			}

		}
		// 社宅情報 入居可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getNyukyoYoteiDate())) {
			initDto.setNyukyoYoteiDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getNyukyoYoteiDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}
		// 社宅情報 寮長・自治会長 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo1())) {
			initDto.setManegeShatakuNo1(teijiDataInfo.getManegeShatakuNo1());
		}
		// 社宅情報 寮長・自治会長 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName1())) {
			initDto.setManegeName1(teijiDataInfo.getManegeName1());
		}
		// 社宅情報 寮長・自治会長 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress1())) {
			initDto.setManegeMailAddress1(teijiDataInfo.getManegeMailAddress1());
		}
		// 社宅情報 寮長・自治会長 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo1())) {
			initDto.setManegeTelNo1(teijiDataInfo.getManegeTelNo1());
		}
		// 社宅情報 寮長・自治会長 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo1())) {
			initDto.setManegeExtensionNo1(teijiDataInfo.getManegeExtensionNo1());
		}
		// 社宅情報 鍵管理者 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo2())) {
			initDto.setManegeShatakuNo2(teijiDataInfo.getManegeShatakuNo2());
		}
		// 社宅情報 鍵管理者 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName2())) {
			initDto.setManegeName2(teijiDataInfo.getManegeName2());
		}
		// 社宅情報 鍵管理者 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress2())) {
			initDto.setManegeMailAddress2(teijiDataInfo.getManegeMailAddress2());
		}
		// 社宅情報 鍵管理者 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo2())) {
			initDto.setManegeTelNo2(teijiDataInfo.getManegeTelNo2());
		}
		// 社宅情報 鍵管理者 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo2())) {
			initDto.setManegeExtensionNo2(teijiDataInfo.getManegeExtensionNo2());
		}

		// 社宅情報 寮母・管理会社 部屋名称
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeShatakuNo3())) {
			initDto.setManegeShatakuNo3(teijiDataInfo.getManegeShatakuNo3());
		}
		// 社宅情報 寮母・管理会社 氏名
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeName3())) {
			initDto.setManegeName3(teijiDataInfo.getManegeName3());
		}
		// 社宅情報 寮母・管理会社 電子メールアドレス
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeMailAddress3())) {
			initDto.setManegeMailAddress3(teijiDataInfo.getManegeMailAddress3());
		}
		// 社宅情報 寮母・管理会社 電話番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeTelNo3())) {
			initDto.setManegeTelNo3(teijiDataInfo.getManegeTelNo3());
		}
		// 社宅情報 寮母・管理会社 内線番号
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getManegeExtensionNo3())) {
			initDto.setManegeExtensionNo3(teijiDataInfo.getManegeExtensionNo3());
		}
		// 社宅情報及び駐車場 都道府県コード（保有社宅のみ設定される）
		String wkPrefNameParking = CodeConstant.NONE;
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getPrefCdParking())) {
			wkPrefNameParking = skf2020sc003SharedService.getShatakuPrefName(teijiDataInfo.getPrefCdParking());
		}
		// 駐車場情報 １台目 自動車の保管場所
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingAddress1())) {
			initDto.setParkingAddress1(wkPrefNameParking + teijiDataInfo.getParkingAddress1());
		}
		// 駐車場情報 １台目 位置番号等
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingBlock1())) {
			initDto.setCarIchiNo1(teijiDataInfo.getParkingBlock1());
		}
		// 駐車場情報 １台目 自動車の保管場所に係わる使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingRental1())) {
			Long parkingRental1 = Long.parseLong(teijiDataInfo.getParkingRental1());
			initDto.setParkingRental1(nfNum.format(parkingRental1) + SkfCommonConstant.FORMAT_EN);
		}
		// 駐車場情報 １台目 使用開始可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParking1StartDate())) {
			initDto.setParking1StartDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getParking1StartDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
		}

		// 駐車場情報 ２台目存在チェック
		boolean bCar2 = false;

		// 駐車場情報 ２台目 自動車の保管場所
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingAddress2())) {
			initDto.setParkingAddress2(wkPrefNameParking + teijiDataInfo.getParkingAddress2());
			bCar2 = true;
		}
		// 駐車場情報 ２台目 位置番号等
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingBlock2())) {
			initDto.setCarIchiNo2(teijiDataInfo.getParkingBlock2());
			bCar2 = true;
		}
		// 駐車場情報 ２台目 自動車の保管場所に係わる使用料(月)
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParkingRental2())) {
			Long parkingRental2 = Long.parseLong(teijiDataInfo.getParkingRental2());
			initDto.setParkingRental2(nfNum.format(parkingRental2) + SkfCommonConstant.FORMAT_EN);
			bCar2 = true;
		}
		// 駐車場情報 ２台目 使用開始可能日
		if (!NfwStringUtils.isEmpty(teijiDataInfo.getParking2StartDate())) {
			initDto.setParking2StartDate(skfDateFormatUtils.dateFormatFromString(teijiDataInfo.getParking2StartDate(),
					SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH));
			bCar2 = true;
		}

		// 社宅管理番号
		if (teijiDataInfo.getShatakuKanriNo() != CodeConstant.LONG_ZERO) {
			initDto.setNewShatakuKanriNo(String.valueOf(teijiDataInfo.getShatakuKanriNo()));
		}
		// 部屋管理番号
		if (teijiDataInfo.getShatakuRoomKanriNo() != CodeConstant.LONG_ZERO) {
			initDto.setNowShatakuRoomKanriNo(String.valueOf(teijiDataInfo.getShatakuRoomKanriNo()));
		}

		// 駐車場情報 駐車場のみ割当先判定
		// 社宅貸与必要＝3:駐車場のみ
		if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(initDto.getTaiyoHituyo()) && bCar2) {
			// ２台目 自動車の車名、登録番号、使用者、保管場所使用開始日(予定日)が空白なら１台のみ申請
			if (NfwStringUtils.isEmpty(initDto.getCarName2()) && NfwStringUtils.isEmpty(initDto.getCarNo2())
					&& NfwStringUtils.isEmpty(initDto.getCarUser2())
					&& NfwStringUtils.isEmpty(initDto.getParking2StartDate())) {
				// 駐車場のみで1台のみ追加された場合は割当先を判定
				if (teijiDataInfo.getShatakuKanriNo() != CodeConstant.LONG_ZERO
						&& teijiDataInfo.getShatakuRoomKanriNo() != CodeConstant.LONG_ZERO) {
					long shatakuKanriNo = teijiDataInfo.getShatakuKanriNo();
					long shatakuRoomKanriNo = teijiDataInfo.getShatakuRoomKanriNo();

					// 月次処理管理データの対象年月を取得
					String syoriYYMM = skf2020sc003SharedService.getMaxCycleBillingYYMM();

					// 駐車場履歴テーブルから現在割当の駐車場を取得
					List<Skf2020Sc003GetParkingRirekiDataExp> parkingRirekiList = new ArrayList<Skf2020Sc003GetParkingRirekiDataExp>();
					parkingRirekiList = skf2020sc003SharedService.getParkingRirekiData(shatakuKanriNo,
							shatakuRoomKanriNo, shainNo, syoriYYMM);
					// 駐車場履歴データが取得できた場合
					if (parkingRirekiList != null && parkingRirekiList.size() > 0) {
						Skf2020Sc003GetParkingRirekiDataExp parkingRirekiData = parkingRirekiList.get(0);
						// 今回提示か判定、駐車場2が割当なら移動
						if (parkingRirekiData.getParkingKanriNo1() == CodeConstant.LONG_ZERO
								&& parkingRirekiData.getParkingKanriNo2() != CodeConstant.LONG_ZERO) {
							initDto.setParking1stPlace(initDto.getParking2stPlace());
							initDto.setCarIchiNo1(initDto.getCarIchiNo2());
							initDto.setParkingRental1(initDto.getParkingRental2());
							initDto.setParking1StartDate(initDto.getParking2StartDate());
							initDto.setParking2stPlace(CodeConstant.NONE);
							initDto.setCarIchiNo2(CodeConstant.NONE);
							initDto.setParkingRental2(CodeConstant.NONE);
							initDto.setParking2StartDate(CodeConstant.NONE);
						}
					}
				}
			}
		}

		// 既存の添付資料をクリアする

		// 社宅補足ファイル1
		if (StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementFile1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize1())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName1(), teijiDataInfo.getShatakuSupplementFile1(),
					teijiDataInfo.getShatakuSupplementSize1());
		}
		// 社宅補足ファイル2
		if (StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementFile2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize2())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName2(), teijiDataInfo.getShatakuSupplementFile2(),
					teijiDataInfo.getShatakuSupplementSize2());
		}
		// 社宅補足ファイル3
		if (StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementFile3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementName3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShatakuSupplementSize3())) {
			addShatakuAttachedFile(teijiDataInfo.getShatakuSupplementName3(), teijiDataInfo.getShatakuSupplementFile3(),
					teijiDataInfo.getShatakuSupplementSize3());
		}
		// 駐車場補足ファイル1
		if (StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementFile1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName1())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize1())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName1(), teijiDataInfo.getParkingSupplementFile1(),
					teijiDataInfo.getParkingSupplementSize1());
		}
		// 駐車場補足ファイル2
		if (StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementFile2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName2())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize2())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName2(), teijiDataInfo.getParkingSupplementFile2(),
					teijiDataInfo.getParkingSupplementSize2());
		}
		// 駐車場補足ファイル3
		if (StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementFile3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementName3())
				&& StringUtils.isNotEmpty(teijiDataInfo.getParkingSupplementSize3())) {
			addShatakuAttachedFile(teijiDataInfo.getParkingSupplementName3(), teijiDataInfo.getParkingSupplementFile3(),
					teijiDataInfo.getParkingSupplementSize3());
		}
		// 資料補足ファイル
		if (StringUtils.isNotEmpty(teijiDataInfo.getShiryoHosokuFile())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShiryoHosokuName())
				&& StringUtils.isNotEmpty(teijiDataInfo.getShiryoHosokuSize())) {
			addShatakuAttachedFile(teijiDataInfo.getShiryoHosokuName(), teijiDataInfo.getShiryoHosokuFile(),
					teijiDataInfo.getShiryoHosokuSize());
		}

		// 添付ファイル情報の取得
		List<Map<String, Object>> shatakuAttachedFileList = new ArrayList<Map<String, Object>>();
		shatakuAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);
		initDto.setShatakuAttachedFileList(shatakuAttachedFileList);

		return true;
	}

	@SuppressWarnings("unchecked")
	private void addShatakuAttachedFile(String name, String file, String size) {
		List<Map<String, Object>> shatakuAttachedFileList = null;

		// 添付資料のコレクションをSessionより取得
		// 添付ファイル情報の取得
		shatakuAttachedFileList = (List<Map<String, Object>>) menuScopeSessionBean.get(sessionKey);

		// リンクリストチェック
		boolean findFlg = false;
		if (shatakuAttachedFileList != null) {
			for (Map<String, Object> attachedFileMap : shatakuAttachedFileList) {
				if (name.equals(attachedFileMap.get("attachedName"))) {
					findFlg = true;
					break;
				}
			}
		}

		// 添付ファイルリストに無い場合
		if (!findFlg) {
			Map<String, Object> addAttachedFileInfo = new HashMap<String, Object>();
			int attachedNo = 0;
			if (shatakuAttachedFileList.size() > 0) {
				// 添付資料番号
				attachedNo = Integer.parseInt(
						shatakuAttachedFileList.get(shatakuAttachedFileList.size() - 1).get("attachedNo").toString());
			}
			addAttachedFileInfo.put("attachedNo", attachedNo);

			// 添付資料名
			addAttachedFileInfo.put("attachedName", name);
			// ファイルサイズ
			addAttachedFileInfo.put("attachedFileSize", size);
			// 更新日
			addAttachedFileInfo.put("registDate", new Date());
			// 添付資料
			addAttachedFileInfo.put("attachedFileSize", file);
			// 添付ファイルステータス
			// ファイルタイプ
			addAttachedFileInfo.put("fileType", SkfAttachedFileUtiles.getFileTypeInfo(name));

			shatakuAttachedFileList.add(addAttachedFileInfo);
		}

		menuScopeSessionBean.put(sessionKey, shatakuAttachedFileList);

	}

	/**
	 * 要返却備品のリスト作成
	 * 
	 * @param applNo
	 * @param initDto
	 */
	private void setBihinItemToBeReturn(String applNo, Skf2020Sc003InitDto initDto) {
		String shatakuKanriId = String.valueOf(skf2020sc003SharedService.getNowShatakuKanriID(initDto.getShainNo(),
				initDto.getNowShatakuKanriNo(), initDto.getNowShatakuRoomKanriNo()));
		String yearMonth = skfDateFormatUtils.dateFormatFromDate(new Date(), SkfCommonConstant.YMD_STYLE_YYYYMM_FLAT);
		// 備品状態が2:保有備品または3:レンタルの表示
		List<Skf2020Sc003GetBihinInfoExp> bihinInfoList = new ArrayList<Skf2020Sc003GetBihinInfoExp>();
		bihinInfoList = skf2020sc003SharedService.getBihinInfo(shatakuKanriId, initDto.getShainNo(), yearMonth);
		String returnEquipment = CodeConstant.NONE;
		if (bihinInfoList != null && bihinInfoList.size() > 0) {
			List<String> colList = new ArrayList<String>(); // テーブルのTD領域
			List<List<String>> rowList = new ArrayList<List<String>>(); // テーブルのTR領域
			int index = 0;
			for (Skf2020Sc003GetBihinInfoExp bihinInfo : bihinInfoList) {
				if (index <= 2) {
					colList.add(bihinInfo.getBihinName());
				} else {
					rowList.add(colList);
					colList = new ArrayList<String>();
					colList.add(bihinInfo.getBihinName());
					index = 1;
				}
			}
			returnEquipment = skfHtmlCreateUtils.htmlBihinCreateTable(rowList, 2);
		}
		initDto.setReturnEquipment(returnEquipment);
		return;
	}

	/**
	 * 申請情報の設定（社宅入居希望等調書の表示）
	 * 
	 * @param applNo
	 * @param initDto
	 */
	private void getSinseiInfo(String applNo, Skf2020Sc003InitDto initDto) {
		// 入居希望等調査・入居決定通知テーブルより社宅入居希望等調書情報を取得する
		Skf2020Sc003GetShatakuNyukyoKiboInfoExp shatakuNyukyoKiboInfo = new Skf2020Sc003GetShatakuNyukyoKiboInfoExp();
		shatakuNyukyoKiboInfo = skf2020sc003SharedService.getShatakuNyukyoKiboInfo(companyCd, applNo);

		if (shatakuNyukyoKiboInfo == null) {
			ServiceHelper.addErrorResultMessage(initDto, null, MessageIdConstant.E_SKF_1077);
			// 更新処理を行わせないようボタンを使用不可に
			// 差戻しのみ使用可能に
			// 備品申請要否の非活性制御
			// 備品非表示制御
			return;
		}

		// 申請内容
		// 所属 機関
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAgency())) {
			initDto.setAgencyName(shatakuNyukyoKiboInfo.getAgency());
		}
		// 所属 部等
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAffiliation1())) {
			initDto.setAffiliation1Name(shatakuNyukyoKiboInfo.getAffiliation1());
		}
		// 所属 室、チーム又は課
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getAffiliation2())) {
			initDto.setAffiliation2Name(shatakuNyukyoKiboInfo.getAffiliation2());
		}
		// 所属 勤務先のTEL
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTel())) {
			initDto.setTel(shatakuNyukyoKiboInfo.getTel());
		}
		// 申請者 社員番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getShainNo())) {
			initDto.setShainNo(shatakuNyukyoKiboInfo.getShainNo());
		}
		// 申請者 氏名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getName())) {
			initDto.setName(shatakuNyukyoKiboInfo.getName());
		}
		// 申請者 等級
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTokyu())) {
			initDto.setTokyu(shatakuNyukyoKiboInfo.getTokyu());
		}
		// 社宅を必要としますか？
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaiyoHitsuyo())) {
			initDto.setTaiyoHituyo(shatakuNyukyoKiboInfo.getTaiyoHitsuyo());
		}

		// 社宅を必要とする理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getHitsuyoRiyu())) {
			initDto.setHitsuyoRiyu(shatakuNyukyoKiboInfo.getHitsuyoRiyu());
		}
		// 社宅を必要としない理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getFuhitsuyoRiyu())) {
			initDto.setFuhitsuyoRiyu(shatakuNyukyoKiboInfo.getFuhitsuyoRiyu());
		}

		// 新所属 機関
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAgency())) {
			initDto.setNewAgency(shatakuNyukyoKiboInfo.getNewAgency());
		}
		// 新所属 部等
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAffiliation1())) {
			initDto.setNewAffiliation1(shatakuNyukyoKiboInfo.getNewAffiliation1());
		}
		// 新所属 室、チーム又は課
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNewAffiliation2())) {
			initDto.setNewAffiliation2(shatakuNyukyoKiboInfo.getNewAffiliation2());
		}
		// 必要とする社宅
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getHitsuyoShataku())) {
			initDto.setHitsuyoShataku(shatakuNyukyoKiboInfo.getHitsuyoShataku());
		}
		// 同居家族 家族1
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName1())) {
			initDto.setDokyoName1(shatakuNyukyoKiboInfo.getDokyoName1());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation1())) {
			initDto.setDokyoRelation1(shatakuNyukyoKiboInfo.getDokyoRelation1());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge1())) {
			initDto.setDokyoAge1(shatakuNyukyoKiboInfo.getDokyoAge1());
		}
		// 同居家族 家族2
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName2())) {
			initDto.setDokyoName2(shatakuNyukyoKiboInfo.getDokyoName2());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation2())) {
			initDto.setDokyoRelation2(shatakuNyukyoKiboInfo.getDokyoRelation2());
		}
		if (shatakuNyukyoKiboInfo.getDokyoAge2() != null
				&& !NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge2())) {
			initDto.setDokyoAge2(shatakuNyukyoKiboInfo.getDokyoAge2());
		}
		// 同居家族 家族3
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName3())) {
			initDto.setDokyoName3(shatakuNyukyoKiboInfo.getDokyoName3());
		}
		if (shatakuNyukyoKiboInfo.getDokyoRelation3() != null
				&& !NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation3())) {
			initDto.setDokyoRelation3(shatakuNyukyoKiboInfo.getDokyoRelation3());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge3())) {
			initDto.setDokyoAge3(shatakuNyukyoKiboInfo.getDokyoAge3());
		}
		// 同居家族 家族4
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName4())) {
			initDto.setDokyoName4(shatakuNyukyoKiboInfo.getDokyoName4());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation4())) {
			initDto.setDokyoRelation4(shatakuNyukyoKiboInfo.getDokyoRelation4());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge4())) {
			initDto.setDokyoAge4(shatakuNyukyoKiboInfo.getDokyoAge4());
		}
		// 同居家族 家族5
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName5())) {
			initDto.setDokyoName5(shatakuNyukyoKiboInfo.getDokyoName5());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation5())) {
			initDto.setDokyoRelation5(shatakuNyukyoKiboInfo.getDokyoRelation5());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge5())) {
			initDto.setDokyoAge5(shatakuNyukyoKiboInfo.getDokyoAge5());
		}
		// 同居家族 家族6
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoName6())) {
			initDto.setDokyoName6(shatakuNyukyoKiboInfo.getDokyoName6());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoRelation6())) {
			initDto.setDokyoRelation6(shatakuNyukyoKiboInfo.getDokyoRelation6());
		}
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getDokyoAge6())) {
			initDto.setDokyoAge6(shatakuNyukyoKiboInfo.getDokyoAge6());
		}
		// 入居希望日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNyukyoYoteiDate())) {
			initDto.setNyukyoYoteiDate(shatakuNyukyoKiboInfo.getNyukyoYoteiDate());
		}
		// １台目
		// 自動者の保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUmu())) {
			initDto.setParkingUmu(shatakuNyukyoKiboInfo.getParkingUmu());
		}
		// 自動車の登録番号の入力フラグ
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNoInputFlg())) {
			initDto.setCarNoInputFlg(shatakuNyukyoKiboInfo.getCarNoInputFlg());
		}
		// 自動車の車名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarName())) {
			initDto.setCarName(shatakuNyukyoKiboInfo.getCarName());
		}
		// 自動車の登録番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNo())) {
			initDto.setCarNo(shatakuNyukyoKiboInfo.getCarNo());
		}
		// 車検の有効期間満了日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarExpirationDate())) {
			initDto.setCarExpirationDate(shatakuNyukyoKiboInfo.getCarExpirationDate());
		}
		// 自動車の使用者
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarUser())) {
			initDto.setCarUser(shatakuNyukyoKiboInfo.getCarUser());
		}
		// 自動車の保管場所 使用開始日(予定日)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUseDate())) {
			initDto.setParkingUseDate(shatakuNyukyoKiboInfo.getParkingUseDate());
		}
		// ２台目
		// 自動車の登録番号の入力フラグ
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNoInputFlg2())) {
			initDto.setCarNoInputFlg2(shatakuNyukyoKiboInfo.getCarNoInputFlg2());
		}
		// 自動車の車名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarName2())) {
			initDto.setCarName2(shatakuNyukyoKiboInfo.getCarName2());
		}
		// 自動車の登録番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarNo2())) {
			initDto.setCarNo2(shatakuNyukyoKiboInfo.getCarNo2());
		}
		// 車検の有効期間満了日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarExpirationDate2())) {
			initDto.setCarExpirationDate2(shatakuNyukyoKiboInfo.getCarExpirationDate2());
		}
		// 自動車の使用者
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getCarUser2())) {
			initDto.setCarUser2(shatakuNyukyoKiboInfo.getCarUser2());
		}
		// 自動車の保管場所 使用開始日(予定日)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingUseDate2())) {
			initDto.setParkingUseDate2(shatakuNyukyoKiboInfo.getParkingUseDate2());
		}
		// 現居住宅
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShataku())) {
			initDto.setNowShataku(shatakuNyukyoKiboInfo.getNowShataku());
		}
		// 現居住宅 保有社宅名
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuName())) {
			initDto.setNowShatakuName(shatakuNyukyoKiboInfo.getNowShatakuName());
		}
		// 現居住宅 室番号
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuNo())) {
			initDto.setNowShatakuNo(shatakuNyukyoKiboInfo.getNowShatakuNo());
		}
		// 現居住宅 規格(間取り)
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuKikaku())) {
			initDto.setNowShatakuKikaku(shatakuNyukyoKiboInfo.getNowShatakuKikaku());
		}
		// 現居住宅 面積
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getNowShatakuMenseki())) {
			initDto.setNowShatakuMenseki(
					shatakuNyukyoKiboInfo.getNowShatakuMenseki() + SkfCommonConstant.SQUARE_MASTER);
		}
		// 駐車場 １台目 保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingArea())) {
			initDto.setParking1stPlace(shatakuNyukyoKiboInfo.getParkingArea());
		}
		// 駐車場 ２台目 保管場所
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getParkingArea2())) {
			initDto.setParking2stPlace(shatakuNyukyoKiboInfo.getParkingArea2());
		}
		// 特殊事情など
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTokushuJijo())) {
			initDto.setTokushuJijo(shatakuNyukyoKiboInfo.getTokushuJijo());
		}
		// 現保有の社宅（退居予定）
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoYotei())) {
			initDto.setTaikyoYotei(shatakuNyukyoKiboInfo.getTaikyoYotei());
		}
		// 現保有の社宅 社宅管理番号
		initDto.setNowShatakuKanriNo(String.valueOf(shatakuNyukyoKiboInfo.getNowShatakuKanriNo()));
		// 現保有の社宅 部屋管理番号
		initDto.setNowShatakuRoomKanriNo(String.valueOf(shatakuNyukyoKiboInfo.getNowShatakuKanriNo()));
		// 退居予定日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoYoteiDate())) {
			initDto.setTaikyoYoteiDate(shatakuNyukyoKiboInfo.getTaikyoYoteiDate());
		}
		// 社宅の状態
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getShatakuJotai())) {
			initDto.setShatakuJotai(shatakuNyukyoKiboInfo.getShatakuJotai());
		}

		// 退居理由
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyoRiyu())) {
			initDto.setTaikyoRiyu(shatakuNyukyoKiboInfo.getTaikyoRiyu());
		}
		// 退居後の連絡先
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getTaikyogoRenrakusaki())) {
			initDto.setTaikyogoRenrakuSaki(shatakuNyukyoKiboInfo.getTaikyogoRenrakusaki());
		}
		// 返却立会希望日
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getSessionDay())) {
			initDto.setSessionDay(shatakuNyukyoKiboInfo.getSessionDay());
		}
		// 連絡先
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getRenrakuSaki())) {
			initDto.setRenrakuSaki(shatakuNyukyoKiboInfo.getRenrakuSaki());
		}
		// 更新日時
		if (shatakuNyukyoKiboInfo.getUpdateDate() != null) {
			initDto.addLastUpdateDate("skf2020TNyukyoChoshoTsuchiUpdatedate", shatakuNyukyoKiboInfo.getUpdateDate());
		}
		// 備品希望申請の再掲分
		// 申請者 氏名（社宅希望申請の氏名で代用）

		// 申請者 性別
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getGender())) {
			String genderText = CodeConstant.NONE;
			if (CodeConstant.MALE.equals(shatakuNyukyoKiboInfo.getGender())) {
				genderText = SkfCommonConstant.GENDER_MAIL;
			} else if (CodeConstant.FEMALE.equals(shatakuNyukyoKiboInfo.getGender())) {
				genderText = SkfCommonConstant.GENDER_FEMAIL;
			}
			initDto.setGender(genderText);
		}
		// 備品希望申請を希望する/しない ラジオボタン
		if (!NfwStringUtils.isEmpty(shatakuNyukyoKiboInfo.getBihinKibo())) {
			initDto.setBihinKibo(shatakuNyukyoKiboInfo.getBihinKibo());
		}

		return;
	}

	/**
	 * 画面表示処理
	 * 
	 * @param initDto
	 */
	private void setDispVisible(Skf2020Sc003InitDto initDto) {
		// 社宅を必要する場合以外は、編集ボタンを非表示
		String taiyoHitsuyo = initDto.getTaiyoHituyo();
		initDto.setEditBtnVisible("true");
		if (!taiyoHitsuyo.equals(CodeConstant.ASKED_SHATAKU_HITSUYO)) {
			initDto.setEditBtnVisible("false");
		}
	}

}
