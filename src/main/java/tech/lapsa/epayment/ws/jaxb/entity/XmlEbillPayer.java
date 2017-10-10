package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lapsa.validation.NotEmptyString;
import com.lapsa.validation.NotNullValue;

@XmlRootElement(name = "ebillPayer")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillPayer implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlAttribute
    @NotNullValue
    @NotEmptyString
    protected String name;

    @XmlAttribute
    @NotNullValue
    @NotEmptyString
    protected String email;

    public XmlEbillPayer() {
    }

    public XmlEbillPayer(String name, String email) {
	this.name = name;
	this.email = email;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }
}
