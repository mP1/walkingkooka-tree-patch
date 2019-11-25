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

public final class NodePatchNotEmptyCopyOrMoveMoveTest extends NodePatchNotEmptyCopyOrMoveTestCase<NodePatchNotEmptyCopyOrMoveMove<JsonNode, JsonPropertyName>> {

    @Test
    public void testMoveChild() {
        this.applyAndCheck(this.createPatch(),
                "{\"b2\": \"value1\"}",
                "{\"a1\": \"value1\"}");
    }

    @Test
    public void testMoveChild2() {
        this.applyAndCheck(this.createPatch(),
                "{\"b2\": \"value1\", \"c3\": \"value3\"}",
                "{\"a1\": \"value1\", \"c3\": \"value3\"}");
    }

    @Test
    public void testMoveDifferentBranches() {
        this.applyAndCheck(this.createPatch("/a1/b2", "/a1/c3"),
                "{\"a1\": { \"b2\": \"value1\"}}",
                "{\"a1\": { \"c3\": \"value1\"}}");
    }

    @Override
    NodePatchNotEmptyCopyOrMoveMove<JsonNode, JsonPropertyName> createPatch(final NodePointer<JsonNode, JsonPropertyName> from,
                                                                            final NodePointer<JsonNode, JsonPropertyName> path) {
        return NodePatchNotEmptyCopyOrMoveMove.with(from, path);
    }

    @Override
    String operation() {
        return "move";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<NodePatchNotEmptyCopyOrMoveMove<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatchNotEmptyCopyOrMoveMove.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Move";
    }
}
