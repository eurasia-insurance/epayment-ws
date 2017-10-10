package tech.lapsa.epayment.ws.rs.app;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.lapsa.commons.function.MyOptionals;
import com.lapsa.international.localization.LocalizationLanguage;

public abstract class ALanguageDetectorWS {

    @Context
    private HttpHeaders headers;

    Optional<LocalizationLanguage> getAcceptLanguage() {
	return MyOptionals.streamOf(headers.getAcceptableLanguages()) //
		.flatMap(Stream::findFirst) //
		.map(LocalizationLanguage::byLocale);
    }

    LocalizationLanguage getAcceptLanguageOrDefault() {
	return MyOptionals.streamOf(headers.getAcceptableLanguages()) //
		.flatMap(Stream::findFirst) //
		.map(LocalizationLanguage::byLocale)
		.orElse(LocalizationLanguage.getDefault());
    }

    protected LocalizationLanguage getLanguageOrDefault() {
	return LocalizationLanguage.byLocalePriorityListOrDefault(headers.getAcceptableLanguages());
    }

    protected Locale getLocaleOrDefault() {
	return getLanguageOrDefault().getLocale();
    }
}
