// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: roll.proto
// Protobuf Java Version: 4.28.2

package com.github.jameshnsears.chance.data.domain.proto;

public final class Roll {
    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor;
    static final
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
        descriptor;

    static {
        com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
            com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
            /* major= */ 4,
            /* minor= */ 28,
            /* patch= */ 2,
            /* suffix= */ "",
            Roll.class.getName());
    }

    static {
        java.lang.String[] descriptorData = {
            "\n\nroll.proto\0220com.github.jameshnsears.ch" +
                "ance.data.domain.proto\032\nside.proto\"\322\001\n\022R" +
                "ollProtocolBuffer\022\021\n\tdiceEpoch\030\001 \001(\003\022R\n\004" +
                "side\030\002 \001(\0132D.com.github.jameshnsears.cha" +
                "nce.data.domain.proto.SideProtocolBuffer" +
                "\022\027\n\017multiplierIndex\030\003 \001(\005\022\024\n\014explodeInde" +
                "x\030\004 \001(\005\022\027\n\017scoreAdjustment\030\005 \001(\005\022\r\n\005scor" +
                "e\030\006 \001(\005B\002P\001b\006proto3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
            .internalBuildGeneratedFileFrom(descriptorData,
                new com.google.protobuf.Descriptors.FileDescriptor[]{
                    com.github.jameshnsears.chance.data.domain.proto.Side.getDescriptor(),
                });
        internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor =
            getDescriptor().getMessageTypes().get(0);
        internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_com_github_jameshnsears_chance_data_domain_proto_RollProtocolBuffer_descriptor,
            new java.lang.String[]{"DiceEpoch", "Side", "MultiplierIndex", "ExplodeIndex", "ScoreAdjustment", "Score",});
        descriptor.resolveAllFeaturesImmutable();
        com.github.jameshnsears.chance.data.domain.proto.Side.getDescriptor();
    }

    private Roll() {
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
