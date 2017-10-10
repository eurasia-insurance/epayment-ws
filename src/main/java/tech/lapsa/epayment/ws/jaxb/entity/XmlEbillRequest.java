package tech.lapsa.epayment.ws.jaxb.entity;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lapsa.validation.NotNullValue;

import tech.lapsa.java.jaxb.adapter.XmlURIAdapter;

@XmlRootElement(name = "ebillRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillRequest extends XmlEbillShort {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    @XmlJavaTypeAdapter(XmlURIAdapter.class)
    protected URI returnUri;

    public XmlEbillRequest() {
    }

    public XmlEbillRequest(String id) {
	super(id);
    }

    public XmlEbillRequest(String id, URI returnUri) {
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

    public void setReturnUri(URI returnUri) {
	this.returnUri = returnUri;
    }
}
