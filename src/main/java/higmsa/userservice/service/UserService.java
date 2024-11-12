package higmsa.userservice.service;

import higmsa.userservice.dto.UserDto;
import higmsa.userservice.jpa.UserEntity;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String id);

    Iterable<UserEntity> getUserByAll();
}
