package com.talk.demo.controller;

import com.talk.demo.dto.AccessTokenDto;
import com.talk.demo.dto.GithubUser;
import com.talk.demo.dto.User;
import com.talk.demo.mapper.UserMapper;
import com.talk.demo.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeCotroller {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/callback")
    public String callback(@RequestParam(name="code") String code, @RequestParam(name="state") String state, HttpServletRequest request, Model model) {
        AccessTokenDto dto = new AccessTokenDto();
        dto.setClient_id(clientId);
        dto.setClient_secret(clientSecret);
        dto.setCode(code);
        dto.setState(state);
        dto.setRedirected_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(dto);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        /*System.out.println(user);*/
        if(githubUser != null) {
            request.getSession().setAttribute("user", githubUser);
            User user = new User();
            user.setAccountId(githubUser.getId().toString());
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            userMapper.insertUser(user);
            return "redirect:/";
        }
        else {
            return "redirect:/";
        }
    }
}
