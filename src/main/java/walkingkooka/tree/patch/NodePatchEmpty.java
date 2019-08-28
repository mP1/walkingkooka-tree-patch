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

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.ToJsonNodeContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * An empty patch, which also acts at the starting point for building a multi step patch.
 */
final class NodePatchEmpty<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatch<N, NAME> {

    /**
     * Creates an empty patch to begin addition of new operations.
     */
    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchEmpty<N, NAME> get(final Class<N> type) {
        Objects.requireNonNull(type, "type");
        return Cast.to(INSTANCE);
    }

    /**
     * Creates an empty patch to begin addition of new operations.
     */
    static NodePatchEmpty<?, ?> getWildcard() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton instance
     */
    private final static NodePatchEmpty<?, ?> INSTANCE = new NodePatchEmpty<>();

    /**
     * Private ctor use singleton getter.
     */
    private NodePatchEmpty() {
        super();
    }

    /**
     * Adding a patch to this returns the patch.
     */
    @Override
    NodePatchNonEmpty<N, NAME> append0(final NodePatchNonEmpty<N, NAME> next) {
        return next;
    }

    /**
     * Do nothing and return the original {@link Node}.
     */
    @Override
    N apply0(final N node, final NodePointer<N, NAME> start) {
        return node;
    }

    @Override
    NodePatchNonEmpty<N, NAME> nextOrNull() {
        return null; // end of patch.
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    void toString0(final StringBuilder b) {
        throw new UnsupportedOperationException(); // never
    }

    // HasJsonNode.............................................................................................

    /**
     * An empty patch is represented as an empty {@link JsonArrayNode}.
     * <pre>
     * []
     * </pre>
     */
    @Override
    JsonArrayNode toJsonNode0(final NodePatchToJsonFormat format,
                              final ToJsonNodeContext context) {
        return JsonNode.array();
    }
}
