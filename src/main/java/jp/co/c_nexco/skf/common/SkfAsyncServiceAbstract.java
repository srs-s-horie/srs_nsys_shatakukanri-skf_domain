/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.common;

import javax.ejb.DuplicateKeyException;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;

import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.common.SkfAsyncServiceAbstract;

/**
 * エラーログ出力用サービス
 * 
 * @author NEXCOシステムズ
 */
public abstract class SkfAsyncServiceAbstract<DTO extends AsyncBaseDto> extends AsyncBaseServiceAbstract<DTO>{
	
	@Override
	@Transactional(rollbackFor = { Exception.class, PessimisticLockingFailureException.class,
			OptimisticLockingFailureException.class, DuplicateKeyException.class })
	public AsyncBaseDto executeService(DTO inDto) throws Exception {
		AsyncBaseDto outputDto = null;
		try {
			outputDto = super.executeService(inDto);
		} catch (Exception e) {
			String userCd = LoginUserInfoUtils.getUserCd();
			String dtoName = inDto.getClass().getName();
			LogUtils.errorByMsg("ユーザID:"+ userCd + "\tDTO：" + dtoName + "\tエラー内容:" + e.toString());
			throw e;
		}
		return outputDto;
	}
}
