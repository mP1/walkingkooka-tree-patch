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
 * Base class for both add and replace.
 */
@SuppressWarnings("lgtm[java/inconsistent-equals-and-hashcode]")
abstract class NodePatchNotEmptyAddReplaceOrTest<N extends Node<N, NAME, ?, ?>, NAME extends Name> extends NodePatchNonEmpty<N, NAME> {

    static void checkValue(final Node<?, ?, ?, ?> node) {
        Objects.requireNonNull(node, "node");
    }

    NodePatchNotEmptyAddReplaceOrTest(final NodePointer<N, NAME> path,
                                      final N value,
                                      final NodePatchNonEmpty<N, NAME> next) {
        super(path, next);
        this.value = value.removeParent();
    }

    final N value;

    // Object........................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.path, this.value, this.next);
    }

    @Override
    final boolean equals1(final NodePatchNonEmpty<?, ?> other) {
        return this.path.equals(other.path) &&
                this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodePatchNotEmptyAddReplaceOrTest<?, ?> other) {
        return this.value.equals(other.value);
    }

    @Override
    final void toString0(final StringBuilder b) {
        b.append(this.operation())
                .append(" path=")
                .append(toString(this.path))
                .append(" value=")
                .append(this.value);
    }

    /**
     * Should return either "add" or "replace".
     */
    abstract String operation();

    // JsonNodeMarshallContext................................................................................................

    /**
     * <pre>
     * {
     *     "op": "add",
     *     "path-name-type": "json-property-name",
     *     "path": "/1/2/abc",
     *     "value-type": "json-node",
     *     "value": []
     * }
     * </pre>
     */
    @Override
    final JsonObject marshall1(final JsonObject object,
                               final NodePatchToJsonFormat format,
                               final JsonNodeMarshallContext context) {
        final N value = this.value;

        return format.setValueType(this.setPath(format.setPathNameType(object, this.path, context)),
                value,
                context)
                .set(VALUE_PROPERTY, context.marshall(value));
    }
}
