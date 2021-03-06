package io.baselogic.springsecurity.web.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

/**
 * This displays the welcome screen that shows what will be happening in each chapter.
 *
 * @since chapter01.00
 * @author mickknutson
 *
 *  @since chapter09.02 showCreateLink() method
 *  @since chapter09.03 Imported webInvocationPrivilegeEvaluator
 */
@Controller
@Slf4j
public class WelcomeController {

    private final WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator;

    @Autowired
    public WelcomeController(WebInvocationPrivilegeEvaluator webInvocationPrivilegeEvaluator) {
        this.webInvocationPrivilegeEvaluator = webInvocationPrivilegeEvaluator;
    }


    @GetMapping("/")
    public String welcome() {
        return "index";
    }

    /**
     * Populates a {@link HttpServletRequest} attribute named usernameContainsUser for any URL processed by this
     * controller. The result is based upon if the username contains "user".
     *
     * @param authentication
     *            Contains the current {@link Authentication} object. This is a more simple way of obtaining the
     *            Authentication from {@link SecurityContextHolder#getContext()}.
     * @return
     */
    @ModelAttribute("showCreateLink")
    public boolean showCreateLink(Authentication authentication) {
        log.info("*** authentication: " + authentication);
        // NOTE We could also get the Authentication from SecurityContextHolder.getContext().getAuthentication()
        boolean result = authentication != null && authentication.getName().contains("user");
        log.info("*** result: {}", result);
        return result;
    }

    /**
     * Populates a {@link HttpServletRequest} attribute named showAdminLink for any URL processed by this controller.
     * The result is based upon if the user has access to the URL /admin/. This demonstrates if you are not using JSP
     * tags how you can leverage the http.authorizeRequests() mappings.
     *
     * @param authentication
     *            Contains the current {@link Authentication} object. This is a more simple way of obtaining the
     *            Authentication from {@link SecurityContextHolder#getContext()}.
     * @return
     */
    @ModelAttribute("showAdminLink")
    public boolean showAdminLink(Authentication authentication) {
        // NOTE We could also get the Authentication from SecurityContextHolder.getContext().getAuthentication()
        return webInvocationPrivilegeEvaluator.isAllowed("/admin/", authentication);
    }



} // The End...