package com.example.demo;

import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.rmi.dgc.DGC;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.flyway.clean-disabled=false"})
@AutoConfigureMockMvc
class WordRelationTest {

	public static final String BASE_URL = "/word-relation";
	public static final String ANTONYM = "antonym";
	@Autowired
	private MockMvc mockMvc;
	@Autowired

	private WordRelationRepository repository;
	@Autowired
	private Flyway flyway;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	void setUp() {
		flyway.clean();
		flyway.migrate();
	}

	@Nested
	@DisplayName("When create")
	class Create {
		@Test
		@DisplayName("given relation then relation is stored")
		void ok() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
							{
							  "firstWord": "son",
							  "secondWord": "daughter",
							  "type": "antonym"
							}
							"""))
					.andExpect(status().isCreated())
					.andExpect(content().json("""
                            {
                              "firstWord": "son",
                              "secondWord": "daughter",
                              "type": "antonym"
                            }
                            """));
		}
		@Test
		@DisplayName("given empty relation then error")
		void blank() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
							{
					
							}
							"""))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("When GET")
	class Get {
		@Test
		@DisplayName("given some relations exist then relations are returned")
		void getAll() throws Exception {
			repository.save(sonAntonym());
			repository.save(roadAntonym());
			repository.save(roadRelated());

			mockMvc.perform(get(BASE_URL))
					.andExpect(status().isOk())
					.andExpect(content().json("""
                            [
                              {
                                "firstWord": "son",
                                "secondWord": "daughter",
                                "type": "antonym"
                              },
                              {
                                "firstWord": "road",
                                "secondWord": "street",
                                "type": "antonym"
                              },
                              {
                                "firstWord": "road",
                                "secondWord": "avenue",
                                "type": "related"
                              }
                            ]
                                                        """));
		}
		@Test
		@DisplayName("given some relations exist and a fiter then filtered relations are returned")
		void getAllFiltered() throws Exception {
			repository.save(sonAntonym());
			repository.save(roadAntonym());
			repository.save(roadRelated());

			mockMvc.perform(get(BASE_URL).param("type", ANTONYM))
					.andExpect(status().isOk())
					.andExpect(content().json("""
                            [
                              {
                                "firstWord": "son",
                                "secondWord": "daughter",
                                "type": "antonym"
                              },
                              {
                                "firstWord": "road",
                                "secondWord": "street",
                                "type": "antonym"
                              }
                            ]
                                                        """));
		}
	}

	private static WordRelation roadRelated() {
		return new WordRelation("road", "avenue", "related");
	}

	private static WordRelation roadAntonym() {
		return new WordRelation("road", "street", ANTONYM);
	}

	private static WordRelation sonAntonym() {
		return new WordRelation("son", "daughter", ANTONYM);
	}
}
