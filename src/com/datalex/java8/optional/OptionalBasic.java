package com.datalex.java8.optional;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Optional;

/**
 * Created by shaojie.xu on 11/04/2017.
 */
public class OptionalBasic {

    public static void main(String[] args) {

        Optional<String> optionalGoal = Optional.of("goal");
        System.out.println(optionalGoal.isPresent());           // true
        System.out.println(optionalGoal.get());           // "goal"
        System.out.println(optionalGoal.orElse("fallback"));  //"goal"
        optionalGoal.ifPresent((s) -> System.out.println(s.charAt(0)));     // "g"

        Optional<String> optionalNull = Optional.ofNullable(null);
        System.out.println(optionalNull.isPresent());    //false
        System.out.println(optionalNull.orElse("fallback"));  //"fallback"
        // here an IllegalArgumentException will be thrown, in real case
        // in real scenario an application checked exception could be defined.
        optionalNull.ifPresent((s) -> System.out.println(s.charAt(0)));     // no output, no NPE
        System.out.println(optionalNull.orElseThrow(() ->
                                    new IllegalArgumentException("there is no element in it")));


    }

}
