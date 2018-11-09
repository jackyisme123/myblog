package com.yuanandrui.blog.web.admin;

import com.yuanandrui.blog.po.Tag;
import com.yuanandrui.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import static com.yuanandrui.blog.util.SomeNumber.TAGSNUMBER;

@Controller
@RequestMapping("/admin")
public class TagController {

    private static final String TAGS = "admin/tags";
    private static final String TAGSINPUT = "admin/tags-input";
    private static final String REDIRECT_TAGS = "redirect:/admin/tags";

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = TAGSNUMBER,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page",tagService.listTag(pageable));
        return TAGS;
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return TAGSINPUT;
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id).get());
        return TAGSINPUT;
    }


    @PostMapping("/tags")
    public String post(@Valid Tag tag,BindingResult result, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","Tag name has existed.");
        }
        if (result.hasErrors()) {
            return TAGSINPUT;
        }
        Tag t = tagService.saveTag(tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "Fail to add a tag");
        } else {
            attributes.addFlashAttribute("message", "Success to add a tag");
        }
        return REDIRECT_TAGS;
    }


    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","Tag name has existed.");
        }
        if (result.hasErrors()) {
            return TAGSINPUT;
        }
        Tag t = tagService.updateTag(id,tag);
        if (t == null ) {
            attributes.addFlashAttribute("message", "Fail to add a tag");
        } else {
            attributes.addFlashAttribute("message", "Success to add a tag");
        }
        return REDIRECT_TAGS;
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "Success to delete");
        return REDIRECT_TAGS;
    }


}
