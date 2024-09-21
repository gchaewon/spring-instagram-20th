# spring-instagram-20th

CEOS 20th BE study - instagram clone coding

## 서비스 설명, 기능 명세

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




