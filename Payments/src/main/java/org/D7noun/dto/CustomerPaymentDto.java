package org.D7noun.dto;

import java.util.Calendar;
import java.util.Date;

public class CustomerPaymentDto {

	private Long customerId;
	private String name;
	private String phoneNumber;
	private Long paymentId;
	private double price;
	private Date date;
	private boolean payed;
	private String notes;

	public CustomerPaymentDto() {
		super();
	}

	public CustomerPaymentDto(Long customerId, String name, String phoneNumber, Long paymentId, double price, Date date,
			String notes) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.paymentId = paymentId;
		this.price = price;
		this.date = date;
		this.notes = notes;
	}

	public CustomerPaymentDto(Long customerId, String name, String phoneNumber, Long paymentId, boolean payed,
			double price, Date date, String notes) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.paymentId = paymentId;
		this.price = price;
		this.date = date;
		this.payed = payed;
		this.notes = notes;
	}

	public CustomerPaymentDto(Long customerId, String name, String phoneNumber, Long paymentId, double price, Date date,
			boolean payed, String notes) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.paymentId = paymentId;
		this.price = price;
		this.date = date;
		this.payed = payed;
		this.notes = notes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the payed
	 */
	public boolean isPayed() {
		return payed;
	}

	/**
	 * @param payed
	 *            the payed to set
	 */
	public void setPayed(boolean payed) {
		this.payed = payed;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the customerId
	 */
	public long getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the paymentId
	 */
	public long getPaymentId() {
		return paymentId;
	}

	/**
	 * @param paymentId
	 *            the paymentId to set
	 */
	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * @param customerId
	 *            the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/**
	 * @param paymentId
	 *            the paymentId to set
	 */
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * 
	 * D7noun
	 * 
	 */
	public boolean threeDaysNotPayed() {
		if (!payed && betweenThreeAndOneDay()) {
			return true;
		}
		return false;
	}

	public boolean OneDayNotPayed() {
		if (!payed && beforeOneDay()) {
			return true;
		}
		return false;

	}

	private boolean betweenThreeAndOneDay() {
		Date today = new Date();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar1.setTime(date);
		calendar1.add(Calendar.DATE, -3);
		Date beforeThreeDays = calendar1.getTime();

		calendar2.setTime(date);
		calendar2.add(Calendar.DATE, -1);
		Date beforeOneDays = calendar2.getTime();

		if (date != null && today.after(beforeThreeDays) && today.before(beforeOneDays)) {
			return true;
		}
		return false;
	}

	private boolean beforeOneDay() {
		Date today = new Date();
		Calendar calendar1 = Calendar.getInstance();

		calendar1.setTime(date);
		calendar1.add(Calendar.DATE, -1);
		Date beforeOnedayDate = calendar1.getTime();

		if (date != null && today.after(beforeOnedayDate)) {
			return true;
		}
		return false;
	}

	public String rowStyleClass() {
		if (threeDaysNotPayed()) {
			return "bg-threedays";
		} else if (OneDayNotPayed()) {
			return "bg-onedays";
		} else {
			return "";
		}
	}

}
