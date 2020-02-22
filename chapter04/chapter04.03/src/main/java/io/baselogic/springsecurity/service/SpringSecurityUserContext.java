package io.baselogic.springsecurity.service;

import io.baselogic.springsecurity.core.authority.UserAuthorityUtils;
import io.baselogic.springsecurity.domain.AppUser;
import io.baselogic.springsecurity.domain.EventUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * An implementation of {@link UserContext} that looks up the {@link AppUser} using the Spring Security's
 * {@link Authentication} by principal name.
 *
 * @author Mick Knutson
 * @since chapter03.01
 * @since chapter04.02 added conversion to/from {@link org.springframework.security.core.userdetails.User}
 */
@Component
public class SpringSecurityUserContext implements UserContext {

    private final EventService eventService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SpringSecurityUserContext(final @NotNull EventService eventService,
                                     final @NotNull @Qualifier("eventUserDetailsService") UserDetailsService userDetailsService) {

        this.eventService = eventService;
        this.userDetailsService = userDetailsService;
    }


    /**
     * Get the {@link AppUser} by obtaining the currently logged in Spring Security user's
     * {@link Authentication#getName()} and using that to find the {@link AppUser} by email address (since for our
     * application Spring Security username's are email addresses).
     *
     * @since chapter03.04:
     * Get the {@link AppUser} by casting
     * the {@link Authentication}'s principal to a {@link AppUser}.
     */
    @Override
    public AppUser getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            return null;
        }

        EventUserDetails user = (EventUserDetails)authentication.getPrincipal();
        String email = user.getUsername();
//        String email = user.getEmail();
        if (email == null) {
            return null;
        }
        AppUser result = eventService.findUserByEmail(email);
        if (result == null) {
            throw new IllegalStateException(
                    "Spring Security is not in synch with AppUsers. Could not find user with email " + email);
        }
        return result;
    }

    /**
     * Sets the {@link AppUser} as the current {@link Authentication}'s
     * principal.
     */
    @Override
    public void setCurrentUser(final @Valid @NotNull(message = "user.notNull.key") AppUser appUser) {
        if (appUser.getEmail() == null) {
            throw new IllegalArgumentException("email cannot be null");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(appUser.getEmail());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                appUser.getPassword(),userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

} // The End...