/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.tree.patch.sample;

import org.junit.jupiter.api.Assertions;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.patch.NodePatch;
import walkingkooka.tree.pointer.NodePointer;

public final class Sample {

    public static void main(final String[] args) {
        final JsonNode before = JsonNode.parse("{\"hello\": { \"colour\": \"replaced-1\", \"tree\": \"removed-3\"}}");

        final NodePatch<JsonNode, JsonPropertyName> patch = emptyPatch()
            .add(pointer("/added-1"), JsonNode.number(1))
            .add(pointer("/hello/colour"), JsonNode.string("green-2"))
            .remove(pointer("/hello/tree"));

        final JsonNode after = patch.apply(before);

        final JsonNode expected = JsonNode.parse("{\n" +
            "  \"hello\": {\n" +
            "    \"colour\": \"green-2\"\n" +
            "  },\n" +
            "  \"added-1\": 1\n" +
            "}");

        Assertions.assertEquals(expected, after);
    }

    private static NodePatch<JsonNode, JsonPropertyName> emptyPatch() {
        return NodePatch.empty(JsonNode.class);
    }

    private static NodePointer<JsonNode, JsonPropertyName> pointer(final String path) {
        return NodePointer.parse(path, JsonPropertyName::with, JsonNode.class);
    }
}
