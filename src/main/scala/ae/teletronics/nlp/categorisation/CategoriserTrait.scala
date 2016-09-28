package ae.teletronics.nlp.categorisation

/**
  * Created by trym on 26-09-2016.
  */
trait CategoriserTrait {

  def categorise(sentence: String): List[CategoryMatch]

}
