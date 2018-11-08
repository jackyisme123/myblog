package com.yuanandrui.blog.web;

import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Tag;
import com.yuanandrui.blog.service.BlogService;
import com.yuanandrui.blog.service.TagService;
import com.yuanandrui.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id,
                        @PageableDefault(size = 6, sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        List<Tag> tags = tagService.listTagTop(10000);
        if(id == -1){
            id = tags.get(0).getId();
        }
        int total = 0;
        for(Tag tag : tags){
            int count = 0;
            List<Blog> blogs = tag.getBlogs();
            for(Blog blog :blogs){
                if(blog.isPublished()){
                    count++;
                }
            }
            if(count != 0){
                total++;
            }
            tag.setPublishedBlogNum(count);
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(pageable, id));
        model.addAttribute("activeTagId", id);
        model.addAttribute("total", total);
        return "tags";
    }
}
