package tech.lapsa.epayment.ws.mail;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.mail.Session;

import tech.lapsa.lapsa.mail.MailBuilderException;
import tech.lapsa.lapsa.mail.MailFactory;
import tech.lapsa.lapsa.mail.impl.SessionMailFactory;

@Singleton
public class MailFactoryProducer {

    public static final String JNDI_PROPERTIES_ADMIN_MAIL_SESSION = "epayment/mail/AdminNotification";

    @Resource(mappedName = JNDI_PROPERTIES_ADMIN_MAIL_SESSION)
    private Session adminMailSession;

    @Produces
    @QAdmin
    public MailFactory adminMailFactory() throws MailBuilderException {
	return new SessionMailFactory(adminMailSession);
    }
}
