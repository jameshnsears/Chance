// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: settings.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

/**
 * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer}
 */
public final class SettingsProtocolBuffer extends
        com.google.protobuf.GeneratedMessageV3 implements
        // @@protoc_insertion_point(message_implements:com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer)
        SettingsProtocolBufferOrBuilder {
    public static final int TABROWCHANCE_FIELD_NUMBER = 1;
    public static final int BAGDEMOBAG_FIELD_NUMBER = 2;
    public static final int ROLLSEQUENTIALLY_FIELD_NUMBER = 3;
    public static final int ROLLHISTORY_FIELD_NUMBER = 4;
    public static final int ROLLSCORE_FIELD_NUMBER = 5;
    public static final int ROLLDICETITLE_FIELD_NUMBER = 6;
    public static final int ROLLSIDENUMBER_FIELD_NUMBER = 7;
    public static final int ROLLSOUND_FIELD_NUMBER = 8;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer)
    private static final com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer DEFAULT_INSTANCE;
    private static final com.google.protobuf.Parser<SettingsProtocolBuffer>
            PARSER = new com.google.protobuf.AbstractParser<SettingsProtocolBuffer>() {
        @java.lang.Override
        public SettingsProtocolBuffer parsePartialFrom(
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
        DEFAULT_INSTANCE = new com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer();
    }

    private int tabRowChance_ = 0;
    private boolean bagDemoBag_ = false;
    private boolean rollSequentially_ = false;
    private boolean rollHistory_ = false;
    private boolean rollScore_ = false;
    private boolean rollDiceTitle_ = false;
    private boolean rollSideNumber_ = false;
    private boolean rollSound_ = false;
    private byte memoizedIsInitialized = -1;

    // Use SettingsProtocolBuffer.newBuilder() to construct.
    private SettingsProtocolBuffer(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }

    private SettingsProtocolBuffer() {
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.github.jameshnsears.chance.data.protocolbuffer.Settings.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_SettingsProtocolBuffer_descriptor;
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            java.nio.ByteBuffer data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseDelimitedFrom(java.io.InputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3
                .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<SettingsProtocolBuffer> parser() {
        return PARSER;
    }

    @java.lang.Override
    @SuppressWarnings({"unused"})
    protected java.lang.Object newInstance(
            UnusedPrivateParameter unused) {
        return new SettingsProtocolBuffer();
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.github.jameshnsears.chance.data.protocolbuffer.Settings.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_SettingsProtocolBuffer_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                        com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.Builder.class);
    }

    /**
     * <code>int32 tabRowChance = 1;</code>
     *
     * @return The tabRowChance.
     */
    @java.lang.Override
    public int getTabRowChance() {
        return tabRowChance_;
    }

    /**
     * <code>bool bagDemoBag = 2;</code>
     *
     * @return The bagDemoBag.
     */
    @java.lang.Override
    public boolean getBagDemoBag() {
        return bagDemoBag_;
    }

    /**
     * <code>bool rollSequentially = 3;</code>
     *
     * @return The rollSequentially.
     */
    @java.lang.Override
    public boolean getRollSequentially() {
        return rollSequentially_;
    }

    /**
     * <code>bool rollHistory = 4;</code>
     *
     * @return The rollHistory.
     */
    @java.lang.Override
    public boolean getRollHistory() {
        return rollHistory_;
    }

    /**
     * <code>bool rollScore = 5;</code>
     *
     * @return The rollScore.
     */
    @java.lang.Override
    public boolean getRollScore() {
        return rollScore_;
    }

    /**
     * <code>bool rollDiceTitle = 6;</code>
     *
     * @return The rollDiceTitle.
     */
    @java.lang.Override
    public boolean getRollDiceTitle() {
        return rollDiceTitle_;
    }

    /**
     * <code>bool rollSideNumber = 7;</code>
     *
     * @return The rollSideNumber.
     */
    @java.lang.Override
    public boolean getRollSideNumber() {
        return rollSideNumber_;
    }

    /**
     * <code>bool rollSound = 8;</code>
     *
     * @return The rollSound.
     */
    @java.lang.Override
    public boolean getRollSound() {
        return rollSound_;
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
        if (tabRowChance_ != 0) {
            output.writeInt32(1, tabRowChance_);
        }
        if (bagDemoBag_ != false) {
            output.writeBool(2, bagDemoBag_);
        }
        if (rollSequentially_ != false) {
            output.writeBool(3, rollSequentially_);
        }
        if (rollHistory_ != false) {
            output.writeBool(4, rollHistory_);
        }
        if (rollScore_ != false) {
            output.writeBool(5, rollScore_);
        }
        if (rollDiceTitle_ != false) {
            output.writeBool(6, rollDiceTitle_);
        }
        if (rollSideNumber_ != false) {
            output.writeBool(7, rollSideNumber_);
        }
        if (rollSound_ != false) {
            output.writeBool(8, rollSound_);
        }
        getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (tabRowChance_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                    .computeInt32Size(1, tabRowChance_);
        }
        if (bagDemoBag_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(2, bagDemoBag_);
        }
        if (rollSequentially_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(3, rollSequentially_);
        }
        if (rollHistory_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(4, rollHistory_);
        }
        if (rollScore_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(5, rollScore_);
        }
        if (rollDiceTitle_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(6, rollDiceTitle_);
        }
        if (rollSideNumber_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(7, rollSideNumber_);
        }
        if (rollSound_ != false) {
            size += com.google.protobuf.CodedOutputStream
                    .computeBoolSize(8, rollSound_);
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
        if (!(obj instanceof com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer)) {
            return super.equals(obj);
        }
        com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer other = (com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer) obj;

        if (getTabRowChance()
                != other.getTabRowChance()) return false;
        if (getBagDemoBag()
                != other.getBagDemoBag()) return false;
        if (getRollSequentially()
                != other.getRollSequentially()) return false;
        if (getRollHistory()
                != other.getRollHistory()) return false;
        if (getRollScore()
                != other.getRollScore()) return false;
        if (getRollDiceTitle()
                != other.getRollDiceTitle()) return false;
        if (getRollSideNumber()
                != other.getRollSideNumber()) return false;
        if (getRollSound()
                != other.getRollSound()) return false;
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
        hash = (37 * hash) + TABROWCHANCE_FIELD_NUMBER;
        hash = (53 * hash) + getTabRowChance();
        hash = (37 * hash) + BAGDEMOBAG_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getBagDemoBag());
        hash = (37 * hash) + ROLLSEQUENTIALLY_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollSequentially());
        hash = (37 * hash) + ROLLHISTORY_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollHistory());
        hash = (37 * hash) + ROLLSCORE_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollScore());
        hash = (37 * hash) + ROLLDICETITLE_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollDiceTitle());
        hash = (37 * hash) + ROLLSIDENUMBER_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollSideNumber());
        hash = (37 * hash) + ROLLSOUND_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
                getRollSound());
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
    public com.google.protobuf.Parser<SettingsProtocolBuffer> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer}
     */
    public static final class Builder extends
            com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
            // @@protoc_insertion_point(builder_implements:com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer)
            com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBufferOrBuilder {
        private int bitField0_;
        private int tabRowChance_;
        private boolean bagDemoBag_;
        private boolean rollSequentially_;
        private boolean rollHistory_;
        private boolean rollScore_;
        private boolean rollDiceTitle_;
        private boolean rollSideNumber_;
        private boolean rollSound_;

        // Construct using com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.newBuilder()
        private Builder() {

        }

        private Builder(
                com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);

        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Settings.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_SettingsProtocolBuffer_descriptor;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Settings.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_SettingsProtocolBuffer_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                            com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.class, com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.Builder.class);
        }

        @java.lang.Override
        public Builder clear() {
            super.clear();
            bitField0_ = 0;
            tabRowChance_ = 0;
            bagDemoBag_ = false;
            rollSequentially_ = false;
            rollHistory_ = false;
            rollScore_ = false;
            rollDiceTitle_ = false;
            rollSideNumber_ = false;
            rollSound_ = false;
            return this;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.Settings.internal_static_com_github_jameshnsears_chance_data_protocolbuffer_SettingsProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer getDefaultInstanceForType() {
            return com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.getDefaultInstance();
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer build() {
            com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer buildPartial() {
            com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer result = new com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer(this);
            if (bitField0_ != 0) {
                buildPartial0(result);
            }
            onBuilt();
            return result;
        }

        private void buildPartial0(com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer result) {
            int from_bitField0_ = bitField0_;
            if (((from_bitField0_ & 0x00000001) != 0)) {
                result.tabRowChance_ = tabRowChance_;
            }
            if (((from_bitField0_ & 0x00000002) != 0)) {
                result.bagDemoBag_ = bagDemoBag_;
            }
            if (((from_bitField0_ & 0x00000004) != 0)) {
                result.rollSequentially_ = rollSequentially_;
            }
            if (((from_bitField0_ & 0x00000008) != 0)) {
                result.rollHistory_ = rollHistory_;
            }
            if (((from_bitField0_ & 0x00000010) != 0)) {
                result.rollScore_ = rollScore_;
            }
            if (((from_bitField0_ & 0x00000020) != 0)) {
                result.rollDiceTitle_ = rollDiceTitle_;
            }
            if (((from_bitField0_ & 0x00000040) != 0)) {
                result.rollSideNumber_ = rollSideNumber_;
            }
            if (((from_bitField0_ & 0x00000080) != 0)) {
                result.rollSound_ = rollSound_;
            }
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
            if (other instanceof com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer) {
                return mergeFrom((com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer other) {
            if (other == com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer.getDefaultInstance())
                return this;
            if (other.getTabRowChance() != 0) {
                setTabRowChance(other.getTabRowChance());
            }
            if (other.getBagDemoBag() != false) {
                setBagDemoBag(other.getBagDemoBag());
            }
            if (other.getRollSequentially() != false) {
                setRollSequentially(other.getRollSequentially());
            }
            if (other.getRollHistory() != false) {
                setRollHistory(other.getRollHistory());
            }
            if (other.getRollScore() != false) {
                setRollScore(other.getRollScore());
            }
            if (other.getRollDiceTitle() != false) {
                setRollDiceTitle(other.getRollDiceTitle());
            }
            if (other.getRollSideNumber() != false) {
                setRollSideNumber(other.getRollSideNumber());
            }
            if (other.getRollSound() != false) {
                setRollSound(other.getRollSound());
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
                            tabRowChance_ = input.readInt32();
                            bitField0_ |= 0x00000001;
                            break;
                        } // case 8
                        case 16: {
                            bagDemoBag_ = input.readBool();
                            bitField0_ |= 0x00000002;
                            break;
                        } // case 16
                        case 24: {
                            rollSequentially_ = input.readBool();
                            bitField0_ |= 0x00000004;
                            break;
                        } // case 24
                        case 32: {
                            rollHistory_ = input.readBool();
                            bitField0_ |= 0x00000008;
                            break;
                        } // case 32
                        case 40: {
                            rollScore_ = input.readBool();
                            bitField0_ |= 0x00000010;
                            break;
                        } // case 40
                        case 48: {
                            rollDiceTitle_ = input.readBool();
                            bitField0_ |= 0x00000020;
                            break;
                        } // case 48
                        case 56: {
                            rollSideNumber_ = input.readBool();
                            bitField0_ |= 0x00000040;
                            break;
                        } // case 56
                        case 64: {
                            rollSound_ = input.readBool();
                            bitField0_ |= 0x00000080;
                            break;
                        } // case 64
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
         * <code>int32 tabRowChance = 1;</code>
         *
         * @return The tabRowChance.
         */
        @java.lang.Override
        public int getTabRowChance() {
            return tabRowChance_;
        }

        /**
         * <code>int32 tabRowChance = 1;</code>
         *
         * @param value The tabRowChance to set.
         * @return This builder for chaining.
         */
        public Builder setTabRowChance(int value) {

            tabRowChance_ = value;
            bitField0_ |= 0x00000001;
            onChanged();
            return this;
        }

        /**
         * <code>int32 tabRowChance = 1;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearTabRowChance() {
            bitField0_ = (bitField0_ & ~0x00000001);
            tabRowChance_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>bool bagDemoBag = 2;</code>
         *
         * @return The bagDemoBag.
         */
        @java.lang.Override
        public boolean getBagDemoBag() {
            return bagDemoBag_;
        }

        /**
         * <code>bool bagDemoBag = 2;</code>
         *
         * @param value The bagDemoBag to set.
         * @return This builder for chaining.
         */
        public Builder setBagDemoBag(boolean value) {

            bagDemoBag_ = value;
            bitField0_ |= 0x00000002;
            onChanged();
            return this;
        }

        /**
         * <code>bool bagDemoBag = 2;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearBagDemoBag() {
            bitField0_ = (bitField0_ & ~0x00000002);
            bagDemoBag_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSequentially = 3;</code>
         *
         * @return The rollSequentially.
         */
        @java.lang.Override
        public boolean getRollSequentially() {
            return rollSequentially_;
        }

        /**
         * <code>bool rollSequentially = 3;</code>
         *
         * @param value The rollSequentially to set.
         * @return This builder for chaining.
         */
        public Builder setRollSequentially(boolean value) {

            rollSequentially_ = value;
            bitField0_ |= 0x00000004;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSequentially = 3;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollSequentially() {
            bitField0_ = (bitField0_ & ~0x00000004);
            rollSequentially_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollHistory = 4;</code>
         *
         * @return The rollHistory.
         */
        @java.lang.Override
        public boolean getRollHistory() {
            return rollHistory_;
        }

        /**
         * <code>bool rollHistory = 4;</code>
         *
         * @param value The rollHistory to set.
         * @return This builder for chaining.
         */
        public Builder setRollHistory(boolean value) {

            rollHistory_ = value;
            bitField0_ |= 0x00000008;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollHistory = 4;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollHistory() {
            bitField0_ = (bitField0_ & ~0x00000008);
            rollHistory_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollScore = 5;</code>
         *
         * @return The rollScore.
         */
        @java.lang.Override
        public boolean getRollScore() {
            return rollScore_;
        }

        /**
         * <code>bool rollScore = 5;</code>
         *
         * @param value The rollScore to set.
         * @return This builder for chaining.
         */
        public Builder setRollScore(boolean value) {

            rollScore_ = value;
            bitField0_ |= 0x00000010;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollScore = 5;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollScore() {
            bitField0_ = (bitField0_ & ~0x00000010);
            rollScore_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollDiceTitle = 6;</code>
         *
         * @return The rollDiceTitle.
         */
        @java.lang.Override
        public boolean getRollDiceTitle() {
            return rollDiceTitle_;
        }

        /**
         * <code>bool rollDiceTitle = 6;</code>
         *
         * @param value The rollDiceTitle to set.
         * @return This builder for chaining.
         */
        public Builder setRollDiceTitle(boolean value) {

            rollDiceTitle_ = value;
            bitField0_ |= 0x00000020;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollDiceTitle = 6;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollDiceTitle() {
            bitField0_ = (bitField0_ & ~0x00000020);
            rollDiceTitle_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSideNumber = 7;</code>
         *
         * @return The rollSideNumber.
         */
        @java.lang.Override
        public boolean getRollSideNumber() {
            return rollSideNumber_;
        }

        /**
         * <code>bool rollSideNumber = 7;</code>
         *
         * @param value The rollSideNumber to set.
         * @return This builder for chaining.
         */
        public Builder setRollSideNumber(boolean value) {

            rollSideNumber_ = value;
            bitField0_ |= 0x00000040;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSideNumber = 7;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollSideNumber() {
            bitField0_ = (bitField0_ & ~0x00000040);
            rollSideNumber_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSound = 8;</code>
         *
         * @return The rollSound.
         */
        @java.lang.Override
        public boolean getRollSound() {
            return rollSound_;
        }

        /**
         * <code>bool rollSound = 8;</code>
         *
         * @param value The rollSound to set.
         * @return This builder for chaining.
         */
        public Builder setRollSound(boolean value) {

            rollSound_ = value;
            bitField0_ |= 0x00000080;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSound = 8;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollSound() {
            bitField0_ = (bitField0_ & ~0x00000080);
            rollSound_ = false;
            onChanged();
            return this;
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


        // @@protoc_insertion_point(builder_scope:com.github.jameshnsears.chance.data.protocolbuffer.SettingsProtocolBuffer)
    }

}
