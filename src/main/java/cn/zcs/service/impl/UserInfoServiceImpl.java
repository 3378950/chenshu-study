package cn.zcs.service.impl;

import cn.zcs.pojo.UserInfo;
import cn.zcs.mapper.UserInfoMapper;
import cn.zcs.service.UserInfoService;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
