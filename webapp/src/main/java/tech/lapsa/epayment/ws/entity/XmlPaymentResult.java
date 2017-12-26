package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.time.Instant;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.java.jaxb.adapter.XmlInstantAdapter;
import tech.lapsa.javax.validation.NotNullValue;

@XmlRootElement(name = "ebillResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPaymentResult implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected XmlPaymentMethodType type;

    @XmlAttribute
    @NotNullValue
    protected String paymentReference;

    @XmlAttribute
    @NotNullValue
    @XmlJavaTypeAdapter(XmlInstantAdapter.class)
    protected Instant paymentTimestamp;

    public XmlPaymentResult() {
    }

    public XmlPaymentResult(final XmlPaymentMethodType type, final String paymentReference,
	    final Instant paymentTimestamp) {
	this.type = type;
	this.paymentReference = paymentReference;
	this.paymentTimestamp = paymentTimestamp;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, XmlConstants.DEFAULT_TO_STRING_STYLE);
    }

    public XmlPaymentMethodType getType() {
	return type;
    }

    public void setType(final XmlPaymentMethodType type) {
	this.type = type;
    }

    public String getPaymentReference() {
	return paymentReference;
    }

    public void setPaymentReference(final String paymentReference) {
	this.paymentReference = paymentReference;
    }

    public Instant getPaymentTimestamp() {
	return paymentTimestamp;
    }

    public void setPaymentTimestamp(final Instant paymentTimestamp) {
	this.paymentTimestamp = paymentTimestamp;
    }
}
