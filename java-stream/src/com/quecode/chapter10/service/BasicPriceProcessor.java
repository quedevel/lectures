package com.quecode.chapter10.service;

import com.quecode.chapter10.model.Price;

public class BasicPriceProcessor implements PriceProcessor{

    @Override
    public Price process(Price price) {
        return price;
    }
}
