package logic;

public class OrderReport {
	private String machineID, machineName, orderDate, orderID, deliveryMethod, orderPrice;

	public String getMachineID() {
		return machineID;
	}

	public void setMachineID(String machineID) {
		this.machineID = machineID;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public OrderReport(String machineID, String machineName, String orderDate, String orderID, String deliveryMethod,
			String orderPrice) {
		super();
		this.machineID = machineID;
		this.machineName = machineName;
		this.orderDate = orderDate;
		this.orderID = orderID;
		this.deliveryMethod = deliveryMethod;
		this.orderPrice = orderPrice;
	}

	@Override
	public String toString() {
		return "OrderReport [machineID=" + machineID + ", machineName=" + machineName + ", orderDate=" + orderDate
				+ ", orderID=" + orderID + ", deliveryMethod=" + deliveryMethod + ", orderPrice=" + orderPrice + "]";
	}

}
