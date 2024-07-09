package ru.otus.software_architect.eshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import ru.otus.software_architect.eshop.handler.CustomLogoutSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource; // get by Spring

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .authorizeRequests()
                // Here, you are making the public directory on the classpath root available without authentication (e..g. for css files)
                .antMatchers("/public/**", "/registration.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .successHandler((request, response, authentication) -> new DefaultRedirectStrategy().sendRedirect(request, response, "/index"))
                .failureUrl("/login-error.html")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .permitAll();
    }

    // login by user from db
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("SELECT username, password, active FROM usr WHERE username=?")
                .authoritiesByUsernameQuery("SELECT u.username, ur.role FROM usr u INNER JOIN user_roles ur ON u.id = ur.user_id WHERE u.username=?");
    }
    /*-
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                // Spring Security 5 requires specifying the password storage format
                .password("{noop}pass")// password = pass
                .roles("USER");
    }

     */

}
