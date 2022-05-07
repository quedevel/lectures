package com.quecode.chapter10.service;

import com.quecode.chapter10.model.Price;

@FunctionalInterface
public interface PriceProcessor {

    Price process(Price price);

    default PriceProcessor andThen(PriceProcessor next){
        return price -> next.process(process(price));
    }
}
