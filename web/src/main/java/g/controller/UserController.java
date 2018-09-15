package g.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import g.service.UserService;
import g.util.JsonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import g.vo.UserVO;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chengjin.lyf on 2018/8/17 下午1:27
 * @since 1.0.25
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping(value = "v1/user/register", method = RequestMethod.GET)
    @ResponseBody
    public JSON register(@ModelAttribute("form") UserVO userVO, HttpServletRequest request){
        try{
            JsonResponse response = userService.register(userVO);
            return response.toJSON();
        }catch (Exception e){
            return JsonResponse.failed("注册失败，请重试！").toJSON();
        }
    }

    @RequestMapping(value = "v1/user/login", method = RequestMethod.GET)
    @ResponseBody
    public JSON login(@ModelAttribute("form") UserVO userVO, HttpServletRequest request){
        try{
            JsonResponse response = userService.login(userVO);
            return response.toJSON();
        }catch (Exception e){
            return JsonResponse.failed("登录失败，请重试！").toJSON();
        }
    }
}
