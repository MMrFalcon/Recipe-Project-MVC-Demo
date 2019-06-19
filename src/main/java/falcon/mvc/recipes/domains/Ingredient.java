package falcon.mvc.recipes.domains;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal amount;

    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure unitOfMeasure;


    public Ingredient(){}

    public Ingredient (String name, BigDecimal amount, UnitOfMeasure unit) {
        this.name = name;
        this.amount = amount;
        this.unitOfMeasure = unit;
    }

}
