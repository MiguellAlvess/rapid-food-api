package br.com.db.rapid_food_api.vendors.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vendors")
@Getter
@Setter
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String cnpj;

    @Column(nullable = false)
    private Boolean active;

    public void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Restaurante está inativo no momento");
        }

        this.active = false;
    }

}
