package ae.teletronics.nlp.categorisation

import com.fasterxml.jackson.annotation.JsonProperty

/**
  * Created by Boris on 04-04-2016.
  */
case class CategoryMatch(@JsonProperty("category_name") val categoryName: String, @JsonProperty("entry_matches") val entryMatches: java.util.List[EntryMatch]) {

  def getCategoryName: String = categoryName

  def getEntryMatches: java.util.List[EntryMatch] = entryMatches
}
