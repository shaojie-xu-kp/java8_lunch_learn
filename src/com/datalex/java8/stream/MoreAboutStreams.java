package com.datalex.java8.stream;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by shaojie.xu on 11/04/2017.
 */
public class MoreAboutStreams {

    public static void main(String... args) {


        List<Person> persons = Arrays.asList(
                new Person("perter", 24, "Mr"),
                new Person("ian", 30, "Mr"),
                new Person("jacob", 3, "Mr"),
                new Person("angela", 28, "Ms"),
                new Person("mike", 24, "Mr"),
                new Person("steve", 60, "Mr"),
                new Person("andy", 30, "Mr"),
                new Person("jack", 15, "Mr"),
                new Person("anna", 20, "Ms"),
                new Person("andrea", 24, "Mr"));

        sortAndCollectToPersonList(persons);
        mutableSortToPersonList(persons);
        collectToPersonNameSet(persons);
        groupByAge(persons);
        sumAge(persons);
        averageAge(persons);
        findPersonNameStartsWithA(persons);
        findAndSortNamesStartsWithAAndAgeBiggerThan10(persons);
        countPersonNameContainsA(persons);
        countTitleWithMr(persons);
    }


    private static void sortAndCollectToPersonList(List<Person> persons) {

        //immutable sort, good practice
        List<Person> sortedPersons = persons.stream()
                .sorted((p1, p2) -> (p1.getAge() - p2.getAge() > 0 ? 1 : -1))
                .collect(Collectors.toList());
        System.out.println(sortedPersons);

    }

    private static void mutableSortToPersonList(List<Person> persons) {

        // mutable sort, bad practice
        List<Person> sortedPersons = new ArrayList<>();
        persons.stream()
                .sorted((p1, p2) -> (p1.getAge() - p2.getAge() > 0 ? 1 : -1))
                .forEach(person -> sortedPersons.add(person));
        System.out.println(sortedPersons);

    }


    private static void collectToPersonNameSet(List<Person> persons) {

        Set<String> personNames = persons.stream()
//                                    .map(p -> p.getName())
                .map(Person::getName)
                .collect(Collectors.toSet());
        System.out.println(personNames);

    }

    private static void groupByAge(List<Person> persons) {

        Map<Integer, List<Person>> ageMap = persons.stream()
                .collect(Collectors.groupingBy(Person::getAge));
        System.out.println(ageMap);

    }

    private static void sumAge(List<Person> persons) {

        // sum with map -> reduce
        System.out.println("Age sum : " + persons.stream()
                .map(person -> person.getAge())
                .reduce(0, (sum, age) -> sum + age));

        // sum with map -> reduce
        System.out.println("Age sum : " + persons.stream()
                .map(Person::getAge)
                .reduce(0, Integer::sum));

        // sum with mapToInt
        System.out.println("Age sum : " + persons.stream()
                .mapToInt(person -> person.getAge())
                .sum());

        // sum with accumulator and combiner, accumlator starts with 0
        Integer ageSum = persons
                .parallelStream()
                .reduce(0,
                        (sum, p) -> sum += p.getAge(),
                        (sum1, sum2) -> sum1 + sum2);

    }


    private static void averageAge(List<Person> persons) {
        // with Collect
        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(Person::getAge));
        System.out.println("Average Age : " + averageAge.intValue());

        // with map to int return an OptionalDouble
        OptionalDouble optionalAverage = persons.stream()
                .mapToInt(Person::getAge)
                .average();
        System.out.println("Average Age : " + (int) optionalAverage.orElse(0));

    }

    private static void findAndSortNamesStartsWithAAndAgeBiggerThan10(List<Person> persons) {
        System.out.println("Name Starts With j more than 10 years old : "
                + persons
                    .stream()
                    .filter(person -> person.getName().startsWith("a"))
                    .filter(person -> person.getAge() > 10)
                    .map(person -> person.getName())
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList())
        );
    }

    private static void findPersonNameStartsWithA(List<Person> persons) {

        System.out.println("Name Starts With a : " +
                persons
                        .stream()
                        .filter(person -> person.getName().startsWith("a"))
                        .collect(Collectors.toList()));
    }

    private static void countPersonNameContainsA(List<Person> persons) {
        long c = persons
                .stream()
                .filter(person -> person.getName().contains("a"))
                .count();
        System.out.println("Count of Person Name Contains a : " + c);
    }

    private static void countTitleWithMr(List<Person> persons) {
        System.out.println(String.format("there are %d with title Mr.", persons.stream()
                .map((FunctionNonThrowException<Person, String>) person -> person.getTitle())
                .filter(title -> title.equals("Mr"))
                .count()));
    }

}


class Person {

    private String name;
    private int age;
    private String title;

    public Person(String name, int age, String title) {
        this.name = name;
        this.age = age;
        this.title = title;
    }

    public String getTitle() throws IOException{
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s : %d", name, age);
    }
}


//an function to void checked exception handling in lambda express
interface FunctionNonThrowException<T, R> extends Function<T, R> {

    @Override
    default R apply(T t){
        try{
            return applyThrows(t);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    R applyThrows(T t) throws Exception;
}
