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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.pointer.NodePointer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NodePatchTestCase3<P extends NodePatch<JsonNode, JsonPropertyName>> extends NodePatchTestCase2<P>
        implements HashCodeEqualsDefinedTesting2<P>,
        JsonNodeMarshallingTesting<P>,
        TypeNameTesting<P> {

    NodePatchTestCase3() {
        super();
    }

    @Test
    public final void testApplyNullFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().apply(null));
    }

    @Test
    public final void testAddNullPathFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().add(null, JsonNode.string("value")));
    }

    @Test
    public final void testAddNullValueFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().add(this.path(), null));
    }

    @Test
    public final void testCopyNullFromFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().copy(null, this.path()));
    }

    @Test
    public final void testCopyNullPathFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().copy(this.path(), null));
    }

    @Test
    public final void testCopyFromSameAsPathFails() {
        final NodePointer<JsonNode, JsonPropertyName> path = this.path();

        assertThrows(IllegalArgumentException.class, () -> this.createPatch().copy(path, path));
    }

    @Test
    public final void testMoveNullFromFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().move(null, this.path()));
    }

    @Test
    public final void testMoveNullPathFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().move(this.path(), null));
    }

    @Test
    public final void testMoveFromSameAsPathFails() {
        final NodePointer<JsonNode, JsonPropertyName> path = this.path();

        assertThrows(IllegalArgumentException.class, () -> this.createPatch().move(path, path));
    }

    @Test
    public final void testReplaceNullPathFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().replace(null, this.value()));
    }

    @Test
    public final void testReplaceNullValueFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().replace(this.path(), null));
    }

    @Test
    public final void testRemoveNullFromFails() {
        assertThrows(NullPointerException.class, () -> this.createPatch().remove(null));
    }

    // HasJsonNode..................................................................................

    @Test
    public final void testFromJsonBooleanNodeFails() {
        this.unmarshallFails(JsonNode.booleanNode(true));
    }

    @Test
    public final void testFromJsonNumberNodeFails() {
        this.unmarshallFails(JsonNode.number(123));
    }

    @Test
    public final void testFromJsonObjectNodeFails() {
        this.unmarshallFails(JsonNode.object());
    }

    @Test
    public final void testFromJsonStringNodeFails() {
        this.unmarshallFails(JsonNode.string("string123"));
    }

    final void toJsonPatchAndCheck(final NodePatch<JsonNode, JsonPropertyName> patch,
                                   final String json) {
        this.toJsonPatchAndCheck(patch,
                JsonNode.parse(json));
    }

    final void toJsonPatchAndCheck(final NodePatch<JsonNode, JsonPropertyName> patch,
                                   final JsonNode node) {
        this.checkEquals(node,
                patch.toJsonPatch(),
                () -> patch + " toJsonPatch");
    }

    // NodePatchTestCase3..................................................................................

    abstract P createPatch();

    @Override
    public final P createObject() {
        return this.createPatch();
    }

    private NodePointer<JsonNode, JsonPropertyName> path() {
        return NodePointer.any(JsonNode.class);
    }

    private JsonNode value() {
        return JsonNode.nullNode();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public final P unmarshall(final JsonNode from,
                              final JsonNodeUnmarshallContext context) {
        return Cast.to(NodePatch.unmarshall(from, context));
    }

    @Override
    public final P createJsonNodeMarshallingValue() {
        return this.createPatch();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return NodePatch.class.getSimpleName();
    }
}
