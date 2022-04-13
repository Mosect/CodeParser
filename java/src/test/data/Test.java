package com.sun.security.ntlm;

import java.math.BigInteger;

public final class Client extends NTLM {
    private byte[] pw1, pw2;

    public Client(String version, String hostname, String username,
                  String domain, char[] password) throws NTLMException {
        this.pw1 = getP1(password);
        this.pw2 = getP2(password);
        debug("NTLM Client: (h,u,t,version(v)) = (%s,%s,%s,%s(%s))\n",
                hostname, username, domain, version, v.toString());
    }
}
