package flowz.cloudflowz.configuration;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
           
	/*@Configuration
    @Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter{
			
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("user").password("123").authorities("USER");
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .antMatcher("/emailz/**")
                    .authorizeRequests()
                        .anyRequest().hasAnyRole("USER")
                        .and()
                    .httpBasic();
        }
                
    }*/
	
    @Configuration
    @Order(1)
    public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter{
    
        @Autowired
        private DataSource datasource;
    	
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
            userDetailsService.setDataSource(datasource);
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
            auth.jdbcAuthentication().dataSource(datasource);

            if(!userDetailsService.userExists("user")) {
                List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("USER"));
                User userDetails = new User("user", encoder.encode("password"), authorities);

                userDetailsService.createUser(userDetails);
            }
        }  
        
        /*private CsrfTokenRepository csrfTokenRepository() 
    	{ 
    	    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
    	    repository.setSessionAttributeName("_csrf");
    	    return repository; 
    	}*/  	
    	    
        /* need to add csrf for posts */
        
    	@Override
    	protected void configure(HttpSecurity http) throws Exception { 	
    		http
        		.authorizeRequests()
        		.antMatchers("/resources/**").permitAll()
        		.antMatchers("/images/**").permitAll()
        		.antMatchers("/emailz/**").permitAll()
                .anyRequest().authenticated();
    		http
                .formLogin().failureUrl("/login?error")
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .permitAll()
        		.and()
        		.csrf().disable();
        		//.and()
        		//.csrf().csrfTokenRepository(csrfTokenRepository());    		
    	}
    	
    }

    
}