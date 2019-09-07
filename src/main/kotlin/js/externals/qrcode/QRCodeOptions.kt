package js.externals.qrcode

data class QRCodeOptions(
    val errorCorrectionLevel: String = "M",
    val maskPattern: Int = 0,
    val scale: Int = 4
)