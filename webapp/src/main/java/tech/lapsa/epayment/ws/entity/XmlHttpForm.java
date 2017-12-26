package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.net.URI;

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

@XmlRootElement(name = "httpForm")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlHttpForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected URI uri;

    @XmlAttribute
    @NotNullValue
    protected String method;

    @XmlElementWrapper
    @XmlElementRef
    @NotNullValue
    @Valid
    @Size(min = 1, max = 10)
    protected XmlHttpFormParam[] params;

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, XmlConstants.DEFAULT_TO_STRING_STYLE);
    }

    public XmlHttpForm() {
    }

    public XmlHttpForm(final URI url, final String method, final XmlHttpFormParam[] params) {
	uri = url;
	this.method = method;
	this.params = params;
    }

    public URI getUri() {
	return uri;
    }

    public void setUri(final URI uri) {
	this.uri = uri;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(final String method) {
	this.method = method;
    }

    public XmlHttpFormParam[] getParams() {
	return params;
    }

    public void setParams(final XmlHttpFormParam[] params) {
	this.params = params;
    }

}
