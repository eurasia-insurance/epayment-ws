package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.ws.jaxb.validator.ValidInvoiceNumber;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.javax.validation.NotNullValue;
import tech.lapsa.javax.validation.NotZeroAmount;

@XmlRootElement(name = "paymentCompleteUnkownTypeRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPaymentCompleteUnkownTypeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // invoiceNumber *

    @XmlAttribute
    @NotNullValue
    @ValidInvoiceNumber
    private String invoiceNumber;

    public String getInvoiceNumber() {
	return invoiceNumber;
    }

    public void setInvoiceNumber(final String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    // paidAmount *

    @XmlAttribute
    @NotNullValue
    @NotZeroAmount
    private Double paidAmount;

    public Double getPaidAmount() {
	return paidAmount;
    }

    public void setPaidAmount(final Double paidAmount) {
	this.paidAmount = paidAmount;
    }

    // paidInstant *

    @XmlAttribute
    @NotNullValue
    private Instant paidInstant;

    public Instant getPaidInstant() {
	return paidInstant;
    }

    public void setPaidInstant(final Instant paidInstant) {
	this.paidInstant = paidInstant;
    }

    // paidReference

    @XmlAttribute
    private String paidReference;

    public String getPaidReference() {
	return paidReference;
    }

    public Optional<String> optPaidReference() {
	return MyOptionals.of(paidReference);
    }

    public void setPaidReference(final String paidReference) {
	this.paidReference = paidReference;
    }

    // CONSTRUCTORS

    public XmlPaymentCompleteUnkownTypeRequest() {
    }

    public XmlPaymentCompleteUnkownTypeRequest(final String invoiceNumber, final Double paidAmount,
	    final Instant paidInstant, final String paidReference) {
	this.invoiceNumber = invoiceNumber;
	this.paidAmount = paidAmount;
	this.paidInstant = paidInstant;
	this.paidReference = paidReference;
    }
}
