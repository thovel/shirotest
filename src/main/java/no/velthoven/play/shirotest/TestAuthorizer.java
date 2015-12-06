package no.velthoven.play.shirotest;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by thomas on 06.12.15.
 */
public class TestAuthorizer extends AbstractAuthorizer {
    @Override
    public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {

        if (permission.toString().startsWith("[combined]") ) {
            Object otpPrincipal = subjectPrincipal.fromRealm("OtpRealm");
            Object iniPrincipal = subjectPrincipal.fromRealm("org.apache.shiro.realm.text.IniRealm_0");
            if (otpPrincipal != null && iniPrincipal != null &&
                otpPrincipal.toString().equals(iniPrincipal.toString()
                )) {
                return true;
            }
        }
        return false;

    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return false;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
    }
}
