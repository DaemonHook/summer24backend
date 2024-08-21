package com.wty.summer24backend.config;

import com.wty.summer24backend.common.enums.CommonStatusEnum;
import com.wty.summer24backend.common.enums.HeaderEnum;
import com.wty.summer24backend.pojo.TokenData;
import com.wty.summer24backend.util.TokenUtils;
import lombok.NonNull;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (DevConfig.ENABLE_SECURITY) {
            http
                    .authorizeRequests()
                    .antMatchers("/swagger-ui.html/**",
                            "/webjars/**",
                            "/swagger-resources/**",
                            "/doc.html").permitAll()
                    .antMatchers("/auth/login",
                            "/auth/register").permitAll() // 允许访问登录和注册页面
                    .anyRequest().authenticated() // 其他请求需要认证
                    .and().exceptionHandling()
                    .authenticationEntryPoint((request, response, authenticationException) ->
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CommonStatusEnum.UNAUTHORIZED.getMessage()))
                    .accessDeniedHandler((request, response, accessDeniedException) ->
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, CommonStatusEnum.FORBIDDEN.getMessage()));
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }
        http.addFilterBefore(new LoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    private static class LoginFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
            TokenData user = getUserInfo(request);
            if (user != null) {
                // 已登录，创建认证对象
                List<SimpleGrantedAuthority> authorities = Arrays.stream(user.permission.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                authorities.addAll(Arrays.stream(user.role.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 未登录，创建匿名认证对象
                AnonymousAuthenticationToken anonymousAuthentication = new AnonymousAuthenticationToken("key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
                SecurityContextHolder.getContext().setAuthentication(anonymousAuthentication);
            }
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 检查权限
     *
     * @param needAuthority 需要的角色或权限
     * @return 是否有权限
     */
    public static boolean checkAuthority(String needAuthority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取请求头中的用户信息
     *
     * @param request 请求对象
     * @return 用户信息
     */
    public static TokenData getUserInfo(HttpServletRequest request) {
        return TokenUtils.getAllInfoFromToken(request.getHeader(HeaderEnum.AUTHORIZATION.getValue()));
    }
}
