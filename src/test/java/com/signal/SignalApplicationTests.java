package com.signal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import com.signal.source.SourceRepository;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
    "spring.flyway.enabled=false",
    "spring.jpa.hibernate.ddl-auto=none"
})
class SignalApplicationTests {

    @MockBean
    private DataSource dataSource;

    @MockBean
    private SourceRepository sourceRepository;

    @Test
    void contextLoads() {
        assertThat(sourceRepository).isNotNull();
    }
}

