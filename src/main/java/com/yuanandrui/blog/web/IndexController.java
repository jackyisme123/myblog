package com.yuanandrui.blog.web;

import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Tag;
import com.yuanandrui.blog.po.Type;
import com.yuanandrui.blog.service.BlogService;
import com.yuanandrui.blog.service.TagService;
import com.yuanandrui.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.yuanandrui.blog.util.SomeNumber.*;

@Controller
public class IndexController {

    private static final String INDEX = "index";
    private static final String SEARCH = "search";
    private static final String BLOG = "blog";
    private static final String FRAGMENTSNEWBLOGLIST = "_fragments :: newblogList";

    @Value("${wechat.tip}")
    private String wechat_tip;

    @Value("${alipay.tip}")
    private String alipay_tip;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = BLOGNUMBERS, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model)    {
        model.addAttribute("page", blogService.listBlog(pageable));
        List<Type> types = typeService.listTypeTop(HUGENUMBER);
        for(Type type:types){
            List<Blog> typeBlogs = type.getBlogs();
            int countType = 0;
            int typeNum = 0;
            for(Blog typeBlog : typeBlogs){
                if(typeBlog.isPublished()){
                    countType++;
                }
            }
            if(countType!=0){
                typeNum++;
            }
            type.setPublishedBlogNum(countType);
            if(typeNum > BLOGTYPES){
                break;
            }
        }
        model.addAttribute("types", types);
        List<Tag> tags = tagService.listTagTop(HUGENUMBER);
        for(Tag tag : tags){
            int tagNum = 0;
            int count = 0;
            List<Blog> blogs = tag.getBlogs();
            for(Blog blog :blogs){
                if(blog.isPublished()){
                    count++;
                }
            }
            if(count != 0){
                tagNum++;
            }
            tag.setPublishedBlogNum(count);
            if(tagNum >= BLOGTAGS){
                break;
            }
        }
        model.addAttribute("tags", tags);
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(RECOMMENDBLOGNUMBER));
        return INDEX;
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = BLOGNUMBERS, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                         Model model,
                         @RequestParam String query){
        model.addAttribute("page", blogService.listBlog(pageable, "%"+query+"%"));
        model.addAttribute("query", query);
        return SEARCH;
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        model.addAttribute("alipay_tip", alipay_tip);
        model.addAttribute("wechat_tip", wechat_tip);
        return BLOG;
    }


    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs", blogService.listRecommendBlogTop(RECOMMENDBLOGNUMBER));
        return FRAGMENTSNEWBLOGLIST;
    }

}
