package com.mjgomes.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjgomes.cursomc.dto.EmailDTO;
import com.mjgomes.cursomc.security.JWTUtil;
import com.mjgomes.cursomc.security.UserSS;
import com.mjgomes.cursomc.services.AuthService;
import com.mjgomes.cursomc.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthService service;

    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
        //Pega o usuário logado
        UserSS user = UserService.authenticated();
        //Gera o token
        String token = jwtUtil.generateToken(user.getUsername());
        //Adiciona o token ao header da resposta
        response.addHeader("Authorization", "Bearer " + token);
        //Necessário para que o front-end receba o token
        response.addHeader("access-control-expose-headers", "Authorization");
        return ResponseEntity.noContent().build();
    }

    //Endpoint para redefinir a senha, envia um email com um código para o usuário
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    // @Valid faz a validação do DTO
    // @RequestBody transforma o JSON em objeto
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
        service.sendNewPasswordEmail(objDTO.getEmail());
        return ResponseEntity.noContent().build();
    }
}
