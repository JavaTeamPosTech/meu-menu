package com.postechfiap.meumenu.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postechfiap.meumenu.core.controllers.cliente.CadastrarClienteInputPort;
import com.postechfiap.meumenu.core.controllers.proprietario.CadastrarProprietarioInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.CadastrarRestauranteInputPort;
import com.postechfiap.meumenu.core.controllers.restaurante.item.AdicionarItemCardapioInputPort;
import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.gateways.AdminGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.dtos.request.AdicionarItemCardapioRequestDTO;
import com.postechfiap.meumenu.core.dtos.request.CadastrarClienteRequestDTO;
import com.postechfiap.meumenu.core.dtos.request.CadastrarProprietarioRequestDTO;
import com.postechfiap.meumenu.core.dtos.request.CadastrarRestauranteRequestDTO;
import com.postechfiap.meumenu.core.dtos.response.CadastrarProprietarioResponseDTO;
import com.postechfiap.meumenu.core.dtos.response.CadastrarRestauranteResponseDTO;
import com.postechfiap.meumenu.infrastructure.api.presenters.proprietario.CadastrarProprietarioPresenter;
import com.postechfiap.meumenu.infrastructure.api.presenters.restaurante.CadastrarRestaurantePresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    @Value("${meumenu.admin.login}")
    private String defaultAdminLogin;

    @Value("${meumenu.admin.email}")
    private String defaultAdminEmail;

    @Value("${meumenu.admin.password}")
    private String defaultAdminPassword;

    private final AdminGateway adminGateway;
    private final UsuarioGateway usuarioGateway;
    private final CadastrarClienteInputPort cadastrarClienteInputPort;
    private final CadastrarProprietarioInputPort cadastrarProprietarioInputPort;
    private final CadastrarProprietarioPresenter cadastrarProprietarioPresenter;
    private final CadastrarRestauranteInputPort cadastrarRestauranteInputPort;
    private final CadastrarRestaurantePresenter cadastrarRestaurantePresenter;
    private final AdicionarItemCardapioInputPort adicionarItemCardapioInputPort;
    private final PasswordService passwordService;

    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!usuarioGateway.existsByLogin(defaultAdminLogin)) {
            System.out.println("Iniciando a criação do primeiro usuário Admin padrão...");

            String hashedPassword = passwordService.encryptPassword(defaultAdminPassword);

            AdminDomain admin = new AdminDomain(
                    "Administrator",
                    defaultAdminEmail,
                    defaultAdminLogin,
                    hashedPassword
            );

            admin = adminGateway.cadastrarAdmin(admin);
            System.out.println("Usuário Admin '" + admin.getLogin() + "' criado com sucesso! ID: " + admin.getId());
        } else {
            System.out.println("Usuário Admin padrão já existe.");
        }
        loadClientes();
        loadProprietarios();
    }

    private void loadClientes() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("clientes.json");
        if (inputStream == null) {
            System.out.println("clientes.json não encontrado");
            return;
        }

        List<CadastrarClienteRequestDTO> clientes = objectMapper.readValue(
                inputStream,
                new TypeReference<List<CadastrarClienteRequestDTO>>() {}
        );

        for (CadastrarClienteRequestDTO cliente : clientes) {
            try {
                cadastrarClienteInputPort.execute(cliente.toInputModel());
            } catch (Exception e) {
                System.err.println("Erro ao cadastrar cliente: " + cliente.login() + " - " + e.getMessage());
            }
        }
    }

    private void loadProprietarios() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("proprietarios.json");
        if (inputStream == null) {
            System.out.println("proprietarios.json não encontrado");
            return;
        }

        List<CadastrarProprietarioRequestDTO> proprietarios = objectMapper.readValue(
                inputStream,
                new TypeReference<List<CadastrarProprietarioRequestDTO>>() {}
        );

        int restauranteIndex = 0;
        int itemCardapioIndex = 0;

        for (CadastrarProprietarioRequestDTO proprietario : proprietarios) {
            try {
                cadastrarProprietarioInputPort.execute(proprietario.toInputModel());
                CadastrarProprietarioResponseDTO proprietarioResponseDTO = cadastrarProprietarioPresenter.getViewModel();

                loadRestaurante(restauranteIndex++, proprietarioResponseDTO.id(), itemCardapioIndex++);
                loadRestaurante(restauranteIndex++, proprietarioResponseDTO.id(), itemCardapioIndex++);

            } catch (Exception e) {
                System.err.println("Erro ao cadastrar proprietario: " + proprietario.login() + " - " + e.getMessage());
            }
        }
    }

    private void loadRestaurante(Integer index, UUID proprietárioID, int  itemCardapioIndex) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("restaurantes.json");
        if (inputStream == null) {
            System.out.println("restaurantes.json não encontrado");
            return;
        }

        List<CadastrarRestauranteRequestDTO> restaurantes = objectMapper.readValue(
                inputStream,
                new TypeReference<List<CadastrarRestauranteRequestDTO>>() {}
        );

        try {
            cadastrarRestauranteInputPort.execute(restaurantes.get(index).toInputModel(proprietárioID));
            CadastrarRestauranteResponseDTO restauranteResponseDTO =  cadastrarRestaurantePresenter.getViewModel();
            loadItemCardapio(itemCardapioIndex, restauranteResponseDTO.id());
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar restaurante: " + restaurantes.get(index).nomeFantasia() + " - " + e.getMessage());
        }
    }

    private void loadItemCardapio(int itemCardapioIndex, UUID restauranteID) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("itens-cardapio.json");
        if (inputStream == null) {
            System.out.println("itens-cardapio.json não encontrado");
            return;
        }

        List<AdicionarItemCardapioRequestDTO> itensCardapio = objectMapper.readValue(
                inputStream,
                new TypeReference<List<AdicionarItemCardapioRequestDTO>>() {}
        );

        int startIndex = itemCardapioIndex * 3;
        int endIndex = Math.min(startIndex + 3, itensCardapio.size());

        for (int i = startIndex; i < endIndex; i++) {
            try {
                adicionarItemCardapioInputPort.execute(restauranteID, itensCardapio.get(i).toInputModel());
            } catch (Exception e) {
                System.err.println("Erro ao cadastrar item de cardápio: " + itensCardapio.get(i).nome() + " - " + e.getMessage());
            }
        }
    }
}