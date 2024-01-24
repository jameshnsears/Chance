// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bag.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

public interface BagProtocolBufferOrBuilder extends
        // @@protoc_insertion_point(interface_extends:com.github.jameshnsears.chance.data.protocolbuffer.BagProtocolBuffer)
        com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 tabRowChance = 1;</code>
     *
     * @return The tabRowChance.
     */
    int getTabRowChance();

    /**
     * <code>bool bagDemoBag = 2;</code>
     *
     * @return The bagDemoBag.
     */
    boolean getBagDemoBag();

    /**
     * <code>int32 bagZoom = 3;</code>
     *
     * @return The bagZoom.
     */
    int getBagZoom();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer dice = 4;</code>
     */
    java.util.List<com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer>
    getDiceList();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer dice = 4;</code>
     */
    com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer getDice(int index);

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer dice = 4;</code>
     */
    int getDiceCount();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer dice = 4;</code>
     */
    java.util.List<? extends com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBufferOrBuilder>
    getDiceOrBuilderList();

    /**
     * <code>repeated .com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBuffer dice = 4;</code>
     */
    com.github.jameshnsears.chance.data.protocolbuffer.DiceProtocolBufferOrBuilder getDiceOrBuilder(
            int index);

    /**
     * <code>bool rollSequentially = 5;</code>
     *
     * @return The rollSequentially.
     */
    boolean getRollSequentially();

    /**
     * <code>int32 rollZoom = 6;</code>
     *
     * @return The rollZoom.
     */
    int getRollZoom();

    /**
     * <code>bool rollTitle = 7;</code>
     *
     * @return The rollTitle.
     */
    boolean getRollTitle();

    /**
     * <code>bool rollSlideNumber = 8;</code>
     *
     * @return The rollSlideNumber.
     */
    boolean getRollSlideNumber();

    /**
     * <code>bool rollTotal = 9;</code>
     *
     * @return The rollTotal.
     */
    boolean getRollTotal();

    /**
     * <code>bool rollHistory = 10;</code>
     *
     * @return The rollHistory.
     */
    boolean getRollHistory();

    /**
     * <code>bool rollSound = 11;</code>
     *
     * @return The rollSound.
     */
    boolean getRollSound();
}