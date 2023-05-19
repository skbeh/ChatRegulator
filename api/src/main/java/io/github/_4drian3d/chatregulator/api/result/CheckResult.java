package io.github._4drian3d.chatregulator.api.result;

import io.github._4drian3d.chatregulator.api.enums.InfractionType;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;

public sealed interface CheckResult {
    static @NotNull CheckResult denied(final @NotNull InfractionType type) {
        return new DeniedCheckresult(type);
    }

    static @NotNull CheckResult allowed() {
        return AllowedCheckResult.INSTANCE;
    }

    static @NotNull CheckResult modified(final @NotNull String modifier) {
        return new ReplaceCheckResult(modifier);
    }

    boolean isAllowed();

    boolean isDenied();

    boolean shouldModify();

    final class AllowedCheckResult implements CheckResult {
        private static final AllowedCheckResult INSTANCE = new AllowedCheckResult();

        @Override
        public boolean isAllowed() {
            return true;
        }

        @Override
        public boolean isDenied() {
            return false;
        }

        @Override
        public boolean shouldModify() {
            return false;
        }
    }

    final class DeniedCheckresult implements CheckResult {
        private final InfractionType infractionType;

        public DeniedCheckresult(InfractionType type) {
            this.infractionType = type;
        }

        @Override
        public boolean isAllowed() {
            return false;
        }

        @Override
        public boolean isDenied() {
            return true;
        }

        @Override
        public boolean shouldModify() {
            return false;
        }

        public InfractionType infractionType() {
            return this.infractionType;
        }
    }

    final class ReplaceCheckResult implements CheckResult {
        private final String modified;
        private ReplaceCheckResult(final String modified) {
            this.modified = requireNonNull(modified);
        }

        @Override
        public boolean isAllowed() {
            return false;
        }

        @Override
        public boolean isDenied() {
            return false;
        }

        @Override
        public boolean shouldModify() {
            return true;
        }

        public String replaced() {
            return modified;
        }
    }
}
