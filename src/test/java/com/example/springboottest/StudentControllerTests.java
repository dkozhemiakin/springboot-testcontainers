package com.example.springboottest;

import com.example.springboottest.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:db/script.sql")
public class StudentControllerTests {

  @ClassRule
  public static final PostgreSQLContainer PSQL_CONTAINER = new PostgreSQLContainer("postgres:9.4");

  @Autowired
  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testRetrieveStudent() throws Exception {
    String id = UUID.randomUUID().toString();
    Student student = Student.of(id, "Willy", "Chilly");

    String json = objectMapper.writeValueAsString(student);
    mockMvc.perform(
        MockMvcRequestBuilders
            .post("/students")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
    );

    RequestBuilder builder = MockMvcRequestBuilders
        .get("/students/" + id);

    mockMvc
        .perform(builder)
        .andExpect(result -> JSONAssert.assertEquals(json, result.getResponse().getContentAsString(), false));
  }

  @TestConfiguration
  public static class TestConfig {

    @Bean
    public DataSource dataSource() {
      return DataSourceBuilder
          .create()
          .driverClassName("org.postgresql.Driver")
          .username(PSQL_CONTAINER.getUsername())
          .password(PSQL_CONTAINER.getPassword())
          .url(String.format("jdbc:postgresql://%s:%s/%s",
              PSQL_CONTAINER.getContainerIpAddress(),
              PSQL_CONTAINER.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
              PSQL_CONTAINER.getDatabaseName()))
          .build();
    }

  }

}

