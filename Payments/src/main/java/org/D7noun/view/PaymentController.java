package org.D7noun.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.D7noun.model.Payment;

@ManagedBean
@ViewScoped
public class PaymentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private PaymentFacade paymentFacade;

	public List<Payment> payments = new ArrayList<Payment>();

	private Payment addPayment;
	private Payment editPayment;
	private Payment deletePayment;

	@PostConstruct
	public void init() {
		try {
			payments = paymentFacade.getAll();
		} catch (Exception e) {
			System.err.println("D7noun: init");
			e.printStackTrace();
		}
	}

	public void openAddDialog() {
		addPayment = new Payment();
	}

	public void openEditDialog(Payment payment) {
		editPayment = payment;
	}

	public void add() {
		try {
			addPayment = paymentFacade.edit(addPayment);
			payments = paymentFacade.getAll();
		} catch (Exception e) {
			System.err.println("D7noun: add");
			e.printStackTrace();
		}
	}

	public void edit() {
		try {
			editPayment = paymentFacade.edit(editPayment);
			payments = paymentFacade.getAll();
		} catch (Exception e) {
			System.err.println("D7noun: edit");
			e.printStackTrace();
		}
	}

	public void selectForDelete(Payment payment) {
		deletePayment = payment;
	}

	public void delete() {
		try {
			paymentFacade.remove(deletePayment);
			payments = paymentFacade.getAll();
		} catch (Exception e) {
			System.err.println("D7noun: delete");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * D7noun: GETTERS&SETTERS
	 * 
	 */

	/**
	 * @return the paymentFacade
	 */
	public PaymentFacade getPaymentFacade() {
		return paymentFacade;
	}

	/**
	 * @param paymentFacade
	 *            the paymentFacade to set
	 */
	public void setPaymentFacade(PaymentFacade paymentFacade) {
		this.paymentFacade = paymentFacade;
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
	 * @return the addPayment
	 */
	public Payment getAddPayment() {
		return addPayment;
	}

	/**
	 * @param addPayment
	 *            the addPayment to set
	 */
	public void setAddPayment(Payment addPayment) {
		this.addPayment = addPayment;
	}

	/**
	 * @return the editPayment
	 */
	public Payment getEditPayment() {
		return editPayment;
	}

	/**
	 * @param editPayment
	 *            the editPayment to set
	 */
	public void setEditPayment(Payment editPayment) {
		this.editPayment = editPayment;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the deletePayment
	 */
	public Payment getDeletePayment() {
		return deletePayment;
	}

	/**
	 * @param deletePayment
	 *            the deletePayment to set
	 */
	public void setDeletePayment(Payment deletePayment) {
		this.deletePayment = deletePayment;
	}

}
