package ae.teletronics.nlp.categorisation.storage

import ae.teletronics.nlp.categorisation.Category

/**
  * Created by trym on 26-09-2016.
  */
class Topic(val name: String, val categories: List[Category] = List[Category]()) extends Serializable
