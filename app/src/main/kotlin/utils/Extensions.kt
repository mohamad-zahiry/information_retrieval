package utils

import java.io.File


fun File.empty(): File {
    this.writeText("")
    return this
}
