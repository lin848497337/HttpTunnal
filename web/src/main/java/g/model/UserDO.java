package g.model;

import lombok.Data;

/**
 * @author chengjin.lyf on 2018/8/17 下午2:57
 * @since 1.0.25
 */
@Data
public class UserDO {
    private Long id;
    private String name;
    private String nickName;
    private String account;
    private String password;
    private String email;
    private String phoneNumber;
}
