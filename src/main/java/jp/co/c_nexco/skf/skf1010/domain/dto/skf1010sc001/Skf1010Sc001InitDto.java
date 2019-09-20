/*
 * Copyright(c) 2020 NEXCO Systems company limited All rights reserved.
 */
package jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001;

import jp.co.c_nexco.skf.skf1010.domain.dto.skf1010sc001common.Skf1010Sc001CommonDto;
import lombok.EqualsAndHashCode;

/**
 * Skf1010_Sc001社宅管理TOP画面のInitDto。
 * 
 */
@lombok.Data
@EqualsAndHashCode(callSuper = true)
public class Skf1010Sc001InitDto extends Skf1010Sc001CommonDto {

	private static final long serialVersionUID = -1902278406295003652L;

	// 会社コード
	private String companyCd;
	// 社員番号
	private String shainNo;
	// ロールID
	private String roleId;
	// ユーザ名
	private String userName;
	// 画面表示(申請)
	private String level1;
	// 画面表示(窓口)
	private String level2;
	// 画面表示（窓口 申請書類を承認する）
	private String level2_1;
	// 画面表示（社宅を管理する～社員情報を一括更新する）
	private String level2_2;
	// 画面表示（月締め処理を行う）
	private String level2_3;
	// 画面表示（レンタル備品指示書を作成する、備品搬入・搬出確認リストを作成する）
	private String level2_4;
	// 画面表示（法定調書データを管理する）
	private String level2_5;
	// 画面表示（年齢による使用料の変更通知）
	private String level2_6;
	// 画面表示(管理者）
	private String level3;
	// 画面表示（操作に困ったときは）
	private String level4;
	// 画面表示（操作に困ったときは 管理者）
	private String level4_1;
	// 画面表示（未処理情報）
	private String level5;
	// システムに関するお知らせ
	private String note;
	// 入退居区分
	private String nyutaikyoKbn;
	// 入退居申請状況区分
	private String nyutaikyoApplStatusKbn;
	private String nyutaikyoCount1;
	private String nyutaikyoCount2;

	// 社宅入居希望等調書の申請の件数（本社）
	private int nyukyoApplicationHonsya;
	// 社宅入居希望等調書の申請の件数（東京支社）
	private int nyukyoApplicationTokyo;
	// 社宅入居希望等調書の申請の件数（八王子支社）
	private int nyukyoApplicationHatiouzi;
	// 社宅入居希望等調書の申請の件数（名古屋支社）
	private int nyukyoApplicationNagoya;
	// 社宅入居希望等調書の申請の件数（金沢支社）
	private int nyukyoApplicationKanazawa;
	// 入居希望者への社宅の提示が未完了の件数（本社）
	private int nyukyoUnfinishedTipsHonsya;
	// 入居希望者への社宅の提示が未完了の件数（東京支社）
	private int nyukyoUnfinishedTipsTokyo;
	// 入居希望者への社宅の提示が未完了の件数（八王子支社）
	private int nyukyoUnfinishedTipsHatiouzi;
	// 入居希望者への社宅の提示が未完了の件数（名古屋支社）
	private int nyukyoUnfinishedTipsNagoya;
	// 入居希望者への社宅の提示が未完了の件数（金沢支社）
	private int nyukyoUnfinishedTipsKanazawa;
	// 提示社宅の本人確認が未完了の件数（本社）
	private int nyukyoUnfinishedCheckHonsya;
	// 提示社宅の本人確認が未完了の件数（東京支社）
	private int nyukyoUnfinishedCheckTokyo;
	// 提示社宅の本人確認が未完了の件数（八王子支社）
	private int nyukyoUnfinishedCheckHatiouzi;
	// 提示社宅の本人確認が未完了の件数（名古屋支社）
	private int nyukyoUnfinishedCheckNagoya;
	// 提示社宅の本人確認が未完了の件数（金沢支社）
	private int nyukyoUnfinishedCheckKanazawa;
	// 入居希望者の同意済の件数（本社）
	private int nyukyoAgreedHonsya;
	// 入居希望者の同意済の件数（東京支社）
	private int nyukyoAgreedTokyo;
	// 入居希望者の同意済の件数（八王子支社）
	private int nyukyoAgreedHatiouzi;
	// 入居希望者の同意済の件数（名古屋支社）
	private int nyukyoAgreedNagoya;
	// 入居希望者の同意済の件数（金沢支社）
	private int nyukyoAgreedKanazawa;
	// 入居希望者の同意されなかった件数（本社）
	private int nyukyoDisagreedHonsya;
	// 入居希望者の同意されなかった件数（東京支社）
	private int nyukyoDisagreedTokyo;
	// 入居希望者の同意されなかった件数（八王子支社）
	private int nyukyoDisagreedHatiouzi;
	// 入居希望者の同意されなかった件数（名古屋支社）
	private int nyukyoDisagreedNagoya;
	// 入居希望者の同意されなかった件数（金沢支社）
	private int nyukyoDisagreedKanazawa;
	// 退居届の申請の件数（本社）
	private int taikyoApplicationHonsya;
	// 退居届の申請の件数（東京支社）
	private int taikyoApplicationTokyo;
	// 退居届の申請の件数（八王子支社）
	private int taikyoApplicationHatiouzi;
	// 退居届の申請の件数（名古屋支社）
	private int taikyoApplicationNagoya;
	// 退居届の申請の件数（金沢支社）
	private int taikyoApplicationKanazawa;
	// 備品希望の申請が無い入居情報の件数（本社）
	private int bihinNotApplicationHonsya;
	// 備品希望の申請が無い入居情報の件数（東京支社）
	private int bihinNotApplicationTokyo;
	// 備品希望の申請が無い入居情報の件数（八王子支社）
	private int bihinNotApplicationHatiouzi;
	// 備品希望の申請が無い入居情報の件数（名古屋支社）
	private int bihinNotApplicationNagoya;
	// 備品希望の申請が無い入居情報の件数（金沢支社）
	private int bihinNotApplicationKanazawa;
	// 備品希望の提示が未完了の件数（本社）
	private int bihinKiboUnfinishedTipsHonsya;
	// 備品希望の提示が未完了の件数（東京支社）
	private int bihinKiboUnfinishedTipsTokyo;
	// 備品希望の提示が未完了の件数（八王子支社）
	private int bihinKiboUnfinishedTipsHatiouzi;
	// 備品希望の提示が未完了の件数（名古屋支社）
	private int bihinKiboUnfinishedTipsNagoya;
	// 備品希望の提示が未完了の件数（金沢支社）
	private int bihinKiboUnfinishedTipsKanazawa;
	// 備品希望の搬入が未完了の件数（本社）
	private int bihinKiboUnfinishedEmplacementHonsya;
	// 備品希望の搬入が未完了の件数（東京支社）
	private int bihinKiboUnfinishedEmplacementTokyo;
	// 備品希望の搬入が未完了の件数（八王子支社）
	private int bihinKiboUnfinishedEmplacementHatiouzi;
	// 備品希望の搬入が未完了の件数（名古屋支社）
	private int bihinKiboUnfinishedEmplacementNagoya;
	// 備品希望の搬入が未完了の件数（金沢支社）
	private int bihinKiboUnfinishedEmplacementKanazawa;
	// 備品返却の提示が未完了の件数（本社）
	private int bihinHenkyakuUnfinishedTipsHonsya;
	// 備品返却の提示が未完了の件数（東京支社）
	private int bihinHenkyakuUnfinishedTipsTokyo;
	// 備品返却の提示が未完了の件数（八王子支社）
	private int bihinHenkyakuUnfinishedTipsHatiouzi;
	// 備品返却の提示が未完了の件数（名古屋支社）
	private int bihinHenkyakuUnfinishedTipsNagoya;
	// 備品返却の提示が未完了の件数（金沢支社）
	private int bihinHenkyakuUnfinishedTipsKanazawa;
	// 備品返却提示の本人確認が未完了の件数（本社）
	private int bihinHenkyakuUnfinishedCheckHonsya;
	// 備品返却提示の本人確認が未完了の件数（東京支社）
	private int bihinHenkyakuUnfinishedCheckTokyo;
	// 備品返却提示の本人確認が未完了の件数（八王子支社）
	private int bihinHenkyakuUnfinishedCheckHatiouzi;
	// 備品返却提示の本人確認が未完了の件数（名古屋支社）
	private int bihinHenkyakuUnfinishedCheckNagoya;
	// 備品返却提示の本人確認が未完了の件数（金沢支社）
	private int bihinHenkyakuUnfinishedCheckKanazawa;
	// 備品返却提示の同意済の件数（本社）
	private int bihinHenkyakuAgreedHonsya;
	// 備品返却提示の同意済の件数（東京支社）
	private int bihinHenkyakuAgreedTokyo;
	// 備品返却提示の同意済の件数（八王子支社）
	private int bihinHenkyakuAgreedHatiouzi;
	// 備品返却提示の同意済の件数（名古屋支社）
	private int bihinHenkyakuAgreedNagoya;
	// 備品返却提示の同意済の件数（金沢支社）
	private int bihinHenkyakuAgreedKanazawa;
	// 備品の搬出が未完了の件数（本社）
	private int bihinUnfinishedMovementHonsya;
	// 備品の搬出が未完了の件数（東京支社）
	private int bihinUnfinishedMovementTokyo;
	// 備品の搬出が未完了の件数（八王子支社）
	private int bihinUnfinishedMovementHatiouzi;
	// 備品の搬出が未完了の件数（名古屋支社）
	private int bihinUnfinishedMovementNagoya;
	// 備品の搬出が未完了の件数（金沢支社）
	private int bihinUnfinishedMovementKanazawa;
}
