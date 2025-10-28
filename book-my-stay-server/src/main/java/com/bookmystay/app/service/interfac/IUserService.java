package com.bookmystay.app.service.interfac;


import com.bookmystay.app.dto.LoginRequest;
import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
