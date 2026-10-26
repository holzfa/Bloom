package builtIn

import TokenType

enum class KeywordTokenType: TokenType {
    Null,
    It,
    Self,
    Base,

    Val,
    Var,
    Const,

    Public,
    Internal,
    Protected,
    Private,

    Static,
    Weak,
    Extern,

    Open,
    Abstract,
    Impl,

    Struct,
    Class,
    Trait,
    Enum,
    Extensions,
    Fun,

    If,
    Else,
    Match,
    While,
    For,
    In,
    Break,
    Continue,
    Return,

    Module,
    Import,
    From,

    Ref,
    Get,
    Set
}

val keywords: Map<String, TokenType> = mapOf(
    Pair("null", KeywordTokenType.Null),
    Pair("it", KeywordTokenType.It),
    Pair("self", KeywordTokenType.Self),
    Pair("base", KeywordTokenType.Base),

    Pair("val", KeywordTokenType.Val),
    Pair("var", KeywordTokenType.Var),
    Pair("const", KeywordTokenType.Const),

    Pair("public", KeywordTokenType.Public),
    Pair("internal", KeywordTokenType.Internal),
    Pair("protected", KeywordTokenType.Protected),
    Pair("private", KeywordTokenType.Private),

    Pair("static", KeywordTokenType.Static),
    Pair("weak", KeywordTokenType.Weak),
    Pair("extern", KeywordTokenType.Extern),

    Pair("open", KeywordTokenType.Open),
    Pair("abstract", KeywordTokenType.Abstract),
    Pair("impl", KeywordTokenType.Impl),

    Pair("struct", KeywordTokenType.Struct),
    Pair("class", KeywordTokenType.Class),
    Pair("trait", KeywordTokenType.Trait),
    Pair("enum", KeywordTokenType.Enum),
    Pair("extensions", KeywordTokenType.Extensions),
    Pair("fun", KeywordTokenType.Fun),

    Pair("if", KeywordTokenType.If),
    Pair("else", KeywordTokenType.Else),
    Pair("match", KeywordTokenType.Match),
    Pair("while", KeywordTokenType.While),
    Pair("for", KeywordTokenType.For),
    Pair("in", KeywordTokenType.In),
    Pair("break", KeywordTokenType.Break),
    Pair("continue", KeywordTokenType.Continue),
    Pair("return", KeywordTokenType.Return),

    Pair("module", KeywordTokenType.Module),
    Pair("import", KeywordTokenType.Import),
    Pair("from", KeywordTokenType.From),

    Pair("ref", KeywordTokenType.Ref),
    Pair("get", KeywordTokenType.Get),
    Pair("set", KeywordTokenType.Set),
)