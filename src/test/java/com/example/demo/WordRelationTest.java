package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WordRelationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Nested
	@DisplayName("When create")
	class Create {
		@Test
		@DisplayName("given relation then relation is stored")
		void ok() throws Exception {
			mockMvc.perform(post("/word-relation").contentType(MediaType.APPLICATION_JSON).content("""
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

}
