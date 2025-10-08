package demo.ecommerce.controller.mapper;

import demo.ecommerce.controller.dto.LoginUserRequest;
import demo.ecommerce.controller.dto.LoginUserResponse;
import demo.ecommerce.usecase.user.LoginUserInput;
import demo.ecommerce.usecase.user.LoginUserOutput;

public class LoginUserMapper {
    public static LoginUserInput toInput(LoginUserRequest request) {
        return new LoginUserInput(
                request.email(),
                request.password()
        );
    }

    public static LoginUserResponse toResponse(LoginUserOutput output) {
        return new LoginUserResponse(output.token(), output.role());
    }
}