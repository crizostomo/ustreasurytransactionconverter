package apitransactiontreasuryconv.govtreasuryapi;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/***
 * Configuration problem: @Configuration class 'TreasuryApiRestConfig' may not be final.
 */
@Configuration
public class TreasuryApiRestConfig {

    private final RestTemplateBuilder restTemplateBuilder;

    public TreasuryApiRestConfig(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Bean
    public RestTemplate restTemplate() {
        return this.restTemplateBuilder.build();
    }
}
