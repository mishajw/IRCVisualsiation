package ircvis.back.retriever

import java.io.{InputStreamReader, BufferedReader, OutputStreamWriter, BufferedWriter}
import java.net.Socket

import ircvis.IRC

object IRCUserRetriever {

  private val botName = "ircvisbot"
  private val botDesc = "ircvisbot"

  private val defaultPort = 6667
  private val defaultHost = "irc.freenode.net"

  def get(implicit irc: IRC) = {
    val (in, out) = streams
    sendAuth(out)
    readLoop(in)
  }

  private def readLoop(in: BufferedReader): Unit = {
    while (true) {
      val line = in.readLine()
      println(line)
    }
  }

  private def streams(implicit irc: IRC) = {
    val connect = new Socket(defaultHost, defaultPort)

    (
      new BufferedReader(new InputStreamReader(connect.getInputStream)),
      new BufferedWriter(new OutputStreamWriter(connect.getOutputStream))
    )
  }

  private def sendAuth(out: BufferedWriter)(implicit irc: IRC): Unit = {
    Seq("NICK " + botName,
        "USER " + botName + " 8 * " + botDesc,
        "JOIN " + irc.name)
        .foreach(s => {
          out.write(s) ; out.newLine() ; out.flush()
        })
  }

  def main(args: Array[String]) {
    get(IRC("#archlinux"))
  }
}
