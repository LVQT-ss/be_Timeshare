package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.LoginRequestDTO;
import tech.rent.be.dto.LoginResponseDTO;
import tech.rent.be.dto.RegisterRequestDTO;
import tech.rent.be.entity.Users;
import tech.rent.be.exception.DuplicateException;
import tech.rent.be.repository.UsersRepository;
import tech.rent.be.utils.TokenHandler;

@Service
public class AuthenticationService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenHandler tokenHandler;

    public Users register(Users users){
        String rawPassword = users.getPassword();
        users.setPassword(passwordEncoder.encode(rawPassword));
        //encode
        try {
            return usersRepository.save(users);
        }catch (DataIntegrityViolationException e){
            throw new DuplicateException("Duplicate Email");
        }
    }
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequestDTO.getUsername(),
                    loginRequestDTO.getPassword()
            ));
            // acoount chuan
            Users users = (Users) authentication.getPrincipal();
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setId(users.getId());
            loginResponseDTO.setFullName(users.getFullname());
            loginResponseDTO.setUsername(users.getUsername());
            loginResponseDTO.setToken(tokenHandler.generateToken(users));
            return loginResponseDTO;
        }catch (Exception e){
            e.printStackTrace();
            throw new BadCredentialsException("Username or password invalid");
        }
    }
}