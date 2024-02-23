package tech.rent.be.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.rent.be.dto.UserDTO;
import tech.rent.be.entity.Users;
import tech.rent.be.repository.UsersRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    // Other autowiring and methods

    public UserDTO getUserData(Long userId) {
        Optional<Users> userOptional = usersRepository.findById(userId);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            UserDTO userDTO = new UserDTO();
            // Map fields from user to userDTO
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole()); // Assuming Role is an Enum or similar
            userDTO.setFullname(user.getFullname());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setGender(user.getGender());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setAddress(user.getAddress());
            return userDTO;
        }
        // Handle not found user
        throw new RuntimeException("User not found"); // Or a proper exception handling
    }
}