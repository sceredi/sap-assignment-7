package sap.escooters.apigateway;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Counter requestsTotalCounter(MeterRegistry registry) {
        return Counter.builder("requests_total")
                .description("The total number of received requests")
                .register(registry);
    }
}

