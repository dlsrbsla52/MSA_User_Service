package higmsa.userservice.service;

import higmsa.userservice.dto.UserDto;
import higmsa.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String id);

    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String userName);
}
