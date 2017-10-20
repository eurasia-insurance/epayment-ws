package test.roles;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import tech.lapsa.epayment.ws.auth.EpaymentSecurity;

public class RolesTest {

    @Test
    public void simpleTest() {
	EpaymentSecurity.ALL.stream()
		.map(EpaymentSecurity.Role::forName)
		.forEach(Assert::assertNotNull);

	assertTrue(EpaymentSecurity.Role.values().length == EpaymentSecurity.ALL.size());
    }
}
