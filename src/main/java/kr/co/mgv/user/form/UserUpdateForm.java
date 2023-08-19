package kr.co.mgv.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class UserUpdateForm {

    private String id;
    private String name;
    private Date birth;
    private String email;
    private String zipcode;
    private String bgAddress;
    private String dtAddress;
}
