package com.fasterpay.sdk;

import java.util.Map;

public class SubscriptionsForm extends Form {

    protected SubscriptionsForm(String privateKey, Map<String, String> parameters, String apiUrl) {
        super(privateKey, parameters, apiUrl);
    }

    public SubscriptionsForm recurringTrialAmount(String recurring_trial_amount) {
        parameters.put(FORM_RECURRING_TRIAL_AMOUNT_FIELD, recurring_trial_amount);
        return this;
    }

    public SubscriptionsForm recurringTrialPeriod(String recurring_trial_period) {
        parameters.put(FORM_RECURRING_TRIAL_PERIOD_FIELD, recurring_trial_period);
        return this;
    }

    public SubscriptionsForm recurringDuration(String recurring_duration) {
        parameters.put(FORM_RECURRING_DURATION_FIELD, recurring_duration);
        return this;
    }

    public interface RequireRecurringName<T> {
        RequireRecurringSkuId<T> recurringName(String recurring_name);
    }

    public interface RequireRecurringSkuId<T> {
        RequiredRecurringPeriod<T> recurringSkuId(String recurring_sku_id);
    }

    public interface RequiredRecurringPeriod<T> {
        T recurringPeriod(String recurring_period);
    }
}


