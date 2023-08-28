package kr.co.mgv.user.form;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserFindForm {

    private String id;
    private String name;
    private LocalDate birth;
    private String email;

}
