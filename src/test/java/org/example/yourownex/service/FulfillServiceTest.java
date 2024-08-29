package org.example.yourownex.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.jdbc.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FulfillServiceTest {
    @Autowired
    FulfillService fulfillService;

    @Test
    @Sql(statements = "")
    void getToFulfill() {
        fulfillService.getToFulfill();
    }

    @Test
    void fulfill() {
    }
}
