package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lapsa.validation.NotEmptyString;
import com.lapsa.validation.NotNullValue;

@XmlRootElement(name = "ebillPurposeItem")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillPurposeItem implements Serializable {
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

    public XmlEbillPurposeItem() {
    }

    public XmlEbillPurposeItem(String title, Double price, Integer quantity, Double amount) {
	setTitle(title);
	setPrice(price);
	setQuantity(quantity);
	setAmount(amount);
    }

    public XmlEbillPurposeItem(String title, Double price, Integer quantity) {
	this(title, price, quantity, price * quantity);
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    @XmlAttribute
    public Double getAmount() {
	return price * quantity;
    }

    //

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Integer getQuantity() {
	return quantity;
    }

    public void setQuantity(Integer quantity) {
	this.quantity = quantity;
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    public void setAmount(Double amount) {
	this.amount = amount;
    }

}
