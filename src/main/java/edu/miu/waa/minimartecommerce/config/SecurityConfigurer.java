package edu.miu.waa.minimartecommerce.config;

import edu.miu.waa.minimartecommerce.jwt_factory.UserDetailService;
import edu.miu.waa.minimartecommerce.jwt_factory.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
@EnableWebSecurity
@CrossOrigin(origins = {"http://localhost:3000"})
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final UserDetailService myUserDetailsService;
    private final JwtFilter jwtRequestFilter;

    public SecurityConfigurer(UserDetailService myUserDetailsService, JwtFilter jwtRequestFilter) {
        this.myUserDetailsService = myUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(myUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests() //
                .antMatchers("/auth").permitAll()
                // Seller and Buyer registration
                .antMatchers(HttpMethod.POST,"/user/seller").permitAll()
                .antMatchers(HttpMethod.POST,"/user/buyer").permitAll()

                .antMatchers("/order").hasAuthority("SELLER")
                .antMatchers("/order/order-status/update").hasAuthority("SELLER")
                .antMatchers("/order/invoice-status/update").hasAuthority("SELLER")

                // Buyer
                .antMatchers("/user/payment-details").hasAuthority("BUYER")
                .antMatchers("/user/{userId}/payment-details").hasAuthority("BUYER")

                .antMatchers("/order/user/{buyerId}").hasAuthority("BUYER")
                .antMatchers("/order/checkout").hasAuthority("BUYER")


                // Product APIs
                .antMatchers(HttpMethod.POST, "/product").hasAuthority("SELLER")
                .antMatchers(HttpMethod.DELETE, "/product/{id}").hasAuthority("SELLER")
                .antMatchers(HttpMethod.PUT, "/product").hasAuthority("SELLER")
                .antMatchers(HttpMethod.GET, "/product/**").permitAll()

                // Cart APIs
                .antMatchers("/cart/**").hasAuthority("BUYER")

                // Admin APIs
                .antMatchers(HttpMethod.GET,"/user/seller/unapproved/get-all").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/user/seller/{id}/approve").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().sameOrigin(); // to show my database
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
