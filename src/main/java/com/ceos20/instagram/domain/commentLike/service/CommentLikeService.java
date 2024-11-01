package com.ceos20.instagram.domain.commentLike.service;

import com.ceos20.instagram.domain.comment.domain.Comment;
import com.ceos20.instagram.domain.commentLike.domain.CommentLike;
import com.ceos20.instagram.domain.commentLike.dto.CommentLikeResponseDto;
import com.ceos20.instagram.domain.commentLike.repository.CommentLikeRepository;
import com.ceos20.instagram.domain.comment.repository.CommentRepository;
import com.ceos20.instagram.domain.postLike.domain.PostLike;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    // 댓글 좋아요 여부 조회 메서드
    public boolean getCommentLike(Long commentId, Long userId) {
        return commentLikeRepository.findByCommentIdAndUserId(commentId, userId).isPresent();
    }

    // 댓글 좋아요 생성 메서드
    @Transactional
    public CommentLikeResponseDto createCommentLike(Long commentId, Long userId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다.", commentId));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 이미 좋아요가 존재하는지 확인
        if (getCommentLike(commentId, userId)) {
            throw new CustomException(ErrorCode.CONFLICT, "이미 좋아요가 존재합니다.", commentId);
        }

        // 좋아요 만들기
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .user(user)
                .build();

        // 좋아요 저장
        CommentLike savedCommentLike = commentLikeRepository.save(commentLike);

        return CommentLikeResponseDto.from(savedCommentLike);
    }

    // 댓글 좋아요 삭제 메서드
    @Transactional
    public void deleteCommentLike(Long commentId, Long userId) {
        // 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다.", commentId));

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 댓글의 모든 좋아요 리스트 조회
        List<CommentLike> commentLikes = commentLikeRepository.findByCommentId(commentId);

        // 유저의 좋아요가 있는지 확인
        boolean userLiked = commentLikes.stream()
                .anyMatch(like -> like.getUser().getId().equals(userId));

        if (!userLiked) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "삭제할 수 있는 좋아요가 없습니다.", userId);
        }

        // 유저의 좋아요 찾기
        CommentLike commentLike = commentLikes.stream()
                .filter(like -> like.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "삭제하려는 좋아요를 찾을 수 없습니다.", commentId));

        // 좋아요 삭제
        commentLikeRepository.delete(commentLike);
    }
}
