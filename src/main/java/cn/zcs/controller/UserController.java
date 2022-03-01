package cn.zcs.controller;



import cn.zcs.pojo.UserInfo;
import cn.zcs.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chenshu
 * @since 2021-02-18
 */
@Controller
public class UserController {

    @Autowired
    UserInfoService userInfoService;


}

