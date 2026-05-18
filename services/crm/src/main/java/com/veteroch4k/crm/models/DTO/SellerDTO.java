package com.veteroch4k.crm.models.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Данные для создания или обновления продавца")
public record SellerDTO(

    @Schema(description = "Имя продавца", example = "Григорян")
    @NotBlank(message = "Имя продавца не может быть пустым")
    @Length(max = 50, message = "Имя продавца не должно быть длиннее 50 символов")
    String name,

    @Schema(description = "Контактная информация продавца",
        example = "почта: ivanich@mail.ru")
    @Length(max = 1000, message = "Контактная информация избыточна: не должна превышать 1000 символов")
    String contactInfo
)
{}
