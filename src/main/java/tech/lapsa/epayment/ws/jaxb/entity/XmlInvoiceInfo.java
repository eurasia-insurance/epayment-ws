package tech.lapsa.epayment.ws.jaxb.entity;

import java.time.Instant;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.java.jaxb.adapter.XmlInstantAdapter;
import tech.lapsa.javax.validation.NotNullValue;

@XmlRootElement(name = "ebill")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInvoiceInfo extends XmlInvoiceShort {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected InvoiceStatus status;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlInstantAdapter.class)
    @NotNullValue
    protected Instant created;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlInstantAdapter.class)
    @NotNullValue
    protected Instant paid;

    @Valid
    protected XmlPayer payer;

    @Valid
    protected XmlPayment payment;

    @XmlElementWrapper
    @XmlElementRef
    @Valid
    @Size(min = 0)
    protected XmlPaymentMethod[] availableMethods;

    @Valid
    protected XmlPaymentResult result;

    public XmlInvoiceInfo() {
    }

    public XmlInvoiceInfo(final String id) {
	super(id);
    }

    public XmlInvoiceInfo(final String id, final InvoiceStatus status, final Instant created, final Instant paid,
	    final XmlPayer payer,
	    final XmlPayment payment,
	    final XmlPaymentMethod[] availableMethods, final XmlPaymentResult result) {
	super(id);
	this.status = status;
	this.created = created;
	this.paid = paid;
	this.payer = payer;
	this.payment = payment;
	this.availableMethods = availableMethods;
	this.result = result;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public InvoiceStatus getStatus() {
	return status;
    }

    public void setStatus(final InvoiceStatus status) {
	this.status = status;
    }

    public XmlPaymentMethod[] getAvailableMethods() {
	return availableMethods;
    }

    public void setAvailableMethods(final XmlPaymentMethod[] availableMethods) {
	this.availableMethods = availableMethods;
    }

    public void setCreated(final Instant created) {
	this.created = created;
    }

    public Instant getCreated() {
	return created;
    }

    public XmlPaymentResult getResult() {
	return result;
    }

    public void setResult(final XmlPaymentResult result) {
	this.result = result;
    }

    public XmlPayment getPayment() {
	return payment;
    }

    public void setPayment(final XmlPayment purpose) {
	payment = purpose;
    }

    public Instant getPaid() {
	return paid;
    }

    public void setPaid(final Instant paid) {
	this.paid = paid;
    }

    public XmlPayer getPayer() {
	return payer;
    }

    public void setPayer(final XmlPayer payer) {
	this.payer = payer;
    }
}
