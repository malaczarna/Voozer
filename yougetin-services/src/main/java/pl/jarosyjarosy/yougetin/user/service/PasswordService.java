package pl.jarosyjarosy.yougetin.user.service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Arrays;

@Component
public class PasswordService {
    private String salt;

    @Autowired
    public PasswordService(@Value("${user.password.salt:special-salt-for-our-use}") String salt) {
        this.salt = salt;
    }

    public String getPasswordHash(String plainText) {
        if (Strings.isNullOrEmpty(plainText)) {
            return "";
        }
        return Arrays.toString(DigestUtils.md5Digest((plainText + salt).getBytes()));
    }
}
