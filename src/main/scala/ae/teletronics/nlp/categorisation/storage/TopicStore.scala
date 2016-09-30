package ae.teletronics.nlp.categorisation.storage

import org.apache.commons.lang3.SerializationUtils
import org.mapdb.{DBMaker, HTreeMap, Serializer}


/**
  * Created by trym on 26-09-2016.
  */
class TopicStore(dbFileName: String) {

  def create(name: String): Topic = create(new Topic(name))

  def create(t: Topic): Topic = deserialize(topics(_.putIfAbsent(t.name, serialize(t))))

  def delete(name: String): Topic = deserialize(topics(_.remove(name)))

  def get(name: String): Topic = deserialize(topics(_.get(name)))

  def update(topic: Topic): Topic = deserialize(topics(_.replace(topic.name, serialize(topic))))

  def list(): List[Topic] = {
    import scala.collection.JavaConversions._
    topics(_.getValues().toList)
      .map(deserialize)
  }


  private def topics[T](action: HTreeMap[String, Array[Byte]] => T): T = {
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

  private def serialize(t: Topic): Array[Byte] = if (t != null) SerializationUtils.serialize(t) else null
  private def deserialize(bytes: Array[Byte]): Topic = if (bytes != null) SerializationUtils.deserialize(bytes) else null

}
