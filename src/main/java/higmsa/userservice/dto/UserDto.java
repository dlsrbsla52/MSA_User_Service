package higmsa.userservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private String createAt;

    private String encryptedPwd;
}