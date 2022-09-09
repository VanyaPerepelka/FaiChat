package service.userService;

import dto.UserDTO;
import org.springframework.messaging.simp.user.UserRegistryMessageHandler;
import org.springframework.stereotype.Service;
import repo.UsersRepository;
import service.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultUserService implements UserService{

    UsersRepository usersRepository;
    UserConverter userConverter;

    @Override
    public UserDTO saveUser(UserDTO userDTO){
        if(validateUserDTO(userDTO)){
            usersRepository.save(userConverter.fromUserDTOtoUser(userDTO));
        }
        return userDTO;
    }

    private boolean validateUserDTO(UserDTO userDTO) {
        //can't spend time for validation bureaucrat, but promise me - I can! :)
        return userDTO != null;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        var toDelete = usersRepository.findById(userId);
        if (toDelete.isPresent()) {
            usersRepository.deleteById(userId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        return userConverter.fromUserToUserDTO(usersRepository.findByEmail(email));
    }

    @Override
    public List<UserDTO> findAll() {
        return usersRepository.findAll()
                .stream()
                .map(userConverter::fromUserToUserDTO)
                .collect(Collectors.toList());
    }
}
