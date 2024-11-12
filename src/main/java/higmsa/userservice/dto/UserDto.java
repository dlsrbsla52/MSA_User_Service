package higmsa.userservice.dto;

import higmsa.userservice.vo.ResponseOrder;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private String createAt;

    private String encryptedPwd;

    private List<ResponseOrder> orders;
}
