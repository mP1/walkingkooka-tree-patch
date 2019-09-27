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
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.pointer.NodePointer;

/**
 * Represents a TEST node-patch operation.
 */
final class NodePatchNotEmptyAddReplaceOrTestTest<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNotEmptyAddReplaceOrTest<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchNotEmptyAddReplaceOrTestTest<N, NAME> with(final NodePointer<N, NAME> path,
                                                                                                                  final N value) {
        checkPath(path);
        checkValue(value);

        return new NodePatchNotEmptyAddReplaceOrTestTest<>(path,
                value.removeParent(),
                null);
    }

    private NodePatchNotEmptyAddReplaceOrTestTest(final NodePointer<N, NAME> path,
                                                  final N value,
                                                  final NodePatchNonEmpty<N, NAME> next) {
        super(path, value, next);
    }

    @Override
    NodePatchNotEmptyAddReplaceOrTestTest<N, NAME> append0(final NodePatchNonEmpty<N, NAME> next) {
        return new NodePatchNotEmptyAddReplaceOrTestTest<>(this.path,
                this.value,
                next);
    }

    /**
     * Find the node at the path and then test its value and then return the original {@link Node}.
     */
    @Override
    final N apply1(final N node, final NodePointer<N, NAME> start) {
        this.path.traverse(node).ifPresentOrElse(
                this::test,
                this::throwFailed);
        return node;
    }

    private void test(final N node) {
        final N without = node.removeParent();
        if (!this.value.equals(without)) {
            throw new ApplyNodePatchException("Value test failed: " + without, this);
        }
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePatchNotEmptyAddReplaceOrTestTest;
    }

    @Override
    String operation() {
        return TEST;
    }

    // JsonNodeMarshallContext................................................................................................

    private final static JsonObjectNode JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(TEST));

    @Override
    JsonObjectNode jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }
}
