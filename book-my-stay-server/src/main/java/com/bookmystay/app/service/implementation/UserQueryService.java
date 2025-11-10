package com.bookmystay.app.service.implementation;


import com.bookmystay.app.dto.Response;
import com.bookmystay.app.entity.User;
import com.bookmystay.app.exception.OurException;
import com.bookmystay.app.repository.UserRepository;
import com.bookmystay.app.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserQueryService {

        private final UserRepository userRepository;
        private static final String USER_NOT_FOUND = "User not found!";
        private static final String SUCCESS_MESSAGE = "successful";

        public UserQueryService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @FunctionalInterface
        private interface UserQueryCall {
            void execute(Response response) throws OurException;
        }

        private Response handle(UserQueryCall call, String errorContext) {
            Response response = new Response();
            try {
                call.execute(response);
                if (response.getStatusCode() == 0) response.setStatusCode(200);
                if (response.getMessage() == null) response.setMessage(SUCCESS_MESSAGE);
            } catch (OurException e) {
                response.setStatusCode(404);
                response.setMessage(e.getMessage());
            } catch (Exception e) {
                response.setStatusCode(500);
                response.setMessage(errorContext + e.getMessage());
            }
            return response;
        }

        public Response getAllUsers() {
            return handle(response -> {
                List<User> users = userRepository.findAll();
                response.setUserList(Utils.mapUserListEntityToUserDTO(users));
            }, "Error fetching users: ");
        }

        public Response getUserById(Long userId) {
            return handle(response -> {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new OurException(USER_NOT_FOUND));
                response.setUser(Utils.mapUserEntityToUserDTO(user));
            }, "Error fetching user: ");
        }

        public Response getUserBookingHistory(Long userId) {
            return handle(response -> {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new OurException(USER_NOT_FOUND));
                response.setUser(Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user));
            }, "Error fetching booking history: ");
        }

        public Response deleteUser(Long userId) {
            return handle(response -> {
                userRepository.findById(userId)
                        .orElseThrow(() -> new OurException(USER_NOT_FOUND));
                userRepository.deleteById(userId);
            }, "Error deleting user: ");
        }

        public Response getMyInfo(String email) {
            return handle(response -> {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new OurException(USER_NOT_FOUND));
                response.setUser(Utils.mapUserEntityToUserDTO(user));
            }, "Error fetching my info: ");
        }
    }
