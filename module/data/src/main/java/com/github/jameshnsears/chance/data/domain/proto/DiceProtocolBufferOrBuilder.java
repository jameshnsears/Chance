// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: dice.proto

// Protobuf Java Version: 3.25.3
package com.github.jameshnsears.chance.data.domain.proto;

public interface DiceProtocolBufferOrBuilder extends
        // @@protoc_insertion_point(interface_extends:com.github.jameshnsears.chance.data.domain.proto.DiceProtocolBuffer)
        com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 epoch = 1;</code>
     *
     * @return The epoch.
     */
    long getEpoch();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    java.util.List<com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer>
    getSideList();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer getSide(int index);

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    int getSideCount();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    java.util.List<? extends com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder>
    getSideOrBuilderList();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.domain.proto.SideProtocolBuffer side = 2;</code>
     */
    com.github.jameshnsears.chance.data.domain.proto.SideProtocolBufferOrBuilder getSideOrBuilder(
            int index);

    /**
     * <code>string title = 3;</code>
     *
     * @return The title.
     */
    java.lang.String getTitle();

    /**
     * <code>string title = 3;</code>
     *
     * @return The bytes for title.
     */
    com.google.protobuf.ByteString
    getTitleBytes();

    /**
     * <code>int32 titleStringsId = 4;</code>
     *
     * @return The titleStringsId.
     */
    int getTitleStringsId();

    /**
     * <code>string colour = 5;</code>
     *
     * @return The colour.
     */
    java.lang.String getColour();

    /**
     * <code>string colour = 5;</code>
     *
     * @return The bytes for colour.
     */
    com.google.protobuf.ByteString
    getColourBytes();

    /**
     * <code>bool selected = 6;</code>
     *
     * @return The selected.
     */
    boolean getSelected();

    /**
     * <code>bool multiplier = 7;</code>
     *
     * @return The multiplier.
     */
    boolean getMultiplier();

    /**
     * <code>int32 multiplierValue = 8;</code>
     *
     * @return The multiplierValue.
     */
    int getMultiplierValue();

    /**
     * <code>bool explode = 9;</code>
     *
     * @return The explode.
     */
    boolean getExplode();

    /**
     * <code>string explodeWhen = 10;</code>
     *
     * @return The explodeWhen.
     */
    java.lang.String getExplodeWhen();

    /**
     * <code>string explodeWhen = 10;</code>
     *
     * @return The bytes for explodeWhen.
     */
    com.google.protobuf.ByteString
    getExplodeWhenBytes();

    /**
     * <code>int32 explodeValue = 11;</code>
     *
     * @return The explodeValue.
     */
    int getExplodeValue();

    /**
     * <code>bool modifyScore = 12;</code>
     *
     * @return The modifyScore.
     */
    boolean getModifyScore();

    /**
     * <code>int32 modifyScoreValue = 13;</code>
     *
     * @return The modifyScoreValue.
     */
    int getModifyScoreValue();
}
