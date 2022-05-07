package com.quecode.chapter9;

import java.util.function.Function;

public class FunctionComposition {
    public static void main(String[] args) {
        /**
         * 9.3 Function Composition
         */
        Function<Integer,Integer> multiplyByTwo = x-> 2*x;
        Function<Integer,Integer> addTen = x-> x+10;
        Function<Integer,Integer> composedFunction = multiplyByTwo.compose(addTen);
        System.out.println("composedFunction.apply(3) = " + composedFunction.apply(3));
        
        Function<Integer,Integer> andThenFunction = multiplyByTwo.andThen(addTen);
        System.out.println("andThenFunction.apply(3) = " + andThenFunction.apply(3));

        
    }
}
