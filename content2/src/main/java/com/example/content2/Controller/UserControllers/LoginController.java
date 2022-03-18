package com.example.content2.Controller.UserControllers;

import com.example.content2.POJO.Result;
import com.example.content2.POJO.Users;
import com.example.content2.Service.UsersService;
import com.example.content2.Util.StringLengthGreaterThanZero;
import com.example.content2.Util.TokenStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class LoginController {
    private UsersService usersService;
    private Log log = LogFactory.getLog(this.getClass());
    private ConcurrentHashMap<String, TokenStore> tokenMapping;

    @Autowired
    public LoginController(UsersService usersService
            , @Qualifier("tokenMapping") ConcurrentHashMap<String, TokenStore> tokenMapping) {
        this.usersService = usersService;
        this.tokenMapping = tokenMapping;
    }

    @PostMapping("/login")
    public Result Login(@RequestBody HashMap params, HttpServletRequest request, HttpServletResponse response) throws IOException {

        Boolean cookieLogin = (Boolean) params.get("isCookieLogin");
        Boolean rememberMe = (Boolean) params.get("rememberMe");

        LoginHelper loginHelper = new LoginHelper(usersService, log, tokenMapping);
        return loginHelper.methodHandler(cookieLogin, rememberMe, params, request, response);

    }


    //为了并发，提取出来的内部类，用于执行login具体操作。
    static class LoginHelper {
        private String username;
        private HashMap data = null;
        private String msg;
        private int code = 400;
        private UsersService usersService;
        private Log log;
        private final ConcurrentHashMap<String, TokenStore> tokenMapping;

        public LoginHelper(UsersService usersService, Log log, ConcurrentHashMap<String, TokenStore> tokenMapping) {
            this.usersService = usersService;
            this.log = log;
            this.tokenMapping = tokenMapping;
        }


        private boolean cookieLogin(HttpServletRequest request) {
            HashMap<String, Object> map = new HashMap<>();
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("u")) {
                        this.username = c.getValue();
                        if (usersService.isExistsUsername(this.username) == 0) break;//不存在用户名就返回202失败
                        map.put("loginResult", true);
                        map.put("username", this.username);
                        this.code = 201;
                        this.msg = "登陆成功";
                        data = map;
                        System.out.println("已通过 Cookie 认证");
                        return true;
                    }
                }
            }
            this.code = 202;
            this.msg = "登录信息过期，请重新登录";
            return false;
        }

        //帐号密码登录
        private boolean accountLogin(String username, String password) {

            HashMap<String, String> map = new HashMap<>();

            if (!StringLengthGreaterThanZero.judge(username, password)) {
                map.put("loginResult", "nullUsernameOrPassword");
                map.put("username", username);
                map.put("password", password);
                this.code = 202;
                this.msg = "帐号或密码不能为空";
                this.data = map;
                return false;
            }

            Users user = new Users();
            user.setUsername_(username);
            user.setPassword_(password);

            if (usersService.checkLoginPass(user.getPassword_(), user.getUsername_())) {
                map.put("loginResult", "true");
                this.username = username;
                data = map;
            } else {
                map.put("loginResult", "false");
                this.code = 202;
                this.msg = "帐号或密码错误";
                this.data = map;
                return false;
            }
            this.msg = "登陆成功";
            this.code = 200;
            log.info("已通过 帐号密码认证   [username] ： " + this.username);
            return true;
        }

        //登陆成功
        private Result succeed(Boolean rememberMe, HttpServletRequest request, HttpServletResponse response) {
            //如果带有rememberMe参数，则增加Cookie
            if (rememberMe != null && rememberMe) {
                Cookie cookie = new Cookie("u", this.username);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24 * 3);
                response.addCookie(cookie);
            }


            //session中增加已登录标记
            HttpSession session = request.getSession();
            String rolesByUsername = usersService.getRolesByUsername(this.username);
            session.setAttribute("loginResult", "true");
            session.setAttribute("roles",rolesByUsername);
            data.put("username", this.username);
            data.put("roles", rolesByUsername);

            String uuid = UUID.randomUUID().toString();
            TokenStore tokenStore = new TokenStore(this.username, rolesByUsername, System.currentTimeMillis());
            data.put("token", uuid);
            tokenMapping.put(uuid, tokenStore);

            return Result.getInstance(code, msg, data);
        }

        private Result failure() {
            return Result.getInstance(code, msg, data);
        }

        public Result methodHandler(Boolean cookieLogin, Boolean rememberMe, HashMap params, HttpServletRequest request, HttpServletResponse response) {
            msg = null;
            data = null;

            //是否cookie登录
            if (cookieLogin != null && cookieLogin) {
                if (cookieLogin(request)) {
                    //登录成功
                    return succeed(rememberMe, request, response);
                }
                //检查后返回，不进行accountLogin检查
                return failure();
            }

            //accountLogin登录
            String username = (String) params.get("username");
            String password = (String) params.get("password");
            if (accountLogin(username, password)) {
                //登录成功
                return succeed(rememberMe, request, response);
            }
            //登录失败
            return failure();
        }
    }
}
