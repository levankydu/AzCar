package com.project.AzCar.payments.paypal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {

	@Value("${paypal.client.app}")
	private String clientId;
	@Value("${paypal.client.secret}")
	private String clientSecret;
	@Value("${paypal.mode}")
	private String mode;

	@Bean
	Map<String, String> paypalSdkConfig() {
		Map<String, String> sdkConfig = new HashMap<>();
		sdkConfig.put("mode", mode);
		return sdkConfig;
	}

	@Bean
	OAuthTokenCredential authTokenCredential() {
		return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
	}

	@Bean
	APIContext apiContext() throws PayPalRESTException {
		System.out.println("PaypalConfig::clientId: " + clientId);
		System.out.println("PaypalConfig::clientSecret: " + clientSecret);
		APIContext apiContext = new APIContext(clientId, clientSecret, mode);
		apiContext.setConfigurationMap(paypalSdkConfig());
		return apiContext;
	}
}