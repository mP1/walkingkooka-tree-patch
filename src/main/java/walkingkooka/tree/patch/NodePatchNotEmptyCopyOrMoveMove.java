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
import walkingkooka.tree.pointer.NodePointerException;


/**
 * Represents a MOVE patch operation.
 */
final class NodePatchNotEmptyCopyOrMoveMove<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNotEmptyCopyOrMove<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchNotEmptyCopyOrMoveMove<N, NAME> with(final NodePointer<N, NAME> from,
                                                                                                            final NodePointer<N, NAME> path) {
        checkFromAndPath(from, path);

        return new NodePatchNotEmptyCopyOrMoveMove<>(from, path, null);
    }

    private NodePatchNotEmptyCopyOrMoveMove(final NodePointer<N, NAME> from,
                                            final NodePointer<N, NAME> path,
                                            final NodePatchNonEmpty<N, NAME> next) {
        super(from, path, next);
    }

    @Override
    NodePatchNotEmptyCopyOrMoveMove<N, NAME> append0(final NodePatchNonEmpty<N, NAME> patch) {
        return new NodePatchNotEmptyCopyOrMoveMove<>(this.from, this.path, patch);
    }

    /**
     * Identical to COPY but the original node is removed.
     */
    @Override
    N apply1(final N node, final NodePointer<N, NAME> start) {
        final N copying = this.from.traverse(node)
                .orElseThrow(() -> new NodePointerException("Unable to navigate to find node to copy from: " + node));
        return this.add0(this.remove0(node, this.from, start),
                copying,
                start);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePatchNotEmptyCopyOrMoveMove;
    }

    @Override
    String operation() {
        return MOVE;
    }

    // HasJsonNode...............................................................................

    private final static JsonObject JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(MOVE));

    @Override
    JsonObject jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }
}
