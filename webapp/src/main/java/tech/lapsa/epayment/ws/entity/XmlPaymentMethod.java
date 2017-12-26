package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import tech.lapsa.javax.validation.NotNullValue;

@XmlRootElement(name = "ebillMethod")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlPaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    protected XmlPaymentMethodType type;

    @Valid
    protected XmlHttpForm httpForm;

    public XmlPaymentMethod() {
    }

    public XmlPaymentMethod(final XmlPaymentMethodType type, final XmlHttpForm httpForm) {
	this.type = type;
	this.httpForm = httpForm;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, XmlConstants.DEFAULT_TO_STRING_STYLE);
    }

    public XmlPaymentMethodType getType() {
	return type;
    }

    public void setType(final XmlPaymentMethodType type) {
	this.type = type;
    }

    public XmlHttpForm getHttpForm() {
	return httpForm;
    }

    public void setHttpForm(final XmlHttpForm httpForm) {
	this.httpForm = httpForm;
    }
}
