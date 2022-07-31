package com.sutej.broadcast.services;

import com.sutej.broadcast.modals.User;
import com.sutej.broadcast.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional()
    public UserDetails loadUserByUsername(String username){
        Optional<User> userOptional = userRepository.findByuserName(username);
        com.sutej.broadcast.modals.User user = userOptional.orElseThrow( ()-> new UsernameNotFoundException("No user found with: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(), user.getPassword(), user.isEnabled(),
                true, true, true,
                getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
