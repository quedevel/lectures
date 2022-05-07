package com.quecode.chapter5;

import com.quecode.chapter4.model.User;
import com.quecode.chapter5.model.Car;
import com.quecode.chapter5.model.Sedan;
import com.quecode.chapter5.model.Suv;
import com.quecode.chapter5.model.Van;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 4.1 Method Reference
 * 4.2 Method Reference 2
 * 4.3 Constructor Reference
 */
public class Chapter5 {

    public static void main(String[] args) {

        /**
         * 4.1 Method Reference
         */
        Function<String, Integer> str2int = Integer::parseInt;
        System.out.println("str2int = " + str2int.apply("20"));

        String str = "Hell";
        Predicate<String> equalsToHell = str::equals;
        System.out.println("equalsToHell = " + equalsToHell.test(str));

        System.out.println(calculate(8,2,(x,y)-> x+y));

        // static method
        System.out.println(calculate(8,2,Chapter5::multiply));

        // instance method
        Chapter5 chapter5 = new Chapter5();
        System.out.println(calculate(8,2,chapter5::subtract));
        chapter5.myMethod();

        System.out.println();
        System.out.println();

        /**
         * 4.2 Method Reference 2
         */
        Function<String, Integer> strLength = String::length;
        int length = strLength.apply("Hell World");
        System.out.println("length = " + length);

        BiPredicate<String, String> strEquals = String::equals;
        boolean result = strEquals.test("Hell", "World");
        System.out.println("result = " + result);

        List<User> users = new ArrayList<>();
        users.add(new User(3, "Alice"));
        users.add(new User(1, "Charlie"));
        users.add(new User(5, "Bob"));
        printUserField(users, User::getName);

        System.out.println();
        System.out.println();

        /**
         * 4.3 Constructor Reference
         */
        User u = new User(1, "Alice");
        BiFunction<Integer, String, User> userCreator = User::new;
        User charlie = userCreator.apply(3,"Charlie");
        System.out.println("charlie = " + charlie);

        Map<String, BiFunction<String,String, Car>> carTypeMap = new HashMap<>();
        carTypeMap.put("sedan", Sedan::new);
        carTypeMap.put("suv", Suv::new);
        carTypeMap.put("van", Van::new);

        String[][] inputs = new String[][]{
                {"sedan", "Sonata","Hyundai"},
                {"van", "Sienna","Toyota"},
                {"sedan", "Model S","Tesla"},
                {"suv", "Sorento","KIA"}
        };

        List<Car> carList = new ArrayList<>();
        for (int i = 0; i < inputs.length; i++) {
            String[] input = inputs[i];
            String carType = input[0];
            String name = input[1];
            String brand = input[2];

            carList.add(carTypeMap.get(carType).apply(name,brand));
        }

        for (Car car : carList) {
            car.drive();
        }
    }

    public static int calculate(int x, int y, BiFunction<Integer, Integer, Integer> operator){
        return operator.apply(x,y);
    }

    public static int multiply(int x, int y){
        return x * y;
    }

    public int subtract(int x, int y){
        return x - y;
    }

    public void myMethod(){
        System.out.println(calculate(10,3,this::subtract));
    }

    public static void printUserField(List<User> users, Function<User, Object> getter){
        for (User user : users) {
            System.out.println(getter.apply(user));
        }
    }
}
