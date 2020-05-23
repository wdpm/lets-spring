package io.github.wdpm;

import io.github.wdpm.domain.Person;
import io.github.wdpm.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner run(PersonRepository personRepository) {
        return args -> {

            // create persons
            List<Person> personList = Arrays.asList(new Person("John", "Doe", 55), new Person("Emma", "Watson", 29),
                                                    new Person("Jovan", "Hover", 35), new Person("Atta", "Shah", 30),
                                                    new Person("Mike", "Lee", 61));
            personRepository.saveAll(personList);

            // get all persons by last name
            Pageable     pageable   = PageRequest.of(0, 3);
            Page<Person> personPage = personRepository.findByLastName("Doe", pageable);
            personPage
                    .stream()
                    .forEach(System.out::println);
            System.out.println("==========");

            // get all persons sorted by their age in the descending order
            Pageable pageable2 = PageRequest.of(0, 5, Sort
                    .by("age")
                    .descending());
            Page<Person> personPage2 = personRepository.findAll(pageable2);
            personPage2
                    .stream()
                    .forEach(System.out::println);
            System.out.println("==========");

            // skip pagination
            Slice<Person> personSlice = personRepository.findByAgeBetween(20, 60, Pageable.unpaged());
            personSlice
                    .stream()
                    .forEach(System.out::println);
        };
    }
}
