package com.bookmystay.BookMyStay.service.interfac;

import com.bookmystay.BookMyStay.dto.LoginRequest;
import com.bookmystay.BookMyStay.dto.Response;
import com.bookmystay.BookMyStay.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
