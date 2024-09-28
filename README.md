# spring-instagram-20th

CEOS 20th BE study - instagram clone coding
---

## 2주차 (9/9 - 9/21)

## 📝 서비스 설명, 기능 명세

인스타그램의 핵심 기능 클론 코딩

사진을 포함한 게시글 CRUD, DM 기능을 구현 예정

- 게시글 조회
- 게시글에 사진과 함께 글 작성하기
- 게시글에 댓글 및 대댓글 기능
- 게시글에 좋아요 기능
- 게시글, 댓글, 좋아요 삭제 기능
- 유저 간 1:1 DM 기능

---

## DB 설계

설계한 DB의 ERD는 아래와 같음

![instagram](https://github.com/user-attachments/assets/e41a55e5-ac3f-45a2-bc6e-fac855a94a3e)

### User 엔티티

- `user_id`: 회원 가입시 자동 생성되는 고유 번호 (PK)
- `username`: @'hcae_on' 과 같은 회원 이름
- `nickname`: '이채원' 과 같은 회원 실명
  그 외 password, email, phone 필드 포함

  이메일과 전화번호의 경우, 각 값의 최대 길이를 기준으로 크기 선정

### Profile 엔티티

User와 1:1 관계

User의 상세 정보를 가지고 있음

- `profile_id`: 프로필 생성 시 자동 생성되는 고유 번호 (PK)
- `user_id`: 프로필과 1:1 매칭되는 user의 고유 번호 (FK)
  그 외 링크, 소개, 성별, 프로필 공개 여부, 프로필 사진 파일 경로 등 포함

  성별의 경우 남성, 여성, 그 외로 Enum 타입으로 생성

### Follow 엔티티

User는 여러 팔로우를 가질 수 있음, User와 N:1 관계

- `follow_id`: 팔로우 생성시 자동 생성되는 고유 번호 (PK)
- `user_id`: N:1 매칭되는 user의 고유 번호 (FK)

### Post 엔티티

User는 여러 게시물을 작성할 수 있음, User와 N:1 관계

- `post_id`: 게시물 생성 시 자동 생성되는 고유 번호 (PK)
- `user_id`: N:1 매칭되는 user의 고유 번호 (FK)
  그 외 본문, 등록 시간, 수정 시간, 댓글 허용 여부 등 포함
  댓글 허용 여부의 경우 허용, 비허용, 팔로워 허용 등 Enum 타입으로 생성

### PostLike 엔티티

User는 여러 게시물에 좋아요를 누를 수 있음, User와 N:1 관계

Post는 여러 개의 좋아요를 가질 수 있음, Post와 N:1 관계

- `post_like_id`: 게시물 좋아요 생성 시 자동 생성되는 고유 번호 (PK)
- `post_id`: 좋아요를 한 게시물의 id (FK)
- `user_id`: N:1 매칭되는 user의 고유 번호 (FK)

### Image 엔티티

Post는 여러 사진을 포함할 수 있음, Post와 N:1 관계

- `image_id`: 사진 등록 시 자동 생성되는 고유 번호 (PK)
- `post_id`: 사진을 올린 게시물의 id (FK)
  그 외 사진 파일 경로를 가짐

### Comment 엔티티

Post는 여러 댓글을 포함할 수 있음, Post와 N:1 관계

User는 여러 댓글을 달 수 있음, User와 N:1 관계

- `comment_id`: 댓글 생성 시 자동 생성되는 고유 번호 (PK)
- `user_id`: 댓글을 단 회원의 id (FK)
- `post_id`: 댓글을 단 게시물의 id (FK)
- `parent_id`: 대댓글시 부모 댓글 id
  그 외 본문, 등록 시간, 수정 시간을 가짐

### CommentLike 엔티티

User는 여러 댓글에 좋아요를 누를 수 있음, User와 N:1 관계

Comment는 여러 개의 좋아요를 가질 수 있음, Comment와 N:1 관계

- `comment_like_id`: 댓글 좋아요 생성 시 자동 생성되는 고유 번호 (PK)
- `comment_id`: 좋아요를 한 댓글의 id (FK)
- `user_id`: N:1 매칭되는 user의 고유 번호 (FK)

### ChatParticipant 엔티티

User는 여러 채팅방에 참가할 수 있고, ChatRoom에 여러 유저가 포함될 수 있음

N:M 관계이므로 이를 1:N, N:1 관계로 풀기 위한 중간 엔티티

User는 여러 채팅에 참가할 수 있음, User과 N:1 관계

ChatRoom은 한 명 이상의 여러 채팅 참가자를 포함할 수 있음, N:1 관계

- `chat_participant_id`: 채팅 참가 시 자동 생성되는 고유 번호 (PK)
- `chat_room_id`: 참가하는 채팅방의 id (FK)
- `user_id`: 참가하는 회원의 id (FK)

### ChatRoom 엔티티

여러 ChatParticipant는 한 채팅방에 포함될 수 있음, ChatParticipant와 1:N 관계

- `chat_room_id`: 채팅방 생성 시 자동 생성되는 고유 번호 (PK)
  그 외 채팅방 생성 시간을 포함

### Message 엔티티

User는 여러 메시지를 보낼 수 있음, User와 N:1 관계

ChatRoom에는 여러 메시지가 포함될 수 있음, ChatRoom과 N:1 관계

- `message_id`: 메세지 생성 시 자동 생성되는 고유 번호 (PK)
- `chat_room_id`: 메시지를 보낸 채팅방의 id (FK)
- `user_id`: 메시지를 보낸 회원의 id (FK)
- `parent_message_id`: 특정 메시지 답장 기능을 위한 id
- `reaction`: 메시지에 남길 수 있는 공감 이모지
  그 외 본문, 첨부파일 경로, 전송 시간 등을 포함

## Repository 단위 테스트

위 엔티티 중 FK를 포함하는 Post에 대해서 레포지토리 단위 테스트를 진행

DataJpaTest를 사용할 때, 내장 데이터베이스로 테스트시 변경하는 문제가 있어 아래 어노테이션 추가

```java
@DataJpaTest
// 테스트용 내장 DB로 변경하지 않기 위한 어노테이션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
```

실제 mysql DB에 들어가는지 확인하기 위해 Rollback 옵션을 끄고 진행

```
 @Test
    @Transactional
    @Rollback(false) // mysql에 실제로 들어가는지 확인하기 위한 어노테이션
    @DisplayName("Post 저장 테스트")
```

따라서 테스트 실행 전 쌓인 DB를 지우는 코드를 추가함

```java
  // 테스트 시작 전 쌓인 DB를 지우기
  @BeforeEach
  void cleanUp() {
      postRepository.deleteAll();
  }
```

given, when, then으로 나눠 테스트 코드 작성

Post 객체 생성 전, `user_id`를 FK로 가지고 있어 User 객체 생성 후 저장

게시물 수가 3개인지, 각 게시물 내용이 일치하는지 확인

```java
  //given
        // 사용자 생성 및 저장
        User user = new User();
        user.setUsername("testUser");
        userRepository.save(user);

        // 게시물 3개 생성
        for (int i = 1; i <= 3; i++) {
            Post post = new Post();
            post.setContent("Test Content " + i);
            post.setUser(user);
            post.setCreated_time(LocalDateTime.now());
            post.setEdited_time(LocalDateTime.now());
            post.setComment_option(CommentOption.ENABLED);
            postRepository.save(post);
        }

        // when
        // 저장된 게시물 전부 조회
        List<Post> posts = postRepository.findAll();

        // then
        // 저장된 게시물 개수 검증
        assertThat(posts).hasSize(3);
        // 반복문으로 내용 검증 및 출력
        for (int i = 0; i < posts.size(); i++) {
            assertThat(posts.get(i).getContent()).isEqualTo("Test Content " + (i + 1));
            System.out.println(posts.get(i).getContent());
        }
```

테스트 수행 시 발생하는 JPA 쿼리는 아래와 같음
User, Post 순으로 생성하고 DB에 insert하는 것을 확인

<img width="500" alt="JPA 쿼리" src="https://github.com/user-attachments/assets/7a7f935a-1c14-4309-b4ff-58945acf1a36">

테스트 결과 검증 및 내용 출력 확인

<img width="500" alt="실행 결과" src="https://github.com/user-attachments/assets/d842f900-0476-4f57-b4dc-531161fd8965">

실제 mysql DB에 들어간 데이터 확인

<img width="500" alt="DB" src="https://github.com/user-attachments/assets/8bfe07a0-55d9-4a89-b872-c54e53984170"/>

---

## 3주차 (9/23 - 9/28)

## 🛠️ Refactoring
- 카멜케이스로 변수명 수정

- 시간 관련해서 createdAt, modifiedAt으로 변수명 통일

- Setter 쓰지말고 빌더 패턴 사용하기

  ```java
      @Builder
    public User(Long id, String username, String nickname, String password, String email, String phone) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }
  ```

- 생성자 종류 조사 후 적용하기 
  - `@NoArgsConstructor` : 파라미터가 없는 디폴트 생성자를 생성
    - `Access level = protected` 사용하는게 좋음
  - `@AllArgsConstructor` : 모든 필드 값을 파라미터로 받는 생성자를 생성
  - `@RequiredArgsConstructor` : final이나 @NonNull으로 선언된 필드만을 파라미터로 받는 생성자를 생성
  - [레퍼런스](https://velog.io/@code-10/%EB%A1%AC%EB%B3%B5-AllNoArgsConstructor-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EC%95%8C%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EC%9E%90)

- 시간 관련 필드 형식 변경
  - `LocalDateTime` + 어노테이션 `@CreatedDate`, `@LastModifiedDate`
  - `LocalTime`은 날짜 정보를 포함하지 않아서 특정 날짜와 연결된 시간 정보를 저장하는 데 적합하지 않음
	- 아래 어노테이션을 사용해서 JPA가 엔티티 변경을 자동 감지함		      ```@EntityListeners(AuditingEntityListener.class) // @CreatedDate를 위한 어노테이션```
  - `@CreatedDate`: 엔티티 생성 시 특정 필드를 자동으로 데이터베이스에 매핑해주기 위해 사용
	- `@LastModifiedDate`: 엔티티 최종 수정 날짜를 자동으로 데이터베이스에 매핑해주기 위해 사용
	- [레퍼런스](https://velog.io/@ogu1208/JPA-createdAt-updatedAt%EC%9E%90%EB%8F%99-%EC%9E%85%EB%A0%A5 )

- 필수 칼럼은 not null로 수정, nullable과 not null의 차이 조사
    - `column(nullable = false)`
        - 칼럼을 Notnull로 설정하는 것, DDL 쿼리가 나갈 때 NULL 제약 조건 걸어줌
        - 서비스 로직에서 엔티티에 NULL을 삽입하는 것은 에러를 발생시키지 않음
    - `@NotNull`
        - 런타임 시 엔티티에 NULL이 들어오는지 확인
        - IllegalArgumentException을 던짐, 테이블 생성 시 칼럼에 NOT NULL 조건을 걸어 놓음
    - `@NonNull`
        - Lombok에서 제공하는 어노테이션
        - 애플리케이션 레벨에서의 NULL 확인, 칼럼에 NOT NULL을 지정하는 역할로는 사용 불가
    - 결론은 @NotNull을 사용하자
		- [레퍼런스](https://hereishyun.tistory.com/166)

- application.yml ignore에 넣지말고 환경변수 처리하기
  - 최상단.env 파일에 환경 변수 생성
  - application.yml 수정
  ```
  spring:
  # env import
  config:
    import: optional:file:.env[.properties]
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
  ```
- Jparepository를 사용하는 경우 @Repository 어노테이션을 사용하지 않아도 되므로 삭제

---

## 🧃 Service 계층 구현
기본적인 CRUD 기능 구현

DTO를 통해 Repository와 Service 계층간 정보 전달

### UserService
- `register`
  - 이메일 중복 체크, username 중복 체크
  - `BCryptPasswordEncoder` 사용한 비밀번호 암호화
- `login`
  - username으로 사용자 조회
  - password 검사 후 로그인

### ProfileService
- `createprofile`: 유효한 userId에 대해 프로필 생성
- `getProfile`: 특정 userId의 프로필 조회
- `updateProfile`: 프로필 필드 부분 업데이트 가능

### FollowService
- `createFollow`: 유효한 followerId와 followingId로 팔로우 생성
- `deleteFollow`: 팔로우 ID로 기존 팔로우 삭제
- `deleteFollower`: followerId와 followingId로 팔로우 관계 삭제
- `getFollowers`: 특정 사용자 ID로 팔로워 목록 조회
- `getFollowings`: 특정 사용자 ID로 팔로잉 목록 조회

### PostService
- `createPost`: 유효한 userId로 포스트 생성 및 `PostResponseDto` 반환
- `getPost`: 특정 포스트 ID로 포스트 조회 후 `PostResponseDto` 반환
- `updatePost`: 포스트 ID로 기존 포스트 업데이트 후 `PostResponseDto` 반환
- `deletePost`: 포스트 ID로 기존 포스트 삭제
  
### PostLikeService
- `createPostLike`: 유효한 userId로 포스트에 좋아요 생성 후 `PostLikeResponseDto` 반환
- `deletePostLike`: 좋아요 ID로 기존 좋아요 삭제

### CommentService
- `createComment`: 유효한 postId와 userId로 댓글 생성 후 `CommentResponseDto` 반환
- `getComment`: 댓글 ID로 댓글 조회 후 `CommentResponseDto` 반환
- `getReplies`: 부모 댓글 ID로 대댓글 목록 조회
- `updateComment`: 댓글 ID로 기존 댓글 업데이트 후 `CommentResponseDto` 반환
- `deleteComment`: 댓글 ID로 기존 댓글 삭제

### CommentLikeService
- `createCommentLike`: 유효한 userId로 댓글에 좋아요 생성 후 `CommentLikeResponseDto` 반환
- `deleteCommentLike`: 좋아요 ID로 기존 댓글 좋아요 삭제

### ChatRoomService
- `createAndJoinChatRoom`: 새로운 채팅방 생성 및 사용자 참가
- `getChatRoomsByUserId`: 특정 사용자 ID로 참가 중인 채팅방 리스트 조회
- `getParticipantsByChatRoomId`: 특정 채팅방 ID로 참가자 리스트 조회

### MessageService
- `createMessage`: 새로운 메시지 생성
- `getMessageById`: 특정 메시지 ID로 메시지 조회
- `getMessagesByChatRoomId`: 특정 채팅방 ID로 모든 메시지 조회

## Repsotiory 계층 단위 테스트
N+1 문제를 확인하기 위해 `PostRepository`의 단위 테스트를 진행하였다.

**N+1 문제란?**
User는 여러 Post를 작성할 수 있다.  

전체 Post를 조회하는 하나의 쿼리를 날린 상황에서, 

N명의 Post와 연관 관계가 있는 최대 N개의 User에 대한 조회 쿼리가 날아간다.

내가 원하는 건 1개의 쿼리인데, 불필요한 N개의 쿼리가 추가로 나가는 상황 (1+N)이다. 

1+N 문제는 즉시 로딩, 지연 로딩에 상관 없이 발생하는 문제이다. 

성능에 좋지 않은 영향을 주기 때문에 해소할 필요가 있다.


이 문제를 해결하기 위해서는 `fetchJoin`, `@EntityGraph`를 사용할 수 있다.

그 중 fetchJoin을 사용하여 테스트를 해보았다.

아래는 N+1 문제를 확인하기 위한 테스트 코드이다.

게시물 전체를 조회했을 때 User를 조회하는 쿼리가 별도로 나가는지 확인해보겠다.
```java
    @Test
    @DisplayName("N+1 문제 테스트")
    public void nPlusOneProblemTest() {
        // when
        List<Post> posts = postRepository.findAll();

        // 이 시점에서 User에 접근하여 N+1 문제를 유발
        for (Post post : posts) {
            String userEmail = post.getUser().getEmail(); // 이 부분에서 User 정보를 가져옴
            System.out.println("User Email: " + userEmail); // User 정보를 출력
        }

        // then
        assertThat(posts).hasSize(2);
    }
```
아래 결과를 확인하면
post 조회와 User 조회가 따로 나가는 것을 확인할 수 있다.
``` sql
select
    p1_0.post_id,
    p1_0.comment_option,
    p1_0.content,
    p1_0.created_at,
    p1_0.modified_at,
    p1_0.user_id 
from
    post p1_0
User Email: test@example.com
User Email: test2@example.com
```

이를 해결하기 위해서 
`PostRepository` 에 fetchjoin을 통해 Post를 조회할 때 User를 함께 조회하는 메서드를 추가했다.
```javapublic interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.user")
    List<Post> findAllWithUsers();

}
```
`nPlusOneProblemTest`와 달리 Fetch join을 사용한 findAllWithUsers() 메서드로 Post 전체를 조회했다.

```
@Test
  @DisplayName("N+1 문제 해결 테스트")
  public void nPlusOneSolveTest() {
      // when
      List<Post> posts = postRepository.findAllWithUsers();

      // 다른 코드는 동일하여 생략 ...
  }
```

그 결과 Post 조회 시 User도 함께 조회되는 것을 확인할 수 있다.
이를 통해 N+1 문제를 해소 할 수 있다.

```sql
Hibernate: 
    select
        p1_0.post_id,
        p1_0.comment_option,
        p1_0.content,
        p1_0.created_at,
        p1_0.modified_at,
        u1_0.user_id,
        u1_0.email,
        u1_0.nickname,
        u1_0.password,
        u1_0.phone,
        u1_0.username 
    from
        post p1_0 
    join
        user u1_0 
            on u1_0.user_id=p1_0.user_id
User Email: test@example.com
User Email: test2@example.com
```

## Service 계층 단위 테스트
시간 관계상 모든 Service의 단위 테스트를 진행하지는 못했다.

`UserService`의 단위 테스트

given, when, then으로 나누어 작성하였다.

암호화를 위한 Encoder를 등록해두었는데, test 단에서 사용을 못하길래 확인해보았더니

DataJpaTest는 기본적으로 JPA 관련 구성만 로드하기 때문에 직접 등록한 Config는 포함되지 않을 수 있다고 한다.

Import로 등록한 Encoder를 가져오고, BeforeEach를 통해 매 테스트 전 주입했다.
```
@Import(EncoderConfig.class)
class UserServiceTest {
    ....
    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder); // PasswordEncoder 주입
    }
  ....
}
```

이메일 중복 가입에 대한 예외처리 테스트와 회원가입 테스트를 진행했다.

**이메일 중복 체크 예외처리 테스트**
-  이미 등록된 이메일로 회원가입 요청을 시도할 때, 예외가 발생하는지 확인
-  requestDto에 필요한 값을 넣고, 이메일 중복 상황을 설정
-  서비스의 register 메서드를 호출하여 예외 발생 여부를 검증

**회원가입 테스트**
-  새 회원가입 요청을 위해 requestDto에 사용자 정보를 입력
-  register 메서드를 호출하여 정상적으로 데이터베이스에 저장되는지를 확인


아래는 전체 코드이다.
```java
package com.ceos20.instagram.user.service;

import com.ceos20.instagram.config.EncoderConfig;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserRegisterRequestDto;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(EncoderConfig.class)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화용 encoder

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder); // PasswordEncoder 주입
    }

    @Test
    @DisplayName("이미 사용 중인 이메일로 가입 요청 시 예외처리")
    void emailExceptionTest() {
        // given
        // 이미 등록된 이메일이 있는 상황 설정
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto("testUser", "Test Nickname", "password", "test@gmail.com", "010-1234-5678");
        userRepository.save(User.builder()
                .email("test@gmail.com")
                .username("anotherUser")
                .password(passwordEncoder.encode("password")) // 암호화된 비밀번호 저장
                .build());

        // when & then
        // 이메일 중복 체크 시 예외 발생
        assertThatThrownBy(() -> userService.register(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("회원가입 테스트")
    void registerTest() {
        // given
        // 회원 가입 요청 데이터 설정
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto("newUser", "New Nickname", "password", "newuser@gmail.com", "010-5678-1234");

        // when
        // 회원 가입 요청 처리
        userService.register(requestDto);

        // then
        // 이메일이 데이터베이스에 저장되었는지 검증
        assertTrue(userRepository.existsByEmail("newuser@gmail.com"));
    }
}
```

