package jp.co.c_nexco.skf.skf3090.domain.service.skf3090sc007;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc007.Skf3090Sc007GetAffiliation1InfoExp;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3090Sc007.Skf3090Sc007GetAffiliation1InfoExpParameter;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf3090Sc007.Skf3090Sc007GetAffiliation1InfoExpRepository;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.FunctionIdConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3090.domain.dto.skf3090sc007.Skf3090Sc007Affiliation1SearchAsyncDto;

/**
 * Skf3090Sc007Affiliation1SearchService 部等の名称を検索ボタン押下時の処理クラス。
 * 
 * @author NEXCOシステムズ
 *
 */
@Service
public class Skf3090Sc007Affiliation1SearchAsyncService
		extends SkfAsyncServiceAbstract<Skf3090Sc007Affiliation1SearchAsyncDto> {

	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Autowired
	private Skf3090Sc007GetAffiliation1InfoExpRepository skf3090Sc007GetAffiliation1InfoExpRepository;

	@Override
	public Skf3090Sc007Affiliation1SearchAsyncDto index(
			Skf3090Sc007Affiliation1SearchAsyncDto affiliation1SearchAsyncDto) throws Exception {
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("名称を検索", CodeConstant.C001, FunctionIdConstant.SKF3090_SC007);

		List<Skf3090Sc007GetAffiliation1InfoExp> resultList = new ArrayList<Skf3090Sc007GetAffiliation1InfoExp>();
		Skf3090Sc007GetAffiliation1InfoExpParameter param = new Skf3090Sc007GetAffiliation1InfoExpParameter();

		boolean isCheck = true;

		/** 必須入力チェック */
		// 会社
		if (affiliation1SearchAsyncDto.getRegistCompanyCd() == null
				|| CheckUtils.isEmpty(affiliation1SearchAsyncDto.getRegistCompanyCd().trim())) {
			ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registCompanyCd" },
					MessageIdConstant.E_SKF_1048, "会社");
			isCheck = false;
		}

		// 機関コード
		if (affiliation1SearchAsyncDto.getRegistAgencyCd() == null
				|| CheckUtils.isEmpty(affiliation1SearchAsyncDto.getRegistAgencyCd().trim())) {
			ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAgencyCd" },
					MessageIdConstant.E_SKF_1048, "機関コード");
			isCheck = false;
		}

		// 部等コード
		if (affiliation1SearchAsyncDto.getRegistAffiliation1Cd() == null
				|| CheckUtils.isEmpty(affiliation1SearchAsyncDto.getRegistAffiliation1Cd().trim())) {
			ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAffiliation1Cd" },
					MessageIdConstant.E_SKF_1048, "部等コード");
			isCheck = false;
		}

		/** 必須チェックOKだったら桁数チェック */
		if (isCheck) {
			// 機関コード
			if (CheckUtils.isMoreThanByteSize(affiliation1SearchAsyncDto.getRegistAgencyCd().trim(), 4)) {
				ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAgencyCd" },
						MessageIdConstant.E_SKF_1071, "機関コード", "4");
				isCheck = false;
			}

			// 部等コード
			if (CheckUtils.isMoreThanByteSize(affiliation1SearchAsyncDto.getRegistAffiliation1Cd().trim(), 3)) {
				ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAffiliation1Cd" },
						MessageIdConstant.E_SKF_1071, "部等コード", "3");
				isCheck = false;
			}
		}

		/** 必須チェック、桁数チェックOKだったら形式チェック */
		if (isCheck) {
			// 機関コード
			if (!(CheckUtils.isAlphabetNumeric(affiliation1SearchAsyncDto.getRegistAgencyCd().trim()))) {
				ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAgencyCd" },
						MessageIdConstant.E_SKF_1052, "機関コード");
				isCheck = false;
			}

			// 部等コード
			if (!(CheckUtils.isAlphabetNumeric(affiliation1SearchAsyncDto.getRegistAffiliation1Cd().trim()))) {
				ServiceHelper.addErrorResultMessage(affiliation1SearchAsyncDto, new String[] { "registAffiliation1Cd" },
						MessageIdConstant.E_SKF_1052, "部等コード");
				isCheck = false;
			}
		}

		// 必須入力チェック、桁数チェック、形式チェックが全て通ったら部等名称の情報を取得する
		if (isCheck) {
			param.setCompanyCd(affiliation1SearchAsyncDto.getRegistCompanyCd());
			param.setAgencyCd(affiliation1SearchAsyncDto.getRegistAgencyCd());
			param.setAffiliation1Cd(affiliation1SearchAsyncDto.getRegistAffiliation1Cd());
			resultList = skf3090Sc007GetAffiliation1InfoExpRepository.getAffiliation1Info(param);
		} else {
			// 必須入力チェック、桁数チェック、形式チェックのどれかでも引っかかったら、エラーメッセージを表示する
			throwBusinessExceptionIfErrors(affiliation1SearchAsyncDto.getResultMessages());
		}

		// 対象の部等コードが存在する場合
		if (resultList != null && resultList.size() > 0) {
			if (resultList.get(0).getAffiliation1Name() != null) {
				affiliation1SearchAsyncDto.setRegistAffiliation1Cd(resultList.get(0).getAffiliation1Cd());
				affiliation1SearchAsyncDto.setRegistAffiliation1Name(resultList.get(0).getAffiliation1Name());
			}
		} else {
			// 対象の部等が存在しない場合nullを返す
			affiliation1SearchAsyncDto.setRegistAffiliation1Cd(null);
			affiliation1SearchAsyncDto.setRegistAffiliation1Name(null);
		}

		return affiliation1SearchAsyncDto;
	}

}
