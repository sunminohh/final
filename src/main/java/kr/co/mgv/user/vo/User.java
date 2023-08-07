package kr.co.mgv.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
    
    private String id;
    private String password;
    private String name;
    private String email;
    private String emailValifiled; // 이메일 인증여부
    private LocalDate birth;
    private Date createDate;
    private Date updateDate;
    private List<String> roleName;

    @Builder
    public User(String id, String email, String password, String name, LocalDate birth, Date createDate, Date updateDate, List<String> roleName) {
        super();
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.roleName = roleName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

        for (String roleName : roleName) {
            authorities.add(new SimpleGrantedAuthority(roleName));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
