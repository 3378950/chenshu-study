package cn.zcs.service.impl;

import cn.zcs.pojo.User;
import cn.zcs.mapper.UserMapper;
import cn.zcs.pojo.UserRole;
import cn.zcs.service.UserRoleService;
import cn.zcs.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenshu
 * @since 2021-02-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService roleService;
    @Autowired
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));

        session.setAttribute("loginUser", user);

        UserDetails userDetails = null;

        if(user != null) {
            String password = user.getPassword();

            Collection<GrantedAuthority> authorities = getAuthorities(user);

            userDetails = new org.springframework.security.core.userdetails.User(
                    username,
                    password,
                    true,
                    true,
                    true,
                    true,
                    authorities
            );
        }

        return userDetails;
    }

    // 获取角色信息
    private Collection<GrantedAuthority> getAuthorities(User user){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        UserRole role = roleService.getById(user.getRoleId());
        //注意：这里每个权限前面都要加ROLE_。否在最后验证不会通过
        authList.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        return authList;
    }
}
