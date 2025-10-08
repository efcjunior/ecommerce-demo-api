package demo.ecommerce.controller.mapper;

import demo.ecommerce.controller.dto.RegisterUserRequest;
import demo.ecommerce.controller.dto.RegisterUserResponse;
import demo.ecommerce.usecase.user.RegisterUserInput;
import demo.ecommerce.usecase.user.RegisterUserOutput;

public class RegisterUserMapper {
    public static RegisterUserInput toInput(RegisterUserRequest request) {
        return new RegisterUserInput(
                request.name(),
                request.email(),
                request.password(),
                request.role()
        );
    }

    public static RegisterUserResponse toResponse(RegisterUserOutput output) {
        return new RegisterUserResponse(output.userId(), output.email());
    }
}
