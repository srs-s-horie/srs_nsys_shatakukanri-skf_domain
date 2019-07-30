/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc002;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.DateUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.NfwStringUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.constants.SkfCommonConstant;
import jp.co.c_nexco.skf.common.util.SkfCheckUtils;
import jp.co.c_nexco.skf.skf2020.domain.dto.skf2020sc002.Skf2020Sc002CheckAsyncDto;

/**
 * 
 * 入居希望等調書申請画面の申請内容を確認ボタンのチェック処理(非同期)
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf2020Sc002CheckAsyncService extends AsyncBaseServiceAbstract<Skf2020Sc002CheckAsyncDto> {

	@Autowired
	private Skf2020Sc002SharedService skf2020Sc002SharedService;

	@Override
	public AsyncBaseDto index(Skf2020Sc002CheckAsyncDto checkDto) throws Exception {

		// TODO エラー時に赤くならない。そしてメッセージが緑

		Boolean ret = true;
		// チェック処理
		ret = checkDispInfo(checkDto);
		// エラーがあった場合処理終了
		if (!ret) {
			throwBusinessExceptionIfErrors(checkDto.getResultMessages());
		}
		return checkDto;
	}

	/**
	 * 
	 * 入力チェック
	 * 
	 * @return True:正常 False:異常
	 */
	private boolean checkDispInfo(Skf2020Sc002CheckAsyncDto checkDto) throws Exception {

		Boolean ret = true;
		// コントロールをデフォルト色に戻す
		// setDefultColor(checkDto);

		boolean checkEmpty = true;
		boolean checkType = true;
		boolean checkCount = true;
		boolean checkDate = true;
		boolean checkTaikyoDate = true;

		// 未入力確認
		checkEmpty = checkControlEmpty(checkDto);

		// 文字種確認
		checkType = checkValueType(checkDto);

		// バイト数確認
		checkCount = checkByteCount(checkDto);

		// 日付の整合性確認
		checkDate = checkDateFormat(checkDto);

		// エラーメッセージがある場合
		// TODO 退居予定日と返却希望立会日の相関確認（Falseの場合確認メッセージ表示）
		if (checkEmpty == false || checkType == false || checkCount == false || checkDate == false
				|| checkTaikyoDate == false) {
			ret = false;
		}

		return ret;
	}

	/**
	 * 未入力確認
	 * 
	 * @return
	 */
	private Boolean checkControlEmpty(Skf2020Sc002CheckAsyncDto checkDto) {

		boolean result = true;
		String msg = "必須チェック　値確認：";

		// 勤務先のTEL
		LogUtils.debugByMsg(msg + "勤務先のTEL" + checkDto.getTel());
		if (NfwStringUtils.isBlank(checkDto.getTel())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1048,
					"勤務先のTEL");
			result = false;
		}

		// 社宅を必要としますか？
		LogUtils.debugByMsg(msg + "社宅を必要としますか？ " + checkDto.getTaiyoHituyo());
		if (NfwStringUtils.isBlank(checkDto.getTaiyoHituyo())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taiyoHituyo" }, MessageIdConstant.E_SKF_1054,
					"社宅を必要としますか？");
			result = false;
		}

		// 社宅を必要とする理由(必要理由が必要とする場合に、理由がない場合はエラー）
		LogUtils.debugByMsg(msg + "社宅を必要とする理由" + checkDto.getTaiyoHituyo() + checkDto.getHitsuyoRiyu());
		if (NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())) {
			if (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(checkDto.getTaiyoHituyo())) {
				if (NfwStringUtils.isBlank(checkDto.getHitsuyoRiyu())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "hitsuyoRiyu" },
							MessageIdConstant.E_SKF_1054, "社宅を必要とする理由");
					result = false;
				}
			}
		}

		// 社宅を必要としない理由(必要理由が必要としない場合に、理由がない場合はエラー）
		LogUtils.debugByMsg(msg + "社宅を必要としない理由" + checkDto.getTaiyoHituyo() + checkDto.getFuhitsuyoRiyu());
		if (NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())) {
			if (CodeConstant.ASKED_SHATAKU_FUYOU.equals(checkDto.getTaiyoHituyo())) {
				if (NfwStringUtils.isBlank(checkDto.getFuhitsuyoRiyu())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "fuhitsuyoRiyu" },
							MessageIdConstant.E_SKF_1054, "社宅を必要としない理由");
					result = false;
				}
			}
		}

		// 社宅を必要とする理由が異動の場合
		LogUtils.debugByMsg(msg + " 機関" + checkDto.getHitsuyoRiyu() + checkDto.getAgencyCd() + checkDto.getNewAgency());
		if (NfwStringUtils.isNotBlank(checkDto.getHitsuyoRiyu())
				&& checkDto.getHitsuyoRiyu().equals(CodeConstant.IDOU)) {
			// 機関
			if (NfwStringUtils.isBlank(checkDto.getAgencyCd())) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "agencyCd" }, MessageIdConstant.E_SKF_1054,
						"機関");
				result = false;
			}

			// 機関 その他

			// 部等 その他
			LogUtils.debugByMsg(msg + " 部等 その他" + checkDto.getAffiliation1Cd() + checkDto.getNewAffiliation1Other());
			if (CodeConstant.RELATION_OTHERS.equals(checkDto.getAffiliation1Cd())
					&& NfwStringUtils.isBlank(checkDto.getNewAffiliation1Other())) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "newAffiliation1Other" },
						MessageIdConstant.E_SKF_1048, "部 その他");
				result = false;
			}

			// 室、チーム又は課 その他
			LogUtils.debugByMsg(
					msg + " 室、チーム又は課 その他" + checkDto.getAffiliation1Cd() + checkDto.getNewAffiliation1Other());
			if (CodeConstant.RELATION_OTHERS.equals(checkDto.getAffiliation2Cd())
					&& NfwStringUtils.isBlank(checkDto.getNewAffiliation2Other())) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "newAffiliation2Other" },
						MessageIdConstant.E_SKF_1048, "室、チーム又は課 その他");
				result = false;
			}
		}

		// 社宅の貸与が必要で、必要とする社宅が空の場合
		LogUtils.debugByMsg(msg + "社宅の貸与が必要で、必要とする社宅が空の場合" + checkDto.getTaiyoHituyo() + checkDto.getHitsuyoShataku()
				+ checkDto.getHitsuyoShataku());
		if (NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())
				&& CodeConstant.ASKED_SHATAKU_HITSUYO.equals(checkDto.getTaiyoHituyo())
				&& NfwStringUtils.isBlank(checkDto.getHitsuyoShataku())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "hitsuyoShataku" },
					MessageIdConstant.E_SKF_1054, "必要とする社宅 (世帯、単身、独身)");
			result = false;
		}

		// 必要とする社宅が世帯の場合
		LogUtils.debugByMsg(msg + "必要とする社宅が世帯の場合" + checkDto.getHitsuyoShataku());
		if (NfwStringUtils.isNotBlank(checkDto.getHitsuyoShataku())
				&& CodeConstant.SETAI.equals(checkDto.getHitsuyoShataku())) {
			// 家族情報の未選択確認
			result = checkEmptyKazokuInfo(checkDto);
		}

		// 入居希望日(予定日)
		LogUtils.debugByMsg(msg + "入居希望日(予定日)" + checkDto.getHitsuyoShataku() + checkDto.getNyukyoYoteiDate());
		if (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(checkDto.getTaiyoHituyo())
				&& NfwStringUtils.isBlank(checkDto.getNyukyoYoteiDate())) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "nyukyoYoteiDate" },
					MessageIdConstant.E_SKF_1048, "入居希望日(予定日)");
			result = false;

		}

		// 自動車の保管場所
		LogUtils.debugByMsg(msg + "自動車の保管場所" + checkDto.getTaiyoHituyo() + checkDto.getParkingUmu());
		if (NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())
				&& (CodeConstant.ASKED_SHATAKU_HITSUYO.equals(checkDto.getTaiyoHituyo())
						|| CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(checkDto.getTaiyoHituyo()))
				&& (NfwStringUtils.isBlank(checkDto.getParkingUmu()))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUmu" }, MessageIdConstant.E_SKF_1054,
					"自動車の保管場所");
			result = false;
		}

		// 自動車の保管場所が必要の場合
		boolean bCheckNeedCar1 = true;
		boolean bCheckNeedCar2 = true;
		LogUtils.debugByMsg(msg + "自動車の保管場所が必要の場合" + checkDto.getParkingUmu());
		if (CodeConstant.CAR_PARK_HITUYO.equals(checkDto.getParkingUmu())) {
			// 駐車場のみの場合に申請不可チェック
			if (CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(checkDto.getTaiyoHituyo())) {
				switch (skf2020Sc002SharedService.checkParking(checkDto.getParking1stPlace(),
						checkDto.getParking2stPlace())) {
				case 1:
				case 2:
					// すでに駐車場を1台借りている状態で、2台の入力を行っている場合
					// 2台目の項目が入力されている場合はエラー
					LogUtils.debugByMsg(msg + "2台目の項目が入力されている場合はエラー" + checkDto.getCarName2() + checkDto.getCarNo2()
							+ checkDto.getCarExpirationDate2() + checkDto.getCarUser2()
							+ checkDto.getParkingUseDate2());
					if ((NfwStringUtils.isNotBlank(checkDto.getCarName2()))
							|| (NfwStringUtils.isNotBlank(checkDto.getCarNo2()))
							|| (NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate2()))
							|| (NfwStringUtils.isNotBlank(checkDto.getCarUser2()))
							|| (NfwStringUtils.isNotBlank(checkDto.getParkingUseDate2()))) {

						ServiceHelper.addErrorResultMessage(checkDto, null, MessageIdConstant.W_SKF_1001,
								"既に１台駐車場が貸与されています。申請内容を確認", CodeConstant.DOUBLE_QUOTATION);
						result = false;
					}
					break;
				case 3:
					bCheckNeedCar1 = false;
					bCheckNeedCar2 = false;
					ServiceHelper.addErrorResultMessage(checkDto, null, MessageIdConstant.W_SKF_1001,
							"既に２台駐車場が貸与されています。申請内容を確認", CodeConstant.DOUBLE_QUOTATION);
					result = false;
					break;
				}
			}

			// 駐車場1申請可能の場合
			if (bCheckNeedCar1 == true) {
				// 自動車の保有ラジオボタン「保有している」を選択している場合
				if (NfwStringUtils.isBlank(checkDto.getCarNoInputFlg())
						&& CodeConstant.CAR_HOYU.equals(checkDto.getCarNoInputFlg())) {

					LogUtils.debugByMsg(msg + "自動車の保管場所" + checkDto.getTaiyoHituyo() + checkDto.getParkingUmu());
					if (!(CodeConstant.ASKED_SHATAKU_FUYOU.equals(checkDto.getTaiyoHituyo()))
							&& NfwStringUtils.isBlank(checkDto.getParkingUmu())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNoInputFlg" },
								MessageIdConstant.E_SKF_1054, "自動車の保管場所");
						result = false;
					}

					// 自動車の車名(１台目)
					LogUtils.debugByMsg(msg + "自動車の車名(１台目)" + checkDto.getCarName());
					if (NfwStringUtils.isBlank(checkDto.getCarName())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carName" },
								MessageIdConstant.E_SKF_1048, "自動車の車名((１台目)");
						result = false;
					}
					// 自動車の登録番号(１台目)
					LogUtils.debugByMsg(msg + "自動車の登録番号(１台目)" + checkDto.getCarNo());
					if (NfwStringUtils.isBlank(checkDto.getCarNo())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNo" },
								MessageIdConstant.E_SKF_1048, "自動車の登録番号(１台目)");
						result = false;
					}
					// 車検の有効期間満了日(１台目)
					LogUtils.debugByMsg(msg + "車検の有効期間満了日(１台目)" + checkDto.getCarExpirationDate());
					if (NfwStringUtils.isBlank(checkDto.getCarExpirationDate())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate" },
								MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(１台目)");
						result = false;
					}
					// 自動車の使用者(１台目)
					LogUtils.debugByMsg(msg + "自動車の使用者(１台目)" + checkDto.getCarUser());
					if (NfwStringUtils.isBlank(checkDto.getCarUser())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser" },
								MessageIdConstant.E_SKF_1048, "自動車の使用者(１台目)");
						result = false;
					}
					// 自動車の保管場所使用開始日(１台目)
					LogUtils.debugByMsg(msg + "自動車の保管場所使用開始日(１台目)" + checkDto.getCarExpirationDate());
					if (NfwStringUtils.isBlank(checkDto.getParkingUseDate())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate" },
								MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(１台目)");
						result = false;
					}

				} else if (NfwStringUtils.isBlank(checkDto.getCarNoInputFlg())
						&& CodeConstant.CAR_YOTEI.equals(checkDto.getCarNoInputFlg())) {
					// 自動車の保有ラジオボタン「購入を予定している」を選択している場合

					// 自動車の使用者
					LogUtils.debugByMsg(msg + "自動車の使用者(１台目)" + checkDto.getCarUser());
					if (NfwStringUtils.isBlank(checkDto.getCarUser())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser" },
								MessageIdConstant.E_SKF_1048, "自動車の使用者(１台目)");
						result = false;
					}
					// 自動車の保管場所使用開始日
					if (NfwStringUtils.isBlank(checkDto.getParkingUseDate())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate" },
								MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(１台目)");
						result = false;
					}
				} else {
					// 自動車の保有ラジオボタンにチェックが入っていない場合

					// 自動車の保有
					LogUtils.debugByMsg(msg + "自動車の保有" + checkDto.getCarNoInputFlg());
					if (NfwStringUtils.isBlank(checkDto.getCarNoInputFlg())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNoInputFlg" },
								MessageIdConstant.E_SKF_1054, "自動車の保有(１台目)");
						result = false;
					}
					// 自動車の車名
					LogUtils.debugByMsg(msg + "自動車の車名" + checkDto.getCarName());
					if (NfwStringUtils.isBlank(checkDto.getCarName())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carName" },
								MessageIdConstant.E_SKF_1048, "自動車の車名(１台目)");
						result = false;
					}
					// 自動車の登録番号
					LogUtils.debugByMsg(msg + "自動車の登録番号" + checkDto.getCarNo());
					if (NfwStringUtils.isBlank(checkDto.getCarNo())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNo" },
								MessageIdConstant.E_SKF_1048, "自動車の登録番号(１台目)");
						result = false;
					}
					// 車検の有効期間満了日(１台目)
					LogUtils.debugByMsg(msg + "車検の有効期間満了日(１台目)" + checkDto.getCarExpirationDate());
					if (NfwStringUtils.isBlank(checkDto.getCarExpirationDate())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate" },
								MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(１台目)");
						result = false;
					}
					// 自動車の使用者
					LogUtils.debugByMsg(msg + "自動車の使用者(１台目)" + checkDto.getCarUser());
					if (NfwStringUtils.isBlank(checkDto.getCarUser())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser" },
								MessageIdConstant.E_SKF_1048, "自動車の使用者(１台目)");
						result = false;
					}
					// 自動車の保管場所使用開始日
					LogUtils.debugByMsg(msg + "自動車の保管場所使用開始日(１台目)" + checkDto.getParkingUseDate());
					if (NfwStringUtils.isBlank(checkDto.getParkingUseDate())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate" },
								MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(１台目)");
						result = false;
					}
				}
			}

			// 駐車場2申請可能の場合
			if (bCheckNeedCar2 == true) {
				// ２台目の情報のいづれかが設定されている場合
				LogUtils.debugByMsg(msg + "２台目の情報のいづれかが設定されている場合" + checkDto.getCarName2() + checkDto.getCarNo2()
						+ checkDto.getCarExpirationDate2() + checkDto.getCarUser2() + checkDto.getParkingUseDate2());
				if (NfwStringUtils.isNotBlank(checkDto.getCarName2()) || NfwStringUtils.isNotBlank(checkDto.getCarNo2())
						|| NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate2())
						|| NfwStringUtils.isNotBlank(checkDto.getCarUser2())
						|| NfwStringUtils.isNotBlank(checkDto.getParkingUseDate2())) {

					// 自動車の保有ラジオボタン「保有している」を選択している場合
					if (NfwStringUtils.isNotBlank(checkDto.getCarNoInputFlg2())
							&& CodeConstant.CAR_HOYU.equals(checkDto.getCarNoInputFlg2())) {

						// 自動車の車名(2台目)
						LogUtils.debugByMsg(msg + "自動車の車名(2台目)" + checkDto.getCarName());
						if (NfwStringUtils.isBlank(checkDto.getCarName2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carName2" },
									MessageIdConstant.E_SKF_1048, "自動車の車名(2台目)");
							result = false;
						}
						// 自動車の登録番号(2台目)
						LogUtils.debugByMsg(msg + "自動車の登録番号(2台目)" + checkDto.getCarNo2());
						if (NfwStringUtils.isBlank(checkDto.getCarNo2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNo2" },
									MessageIdConstant.E_SKF_1048, "自動車の登録番号(2台目)");
							result = false;
						}
						// 車検の有効期間満了日(2台目)
						LogUtils.debugByMsg(msg + "車検の有効期間満了日(2台目)" + checkDto.getCarExpirationDate2());
						if (NfwStringUtils.isBlank(checkDto.getCarExpirationDate2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate2" },
									MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(2台目)");
							result = false;
						}
						// 自動車の使用者(2台目)
						LogUtils.debugByMsg(msg + "自動車の使用者(2台目)" + checkDto.getCarUser2());
						if (NfwStringUtils.isBlank(checkDto.getCarUser2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser2" },
									MessageIdConstant.E_SKF_1048, "自動車の使用者(2台目)");
							result = false;
						}
						// 自動車の保管場所使用開始日(2台目)
						LogUtils.debugByMsg(msg + "自動車の保管場所使用開始日(１台目)" + checkDto.getCarExpirationDate2());
						if (NfwStringUtils.isBlank(checkDto.getParkingUseDate2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate2" },
									MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(2台目)");
							result = false;
						}

					} else if (NfwStringUtils.isNotBlank(checkDto.getCarNoInputFlg2())
							&& CodeConstant.CAR_YOTEI.equals(checkDto.getCarNoInputFlg2())) {
						// 自動車の保有ラジオボタン「購入を予定している」を選択している場合
						// 自動車の使用者(2台目)
						LogUtils.debugByMsg(msg + "自動車の使用者(2台目)" + checkDto.getCarUser2());
						if (NfwStringUtils.isBlank(checkDto.getCarUser2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser2" },
									MessageIdConstant.E_SKF_1048, "自動車の使用者(2台目)");
							result = false;
						}
						// 自動車の保管場所使用開始日(2台目)
						LogUtils.debugByMsg(msg + "自動車の保管場所使用開始日(１台目)" + checkDto.getCarExpirationDate2());
						if (NfwStringUtils.isBlank(checkDto.getParkingUseDate2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate2" },
									MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(2台目)");
							result = false;
						}
					} else {
						// 自動車の保有ラジオボタンにチェックが入っていない場合

						// 自動車の保有(2台目)
						LogUtils.debugByMsg(msg + "自動車の保有(2台目)" + checkDto.getCarNoInputFlg2());
						if (NfwStringUtils.isBlank(checkDto.getCarNoInputFlg2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNoInputFlg2" },
									MessageIdConstant.E_SKF_1054, "自動車の保有(2台目)");
							result = false;
						}
						// 自動車の車名(2台目)
						LogUtils.debugByMsg(msg + "自動車の車名(2台目)" + checkDto.getCarName());
						if (NfwStringUtils.isBlank(checkDto.getCarName2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carName2" },
									MessageIdConstant.E_SKF_1048, "自動車の車名(2台目)");
							result = false;
						}
						// 自動車の登録番号(2台目)
						LogUtils.debugByMsg(msg + "自動車の登録番号(2台目)" + checkDto.getCarNo2());
						if (NfwStringUtils.isBlank(checkDto.getCarNo2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNo2" },
									MessageIdConstant.E_SKF_1048, "自動車の登録番号(2台目)");
							result = false;
						}
						// 車検の有効期間満了日(2台目)
						LogUtils.debugByMsg(msg + "車検の有効期間満了日(2台目)" + checkDto.getCarExpirationDate2());
						if (NfwStringUtils.isBlank(checkDto.getCarExpirationDate2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate2" },
									MessageIdConstant.E_SKF_1048, "車検の有効期間満了日(2台目)");
							result = false;
						}
						// 自動車の使用者(2台目)
						LogUtils.debugByMsg(msg + "自動車の使用者(2台目)" + checkDto.getCarUser2());
						if (NfwStringUtils.isBlank(checkDto.getCarUser2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser2" },
									MessageIdConstant.E_SKF_1048, "自動車の使用者(2台目)");
							result = false;
						}
						// 自動車の保管場所使用開始日(2台目)
						LogUtils.debugByMsg(msg + "自動車の保管場所使用開始日(2台目)" + checkDto.getCarExpirationDate2());
						if (NfwStringUtils.isBlank(checkDto.getParkingUseDate2())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate2" },
									MessageIdConstant.E_SKF_1048, "自動車の保管場所使用開始日(2台目)");
							result = false;
						}
					}
				}
			}
		}

		// 現居住宅
		LogUtils.debugByMsg(msg + "現居住宅" + checkDto.getShatakuKanriId());
		if (checkDto.getShatakuKanriId() < 0) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "nowShataku" }, MessageIdConstant.E_SKF_1054,
					"現居住宅");
			result = false;
		}

		// 現居住宅が保有の場合
		if ((checkDto.getShatakuKanriId() > 0) && CodeConstant.HOYU.equals(checkDto.getNowShataku())) {
			// 保有社宅名
			LogUtils.debugByMsg(msg + "保有社宅が設定されているか" + checkDto.getShatakuKanriId() + checkDto.getNowShatakuName());
			if (checkDto.getShatakuKanriId() == 0) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "nowShatakuName" },
						MessageIdConstant.E_SKF_1048, "保有社宅名");
				result = false;
			}

			// 現保有の社宅
			LogUtils.debugByMsg(msg + "現保有の社宅(退居予定）" + checkDto.getTaiyoHituyo() + checkDto.getTaikyoYotei());
			if ((!(CodeConstant.ASKED_SHATAKU_PARKING_ONLY.equals(checkDto.getTaiyoHituyo())))
					&& NfwStringUtils.isBlank(checkDto.getTaikyoYotei())) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoYotei" },
						MessageIdConstant.E_SKF_1054, "現保有の社宅");
				result = false;
			}

			// 現保有の社宅 「退居する」の場合
			LogUtils.debugByMsg(msg + "現保有の社宅 「退居する」の場合" + checkDto.getTaikyoYotei());
			if (CodeConstant.LEAVE.equals(checkDto.getTaikyoYotei())) {
				// 退居予定日
				LogUtils.debugByMsg(msg + "退居予定日" + checkDto.getTaikyoYoteiDate());
				if (NfwStringUtils.isBlank(checkDto.getTaikyoYoteiDate())) {
					ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoYoteiDate" },
							MessageIdConstant.E_SKF_1048, "退居予定日");
					result = false;
				}

				// 社宅を「必要としない」以外の場合
				LogUtils.debugByMsg(msg + "退居理由" + checkDto.getTaikyoRiyuKbn() + checkDto.getTaiyoHituyo());
				if (NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())
						&& !(CodeConstant.ASKED_SHATAKU_FUYOU.equals(checkDto.getTaiyoHituyo()))) {
					// 退居理由
					if (NfwStringUtils.isBlank(checkDto.getTaikyoRiyuKbn())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoRiyuKbn" },
								MessageIdConstant.E_SKF_1054, "退居理由");
						result = false;
					}
					// 退居理由がその他の場合
					LogUtils.debugByMsg(msg + "退居理由その他" + checkDto.getTaikyoRiyuKbn() + checkDto.getTaikyoRiyu());
					if (NfwStringUtils.isNotBlank(checkDto.getTaikyoRiyuKbn())) {
						if (CodeConstant.OTHER_RIYU_VALUE.equals(checkDto.getTaikyoRiyuKbn())) {
							if (NfwStringUtils.isBlank(checkDto.getTaikyoRiyu())) {
								ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoRiyu" },
										MessageIdConstant.E_SKF_1048, "退居理由その他");
								result = false;
							}
						}
					}

					// 退居後連絡先
					LogUtils.debugByMsg(msg + "退居後連絡先" + checkDto.getTaikyogoRenrakuSaki());
					if (NfwStringUtils.isBlank(checkDto.getTaikyogoRenrakuSaki())) {
						ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyogoRenrakuSaki" },
								MessageIdConstant.E_SKF_1048, "退居後連絡先");
						result = false;
					}
					// 備品返却がある場合のみチェック
					LogUtils.debugByMsg(msg + "返却有無" + checkDto.getHdnBihinHenkyakuUmu());
					if (NfwStringUtils.isNotBlank(checkDto.getHdnBihinHenkyakuUmu())
							&& CodeConstant.BIHIN_HENKYAKU_SURU.equals(checkDto.getHdnBihinHenkyakuUmu())) {

						// 返却立会希望日(日)
						LogUtils.debugByMsg(msg + "返却立会希望日(日)" + checkDto.getSessionDay());
						if (NfwStringUtils.isBlank(checkDto.getSessionDay())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionDay" },
									MessageIdConstant.E_SKF_1048, "返却立会希望日(日)");
							result = false;
						}
						// 返却立会希望日(時)
						LogUtils.debugByMsg(msg + "返却立会希望日(時)" + checkDto.getSessionTime());
						if (NfwStringUtils.isBlank(checkDto.getSessionTime())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionTime" },
									MessageIdConstant.E_SKF_1054, "返却立会希望日(時)");
							result = false;
						}
						// 連絡先
						LogUtils.debugByMsg(msg + "連絡先" + checkDto.getRenrakuSaki());
						if (NfwStringUtils.isBlank(checkDto.getRenrakuSaki())) {
							ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" },
									MessageIdConstant.E_SKF_1048, "連絡先");
							result = false;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * 家族情報の未入力判定
	 * 
	 * @param result エラー情報
	 */
	private boolean checkEmptyKazokuInfo(Skf2020Sc002CheckAsyncDto checkDto) {

		boolean result = true;
		boolean isZokugaraError = false;
		boolean isShimeiError = false;
		boolean isNenreiError = false;

		// エラー箇所格納用
		List<String> zokugaraError = new ArrayList<String>();
		List<String> shimeiError = new ArrayList<String>();
		List<String> nenreiError = new ArrayList<String>();

		String msg = "必須チェック　値確認：";
		LogUtils.debugByMsg(
				msg + "家族1" + checkDto.getDokyoRelation1() + checkDto.getDokyoName1() + checkDto.getDokyoAge1());

		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation1()) && NfwStringUtils.isEmpty(checkDto.getDokyoName1())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge1())))) {
			// 家族1
			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation1())) {
				zokugaraError.add("dokyoRelation1");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName1())) {
				shimeiError.add("dokyoName1");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge1())) {
				nenreiError.add("dokyoAge1");
				isNenreiError = true;
			}
		}

		// 家族2
		LogUtils.debugByMsg(
				msg + "家族2" + checkDto.getDokyoRelation2() + checkDto.getDokyoName2() + checkDto.getDokyoAge2());
		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation2()) && NfwStringUtils.isEmpty(checkDto.getDokyoName2())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge2())))) {

			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation2())) {
				zokugaraError.add("dokyoRelation2");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName2())) {
				shimeiError.add("dokyoName2");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge2())) {
				nenreiError.add("dokyoAge2");
				isNenreiError = true;
			}
		}

		// 家族3
		LogUtils.debugByMsg(
				msg + "家族3" + checkDto.getDokyoRelation3() + checkDto.getDokyoName3() + checkDto.getDokyoAge3());
		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation3()) && NfwStringUtils.isEmpty(checkDto.getDokyoName3())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge3())))) {

			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation3())) {
				zokugaraError.add("dokyoRelation3");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName3())) {
				shimeiError.add("dokyoName3");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge3())) {
				nenreiError.add("dokyoAge3");
				isNenreiError = true;
			}
		}

		// 家族4
		LogUtils.debugByMsg(
				msg + "家族4" + checkDto.getDokyoRelation4() + checkDto.getDokyoName4() + checkDto.getDokyoAge4());
		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation4()) && NfwStringUtils.isEmpty(checkDto.getDokyoName4())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge4())))) {

			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation4())) {
				zokugaraError.add("dokyoRelation4");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName4())) {
				shimeiError.add("dokyoName4");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge4())) {
				nenreiError.add("dokyoAge4");
				isNenreiError = true;
			}
		}

		// 家族5
		LogUtils.debugByMsg(
				msg + "家族5" + checkDto.getDokyoRelation5() + checkDto.getDokyoName5() + checkDto.getDokyoAge5());
		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation5()) && NfwStringUtils.isEmpty(checkDto.getDokyoName5())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge5())))) {

			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation5())) {
				zokugaraError.add("dokyoRelation5");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName5())) {
				shimeiError.add("dokyoName5");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge5())) {
				nenreiError.add("dokyoAge5");
				isNenreiError = true;
			}
		}

		// 家族6
		LogUtils.debugByMsg(
				msg + "家族6" + checkDto.getDokyoRelation6() + checkDto.getDokyoName6() + checkDto.getDokyoAge6());
		if (!((NfwStringUtils.isEmpty(checkDto.getDokyoRelation6()) && NfwStringUtils.isEmpty(checkDto.getDokyoName6())
				&& NfwStringUtils.isEmpty(checkDto.getDokyoAge6())))) {

			if (NfwStringUtils.isBlank(checkDto.getDokyoRelation6())) {
				zokugaraError.add("dokyoRelation6");
				isZokugaraError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoName6())) {
				shimeiError.add("dokyoName6");
				isShimeiError = true;
			}
			if (NfwStringUtils.isBlank(checkDto.getDokyoAge6())) {
				nenreiError.add("dokyoAge6");
				isNenreiError = true;
			}
		}

		// 続柄エラー判定
		if (isZokugaraError) {
			String[] zokugaraErrorList = zokugaraError.toArray(new String[zokugaraError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, zokugaraErrorList, MessageIdConstant.E_SKF_1048, "同居家族 続柄");
		}
		// 氏名エラー判定
		if (isShimeiError) {
			String[] shimeiErrorList = shimeiError.toArray(new String[shimeiError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, shimeiErrorList, MessageIdConstant.E_SKF_1048, "同居家族 氏名");
		}
		// 年齢エラー判定
		if (isNenreiError) {
			String[] nenreiErrorList = nenreiError.toArray(new String[nenreiError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, nenreiErrorList, MessageIdConstant.E_SKF_1048, "同居家族 年齢");
		}

		// 続柄、氏名、年齢の一つでもエラーがあればfalse
		if (isZokugaraError == true || isShimeiError == true || isNenreiError == true) {
			result = false;
		}

		return result;
	}

	/**
	 * 文字種を判定
	 * 
	 * @param checkDto
	 * @return
	 */
	private Boolean checkValueType(Skf2020Sc002CheckAsyncDto checkDto) {

		boolean result = true;
		String msg = "文字種類判定";
		// 勤務先のTEL
		LogUtils.debugByMsg(msg + "勤務先のTEL" + checkDto.getTel());
		if ((NfwStringUtils.isNotBlank(checkDto.getTel()))
				&& SkfCheckUtils.isSkfPhoneFormat(checkDto.getTel()) == false) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1003,
					"勤務先のTEL");
			result = false;
		}

		List<String> nenreiError = new ArrayList<String>();
		// 必要とする社宅が世帯の場合
		LogUtils.debugByMsg(msg + "必要とする社宅が世帯の場合" + checkDto.getHitsuyoShataku());
		if ((NfwStringUtils.isNotBlank(checkDto.getHitsuyoShataku()))
				&& CodeConstant.SETAI.equals(checkDto.getHitsuyoShataku())) {

			boolean isError = false;
			// 年齢1
			LogUtils.debugByMsg(msg + "年齢1" + checkDto.getDokyoAge1());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge1())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge1()) == false)) {
				nenreiError.add("dokyoAge1");
				isError = true;
			}
			// 年齢2
			LogUtils.debugByMsg(msg + "年齢2" + checkDto.getDokyoAge2());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge2())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge2()) == false)) {
				nenreiError.add("dokyoAge2");
				isError = true;
			}
			// 年齢3
			LogUtils.debugByMsg(msg + "年齢3" + checkDto.getDokyoAge3());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge3())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge3()) == false)) {
				nenreiError.add("dokyoAge3");
				isError = true;
			}
			// 年齢4
			LogUtils.debugByMsg(msg + "年齢4" + checkDto.getDokyoAge4());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge4())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge4()) == false)) {
				nenreiError.add("dokyoAge4");
				isError = true;
			}
			// 年齢5
			LogUtils.debugByMsg(msg + "年齢5" + checkDto.getDokyoAge5());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge5())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge5()) == false)) {
				nenreiError.add("dokyoAge5");
				isError = true;
			}
			// 年齢6
			LogUtils.debugByMsg(msg + "年齢6" + checkDto.getDokyoAge6());
			if ((NfwStringUtils.isNotBlank(checkDto.getDokyoAge6())
					&& CheckUtils.isNumeric(checkDto.getDokyoAge6()) == false)) {
				nenreiError.add("dokyoAge6");
				isError = true;
			}
			if (isError) {
				String[] nenreiErrorList = nenreiError.toArray(new String[nenreiError.size()]);
				ServiceHelper.addErrorResultMessage(checkDto, nenreiErrorList, MessageIdConstant.E_SKF_1050, "同居家族 年齢");
				result = false;
			}
		}

		// 連絡先
		LogUtils.debugByMsg(msg + "連絡先" + checkDto.getRenrakuSaki());
		if (NfwStringUtils.isNotBlank(checkDto.getRenrakuSaki())
				&& SkfCheckUtils.isSkfPhoneFormat(checkDto.getRenrakuSaki()) == false) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" }, MessageIdConstant.E_SKF_1003,
					"連絡先");
		}

		return result;
	}

	/**
	 * バイト数を判定
	 * 
	 * @param checkDto
	 * @return
	 */
	private Boolean checkByteCount(Skf2020Sc002CheckAsyncDto checkDto) throws UnsupportedEncodingException {

		boolean result = true;

		// 勤務先のTEL
		LogUtils.debugByMsg("桁数チェック " + "勤務先のTEL - " + checkDto.getTel());
		if (NfwStringUtils.isNotBlank(checkDto.getNewAffiliation1Other())
				&& CheckUtils.isMoreThanByteSize(checkDto.getTel().trim(), 13)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "tel" }, MessageIdConstant.E_SKF_1071,
					"勤務先のTEL", "13");
			result = false;
		}

		// 新所属 機関-その他

		// 新所属 部等-その他
		LogUtils.debugByMsg(
				"桁数チェック " + "新所属 部等-その他 - " + checkDto.getAffiliation1Cd() + checkDto.getNewAffiliation1Other());
		if ((NfwStringUtils.isNotBlank(checkDto.getAffiliation1Cd())
				&& CodeConstant.OTHER_AGENCY_ITEM_VALUE.equals(checkDto.getAffiliation1Cd()))) {
			if (NfwStringUtils.isNotBlank(checkDto.getNewAffiliation1Other())
					&& CheckUtils.isMoreThanByteSize(checkDto.getNewAffiliation1Other().trim(), 128)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "newAffiliation1Other" },
						MessageIdConstant.E_SKF_1071, "新所属 部等-その他", "64");
				result = false;
			}
		}

		// 新所属 室チームまたは課-その他
		LogUtils.debugByMsg(
				"桁数チェック " + "新所属 室チームまたは課-その他 - " + checkDto.getAffiliation2Cd() + checkDto.getNewAffiliation2Other());
		if ((NfwStringUtils.isNotBlank(checkDto.getAffiliation2Cd())
				&& CodeConstant.OTHER_AGENCY_ITEM_VALUE.equals(checkDto.getAffiliation2Cd()))) {
			if (NfwStringUtils.isNotBlank(checkDto.getNewAffiliation2Other())
					&& CheckUtils.isMoreThanByteSize(checkDto.getNewAffiliation2Other().trim(), 128)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "newAffiliation2Other" },
						MessageIdConstant.E_SKF_1071, "新所属 室チームまたは課-その他", "64");
				result = false;
			}
		}

		// 必要とする社宅が世帯の場合
		if ((NfwStringUtils.isNotBlank(checkDto.getHitsuyoShataku())
				&& NfwStringUtils.isNotBlank(checkDto.getHitsuyoShataku()))
				&& CodeConstant.SETAI.equals(checkDto.getHitsuyoShataku())) {
			// 続柄
			result = checkByteCountZokugara(checkDto);
			// 氏名
			result = checkByteCountShimei(checkDto);
			// 年齢
			result = checkByteCountNenrei(checkDto);
		}

		// 自動車の保管場所
		// 自動車の保管場所が必要の場合
		LogUtils.debugByMsg("桁数チェック " + "自動車の保管場所必要有無 - " + checkDto.getParkingUmu());
		if (CodeConstant.CAR_PARK_HITUYO.equals(checkDto.getParkingUmu())) {
			// 自動車の車名
			LogUtils.debugByMsg("桁数チェック " + "自動車の車名　- " + checkDto.getCarName());
			if (NfwStringUtils.isNotBlank(checkDto.getCarName())
					&& CheckUtils.isMoreThanByteSize(checkDto.getCarName().trim(), 66)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carName" }, MessageIdConstant.E_SKF_1071,
						"自動車の車名", "33");
				result = false;
			}
			// 自動車の登録番号
			LogUtils.debugByMsg("桁数チェック " + "自動車の登録番号　- " + checkDto.getCarNo());
			if (NfwStringUtils.isNotBlank(checkDto.getCarNo())
					&& CheckUtils.isMoreThanByteSize(checkDto.getCarNo().trim(), 66)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carNo" }, MessageIdConstant.E_SKF_1071,
						"自動車の登録番号", "33");
				result = false;
			}
			// 自動車の使用者
			LogUtils.debugByMsg("桁数チェック " + "自動車の使用者　- " + checkDto.getCarUser());
			if (NfwStringUtils.isNotBlank(checkDto.getCarUser())
					&& CheckUtils.isMoreThanByteSize(checkDto.getCarUser().trim(), 66)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carUser" }, MessageIdConstant.E_SKF_1071,
						"自動車の使用者", "33");
				result = false;
			}
		}

		// 現保有の社宅
		// 退居予定が退居するの場合
		if (CodeConstant.LEAVE.equals(checkDto.getTaikyoYotei())) {
			// 特殊事情など
			LogUtils.debugByMsg("桁数チェック " + "特殊事情など　- " + checkDto.getTokushuJijo());
			if (NfwStringUtils.isNotBlank(checkDto.getTokushuJijo())
					&& CheckUtils.isMoreThanByteSize(checkDto.getTokushuJijo().trim(), 256)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "tokushuJijo" },
						MessageIdConstant.E_SKF_1071, "特殊事情など", "128");
				result = false;
			}
			// 社宅の状態
			LogUtils.debugByMsg("桁数チェック " + "社宅の状態　- " + checkDto.getShatakuJyotai());
			if (NfwStringUtils.isNotBlank(checkDto.getShatakuJyotai())
					&& CheckUtils.isMoreThanByteSize(checkDto.getShatakuJyotai().trim(), 256)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "shatakuJyotai" },
						MessageIdConstant.E_SKF_1071, "社宅の状態", "128");
				result = false;
			}
			// 退居理由がその他の場合
			LogUtils.debugByMsg("桁数チェック " + "退居理由がその他の場合　- " + checkDto.getTaikyoRiyuKbn() + checkDto.getTaikyoRiyu());
			if (NfwStringUtils.isNotBlank(checkDto.getTaikyoRiyuKbn())
					&& CodeConstant.OTHER_AGENCY_IITEM_NAME.equals(checkDto.getTaikyoRiyuKbn())
					&& NfwStringUtils.isNotBlank(checkDto.getTaikyoRiyu())
					&& CheckUtils.isMoreThanByteSize(checkDto.getTaikyoRiyu().trim(), 256)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoRiyu" },
						MessageIdConstant.E_SKF_1071, "退居理由", "126");
				result = false;
			}
			// 退居後の連絡先
			LogUtils.debugByMsg("桁数チェック " + "退居後の連絡先　- " + checkDto.getTaikyogoRenrakuSaki());
			if (NfwStringUtils.isNotBlank(checkDto.getTaikyogoRenrakuSaki())
					&& CheckUtils.isMoreThanByteSize(checkDto.getTaikyogoRenrakuSaki().trim(), 128)) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyogoRenrakuSaki" },
						MessageIdConstant.E_SKF_1071, "退居後の連絡先", "64");
				result = false;
			}
		}

		// 連絡先
		LogUtils.debugByMsg("桁数チェック " + "連絡先 - " + checkDto.getRenrakuSaki());
		if (NfwStringUtils.isNotBlank(checkDto.getRenrakuSaki())
				&& CheckUtils.isMoreThanByteSize(checkDto.getRenrakuSaki().trim(), 13)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "renrakuSaki" }, MessageIdConstant.E_SKF_1071,
					"連絡先", "13");
			result = false;
		}
		return result;

	}

	/**
	 * 続柄のバイト数を判定
	 * 
	 * @param checkDto
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean checkByteCountZokugara(Skf2020Sc002CheckAsyncDto checkDto) throws UnsupportedEncodingException {
		boolean result = true;
		boolean isError = false;
		List<String> zokugaraError = new ArrayList<String>();

		// 続柄1
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数1 - " + checkDto.getDokyoRelation1());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation1())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation1().trim(), 10)) {
			zokugaraError.add("dokyoRelation1");
			isError = true;
		}
		// 続柄2
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数2 - " + checkDto.getDokyoRelation2());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation2())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation2().trim(), 10)) {
			zokugaraError.add("dokyoRelation2");
			isError = true;
		}
		// 続柄3
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数3 - " + checkDto.getDokyoRelation3());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation3())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation3().trim(), 10)) {
			zokugaraError.add("dokyoRelation3");
			isError = true;
		}
		// 続柄4
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数4 - " + checkDto.getDokyoRelation4());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation4())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation4().trim(), 10)) {
			zokugaraError.add("dokyoRelation4");
			isError = true;
		}
		// 続柄5
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数5 - " + checkDto.getDokyoRelation5());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation5())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation5().trim(), 10)) {
			zokugaraError.add("dokyoRelation5");
			isError = true;
		}
		// 続柄6
		LogUtils.debugByMsg("桁数チェック " + "続柄のバイト数6 - " + checkDto.getDokyoRelation6());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoRelation6())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoRelation6().trim(), 10)) {
			zokugaraError.add("dokyoRelation6");
			isError = true;
		}
		// エラーメッセージの追加
		if (isError) {
			String[] zokugaraErrorList = zokugaraError.toArray(new String[zokugaraError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, zokugaraErrorList, MessageIdConstant.E_SKF_1071, "同居家族 続柄",
					"5");
			result = false;
		}
		return result;
	}

	/**
	 * 氏名のバイト数を判定
	 * 
	 * @param checkDto
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean checkByteCountShimei(Skf2020Sc002CheckAsyncDto checkDto) throws UnsupportedEncodingException {
		boolean isError = false;
		boolean result = true;
		List<String> shimeiError = new ArrayList<String>();
		// 氏名1
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数1 - " + checkDto.getDokyoName1());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName1())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName1().trim(), 26)) {
			shimeiError.add("dokyoName1");
			isError = true;
		}
		// 氏名2
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数2 - " + checkDto.getDokyoName2());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName2())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName2().trim(), 26)) {
			shimeiError.add("dokyoName2");
			isError = true;
		}
		// 氏名3
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数3 - " + checkDto.getDokyoName3());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName3())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName3().trim(), 26)) {
			shimeiError.add("dokyoName3");
			isError = true;
		}
		// 氏名4
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数4 - " + checkDto.getDokyoName4());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName4())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName4().trim(), 26)) {
			shimeiError.add("dokyoName4");
			isError = true;
		}
		// 氏名5
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数5 - " + checkDto.getDokyoName5());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName5())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName5().trim(), 26)) {
			shimeiError.add("dokyoName5");
			isError = true;
		}
		// 氏名6
		LogUtils.debugByMsg("桁数チェック " + "氏名のバイト数6 - " + checkDto.getDokyoName6());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoName6())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoName6().trim(), 26)) {
			shimeiError.add("dokyoName6");
			isError = true;
		}
		// エラーメッセージの追加
		if (isError) {
			String[] shimeiErrorList = shimeiError.toArray(new String[shimeiError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, shimeiErrorList, MessageIdConstant.E_SKF_1071, "同居家族 氏名",
					"13");
			result = false;
		}
		return result;
	}

	/**
	 * 
	 * 年齢のバイト数を判定
	 * 
	 * @param checkDto
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private boolean checkByteCountNenrei(Skf2020Sc002CheckAsyncDto checkDto) throws UnsupportedEncodingException {
		boolean isError = false;
		boolean result = true;
		List<String> nenreiError = new ArrayList<String>();
		// 年齢1
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数1 - " + checkDto.getDokyoAge1());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge1())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge1().trim(), 3)) {
			nenreiError.add("dokyoAge1");
			isError = true;
		}
		// 年齢2
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数2 - " + checkDto.getDokyoAge2());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge2())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge2().trim(), 3)) {
			nenreiError.add("dokyoAge2");
			isError = true;
		}
		// 年齢3
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数3 - " + checkDto.getDokyoAge3());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge3())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge3().trim(), 3)) {
			nenreiError.add("dokyoAge3");
			isError = true;
		}
		// 年齢4
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数4 - " + checkDto.getDokyoAge4());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge4())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge4().trim(), 3)) {
			nenreiError.add("dokyoAge4");
			isError = true;
		}
		// 年齢5
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数5 - " + checkDto.getDokyoAge5());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge5())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge5().trim(), 3)) {
			nenreiError.add("dokyoAge5");
			isError = true;
		}
		// 年齢6
		LogUtils.debugByMsg("桁数チェック " + "年齢のバイト数6 - " + checkDto.getDokyoAge6());
		if (NfwStringUtils.isNotBlank(checkDto.getDokyoAge6())
				&& CheckUtils.isMoreThanByteSize(checkDto.getDokyoAge6().trim(), 3)) {
			nenreiError.add("dokyoAge6");
			isError = true;
		}
		// エラーメッセージの追加
		if (isError) {
			String[] nenreiErrorList = nenreiError.toArray(new String[nenreiError.size()]);
			ServiceHelper.addErrorResultMessage(checkDto, nenreiErrorList, MessageIdConstant.E_SKF_1049, "同居家族 年齢",
					"3");
			result = false;
		}
		return result;
	}

	/**
	 * 日付の整合性、形式を判定
	 * 
	 * @param checkDto
	 * @return
	 */
	private Boolean checkDateFormat(Skf2020Sc002CheckAsyncDto checkDto) {

		boolean result = true;
		// システム日付を文字列に変換
		String sysDate = DateUtils.getSysDateString(SkfCommonConstant.YMD_STYLE_YYYYMMDD_SLASH);

		// 入居希望日(予定日)
		LogUtils.debugByMsg("形式チェック " + "入居希望日(予定日) - " + checkDto.getNyukyoYoteiDate() + checkDto.getTaiyoHituyo());
		if ((NfwStringUtils.isNotBlank(checkDto.getTaiyoHituyo())
				&& CodeConstant.ASKED_SHATAKU_HITSUYO.equals(checkDto.getTaiyoHituyo()))
				&& NfwStringUtils.isNotBlank(checkDto.getNyukyoYoteiDate()) && !(SkfCheckUtils
						.isSkfDateFormat(checkDto.getNyukyoYoteiDate().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "nyukyoYoteiDate" },
					MessageIdConstant.E_SKF_1055, "入居希望日(予定日)");
			result = false;
		}

		// 車検の有効期間満了日(１台目)
		LogUtils.debugByMsg("形式チェック " + "車検の有効期間満了日(１台目) - " + checkDto.getCarExpirationDate());
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate())) && !(SkfCheckUtils
				.isSkfDateFormat(checkDto.getCarExpirationDate().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate" },
					MessageIdConstant.E_SKF_1055, "車検の有効期間満了日(1台目)");
			result = false;
		}
		// 当日未満ならエラー
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate()))
				&& CheckUtils.isDateLessThan(checkDto.getCarExpirationDate(), sysDate)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate" },
					MessageIdConstant.E_SKF_2017, "車検の有効期間満了日(1台目)");
			result = false;
		}
		// 車検の有効期間満了日(２台目)
		LogUtils.debugByMsg("形式チェック " + "車検の有効期間満了日(2台目) - " + checkDto.getCarExpirationDate2());
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate2())) && !(SkfCheckUtils
				.isSkfDateFormat(checkDto.getCarExpirationDate2().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate2" },
					MessageIdConstant.E_SKF_1055, "車検の有効期間満了日(2台目)");
			result = false;
		}
		// 当日未満ならエラー
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate2()))
				&& CheckUtils.isDateLessThan(checkDto.getCarExpirationDate2(), sysDate)) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "carExpirationDate2" },
					MessageIdConstant.E_SKF_2017, "車検の有効期間満了日(2台目)");
			result = false;
		}

		// 保管場所使用開始日(1台目)
		LogUtils.debugByMsg("形式チェック " + "保管場所使用開始日(1台目) - " + checkDto.getParkingUseDate());
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate())) && !(SkfCheckUtils
				.isSkfDateFormat(checkDto.getParkingUseDate().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate" },
					MessageIdConstant.E_SKF_1055, "保管場所使用開始日(1台目)");
			result = false;
		}
		// 保管場所使用開始日(2台目)
		LogUtils.debugByMsg("形式チェック " + "保管場所使用開始日(2台目) - " + checkDto.getParkingUseDate2());
		if ((NfwStringUtils.isNotBlank(checkDto.getCarExpirationDate2())) && !(SkfCheckUtils
				.isSkfDateFormat(checkDto.getParkingUseDate2().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
			ServiceHelper.addErrorResultMessage(checkDto, new String[] { "parkingUseDate2" },
					MessageIdConstant.E_SKF_1055, "保管場所使用開始日(2台目)");
			result = false;
		}

		// 現保有の社宅
		// 退居予定が退居するの場合
		if (CodeConstant.LEAVE.equals(checkDto.getTaikyoYotei())) {
			// 退居予定日
			LogUtils.debugByMsg("形式チェック " + "退居予定日 - " + checkDto.getTaikyoYoteiDate());
			if ((NfwStringUtils.isNotBlank(checkDto.getTaikyoYoteiDate())) && !(SkfCheckUtils
					.isSkfDateFormat(checkDto.getTaikyoYoteiDate().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "taikyoYoteiDate" },
						MessageIdConstant.E_SKF_1055, "退居予定日");
				result = false;
			}
			// 返却立会希望日(日)
			LogUtils.debugByMsg("形式チェック " + "返却立会希望日(日) - " + checkDto.getSessionDay());
			if ((NfwStringUtils.isNotBlank(checkDto.getSessionDay())) && !(SkfCheckUtils
					.isSkfDateFormat(checkDto.getSessionDay().trim(), CheckUtils.DateFormatType.YYYYMMDD))) {
				ServiceHelper.addErrorResultMessage(checkDto, new String[] { "sessionDay" },
						MessageIdConstant.E_SKF_1055, "返却立会希望日(日)");
				result = false;
			}
		}
		return result;
	}

	// /**
	// * デフォルト色に設定
	// *
	// * @param dto
	// */
	// protected void setDefultColor(Skf2020Sc002CheckAsyncDto dto) {
	//
	// // TEL
	// dto.setTelErr(CodeConstant.DOUBLE_QUOTATION);
	// // 社宅を必要としますか？
	// dto.setTaiyoHituyoErr(CodeConstant.DOUBLE_QUOTATION);
	// // 社宅を必要とする理由
	// dto.setHitsuyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);
	// // 社宅を必要としない理由
	// dto.setFuhitsuyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);
	// // 機関
	// dto.setNewAgencyErr(CodeConstant.DOUBLE_QUOTATION);
	// // 部等
	// dto.setNewAffiliation1Err(CodeConstant.DOUBLE_QUOTATION);
	// // 室、チーム又は課
	// dto.setNewAffiliation2Err(CodeConstant.DOUBLE_QUOTATION);
	// // 必要とする社宅
	// dto.setHitsuyoShatakuErr(CodeConstant.DOUBLE_QUOTATION);
	// // 続柄
	// dto.setDokyoRelation1Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoRelation2Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoRelation3Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoRelation4Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoRelation5Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoRelation6Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 氏名
	// dto.setDokyoName1Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoName2Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoName3Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoName4Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoName5Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoName6Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 年齢
	// dto.setDokyoAge1Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoAge2Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoAge3Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoAge4Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoAge5Err(CodeConstant.DOUBLE_QUOTATION);
	// dto.setDokyoAge6Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 入居希望日
	// dto.setNyukyoYoteiDateErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の保管場所
	// dto.setParkingUmuErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の保管場所
	// dto.setCarNoInputFlgErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の車名(１台目)
	// dto.setCarNameErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の登録番号(１台目)
	// dto.setCarNoErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 車検の有効期間満了日(１台目)
	// dto.setCarExpirationDateErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の使用者(１台目)
	// dto.setCarUserErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の保管場所 使用開始日(１台目)
	// dto.setParkingUseDateErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の車名(2台目)
	// dto.setCarName2Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の登録番号(2台目)
	// dto.setCarNo2Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 車検の有効期間満了日(2台目)
	// dto.setCarExpirationDate2Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の使用者(2台目)
	// dto.setCarUser2Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 自動車の保管場所 使用開始日(2台目)
	// dto.setParkingUseDate2Err(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 現居住宅
	// dto.setNowShatakuErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 保有社宅名
	// dto.setNowShatakuNameErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 特殊事情等
	// dto.setTokushuJijoErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 現保有の社宅(退居予定)
	// dto.setTaikyoYoteiErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 退居予定日
	// dto.setTaikyoYoteiDateErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 社宅の状態
	// dto.setShatakuJyotaiErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 退居理由
	// dto.setDdlTaikyoRiyuKbnListErr(CodeConstant.DOUBLE_QUOTATION);
	// dto.setTaikyoRiyuErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 退居連絡先
	// dto.setTaikyogoRenrakuSakiErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 返却立会希望日
	// dto.setSessionDayErr(CodeConstant.DOUBLE_QUOTATION);
	// dto.setSessionTimeErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// // 連絡先
	// dto.setRenrakuSakiErr(CodeConstant.DOUBLE_QUOTATION);
	//
	// }

}
