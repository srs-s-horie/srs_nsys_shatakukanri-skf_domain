/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.common;

import javax.ejb.DuplicateKeyException;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.message.ResultMessage;
import org.terasoluna.gfw.common.message.ResultMessages;
import jp.co.c_nexco.nfw.common.bean.NfwResultMessages;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.common.utils.LoginUserInfoUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.BaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.SkfServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.CodeConstant.GyomuErrMsgID;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.intra_mart.foundation.security.exception.AccessSecurityException;
import jp.co.intra_mart.foundation.security.message.MessageManager;

/**
 * エラーログ出力用サービス
 * 
 * @author NEXCOシステムズ
 */
public abstract class SkfServiceAbstract<DTO extends BaseDto> extends BaseServiceAbstract<DTO>{
	
	@Override
	@Transactional(rollbackFor = { Exception.class, PessimisticLockingFailureException.class,
			OptimisticLockingFailureException.class, DuplicateKeyException.class })
	public BaseDto executeService(DTO inDto) throws Exception {
		BaseDto outputDto = null;
		try {
			outputDto = super.executeService(inDto);
		} catch (Exception e) {
			String errMsg = getGyomuErrMsg(inDto.getResultMessages());
			if( !CodeConstant.NONE.equals(errMsg) ){
				//業務エラーメッセージが設定されていた場合、skf_logger.logにエラーログを出力する
				outputErrLogMsg(e, inDto, errMsg);
			}
			throw e;
		}
		return outputDto;
	}
	
	/**
	 * 例外発生時に呼び出されるメッセージ出力処理。
	 * 画面固有のメッセージ出力を行いたい場合は本処理をオーバーライドしてください。
	 * 
	 * @param e 発生例外
	 * @param inDto DTO
	 * @param errText ログに出力するinfoテキスト
	 */
	private void outputErrLogMsg(Exception e, DTO inDto, String errText){
		String userCd = LoginUserInfoUtils.getUserCd();
		String dtoName = inDto.getClass().getName();
		
		LogUtils.infoByMsg("発生Exception:" + e.getMessage());
		LogUtils.infoByMsg("ユーザID:"+ userCd + "\tDTO：" + dtoName +"\t発生Exception:" + e.toString()
				+"\tエラーメッセージ:" + errText);
	}
	
	/**
	 * dtoに設定されたresultMessagesに、業務エラーとすべきものが含まれているかを調べ、
	 * 業務エラーが含まれていた場合はエラーメッセージ文字列を返す。
	 * 
	 * @param resultMessages
	 * @return 業務エラーを含む場合エラーメッセージ文字列、含まない場合は空文字("")を返す。
	 * @throws AccessSecurityException 
	 * @see MessageIdConstant.GyomuErrMsgID
	 */
	private String getGyomuErrMsg(NfwResultMessages resultMessages) throws AccessSecurityException{
		
		for( ResultMessage msg : resultMessages.getList() ){
			//発生しているエラーメッセージを一つづつ取得
			for( GyomuErrMsgID gyomuErrIDName : GyomuErrMsgID.values() ){
				if( gyomuErrIDName.isEqualsErrMsg(msg.getCode())){
					//業務エラーとするべきエラーメッセージが含まれていた場合エラーメッセージ文字列を返す
					return MessageManager.getInstance().getMessage(msg.getCode(), msg.getArgs().toString());
				}
			}
		}
		return CodeConstant.NONE;
	}
}
