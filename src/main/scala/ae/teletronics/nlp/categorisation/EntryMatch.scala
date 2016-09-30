package ae.teletronics.nlp.categorisation

import com.fasterxml.jackson.annotation.JsonProperty

/**
  * Created by Boris on 2016-04-13.
  */
case class EntryMatch(@JsonProperty("label") val label: String, @JsonProperty("matcher_name") val matcherName: String,
                 @JsonProperty("exact_entry") val exactEntry: String, @JsonProperty("matches") val matches: java.util.List[Match]) {

  def getLabel: String = label
  def getMatcherName: String = matcherName
  def getExactEntry: String = exactEntry
  def getMatches: java.util.List[Match] = matches
}
