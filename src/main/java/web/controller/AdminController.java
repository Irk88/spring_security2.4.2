package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping("/{id}")
    public String showUserId(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "show";
    }

    @GetMapping("/admin")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/admin/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "/add";
    }

    @PostMapping("/admin/add")
    public String create(@ModelAttribute("user") User user,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/add";
        }
        if ((!user.getPassword().equals(user.getConfirmPassword()))) {
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "/add";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUser(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    @PostMapping("/admin/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(required = false, name = "ROLE_ADMIN") String roleAdmin,
                             @RequestParam(required = false, name = "ROLE_USER") String roleUser) {
        Set<Role> roles = new HashSet<>();
        if (roleAdmin != null) {
            roles.add(new Role(1L, roleAdmin));
        }
        if (roleUser != null) {
            roles.add(new Role(2L, roleUser));
        }
        user.setName(user.getName());
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }
}
