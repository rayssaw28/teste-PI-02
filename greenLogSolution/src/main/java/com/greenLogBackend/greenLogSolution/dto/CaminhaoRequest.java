package com.greenLogBackend.greenLogSolution.dto;

import com.greenLogBackend.greenLogSolution.enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.util.Set;

public record CaminhaoRequest(
        @NotBlank(message = "A placa é obrigatória")
        @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", message = "A placa deve seguir o padrão (Ex: ABC1234 ou ABC1D23)")
        String placa,

        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @NotBlank(message = "O motorista é obrigatório")
        String motorista,

        @Positive(message = "A capacidade deve ser maior que zero")
        Double capacidadeCarga,

        @NotEmpty(message = "O caminhão deve aceitar pelo menos um tipo de resíduo")
        Set<TipoResiduo> tiposResiduos
) {}