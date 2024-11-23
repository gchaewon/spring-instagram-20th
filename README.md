# spring-instagram-20th

CEOS 20th BE study - instagram clone coding

## 목차
- [2주차 DB 설계, 도메인, 레포지토리 개발](#2주차-99---921)
- [3주차 서비스 개발](#3주차-923---928)
- [4주차 API 개발](#4주차-930---112)
- [5주차 JWT토큰 기반 유저인증](#5주차-114---119)
- [6주차 Docker](#6주차-1111---1116)
- [7주차 Deploy](#7주차-1118---1123)

  
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

## 4주차 (9/30 - 11/2)

## 🛠️ Refactoring

서비스 구현을 완료하고, API 개발에 앞서서 지금까지 코드를 다시 돌아보고 리팩토링했다.

리팩토링에서 중점적으로 둔 요소는 아래와 같다.  그 외에도 컨트롤러 개발하면서 엄청 자잘한 수정을 많이 했다.

**Entity**

- BaseTimeEntity 적용
  
**DTO**

- 불필요한 DTO 삭제
- DTO에 정적 팩토리 메서드 적용하기
- 응답에 필요한 ResponseDTO 적절하게 만들기

**Service**

- Service에서 Transactional(readonly=true) 전역 설정
- GlobalException 적용하기
- PostService 게시글 수정 중 이미지 관련 역할 분리

---

### Entity Refactoring

저번 코드 리뷰에서 시간 관련 필드를 묶어서 BaseTimeEntity로 만들고 이를 전역에서 적용해보라는 피드백이 있었다.

그래서 global 패키지에 BaseTimeEntity 클래스를 생성하고, Entity에서 상속 받아 사용했다.

⭐️필드가 비는 오류가 있었는데, Application에서 `@EnableJpaAuditing`을 붙여야 제대로 동작한다.

**BaseTimeEntity**

```java
...
@Getter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public abstract class BaseTimeEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
```

---

### DTO Refactoring
실제 Controller 개발로 가니, 불필요한 DTO가 많았다.

ResponseDTO로 적합하지 못한 것들이 많아서, 추가 및 수정을 거쳤다.

리팩토링에 앞서서 DTO에 대해 더 찾아봤다.

**DTO에 어디까지 포함해야는가?**

DTO를 사용하는 이유 ==  불필요한 데이터를 노출하지 않고 필요한 데이터만 내보내기 위함

따라서 비즈니스 로직에서 필요하지 않은 데이터, 노출할 필요가 없는 데이터는 DTO에 포함하지 않아야한다.

**DTO는 어디까지 사용할 수 있는가?**

DTO를 Repository에서 사용하는 것은 지양하는 것이 좋다. 

Repository를 Entity의 영속성을 관장해야하기 떄문에, DTO - Entity 사이의 변환 로직은 다른 계층에서 진행하는 것이 적합하다.

그렇다면 Controller와 Service 레이어 중 어느 곳이 적합할까?

**Controller에서 Entity - DTO 변환을 할 때 단점**

1. Entity의 불필요한 데이터가 Controller까지 넘어오게 됨
2. 여러 domain을 조회하는 경우, 하나의 controller가 의존하는 서비스가 많아짐
3. 여러 doamin을 조합해서 DTO를 생성하는 경우, service가 controller에 포함됨

이러한 단점이 있기 때문에, Service 레이어에 변환 로직을 포함하는 것이 좋다.

### DTO에 정적 팩토리 메서드 적용하기

서비스 레이이어에서 DTO - Entity간 변환시 정적 팩토리 메서드를 적용했다.

**정적 팩토리 메서드란?** 

정적 팩토리 메서드는 객체 생성 역할을 하는 클래스 메서드이다. 일반 생성자 대신 정적 팩토리 메서드를 사용해야하는 이유가 뭘까?

**일반 생성자와 차이점**

**1. 이름을 가질 수 있음**

정적 팩토리 메서드를 사용하면 메서드 이름에 객체 생성 목적을 담을 수 있다. 

매개변수만으로는 생성자에서 반환되는 객체 특성을 제대로 표현하기 어렵다.

**일반 생성자 new 사용**

```java
public static void main(String[] args) {
    // 검정색 테슬라 자동차 
    Car teslaCar = new Car("Tesla");

    // 빨간색 BMW 자동차
    Car bmwRedCar = new Car("BMW", "Red");
}
```

**정적 팩토리 메서드 적용**

정적 팩토리 메서드를 사용하는 경우 생성자 이름을 통해 그 목적을 잘 표현할 수 있다.

기본 생성자는 private으로 Class 안에서만 접근할 수 있게 변경한다.

```java
class Car {
	...
	// 정적 팩토리 메서드
	public static Car brandBlackFrom(String brand) {
	    return new Car(brand, "black");
	}
	
	// 정적 팩토리 메서드
	public static Car brandColorOf(String brand, String color) {
	    return new Car(brand, color);
	}
}
```

**정적 팩토리 메서드의 네이밍 컨벤션** 

- `from`: 하나의 객체 (다른 타입)에서 데이터를 가져와 새 객체를 만들 때 사용
- `of`: 주로 여러 매개 변수를 받아 객체를 생성할 때 사용

Entity -> DTO로 변환할 때는 관례적으로 `from`을 사용한다.

DTO -> Entity를 변환할 때는 `toEntity` 를 사용한다. 

- 정적 팩토리 메서드 네이밍 규칙에는 없지만 변환 로직 의미를 잘 나타내기 때문에 `toEntity`로 네이밍

```java
public class ProfileRequestDto {
		// ... 필드
    public static Profile toEntity(ProfileRequestDto dto, User user) {
        return Profile.builder()
                .user(user) // 프로필을 만든 사용자
                .link(dto.getLink())
                .introduce(dto.getIntroduce())
                .gender(dto.getGender())
                .publicOption(dto.getPublicOption())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
    }
}

public class ProfileResponseDto {
		// ... 필드
    public static ProfileResponseDto from(Profile profile) {
        return ProfileResponseDto.builder()
                .id(profile.getId())
                .link(profile.getLink())
                .introduce(profile.getIntroduce())
                .gender(profile.getGender())
                .publicOption(profile.getPublicOption())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
```

---

### Service Refactoring

Controller 개발을 같이 하면서 검증 로직이 추가로 필요한 경우가 많았다.

PostService의 수정 메서드처럼, 여러 책임을 가지고 있는 경우 분리하는 과정을 거쳤다.

**@Transactional(readOnly = true) 전역 설정 이유?**

서비스 코드 수정 전에, 저번 코드 피드백 중에 있었던 `@Transactional(readOnly = true)`  전역 설정을 먼저 했다.

아래 코드처럼 Service 클래스 전체 readOnly 옵션을 주고, 실제 DB 변경이 있는 메서드에만 

`@Transactional` 를 적용하면 된다.

```java
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    // 포스트 생성 메서드
    @Transactional
		public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
			....
		}
		....
}
```

`@Transactional(readOnly = true)` 를 전역 설정하는 경우 아래와 같은 이점이 있다.

- 일관된 트랜잭션
    
    서비스 클래스 메서드에 개별적으로 적용할 필요가 없어 유지 보수가 쉬워진다.
    
- 불필요한 변경 방지
    
    메서드 내 실수로 데이터 작업 변경이 시도될 경우, 전역 설정을 통해 오류를 발생시켜 데이터 무결성을 유지할 수 있다.
    

---

### GlobalException 적용

Global Exception은 코드 중복을 줄이고 일관된 에러 응답 형식을 제공하기 위해 사용한다.

이를 통해 예외처리 로직을 분리하여, 유지보수성을 향상시킬 수 있다.

### GlobalExceptionHandler

 GlobalExceptionHandler를 사용하면, 매번 예외처리 로직 작성을 하지 않아도된다. 

일관된 응답, 유지보수성 향상 등의 장점이 있다.

`@RestControllerAdvice`:  `@ControllerAdvice`+ `@ResponseBody` 가 합쳐진 어노테이션이다.

**ErrorResponse**

먼저 예외 시 Response 틀이 되어줄 ErrorResponse 생성했다.

예외 발생 시간, 상태, 메시지 포함하여 아래와 같은 구성으로 만들었다.

```java
@Getter

public class ErrorResponse {
		private final LocalDateTime timestamp = LocalDateTime.now();
		private final int statusCode;
		private final String error;
		private final String message;
	
		public ErrorResponse(ErrorCode errorCode) {
			this.statusCode = errorCode.getHttpStatus().value();
			this.error = errorCode.getHttpStatus().name();
			this.message = errorCode.getMessage();
		}
}
```

**RestApiException**

직접 정의한 예외를 다룰 때 사용할 RuntimeException 하위 클래스로 ErrorCode 를 통해 상태코드, 메시지를 담는다.

```java
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {
	private final ErrorCode errorCode;
}
```

**ErrorCode**

어떤 종류의 예외가 발생했는지에 대한 정보를 enum 타입으로 정의했다.

```java
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 인증 실패
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),

    // 권한 없음
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 리소스 충돌
    CONFLICT(HttpStatus.CONFLICT, "이미 생성된 리소스입니다."),

    // 리소스 찾을 수 없음
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

```

**GlobalExceptionHandler**

위 파일들을 다 만들고 나서, 이를 바탕으로 전역예외처리 handler 만들었다.

```java
...
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // CustomException에 존재하는 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        // ErrorCode에서 상태 코드와 메시지를 가져와 ErrorResponse 생성 후 응답
        ErrorResponse response = new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
```

### **CustomException**

서비스에서 자주 사용하는 조회 실패시 예외처리를 CustomException을 통해 global하게 만들 수 있다.

RuntimeException을 상속 받아 CustomException 클래스를 정의한다.

나는 예외 정보를 받아서, 그에 맞는 errorcode와 메시지, 타겟 정보를 반환하는 방식으로 구성하였다.

```java
...
@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    // ErrorCode 받아서 예외 메시지 저장
    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    // 에러 추가 정보를 받아 저장
    public CustomException(ErrorCode errorCode, String message, Object target) {
        super(String.format("%s - %s", message, target)); // 메시지에 타겟 정보를 포함
        this.errorCode = errorCode;
    }
}

```

이후 서비스에서 해당 exception을 사용하면 된다.

```java
// userId로 사용자 조회
User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

```

---

## ☕️ Controller 개발

4주차 과제인 REST API 만들기를 진행했다.

아직 유저 인증 부분을 하지 않았기 때문에 유저 관련 기능은 만들지 못했다.

`Swagger`를 통해서 효율적으로 API 문서화, 테스트를 할 수 있었다.

먼저 개발하기 전에 API 명세서를 작성해서, URI와 메서드 방식 등을 미리 정리했다.

<img width="500" src= "https://github.com/user-attachments/assets/bd9bcd3e-3d4f-470b-ae37-7f010d6fb32a">
### Swagger 연동

**Swagger란?**

OepnAPI를 중심으로 REST API 설계, 빌드, 문서화할 수 있는 오픈 소스

문서 화면 UI에서 바로 API 테스트가 가능하다.

1. **Configuration 추가**

swagger를 사용하려면 먼저 configuration을 추가해야한다. 

```java
....

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    // API 정보
    private Info apiInfo() {
        return new Info()
                .title("Instagram API") // API 제목 
                .description("Instagram API Doc") // 설명
                .version("1.0.0"); // API 버전
    }
}
```

1. **어노테이션 정리**

`Swagger`를 적용하기 전에 자주 사용하는 어노테이션을 정리했다.

- **@Tag(name=“카테고리 명”, description =“카테고리 설명”)**
    
    API 엔드포인트에 태그를 할당하고, 태그를 기반으로 엔드포인트 그룹화 -> 문서 내부에서 카테고리 생성
    
    ```java
    @Tag(name = "Post Controller", description = "게시글 컨트롤러 \n 작성, 수정, 삭제, 조회 로직을 포함합니다.")
    public class PostController {
    	....
    }
    ```
    
- **@Operation(summary=“작업 요약”, description=“작업 구체적 설명”)**
    
    API 엔드 포인트 작업에 대한 설명 추가, 세부 정보 제공
    
- **@ApiResponse(responseCode = “200”,  description = “조회 성공”)**
    
    API 응답, 설명, 상태 코드 정의
    
- **@Schema(description = “속성 설명”, example = “예시 값 정의”)**
    
    API 모델 속성 정의 및 문서화에 사용 (명세서 작성 기반이 됨)
    
    직접 DTO 클래스에서 @Schema 부분을 작성하면 API 명세서 작성을 자동으로 해준다. 
    
    ```java
       // 게시글 작성
        @Operation(summary = "게시글 작성")
        @PostMapping("/posts")
        @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 작성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 유저의 요청입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseTemplate.class))
            )
        })
        public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto){
    				....
        }
    ```
    

이런식으로 [localhost:8080/swagger-ui/index-html](http://localhost:8080/swagger-ui/index.html) 접속 후 API 테스트와 문서화된 API를 확인할 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/90976a55-3695-47fd-bf94-f967e3a73b9d">

Try it out 버튼을 누르면 실제 API 테스트가 가능하다.

<img width="500" src="https://github.com/user-attachments/assets/63ff963b-a5f0-40e2-8b11-963b5ca1afb2">

아래는 API 중 글과 관련된 API에 대한 상세 설명과 테스트 결과이다.

### **Create API**

**게시글 작성 API**

로그인한 유저의 정보를 받아오는 부분은 고정 값으로 설정했고 추후 수정할 예정이다.

```java
  // 게시글 작성
  @Operation(summary = "게시글 작성")
  @PostMapping("/posts")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "게시글 작성 성공",
              content = @Content(mediaType = "application/json",
                      schema = @Schema(implementation = PostResponseDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "존재하지 않는 유저의 요청입니다.",
              content = @Content(mediaType = "application/json",
                      schema = @Schema(implementation = ResponseTemplate.class))
      )
  })
  public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto){
      // 현재 유저의 id 값을 가져오는 코드로 수정 예정
      Long userId = 180L;
      PostResponseDto responseDto = postService.createPost(userId, requestDto);
      return ResponseEntity.ok(responseDto);
  }
```

**API와 대응되는 서비스 코드**

```java
// 포스트 생성 메서드
@Transactional
public PostResponseDto createPost(Long userId, PostRequestDto requestDto) {
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

    // dto -> entity 변환
    Post post = requestDto.toEntity(requestDto, user);

    // 포스트 저장
    Post savedPost = postRepository.save(post);

    // 이미지 생성 및 저장
    List<Image> images = imageService.createImages(requestDto.getImageUrls(), savedPost);

    return PostResponseDto.from(savedPost, images); // response DTO 반환
}
```

**API 테스트 결과**

Response가 좀 지저분해서, ResponseDTO를 수정해서 다음주까지 리팩토링하려고한다.

<img width="500" src="https://github.com/user-attachments/assets/33d080bf-8581-472f-8b59-a014b7898a7a">

### **Get API**

**특정 게시글 세부 조회 API**

게시글 고유 번호를 받아서 조회한다.

```java
  // 게시글 조회
  @Operation(summary = "게시글 조회")
  @GetMapping("/posts/{postId}")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "게시글 조회 성공",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = PostResponseDto.class))
          ),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ResponseTemplate.class))
          )
  })
  public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId){
      PostResponseDto responseDto = postService.getPost(postId);
      return ResponseEntity.ok(responseDto);
  }
```

**API와 대응되는 서비스 코드**

```java
// 포스트 조회 메서드
public PostResponseDto getPost(Long postId) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

    // 포스트 아이디와 맵핑되는 이미지 리스트
    List<Image> images = imageService.getImagesByPostId(postId);

    return PostResponseDto.from(post, images); // response DTO 반환
}
```

**API 테스트 결과**

<img width="500" src="https://github.com/user-attachments/assets/16e45f9e-9c6b-495b-be80-6af3709bfe79"/>

**유저의 게시글 전체 조회하는 API** 

```java
  // 특정 유저의 게시글 목록 조회
  @Operation(summary = "특정 유저의 게시글 전체 조회")
  @GetMapping("/users/{userId}/posts")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "특정 유저의 전체 게시글 조회 성공",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = PostResponseDto.class))
          ),
          @ApiResponse(responseCode = "404", description = "작성한 글이 없습니다.",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ResponseTemplate.class))
          )
  })
  public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable Long userId){
      List<PostResponseDto> posts = postService.getPostsByUserId(userId);
      return ResponseEntity.ok(posts);
  }
```

**API와 대응되는 서비스 코드**

이미지 조회에 대한 부분을 이미지 서비스로 분리하여 처리하였다.

```java
// 특정 유저의 포스트 전체 조회 메서드
public List<PostResponseDto> getPostsByUserId(Long userId){
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

    // 유저의 모든 포스트 조회
    List<Post> posts = postRepository.findByUserId(userId);

    // 조회한 포스트 ID 리스트
    List<Long> postIds = posts.stream()
            .map(Post::getId)
            .collect(Collectors.toList());

    // 조회한 포스트의 이미지 전체 리스트
    List<List<Image>> images = imageService.getImagesByPostIds(
            posts.stream().map(Post::getId).collect(Collectors.toList()));

    // 조회한 포스트와 이미지로 response DTO 생성하기
    List<PostResponseDto> postResponseDtos = new ArrayList<>();
    for(int i=0; i<posts.size(); i++){
        postResponseDtos.add(PostResponseDto.from(posts.get(i), images.get(i)));
    }
    return postResponseDtos;
}
```

**API 테스트 결과**

<img width="500" src="https://github.com/user-attachments/assets/1cbc172f-fab8-48bc-bd8c-1b023a7a9608"/>

### Delete API

**게시글 삭제 API**

삭제 API에서는 requestBody 없이 구성했고, response도 주지 않아서 204로 설정해두었다.
```java
  // 게시글 삭제
  @Operation(summary = "게시글 삭제")
  @DeleteMapping("/posts/{postId}")
  @ApiResponses({
          // 반환할 데이터 없음
          @ApiResponse(responseCode = "204", description = "게시글 삭제 성공")
  })
  public ResponseEntity<Void> deletePost(@PathVariable Long postId){
      // 현재 유저의 id 값을 가져오는 코드로 수정 예정
      Long userId = 181L;

      postService.deletePost(postId, userId);
      return ResponseEntity.noContent().build();
  }
```

**API와 대응되는 서비스코드**

요청 유저와 글 작성 유저를 비교하여 권한 확인을 했다.

글과 연결된 이미지도 삭제 되어야하므로, 이미지 삭제 호출 후 게시글 삭제하는 로직으로 구성했다.

```java
  // 포스트 삭제 메서드
  @Transactional
  public void deletePost(Long postId, Long userId) {
      // 포스트 아이디로 조회
      Post targetPost = postRepository.findById(postId)
              .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

      // 삭제 요청 유저와 포스트 작성 유저가 같은지 확인
      Long writerId = targetPost.getUser().getId();
      if (!writerId.equals(userId)) {
          throw new CustomException(ErrorCode.UNAUTHORIZED, "포스트를 삭제할 권한이 없습니다.", userId);
      }

      // 관련 이미지 삭제
      imageService.deleteImagesByPostId(postId);

      // 포스트 삭제
      postRepository.delete(targetPost);
  }
```

**API 테스트 결과**

<img width="500" src="https://github.com/user-attachments/assets/c8891c04-7b68-43b3-8a8d-5e740ee9129a">

권한이 없는 유저가 삭제 요청 시 

<img width="500" src="https://github.com/user-attachments/assets/db832899-5936-4bd5-b196-86647ac8b1cc"/>

### Update API

게시글 수정의 경우, 필드 일부분 수정이 가능한 형태이므로 Patch 방식으로 설정했다.

인스타그램 상으로 게시글의 이미지는 삭제만 가능하기 때문에, 이미지를 추가하는 로직은 넣지 않았다.

**게시글 수정 API**

```java
  // 게시글 수정
  @Operation(summary = "게시글 수정")
  @PatchMapping("/posts/{postId}") // 필드 일부 수정 가능
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "게시글 수정 성공",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = PostResponseDto.class))
          ),
          @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글입니다.",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ResponseTemplate.class))
          )
  })
  public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId,
                                                    @RequestBody PostUpdateRequestDto requestDto){
      // 현재 유저의 id 값을 가져오는 코드로 수정 예정
      Long userId = 181L;

      PostResponseDto responseDto = postService.updatePost(postId, userId, requestDto);
      return ResponseEntity.ok(responseDto);
  }
```

**API와 대응되는 서비스 코드**

삭제와 마찬가지로 수정 요청 유저와 작성 유저가 같은지 검증했다.

필드 일부분 수정 로직과 이미지는 삭제 로직만 포함하였다.

```java
// 포스트 수정 메서드
@Transactional
public PostResponseDto updatePost(Long postId, Long userId, PostUpdateRequestDto requestDto) {
    // 포스트 아이디로 조회
    Post targetPost = postRepository.findById(postId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));

    // 수정 요청 유저와 포스트 작성 유저가 같은지 확인
    Long writerId = targetPost.getUser().getId();
    if (!writerId.equals(userId)) {
        throw new CustomException(ErrorCode.UNAUTHORIZED, "포스트를 수정할 권한이 없습니다.", userId);
    }

    // 업데이트 요청 반영한 포스트 객체 생성
    Post updatedPost = Post.builder()
            .id(targetPost.getId())
            .user(targetPost.getUser())
            .content(requestDto.getContent() != null ? requestDto.getContent() : targetPost.getContent()) // content 업데이트
            .commentOption(requestDto.getCommentOption() != null ? requestDto.getCommentOption() : targetPost.getCommentOption()) // 댓글 옵션 업데이트
            .build();

    // 포스트 저장
    Post savedPost = postRepository.save(updatedPost);

    // 이미지 서비스 호출하여 이미지 삭제
    if (requestDto.getImageIdList() != null) { // 삭제할 이미지가 있을 때만 수행
        imageService.deleteImagesByIds(requestDto.getImageIdList());
    }
    // 이미지 객체 리스트 생성
    List<Image> images = imageService.getImagesByPostId(postId);

    return PostResponseDto.from(savedPost, images);
}
```

**API 테스트 결과**

<img width="500" src="https://github.com/user-attachments/assets/1e1e5b61-097b-4195-a63a-cd33d2ab3b9a"/>

권한이 없는 유저가 삭제 요청시

<img width="500" src="https://github.com/user-attachments/assets/ea494dcf-6bae-428e-9ee1-dc04849e9adc"/>

---

## 레퍼런스
- [DTO](https://stella-ul.tistory.com/entry/DTO%EC%9D%98-%EC%82%AC%EC%9A%A9%EB%B2%94%EC%9C%84%EB%8A%94-%EC%96%B4%EB%94%94%EA%B9%8C%EC%A7%80-%EB%98%90-DTO-%EB%B3%80%ED%99%98%EC%9D%80-%EC%96%B4%EB%94%94%EC%84%9C)
- [정적 팩토리 메서드](https://newbie-in-softengineering.tistory.com/entry/%EC%8A%A4%ED%84%B0%EB%94%94-%EC%A0%95%EC%A0%81-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EB%A9%94%EC%84%9C%EB%93%9CStatic-Factory-Method)
- [GlobalException](https://ngwdeveloper.tistory.com/156)
- [Swagger1](https://woo-chang.tistory.com/80)
- [Swagger2](https://hoons-dev.tistory.com/127)

--- 

## 5주차 (11/4 - 11/9)

## 🛠️ Refactoring

### Response Template 통일

ResponseTemplate을 통일하고, data 부분에 DTO를 넣어서 보내는 구조로 통일했다.

몇 API 중에서는 DTO를 따로 만들지 않고 바로 보내거나 데이터가 없는 경우가 있어서, 제너릭 타입으로 만들었다.

돌려 보낼 data가 없는 경우 (responseDTO가 없음) data 필드가 null로 가는 문제가 있었다.

`@JsonInclude(JsonInclude.Include.NON_NULL)` 옵션을 줘서 null인 필드는 제외하는 방식으로 해결했다.

```json
{
    "status": 200,
    "success": true,
    "message": "로그인 성공",
    "data": {
        "userId": 201,
        "username": "testuser",
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImF1dGgiOiIiLCJleHAiOjE3MzExMzc5NTR9.0HkUQZ6vsgXMgKzAdwJzkdg0k5cKHgGtEyNvCBSzUCI",
        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MzExMzc5NTR9.A52cbEHErV-f8tU57HCsxLHHelTFoi9wqhCb0f94ypA"
    }
}
```

```java
package com.ceos20.instagram.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL) // null 값을 가진 필드를 제외
@Builder
public class ResponseTemplate<T> {
    public int status;
    public boolean success;
    public String message;
    public T data;

    public static <T> ResponseEntity<ResponseTemplate<T>> createTemplate(HttpStatus status, String message, T data) {
        ResponseTemplate<T> responseTemplate = ResponseTemplate.<T>builder()
                .status(status.value())
                .success(true)
                .message(message)
                .data(data)
                .build();

        return ResponseEntity
                .status(status)
                .body(responseTemplate);
    }
}

```

---
## 인증 방식 조사 

### Cookie & Session

**등장 배경**

HTTP 프로토콜은 Stateless, Connectionless 라는 특징을 가지고 있음.

이러한 특징은 서버에 상태 정보를 저장하지 않아 서버 자원을 절약할 수 있다는 장점이 있음

하지만 사용자를 식별할 수 없어서 같은 사용자임에도 매번 사용자를 다르게 인식하는 문제점이 있고 

이를 보완하기 위해 나온 것이 Cookie와 Session

**Cookie**

**Client-Stateful** 방식, **사용자 웹 브라우저 (Client)**에 서버 대신 필요한 상태 정보를 저장하는 방법

클라이언트 요청 후 서버에서 발급한 정보를 클라이언트에서 저장하고, 요청 마다 함께 보내서 사용

**동작 과정**

서버가 저장하고자 하는 정보를 Set-Cookie라는 Header 속성으로 응답에 포함하여 돌려줌 

→ 사용자 웹 브라우저가 Cookie에 해당 정보를 저장하고 요청 시 함께 보내는 구조

**보안상 문제점**

- Cookie는 Javascript를 통해서 탈취가 가능함
    - HTTP-Only 옵션을 사용해서 XSS (js를 통한 스크립트 공격)을 막을 수 있음
- server → client cookie 정보 전달 과정에서 sniffing 위험이 있음
    - Secure 옵션을 통해 HTTPS를 통한 통신에만 쿠키를 보내게해서 sniffing 방지 가능

---

**Session**

쿠키와 다르게 **Server-Stateful** 방식으로 **서버 측에 인증 정보를 저장**하는 방식

서버는 클라이언트 로그인 요청에 대한 응답 시 ****인증 정보는 서버에 저장, 사용자 식별자인 **session_id를 쿠키에 담아서 전송** 

클라이언트는 요청을 보낼 때 마다 session_id를 함께 담아서 보내고, 이를 통해 서버는 사용자 식별 

**동작 과정**

1. 클라이언트가 첫 요청을 보냄, 서버에서는 session_id 쿠키 값이 없으므로 새로 발급해서 응답함
2. 클라이언트는 요청마다 session_id 값을 헤더 쿠키에 넣어서 전달
3. 서버는 session_id로 사용자 식별

<img width="500" src="https://github.com/user-attachments/assets/824d4d41-9a18-4dbe-be35-af1f86c0b6c8"/>

---

### JWT

**Json Web Token**으로 인터넷 표준 인증 방식, 쿠키와의 차이는 JWT는 **서명이 된** 토큰이라는 점

인증에 필요한 정보를 Token에 담아 암호화

**구성 요소 (xxxx.yyyy.zzzz)**

구성 요소가 .으로 구분되어 있는 형태

- Header
    - 토큰 타입, 서명 생성 알고리즘 종류
- Payload
    - 토큰에 대한 property를 {key:value}로 저장 (Claim)
    - payload에는 민감한 정보를 담지 않도록 해야함
        
        (Header, Payload는 json이 단순 디코딩된 형태이기 때문)
        
- Signature
    
    (디코딩된 Header+Payload)를 서버의 개인키 (발급 받은 것)으로 암호화
    

**복호화 과정**

1. JWT 토큰을 클라이언트가 요청 시 헤더에 함께 보냄
2. 서버는 토큰에서 Signature를 복호화하고, 인코딩한 Header, Payload가 JWT의 값과 일치하는지 확인
3. 일치하다면 인증 시작

**단점**

- 쿠키, 세션과 다르게 base64 인코딩을 통한 정보 전달 → 전달량이 많은 경우 부하가 생길 수 있음
- Payload는 암호화가 되어있지 않기 때문에 민감 정보를 저장할 수 없음
- **토큰이 탈취당하면 만료시까지 대처가 불가능함**

서버에 클라이언트 상태를 저장하는 session과 달리, stateless한 상황으로 토큰을 발급하면

이후는 클라이언트에서 관리하는 구조.  토큰 탈취가 발생해도 서버에서 관리할 수 있는 방법이 없음

→ **토큰이 탈취당하는 것을 방지하기 위해서 만료 시간을 짧게 둠**

⭐️ **사용자 입장에서는 토큰 만료 시간이 짧으면 불편함 → 이를 보완하기 위해서 refresh token을 함께 발급함**

**RefreshToken** 

JWT를 처음 발급할 때 access token과 함께 발급하여 사용, 이름 그대로 access token을 refresh 해주는 토큰

클라이언트 측에서 access Token이 만료된 것을 알았거나, 서버 측으로부터 토큰이 만료된 것을 확인 받은 경우

RefreshToken을 사용해서 AccessToken을 다시 발급 받게 하는 구조

access token에 비해 긴 시간 (7일 ~ 30일)을 두는 것이 특징

**동작 과정**

1. 클라이언트가 ID, PW로 서버에 인증 요청, 서버에서는 Refresh, Access Token 을 줌
2. 클라이언트는 AccessToken을 헤더에 넣어 유저 인증이 필요한 API 요청을 보냄
3. 사용 도중 AccessToken이 만료됨을 인지하고, 클라이언트에서 Refresh Token을 서버로 보냄
4. 서버는 RefreshToken Storage에 해당 토큰이 있는지 확인하고, Access Token을 만들어서 보냄

<img width="500" src="https://github.com/user-attachments/assets/b748ae68-e8cb-463f-bf3f-54ca6f10a3d3">


---

### OAuth 2.0

사용자가 어플리케이션에 ID, PW를 제공하지 않고 신뢰할 수 있는 **외부 어플리케이션 (Naver, Google, Kakao 등)이 대신 인증하고, 사용자 리소스 접근 권한을 위임** 받는 방식

그 중 카카오 로그인에 관심이 있어서 방식을 조사해봤다.

**Step1) 카카오 로그인 및 토큰 발급**

카카오 로그인 요청 → 서비스 서버는 카카오 인증 서버로 리다이렉트 

이후 사용자가 카카오 계정으로 로그인 및 동의 → 카카오 인증 서버는 서비스에서 등록한 리다이렉트 URI에 인증 코드를 붙여서 줌

```json
https://your-redirect-uri?code=AUTHORIZATION_CODE
```

이후 AUTHORIZATION_CODE 로 카카오 인증 서버에 엑세스 토큰 발급 요청

유효한 요청인 경우 엑세스 토큰, 리프레시 토큰을 발급해줌

```json
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
{
    "token_type":"bearer",
    "access_token":"${ACCESS_TOKEN}",
    "expires_in":43199,
    "refresh_token":"${REFRESH_TOKEN}",
    "refresh_token_expires_in":5184000,
    "scope":"account_email profile"
}

```
<img width="500" src="https://github.com/user-attachments/assets/6186c5e3-65f7-4e28-8443-0d1fe2ba6c38">

**Step2) 서비스 회원 가입, 로그인**

서비스 서버는 카카오 인증 서버에서 발급해준 토큰을 통해, 사용자 정보 가져오기를 요청

서비스 서버는 받은 사용자 정보로 회원 여부 확인 후, 신규 사용자는 회원 가입처리 아닌 경우 로그인처리함

<img width="500" src="https://github.com/user-attachments/assets/2a51d0e6-4d2d-46b2-9a7b-aea3dd958093">


기존 사용자는 회원 여부 확인 후 로그인처리하여 자체 JWT를 생성하여 클라이언트에게 반환함

---

### JWT 자체 로그인 + KakaoLogin

1. **Allow url에 리다이렉트 URI 등록하기**

현재 코드는  filterChain에 하나씩 등록을 해두었는데 이 코드처럼 allowUrls를 따로 빼서 관리하는 것이 좋아보인다.

```java
    public static final String[] allowUrls = {
    	    "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            "/login",
            "/auth/login/kakao/**"
    };
    
 
    public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// 다른 설정 중략
        http
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(ALLOWED_URLS).permitAll()  // 허용 URL 설정
                    .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
            )
            .csrf().disable()  // CSRF 보호 비활성화 (API 서버의 경우)
            .formLogin().disable()  // 폼 로그인 비활성화
            .httpBasic().disable();  // HTTP Basic 인증 비활성화

        return http.build();
    }
}
    
```

**2. AuthController** 

리다이렉트 URI 뒤에 함께 전달 된 인증 코드를 받아오는 역할

```java
RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    @GetMapping("/auth/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String accessCode, HttpServletResponse httpServletResponse) {

    }
    
}
```

---

⭐️ **Service와 Util의 구분**
**서비스**
- 비즈니스 로직 포함
- 도메인과 밀접 연관
- 트랜잭션 관리

**유틸리티**
- 재사용 가능한 헬퍼 메서드 제공
- 특정 도메인에 종속되지 않음
- 외부 API 호출이나 특정 기능에 특화된 로직 포함

---

**3 .Kakao Token 발급**

**AuthService - oAuthLogin**

AuthController에서 AccessCode를 받아서, kakaoUtil에서 구현한 토큰 발급 메서드를 호출함

```java
@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
  
		}
}

```

**KakaoUtil - requestToken**

실제 카카오 서비스에 토큰 발급을 요청하는 로직 포함

서버에서 요청을 보낼 때는 RestTemplate을 사용

확장성을 고려하면 WebClient를 사용하는 것이 좋지만 간단한 요청의 경우 RestTemplate도 좋음

RestTempalte을 생성하고 requestToken 메서드 안에서 AccessCode를 포함하여 요청을 보냄

```java
public class KakaoUtil {
    @Value("${spring.kakao.auth.client}")
    private String client;
    @Value("${spring.kakao.auth.redirect}")
    private String redirect;

    public KakaoDTO.OAuthToken requestToken(String accessCode) {
		    // 토큰 요청을 위한 Request 생성
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client);
        params.add("redirect_url", redirect);
        params.add("code", accessCode);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		    
		    // 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
        "https://kauth.kakao.com/oauth/token",
        HttpMethod.POST,
        kakaoTokenRequest,
        String.class);
				
				// Response JSON 직렬화를 위한 Object Mapper
				ObjectMapper objectMapper = new ObjectMapper();
				KakaoDTO.OAuthToken oAuthToken = null;
				
				// Response에서 데이터 꺼내기
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), KakaoDTO.OAuthToken.class);
            log.info("oAuthToken : " + oAuthToken.getAccess_token());
        } catch (JsonProcessingException e) {
            throw new AuthHandler(ErrorStatus._PARSING_ERROR);
        }
        return oAuthToken;
    }
 }
```

**4. AccessToken으로 사용자 정보 요청하기**

AuthService 로그인 메서드에 사용자 정보를 요청하는 코드를 추가한다. 

상세 로직은 KakaoUtil에서 처리한다.

```java
 // AuthService
 @Override
    public User oAuthLogin(String accessCode, HttpServletResponse httpServletResponse) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
		}

```

**KakaoUtil - requestProfile**

프로필 요청하는 상세 로직을 구현, 카카오 인증 서버에서 받은 토큰을 헤더에 함께 넣어서 요청

```java
// KakaoUtil
    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken oAuthToken){
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization","Bearer "+ oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest = new HttpEntity <>(headers2);

        ResponseEntity<String> response2 = restTemplate2.exchange(
                "https://kapi.kakao.com/v2/user/me", 
                HttpMethod.GET,
                kakaoProfileRequest,
                String.class);
	}
```

**5. 받은 사용자 정보를 이용한 회원 가입, 로그인** 

카카오에서 받은 프로필 정보를 사용해서 기존 회원인지 확인하고 없다면 회원가입, 있다면 로그인 

일반 회원 가입이 있는 서비스라면 OAuth로 새로 생성된 사용자는 DB password, Username (ID) 등을  null로 넣어서 기존 DB에 추가하면 된다.


---


## JWT 기반 인증 구현

**1. JWT Token DTO 생성**

클라이언트에 response로 보낼 token의 DTO 만들기

grantType = JWT 인증 타입, 여기서는 Bearer 인증 방식을 사용

```java
package com.ceos20.instagram.global.config.jwt;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}

```

**2. 암호키 설정하기**

토큰 암호화/복호화에 사용할 암호키를 설정

32자 이상으로 설정 terminal에 입력하여 새로 생성했다.

발급 받은 암호키는 .env에 등록하고, application.yml 파일에서 환경변수로 불러와준다.

```java
openssl rand -hex 32
```

**3. JwtTokenProvider**

Spring security와 JWT 토큰을 사용하여 인증, 권한 부여를 처리하는 클래스

토큰 생성, 검증, 토큰 내 정보 파싱 등을 구현했다.

그 중에서 claim에 저장된 username (회원의 ID) 을 가져오는 메서드를 추가로 만들었다.

`@Value("${jwt.secret"}` 을 통해서 yml에서 secret Key를 받아온다.

```java
...
@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

     // accessToken, refreshToken을 생성하는 메서드
    public JwtToken generateToken(Authentication authentication) {
        long now = (new Date()).getTime();

        // 기본 권한 설정
        String authorities = "USER";

        // accessToken 생성
        Date accessTokenExpiresIn = new Date(now + 1800000); // 30분
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refreshToken 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + 604800000)) // 일주일
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Jwt 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "권한 정보가 없는 토큰입니다.", null);
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 권한이 없는 경우 빈 컬렉션을 설정
        if (authorities.isEmpty()) {
            authorities = new ArrayList<>();
        }

        // UserDetails 객체를 만들어서 Authentication return
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    // accessToken 파싱하는 메서드
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰에서 username 가져오는 메서드
    public String getUsernameFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

}

```

**4. JWTAuthenticationFilter** 

JWT 인증을 하기 위한 커스텀 필터

doFilter를 통해서, 클라이언트 요청에 대한 인증 처리를 진행함

→ 토큰 유효성 검사를 통해서 유효하지 않은 경우 response 반환 후 체인 중단

→ 유효한 경우 사용자 인증처리 후 SecurityContext에 저장함 

거의 대부분의 서비스에서 username이 아닌 userId를 사용하므로, 토큰 인증 후 인증된 사용자 정보 저장 시 해당 유저의 userId (고유 번호)를 principal로 설정함

resolveToken을 통해서 헤더에서 토큰을 추출

```java
... 

@Builder
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager; 
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        // 토큰 유효성 검사
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            // 필터 내에서 직접 응답 처리
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json;charset=UTF-8");

            String message = "JWT 토큰이 유효하지 않거나 누락되었습니다.";
            ResponseTemplate<?> responseTemplate = ResponseTemplate.builder()
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .success(false)
                    .message(message)
                    .data(null)
                    .build();

            // ObjectMapper를 사용해서 json으로 직렬화
            ObjectMapper objectMapper = new ObjectMapper();
            String responseMessage = objectMapper.writeValueAsString(responseTemplate);
            httpResponse.getWriter().write(responseMessage);

            return;
        }

        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 username을 추출하고, username으로 userId를 조회
            String username = jwtTokenProvider.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);  // username으로 조회
            Long userId = ((User) userDetails).getId();  // userId 추출

            Authentication authentication = jwtTokenProvider.getAuthentication(token);

            Authentication authenticated = new UsernamePasswordAuthenticationToken(
                    userId,  // userId를 Principal로 설정
                    "",
                    authentication.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }

        chain.doFilter(request, response);
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // "Bearer " 이후의 토큰을 반환
        }
        return null;
    }
}

```

**5. SecurityConfig**

Spring security 설정 파일

**filterChain**

이 중에서 인가 규칙 설정 부분은 allowUrls 를 따로 만들어서 추후 리팩토링 예정 

- 인증 방식, 세션 관리 설정
- 인가 규칙 설정
- 필터 설정

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    AuthenticationManager authenticationManager = authenticationManager(httpSecurity);

    return httpSecurity
				    // 인증 방식 및 세션 관리 설정
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
	           // 인가 규칙 설정
            .authorizeHttpRequests()
            .requestMatchers("/users/login").permitAll()
            .requestMatchers("/users/id/**").permitAll()
            .requestMatchers("/users/register").permitAll()
            .anyRequest().authenticated()
            .and()
            // 필터 설정
            // jwtAuthenticationFilter를 앞에 추가해서 먼저 실행되도록 함
            .addFilterBefore(jwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
            .build();
}

```

**jwtAuthenticationFilter** 

인증 필터를 빈으로 등록하고 필요한 의존을 주입하는 메서드

- `jwtTokenProvider` : 토큰 생성 및 검증
- `AuthenticationManager` : 사용자 인증 관리, `JwtAuthenticationFilter` 에서 사용
- `customUserDetailsService` : 사용자 정보 조회 메서드

```java
@Bean
public JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    return JwtAuthenticationFilter.builder()
            .jwtTokenProvider(jwtTokenProvider)
            .authenticationManager(authenticationManager)
            .customUserDetailsService(customUserDetailsService)
            .build();
}

```

**6. AuthenticationUtils**

인증된 사용자 정보 접근에 필요한 메서드를 모아둔 클래스

**getLoginUserId (로그인한 사용자 정보, 즉 token 인증된 사용자)**

인증된 사용자를 SecurityContextHolder에서 가져와서 userId를 가져오는 메서드

```java
package com.ceos20.instagram.global;

import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

    // 인증된 사용자의 userId를 가져오는 메서드
    public static Long getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "인증 정보가 없습니다.", null);
        }

        return (Long) authentication.getPrincipal();
    }
}

```

## 회원가입, 로그인 API 구현 및 테스트

### 회원가입 API 구현

컨트롤러 단에서 필드 오류를 잡고, 이후에 회원가입 로직처리

**UserController - register**

```java
// 회원가입
@Operation(summary = "회원 가입")
@PostMapping("/register")
public ResponseEntity<ResponseTemplate<UserRegisterResponseDto>> register(@Valid @RequestBody UserRegisterRequestDto requestDto,
                                                                          BindingResult bindingResult) {
    // DTO 필드 검증
    if (bindingResult.hasErrors()) {
        // 필드와 기본 메시지를 CustomException으로 던짐
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }
        // CustomException으로 필드 오류 전달
        throw new CustomException(ErrorCode.BAD_REQUEST, "잘못된 필드 형식입니다.", fieldErrors);
    }

    // 회원 정보 저장
    UserRegisterResponseDto responseDto = userService.register(requestDto);
    // ResponseTemplate을 사용해 응답 생성
    return ResponseTemplate.createTemplate(HttpStatus.CREATED, "회원가입 성공", responseDto);
}

```

**201** 

회원가입 성공
<img width="500" src="https://github.com/user-attachments/assets/a0b10785-b91a-4d57-9eec-1320b6efc58d">


**409**

이미 사용중인 이메일, 아이디에 대한 요청 에러처리

<img width="500" src="https://github.com/user-attachments/assets/fec5d05e-30ed-4eda-8f62-d034f5f3a334">

<img width="500" src="https://github.com/user-attachments/assets/c87cfe83-6498-4672-860a-0a1dcd14759a">

**400**

잘못된 필드에 대한 형식, 필수 필드 누락시에 대한 에러 처리

<img width="500" src="https://github.com/user-attachments/assets/bb759cbf-59c4-4879-93c2-3e4739af8ae7">

### 로그인 API 구현

**UserController - login**

필드 오류는 컨트롤러 단에서 잡고, 이후 로그인 처리 

```java
  // 로그인
  @Operation(summary = "로그인")
  @PostMapping("/login")
  public ResponseEntity<ResponseTemplate<UserLoginResponseDto>> login(@Valid @RequestBody UserLoginRequestDto requestDto,
                                                                      BindingResult bindingResult) {
      // DTO 필드 검증
      if (bindingResult.hasErrors()) {
          // 필드와 기본 메시지를 CustomException으로 던짐
          Map<String, String> fieldErrors = new HashMap<>();
          for (FieldError error : bindingResult.getFieldErrors()) {
              fieldErrors.put(error.getField(), error.getDefaultMessage());
          }
          // CustomException으로 필드 오류 전달
          throw new CustomException(ErrorCode.BAD_REQUEST, "잘못된 필드 형식입니다.", fieldErrors);
      }
      // 로그인
      UserLoginResponseDto responseDto = userService.login(requestDto);

      // ResponseTemplate을 사용해 응답 생성
      return ResponseTemplate.createTemplate(HttpStatus.OK, "로그인 성공", responseDto);
  }

```

**UserService - login**

username, password 검증 이후 인증 매니저로 인증 정보 생성

인증 정보로 token 생성 후 ResponseDto에 포함하여 리턴

```java
  // 로그인 메서드
  @Transactional(readOnly = true)
  public UserLoginResponseDto login(UserLoginRequestDto requestDto){
      // username으로 사용자 조회
      User user = userRepository.findByUsername(requestDto.getUsername())
              .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "존재하지 않는 아이디입니다." ,requestDto.getUsername()));

      // 비밀번호 확인
      if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
          throw new CustomException(ErrorCode.NOT_FOUND, "비밀번호가 맞지 않습니다." ,requestDto.getPassword());
      }

      // UsernamePasswordAuthenticationToken 생성하여 인증 매니저에 넘기기
      UsernamePasswordAuthenticationToken authenticationToken =
              new UsernamePasswordAuthenticationToken(user.getUsername(), requestDto.getPassword());

      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      // 인증 정보로 JWT 토큰 생성
      JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

      // 로그인 성공 응답
      return UserLoginResponseDto.from(user, jwtToken);
  }
```

**200**

로그인 성공하는 경우 토큰과 함께 userId, username 반환
<img width="500" src="https://github.com/user-attachments/assets/9d896a23-0bad-4542-a3ff-0585928439e1">


**404**

존재하지 않는 아이디인 경우, 비밀번호가 일치하지 않는 경우 처리
<img width="500" src="https://github.com/user-attachments/assets/827c4685-3401-4921-a701-bd9c8f387cb2">
<img width="500" src="https://github.com/user-attachments/assets/f6562cdd-108a-4234-a715-c69c76ca3397">


**400** 

필수 필드가 비어있는 경우

<img width="500" src="https://github.com/user-attachments/assets/6916d600-d5ec-4a23-9c8b-3892f89607dc">


## 토큰 이용 권한 인증 API 구현 및 테스트

로그인해야 이용할 수 있는 글 관련 기능에 대한 API 테스트

**게시글 수정 API** 

기존 userId를 static value에서 직접 SecurityContex에서 가져오는 코드로 변경

AuthenticationUtils에 만들어둔 인증 받은 유저의 userId 가져오기 메서드 `getLoginUserId` 사용

service 단에서는 수정한 코드 없음

```java
  // 게시글 수정
  @Operation(summary = "게시글 수정")
  @PatchMapping("/posts/{postId}") // 필드 일부 수정 가능
  public  ResponseEntity<ResponseTemplate<PostResponseDto>> updatePost(@PathVariable Long postId,
                                                    @RequestBody PostUpdateRequestDto requestDto){
      **Long userId = AuthenticationUtils.getLoginUserId();**
      PostResponseDto responseDto = postService.updatePost(postId, userId, requestDto);
      return ResponseTemplate.createTemplate(HttpStatus.OK, "게시글 수정 성공", responseDto);
  }
```

**Postman을 사용한 API 테스트** 

Auth에 Bearer 토큰란에 토큰을 포함하여 요청 보냄

<img width="500" src="https://github.com/user-attachments/assets/c6070836-d627-4673-a66b-e9c3c07a232c">

**권한이 없는 유저가 수정 요청 시**

권한이 없음을 의미하는 401 코드 반환하고 유저의 고유 번호 id를 함께 반환함

<img width="500" src="https://github.com/user-attachments/assets/00294701-ca7f-4e19-b5bf-923ddee8c423">
---

## Reference

- [JWT](https://velog.io/@jinyoungchoi95/JWTJson-Web-Token-%EC%9D%B8%EC%A6%9D%EB%B0%A9%EC%8B%9D)
- [RefreshToken](https://medium.com/@dkfud2121/jwt-token-%EC%9D%B4%EA%B2%83%EB%A7%8C%EC%9D%80-%EC%95%8C%EA%B3%A0-%EA%B0%80%EC%9E%90-d96a49fbcabe)
- [Cookie](https://velog.io/@dnjsdn96/Cookie-Cookie%EB%9E%80)
- [OAuth - kakao](https://jinhos-devlog.tistory.com/entry/Spring-Rest-API-%EC%B9%B4%EC%B9%B4%EC%98%A4-Kakao-OAuth-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)


---
## 6주차 (11/11 - 11/16)

---

## 🐳 Docker

Docker란 애플리케이션 구축, 구현 및 테스트를 위해 격리된 가상화 환경을 생성하는 서비스형 플랫폼이다.

**Docker를 사용해야하는 이유**

OS 환경에 구애 받지 않고, 동일한 환경을 만들 수 있기 때문에 애플리케이션을 신속하게 배포, 확장할 수 있다.

Spring Boot 서버를 띄우는 과정에서 JDK, GIt등을 설치해야하는 번거로움이 없고, OS 환경이 다른 경우에도 배포 과정의 오류를 줄일 수 있다.

### Docker Image

Application을 포장, 전송하기 위해 사용하는 파일, 어플리케이션 실행에 필요한 독립적인 환경을 포함한다.

도커 이미지는 소스코드, 라이브러리, 종속성, 도구, 응용프로그램 등을 실행하기 위한 기타 파일을 포함하는 **불변 파일(읽기 전용)이다.**

특정 시점의 애플리케이션과 가상 환경으로 스냅샷이라고도 불린다. 

**이러한 특징으로 일관성을 유지하며, 안정적이고 동일한 환경에서 소프트웨어를 테스트하고 실험할 수 있는 것이다.**

### Docker Container

컨테이너는 이미지 목적에 따라 생성되는 프로세스 단위의 격리 환경이다. 

이미지를 동적인 형태로 변경한 것이 컨테이너이며, 즉 애플리테이션을 실행할 격리 환경이다.

컨테이너를 생성하면 쓰기 가능한 레이어가 불변 이미지 위에 추가가 되는 방식으로 작동한다.

Base 이미지에서 도커 이미지를 무제한으로 생성할 수 있다.

각 이미지 계층은 가상환경을 사용할 때 추가된 읽기 전용 파일이다.

<img width="500" src="https://github.com/user-attachments/assets/61c79650-81b7-42f5-a7db-a0273d09d656">



### Dockerfile, Image, Dockerhub, Container

```
Dockerfile --(Build)--> Image --(Create)--> Container
```

 ****이미지를 빌드하는 방식을 정의한 스크립트인 Dockerfile을 통해 이미지를 생성한다.

이후 이미지를 동적 형태로 변경하여 컨테이너를 구동시키는 형식으로 진행한다.

생성한 이미지는 DockerHub에서 다운 받아, 실행시킬 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/7f5d1e24-3db3-4a43-b897-6194e59d2fbc">


### CD (Continuous Delivery)

Docker는 이미지(불변)를 기반으로 생성된다.

그렇다면 수정사항이 발생한다면 어떻게 해야할까?

개발자가 하나하나 도커 이미지를 다시 만들고, 재배포해야할까? 당연히 아니다. 

→ github actions, Jenkins 등 CI/CD 도구 등을 통해 배포를 자동화할 수 있다.

### 포트 포워딩

기존 운영체제 (Host)와 도커 컨테이너는 독립된 실행 환경으로, 다른 포트와 파일 시스템을 가지고 있다.

따라서 Host와 Container의 포트를 연결해주어야 Host의 port로 전송된 것들을 Container port로 전달할 수 있다.

이를 **포트 포워딩**이라고 한다.

<img width="500" src="https://github.com/user-attachments/assets/83af0d91-3a3e-412a-8363-8893192e6144">



Host의 8080 포트 (외부)와 Container의 80 포트 (내부)를 연결하여 포워딩한다.

도커는 호스트 컴퓨터의 8080 포트로 들어오는 트래픽을 주시하다가 필요한 트래픽을 컨테이너 80 포트로 전달한다.

-p는 publish의 약어이다.

이 플래그 덕분에 컨테이너의 포트가 공개되어, Host 컴퓨터의 물리 네트워크 주소가 컨테이너의 가상 네트워크 주소에 접근할 수 있다.

**CLI 사용**

```cpp
docker run --name {name} -d -p {host포트}:{container포트} {이미지명}
```

localhost8080에서 잘 접속된 것을 확인할 수 있다

<img width="500" src="https://github.com/user-attachments/assets/3974a2c6-d174-4cff-9951-378b0838f21a">


**Docker Desktop 사용**

Docker > Images 탭에서  httpd를 실행할 때 이미지를 실행할 컨테이너 옵션 설정을 할 수 있다.

Ports 

- Container Port = 이미지의 프로세스로 접속시 사용할 포트
- Host Port = 우리가 설정해줄 port

host의 몇 번 포트로 접속했을 때 container port로 연결할 지 설정하는 것

<img width="500" src="https://github.com/user-attachments/assets/bee30f8d-bb2e-4a52-99b9-237013e687d6">


Host의 8080 포트로 접속할 경우 해당 컨테이너의 80번 포트와 연결되도록 설정

<img width="500" src="https://github.com/user-attachments/assets/6db7f987-2995-4b06-abc8-96e2fdef5642">


localhost8080 으로 접속하면 잘 연결된 것을 확인할 수 있다.
<img width="500" src="https://github.com/user-attachments/assets/9c8b9d32-e253-40c4-a048-42e9e45b868e">


### Docker 컨테이너 데이터 저장

docker 컨테이너의 데이터는 컨테이너 삭제 시 함께 사라짐

→ docker에서 돌아가는 애플리케이션은 컨테이너 생명 주기와는 관계 없이 영속적인 데이터 저장이 필요

바인드 마운트, docker 볼륨으로 해결 가능

**바인드 마운트**

<img width="500" src="https://github.com/user-attachments/assets/dfd9727f-8b6a-46f9-aef9-4015fe0c24fa">



호스트 파일 시스템 특정 경로를 컨테이너로 바로 가져올 수 있음

docker run 실행시 -v 옵션으로 호스트의 파일 경로를 지정해주면 됨

```bash
docker run --name {컨테이너명} -d -p {Host port}:{container port} -v {경로} {이미지명}
```
<img width="500" src="https://github.com/user-attachments/assets/7bc27d2d-b708-49af-9585-29670e794851">


등록 한 후에

docker inspect bindTest를 하고 Mounts 부분을 보면 확인 가능하다.

<img width="500" src="https://github.com/user-attachments/assets/90aa19d0-dd29-4951-a6bb-f0b2080f57e7">


**docker volume** 

docker에서 권장하는 방법

컨테이너의 네부 데이터를 외부로 링크를 걸어주는 기능, 내부에서 수정되는 경우 외부 데이터도 같이 수정됨

컨테이너는 삭제되어도 외부에 데이터가 남게 됨

<img width="500" src="https://github.com/user-attachments/assets/be4e5b9e-5ee3-4ad8-be37-891b755020ff">


docker run -v 옵션을 통해서 내부 디렉토리와 volume을 연결할 수 있다. 

**⭐️ 위 방식을 사용하는 경우 입력한 컨테이너 외부 디렉터리(볼륨) 데이터로 내부 데이터가 덮어씌워짐**

만약 외부가 빈 상태라면 내부 디렉터리 내용이 삭제되고 빈 상태가 된다.

볼륨을 삭제할 때는 마운트된 컨테이너를 먼저 삭제해야한다. 

```bash
# 볼륨 생성
docker volume create {볼륨명}

# 볼륨 정보 확인
docker volume inspect {볼륨명}

# 볼륨 - 컨테이너 연결 (마운트)
docker run -v {볼륨명}:{컨테이너 내부 디렉토리 경로} --name {컨테이너명} {이미지명}

# 볼륨 삭제
docker volume rm {볼륨명} 

# 마운트되어있지 않은 볼륨 삭제
docker volume prune
```

volume을 생성하고, 이미지로 실행한 컨테이너와 volume을 연결한다

<img width="500" src="https://github.com/user-attachments/assets/16b94fb3-51ef-4320-882d-2b755a51defb">


이후 컨테이너 정보를 출력하면 MountPoint에 volume이 연결되어 있는 것을 확인할 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/31afea9d-08b3-412d-afca-9c55007b604b">


---

## Docker 기반 스프링부트 빌드

도커에 띄울 때 2가지 방식을 사용할 수 있다.

### DockerFile

1. jar 파일 생성

수정사항이 있으며 bootJar을 눌러서 반영해줘야한다

<img width="300" src="https://github.com/user-attachments/assets/4d01226b-a9ee-4fb9-9b2f-209816f3cfdf">


1. **Dockerfile 생성**

프로젝트 최상단에 Dockerfile 명으로 파일을 생성한다.

jdk 버전을 실제 사용 버전과 맞춰야한다. 

자바 22는 지원을 안해서….. gradle 부터 프로젝트 jdk까지 다 변경했다 😱😱😱

```bash
FROM openjdk:21
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "/app.jar"]
```

1. **도커 이미지 생성**

Dockerfile 위치와 같은 곳에서 명령어를 입력한다면 . 을 넣으면 됨

```bash
docker build -t {이미지명} {docker 파일 위치}
```

<img width="500" src="https://github.com/user-attachments/assets/822863cb-ec40-4e58-a043-d3587bb86091">



test1 이름으로 이미지가 만들어진 것을 확인 할 수 있다

<img width="500" src="https://github.com/user-attachments/assets/45356840-3ed6-4f91-a3a5-02400bc243f2">


**4.  도커 이미지 실행** 

JWT 토큰 암호화 값 등 env file을 옵션으로 전달해줘야함 

```bash
docker run --env-file .env -p 8080:8080 testimage
```

실행하면 도커 컨테이너에서 애플리케이션이 돌아간다 ~ 

<img width="500" src="https://github.com/user-attachments/assets/e0237d52-e1a2-4c73-8e1d-61de2151e6c6">



컨테이너에서 test1 이미지로 실행 중인 컨테이너를 확인할 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/713f441a-ee72-4b12-b953-d11799e0d29a">


**+) mysql 오류 해결**

자꾸 mysql을 못 불러오고, Jdbc에서 DB를 연결할 수 없다는 에러가 뜨면서 실행이 안 됐다 .. 

오류 문구 중 이런게 있어서

```bash
2024-11-15T12:24:54.007Z ERROR 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Failed to initialize JPA EntityManagerFactory: Unable to create requested service [org.hibernate.engine.jdbc.env.spi.JdbcEnvironment] due to: Unable to determine Dialect without JDBC metadata (please set 'jakarta.persistence.jdbc.url' for common cases or 'hibernate.dialect' when a custom Dialect implementation must be provided)
```

application.yml 에 dialect 를 추가하는 방식으로 해결했다. 

왜 해결됐는지 찾아봐야겠다 …….

```bash
  jpa:
    show-sql: true
    properties:
      hibernate:
        ddl-auto: update
        **dialect: org.hibernate.dialect.MySQL8Dialect**
```

### Docker Compose

1. **docker-compose.yml 생성**

최상단에 docker-compose.yml을 생성한다.

환경 변수들은 .env에서 관리하고 불러왔다.

db ports 번호는 host, container 순인데 host (내 컴퓨터)에서 3306에서 돌아가는게 있어서 3307으로 변경했다.

```bash
Error response from daemon: Ports are not available: exposing port TCP 0.0.0.0:3306 -> 0.0.0.0:0: listen tcp4 0.0.0.0:3306: bind: address already in use

```

```bash
version: "3"

services:
  db:
    image: mariadb:latest
    environment:
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
      MYSQL_DATABASE: ${DB_NAME}
    volumes:
      - dbdata:/var/lib/mysql
    ports:
      - "3307:${DB_PORT}"
    restart: always

  web:
    container_name: web
    build: .
    ports:
      - "${APP_PORT}:${APP_PORT}"
    depends_on:
      - db
    environment:
      MYSQL_HOST: ${DB_HOST}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASSWORD}
      MYSQL_DB: ${DB_NAME}
    restart: always
    volumes:
      - app:/app

volumes:
  dbdata:
  app:

```

1. **실행**

```bash
docker-compose -f docker-compose.yml up --build
```

빌드가 끝나면 마찬가지로 실행된다 

<img width="500" src="https://github.com/user-attachments/assets/02d8fdd7-cc46-44b9-b65c-f29f17b713c5">
<img width="500" src="https://github.com/user-attachments/assets/ba26afc1-534a-4fa4-a7d8-2b5c403a956a">
<img width="500" src="https://github.com/user-attachments/assets/e9d4306d-92ee-4811-9cab-0e4325e7ac39">


### DockerFile vs Docker Compose

두 방식의 차이는 뭘까?
`Dockerfile` : 이미지를 생성하기 위한 일종의 설계도

`Docker Compose` : 여러 도커 컨테이너를 정의하고 실행하기 위한 도구

**차이점**

- **목적**: Dockerfile은 단일 이미지를 구축하는 데 집중하는 반면, Docker Compose는 여러 컨테이너의 구성과 관리에 집중
- **범위**: Dockerfile은 한 컨테이너 내부의 설정에 관한 것이고, Docker Compose는 여러 컨테이너가 어떻게 함께 동작해야 하는지에 대한 외부 설정을 다룸

---

## Reference

- [Docker](https://sunrise-min.tistory.com/entry/Docker-Container%EC%99%80-Image%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80)
- [포트포워딩](https://velog.io/@luna_lee/Docker-%EB%8F%84%EC%BB%A4-%EC%9E%85%EB%AC%B8%EA%B3%BC%EC%A0%95-Port-forwarding)
- [바인드 마운트](https://www.daleseo.com/docker-volumes-bind-mounts/)
- [dockerfile, compose](https://www.inflearn.com/community/questions/1160100/dockerfile%EA%B3%BC-dockercompose-%EC%B0%A8%EC%9D%B4%EA%B0%80-%EA%B6%81%EA%B8%88%ED%95%A9%EB%8B%88%EB%8B%A4?srsltid=AfmBOorglkZj_6AD9mVZpyPr7LKsc_wdDBPhMHi13e93Sb-PHzb_GKbh)
- [volume](https://formulous.tistory.com/17)


---
## 7주차 (11/18 - 11/23)

---

## 저번주 트러블 슈팅

저번주에 mysql을 불러올 수 없다는 문제가 있었다. 

application.yml에 dialect를 추가하는 방식으로 해결했으나, 제대로된 해결이 아닌듯하여 다시 해보기로했다. 

→ 그 당시 3306에 로컬 mysql 서버가 동작하고 있어서 해결이 된 상황으로 확인 

내가 원하는 것은 도커 컨테이너에서 동작하는 mysql이므로 해당 서버를 끄고 다시 시도

확인해보니 내 로컬 환경에서 java application, mysql 두 도커 컨테이너가 동작하는 상황인데 

Datasource url이 변경되어있지 않아서 발생한 문제였다. 

application.yml의 datasource url 부분에서 호스트 부분을 [localhost](http://localhost) → host.docker.internal 로 바꿔줘야한다.

.env 파일의 값을 변경하고 재실행했더니 잘 동작하는 것으로 확인 

```bash
docker pull mysql
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD={password} -d -p 3307:3306 mysql:latest
```

```bash
docker build -t {이미지명} .
docker run --env-file .env -p 8080:8080 {이미지명}
```
<img width="500" src="https://github.com/user-attachments/assets/790eecad-2085-4e71-b639-48530ac2a18e">

로컬에서 잘 작동하는 것을 확인했고, 다른 허용 URI 미적용 문제등을 해결하고 

다시 도커이미지 생성하여 배포를 진행했다. 

---

## Deploy

### 1. 도커 이미지 생성 및 허브 등록

**애플리케이션 빌드 및 이미지 생성**

애플리케이션은 gradle/bootJar를 통해서 빌드하였다. 

이후 아래 명령어로 도커 이미지를 생성했다.

M1 이후부터는 플랫폼 명시를 해줘야한다, 명시하지 않으면 arm 기반 이미지를 생성해서 linux 기반 인스턴스에서 도커이미지를 실행할 수 없다.

```markdown
docker build --platform linux/amd64 -t {도커아이디}/{애플리케이션명} .
```
<img width="500" src="https://github.com/user-attachments/assets/959249bf-fc4c-450b-a9c7-c2e9ff63837b">

### 2. EC2 배포

**1) 보안 그룹 설정**

http(80), ssh(22), mysql(3306) 인바운드 추가

http는 이후 postman으로 API 테스트를 하기 위해서, ssh는 인스턴스 접속용으로 추가

**2) 패키지 업데이트**

Amazon linux의 경우 apt 명령어 대신 yum 사용

```bash
sudo yum update
```

**3) 스왑 메모리설정**

제한된 프리티어에서 스왑 메모리를 사용하여 최대한 많은 메모리를 사용할 수 있게 한다.

1. **스왑 파일 생성 및 읽기/쓰기 권한 부여**

스왑 메모리는 램 메모리의 2배 이상을 추천

프리티어 사용시 램 1GB이므로 스왑 메모리 2GB로 설정

```bash
sudo dd if=/dev/zero of=/swapfile bs=128M count=16
sudo chmod 600 /swapfile
```

dd: 블록 단위 파일 복사, 변환 명령어

if: 지정한 파일을 입력 대상으로 설정

of: 지정한 파일을 출력 대상으로 설정

bs: 한 번에 변환 가능한 바이트 크기

count: 지정한 블록 수 만큼 복사

**b. 스왑 공간 생성** 

```bash
sudo mkswap /swapfile
```

**c. 생성한 스왑 공간에 스왑 파일 추가 후 확인하기**

```bash
sudo swapon /swapfile
sudo swapon -s
```

<img width="500" src="https://github.com/user-attachments/assets/ba2d39a3-6a3d-4f82-92cf-8d659ef71b40">

**d. 스왑 파일 시스템 설정**

시스템 부팅 시 자동 활성화되도록 파일 시스템 수정

```bash
sudo vi /etc/fstab
```

vi 에디터 열리면, 아래 내용을 추가하기

Esc키 명령 모드 → G를 눌러 맨 아랫줄로 이동

→ i키 눌러서 편집 모드 

→ 내용 추가후 Esc, :wq + enter 키로 저장

```bash
/swapfile swap swap defaults 0 0
```

**e. 메모리 확인**

```bash
free
```

<img width="500" src="https://github.com/user-attachments/assets/0b4ecb69-bc09-4089-a9ce-2ca751741bc1">


**4) 도커 설치 및 실행**

이미 ec2 CLI 에서 진행하고 있기 때문에 별도 연결 없이 도커 설치를 했다.

linux 환경이므로 yum 을 사용해서 설치한다

버전 확인을 하고, 도커 명령어를 실행하려면 도커가 실행 중이어야한다. 

```bash
sudo yum install docker -y
docker --version

# 도커 실행
sudo systemctl start docker
```

**5)mysql 이미지 다운 후 실행**

⭐️ mysql 이미지를 먼저 실행하고 있어야지 애플리케이션에서 db 연결 오류가 나지 않는다. 

이때 인자로 주는 비밀번호가 mysql root 접속 비밀번호가 된다. 

```bash
# MYSQL 이미지 pull, 실행
sudo docker pull mysql

sudo docker run --name mysql-container -e MYSQL_ROOT_PASSWORD={password} -d -p 3306:3306 mysql
```

ec2에서 동작하는 Mysql을 workbench와 연결해서 스키마를 만들어줘야한다.

JPA는 만들어진 db와 연결하여 테이블을 생성해주지만 db 자체는 만들 수 없기 때문에,

만들지 않고 스프링앱을 실행하면 db_name이 뭔데 ?? 라는 오류를 낸다.

.env 파일의 DB_NAME과 동일하게 스키마를 생성한다.

hostname 부분은 퍼블릭 IP DNS 이름을 복사해서 넣어주면 된다. 사진으로는 잘렸는데 끝까지 넣어줘야한다.

비밀번호는 위에서 mysql 이미지 실행 시 넣어줬던 비밀번호로 설정하면 된다.

<img width="500" src="https://github.com/user-attachments/assets/082f392b-e885-4731-8bf8-b5679b1863cf">

이후 workbench와 연결되면, 스키마를 생성한다.

만약 권한이 없거나 조회가 안되는 경우 루트 유저에 권한 부여 후 진행하면 된다.

```sql
CREATE DATABASE db_name;
```

mysql db 생성이 제대로 되어서, 로컬에서 돌아가는 spring boot 애플리케이션과 연결이 되는지 확인해봤다.

그 과정에서 외부 접근 허용을 위해서는 [mysql bind 설정을 변경](https://wanbaep.tistory.com/11)해줘야한다는 것을 알게 되었다.  수정 후 진행하였고, 각 테이블도 잘 생성된 것을 확인했다.

**6).env 파일 생성**

애플리케이션 이미지를 실행할 때 .env 파일을 전달하는 방식을 사용하기 위해 파일을 생성한다. 

```bash
vi .env
```

후 기존 로컬 .env 파일 중 DB host 부분을 local host에서 EC2 public IP로 변경한다.

이제 ec2 환경에서 동작하는 mysql과 연결해야하기 때문이다.

```bash
DB_HOST={public IP}
DB_PORT=3306
DB_NAME={db 이름}
DB_USER=root
DB_PASSWORD={password}
JWT={토큰 값}
APP_PORT=8080
```

**7)스프링 이미지 다운 후 실행**

여기서 주의할 점은 -e로 인자를 넘겨줬을 때 자꾸 스프링 컨테이너가 꺼졌다.

—env-file로 넘겨주니까 꺼지지 않고 잘 돌아갔다. 

```bash
# 스프링 이미지
sudo docker pull {도커아이디}/{리포지토리명}
sudo docker run -e .env -d -p 80:8080 {도커아이디}/{리포지토리명 혹은 이미지ID}
```
<img width="500" src="https://github.com/user-attachments/assets/c89e98af-3b8f-432c-959e-4f3367146c4f">

### 3. API 테스트

두 도커 이미지가 돌아가는 인스턴스의 public Ip 주소를 넣어서 회원가입과 로그인 테스트를 진행했다.

스프링 애플리케이션을 실행할 때 80:8080으로 설정했기 때문에 

`http://{publicIp}:80/{테스트할URI}`

이렇게 요청을 보내면 된다.

회원가입과 로그인 기능이 잘 동작하는 것을 확인할 수 있다.

<img width="500" src="https://github.com/user-attachments/assets/11097f60-e676-4365-aec3-a980cce98afa">

<img width="500" src="https://github.com/user-attachments/assets/85ffc261-9d39-4d70-8c9f-91fbe0f08a17">

---

### 레퍼런스

[스왑메모리](https://diary-developer.tistory.com/32)

[ec2에서 돌아가는 mysql 연결하기](https://yejin-code.tistory.com/50)

[mysql bind 설정을 변경](https://wanbaep.tistory.com/11)

