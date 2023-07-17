package com.example.demo;

import jakarta.persistence.EntityManager;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WordRelationTest {

	public static final String BASE_URL = "/word-relation";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WordRelationRepository repository;

	@Test
	void contextLoads() {
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
			repository.save(new WordRelation("son", "daughter", "antonym"));
			repository.save(new WordRelation("road", "street", "antonym"));
			repository.save(new WordRelation("road", "avenue", "related"));

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
	}
}
