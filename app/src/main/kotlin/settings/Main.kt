package settings

import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

// get user home dir to store app files
val USER_HOME = System.getProperty("user.home")

// this project db files extension
val FILE_EXT = "irdb"

// linux standard for multiple files with the same purpose
val DIR_EXT = "d"

// this project standard for temprary files or directories naming
val TMP_EXT = "tmp"

// this project standard for temprary directories naming
val TMP_DIR_EXT = "$TMP_EXT.$DIR_EXT"

// default db directory
val DB_DIR = "$USER_HOME/.irdb"

// default app documents directory
val DOCS_DIR = "$USER_HOME/ir-docs"

// documents-ids db file
val DOCS_IDS_DB = "$DB_DIR/00_docs-ids.$FILE_EXT"

// simple-inverted-index db file
val SIMPLE_INVERTED_INDEX_DB = "$DB_DIR/01_simple-inverted-index.$FILE_EXT"
// biword-inverted-index db file
val BIWORD_INVERTED_INDEX_DB = "$DB_DIR/02_biword-inverted-index.$FILE_EXT"
// stores each file temprary positional-index
val POSITIONAL_INDEX_TEMP_DIR = "$DB_DIR/03_positional-index.$TMP_DIR_EXT"
// positional-index db file
val POSITIONAL_INDEX_DB = "$DB_DIR/03_positional-index.$FILE_EXT"

fun doSettings() {
    // create DB_DIR if doesn't exist
    val dbDir = Path(DB_DIR)
    if (!dbDir.exists()) {
        println("Create default app DB directory \"$DB_DIR\"")
        dbDir.createDirectory()
    }

    // create DOCS_DIR if doesn't exist
    val docsDir = Path(DOCS_DIR)
    if (!docsDir.exists()) {
        println("Create default app documents directory \"$DOCS_DIR\"")
        docsDir.createDirectory()
    }

    // create POSITIONAL_INDEX_TEMP_DIR if doesn't exist
    val tempPosIndexDir = Path(POSITIONAL_INDEX_TEMP_DIR)
    if (!tempPosIndexDir.exists()) {
        println("Create positional-index temp directory \"$POSITIONAL_INDEX_TEMP_DIR\"")
        tempPosIndexDir.createDirectory()
    }
}
