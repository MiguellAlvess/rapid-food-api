package br.com.db.rapid_food_api.vendors.domain.enums;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CNPJ;

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

    private CNPJ cnpj; // ou string - ver depois

    @Column(nullable = false)
    private Boolean active;

    public  void deactivate() {
        if (!this.active) {
            throw new IllegalStateException("Restaurante está inativo no momento");
        }

        this.active = false;
    }

}
