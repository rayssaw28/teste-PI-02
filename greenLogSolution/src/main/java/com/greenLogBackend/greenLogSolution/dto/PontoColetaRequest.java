package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record PontoColetaRequest(
        @NotBlank(message = "O nome do ponto é obrigatório")
        String nome,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "O nome do responsável é obrigatório")
        String responsavel,

        @NotBlank(message = "O CPF do responsável é obrigatório")
        @Pattern(regexp = "^\\d{11}$", message = "O CPF deve conter apenas 11 dígitos numéricos")
        String cpfResponsavel,

        @NotBlank(message = "O contato é obrigatório")
        String contato,

        @NotEmpty(message = "O ponto deve aceitar pelo menos um tipo de resíduo")
        Set<TipoResiduo> tiposResiduos,

        @NotNull(message = "O ID do bairro é obrigatório")
        Long bairroId
) {}