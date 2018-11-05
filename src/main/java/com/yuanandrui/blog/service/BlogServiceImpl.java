package com.yuanandrui.blog.service;

import com.yuanandrui.blog.NotFoundException;
import com.yuanandrui.blog.dao.BlogRepository;
import com.yuanandrui.blog.dao.CommentRepository;
import com.yuanandrui.blog.po.Blog;
import com.yuanandrui.blog.po.Comment;
import com.yuanandrui.blog.po.Type;
import com.yuanandrui.blog.po.User;
import com.yuanandrui.blog.util.MarkdownUtils;
import com.yuanandrui.blog.util.MyBeanUtils;
import com.yuanandrui.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Optional<Blog> getBlog(Long id) {
        return blogRepository.findById(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Optional<Blog> b = blogRepository.findById(id);
        if(!b.isPresent()){
            throw new NotFoundException("The blog is not existing");
        }
        Blog blog = b.get();
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog, blog1);
        String content = blog.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return blog1;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!= null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable p) {
        return blogRepository.findAll(new Specification<Blog>(){

            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.<Boolean>get("published"), true));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, p);
    }

    @Override
    public Page<Blog> listBlog(Pageable p, String query) {
        return blogRepository.findByQuery(query, p);
    }

    @Override
    public Page<Blog> listBlog(Pageable p, Long tagId) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, p);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for(String year: years){
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        Map<String, List<Blog>>  map = archiveBlog();
        Long myCountBlog = Long.valueOf(0);
        for (Map.Entry<String, List<Blog>> entry : map.entrySet())
        {
            myCountBlog += entry.getValue().size();
        }
        return myCountBlog;
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == 0) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Optional<Blog> b = blogRepository.findById(id);
        if (!b.isPresent()){
            throw new NotFoundException("The blog is not existing");
        }
        Blog blog1 = b.get();
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }





}
