package com.flab.gettoticket.common;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptEncoder {
    public String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matches(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
