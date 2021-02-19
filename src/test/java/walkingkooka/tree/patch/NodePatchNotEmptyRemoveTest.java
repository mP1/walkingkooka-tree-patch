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
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.pointer.NodePointer;

public final class NodePatchNotEmptyRemoveTest extends NodePatchNonEmptyTestCase<NodePatchNotEmptyRemove<JsonNode, JsonPropertyName>> {

    @Test
    public void testPathUnknownFails() {
        this.applyFails(this.createPatch("/a1/b2/c3"),
                JsonNode.object());
    }

    @Test
    public void testPathUnknownFails2() {
        this.applyFails(this.createPatch("/a1/b2/c3"),
                "{\"a1\": {}}");
    }

    @Test
    public void testRemoveChild() {
        this.applyAndCheck(this.createPatch(),
                JsonNode.object()
                        .set(this.property1(), this.value1()),
                JsonNode.object());
    }

    @Test
    public void testRemoveTwoChildren() {
        this.applyAndCheck(this.createPatch().append0(this.createPatch(this.property2())),
                "{\"a1\":\"value1\", \"b2\": \"value2\", \"c3\": \"value3\"}",
                "{\"c3\": \"value3\"}");
    }

    @Test
    public void testRemoveGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2"),
                "{\"a1\": {\"b2\": \"value2\"}}",
                "{\"a1\": {}}");
    }

    @Test
    public void testRemoveGreatGrandChild() {
        this.applyAndCheck(this.createPatch("/a1/b2/c3"),
                "{\"a1\": {\"b2\": {\"c3\": \"value3\"}}}",
                "{\"a1\": { \"b2\": {}}}");
    }

    @Test
    public void testRemoveMultiStep() {
        this.applyAndCheck(this.createPatch("/a1/b2").append0(this.createPatch("/a1")),
                "{\"a1\": {\"b2\": \"value2\"}, \"c3\": \"value3\"}",
                "{\"c3\": \"value3\"}");
    }

    @Test
    public void testJsonNodeUnmarshallRequiredPathNameTypeMissingFails() {
        this.unmarshallAndCheck("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    // HasJsonNode..................................................................................................

    @Test
    public void testJsonNodeUnmarshallFromPropertyFails() {
        this.unmarshallFails("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"from\": \"/123\"\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeUnmarshallValueTypePropertyFails() {
        this.unmarshallFails("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"value-type\": \"json-property-name\"\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeUnmarshallValuePropertyFails() {
        this.unmarshallFails("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"value\": true\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeUnmarshallPathNameTypeMissing() {
        this.unmarshallAndCheck("[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/123\"\n" +
                        "}]",
                this.createPatch(NodePointer.indexed(123, JsonNode.class)));
    }

    @Test
    public void testJsonNodeMarshallPathNameTypeNotRequired() {
        this.marshallAndCheck(this.createPatch(NodePointer.indexed(123, JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path\": \"/123\"\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeMarshallPathNameTypeRequired() {
        this.marshallAndCheck(this.createPatch(NodePointer.named(JsonPropertyName.with("abc"), JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path-name-type\": \"json-property-name\",\n" +
                        "  \"path\": \"/abc\"\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeMarshallPathNameTypeRequired2() {
        this.marshallAndCheck(this.createPatch(NodePointer.named(JsonPropertyName.with("abc"), JsonNode.class)),
                "[{\n" +
                        "  \"op\": \"remove\",\n" +
                        "  \"path\": \"/abc\",\n" +
                        "  \"path-name-type\": \"json-property-name\"\n" +
                        "}]");
    }

    @Test
    public void testJsonNodeMarshallRoundtrip() {
        this.marshallWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .remove(this.path2()));
    }

    @Test
    public void testJsonNodeMarshallRoundtrip2() {
        this.marshallWithTypeRoundTripTwiceAndCheck(this.createPatch()
                .remove(this.path2())
                .remove(this.path3()));
    }

    // fromJsonPatch/toJsonPatch..........................................................................................

    @Test
    public final void testFromJsonPatch() {
        this.fromJsonPatchAndCheck2("[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]",
                this.createPatch());
    }

    @Test
    public final void testToJsonPatch() {
        this.toJsonPatchAndCheck2(this.createPatch(),
                "[{\n" +
                        "  \"op\": \"$OP\",\n" +
                        "  \"path\": \"/a1\"\n" +
                        "}]");
    }

    @Test
    public final void testToJsonPatchFromJsonPatch() {

    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPatch(), "remove path=\"/a1\"");
    }

    @Override
    NodePatchNotEmptyRemove<JsonNode, JsonPropertyName> createPatch(final NodePointer<JsonNode, JsonPropertyName> path) {
        return NodePatchNotEmptyRemove.with(path);
    }

    private NodePatchNotEmptyRemove<JsonNode, JsonPropertyName> createPatch(final JsonPropertyName property) {
        return NodePatchNotEmptyRemove.with(NodePointer.named(property, JsonNode.class));
    }

    private NodePatchNotEmptyRemove<JsonNode, JsonPropertyName> createPatch(final String path) {
        return NodePatchNotEmptyRemove.with(this.pointer(path));
    }

    @Override
    String operation() {
        return "remove";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<NodePatchNotEmptyRemove<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatchNotEmptyRemove.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Remove";
    }
}
