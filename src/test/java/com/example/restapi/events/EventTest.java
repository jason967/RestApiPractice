package com.example.restapi.events;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    public void builder()
    {
        Event event = Event.builder()
                .name("이름이 뭔가요?")
                .description("Rest Api 개발")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean()
    {
        //Given
        String name = "이름";
        String description = "설명";

        //When
        Event event = new Event();
        event.setName("이름");
        event.setDescription(description);

        //Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }
    @Test
    public void testFree()
    {
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isFree()).isTrue();

        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline()
    {
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isTrue();

        //Given
        event = Event.builder()
                .build();

        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isFalse();
    }
}