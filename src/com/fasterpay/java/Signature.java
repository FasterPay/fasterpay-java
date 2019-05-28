package com.fasterpay.java;

import com.fasterpay.java.utils.HashUtils;
import java.util.*;
import java.io.*;


public class Signature {

    Gateway gateway = null;
    public  Signature(Gateway gateway) {
        this.gateway = gateway;
    }

    public String http_build_query(Map<String ,String> array){
        String reString = "";
        Iterator it = array.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry =(Map.Entry) it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            reString += key+"="+value+"&";
        }
        reString = reString.substring(0, reString.length()-1);
        try {
            reString = java.net.URLEncoder.encode(reString,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        reString = reString.replace("%3D", "=").replace("%26", "&");
        return reString;
    }


    public String calculateHash(HashMap<String, String> params)
    {
        TreeMap<String,String> sorted = new TreeMap<String,String>();
        sorted.putAll(params);
        params = new LinkedHashMap<String, String>();
        params.putAll(sorted);
        String baseString = "";
        baseString += http_build_query(params);
        baseString += this.gateway.getConfig().getPrivateKey();
        String hash = HashUtils.sha256String(baseString);
        return hash;
    }

}
