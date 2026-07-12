package com.airline.auth.config;


import com.airline.auth.entity.User;
import com.airline.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername");

        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email Not Exist"));

        System.out.println("loadUserByUsername : with user details : "+ user.getEmail());
        return new CustomUserDetails(user);
    }
}
