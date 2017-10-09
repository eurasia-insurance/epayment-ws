package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lapsa.validation.NotNullValue;

@XmlRootElement(name = "ebillMethod")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected EbillMethodType type;

    @Valid
    protected XmlHttpForm httpForm;

    public XmlEbillMethod() {
    }

    public XmlEbillMethod(EbillMethodType type, XmlHttpForm httpForm) {
	this.type = type;
	this.httpForm = httpForm;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public EbillMethodType getType() {
	return type;
    }

    public void setType(EbillMethodType type) {
	this.type = type;
    }

    public XmlHttpForm getHttpForm() {
	return httpForm;
    }

    public void setHttpForm(XmlHttpForm httpForm) {
	this.httpForm = httpForm;
    }
}
