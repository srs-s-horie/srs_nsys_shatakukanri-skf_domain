/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf3022.domain.service.skf3022sc001;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.c_nexco.businesscommon.entity.skf.exp.Skf3022Sc001.Skf3022Sc001GetShatakuRoomExp;
import jp.co.c_nexco.nfw.common.utils.LogUtils;
import jp.co.c_nexco.nfw.webcore.domain.service.BaseServiceAbstract;
import jp.co.c_nexco.skf.common.constants.CodeConstant;
import jp.co.c_nexco.skf.common.constants.MessageIdConstant;
import jp.co.c_nexco.skf.common.util.SkfOperationLogUtils;
import jp.co.c_nexco.skf.skf3022.domain.dto.skf3022sc001.Skf3022Sc001InitDto;

/**
 * Skf3022Sc001InitService 社宅部屋入力支援画面のInitサービス処理クラス。
 * 
 * @author NEXCOシステムズ
 */
@Service
public class Skf3022Sc001InitService extends BaseServiceAbstract<Skf3022Sc001InitDto> {

	@Autowired
	private Skf3022Sc001SharedService skf3022Sc001SharedService;
	@Autowired
	private SkfOperationLogUtils skfOperationLogUtils;

	@Value("${skf3022.skf3022_sc001.max_search_count}")
	private String maxCount;
	/**
	 * サービス処理を行う。
	 * 
	 * @param initDto
	 *            インプットDTO
	 * @return 処理結果
	 * @throws Exception
	 *             例外
	 */
	@Override
	public Skf3022Sc001InitDto index(Skf3022Sc001InitDto initDto) throws Exception {

		initDto.setPageTitleKey(MessageIdConstant.SKF3022_SC001_TITLE);

		// デバッグログ
		LogUtils.debugByMsg("初期表示");
		// 操作ログを出力する
		skfOperationLogUtils.setAccessLog("初期表示", CodeConstant.C001, initDto.getPageId());

		// 戻り値に設定するドロップダウンリストのインスタンスを生成
		List<Map<String, Object>> sc001EmptyRoomSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc001EmptyParkingSelectList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc001KikakuSelecteList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> sc001YoutoSelectList = new ArrayList<Map<String, Object>>();

		init(initDto);
		// 空き部屋を「あり」に設定
		initDto.setSc001EmptyRoomSelect("1");
		// ドロップダウン作成
		skf3022Sc001SharedService.getDoropDownList(initDto.getSc001EmptyRoomSelect(), sc001EmptyRoomSelectList,
				"", sc001EmptyParkingSelectList, "", sc001KikakuSelecteList, "", sc001YoutoSelectList);

		// 初期表示では検索結果は0件表示
		List<Skf3022Sc001GetShatakuRoomExp> roomList = new ArrayList<Skf3022Sc001GetShatakuRoomExp>();
		initDto.setListTableList(skf3022Sc001SharedService.createListTable(roomList));
		// 戻り値をセット
		initDto.setSc001EmptyParkingSelectList(sc001EmptyParkingSelectList);
		initDto.setSc001EmptyRoomSelectList(sc001EmptyRoomSelectList);
		initDto.setSc001KikakuSelecteList(sc001KikakuSelecteList);
		initDto.setSc001YoutoSelectList(sc001YoutoSelectList);

		return initDto;
	}

	/**
	 * 初期化処理
	 * 
	 * 「※」項目はアドレスとして戻り値になる。 
	 * 
	 * @param initDto	*DTO
	 */
	private void init(Skf3022Sc001InitDto initDto) {

		initDto.setListTableList(null);
		initDto.setShatakuName("");
		initDto.setRoomNo("");
		initDto.setSc001EmptyParkingSelect("");
		initDto.setSc001EmptyParkingSelectList(null);
		initDto.setSc001EmptyRoomSelect("");
		initDto.setSc001EmptyRoomSelectList(null);
		initDto.setSc001KikakuSelecte("");
		initDto.setSc001KikakuSelecteList(null);
		initDto.setSc001YoutoSelect("");
		initDto.setSc001YoutoSelectList(null);
		initDto.setMaxCount(maxCount);
	}
}
