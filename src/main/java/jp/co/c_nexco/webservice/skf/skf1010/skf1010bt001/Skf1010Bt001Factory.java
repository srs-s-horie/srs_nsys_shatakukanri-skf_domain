package jp.co.c_nexco.webservice.skf.skf1010.skf1010bt001;

import jp.co.c_nexco.nfw.webservice.BaseWebServiceFactoryAbstract;
import jp.co.intra_mart.foundation.web_api_maker.annotation.ProvideFactory;
import jp.co.intra_mart.foundation.web_api_maker.annotation.ProvideService;
import jp.co.intra_mart.foundation.web_api_maker.annotation.WebAPIMaker;

/**
 * SKF1010_BT002_従業員取込のWebService用ファクトリー。
 * 
 * @author NEXCOシステムズ
 *
 */
@WebAPIMaker
public class Skf1010Bt001Factory extends BaseWebServiceFactoryAbstract {

	/**
	 * WebServiceファクトリを取得する。
	 * 
	 * @return WebServiceファクトリ
	 */
	@ProvideFactory
	public static Skf1010Bt001Factory getFactory() {
		return new Skf1010Bt001Factory();
	}

	/**
	 * WebServiceオブジェクトを取得する。
	 * 
	 * @return WebServiceオブジェクト
	 */
	@ProvideService
	public Skf1010Bt001Service getService() {
		return new Skf1010Bt001Service();
	}

}
