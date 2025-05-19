package practice.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import practice.shop.domain.People;
import practice.shop.dtos.PeopleDto;
import practice.shop.repository.PeopleQueryRepository;
import practice.shop.repository.PeopleRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@WebMvcTest(controllers = PeopleController.class)
@ExtendWith(RestDocumentationExtension.class)
class PeopleControllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private PeopleQueryRepository QueryRepository;

    @MockitoBean
    private PeopleRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PeopleDto dto = new PeopleDto("홍길동", 50, "123456-9876543");

    @BeforeEach
    void setMockMvc(WebApplicationContext wc, RestDocumentationContextProvider rc) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wc)
                .apply(documentationConfiguration(rc))
                .build();
    }

    @Test
    void info() throws Exception {
        given(repository.findById(anyLong())).willReturn(Optional.of(People.dtoToPeople(dto)));

        mockMvc.perform(get("/api/people/info/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.age").value(dto.getAge()))
                .andExpect(jsonPath("$.personalNumber").value(dto.getPersonalNumber()))
                .andExpect(status().isOk())
                .andDo(document("PeopleInfo", responseFields(
                        fieldWithPath("id").description("DB id"),
                        fieldWithPath("name").description("People's name"),
                        fieldWithPath("age").description("People's age"),
                        fieldWithPath("personalNumber").description("People's personalNumber")
                )));
    }

    @Test
    void save() throws Exception{
        People people = People.dtoToPeople(dto);
        given(repository.save(any())).willReturn(people);

        mockMvc.perform(post("/api/people/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(people)))
                .andExpect(jsonPath("$.age").value(dto.getAge()))
                .andExpect(jsonPath("$.personalNumber").value(dto.getPersonalNumber()))
                .andExpect(status().isOk())
                .andDo(document("PeopleSave"));
    }
}