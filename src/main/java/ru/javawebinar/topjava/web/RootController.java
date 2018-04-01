package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.WebRequest;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.VerificationToken;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.OnRegistrationCompleteEvent;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.user.AbstractUserController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class RootController extends AbstractUserController {
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RootController(UserService service, ApplicationEventPublisher eventPublisher) {
        super(service);
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    //    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/meals")
    public String meals() {
        return "meals";
    }

    @GetMapping("/profile")
    public String profile(ModelMap model, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        model.addAttribute("userTo", authorizedUser.getUserTo());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status, @AuthenticationPrincipal AuthorizedUser authorizedUser) {
        if (!result.hasErrors()) {
            try {
                super.update(userTo, authorizedUser.getId());
                authorizedUser.update(userTo);
                status.setComplete();
                return "redirect:meals";
            }
            catch (DataIntegrityViolationException ex) {
                result.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
            }
        }
        return "profile";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @GetMapping("/confirmEmail")
    public String confirmEmail(@RequestParam(value = "token") String token) {
        log.info("Email confirmation for {}", token);
        VerificationToken verificationToken = service.getVerificationToken(token);
        User user = verificationToken.getUser();
        if (LocalDateTime.now().isAfter(verificationToken.getExpiryDate())) {
            service.deleteVerificationToken(verificationToken.getId());
            log.error("Token expired {}", token);
            return "redirect:login?error=user.token.expired";
        }
        user.setEmailConfirmed(true);
        service.saveAndDeleteVerificationToken(user, verificationToken.getId());
        log.info("Email confirmed successfully for {} with token {}", user, token);

        return "redirect:login?message=user.email.confirmed";
    }

    @GetMapping("/resendToken")
    public String resendToken() {
        return "resendToken";
    }

    @PostMapping("/resendToken")
    public String resendNewToken(@RequestParam("email") String email, WebRequest request) {
        User expiredUser = service.getByEmail(email.toLowerCase());
        if (expiredUser.isEmailConfirmed()) return "redirect:login?message=user.email.alreadyConfirmed";
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(expiredUser, LocaleContextHolder.getLocale(), request.getContextPath()));
        return "redirect:login?message=app.resend";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model, WebRequest request) {
        if (!result.hasErrors()) {
            try {
                User registered = super.create(UserUtil.createNewFromTo(userTo));
                try {
                    String appUrl = request.getContextPath();
                    eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                            (registered, LocaleContextHolder.getLocale(), appUrl));
                }
                catch (Exception e) {
                    log.error("Cannot send confirmation email to " + registered.getEmail(), e);
                    return "login?error=exception.user.emailSending";
//                    return new ModelAndView("emailError", "user", accountDto);
                }
//                return new ModelAndView("successRegister", "user", accountDto);
                status.setComplete();
                return "redirect:login?message=app.registered";//&username=" + userTo.getEmail();
            }
            catch (DataIntegrityViolationException ex) {
                result.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
            }
        }
        model.addAttribute("register", true);
        return "profile";
    }
}
