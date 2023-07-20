package be.maartenaerts.assessment;

import be.maartenaerts.assessment.wordrelation.WordRelation;
import be.maartenaerts.assessment.wordrelation.WordRelationRepository;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.flyway.clean-disabled=false"})
@AutoConfigureMockMvc
@Import(TestContainerConfiguration.class)
class WordRelationTest {

    public static final String BASE_URL = "/word-relation";
    public static final String ANTONYM = "antonym";
    public static final String RELATED = "related";
    @Autowired
    private MockMvc mockMvc;
    @Autowired

    private WordRelationRepository repository;
    @Autowired
    private Flyway flyway;

    @Autowired
    private DataSource dataSource;

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
        @DisplayName("given relation exists and duplicate is provided then error is returned")
        void duplicateRelation() throws Exception {
            repository.save(sonDaughterAntonym());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "firstWord": "son",
                              "secondWord": "daughter",
                              "type": "antonym"
                            }
                            """))
                    .andExpect(status().isConflict())
                    .andExpect(content().json("""
                            [{"message":"Relation already exists"}]
                            """));
        }

        @Test
        @DisplayName("given relation exists and inverse duplicate is provided then error is returned")
        void inverseDuplicateRelation() throws Exception {
            repository.save(sonDaughterAntonym());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "firstWord": "daughter",
                              "secondWord": "son",
                              "type": "antonym"
                            }
                            """))
                    .andExpect(status().isConflict())
                    .andExpect(content().json("""
                            [{"message":"Relation already exists"}]
                            """));
        }

        @Test
        @DisplayName("given relation with non alphanumeric characters then error is returned")
        void illegalCharacters() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "firstWord": "1son",
                              "secondWord": "%daughter",
                              "type": "antonym ok"
                            }
                            """))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                            [
                              {
                                "message": "Only alphabetic or spaces allowed",
                                "field": "firstWord"
                              },
                              {
                                "message": "Only alphabetic or spaces allowed",
                                "field": "secondWord"
                              }
                            ]
                                                        """));
        }

        @Test
        @DisplayName("given relation with all fields including uppercases and spaces then relation is stored with all fields in trimmed lowercase")
        void saveAsLowerCase() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
                            {
                              "firstWord": " Son ",
                              "secondWord": " Daughter ",
                              "type": " Antonym "
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

            assertThat(repository.findAll()).hasSize(1).first().satisfies(wr -> {
                assertThat(wr.getFirstWord()).isEqualTo("son");
                assertThat(wr.getSecondWord()).isEqualTo("daughter");
                assertThat(wr.getType()).isEqualTo("antonym");
                assertThat(wr.getId()).isNotNull();
            });
        }

        @Test
        @DisplayName("given empty relation then errors")
        void blank() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content("""
                            {
                            					
                            }
                            """))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                            [
                              {
                                "message": "must not be blank",
                                "field": "type"
                              },
                              {
                                "message": "must not be blank",
                                "field": "secondWord"
                              },
                              {
                                "message": "must not be blank",
                                "field": "firstWord"
                              }
                            ]
                                                        """));
        }
    }

    @Nested
    @DisplayName("When GET")
    class Get {
        @Test
        @DisplayName("given some relations exist then relations are returned")
        void getAll() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());

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
        @DisplayName("given some relations exist and a filter then filtered relations are returned")
        void getAllFiltered() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());

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

        @Test
        @DisplayName("given some relations exist and inverse then relations are included")
        void getAllWithInverse() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());

            mockMvc.perform(get(BASE_URL).param("inverse", "true"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            [
                              {
                                "firstWord": "son",
                                "secondWord": "daughter",
                                "type": "antonym",
                                "inverse": false
                              },
                              {
                                "firstWord": "road",
                                "secondWord": "street",
                                "type": "antonym",
                                "inverse": false
                              },
                              {
                                "firstWord": "road",
                                "secondWord": "avenue",
                                "type": "related",
                                "inverse": false
                              },
                              {
                                "firstWord": "daughter",
                                "secondWord": "son",
                                "type": "antonym",
                                "inverse": true
                              },
                              {
                                "firstWord": "street",
                                "secondWord": "road",
                                "type": "antonym",
                                "inverse": true
                              },
                              {
                                "firstWord": "avenue",
                                "secondWord": "road",
                                "type": "related",
                                "inverse": true
                              }
                            ]
                                                                                 \s"""));
        }
    }

    @Nested
    @DisplayName("When Find Path")
    class FindPath {
        @Test
        @DisplayName("given multiple linked relations then path is returned")
        void path() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());

            mockMvc.perform(get(BASE_URL + "/path/street/avenue"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            [
                              {
                                "start": "street",
                                "type": "antonym",
                                "end": "road"
                              },
                              {
                                "start": "road",
                                "type": "related",
                                "end": "avenue"
                              }
                            ]
                                                                                   """));
        }
        @Test
        @DisplayName("given no path exists then 404 not found")
        void pathNotFound() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());

            mockMvc.perform(get(BASE_URL + "/path/street/son"))
                    .andExpect(status().isNotFound());
        }
        @Test
        @DisplayName("given multiple linked relations including inverse and transitive then path is returned")
        void complexPath() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadAvenueRelated());
            repository.save(streetRoadSynonym());
            repository.save(new WordRelation("cul de sac", "avenue", RELATED));

            mockMvc.perform(get(BASE_URL + "/path/street/cul de sac"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            [
                              {
                                "start": "street",
                                "type": "synonym",
                                "end": "road"
                              },
                              {
                                "start": "road",
                                "type": "related",
                                "end": "avenue"
                              },
                              {
                                "start": "avenue",
                                "type": "related",
                                "end": "cul de sac"
                              }
                            ]
                                                                                                              \s"""));
        }
        @Test
        @DisplayName("given multiple paths then shortest path is returned")
        void shortestPath() throws Exception {
            repository.save(sonDaughterAntonym());
            repository.save(roadStreetAntonym());
            repository.save(roadAvenueRelated());
            repository.save(new WordRelation("intersection", "avenue", RELATED));
            repository.save(new WordRelation("intersection", "cul de sac", RELATED));
            repository.save(new WordRelation("cul de sac", "avenue", RELATED));

            mockMvc.perform(get(BASE_URL + "/path/street/cul de sac"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                            [
                              {
                                "start": "street",
                                "type": "antonym",
                                "end": "road"
                              },
                              {
                                "start": "road",
                                "type": "related",
                                "end": "avenue"
                              },
                              {
                                "start": "avenue",
                                "type": "related",
                                "end": "cul de sac"
                              }
                            ]
                                                                                                              \s"""));
        }
    }


    private static WordRelation roadAvenueRelated() {
        return new WordRelation("road", "avenue", RELATED);
    }

    private static WordRelation roadStreetAntonym() {
        return new WordRelation("road", "street", ANTONYM);
    }

    private static WordRelation streetRoadSynonym() {
        return new WordRelation("street", "road", "synonym");
    }

    private static WordRelation sonDaughterAntonym() {
        return new WordRelation("son", "daughter", ANTONYM);
    }
}
