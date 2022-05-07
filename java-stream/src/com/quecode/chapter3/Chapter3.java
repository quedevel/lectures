package com.quecode.chapter3;

import com.quecode.chapter3.util.Adder;
import com.quecode.chapter3.util.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 3.1 Function Interface
 * 3.2 Lambda Expression ( Anonymous function )
 * 3.3 BiFunction Interface
 * 3.4 Custom Functional Interface
 */
public class Chapter3 {

    public static void main(String[] args) {

        /**
         * 3.1 Function Interface
         */
        Function<Integer, Integer> adder = new Adder();

        int result = adder.apply(1);

        System.out.println("> 3.1 Function Interface");
        System.out.println("result = " + result);
        System.out.println();

        /**
         * 3.2 Lambda Expression ( Anonymous function )
         *  매개변수의 타입이 유추 가능할 경우 타입 생략 가능
         *  매개변수가 하나일 경우 괄호 생략 가능
         *  바로 리턴하는 경우 중괄호 생략 가능
         */
        Function<Integer, Integer> adder2 = i -> i+10;

        int result2 = adder2.apply(1);

        System.out.println("> 3.2 Lambda Expression ( Anonymous function )");
        System.out.println("result2 = " + result2);
        System.out.println();

        /**
         * 3.3 BiFunction Interface
         *  매개변수가 두 개일 때
         */
        BiFunction<Integer, Integer, Integer> adder3 = (x, y) -> x+y;

        int result3 = adder3.apply(3,4);

        System.out.println("> 3.3 BiFunction Interface");
        System.out.println("result3 = " + result3);
        System.out.println();

        /**
         * 3.4 Custom Functional Interface
         *  매개변수가 세 개일 때
         */
        TriFunction<Integer,Integer,Integer,Integer> adder4 = (x,y,z) -> x+y+z;

        int result4 = adder4.apply(1,2,3);

        System.out.println("> 3.4 Custom Functional Interface");
        System.out.println("result4 = " + result4);
        System.out.println();

    }
}
