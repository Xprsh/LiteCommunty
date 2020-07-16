package top.youmunan.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.community.Provider.GitHubProvider;
import top.youmunan.community.dto.AccessTokenDTO;
import top.youmunan.community.dto.GitHubUser;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.model.User;
import top.youmunan.community.model.UserExample;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;

    @Autowired
    UserMapper userMapper;

    @Value("${github.redirect.uri}")
    private String redirectId;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request, HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setRedirect_uri(redirectId);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setState(state);
        accessTokenDTO.setCode(code);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDTO);
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(accessToken);
        System.out.println(gitHubUser);

        if (gitHubUser != null && gitHubUser.getLogin() != null) {
            User user = null;
            // 登录成功
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(String.valueOf(gitHubUser.getId()));
            List<User> users = userMapper.selectByExample(example);
            if(users.size() == 0){
                user = new User();
                user.setToken(UUID.randomUUID().toString());
                user.setName(gitHubUser.getLogin());
                user.setAccountId(String.valueOf(gitHubUser.getId()));
                user.setGmtCreate(System.currentTimeMillis());
                user.setGmtModified(user.getGmtCreate());
                user.setAvatarUrl(gitHubUser.getAvatarUrl());
                userMapper.insertSelective(user);
            }else {
                user = users.get(0);
                if(user.getName().equals(gitHubUser.getLogin())){
                    // 已存在用户
                    // 更新信息
                    user = users.get(0);
                    user.setToken(UUID.randomUUID().toString());
                    user.setName(gitHubUser.getLogin());
                    user.setAccountId(String.valueOf(gitHubUser.getId()));
                    user.setGmtModified(System.currentTimeMillis());
                    user.setAvatarUrl(gitHubUser.getAvatarUrl());
                    userMapper.updateByPrimaryKey(user);
                }
            }



//            HttpSession session = request.getSession();
//            session.setAttribute("user", gitHubUser);
            // 使用cookie
            // 重定向，改变地址栏地址
            Cookie cookie = new Cookie("token", user.getToken());
            response.addCookie(cookie);
            System.out.println("userToken:"+user.getToken());
            return "redirect:/";
        } else {
            // 登陆失败
            return "redirect:/";
        }
    }
}
