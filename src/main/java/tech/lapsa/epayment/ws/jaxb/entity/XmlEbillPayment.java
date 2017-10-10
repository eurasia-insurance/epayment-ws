package tech.lapsa.epayment.ws.jaxb.entity;

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

import com.lapsa.validation.NotNullValue;
import com.lapsa.validation.NotZeroAmount;

@XmlRootElement(name = "ebillPayment")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillPayment implements Serializable {
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
    protected XmlEbillPurposeItem[] items;

    public XmlEbillPayment() {
    }

    public XmlEbillPayment(Double totalAmount, XmlEbillPurposeItem[] items) {
	this.totalAmount = totalAmount;
	this.items = items;
    }

    public XmlEbillPayment(XmlEbillPurposeItem[] items) {
	this(Arrays.stream(items).mapToDouble(x -> x.getAmount()).sum(), items);
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public Double getTotalAmount() {
	return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
	this.totalAmount = totalAmount;
    }

    public XmlEbillPurposeItem[] getItems() {
	return items;
    }

    public void setItems(XmlEbillPurposeItem[] items) {
	this.items = items;
    }
}
