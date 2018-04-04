package org.D7noun.view;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaQuery;

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

	public List<Payment> getAll() {
		CriteriaQuery<Payment> criteria = this.entityManager.getCriteriaBuilder().createQuery(Payment.class);
		return this.entityManager.createQuery(criteria.select(criteria.from(Payment.class))).getResultList();
	}

	public Payment edit(Payment payment) {
		return this.entityManager.merge(payment);
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
