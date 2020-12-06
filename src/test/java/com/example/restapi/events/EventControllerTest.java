package com.example.restapi.events;

import com.example.restapi.Common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

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
                ;
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
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
                //.andDo(print());
    }
}
