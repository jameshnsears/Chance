// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: roll.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

public interface RollProtocolBufferOrBuilder extends
        // @@protoc_insertion_point(interface_extends:com.github.jameshnsears.chance.data.protocolbuffer.RollProtocolBuffer)
        com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int64 diceEpoch = 1;</code>
     *
     * @return The diceEpoch.
     */
    long getDiceEpoch();

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     *
     * @return Whether the side field is set.
     */
    boolean hasSide();

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     *
     * @return The side.
     */
    com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer getSide();

    /**
     * <code>.com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBuffer side = 2;</code>
     */
    com.github.jameshnsears.chance.data.protocolbuffer.SideProtocolBufferOrBuilder getSideOrBuilder();
}
