package logic;

public class Subscriber {

	private String subFirstname, subLastname, subId, subPhone, subEmail, subCreditcard, subNumber;

	public Subscriber(String subFirstname, String subLastname, String subId, String subPhone, String subEmail,
			String subCreditcard, String subNumber) {
		this.subFirstname = subFirstname;
		this.subLastname = subLastname;
		this.subId = subId;
		this.subPhone = subPhone;
		this.subEmail = subEmail;
		this.subCreditcard = subCreditcard;
		this.subNumber = subNumber;
	}

	public String getSubFirstname() {
		return subFirstname;
	}

	public void setSubFirstname(String subFirstname) {
		this.subFirstname = subFirstname;
	}

	public String getSubLastname() {
		return subLastname;
	}

	public void setSubLastname(String subLastname) {
		this.subLastname = subLastname;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getSubPhone() {
		return subPhone;
	}

	public void setSubPhone(String subPhone) {
		this.subPhone = subPhone;
	}

	public String getSubEmail() {
		return subEmail;
	}

	public void setSubEmail(String subEmail) {
		this.subEmail = subEmail;
	}

	public String getSubCreditcard() {
		return subCreditcard;
	}

	public void setSubCreditcard(String subCreditcard) {
		this.subCreditcard = subCreditcard;
	}

	public String getSubNumber() {
		return subNumber;
	}

	public void setSubNumber(String subNumber) {
		this.subNumber = subNumber;
	}

	@Override
	public String toString() {
		return "Subscriber [subFirstname=" + subFirstname + ", subLastname=" + subLastname + ", subId=" + subId
				+ ", subPhone=" + subPhone + ", subEmail=" + subEmail + ", subCreditcard=" + subCreditcard
				+ ", subNumber=" + subNumber + "]";
	}

}
