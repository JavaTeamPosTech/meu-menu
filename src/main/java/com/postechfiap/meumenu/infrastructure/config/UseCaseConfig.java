package com.postechfiap.meumenu.infrastructure.config;

import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.CadastrarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.DeletarProprietarioOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.*;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.AdicionarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.AtualizarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.BuscarItemCardapioPorIdOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.restaurante.item.DeletarItemCardapioOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.usuario.AlterarSenhaOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.usuario.LoginOutputPort;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.CadastrarAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.impl.BuscarTodosClientesAdminUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.admin.impl.BuscarTodosProprietariosAdminUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.admin.impl.CadastrarAdminUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.cliente.*;
import com.postechfiap.meumenu.core.domain.usecases.cliente.impl.*;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.impl.AtualizarProprietarioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.impl.BuscarProprietarioPorIdUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.impl.CadastrarProprietarioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.impl.DeletarProprietarioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.*;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.impl.*;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.BuscarItemCardapioPorIdUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl.AdicionarItemCardapioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl.AtualizarItemCardapioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl.BuscarItemCardapioPorIdUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.impl.DeletarItemCardapioUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.usuario.AlterarSenhaUseCase;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import com.postechfiap.meumenu.core.domain.usecases.usuario.impl.AlterarSenhaUseCaseImpl;
import com.postechfiap.meumenu.core.domain.usecases.usuario.impl.LoginUseCaseImpl;
import com.postechfiap.meumenu.core.gateways.*;
import com.postechfiap.meumenu.infrastructure.security.services.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class UseCaseConfig {

    @Bean
    public CadastrarClienteUseCase cadastrarClienteUseCase(
            ClienteGateway clienteGateway,
            UsuarioGateway usuarioGateway,
            PasswordService passwordService
    ) {
        return new CadastrarClienteUseCaseImpl(
                clienteGateway,
                usuarioGateway,
                passwordService)
                ;
    }

    @Bean
    public AtualizarClienteUseCase atualizarClienteUseCase(
            ClienteGateway clienteGateway,
            UsuarioGateway usuarioGateway
    ) {
        return new AtualizarClienteUseCaseImpl(
                clienteGateway,
                usuarioGateway
        );
    }

    @Bean
    public BuscarClientePorIdUseCase buscarClientePorIdUseCase(
            ClienteGateway clienteGateway) {
        return new BuscarClientePorIdUseCaseImpl(
                clienteGateway
        );
    }

    @Bean
    public BuscarTodosClientesUseCase buscarTodosClientesUseCase(
            ClienteGateway clienteGateway
    ) {
        return new BuscarTodosClientesUseCaseImpl(
                clienteGateway
        );
    }

    @Bean
    public DeletarClienteUseCase deletarClienteUseCase(
            ClienteGateway clienteGateway) {
        return new DeletarClienteUseCaseImpl(
                clienteGateway
        );
    }

    @Bean
    public CadastrarProprietarioUseCase cadastrarProprietarioUseCase(
            ProprietarioGateway proprietarioGateway,
            UsuarioGateway usuarioGateway,
            PasswordService passwordService,
            CadastrarProprietarioOutputPort cadastrarProprietarioOutputPort) {
        return new CadastrarProprietarioUseCaseImpl(
                proprietarioGateway,
                usuarioGateway,
                passwordService,
                cadastrarProprietarioOutputPort);
    }

    @Bean
    public AtualizarProprietarioUseCase atualizarProprietarioUseCase(
            ProprietarioGateway proprietarioGateway,
            UsuarioGateway usuarioGateway) {
        return new AtualizarProprietarioUseCaseImpl(
                proprietarioGateway,
                usuarioGateway
        );
    }

    @Bean
    public BuscarProprietarioPorIdUseCase buscarProprietarioPorIdUseCase(
            ProprietarioGateway proprietarioGateway) {
        return new BuscarProprietarioPorIdUseCaseImpl(
                proprietarioGateway
        );
    }

    @Bean
    public DeletarProprietarioUseCase deletarProprietarioUseCase(
            ProprietarioGateway proprietarioGateway,
            DeletarProprietarioOutputPort deletarProprietarioOutputPort) {
        return new DeletarProprietarioUseCaseImpl(
                proprietarioGateway,
                deletarProprietarioOutputPort);
    }

    @Bean
    public CadastrarRestauranteUseCase cadastrarRestauranteUseCase(
            RestauranteGateway restauranteGateway,
            ProprietarioGateway proprietarioGateway,
            TipoCozinhaGateway tipoCozinhaGateway,
            CadastrarRestauranteOutputPort cadastrarRestauranteOutputPort) {
        return new CadastrarRestauranteUseCaseImpl(
                restauranteGateway,
                proprietarioGateway,
                tipoCozinhaGateway,
                cadastrarRestauranteOutputPort);
    }

    @Bean
    public BuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase(
            RestauranteGateway restauranteGateway,
            BuscarTodosRestaurantesOutputPort buscarTodosRestaurantesOutputPort) {
        return new BuscarTodosRestaurantesUseCaseImpl(
                restauranteGateway,
                buscarTodosRestaurantesOutputPort);
    }

    @Bean
    public BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase(
            RestauranteGateway restauranteGateway,
            BuscarRestaurantePorIdOutputPort buscarRestaurantePorIdOutputPort) {
        return new BuscarRestaurantePorIdUseCaseImpl(
                restauranteGateway,
                buscarRestaurantePorIdOutputPort);
    }

    @Bean
    public AtualizarRestauranteUseCase atualizarRestauranteUseCase(
            RestauranteGateway restauranteGateway,
            ProprietarioGateway proprietarioGateway,
            TipoCozinhaGateway tipoCozinhaGateway,
            AtualizarRestauranteOutputPort atualizarRestauranteOutputPort) {
        return new AtualizarRestauranteUseCaseImpl(
                restauranteGateway,
                proprietarioGateway,
                tipoCozinhaGateway,
                atualizarRestauranteOutputPort);
    }

    @Bean
    public DeletarRestauranteUseCase deletarRestauranteUseCase(
            RestauranteGateway restauranteGateway,
            DeletarRestauranteOutputPort deletarRestauranteOutputPort) {
        return new DeletarRestauranteUseCaseImpl(
                restauranteGateway,
                deletarRestauranteOutputPort);
    }

    @Bean
    public AdicionarItemCardapioUseCase adicionarItemCardapioUseCase(
            RestauranteGateway restauranteGateway,
            AdicionarItemCardapioOutputPort adicionarItemCardapioOutputPort) {
        return new AdicionarItemCardapioUseCaseImpl(
                restauranteGateway,
                adicionarItemCardapioOutputPort);
    }

    @Bean
    public AtualizarItemCardapioUseCase atualizarItemCardapioUseCase(
            RestauranteGateway restauranteGateway,
            AtualizarItemCardapioOutputPort atualizarItemCardapioOutputPort) {
        return new AtualizarItemCardapioUseCaseImpl(
                restauranteGateway,
                atualizarItemCardapioOutputPort);
    }

    @Bean
    public BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase(
            RestauranteGateway restauranteGateway,
            BuscarItemCardapioPorIdOutputPort buscarItemCardapioPorIdOutputPort) {
        return new BuscarItemCardapioPorIdUseCaseImpl(
                restauranteGateway,
                buscarItemCardapioPorIdOutputPort);
    }

    @Bean
    public DeletarItemCardapioUseCase deletarItemCardapioUseCase(
            RestauranteGateway restauranteGateway,
            DeletarItemCardapioOutputPort deletarItemCardapioOutputPort) {
        return new DeletarItemCardapioUseCaseImpl(
                restauranteGateway,
                deletarItemCardapioOutputPort);
    }

    @Bean
    public LoginUseCase loginUseCase(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            LoginOutputPort loginOutputPort) {
        return new LoginUseCaseImpl(
                authenticationManager,
                tokenService,
                loginOutputPort);
    }

    @Bean
    public AlterarSenhaUseCase alterarSenhaUseCase(
            UsuarioGateway usuarioGateway,
            PasswordService passwordService,
            AlterarSenhaOutputPort alterarSenhaOutputPort) {
        return new AlterarSenhaUseCaseImpl(
                usuarioGateway,
                passwordService,
                alterarSenhaOutputPort);
    }

    @Bean
    public BuscarTodosClientesAdminUseCase buscarTodosClientesAdminUseCase(
            ClienteGateway clienteGateway,
            BuscarTodosClientesOutputPort buscarTodosClientesOutputPort) {
        return new BuscarTodosClientesAdminUseCaseImpl(
                clienteGateway
        );
    }

    @Bean
    public BuscarTodosProprietariosAdminUseCase buscarTodosProprietariosAdminUseCase(
            ProprietarioGateway proprietarioGateway
    ) {
        return new BuscarTodosProprietariosAdminUseCaseImpl(
                proprietarioGateway);
    }

    @Bean
    public CadastrarAdminUseCase cadastrarAdminUseCase(
            AdminGateway adminGateway,
            UsuarioGateway usuarioGateway,
            PasswordService passwordService
    ) {
        return new CadastrarAdminUseCaseImpl(
                adminGateway,
                usuarioGateway,
                passwordService);
    }

}
