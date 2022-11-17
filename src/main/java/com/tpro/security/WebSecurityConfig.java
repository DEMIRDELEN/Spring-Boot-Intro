package com.tpro.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // konfigürasyon yapacağımı söylüyorum
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable(). // csrf() koruması iptal edildi ,API de ihtiyaç olmadığı için
			authorizeHttpRequests().
			antMatchers("/","index.html","/css/*","/js/*").permitAll(). // bu end-pointlere security uygulanmasın
			anyRequest().
			authenticated().
			and().
			httpBasic();  // Basic authebtication yapacağımı belirttim
			
		}
		
		/*
		 *
		
		// InMemory olarak userları oluşturuyoruz
		@Override
		@Bean
		protected UserDetailsService userDetailsService() {
		
			UserDetails userIsmail = User.builder().
					username("ismail").
					password(passwordEncoder().encode("akdogan")).
					roles("ADMIN").
					build();
			UserDetails userSuleyman = User.builder().
					username("suleyman").
					password(passwordEncoder().encode("sari")).
					roles("STUDENT").
					build();
			UserDetails userTarik = User.builder().
					username("tarik").
					password(passwordEncoder().encode("kose")).
					roles("STUDENT","ADMIN").
					build();
			
			return new InMemoryUserDetailsManager(new UserDetails[] {userIsmail,userSuleyman,userTarik});
			
			
		}
		*/
		
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(authProvider());
		}
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder(10);
		}
		
		
		@Bean
		public DaoAuthenticationProvider authProvider() {
			DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
			// kullanıcı bilgileri
			authProvider.setUserDetailsService(userDetailsService);
			//kullanılacak encoder-decoder metodu belirliyoruz
			authProvider.setPasswordEncoder(passwordEncoder());
			return authProvider;
		}
			
}