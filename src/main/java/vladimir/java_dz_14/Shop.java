package vladimir.java_dz_14;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String category;
    private String description;
}
