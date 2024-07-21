package logic;

import java.io.Serializable;
import java.sql.Timestamp;

public class PopUpMessage implements Serializable {
	private String email;
    private String telephone;
    private String OrderId;
    private Timestamp SuppDate;
    
    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public Timestamp getSuppDate() {
		return SuppDate;
	}
	public void setSuppDate(Timestamp suppDate) {
		SuppDate = suppDate;
	}
    }
