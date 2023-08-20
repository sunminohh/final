package kr.co.mgv.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateForm {

    private String id;
    private String name;
    private String password;
    private LocalDate birth;
    private String email;
    private String zipcode;
    private String bgAddress;
    private String dtAddress;
}
