package sap.escooters.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(0)  // Ensure the filter runs early in the chain
public class RequestCountingGlobalFilter implements GlobalFilter {

    private final Counter requestsTotalCounter;

    @Autowired
    public RequestCountingGlobalFilter(Counter requestsTotalCounter) {
        this.requestsTotalCounter = requestsTotalCounter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        requestsTotalCounter.increment();
        return chain.filter(exchange);
    }
}

