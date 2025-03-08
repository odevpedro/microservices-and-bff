package br.com.odevpedro.user_service_api.entity;

import br.com.odevpedro.hd_commons_lib.models.enums.ProfileEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import java.util.Set;

@With
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private Set<ProfileEnum> profile;
}