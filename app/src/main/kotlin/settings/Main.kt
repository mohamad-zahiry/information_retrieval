package settings

import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.createDirectory

// get user home dir to store app files
val USER_HOME = System.getProperty("user.home")

// this project db files extension
val FILE_EXT = "irdb"

// default db directory
val DB_DIR = "$USER_HOME/.irdb"

// documents-ids db file
val DOCS_IDS_DB = "$DB_DIR/00_docs-ids.$FILE_EXT"

// simple-inverted-index db file
val SIMPLE_INVERTED_INDEX_DB = "$DB_DIR/01_simple-inverted-index.$FILE_EXT"
// biword-inverted-index db file
val BIWORD_INVERTED_INDEX_DB = "$DB_DIR/02_biword-inverted-index.$FILE_EXT"


fun doSettings() {
    // create DB_DIR if doesn't exist
    val dbDir = Path(DB_DIR)
    if (!dbDir.exists()){
        println("Create default app DB directory \"$DB_DIR\"")
        dbDir.createDirectory()
    }
}
