package com.ceos20.instagram.comment.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.dto.CommentRequestDto;
import com.ceos20.instagram.comment.dto.CommentResponseDto;
import com.ceos20.instagram.comment.dto.CommentUpdateRequestDto; // 추가
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성 메서드 (대댓글 기능 포함)
    @Transactional
    public CommentResponseDto createComment(Long postId, Long userId, CommentRequestDto requestDto, Long parentCommentId) {
        // 댓글 달 포스트 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("포스트를 찾을 수 없습니다: " + postId));

        // 댓글 달 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        // 부모 댓글이 있는 경우, 부모 댓글 조회
        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다: " + parentCommentId));
        }

        // 댓글 생성 후 저장
        Comment comment = requestDto.toEntity(post, user, parentComment); // 부모 댓글 포함
        Comment savedComment = commentRepository.save(comment);

        // 댓글 생성 성공
        return CommentResponseDto.from(savedComment);
    }


    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentResponseDto getComment(Long commentId) {
        // 댓글 id로 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        return CommentResponseDto.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getReplies(Long parentCommentId) {
        List<Comment> replies = commentRepository.findByParentComment_Id(parentCommentId); // 대댓글 조회
        return replies.stream()
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());
    }

    // 댓글 수정 메서드
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentUpdateRequestDto updateDto) {
        // 댓글 id로 댓글 조회
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));

        // 댓글 수정
        existingComment = Comment.builder()
                .id(existingComment.getId())
                .user(existingComment.getUser())
                .post(existingComment.getPost())
                // 댓글 내용 수정
                .content(updateDto.getContent() != null ? updateDto.getContent() : existingComment.getContent())
                .createdAt(existingComment.getCreatedAt())
                .modifiedAt(LocalDateTime.now()) // 댓글 수정일 반영
                .build();

        // 댓글 저장
        Comment savedComment = commentRepository.save(existingComment);
        // 댓글 수정 성공
        return CommentResponseDto.from(savedComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        // 댓글 id로 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));
        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
