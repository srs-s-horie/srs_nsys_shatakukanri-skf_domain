package jp.co.c_nexco.skf.skf1010.domain.webservice.skf1010bt002;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf1010Bt002.Skf1010MShainExp;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShain;
import jp.co.c_nexco.businesscommon.entity.skf.table.Skf1010MShainKey;
import jp.co.c_nexco.businesscommon.repository.skf.exp.Skf1010Bt002.Skf1010MShainExpRepository;
import jp.co.c_nexco.businesscommon.repository.skf.table.Skf1010MShainRepository;
import jp.co.c_nexco.nfw.common.utils.CopyUtils;

@Component
public class Skf1010Bt002SharedService {

	@Autowired
	private Skf1010MShainRepository skf1010MShainRepository;
	@Autowired
	private Skf1010MShainExpRepository skf1010MShainExpRepository;

	private String companyCd = "C001";

	/**
	 * データを削除する
	 * 
	 * @param key
	 */
	public void delete(String shainNo) {
		Skf1010MShainKey key = new Skf1010MShainKey();
		key.setCompanyCd(companyCd);
		key.setShainNo(shainNo);
		skf1010MShainRepository.deleteByPrimaryKey(key);
	}

	/**
	 * データを登録する
	 * 
	 * @param data
	 * @return
	 */
	public int regist(Skf1010Bt002 data) throws Exception {
		Skf1010MShain mShainEntity = new Skf1010MShain();
		CopyUtils.copyProperties(mShainEntity, data);
		return skf1010MShainRepository.insertSelective(mShainEntity);
	}

	/**
	 * データを更新する
	 * 
	 * @param data
	 * @return
	 */
	public int update(Skf1010Bt002 data) throws Exception {
		Skf1010MShain mShainEntity = new Skf1010MShain();
		CopyUtils.copyProperties(mShainEntity, data);
		return skf1010MShainRepository.updateByPrimaryKeySelective(mShainEntity);
	}

	/**
	 * データを全件取得する
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Skf1010Bt002> getAll() throws Exception {
		List<Skf1010MShainExp> list = skf1010MShainExpRepository.selectAllData();
		List<Skf1010Bt002> resList = new ArrayList<Skf1010Bt002>();
		if (list == null || list.size() <= 0) {
			return resList;
		}
		for (Skf1010MShainExp data : list) {
			Skf1010Bt002 resData = new Skf1010Bt002();
			CopyUtils.copyProperties(resData, data);
			resList.add(resData);
		}
		return resList;
	}
}
