package org.D7noun.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.D7noun.model.Payment;
import org.primefaces.event.SelectEvent;

@ManagedBean
@ViewScoped
public class NotificationController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private NotificationFacade notificationFacade;

	private List<Payment> payments = new ArrayList<Payment>();

	private Date date;

	@PostConstruct
	public void init() {
		try {
			date = returnDateAfter2Days();
			payments = notificationFacade.findPaymentsByDate(date);
		} catch (Exception e) {
			System.err.println("D7noun: init");
			e.printStackTrace();
		}
	}

	private Date returnDateAfter2Days() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 2);
		return calendar.getTime();
	}

	public void changeDate(SelectEvent event) {
		payments = notificationFacade.findPaymentsByDate(date);
	}

	/**
	 * 
	 * D7noun: GETTERS&SETTERS
	 * 
	 */

	/**
	 * @return the notificationFacade
	 */
	public NotificationFacade getNotificationFacade() {
		return notificationFacade;
	}

	/**
	 * @param notificationFacade
	 *            the notificationFacade to set
	 */
	public void setNotificationFacade(NotificationFacade notificationFacade) {
		this.notificationFacade = notificationFacade;
	}

	/**
	 * @return the payments
	 */
	public List<Payment> getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
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

}
