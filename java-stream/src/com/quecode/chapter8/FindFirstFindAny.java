package com.quecode.chapter8;

import java.util.Optional;
import java.util.stream.Stream;

public class FindFirstFindAny {
    public static void main(String[] args) {
        /**
         * 8.3 Find First Find Any
         */
        Optional<Integer> any = Stream.of(3, 2, -5, 6).filter(x -> x < 0).findAny();
        System.out.println("any = " + any.get());

        Optional<Integer> first = Stream.of(3, 2, -5, 6).filter(x -> x > 0).findFirst();
        System.out.println("first = " + first.get());
    }
}
