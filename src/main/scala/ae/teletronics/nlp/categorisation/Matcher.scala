package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  */
trait Matcher {

  def doMatch(sentence: String, categoryEntry: String): List[Match]

  def name: String = this.getClass.getSimpleName
}
