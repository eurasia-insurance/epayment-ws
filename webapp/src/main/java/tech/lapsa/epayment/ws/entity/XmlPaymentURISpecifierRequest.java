package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.epayment.ws.jaxb.validator.ValidInvoiceNumber;
import tech.lapsa.javax.validation.NotNullValue;

@XmlRootElement(name = "paymentURISpecifierRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPaymentURISpecifierRequest implements Serializable {

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

    // CONSTRUCTORS

    public XmlPaymentURISpecifierRequest() {
    }

    public XmlPaymentURISpecifierRequest(final String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }
}
