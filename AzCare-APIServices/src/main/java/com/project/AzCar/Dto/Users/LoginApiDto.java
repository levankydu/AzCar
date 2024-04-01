package com.project.AzCar.Dto.Users;

import lombok.Data;

@Data
public class LoginApiDto {
    private String usernameOrEmail;
    private String password;
}