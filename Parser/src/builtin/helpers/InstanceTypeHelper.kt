package builtin.helpers

import ParserReader
import builtin.nodes.InstanceType
import builtin.tokentypes.KeywordTokenType

fun parseInstanceType(reader: ParserReader): InstanceType? {
    if (reader.hope(KeywordTokenType.Struct) != null) return InstanceType.Struct
    if (reader.hope(KeywordTokenType.Class) != null) return InstanceType.Class
    if (reader.hope(KeywordTokenType.Object) != null) return InstanceType.Object
    if (reader.hope(KeywordTokenType.Trait) != null) return InstanceType.Trait
    if (reader.hope(KeywordTokenType.Enum) != null) return InstanceType.Enum

    return null
}