package com.yuanandrui.blog.service;

import com.yuanandrui.blog.po.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    Comment listCommentById(Long id);

    void update(String commentContent, Long commentId);
}
