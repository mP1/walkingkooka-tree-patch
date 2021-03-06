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

public final class NodePatchNotEmptyCopyOrMoveCopyTest extends NodePatchNotEmptyCopyOrMoveTestCase<NodePatchNotEmptyCopyOrMoveCopy<JsonNode, JsonPropertyName>> {

    @Test
    public void testCopyChild() {
        this.applyAndCheck(this.createPatch(),
                "{\"b2\": \"COPIED\"}",
                "{\"a1\": \"COPIED\", \"b2\": \"COPIED\"}");
    }

    @Test
    public void testCopyChild2() {
        this.applyAndCheck(this.createPatch(),
                "{\"b2\": \"COPIED\", \"c3\": \"value3\"}",
                "{\"a1\": \"COPIED\", \"b2\": \"COPIED\", \"c3\": \"value3\"}");
    }

    @Test
    public void testCopyDifferentBranches() {
        this.applyAndCheck(this.createPatch("/a1/b2", "/a1/c3"),
                "{\"a1\": { \"b2\": \"COPIED\"}}",
                "{\"a1\": { \"b2\": \"COPIED\", \"c3\": \"COPIED\"}}");
    }

    @Override
    NodePatchNotEmptyCopyOrMoveCopy<JsonNode, JsonPropertyName> createPatch(final NodePointer<JsonNode, JsonPropertyName> from,
                                                                            final NodePointer<JsonNode, JsonPropertyName> path) {
        return NodePatchNotEmptyCopyOrMoveCopy.with(from, path);
    }

    @Override
    String operation() {
        return "copy";
    }

    // ClassTesting2............................................................................

    @Override
    public Class<NodePatchNotEmptyCopyOrMoveCopy<JsonNode, JsonPropertyName>> type() {
        return Cast.to(NodePatchNotEmptyCopyOrMoveCopy.class);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNameSuffix() {
        return "Copy";
    }
}
