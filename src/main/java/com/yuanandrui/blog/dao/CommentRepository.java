package com.yuanandrui.blog.dao;

import com.yuanandrui.blog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogIdAndParentCommentIsNull(Long blogId, Sort sort);

    @Transactional
    @Modifying
    @Query("update Comment c set c.content=?1 where c.id=?2")
    void updateCommentById(String commentContent, Long commentId);
}
