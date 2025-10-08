package demo.ecommerce.controller;

import demo.ecommerce.controller.dto.LoginUserRequest;
import demo.ecommerce.controller.dto.LoginUserResponse;
import demo.ecommerce.controller.dto.RegisterUserRequest;
import demo.ecommerce.controller.dto.RegisterUserResponse;
import demo.ecommerce.controller.mapper.LoginUserMapper;
import demo.ecommerce.controller.mapper.RegisterUserMapper;
import demo.ecommerce.usecase.user.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest request) {

        var input = RegisterUserMapper.toInput(request);
        var output = registerUserUseCase.execute(input);
        var response = RegisterUserMapper.toResponse(output);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(
            @Valid @RequestBody LoginUserRequest request) {

        var input = LoginUserMapper.toInput(request);
        var output = loginUserUseCase.execute(input);
        var response = LoginUserMapper.toResponse(output);

        return ResponseEntity.ok(response);
    }
}