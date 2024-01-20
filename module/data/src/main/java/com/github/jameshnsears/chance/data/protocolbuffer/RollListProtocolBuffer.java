// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: roll_history.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

/**
 * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer}
 */
public final class RollListProtocolBuffer extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer)
        RollListProtocolBufferOrBuilder {
    public static final int ROLL_FIELD_NUMBER = 1;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer)
    private static final com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<RollListProtocolBuffer>
            PARSER = new com.google.protobuf.AbstractParser<RollListProtocolBuffer>() {
        @java.lang.Override
        public RollListProtocolBuffer parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            Builder builder = newBuilder();
            try {
                builder.mergeFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (com.google.protobuf.UninitializedMessageException e) {
                throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e)
                        .setUnfinishedMessage(builder.buildPartial());
            }
            return builder.buildPartial();
        }
    };

    static {
        DEFAULT_INSTANCE = new com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer();
    }

    @SuppressWarnings("serial")
    private java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer> roll_;
    private byte memoizedIsInitialized = -1;

    // Use RollListProtocolBuffer.newBuilder() to construct.
    private RollListProtocolBuffer(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private RollListProtocolBuffer() {
        roll_ = java.util.Collections.emptyList();
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.github.jameshnsears.chance.data.protocolbuffer.RollHistory.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollListProtocolBuffer_descriptor;
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<RollListProtocolBuffer> parser() {
        return PARSER;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
            UnusedPrivateParameter unused) {
        return new RollListProtocolBuffer();
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.github.jameshnsears.chance.data.protocolbuffer.RollHistory.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollListProtocolBuffer_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.Builder.class);
    }

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
     */
    @java.lang.Override
    public java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer> getRollList() {
        return roll_;
    }

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
     */
    @java.lang.Override
    public java.util.List<? extends com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder>
    getRollOrBuilderList() {
        return roll_;
    }

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
     */
    @java.lang.Override
    public int getRollCount() {
        return roll_.size();
    }

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer getRoll(int index) {
        return roll_.get(index);
    }

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder getRollOrBuilder(
            int index) {
        return roll_.get(index);
    }

    @java.lang.Override
    public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) return true;
        if (isInitialized == 0) return false;

        memoizedIsInitialized = 1;
        return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
        for (int i = 0; i < roll_.size(); i++) {
            output.writeMessage(1, roll_.get(i));
        }
        getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        for (int i = 0; i < roll_.size(); i++) {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(1, roll_.get(i));
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSize = size;
        return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer)) {
            return super.equals(obj);
        }
        com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer other = (com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer) obj;

        if (!getRollList()
                .equals(other.getRollList())) return false;
        if (!getUnknownFields().equals(other.getUnknownFields())) return false;
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        if (getRollCount() > 0) {
            hash = (37 * hash) + ROLL_FIELD_NUMBER;
            hash = (53 * hash) + getRollList().hashCode();
        }
        hash = (29 * hash) + getUnknownFields().hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    @java.lang.Override
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @java.lang.Override
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
                ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<RollListProtocolBuffer> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer)
            com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBufferOrBuilder {
        private int bitField0_;
        private java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer> roll_ =
                java.util.Collections.emptyList();
        private com.google.protobuf.RepeatedFieldBuilderV3<
                com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder> rollBuilder_;

        // Construct using com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.newBuilder()
        private Builder() {

        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);

        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.github.jameshnsears.chance.data.protocolbuffer.RollHistory.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollListProtocolBuffer_descriptor;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.github.jameshnsears.chance.data.protocolbuffer.RollHistory.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollListProtocolBuffer_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.Builder.class);
        }

        @java.lang.Override
        public Builder clear() {
            super.clear();
            bitField0_ = 0;
            if (rollBuilder_ == null) {
                roll_ = java.util.Collections.emptyList();
            } else {
                roll_ = null;
                rollBuilder_.clear();
            }
            bitField0_ = (bitField0_ & ~0x00000001);
            return this;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.RollHistory.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollListProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer getDefaultInstanceForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.getDefaultInstance();
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer build() {
            com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer buildPartial() {
            com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer result = new com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer(this);
            buildPartialRepeatedFields(result);
            if (bitField0_ != 0) {
                buildPartial0(result);
            }
            onBuilt();
            return result;
        }

        private void buildPartialRepeatedFields(com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer result) {
            if (rollBuilder_ == null) {
                if (((bitField0_ & 0x00000001) != 0)) {
                    roll_ = java.util.Collections.unmodifiableList(roll_);
                    bitField0_ = (bitField0_ & ~0x00000001);
                }
                result.roll_ = roll_;
            } else {
                result.roll_ = rollBuilder_.build();
            }
        }

        private void buildPartial0(com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer result) {
            int from_bitField0_ = bitField0_;
        }

        @java.lang.Override
        public Builder clone() {
            return super.clone();
        }

        @java.lang.Override
        public Builder setField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                java.lang.Object value) {
            return super.setField(field, value);
        }

        @java.lang.Override
        public Builder clearField(
                com.google.protobuf.Descriptors.FieldDescriptor field) {
            return super.clearField(field);
        }

        @java.lang.Override
        public Builder clearOneof(
                com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return super.clearOneof(oneof);
        }

        @java.lang.Override
        public Builder setRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                int index, java.lang.Object value) {
            return super.setRepeatedField(field, index, value);
        }

        @java.lang.Override
        public Builder addRepeatedField(
                com.google.protobuf.Descriptors.FieldDescriptor field,
                java.lang.Object value) {
            return super.addRepeatedField(field, value);
        }

        @java.lang.Override
        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer) {
                return mergeFrom((com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer other) {
            if (other == com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer.getDefaultInstance())
                return this;
            if (rollBuilder_ == null) {
                if (!other.roll_.isEmpty()) {
                    if (roll_.isEmpty()) {
                        roll_ = other.roll_;
                        bitField0_ = (bitField0_ & ~0x00000001);
                    } else {
                        ensureRollIsMutable();
                        roll_.addAll(other.roll_);
                    }
                    onChanged();
                }
            } else {
                if (!other.roll_.isEmpty()) {
                    if (rollBuilder_.isEmpty()) {
                        rollBuilder_.dispose();
                        rollBuilder_ = null;
                        roll_ = other.roll_;
                        bitField0_ = (bitField0_ & ~0x00000001);
                        rollBuilder_ =
                                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                                        getRollFieldBuilder() : null;
                    } else {
                        rollBuilder_.addAllMessages(other.roll_);
                    }
                }
            }
            this.mergeUnknownFields(other.getUnknownFields());
            onChanged();
            return this;
        }

        @java.lang.Override
        public final boolean isInitialized() {
            return true;
        }

        @java.lang.Override
        public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws java.io.IOException {
            if (extensionRegistry == null) {
                throw new java.lang.NullPointerException();
            }
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        case 10: {
                            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer m =
                                    input.readMessage(
                                            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.parser(),
                                            extensionRegistry);
                            if (rollBuilder_ == null) {
                                ensureRollIsMutable();
                                roll_.add(m);
                            } else {
                                rollBuilder_.addMessage(m);
                            }
                            break;
                        } // case 10
                        default: {
                            if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                                done = true; // was an endgroup tag
                            }
                            break;
                        } // default:
                    } // switch (tag)
                } // while (!done)
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.unwrapIOException();
            } finally {
                onChanged();
            } // finally
            return this;
        }

        private void ensureRollIsMutable() {
            if (!((bitField0_ & 0x00000001) != 0)) {
                roll_ = new java.util.ArrayList<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer>(roll_);
                bitField0_ |= 0x00000001;
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer> getRollList() {
            if (rollBuilder_ == null) {
                return java.util.Collections.unmodifiableList(roll_);
            } else {
                return rollBuilder_.getMessageList();
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public int getRollCount() {
            if (rollBuilder_ == null) {
                return roll_.size();
            } else {
                return rollBuilder_.getCount();
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer getRoll(int index) {
            if (rollBuilder_ == null) {
                return roll_.get(index);
            } else {
                return rollBuilder_.getMessage(index);
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder setRoll(
                int index, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer value) {
            if (rollBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRollIsMutable();
                roll_.set(index, value);
                onChanged();
            } else {
                rollBuilder_.setMessage(index, value);
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder setRoll(
                int index, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder builderForValue) {
            if (rollBuilder_ == null) {
                ensureRollIsMutable();
                roll_.set(index, builderForValue.build());
                onChanged();
            } else {
                rollBuilder_.setMessage(index, builderForValue.build());
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder addRoll(com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer value) {
            if (rollBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRollIsMutable();
                roll_.add(value);
                onChanged();
            } else {
                rollBuilder_.addMessage(value);
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder addRoll(
                int index, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer value) {
            if (rollBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureRollIsMutable();
                roll_.add(index, value);
                onChanged();
            } else {
                rollBuilder_.addMessage(index, value);
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder addRoll(
                com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder builderForValue) {
            if (rollBuilder_ == null) {
                ensureRollIsMutable();
                roll_.add(builderForValue.build());
                onChanged();
            } else {
                rollBuilder_.addMessage(builderForValue.build());
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder addRoll(
                int index, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder builderForValue) {
            if (rollBuilder_ == null) {
                ensureRollIsMutable();
                roll_.add(index, builderForValue.build());
                onChanged();
            } else {
                rollBuilder_.addMessage(index, builderForValue.build());
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder addAllRoll(
                java.lang.Iterable<? extends com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer> values) {
            if (rollBuilder_ == null) {
                ensureRollIsMutable();
                com.google.protobuf.AbstractMessageLite.Builder.addAll(
                        values, roll_);
                onChanged();
            } else {
                rollBuilder_.addAllMessages(values);
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder clearRoll() {
            if (rollBuilder_ == null) {
                roll_ = java.util.Collections.emptyList();
                bitField0_ = (bitField0_ & ~0x00000001);
                onChanged();
            } else {
                rollBuilder_.clear();
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public Builder removeRoll(int index) {
            if (rollBuilder_ == null) {
                ensureRollIsMutable();
                roll_.remove(index);
                onChanged();
            } else {
                rollBuilder_.remove(index);
            }
            return this;
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder getRollBuilder(
                int index) {
            return getRollFieldBuilder().getBuilder(index);
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder getRollOrBuilder(
                int index) {
            if (rollBuilder_ == null) {
                return roll_.get(index);
            } else {
                return rollBuilder_.getMessageOrBuilder(index);
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public java.util.List<? extends com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder>
        getRollOrBuilderList() {
            if (rollBuilder_ != null) {
                return rollBuilder_.getMessageOrBuilderList();
            } else {
                return java.util.Collections.unmodifiableList(roll_);
            }
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder addRollBuilder() {
            return getRollFieldBuilder().addBuilder(
                    com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.getDefaultInstance());
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder addRollBuilder(
                int index) {
            return getRollFieldBuilder().addBuilder(
                    index, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.getDefaultInstance());
        }

        /**
         * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer roll = 1;</code>
         */
        public java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder>
        getRollBuilderList() {
            return getRollFieldBuilder().getBuilderList();
        }

        private com.google.protobuf.RepeatedFieldBuilderV3<
                com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder>
        getRollFieldBuilder() {
            if (rollBuilder_ == null) {
                rollBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
                        com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder>(
                        roll_,
                        ((bitField0_ & 0x00000001) != 0),
                        getParentForChildren(),
                        isClean());
                roll_ = null;
            }
            return rollBuilder_;
        }

        @java.lang.Override
        public final Builder setUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.setUnknownFields(unknownFields);
        }

        @java.lang.Override
        public final Builder mergeUnknownFields(
                final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.mergeUnknownFields(unknownFields);
        }


        // @@protoc_insertion_point(builder_scope:com.github.jameshnsears.chance.data.protocolbuffer.RollListProtocolBuffer)
    }

}

