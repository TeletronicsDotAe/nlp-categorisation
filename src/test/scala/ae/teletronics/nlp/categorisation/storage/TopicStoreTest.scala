package ae.teletronics.nlp.categorisation.storage

import java.io.File
import java.nio.file.{Files, Paths}
import java.util
import java.util.UUID

import ae.teletronics.nlp.categorisation.Category
import org.junit.Assert._
import org.junit.{After, Before, Test}

/**
  * Created by trym on 26-09-2016.
  */
class TopicStoreTest {

  private val storageFile = "target/storage-test.db"
  private val key = new UUID(1L, 1L)
  private val name = "topic"
  private val emptyEntries = util.Arrays.asList[String]()
  private val newCategory = new Category(name, UUID.randomUUID(), emptyEntries)

  @Before
  def beforeTest(): Unit = {
    topicStore().clearAll()
  }

  @After
  def afterTest(): Unit = {
    topicStore().clearAll()
  }

  @Test(expected = classOf[Exception])
  def testCreate(): Unit = {
    val underTest = topicStore()
    assertNotNull(underTest.create(newCategory))
    // throw exception if we try to create another category with the same id
    underTest.create(newCategory)
  }

  @Test
  def testGet(): Unit = {
    val underTest = topicStore()
    // start state
    underTest.create(newCategory)
    // action
    assertNotNull(underTest.get(newCategory.id))
    assertEquals(name, underTest.get(newCategory.id).name)
    assertEquals(0, underTest.get(newCategory.id).entries.size())
  }

  @Test
  def testList(): Unit = {
    val underTest = topicStore()
    // start state
    underTest.create(name)
    // action
    assertNotNull(underTest.list())
    assertEquals(1, underTest.list().size)
  }

  @Test
  def testUpdate(): Unit = {
    val underTest = topicStore()
    // start state
    underTest.create(newCategory)
    assertNotNull(underTest.get(newCategory.id))
    val t = new Category(newCategory.name, newCategory.id, util.Arrays.asList("cat1"))
    // action
    assertNotNull(underTest.update(t))
  }

  @Test
  def testDelete(): Unit = {
    val underTest = topicStore()
    // start state
    underTest.create(newCategory)
    // action
    assertNotNull(underTest.delete(newCategory.id))
    // verify
    assertNotNull(underTest.list())
    assertEquals(0, underTest.list().size)
  }

  def topicStore(): TopicStore = {
    val storage = storageFile
    new File(storage).getParentFile().mkdirs() // ensure path exists
    new TopicStore(storage)
  }
}
