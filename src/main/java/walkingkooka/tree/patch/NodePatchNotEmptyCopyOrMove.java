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
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.pointer.NodePointer;

import java.util.Objects;

/**
 * Base class for both copy and move operations.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
abstract class NodePatchNotEmptyCopyOrMove<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNonEmpty<N, NAME> {

    static <N extends Node<N, NAME, ?, ?>, NAME extends Name> void checkFromAndPath(final NodePointer<N, NAME> from,
                                                                                    final NodePointer<N, NAME> path) {
        Objects.requireNonNull(from, "from");
        checkPath(path);

        if (from.equals(path)) {
            throw new IllegalArgumentException("From and to path are equal=" + from);
        }
    }

    NodePatchNotEmptyCopyOrMove(final NodePointer<N, NAME> from,
                                final NodePointer<N, NAME> path,
                                final NodePatchNonEmpty<N, NAME> next) {
        super(path, next);
        this.from = from;
    }

    final NodePointer<N, NAME> from;

    // Object........................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.from, this.path, this.next);
    }

    @Override
    final boolean equals1(final NodePatchNonEmpty<?, ?> other) {
        return this.path.equals(other.path) &&
                this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodePatchNotEmptyCopyOrMove<?, ?> other) {
        return this.from.equals(other.from);
    }

    @Override
    final void toString0(final StringBuilder b) {
        b.append(this.operation())
                .append(" from=")
                .append(toString(this.from))
                .append(" path=")
                .append(toString(this.path));
    }

    /**
     * Should return either "copy" or "move".
     */
    abstract String operation();

    // JsonNodeMarshallContext................................................................................................

    /**
     * <pre>
     * {
     *     "op": "add",
     *     "path-name-type": "json-property-name",
     *     "from: "/1/2/from"
     *     "path": "/1/2/target",
     *     "value-type": "json-node",
     *     "value": []
     * }
     * </pre>
     */
    @Override
    final JsonObject marshall1(final JsonObject object,
                               final NodePatchToJsonFormat format,
                               final JsonNodeMarshallContext context) {
        return this.setPath(
                format.setPathNameType(object, this.from, this.path, context)
                        .set(FROM_PROPERTY, pathJsonNodeMarshall(this.from)));
    }
}
