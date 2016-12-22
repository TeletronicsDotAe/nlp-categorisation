package ae.teletronics.nlp.categorisation.storage

import ae.teletronics.nlp.categorisation.Category
import org.apache.commons.lang3.SerializationUtils
import org.mapdb.{DB, DBMaker, HTreeMap, Serializer}
import java.util.UUID
import java.nio.file.{Files, Paths}

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

  // useful for testing purposes
  def clearAll(): Unit = {
    categories(_.clear())
  }

  def list(): List[Category] = {
  import scala.collection.JavaConversions._
  categories(_.getValues().toList)
    .map(deserialize)
  }

  def categories[T](action: HTreeMap[UUID, Array[Byte]] => T): T = {
    TopicStore.commitOnTopicsInDb(dbFileName, action)
  }

  private def serialize(t: Category): Array[Byte] = if (t != null) SerializationUtils.serialize(t) else null
  private def deserialize(bytes: Array[Byte]): Category = if (bytes != null) SerializationUtils.deserialize(bytes) else null
}

object TopicStore {

  private var fileDbsMap: Map[String, DB] = Map[String, DB]()

  def commitOnTopicsInDb[T](dbFileName: String, action: HTreeMap[UUID, Array[Byte]] => T): T = {
    this.synchronized {
      val db = dbForFile(dbFileName)
      val topics: HTreeMap[UUID, Array[Byte]] = db.hashMap("topics")
        .keySerializer(Serializer.UUID)
        .valueSerializer(Serializer.BYTE_ARRAY)
        .createOrOpen()

      val result = action(topics)
      db.commit()
      result
    }
  }

  private def dbForFile(file: String): DB = {
    this.synchronized {
      val mapContains = fileDbsMap.containsKey(file)
      val fileExists = Files.exists(Paths.get(file))
      if (mapContains && fileExists) {
        // all is good
        return fileDbsMap(file)
      } else if (!mapContains) {
        // When we start the service. The file may exist from a previous run.
        val db = DBMaker.fileDB(file)
          .make()
        this.fileDbsMap = this.fileDbsMap + (file -> db)
        return db
      } else {
        // we have the DB object in memory, but the backing file is missing. This is an error state
        throw new Exception("Missing MapDb file: " + file)
      }
    }
  }
}
