package com.vividbobo.easy.network.response;

import java.util.List;
import java.util.Map;

public class RateResponse {
    /**** THE API RESPONSE
     **** SUCCESS RESPONSE
     *{
     * "result": "success",
     * 	"documentation": "https://www.exchangerate-api.com/docs",
     * 	"terms_of_use": "https://www.exchangerate-api.com/terms",
     * 	"time_last_update_unix": 1585267200,
     * 	"time_last_update_utc": "Fri, 27 Mar 2020 00:00:00 +0000",
     * 	"time_next_update_unix": 1585353700,
     * 	"time_next_update_utc": "Sat, 28 Mar 2020 00:00:00 +0000",
     * 	"base_code": "USD",
     * 	"conversion_rates": {
     * 		"USD": 1,
     * 		"AUD": 1.4817,
     * 		"BGN": 1.7741,
     * 		"CAD": 1.3168,
     * 		"CHF": 0.9774,
     * 		"CNY": 6.9454,
     * 		"EGP": 15.7361,
     * 		"EUR": 0.9013,
     * 		"GBP": 0.7679,
     * 		"...": 7.8536,
     * 		"...": 1.3127,
     * 		"...": 7.4722, etc. etc.
     *        }
     *
     **** ERROR RESPONSE
     * {
     * 	"result": "error",
     * 	"error-type": "unknown-code"
     * }
     */

    private String result;
    private String time_last_update_unix;
    private String base_code;
    private Map<String,Float> conversion_rates;

    public Map<String, Float> getConversion_rates() {
        return conversion_rates;
    }

    public void setConversion_rates(Map<String, Float> conversion_rates) {
        this.conversion_rates = conversion_rates;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTime_last_update_unix() {
        return time_last_update_unix;
    }

    public void setTime_last_update_unix(String time_last_update_unix) {
        this.time_last_update_unix = time_last_update_unix;
    }

    public String getBase_code() {
        return base_code;
    }

    public void setBase_code(String base_code) {
        this.base_code = base_code;
    }

    @Override
    public String toString() {
        return "RateResponse{" +
                "result='" + result + '\'' +
                ", time_last_update_unix='" + time_last_update_unix + '\'' +
                ", base_code='" + base_code + '\'' +
                ", conversion_rates=" + conversion_rates +
                '}';
    }
}
