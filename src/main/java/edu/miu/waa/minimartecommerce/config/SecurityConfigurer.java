package edu.miu.waa.minimartecommerce.config;

import edu.miu.waa.minimartecommerce.jwt_factory.UserDetailService;
import edu.miu.waa.minimartecommerce.jwt_factory.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                // Seller and Buyer registration
                .antMatchers("/user/seller").permitAll()
                .antMatchers("/user/buyer/save").permitAll()

                // User : Buyer
                .antMatchers("/user/payment-details").hasAuthority("BUYER")
                .antMatchers("/user/{userId}/payment-details").hasAuthority("BUYER")

                // Order : Seller
                .antMatchers("/order").hasAuthority("SELLER")
                .antMatchers("/order/order-status/update").hasAuthority("SELLER")
                .antMatchers("/order/invoice-status/update").hasAuthority("SELLER")

                // Order : Buyer
                .antMatchers("/order/user/{buyerId}").hasAuthority("BUYER")
                .antMatchers("/order/checkout").hasAuthority("BUYER")

                // Product APIs
                .antMatchers(HttpMethod.GET, "/product/**").permitAll()
                .antMatchers(HttpMethod.POST, "/product").hasAuthority("SELLER")
//                .antMatchers( "/product/save").hasAuthority("SELLER")
                .antMatchers(HttpMethod.DELETE, "/product/{id}").hasAuthority("SELLER")
                .antMatchers(HttpMethod.PUT, "/product").hasAuthority("SELLER")

                // Cart APIs
                .antMatchers("/cart/**").hasAuthority("BUYER")

                // Review
                .antMatchers(HttpMethod.GET, "/review/unapproved").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/review/approve/{id}").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/review/{id}").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/review/product/{productId}").permitAll()
                .antMatchers(HttpMethod.POST, "/review").hasAuthority("BUYER")
                .antMatchers(HttpMethod.PUT,"/review").hasAuthority("BUYER")

                // Following
                .antMatchers(HttpMethod.POST, "/user/following").hasAuthority("BUYER")
                .antMatchers(HttpMethod.DELETE,"/user/following/{followerId}").hasAuthority("BUYER")
                .antMatchers(HttpMethod.DELETE,"/user/{userId}/following").hasAuthority("BUYER")

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

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }
}
