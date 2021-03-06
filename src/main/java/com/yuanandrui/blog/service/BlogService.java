package com.yuanandrui.blog.service;

import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Comment;
import com.yuanandrui.blog.po.User;
import com.yuanandrui.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BlogService {
    Optional<Blog> getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable p, BlogQuery blog);

    Page<Blog> listBlogPageable(Pageable p, BlogQuery blog);

    Page<Blog> listBlog(Pageable p);

    Page<Blog> listBlog(Pageable p, String query);

    Page<Blog> listBlog(Pageable p, Long tagId);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

}
