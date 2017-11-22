package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.javax.validation.NotEmptyString;
import tech.lapsa.javax.validation.NotNullValue;

@XmlRootElement(name = "ebillPurposeItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInvoicePurposeItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    @NotEmptyString
    protected String title;

    @XmlAttribute
    @NotNullValue
    @Min(0)
    private Double price;

    @XmlAttribute
    @NotNullValue
    @Min(0)
    protected Integer quantity;

    @XmlAttribute
    @NotNullValue
    @Min(0)
    private Double amount;

    public XmlInvoicePurposeItem() {
    }

    public XmlInvoicePurposeItem(final String title, final Double price, final Integer quantity, final Double amount) {
	this.title = title;
	this.price = price;
	this.quantity = quantity;
	this.amount = amount;
    }

    public XmlInvoicePurposeItem(final String title, final Double price, final Integer quantity) {
	this(title, price, quantity, price * quantity);
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    public Integer getQuantity() {
	return quantity;
    }

    public void setQuantity(final Integer quantity) {
	this.quantity = quantity;
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(final Double price) {
	this.price = price;
    }

    public Double getAmount() {
	return amount;
    }

    public void setAmount(final Double amount) {
	this.amount = amount;
    }
}
