package cn.gjy.news361.web;


import cn.gjy.news361.dao.ChooseDAO;
import cn.gjy.news361.pojo.User;
import cn.gjy.news361.service.UserService;
import cn.gjy.news361.util.AesCbcUtil;
import cn.gjy.news361.util.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    ChooseDAO chooseDAO;
    /**
     *     * @Title: decodeUserInfo
     *     * @author：lizheng
     *     * @date：2018年3月25日
     *     * @Description: 解密用户敏感数据
     *     * @param encryptedData 明文,加密数据
     *
     * @param iv   加密算法的初始向量
     * @param code 用户允许登录后，回调内容会带上 code（有效期五分钟），开发者需要将 code 发送到开发者服务器后台，使用code 换取 session_key api，将 code 换成 openid 和 session_key
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @PostMapping("/decodeUserInfo")
    @ResponseBody
    public Map decodeUserInfo(String encryptedData, String iv, String code) {

        Map map = new HashMap();

        // 登录凭证不能为空
        if (code == null || code.length() == 0) {
            map.put("status", 0);
            map.put("msg", "code 不能为空");
            return map;
        }

        // 小程序唯一标识 (在微信小程序管理后台获取)
        String wxspAppid = "wx6717c031e8e4520f";
        // 小程序的 app secret (在微信小程序管理后台获取)
        String wxspSecret = "a81f0d501ef18bd067d7ebf37d1e2aa5";
        // 授权（必填）
        String grant_type = "authorization_code";

        //////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        //////////////// ////////////////
        // 请求参数
        String params = "appid=" + wxspAppid + "&secret=" + wxspSecret + "&js_code=" + code + "&grant_type="
                + grant_type;
        // 发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        JSONObject json = JSON.parseObject(sr);
        // 获取会话密钥（session_key）
        String session_key = json.get("session_key").toString();
        // 用户的唯一标识（openid）
        String openid = (String) json.get("openid");

        //////////////// 2、对encryptedData加密数据进行AES解密 ////////////////
        try {
            String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
            if (null != result && result.length() > 0) {
                map.put("status", 1);
                map.put("msg", "解密成功");

                JSONObject userInfoJSON = JSON.parseObject(result);
                Map userInfo = new HashMap();
                userInfo.put("openId", userInfoJSON.get("openId"));
                userInfo.put("nickName", userInfoJSON.get("nickName"));
                userInfo.put("gender", userInfoJSON.get("gender"));
                userInfo.put("city", userInfoJSON.get("city"));
                userInfo.put("province", userInfoJSON.get("province"));
                userInfo.put("country", userInfoJSON.get("country"));
                userInfo.put("avatarUrl", userInfoJSON.get("avatarUrl"));
                // 解密unionId & openId;

                userInfo.put("unionId", userInfoJSON.get("unionId"));
                map.put("userInfo", userInfo);

                //判断是否为新用户
                if(userService.existsUser(openid)){
                    userInfo.put("firstLogin", false);
                }else {
                    userService.createUser(new User(openid));
                    userInfo.put("firstLogin", true);
                }

            } else {
                map.put("status", 0);
                map.put("msg", "解密失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    @GetMapping("/user/{openId}")
    public User getUser(@PathVariable String openId){
        return userService.findUser(openId);
    }
//    @GetMapping("/checkUser")
//    public Object checkUser(String openId){
//
//    }
}
