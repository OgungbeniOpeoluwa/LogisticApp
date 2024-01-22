package org.example.service;

import org.example.dto.AdministratorRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AdministratorServiceImplTest {
    @Autowired
    AdministratorService administratorService;
    @Test
    public void testThatAdministratorCanRegister(){
        AdministratorRequest request = new AdministratorRequest();
    }


}