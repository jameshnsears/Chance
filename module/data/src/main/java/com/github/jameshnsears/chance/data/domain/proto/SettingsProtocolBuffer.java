// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: settings.proto
// Protobuf Java Version: 4.28.2

package com.github.jameshnsears.chance.data.domain.proto;

/**
 * Protobuf type {@code com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer}
 */
public final class SettingsProtocolBuffer extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer)
    SettingsProtocolBufferOrBuilder {
    public static final int RESIZE_FIELD_NUMBER = 1;
    public static final int ROLLINDEXTIME_FIELD_NUMBER = 2;
    public static final int ROLLSCORE_FIELD_NUMBER = 3;
    public static final int DICETITLE_FIELD_NUMBER = 4;
    public static final int SIDENUMBER_FIELD_NUMBER = 5;
    public static final int BEHAVIOUR_FIELD_NUMBER = 6;
    public static final int SIDEDESCRIPTION_FIELD_NUMBER = 7;
    public static final int SIDESVG_FIELD_NUMBER = 8;
    public static final int ROLLSOUND_FIELD_NUMBER = 9;
    public static final int SHUFFLE_FIELD_NUMBER = 10;
    private static final long serialVersionUID = 0L;
    // @@protoc_insertion_point(class_scope:com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer)
    private static final com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer DEFAULT_INSTANCE;
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
        com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
            com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
            /* major= */ 4,
            /* minor= */ 28,
            /* patch= */ 2,
            /* suffix= */ "",
            SettingsProtocolBuffer.class.getName());
    }

    static {
        DEFAULT_INSTANCE = new com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer();
    }

    private int resize_ = 0;
    private boolean rollIndexTime_ = false;
    private boolean rollScore_ = false;
    private boolean diceTitle_ = false;
    private boolean sideNumber_ = false;
    private boolean behaviour_ = false;
    private boolean sideDescription_ = false;
    private boolean sideSVG_ = false;
    private boolean rollSound_ = false;
    private boolean shuffle_ = false;
    private byte memoizedIsInitialized = -1;

    // Use SettingsProtocolBuffer.newBuilder() to construct.
    private SettingsProtocolBuffer(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
    }

    private SettingsProtocolBuffer() {
    }

    public static final com.google.protobuf.Descriptors.Descriptor
    getDescriptor() {
        return com.github.jameshnsears.chance.data.domain.proto.Settings.internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor;
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseDelimitedWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessage
            .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    public static com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static com.google.protobuf.Parser<SettingsProtocolBuffer> parser() {
        return PARSER;
    }

    /**
     * <code>int32 resize = 1;</code>
     *
     * @return The resize.
     */
    @java.lang.Override
    public int getResize() {
        return resize_;
    }

    /**
     * <code>bool rollIndexTime = 2;</code>
     *
     * @return The rollIndexTime.
     */
    @java.lang.Override
    public boolean getRollIndexTime() {
        return rollIndexTime_;
    }

    /**
     * <code>bool rollScore = 3;</code>
     *
     * @return The rollScore.
     */
    @java.lang.Override
    public boolean getRollScore() {
        return rollScore_;
    }

    /**
     * <code>bool diceTitle = 4;</code>
     *
     * @return The diceTitle.
     */
    @java.lang.Override
    public boolean getDiceTitle() {
        return diceTitle_;
    }

    /**
     * <code>bool sideNumber = 5;</code>
     *
     * @return The sideNumber.
     */
    @java.lang.Override
    public boolean getSideNumber() {
        return sideNumber_;
    }

    /**
     * <code>bool behaviour = 6;</code>
     *
     * @return The behaviour.
     */
    @java.lang.Override
    public boolean getBehaviour() {
        return behaviour_;
    }

    /**
     * <code>bool sideDescription = 7;</code>
     *
     * @return The sideDescription.
     */
    @java.lang.Override
    public boolean getSideDescription() {
        return sideDescription_;
    }

    /**
     * <code>bool sideSVG = 8;</code>
     *
     * @return The sideSVG.
     */
    @java.lang.Override
    public boolean getSideSVG() {
        return sideSVG_;
    }

    /**
     * <code>bool rollSound = 9;</code>
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
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
    internalGetFieldAccessorTable() {
        return com.github.jameshnsears.chance.data.domain.proto.Settings.internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.class, com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.Builder.class);
    }

    /**
     * <code>bool shuffle = 10;</code>
     *
     * @return The shuffle.
     */
    @java.lang.Override
    public boolean getShuffle() {
        return shuffle_;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
        throws java.io.IOException {
        if (resize_ != 0) {
            output.writeInt32(1, resize_);
        }
        if (rollIndexTime_ != false) {
            output.writeBool(2, rollIndexTime_);
        }
        if (rollScore_ != false) {
            output.writeBool(3, rollScore_);
        }
        if (diceTitle_ != false) {
            output.writeBool(4, diceTitle_);
        }
        if (sideNumber_ != false) {
            output.writeBool(5, sideNumber_);
        }
        if (behaviour_ != false) {
            output.writeBool(6, behaviour_);
        }
        if (sideDescription_ != false) {
            output.writeBool(7, sideDescription_);
        }
        if (sideSVG_ != false) {
            output.writeBool(8, sideSVG_);
        }
        if (rollSound_ != false) {
            output.writeBool(9, rollSound_);
        }
        if (shuffle_ != false) {
            output.writeBool(10, shuffle_);
        }
        getUnknownFields().writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) return size;

        size = 0;
        if (resize_ != 0) {
            size += com.google.protobuf.CodedOutputStream
                .computeInt32Size(1, resize_);
        }
        if (rollIndexTime_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(2, rollIndexTime_);
        }
        if (rollScore_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(3, rollScore_);
        }
        if (diceTitle_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(4, diceTitle_);
        }
        if (sideNumber_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(5, sideNumber_);
        }
        if (behaviour_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(6, behaviour_);
        }
        if (sideDescription_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(7, sideDescription_);
        }
        if (sideSVG_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(8, sideSVG_);
        }
        if (rollSound_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(9, rollSound_);
        }
        if (shuffle_ != false) {
            size += com.google.protobuf.CodedOutputStream
                .computeBoolSize(10, shuffle_);
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
        if (!(obj instanceof com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer)) {
            return super.equals(obj);
        }
        com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer other = (com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer) obj;

        if (getResize()
            != other.getResize()) return false;
        if (getRollIndexTime()
            != other.getRollIndexTime()) return false;
        if (getRollScore()
            != other.getRollScore()) return false;
        if (getDiceTitle()
            != other.getDiceTitle()) return false;
        if (getSideNumber()
            != other.getSideNumber()) return false;
        if (getBehaviour()
            != other.getBehaviour()) return false;
        if (getSideDescription()
            != other.getSideDescription()) return false;
        if (getSideSVG()
            != other.getSideSVG()) return false;
        if (getRollSound()
            != other.getRollSound()) return false;
        if (getShuffle()
            != other.getShuffle()) return false;
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
        hash = (37 * hash) + RESIZE_FIELD_NUMBER;
        hash = (53 * hash) + getResize();
        hash = (37 * hash) + ROLLINDEXTIME_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getRollIndexTime());
        hash = (37 * hash) + ROLLSCORE_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getRollScore());
        hash = (37 * hash) + DICETITLE_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getDiceTitle());
        hash = (37 * hash) + SIDENUMBER_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getSideNumber());
        hash = (37 * hash) + BEHAVIOUR_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getBehaviour());
        hash = (37 * hash) + SIDEDESCRIPTION_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getSideDescription());
        hash = (37 * hash) + SIDESVG_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getSideSVG());
        hash = (37 * hash) + ROLLSOUND_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getRollSound());
        hash = (37 * hash) + SHUFFLE_FIELD_NUMBER;
        hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
            getShuffle());
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
    public com.google.protobuf.Parser<SettingsProtocolBuffer> getParserForType() {
        return PARSER;
    }

    @java.lang.Override
    public com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Protobuf type {@code com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer)
        com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBufferOrBuilder {
        private int bitField0_;
        private int resize_;
        private boolean rollIndexTime_;
        private boolean rollScore_;
        private boolean diceTitle_;
        private boolean sideNumber_;
        private boolean behaviour_;
        private boolean sideDescription_;
        private boolean sideSVG_;
        private boolean rollSound_;
        private boolean shuffle_;

        // Construct using com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.newBuilder()
        private Builder() {

        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
            super(parent);

        }

        public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return com.github.jameshnsears.chance.data.domain.proto.Settings.internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return com.github.jameshnsears.chance.data.domain.proto.Settings.internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer getDefaultInstanceForType() {
            return com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.getDefaultInstance();
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer build() {
            com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @java.lang.Override
        public com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer buildPartial() {
            com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer result = new com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer(this);
            if (bitField0_ != 0) {
                buildPartial0(result);
            }
            onBuilt();
            return result;
        }

        @java.lang.Override
        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer) {
                return mergeFrom((com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer) other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        @java.lang.Override
        public final boolean isInitialized() {
            return true;
        }

        /**
         * <code>int32 resize = 1;</code>
         *
         * @return The resize.
         */
        @java.lang.Override
        public int getResize() {
            return resize_;
        }

        /**
         * <code>int32 resize = 1;</code>
         *
         * @param value The resize to set.
         * @return This builder for chaining.
         */
        public Builder setResize(int value) {

            resize_ = value;
            bitField0_ |= 0x00000001;
            onChanged();
            return this;
        }

        /**
         * <code>int32 resize = 1;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearResize() {
            bitField0_ = (bitField0_ & ~0x00000001);
            resize_ = 0;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollIndexTime = 2;</code>
         *
         * @return The rollIndexTime.
         */
        @java.lang.Override
        public boolean getRollIndexTime() {
            return rollIndexTime_;
        }

        /**
         * <code>bool rollIndexTime = 2;</code>
         *
         * @param value The rollIndexTime to set.
         * @return This builder for chaining.
         */
        public Builder setRollIndexTime(boolean value) {

            rollIndexTime_ = value;
            bitField0_ |= 0x00000002;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollIndexTime = 2;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollIndexTime() {
            bitField0_ = (bitField0_ & ~0x00000002);
            rollIndexTime_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollScore = 3;</code>
         *
         * @return The rollScore.
         */
        @java.lang.Override
        public boolean getRollScore() {
            return rollScore_;
        }

        /**
         * <code>bool rollScore = 3;</code>
         *
         * @param value The rollScore to set.
         * @return This builder for chaining.
         */
        public Builder setRollScore(boolean value) {

            rollScore_ = value;
            bitField0_ |= 0x00000004;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollScore = 3;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollScore() {
            bitField0_ = (bitField0_ & ~0x00000004);
            rollScore_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool diceTitle = 4;</code>
         *
         * @return The diceTitle.
         */
        @java.lang.Override
        public boolean getDiceTitle() {
            return diceTitle_;
        }

        /**
         * <code>bool diceTitle = 4;</code>
         *
         * @param value The diceTitle to set.
         * @return This builder for chaining.
         */
        public Builder setDiceTitle(boolean value) {

            diceTitle_ = value;
            bitField0_ |= 0x00000008;
            onChanged();
            return this;
        }

        /**
         * <code>bool diceTitle = 4;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearDiceTitle() {
            bitField0_ = (bitField0_ & ~0x00000008);
            diceTitle_ = false;
            onChanged();
            return this;
        }

        /**
         * <code>bool sideNumber = 5;</code>
         *
         * @return The sideNumber.
         */
        @java.lang.Override
        public boolean getSideNumber() {
            return sideNumber_;
        }

        /**
         * <code>bool sideNumber = 5;</code>
         *
         * @param value The sideNumber to set.
         * @return This builder for chaining.
         */
        public Builder setSideNumber(boolean value) {

            sideNumber_ = value;
            bitField0_ |= 0x00000010;
            onChanged();
            return this;
        }

        /**
         * <code>bool sideNumber = 5;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearSideNumber() {
            bitField0_ = (bitField0_ & ~0x00000010);
            sideNumber_ = false;
            onChanged();
            return this;
        }

        @java.lang.Override
        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return com.github.jameshnsears.chance.data.domain.proto.Settings.internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.class, com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.Builder.class);
        }

        /**
         * <code>bool behaviour = 6;</code>
         *
         * @return The behaviour.
         */
        @java.lang.Override
        public boolean getBehaviour() {
            return behaviour_;
        }

        /**
         * <code>bool behaviour = 6;</code>
         *
         * @param value The behaviour to set.
         * @return This builder for chaining.
         */
        public Builder setBehaviour(boolean value) {

            behaviour_ = value;
            bitField0_ |= 0x00000020;
            onChanged();
            return this;
        }

        /**
         * <code>bool behaviour = 6;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearBehaviour() {
            bitField0_ = (bitField0_ & ~0x00000020);
            behaviour_ = false;
            onChanged();
            return this;
        }

        @java.lang.Override
        public Builder clear() {
            super.clear();
            bitField0_ = 0;
            resize_ = 0;
            rollIndexTime_ = false;
            rollScore_ = false;
            diceTitle_ = false;
            sideNumber_ = false;
            behaviour_ = false;
            sideDescription_ = false;
            sideSVG_ = false;
            rollSound_ = false;
            shuffle_ = false;
            return this;
        }

        /**
         * <code>bool sideDescription = 7;</code>
         *
         * @return The sideDescription.
         */
        @java.lang.Override
        public boolean getSideDescription() {
            return sideDescription_;
        }

        /**
         * <code>bool sideDescription = 7;</code>
         *
         * @param value The sideDescription to set.
         * @return This builder for chaining.
         */
        public Builder setSideDescription(boolean value) {

            sideDescription_ = value;
            bitField0_ |= 0x00000040;
            onChanged();
            return this;
        }

        /**
         * <code>bool sideDescription = 7;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearSideDescription() {
            bitField0_ = (bitField0_ & ~0x00000040);
            sideDescription_ = false;
            onChanged();
            return this;
        }

        private void buildPartial0(com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer result) {
            int from_bitField0_ = bitField0_;
            if (((from_bitField0_ & 0x00000001) != 0)) {
                result.resize_ = resize_;
            }
            if (((from_bitField0_ & 0x00000002) != 0)) {
                result.rollIndexTime_ = rollIndexTime_;
            }
            if (((from_bitField0_ & 0x00000004) != 0)) {
                result.rollScore_ = rollScore_;
            }
            if (((from_bitField0_ & 0x00000008) != 0)) {
                result.diceTitle_ = diceTitle_;
            }
            if (((from_bitField0_ & 0x00000010) != 0)) {
                result.sideNumber_ = sideNumber_;
            }
            if (((from_bitField0_ & 0x00000020) != 0)) {
                result.behaviour_ = behaviour_;
            }
            if (((from_bitField0_ & 0x00000040) != 0)) {
                result.sideDescription_ = sideDescription_;
            }
            if (((from_bitField0_ & 0x00000080) != 0)) {
                result.sideSVG_ = sideSVG_;
            }
            if (((from_bitField0_ & 0x00000100) != 0)) {
                result.rollSound_ = rollSound_;
            }
            if (((from_bitField0_ & 0x00000200) != 0)) {
                result.shuffle_ = shuffle_;
            }
        }

        /**
         * <code>bool sideSVG = 8;</code>
         *
         * @return The sideSVG.
         */
        @java.lang.Override
        public boolean getSideSVG() {
            return sideSVG_;
        }

        /**
         * <code>bool sideSVG = 8;</code>
         *
         * @param value The sideSVG to set.
         * @return This builder for chaining.
         */
        public Builder setSideSVG(boolean value) {

            sideSVG_ = value;
            bitField0_ |= 0x00000080;
            onChanged();
            return this;
        }

        /**
         * <code>bool sideSVG = 8;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearSideSVG() {
            bitField0_ = (bitField0_ & ~0x00000080);
            sideSVG_ = false;
            onChanged();
            return this;
        }

        public Builder mergeFrom(com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer other) {
            if (other == com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer.getDefaultInstance()) return this;
            if (other.getResize() != 0) {
                setResize(other.getResize());
            }
            if (other.getRollIndexTime() != false) {
                setRollIndexTime(other.getRollIndexTime());
            }
            if (other.getRollScore() != false) {
                setRollScore(other.getRollScore());
            }
            if (other.getDiceTitle() != false) {
                setDiceTitle(other.getDiceTitle());
            }
            if (other.getSideNumber() != false) {
                setSideNumber(other.getSideNumber());
            }
            if (other.getBehaviour() != false) {
                setBehaviour(other.getBehaviour());
            }
            if (other.getSideDescription() != false) {
                setSideDescription(other.getSideDescription());
            }
            if (other.getSideSVG() != false) {
                setSideSVG(other.getSideSVG());
            }
            if (other.getRollSound() != false) {
                setRollSound(other.getRollSound());
            }
            if (other.getShuffle() != false) {
                setShuffle(other.getShuffle());
            }
            this.mergeUnknownFields(other.getUnknownFields());
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSound = 9;</code>
         *
         * @return The rollSound.
         */
        @java.lang.Override
        public boolean getRollSound() {
            return rollSound_;
        }

        /**
         * <code>bool rollSound = 9;</code>
         *
         * @param value The rollSound to set.
         * @return This builder for chaining.
         */
        public Builder setRollSound(boolean value) {

            rollSound_ = value;
            bitField0_ |= 0x00000100;
            onChanged();
            return this;
        }

        /**
         * <code>bool rollSound = 9;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearRollSound() {
            bitField0_ = (bitField0_ & ~0x00000100);
            rollSound_ = false;
            onChanged();
            return this;
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
                            resize_ = input.readInt32();
                            bitField0_ |= 0x00000001;
                            break;
                        } // case 8
                        case 16: {
                            rollIndexTime_ = input.readBool();
                            bitField0_ |= 0x00000002;
                            break;
                        } // case 16
                        case 24: {
                            rollScore_ = input.readBool();
                            bitField0_ |= 0x00000004;
                            break;
                        } // case 24
                        case 32: {
                            diceTitle_ = input.readBool();
                            bitField0_ |= 0x00000008;
                            break;
                        } // case 32
                        case 40: {
                            sideNumber_ = input.readBool();
                            bitField0_ |= 0x00000010;
                            break;
                        } // case 40
                        case 48: {
                            behaviour_ = input.readBool();
                            bitField0_ |= 0x00000020;
                            break;
                        } // case 48
                        case 56: {
                            sideDescription_ = input.readBool();
                            bitField0_ |= 0x00000040;
                            break;
                        } // case 56
                        case 64: {
                            sideSVG_ = input.readBool();
                            bitField0_ |= 0x00000080;
                            break;
                        } // case 64
                        case 72: {
                            rollSound_ = input.readBool();
                            bitField0_ |= 0x00000100;
                            break;
                        } // case 72
                        case 80: {
                            shuffle_ = input.readBool();
                            bitField0_ |= 0x00000200;
                            break;
                        } // case 80
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
         * <code>bool shuffle = 10;</code>
         *
         * @return The shuffle.
         */
        @java.lang.Override
        public boolean getShuffle() {
            return shuffle_;
        }

        /**
         * <code>bool shuffle = 10;</code>
         *
         * @param value The shuffle to set.
         * @return This builder for chaining.
         */
        public Builder setShuffle(boolean value) {

            shuffle_ = value;
            bitField0_ |= 0x00000200;
            onChanged();
            return this;
        }

        /**
         * <code>bool shuffle = 10;</code>
         *
         * @return This builder for chaining.
         */
        public Builder clearShuffle() {
            bitField0_ = (bitField0_ & ~0x00000200);
            shuffle_ = false;
            onChanged();
            return this;
        }

        // @@protoc_insertion_point(builder_scope:com.github.jameshnsears.chance.data.domain.proto.SettingsProtocolBuffer)
    }

}

