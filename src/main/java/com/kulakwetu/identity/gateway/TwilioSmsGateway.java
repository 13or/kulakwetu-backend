package com.kulakwetu.identity.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class TwilioSmsGateway implements SmsGateway {

    private final RestClient twilioRestClient;
    private final String accountSid;
    private final String fromNumber;
    private final boolean enabled;

    public TwilioSmsGateway(
            @Value("${app.twilio.account-sid:}") String accountSid,
            @Value("${app.twilio.auth-token:}") String authToken,
            @Value("${app.twilio.from-number:}") String fromNumber,
            @Value("${app.twilio.enabled:false}") boolean enabled) {
        this.accountSid = accountSid;
        this.fromNumber = fromNumber;
        this.enabled = enabled;
        this.twilioRestClient = RestClient.builder()
                .baseUrl("https://api.twilio.com")
                .defaultHeaders(headers -> headers.setBasicAuth(accountSid, authToken))
                .build();
    }

    @Override
    public void sendVerificationCode(String phoneNumber, String code) {
        sendCode(phoneNumber, "Votre code de confirmation Kulakwetu est : " + code, "verification", code);
    }

    @Override
    public void sendPasswordResetCode(String phoneNumber, String code) {
        sendCode(phoneNumber, "Votre code de réinitialisation Kulakwetu est : " + code, "password reset", code);
    }

    private void sendCode(String phoneNumber, String bodyText, String messageType, String code) {
        if (!enabled) {
            log.info("Twilio disabled. {} code for {} is {}", messageType, phoneNumber, code);
            return;
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("To", phoneNumber);
        body.add("From", fromNumber);
        body.add("Body", bodyText);

        twilioRestClient.post()
                .uri("/2010-04-01/Accounts/{sid}/Messages.json", accountSid)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}
