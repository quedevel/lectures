package com.quecode.chapter9;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ScopeClosureCurry {
    public static void main(String[] args) {
        /**
         * 9.1 Scope, Closure, Curry
         *
         * Scope (유효범위) : 변수에 접근할 수 있는 범위
         *                   함수 안에 함수가 있을 때 내부 ㅎ마수에서 외부 함수에 있는
         *                   변수에 접근이 가능하다(lexical scope). 그 반대는 불가능 하다.
         *
         * Closure : 내부 함수가 존재하는 한 내부 함수가 사용한 외부 함수의 변수들 역시 계속 존재한다.
         *           이렇게 lexical scope를 포함하는 함수를 closure라 한다.
         *           이 때 내부 함수가 사용한 외부 함수의 변수들은 내부 함수 선언 당시로부터 변할 수 없기
         *           때문에 final로 선언되지 않더라도 암묵적으로 final로 취급된다.
         *
         * Curry : 여러 개의 매개변수를 받는 함수를 중첩된 여러 개의 함수로 쪼개어 매개 변수를
         *         한 번에 받지 않고 여러 단계에 걸쳐 나눠 받을 수 있게 하는 기술
         */
        Supplier<String> supplier = getStringSupplier();
        System.out.println("supplier = " + supplier.get());

        BiFunction<Integer,Integer,Integer> add = (x,y) -> x+y;
        Function<Integer, Function<Integer,Integer>> curriedAdd = x -> y -> x+y;

        Function<Integer, Integer> addThree = curriedAdd.apply(3);
        int result = addThree.apply(10);
        System.out.println("result = " + result);
    }

    public static Supplier<String> getStringSupplier(){
        String hell = "Hell";
        Supplier<String> supplier = () ->{
            String world = " World";
            return hell+world;
        };
        return supplier;
    }
}
