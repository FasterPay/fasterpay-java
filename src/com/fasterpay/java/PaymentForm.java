package com.fasterpay.java;
import java.util.HashMap;
import java.util.Map;


public class PaymentForm {


	final static String END_POINT = "/payment/form";
	final static String FORM_AMOUNT_FIELD = "amount";
	final static String FORM_DESCRIPTION_FIELD = "description";
	final static String FORM_CURRENCY_FIELD = "currency";
	final static String FORM_API_KEY_FIELD = "api_key";
	final static String FORM_MERCHANT_ORDER_ID_FIELD = "merchant_order_id";
	final static String FORM_SUCCESS_URL = "success_url";
	final static String FORM_HASH_FIELD = "hash";

	final static String FORM_RECURRING_NAME_FIELD = "recurring_name";
	final static String FORM_RECURRING_SKU_ID_FIELD = "recurring_sku_id";
	final static String FORM_RECURRING_PERIOD_FIELD = "recurring_period";
	final static String FORM_RECURRING_TRIAL_AMOUNT_FIELD = "recurring_trial_amount";
	final static String FORM_RECURRING_TRIAL_PERIOD_FIELD = "recurring_trial_period";

//gateway initialize
	Gateway gateway = null;
	public  PaymentForm(Gateway gateway) {
		this.gateway = gateway;
	}

	public static String[] getBasicPaymentFields() {

		String [] result ={FORM_AMOUNT_FIELD,
				           FORM_DESCRIPTION_FIELD,
				           FORM_CURRENCY_FIELD,
				           FORM_API_KEY_FIELD,
				           FORM_MERCHANT_ORDER_ID_FIELD,
				           FORM_SUCCESS_URL};
		return result;

	}

	public static String[] getSubscriptionPaymentFields() {

		String [] result ={FORM_RECURRING_NAME_FIELD,
				           FORM_RECURRING_SKU_ID_FIELD,
				           FORM_RECURRING_PERIOD_FIELD};
		return result;
	}

	public static String[] getSubscriptionTrialFields() {

		String [] result ={FORM_RECURRING_TRIAL_AMOUNT_FIELD,
				           FORM_RECURRING_TRIAL_PERIOD_FIELD};

		return result;
	}

	public String buildForm(HashMap<String, String> attributes)
	{
		attributes.put(FORM_API_KEY_FIELD,this.gateway.getConfig().getPublicKey());
		attributes.put(FORM_HASH_FIELD,this.gateway.signature().calculateHash(attributes));
		String form = "";
		form = form+"<form align=\"center\" method=\"post\" action=\""+this.gateway.getConfig().getApiBaseUrl()+END_POINT+"\" name=\"fasterpay_payment_form\" id=\"fasterpay_payment_form\">";
		for(Map.Entry<String,String> me : attributes.entrySet()){
			form += "<input type=\"hidden\" name=\""+me.getKey()+"\" value=\""+me.getValue()+"\"/>";
		}
		//$form .= '<input type="Submit" value="Pay Now" id="fasterpay_submit"/></form>';
		form += "<input type=\"submit\" value=\"Pay Now\" id=\"fasterpay_submit\"/></form>";
		return form;
	}

}

