package org.D7noun.view;

import java.io.ByteArrayOutputStream;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

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

	private CustomerPaymentDto addCustomerPaymentDto = new CustomerPaymentDto();
	private CustomerPaymentDto editCustomerPaymentDto = new CustomerPaymentDto();
	private CustomerPaymentDto deleteCustomerPaymentDto = new CustomerPaymentDto();

	///////////////////////////
	private Date fromDate;
	private Date toDate;
	private double totalPrice = 0;

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

	public void search() {
		try {
			if (fromDate != null) {
				if (toDate != null) {
					totalPrice = 0;
					payments = paymentFacade.getAllUnpaidBetweenTwoDates(fromDate, toDate);
					totalPrice = paymentFacade.getPricesBetweenTwoDates(fromDate, toDate);
				} else {
					totalPrice = 0;
					payments = paymentFacade.getAllUnpaidWithOneDate(fromDate);
					totalPrice = paymentFacade.getPriceWithOneDate(fromDate);
				}
			} else if (toDate != null) {
				totalPrice = 0;
				payments = paymentFacade.getAllUnpaidWithOneDate(toDate);
				totalPrice = paymentFacade.getPriceWithOneDate(toDate);
			} else {
				totalPrice = 0;
				payments = paymentFacade.getAllUnpaid();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: search");
		}

	}

	public void exportToExcel() {

		try {

			// Create a Workbook
			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for
													// generating `.xls` file

			/*
			 * CreationHelper helps us create instances of various things like
			 * DataFormat, Hyperlink, RichTextString etc, in a format (HSSF,
			 * XSSF) independent way
			 */
			CreationHelper createHelper = workbook.getCreationHelper();

			// Create a Sheet
			Sheet sheet = workbook.createSheet("Payments");

			// Create a Font for styling header cells
			Font headerFont = workbook.createFont();
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.RED.getIndex());

			// Create a CellStyle with the font
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			// Create Cell Style for formatting Date
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			// Create a Row
			Row headerRow = sheet.createRow(0);
			Cell headerPersonName = headerRow.createCell(0);
			Cell headerPhoneNumber = headerRow.createCell(1);
			Cell headerPrice = headerRow.createCell(2);
			Cell headerDate = headerRow.createCell(3);
			Cell headerNotes = headerRow.createCell(4);

			headerPersonName.setCellValue("Person Name");
			headerPhoneNumber.setCellValue("Phone Number");
			headerPrice.setCellValue("Price");
			headerDate.setCellValue("Date");
			headerNotes.setCellValue("Notes");

			headerPersonName.setCellStyle(headerCellStyle);
			headerPhoneNumber.setCellStyle(headerCellStyle);
			headerPrice.setCellStyle(headerCellStyle);
			headerDate.setCellStyle(headerCellStyle);
			headerNotes.setCellStyle(headerCellStyle);
			//////////////////////////////
			CellStyle oneDayStyle = workbook.createCellStyle();
			oneDayStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
			oneDayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			CellStyle oneDayDateStyle = workbook.createCellStyle();
			oneDayDateStyle.setFillForegroundColor(IndexedColors.RED1.getIndex());
			oneDayDateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			oneDayDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			CellStyle threeDayStyle = workbook.createCellStyle();
			threeDayStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			threeDayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			CellStyle threeDayDateStyle = workbook.createCellStyle();
			threeDayDateStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			threeDayDateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			threeDayDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

			// Create Other rows and cells with employees data
			int rowNum = 1;
			for (CustomerPaymentDto customerPaymentDto : payments) {
				Row row = sheet.createRow(rowNum++);

				Cell personNameCell = row.createCell(0);
				personNameCell.setCellValue(customerPaymentDto.getName());

				Cell phoneNumberCell = row.createCell(1);
				phoneNumberCell.setCellValue(customerPaymentDto.getPhoneNumber());

				Cell priceCell = row.createCell(2);
				priceCell.setCellValue(customerPaymentDto.getPrice());

				Cell datecell = row.createCell(3);
				datecell.setCellValue(customerPaymentDto.getDate());
				datecell.setCellStyle(dateCellStyle);

				Cell notesCell = row.createCell(4);
				notesCell.setCellValue(customerPaymentDto.getNotes());

				if (customerPaymentDto.threeDaysNotPayed()) {
					personNameCell.setCellStyle(threeDayStyle);
					phoneNumberCell.setCellStyle(threeDayStyle);
					datecell.setCellStyle(threeDayDateStyle);
					priceCell.setCellStyle(threeDayStyle);
					notesCell.setCellStyle(threeDayStyle);
				}
				if (customerPaymentDto.OneDayNotPayed()) {
					personNameCell.setCellStyle(oneDayStyle);
					phoneNumberCell.setCellStyle(oneDayStyle);
					datecell.setCellStyle(oneDayDateStyle);
					priceCell.setCellStyle(oneDayStyle);
					notesCell.setCellStyle(oneDayStyle);
				}
			}

			// Resize all columns to fit the content size
			for (int i = 0; i < payments.size(); i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
			workbook.write(fileOut);

			Faces.sendFile(fileOut.toByteArray(), "payments.xlsx", true);

			fileOut.close();
			// Closing the workbook
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("D7noun: exportToExcel");
		}

	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * D7noun: GETTERS&SETTERS
	 * 
	 * 
	 * 
	 * 
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

	/**
	 * @return the customerFacade
	 */
	public CustomerFacade getCustomerFacade() {
		return customerFacade;
	}

	/**
	 * @param customerFacade
	 *            the customerFacade to set
	 */
	public void setCustomerFacade(CustomerFacade customerFacade) {
		this.customerFacade = customerFacade;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
		return totalPrice;
	}

	/**
	 * @param totalPrice
	 *            the totalPrice to set
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
