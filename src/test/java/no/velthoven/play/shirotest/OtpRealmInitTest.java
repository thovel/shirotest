package no.velthoven.play.shirotest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import no.velthoven.lib.shiroyubikey.OtpRealm;
import no.velthoven.lib.shiroyubikey.OtpToken;

/**
 * Created by thomas on 06.12.15.
 */
public class OtpRealmInitTest {
    @BeforeClass
    static public void init() throws IOException {
        List<Realm> realms = new ArrayList<>();

        realms.add(new OtpRealm());
        realms.add(new IniRealm("classpath:shiro.ini"));
        realms.add(new TestAuthorizer());

        SecurityUtils.setSecurityManager(new DefaultSecurityManager(realms));
    }

    @Test
    public void simpleTest() {
        Subject subject = SecurityUtils.getSubject();
        subject.login(new TestToken("cccccceijfdg", "blabla", "cccccceijfdgithrchduddndhdcnibjfgvtcbherednt" +
            ""));

        Assert.assertTrue("subject is not authenticated", subject.isAuthenticated());
        boolean found = false;
        for (Object principal : subject.getPrincipals()) {
            if ("cccccceijfdg".equals(principal.toString())) {
                found = true;
            }
        }
        Assert.assertTrue(found);
        System.out.println(subject.getPrincipal().toString());

        subject.checkPermission(new WildcardPermission("winnebago:drive:eagle5"));
        subject.checkPermission(new WildcardPermission("combined:test:test"));
        subject.checkPermission(new WildcardPermission("combined:test2:test"));
        subject.checkPermission(new WildcardPermission("combined:test3:test1"));


    }


}
