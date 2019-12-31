package com.bevbot.order.drink.api.gateway.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DrinkOrderRequest {
    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("drinkName")
    private String drinkName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
