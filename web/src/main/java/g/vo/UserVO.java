package g.vo;

import lombok.Data;

/**
 * @author chengjin.lyf on 2018/8/17 下午7:29
 * @since 1.0.25
 */
@Data
public class UserVO {
    private Long id;
    private String account;
    private String password;
    private String email;
    private String phoneNumber;
}
