// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: roll.proto
// Protobuf Java Version: 4.28.2

package com.github.jameshnsears.chance.data.domain.proto;

/**
 * Protobuf type {@code com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer}
 */
public final class RollProtocolBuffer extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer)
    RollProtocolBufferOrBuilder {
    public static final int DICEEPOCH_FIELD_NUMBER = 1;
    public static final int SIDE_FIELD_NUMBER = 2;
    public static final int MULTIPLIERINDEX_FIELD_NUMBER = 3;
    public static final int EXPLODEINDEX_FIELD_NUMBER = 4;
    public static final int SCOREADJUSTMENT_FIELD_NUMBER = 5;
    public static final int SCORE_FIELD_NUMBER = 6;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer)
    private static final com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer DEFAULT_INSTANCE;
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
        com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
            com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
            /* major= */ 4,
            /* minor= */ 28,
            /* patch= */ 2,
            /* suffix= */ "",
            RollProtocolBuffer.class.getName());
    }

    static {
        DEFAULT_INSTANCE = new com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer();
    }

    private int bitField0_;
    private long diceEpoch_ = 0L;
    private com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side_;
    private int multiplierIndex_ = 0;
    private int explodeIndex_ = 0;
    private int scoreAdjustment_ = 0;
    private int score_ = 0;
    private byte memoizedIsInitialized = -1;

    // Use RollProtocolBuffer.newBuilder() to construct.
    private RollProtocolBuffer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
    }

    private RollProtocolBuffer() {
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.github.jameshnsears.chance.data.domain.proto.Roll.internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor;
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<RollProtocolBuffer> parser() {
        return PARSER;
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
     * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     *
     * @return Whether the side field is set.
     */
    @java.lang.Override
    public boolean hasSide() {
        return ((bitField0_ & 0x00000001) != 0);
    }

    /**
     * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     *
     * @return The side.
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer getSide() {
        return side_ == null ? com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.getDefaultInstance() : side_;
    }

    /**
     * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    @java.lang.Override
    public com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder getSideOrBuilder() {
        return side_ == null ? com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.getDefaultInstance() : side_;
    }

    /**
     * <code>int32 multiplierIndex = 3;</code>
     *
     * @return The multiplierIndex.
     */
    @java.lang.Override
    public int getMultiplierIndex() {
        return multiplierIndex_;
    }

    /**
     * <code>int32 explodeIndex = 4;</code>
     *
     * @return The explodeIndex.
     */
    @java.lang.Override
    public int getExplodeIndex() {
        return explodeIndex_;
    }

    /**
     * <code>int32 scoreAdjustment = 5;</code>
     *
     * @return The scoreAdjustment.
     */
    @java.lang.Override
    public int getScoreAdjustment() {
        return scoreAdjustment_;
    }

    /**
     * <code>int32 score = 6;</code>
     *
     * @return The score.
     */
    @java.lang.Override
    public int getScore() {
        return score_;
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
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.github.jameshnsears.chance.data.domain.proto.Roll.internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.class, com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.Builder.class);
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
        if (multiplierIndex_ != 0) {
            output.writeInt32(3, multiplierIndex_);
        }
        if (explodeIndex_ != 0) {
            output.writeInt32(4, explodeIndex_);
        }
        if (scoreAdjustment_ != 0) {
            output.writeInt32(5, scoreAdjustment_);
        }
        if (score_ != 0) {
            output.writeInt32(6, score_);
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
        if (multiplierIndex_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(3, multiplierIndex_);
        }
        if (explodeIndex_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(4, explodeIndex_);
        }
        if (scoreAdjustment_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(5, scoreAdjustment_);
        }
        if (score_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(6, score_);
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
        if (!(obj instanceof com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer)) {
            return super.equals(obj);
        }
        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer other = (com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer) obj;

        if (getDiceEpoch()
            != other.getDiceEpoch()) return false;
        if (hasSide() != other.hasSide()) return false;
        if (hasSide()) {
            if (!getSide()
                .equals(other.getSide())) return false;
        }
        if (getMultiplierIndex()
            != other.getMultiplierIndex()) return false;
        if (getExplodeIndex()
            != other.getExplodeIndex()) return false;
        if (getScoreAdjustment()
            != other.getScoreAdjustment()) return false;
        if (getScore()
            != other.getScore()) return false;
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
        hash = (37 * hash) + MULTIPLIERINDEX_FIELD_NUMBER;
        hash = (53 * hash) + getMultiplierIndex();
        hash = (37 * hash) + EXPLODEINDEX_FIELD_NUMBER;
        hash = (53 * hash) + getExplodeIndex();
        hash = (37 * hash) + SCOREADJUSTMENT_FIELD_NUMBER;
        hash = (53 * hash) + getScoreAdjustment();
        hash = (37 * hash) + SCORE_FIELD_NUMBER;
        hash = (53 * hash) + getScore();
        hash = (29 * hash) + getUnknownFields().hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    @java.lang.Override
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE
            ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<RollProtocolBuffer> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer)
        com.github.jameshnsears.chance.data.domain.proto.RollProtocolBufferOrBuilder {
        private int bitField0_;
        private long diceEpoch_;
        private com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side_;
        private com.google.protobuf.SingleFieldBuilder<
            com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder> sideBuilder_;
        private int multiplierIndex_;
        private int explodeIndex_;
        private int scoreAdjustment_;
        private int score_;

        // Construct using com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.github.jameshnsears.chance.data.domain.proto.Roll.internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer getDefaultInstanceForType() {
            return com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.getDefaultInstance();
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer build() {
            com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer buildPartial() {
            com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer result = new com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer(this);
            if (bitField0_ != 0) {
                buildPartial0(result);
            }
            onBuilt();
            return result;
        }

        @java.lang.Override
        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer) {
                return mergeFrom((com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer other) {
            if (other == com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.getDefaultInstance()) return this;
            if (other.getDiceEpoch() != 0L) {
                setDiceEpoch(other.getDiceEpoch());
            }
            if (other.hasSide()) {
                mergeSide(other.getSide());
            }
            if (other.getMultiplierIndex() != 0) {
                setMultiplierIndex(other.getMultiplierIndex());
            }
            if (other.getExplodeIndex() != 0) {
                setExplodeIndex(other.getExplodeIndex());
            }
            if (other.getScoreAdjustment() != 0) {
                setScoreAdjustment(other.getScoreAdjustment());
            }
            if (other.getScore() != 0) {
                setScore(other.getScore());
            }
            this.mergeUnknownFields(other.getUnknownFields());
            onChanged();
            return this;
        }

        @java.lang.Override
        public final boolean isInitialized() {
            return true;
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

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.github.jameshnsears.chance.data.domain.proto.Roll.internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.class, com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer.Builder.class);
        }

        private void maybeForceBuilderInitialization() {
            if (com.google.protobuf.GeneratedMessage
                .alwaysUseFieldBuilders) {
                getSideFieldBuilder();
            }
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         *
         * @return Whether the side field is set.
         */
        public boolean hasSide() {
            return ((bitField0_ & 0x00000002) != 0);
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         *
         * @return The side.
         */
        public com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer getSide() {
            if (sideBuilder_ == null) {
                return side_ == null ? com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.getDefaultInstance() : side_;
            } else {
                return sideBuilder_.getMessage();
            }
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        public Builder setSide(com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer value) {
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
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        public Builder setSide(
            com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.Builder builderForValue) {
            if (sideBuilder_ == null) {
                side_ = builderForValue.build();
            } else {
                sideBuilder_.setMessage(builderForValue.build());
            }
            bitField0_ |= 0x00000002;
            onChanged();
            return this;
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
            multiplierIndex_ = 0;
            explodeIndex_ = 0;
            scoreAdjustment_ = 0;
            score_ = 0;
            return this;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.github.jameshnsears.chance.data.domain.proto.Roll.internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
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
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        public com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.Builder getSideBuilder() {
            bitField0_ |= 0x00000002;
            onChanged();
            return getSideFieldBuilder().getBuilder();
        }

        private void buildPartial0(com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer result) {
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
            if (((from_bitField0_ & 0x00000004) != 0)) {
                result.multiplierIndex_ = multiplierIndex_;
            }
            if (((from_bitField0_ & 0x00000008) != 0)) {
                result.explodeIndex_ = explodeIndex_;
            }
            if (((from_bitField0_ & 0x00000010) != 0)) {
                result.scoreAdjustment_ = scoreAdjustment_;
            }
            if (((from_bitField0_ & 0x00000020) != 0)) {
                result.score_ = score_;
            }
            result.bitField0_ |= to_bitField0_;
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
                        case 24: {
                            multiplierIndex_ = input.readInt32();
                            bitField0_ |= 0x00000004;
                            break;
                        } // case 24
                        case 32: {
                            explodeIndex_ = input.readInt32();
                            bitField0_ |= 0x00000008;
                            break;
                        } // case 32
                        case 40: {
                            scoreAdjustment_ = input.readInt32();
                            bitField0_ |= 0x00000010;
                            break;
                        } // case 40
                        case 48: {
                            score_ = input.readInt32();
                            bitField0_ |= 0x00000020;
                            break;
                        } // case 48
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
         * <code>int32 multiplierIndex = 3;</code>
         *
         * @return The multiplierIndex.
         */
        @java.lang.Override
        public int getMultiplierIndex() {
            return multiplierIndex_;
        }

        /**
         * <code>int32 multiplierIndex = 3;</code>
         *
         * @param value The multiplierIndex to set.
         * @return This builder for chaining.
         */
        public Builder setMultiplierIndex(int value) {

            multiplierIndex_ = value;
            bitField0_ |= 0x00000004;
            onChanged();
            return this;
        }

        /**
         * <code>int32 multiplierIndex = 3;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearMultiplierIndex() {
            bitField0_ = (bitField0_ & ~0x00000004);
            multiplierIndex_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        public Builder mergeSide(com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer value) {
            if (sideBuilder_ == null) {
                if (((bitField0_ & 0x00000002) != 0) &&
                    side_ != null &&
                    side_ != com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.getDefaultInstance()) {
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
         * <code>int32 explodeIndex = 4;</code>
         *
         * @return The explodeIndex.
         */
        @java.lang.Override
        public int getExplodeIndex() {
            return explodeIndex_;
        }

        /**
         * <code>int32 explodeIndex = 4;</code>
         *
         * @param value The explodeIndex to set.
         * @return This builder for chaining.
         */
        public Builder setExplodeIndex(int value) {

            explodeIndex_ = value;
            bitField0_ |= 0x00000008;
            onChanged();
            return this;
        }

        /**
         * <code>int32 explodeIndex = 4;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearExplodeIndex() {
            bitField0_ = (bitField0_ & ~0x00000008);
            explodeIndex_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        public com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder getSideOrBuilder() {
            if (sideBuilder_ != null) {
                return sideBuilder_.getMessageOrBuilder();
            } else {
                return side_ == null ?
                    com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.getDefaultInstance() : side_;
            }
        }

        /**
         * <code>int32 scoreAdjustment = 5;</code>
         *
         * @return The scoreAdjustment.
         */
        @java.lang.Override
        public int getScoreAdjustment() {
            return scoreAdjustment_;
        }

        /**
         * <code>int32 scoreAdjustment = 5;</code>
         *
         * @param value The scoreAdjustment to set.
         * @return This builder for chaining.
         */
        public Builder setScoreAdjustment(int value) {

            scoreAdjustment_ = value;
            bitField0_ |= 0x00000010;
            onChanged();
            return this;
        }

        /**
         * <code>int32 scoreAdjustment = 5;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearScoreAdjustment() {
            bitField0_ = (bitField0_ & ~0x00000010);
            scoreAdjustment_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>.com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
         */
        private com.google.protobuf.SingleFieldBuilder<
            com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder>
        getSideFieldBuilder() {
            if (sideBuilder_ == null) {
                sideBuilder_ = new com.google.protobuf.SingleFieldBuilder<
                    com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer.Builder, com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder>(
                    getSide(),
                    getParentForChildren(),
                    isClean());
                side_ = null;
            }
            return sideBuilder_;
        }

        /**
         * <code>int32 score = 6;</code>
         *
         * @return The score.
         */
        @java.lang.Override
        public int getScore() {
            return score_;
        }

        /**
         * <code>int32 score = 6;</code>
         *
         * @param value The score to set.
         * @return This builder for chaining.
         */
        public Builder setScore(int value) {

            score_ = value;
            bitField0_ |= 0x00000020;
            onChanged();
            return this;
        }

        /**
         * <code>int32 score = 6;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearScore() {
            bitField0_ = (bitField0_ & ~0x00000020);
            score_ = 0;
            onChanged();
            return this;
        }

        // @@protoc_insertion_point(builder_scope:com.github.jameshnsears.chance.data.domain.proto.RollProtocolBuffer)
    }

}

