package org.D7noun.view;

import java.io.Serializable;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaQuery;

import org.D7noun.model.Customer;

@Stateful
@LocalBean
public class CustomerFacade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PersistenceContext(unitName = "payments-pu", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public Customer findById(Long id) {
		try {
			return this.entityManager.find(Customer.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(" Customer findById");
		}
		return null;
	}

	public List<Customer> getAll() {
		CriteriaQuery<Customer> criteria = this.entityManager.getCriteriaBuilder().createQuery(Customer.class);
		return this.entityManager.createQuery(criteria.select(criteria.from(Customer.class))).getResultList();
	}

	public void edit(Customer customer) {
		if (customer.getId() == null) {
			this.entityManager.persist(customer);
		} else {
			this.entityManager.merge(customer);
		}
	}

	public void remove(Customer customer) {
		this.entityManager.remove(customer);
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
