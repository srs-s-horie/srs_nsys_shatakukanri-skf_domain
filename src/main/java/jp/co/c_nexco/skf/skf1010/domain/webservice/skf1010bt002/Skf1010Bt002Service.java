package jp.co.c_nexco.skf.skf1010.domain.webservice.skf1010bt002;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.nfw.common.bean.SpringContext;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webservice.BaseWebServiceAbstract;
import jp.co.c_nexco.nfw.webservice.exception.NotFoundException;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Body;
import jp.co.intra_mart.foundation.web_api_maker.annotation.DELETE;
import jp.co.intra_mart.foundation.web_api_maker.annotation.GET;
import jp.co.intra_mart.foundation.web_api_maker.annotation.IMAuthentication;
import jp.co.intra_mart.foundation.web_api_maker.annotation.POST;
import jp.co.intra_mart.foundation.web_api_maker.annotation.PUT;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Path;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Required;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Response;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Variable;

/**
 * WebService用サービス。
 * 
 */
@IMAuthentication
public class Skf1010Bt002Service extends BaseWebServiceAbstract {

	private Skf1010Bt002SharedService mShainSharedService = null;

	public Skf1010Bt002Service() {
		super(Skf1010Bt002Service.class);
		mShainSharedService = (Skf1010Bt002SharedService)SpringContext.getBean("skf1010Bt002SharedService");
	}
	
	public Skf1010Bt002Service(Class<?> cls) {
		super(cls);
		mShainSharedService = (Skf1010Bt002SharedService)SpringContext.getBean("skf1010Bt002SharedService");
	}
	/**
	 * データを全部取得する。
	 * 
	 * @return 取得したデータ
	 * @throws NotFoundException
	 *             データ取得できない場合、発生する
	 */
	@Path("/api/skf/v1.0/skf1010bt002")
	@GET
	@Response(code = 200)
	public List<Skf1010Bt002> getAll() throws NotFoundException {
		List<Skf1010Bt002> shainList = new ArrayList<Skf1010Bt002>();
		try {
			shainList = mShainSharedService.getAll();
		} catch (Exception ex) {
			throw new NotFoundException();
		}
		return shainList;
	}

	/**
	 * データを削除する。
	 * 
	 * @param id 削除するデータのID
	 * @throws NotFoundException 例外
	 */
	@Path("/api/skf/skf3090_del/{company_cd}/{shain_no}")
	@DELETE
	@Response(code = 204)
	public void delete(@Required @Variable(name = "company_cd") final String companyCd, 
			@Required @Variable(name = "shain_no") final String shainNo) throws NotFoundException {
		try {
			mShainSharedService.delete(shainNo);
		} catch (Exception ex) {
			throw new NotFoundException();
		}
	}
	
	/**
	 * データを更新する。
	 * 
	 * @param pet 更新オブジェクト
	 * @return 更新データ数
	 * @throws NotFoundException 例外
	 */
	@Path("/api/skf/skf3090_upd")
	@POST
	@Response(code = 200)
	public int update(@Required @Body Skf1010Bt002 shainData) throws NotFoundException { 
		try {
			return mShainSharedService.update(shainData);
		} catch (Exception ex) {
			throw new NotFoundException();
		}
	}
	
	/**
	 * データを登録する
	 * @param shainData
	 * @return
	 * @throws NotFoundException
	 */
	@Path("/api/skf/skf3090_reg")
	@PUT
	@Response(code = 201)
	public int insert(@Required @Body Skf1010Bt002 shainData) throws NotFoundException { 
		try {
			return mShainSharedService.regist(shainData);
		} catch (Exception ex) {
			throw new NotFoundException();
		}
	}
	

}
