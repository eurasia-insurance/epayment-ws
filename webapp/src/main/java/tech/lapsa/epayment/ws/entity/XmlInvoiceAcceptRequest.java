package tech.lapsa.epayment.ws.entity;

import java.io.Serializable;
import java.util.Currency;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.lapsa.international.localization.LocalizationLanguage;
import com.lapsa.international.phone.PhoneNumber;

import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.jaxb.adapter.XmlCurrencyAdapter;
import tech.lapsa.javax.validation.NotEmptyString;
import tech.lapsa.javax.validation.NotNullValue;
import tech.lapsa.javax.validation.ValidEmail;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@XmlRootElement(name = "invoiceAcceptRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlInvoiceAcceptRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    // items *

    @XmlElementWrapper
    @XmlElementRef
    @NotNullValue
    @Valid
    @Size(min = 1)
    protected XmlInvoicePurposeItem[] items;

    public XmlInvoicePurposeItem[] getItems() {
	return items;
    }

    public void setItems(final XmlInvoicePurposeItem[] items) {
	this.items = items;
    }

    public void setItem(final XmlInvoicePurposeItem item) {
	items = new XmlInvoicePurposeItem[] { item };
    }

    // currency *

    @XmlAttribute
    @XmlJavaTypeAdapter(XmlCurrencyAdapter.class)
    @NotNullValue
    protected Currency currency;

    public Currency getCurrency() {
	return currency;
    }

    public void setCurrency(final Currency currency) {
	this.currency = currency;
    }

    // language *

    @XmlAttribute
    @NotNullValue
    protected LocalizationLanguage language;

    public LocalizationLanguage getLanguage() {
	return language;
    }

    public void setLanguage(final LocalizationLanguage language) {
	this.language = language;
    }

    // name *

    @XmlAttribute
    @NotNullValue
    @NotEmptyString
    protected String name;

    public String getName() {
	return name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    // externalId

    @XmlAttribute
    protected String externalId;

    public String getExternalId() {
	return externalId;
    }

    public Optional<String> optExternalId() {
	return MyOptionals.of(externalId);
    }

    public void setExternalId(final String externalId) {
	this.externalId = externalId;
    }

    public void setExternalId(final Number externalId) {
	this.externalId = String.valueOf(externalId);
    }

    // email

    @XmlAttribute
    @ValidEmail
    protected String email;

    public String getEmail() {
	return email;
    }

    public Optional<String> optEmail() {
	return MyOptionals.of(getEmail());
    }

    public void setEmail(final String email) {
	this.email = email;
    }

    // phoneNumber

    @XmlAttribute
    protected PhoneNumber phoneNumber;

    public PhoneNumber getPhoneNumber() {
	return phoneNumber;
    }

    public Optional<PhoneNumber> optPhoneNumber() {
	return MyOptionals.of(getPhoneNumber());
    }

    public void setPhoneNumber(final PhoneNumber phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    // taxpayerNumber

    @XmlAttribute
    protected TaxpayerNumber taxpayerNumber;

    public TaxpayerNumber getTaxpayerNumber() {
	return taxpayerNumber;
    }

    public Optional<TaxpayerNumber> optTaxpayerNumber() {
	return MyOptionals.of(getTaxpayerNumber());
    }

    public void setTaxpayerNumber(final TaxpayerNumber taxpayerNumber) {
	this.taxpayerNumber = taxpayerNumber;
    }

    // CONSTRUCTORS

    public XmlInvoiceAcceptRequest() {
    }
}
