package com.postechfiap.meumenu.core.domain.usecases.proprietario.impl;

import com.postechfiap.meumenu.core.domain.entities.EnderecoDomain;
import com.postechfiap.meumenu.core.domain.entities.ProprietarioDomain;
import com.postechfiap.meumenu.core.domain.services.PasswordService;
import com.postechfiap.meumenu.core.domain.usecases.proprietario.CadastrarProprietarioUseCase;
import com.postechfiap.meumenu.core.dtos.proprietario.CadastrarProprietarioInputModel;
import com.postechfiap.meumenu.core.exceptions.BusinessException;
import com.postechfiap.meumenu.core.gateways.ProprietarioGateway;
import com.postechfiap.meumenu.core.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CadastrarProprietarioUseCaseImpl implements CadastrarProprietarioUseCase {

    private final ProprietarioGateway proprietarioGateway;
    private final UsuarioGateway usuarioGateway;
    private final PasswordService passwordService;


    @Override
    public ProprietarioDomain execute(CadastrarProprietarioInputModel input) {
        if (usuarioGateway.existsByLogin(input.getLogin())) {
            throw new BusinessException("Login já cadastrado.");
        }
        if (usuarioGateway.existsByEmail(input.getEmail())) {
            throw new BusinessException("Email já cadastrado.");
        }
        if (proprietarioGateway.existsByCpf(input.getCpf())) {
            throw new BusinessException("CPF já cadastrado.");
        }

        String senhaCriptografada = passwordService.encryptPassword(input.getSenha());

        ProprietarioDomain novoProprietario = new ProprietarioDomain(
                input.getNome(), input.getEmail(), input.getLogin(), senhaCriptografada,
                input.getCpf(),
                input.getWhatsapp()
        );

        List<EnderecoDomain> enderecosDomain = input.getEnderecos().stream()
                .map(enderecoInput -> new EnderecoDomain(
                        enderecoInput.getEstado(),
                        enderecoInput.getCidade(),
                        enderecoInput.getBairro(),
                        enderecoInput.getRua(),
                        enderecoInput.getNumero(),
                        enderecoInput.getComplemento(),
                        enderecoInput.getCep()
                ))
                .collect(Collectors.toList());

        enderecosDomain.forEach(endereco -> endereco.setUsuario(novoProprietario));
        novoProprietario.setEnderecos(enderecosDomain);
        return proprietarioGateway.cadastrarProprietario(novoProprietario);


    }
}