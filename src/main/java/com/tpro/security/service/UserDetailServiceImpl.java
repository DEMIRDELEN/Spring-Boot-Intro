package com.tpro.security.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.tpro.domain.Role;
import com.tpro.domain.User;
import com.tpro.exception.ResourceNotFoundException;
import com.tpro.repository.UserRepository;
@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		    User user = userRepository.findByUserName(username).orElseThrow(
		    									()-> new ResourceNotFoundException("user not found username: " + username));
	
		    if(user!=null) {
		    	return new org.springframework.security.core.userdetails.
		    			User(user.getUserName(),user.getPassword(),buildGrantedAuthorities(user.getRoles()));
		    } else {
		    	throw new UsernameNotFoundException("User not found username : " + username);
		    }
	
	}
	
	
	// role özelliği security katmanında simpleGrantedAuthority yapısında olması gerekiyor
	
   private static  List<SimpleGrantedAuthority>  buildGrantedAuthorities (final Set<Role> roles) {
	   List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	   for(Role role:roles) {
		   // role enum yapısında olduğu için getName().name() yazdık
		   authorities.add(new SimpleGrantedAuthority(role.getName().name()));
	   }
	  
	   return authorities;
   }
	
}