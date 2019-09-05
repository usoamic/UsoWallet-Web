package js.externals.datatables.net.model

data class DataTableOption(
    val data: List<List<Any>>,
    val bDestroy: Boolean = true
) {
    companion object {
        fun initEmpty(): DataTableOption {
            return DataTableOption(mutableListOf())
        }
    }
}