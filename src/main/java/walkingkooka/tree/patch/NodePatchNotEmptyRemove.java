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
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * Represents an REMOVE operation within a patch.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
final class NodePatchNotEmptyRemove<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNonEmpty<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchNotEmptyRemove<N, NAME> with(final NodePointer<N, NAME> path) {
        checkPath(path);

        return new NodePatchNotEmptyRemove<>(path, null);
    }

    private NodePatchNotEmptyRemove(final NodePointer<N, NAME> path,
                                    final NodePatchNonEmpty<N, NAME> next) {
        super(path, next);
    }

    @Override
    NodePatchNotEmptyRemove<N, NAME> append0(final NodePatchNonEmpty<N, NAME> next) {
        return new NodePatchNotEmptyRemove<>(this.path, next);
    }

    @Override
    final N apply1(final N node, final NodePointer<N, NAME> start) {
        return this.remove0(node, this.path, start);
    }

    // Object........................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.path, this.next);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodePatchNotEmptyRemove;
    }

    @Override
    boolean equals1(final NodePatchNonEmpty<?, ?> other) {
        return this.path.equals(other.path);
    }

    @Override
    void toString0(final StringBuilder b) {
        b.append(REMOVE + " path=")
                .append(toString(this.path));
    }

    // JsonNodeContext..................................................................................................

    private final static JsonObject JSON_OBJECT_WITH_OPERATION = JsonNode.object()
            .set(OP_PROPERTY, JsonNode.string(REMOVE));

    @Override
    JsonObject jsonObjectWithOp() {
        return JSON_OBJECT_WITH_OPERATION;
    }

    /**
     * <pre>
     * {
     *     "op": "add",
     *     "path-name-type": "json-property-name",
     *     "path": "/1/2/abc"
     * }
     * </pre>
     */
    @Override
    JsonObject marshall1(final JsonObject object,
                         final NodePatchToJsonFormat format,
                         final JsonNodeMarshallContext context) {
        return this.setPath(format.setPathNameType(object,
                this.path,
                context));
    }
}
