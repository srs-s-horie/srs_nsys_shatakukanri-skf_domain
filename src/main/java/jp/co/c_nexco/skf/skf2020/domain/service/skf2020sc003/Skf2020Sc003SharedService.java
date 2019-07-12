package jp.co.c_nexco.skf.skf2020.domain.service.skf2020sc003;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetMaxCycleBillingYYMMExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetBihinInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetMaxCycleBillingYYMMExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetParkingRirekiDataExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNameListExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf2020Sc003.Skf2020Sc003GetTeijiDataInfoExpRepository;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.util.SkfDateFormatUtils;
import jp.co.c_nexco.skf.common.util.SkfGenericCodeUtils;

@Service
public class Skf2020Sc003SharedService {

	@Autowired
	private Skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetShatakuNameListExpRepository skf2020Sc003GetShatakuNameListExpRepository;
	@Autowired
	private Skf2020Sc003GetBihinInfoExpRepository skf2020Sc003GetBihinInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetTeijiDataInfoExpRepository skf2020Sc003GetTeijiDataInfoExpRepository;
	@Autowired
	private Skf2020Sc003GetMaxCycleBillingYYMMExpRepository skf2020Sc003GetMaxCycleBillingYYMMExpRepository;
	@Autowired
	private Skf2020Sc003GetParkingRirekiDataExpRepository skf2020Sc003GetParkingRirekiDataExpRepository;

	@Autowired
	private SkfDateFormatUtils skfDateFormatUtils;
	@Autowired
	private SkfGenericCodeUtils skfGenericCodeUtils;

	public Skf2020Sc003GetShatakuNyukyoKiboInfoExp getShatakuNyukyoKiboInfo(String companyCd, String applNo) {
		Skf2020Sc003GetShatakuNyukyoKiboInfoExp shatakuNyukyoKiboInfo = new Skf2020Sc003GetShatakuNyukyoKiboInfoExp();
		Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter param = new Skf2020Sc003GetShatakuNyukyoKiboInfoExpParameter();
		param.setCompanyCd(companyCd);
		param.setApplNo(applNo);
		shatakuNyukyoKiboInfo = skf2020Sc003GetShatakuNyukyoKiboInfoExpRepository.getShatakuNyukyoKiboInfo(param);

		return shatakuNyukyoKiboInfo;
	}

	/**
	 * 保有社宅の社宅管理IDを取得します
	 * 
	 * @param shainNo
	 * @param shatakuKanriNo
	 * @param shatakuRoomKanriNo
	 * @return
	 */
	public int getNowShatakuKanriID(String shainNo, String shatakuKanriNo, String shatakuRoomKanriNo) {
		int shatakuKanriId = 0;
		String nyukyoDate = skfDateFormatUtils.dateFormatFromDate(new Date(), "yyyyMMdd");
		List<Skf2020Sc003GetShatakuNameListExp> shatakuNameList = new ArrayList<Skf2020Sc003GetShatakuNameListExp>();
		Skf2020Sc003GetShatakuNameListExpParameter param = new Skf2020Sc003GetShatakuNameListExpParameter();
		param.setShainNo(shainNo);
		param.setNyukyoDate(nyukyoDate);
		shatakuNameList = skf2020Sc003GetShatakuNameListExpRepository.getShatakuNameList(param);
		if (shatakuNameList != null && shatakuNameList.size() > 0) {
			for (Skf2020Sc003GetShatakuNameListExp shatakuNameData : shatakuNameList) {
				if (shatakuKanriNo.equals(String.valueOf(shatakuNameData.getShatakuKanriNo()))
						&& shatakuRoomKanriNo.equals(String.valueOf(shatakuNameData.getShatakuRoomKanriNo()))) {
					shatakuKanriId = shatakuNameData.getShatakuKanriId();
				}
			}
		}

		return shatakuKanriId;
	}

	public List<Skf2020Sc003GetBihinInfoExp> getBihinInfo(String shatakuKanriId, String shainNo, String yearMonth) {
		List<Skf2020Sc003GetBihinInfoExp> bihinInfo = new ArrayList<Skf2020Sc003GetBihinInfoExp>();
		Skf2020Sc003GetBihinInfoExpParameter param = new Skf2020Sc003GetBihinInfoExpParameter();
		param.setShatakuKanriId(Long.parseLong(shatakuKanriId));
		param.setShainNo(shainNo);
		param.setYearMonth(yearMonth);
		bihinInfo = skf2020Sc003GetBihinInfoExpRepository.getBihinInfo(param);

		return bihinInfo;
	}

	/**
	 * 条件を指定して提示データ情報を取得します
	 * 
	 * @param shainNo
	 * @param nyutaikyoKbn
	 * @param applNo
	 * @return
	 */
	public List<Skf2020Sc003GetTeijiDataInfoExp> getTeijiDataInfo(String shainNo, String nyutaikyoKbn, String applNo) {
		List<Skf2020Sc003GetTeijiDataInfoExp> teijiDataList = new ArrayList<Skf2020Sc003GetTeijiDataInfoExp>();
		Skf2020Sc003GetTeijiDataInfoExpParameter param = new Skf2020Sc003GetTeijiDataInfoExpParameter();
		param.setShainNo(shainNo);
		param.setNyutaikyoKbn(nyutaikyoKbn);
		param.setApplNo(applNo);
		teijiDataList = skf2020Sc003GetTeijiDataInfoExpRepository.getTeijiDataInfo(param);
		return teijiDataList;
	}

	/**
	 * 社宅管理システム都道府県名称取得
	 * 
	 * @param prefCd
	 * @return
	 */
	public String getShatakuPrefName(String prefCd) {
		String retPrefName = CodeConstant.NONE;

		// 都道府県名称の取得
		Map<String, String> prefMap = new HashMap<String, String>();
		prefMap = skfGenericCodeUtils.getGenericCode("SKF1064");
		if (prefMap != null) {
			retPrefName = prefMap.get(prefCd);
		}

		return retPrefName;
	}

	/**
	 * 社宅管理システム規格名称取得
	 * 
	 * @param kikakuCd
	 * @return
	 */
	public String getShatakuKikakuKBN(String kikakuCd) {
		String retKikakuName = CodeConstant.NONE;

		// 規格区分の取得
		Map<String, String> kikakuMap = new HashMap<String, String>();
		kikakuMap = skfGenericCodeUtils.getGenericCode("SKF1073");
		if (kikakuMap != null) {
			retKikakuName = kikakuMap.get(kikakuCd);
		}

		return retKikakuName;
	}

	public String getMaxCycleBillingYYMM() {
		String syoriYYMM = CodeConstant.NONE;
		Skf2020Sc003GetMaxCycleBillingYYMMExp resultData = new Skf2020Sc003GetMaxCycleBillingYYMMExp();
		resultData = skf2020Sc003GetMaxCycleBillingYYMMExpRepository.getMaxCycleBillingYYMM();
		if (resultData != null) {
			syoriYYMM = resultData.getMaxCycleBillingYymm();
		}

		return syoriYYMM;
	}

	public List<Skf2020Sc003GetParkingRirekiDataExp> getParkingRirekiData(long shatakuKanriNo, long shatakuRoomKanriNo,
			String shainNo, String yearMonth) {
		List<Skf2020Sc003GetParkingRirekiDataExp> returnList = new ArrayList<Skf2020Sc003GetParkingRirekiDataExp>();
		Skf2020Sc003GetParkingRirekiDataExpParameter param = new Skf2020Sc003GetParkingRirekiDataExpParameter();
		param.setShainNo(shainNo);
		param.setShatakuKanriNo(shatakuKanriNo);
		param.setShatakuRoomKanriNo(shatakuRoomKanriNo);
		param.setYearMonth(yearMonth);
		returnList = skf2020Sc003GetParkingRirekiDataExpRepository.getParkingRirekiData(param);
		return returnList;
	}
}
