package com.example.lab9.service;

import com.example.lab9.model.User;
import com.example.lab9.dto.UserDto;
import com.example.lab9.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userDao;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    
    
    public final List<String> imageUrls = Arrays.asList(
                "https://mdbcdn.b-cdn.net/img/new/avatars/8.webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(11).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(10).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(11).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(12).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(13).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(14).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(15).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(16).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(17).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(18).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(19).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(21).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(20).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(22).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(23).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(24).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(25).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(26).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(27).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(28).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(29).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(30).webp"
        );
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: "
                    + username);
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User save(UserDto user) {
        Random rand = new Random();
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setImageUrl(imageUrls.get(rand.nextInt(imageUrls.size())));
        return userDao.save(newUser);
    }

    public boolean isUsernameTaken(String username) {
        return userDao.findByUsername(username) != null;
    }
}
