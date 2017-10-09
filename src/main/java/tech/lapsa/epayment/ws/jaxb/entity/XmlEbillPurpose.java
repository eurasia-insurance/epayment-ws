package tech.lapsa.epayment.ws.jaxb.entity;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.lapsa.validation.NotNullValue;

@XmlRootElement(name = "ebillPurpose")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlEbillPurpose implements Serializable {
    private static final long serialVersionUID = 1L;

    @XmlElementWrapper
    @XmlElementRef
    @NotNullValue
    @Valid
    @Size(min = 1)
    protected XmlEbillPurposeItem[] items;

    public XmlEbillPurpose() {
    }

    public XmlEbillPurpose(XmlEbillPurposeItem[] items) {
	this.items = items;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this, Constants.DEFAULT_TO_STRING_STYLE);
    }

    public XmlEbillPurposeItem[] getItems() {
	return items;
    }

    public void setItems(XmlEbillPurposeItem[] items) {
	this.items = items;
    }

}
