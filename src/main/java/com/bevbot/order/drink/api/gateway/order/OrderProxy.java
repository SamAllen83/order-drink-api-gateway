package com.bevbot.order.drink.api.gateway.order;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
public class OrderProxy {
    private Logger logger = LoggerFactory.getLogger(OrderProxy.class);

    @Value("${bartender.api.graph.ql.url}")
    private URI bartenderApiGraphQlUrl;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/order/api/drink-orders")
    public String orderDrink(@RequestBody DrinkOrderRequest drinkOrderRequest) {
        String orderDrinkMutation = asString(resourceLoader.getResource("classpath:/graphql/order/orderDrink.graphql"));

        GraphQLQuery orderDrinkGraphQlQuery = new GraphQLQuery();
        orderDrinkGraphQlQuery.setVariables(drinkOrderRequest);
        orderDrinkGraphQlQuery.setQuery(orderDrinkMutation);

        GraphQLQueryResponse response = restTemplate.postForObject(bartenderApiGraphQlUrl.toString(), orderDrinkGraphQlQuery, GraphQLQueryResponse.class);
        logger.info("graphQl response: {}", response);
        String orderDrink = response.getData().get("orderDrink").toString();
        logger.info("orderDrink: {}", orderDrink);
        return orderDrink;

    }

    public String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
