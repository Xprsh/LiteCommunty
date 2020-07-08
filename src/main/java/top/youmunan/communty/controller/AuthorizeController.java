package top.youmunan.communty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.youmunan.communty.Provider.GitHubProvider;
import top.youmunan.communty.dto.AccessTokenDTO;
import top.youmunan.communty.dto.GitHubUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthorizeController {

    @Autowired
    GitHubProvider gitHubProvider;

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
            // 登录成功
            HttpSession session = request.getSession();
            session.setAttribute("user", gitHubUser);
            // 重定向，改变地址栏地址
            return "redirect:/";
        } else {
            // 登陆失败
            return "index";
        }
    }
}
