package tech.lapsa.epayment.ws.mail;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.mail.Session;

import com.lapsa.mail2.MailBuilderException;
import com.lapsa.mail2.MailFactory;
import com.lapsa.mail2.impl.SessionMailFactory;

@Singleton
public class MailFactoryProducer {

    public static final String JNDI_PROPERTIES_ADMIN_MAIL_SESSION = "eurasia36/mail/AdminNotificationSession";

    @Resource(mappedName = JNDI_PROPERTIES_ADMIN_MAIL_SESSION)
    private Session adminMailSession;

    @Produces
    @QAdmin
    public MailFactory adminMailFactory() throws MailBuilderException {
	return new SessionMailFactory(adminMailSession);
    }
}
