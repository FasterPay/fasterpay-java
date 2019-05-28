package com.fasterpay.java;

import java.util.HashMap;

public class Gateway {

        Config config;

        public Gateway(HashMap<String,String> array)
        {
            this.config = new Config(array);
        }

        public  PaymentForm paymentForm()
        {
            return new PaymentForm(this);
        }

        public  Signature signature()
        {
            return new Signature(this);
        }

        public  Pingback pingback()
        {
            return new Pingback(this);
        }



        public  Config getConfig()
        {
            return this.config;
        }






}
