package jp.co.c_nexco.skf.skf2010.domain.service.skf2010sc008;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.businesscommon.entity.skf.exp.SkfGetInfoUtils.SkfGetInfoUtilsGetShainInfoExp;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.nfw.webcore.domain.service.ServiceHelper;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf2010.domain.dto.skf2010sc008.Skf2010Sc008SelectDto;

/**
 * Skf2010Sc008 代行ログイン画面対象社員選択処理クラス
 *
 * @author NEXCOシステムズ
 */
@Service
public class Skf2010Sc008SelectService extends BaseServiceAbstract<Skf2010Sc008SelectDto> {

	/**
	 * 代行ログイン画面 共通サービス
	 */
	@Autowired
	private Skf2010Sc008SharedService skf2010Sc008SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;
	
	// 会社コード
	private String companyCd = CodeConstant.C001;

	/**
	 * 代行ログイン画面 対象社員選択処理を行う。
	 * 
	 * @param selectDto インプットDTO
	 * @return 処理結果
	 * @throws Exception 例外
	 */
	@Override
	public Skf2010Sc008SelectDto index(Skf2010Sc008SelectDto selectDto) throws Exception {

		selectDto.setPageTitleKey(MessageIdConstant.SKF2010_SC008_TITLE);
		
		// 操作ログを出力
		skfOperationLogUtils.setAccessLog("選択する", companyCd, selectDto.getPageId());

		// 入力チェックを行う。社員番号の未入力チェック、半角数値チェック
		// 未入力チェック
		if (selectDto.getShainNo() == null || CheckUtils.isEmpty(selectDto.getShainNo().trim())) {
			ServiceHelper.addErrorResultMessage(selectDto, new String[] { "shainNo" }, MessageIdConstant.E_SKF_1048,
					"社員番号");
		// 文字種チェック
		} else if (!Pattern.matches("^[0-9]*$", selectDto.getShainNo())) {
			ServiceHelper.addErrorResultMessage(selectDto, new String[] { "shainNo" }, MessageIdConstant.E_SKF_1050,
					"社員番号");
		} else {

			// 対象社員情報を検索
			SkfGetInfoUtilsGetShainInfoExp shainInfoEntity = skf2010Sc008SharedService
					.getShainInfo(selectDto.getShainNo());

			if (shainInfoEntity != null) {
				// 対象の社員が存在する場合
				// Dtoに対象社員情報を格納
				setDto(selectDto, shainInfoEntity);
			} else {
				// 対象の社員が存在しない場合
				// 関連チェックエラーとする
				ServiceHelper.addErrorResultMessage(selectDto, new String[] { "shainNo" }, MessageIdConstant.E_SKF_1067,
						"該当する社員情報");
			}
		}

		return selectDto;
	}

	/**
	 * DtoにDBから取得した社員情報を設定する。
	 * 
	 * @param selectDto 代行ログイン画面選択Dto
	 * @param shainInfoEntity 社員情報を保持したエンティティ
	 */
	private void setDto(Skf2010Sc008SelectDto selectDto, SkfGetInfoUtilsGetShainInfoExp shainInfoEntity) {
		selectDto.setShainNo(shainInfoEntity.getShainNo());
		selectDto.setShainName(shainInfoEntity.getName());
		selectDto.setAgency(shainInfoEntity.getAgencyCd());
		selectDto.setAgencyName(shainInfoEntity.getAgencyName());
		selectDto.setAffiliation1(shainInfoEntity.getAffiliation1Cd());
		selectDto.setAffiliation1Name(shainInfoEntity.getAffiliation1Name());
		selectDto.setAffiliation2(shainInfoEntity.getAffiliation2Cd());
		selectDto.setAffiliation2Name(shainInfoEntity.getAffiliation2Name());
	}

}
