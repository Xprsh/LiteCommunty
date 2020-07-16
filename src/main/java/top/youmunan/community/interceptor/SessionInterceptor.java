package top.youmunan.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.youmunan.community.mapper.UserMapper;
import top.youmunan.community.model.User;
import top.youmunan.community.model.UserExample;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
            if(token != null){
                UserExample userExample = new UserExample();
                userExample.createCriteria()
                        .andTokenEqualTo(token);
                List<User> users = userMapper.selectByExample(userExample);
                if (users != null && users.size() != 0) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", users.get(0));
                }
            }else {
                // 清除无token，清除session
                request.getSession().removeAttribute("user");
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
