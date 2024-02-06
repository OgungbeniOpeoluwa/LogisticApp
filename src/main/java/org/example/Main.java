package org.example;

import org.example.data.model.Administrator;
import org.example.data.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    AdministratorRepository adminRepository;

    public static void main(String[] args)  {

        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Administrator administrator = adminRepository.findByUsername("Admin");
        if(administrator == null){
            administrator = new Administrator();
            administrator.setEmail("opeoluwaagnes@gmail.com");
            administrator.setPassword("1234");
            administrator.setUsername("Admin");
            adminRepository.save(administrator);
        }

    }
}