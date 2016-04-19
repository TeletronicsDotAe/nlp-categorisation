package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  */

object EntryParser {

  val regexMatcher = new RegexMatcher
  val fuzzyMatcher = new FuzzyMatcher(2, 0.15)
  val strictMatcher = new StrictMatcher

  val signifierParser = "^([~/]?)(.*)$".r

  def parse(entry: String): EntryParseResult ={
    val matchResult = signifierParser.findFirstMatchIn(entry).get // signifierParser will always succeed
    val signifier = matchResult.group(1)
    val entryWithLabel = matchResult.group(2)

    val exactEntryAndLabel = if (entryWithLabel.contains("/")) {
      val lastSlash = entryWithLabel.lastIndexOf("/")
      val exactEntryPart = entryWithLabel.substring(0, lastSlash)
      val labelPart = entryWithLabel.substring(lastSlash + 1)
      (exactEntryPart, labelPart)
    } else (entryWithLabel, entryWithLabel)

    val exactEntry = exactEntryAndLabel._1
    val label = exactEntryAndLabel._2
    val matcher = signifier match {
      case "/" => regexMatcher
      case "~" => fuzzyMatcher
      case _   => strictMatcher
    }

    EntryParseResult(matcher, label, exactEntry)
  }
}
