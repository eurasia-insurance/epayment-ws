package tech.lapsa.epayment.ws.auth;

import java.util.Set;
import java.util.stream.Stream;

import tech.lapsa.java.commons.function.MySets;

public final class EpaymentSecurity {

    private EpaymentSecurity() {
    }

    public static final String ROLE_ADMIN = "role-admin";
    // TODO REFACT : Rename role to role-user
    public static final String ROLE_ROBOT = "role-robot";

    public static final Set<String> ALL = MySets.of(ROLE_ADMIN, ROLE_ROBOT);

    public static enum Role {

	ADMIN(EpaymentSecurity.ROLE_ADMIN),
	ROBOT(EpaymentSecurity.ROLE_ROBOT);

	private final String roleName;

	private Role(final String roleName) {
	    this.roleName = roleName;
	}

	public static Role forName(final String roleName) {
	    return Stream.of(values()) //
		    .filter(x -> x.roleName().equals(roleName)) //
		    .findAny().orElse(null);
	}

	public String roleName() {
	    return roleName;
	}
    }
}
