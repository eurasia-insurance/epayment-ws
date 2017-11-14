package tech.lapsa.epayment.ws.rs.app;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.lapsa.international.localization.LocalizationLanguage;

import tech.lapsa.epayment.ws.mail.QAdmin;
import tech.lapsa.java.commons.function.MyOptionals;
import tech.lapsa.java.commons.function.MyStrings;
import tech.lapsa.java.commons.logging.MyLogger;
import tech.lapsa.javax.mail.MailException;
import tech.lapsa.javax.mail.MailFactory;
import tech.lapsa.javax.mail.MailMessageBuilder;
import tech.lapsa.javax.rs.utility.WrongArgumentException;
import tech.lapsa.javax.rs.utility.InternalServerErrorException;

public abstract class ABaseWS {

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

    private final MyLogger logger = MyLogger.newBuilder() //
	    .withNameOf(QazkomWS.class) //
	    .build();

    void mailServerErrorAdmin(InternalServerErrorException e, String textAttachement) {
	mailAdmin(String.format("CRITICAL ERROR : %1$s : %2$s", e.getClass().getSimpleName(),
		(e.getMessage() != null ? e.getMessage() : "NONE")), e, textAttachement);
    }

    void mailApplicationErrorAdmin(WrongArgumentException e, String textAttachement) {
	mailAdmin(String.format("Error : %1$s : %2$s", e.getClass().getSimpleName(),
		(e.getMessage() != null ? e.getMessage() : "NONE")), e, textAttachement);
    }

    @Inject
    @QAdmin
    private MailFactory mailFactory;

    private void mailAdmin(String subject, Exception e, String textAttachement) {
	try {
	    MailMessageBuilder messageBuilder = mailFactory.newMailBuilder()
		    .withDefaultSender()
		    .withDefaultRecipient()
		    .withSubject(subject);
	    if (e != null)
		messageBuilder.withExceptionAttached(e, "stacktrace.txt");
	    if (MyStrings.nonEmpty(textAttachement))
		messageBuilder.withTextAttached(textAttachement, "attachement.txt");
	    messageBuilder.build()
		    .send();
	} catch (MailException e1) {
	    logger.SEVERE.log(e1);
	}

    }
}
