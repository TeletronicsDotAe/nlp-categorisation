package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 04-04-2016.
  */
case class CategoryMatch(categoryName: String, entryMatches: java.util.List[EntryMatch]) {

  def getCategoryName: String = categoryName

  def getEntryMatches: java.util.List[EntryMatch] = entryMatches
}
