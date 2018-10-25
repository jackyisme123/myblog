package com.yuanandrui.blog.service;

import com.yuanandrui.blog.NotFoundException;
import com.yuanandrui.blog.dao.TagRepository;
import com.yuanandrui.blog.po.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Optional<Tag> getTag(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
//        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findTop(pageable);
    }


    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                String temp = idarray[i];
                Long addTagId;
                if(isNumeric(temp)){
                    addTagId = Long.valueOf(temp);
                }else{
                    if (temp.startsWith("$") && temp.endsWith("&")){
                        temp = temp.substring(1, temp.length()-1);
                    }
                    Tag tag = new Tag();
                    tag.setName(temp);
                    addTagId = saveTag(tag).getId();
                }
                list.add(addTagId);
            }
        }
        return list;
    }

    private final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Optional<Tag> t = tagRepository.findById(id);
        if (!t.isPresent()) {
            throw new NotFoundException("Tag is not existing");
        }
        Tag tag1 = t.get();
        BeanUtils.copyProperties(tag, tag1);
        return tagRepository.save(tag1);
    }


    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
