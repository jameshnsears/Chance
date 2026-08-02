package com.github.jameshnsears.chance.data.repo.api

import com.github.jameshnsears.chance.data.domain.core.Dice
import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.group.Group
import com.github.victools.jsonschema.generator.OptionPreset
import com.github.victools.jsonschema.generator.SchemaGenerator
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder
import com.github.victools.jsonschema.generator.SchemaVersion
import org.junit.Test
import java.lang.reflect.Type

// gives some "advice" for manually editing an ImportSchema - i.e. RepositoryImportSchemaV250
class CreateImportSchemaFromType {
    @Test
    fun createJsonSchema() {
        val dice: Type = Dice::class.java
        val side: Type = Side::class.java
        val group: Type = Group::class.java

        val schemaGeneratorConfigBuilder =
            SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
        val schemaGenerator = SchemaGenerator(schemaGeneratorConfigBuilder.build())

        println(schemaGenerator.generateSchema(group).toPrettyString())
    }
}
