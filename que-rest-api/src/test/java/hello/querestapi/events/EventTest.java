package hello.querestapi.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

//@RunWith(JUnitParamsRunner.class)
class EventTest {

    @Test
    @DisplayName("빌더 생성 테스트")
    void builder() {
        Event event = Event.builder()
                .name("Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    @DisplayName("자바빈 생성 테스트")
    void javaBean() {
        //given
        String name = "EVENT";
        String description = "SPRING";
        //when
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);
        //then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest
    @MethodSource("parametersForTestFree")
    @DisplayName("프리 여부 테스트")
    void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Stream<Arguments> parametersForTestFree() {
        return Stream.of(
                Arguments.of(0,0,true),
                Arguments.of(100,0,false),
                Arguments.of(0,100,false),
                Arguments.of(100,200,false)
        );
    }

    @ParameterizedTest
    @MethodSource("parametersForTestOffline")
    @DisplayName("오프라인 여부 테스트")
    void testOffline(String location, boolean isOffline){
        //given
        Event event = Event.builder()
                .location(location)
                .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Stream<Arguments> parametersForTestOffline() {
        return Stream.of(
                Arguments.of("압구정", true),
                Arguments.of(null, false),
                Arguments.of("", false)
        );
    }
}