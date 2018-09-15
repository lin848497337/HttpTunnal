package g.dao;

import g.model.UserDO;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * @author chengjin.lyf on 2018/8/17 下午2:57
 * @since 1.0.25
 */
public class UserDAO extends SqlSessionDaoSupport{

    public UserDO queryByName(String name){
        return  getSqlSession().selectOne("g.user.selectUserByName", name);
    }

    public UserDO queryByAccount(String account){
        return  getSqlSession().selectOne("g.user.selectUserByAccount", account);
    }

    public void insert(UserDO userDO){
        getSqlSession().insert("g.user.insertUser", userDO);
    }
}
