package no.velthoven.play.shirotest;

import org.apache.shiro.authc.UsernamePasswordToken;

import no.velthoven.lib.shiroyubikey.OtpToken;

/**
 * Created by thomas on 06.12.15.
 */
public class TestToken extends UsernamePasswordToken implements OtpToken {
    private final String otp;

    public TestToken(String userName, String password, String otp) {
        super(userName, password);
        this.otp = otp;
    }

    @Override
    public String getOtp() {
        return otp;
    }
}
