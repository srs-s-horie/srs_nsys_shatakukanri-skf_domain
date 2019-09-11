package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc007;

import org.springframework.stereotype.Service;

@Service
public class Skf3010Sc007CommonSharedService {

	//社宅と一括契約
	public static final String CONTRACT_TYPE_1 = "1";
	//社宅と別契約
	public static final String CONTRACT_TYPE_2 = "2";
	
	//削除フラグ(削除)
	public static final String DELETE_FLAG_ON = "1";
	//削除フラグ(削除しない)
	public static final String DELETE_FLAG_OFF = "0";
	
	public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss.SSS";
}
