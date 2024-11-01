package com.ceos20.instagram.domain.comment.service;

import com.ceos20.instagram.domain.comment.domain.Comment;
import com.ceos20.instagram.domain.comment.dto.CommentRequestDto;
import com.ceos20.instagram.domain.comment.dto.CommentResponseDto;
import com.ceos20.instagram.domain.comment.repository.CommentRepository;
import com.ceos20.instagram.domain.post.domain.Post;
import com.ceos20.instagram.domain.post.repository.PostRepository;
import com.ceos20.instagram.domain.user.domain.User;
import com.ceos20.instagram.domain.user.repository.UserRepository;
import com.ceos20.instagram.global.exception.CustomException;
import com.ceos20.instagram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 생성 메서드 (대댓글 기능 포함)
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long postId, Long userId) {
        Long parentCommentId = requestDto.getParentCommentId(); // 부모 댓글 id

        // 댓글 달 포스트 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "포스트를 찾을 수 없습니다.", postId));


        // 댓글 달 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "유효하지 않은 유저로부터 요청입니다.", userId));

        // 부모 댓글이 있는 경우, 부모 댓글 조회
        Comment parentComment = null;
        if (parentCommentId != null) {
            parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "부모 댓글을 찾을 수 없습니다.", parentCommentId));
        }

        // 댓글 생성 후 저장
        Comment comment = requestDto.toEntity(requestDto, post, user, parentComment); // 부모 댓글 포함
        Comment savedComment = commentRepository.save(comment);

        // 댓글 생성 성공
        return CommentResponseDto.from(savedComment);
    }


    // 특정 게시글의 댓글 전체 조회 메서드
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        // 댓글이 달린 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND,"게시글을 찾을 수 없습니다.", postId));
        // 댓글 조회
        List<Comment> comments = commentRepository.findByPostId(postId);

        // DTO로 변환
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                                                            .map(CommentResponseDto::from)
                                                            .collect(Collectors.toList());
        return commentResponseDtos;
    }

    // 대댓글 조회 메서드
    public List<CommentResponseDto> getRecomments(Long parentCommentId) {
        // 부모 댓글 존재 여부 확인
        Comment parrentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다.", parentCommentId));
        // 대댓글 조회
        List<Comment> recomments = commentRepository.findByParentCommentId(parentCommentId);

        // DTO로 변환
        List<CommentResponseDto> commentResponseDtos = recomments.stream()
                                                        .map(CommentResponseDto::from)
                                                        .collect(Collectors.toList());
        return commentResponseDtos;
    }

    // 댓글 수정 메서드
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, Long userId) {
        // 수정할 댓글 조회
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다.", commentId));

        // 수정 요청한 유저가 댓글의 작성자인지 확인
        if (!existingComment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "댓글을 수정할 권한이 없습니다.", userId);
        }

        // 댓글 수정
        existingComment = Comment.builder()
                .user(existingComment.getUser())
                .post(existingComment.getPost())
                // 댓글 내용 수정
                .content(requestDto.getContent() != null ? requestDto.getContent() : existingComment.getContent())
                .build();

        // 댓글 저장
        Comment savedComment = commentRepository.save(existingComment);
        // 댓글 수정 성공
        return CommentResponseDto.from(savedComment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        // 삭제할 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "댓글을 찾을 수 없습니다.", commentId));

        // 삭제 요청한 유저가 댓글의 작성자인지 확인
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "댓글을 삭제할 권한이 없습니다.", userId);
        }

        // 댓글이 부모 댓글인 경우, 그에 연결된 모든 대댓글 조회
        if (comment.getParentComment() == null) { // 부모 댓글인지 확인
            List<Comment> childComments = commentRepository.findByParentCommentId(commentId);
            // 대댓글 삭제
            for (Comment childComment : childComments) {
                commentRepository.delete(childComment);
            }
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
