package com.webatrio.testjava.services;


import com.webatrio.testjava.config.JwtUtil;
import com.webatrio.testjava.config.UserDetailsServiceImpl;
import com.webatrio.testjava.exceptions.ParticipantException;
import com.webatrio.testjava.exceptions.UserException;
import com.webatrio.testjava.interfaces.AuthenticationInterface;
import com.webatrio.testjava.interfaces.ParticipantMapper;
import com.webatrio.testjava.interfaces.UserMapper;
import com.webatrio.testjava.mapStruct.ParticipantDTO;
import com.webatrio.testjava.mapStruct.UserDTO;
import com.webatrio.testjava.models.Participant;
import com.webatrio.testjava.models.RequestLogin;
import com.webatrio.testjava.models.Role;
import com.webatrio.testjava.models.User;
import com.webatrio.testjava.repositories.ParticipantRepository;
import com.webatrio.testjava.repositories.RoleRepository;
import com.webatrio.testjava.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationInterface {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ParticipantMapper participantMapper;

    @Value("${role.list}")
    private List<String> roles;

    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Scheduled(fixedRate = 86400000)
    private void genererLesRoles(){
        logger.info("Initialisation des roles ");
        roles.stream().forEach(s -> {
            Role role = Role.builder().nom(s).build();
            if(roleRepository.findByNomIgnoreCase(s).isEmpty()){
                roleRepository.save(role);
            }
        });
    }
    @Override
    public String login(RequestLogin requestLogin) throws UserException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestLogin.getUsername(),requestLogin.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Login invalid");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(requestLogin.getUsername());
        User user = userRepository.findByUsername(requestLogin.getUsername()).orElseThrow(()-> new UserException("Username invalid"));

        return jwtUtil.generateToken(user);
    }

    @Override
    public UserDTO creationUser(UserDTO userDTO) {
        try {
            Role role = new Role();
            if (userRepository.findByUsername(userDTO.getUsername()).isEmpty()){
                logger.info("Debut de la création");
                logger.info(userDTO.getRole().getNom());
                if(userDTO.getRole()!= null){
                    role = roleRepository.findByNomIgnoreCase(userDTO.getRole().getNom())
                            .orElseThrow(()-> new IllegalArgumentException("Une erreur s'est produite lors de la récupération du role"));
                    userDTO.setRole(role);
                    if(participantRepository.findByEmail(userDTO.getUsername()).isPresent()){
                        Participant participant = participantRepository.findByEmail(userDTO.getUsername()).orElseThrow(()-> new ParticipantException(""));
                        userDTO.setParticipant(participantMapper.toParticipantDto(participant));
                    }
                }
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                User user = userMapper.toUser(userDTO);
                userRepository.save(user);
                logger.info("Fin de la création");
                User userSave = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(()-> new UserException("Une erreur s'est produite lors de la récupération de l'user"));
                return userMapper.toUserDto(userSave);
            }
            throw new UserException("L'utilisateur existe déjà dans la base des données");
        }catch (UserException | ParticipantException e) {
            logger.error(e.getMessage(), e);
        }

        return userDTO;
    }
}
