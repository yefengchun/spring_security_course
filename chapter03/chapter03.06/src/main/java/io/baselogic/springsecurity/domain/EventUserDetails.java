package io.baselogic.springsecurity.domain;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * There are advantages to creating a class that extends
 * {@link io.baselogic.springsecurity.domain.User}, our domain notion of a user, and
 * implements {@link UserDetails}, Spring Security's notion of a user.
 * <ul>
 * <li>First we can obtain all the custom information in the
 * {@link io.baselogic.springsecurity.domain.User}</li>
 * <li>Second, we can use this service to integrate with Spring Security in other
 * ways (i.e. when implementing Spring Security's <a
 * href="http://static.springsource.org/spring-security/site/docs/current/reference/remember-me.html">
 * Remember-Me Authentication</a></li>
 * </ul>
 *
 * @author mickknutson
 * @since chapter03.04
 *
 */
public class EventUserDetails
        extends io.baselogic.springsecurity.domain.User
        implements UserDetails {

    public EventUserDetails(io.baselogic.springsecurity.domain.User user) {
        setId(user.getId());
        setEmail(user.getEmail());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setPassword(user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return UserAuthorityUtils.createAuthorities(this);
    }

    @Override
    public String getUsername() {
        return getEmail();
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

    private static final long serialVersionUID = 1234436451564509032L;

} // The End...