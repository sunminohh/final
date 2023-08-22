package kr.co.mgv.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
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
@Alias("User")
public class User implements UserDetails {
    
    private String id;
    private String password;
    private String name;
    private String email;
    private LocalDate birth;
    private String zipcode;
    private String address;
    private Date createDate;
    private Date updateDate;
    private String disabled;
    private List<String> roleName;

    @Builder
    public User(String id, String email, String password, String name, LocalDate birth, Date createDate,
               String zipcode, String address, Date updateDate, String disabled, List<String> roleName) {
        super();
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.birth = birth;
        this.zipcode = zipcode;
        this.address = address;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.disabled = disabled;
        this.roleName = roleName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        
        if (roleName != null) {
        	for (String roleName : roleName) {
        		authorities.add(new SimpleGrantedAuthority(roleName));
        	}
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
