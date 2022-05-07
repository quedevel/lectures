package com.quecode.chapter9;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyEvaluation {
    public static void main(String[] args) {
        /**
         * 9.2 Lazy Evaluation
         */
        if (returnTrue()||returnFalse()){
            System.out.println("1. true");
        }

        if (or(returnTrue(),returnFalse())){
            System.out.println("2. true");
        }

        if (lazyOr(()->returnTrue(),()->returnFalse())){
            System.out.println("3. true");
        }

        Stream<Integer> integerStream = Stream.of(3,-2,5,8,-3,-10)
                .filter(x -> x>0)
                .peek(x -> System.out.println("peeking x = " + x))
                .filter(x -> x%2 == 0);
        System.out.println("Before collect");

        List<Integer> integers = integerStream.collect(Collectors.toList());
        System.out.println("After collect");
    }

    public static boolean lazyOr(Supplier<Boolean> x, Supplier<Boolean> y){
        return x.get()||y.get();
    }

    public static boolean or(boolean x, boolean y){
        return x||y;
    }

    public static boolean returnTrue(){
        System.out.println("LazyEvaluation.returnTrue");
        return true;
    }

    public static boolean returnFalse(){
        System.out.println("LazyEvaluation.returnFalse");
        return false;
    }
}
