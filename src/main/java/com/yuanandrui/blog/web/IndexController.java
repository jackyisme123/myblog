package com.yuanandrui.blog.web;

import com.yuanandrui.blog.NotFoundException;
import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Tag;
import com.yuanandrui.blog.service.BlogService;
import com.yuanandrui.blog.service.TagService;
import com.yuanandrui.blog.service.TypeService;
import com.yuanandrui.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model)    {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("types", typeService.listTypeTop(6));
        List<Tag> tags = tagService.listTagTop(10000);
        for(Tag tag : tags){
            int tag_num = 0;
            int count = 0;
            List<Blog> blogs = tag.getBlogs();
            for(Blog blog :blogs){
                if(blog.isPublished()){
                    count++;
                }
            }
            if(count != 0){
                tag_num++;
            }
            tag.setPublishedBlogNum(count);
            if(tag_num >= 8){
                break;
            }
        }
        model.addAttribute("tags", tags);
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(8));
        return "index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model,
                         @RequestParam String query){
        model.addAttribute("page", blogService.listBlog(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }


    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
