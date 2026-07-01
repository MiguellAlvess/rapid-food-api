package br.com.db.rapid_food_api.vendors.domain;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vendors")
@Getter
@Setter
@NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String cnpj;

    @Column(nullable = false)
    private Boolean active;

    public  void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Restaurante está inativo no momento");
        }

        this.active = false;
    }

    public Vendor (String name, String cnpj){
        this.name = name;
        this.cnpj = cnpj;
        this.active = true;
    }
}