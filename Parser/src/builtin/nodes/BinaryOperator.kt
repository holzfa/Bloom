package builtin.nodes

enum class BinaryOperator {
    Equality /** == */,
    Inequality /** != */,
    LessThanEqual /** <= */,
    LogicalAnd /** && */,
    LogicalOr /** || */,
    LogicalXor /** ^^ */,

    LeftShift /** << */,
    RightShift /** >> */,

    Add /** + */,
    Subtract /** - */,
    Multiply /** * */,
    Divide /** / */,
    Modulo /** % */,

    LessThan /** < */,
    GreaterThan /** `> */,
    LogicalNot /** ! */,
    BitwiseAnd /** & */,
    BitwiseOr /** | */,
    BitwiseXor /** ^ */,

    Is /** is */;

    override fun toString(): String = when (this) {
        Equality -> "=="
        Inequality -> "!="
        LessThanEqual -> "<="
        LogicalAnd -> "&&"
        LogicalOr -> "||"
        LogicalXor -> "^^"
        LeftShift -> "<<"
        RightShift -> ">>"
        Add -> "+"
        Subtract -> "-"
        Multiply -> "*"
        Divide -> "/"
        Modulo -> "%"
        LessThan -> "<"
        GreaterThan -> "`>"
        LogicalNot -> "!"
        BitwiseAnd -> "&"
        BitwiseOr -> "|"
        BitwiseXor -> "^"
        else -> "<operator>"
    }
}