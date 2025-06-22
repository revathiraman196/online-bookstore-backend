package com.bnppf.kata.online_book_store.config;

import com.bnppf.kata.online_book_store.entity.Book;
import com.bnppf.kata.online_book_store.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
@Profile("local") // This ensures the bean is only created if the 'local' profile is active
public class DataInitializer {
    /**
     * It will check if books already exist in the database and will only insert data if none exists.
     *
     * @param bookRepository The repository to check and save data into the database.
     * @return CommandLineRunner instance
     */
    @Bean
    public CommandLineRunner loadData(BookRepository bookRepository) {
        return args -> {
            // Check if books already exist in the database
            if (bookRepository.count() == 0) {
                // No books found, so add initial data

                Book book1 = new Book(1L, "Java 17", "Joshua Bloch", 45.00);
                Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", 38.50);
                Book book3 = new Book(3L, "Spring in Action", "Craig Walls", 35.00);

                // Save data to the H2 database
                bookRepository.save(book1);
                bookRepository.save(book2);
                bookRepository.save(book3);


                log.info("Books have been saved to the H2 database.");
            } else {
                // Data already exists, so we skip inserting new data
                log.info("Books already exist in the database. Skipping data insertion.");
            }
        };
    }
}

