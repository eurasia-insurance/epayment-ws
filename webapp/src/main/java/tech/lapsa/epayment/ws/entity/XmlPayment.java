package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.util.Arrays;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.javax.validation.NotNullValue;
import tech.lapsa.javax.validation.NotZeroAmount;

@XmlRootElement(name = "ebillPayment")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPayment implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    @NotZeroAmount
    protected Double totalAmount;

    @XmlElementWrapper
    @XmlElementRef
    @NotNullValue
    @Valid
    @Size(min = 1)
    protected XmlInvoicePurposeItem[] items;

    @XmlAttribute
    protected String externalId;

    public XmlPayment() {
    }

    public XmlPayment(final Double totalAmount, final XmlInvoicePurposeItem[] items, final String externalId) {
	this.totalAmount = totalAmount;
	this.items = items;
	this.externalId = externalId;
    }

    public XmlPayment(final XmlInvoicePurposeItem[] items, final String externalId) {
	this(Arrays.stream(items).mapToDouble(x -> x.getAmount()).sum(), items, externalId);
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, XmlConstants.DEFAULT_TO_STRING_STYLE);
    }

    public Double getTotalAmount() {
	return totalAmount;
    }

    public void setTotalAmount(final Double totalAmount) {
	this.totalAmount = totalAmount;
    }

    public XmlInvoicePurposeItem[] getItems() {
	return items;
    }

    public void setItems(final XmlInvoicePurposeItem[] items) {
	this.items = items;
    }

    public String getExternalId() {
	return externalId;
    }

    public void setExternalId(final String externalId) {
	this.externalId = externalId;
    }
}
