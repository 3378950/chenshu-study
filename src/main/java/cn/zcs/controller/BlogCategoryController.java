package cn.zcs.controller;

import cn.zcs.pojo.BlogCategory;
import cn.zcs.service.BlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenshu
 * @since 2021-03-03
 */
@Controller
public class BlogCategoryController {

    @Autowired
    BlogCategoryService blogCategoryService;

    @PostMapping("/blog/tag")
    public String addTag(String tagName) {
        BlogCategory blogCategory = new BlogCategory();

        blogCategory.setCategory(tagName);

        blogCategoryService.save(blogCategory);

        return "blog/write";
    }

}

