package com.quecode.chapter8;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Collectors {
    public static void main(String[] args) {
        /**
         * 8.5 Collectors
         */
        List<Integer> integerList = Stream.of(3, 5, -3, 3, 4, 5).collect(java.util.stream.Collectors.toList());
        System.out.println("integerList = " + integerList);

        Set<Integer> integerSet = Stream.of(3, 5, -3, 3, 4, 5).collect(java.util.stream.Collectors.toSet());
        System.out.println("integerSet = " + integerSet);

        List<Integer> integerList1 = Stream.of(3, 5, -3, 3, 4, 5).collect(java.util.stream.Collectors.mapping(x -> Math.abs(x), java.util.stream.Collectors.toList()));
        System.out.println("integerList1 = " + integerList1);

        Set<Integer> integerSet1 = Stream.of(3, 5, -3, 3, 4, 5).collect(java.util.stream.Collectors.mapping(x -> Math.abs(x), java.util.stream.Collectors.toSet()));
        System.out.println("integerSet1 = " + integerSet1);

        int sum = Stream.of(3, 5, -3, 3, 4, 5).collect(java.util.stream.Collectors.reducing(0, (x,y)->x+y));
        System.out.println("sum = " + sum);
    }
}
