/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3010.domain.service.skf3010sc005;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import jp.co.c_nexco.nfw.common.utils.CheckUtils;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.model.AsyncBaseDto;
import jp.co.c_nexco.nfw.webcore.domain.service.AsyncBaseServiceAbstract;
import jp.co.c_nexco.skf.skf3010.domain.dto.skf3010sc005.Skf3010Sc005ChangeDropDownAsyncDto;

/**
 * Skf3010Sc001ChangeDropDownAsyncService 社宅一覧ドロップダウンリスト変更時非同期処理クラス
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3010Sc005ChangeDropDownAsyncService
		extends AsyncBaseServiceAbstract<Skf3010Sc005ChangeDropDownAsyncDto> {

	/**
	 * 物置調整面積表示データを生成する。
	 */
	@Override
	public AsyncBaseDto index(Skf3010Sc005ChangeDropDownAsyncDto asyncDto) throws Exception {

		//物置調整面積の計算CalcMonookiChoseiMenseki()
		//初期値0
		BigDecimal barnMensekiAdjust = BigDecimal.ZERO;
		//計算用3
		BigDecimal three = new BigDecimal("3.00");
		
        //本来用途を取得
		String originalAuse = asyncDto.getOriginalAuse();

        //物置面積を取得
		BigDecimal barnMenseki = BigDecimal.ZERO;
		String txtbarnMenseki = asyncDto.getBarnMenseki();
		if(txtbarnMenseki!= null && !txtbarnMenseki.isEmpty()) {
			if(CheckUtils.isNumberFormat(txtbarnMenseki)){
				barnMenseki = new BigDecimal(asyncDto.getBarnMenseki());
			}else{
				//面積が整数変換できない場合、再計算は行わない。
				return asyncDto;
			}		
		}

		if(originalAuse != null && !originalAuse.isEmpty()){
//          '本来用途が「世帯(1)」であり、かつ「物置面積」が３未満の場合
			if((originalAuse.compareTo("1") == 0) && (barnMenseki.intValue() < 3)){
				barnMensekiAdjust = three.subtract(barnMenseki);
			}

		}
		barnMensekiAdjust = barnMensekiAdjust.setScale(2);
		asyncDto.setBarnMensekiAdjust(barnMensekiAdjust.toPlainString());

		// Debugログで出力
		LogUtils.debugByMsg("物置調整面積再計算：" + asyncDto.getBarnMensekiAdjust());
		
		return asyncDto;
	}

}
