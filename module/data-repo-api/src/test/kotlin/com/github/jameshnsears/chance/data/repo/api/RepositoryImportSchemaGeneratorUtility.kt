package com.github.jameshnsears.chance.data.repo.api

import com.github.jameshnsears.chance.data.domain.core.roll.Roll
import com.github.victools.jsonschema.generator.OptionPreset
import com.github.victools.jsonschema.generator.SchemaGenerator
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder
import com.github.victools.jsonschema.generator.SchemaVersion
import org.junit.Test
import java.lang.reflect.Type


class RepositoryImportSchemaGeneratorUtility {
    @Test
    fun createJsonSchema() {
        val value: Type = Roll::class.java  // Side::class.java; etc ...

        val schemaGeneratorConfigBuilder =
            SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
        val schemaGenerator = SchemaGenerator(schemaGeneratorConfigBuilder.build())

        println(schemaGenerator.generateSchema(value).toPrettyString())
    }
}
