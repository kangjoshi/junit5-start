package com.example.junit5start;

import jdk.jfr.Enabled;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @BeforeAll
    static void beforeAll() {
        System.out.println("StudyTest.beforeAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("StudyTest.beforeEach");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("StudyTest.afterAll");
    }

    @AfterEach
    void afterEach() {
        System.out.println("StudyTest.afterEach");
    }

    @Test
    @DisplayName("스터디공일")
    void test01() {
        System.out.println("StudyTest.test01");
    }

    @Test
    @DisplayName("스터디공이")
    void test02() {
        System.out.println("StudyTest.test02");
    }

    @Test
    @DisplayName("스터디공삼")
    void test03() {
        String name = "kang";
        assertAll(
                () -> assertNull(name, "이름은 null이다."),
                () -> assertEquals("kang", name, "이름이 kang이다.")
        );
    }

    @DisplayName("반복 수행 테스트")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeatedTest(RepetitionInfo repetitionInfo) {
        System.out.println("repetitionInfo.toString() = " + repetitionInfo.toString());
    }

    @DisplayName("반복 수행 테스트")
    @ParameterizedTest(name = "{displayName}, index={index} parameter={0}")
    @ValueSource(strings = {"하늘이", "아주", "맑습니다."})
    void parameterizedTest(String message) {
        System.out.println("message = " + message);
    }

}
