package higmsa.userservice.controller;

import higmsa.userservice.dto.UserDto;
import higmsa.userservice.jpa.UserEntity;
import higmsa.userservice.service.UserService;
import higmsa.userservice.vo.Greeting;
import higmsa.userservice.vo.RequestUser;
import higmsa.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/user-service")
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final UserService userService;
    private final Greeting greeting;

    @GetMapping("/heath_check")
    public String status(){
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", token expiration time=" + env.getProperty("token.expiration_time"))
//                + ", gateway ip(env)=" + env.getProperty("gateway.ip")
//                + ", gateway ip(value)=" + greeting.getIp()
//                + ", message=" + env.getProperty("greeting.message")
//                + ", token secret=" + greeting.getSecret()
                ;
//        return String.format("It's Working in User Service on PORT %s", env.getProperty("server.port"));
    }

    @GetMapping("/welcome")
    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();
    }


    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(user, UserDto.class);
        userService.createUser(userDto);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();

        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseUser.class));
        });


        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable String userId){
        UserDto userdto = userService.getUserById(userId);

        ResponseUser returnValue = new ModelMapper().map(userdto, ResponseUser.class);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }
}
