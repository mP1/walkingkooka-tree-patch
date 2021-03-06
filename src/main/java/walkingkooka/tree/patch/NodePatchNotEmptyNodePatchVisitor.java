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
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.JsonString;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.visit.Visitor;

import java.util.function.BiFunction;

/**
 * A visitor used by all {@link NodePatch} sub classes to build a {@link NodePatch} from a {@link JsonNode}.
 */
abstract class NodePatchNotEmptyNodePatchVisitor extends Visitor<JsonNode> {

    NodePatchNotEmptyNodePatchVisitor(final JsonObject patch,
                                      final NodePatchFromJsonFormat format,
                                      final JsonNodeUnmarshallContext context) {
        super();
        this.patch = patch;
        this.format = format;
        this.context = context;
    }

    @Override
    public final void accept(final JsonNode node) {
        this.format.accept(this, node.objectOrFail());
    }

    final void acceptJsonPatch(final JsonObject node) {
        for (JsonNode property : node.children()) {
            final JsonPropertyName propertyName = property.name();

            switch (propertyName.value()) {
                case NodePatch.OP:
                    break;
                case NodePatch.FROM:
                    this.visitFrom(property.stringOrFail());
                    break;
                case NodePatch.PATH:
                    this.visitPath(property.stringOrFail());
                    break;
                case NodePatch.VALUE:
                    this.visitValue(property);
                    break;
                default:
                    JsonNodeUnmarshallContext.unknownPropertyPresent(propertyName, property.removeParent());
            }
        }
    }

    final void acceptNodePatch(final JsonObject node) {
        for (JsonNode property : node.children()) {
            final JsonPropertyName propertyName = property.name();

            switch (propertyName.value()) {
                case NodePatch.OP:
                    break;
                case NodePatch.PATH_NAME_TYPE:
                    this.visitPathNameType(property);
                    break;
                case NodePatch.FROM:
                    this.visitFrom(property.stringOrFail());
                    break;
                case NodePatch.PATH:
                    this.visitPath(property.stringOrFail());
                    break;
                case NodePatch.VALUE_TYPE:
                    this.visitValueType(property);
                    break;
                case NodePatch.VALUE:
                    this.visitValue(property);
                    break;
                default:
                    JsonNodeUnmarshallContext.unknownPropertyPresent(propertyName, property.removeParent());
            }
        }
    }

    // PATH NAME TYPE .............................................................................................

    private void visitPathNameType(final JsonNode pathNameType) {
        pathNameType.stringOrFail();
    }

    /**
     * Getter that returns a factory that assumes {@link NodePatch#PATH_NAME_TYPE_PROPERTY} holds the type name.
     */
    final BiFunction<JsonNode, JsonNodeUnmarshallContext, Name> pathNameFactory(final JsonNodeUnmarshallContext context) {
        if (null == this.pathNameFactory) {
            this.pathNameFactory = context.unmarshallWithType(NodePatch.PATH_NAME_TYPE_PROPERTY, this.patch, Name.class);
        }
        return this.pathNameFactory;
    }

    private BiFunction<JsonNode, JsonNodeUnmarshallContext, Name> pathNameFactory;

    // FROM ........................................................................................................

    abstract void visitFrom(final String from);

    // PATH ........................................................................................................

    private void visitPath(final String path) {
        this.path = path;
    }

    final NodePointer<?, ?> path() {
        return this.pathOrFail(this.path, NodePatch.PATH_PROPERTY);
    }

    /**
     * Once all properties are visited this will be converted into a {@link NodePointer}
     */
    private String path;

    /**
     * Creates a {@link NodePointer} from the {@link JsonString} using the property name in any error messages.
     */
    @SuppressWarnings("unchecked")
    final NodePointer<?, ?> pathOrFail(final String path,
                                       final JsonPropertyName property) {
        try {
            return NodePointer.parse(path,
                    this.format.nameFactory(this, this.context),
                    Node.class);
        } catch (final RuntimeException cause) {
            throw new IllegalArgumentException("Invalid " + property + " in " + this.patch, cause);
        }
    }

    final JsonNodeUnmarshallContext context;

    // VALUE ........................................................................................................

    abstract void visitValueType(final JsonNode valueType);

    abstract void visitValue(final JsonNode value);

    // HELPER ...........................................................................................................

    /**
     * Helper that fails if the value is null using the property name in the message detail.
     */
    final <T> T valueOrFail(final T value) {
        if (null == value) {
            throw new IllegalArgumentException("Required property " + NodePatch.VALUE_PROPERTY + " missing " + this.patch);
        }
        return value;
    }

    /**
     * Reports that an unknown property is present.
     */
    final void unknownPropertyPresent(final JsonPropertyName property) {
        JsonNodeUnmarshallContext.unknownPropertyPresent(property, this.patch);
    }

    /**
     * The json object representing the patch.
     */
    final JsonObject patch;

    final NodePatchFromJsonFormat format;

    @Override
    public final String toString() {
        return this.patch.toString();
    }
}
