package cn.zcs.controller;


import cn.zcs.pojo.Blog;
import cn.zcs.pojo.BlogCategory;
import cn.zcs.service.BlogCategoryService;
import cn.zcs.service.BlogService;
import cn.zcs.utils.MyUtils;
import cn.zcs.vo.QuestionWriteForm;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenshu
 * @since 2021-02-18
 */
@Controller
public class BlogController {

    @Autowired
    BlogCategoryService blogCategoryService;

    @Autowired
    BlogService blogService;


    @GetMapping("/blog")
    public String toBlogList(Model model) {

        Page<Blog> pageParm = new Page<>(1, 10);
        blogService.page(pageParm, new QueryWrapper<Blog>().orderByDesc("gmt_create"));

        List<Blog> blogList = pageParm.getRecords();
        model.addAttribute("blogList", blogList);
        model.addAttribute("pageParm", pageParm);

        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);

        return "blog/list";
    }

    @GetMapping("/blog/{page}/{limit}")
    public String blogList(@PathVariable("page") int page, @PathVariable("limit") int limit, Model model) {
        if(page < 1) {
            page = 1;
        }

        Page<Blog> pageParm = new Page<>(page, limit);

        blogService.page(pageParm, new QueryWrapper<Blog>().orderByDesc("gmt_create"));

        List<Blog> blogList = pageParm.getRecords();
        model.addAttribute("blogList", blogList);
        model.addAttribute("pageParm", pageParm);

        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);

        return "blog/list";
    }

    @GetMapping("/blog/write")
    public String toBlogWrite(Model model) {
        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList", categoryList);
        return "blog/write";
    }

    @PostMapping("/blog/write")
    public  synchronized String writeBlog(QuestionWriteForm questionWriteForm) {
        Blog blog = new Blog();

        blog.setBid(MyUtils.getUuid());
        blog.setTitle(questionWriteForm.getTitle());
        blog.setContent(questionWriteForm.getContent());
        blog.setSort(0);
        blog.setViews(0);

        blog.setAuthorId(questionWriteForm.getAuthorId());
        blog.setAuthorName(questionWriteForm.getAuthorName());
        blog.setAuthorAvatar(questionWriteForm.getAuthorAvatar());

        BlogCategory category = blogCategoryService.getById(questionWriteForm.getCategoryId());
        blog.setCategoryId(questionWriteForm.getCategoryId());
        blog.setCategoryName(category.getCategory());
        blog.setGmtCreate(MyUtils.getTime());
        blog.setGmtUpdate(MyUtils.getTime());

        blogService.save(blog);

        return "redirect:/blog";
    }

    @GetMapping("/blog/read/{bid}")
    public String read(@PathVariable("bid") String bid, Model model) {
        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));

        blog.setViews(blog.getViews() + 1);
        blogService.updateById(blog);
        model.addAttribute("blog", blog);


        return "blog/read";
    }

    @GetMapping("/blog/editor/{uid}/{bid}")
    public synchronized String toEditor(@PathVariable("uid") String uid, @PathVariable("bid") String bid, Model model) {
        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));

        if (!blog.getAuthorId().equals(uid)){
            MyUtils.print("禁止非法编辑");
            return "redirect:/blog";
        }

        model.addAttribute("blog",blog);

        List<BlogCategory> categoryList = blogCategoryService.list(null);
        model.addAttribute("categoryList",categoryList);

        return "blog/editor";
    }

    @PostMapping("/blog/editor")
    public String editor(Blog blog) {
        Blog queryBlog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", blog.getBid()));
        queryBlog.setTitle(blog.getTitle());
        queryBlog.setCategoryId(blog.getCategoryId());
        queryBlog.setContent(blog.getContent());
        queryBlog.setGmtUpdate(MyUtils.getTime());

        blogService.updateById(queryBlog);

        return "redirect:/blog/read/"+blog.getBid();
    }

    @GetMapping("/blog/delete/{uid}/{bid}")
    public String delete(@PathVariable("uid") String uid, @PathVariable("bid") String bid) {

        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("bid", bid));

        if(!blog.getAuthorId().equals(uid)) {
            MyUtils.print("非法删除");
            return "redirect:/blog";
        }

        blogService.removeById(blog);

        return "redirect:/blog";
    }




}

