// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: settings.proto

// Protobuf Java Version: 3.25.3
package com.github.jameshnsears.chance.data.domain.proto;

public final class Settings {
    static final com.google.protobuf.Descriptors.Descriptor
            internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor;
    static final
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
            internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.FileDescriptor
            descriptor;

    static {
        java.lang.String[] descriptorData = {
                "\n\016settings.proto\0220com.github.jameshnsear" +
                        "s.chance.data.domain.proto\"\311\001\n\026SettingsP" +
                        "rotocolBuffer\022\016\n\006resize\030\001 \001(\005\022\025\n\rrollInd" +
                        "exTime\030\002 \001(\010\022\021\n\trollScore\030\003 \001(\010\022\021\n\tdiceT" +
                        "itle\030\004 \001(\010\022\022\n\nsideNumber\030\005 \001(\010\022\021\n\tbehavi" +
                        "our\030\006 \001(\010\022\027\n\017sideDescription\030\007 \001(\010\022\017\n\007si" +
                        "deSVG\030\010 \001(\010\022\021\n\trollSound\030\t \001(\010B\002P\001b\006prot" +
                        "o3"
        };
        descriptor = com.google.protobuf.Descriptors.FileDescriptor
                .internalBuildGeneratedFileFrom(descriptorData,
                        new com.google.protobuf.Descriptors.FileDescriptor[]{
                        });
        internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor =
                getDescriptor().getMessageTypes().get(0);
        internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_fieldAccessorTable = new
                com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
                internal_static_com_github_jameshnsears_chance_data_domain_proto_SettingsProtocolBuffer_descriptor,
                new java.lang.String[]{"Resize", "RollIndexTime", "RollScore", "DiceTitle", "SideNumber", "Behaviour", "SideDescription", "SideSVG", "RollSound",});
    }
    private Settings() {
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
