package cn.zcs.controller;

import cn.zcs.pojo.Invite;
import cn.zcs.pojo.User;
import cn.zcs.pojo.UserInfo;
import cn.zcs.service.InviteService;
import cn.zcs.service.UserInfoService;
import cn.zcs.service.UserService;
import cn.zcs.utils.MyUtils;
import cn.zcs.vo.RegisterForm;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    UserService userService;
    @Autowired
    UserInfoService userInfoService;

    @Autowired
    InviteService inviteService;


    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterForm registerForm, Model model) {

        if(!registerForm.getPassword().equals(registerForm.getRepassword())) {
            model.addAttribute("registerMsg", "密码输入有误");
            return "register";
        }

        User hasUser = userService.getOne(new QueryWrapper<User>().eq("username", registerForm.getUsername()));
        if(hasUser != null) {
            model.addAttribute("registerMsg", "用户名已存在");
            return "register";
        }

        Invite invite = inviteService.getOne(new QueryWrapper<Invite>().eq("code", registerForm.getCode()));

        if(invite == null) {
            model.addAttribute("registerMsg", "邀请码不存在");
            return "register";
        } else {
            if(invite.getStatus() == 1) {
                model.addAttribute("registerMsg", "邀请码已被使用");
                return "register";
            } else {
                User user = new User();

                user.setUid(MyUtils.getUuid());
                user.setRoleId(2);
                user.setUsername(registerForm.getUsername());

                String bCrptPassword = new BCryptPasswordEncoder().encode(registerForm.getPassword());

                user.setPassword(bCrptPassword);

                user.setGmtCreate(MyUtils.getTime());
                user.setLoginDate(MyUtils.getTime());

                userService.save(user);
                MyUtils.print("新用户注册成功" + user);
                invite.setActiveTime(MyUtils.getTime());
                invite.setStatus(1);
                invite.setUid(user.getUid());
                inviteService.updateById(invite);

                userInfoService.save(new UserInfo().setUid(user.getUid()));

                return "redirect:/toLogin";
            }
        }
    }
}
