package com.yuanandrui.blog.web;

import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Comment;
import com.yuanandrui.blog.po.User;
import com.yuanandrui.blog.service.BlogService;
import com.yuanandrui.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentControl {

    private static final String BLOGCOMMENTLIST = "blog :: commentList";
    private static final String REDIRECT_COMMENTS = "redirect:/comments/";

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @Value("${comment.delete}")
    private String deletedComment;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model, HttpSession session){
        List<Comment> commentList = commentService.listCommentByBlogId(blogId);
        User user = (User) session.getAttribute("user");
        Boolean isEmpty = commentList.isEmpty();
        model.addAttribute("isEmpty", isEmpty);
        if(!isEmpty){
            model.addAttribute("comments", commentList);
            if(user != null){
                model.addAttribute("isAdmin", commentList.get(0).getBlog().getUser().getId() == user.getId());
            }
        }
        return BLOGCOMMENTLIST;
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId).get());
        User user = (User) session.getAttribute("user");
        if(user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        }else{
            comment.setAvatar(avatar);
            comment.setAdminComment(false);
        }
        commentService.saveComment(comment);
        return REDIRECT_COMMENTS + blogId;
    }

    @GetMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session){
        User user = (User) session.getAttribute("user");
        System.out.println(id);
        Comment comment = commentService.listCommentById(id);
        Blog blog = comment.getBlog();
        User author = blog.getUser();
        if(user != null && user.getId() == author.getId()){
            commentService.update(deletedComment, comment.getId());
        }
        return REDIRECT_COMMENTS + blog.getId();
    }
}
