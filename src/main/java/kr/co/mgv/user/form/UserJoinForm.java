package kr.co.mgv.user.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@NoArgsConstructor
@Getter
@Setter
public class UserJoinForm {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{3,10}", message = "아이디는 영문, 숫자만 가능하며 3~10자리까지 가능합니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,14}", message = "비밀번호는 8자~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(min = 2, message = "이름은 최소 2글자 이상만 가능합니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "유효한 전화번호 형식이 아닙니다.")
    private String tel;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 올바르지 않습니다.")
    private String email;

}
