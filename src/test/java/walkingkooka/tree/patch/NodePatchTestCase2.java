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

package walkingkooka.tree.patch;

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.pointer.NodePointer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePatchTestCase2<P> extends NodePatchTestCase<P> {

    NodePatchTestCase2() {
        super();
    }

    final NodePointer<JsonNode, JsonPropertyName> path1() {
        return NodePointer.named(this.property1(), JsonNode.class);
    }

    final JsonPropertyName property1() {
        return JsonPropertyName.with("a1");
    }

    final JsonNode value1() {
        return JsonNode.string("value1");
    }

    final NodePointer<JsonNode, JsonPropertyName> path2() {
        return NodePointer.named(this.property2(), JsonNode.class);
    }

    final JsonPropertyName property2() {
        return JsonPropertyName.with("b2");
    }

    final JsonNode value2() {
        return JsonNode.string("value2");
    }

    final NodePointer<JsonNode, JsonPropertyName> path3() {
        return NodePointer.named(this.property3(), JsonNode.class);
    }

    final JsonPropertyName property3() {
        return JsonPropertyName.with("c3");
    }

    final JsonNode value3() {
        return JsonNode.string("value3");
    }

    final NodePointer<JsonNode, JsonPropertyName> pointer(final String path) {
        return NodePointer.parse(path, JsonPropertyName::with, JsonNode.class);
    }

    final void applyAndCheck(final NodePatch<JsonNode, JsonPropertyName> patch,
                             final String before,
                             final String expected) {
        this.applyAndCheck(patch,
                JsonNode.parse(before),
                JsonNode.parse(expected));
    }

    final void applyAndCheck(final NodePatch<JsonNode, JsonPropertyName> patch,
                             final JsonNode before,
                             final JsonNode expected) {
        assertEquals(expected,
                patch.apply(before),
                () -> "patch " + patch + " failed");
    }

    final ApplyNodePatchException applyFails(final NodePatch<JsonNode, JsonPropertyName> patch,
                                             final String json) {
        return this.applyFails(patch, JsonNode.parse(json));
    }

    final ApplyNodePatchException applyFails(final NodePatch<JsonNode, JsonPropertyName> patch,
                                             final JsonNode before) {
        return assertThrows(ApplyNodePatchException.class, () -> patch.apply(before));
    }
}
