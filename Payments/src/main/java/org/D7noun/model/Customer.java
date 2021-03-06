package org.D7noun.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@NamedQueries({
		@NamedQuery(name = Customer.getAllUnpaidBetweenTwoDates, query = "SELECT new org.D7noun.dto.CustomerPaymentDto(c.id, c.name, c.phoneNumber, p.id, p.price, p.date, p.notes) FROM Customer c INNER JOIN c.payments p WHERE p.payed=false AND c.id = p.customer.id AND p.date BETWEEN :fromDate AND :toDate ORDER BY p.date asc"),
		@NamedQuery(name = Customer.getAllUnpaidWithOneDate, query = "SELECT new org.D7noun.dto.CustomerPaymentDto(c.id, c.name, c.phoneNumber, p.id, p.price, p.date, p.notes) FROM Customer c INNER JOIN c.payments p WHERE p.payed=false AND c.id = p.customer.id AND p.date = :date ORDER BY p.date asc"),
		@NamedQuery(name = Customer.getAllUnpaid, query = "SELECT new org.D7noun.dto.CustomerPaymentDto(c.id, c.name, c.phoneNumber, p.id, p.price, p.date, p.notes) FROM Customer c INNER JOIN c.payments p WHERE p.payed=false AND c.id = p.customer.id ORDER BY p.date asc"),
		@NamedQuery(name = Customer.getAllPaidBetweenTwoDates, query = "SELECT new org.D7noun.dto.CustomerPaymentDto(c.id, c.name, c.phoneNumber, p.id, p.price, p.date, p.notes) FROM Customer c INNER JOIN c.payments p WHERE c.id = p.customer.id AND p.date BETWEEN :fromDate AND :toDate ORDER BY p.date asc"),
		@NamedQuery(name = Customer.getAllPaidWithOneDate, query = "SELECT new org.D7noun.dto.CustomerPaymentDto(c.id, c.name, c.phoneNumber, p.id, p.price, p.date, p.notes) FROM Customer c INNER JOIN c.payments p WHERE c.id = p.customer.id AND p.date = :date ORDER BY p.date asc"),
		@NamedQuery(name = Customer.getPricesWithOneDate, query = "SELECT p.price FROM Payment p WHERE p.payed = true AND p.date = :date "),
		@NamedQuery(name = Customer.getPricesWithTwoDates, query = "SELECT p.price FROM Payment p WHERE p.payed=true AND p.date BETWEEN :fromDate AND :toDate ") })
@Entity
public class Customer implements Serializable {

	public static final String getAllUnpaid = "getAllUnpaid";
	public static final String getAllUnpaidWithOneDate = "getAllUnpaidWithOneDate";
	public static final String getAllUnpaidBetweenTwoDates = "getAllUnpaidBetweenTwoDates";
	public static final String getAllPaidWithOneDate = "getAllPaidWithOneDate";
	public static final String getAllPaidBetweenTwoDates = "getAllpaidBetweenTwoDates";
	public static final String getPricesWithOneDate = "getPricesWithOneDate";
	public static final String getPricesWithTwoDates = "getPricesWithTwoDates";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String name;

	@Column
	private String phoneNumber;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Payment> payments = new HashSet<Payment>();

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
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

}
