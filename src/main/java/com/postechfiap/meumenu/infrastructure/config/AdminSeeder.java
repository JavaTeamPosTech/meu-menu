package com.postechfiap.meumenu.infrastructure.config;

import com.postechfiap.meumenu.core.domain.entities.AdminDomain;
import com.postechfiap.meumenu.core.gateways.AdminGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private final PasswordService passwordService;

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
    }
}