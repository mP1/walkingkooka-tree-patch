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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.StandardThrowableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ApplyNodePatchExceptionTest implements StandardThrowableTesting<ApplyNodePatchException> {

    private final static String MESSAGE = "message123";

    @Test
    public void testWithNullPatchFails() {
        assertThrows(NullPointerException.class, () -> new ApplyNodePatchException("message", null));
    }

    @Override
    public void testWithMessageAndCause() {
    }

    @Override
    public void testWithMessageAndNullCauseFails() {
    }

    @Override
    public void testWithNullMessageAndCauseExceptionFails() {
    }

    @Test
    public void testWithPatch() {
        final NodePatch<JsonNode, JsonNodeName> patch = this.empty();
        final ApplyNodePatchException exception = new ApplyNodePatchException(MESSAGE, patch);
        this.checkMessage(exception, MESSAGE);
        this.checkPatch(exception, patch);
        this.checkCause(exception, null);
    }

    @Test
    public void testWithMessageAndNullPathAndCause() {
        assertThrows(NullPointerException.class, () -> new ApplyNodePatchException(MESSAGE, null, new Exception()));
    }

    @Test
    public void testWithPatchAndCause() {
        final NodePatch<JsonNode, JsonNodeName> patch = this.empty();
        final Exception cause = new Exception("cause!");

        final ApplyNodePatchException exception = new ApplyNodePatchException(MESSAGE, patch, cause);
        this.checkMessage(exception, MESSAGE);
        this.checkPatch(exception, patch);
        this.checkCause(exception, cause);
    }

    private NodePatch<JsonNode, JsonNodeName> empty() {
        return NodePatch.empty(JsonNode.class);
    }

    private void checkPatch(final ApplyNodePatchException exception,
                            final NodePatch<JsonNode, JsonNodeName> patch) {
        assertSame(patch, exception.patch(), "patch");
    }

    @Override
    public ApplyNodePatchException createThrowable(final String message) {
        return new ApplyNodePatchException(message, this.empty());
    }

    @Override
    public ApplyNodePatchException createThrowable(final String message, final Throwable cause) {
        return new ApplyNodePatchException(message, this.empty(), cause);
    }

    @Override
    public Class<ApplyNodePatchException> type() {
        return ApplyNodePatchException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
