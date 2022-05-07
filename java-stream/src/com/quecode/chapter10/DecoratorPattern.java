package com.quecode.chapter10;

import com.quecode.chapter10.model.Price;
import com.quecode.chapter10.service.BasicPriceProcessor;
import com.quecode.chapter10.service.DiscountPriceProcessor;
import com.quecode.chapter10.service.PriceProcessor;
import com.quecode.chapter10.service.TaxPriceProcessor;

public class DecoratorPattern {
    public static void main(String[] args) {
        /**
         * 10.3 Decorator Pattern
         */
        Price unprocessedPrice = new Price("Original Price");
        PriceProcessor basicPriceProcessor = new BasicPriceProcessor();
        PriceProcessor discountPriceProcessor = new DiscountPriceProcessor();
        PriceProcessor taxPriceProcessor = new TaxPriceProcessor();

        PriceProcessor decoratedPriceProcessor = basicPriceProcessor
                .andThen(discountPriceProcessor)
                .andThen(taxPriceProcessor);
        Price processedPrice = decoratedPriceProcessor.process(unprocessedPrice);
        System.out.println("processedPrice = " + processedPrice.getPrice());

        PriceProcessor decoratedPriceProcessor2 = basicPriceProcessor
                .andThen(taxPriceProcessor)
                .andThen(price -> new Price(price.getPrice()+", then apply another procedure"));
        Price processedPrice2 = decoratedPriceProcessor2.process(unprocessedPrice);
        System.out.println("processedPrice2 = " + processedPrice2.getPrice());
    }
}
