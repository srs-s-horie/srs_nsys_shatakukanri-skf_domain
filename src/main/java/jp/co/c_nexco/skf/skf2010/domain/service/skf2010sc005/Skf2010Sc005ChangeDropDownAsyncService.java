package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc005;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.util.SkfDropDownUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc005.Skf2010Sc005ChangeDropDownAsyncDto;

/**
 * Skf2010Sc005 承認一覧ドロップダウン更新処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc005ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf2010Sc005ChangeDropDownAsyncDto> {

	@Autowired
	private Skf2010Sc005SharedService skf2010Sc005SharedService;
	@Autowired
	private SkfDropDownUtils skfDropDownUtils;
	@Value("${skf.common.company_cd}")
	private String companyCd;

	@Override
	public AsyncBaseDto index(Skf2010Sc005ChangeDropDownAsyncDto dto) throws Exception {

		String agencyCd = dto.getAgency();
		String affiliation1Cd = dto.getAffiliation1();

		// 初期化処理
		List<Map<String, Object>> affiliation1List = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> affiliation2List = new ArrayList<Map<String, Object>>();

		// 機関コードが設定されていた場合は部等コードリストを作成
		if (agencyCd != null && !CheckUtils.isEmpty(agencyCd)) {
			affiliation1List = skfDropDownUtils.getDdlAffiliation1ByCd(companyCd, agencyCd, null, true);
		}
		// 部等コードが設定されていた場合は室、チーム又は課コードリストを作成
		if (affiliation1Cd != null && !CheckUtils.isEmpty(affiliation1Cd)) {
			affiliation2List = skfDropDownUtils.getDdlAffiliation2ByCd(companyCd, agencyCd, affiliation1Cd, null, true);
		}

		dto.setDdlAffiliation1List(affiliation1List);
		dto.setDdlAffiliation2List(affiliation2List);

		return dto;
	}

}
