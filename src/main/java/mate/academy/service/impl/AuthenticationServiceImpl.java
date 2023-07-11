package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userFromDbOptional = userService.findByEmail(email);
        User user = userFromDbOptional.orElseThrow(() ->
                new AuthenticationException("Login is incorrect"));
        String hashedPassword = HashUtil.hashPassword(password, user.getSalt());
        if (!user.getPassword().equals(hashedPassword)) {
            throw new AuthenticationException("Password is incorrect");
        }
        return user;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (!userService.findByEmail(email).isEmpty()) {
            throw new RegistrationException("This email has already been registered");
        }
        User user = new User(email,password);
        userService.add(user);
        return user;
    }
}