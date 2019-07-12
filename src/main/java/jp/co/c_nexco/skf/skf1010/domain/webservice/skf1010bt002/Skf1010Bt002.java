package jp.co.c_nexco.skf.skf1010.domain.webservice.skf1010bt002;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import jp.co.intra_mart.foundation.web_api_maker.annotation.Required;
import jp.co.intra_mart.foundation.web_api_maker.annotation.Variable;

@XmlRootElement(name = "MShain")
public class Skf1010Bt002 {
	
	//@XmlElement(name = "companyCd")
	private String companyCd;
	
	//@XmlElement(name = "shainNo")
	private String shainNo;
	
	//@XmlElement(name = "name")
	private String name;
	
	//@XmlElement(name = "nameKk")
	private String nameKk;
	
	//@XmlElement(name = "originalCompanyCd")
	private String originalCompanyCd;
	
	//@XmlElement(name = "agencyCd")
	private String agencyCd;
	
	//@XmlElement(name = "affiliation1Cd")
	private String affiliation1Cd;
	
	//@XmlElement(name = "affiliation2Cd")
	private String affiliation2Cd;
	
	//@XmlElement(name = "roleId")
	private String roleId;
	
	public String getCompanyCd() {
		return this.companyCd;
	}
	@Required
	@Variable(name="company_cd")
	public void setCompanyCd(String companyCd) {
		this.companyCd = companyCd;
	}

	public String getShainNo() {
		return this.shainNo;
	}
	@Required
	@Variable(name="shain_no")
	public void setShainNo(String shainNo) {
		this.shainNo = shainNo;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getNameKk() {
		return this.nameKk;
	}
	public void setNameKk(String nameKk) {
		this.nameKk = nameKk;
	}
	
	public String getOriginalCompanyCd() {
		return this.originalCompanyCd;
	}
	public void setOriginalCompanyCd(String originalCompanyCd) {
		this.originalCompanyCd = originalCompanyCd;
	}
	
	public String getAgencyCd() {
		return this.agencyCd;
	}
	public void setAgencyCd(String agencyCd) {
		this.agencyCd = agencyCd;
	}
	
	public String getAffiliation1Cd() {
		return this.affiliation1Cd;
	}
	public void setAffiliation1Cd(String affiliation1Cd) {
		this.affiliation1Cd = affiliation1Cd;
	}
	
	public String getAffiliation2Cd() {
		return this.affiliation2Cd;
	}
	public void setAffiliation2Cd(String affiliation2Cd) {
		this.affiliation2Cd = affiliation2Cd;
	}
	
	public String getRoleId() {
		return this.roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}