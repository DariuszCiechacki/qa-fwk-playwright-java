package io.github.qa.config;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents browser viewport dimensions.
 */
@Getter
@Setter
public class ViewportConfig {
    /** Width of the browser viewport in pixels. */
    private int width;

    /** Height of the browser viewport in pixels. */
    private int height;
}
