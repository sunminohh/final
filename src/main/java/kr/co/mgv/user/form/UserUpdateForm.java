package kr.co.mgv.user.form;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserUpdateForm {

    private String checkPassword;
    private String newPassword;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 올바르지 않습니다.")
    private String email;

    private String zipcode;
    private String address;
    private String reason;
}
