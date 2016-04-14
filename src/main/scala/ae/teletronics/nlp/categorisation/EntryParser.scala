package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  */

import scala.util

object EntryParser extends {

  val regexMatcher = new RegexMatcher
  val fuzzyMatcher = new FuzzyMatcher(1, 0.1)
  val strictMatcher = new FuzzyMatcher(0, 0)

  val signifierPattern = "([~/]?)"
  val exactMatchPattern = "(.+)"
  val optionalLabelPattern = "(/([^/]*))?"
  val entryPattern = "^" + signifierPattern + exactMatchPattern + optionalLabelPattern + "$"
  val entryParser = entryPattern.r("signifier", "exactMatch", "wholeLabel", "label")

  def parse(entry: String): EntryParseResult ={
    val matchResult = entryParser.findFirstMatchIn(entry)
    val optionParseResult = matchResult.map(m => {
      val signifier = m.group("signifier")
      val exactMatch = m.group("exactMatch").trim
      val optionalLabel = m.group("label").trim
      val label = if (optionalLabel.isEmpty) exactMatch else optionalLabel

      val matcher = signifier match {
        case "/" => regexMatcher
        case "~" => fuzzyMatcher
        case _   => strictMatcher
      }

      EntryParseResult(matcher, exactMatch, label)
    })

    optionParseResult.getOrElse(EntryParseResult(strictMatcher, entry, entry))
  }
}
