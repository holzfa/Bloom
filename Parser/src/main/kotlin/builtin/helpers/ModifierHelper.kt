package builtin.helpers

import ParserReader
import builtin.nodes.Modifier
import builtin.tokentypes.KeywordTokenType
import java.util.EnumSet

fun parseModifier(reader: ParserReader): Modifier? {
    if (reader.hope(KeywordTokenType.Public) != null) return Modifier.Public
    if (reader.hope(KeywordTokenType.Internal) != null) return Modifier.Internal
    if (reader.hope(KeywordTokenType.Protected) != null) return Modifier.Protected
    if (reader.hope(KeywordTokenType.Private) != null) return Modifier.Private

    if (reader.hope(KeywordTokenType.Abstract) != null) return Modifier.Abstract
    if (reader.hope(KeywordTokenType.Open) != null) return Modifier.Open
    if (reader.hope(KeywordTokenType.Impl) != null) return Modifier.Impl

    if (reader.hope(KeywordTokenType.Weak) != null) return Modifier.Weak
    if (reader.hope(KeywordTokenType.Extern) != null) return Modifier.Extern

    return null
}

fun parseModifiers(reader: ParserReader): Set<Modifier> {
    val modifiers = mutableSetOf<Modifier>()

    do {
        val mod = parseModifier(reader)
        if (mod != null) modifiers.add(mod)
    } while (mod != null)

    return modifiers
}