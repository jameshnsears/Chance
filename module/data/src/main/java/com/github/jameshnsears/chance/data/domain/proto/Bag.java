// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: bag.proto
// Protobuf Java Version: 4.28.2

package com.github.jameshnsears.chance.data.domain.proto;

public final class Bag {
    static final com.google.protobuf.Descriptors.Descriptor
        internal_static_com_github_jameshnsears_chance_data_domain_proto_BagProtocolBuffer_descriptor;
    static final
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internal_static_com_github_jameshnsears_chance_data_domain_proto_BagProtocolBuffer_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
        descriptor;

    static {
        com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
            com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
            /* major= */ 4,
            /* minor= */ 28,
            /* patch= */ 2,
            /* suffix= */ "",
            Bag.class.getName());
    }

    static {
        java.lang.String[] descriptorData = {
            "\n\tbag.proto\0220com.github.jameshnsears.cha" +
                "nce.data.domain.proto\032\ndice.proto\"g\n\021Bag" +
                "ProtocolBuffer\022R\n\004dice\030\001 \003(\0132D.com.githu" +
                "b.jameshnsears.chance.data.domain.proto." +
                "DiceProtocolBufferB\002P\001b\006proto3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
            .internalBuildGeneratedFileFrom(descriptorData,
                new com.google.protobuf.Descriptors.FileDescriptor[]{
                    com.github.jameshnsears.chance.data.domain.proto.Dice.getDescriptor(),
                });
        internal_static_com_github_jameshnsears_chance_data_domain_proto_BagProtocolBuffer_descriptor =
            getDescriptor().getMessageTypes().get(0);
        internal_static_com_github_jameshnsears_chance_data_domain_proto_BagProtocolBuffer_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
            internal_static_com_github_jameshnsears_chance_data_domain_proto_BagProtocolBuffer_descriptor,
            new java.lang.String[]{"Dice",});
        descriptor.resolveAllFeaturesImmutable();
        com.github.jameshnsears.chance.data.domain.proto.Dice.getDescriptor();
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
