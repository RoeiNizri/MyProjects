package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.Response;
import common.Transaction;
import enums.RegionEnum;
import logic.Offer;

public class OffersQuaries {

	/**
	 * 
	 * The getOffers method is used to retrieve offers from the database. It takes
	 * in a Transaction object as an argument and retrieves the region from the
	 * transaction data. The method then queries the database for offers in the
	 * specified region, and creates Offer objects from the query results. If the
	 * query returns a result, the method sets the transaction data to be a List of
	 * Offer objects and sets the response to indicate successful retrieval of
	 * offers. If the query does not return a result, the method sets the response
	 * to indicate failure to retrieve offers. The PromoteOffer method is used to
	 * promote an offer in the database. It takes in a Transaction object as an
	 * argument and retrieves the Offer object from the transaction data. The method
	 * then updates the database to set is_in_sale of the product in the machines to
	 * 1 and isActive of the offers to 1. If the update is successful, the method
	 * sets the response to indicate successful promotion of the offer. If the
	 * update is not successful, the method sets the response to indicate failure to
	 * promote the offer.
	 * 
	 * @param msg - A Transaction object containing the region or Offer object
	 */
	public static void getOffers(Transaction msg) {
		List<String> Alist = new ArrayList<>();
		if (msg instanceof Transaction) {
			Object obj = msg.getData();
			RegionEnum region = RegionEnum.valueOf(obj.toString());
			try {
				ResultSet rs = dbController.getInstance()
						.executeQuery("SELECT pro_code, name, price, discount,isActive FROM ekurt.offers WHERE region='" + region.toString() + "'");
				System.out.println("Number of rows: " + rs.getRow());
				if(!(rs.next())) {
					msg.setResponse(Response.FAILED_TO_GET_OFFERS);
					return;
				}
				if (rs == null)
					msg.setResponse(Response.FAILED_TO_GET_OFFERS);
				else {
					    rs.previous();
						List<Offer> offers = new ArrayList<>();
						while (rs.next()) {
						  String id = rs.getString("pro_code");
						  String name = rs.getString("name");
						  String price = rs.getString("price");
						  String discount = rs.getString("discount");
						  int isActive = rs.getInt("isActive");
						  if(isActive==1) {
							  Offer offer = new Offer(id, name, price, discount,"ON");
							  offers.add(offer);
							  }
						  else {
						  Offer offer = new Offer(id, name, price, discount,"OFF");
						  offers.add(offer);
						  }
						}
				msg.setResponse(Response.FOUND_OFFERS);
				msg.setData(offers);
				rs.close();}
				}catch (SQLException e) {
				e.printStackTrace();
				msg.setResponse(Response.FAILED_TO_GET_OFFERS);
				return;
			}
		} else
			msg.setResponse(Response.FAILED_TO_GET_OFFERS);
	}
	
	/**
	 * 
	 * The PromoteOffer method is used to promote an offer in the database. It takes
	 * in a Transaction object as an argument and retrieves the Offer object from
	 * the transaction data. The method then updates the database to set is_in_sale
	 * of the product in the machines and product in warehouse and isActive of the
	 * offers to 1. If the update is successful, the method sets the response to
	 * indicate successful promotion of the offer. If the update is not successful,
	 * the method sets the response to indicate failure to promote the offer. The
	 * StopOffer method is used to stop an offer from being promoted in the
	 * database. It takes in a Transaction object as an argument and retrieves the
	 * Offer object from the transaction data. The method then updates the database
	 * to set is_in_sale of the product in the machines and product in warehouse and
	 * isActive of the offers to 0. If the update is successful, the method sets the
	 * response to indicate successful stopping of the offer. If the update is not
	 * successful, the method sets the response to indicate failure to stop the
	 * offer.
	 * 
	 * @param msg - A Transaction object containing the Offer object
	 */
	public static void PromoteOffer(Transaction msg) {
		if (msg instanceof Transaction) {
			Offer offer = (Offer) msg.getData();
			RegionEnum region = RegionEnum.valueOf(offer.getRegion().toString());
			try {
				int rowsAffected = dbController.getInstance().executeUpdate(
						"UPDATE productinmachine SET is_in_sale=1 WHERE machine_code IN (SELECT machine_code FROM"
						+ " machines WHERE region='"+region.toString()+"')"
								+ " AND pro_code='"+offer.getProductID()+"'");
				rowsAffected = dbController.getInstance().executeUpdate(
		                "UPDATE offers SET isActive = 1 WHERE "
		                + "pro_code='"+offer.getProductID()+"' AND region ='"+region.toString()+"'");
				rowsAffected = dbController.getInstance().executeUpdate(
						"UPDATE productinwarehouse SET is_in_sale=1 WHERE region='"+region+"')"
								+ "AND pro_code='"+offer.getProductID()+"'");
				msg.setResponse(Response.OFFER_PROMOTED_SUCCESSFULLY);
			}
			catch (Exception e){
				e.printStackTrace();
				msg.setResponse(Response.OFFER_PROMOTED_UNSUCCESSFULLY);
			}
		  }
		}
	/**
	 * 
	 * The StopOffer method is used to stop an offer from being promoted in the
	 * database. It takes in a Transaction object as an argument and retrieves the
	 * Offer object from the transaction data. The method then updates the database
	 * to set is_in_sale of the product in the machines and product in warehouse and
	 * isActive of the offers to 0. If the update is successful, the method sets the
	 * response to indicate successful stopping of the offer. If the update is not
	 * successful, the method sets the response to indicate failure to stop the
	 * offer.
	 * 
	 * @param msg - A Transaction object containing the Offer object
	 */
	public static void StopOffer(Transaction msg) {
		if (msg instanceof Transaction) {
			Offer offer = (Offer) msg.getData();
			RegionEnum region = RegionEnum.valueOf(offer.getRegion().toString());
			try {
				int rowsAffected = dbController.getInstance().executeUpdate(
						"UPDATE productinmachine SET is_in_sale=0 WHERE machine_code IN (SELECT machine_code FROM"
						+ " machines WHERE region='"+region.toString()+"')"
								+ " AND pro_code='"+offer.getProductID()+"'");
				rowsAffected = dbController.getInstance().executeUpdate(
		                "UPDATE offers SET isActive = 0 WHERE "
		                + "pro_code='"+offer.getProductID()+"' AND region ='"+region.toString()+"'");
				rowsAffected = dbController.getInstance().executeUpdate(
						"UPDATE productinwarehouse SET is_in_sale=0 WHERE region='"+region+"')"
								+ "AND pro_code='"+offer.getProductID()+"'");
				msg.setResponse(Response.OFFER_STOPPED_SUCCESSFULLY);
			}
			catch (Exception e){
				e.printStackTrace();
				msg.setResponse(Response.OFFER_PROMOTED_UNSUCCESSFULLY);
			}
		  }
		}
	
}