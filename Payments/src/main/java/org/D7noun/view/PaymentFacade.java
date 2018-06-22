package org.D7noun.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import org.D7noun.dto.CustomerPaymentDto;
import org.D7noun.model.Customer;
import org.D7noun.model.Payment;

@Stateful
@LocalBean
public class PaymentFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "payments-pu", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public Payment findById(Long id) {
		try {
			return this.entityManager.find(Payment.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(" Payment findById");
		}
		return null;
	}

	public List<Payment> getAll() {
		CriteriaQuery<Payment> criteria = this.entityManager.getCriteriaBuilder().createQuery(Payment.class);
		return this.entityManager.createQuery(criteria.select(criteria.from(Payment.class))).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CustomerPaymentDto> getAllUnpaid() {
		try {
			Query query = this.entityManager.createNamedQuery(Customer.getAllUnpaid);
			List<CustomerPaymentDto> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllUnpaid");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerPaymentDto> getAllUnpaidWithOneDate(Date date) {
		try {
			Query query = this.entityManager.createNamedQuery(Customer.getAllUnpaidWithOneDate);
			query.setParameter("date", date);
			List<CustomerPaymentDto> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllUnpaidWithOneDate");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerPaymentDto> getAllUnpaidBetweenTwoDates(Date fromDate, Date toDate) {
		try {
			Query query = this.entityManager.createNamedQuery(Customer.getAllUnpaidBetweenTwoDates);
			query.setParameter("fromDate", fromDate);
			query.setParameter("toDate", toDate);
			List<CustomerPaymentDto> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllUnpaidBetweenTwoDates");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerPaymentDto> getAllPaidWithOneDate(Date date) {
		try {
			Query query = this.entityManager.createNamedQuery(Customer.getAllPaidWithOneDate);
			query.setParameter("date", date);
			List<CustomerPaymentDto> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllPaidWithOneDate");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CustomerPaymentDto> getAllPaidBetweenTwoDates(Date fromDate, Date toDate) {
		try {
			Query query = this.entityManager.createNamedQuery(Customer.getAllPaidBetweenTwoDates);
			query.setParameter("fromDate", fromDate);
			query.setParameter("toDate", toDate);
			List<CustomerPaymentDto> result = query.getResultList();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllPaidBetweenTwoDates");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public double getPriceWithOneDate(Date date) {
		try {
			double totalPrice = 0;
			Query query = this.entityManager.createNamedQuery(Customer.getPricesWithOneDate);
			query.setParameter("date", date);
			List<Double> result = query.getResultList();
			for (Double double1 : result) {
				totalPrice += double1;
			}
			return totalPrice;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllPaidBetweenTwoDates");
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public double getPricesBetweenTwoDates(Date fromDate, Date toDate) {
		try {
			double totalPrice = 0;
			Query query = this.entityManager.createNamedQuery(Customer.getPricesWithTwoDates);
			query.setParameter("fromDate", fromDate);
			query.setParameter("toDate", toDate);
			List<Double> result = query.getResultList();
			for (Double double1 : result) {
				totalPrice += double1;
			}
			return totalPrice;
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: getAllPaidBetweenTwoDates");
		}
		return 0;
	}

	public void edit(Payment payment) {
		if (payment.getId() == null) {
			this.entityManager.persist(payment);
		} else {
			this.entityManager.merge(payment);
		}
	}

	public void remove(Payment payment) {
		this.entityManager.remove(payment);
	}

	/**
	 * 
	 * D7noun: GETTERS&SETTERS
	 * 
	 */

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager
	 *            the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
