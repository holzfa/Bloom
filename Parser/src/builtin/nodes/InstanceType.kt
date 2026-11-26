package builtin.nodes

enum class InstanceType {
    Struct,
    Class,
    Object,
    Enum,
    Trait;

    override fun toString(): String = this.name.lowercase()
}