package edu.scut.jeebbs;

import edu.scut.jeebbs.domain.Customer;
import edu.scut.jeebbs.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository repo) {
	    return (args) -> {
	        repo.save(new Customer("Jack", "Bauer"));
            repo.save(new Customer("Chloe", "O'Brian"));
            repo.save(new Customer("Kim", "Bauer"));
            repo.save(new Customer("David", "Palmer"));
            repo.save(new Customer("Michelle", "Dessler"));

            // fetch all customer
            log.info("Customer found with findAll");
            log.info("---------------------------");
            repo.findAll().forEach(customer -> log.info(customer.toString()));
            log.info("");

            // fetch an individual customer by ID
            repo.findById(1L)
                    .ifPresent(customer -> {
                        log.info("Customer found with findById(1L):");
                        log.info("--------------------------------");
                        log.info(customer.toString());
                        log.info("");
                    });

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            repo.findByLastName("Bauer").forEach(bauer -> log.info(bauer.toString()));
            log.info("");
        };
    }
}
