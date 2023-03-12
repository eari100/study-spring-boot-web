package com.example.studyspringbootweb.resolver;

import com.example.studyspringbootweb.annotation.SocialUser;
import com.example.studyspringbootweb.domain.Users;
import com.example.studyspringbootweb.domain.enums.SocialType;
import com.example.studyspringbootweb.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.studyspringbootweb.domain.enums.SocialType.*;

@RequiredArgsConstructor
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UsersRepository usersRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class) != null
                && parameter.getParameterType().equals(Users.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
        Users users = (Users) session.getAttribute("users");
        return getUsers(users, session);
    }

    private Users getUsers(Users users, HttpSession session) {
        if(users == null) {
            try {
                OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();

                Map<String, Object> map = authentication.getPrincipal().getAttributes();
                Users convertUsers = convertUsers(authentication.getAuthorizedClientRegistrationId(), map);

                users = usersRepository.findByEmail(convertUsers.getEmail());

                if(users == null) users = usersRepository.save(convertUsers);

                setRoleIfNotSame(users, authentication, map);
                session.setAttribute("users", users);
            } catch (ClassCastException e) {
                return users;
            }
        }

        return users;
    }

    private Users convertUsers(String authority, Map<String, Object> map) {
        if(FACEBOOK.isEquals(authority)) return getModernUsers(FACEBOOK, map);
        else if(GOOGLE.isEquals(authority)) return getModernUsers(GOOGLE, map);
        else if(KAKAO.isEquals(authority)) return getKaKaoUsers(map);
        return null;
    }

    private Users getModernUsers(SocialType socialType, Map<String, Object> map) {
        return Users.builder()
                .name(String.valueOf(map.get("name")))
                .email(String.valueOf(map.get("email")))
                .principal(String.valueOf(map.get("id")))
                .socialType(socialType)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private Users getKaKaoUsers(Map<String, Object> map) {
        Map<String, String> propertyMap = (HashMap<String, String>) map.get("properties");
        return Users.builder()
                .name(propertyMap.get("nickname"))
                .email(String.valueOf(map.get("kaccount_email")))
                .principal(String.valueOf(map.get("id")))
                .socialType(KAKAO)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private void setRoleIfNotSame(Users users, OAuth2AuthenticationToken authentication, Map<String, Object> map) {
        if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority(users.getSocialType().getRoleType()))) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(map, "N/A", AuthorityUtils.createAuthorityList(users.getSocialType().getRoleType())));
        }
    }
}
