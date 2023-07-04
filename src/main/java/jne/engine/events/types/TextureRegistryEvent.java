package jne.engine.events.types;

import jne.engine.texture.TextureContainer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class TextureRegistryEvent extends Event {

    private final TextureContainer container;

}
