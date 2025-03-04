package com.aw.midtrans_integration.configs;

import com.midtrans.Config;
import com.midtrans.ConfigFactory;
import com.midtrans.service.MidtransSnapApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MidtransConfig {

    @Value("${midtrans.serverKey}")
    private String serverKey;

    @Value("${midtrans.clientKey}")
    private String clientKey;

    @Value("${midtrans.isProduction}")
    private boolean isProduction;

    @Bean
    public MidtransSnapApi midtransSnapApi() {
        Config config = new Config(serverKey, clientKey, isProduction);
        return new ConfigFactory(config).getSnapApi();
    }
}