package com.github.jameshnsears.chance.data.domain.utility.json

import com.github.jameshnsears.chance.data.domain.state.Roll
import com.github.victools.jsonschema.generator.OptionPreset
import com.github.victools.jsonschema.generator.SchemaGenerator
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder
import com.github.victools.jsonschema.generator.SchemaVersion
import java.lang.reflect.Type

fun generateSchema(value: Type) {
    val schemaGeneratorConfigBuilder =
        SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
    val schemaGenerator = SchemaGenerator(schemaGeneratorConfigBuilder.build())
    println(schemaGenerator.generateSchema(value).toPrettyString())
}

fun main() {
    generateSchema(Roll::class.java)
}
