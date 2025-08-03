package com.postechfiap.meumenu.infrastructure.config;

import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosClientesAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.BuscarTodosProprietariosAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.CadastrarAdminInputPort;
import com.postechfiap.meumenu.core.controllers.admin.impl.BuscarTodosClientesAdminInputPortImpl;
import com.postechfiap.meumenu.core.controllers.admin.impl.BuscarTodosProprietariosAdminAdminInputPortImpl;
import com.postechfiap.meumenu.core.controllers.admin.impl.CadastrarAdminInputPortImpl;
import com.postechfiap.meumenu.core.controllers.cliente.*;
import com.postechfiap.meumenu.core.controllers.cliente.impl.*;
import com.postechfiap.meumenu.core.controllers.proprietario.AtualizarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.BuscarProprietarioPorIdInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.DeletarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.impl.AtualizarProprietarioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.proprietario.impl.BuscarProprietarioPorIdInputPortImpl;
import com.postechfiap.meumenu.core.controllers.proprietario.impl.CadastrarProprietarioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.proprietario.impl.DeletarProprietarioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.restaurante.*;
import com.postechfiap.meumenu.core.controllers.restaurante.impl.*;
import com.postechfiap.meumenu.core.controllers.restaurante.item.AdicionarItemCardapioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.AtualizarItemCardapioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.BuscarItemCardapioPorIdInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.DeletarItemCardapioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.AdicionarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.AtualizarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.BuscarItemCardapioPorIdInputPortImpl;
import com.postechfiap.meumenu.core.controllers.restaurante.item.impl.DeletarItemCardapioInputPortImpl;
import com.postechfiap.meumenu.core.controllers.usuario.AlterarSenhaInputPort;
import com.postechfiap.meumenu.core.controllers.usuario.LoginInputPort;
import com.postechfiap.meumenu.core.controllers.usuario.impl.AlterarSenhaInputPortImpl;
import com.postechfiap.meumenu.core.controllers.usuario.impl.LoginInputPortImpl;
import com.postechfiap.meumenu.core.domain.presenters.admin.CadastrarAdminOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.cliente.AtualizarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.cliente.BuscarTodosClientesOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.cliente.CadastrarClienteOutputPort;
import com.postechfiap.meumenu.core.domain.presenters.proprietario.BuscarTodosProprietariosOutputPort;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosClientesAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.BuscarTodosProprietariosAdminUseCase;
import com.postechfiap.meumenu.core.domain.usecases.admin.CadastrarAdminUseCase;
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
            CadastrarClienteUseCase cadastrarClienteUseCase,
            CadastrarClienteOutputPort clienteOutputPort) {
        return new CadastrarClienteInputPortImpl(
                cadastrarClienteUseCase,
                clienteOutputPort);
    }

    @Bean
    public BuscarClientePorIdInputPort buscarClientePorIdInputPort(
            BuscarClientePorIdUseCase buscarClientePorIdUseCase,
            BuscarClienteOutputPort buscarClienteOutputPort) {
        return new BuscarClientePorIdInputPortImpl(
                buscarClientePorIdUseCase,
                buscarClienteOutputPort);
    }

    @Bean
    public BuscarTodosClientesInputPort buscarTodosClientesInputPort(
            BuscarTodosClientesUseCase buscarTodosClientesUseCase,
            BuscarTodosClientesOutputPort buscarTodosClientesOutputPort) {
        return new BuscarTodosClientesInputPortImpl(
                buscarTodosClientesUseCase,
                buscarTodosClientesOutputPort);
    }

    @Bean
    public AtualizarClienteInputPort atualizarClienteInputPort(
            AtualizarClienteUseCase atualizarClienteUseCase,
            AtualizarClienteOutputPort atualizarClienteOutputPort) {
        return new AtualizarClienteInputPortImpl(
                atualizarClienteUseCase,
                atualizarClienteOutputPort);
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
            BuscarTodosClientesAdminUseCase buscarTodosClientesAdminUseCase, BuscarTodosClientesOutputPort buscarTodosClientesOutputPort) {
        return new BuscarTodosClientesAdminInputPortImpl(
                buscarTodosClientesAdminUseCase, buscarTodosClientesOutputPort);
    }

    @Bean
    public BuscarTodosProprietariosAdminInputPort buscarTodosProprietariosAdminInputPort(
            BuscarTodosProprietariosAdminUseCase buscarTodosProprietariosAdminUseCase,
            BuscarTodosProprietariosOutputPort buscarTodosProprietariosOutputPort) {
        return new BuscarTodosProprietariosAdminAdminInputPortImpl(
                buscarTodosProprietariosAdminUseCase,
                buscarTodosProprietariosOutputPort);
    }

    @Bean
    public CadastrarAdminInputPort cadastrarAdminInputPort(
            CadastrarAdminUseCase cadastrarAdminUseCase,
            CadastrarAdminOutputPort cadastrarAdminOutputPort) {
        return new CadastrarAdminInputPortImpl(
                cadastrarAdminUseCase, cadastrarAdminOutputPort);
    }

}
