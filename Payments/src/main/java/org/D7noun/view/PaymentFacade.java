package org.D7noun.view;

import java.io.Serializable;
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
