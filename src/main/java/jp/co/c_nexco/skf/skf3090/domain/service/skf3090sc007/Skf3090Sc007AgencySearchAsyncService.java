package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc007.Skf3090Sc007GetAgencyInfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc007.Skf3090Sc007GetAgencyInfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc007.Skf3090Sc007GetAgencyInfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007AgencySearchAsyncDto;

/**
 * Skf3090Sc007AgencySearchService 機関の名称を検索ボタン押下時の処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007AgencySearchAsyncService extends SkfAsyncServiceAbstract<Skf3090Sc007AgencySearchAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf3090Sc007GetAgencyInfoExpRepository skf3090Sc007GetAgencyInfoExpRepository;

	@Override
	public Skf3090Sc007AgencySearchAsyncDto index(Skf3090Sc007AgencySearchAsyncDto agencySearchAsyncDto)
			throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("名称を検索", CodeConstant.C001, FunctionIdConstant.SKF3090_SC007);

		List<Skf3090Sc007GetAgencyInfoExp> resultList = new ArrayList<Skf3090Sc007GetAgencyInfoExp>();
		Skf3090Sc007GetAgencyInfoExpParameter param = new Skf3090Sc007GetAgencyInfoExpParameter();

		boolean isCheck = true;

		/** 必須入力チェック */
		// 会社
		if (agencySearchAsyncDto.getRegistCompanyCd() == null
				|| CheckUtils.isEmpty(agencySearchAsyncDto.getRegistCompanyCd().trim())) {
			ServiceHelper.addErrorResultMessage(agencySearchAsyncDto, new String[] { "registCompanyCd" },
					MessageIdConstant.E_SKF_1048, "会社");
			isCheck = false;
		}

		// 機関コード
		if (agencySearchAsyncDto.getRegistAgencyCd() == null
				|| CheckUtils.isEmpty(agencySearchAsyncDto.getRegistAgencyCd().trim())) {
			ServiceHelper.addErrorResultMessage(agencySearchAsyncDto, new String[] { "registAgencyCd" },
					MessageIdConstant.E_SKF_1048, "機関コード");
			isCheck = false;
		}

		/** 必須チェックOKだったら桁数チェック */
		if (isCheck) {
			// 機関コード
			if (CheckUtils.isMoreThanByteSize(agencySearchAsyncDto.getRegistAgencyCd().trim(), 4)) {
				ServiceHelper.addErrorResultMessage(agencySearchAsyncDto, new String[] { "registAgencyCd" },
						MessageIdConstant.E_SKF_1071, "機関コード", "4");
				isCheck = false;
			}
		}

		/** 必須チェック、桁数チェックOKだったら形式チェック */
		if (isCheck) {
			if (!(CheckUtils.isAlphabetNumeric(agencySearchAsyncDto.getRegistAgencyCd().trim()))) {
				ServiceHelper.addErrorResultMessage(agencySearchAsyncDto, new String[] { "registAgencyCd" },
						MessageIdConstant.E_SKF_1052, "機関コード");
				isCheck = false;
			}
		}

		// 入力チェック、桁数チェック、形式チェックが全て通ったら機関名称を取得する
		if (isCheck) {
			param.setCompanyCd(agencySearchAsyncDto.getRegistCompanyCd());
			param.setAgencyCd(agencySearchAsyncDto.getRegistAgencyCd());
			resultList = skf3090Sc007GetAgencyInfoExpRepository.getAgencyInfo(param);
		} else {
			// 入力チェック、桁数チェック、形式チェックのどれかが引っかかったら、エラーメッセージを表示する
			throwBusinessExceptionIfErrors(agencySearchAsyncDto.getResultMessages());
		}
		// 対象の機関コードが存在する場合
		if (resultList != null && resultList.size() > 0) {
			if (resultList.get(0).getAgencyName() != null) {
				agencySearchAsyncDto.setRegistAgencyCd(resultList.get(0).getAgencyCd());
				agencySearchAsyncDto.setRegistAgencyName(resultList.get(0).getAgencyName());
			}
		} else {
			// 対象の機関コードが存在しない場合nullを返す
			agencySearchAsyncDto.setRegistAgencyCd(null);
			agencySearchAsyncDto.setRegistAgencyName(null);
		}

		return agencySearchAsyncDto;
	}

}
