package io.github.xmori.emailnormalizer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailNormalizerTest {
    @Test void trimsAndLowercasesOnlyTheDomain() {
        assertEquals("Jane@sample.com", EmailNormalizer.normalize(" Jane@SAMPLE.COM "));
        assertEquals("Jane.Doe+tag@example.com", EmailNormalizer.normalize("Jane.Doe+tag@Example.COM"));
    }

    @Test void rejectsMalformedAddresses() {
        for (String bad : new String[] {"", "jane", "@example.com", "jane@", "a@b@c", "ja ne@example.com", "jane@exa\tmple.com"}) {
            assertThrows(IllegalArgumentException.class, () -> EmailNormalizer.normalize(bad), bad);
        }
        assertThrows(IllegalArgumentException.class, () -> EmailNormalizer.normalize(null));
    }
}
