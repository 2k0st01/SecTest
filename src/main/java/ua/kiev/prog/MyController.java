package ua.kiev.prog;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
public class MyController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public MyController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index(Model model) {
        User user = getCurrentUser();

        String login = user.getUsername();
        CustomUser dbUser = userService.findByLogin(login);

        model.addAttribute("login", login);
        model.addAttribute("roles", user.getAuthorities());
        model.addAttribute("admin", isAdmin(user));
        model.addAttribute("moder", isModer(user));
        model.addAttribute("email", dbUser.getEmail());
        model.addAttribute("phone", dbUser.getPhone());
        model.addAttribute("address", dbUser.getAddress());
        model.addAttribute("status", dbUser.isStatus());

        return "index";
    }

    @PostMapping(value = "/update")
    public String update(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address) {
        User user = getCurrentUser();

        String login = user.getUsername();
        userService.updateUser(login, email, phone, address);

        return "redirect:/";
    }

    @PostMapping(value = "/status")
    public String changeUserStatus(@RequestParam(name = "userStatus[]", required = false) List<Long> ids,
                                   @RequestParam(name = "status") String status,
                                   Model model) {

        System.out.println("Received ids: " + ids);
        System.out.println("Received status: " + status);

        if (ids != null) {
            for (Long id : ids) {
                CustomUser dbUser = userService.getUsersById(id);
                if(dbUser.getRole() == UserRole.ADMIN){
                    return "admin";
                }
                if(status.equals("true")){
                    userService.updateUserStatus(dbUser.getLogin(), true);
                } else userService.updateUserStatus(dbUser.getLogin(), false);
            }
        }
        return "admin";
    }

    @PostMapping(value = "/newuser")
    public String update(@RequestParam String login,
                         @RequestParam String password,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) String phone,
                         @RequestParam(required = false) String address,
                         Model model) {
        String passHash = passwordEncoder.encode(password);

        //if (password.length() < 8)
        //    return "error";

        if (!userService.addUser(login, passHash, UserRole.USER, email, phone, address)) {
            model.addAttribute("exists", true);
            model.addAttribute("login", login);
            return "register";
        }

        return "redirect:/";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam(name = "toDelete[]", required = false) List<Long> ids,
                         Model model) {
        userService.deleteUsers(ids);
        model.addAttribute("users", userService.getAllUsers());

        return "admin";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODER')") // SpEL !!!
    public String admin(Model model) {
        User user = getCurrentUser();
        model.addAttribute("admin", isAdmin(user));
        model.addAttribute("moder", isModer(user));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model) {
        User user = getCurrentUser();
        model.addAttribute("login", user.getUsername());
        return "unauthorized";
    }

    // ----

    private User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }

    private boolean isModer(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();

        for (GrantedAuthority auth : roles) {
            if ("ROLE_MODER".equals(auth.getAuthority()))
                return true;
        }

        return false;
    }
}
