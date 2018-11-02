package com.yuanandrui.blog.service;

import com.yuanandrui.blog.dao.CommentRepository;
import com.yuanandrui.blog.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private List<Comment> tempReplys = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.ASC, "createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentIsNull(blogId, sort);
        return eachComment(comments);
    }

    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for(Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for(Comment comment: comments){
            List<Comment> replys =  comment.getReplyComments();
            for (Comment reply : replys){
                recursively(reply);
            }
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private void recursively(Comment comment) {
        tempReplys.add(comment);
        if(comment.getReplyComments().size() > 0){
            List<Comment> replys = comment.getReplyComments();
            for(Comment reply: replys){
                tempReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1){
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Comment listCommentById(Long id) {
        Comment comment = commentRepository.findById(id).get();
        return comment;
    }

    @Override
    public void update(String commentContent, Long commentId) {
        commentRepository.updateCommentById(commentContent, commentId);
    }
}
