package com.yuanandrui.blog.web;

import com.yuanandrui.blog.po.Blog;
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


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PathVariable Long id,
                        @PageableDefault(size = 8, sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        List<Type> types = typeService.listTypeTop(10000);
        if(id == -1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        Map<Integer, Type> typePublishedNum = new TreeMap<>();
        int total = 0;
        for(Type type : types){
            int count = 0;
            for(Blog blog : type.getBlogs()){
                if(blog.isPublished()){
                    count++;
                }
                typePublishedNum.put(count, type);
            }
            total += count;
        }
        model.addAttribute("types", typePublishedNum);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        model.addAttribute("total", total);
        return "category";
    }
}
