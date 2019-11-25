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

import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.pointer.NodePointer;

/**
 * Represents a REPLACE operation within a patch.
 */
final class NodePatchNotEmptyAddReplaceOrTestReplace<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNotEmptyAddReplaceOrTest<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchNotEmptyAddReplaceOrTestReplace<N, NAME> with(final NodePointer<N, NAME> path,
                                                                                                                     final N value) {
        checkPath(path);
        checkValue(value);

        return new NodePatchNotEmptyAddReplaceOrTestReplace<>(path, value, null);
    }

    private NodePatchNotEmptyAddReplaceOrTestReplace(final NodePointer<N, NAME> path,
                                                     final N value,
                                                     final NodePatchNonEmpty<N, NAME> next) {
        super(path, value, next);
    }

    @Override
    NodePatchNotEmptyAddReplaceOrTestReplace<N, NAME> append0(final NodePatchNonEmpty<N, NAME> next) {
        return new NodePatchNotEmptyAddReplaceOrTestReplace<>(this.path,
                this.value,
                next);
    }

    /**
     * <a href="http://jsonpatch.com/"></a>
     * <pre>
     * Replace
     * { "op": "replace", "path": "/biscuits/0/name", "value": "Chocolate Digestive" }
     * Replaces a value. Equivalent to a “remove” followed by an “add”.
     * </pre>
     */
    @Override
    final N apply1(final N node, final NodePointer<N, NAME> start) {
        return this.add0(this.remove0(node, this.path, start),
                this.value,
                start);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePatchNotEmptyAddReplaceOrTestReplace;
    }

    @Override
    String operation() {
        return REPLACE;
    }

    // JsonNodeMarshallContext................................................................................................

    private final static JsonObject JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(REPLACE));

    @Override
    JsonObject jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }
}
