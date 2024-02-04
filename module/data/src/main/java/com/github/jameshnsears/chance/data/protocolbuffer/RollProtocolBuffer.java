// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: roll.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

/**
 * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer}
 */
public final class RollProtocolBuffer extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)
        RollProtocolBufferOrBuilder {
    public static final int DICEEPOCH_FIELD_NUMBER = 1;
    public static final int SIDE_FIELD_NUMBER = 2;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)
    private static final com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<RollProtocolBuffer>
            PARSER = new com.google.protobuf.AbstractParser<RollProtocolBuffer>() {
        @java.lang.Override
        public RollProtocolBuffer parsePartialFrom(
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
        DEFAULT_INSTANCE = new com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer();
    }

    private int bitField0_;
    private long diceEpoch_ = 0L;
    private com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side_;
    private byte memoizedIsInitialized = -1;

    // Use RollProtocolBuffer.newBuilder() to construct.
    private RollProtocolBuffer(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private RollProtocolBuffer() {
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.github.jameshnsears.chance.data.protocolbuffer.Roll.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollProtocolBuffer_descriptor;
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<RollProtocolBuffer> parser() {
        return PARSER;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
            UnusedPrivateParameter unused) {
        return new RollProtocolBuffer();
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.github.jameshnsears.chance.data.protocolbuffer.Roll.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollProtocolBuffer_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder.class);
    }

    /**
     * <code>int64 diceEpoch = 1;</code>
     *
     * @return The diceEpoch.
     */
    @java.lang.Override
    public long getDiceEpoch() {
        return diceEpoch_;
    }

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     *
     * @return Whether the side field is set.
     */
    @java.lang.Override
    public boolean hasSide() {
        return ((bitField0_ & 0x00000001) != 0);
    }

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     *
     * @return The side.
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer getSide() {
        return side_ == null ? com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.getDefaultInstance() : side_;
    }

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder getSideOrBuilder() {
        return side_ == null ? com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.getDefaultInstance() : side_;
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
        if (diceEpoch_ != 0L) {
            output.writeInt64(1, diceEpoch_);
        }
        if (((bitField0_ & 0x00000001) != 0)) {
            output.writeMessage(2, getSide());
        }
        getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (diceEpoch_ != 0L) {
            size += com.google.protobuf.CodedOutputStream
                    .computeInt64Size(1, diceEpoch_);
        }
        if (((bitField0_ & 0x00000001) != 0)) {
            size += com.google.protobuf.CodedOutputStream
                    .computeMessageSize(2, getSide());
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
        if (!(obj instanceof com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)) {
            return super.equals(obj);
        }
        com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer other = (com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer) obj;

        if (getDiceEpoch()
                != other.getDiceEpoch()) return false;
        if (hasSide() != other.hasSide()) return false;
        if (hasSide()) {
            if (!getSide()
                    .equals(other.getSide())) return false;
        }
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
        hash = (37 * hash) + DICEEPOCH_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
                getDiceEpoch());
        if (hasSide()) {
            hash = (37 * hash) + SIDE_FIELD_NUMBER;
            hash = (53 * hash) + getSide().hashCode();
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
    public com.google.protobuf.Parser<RollProtocolBuffer> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)
            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBufferOrBuilder {
        private int bitField0_;
        private long diceEpoch_;
        private com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side_;
        private com.google.protobuf.SingleFieldBuilderV3<
                com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder> sideBuilder_;

        // Construct using com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Roll.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollProtocolBuffer_descriptor;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Roll.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollProtocolBuffer_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessageV3
                    .alwaysUseFieldBuilders) {
                getSideFieldBuilder();
            }
        }

        @java.lang.Override
        public Builder clear() {
            super.clear();
            bitField0_ = 0;
            diceEpoch_ = 0L;
            side_ = null;
            if (sideBuilder_ != null) {
                sideBuilder_.dispose();
                sideBuilder_ = null;
            }
            return this;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Roll.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_RollProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer getDefaultInstanceForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.getDefaultInstance();
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer build() {
            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer buildPartial() {
            com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer result = new com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer(this);
            if (bitField0_ != 0) {
                buildPartial0(result);
            }
            onBuilt();
            return result;
        }

        private void buildPartial0(com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer result) {
            int from_bitField0_ = bitField0_;
            if (((from_bitField0_ & 0x00000001) != 0)) {
                result.diceEpoch_ = diceEpoch_;
            }
            int to_bitField0_ = 0;
            if (((from_bitField0_ & 0x00000002) != 0)) {
                result.side_ = sideBuilder_ == null
                        ? side_
                        : sideBuilder_.build();
                to_bitField0_ |= 0x00000001;
            }
            result.bitField0_ |= to_bitField0_;
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
            if (other instanceof com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer) {
                return mergeFrom((com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer other) {
            if (other == com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer.getDefaultInstance())
                return this;
            if (other.getDiceEpoch() != 0L) {
                setDiceEpoch(other.getDiceEpoch());
            }
            if (other.hasSide()) {
                mergeSide(other.getSide());
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
                        case 8: {
                            diceEpoch_ = input.readInt64();
                            bitField0_ |= 0x00000001;
                            break;
                        } // case 8
                        case 18: {
                            input.readMessage(
                                    getSideFieldBuilder().getBuilder(),
                                    extensionRegistry);
                            bitField0_ |= 0x00000002;
                            break;
                        } // case 18
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

        /**
         * <code>int64 diceEpoch = 1;</code>
         *
         * @return The diceEpoch.
         */
        @java.lang.Override
        public long getDiceEpoch() {
            return diceEpoch_;
        }

        /**
         * <code>int64 diceEpoch = 1;</code>
         *
         * @param value The diceEpoch to set.
         * @return This builder for chaining.
         */
        public Builder setDiceEpoch(long value) {

            diceEpoch_ = value;
            bitField0_ |= 0x00000001;
            onChanged();
            return this;
        }

        /**
         * <code>int64 diceEpoch = 1;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearDiceEpoch() {
            bitField0_ = (bitField0_ & ~0x00000001);
            diceEpoch_ = 0L;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         *
         * @return Whether the side field is set.
         */
        public boolean hasSide() {
            return ((bitField0_ & 0x00000002) != 0);
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         *
         * @return The side.
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer getSide() {
            if (sideBuilder_ == null) {
                return side_ == null ? com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.getDefaultInstance() : side_;
            } else {
                return sideBuilder_.getMessage();
            }
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public Builder setSide(com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer value) {
            if (sideBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                side_ = value;
            } else {
                sideBuilder_.setMessage(value);
            }
            bitField0_ |= 0x00000002;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public Builder setSide(
                com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.Builder builderForValue) {
            if (sideBuilder_ == null) {
                side_ = builderForValue.build();
            } else {
                sideBuilder_.setMessage(builderForValue.build());
            }
            bitField0_ |= 0x00000002;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public Builder mergeSide(com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer value) {
            if (sideBuilder_ == null) {
                if (((bitField0_ & 0x00000002) != 0) &&
                        side_ != null &&
                        side_ != com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.getDefaultInstance()) {
                    getSideBuilder().mergeFrom(value);
                } else {
                    side_ = value;
                }
            } else {
                sideBuilder_.mergeFrom(value);
            }
            if (side_ != null) {
                bitField0_ |= 0x00000002;
                onChanged();
            }
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public Builder clearSide() {
            bitField0_ = (bitField0_ & ~0x00000002);
            side_ = null;
            if (sideBuilder_ != null) {
                sideBuilder_.dispose();
                sideBuilder_ = null;
            }
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.Builder getSideBuilder() {
            bitField0_ |= 0x00000002;
            onChanged();
            return getSideFieldBuilder().getBuilder();
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        public com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder getSideOrBuilder() {
            if (sideBuilder_ != null) {
                return sideBuilder_.getMessageOrBuilder();
            } else {
                return side_ == null ?
                        com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.getDefaultInstance() : side_;
            }
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
         */
        private com.google.protobuf.SingleFieldBuilderV3<
                com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder>
        getSideFieldBuilder() {
            if (sideBuilder_ == null) {
                sideBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
                        com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder>(
                        getSide(),
                        getParentForChildren(),
                        isClean());
                side_ = null;
            }
            return sideBuilder_;
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


        // @@protoc_insertion_point(builder_scope:com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)
    }

}

