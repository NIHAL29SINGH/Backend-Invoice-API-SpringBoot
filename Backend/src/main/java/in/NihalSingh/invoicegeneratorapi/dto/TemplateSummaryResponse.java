package in.NihalSingh.invoicegeneratorapi.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class TemplateSummaryResponse {

    private Long id;
    private String name;
    private boolean active;
}