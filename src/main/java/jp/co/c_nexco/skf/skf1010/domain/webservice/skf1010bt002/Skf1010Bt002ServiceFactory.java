package jp.co.c_nexco.skf.skf1010.domain.webservice.skf1010bt002;

import jp.co.c_nexco.nfw.webservice.BaseWebServiceFactoryAbstract;
import jp.co.intra_mart.foundation.web_api_maker.annotation.ProvideFactory;
import jp.co.intra_mart.foundation.web_api_maker.annotation.ProvideService;
import jp.co.intra_mart.foundation.web_api_maker.annotation.WebAPIMaker;

@WebAPIMaker
public class Skf1010Bt002ServiceFactory extends BaseWebServiceFactoryAbstract {

	/**
	 * WebServiceファクトリを取得する。
	 * 
	 * @return WebServiceファクトリ
	 */
	@ProvideFactory
	public static Skf1010Bt002ServiceFactory getFactory() {
		return new Skf1010Bt002ServiceFactory();
	}
	
	/**
	 * WebServiceオブジェクトを取得する。
	 * 
	 * @return WebServiceオブジェクト
	 */
	@ProvideService
	public Skf1010Bt002Service getService() {
		//return new BatchMShainService();
		return new Skf1010Bt002Service(Skf1010Bt002Service.class);
	}
}
