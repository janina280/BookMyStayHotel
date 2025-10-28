package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.LoginRequest;
import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.UserRepository;
import com.bookmystay.app.service.interfac.IUserService;
import com.bookmystay.app.utils.JWTUtils;
import com.bookmystay.app.utils.Utils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private static final String USER_NOT_FOUND = "User not found!";


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Helper generic pentru wrap-ul try-catch
     */
    private Response handleUserServiceCall(UserServiceCall call) {
        Response response = new Response();
        try {
            call.execute(response);
            response.setStatusCode(200);
            if (response.getMessage() == null) {
                response.setMessage("successful");
            }
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occurred: " + e.getMessage());
        }
        return response;
    }

    @FunctionalInterface
    private interface UserServiceCall {
        void execute(Response response);
    }


    @Override
    public Response register(User user) {
        return handleUserServiceCall(response -> {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new OurException(user.getEmail() + " already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            response.setUser(Utils.mapUserEntityToUserDTO(savedUser));
        });
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        return handleUserServiceCall(response -> {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException(USER_NOT_FOUND));

            String token = jwtUtils.generateToken(user);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("successful");
        });
    }

    @Override
    public Response getAllUsers() {
        return handleUserServiceCall(response -> {
            List<User> users = userRepository.findAll();
            response.setUserList(Utils.mapUserListEntityToUserDTO(users));
        });
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        return handleUserServiceCall(response -> {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException(USER_NOT_FOUND));
            response.setUser(Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user));
        });
    }

    @Override
    public Response deleteUser(String userId) {
        return handleUserServiceCall(response -> {
            userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException(USER_NOT_FOUND));
            userRepository.deleteById(Long.valueOf(userId));
        });
    }

    @Override
    public Response getUserById(String userId) {
        return handleUserServiceCall(response -> {
            User user = userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new OurException(USER_NOT_FOUND));
            response.setUser(Utils.mapUserEntityToUserDTO(user));
        });
    }

    @Override
    public Response getMyInfo(String email) {
        return handleUserServiceCall(response -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException(USER_NOT_FOUND));
            response.setUser(Utils.mapUserEntityToUserDTO(user));
        });
    }
}
