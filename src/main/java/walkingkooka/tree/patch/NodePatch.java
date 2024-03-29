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
import walkingkooka.NeverError;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonArray;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObject;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContexts;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallException;
import walkingkooka.tree.pointer.NodePointer;
import walkingkooka.tree.pointer.NodePointerException;

import java.math.MathContext;
import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link NodePatch} supports operations for a {@link Node} that match the functionality of json-patch with json.<br>
 * <A href="http://jsonpatch.com">jsonpatch.com</A>
 */
public abstract class NodePatch<N extends Node<N, NAME, ?, ?>, NAME extends Name> implements Function<N, N> {

    /**
     * Returns an empty patch.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatchEmpty<N, NAME> empty(final Class<N> type) {
        return NodePatchEmpty.get(type);
    }

    /**
     * Package private to limit sub classing.
     */
    NodePatch() {
        super();
    }

    // public factory methods supporting the builder pattern to build up a patch.........................

    /**
     * Adds an ADD operation to this patch.
     */
    public NodePatch<N, NAME> add(final NodePointer<N, NAME> path,
                                  final N value) {
        return this.append(NodePatchNotEmptyAddReplaceOrTestAdd.with(path, value));
    }

    /**
     * Adds a COPY operation to this patch.
     */
    public NodePatch<N, NAME> copy(final NodePointer<N, NAME> from,
                                   final NodePointer<N, NAME> path) {
        return this.append(NodePatchNotEmptyCopyOrMoveCopy.with(from, path));
    }

    /**
     * Adds a MOVE operation to this patch.
     */
    public NodePatch<N, NAME> move(final NodePointer<N, NAME> from,
                                   final NodePointer<N, NAME> path) {
        return this.append(NodePatchNotEmptyCopyOrMoveMove.with(from, path));
    }

    /**
     * Adds an REMOVE operation to this patch.
     */
    public NodePatch<N, NAME> remove(final NodePointer<N, NAME> path) {
        return this.append(NodePatchNotEmptyRemove.with(path));
    }

    /**
     * Adds an REPLACE operation to this patch.
     */
    public NodePatch<N, NAME> replace(final NodePointer<N, NAME> path,
                                      final N value) {
        return this.append(NodePatchNotEmptyAddReplaceOrTestReplace.with(path, value));
    }

    /**
     * Adds a TEST operation to this patch.
     */
    public NodePatch<N, NAME> test(final NodePointer<N, NAME> path,
                                   final N value) {
        return this.append(NodePatchNotEmptyAddReplaceOrTestTest.with(path, value));
    }

    /**
     * Used to append a new patch operation to a previous creating a collection of patch ops.
     */
    final NodePatchNonEmpty<N, NAME> append(final NodePatchNonEmpty<N, NAME> patch) {
        final NodePatchNonEmpty<N, NAME> next = this.nextOrNull();
        return this.append0(null != next ? next.append(patch) : patch);
    }

    /**
     * Used to concatenate multiple patch components.
     */
    abstract NodePatchNonEmpty<N, NAME> append0(final NodePatchNonEmpty<N, NAME> patch);

    // Function................................................................................................

    /**
     * Executes this patch accepting the {@link Node} as the base of all pointer paths and operations.
     */
    @Override
    public final N apply(final N node) {
        Objects.requireNonNull(node, "node");

        final NodePointer<N, NAME> start = node.pointer();
        NodePatch<N, NAME> currentPatch = this;
        N currentNode = node;

        do {
            currentNode = currentPatch.apply0(currentNode, start);
            currentPatch = currentPatch.nextOrNull();
        } while (null != currentPatch);

        return currentNode;
    }

    /**
     * Performs the actual operation.
     */
    abstract N apply0(final N node, final NodePointer<N, NAME> start);

    /**
     * Helper which locates the original start {@link Node} or fails.
     */
    final N traverseStartOrFail(final N node, final NodePointer<N, NAME> start) {
        return start.traverse(node.root())
                .orElseThrow(() -> new NodePointerException("Unable to navigate to starting node: " + node));
    }

    /**
     * Returns the next component in the patch or null if the end has been reached.
     */
    abstract NodePatchNonEmpty<N, NAME> nextOrNull();

    // Object................................................................................................

    @Override
    public abstract String toString();

    /**
     * Sub classes must add their individual toString representation.
     */
    abstract void toString0(final StringBuilder b);

    // HasJsonNode...............................................................................

    /**
     * Similar to {@link #unmarshall(JsonNode, JsonNodeUnmarshallContext)} but names and values are created using the two factories.
     */
    public static <N extends Node<N, NAME, ?, ?>, NAME extends Name> NodePatch<N, NAME> fromJsonPatch(final JsonNode node,
                                                                                                      final Function<String, NAME> nameFactory,
                                                                                                      final Function<JsonNode, N> valueFactory,
                                                                                                      final ExpressionNumberKind kind,
                                                                                                      final MathContext context) {
        checkNode(node);
        Objects.requireNonNull(nameFactory, "nameFactory");
        Objects.requireNonNull(valueFactory, "valueFactory");

        return Cast.to(
                unmarshall0(
                        node,
                        NodePatchFromJsonFormat.jsonPatch(nameFactory, valueFactory),
                        JsonNodeUnmarshallContexts.basic(
                                kind,
                                context
                        )
                )
        );
    }

    /**
     * Creates a json-patch json which is identical to the {@link #marshall(JsonNodeMarshallContext)} but without the type properties.
     */
    public final JsonArray toJsonPatch() {
        return this.marshall0(NodePatchToJsonFormat.JSON_PATCH, JsonNodeMarshallContexts.basic());
    }

    // HasJsonNode...............................................................................

    /**
     * Accepts a json string holding a patch collection.
     * <pre>
     * {
     *      "type"="node-patch",
     *      "node-pointer-name-type"=“String”,
     *      "node-patch-value-type"=“String”,
     * 	    "value"=[
     *            {
     * 	            "op": "remove",
     * 	            "patch": "/1"
     *            },
     *            {
     * 	            "op": "remove",
     * 	            "patch": "/2"
     *            }
     * 	    ]
     * }
     * </pre>
     */
    static NodePatch<?, ?> unmarshall(final JsonNode node,
                                      final JsonNodeUnmarshallContext context) {
        checkNode(node);

        return unmarshall0(node,
                NodePatchFromJsonFormat.unmarshallContext(),
                context);
    }

    private static void checkNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");
    }

    private static NodePatch<?, ?> unmarshall0(final JsonNode node,
                                               final NodePatchFromJsonFormat format,
                                               final JsonNodeUnmarshallContext context) {
        try {
            return unmarshall1(node.arrayOrFail(), format, context);
        } catch (final JsonNodeUnmarshallException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new JsonNodeUnmarshallException(cause.getMessage(), node, cause);
        }
    }

    private static NodePatch<?, ?> unmarshall1(final JsonArray array,
                                               final NodePatchFromJsonFormat format,
                                               final JsonNodeUnmarshallContext context) {
        NodePatch<?, ?> patch = NodePatchEmpty.getWildcard();

        for (JsonNode child : array.children()) {
            patch = patch.append(Cast.to(unmarshall2(child.objectOrFail(), format, context)));
        }

        return patch;
    }

    final static String ADD = "add";
    final static String COPY = "copy";
    final static String MOVE = "move";
    final static String REMOVE = "remove";
    final static String REPLACE = "replace";
    final static String TEST = "test";

    /**
     * Factory that switches on the op and then creates a {@link NodePatch}.
     */
    private static NodePatchNonEmpty<?, ?> unmarshall2(final JsonObject node,
                                                       final NodePatchFromJsonFormat format,
                                                       final JsonNodeUnmarshallContext context) {
        NodePatchNonEmpty<?, ?> patch = null;

        final String op = node.getOrFail(OP_PROPERTY).stringOrFail();
        switch (op) {
            case ADD:
                patch = NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor.add(node, format, context);
                break;
            case COPY:
                patch = NodePatchNotEmptyCopyOrMoveNodePatchVisitor.copy(node, format, context);
                break;
            case MOVE:
                patch = NodePatchNotEmptyCopyOrMoveNodePatchVisitor.move(node, format, context);
                break;
            case REMOVE:
                patch = NodePatchNotEmptyRemoveNodePatchVisitor.remove(node, format, context);
                break;
            case REPLACE:
                patch = NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor.replace(node, format, context);
                break;
            case TEST:
                patch = NodePatchNotEmptyAddReplaceOrTestNodePatchVisitor.test(node, format, context);
                break;
            default:
                NeverError.unhandledCase(op, ADD, COPY, MOVE, REMOVE, REPLACE, TEST);
        }

        return patch;
    }

    final static String OP = "op";
    final static String PATH_NAME_TYPE = "path-name-type";
    final static String FROM = "from";
    final static String PATH = "path";
    final static String VALUE_TYPE = "value-type";
    final static String VALUE = "value";

    final static JsonPropertyName OP_PROPERTY = JsonPropertyName.with(OP);

    final static JsonPropertyName PATH_NAME_TYPE_PROPERTY = JsonPropertyName.with(PATH_NAME_TYPE);

    final static JsonPropertyName FROM_PROPERTY = JsonPropertyName.with(FROM);
    final static JsonPropertyName PATH_PROPERTY = JsonPropertyName.with(PATH);

    final static JsonPropertyName VALUE_TYPE_PROPERTY = JsonPropertyName.with(VALUE_TYPE);
    final static JsonPropertyName VALUE_PROPERTY = JsonPropertyName.with(VALUE);

    static {
        JsonNodeContext.register("patch",
                NodePatch::unmarshall,
                NodePatch::marshall,
                NodePatch.class,
                NodePatchNotEmptyAddReplaceOrTestAdd.class,
                NodePatchNotEmptyCopyOrMoveCopy.class,
                NodePatchEmpty.class,
                NodePatchNotEmptyCopyOrMoveMove.class,
                NodePatchNotEmptyRemove.class,
                NodePatchNotEmptyAddReplaceOrTestReplace.class,
                NodePatchNotEmptyAddReplaceOrTestTest.class);
    }

    private JsonArray marshall(final JsonNodeMarshallContext context) {
        return this.marshall0(NodePatchToJsonFormat.JSON_NODE_CONTEXT,
                context);
    }

    abstract JsonArray marshall0(final NodePatchToJsonFormat format, final JsonNodeMarshallContext context);
}
