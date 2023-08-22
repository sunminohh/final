package kr.co.mgv.user.form;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Alias("updateForm")
@Data
public class UserUpdateForm {

    private String name;
    private String checkPassword;
    private String newPassword;
    private LocalDate birth;
    private String email;
    private String zipcode;
    private String address;
}
