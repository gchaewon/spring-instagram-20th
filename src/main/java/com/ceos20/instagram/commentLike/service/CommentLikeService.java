package com.ceos20.instagram.commentLike.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.commentLike.domain.CommentLike;
import com.ceos20.instagram.commentLike.dto.CommentLikeRequestDto;
import com.ceos20.instagram.commentLike.dto.CommentLikeResponseDto;
import com.ceos20.instagram.commentLike.repository.CommentLikeRepository;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 좋아요 생성 메서드
    @Transactional
    public CommentLikeResponseDto createCommentLike(Long userId, CommentLikeRequestDto requestDto) {
        // 댓글 조회
        Comment comment = commentRepository.findById(requestDto.getCommentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + requestDto.getCommentId()));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다: " + userId));

        // 좋아요 만들기
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        // 좋아요 저장
        CommentLike savedCommentLike = commentLikeRepository.save(commentLike);

        return CommentLikeResponseDto.from(savedCommentLike);
    }

    // 댓글 좋아요 삭제 메서드
    @Transactional
    public void deleteCommentLike(Long commentLikeId) {
        // 좋아요 조회
        CommentLike commentLike = commentLikeRepository.findById(commentLikeId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 찾을 수 없습니다: " + commentLikeId));

        // 좋아요 삭제
        commentLikeRepository.delete(commentLike);
    }
}
