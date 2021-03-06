package com.yuanandrui.blog.web.admin;

import com.yuanandrui.blog.po.Type;
import com.yuanandrui.blog.service.TypeService;
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

import static com.yuanandrui.blog.util.SomeNumber.TYPESNUMBER;

@Controller
@RequestMapping("/admin")
public class TypeController {

    private static final String TYPES = "admin/types";
    private static final String TYPESINPUT = "admin/types_input";
    private static final String REDIRECT_TYPES = "redirect:/admin/types";

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String list(@PageableDefault(size = TYPESNUMBER, sort = {"id"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return TYPES;
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type", new Type());
        return TYPESINPUT;
    }

    @GetMapping("types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id).get());
        return TYPESINPUT;
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name", "nameError", "Category name has existed.");
        }

        if (result.hasErrors()){
            return TYPESINPUT;
        }

        Type t = typeService.saveType(type);

        if (t == null){
            attributes.addFlashAttribute("message", "Fail to add a type");
        }else{
            attributes.addFlashAttribute("message", "Success to add a type");
        }
        return REDIRECT_TYPES;
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name", "nameError", "Category name has existed.");
        }

        if (result.hasErrors()){
            return TYPESINPUT;
        }

        Type t = typeService.updateType(id, type);

        if (t == null){
            attributes.addFlashAttribute("message", "Fail to update");
        }else{
            attributes.addFlashAttribute("message", "Success to update");
        }
        return REDIRECT_TYPES;
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "Success to delete");
        return REDIRECT_TYPES;
    }
}
