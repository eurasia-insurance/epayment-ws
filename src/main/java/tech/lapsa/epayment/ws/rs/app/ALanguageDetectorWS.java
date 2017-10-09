package tech.lapsa.epayment.ws.rs.app;

import java.util.Locale;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.lapsa.international.localization.LocalizationLanguage;

public abstract class ALanguageDetectorWS {

    @Context
    private HttpHeaders headers;

    protected LocalizationLanguage getLanguageOrDefault() {
	return LocalizationLanguage.byLocalePriorityListOrDefault(headers.getAcceptableLanguages());
    }

    protected Locale getLocaleOrDefault() {
	return getLanguageOrDefault().getLocale();
    }
}
