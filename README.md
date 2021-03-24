## Junit5

#### 기본 애노테이션
1. @Test
    - 테스트 메서드를 지정한다.

2. @BeforeAll
    - 모든 테스트가 실행되기 전 딱 실행된다.
    - static void 메서드로 만들어야 한다.

3. @AfterAll
    - 모든 테스트가 실행된 후 실행된다.
    - static void 메서드로 만들어야 한다.
    
4. @BeforeEach
    - 각 테스트가 실행되기 전 실행된다.
    - 일반 void 메서드로 작성
    
5. @AfterEach
    - 각테스트가 실행된 후 실행된다.
    - 일반 void 메서드로 작성
    
6. @DisplayName
    - 테스트에 이름을 부여한다.

7. @DisplayNameGeneration
    - 이름에 대한 전략을 넣는다.
    - `@DisplayName`으로 직접적으로 이름을 지정할 수 있으므로 사용하지 않아도 될듯.

8. @Disabled
    - 지정된 메서드를 테스트하지 않는다.

9. @EnabledOnOs / @DisabledOnOs
    - 테스트를 특정 OS에서만 수행(수행X) 하도록 지정한다.

10. @DisabledOnJre / @DisabledOnJre
    - 테스트를 특정 JRE 버전에서만 수행(수행X) 하도록 지정한다.

11. @EnabledIfEnvironmentVariable
    - 테스트를 주어진 환경 변수의 값과 동일할 때만 수행하도록 지정한다.
    
#### Assertion
1. assertAll(Excutable... excutables)
    - 이전 assert가 실패하면 다음 assert는 실행되지 않는다. 모두를 같이 실행하고 싶다면 assertAll로 묶어서 실행한다.
    - ```java
      assertAll(
              () -> assertNull(name, "이름은 null이다."),              // 이전에 있는 assert가 실패해도
              () -> assertEquals("kang", name, "이름이 kang이다.")     // 다음에 있는 assert는 실행된다
      );    
      ```
2. assertTimeout(Duration duration)
    - 테스트는 지정된 시간안에 종료가 되어야 한다.

#### 태깅과 필터링
@Tag
    - 태그를 지정한다. 
    - 지정된 태그를 이용하여 특정 태그의 메서드만 수행할 수 있다.
    - 커스텀 애노테이션을 만들어서 사용도 가능하다.

#### 테스트 반복
1. @RepeatedTest
    - 반복 횟수를 지정하여 반복한다.
    - ```java
      @DisplayName("반복 수행 테스트")
      @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
      void repeatedTest(RepetitionInfo repetitionInfo) {
          System.out.println("repetitionInfo.toString() = " + repetitionInfo.toString());
      }
      ```

2. @ParameterizedTest
    - 주어진 파라미터 만큼 반복한다.
    - ```java
      @DisplayName("반복 수행 테스트")
      @ParameterizedTest(name = "{displayName}, index={index} parameter={0}")
      @ValueSource(strings = {"하늘이", "아주", "맑습니다."})
      void parameterizedTest(String message) {
          System.out.println("message = " + message);    
      }
      ```
    - @ValueSource, @NullSource, @EmptySource, @CsvSource와 같이 여러가지 파라미터를 지정할 수 있다. 
    
#### 테스트 인스턴스
- 각 테스트마다 테스트 인스턴스를 새로 생성한다. (독립적인 테스트 실행 보장을 위해)
- Junit5에서는 인스턴스 전략을 변경할 수 있는 기능을 제공한다.
    - @TestInstance(TestInstance.Lifecycle.PER_CLASS)를 클래스에 붙인다.
    - @TestInstance를 붙이게 되면 @BeforeAll/@AfterAll를 static이 아닌 메서드(일반 메서드)로 만들수 있다.

#### 테스트 순서
- 기본적으로 순서를 보장하지 않는다.
- 순서를 보장하도록 지정 가능하다.
- @TestMethodOrder(MethodOrderer.OrderAnnotation.class) : 메서드에 붙은 @Order 순서대로 실행 


### Mockito
Mock : 진짜 객체와 비슷하게 동작하지만 프로그래머가 직접 그 객체의 행동을 관리할 수 있는 객체

#### Mock 객체 만들기
1. 애노테이션 이용
- ```java
  // @Mock 애노테이션을 처리하기 위한 확장을 정의 
  @ExtendWith(MockitoExtension.class)
  class ServiceTest {  
      @Mock
      MemberService memberService;
  }
  ```

2. 스태틱 메서드 이용  
- ```java
    MemberService memberService = Mokito.mock(MemberService.class);
  ```
- `@ExtendWith(MockitoExtension.class)` 확장이 필요 없음.

#### Mock Stubbing
- 목 객체의 행동을 조작한다.
- 목 객체는 기본적으로 null, 기본 값(Primitive Type), 빈 배열등을 반환한다.
- ```java
    // memberService.findById(1L)가 호출되면 새로운 Member 객체를 만들어서 반환하라.
    // findById(1L) <- 파라미터로 1L이 넘어 왔을경우에만 Stub된 동작을 한다. ArgumentMatcher.any()등으로 파라미터 값에 상관없이 Stub도 가능
    when(memberService.findById(1L)).thenReturn(new Member()); 
 
    // 첫번째 호출에는 new Member()를 두번째 호출때는 RuntimeException을 반환하도록 정의
    when(memberService.findByName("kang")).thenReturn(new Member()).thenThrow(new RuntimeException()); 
  ```
  
#### Mock Verify
- 목 객체의 동작을 검증한다.
- ```java
    when(memberService.findById(1L)).thenReturn(new Member()); \
      
    // memberService는 findById(1L)을 한번 호출해야 한다.
    verify(memberService, times(1)).findById(1L);
    // memberService는 findByName("kang")을 호출하지 않아야 한다.
    verify(memberService, never()).findByName("kang");
  ```

   


#### Reference
인프런 강의_더 자바, 애플리케이션을 테스트하는 다양한 방법_백기선