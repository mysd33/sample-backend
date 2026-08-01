package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"dev", "log_default"})
@SpringBootTest
class SampleBackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
