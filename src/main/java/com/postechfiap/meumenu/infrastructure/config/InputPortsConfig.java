package com.postechfiap.meumenu.infrastructure.config;

import com.postechfiap.meumenu.core.controllers.*;
import com.postechfiap.meumenu.core.controllers.impl.*;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.cliente.*;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.AtualizarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.BuscarProprietarioPorIdUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.DeletarProprietarioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.*;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AdicionarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.AtualizarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.BuscarItemCardapioPorIdUseCase;
import com.postechfiap.meumenu.core.domain.usecases.restaurante.item.DeletarItemCardapioUseCase;
import com.postechfiap.meumenu.core.domain.usecases.usuario.AlterarSenhaUseCase;
import com.postechfiap.meumenu.core.domain.usecases.usuario.LoginUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputPortsConfig {

    @Bean
    public CadastrarClienteInputPort cadastrarClienteInputPort(
            CadastrarClienteUseCase cadastrarClienteUseCase) {
        return new CadastrarClienteInputPortImpl(
                cadastrarClienteUseCase);
    }

    @Bean
    public BuscarClientePorIdInputPort buscarClientePorIdInputPort(
            BuscarClientePorIdUseCase buscarClientePorIdUseCase) {
        return new BuscarClientePorIdInputPortImpl(
                buscarClientePorIdUseCase);
    }

    @Bean
    public BuscarTodosClientesInputPort buscarTodosClientesInputPort(
            BuscarTodosClientesUseCase buscarTodosClientesUseCase) {
        return new BuscarTodosClientesInputPortImpl(
                buscarTodosClientesUseCase);
    }

    @Bean
    public AtualizarClienteInputPort atualizarClienteInputPort(
            AtualizarClienteUseCase atualizarClienteUseCase) {
        return new AtualizarClienteInputPortImpl(
                atualizarClienteUseCase);
    }

    @Bean
    public DeletarClienteInputPort deletarClienteInputPort(
            DeletarClienteUseCase deletarClienteUseCase) {
        return new DeletarClienteInputPortImpl(deletarClienteUseCase);
    }

    @Bean
    public CadastrarProprietarioInputPort cadastrarProprietarioInputPort(
            CadastrarProprietarioUseCase cadastrarProprietarioUseCase) {
        return new CadastrarProprietarioInputPortImpl(cadastrarProprietarioUseCase);
    }

    @Bean
    public AtualizarProprietarioInputPort atualizarProprietarioInputPort(
            AtualizarProprietarioUseCase atualizarProprietarioUseCase) {
        return new AtualizarProprietarioInputPortImpl(atualizarProprietarioUseCase);
    }

    @Bean
    public BuscarProprietarioPorIdInputPort buscarProprietarioPorIdInputPort(
            BuscarProprietarioPorIdUseCase buscarProprietarioPorIdUseCase) {
        return new BuscarProprietarioPorIdInputPortImpl(buscarProprietarioPorIdUseCase);
    }

    @Bean
    public DeletarProprietarioInputPort deletarProprietarioInputPort(
            DeletarProprietarioUseCase deletarProprietarioUseCase) {
        return new DeletarProprietarioInputPortImpl(deletarProprietarioUseCase);
    }

    @Bean
    public LoginInputPort loginInputPort(LoginUseCase loginUseCase) {
        return new LoginInputPortImpl(loginUseCase);
    }

    @Bean
    public AlterarSenhaInputPort alterarSenhaInputPort(
            AlterarSenhaUseCase alterarSenhaUseCase) {
        return new AlterarSenhaInputPortImpl(alterarSenhaUseCase);
    }

    @Bean
    public CadastrarRestauranteInputPort cadastrarRestauranteInputPort(
            CadastrarRestauranteUseCase cadastrarRestauranteUseCase) {
        return new CadastrarRestauranteInputPortImpl(cadastrarRestauranteUseCase);
    }

    @Bean
    public AtualizarRestauranteInputPort atualizarRestauranteInputPort(
            AtualizarRestauranteUseCase atualizarRestauranteUseCase) {
        return new AtualizarRestauranteInputPortImpl(atualizarRestauranteUseCase);
    }

    @Bean
    public BuscarRestaurantePorIdInputPort buscarRestaurantePorIdInputPort(
            BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase) {
        return new BuscarRestaurantePorIdInputPortImpl(buscarRestaurantePorIdUseCase);
    }

    @Bean
    public BuscarTodosRestaurantesInputPort buscarTodosRestaurantesInputPort(
            BuscarTodosRestaurantesUseCase buscarTodosRestaurantesUseCase) {
        return new BuscarTodosRestaurantesInputPortImpl(
                buscarTodosRestaurantesUseCase);
    }

    @Bean
    public DeletarRestauranteInputPort deletarRestauranteInputPort(
            DeletarRestauranteUseCase deletarRestauranteUseCase) {
        return new DeletarRestauranteInputPortImpl(deletarRestauranteUseCase);
    }

    @Bean
    public AdicionarItemCardapioInputPort adicionarItemCardapioInputPort(
            AdicionarItemCardapioUseCase adicionarItemCardapioUseCase) {
        return new AdicionarItemCardapioInputPortImpl(adicionarItemCardapioUseCase);
    }

    @Bean
    public AtualizarItemCardapioInputPort atualizarItemCardapioInputPort(
            AtualizarItemCardapioUseCase atualizarItemCardapioUseCase) {
        return new AtualizarItemCardapioInputPortImpl(atualizarItemCardapioUseCase);
    }

    @Bean
    public BuscarItemCardapioPorIdInputPort buscarItemCardapioPorIdInputPort(
            BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase) {
        return new BuscarItemCardapioPorIdInputPortImpl(buscarItemCardapioPorIdUseCase);
    }

    @Bean
    public DeletarItemCardapioInputPort deletarItemCardapioInputPort(
            DeletarItemCardapioUseCase deletarItemCardapioUseCase) {
        return new DeletarItemCardapioInputPortImpl(deletarItemCardapioUseCase);
    }

    @Bean
    public BuscarTodosClientesAdminInputPort buscarTodosClientesAdminInputPort(
            BuscarTodosClientesAdminUseCase buscarTodosClientesAdminUseCase) {
        return new BuscarTodosClientesAdminInputPortImpl(
                buscarTodosClientesAdminUseCase);
    }

    @Bean
    public BuscarTodosProprietariosAdminInputPort buscarTodosProprietariosAdminInputPort(
            BuscarTodosProprietariosAdminUseCase buscarTodosProprietariosAdminUseCase) {
        return new BuscarTodosProprietariosAdminAdminInputPortImpl(
                buscarTodosProprietariosAdminUseCase);
    }

}
