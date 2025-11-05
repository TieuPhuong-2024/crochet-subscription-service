package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * DTO for updating PayPal subscriptions (PATCH operation).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UpdateSubscriptionRequest {

    private final List<PatchOperation> operations;

    public UpdateSubscriptionRequest(List<PatchOperation> operations) {
        this.operations = operations;
    }

    public List<PatchOperation> getOperations() { return operations; }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private List<PatchOperation> operations;

        public Builder operations(List<PatchOperation> operations) {
            this.operations = operations;
            return this;
        }

        public UpdateSubscriptionRequest build() {
            return new UpdateSubscriptionRequest(operations);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class PatchOperation {
        private final String op;
        private final String path;
        private final Object value;
        private final String from;

        public PatchOperation(String op, String path, Object value, String from) {
            this.op = op;
            this.path = path;
            this.value = value;
            this.from = from;
        }

        public String getOp() { return op; }
        public String getPath() { return path; }
        public Object getValue() { return value; }
        public String getFrom() { return from; }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private String op;
            private String path;
            private Object value;
            private String from;

            public Builder op(String op) {
                this.op = op;
                return this;
            }

            public Builder path(String path) {
                this.path = path;
                return this;
            }

            public Builder value(Object value) {
                this.value = value;
                return this;
            }

            public Builder from(String from) {
                this.from = from;
                return this;
            }

            public PatchOperation build() {
                return new PatchOperation(op, path, value, from);
            }
        }
    }
}
