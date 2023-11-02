package ir

import cli.cmdAddDocs
import cli.cmdCreateIndex
import cli.cmdFind
import cli.cmdFindWithSkips
import cli.cmdHelp
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size == 0) {
        println("not enough arguments\n")
        cmdHelp()
        exitProcess(1)
    }

    val command = args[0].lowercase()

    when (command) {
        "add_docs" -> cmdAddDocs()
        "create_index" -> cmdCreateIndex(args)
        "find" -> cmdFind(args)
        "find_skip" -> cmdFindWithSkips(args)
        "help" -> cmdHelp()
        else -> println("wrong command")
    }

    exitProcess(0)
}
