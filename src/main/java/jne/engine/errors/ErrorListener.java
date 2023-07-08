package jne.engine.errors;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorListener {

    private final Long initTime;
    private final Long lifeTime;

    private final String keyError;
    private final String fullError;



}
