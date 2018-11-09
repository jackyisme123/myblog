package com.yuanandrui.blog.web;

import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Tag;
import com.yuanandrui.blog.po.Type;
import com.yuanandrui.blog.service.BlogService;
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

import java.util.List;

import static com.yuanandrui.blog.util.SomeNumber.HUGENUMBER;
import static com.yuanandrui.blog.util.SomeNumber.TYPEBLOGSNUMBER;

@Controller
public class TypeShowController {

    private static final String CATEGORY = "category";

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id,
                        @PageableDefault(size = TYPEBLOGSNUMBER, sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        List<Type> types = typeService.listTypeTop(HUGENUMBER);
        if(id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        int total = 0;
        for(Type type : types){
            int count = 0;
            List<Blog> blogs = type.getBlogs();
            for(Blog blog :blogs){
                if(blog.isPublished()){
                    count++;
                }
            }
            if(count != 0){
                total++;
            }
            type.setPublishedBlogNum(count);
        }
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.listBlogPageable(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        model.addAttribute("total", total);
        return CATEGORY;
    }
}
