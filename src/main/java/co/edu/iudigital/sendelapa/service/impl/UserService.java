package co.edu.iudigital.sendelapa.service.impl;
import co.edu.iudigital.sendelapa.model.Role;
import co.edu.iudigital.sendelapa.model.UserApp;
import co.edu.iudigital.sendelapa.repository.IUserRepository;
import co.edu.iudigital.sendelapa.service.iface.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService, IUserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public UserApp findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LOG.info("Starting Session!");
        UserApp user = userRepository.findByUsername(username);

        if(user == null)
        {
            LOG.error(" Cant log In User " + username);

            throw new UsernameNotFoundException("Error in login with credential " + username);

        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<Role> roles = user.getRoles();
        for(Role role : roles)
        {
            GrantedAuthority ga = new SimpleGrantedAuthority(role.getName());
            authorities.add(ga);
        }
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getEnable(),
                true,
                true,
                true,
                authorities
        );
    }
}
