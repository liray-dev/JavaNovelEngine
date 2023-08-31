package jne.engine.debug;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@Data
@RequiredArgsConstructor
public class DebugListener {

    private final Long initTime;
    private final Long lifeTime;

    private final String keyError;
    private final String fullError;

    private final Color color;

}
