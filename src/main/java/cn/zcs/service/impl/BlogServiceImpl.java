package cn.zcs.service.impl;

import cn.zcs.pojo.Blog;
import cn.zcs.mapper.BlogMapper;
import cn.zcs.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenshu
 * @since 2021-02-18
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
