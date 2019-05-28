
package com.fasterpay.java;

import java.util.HashMap;

public class Pingback {

    Gateway gateway = null;
    public  Pingback(Gateway gateway) {
        this.gateway = gateway;
    }

    public boolean validate(HashMap<String, String> params)
    {
        if (params.isEmpty()) {
            return false;
        }

        if (!params.containsKey("apiKey")) {
            return false;
        }

        if (params.get("apiKey") == this.gateway.getConfig().getPrivateKey()) {
            return true;
        }
            return false;
    }

}
