package ae.teletronics.nlp.categorisation.storage

import ae.teletronics.nlp.categorisation.Category
import org.apache.commons.lang3.SerializationUtils
import org.mapdb.{DBMaker, HTreeMap, Serializer}


/**
  * Created by trym on 26-09-2016.
  */
class TopicStore(dbFileName: String) {

  def create(name: String): Category = create(new Category(name))

  def create(t: Category): Category = deserialize(categories(_.putIfAbsent(t.name, serialize(t))))

  def delete(name: String): Category = deserialize(categories(_.remove(name)))

  def get(name: String): Category = deserialize(categories(_.get(name)))

  def update(topic: Category): Category = deserialize(categories(_.replace(topic.name, serialize(topic))))

  def list(): List[Category] = {
    import scala.collection.JavaConversions._
    categories(_.getValues().toList)
      .map(deserialize)
  }


  private def categories[T](action: HTreeMap[String, Array[Byte]] => T): T = {
  val db = DBMaker.fileDB(dbFileName).make
  val topics: HTreeMap[String, Array[Byte]] = db.hashMap("topics")
    .keySerializer(Serializer.STRING)
    .valueSerializer(Serializer.BYTE_ARRAY)
    .createOrOpen()

    try {
      action(topics)
    } finally {
      db.close()
    }
  }

  private def serialize(t: Category): Array[Byte] = if (t != null) SerializationUtils.serialize(t) else null
  private def deserialize(bytes: Array[Byte]): Category = if (bytes != null) SerializationUtils.deserialize(bytes) else null

}
