package in.NihalSingh.invoicegeneratorapi.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Item {
    private String name;
    private int qty;
    private double amount;
}
