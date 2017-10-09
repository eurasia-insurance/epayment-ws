package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;
import java.net.URL;

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

@XmlRootElement(name = "httpForm")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlHttpForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected URL url;

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
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public XmlHttpForm() {
    }

    public XmlHttpForm(URL url, String method, XmlHttpFormParam[] params) {
	this.url = url;
	this.method = method;
	this.params = params;
    }

    public URL getUrl() {
	return url;
    }

    public void setUrl(URL url) {
	this.url = url;
    }

    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public XmlHttpFormParam[] getParams() {
	return params;
    }

    public void setParams(XmlHttpFormParam[] params) {
	this.params = params;
    }

}
