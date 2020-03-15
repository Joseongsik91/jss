package com.oneplat.oap.mgmt.login.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LoginUser extends User {
    private static final long serialVersionUID = -3531439484732724601L;

    // private final List<OperatorRoleGroup> groups;
    private Long loginUserNumber;
    private String loginUserId;
    private String loginUserName;

    public LoginUser(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
            List<String> groups, Long loginUserNumber, String loginUserId, String loginUserName) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        // this.groups = groups;
        this.loginUserNumber = loginUserNumber;
        this.loginUserId = loginUserId;
        this.loginUserName = loginUserName;
    }

}
