// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: bag.proto

// Protobuf Java Version: 3.25.1
package com.github.jameshnsears.chance.data.protocolbuffer;

public final class Bag {
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_com_github_jameshnsears_chance_data_protocolbuffer_BagProtocolBuffer_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_com_github_jameshnsears_chance_data_protocolbuffer_BagProtocolBuffer_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\tbag.proto\0222com.github.jameshnsears.cha" +
                        "nce.data.protocolbuffer\032\ndice.proto\"\267\002\n\021" +
                        "BagProtocolBuffer\022\024\n\014tabRowChance\030\001 \001(\005\022" +
                        "\022\n\nbagDemoBag\030\002 \001(\010\022\017\n\007bagZoom\030\003 \001(\005\022T\n\004" +
                        "dice\030\004 \003(\0132F.com.github.jameshnsears.cha" +
                        "nce.data.protocolbuffer.DiceProtocolBuff" +
                        "er\022\030\n\020rollSequentially\030\005 \001(\010\022\020\n\010rollZoom" +
                        "\030\006 \001(\005\022\021\n\trollTitle\030\007 \001(\010\022\027\n\017rollSlideNu" +
                        "mber\030\010 \001(\010\022\021\n\trollTotal\030\t \001(\010\022\023\n\013rollHis" +
                        "tory\030\n \001(\010\022\021\n\trollSound\030\013 \001(\010B\002P\001b\006proto" +
                        "3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                                com.github.jameshnsears.chance.data.protocolbuffer.Dice.getDescriptor(),
                        });
        internal_static_com_github_jameshnsears_chance_data_protocolbuffer_BagProtocolBuffer_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_com_github_jameshnsears_chance_data_protocolbuffer_BagProtocolBuffer_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_com_github_jameshnsears_chance_data_protocolbuffer_BagProtocolBuffer_descriptor,
                new java.lang.String[]{"TabRowChance", "BagDemoBag", "BagZoom", "Dice", "RollSequentially", "RollZoom", "RollTitle", "RollSlideNumber", "RollTotal", "RollHistory", "RollSound",});
        com.github.jameshnsears.chance.data.protocolbuffer.Dice.getDescriptor();
    }

    private Bag() {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(
            com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions(
                (com.google.protobuf.ExtensionRegistryLite) registry);
    }

    public static com.google.protobuf.Descriptors.FileDescriptor
    getDescriptor() {
        return descriptor;
    }

    // @@protoc_insertion_point(outer_class_scope)
}
