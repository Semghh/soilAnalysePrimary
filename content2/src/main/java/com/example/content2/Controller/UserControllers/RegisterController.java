package com.example.content2.Controller.UserControllers;

import com.example.content2.POJO.SoilAnalyse.Peasant;
import com.example.content2.POJO.SoilAnalyse.Result;
import com.example.content2.POJO.SoilAnalyse.Users;
import com.example.content2.Service.Impl.PeasantServiceImpl;
import com.example.content2.Service.Impl.UsersServiceImpl;

import com.example.content2.Util.CheckId_cardLegality;
import com.example.content2.Util.StringLengthGreaterThanZero;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class RegisterController {
    private Log log = LogFactory.getLog(this.getClass());
    private UsersServiceImpl usersService;
    private PeasantServiceImpl peasantService;

    @Autowired
    public RegisterController(UsersServiceImpl UsersService, PeasantServiceImpl peasantService) {
        this.usersService = UsersService;
        this.peasantService = peasantService;
    }

    /**
     * 检查是否username重复
     *
     * @param map
     * @return
     */
    @PostMapping("/checkRepeatUsername")
    public Result checkRepeatUsername(@RequestBody HashMap map) {
        String username = (String) map.get("username");

        return usersService.checkRepeatUsername(username);
    }

    /**
     * 检查身份证号是否重复
     *
     * @param map
     * @return
     */
    @PostMapping("/checkRepeatId_card")
    public Result checkRepeatId_card(@RequestBody HashMap map) {
        String id_card = (String) map.get("id_card");

        return checkRepeatId_card(id_card);
    }

    /**
     * 真正的注册接口
     *
     * @param params
     * @return
     */
    @PostMapping("/registerUsers")
    public Result registerUsers(@RequestBody HashMap params) {

        String username = (String) params.get("username");
        String password = (String) params.get("password");
        String id_card = (String) params.get("id_card");
        String peasantName = (String) params.get("peasantName");

        HashMap<Object, Object> returnMap = new HashMap();

        //检查 用户名 密码 身份证 人名是否都不空
        if (StringLengthGreaterThanZero.judge(username, password, id_card, peasantName)) {
            //都不空

            //身份证合法性检测
            if (!CheckId_cardLegality.judge(id_card)) {
                //不合法
                returnMap.put("registerResult", "failure");
                returnMap.put("id_card", id_card);
                return Result.getInstance(403, "身份证号不合法", returnMap);
            }
            //合法
            Peasant Peasant = new Peasant(null, peasantName, id_card);
            Users Users = new Users(null, username, password, null, "peasant");
//            Users.setPassword_(BCrypt.hashpw(Users.getPassword_(), BCrypt.gensalt()));//使用BCrypt存储
            return register(Users, Peasant, returnMap);
        }
        //有空
        return Result.getInstance(400, "注册信息含有空项。后端收到的全部信息如下", params);
    }


    private Result checkRepeatId_card(String id_card) {

        if (!StringLengthGreaterThanZero.judge(id_card)) {
            return Result.getInstance(403, "身份证号为空", null);
        }

        if (!CheckId_cardLegality.judge(id_card)) {
            return Result.getInstance(406, "身份证号 不合法", null);
        }

        int existsId_card = peasantService.isExistsId_card(id_card);
        if (existsId_card == 0) {
            return Result.getInstance(200, "恭喜你,未被注册", null);
        }

        log.info("URI: /checkRepeatId_card  Duplicate id_card : [" + id_card + "]");
        return Result.getInstance(207, "该身份证已被注册", null);
    }

    private Result register(Users u, Peasant p, HashMap<Object, Object> returnMap) {

        // 检查身份证号
        Result result = checkRepeatId_card(p.getId_card());
        if (result.getCode() != 200) {
            return result;
        }
        //检查帐号
        Result result1 = usersService.checkRepeatUsername(u.getUsername_());
        if (result.getCode() != 200) {
            return result1;
        }


        //注册Peasant
        int i = peasantService.registerPeasant(p);
        if (i != 1) {
            returnMap.put("user", u);
            returnMap.put("peasant", p);
            returnMap.put("registerResult", "failure");
            Result unknownResult = Result.getInstance(406, "注册失败,未知错误请联系管理员", returnMap);
            log.warn(unknownResult);
            return unknownResult;
        }

        //设置外键
        Integer peasantId = peasantService.getIdByIdCard(p.getId_card());
        u.setPeasant_id(peasantId);

        //注册user
        int j = usersService.registerNewUser(u);
        if (j != 1) {
            returnMap.put("registerResult", "failure");
            returnMap.put("id_card", p.getId_card());
            return Result.getInstance(406, "未知错误，请联系管理员", null);
        }
        return Result.getInstance(201, "恭喜你注册成功", null);
    }
}
