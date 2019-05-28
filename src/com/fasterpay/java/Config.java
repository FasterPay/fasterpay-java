
package com.fasterpay.java;

import java.util.HashMap;
import java.util.Map;


public class Config {


    public final static String VERSION = "1.0.0";
    public final static String API_BASE_URL = "https://pay.fasterpay.com";
    public final static String API_SANDBOX_BASE_URL = "https://pay.sandbox.fasterpay.com";

    public static String instance;

    private String publicKey = null;
    private String privateKey = null;
    private String apiBaseUrl = API_BASE_URL;
    private int isTest = 0;

    //Config class construct function
    public  Config(HashMap<String,String> config)
    {
        for(Map.Entry<String,String> me : config.entrySet()){
           if(me.getKey() == "publicKey"){
               this.publicKey = me.getValue();
           }

            if(me.getKey() == "privateKey"){
                this.privateKey = me.getValue();
            }

            if(me.getKey() == "apiBaseUrl"){
                this.apiBaseUrl = me.getValue();
            }

            if(me.getKey() == "isTest" && me.getValue()=="1"){
                this.isTest = 1;
                this.apiBaseUrl = API_SANDBOX_BASE_URL;
            }

        }

    }

    public String getVersion()
    {
        return VERSION;
    }

    public void setPrivateKey(String key)
    {
        this.privateKey = key;
    }

    public String getPrivateKey()
    {
        return this.privateKey;
    }

    public void setPublicKey(String key)
    {
        this.publicKey = key;
    }

    public String getPublicKey()
    {
        return this.publicKey;
    }

    public String getApiBaseUrl()
    {
        return this.apiBaseUrl;
    }

    public void setBaseUrl(String url)
    {
        this.apiBaseUrl = url;
    }

    public int getIsTest()
    {
        return this.isTest;
    }

    public  void setIsTest(int value)
    {
        if (value != 0 ){
            this.isTest = 1;
        } else {
            this.isTest = 0;
        }
    }

}
