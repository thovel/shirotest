package no.velthoven.play.shirotest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.ldap.JndiLdapContextFactory;
import org.apache.shiro.realm.ldap.JndiLdapRealm;
import org.apache.shiro.realm.ldap.LdapContextFactory;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import no.velthoven.lib.shiroyubikey.OtpRealm;
import no.velthoven.lib.shiroyubikey.OtpToken;

/**
 * ldapsearch -x -LLL -H ldap://localhost:3389 -b dc=oms uid=thomas
 * ldapwhoami -x -h localhost -p 3389 -D "uid=thomas,dc=oms" -W
 */
public class OtpRealmInitTest {
    @BeforeClass
    static public void init() throws IOException {
        List<Realm> realms = new ArrayList<>();

        realms.add(new OtpRealm());
        realms.add(new IniRealm("classpath:shiro.ini"));
        realms.add(createLdapRealm());
        realms.add(new TestAuthorizer());

        SecurityUtils.setSecurityManager(new DefaultSecurityManager(realms));
    }

    private static Realm createLdapRealm() {

        JndiLdapContextFactory lcf = new JndiLdapContextFactory();
        lcf.setUrl("ldap://localhost:3389");
        JndiLdapRealm nlr = new JndiLdapRealm();
        nlr.setContextFactory(lcf);
        nlr.setUserDnTemplate("uid={0},dc=oms");
        return nlr;
    }

    @Test
    public void simpleTest() {

        String yubikeyId = "";


        Subject subject = SecurityUtils.getSubject();

        // Thomas
        /*yubikeyId = "cccccceijfdg";
        subject.login(new TestToken(yubikeyId, "blabla", "cccccceijfdgivlktreugfegvcjlrttugenvvbguiuue" +
            ""))*/

        // Espen
        /*yubikeyId = "cccccceildcl";
        subject.login(new TestToken(yubikeyId, "blabla", "cccccceildcldlfugbcfhrljgjchengfcdcrfvjigtgt" +
            ""));*/

        // no otp
        subject.login(new UsernamePasswordToken("thomas", "tioL!dnt5"));

        Assert.assertTrue("subject is not authenticated", subject.isAuthenticated());
        boolean found = false;
        for (Object principal : subject.getPrincipals()) {
            if (yubikeyId.equals(principal.toString())) {
                found = true;
            }
        }
        //Assert.assertTrue(found);
        System.out.println(subject.getPrincipal().toString());

        subject.checkPermission(new WildcardPermission("winnebago:drive:eagle5"));
        subject.checkPermission(new WildcardPermission("combined:test:test"));
        subject.checkPermission(new WildcardPermission("combined:test2:test"));
        subject.checkPermission(new WildcardPermission("combined:test3:test1"));


    }


}
