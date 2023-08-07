package kr.co.mgv.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    private User user;
    private String roleName;

    @Builder
    public UserRole(User user, String roleName) {
        super();
        this.user = user;
        this.roleName = roleName;
    }
}
