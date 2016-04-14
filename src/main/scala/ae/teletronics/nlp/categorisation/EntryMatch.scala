package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  */
case class EntryMatch(label: String, matcherName: String, exactEntry: String, matches: java.util.List[Match]) {

  def getLabel: String = label
  def getMatcherName: String = matcherName
  def getExactEntry: String = exactEntry
  def getMatches: java.util.List[Match] = matches
}
