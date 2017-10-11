package tech.lapsa.epayment.ws.rs.app;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;

import tech.lapsa.epayment.ws.jaxb.entity.EbillMethodType;
import tech.lapsa.epayment.ws.jaxb.entity.EbillStatus;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillInfo;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillMethod;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPayer;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPayment;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillPurposeItem;
import tech.lapsa.epayment.ws.jaxb.entity.XmlEbillResult;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpForm;
import tech.lapsa.epayment.ws.jaxb.entity.XmlHttpFormParam;

public class SamplesUtil {
    public static XmlEbillInfo ebillInfoSample() {
	XmlEbillInfo sample = new XmlEbillInfo("833835829896744", //
		EbillStatus.READY, //
		Instant.parse("2016-08-01T19:24:11Z"), //
		Instant.parse("2016-08-01T19:24:11Z"), //
		ebillPayerSample(), //
		ebillPaymentSample(), //
		new XmlEbillMethod[] { ebillMethodSample() }, ebillResultSample());
	return sample;
    }

    public static XmlEbillResult ebillResultSample() {
	XmlEbillResult sample = new XmlEbillResult(EbillMethodType.QAZKOM, "4444444444", Instant.now());
	return sample;
    }

    public static XmlEbillMethod ebillMethodSample() {
	XmlEbillMethod sample = new XmlEbillMethod(EbillMethodType.QAZKOM, httpFormSample());
	return sample;
    }

    public static XmlHttpForm httpFormSample() {
	try {
	    XmlHttpForm sample = new XmlHttpForm(new URI("https://testpay.kkb.kz/jsp/process/logon.jsp"), "POST",
		    Arrays.asList( //
			    new XmlHttpFormParam("Signed_Order_B64",
				    "PGRvY3VtZW50PjxtZXJjaGFudCBjZXJ0X2lkPSI3MWY4MDQxMCIgbmFtZT0iZXVyYXNpYTM2Lmt6Ij48b3JkZXIgb3JkZXJfaWQ9IjgzMzgzNTgyOTg5Njc0NCIgY3VycmVuY3k9IjM5OCIgYW1vdW50PSIxMzcxMS44MiI+PGRlcGFydG1lbnQgbWVyY2hhbnRfaWQ9Ijk4MTE2NjUxIiBhbW91bnQ9IjEzNzExLjgyIi8+PC9vcmRlcj48L21lcmNoYW50PjxtZXJjaGFudF9zaWduIHR5cGU9IlJTQSI+a0hQSzRsY1krZTF0QWpKZlQycVRwdG1oZVVxS2pkNkdTb2hyMk1sdXkxUzMvak9tSDVWVEpza1pPUENGUGNGM0gxZUtmU2tZc3FJdkhuVjF6blhnTVQzWjhEQkx5dWVrQk93d29tRjJJTlpscFltZ1F6cE5WbkJVSzY4eG5Uemxpa0JMMjYxRmI3SmFvbWlhT29DNDNtcWl6R0ZDaWI1UnRJQjNHS3V4OHdYTG1qbThCRGJaZ1lzSEhGNHYrc3BaVjhsSU14aUpiN3JreG1DSCtUeVNBQlNrRnFmYm9acm5EbzhXMklSRnFiU0p6cWc3WDlpQzhwTDZvRndjT2NhekhuY1NWbXRsNkc5Q081K0h4OXEzaG9UcW9WVXJwSEFPdjg3YnZhaUlZa3A0V3M3UTYwTVRGaFRMQmVJUUtpWUN1Z2pUcHJLTmRhMGY2d3FUNlYrMkN3PT08L21lcmNoYW50X3NpZ24+PC9kb2N1bWVudD4="), //
			    new XmlHttpFormParam("template", "default.xsl"), //
			    new XmlHttpFormParam("email", "jonn@gmail.com"), //
			    new XmlHttpFormParam("PostLink", "https://localhost:8181/order/ws/qazkom/ok"), //
			    new XmlHttpFormParam("Language", "%%LANGUAGE_TAG%%"), //
			    new XmlHttpFormParam("appendix",
				    "PGRvY3VtZW50PjxpdGVtIG5hbWU9IlBvbGljeSBvZiBPSSBDTFZPIiBudW1iZXI9IjEiIHF1YW50aXR5PSIxIiBhbW91bnQ9IjEzNzExLjgyIi8+PC9kb2N1bWVudD4="),
			    new XmlHttpFormParam("BackLink", "%%PAYMENT_PAGE_URL%%"), //
			    new XmlHttpFormParam("FailureBackLink", "%%PAYMENT_PAGE_URL%%") //
		    ).stream().toArray(XmlHttpFormParam[]::new));
	    return sample;
	} catch (URISyntaxException e) {
	    return null;
	}
    }

    public static XmlEbillPayer ebillPayerSample() {
	XmlEbillPayer sample = new XmlEbillPayer("ДЖОНН БУЛЛ", "jonn@gmail.com");
	return sample;
    }

    public static XmlEbillPayment ebillPaymentSample() {
	XmlEbillPayment sample = new XmlEbillPayment(13711.82d, new XmlEbillPurposeItem[] { ebillPurposeItemSample() },
		"1231");
	return sample;
    }

    public static XmlEbillPurposeItem ebillPurposeItemSample() {
	XmlEbillPurposeItem sample = new XmlEbillPurposeItem("Policy of OI CLVO", 15000d, 1);
	return sample;
    }

}
