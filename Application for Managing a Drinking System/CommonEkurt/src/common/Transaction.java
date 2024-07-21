package common;
import logic.*;
import java.io.Serializable;
/**
 * 
 * Class representing a transaction, containing information about an action,
 * response, and data.
 * 
 * @author Ran Polac, Roei Nizri, Alex, Ilanit, Noy Biton, Alin
 */
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	private Action action;
	private Response response;
	private Object data;
	
	public Transaction() {
		
	}
	/**
	 * 
	 * Constructor for the Transaction class, initializing with values for action,
	 * response, and data.
	 * 
	 * @param mission     the action associated with the transaction
	 * 
	 * @param response    the response associated with the transaction
	 * 
	 * @param information the data associated with the transaction
	 */
	public Transaction(Action mission, Response response, Object information) {

		this.action = mission;
		this.response = response;
		this.data = information;
	}
	/**
	 * 
	 * Constructor for the Transaction class, initializing with values for action
	 * and data, and setting the response to WAIT_RESPONSE.
	 * 
	 * @param action the action associated with the transaction
	 * @param data   the data associated with the transaction
	 */
	public Transaction(Action action, Object data) {
		this.action = action;
		this.response = Response.WAIT_RESPONSE;
		this.data = data;
	}
	/**
	 * 
	 * Returns the action associated with the transaction.
	 * 
	 * @return the action associated with the transaction
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * 
	 * Sets the action associated with the transaction.
	 * 
	 * @param mission the action to be associated with the transaction
	 */
	public void setAction(Action mission) {
		this.action = mission;
	}
	/**
	 * 
	 * Returns the response associated with the transaction.
	 * 
	 * @return the response associated with the transaction
	 */
	public Response getResponse() {
		return response;
	}
	/**
	 * 
	 * Sets the response associated with the transaction.
	 * 
	 * @param response the response to be associated with the transaction
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
	/**
	 * 
	 * Returns the data associated with the transaction.
	 * 
	 * @return the data associated with the transaction
	 */
	public Object getData() {
		return data;
	}
	/**
	 * 
	 * Sets the data associated with the transaction.
	 * 
	 * @param information the data to be associated with the transaction
	 */
	public void setData(Object information) {
		this.data = information;
	}

	@Override
	public String toString() {
		return "Client action : " + getAction() + "  " + "The action status : " + getResponse();
	}
}
