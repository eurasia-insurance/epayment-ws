package test.validationMessages;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;

import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.junit.Before;
import org.junit.Test;

import tech.lapsa.epayment.ws.jaxb.validator.ValidationMessages;

public class TestValidationMessages {

    private MessageInterpolator interpolator;
    private MessageInterpolatorContext context;

    @Before
    public void setup() {
	interpolator = Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
	context = new MessageInterpolatorContext(null, null, null, null);
    }

    @Test
    public void testRussianBundle() {
	Locale locale = Locale.forLanguageTag(Constants.LANGUAGE_TAG_RUSSIAN);
	testBundle(locale, Constants.LANGUAGE_TAG_RUSSIAN);
    }

    @Test
    public void testKazakhBundle() {
	Locale locale = Locale.forLanguageTag(Constants.LANGUAGE_TAG_KAZAKH);
	testBundle(locale, Constants.LANGUAGE_TAG_KAZAKH);
    }

    @Test
    public void testEnglishBundle() {
	Locale locale = Locale.ENGLISH;
	testBundle(locale, Constants.LANGUAGE_TAG_ENGLISH);
    }

    private void testBundle(Locale locale, String expectingLanguageTag) {
	testLocale(locale, expectingLanguageTag);
	testResourceBundle(locale);
	testAnnotated(locale);
    }

    private void testLocale(Locale locale, String expectingLanguageTag) {
	assertThat(locale, not(nullValue()));
	assertThat(locale.getLanguage(), is(expectingLanguageTag));
    }

    private void testResourceBundle(Locale locale) {
	ResourceBundle resources = ResourceBundle.getBundle(Constants.VALIDATION_MESSAGES_BUNDLE_BASE_NAME, locale);
	assertThat(resources, not(nullValue()));
    }

    private void testAnnotated(Locale locale) {
	for (String messageKey : ValidationMessages.ALL)
	    testMessageKey(locale, messageKey);
    }

    private void testMessageKey(Locale locale, String messageKey) {
	assertThat("Message key must be non empty string", messageKey, allOf(not(nullValue()), not(isEmptyString())));
	String interpolatedMessage = null;
	try {
	    interpolatedMessage = interpolator.interpolate(messageKey, context, locale);
	} catch (Exception e) {
	    fail(String.format("Error interpolating message key '%1$s' for locale '%2$s'", messageKey,
		    locale.getDisplayName(Locale.ENGLISH)));
	}
	assertThat("Interpolated message must be non-empty string", interpolatedMessage,
		allOf(not(nullValue()), not(isEmptyString())));
	System.out.println(String.format("key '%1$s' is interpolated as '%2$s' on locale '%3$s'", messageKey,
		interpolatedMessage, locale.getDisplayName(Locale.ENGLISH)));
    }

}
