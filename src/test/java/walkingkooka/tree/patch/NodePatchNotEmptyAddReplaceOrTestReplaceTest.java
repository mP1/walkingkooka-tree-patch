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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.pointer.NodePointer;

public final class NodePatchNotEmptyAddReplaceOrTestReplaceTest extends NodePatchNotEmptyAddReplaceOrTestTestCase<NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName>> {

    @Test
    public void testPathUnknownFails() {
        this.applyFails(this.createPatch("/1/1/1", this.value1()),
                JsonNode.object());
    }

    @Test
    public void testReplacePathMissingFails() {
        this.applyFails(this.createPatch(),
                "{\"b2\": \"value2\"}");
    }

    @Test
    public void testReplaceChild() {
        this.applyAndCheck(this.createPatch(),
                "{\"a1\": \"old-a1\"}",
                "{\"a1\": \"value1\"}");
    }

    @Test
    public void testReplaceChild2() {
        this.applyAndCheck(this.createPatch(),
                "{\"a1\": \"old-a1\", \"b2\": \"value2\"}",
                "{\"a1\": \"value1\", \"b2\": \"value2\"}");
    }

    @Test
    public void testReplaceChildTwice() {
        this.applyAndCheck(this.createPatch().append0(this.createPatch(this.property2(), this.value2())),
                "{\"a1\": \"old-a1\", \"b2\": \"old-b2\"}",
                "{\"a1\":\"value1\", \"b2\": \"value2\"}");
    }

    @Test
    public void testReplaceGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2"),
                "{\"a1\": {\"b2\": \"old-b2\"}}",
                "{\"a1\": {\"b2\": {\"c3\": \"value3\"}}}");
    }
//
//    @Test
//    public void testReplaceMultiStep() {
//        this.applyAndCheck(this.createPatch("/a1/b2", "{\"c3\": \"old-c3\"}").append(this.createPatch("/a1/b2/c3", this.value3())),
//                "{\"a1\": {\"b2\": \"old-b2\"}}",
//                "{\"a1\": {\"b2\": {\"c3\": \"value3\"}}}");
//    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPatch(), "replace path=\"/a1\" value=\"value1\"");
    }

    @Override
    NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName> createPatch(final NodePointer<JsonNode, JsonPropertyName> path, JsonNode value) {
        return NodePatchNotEmptyAddReplaceOrTestReplace.with(path, value);
    }

    private NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName> createPatch(final JsonPropertyName property, final JsonNode value) {
        return NodePatchNotEmptyAddReplaceOrTestReplace.with(NodePointer.named(property, JsonNode.class), value);
    }

    private NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName> createPatch(final String path) {
        return this.createPatch(path, JsonNode.parse("{\"c3\": \"value3\"}"));
    }

    private NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName> createPatch(final String path, final JsonNode value) {
        return NodePatchNotEmptyAddReplaceOrTestReplace.with(this.pointer(path), value);
    }

    @Override
    String operation() {
        return "replace";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<NodePatchNotEmptyAddReplaceOrTestReplace<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatchNotEmptyAddReplaceOrTestReplace.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Replace";
    }
}
