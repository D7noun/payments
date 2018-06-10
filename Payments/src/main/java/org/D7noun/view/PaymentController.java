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

import org.D7noun.dto.CustomerPaymentDto;
import org.D7noun.model.Customer;
import org.D7noun.model.Payment;
import org.omnifaces.util.Ajax;

@ManagedBean
@ViewScoped
public class PaymentController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private CustomerFacade customerFacade;
	@EJB
	private PaymentFacade paymentFacade;

	public List<CustomerPaymentDto> payments = new ArrayList<CustomerPaymentDto>();

	private CustomerPaymentDto addCustomerPaymentDto;
	private CustomerPaymentDto editCustomerPaymentDto;
	private CustomerPaymentDto deleteCustomerPaymentDto;

	@PostConstruct
	public void init() {
		try {
			payments = paymentFacade.getAllUnpaid();
		} catch (Exception e) {
			System.err.println("D7noun: init");
			e.printStackTrace();
		}
	}

	public void openAddDialog() {
		addCustomerPaymentDto = new CustomerPaymentDto();
	}

	public void openEditDialog(CustomerPaymentDto customerPaymentDto) {
		editCustomerPaymentDto = customerPaymentDto;
		Ajax.update("editDialog");
		Ajax.oncomplete("PF('editDialogVar').show()");
	}

	public void add() {
		try {
			Customer customer = new Customer();
			Payment payment = new Payment();

			customer.setName(addCustomerPaymentDto.getName());
			customer.setPhoneNumber(addCustomerPaymentDto.getPhoneNumber());

			customerFacade.edit(customer);

			payment.setCustomer(customer);
			payment.setDate(addCustomerPaymentDto.getDate());
			payment.setPrice(addCustomerPaymentDto.getPrice());
			payment.setNotes(addCustomerPaymentDto.getNotes());

			paymentFacade.edit(payment);

			payments = paymentFacade.getAllUnpaid();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: add");
		}
	}

	public void edit() {
		try {
			Customer customer = customerFacade.findById(editCustomerPaymentDto.getCustomerId());
			Payment payment = paymentFacade.findById(editCustomerPaymentDto.getPaymentId());

			customer.setName(editCustomerPaymentDto.getName());
			customer.setPhoneNumber(editCustomerPaymentDto.getPhoneNumber());
			customerFacade.edit(customer);

			payment.setCustomer(customer);
			payment.setDate(editCustomerPaymentDto.getDate());
			payment.setPrice(editCustomerPaymentDto.getPrice());
			payment.setNotes(editCustomerPaymentDto.getNotes());

			if (editCustomerPaymentDto.isPayed()) {
				payment.setPayed(true);
				Payment newPayment = new Payment();

				newPayment.setCustomer(customer);
				newPayment.setDate(addOneMonth(editCustomerPaymentDto.getDate()));
				newPayment.setPrice(editCustomerPaymentDto.getPrice());
				newPayment.setNotes(editCustomerPaymentDto.getNotes());
				newPayment.setPayed(false);
				paymentFacade.edit(newPayment);
			}

			paymentFacade.edit(payment);
			payments = paymentFacade.getAllUnpaid();

		} catch (Exception e) {
			System.err.println("D7noun: edit");
			e.printStackTrace();
		}
	}

	public void selectForDelete(CustomerPaymentDto customerPaymentDto) {
		deleteCustomerPaymentDto = customerPaymentDto;
	}

	public void delete() {
		try {
			Customer customer = customerFacade.findById(deleteCustomerPaymentDto.getCustomerId());
			if (customer != null) {
				customerFacade.remove(customer);
			}
			payments = paymentFacade.getAllUnpaid();
		} catch (Exception e) {
			System.err.println("D7noun: delete");
			e.printStackTrace();
		}
	}

	private Date addOneMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
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
	public List<CustomerPaymentDto> getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(List<CustomerPaymentDto> payments) {
		this.payments = payments;
	}

	/**
	 * @return the addCustomerPaymentDto
	 */
	public CustomerPaymentDto getAddCustomerPaymentDto() {
		return addCustomerPaymentDto;
	}

	/**
	 * @param addCustomerPaymentDto
	 *            the addCustomerPaymentDto to set
	 */
	public void setAddCustomerPaymentDto(CustomerPaymentDto addCustomerPaymentDto) {
		this.addCustomerPaymentDto = addCustomerPaymentDto;
	}

	/**
	 * @return the editCustomerPaymentDto
	 */
	public CustomerPaymentDto getEditCustomerPaymentDto() {
		return editCustomerPaymentDto;
	}

	/**
	 * @param editCustomerPaymentDto
	 *            the editCustomerPaymentDto to set
	 */
	public void setEditCustomerPaymentDto(CustomerPaymentDto editCustomerPaymentDto) {
		this.editCustomerPaymentDto = editCustomerPaymentDto;
	}

	/**
	 * @return the deleteCustomerPaymentDto
	 */
	public CustomerPaymentDto getDeleteCustomerPaymentDto() {
		return deleteCustomerPaymentDto;
	}

	/**
	 * @param deleteCustomerPaymentDto
	 *            the deleteCustomerPaymentDto to set
	 */
	public void setDeleteCustomerPaymentDto(CustomerPaymentDto deleteCustomerPaymentDto) {
		this.deleteCustomerPaymentDto = deleteCustomerPaymentDto;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
