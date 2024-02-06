package org.example.service;

import org.example.dto.request.LogisticRegisterRequest;
import org.example.service.admin.AdministratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdministratorServiceImplTest {
    @Autowired
    AdministratorService administratorService;
    @Test
    public void testThatAdministratorCanRegister(){

        LogisticRegisterRequest request = new LogisticRegisterRequest();
    }


}