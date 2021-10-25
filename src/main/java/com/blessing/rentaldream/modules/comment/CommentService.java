package com.blessing.rentaldream.modules.comment;

import com.blessing.rentaldream.modules.account.domain.Account;
import com.blessing.rentaldream.modules.account.service.AccountService;
import com.blessing.rentaldream.modules.post.PostService;
import com.blessing.rentaldream.modules.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AccountService accountService;
    private final PostService postService;

    public List<Comment> loadPostComments(Post post){
        return commentRepository.findByPost(post);
    }

    public void saveComment(CommentForm commentForm) {
        Account account = accountService.findAccountById(commentForm.getWriter()).get();
        Post post = postService.findPostById(commentForm.getPost()).get();
        Comment comment = Comment.createNewComment(account,post, commentForm.getContent());
        commentRepository.save(comment);

    }

    Long removeComment(Account account, Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        commentOptional.orElseThrow(IllegalArgumentException::new);
        Comment comment = commentOptional.get();
        Long postId = comment.getPost().getId();
        if(!comment.isWriter(account))
            throw new AccessDeniedException("권한이 없습니다.");
        commentRepository.delete(comment);
        return postId;
    }
}
