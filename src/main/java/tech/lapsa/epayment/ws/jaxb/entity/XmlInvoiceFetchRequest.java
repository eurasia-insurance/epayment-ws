package tech.lapsa.epayment.ws.jaxb.entity;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.java.jaxb.adapter.XmlURIAdapter;
import tech.lapsa.javax.validation.NotNullValue;
import tech.lapsa.javax.validation.ValidURI;

@XmlRootElement(name = "ebillRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInvoiceFetchRequest extends XmlInvoiceShort {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    @ValidURI
    @XmlJavaTypeAdapter(XmlURIAdapter.class)
    protected URI returnUri;

    public XmlInvoiceFetchRequest() {
    }

    public XmlInvoiceFetchRequest(final String id) {
	super(id);
    }

    public XmlInvoiceFetchRequest(final String id, final URI returnUri) {
	super(id);
	this.returnUri = returnUri;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public URI getReturnUri() {
	return returnUri;
    }

    public void setReturnUri(final URI returnUri) {
	this.returnUri = returnUri;
    }
}
