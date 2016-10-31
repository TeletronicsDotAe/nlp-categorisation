package ae.teletronics.nlp.categorisation.storage

import ae.teletronics.nlp.categorisation.Category
import org.apache.commons.lang3.SerializationUtils
import org.mapdb.{DBMaker, HTreeMap, Serializer}
import java.util.UUID
import scala.collection.JavaConversions._


/**
  * Created by trym on 26-09-2016.
  */
class TopicStore(dbFileName: String) {

  def create(name: String): Category = create(name, List())

  def create(name: String, entries: List[String]): Category = create(new Category(name, UUID.randomUUID(), entries))

  def create(t: Category): Category = deserialize(categories(map =>
    if (map.contains(t.id))
      throw new Exception("Cannot create category, id already exists: " + t.id)
    else {
      val value = serialize(t)
      map.put(t.id, value)
      value // put doesnt return the value, so have to return it explicitly
    }))

  def delete(id: UUID): Category = deserialize(categories(_.remove(id)))

  def get(id: UUID): Category = deserialize(categories(_.get(id)))

  def update(topic: Category): Category = deserialize(categories(_.replace(topic.id, serialize(topic))))

  def list(): List[Category] = {
    import scala.collection.JavaConversions._
    categories(_.getValues().toList)
      .map(deserialize)
  }

  private def categories[T](action: HTreeMap[UUID, Array[Byte]] => T): T = {
  val db = DBMaker.fileDB(dbFileName).make
  val topics: HTreeMap[UUID, Array[Byte]] = db.hashMap("topics")
    .keySerializer(Serializer.UUID)
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
