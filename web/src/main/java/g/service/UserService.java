package g.service;

import g.dao.UserDAO;
import g.model.UserDO;
import g.util.JsonResponse;
import g.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author chengjin.lyf on 2018/8/17 下午7:32
 * @since 1.0.25
 */
@Service
public class UserService {

    @Resource
    private UserDAO userDAO;
    /**
     * 注册
     * @param userVO
     * @return
     */
    public JsonResponse register(UserVO userVO){
        UserDO userDO = new UserDO();
        userDO.setAccount(userVO.getAccount());
        userDO.setPassword(userVO.getPassword());
        userDAO.insert(userDO);
        return JsonResponse.success();
    }

    public JsonResponse login(UserVO userVO){
        UserDO userDO = userDAO.queryByAccount(userVO.getAccount());
        if (userDO!=null && userDO.getPassword().equalsIgnoreCase(userVO.getPassword())){
            return JsonResponse.success();
        }
        return JsonResponse.failed();
    }

    public UserDO getUserByAccount(String account){
        return userDAO.queryByAccount(account);
    }
}
