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
public class XmlEbillInfo extends XmlEbillShort {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected EbillStatus status;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlInstantAdapter.class)
    @NotNullValue
    protected Instant created;

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlInstantAdapter.class)
    @NotNullValue
    protected Instant paid;

    @Valid
    protected XmlEbillPayer payer;

    @Valid
    protected XmlEbillPayment payment;

    @XmlElementWrapper
    @XmlElementRef
    @Valid
    @Size(min = 0)
    protected XmlEbillMethod[] availableMethods;

    @Valid
    protected XmlEbillResult result;

    public XmlEbillInfo() {
    }

    public XmlEbillInfo(final String id) {
	super(id);
    }

    public XmlEbillInfo(final String id, final EbillStatus status, final Instant created, final Instant paid,
	    final XmlEbillPayer payer,
	    final XmlEbillPayment payment,
	    final XmlEbillMethod[] availableMethods, final XmlEbillResult result) {
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

    public EbillStatus getStatus() {
	return status;
    }

    public void setStatus(final EbillStatus status) {
	this.status = status;
    }

    public XmlEbillMethod[] getAvailableMethods() {
	return availableMethods;
    }

    public void setAvailableMethods(final XmlEbillMethod[] availableMethods) {
	this.availableMethods = availableMethods;
    }

    public void setCreated(final Instant created) {
	this.created = created;
    }

    public Instant getCreated() {
	return created;
    }

    public XmlEbillResult getResult() {
	return result;
    }

    public void setResult(final XmlEbillResult result) {
	this.result = result;
    }

    public XmlEbillPayment getPayment() {
	return payment;
    }

    public void setPayment(final XmlEbillPayment purpose) {
	payment = purpose;
    }

    public Instant getPaid() {
	return paid;
    }

    public void setPaid(final Instant paid) {
	this.paid = paid;
    }

    public XmlEbillPayer getPayer() {
	return payer;
    }

    public void setPayer(final XmlEbillPayer payer) {
	this.payer = payer;
    }
}
