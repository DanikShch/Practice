package practice.internetshop.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private Map<String, List<String>> errors;

    public ErrorResponse(String message) {
        this.message = message;
        this.errors = null;
    }
}