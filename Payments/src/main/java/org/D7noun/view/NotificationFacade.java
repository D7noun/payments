package org.D7noun.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.D7noun.model.Payment;

@Stateful
@LocalBean
public class NotificationFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "payments-pu", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Payment> findPaymentsByDate(Date date) {
		try {
			Query query = entityManager.createNamedQuery(Payment.findPaymentsByDate, Payment.class);
			query.setParameter("date", date);
			return query.getResultList();
		} catch (Exception e) {
			System.err.println("D7noun: findPaymentsByDate");
			e.printStackTrace();
		}
		return new ArrayList<>();
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
