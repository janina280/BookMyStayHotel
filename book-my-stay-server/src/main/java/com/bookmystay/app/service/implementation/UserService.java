package com.bookmystay.app.service.implementation;

import com.bookmystay.app.dto.LoginRequest;
import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.service.interfac.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final UserQueryService queryService;

    public UserService(UserRegistrationService registrationService,
                       AuthenticationService authenticationService,
                       UserQueryService queryService) {
        this.registrationService = registrationService;
        this.authenticationService = authenticationService;
        this.queryService = queryService;
    }

    @Override
    public Response register(User user) {
        return registrationService.register(user);
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @Override
    public Response getAllUsers() {
        return queryService.getAllUsers();
    }

    @Override
    public Response getUserById(String userId) {
        return queryService.getUserById(Long.valueOf(userId));
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        return queryService.getUserBookingHistory(Long.valueOf(userId));
    }

    @Override
    public Response deleteUser(String userId) {
        return queryService.deleteUser(Long.valueOf(userId));
    }

    @Override
    public Response getMyInfo(String email) {
        return queryService.getMyInfo(email);
    }
}
