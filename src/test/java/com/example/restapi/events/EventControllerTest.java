package com.example.restapi.events;

import com.example.restapi.Common.TestDescription;
import com.example.restapi.common.RestDocsConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.JsonPath;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

//import static org.springframework.restdocs.headers.HeaderDocumentation.*;
//import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    //제대로된(요청이 가능한) 값이 들어오는 경우
    @Test
    @TestDescription("정상정으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollDateTime(LocalDateTime.of(2020, 12, 02, 21, 00))
                .closeEnrollDateTime(LocalDateTime.of(2020, 12, 04, 22, 00))
                .beginEventDateTime(LocalDateTime.of(2020, 12, 05, 14, 21))
                .endEventDateTime(LocalDateTime.of(2020, 12, 07, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                //.andExpect(jsonPath("_link.profile").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time, begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new evnet"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event")
                        )
                        ,
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        )
                        ,
                        responseFields(
                                fieldWithPath("id").description("identified of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time, begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new evnet"),
                                fieldWithPath("limitOfEnrollment").description("limit of new event"),
                                fieldWithPath("offline").description("it tell if this event is offline event of not"),
                                fieldWithPath("free").description("it tell if this event is free or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.*").ignored(),
                                fieldWithPath("_links.query-events.*").ignored(),
                                fieldWithPath("_links.update-event.*").ignored(),
                                fieldWithPath("_links.profile.*").ignored()

                        )
                        

                ));
    }

    //잘못된 값이 들어오는 경우
    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 입력받는 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollDateTime(LocalDateTime.of(2020, 12, 02, 21, 00))
                .closeEnrollDateTime(LocalDateTime.of(2020, 12, 04, 22, 00))
                .beginEventDateTime(LocalDateTime.of(2020, 12, 05, 14, 21))
                .endEventDateTime(LocalDateTime.of(2020, 12, 07, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                ;
    }
    @Test
    @TestDescription("입력값이 잘못되었을 때 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("Rest API Development with Spring")
                .beginEnrollDateTime(LocalDateTime.of(2020, 12, 02, 21, 00))
                .closeEnrollDateTime(LocalDateTime.of(2020, 12, 04, 22, 00))
                //시작하는 날짜보다 끝나는 날짜가 빠른 경우
                .beginEventDateTime(LocalDateTime.of(2020, 12, 05, 14, 21))
                .endEventDateTime(LocalDateTime.of(2020, 12, 04, 14, 21))
                //최대 금액보다 기본 금액이 더 큰 경우
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
                ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception
    {
        IntStream.range(0,30).forEach(i->
                this.generateEvent(i));

        //When
        this.mockMvc.perform(get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event"+index)
                .description("Test event")
                .build();
        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
    public void getEventError404() throws Exception{

        //When &Then
        this.mockMvc.perform(get("/api/events/12345"))
                .andExpect(status().isNotFound())
        ;
    }


    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //Given
        Event event = this.generateEvent(100);

        //When &Then
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
                ;
    }
}
