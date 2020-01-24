/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.dto.common;

import javax.xml.bind.annotation.XmlRootElement;
import jp.co.c_nexco.nfw.webservice.BaseWebServiceModelAbstract;

/**
 * Skfバッチ処理結果用モデル。
 * 
 * @author NEXCOシステムズ
 */
@XmlRootElement(name = "asfJobResponseCommonDto")
public class SkfPersonnalBatchCommonDto extends BaseWebServiceModelAbstract {

	// 終了状態
	private String status;

	// 終了メッセージ
	private String message;

	/**
	 * 終了状態を取得する。
	 * 
	 * @return 終了状態
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 終了状態を設定する。
	 * 
	 * @param status 終了状態
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 終了メッセージを取得する。
	 * 
	 * @return 終了メッセージ
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 終了メッセージを設定する。
	 * 
	 * @param message 終了メッセージ
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
