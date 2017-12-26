package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import tech.lapsa.javax.validation.NotNullValue;
import tech.lapsa.javax.validation.ValidURI;

@XmlRootElement(name = "paymentURISpecifierResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPaymentURISpecifierResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    // uri *

    @XmlAttribute
    @NotNullValue
    @ValidURI
    private URI uri;

    public URI getURI() {
	return uri;
    }

    public void setInvoiceNumber(final URI uri) {
	this.uri = uri;
    }

    // CONSTRUCTORS

    public XmlPaymentURISpecifierResponse() {
    }

    public XmlPaymentURISpecifierResponse(final URI uri) {
	this.uri = uri;
    }
}
