package js.externals.toastr

@JsModule("toastr")
external class toastr {
    companion object {
        fun info(message: String)
        fun warning(message: String)
        fun success(message: String)
    }
}