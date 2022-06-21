package hello.querestapi.events;

import hello.querestapi.accounts.Account;
import hello.querestapi.accounts.AccountRepository;
import hello.querestapi.accounts.AccountRole;
import hello.querestapi.accounts.AccountService;
import hello.querestapi.common.BaseControllerTest;
import hello.querestapi.commons.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @BeforeEach
    public void setUp(){
        eventRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .beginEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .endEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                            links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update event"),
                                linkWithRel("profile").description("profile")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                            ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("location header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            relaxedResponseFields(
                                    fieldWithPath("id").description("identifier of new event"),
                                    fieldWithPath("name").description("name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                    fieldWithPath("free").description("it tells if this event is free or not"),
                                    fieldWithPath("offline").description("it tells if this event is offline meeting of not"),
                                    fieldWithPath("eventStatus").description("event status"),
                                    fieldWithPath("_links.query-events.href").description("link to query event"),
                                    fieldWithPath("_links.self.href").description("link to self"),
                                    fieldWithPath("_links.update-event.href").description("link to update event"),
                                    fieldWithPath("_links.profile.href").description("link to profile")
                            )
                        )
                )
        ;
    }

    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {
        String username = "quedevel@email.com";
        String password = "quedevel";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        accountService.saveAccount(account);

        String clientId = appProperties.getClientId();
        String clientSecret = appProperties.getClientSecret();

        ResultActions resultActions = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientSecret))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"));

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        String content = response.getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(content).get("access_token").toString();
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest() throws Exception {
        // given when
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .beginEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .endEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        // then
        mockMvc.perform(post("/api/events")
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest_Empty_Input() throws Exception {
        //given when
        EventDto eventDto = EventDto.builder().build();

        //then
        mockMvc.perform(post("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    void createEvent_BadRequest_Wrong_Input() throws Exception {
        //given when
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .beginEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .endEventDateTime(LocalDateTime.of(2022, 5, 17, 15, 20))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        //then
        mockMvc.perform(post("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    void queryEvents() throws Exception {
        //given
        IntStream.range(0,30).forEach(this::generateEvent);
        //when
        ResultActions actions = this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC")
        );
        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-event"))
        ;

    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("spring" + i)
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .beginEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .endEventDateTime(LocalDateTime.of(2022, 6, 17, 15, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return eventRepository.save(event);
    }

    @Test
    @DisplayName("기존의 이벤트를 하나 조회하기")
    void getEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);

        //when
        ResultActions actions = this.mockMvc.perform(get("/api/events/{id}", event.getId()));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
        ;
    }

    @Test
    @DisplayName("없는 이벤트를 조회했을 때 404 응답받기")
    void getEvent404() throws Exception {
        //when
        ResultActions actions = this.mockMvc.perform(get("/api/events/21341"));
        //then
        actions.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("이벤트를 정상적으로 수정하기")
    void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(100);
        String eventName = "Update Event";
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setName(eventName);

        //when
        ResultActions actions =
                this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto))
                );

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @Test
    @DisplayName("입력값이 비어있는 경우에 이벤트 수정 실패")
    void updateEvent400empty() throws Exception {
        //given
        Event event = this.generateEvent(100);
        EventDto eventDto = EventDto.builder().build();

        //when
        ResultActions actions =
                this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto))
                );

        //then
        actions.andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("입력값이 잘못된된 경우에 이벤 수정 실패")
    void updateEvent400wrong() throws Exception {
        //given
        Event event = this.generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(100);

        //when
        ResultActions actions =
                this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto))
                );

        //then
        actions.andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("존재하지 않는 경우에 이벤트 수정 실패")
    void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(100);
        EventDto eventDto = modelMapper.map(event, EventDto.class);

        //when
        ResultActions actions =
                this.mockMvc.perform(put("/api/events/123123")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto))
                );

        //then
        actions.andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
}
