package practice.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import practice.shop.domain.People;
import practice.shop.dtos.PeopleDto;
import practice.shop.repository.PeopleQueryRepository;
import practice.shop.repository.PeopleRepository;

import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

@WebMvcTest(controllers = DocsController.class)
@ExtendWith(RestDocumentationExtension.class)
class DocsControllerTest {

    private MockMvc mockMvc;
    private RestDocumentationResultHandler rh;

    @MockitoBean
    private PeopleRepository repository;

    private final ConstrainedFields requestConstrain = new ConstrainedFields(PeopleDto.class);
    private final ConstrainedFields responseConstrain = new ConstrainedFields(People.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final PeopleDto dto = new PeopleDto("홍길동", 50, "123456-9876543");

    @BeforeEach
    void setMockMvc(WebApplicationContext wc, RestDocumentationContextProvider rc) {
        this.rh = document("{class-name}/{method-name}",

                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        this.mockMvc = MockMvcBuilders.webAppContextSetup(wc)
                .apply(documentationConfiguration(rc))
                .alwaysDo(this.rh)
                .build();
    }

    @Test
    void info() throws Exception {
        given(repository.findById(anyLong())).willReturn(Optional.of(People.dtoToPeople(dto)));

        mockMvc.perform(get("/api/docs/info/{id}",1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.age").value(dto.getAge()))
                .andExpect(jsonPath("$.data.personalNumber").value(dto.getPersonalNumber()))
                .andExpect(status().isOk())
                .andDo(this.rh.document(
                        responseFields(
                        fieldWithPath("data.id").description("DB id"),
                        fieldWithPath("data.name").description("People's name"),
                        fieldWithPath("data.age").description("People's age"),
                        fieldWithPath("data.personalNumber").description("People's personalNumber"),
                        fieldWithPath("status").description("Only SUCCESS Or ERROR"),
                        fieldWithPath("time").description("Request Time in Korea Time")),
                        pathParameters(parameterWithName("id").description("DB Id"))));
    }

    @Test
    void save() throws Exception{
        People people = People.dtoToPeople(dto);
        given(repository.save(any())).willReturn(people);

        mockMvc.perform(post("/api/docs/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.data.age").value(dto.getAge()))
                .andExpect(jsonPath("$.data.personalNumber").value(dto.getPersonalNumber()))
                .andExpect(status().isCreated())
                .andDo(this.rh.document(
                        requestFields(requestConstrain.withPath("name").description("Request name, Not Blank"),
                                requestConstrain.withPath("age").description("Request age, Not Blank"),
                                requestConstrain.withPath("personalNumber").description("Request pn, Not Blank")
                                ),
                        responseFields(
                                responseConstrain.withPath("data.id").description("new People's id, Not Blank and Positive"),
                                responseConstrain.withPath("data.name").description("new People's name, Not Blank"),
                                responseConstrain.withPath("data.age").description("new People's age, Not Blank and Positive"),
                                responseConstrain.withPath("data.personalNumber").description("new People's pn, Not Blank and Length == 14"),
                                fieldWithPath("status").description("Only SUCCESS Or ERROR"),
                                fieldWithPath("time").description("Request Time in Korea Time"))));
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }
    
        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints")
                    .value(StringUtils.collectionToDelimitedString(
                            this.constraintDescriptions.descriptionsForProperty(path), ". ")));
        }
    }
}